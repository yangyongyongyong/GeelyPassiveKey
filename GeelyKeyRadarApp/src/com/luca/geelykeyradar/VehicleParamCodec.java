package com.luca.geelykeyradar;

/** 车机只读参数回执的纯Java解码与展示工具，不包含任何写入指令。 */
final class VehicleParamCodec {
    static final String FEAT_CONFIG = "feat_dk_configuration";
    static final String FEAT_CALIBRATION = "feat_calibration";
    static final String FEAT_FAST_CTRL = "feat_dk_fast_ctrl";

    static final int ACT_PE15_SWITCH = 5392;
    static final int ACT_PE15_QUERY = 2;
    static final int ACT_FAST_CTRL = 272;
    static final int ACT_CTRL_QUERY = 0;
    static final int ACT_CALIBRATION_REQUEST = 305;

    /** 解析后的 DK1.5 原生 PE 开关回执。 */
    static final class PeSwitchResult {
        final int request;
        final int state;
        final int resultCode;
        final boolean success;
        final String rawHex;

        /** 保存请求类型、车机状态、结果码和原始回执，供界面与日志复用。 */
        PeSwitchResult(int request, int state, int resultCode, String rawHex) {
            this.request = request;
            this.state = state;
            this.resultCode = resultCode;
            this.success = resultCode == 0 && (state == 0 || state == 1);
            this.rawHex = rawHex == null ? "" : rawHex;
        }

        /** 返回不暴露原始VIN或钥匙字段的短状态文本。 */
        String display() {
            String stateText = state == 1 ? "开启(1)" : state == 0 ? "关闭(0)" : "未知(" + state + ")";
            return stateText + " · result=" + resultCode + (success ? " · 成功" : " · 未确认");
        }
    }

    /** 解析 0x0501 回执；长度不足或状态异常时返回null，不猜测车机值。 */
    static PeSwitchResult decodePeSwitch(byte[] payload, int fallbackResultCode) {
        if (payload == null || payload.length < 2) return null;
        int request = payload[0] & 0xFF;
        int state = payload[1] & 0xFF;
        int resultCode = payload.length >= 4
                ? ((payload[2] & 0xFF) << 8) | (payload[3] & 0xFF) : fallbackResultCode;
        return new PeSwitchResult(request, state, resultCode, compactHex(payload, 64));
    }

    /** 生成有长度上限的十六进制摘要，避免异常大回执污染界面和日志。 */
    static String compactHex(byte[] payload, int maxBytes) {
        if (payload == null || payload.length == 0) return "empty (0B)";
        int count = Math.max(1, Math.min(payload.length, maxBytes));
        String hex = VehicleBeacon.bytesToHex(payload, 0, count);
        return payload.length > count ? hex + "... (" + payload.length + "B)" : hex + " (" + payload.length + "B)";
    }

    /** 只读授权门：PE查询必须value=2，车况查询必须value=0，标定只允许305读取请求。 */
    static boolean isReadOnlyRequest(String feature, int action, int value) {
        if (FEAT_CONFIG.equals(feature) && action == ACT_PE15_SWITCH) return value == ACT_PE15_QUERY;
        if (FEAT_FAST_CTRL.equals(feature) && action == ACT_FAST_CTRL) return value == ACT_CTRL_QUERY;
        return FEAT_CALIBRATION.equals(feature) && action == ACT_CALIBRATION_REQUEST && value == 0;
    }

    /** 禁止实例化纯静态解码工具。 */
    private VehicleParamCodec() {
    }
}
