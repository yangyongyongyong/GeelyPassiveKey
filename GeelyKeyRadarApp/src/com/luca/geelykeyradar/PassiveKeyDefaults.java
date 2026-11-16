package com.luca.geelykeyradar;

/** 出厂触发参数；运行时仍以用户持久化配置为准，升级不覆盖已有设置。 */
final class PassiveKeyDefaults {
    static final float UNLOCK_DBM = -75.0f;
    static final float LOCK_DBM = -85.0f;
    static final long LOCK_DWELL_MS = 2000L;
    static final float CONNECT_DBM = -95.0f;
    static final float LONG_ABSENCE_CONNECT_OFFSET_DBM = 7.0f;
    static final float MIN_CONNECT_DBM = -125.0f;

    /** 禁止实例化纯常量容器。 */
    private PassiveKeyDefaults() {
    }
}
