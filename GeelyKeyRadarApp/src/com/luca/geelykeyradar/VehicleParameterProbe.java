package com.luca.geelykeyradar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 车机参数只读探针。
 *
 * 仅调用官方 DigitalKeyBgService 的查询入口：0x0501(PE状态)、0x0121(车况)、
 * 0x0131(标定读取请求) 并监听 0x0132 回执。绝不调用 0x0195/0x0170/PE开关写入。
 */
final class VehicleParameterProbe {
    static final String PREFS = "vehicle_param_readonly_prefs";

    // 跟随当前配置/已安装官方端（吉利汽车或银河）动态绑定只读探针。
    private static final String GEELY_DK_SERVICE = "com.geely.snc.digitalkey.base.service.DigitalKeyBgService";
    private static final String ACTION_DK_START = "com.zeekr.snc.service.action.DK_START";
    private static final String DESC_DK_SERVICE = "com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService";
    private static final String DESC_DATA_LISTENER = "com.geely.snc.digitalkey.base.dk.business.IDkBleDataListener";
    private static final String DESC_CONNECTION_LISTENER = "com.geely.snc.digitalkey.base.dk.IDigitalKeyConnectionListener";

    private static final String FEAT_CONFIG = VehicleParamCodec.FEAT_CONFIG;
    private static final String FEAT_CALIBRATION = VehicleParamCodec.FEAT_CALIBRATION;
    private static final String FEAT_FAST_CTRL = VehicleParamCodec.FEAT_FAST_CTRL;
    private static final String FEAT_VEHICLE = "feat_vehicle";

    private static final int TRANS_GET_VERSION = 1;
    private static final int TRANS_SET_CONNECTION_LISTENER = 5;
    private static final int TRANS_GET_BLE_STATUS = 13;
    private static final int TRANS_DO_FEATURE_ACTION_DATA = 19;
    private static final int TRANS_ADD_FEATURE_LISTENER = 21;

    private static final int ACT_PE15_SWITCH = VehicleParamCodec.ACT_PE15_SWITCH;
    private static final int ACT_PE15_QUERY = VehicleParamCodec.ACT_PE15_QUERY;
    private static final int ACT_FAST_CTRL = VehicleParamCodec.ACT_FAST_CTRL;
    private static final int ACT_CTRL_QUERY = VehicleParamCodec.ACT_CTRL_QUERY;
    private static final int ACT_CALIBRATION_REQUEST = VehicleParamCodec.ACT_CALIBRATION_REQUEST;
    private static final int LISTENER_CALIBRATION = 306;
    private static final int LISTENER_VEHICLE_STATUS = 289;
    private static final int LISTENER_APPROACH_LOCK = 345;

    private static final String KEY_AIDL_STATUS = "aidl_status";
    private static final String KEY_AIDL_VERSION = "aidl_version";
    private static final String KEY_BLE_STATUS = "ble_status";
    private static final String KEY_PLATFORM = "vehicle_platform";
    private static final String KEY_PE_STATUS = "pe_status";
    private static final String KEY_PE_STATE = "pe_state";
    private static final String KEY_PE_REQUEST = "pe_request";
    private static final String KEY_PE_RESULT = "pe_result";
    private static final String KEY_PE_RAW = "pe_raw_hex";
    private static final String KEY_CAL_STATUS = "cal_status";
    private static final String KEY_CAL_RAW = "cal_raw_hex";
    private static final String KEY_VEHICLE_STATUS = "vehicle_status";
    private static final String KEY_VEHICLE_RAW = "vehicle_raw_hex";
    private static final String KEY_APPROACH_EVENT = "approach_lock_event";
    private static final String KEY_QUERY_AT = "query_at";
    private static final String KEY_LAST_RESPONSE_AT = "last_response_at";
    private static final String KEY_LAST_ERROR = "last_error";

    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final IBinder dataListenerBinder = new DataListenerBinder();
    private final IBinder connectionListenerBinder = new ConnectionListenerBinder();
    private IBinder remoteBinder;
    private boolean binding;
    private int queryGeneration;
    private long lastQueryStartedAtMs;

    /** 保存应用上下文，不持有Activity，避免异步回执造成页面泄漏。 */
    VehicleParameterProbe(Context context) {
        this.context = context.getApplicationContext();
    }

