package com.luca.geelykeyradar;

import android.app.PendingIntent;

/**
 * 官方车控 token 的显式语义封装。
 *
 * NOTIFICATION 是官方通知按钮的精确动作；WIDGET 是官方小组件 LOCK 控件的切换动作，
 * 两者不能混用。
 */
final class OfficialControlToken {
    enum Source {
        NOTIFICATION,
        WIDGET
    }

    enum Semantics {
        EXPLICIT,
        TOGGLE
    }

    final Source source;
    final Semantics semantics;
    final PendingIntent pendingIntent;
    final long capturedAtMs;

    private OfficialControlToken(Source source, Semantics semantics,
                                 PendingIntent pendingIntent, long capturedAtMs) {
        this.source = source;
        this.semantics = semantics;
        this.pendingIntent = pendingIntent;
        this.capturedAtMs = capturedAtMs;
    }

    static OfficialControlToken notification(PendingIntent pendingIntent, long capturedAtMs) {
        return new OfficialControlToken(Source.NOTIFICATION, Semantics.EXPLICIT,
                pendingIntent, capturedAtMs);
    }

    static OfficialControlToken widgetToggle(PendingIntent pendingIntent, long capturedAtMs) {
        return new OfficialControlToken(Source.WIDGET, Semantics.TOGGLE,
                pendingIntent, capturedAtMs);
    }

    boolean isUsable() {
        return pendingIntent != null;
    }
}
