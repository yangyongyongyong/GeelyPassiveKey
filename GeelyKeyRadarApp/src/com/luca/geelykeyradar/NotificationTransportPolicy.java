package com.luca.geelykeyradar;

/** 官方通知车控的发送优先级：当前 RemoteViews 按钮优先，PendingIntent 作为兼容兜底。 */
final class NotificationTransportPolicy {
    enum Transport {
        CURRENT_VIEW,
        CURRENT_PENDING_INTENT,
        CACHED_PENDING_INTENT,
        CACHED_VIEW,
        NONE
    }

    private NotificationTransportPolicy() {
    }

    static Transport select(boolean currentView, boolean currentPendingIntent,
                            boolean cachedPendingIntent, boolean cachedView) {
        if (currentView) return Transport.CURRENT_VIEW;
        if (currentPendingIntent) return Transport.CURRENT_PENDING_INTENT;
        if (cachedPendingIntent) return Transport.CACHED_PENDING_INTENT;
        if (cachedView) return Transport.CACHED_VIEW;
        return Transport.NONE;
    }

    static Transport pendingIntentAfterViewMiss(boolean currentPendingIntent,
                                                boolean cachedPendingIntent) {
        if (currentPendingIntent) return Transport.CURRENT_PENDING_INTENT;
        if (cachedPendingIntent) return Transport.CACHED_PENDING_INTENT;
        return Transport.NONE;
    }
}
