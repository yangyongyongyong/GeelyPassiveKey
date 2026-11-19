package com.luca.geelykeyradar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 根据系统真实 GATT 连接集合选择本车地址。
 *
 * 优先使用官方连接前后新增的唯一 LE 设备；没有增量时，只接受唯一的非绑定 LE 设备
 * 或系统中唯一的 LE 设备。车型名称只作为最后的唯一消歧线索，不再作为学习白名单。
 */
final class ConnectedPeerSelector {
    static final int TYPE_LE = 2;
    static final int BOND_NONE = 10;
    static final int BONDING = 11;
    static final int BONDED = 12;

    static final class Peer {
        final String address;
        final String name;
        final int type;
        final int bondState;

        Peer(String address, String name, int type, int bondState) {
            this.address = VehicleLearningState.hasAddress(address)
                    ? address.toUpperCase(Locale.ROOT) : "";
            this.name = name == null ? "" : name;
            this.type = type;
            this.bondState = bondState;
        }

        boolean valid() {
            return !address.isEmpty();
        }

        boolean le() {
            return type == TYPE_LE;
        }

        boolean bonded() {
            return bondState == BONDED || bondState == BONDING;
        }
    }

    static final class Selection {
        final String address;
        final String reason;
        final int currentLe;
        final int newLe;
        final int unbondedLe;

        Selection(String address, String reason, int currentLe, int newLe, int unbondedLe) {
            this.address = address == null ? "" : address;
            this.reason = reason == null ? "UNKNOWN" : reason;
            this.currentLe = currentLe;
            this.newLe = newLe;
            this.unbondedLe = unbondedLe;
        }

        boolean selected() {
            return !address.isEmpty();
        }

        String diagnostic() {
            return "reason=" + reason + " selected=" + selected()
                    + " current_le=" + currentLe + " new_le=" + newLe + " unbonded_le=" + unbondedLe;
        }
    }

    private ConnectedPeerSelector() {
    }

    static Selection select(Collection<Peer> currentPeers, Collection<String> baselineAddresses) {
        Map<String, Peer> current = dedupe(currentPeers);
        List<Peer> currentLe = new ArrayList<>();
        for (Peer peer : current.values()) if (peer.le()) currentLe.add(peer);

        Set<String> baseline = new LinkedHashSet<>();
        if (baselineAddresses != null) {
            for (String address : baselineAddresses) {
                if (VehicleLearningState.hasAddress(address)) {
                    baseline.add(address.toUpperCase(Locale.ROOT));
                }
            }
        }

        List<Peer> freshLe = new ArrayList<>();
        for (Peer peer : currentLe) if (!baseline.contains(peer.address)) freshLe.add(peer);
        List<Peer> freshUnbonded = unbonded(freshLe);
        if (freshLe.size() == 1) {
            return selection(freshLe.get(0), "NEW_LE_PEER", currentLe, freshLe, freshUnbonded);
        }
        if (freshUnbonded.size() == 1) {
            return selection(freshUnbonded.get(0), "NEW_UNBONDED_PEER", currentLe, freshLe, freshUnbonded);
        }

        List<Peer> allUnbonded = unbonded(currentLe);
        if (allUnbonded.size() == 1) {
            return selection(allUnbonded.get(0), "UNIQUE_UNBONDED_LE", currentLe, freshLe, allUnbonded);
        }
        if (currentLe.size() == 1) {
            return selection(currentLe.get(0), "UNIQUE_LE_PEER", currentLe, freshLe, allUnbonded);
        }

        List<Peer> named = new ArrayList<>();
        for (Peer peer : currentLe) {
            if (VehicleBeacon.isRecognizableVehicleName(peer.name)) named.add(peer);
        }
        if (named.size() == 1) {
            return selection(named.get(0), "RECOGNIZED_NAME_TIEBREAK", currentLe, freshLe, allUnbonded);
        }
        return new Selection("", currentLe.isEmpty() ? "NO_LE_PEER" : "AMBIGUOUS",
                currentLe.size(), freshLe.size(), allUnbonded.size());
    }

    private static Map<String, Peer> dedupe(Collection<Peer> peers) {
        Map<String, Peer> result = new LinkedHashMap<>();
        if (peers == null) return result;
        for (Peer peer : peers) if (peer != null && peer.valid()) result.putIfAbsent(peer.address, peer);
        return result;
    }

    private static List<Peer> unbonded(List<Peer> peers) {
        List<Peer> result = new ArrayList<>();
        for (Peer peer : peers) if (!peer.bonded()) result.add(peer);
        return result;
    }

    private static Selection selection(Peer peer, String reason, List<Peer> currentLe,
                                       List<Peer> freshLe, List<Peer> unbonded) {
        return new Selection(peer.address, reason, currentLe.size(), freshLe.size(), unbonded.size());
    }
}
