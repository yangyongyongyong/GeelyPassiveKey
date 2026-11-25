package com.luca.geelykeyradar;

/** 硬件过滤计划与重建预算回归，直接测试运行时使用的纯Java策略。 */
public final class VehicleScanPolicyTest {
    /** 验证跟踪替换广扫、匿名轮换发现、手填掩码与扫描启动预算。 */
    public static void main(String[] args) {
        trackingReplacesBroadFilters();
        manufacturerPrefixIsDiscoveryOnly();
        manualHintUsesTailMask();
        learningIsFilteredAndBudgeted();
        geelyAutoGattLearningAllowsBothNameVariants();
        compatibilityCaptureBlocksFilterRefresh();
        System.out.println("VehicleScanPolicyTest passed");
    }

    /** 相邻多辆同品牌车不能因残留裸厂商/UUID规则全部绕过本车过滤。 */
    private static void trackingReplacesBroadFilters() {
        VehicleBeacon own = beacon("AA:00:00:00:00:01", "GeelyVehicle245", "45");
        VehicleIdentity identity = VehicleIdentity.from(own);
        VehicleScanPolicy.Plan plan = VehicleScanPolicy.build(identity);
        check(plan.mode.equals("TRACKING") && plan.rules.size() == 4,
                "已学习使用地址、名称、稳定四字节VIN与两字节VIN兜底四条规则");
        check(plan.matches(own), "本车不被过滤掉");
        for (int index = 0; index < 100; index++) {
            if (index == 45) continue;
            String tail = String.format(java.util.Locale.ROOT, "%02d", index);
            VehicleBeacon other = beacon(String.format(java.util.Locale.ROOT, "BB:00:00:00:00:%02X", index),
                    "GeelyVehicle3" + tail, tail);
            check(!plan.matches(other), "同厂商不同尾号不能通过精确计划");
        }
        for (VehicleScanPolicy.Rule rule : plan.rules) {
            check(rule.kind != VehicleScanPolicy.Kind.SERVICE, "跟踪模式没有裸FDFD");
            check(rule.kind != VehicleScanPolicy.Kind.MANUFACTURER || rule.data.length >= 6, "跟踪没有空厂商数据宽过滤");
        }
        int discoveryRules = 0;
        for (VehicleScanPolicy.Rule rule : plan.rules) {
            if (rule.kind != VehicleScanPolicy.Kind.MANUFACTURER) continue;
            discoveryRules++;
            check((rule.mask[0] & 255) == 255 && rule.mask[1] == 0
                    && (rule.mask[4] & 255) == 255 && (rule.mask[5] & 255) == 255,
                    "厂商掩码始终要求vendor与VIN后两位");
        }
        check(discoveryRules == 2, "稳定发现只保留优先与兜底两份厂商掩码");
        VehicleIdentity sameFields = new VehicleIdentity(identity.address, identity.name, identity.vinTail2, identity.vinTail3,
                identity.mfgSignature, identity.manufacturerId, identity.learnedAtMs + 1000);
        check(plan.key.equals(VehicleScanPolicy.build(sameFields).key), "学习时间变化不触发重配置");
    }