    /** App打开时启动绑定；已绑定则按节流自动重新读取一次。 */
    void start() {
        if (remoteBinder != null && remoteBinder.pingBinder()) {
            if (SystemClock.elapsedRealtime() - lastQueryStartedAtMs >= 3000L) queryReadOnly();
            return;
        }
        if (binding) return;
        String unavailable = officialServiceUnavailableReason();
        if (!unavailable.isEmpty()) {
            setValue(KEY_AIDL_STATUS, unavailable);
            setValue(KEY_LAST_ERROR, "");
            DiagnosticLog.event("VehicleParam", "AIDL_UNAVAILABLE", "reason=" + unavailable + " write_actions=none");
            return;
        }
        setValue(KEY_AIDL_STATUS, "绑定中");
        String targetPkg = OfficialTarget.resolveConfiguredPackage(context);
        try {
            Intent intent = new Intent(ACTION_DK_START);
            intent.setComponent(new ComponentName(targetPkg, GEELY_DK_SERVICE));
            binding = context.bindService(intent, connection,
                    Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
            if (!binding) {
                binding = context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
            }
            if (!binding) setValue(KEY_AIDL_STATUS, "绑定请求被系统拒绝");
        } catch (Throwable error) {
            binding = false;
            setValue(KEY_AIDL_STATUS, "绑定失败");
            setValue(KEY_LAST_ERROR, DiagnosticText.errorSummary(error));
            DiagnosticLog.bridgeError("VehicleParam", "AIDL_BIND_ERROR", error);
        }
    }

    /** 当前银河Manifest未导出只读服务时立即返回原因，避免每次打开App都抛SecurityException。 */
    private String officialServiceUnavailableReason() {
        String targetPkg = OfficialTarget.resolveConfiguredPackage(context);
        String appLabel = OfficialBrandPolicy.PKG_GALAXY.equals(targetPkg) ? "银河" : "吉利汽车";
        try {
            ServiceInfo info = context.getPackageManager().getServiceInfo(
                    new ComponentName(targetPkg, GEELY_DK_SERVICE), PackageManager.MATCH_DISABLED_COMPONENTS);
            if (info != null && !info.exported) {
                String version = "未知";
                try {
                    version = context.getPackageManager().getPackageInfo(targetPkg, 0).versionName;
                } catch (Throwable ignored) {
                }
                return "不可用：" + appLabel + version + "未导出DigitalKeyBgService（只读AIDL被系统限制）";
            }
            return "";
        } catch (PackageManager.NameNotFoundException missing) {
            return "不可用：未安装官方 App（吉利汽车 / 银河）";
        } catch (Throwable error) {
            return "不可用：无法检查" + appLabel + " AIDL导出状态";
        }
    }

    /** Activity销毁时解除独立绑定，不影响前台无感钥匙服务本身。 */
    void stop() {
        if (remoteBinder != null || binding) {
            try {
                context.unbindService(connection);
            } catch (Throwable ignored) {
            }
        }
        remoteBinder = null;
        binding = false;
    }

    /** 手动或自动发起一次严格只读查询；所有请求共用同一个generation用于超时说明。 */
    void queryReadOnly() {
        if (remoteBinder == null || !remoteBinder.pingBinder()) {
            start();
            return;
        }
        final int generation = ++queryGeneration;
        lastQueryStartedAtMs = SystemClock.elapsedRealtime();
        String now = nowText();
        SharedPreferences.Editor editor = prefs().edit()
                .putString(KEY_QUERY_AT, now)
                .putString(KEY_PE_STATUS, "等待回执")
                .putString(KEY_CAL_STATUS, "等待回执")
                .putString(KEY_VEHICLE_STATUS, "等待回执")
                .putString(KEY_LAST_ERROR, "");
        editor.apply();

        boolean peSent = sendFeatureAction(FEAT_CONFIG, ACT_PE15_SWITCH, ACT_PE15_QUERY);
        boolean statusSent = sendFeatureAction(FEAT_FAST_CTRL, ACT_FAST_CTRL, ACT_CTRL_QUERY);
        // 305仅请求读取；L6/DK1.5当前实现可能不返回，界面会明确显示“无回执”而不是猜值。
        boolean calibrationSent = sendFeatureAction(FEAT_CALIBRATION, ACT_CALIBRATION_REQUEST, 0);
        DiagnosticLog.event("VehicleParam", "READ_REQUEST", "generation=" + generation
                + " pe_query=" + peSent + " vehicle_status=" + statusSent
                + " calibration_query=" + calibrationSent + " write_actions=none");
        if (!peSent && !statusSent && !calibrationSent) {
            setValue(KEY_AIDL_STATUS, "查询发送失败");
        }
        mainHandler.postDelayed(() -> finishTimeout(generation), 5000L);
    }

    /** 官方回调只读保存到私有偏好和诊断日志，原始帧最长保留64字节摘要。 */
    private void handleFeatureResult(int errorCode, int platform, int action, byte[] payload) {
        SharedPreferences.Editor editor = prefs().edit()
                .putInt(KEY_PLATFORM, platform)
                .putString(KEY_LAST_RESPONSE_AT, nowText())
                .putString(KEY_LAST_ERROR, "");
        if (action == ACT_PE15_SWITCH) {
            VehicleParamCodec.PeSwitchResult result = VehicleParamCodec.decodePeSwitch(payload, errorCode);
            if (result == null) {
                editor.putString(KEY_PE_STATUS, "回执过短/无法解析 · error=" + errorCode);
            } else {
                editor.putInt(KEY_PE_STATE, result.state)
                        .putInt(KEY_PE_REQUEST, result.request)
                        .putInt(KEY_PE_RESULT, result.resultCode)
                        .putString(KEY_PE_RAW, result.rawHex)
                        .putString(KEY_PE_STATUS, result.display());
                DiagnosticLog.event("VehicleParam", "PE_SWITCH_RESULT", "request=" + result.request
                        + " state=" + result.state + " result=" + result.resultCode
                        + " platform=" + platform + " raw=" + result.rawHex + " write_actions=none");
            }
        } else if (action == LISTENER_CALIBRATION) {
            String raw = VehicleParamCodec.compactHex(payload, 64);
            editor.putString(KEY_CAL_STATUS, "已收到0x0132原始回执 · " + raw)
                    .putString(KEY_CAL_RAW, raw);
            DiagnosticLog.event("VehicleParam", "CALIBRATION_RESULT", "action=" + action
                    + " error=" + errorCode + " platform=" + platform + " raw=" + raw + " write_actions=none");
        } else if (action == LISTENER_VEHICLE_STATUS) {
            String raw = VehicleParamCodec.compactHex(payload, 64);
            editor.putString(KEY_VEHICLE_STATUS, "已收到0x0121车况帧 · " + raw)
                    .putString(KEY_VEHICLE_RAW, raw);
            DiagnosticLog.event("VehicleParam", "VEHICLE_STATUS_RESULT", "error=" + errorCode
                    + " platform=" + platform + " raw=" + raw + " write_actions=none");
        } else if (action == LISTENER_APPROACH_LOCK) {
            String raw = VehicleParamCodec.compactHex(payload, 32);
            editor.putString(KEY_APPROACH_EVENT, nowText() + " · raw=" + raw);
            DiagnosticLog.event("VehicleParam", "APPROACH_LOCK_EVENT", "error=" + errorCode
                    + " platform=" + platform + " raw=" + raw + " write_actions=none");
        }
        editor.apply();
    }

    /** 超时只更新“未收到”状态，不重试、不写车机，避免把查询变成隐藏写入或连接风暴。 */
    private void finishTimeout(int generation) {
        if (generation != queryGeneration) return;
        SharedPreferences preferences = prefs();
        SharedPreferences.Editor editor = preferences.edit();
        boolean changed = false;
        if ("等待回执".equals(preferences.getString(KEY_PE_STATUS, ""))) {
            editor.putString(KEY_PE_STATUS, "未收到0x0501回执");
            changed = true;
        }
        if ("等待回执".equals(preferences.getString(KEY_CAL_STATUS, ""))) {
            editor.putString(KEY_CAL_STATUS, "未收到0x0132回执（L6/DK1.5可能不支持0x0131读取）");
            changed = true;
        }
        if ("等待回执".equals(preferences.getString(KEY_VEHICLE_STATUS, ""))) {
            editor.putString(KEY_VEHICLE_STATUS, "未收到0x0121回执");
            changed = true;
        }
        editor.apply();
        if (changed) DiagnosticLog.event("VehicleParam", "READ_TIMEOUT", "generation=" + generation + " write_actions=none");
    }

    /** 注册仅用于读取的参数与状态监听；重复注册在官方服务侧应保持幂等。 */
    private void registerReadOnlyListeners() {
        registerConnectionListener();
        addFeatureListener(FEAT_CONFIG, ACT_PE15_SWITCH);
        addFeatureListener(FEAT_CONFIG, LISTENER_CALIBRATION);
        addFeatureListener(FEAT_VEHICLE, LISTENER_VEHICLE_STATUS);
        addFeatureListener(FEAT_CONFIG, LISTENER_APPROACH_LOCK);
    }

    /** 注册官方连接状态Binder，但本探针只记录，不触发车辆连接或车控。 */
    private void registerConnectionListener() {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESC_DK_SERVICE);
            data.writeString("GeelyKeyRadarReadOnly");
            data.writeStrongBinder(connectionListenerBinder);
            remoteBinder.transact(TRANS_SET_CONNECTION_LISTENER, data, reply, 0);
            reply.readException();
        } catch (Throwable error) {
            setValue(KEY_LAST_ERROR, "连接监听注册失败: " + DiagnosticText.errorSummary(error));
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    /** 通过官方AIDL注册回调；feature与action均使用只读参数链路。 */
    private void addFeatureListener(String feature, int action) {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESC_DK_SERVICE);
            data.writeString(feature);
            data.writeInt(action);
            data.writeStrongBinder(dataListenerBinder);
            remoteBinder.transact(TRANS_ADD_FEATURE_LISTENER, data, reply, 0);
            reply.readException();
        } catch (Throwable error) {
            setValue(KEY_LAST_ERROR, "参数监听注册失败: " + DiagnosticText.errorSummary(error));
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    /** 发送整型Feature动作；本类只允许调用只读白名单中的action。 */
    private boolean sendFeatureAction(String feature, int action, int value) {
        if (remoteBinder == null || !VehicleParamCodec.isReadOnlyRequest(feature, action, value)) return false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESC_DK_SERVICE);
            data.writeString(feature);
            data.writeInt(action);
            data.writeInt(value);
            data.writeStrongBinder(dataListenerBinder);
            remoteBinder.transact(TRANS_DO_FEATURE_ACTION_DATA, data, reply, 0);
            reply.readException();
            return true;
        } catch (Throwable error) {
            setValue(KEY_LAST_ERROR, "只读查询发送失败: " + DiagnosticText.errorSummary(error));
            DiagnosticLog.bridgeError("VehicleParam", "READ_SEND_ERROR", error);
            return false;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    /** 返回AIDL版本；失败不影响后续查询，界面显示未知。 */
    private int readAidlVersion() {
        if (remoteBinder == null) return -1;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESC_DK_SERVICE);
            remoteBinder.transact(TRANS_GET_VERSION, data, reply, 0);
            reply.readException();
            return reply.readInt();
        } catch (Throwable ignored) {
            return -1;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    /** 返回官方BLE状态码；仅用于诊断，不据此放行任何自动车控。 */
    private int readBleStatus() {
        if (remoteBinder == null) return -1;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESC_DK_SERVICE);
            remoteBinder.transact(TRANS_GET_BLE_STATUS, data, reply, 0);
            reply.readException();
            return reply.readInt();
        } catch (Throwable ignored) {
            return -1;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    private final ServiceConnection connection = new ServiceConnection() {
        /** 绑定成功后登记只读监听，并延迟350ms发送查询，避开服务初始化窗口。 */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteBinder = service;
            binding = false;
            int version = readAidlVersion();
            int bleStatus = readBleStatus();
            prefs().edit().putString(KEY_AIDL_STATUS, "已连接")
                    .putInt(KEY_AIDL_VERSION, version)
                    .putInt(KEY_BLE_STATUS, bleStatus).apply();
            DiagnosticLog.event("VehicleParam", "AIDL_CONNECTED", "component=" + name.getClassName()
                    + " version=" + version + " ble_status=" + bleStatus + " mode=read_only");
            registerReadOnlyListeners();
            mainHandler.postDelayed(VehicleParameterProbe.this::queryReadOnly, 350L);
        }

        /** 绑定断开时只更新诊断状态，不清除上次已读取的参数。 */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteBinder = null;
            binding = false;
            setValue(KEY_AIDL_STATUS, "已断开");
            DiagnosticLog.event("VehicleParam", "AIDL_DISCONNECTED", "component=" + name.getClassName());
        }

