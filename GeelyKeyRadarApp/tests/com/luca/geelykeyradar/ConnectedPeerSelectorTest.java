package com.luca.geelykeyradar;

import java.util.List;
import java.util.Set;

/** 系统 GATT 连接增量选择回归，不依赖 Android。 */
public final class ConnectedPeerSelectorTest {
    public static void main(String[] args) {
        selectsNewLePeerAmongPersonalDevices();
        fallsBackToUniqueUnbondedPeer();
        rejectsAmbiguousPeers();
        usesRecognizedNameOnlyAsTiebreak();
        learnsRecognizedVehicleWithoutNotification();
        rejectsUnrecognizedOrAmbiguousVehicleWithoutNotification();
        System.out.println("ConnectedPeerSelectorTest passed");
    }

    private static void selectsNewLePeerAmongPersonalDevices() {
        ConnectedPeerSelector.Peer band = peer("AA:00:00:00:00:01", "Band", 2, 12);
        ConnectedPeerSelector.Peer car = peer("AA:00:00:00:00:02", "", 2, 10);
        ConnectedPeerSelector.Selection selected = ConnectedPeerSelector.select(
                List.of(band, car), Set.of(band.address));
        check(selected.selected() && selected.address.equals(car.address), "应选择官方连接后新增的LE车机");
        check(selected.reason.equals("NEW_LE_PEER"), "增量来源应可诊断");
    }

    private static void fallsBackToUniqueUnbondedPeer() {
        ConnectedPeerSelector.Peer band = peer("BB:00:00:00:00:01", "Band", 2, 12);
        ConnectedPeerSelector.Peer car = peer("BB:00:00:00:00:02", "[E21H-A1-1687]", 2, 10);
        ConnectedPeerSelector.Selection selected = ConnectedPeerSelector.select(
                List.of(band, car), Set.of());
        check(selected.address.equals(car.address), "无增量时应优先唯一非绑定LE设备");
        check(selected.reason.equals("NEW_UNBONDED_PEER"), "唯一非绑定设备原因应明确");
    }

    private static void rejectsAmbiguousPeers() {
        ConnectedPeerSelector.Peer one = peer("CC:00:00:00:00:01", "Device1", 2, 10);
        ConnectedPeerSelector.Peer two = peer("CC:00:00:00:00:02", "Device2", 2, 10);
        ConnectedPeerSelector.Selection selected = ConnectedPeerSelector.select(
                List.of(one, two), Set.of());
        check(!selected.selected() && selected.reason.equals("AMBIGUOUS"), "多个同等LE连接不能猜测");
    }

    private static void usesRecognizedNameOnlyAsTiebreak() {
        ConnectedPeerSelector.Peer one = peer("DD:00:00:00:00:01", "Band", 2, 12);
        ConnectedPeerSelector.Peer two = peer("DD:00:00:00:00:02", "GeelyAuto", 2, 12);
        ConnectedPeerSelector.Selection selected = ConnectedPeerSelector.select(
                List.of(one, two), Set.of());
        check(selected.address.equals(two.address), "名称只能在多个连接中做唯一消歧");
        check(selected.reason.equals("RECOGNIZED_NAME_TIEBREAK"), "名称分支不能冒充连接增量");
    }

    private static void learnsRecognizedVehicleWithoutNotification() {
        ConnectedPeerSelector.Peer band = peer("EE:00:00:00:00:01", "Band", 2, 12);
        ConnectedPeerSelector.Peer car = peer("EE:00:00:00:00:02", "GeelyAuto", 2, 10);
        ConnectedPeerSelector.Selection selected = ConnectedPeerSelector
                .selectRecognizedVehicleWithoutNotification(List.of(band, car));
        check(selected.selected() && selected.address.equals(car.address),
                "通知缺失时唯一 GeelyAuto GATT 连接可用于学习");
        check(selected.reason.equals("UNIQUE_RECOGNIZED_VEHICLE_WITHOUT_NOTIFICATION"),
                "无通知学习来源必须可诊断");
    }

    private static void rejectsUnrecognizedOrAmbiguousVehicleWithoutNotification() {
        ConnectedPeerSelector.Peer band = peer("FF:00:00:00:00:01", "Band", 2, 10);
        ConnectedPeerSelector.Selection unrecognized = ConnectedPeerSelector
                .selectRecognizedVehicleWithoutNotification(List.of(band));
        check(!unrecognized.selected() && unrecognized.reason.equals("NO_RECOGNIZED_VEHICLE"),
                "通知缺失时不能把唯一耳机当成车辆");

        ConnectedPeerSelector.Peer car1 = peer("FF:00:00:00:00:02", "GeelyAuto", 2, 10);
        ConnectedPeerSelector.Peer car2 = peer("FF:00:00:00:00:03", "GeelyVehicle245", 2, 10);
        ConnectedPeerSelector.Selection ambiguous = ConnectedPeerSelector
                .selectRecognizedVehicleWithoutNotification(List.of(car1, car2));
        check(!ambiguous.selected() && ambiguous.reason.equals("AMBIGUOUS_RECOGNIZED_VEHICLE"),
                "通知缺失时多个吉利 GATT 连接必须拒绝猜测");
    }

    private static ConnectedPeerSelector.Peer peer(String address, String name, int type, int bond) {
        return new ConnectedPeerSelector.Peer(address, name, type, bond);
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
