package com.luca.geelykeyradar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 多车无感车库协调器（纯 Java）：
 * - 支持“多车自动无感”（同时扫描并匹配全部启用车辆）或“锁定单车”；
 * - 显式支持对某台待学习新车进入学习模式；
 * - 匹配时严格按 ADDRESS > NAME > HINT 优先级，并拒绝 CONFLICT。
 */
final class VehicleGarage {
    static final class MatchResult {
        final VehicleProfile profile;
        final VehicleIdentity.Match match;

        MatchResult(VehicleProfile profile, VehicleIdentity.Match match) {
            this.profile = profile;
            this.match = match;
        }
    }

    final List<VehicleProfile> profiles;
    final String pinnedProfileId;
    final String learningProfileId;
    final OfficialBrandPolicy.Brand brandMode;

    VehicleGarage(List<VehicleProfile> profiles, String pinnedProfileId,
                  String learningProfileId, OfficialBrandPolicy.Brand brandMode) {
        this.profiles = profiles == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(profiles));
        this.pinnedProfileId = pinnedProfileId == null ? "" : pinnedProfileId.trim();
        this.learningProfileId = learningProfileId == null ? "" : learningProfileId.trim();
        this.brandMode = brandMode == null ? OfficialBrandPolicy.Brand.AUTO : brandMode;
    }

    static VehicleGarage empty(OfficialBrandPolicy.Brand brandMode) {
        return new VehicleGarage(Collections.emptyList(), "", "", brandMode);
    }

    VehicleProfile findById(String id) {
        if (id == null || id.trim().isEmpty()) return null;
        for (VehicleProfile profile : profiles) {
            if (id.equals(profile.id)) return profile;
        }
        return null;
    }

    /** 返回当前参与扫描与匹配的车辆列表。锁定单车时仅包含该车；否则包含所有已启用车辆。 */
    List<VehicleProfile> activeProfiles() {
        VehicleProfile learning = findById(learningProfileId);
        if (learning != null) {
            return Collections.singletonList(learning);
        }
        VehicleProfile pinned = findById(pinnedProfileId);
        if (pinned != null) {
            return Collections.singletonList(pinned);
        }
        List<VehicleProfile> enabled = new ArrayList<>();
        for (VehicleProfile profile : profiles) {
            if (profile.enabled) enabled.add(profile);
        }
        return enabled;
    }

    int enabledCount() {
        int count = 0;
        for (VehicleProfile profile : profiles) {
            if (profile.enabled) count++;
        }
        return count;
    }

    boolean isMultiCarAutoMode() {
        return pinnedProfileId.isEmpty() && learningProfileId.isEmpty() && enabledCount() >= 2;
    }

    boolean isExplicitLearning() {
        VehicleProfile learning = findById(learningProfileId);
        return learning != null && !learning.hasMatchableClue();
    }

    /** 根据当前启用车辆生成硬件 BLE ScanFilter 计划；若待学习新车尚无线索则返回 LEARNING 宽过滤。 */
    VehicleScanPolicy.Plan buildScanPlan() {
        VehicleProfile learning = findById(learningProfileId);
        if (learning != null) {
            return learning.hasMatchableClue()
                    ? VehicleScanPolicy.build(learning.identity)
                    : VehicleScanPolicy.build(null);
        }
        List<VehicleProfile> active = activeProfiles();
        if (active.isEmpty()) return VehicleScanPolicy.build(null);
        List<VehicleIdentity> identities = new ArrayList<>();
        for (VehicleProfile profile : active) {
            if (profile.hasMatchableClue()) {
                identities.add(profile.identity);
            }
        }
        if (identities.isEmpty()) {
            return VehicleScanPolicy.build(null);
        }
        return VehicleScanPolicy.buildMulti(identities);
    }

    /**
     * 在全部参与车辆中选择最匹配当前广播的档案。
     * 优先级：已学习 ADDRESS > 完整 NAME > 稳定 HINT；任何冲突车名直接排除。
     */
    MatchResult matchBest(VehicleBeacon ad) {
        if (ad == null) return null;
        MatchResult best = null;
        int bestRank = -1;
        for (VehicleProfile profile : activeProfiles()) {
            if (profile.identity == null) continue;
            VehicleIdentity.Match m = profile.identity.match(ad);
            int rank = rank(m);
            if (rank > bestRank) {
                bestRank = rank;
                best = new MatchResult(profile, m);
            }
        }
        return best;
    }

    private static int rank(VehicleIdentity.Match match) {
        if (match == VehicleIdentity.Match.ADDRESS) return 3;
        if (match == VehicleIdentity.Match.NAME) return 2;
        if (match == VehicleIdentity.Match.HINT) return 1;
        return -1;
    }
}
