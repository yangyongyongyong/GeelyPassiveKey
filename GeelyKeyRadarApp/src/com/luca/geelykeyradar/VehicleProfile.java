package com.luca.geelykeyradar;

/**
 * 车库中的单台车辆档案：支持别名、品牌归属（吉利汽车 / 吉利银河 / 自动）、车型预设与可信蓝牙身份。
 */
final class VehicleProfile {
    final String id;
    final String alias;
    final OfficialBrandPolicy.Brand brand;
    final String modelHint;
    final VehicleIdentity identity;
    final String learningSource;
    final boolean enabled;

    VehicleProfile(String id, String alias, OfficialBrandPolicy.Brand brand, String modelHint,
                   VehicleIdentity identity, String learningSource, boolean enabled) {
        this.id = id == null ? "" : id.trim();
        this.alias = alias == null ? "" : alias.trim();
        this.brand = brand == null ? OfficialBrandPolicy.Brand.AUTO : brand;
        this.modelHint = modelHint == null ? "" : modelHint.trim();
        this.identity = identity;
        this.learningSource = learningSource == null || learningSource.trim().isEmpty()
                ? VehicleLearningState.UNKNOWN : learningSource.trim();
        this.enabled = enabled;
    }

    String displayTitle() {
        if (!alias.isEmpty()) return alias;
        if (!modelHint.isEmpty()) return modelHint;
        if (identity != null && !identity.displayName().isEmpty()
                && !"未知车辆".equals(identity.displayName())) {
            return identity.displayName();
        }
        return "未命名车辆";
    }

    boolean hasMatchableClue() {
        if (identity == null) return false;
        return VehicleLearningState.hasAddress(identity.address)
                || VehicleBeacon.isRecognizableVehicleName(identity.name)
                || identity.effectiveTail2().length() == 2
                || identity.hasUsableSignature();
    }

    boolean hasLearnedAddress() {
        return identity != null && VehicleLearningState.hasAddress(identity.address);
    }

    VehicleProfile withIdentity(VehicleIdentity updated, String source) {
        return new VehicleProfile(id, alias, brand, modelHint, updated,
                source == null ? learningSource : source, enabled);
    }

    VehicleProfile withBrand(OfficialBrandPolicy.Brand newBrand) {
        return new VehicleProfile(id, alias, newBrand, modelHint, identity, learningSource, enabled);
    }

    VehicleProfile withMetadata(String newAlias, OfficialBrandPolicy.Brand newBrand, String newModelHint,
                                boolean newEnabled) {
        return new VehicleProfile(id, newAlias, newBrand, newModelHint, identity, learningSource, newEnabled);
    }

    VehicleProfile withEnabled(boolean newEnabled) {
        return new VehicleProfile(id, alias, brand, modelHint, identity, learningSource, newEnabled);
    }
}