    /** 同短前缀匿名轮换仍能被发现，但软件强匹配阻止邻车线索参与RSSI或直接改MAC。 */
    private static void manufacturerPrefixIsDiscoveryOnly() {
        VehicleIdentity identity = VehicleIdentity.from(beacon("AA:00:00:00:00:01", "GeelyVehicle245", "45"));
        VehicleBeacon anonymous = beacon("BB:00:00:00:00:02", "", "45", "060300993435");
        VehicleBeacon antennaChanged = beacon("CC:00:00:00:00:03", "", "45", "060201143435");
        VehicleScanPolicy.Plan plan = VehicleScanPolicy.build(identity);
        check(plan.matches(anonymous) && plan.matches(antennaChanged),
                "version/devices变化优先掩码及antenna变化兜底掩码都能发现");
        check(!plan.matches(beacon("DD:00:00:00:00:04", "", "46", "060201143436")),
                "天线变化兜底仍必须匹配VIN后两位");
        VehicleBeacon otherCompany = VehicleBeacon.parse("EE:00:00:00:00:05", "", -70, 1000,
                null, VehicleBeacon.hexToBytes("060200143435"), null, false);
        check(!plan.matches(otherCompany), "Company ID不同不能通过本车厂商计划");
        check(!identity.matches(anonymous) && identity.match(anonymous) == VehicleIdentity.Match.HINT
                        && identity.match(antennaChanged) == VehicleIdentity.Match.HINT,
                "稳定掩码发现不等于可信测距");
        VehicleIdentity addressOnly = new VehicleIdentity(identity.address, "", "", "", "", 1000);
        check(VehicleScanPolicy.build(addressOnly).rules.size() == 1, "只有MAC证据时不擅自添加广扫兜底");
        VehicleIdentity legacy = new VehicleIdentity(identity.address, "", "45", "", "15FF060200143435", 1000);
        VehicleScanPolicy.Plan legacyPlan = VehicleScanPolicy.build(legacy);
        check(legacyPlan.rules.size() == 3, "旧15FF除地址外保留两份稳定掩码规则");
        check(legacyPlan.rules.get(1).manufacturerId == VehicleBeacon.COMPANY_ID_LEGACY
                && legacyPlan.rules.get(1).data.length == 6
                && legacyPlan.rules.get(2).manufacturerId == VehicleBeacon.COMPANY_ID_LEGACY,
                "旧15FF签名前两个company字节剥离后生成两份稳定过滤");
    }

    /** 只填尾号时vendor与VIN位参与过滤，版本字段不写死；这是发现计划而非已学习状态。 */
    private static void manualHintUsesTailMask() {
        VehicleIdentity manual = new VehicleIdentity("", "GeelyVehicle245", "45", "245", "", 1000);
        VehicleScanPolicy.Plan plan = VehicleScanPolicy.build(manual);
        check(plan.mode.equals("HINT_DISCOVERY"), "手填提示没有地址不算跟踪完成");
        check(plan.matches(beacon("AA:00:00:00:00:01", "", "45")), "同尾号匿名候选能用于官方发现");
        check(!plan.matches(beacon("BB:00:00:00:00:02", "GeelyVehicle346", "46")), "不同尾号被掩码排除");
        VehicleScanPolicy.Rule rule = plan.rules.get(1);
        check((rule.mask[0] & 255) == 255 && rule.mask[1] == 0 && (rule.mask[4] & 255) == 255,
                "vendor与尾号需匹配，协议版本不写死");
    }

    /** 所有模式保持非空过滤，四次启动后等待三十秒滚动预算，身份逐包变化不能无限stop/start。 */
    private static void learningIsFilteredAndBudgeted() {
        VehicleScanPolicy.Plan learning = VehicleScanPolicy.build(null);
        boolean hasUnderscoreName = false;
        boolean hasCompactName = false;
        for (VehicleScanPolicy.Rule rule : learning.rules) {
            if (rule.kind != VehicleScanPolicy.Kind.NAME) continue;
            if ("Geely_Auto".equals(rule.value)) hasUnderscoreName = true;
            if ("GeelyAuto".equals(rule.value)) hasCompactName = true;
        }
        check(hasUnderscoreName && hasCompactName,
                "首次学习必须同时由硬件过滤放行 Geely_Auto 与 GeelyAuto");
        check(learning.mode.equals("LEARNING") && learning.rules.size() == 6,
                "首次学习使用四个协议规则加两种星愿名称规则，仍支持息屏");
        VehicleScanPolicy.RestartBudget budget = new VehicleScanPolicy.RestartBudget();
        for (long now = 1000; now <= 4000; now += 1000) {
            check(budget.delayMs(now) == 0, "启动间隔达到一秒可启动");
            budget.started(now);
        }
        check(budget.delayMs(4500) == 26500, "四次三十秒预算阻止第五次频繁启动");
        check(budget.delayMs(31000) == 0, "最老启动离开窗口后可重配置");
        budget.started(31000);
        check(budget.delayMs(31001) > 0, "刚完成重建时不立即再重建");
    }


