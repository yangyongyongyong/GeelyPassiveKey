package com.geely.cockpit.services.controlservice.core.uspapi;

/* JADX INFO: compiled from: UspApiService.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000ò\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\r\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001c\u0010\u0013\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0016\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001e\u0010\u0018\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\b\u0010\u0017\u001a\u0004\u0018\u00010\u0004J\u001c\u0010\u0019\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010\u001a\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010\u001b\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J(\u0010\u001c\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u001eJ\u001c\u0010\u001f\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010 \u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010!\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010\"\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020#\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010$\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010%\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020&\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001e\u0010'\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020(\u0018\u00010\u00150\u00142\b\u0010)\u001a\u0004\u0018\u00010\u0004J\u001e\u0010*\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020+\u0018\u00010\u00150\u00142\b\u0010)\u001a\u0004\u0018\u00010\u0004J(\u0010,\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020-\u0018\u00010\u00150\u00142\b\u0010)\u001a\u0004\u0018\u00010\u00042\b\u0010.\u001a\u0004\u0018\u00010\u0004J\u001c\u0010/\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u000200\u0018\u00010\u00150\u00142\u0006\u00101\u001a\u000202J\u001e\u00103\u001a\u0012\u0012\u000e\u0012\f\u0012\u0006\u0012\u0004\u0018\u000104\u0018\u00010\u00150\u00142\u0006\u00105\u001a\u00020\u0004J\u001c\u00106\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u000207\u0018\u00010\u00150\u00142\u0006\u0010)\u001a\u00020\u0004J(\u00108\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u000209\u0018\u00010\u00150\u00142\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u001eJ\u001c\u0010:\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010;\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010<\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020=\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010>\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020?\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010@\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020A\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001e\u0010B\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020C\u0018\u00010\u00150\u00142\b\u0010\u0017\u001a\u0004\u0018\u00010\u0004J\u001e\u0010D\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020E\u0018\u00010\u00150\u00142\b\u0010\u0017\u001a\u0004\u0018\u00010\u0004J*\u0010F\u001a\u0012\u0012\u000e\u0012\f\u0012\u0006\u0012\u0004\u0018\u00010G\u0018\u00010\u00150\u00142\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u001eJ\u001c\u0010H\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020I\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010J\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020K\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J(\u0010L\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u001eJ\u001a\u0010M\u001a\u0016\u0012\u0012\u0012\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u0002000N\u0018\u00010\u00150\u0014J\u001c\u0010O\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020P\u0018\u00010\u00150\u00142\u0006\u0010)\u001a\u00020\u0004J&\u0010Q\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u000e\u0012\f\u0012\u0006\u0012\u0004\u0018\u00010R\u0018\u00010N\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010S\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020T\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010U\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020V\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010W\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010)\u001a\u00020\u0004J\u001c\u0010X\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020Y\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010Z\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020[\u0018\u00010\u00150\u00142\u0006\u0010)\u001a\u00020\u0004J\u001c\u0010\\\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020]\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010^\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020_\u0018\u00010\u00150\u00142\u0006\u0010)\u001a\u00020\u0004J\u001e\u0010`\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020a\u0018\u00010\u00150\u00142\b\u0010\u0017\u001a\u0004\u0018\u00010\u0004J\u001c\u0010b\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020c\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010d\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020P\u0018\u00010\u00150\u00142\u0006\u00101\u001a\u000202J\u0014\u0010e\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020P\u0018\u00010\u00150\u0014J\u001c\u0010f\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010g\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010h\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001e\u0010i\u001a\u0012\u0012\u000e\u0012\f\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010j\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020k\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010l\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020k\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010m\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J(\u0010n\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u001eJ\u001c\u0010o\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010p\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010q\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010r\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J(\u0010s\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u001eJ\u001c\u0010t\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J\u001c\u0010u\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0017\u001a\u00020\u0004J(\u0010v\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020P\u0018\u00010\u00150\u00142\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u001eJ(\u0010w\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020P\u0018\u00010\u00150\u00142\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u001eR\u001b\u0010\u0003\u001a\u00020\u00048@X\u0080\u0084\u0002¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u001b\u0010\t\u001a\u00020\u00048FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u000b\u0010\b\u001a\u0004\b\n\u0010\u0006R\u001b\u0010\f\u001a\u00020\u00048FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u000e\u0010\b\u001a\u0004\b\r\u0010\u0006R\u0011\u0010\u000f\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0006R\u0011\u0010\u0011\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0006¨\u0006x"}, d2 = {"Lcom/geely/cockpit/services/controlservice/core/uspapi/UspApiService;", "", "()V", "API_PATH_PREFIX", "", "getAPI_PATH_PREFIX$LibCarControlNew_release", "()Ljava/lang/String;", "API_PATH_PREFIX$delegate", "Lkotlin/Lazy;", "BASE_USP_URL", "getBASE_USP_URL", "BASE_USP_URL$delegate", "BASE_USP_URL_NEW", "getBASE_USP_URL_NEW", "BASE_USP_URL_NEW$delegate", "USP_GATEWAY_CODE", "getUSP_GATEWAY_CODE", "USP_GATEWAY_CODE_NEW", "getUSP_GATEWAY_CODE_NEW", "carModelJsonQuery", "Lio/reactivex/Observable;", "Lcom/geely/cockpit/services/controlservice/core/response/BaseUspResult;", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/CarModelJsonQueryResponseBean;", "jsonStr", "controlRefrigerator", "createCarShare", "deleteFileList", "deleteJournalLog", "deleteSentinelFile", "map", "", "editCarNickName", "electronicFenceCreate", "electronicFenceDelete", "electronicFenceQuery", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ElectronicFenceQueryResponseBean;", "electronicFenceUpdate", "getAbnormalVideoList", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/AbnormalVideoResult;", "getBatteryBasic", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/BatteryBasicResponse;", "vin", "getBatteryRecord", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/BatteryRecordResponse;", "getBatteryReport", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/BatteryReportResponse;", "queryDate", "getBeSharedDetail", "Lcom/geely/galaxy/carcontrol/core/functiontools/control/bean/BeSharedBean;", "recordId", "", "getCacheMessage", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/VehicleGetResultResponseVO;", "sessionId", "getCarShareList", "Lcom/geely/galaxy/carcontrol/core/functiontools/control/bean/CarShareList;", "getControlCapability", "Lcom/geely/cockpit/services/controlservice/bean/parser/UspCapabilityResponseBean;", "getControlClimate", "getControlNoEngine", "getControlVehicleSwitchStatus", "Lcom/geely/cockpit/services/controlservice/bean/PlatformVehicleSwitch;", "getJournalLogList", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/JourneyListBean;", "getJournalLogTrack", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/JourneyTrack;", "getParkPhoto", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ParkPhotoResponse;", "getParkPosition", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ParkPositionResponse;", "getPlatformVehicleStatus", "Lcom/geely/cockpit/services/controlservice/bean/PlatformVehicleStatus;", "getReservationChargeQuery", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/UspChargingResultBean;", "getReservationLostSoc", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ReservationGetLastSocResponseBean;", "getSentinelFileList", "getSharedUnhandledList", "", "getUnreadShareCount", "", "menuListQuery", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/MenuListQueryResponseBean;", "msgListQuery", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/MsgListQueryResponseBean;", "otaInstall", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/OtaInstallResponseBean;", "otaInstallCancel", "otaSetting", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/OtaSetBean;", "otaSettingQuery", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/OtaSettingQueryResponseBean;", "otaUpdateHistory", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/OtaUpdateHistoryResponseBean;", "otaVersionQuery", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/OtaVersionQueryResponseBean;", "queryRefrigerator", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/RefrigeratorResponse;", "queryReservationTravel", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/QueryReservationTravelResponseBean;", "readBeShared", "readShare", "reservationCharge", "reservationChargeConfig", "reservationChargeStart", "sendToCar", "setCarHeartBeat", "", "setCarOnOffLine", "setControlDoor", "setControlEngine", "setControlFindCar", "setControlTemperature", "setControlVehicleSwitch", "setControlWindow", "setDefaultCar", "setReservationTravel", "setVehicleLightShow", "unbindShare", "updateShareStatus", "LibCarControlNew_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final class UspApiService {
    public static final com.geely.cockpit.services.controlservice.core.uspapi.UspApiService INSTANCE = new com.geely.cockpit.services.controlservice.core.uspapi.UspApiService();

    /* JADX INFO: renamed from: API_PATH_PREFIX$delegate, reason: from kotlin metadata */
    private static final kotlin.Lazy API_PATH_PREFIX = kotlin.LazyKt.lazy(new kotlin.jvm.functions.Function0<java.lang.String>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$API_PATH_PREFIX$2
        public final java.lang.String invoke() {
            return com.geely.cockpit.services.controlservice.runtime.RuntimeUtilsKt.isGeelyApp() ? "api" : "vc/app";
        }
    });

    /* JADX INFO: renamed from: BASE_USP_URL$delegate, reason: from kotlin metadata */
    private static final kotlin.Lazy BASE_USP_URL = kotlin.LazyKt.lazy(new kotlin.jvm.functions.Function0<java.lang.String>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$BASE_USP_URL$2
        public final java.lang.String invoke() {
            return kotlin.text.StringsKt.trimEnd(com.geely.cockpit.services.controlservice.runtime.CurUspBaseUrl.INSTANCE.getBaseUrl$LibCarControlNew_release(), new char[]{'/'}) + "/";
        }
    });

    /* JADX INFO: renamed from: BASE_USP_URL_NEW$delegate, reason: from kotlin metadata */
    private static final kotlin.Lazy BASE_USP_URL_NEW = kotlin.LazyKt.lazy(new kotlin.jvm.functions.Function0<java.lang.String>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$BASE_USP_URL_NEW$2
        public final java.lang.String invoke() {
            return kotlin.text.StringsKt.trimEnd(com.geely.cockpit.services.controlservice.runtime.CurUspBaseUrl.INSTANCE.getBaseUrlNew$LibCarControlNew_release(), new char[]{'/'}) + "/";
        }
    });

    private UspApiService() {
    }

    public final java.lang.String getUSP_GATEWAY_CODE() {
        return com.geely.cockpit.services.controlservice.runtime.CurUspBaseUrl.INSTANCE.getUSP_GATEWAY_CODE$LibCarControlNew_release();
    }

    public final java.lang.String getUSP_GATEWAY_CODE_NEW() {
        return com.geely.cockpit.services.controlservice.runtime.CurUspBaseUrl.INSTANCE.getUSP_GATEWAY_CODE_NEW$LibCarControlNew_release();
    }

    public final java.lang.String getAPI_PATH_PREFIX$LibCarControlNew_release() {
        return (java.lang.String) API_PATH_PREFIX.getValue();
    }

    public final java.lang.String getBASE_USP_URL() {
        return (java.lang.String) BASE_USP_URL.getValue();
    }

    public final java.lang.String getBASE_USP_URL_NEW() {
        return (java.lang.String) BASE_USP_URL_NEW.getValue();
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getPlatformVehicleStatus$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus>> getPlatformVehicleStatus(java.util.Map<java.lang.String, ? extends java.lang.Object> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CONTROL_STATUS()).setContentType(com.geely.rxhttp.ContentType.Json).useGateWay(true).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJsonBody(map).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getPlatformVehicleStatus$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getControlCapability$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.bean.parser.UspCapabilityResponseBean>> getControlCapability(java.util.Map<java.lang.String, ? extends java.lang.Object> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CONTROL_CAPABILITY()).setContentType(com.geely.rxhttp.ContentType.Json).useGateWay(true).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJsonBody(map).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.bean.parser.UspCapabilityResponseBean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getControlCapability$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setDefaultCar$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> setDefaultCar(java.util.Map<java.lang.String, ? extends java.lang.Object> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CONTROL_DEFAULT()).setContentType(com.geely.rxhttp.ContentType.Json).useGateWay(true).setAppCode(getUSP_GATEWAY_CODE()).setJsonBody(map).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setDefaultCar$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setControlWindow$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> setControlWindow(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CONTROL_WINDOW()).setContentType(com.geely.rxhttp.ContentType.Json).useGateWay(true).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setControlWindow$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setControlDoor$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> setControlDoor(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CONTROL_DOOR()).setContentType(com.geely.rxhttp.ContentType.Json).useGateWay(true).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setControlDoor$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setControlFindCar$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> setControlFindCar(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CONTROL_SEARCH()).setContentType(com.geely.rxhttp.ContentType.Json).useGateWay(true).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setControlFindCar$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setControlEngine$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> setControlEngine(java.util.Map<java.lang.String, ? extends java.lang.Object> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CONTROL_ENGINE()).setContentType(com.geely.rxhttp.ContentType.Json).useGateWay(true).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJsonBody(map).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setControlEngine$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setControlVehicleSwitch$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> setControlVehicleSwitch(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_VEHICLE_CONTROL_SWITCH()).setContentType(com.geely.rxhttp.ContentType.Json).useGateWay(true).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setControlVehicleSwitch$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getControlVehicleSwitchStatus$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.bean.PlatformVehicleSwitch>> getControlVehicleSwitchStatus(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_VEHICLE_SWITCH_STATUS()).useGateWay(true).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.bean.PlatformVehicleSwitch>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getControlVehicleSwitchStatus$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getAbnormalVideoList$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.AbnormalVideoResult>> getAbnormalVideoList(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_ABNORMAL_VIDEO_LIST()).setContentType(com.geely.rxhttp.ContentType.Json).useGateWay(true).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.AbnormalVideoResult>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getAbnormalVideoList$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getControlClimate$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> getControlClimate(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CONTROL_CLIMATE_REC()).setContentType(com.geely.rxhttp.ContentType.Json).useGateWay(true).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getControlClimate$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$deleteFileList$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> deleteFileList(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_DELETE_ABNORMAL_VIDEO()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.useGateWay(true).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$deleteFileList$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getControlNoEngine$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> getControlNoEngine(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CONTROL_NO_ENGINE_RCC()).setContentType(com.geely.rxhttp.ContentType.Json).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.useGateWay(true).setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getControlNoEngine$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setVehicleLightShow$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> setVehicleLightShow(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_LIGHT_SHOW()).setContentType(com.geely.rxhttp.ContentType.Json).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.useGateWay(true).setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setVehicleLightShow$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getReservationChargeQuery$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.UspChargingResultBean>> getReservationChargeQuery(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CHARGE_QUERY()).setContentType(com.geely.rxhttp.ContentType.Json).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.useGateWay(true).setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.UspChargingResultBean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getReservationChargeQuery$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getReservationLostSoc$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ReservationGetLastSocResponseBean>> getReservationLostSoc(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CHARGE_GET_LAST_SOC()).setContentType(com.geely.rxhttp.ContentType.Json).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.useGateWay(true).setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ReservationGetLastSocResponseBean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getReservationLostSoc$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$reservationCharge$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> reservationCharge(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CHARGE_SET()).setContentType(com.geely.rxhttp.ContentType.Json).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.useGateWay(true).setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$reservationCharge$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setReservationTravel$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> setReservationTravel(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CHARGE_TRAVEL()).setContentType(com.geely.rxhttp.ContentType.Json).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.useGateWay(true).setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setReservationTravel$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$queryReservationTravel$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.QueryReservationTravelResponseBean>> queryReservationTravel(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_TRAVEL_QUERY()).setContentType(com.geely.rxhttp.ContentType.Json).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.useGateWay(true).setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.QueryReservationTravelResponseBean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$queryReservationTravel$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setControlTemperature$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> setControlTemperature(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_TEMPERATURE_CONTROL()).setContentType(com.geely.rxhttp.ContentType.Json).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.useGateWay(true).setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setControlTemperature$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getSentinelFileList$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> getSentinelFileList(java.util.Map<java.lang.String, ? extends java.lang.Object> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_SENTINEL_FILE_LIST()).setContentType(com.geely.rxhttp.ContentType.Json).setAppCode(getUSP_GATEWAY_CODE()).useGateWay(true).setJsonBody(map).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getSentinelFileList$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$deleteSentinelFile$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> deleteSentinelFile(java.util.Map<java.lang.String, ? extends java.lang.Object> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_SENTINEL_DELETE_FILE_LIST()).setContentType(com.geely.rxhttp.ContentType.Json).setAppCode(getUSP_GATEWAY_CODE()).useGateWay(true).setJsonBody(map).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$deleteSentinelFile$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$reservationChargeConfig$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> reservationChargeConfig(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CHARGE_CONFIG()).setContentType(com.geely.rxhttp.ContentType.Json).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.useGateWay(true).setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$reservationChargeConfig$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$reservationChargeStart$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> reservationChargeStart(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CHARGE_START()).setContentType(com.geely.rxhttp.ContentType.Json).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.useGateWay(true).setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$reservationChargeStart$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$otaInstall$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.OtaInstallResponseBean>> otaInstall(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_OTA_INSTALL()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.OtaInstallResponseBean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$otaInstall$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$otaInstallCancel$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> otaInstallCancel(java.lang.String vin) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(vin, "vin");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder().addQueryParam("vin", vin).useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_GET_OTA_INSTALL_CANCEL()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$otaInstallCancel$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$otaUpdateHistory$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.OtaUpdateHistoryResponseBean>> otaUpdateHistory(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_GET_OTA_UPDATE_HISTORY()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.OtaUpdateHistoryResponseBean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$otaUpdateHistory$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$otaVersionQuery$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.OtaVersionQueryResponseBean>> otaVersionQuery(java.lang.String vin) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(vin, "vin");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder().addQueryParam("vin", vin).useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_GET_OTA_VERSION_QUERY()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.OtaVersionQueryResponseBean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$otaVersionQuery$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$otaSettingQuery$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.OtaSettingQueryResponseBean>> otaSettingQuery(java.lang.String vin) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(vin, "vin");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder().addQueryParam("vin", vin).useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_GET_OTA_SETTING_QUERY()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.OtaSettingQueryResponseBean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$otaSettingQuery$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$otaSetting$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.OtaSetBean>> otaSetting(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_OTA_SETTING()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.OtaSetBean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$otaSetting$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getJournalLogList$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourneyListBean>> getJournalLogList(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_GET_JOU_LOG()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourneyListBean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getJournalLogList$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getJournalLogTrack$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourneyTrack>> getJournalLogTrack(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_GET_JOU_LOG_DETAIL()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourneyTrack>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getJournalLogTrack$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$deleteJournalLog$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> deleteJournalLog(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_DELETE_JOU_LOG()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$deleteJournalLog$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$msgListQuery$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.MsgListQueryResponseBean>> msgListQuery(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getCAR_MSG_LIST()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.MsgListQueryResponseBean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$msgListQuery$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$menuListQuery$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.util.List<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.MenuListQueryResponseBean>>> menuListQuery(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getCAR_MENU_LIST()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.util.List<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.MenuListQueryResponseBean>>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$menuListQuery$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$carModelJsonQuery$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.CarModelJsonQueryResponseBean>> carModelJsonQuery(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getCAR_MODEL_JSON()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE()).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.CarModelJsonQueryResponseBean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$carModelJsonQuery$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$electronicFenceCreate$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> electronicFenceCreate(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_ELECTRONIC_FENCE_CREATE()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$electronicFenceCreate$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$electronicFenceQuery$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ElectronicFenceQueryResponseBean>> electronicFenceQuery(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_ELECTRONIC_FENCE_QUERY()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ElectronicFenceQueryResponseBean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$electronicFenceQuery$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$electronicFenceUpdate$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> electronicFenceUpdate(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_ELECTRONIC_FENCE_UPDATE()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$electronicFenceUpdate$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$electronicFenceDelete$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> electronicFenceDelete(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_ELECTRONIC_FENCE_DELETE()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$electronicFenceDelete$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$sendToCar$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Object>> sendToCar(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_VEHICLE_SEND_TO_CAR()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Object>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$sendToCar$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getCarShareList$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.functiontools.control.bean.CarShareList>> getCarShareList(java.lang.String vin) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(vin, "vin");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder().addQueryParam("vin", vin).useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_GET_CAR_SHARE_LIST()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.functiontools.control.bean.CarShareList>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getCarShareList$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$createCarShare$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> createCarShare(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_CREATE_CAR_SHARE()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$createCarShare$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getSharedUnhandledList$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.util.List<com.geely.galaxy.carcontrol.core.functiontools.control.bean.BeSharedBean>>> getSharedUnhandledList() {
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_GET_BE_SHARED_LIST()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.util.List<com.geely.galaxy.carcontrol.core.functiontools.control.bean.BeSharedBean>>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getSharedUnhandledList$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getUnreadShareCount$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>> getUnreadShareCount(java.lang.String vin) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(vin, "vin");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder().addQueryParam("vin", vin).useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_UNREAD_SHARE_COUNT()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getUnreadShareCount$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$readShare$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>> readShare() {
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_READ_SHARE()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$readShare$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v8, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$unbindShare$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>> unbindShare(java.util.Map<java.lang.String, java.lang.String> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder jsonBody = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setContentType(com.geely.rxhttp.ContentType.Json).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_UNBIND_SHARE()).setAppCode(getUSP_GATEWAY_CODE_NEW()).setJsonBody(map);
        jsonBody.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = jsonBody.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$unbindShare$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v10, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$updateShareStatus$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>> updateShareStatus(java.util.Map<java.lang.String, ? extends java.lang.Object> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder jsonBody = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setContentType(com.geely.rxhttp.ContentType.Json).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_UPDATE_SHARE()).setAppCode(getUSP_GATEWAY_CODE_NEW()).setJsonBody(map);
        java.lang.String appVersionName = com.blankj.utilcode.util.AppUtils.getAppVersionName();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(appVersionName, "getAppVersionName()");
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder builderAddHeader = jsonBody.addHeader("appversion", appVersionName);
        builderAddHeader.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = builderAddHeader.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$updateShareStatus$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$readBeShared$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>> readBeShared(long recordId) {
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder().addQueryParam("id", java.lang.Long.valueOf(recordId)).useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_READ_BE_SHARED()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$readBeShared$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getBeSharedDetail$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.functiontools.control.bean.BeSharedBean>> getBeSharedDetail(long recordId) {
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder().addQueryParam("id", java.lang.Long.valueOf(recordId)).useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_GET_BE_SHARED_DETAIL()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.functiontools.control.bean.BeSharedBean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getBeSharedDetail$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v8, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getCacheMessage$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleGetResultResponseVO>> getCacheMessage(java.lang.String sessionId) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.CacheMessageReq((java.lang.Integer) null, sessionId, (java.lang.String) null, 5, (kotlin.jvm.internal.DefaultConstructorMarker) null));
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_GET_CONTROL_RESULT()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(json).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleGetResultResponseVO>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getCacheMessage$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setCarOnOffLine$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Boolean>> setCarOnOffLine(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setContentType(com.geely.rxhttp.ContentType.Json).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_LONG_CONNECT_OP()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Boolean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setCarOnOffLine$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setCarHeartBeat$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Boolean>> setCarHeartBeat(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setContentType(com.geely.rxhttp.ContentType.Json).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_LONG_CONNECT_HEART_BEAT()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Boolean>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$setCarHeartBeat$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$editCarNickName$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> editCarNickName(java.lang.String jsonStr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(jsonStr, "jsonStr");
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setCustomUrl(getBASE_USP_URL() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_EDIT_CAR_NICK_NAME()).setContentType(com.geely.rxhttp.ContentType.Json).setJson(jsonStr).setAppCode(getUSP_GATEWAY_CODE()).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$editCarNickName$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getParkPhoto$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ParkPhotoResponse>> getParkPhoto(java.lang.String jsonStr) {
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setContentType(com.geely.rxhttp.ContentType.Json).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_PARK_PHOTO()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ParkPhotoResponse>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getParkPhoto$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getParkPosition$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ParkPositionResponse>> getParkPosition(java.lang.String jsonStr) {
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setContentType(com.geely.rxhttp.ContentType.Json).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_PARK_POSITION()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ParkPositionResponse>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getParkPosition$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$controlRefrigerator$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> controlRefrigerator(java.lang.String jsonStr) {
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_CONTROL_REFRIGERATOR()).setContentType(com.geely.rxhttp.ContentType.Json).useGateWay(true).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$controlRefrigerator$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$queryRefrigerator$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.RefrigeratorResponse>> queryRefrigerator(java.lang.String jsonStr) {
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_REFRIGERATOR_QUERY()).setContentType(com.geely.rxhttp.ContentType.Json).useGateWay(true).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.setJson(jsonStr).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.RefrigeratorResponse>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$queryRefrigerator$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getBatteryBasic$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryBasicResponse>> getBatteryBasic(java.lang.String vin) {
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder contentType = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setContentType(com.geely.rxhttp.ContentType.Json);
        java.util.HashMap<java.lang.String, java.lang.Object> map = new java.util.HashMap<>();
        map.put("vin", vin);
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = contentType.setQueryParams(map).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_BIGDATA_BASIC_INFO()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryBasicResponse>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getBatteryBasic$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getBatteryReport$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryReportResponse>> getBatteryReport(java.lang.String vin, java.lang.String queryDate) {
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder contentType = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setContentType(com.geely.rxhttp.ContentType.Json);
        java.util.HashMap<java.lang.String, java.lang.Object> map = new java.util.HashMap<>();
        map.put("vin", vin);
        map.put("queryDate", queryDate);
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = contentType.setQueryParams(map).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_BIGDATA_REPORT()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryReportResponse>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getBatteryReport$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getBatteryRecord$$inlined$execute$1] */
    public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryRecordResponse>> getBatteryRecord(java.lang.String vin) {
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder contentType = new com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder().useGateWay(true).setContentType(com.geely.rxhttp.ContentType.Json);
        java.util.HashMap<java.lang.String, java.lang.Object> map = new java.util.HashMap<>();
        map.put("vin", vin);
        com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder appCode = contentType.setQueryParams(map).setCustomUrl(getBASE_USP_URL_NEW() + com.geely.cockpit.services.controlservice.core.uspapi.UspApiConstantKt.getAPI_POST_BIGDATA_RECORD()).setAppCode(getUSP_GATEWAY_CODE_NEW());
        appCode.addHeader("x-ca-stage", com.geely.cockpit.services.controlservice.runtime.CurEnvType.INSTANCE.isNotProd() ? "PRE" : "RELEASE");
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = appCode.build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryRecordResponse>>() { // from class: com.geely.cockpit.services.controlservice.core.uspapi.UspApiService$getBatteryRecord$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        return httpExecutorProxy.execute(iBaseRequestBuild, type);
    }
}
