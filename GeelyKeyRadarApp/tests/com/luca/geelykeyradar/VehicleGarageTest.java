package com.luca.geelykeyradar;

import java.util.Arrays;
import java.util.Collections;

/** 多车车库扫描合并、跨品牌车辆匹配隔离与单车锁定纯 Java 回归。 */
public final class VehicleGarageTest {
    public static void main(String[] args) {
        multiCarScanPlanCombinesNarrowFiltersWithoutBroadLeak();
        multiCarMatcherRoutesToCorrectBrandProfile();
        pinnedCarExcludesOtherCars();
        explicitLearningProfileUsesLearningScan();
        System.out.println("VehicleGarageTest passed");
    }

    private static VehicleProfile consumerCar() {
        VehicleIdentity id = VehicleIdentity.from(
                beacon("AA:00:00:00:00:01", "GeelyVehicle819", "19"));
        return new VehicleProfile("car-consumer", "星越L", OfficialBrandPolicy.Brand.CONSUMER,
                "星越L", id, VehicleLearningState.CONNECTED, true);
    }

    private static VehicleProfile galaxyCar() {
        VehicleIdentity id = VehicleIdentity.from(
                beacon("AA:00:00:00:00:02", "GeelyVehicle245", "45"));
        return new VehicleProfile("car-galaxy", "银河E5", OfficialBrandPolicy.Brand.GALAXY,
                "银河E5", id, VehicleLearningState.CONNECTED, true);
    }

    private static void multiCarScanPlanCombinesNarrowFiltersWithoutBroadLeak() {
        VehicleGarage garage = new VehicleGarage(
                Arrays.asList(consumerCar(), galaxyCar()), "", "", OfficialBrandPolicy.Brand.AUTO);
        check(garage.isMultiCarAutoMode(), "两台车均启用且未锁定时应处于多车自动模式");
        VehicleScanPolicy.Plan plan = garage.buildScanPlan();
        check("MULTI_TRACKING".equals(plan.mode), "双车合并计划应使用 MULTI_TRACKING");
        check(plan.matches(beacon("AA:00:00:00:00:01", "GeelyVehicle819", "19")), "应匹配吉利星越L");
        check(plan.matches(beacon("AA:00:00:00:00:02", "GeelyVehicle245", "45")), "应匹配银河E5");
        check(!plan.matches(beacon("BB:00:00:00:00:99", "GeelyVehicle999", "99")),
                "多车合并也不得退化成裸宽过滤放行陌生车辆");
        for (VehicleScanPolicy.Rule rule : plan.rules) {
            check(rule.kind != VehicleScanPolicy.Kind.SERVICE, "多车追踪计划不能保留裸 FDFD 宽规则");
        }
    }

    private static void multiCarMatcherRoutesToCorrectBrandProfile() {
        VehicleGarage garage = new VehicleGarage(
                Arrays.asList(consumerCar(), galaxyCar()), "", "", OfficialBrandPolicy.Brand.AUTO);
        VehicleGarage.MatchResult m1 = garage.matchBest(
                beacon("CC:00:00:00:00:11", "GeelyVehicle819", "19"));
        check(m1 != null && "car-consumer".equals(m1.profile.id)
                && m1.profile.brand == OfficialBrandPolicy.Brand.CONSUMER
                && m1.match == VehicleIdentity.Match.NAME,
                "819 轮换 MAC 后仍必须精准路由到吉利汽车星越L档案");

        VehicleGarage.MatchResult m2 = garage.matchBest(
                beacon("AA:00:00:00:00:02", "", "45"));
        check(m2 != null && "car-galaxy".equals(m2.profile.id)
                && m2.profile.brand == OfficialBrandPolicy.Brand.GALAXY
                && m2.match == VehicleIdentity.Match.ADDRESS,
                "地址匹配必须路由到银河E5档案");

        check(garage.matchBest(beacon("DD:00:00:00:00:77", "GeelyVehicle777", "77")) == null,
                "非车库车辆必须被拒绝");
    }

    private static void pinnedCarExcludesOtherCars() {
        VehicleGarage garage = new VehicleGarage(
                Arrays.asList(consumerCar(), galaxyCar()), "car-galaxy", "",
                OfficialBrandPolicy.Brand.AUTO);
        check(!garage.isMultiCarAutoMode(), "锁定单车时不处于多车模式");
        check(garage.matchBest(beacon("AA:00:00:00:00:01", "GeelyVehicle819", "19")) == null,
                "锁定银河E5时必须忽略星越L广播");
        check(garage.matchBest(beacon("AA:00:00:00:00:02", "GeelyVehicle245", "45")) != null,
                "锁定银河E5时只匹配银河E5");
    }

    private static void explicitLearningProfileUsesLearningScan() {
        VehicleProfile unlearned = new VehicleProfile("car-new", "新星越L",
                OfficialBrandPolicy.Brand.CONSUMER, "星越L", null, VehicleLearningState.UNKNOWN, true);
        VehicleGarage garage = new VehicleGarage(
                Arrays.asList(galaxyCar(), unlearned), "", "car-new", OfficialBrandPolicy.Brand.AUTO);
        check(garage.isExplicitLearning(), "显式学习无线索新车时应识别为学习模式");
        check("LEARNING".equals(garage.buildScanPlan().mode), "学习无线索新车时应使用 LEARNING 扫描计划");
    }

    private static VehicleBeacon beacon(String address, String name, String tail) {
        byte[] payload = new byte[]{6, 2, 0, 20, (byte) tail.charAt(0), (byte) tail.charAt(1)};
        return VehicleBeacon.parse(address, name, -70, 1000, payload, null, null, false);
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
