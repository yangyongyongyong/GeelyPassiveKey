package com.luca.geelykeyradar;

final class WidgetTokenPolicyTest {
    public static void main(String[] args) {
        long now = 100_000L;
        check(WidgetTokenPolicy.canUseToggle(PassiveKeyLogic.Action.UNLOCK, "LOCKED", now - 1000L, now),
                "locked vehicle permits widget unlock");
        check(WidgetTokenPolicy.canUseToggle(PassiveKeyLogic.Action.LOCK, "UNLOCKED", now - 1000L, now),
                "unlocked vehicle permits widget lock");
        check(!WidgetTokenPolicy.canUseToggle(PassiveKeyLogic.Action.LOCK, "LOCKED", now - 1000L, now),
                "locked vehicle must not lock again");
        check(!WidgetTokenPolicy.canUseToggle(PassiveKeyLogic.Action.UNLOCK, "UNLOCKED", now - 1000L, now),
                "unlocked vehicle must not unlock again");
        check(!WidgetTokenPolicy.canUseToggle(PassiveKeyLogic.Action.UNLOCK, "", now - 1000L, now),
                "unknown state must be rejected");
        check(!WidgetTokenPolicy.canUseToggle(PassiveKeyLogic.Action.UNLOCK, "LOCKED",
                now - WidgetTokenPolicy.MAX_LOCK_HINT_AGE_MS - 1L, now),
                "stale state must be rejected");
        check(!WidgetTokenPolicy.canUseToggle(PassiveKeyLogic.Action.NONE, "LOCKED", now - 1000L, now),
                "invalid action must be rejected");
        check(WidgetTokenPolicy.selectRoute(PassiveKeyLogic.Action.UNLOCK, true, true, true,
                        "LOCKED", now - 1000L, now) == WidgetTokenPolicy.Route.NOTIFICATION,
                "notification token must always win");
        check(WidgetTokenPolicy.selectRoute(PassiveKeyLogic.Action.UNLOCK, false, true, true,
                        "LOCKED", now - 1000L, now) == WidgetTokenPolicy.Route.WIDGET,
                "fresh opposite state permits widget fallback");
        check(WidgetTokenPolicy.selectRoute(PassiveKeyLogic.Action.UNLOCK, false, false, true,
                        "LOCKED", now - 1000L, now) == WidgetTokenPolicy.Route.NONE,
                "disabled widget fallback must not send");
        check(WidgetTokenPolicy.selectRoute(PassiveKeyLogic.Action.UNLOCK, false, true, false,
                        "LOCKED", now - 1000L, now) == WidgetTokenPolicy.Route.NONE,
                "missing widget token must not send");
        check(WidgetTokenPolicy.selectRoute(PassiveKeyLogic.Action.UNLOCK, false, true, true,
                        "UNKNOWN", now - 1000L, now) == WidgetTokenPolicy.Route.NONE,
                "unknown state must block widget fallback");
        check("WIDGET_TOKEN_MISSING".equals(WidgetTokenPolicy.routeReason(
                        PassiveKeyLogic.Action.UNLOCK, false, true, false,
                        "LOCKED", now - 1000L, now)),
                "missing token reason must be explicit");
        check("WIDGET_DISABLED".equals(WidgetTokenPolicy.routeReason(
                        PassiveKeyLogic.Action.UNLOCK, false, false, true,
                        "LOCKED", now - 1000L, now)),
                "disabled widget reason must be explicit");
        check(WidgetTokenPolicy.selectRoute(PassiveKeyLogic.Action.LOCK, false, true, true,
                        "LOCKED", now - WidgetTokenPolicy.MAX_LOCK_HINT_AGE_MS - 1L, now)
                        == WidgetTokenPolicy.Route.NONE,
                "stale widget state must not route");
        System.out.println("WidgetTokenPolicyTest OK");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
