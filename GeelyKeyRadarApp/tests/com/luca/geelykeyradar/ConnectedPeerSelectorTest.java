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

    private static ConnectedPeerSelector.Peer peer(String address, String name, int type, int bond) {
        return new ConnectedPeerSelector.Peer(address, name, type, bond);
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