    /** 星愿通过 GATT 学到 GeelyAuto 且无厂商签名时，仍必须同时放行 GeelyAuto 与 Geely_Auto。 */
    private static void geelyAutoGattLearningAllowsBothNameVariants() {
        VehicleIdentity identity = new VehicleIdentity("AA:00:00:00:00:99", "GeelyAuto", "", "", "", -1, 1000L);
        VehicleScanPolicy.Plan plan = VehicleScanPolicy.build(identity);
        check("TRACKING".equals(plan.mode), "已有真实MAC时进入TRACKING");
        int nameCount = 0;
        for (VehicleScanPolicy.Rule rule : plan.rules) {
            if (rule.kind == VehicleScanPolicy.Kind.NAME) nameCount++;
        }
        check(nameCount == 2 && plan.rules.size() == 3, "应包含地址和两种 GeelyAuto/Geely_Auto 名称规则");
        check(plan.matches(VehicleBeacon.parse("BB:00:00:00:00:01", "GeelyAuto", -70, 1000L, null, null, null, false)),
                "必须匹配 GeelyAuto 广播");
        check(plan.matches(VehicleBeacon.parse("BB:00:00:00:00:01", "Geely_Auto", -70, 1000L, null, null, null, false)),
                "必须匹配 Geely_Auto 广播");
        check(!plan.matches(VehicleBeacon.parse("BB:00:00:00:00:02", "OtherCar", -70, 1000L, null, null, null, false)),
                "非目标名称和地址不得匹配");
    }

    /** 10 秒兼容抓包期间禁止后台过滤重配置打断原始抓取。 */
    private static void compatibilityCaptureBlocksFilterRefresh() {
        check(!VehicleScanPolicy.shouldRefreshScanFilters(true, true, true, true, "TRACKING|a", "COMPATIBILITY_RAW"),
                "兼容抓包期间不得触发过滤重配置");
        check(VehicleScanPolicy.shouldRefreshScanFilters(true, true, true, false, "TRACKING|a", "LEARNING|b"),
                "非兼容抓包且过滤计划变化时允许重配置");
        check(!VehicleScanPolicy.shouldRefreshScanFilters(true, true, true, false, "TRACKING|a", "TRACKING|a"),
                "过滤计划未变化时不得重配置");
        check(!VehicleScanPolicy.shouldRefreshScanFilters(true, true, false, false, "TRACKING|a", "STOPPED"),
                "未扫描时不得重配置");
    }

    /** 构造厂商前缀一致且末两位可控的短合成广告。 */
    private static VehicleBeacon beacon(String address, String name, String tail) {
        byte[] payload = new byte[]{6, 2, 0, 20, (byte) tail.charAt(0), (byte) tail.charAt(1)};
        return VehicleBeacon.parse(address, name, -70, 1000, payload, null, null, false);
    }

    /** 构造可指定稳定前缀变体的合成广告，用于验证动态字节不影响发现。 */
    private static VehicleBeacon beacon(String address, String name, String tail, String prefix) {
        byte[] payload = VehicleBeacon.hexToBytes(prefix);
        if (payload.length < 6 || tail.length() != 2
                || payload[4] != (byte) tail.charAt(0) || payload[5] != (byte) tail.charAt(1)) {
            throw new AssertionError("测试前缀尾号必须与名称一致");
        }
        return VehicleBeacon.parse(address, name, -70, 1000, payload, null, null, false);
    }

    /** 断言失败时解释过滤规则，避免“添加精确条件”被误当作已经收窄。 */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