        /** 绑定失效与普通断开始终按同一只读状态处理。 */
        @Override
        public void onBindingDied(ComponentName name) {
            remoteBinder = null;
            binding = false;
            setValue(KEY_AIDL_STATUS, "绑定失效");
            DiagnosticLog.event("VehicleParam", "AIDL_BINDING_DIED", "component=" + name.getClassName());
        }
    };

    /** 官方IDkBleDataListener的本地Binder，只读取回执参数并投递到主线程。 */
    private final class DataListenerBinder extends Binder {
        /** 处理接口描述查询和onResult回调，不向官方服务回写任何值。 */
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1598968902) {
                reply.writeString(DESC_DATA_LISTENER);
                return true;
            }
            if (code == 1) {
                data.enforceInterface(DESC_DATA_LISTENER);
                int errorCode = data.readInt();
                int platform = data.readInt();
                int action = data.readInt();
                byte[] payload = data.createByteArray();
                reply.writeNoException();
                mainHandler.post(() -> handleFeatureResult(errorCode, platform, action, payload));
                return true;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }

    /** 官方连接回调的本地Binder，完整消费参数但不发起连接或车控。 */
    private final class ConnectionListenerBinder extends Binder {
        /** 识别接口描述并消费各版本连接回调，避免官方Binder等待超时。 */
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1598968902) {
                reply.writeString(DESC_CONNECTION_LISTENER);
                return true;
            }
            data.enforceInterface(DESC_CONNECTION_LISTENER);
            switch (code) {
                case 1:
                    data.readInt();
                    data.readString();
                    break;
                case 2:
                    data.readInt();
                    data.readString();
                    data.readInt();
                    break;
                case 3:
                    data.readString();
                    break;
                case 4:
                case 5:
                    break;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
            reply.writeNoException();
            return true;
        }
    }

    /** 返回持久化的只读参数面板，任何写入能力都不会出现在界面文案中。 */
    static String loadDisplay(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String aidl = preferences.getString(KEY_AIDL_STATUS, "尚未读取");
        int version = preferences.getInt(KEY_AIDL_VERSION, -1);
        int bleStatus = preferences.getInt(KEY_BLE_STATUS, -1);
        String pe = preferences.getString(KEY_PE_STATUS, "尚未读取");
        String peRaw = preferences.getString(KEY_PE_RAW, "--");
        String calibration = preferences.getString(KEY_CAL_STATUS, "尚未读取");
        String calibrationRaw = preferences.getString(KEY_CAL_RAW, "--");
        String vehicle = preferences.getString(KEY_VEHICLE_STATUS, "尚未读取");
        String lastQuery = preferences.getString(KEY_QUERY_AT, "--");
        String lastResponse = preferences.getString(KEY_LAST_RESPONSE_AT, "--");
        String error = preferences.getString(KEY_LAST_ERROR, "");
        return "AIDL：" + aidl + (version >= 0 ? " · v" + version : "")
                + " · BLE状态=" + bleStatus + "\n"
                + "原生PE总开关（0x0501）：" + pe + "\n"
                + "PE原始回执：" + peRaw + "\n"
                + "距离/标定读取（0x0131→0x0132）：" + calibration + "\n"
                + "标定原始回执：" + calibrationRaw + "\n"
                + "车况帧（0x0121）：" + vehicle + "\n"
                + "查询时间：" + lastQuery + " · 最后回执：" + lastResponse + "\n"
                + "写入能力：未启用（本页只读）"
                + (error.isEmpty() ? "" : "\n最近错误：" + error);
    }

    private SharedPreferences prefs() {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    /** 单字段持久化只用于绑定状态和错误，不覆盖其他参数回执。 */
    private void setValue(String key, String value) {
        prefs().edit().putString(key, value).apply();
    }

    private String nowText() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT).format(new Date());
    }
}
