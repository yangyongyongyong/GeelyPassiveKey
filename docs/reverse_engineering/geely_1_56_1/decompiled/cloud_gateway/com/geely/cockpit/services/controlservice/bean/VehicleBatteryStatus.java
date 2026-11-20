package com.geely.cockpit.services.controlservice.bean;

/* JADX INFO: compiled from: PlatformVehicleStatus.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000!\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\u000b\n\u0003\b\u0093\u0001\b\u0086\b\u0018\u00002\u00020\u0001BÃ\u0003\b\u0007\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0013\u0012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0013\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u001f\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\"\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\u0013\u0012\n\b\u0002\u0010$\u001a\u0004\u0018\u00010\u0013\u0012\n\b\u0002\u0010%\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010&\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010'\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010(\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010)\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010*J\u0010\u0010{\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u00105J\u000b\u0010|\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010}\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010~\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010\u007f\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u0080\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u0011\u0010\u0081\u0001\u001a\u0004\u0018\u00010\u0013HÆ\u0003¢\u0006\u0002\u0010,J\u0011\u0010\u0082\u0001\u001a\u0004\u0018\u00010\u0013HÆ\u0003¢\u0006\u0002\u0010,J\u0011\u0010\u0083\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u00105J\f\u0010\u0084\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u0011\u0010\u0085\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u00105J\f\u0010\u0086\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u0087\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u0088\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u0089\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u008a\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u008b\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u008c\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u008d\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u008e\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u008f\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u0090\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u0091\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u0092\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u0011\u0010\u0093\u0001\u001a\u0004\u0018\u00010\u0013HÆ\u0003¢\u0006\u0002\u0010,J\u0011\u0010\u0094\u0001\u001a\u0004\u0018\u00010\u0013HÆ\u0003¢\u0006\u0002\u0010,J\f\u0010\u0095\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u0096\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u0097\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u0098\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u0099\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u0011\u0010\u009a\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u00105J\f\u0010\u009b\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u0011\u0010\u009c\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u00105J\f\u0010\u009d\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u009e\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\f\u0010\u009f\u0001\u001a\u0004\u0018\u00010\u0005HÆ\u0003JÌ\u0003\u0010 \u0001\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00132\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00132\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u001f\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\"\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\u00132\n\b\u0002\u0010$\u001a\u0004\u0018\u00010\u00132\n\b\u0002\u0010%\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010&\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010'\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010(\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010)\u001a\u0004\u0018\u00010\u0005HÆ\u0001¢\u0006\u0003\u0010¡\u0001J\u0015\u0010¢\u0001\u001a\u00020\u00132\t\u0010£\u0001\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\n\u0010¤\u0001\u001a\u00020\u0003HÖ\u0001J\n\u0010¥\u0001\u001a\u00020\u0005HÖ\u0001R\u001e\u0010#\u001a\u0004\u0018\u00010\u0013X\u0086\u000e¢\u0006\u0010\n\u0002\u0010/\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u001c\u0010\u001b\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b0\u00101\"\u0004\b2\u00103R\u001e\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u0010\n\u0002\u00108\u001a\u0004\b4\u00105\"\u0004\b6\u00107R\u001c\u0010\u001f\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b9\u00101\"\u0004\b:\u00103R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b;\u00101\"\u0004\b<\u00103R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b=\u00101\"\u0004\b>\u00103R\u001e\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u0010\n\u0002\u00108\u001a\u0004\b?\u00105\"\u0004\b@\u00107R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bA\u00101\"\u0004\bB\u00103R\u001e\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u0010\n\u0002\u00108\u001a\u0004\bC\u00105\"\u0004\bD\u00107R\u001c\u0010(\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bE\u00101\"\u0004\bF\u00103R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bG\u00101\"\u0004\bH\u00103R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bI\u00101\"\u0004\bJ\u00103R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bK\u00101\"\u0004\bL\u00103R\u001c\u0010\r\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bM\u00101\"\u0004\bN\u00103R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bO\u00101\"\u0004\bP\u00103R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bQ\u00101\"\u0004\bR\u00103R\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bS\u00101\"\u0004\bT\u00103R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bU\u00101\"\u0004\bV\u00103R\u001c\u0010%\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bW\u00101\"\u0004\bX\u00103R\u001c\u0010\u001e\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bY\u00101\"\u0004\bZ\u00103R\u001c\u0010!\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b[\u00101\"\u0004\b\\\u00103R\u001c\u0010\"\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b]\u00101\"\u0004\b^\u00103R\u001e\u0010$\u001a\u0004\u0018\u00010\u0013X\u0086\u000e¢\u0006\u0010\n\u0002\u0010/\u001a\u0004\b_\u0010,\"\u0004\b`\u0010.R\u001c\u0010 \u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\ba\u00101\"\u0004\bb\u00103R\u001e\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0086\u000e¢\u0006\u0010\n\u0002\u0010/\u001a\u0004\b\u0012\u0010,\"\u0004\bc\u0010.R\u001e\u0010\u0014\u001a\u0004\u0018\u00010\u0013X\u0086\u000e¢\u0006\u0010\n\u0002\u0010/\u001a\u0004\b\u0014\u0010,\"\u0004\bd\u0010.R\u001c\u0010'\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\be\u00101\"\u0004\bf\u00103R\u001c\u0010&\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bg\u00101\"\u0004\bh\u00103R\u001c\u0010)\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bi\u00101\"\u0004\bj\u00103R\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bk\u00101\"\u0004\bl\u00103R\u001e\u0010\u0015\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u0010\n\u0002\u00108\u001a\u0004\bm\u00105\"\u0004\bn\u00107R\u001c\u0010\u0016\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bo\u00101\"\u0004\bp\u00103R\u001e\u0010\u0017\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u0010\n\u0002\u00108\u001a\u0004\bq\u00105\"\u0004\br\u00107R\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bs\u00101\"\u0004\bt\u00103R\u001c\u0010\u0019\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bu\u00101\"\u0004\bv\u00103R\u001c\u0010\u001c\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bw\u00101\"\u0004\bx\u00103R\u001c\u0010\u001a\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\by\u00101\"\u0004\bz\u00103¨\u0006¦\u0001"}, d2 = {"Lcom/geely/cockpit/services/controlservice/bean/VehicleBatteryStatus;", "", "bookChargeSts", "", "chargeIAct", "", "chargeLevel", "chargeSts", "chargeUAct", "chargerState", "dcChargeIAct", "dcChargeSts", "dcDcActvd", "dcDcConnectStatus", "disChargeConnectStatus", "disChargeIAct", "disChargeSts", "disChargeUAct", "isCharging", "", "isPluggedIn", "stateOfCharge", "stateOfHealth", "statusOfChargerConnection", "timeToFullyCharged", "timeToTargetDisCharged", "voltage", "bmshChgConnState", "totalConsumptionMileage", "remainingConsumableMileage", "highVoltageBatteryStatus", "cgRemind", "hvTempLevel", "hvBatteryHTMStatus", "hvBatteryOptimizeFlag", "batteryHeatModeActive", "hvBatteryPreHeatingActive", "fuelLowWarning", "megaWattCharingStatus", "megaWattCharingHint", "chargingPileType", "powerLowStatus", "(Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getBatteryHeatModeActive", "()Ljava/lang/Boolean;", "setBatteryHeatModeActive", "(Ljava/lang/Boolean;)V", "Ljava/lang/Boolean;", "getBmshChgConnState", "()Ljava/lang/String;", "setBmshChgConnState", "(Ljava/lang/String;)V", "getBookChargeSts", "()Ljava/lang/Integer;", "setBookChargeSts", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "getCgRemind", "setCgRemind", "getChargeIAct", "setChargeIAct", "getChargeLevel", "setChargeLevel", "getChargeSts", "setChargeSts", "getChargeUAct", "setChargeUAct", "getChargerState", "setChargerState", "getChargingPileType", "setChargingPileType", "getDcChargeIAct", "setDcChargeIAct", "getDcChargeSts", "setDcChargeSts", "getDcDcActvd", "setDcDcActvd", "getDcDcConnectStatus", "setDcDcConnectStatus", "getDisChargeConnectStatus", "setDisChargeConnectStatus", "getDisChargeIAct", "setDisChargeIAct", "getDisChargeSts", "setDisChargeSts", "getDisChargeUAct", "setDisChargeUAct", "getFuelLowWarning", "setFuelLowWarning", "getHighVoltageBatteryStatus", "setHighVoltageBatteryStatus", "getHvBatteryHTMStatus", "setHvBatteryHTMStatus", "getHvBatteryOptimizeFlag", "setHvBatteryOptimizeFlag", "getHvBatteryPreHeatingActive", "setHvBatteryPreHeatingActive", "getHvTempLevel", "setHvTempLevel", "setCharging", "setPluggedIn", "getMegaWattCharingHint", "setMegaWattCharingHint", "getMegaWattCharingStatus", "setMegaWattCharingStatus", "getPowerLowStatus", "setPowerLowStatus", "getRemainingConsumableMileage", "setRemainingConsumableMileage", "getStateOfCharge", "setStateOfCharge", "getStateOfHealth", "setStateOfHealth", "getStatusOfChargerConnection", "setStatusOfChargerConnection", "getTimeToFullyCharged", "setTimeToFullyCharged", "getTimeToTargetDisCharged", "setTimeToTargetDisCharged", "getTotalConsumptionMileage", "setTotalConsumptionMileage", "getVoltage", "setVoltage", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component29", "component3", "component30", "component31", "component32", "component33", "component34", "component35", "component36", "component37", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/geely/cockpit/services/controlservice/bean/VehicleBatteryStatus;", "equals", "other", "hashCode", "toString", "LibCarControlNew_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final /* data */ class VehicleBatteryStatus {
    private java.lang.Boolean batteryHeatModeActive;
    private java.lang.String bmshChgConnState;
    private java.lang.Integer bookChargeSts;
    private java.lang.String cgRemind;
    private java.lang.String chargeIAct;
    private java.lang.String chargeLevel;
    private java.lang.Integer chargeSts;
    private java.lang.String chargeUAct;
    private java.lang.Integer chargerState;
    private java.lang.String chargingPileType;
    private java.lang.String dcChargeIAct;
    private java.lang.String dcChargeSts;
    private java.lang.String dcDcActvd;
    private java.lang.String dcDcConnectStatus;
    private java.lang.String disChargeConnectStatus;
    private java.lang.String disChargeIAct;
    private java.lang.String disChargeSts;
    private java.lang.String disChargeUAct;
    private java.lang.String fuelLowWarning;
    private java.lang.String highVoltageBatteryStatus;
    private java.lang.String hvBatteryHTMStatus;
    private java.lang.String hvBatteryOptimizeFlag;
    private java.lang.Boolean hvBatteryPreHeatingActive;
    private java.lang.String hvTempLevel;
    private java.lang.Boolean isCharging;
    private java.lang.Boolean isPluggedIn;
    private java.lang.String megaWattCharingHint;
    private java.lang.String megaWattCharingStatus;
    private java.lang.String powerLowStatus;
    private java.lang.String remainingConsumableMileage;
    private java.lang.Integer stateOfCharge;
    private java.lang.String stateOfHealth;
    private java.lang.Integer statusOfChargerConnection;
    private java.lang.String timeToFullyCharged;
    private java.lang.String timeToTargetDisCharged;
    private java.lang.String totalConsumptionMileage;
    private java.lang.String voltage;

    public VehicleBatteryStatus() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -1, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num) {
        this(num, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -2, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str) {
        this(num, str, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -4, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2) {
        this(num, str, str2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -8, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2) {
        this(num, str, str2, num2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -16, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3) {
        this(num, str, str2, num2, str3, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -32, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3) {
        this(num, str, str2, num2, str3, num3, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -64, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4) {
        this(num, str, str2, num2, str3, num3, str4, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -128, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5) {
        this(num, str, str2, num2, str3, num3, str4, str5, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -256, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -512, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -1024, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -2048, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -4096, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -8192, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -16384, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -32768, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -65536, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -131072, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -262144, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -524288, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -1048576, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -2097152, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -4194304, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, str16, null, null, null, null, null, null, null, null, null, null, null, null, null, null, -8388608, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, str16, str17, null, null, null, null, null, null, null, null, null, null, null, null, null, -16777216, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, str16, str17, str18, null, null, null, null, null, null, null, null, null, null, null, null, -33554432, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18, java.lang.String str19) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, str16, str17, str18, str19, null, null, null, null, null, null, null, null, null, null, null, -67108864, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18, java.lang.String str19, java.lang.String str20) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, str16, str17, str18, str19, str20, null, null, null, null, null, null, null, null, null, null, -134217728, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18, java.lang.String str19, java.lang.String str20, java.lang.String str21) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, str16, str17, str18, str19, str20, str21, null, null, null, null, null, null, null, null, null, -268435456, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18, java.lang.String str19, java.lang.String str20, java.lang.String str21, java.lang.String str22) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, str16, str17, str18, str19, str20, str21, str22, null, null, null, null, null, null, null, null, -536870912, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18, java.lang.String str19, java.lang.String str20, java.lang.String str21, java.lang.String str22, java.lang.String str23) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, str16, str17, str18, str19, str20, str21, str22, str23, null, null, null, null, null, null, null, -1073741824, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18, java.lang.String str19, java.lang.String str20, java.lang.String str21, java.lang.String str22, java.lang.String str23, java.lang.Boolean bool3) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, str16, str17, str18, str19, str20, str21, str22, str23, bool3, null, null, null, null, null, null, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18, java.lang.String str19, java.lang.String str20, java.lang.String str21, java.lang.String str22, java.lang.String str23, java.lang.Boolean bool3, java.lang.Boolean bool4) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, str16, str17, str18, str19, str20, str21, str22, str23, bool3, bool4, null, null, null, null, null, 0, 31, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18, java.lang.String str19, java.lang.String str20, java.lang.String str21, java.lang.String str22, java.lang.String str23, java.lang.Boolean bool3, java.lang.Boolean bool4, java.lang.String str24) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, str16, str17, str18, str19, str20, str21, str22, str23, bool3, bool4, str24, null, null, null, null, 0, 30, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18, java.lang.String str19, java.lang.String str20, java.lang.String str21, java.lang.String str22, java.lang.String str23, java.lang.Boolean bool3, java.lang.Boolean bool4, java.lang.String str24, java.lang.String str25) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, str16, str17, str18, str19, str20, str21, str22, str23, bool3, bool4, str24, str25, null, null, null, 0, 28, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18, java.lang.String str19, java.lang.String str20, java.lang.String str21, java.lang.String str22, java.lang.String str23, java.lang.Boolean bool3, java.lang.Boolean bool4, java.lang.String str24, java.lang.String str25, java.lang.String str26) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, str16, str17, str18, str19, str20, str21, str22, str23, bool3, bool4, str24, str25, str26, null, null, 0, 24, null);
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18, java.lang.String str19, java.lang.String str20, java.lang.String str21, java.lang.String str22, java.lang.String str23, java.lang.Boolean bool3, java.lang.Boolean bool4, java.lang.String str24, java.lang.String str25, java.lang.String str26, java.lang.String str27) {
        this(num, str, str2, num2, str3, num3, str4, str5, str6, str7, str8, str9, str10, str11, bool, bool2, num4, str12, num5, str13, str14, str15, str16, str17, str18, str19, str20, str21, str22, str23, bool3, bool4, str24, str25, str26, str27, null, 0, 16, null);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final java.lang.Integer getBookChargeSts() {
        return this.bookChargeSts;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final java.lang.String getDcDcConnectStatus() {
        return this.dcDcConnectStatus;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final java.lang.String getDisChargeConnectStatus() {
        return this.disChargeConnectStatus;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final java.lang.String getDisChargeIAct() {
        return this.disChargeIAct;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final java.lang.String getDisChargeSts() {
        return this.disChargeSts;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final java.lang.String getDisChargeUAct() {
        return this.disChargeUAct;
    }

    /* JADX INFO: renamed from: component15, reason: from getter */
    public final java.lang.Boolean getIsCharging() {
        return this.isCharging;
    }

    /* JADX INFO: renamed from: component16, reason: from getter */
    public final java.lang.Boolean getIsPluggedIn() {
        return this.isPluggedIn;
    }

    /* JADX INFO: renamed from: component17, reason: from getter */
    public final java.lang.Integer getStateOfCharge() {
        return this.stateOfCharge;
    }

    /* JADX INFO: renamed from: component18, reason: from getter */
    public final java.lang.String getStateOfHealth() {
        return this.stateOfHealth;
    }

    /* JADX INFO: renamed from: component19, reason: from getter */
    public final java.lang.Integer getStatusOfChargerConnection() {
        return this.statusOfChargerConnection;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final java.lang.String getChargeIAct() {
        return this.chargeIAct;
    }

    /* JADX INFO: renamed from: component20, reason: from getter */
    public final java.lang.String getTimeToFullyCharged() {
        return this.timeToFullyCharged;
    }

    /* JADX INFO: renamed from: component21, reason: from getter */
    public final java.lang.String getTimeToTargetDisCharged() {
        return this.timeToTargetDisCharged;
    }

    /* JADX INFO: renamed from: component22, reason: from getter */
    public final java.lang.String getVoltage() {
        return this.voltage;
    }

    /* JADX INFO: renamed from: component23, reason: from getter */
    public final java.lang.String getBmshChgConnState() {
        return this.bmshChgConnState;
    }

    /* JADX INFO: renamed from: component24, reason: from getter */
    public final java.lang.String getTotalConsumptionMileage() {
        return this.totalConsumptionMileage;
    }

    /* JADX INFO: renamed from: component25, reason: from getter */
    public final java.lang.String getRemainingConsumableMileage() {
        return this.remainingConsumableMileage;
    }

    /* JADX INFO: renamed from: component26, reason: from getter */
    public final java.lang.String getHighVoltageBatteryStatus() {
        return this.highVoltageBatteryStatus;
    }

    /* JADX INFO: renamed from: component27, reason: from getter */
    public final java.lang.String getCgRemind() {
        return this.cgRemind;
    }

    /* JADX INFO: renamed from: component28, reason: from getter */
    public final java.lang.String getHvTempLevel() {
        return this.hvTempLevel;
    }

    /* JADX INFO: renamed from: component29, reason: from getter */
    public final java.lang.String getHvBatteryHTMStatus() {
        return this.hvBatteryHTMStatus;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final java.lang.String getChargeLevel() {
        return this.chargeLevel;
    }

    /* JADX INFO: renamed from: component30, reason: from getter */
    public final java.lang.String getHvBatteryOptimizeFlag() {
        return this.hvBatteryOptimizeFlag;
    }

    /* JADX INFO: renamed from: component31, reason: from getter */
    public final java.lang.Boolean getBatteryHeatModeActive() {
        return this.batteryHeatModeActive;
    }

    /* JADX INFO: renamed from: component32, reason: from getter */
    public final java.lang.Boolean getHvBatteryPreHeatingActive() {
        return this.hvBatteryPreHeatingActive;
    }

    /* JADX INFO: renamed from: component33, reason: from getter */
    public final java.lang.String getFuelLowWarning() {
        return this.fuelLowWarning;
    }

    /* JADX INFO: renamed from: component34, reason: from getter */
    public final java.lang.String getMegaWattCharingStatus() {
        return this.megaWattCharingStatus;
    }

    /* JADX INFO: renamed from: component35, reason: from getter */
    public final java.lang.String getMegaWattCharingHint() {
        return this.megaWattCharingHint;
    }

    /* JADX INFO: renamed from: component36, reason: from getter */
    public final java.lang.String getChargingPileType() {
        return this.chargingPileType;
    }

    /* JADX INFO: renamed from: component37, reason: from getter */
    public final java.lang.String getPowerLowStatus() {
        return this.powerLowStatus;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final java.lang.Integer getChargeSts() {
        return this.chargeSts;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final java.lang.String getChargeUAct() {
        return this.chargeUAct;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final java.lang.Integer getChargerState() {
        return this.chargerState;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final java.lang.String getDcChargeIAct() {
        return this.dcChargeIAct;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final java.lang.String getDcChargeSts() {
        return this.dcChargeSts;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final java.lang.String getDcDcActvd() {
        return this.dcDcActvd;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleBatteryStatus copy(java.lang.Integer bookChargeSts, java.lang.String chargeIAct, java.lang.String chargeLevel, java.lang.Integer chargeSts, java.lang.String chargeUAct, java.lang.Integer chargerState, java.lang.String dcChargeIAct, java.lang.String dcChargeSts, java.lang.String dcDcActvd, java.lang.String dcDcConnectStatus, java.lang.String disChargeConnectStatus, java.lang.String disChargeIAct, java.lang.String disChargeSts, java.lang.String disChargeUAct, java.lang.Boolean isCharging, java.lang.Boolean isPluggedIn, java.lang.Integer stateOfCharge, java.lang.String stateOfHealth, java.lang.Integer statusOfChargerConnection, java.lang.String timeToFullyCharged, java.lang.String timeToTargetDisCharged, java.lang.String voltage, java.lang.String bmshChgConnState, java.lang.String totalConsumptionMileage, java.lang.String remainingConsumableMileage, java.lang.String highVoltageBatteryStatus, java.lang.String cgRemind, java.lang.String hvTempLevel, java.lang.String hvBatteryHTMStatus, java.lang.String hvBatteryOptimizeFlag, java.lang.Boolean batteryHeatModeActive, java.lang.Boolean hvBatteryPreHeatingActive, java.lang.String fuelLowWarning, java.lang.String megaWattCharingStatus, java.lang.String megaWattCharingHint, java.lang.String chargingPileType, java.lang.String powerLowStatus) {
        return new com.geely.cockpit.services.controlservice.bean.VehicleBatteryStatus(bookChargeSts, chargeIAct, chargeLevel, chargeSts, chargeUAct, chargerState, dcChargeIAct, dcChargeSts, dcDcActvd, dcDcConnectStatus, disChargeConnectStatus, disChargeIAct, disChargeSts, disChargeUAct, isCharging, isPluggedIn, stateOfCharge, stateOfHealth, statusOfChargerConnection, timeToFullyCharged, timeToTargetDisCharged, voltage, bmshChgConnState, totalConsumptionMileage, remainingConsumableMileage, highVoltageBatteryStatus, cgRemind, hvTempLevel, hvBatteryHTMStatus, hvBatteryOptimizeFlag, batteryHeatModeActive, hvBatteryPreHeatingActive, fuelLowWarning, megaWattCharingStatus, megaWattCharingHint, chargingPileType, powerLowStatus);
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof com.geely.cockpit.services.controlservice.bean.VehicleBatteryStatus)) {
            return false;
        }
        com.geely.cockpit.services.controlservice.bean.VehicleBatteryStatus vehicleBatteryStatus = (com.geely.cockpit.services.controlservice.bean.VehicleBatteryStatus) other;
        return kotlin.jvm.internal.Intrinsics.areEqual(this.bookChargeSts, vehicleBatteryStatus.bookChargeSts) && kotlin.jvm.internal.Intrinsics.areEqual(this.chargeIAct, vehicleBatteryStatus.chargeIAct) && kotlin.jvm.internal.Intrinsics.areEqual(this.chargeLevel, vehicleBatteryStatus.chargeLevel) && kotlin.jvm.internal.Intrinsics.areEqual(this.chargeSts, vehicleBatteryStatus.chargeSts) && kotlin.jvm.internal.Intrinsics.areEqual(this.chargeUAct, vehicleBatteryStatus.chargeUAct) && kotlin.jvm.internal.Intrinsics.areEqual(this.chargerState, vehicleBatteryStatus.chargerState) && kotlin.jvm.internal.Intrinsics.areEqual(this.dcChargeIAct, vehicleBatteryStatus.dcChargeIAct) && kotlin.jvm.internal.Intrinsics.areEqual(this.dcChargeSts, vehicleBatteryStatus.dcChargeSts) && kotlin.jvm.internal.Intrinsics.areEqual(this.dcDcActvd, vehicleBatteryStatus.dcDcActvd) && kotlin.jvm.internal.Intrinsics.areEqual(this.dcDcConnectStatus, vehicleBatteryStatus.dcDcConnectStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.disChargeConnectStatus, vehicleBatteryStatus.disChargeConnectStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.disChargeIAct, vehicleBatteryStatus.disChargeIAct) && kotlin.jvm.internal.Intrinsics.areEqual(this.disChargeSts, vehicleBatteryStatus.disChargeSts) && kotlin.jvm.internal.Intrinsics.areEqual(this.disChargeUAct, vehicleBatteryStatus.disChargeUAct) && kotlin.jvm.internal.Intrinsics.areEqual(this.isCharging, vehicleBatteryStatus.isCharging) && kotlin.jvm.internal.Intrinsics.areEqual(this.isPluggedIn, vehicleBatteryStatus.isPluggedIn) && kotlin.jvm.internal.Intrinsics.areEqual(this.stateOfCharge, vehicleBatteryStatus.stateOfCharge) && kotlin.jvm.internal.Intrinsics.areEqual(this.stateOfHealth, vehicleBatteryStatus.stateOfHealth) && kotlin.jvm.internal.Intrinsics.areEqual(this.statusOfChargerConnection, vehicleBatteryStatus.statusOfChargerConnection) && kotlin.jvm.internal.Intrinsics.areEqual(this.timeToFullyCharged, vehicleBatteryStatus.timeToFullyCharged) && kotlin.jvm.internal.Intrinsics.areEqual(this.timeToTargetDisCharged, vehicleBatteryStatus.timeToTargetDisCharged) && kotlin.jvm.internal.Intrinsics.areEqual(this.voltage, vehicleBatteryStatus.voltage) && kotlin.jvm.internal.Intrinsics.areEqual(this.bmshChgConnState, vehicleBatteryStatus.bmshChgConnState) && kotlin.jvm.internal.Intrinsics.areEqual(this.totalConsumptionMileage, vehicleBatteryStatus.totalConsumptionMileage) && kotlin.jvm.internal.Intrinsics.areEqual(this.remainingConsumableMileage, vehicleBatteryStatus.remainingConsumableMileage) && kotlin.jvm.internal.Intrinsics.areEqual(this.highVoltageBatteryStatus, vehicleBatteryStatus.highVoltageBatteryStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.cgRemind, vehicleBatteryStatus.cgRemind) && kotlin.jvm.internal.Intrinsics.areEqual(this.hvTempLevel, vehicleBatteryStatus.hvTempLevel) && kotlin.jvm.internal.Intrinsics.areEqual(this.hvBatteryHTMStatus, vehicleBatteryStatus.hvBatteryHTMStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.hvBatteryOptimizeFlag, vehicleBatteryStatus.hvBatteryOptimizeFlag) && kotlin.jvm.internal.Intrinsics.areEqual(this.batteryHeatModeActive, vehicleBatteryStatus.batteryHeatModeActive) && kotlin.jvm.internal.Intrinsics.areEqual(this.hvBatteryPreHeatingActive, vehicleBatteryStatus.hvBatteryPreHeatingActive) && kotlin.jvm.internal.Intrinsics.areEqual(this.fuelLowWarning, vehicleBatteryStatus.fuelLowWarning) && kotlin.jvm.internal.Intrinsics.areEqual(this.megaWattCharingStatus, vehicleBatteryStatus.megaWattCharingStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.megaWattCharingHint, vehicleBatteryStatus.megaWattCharingHint) && kotlin.jvm.internal.Intrinsics.areEqual(this.chargingPileType, vehicleBatteryStatus.chargingPileType) && kotlin.jvm.internal.Intrinsics.areEqual(this.powerLowStatus, vehicleBatteryStatus.powerLowStatus);
    }

    public int hashCode() {
        java.lang.Integer num = this.bookChargeSts;
        int iHashCode = (num == null ? 0 : num.hashCode()) * 31;
        java.lang.String str = this.chargeIAct;
        int iHashCode2 = (iHashCode + (str == null ? 0 : str.hashCode())) * 31;
        java.lang.String str2 = this.chargeLevel;
        int iHashCode3 = (iHashCode2 + (str2 == null ? 0 : str2.hashCode())) * 31;
        java.lang.Integer num2 = this.chargeSts;
        int iHashCode4 = (iHashCode3 + (num2 == null ? 0 : num2.hashCode())) * 31;
        java.lang.String str3 = this.chargeUAct;
        int iHashCode5 = (iHashCode4 + (str3 == null ? 0 : str3.hashCode())) * 31;
        java.lang.Integer num3 = this.chargerState;
        int iHashCode6 = (iHashCode5 + (num3 == null ? 0 : num3.hashCode())) * 31;
        java.lang.String str4 = this.dcChargeIAct;
        int iHashCode7 = (iHashCode6 + (str4 == null ? 0 : str4.hashCode())) * 31;
        java.lang.String str5 = this.dcChargeSts;
        int iHashCode8 = (iHashCode7 + (str5 == null ? 0 : str5.hashCode())) * 31;
        java.lang.String str6 = this.dcDcActvd;
        int iHashCode9 = (iHashCode8 + (str6 == null ? 0 : str6.hashCode())) * 31;
        java.lang.String str7 = this.dcDcConnectStatus;
        int iHashCode10 = (iHashCode9 + (str7 == null ? 0 : str7.hashCode())) * 31;
        java.lang.String str8 = this.disChargeConnectStatus;
        int iHashCode11 = (iHashCode10 + (str8 == null ? 0 : str8.hashCode())) * 31;
        java.lang.String str9 = this.disChargeIAct;
        int iHashCode12 = (iHashCode11 + (str9 == null ? 0 : str9.hashCode())) * 31;
        java.lang.String str10 = this.disChargeSts;
        int iHashCode13 = (iHashCode12 + (str10 == null ? 0 : str10.hashCode())) * 31;
        java.lang.String str11 = this.disChargeUAct;
        int iHashCode14 = (iHashCode13 + (str11 == null ? 0 : str11.hashCode())) * 31;
        java.lang.Boolean bool = this.isCharging;
        int iHashCode15 = (iHashCode14 + (bool == null ? 0 : bool.hashCode())) * 31;
        java.lang.Boolean bool2 = this.isPluggedIn;
        int iHashCode16 = (iHashCode15 + (bool2 == null ? 0 : bool2.hashCode())) * 31;
        java.lang.Integer num4 = this.stateOfCharge;
        int iHashCode17 = (iHashCode16 + (num4 == null ? 0 : num4.hashCode())) * 31;
        java.lang.String str12 = this.stateOfHealth;
        int iHashCode18 = (iHashCode17 + (str12 == null ? 0 : str12.hashCode())) * 31;
        java.lang.Integer num5 = this.statusOfChargerConnection;
        int iHashCode19 = (iHashCode18 + (num5 == null ? 0 : num5.hashCode())) * 31;
        java.lang.String str13 = this.timeToFullyCharged;
        int iHashCode20 = (iHashCode19 + (str13 == null ? 0 : str13.hashCode())) * 31;
        java.lang.String str14 = this.timeToTargetDisCharged;
        int iHashCode21 = (iHashCode20 + (str14 == null ? 0 : str14.hashCode())) * 31;
        java.lang.String str15 = this.voltage;
        int iHashCode22 = (iHashCode21 + (str15 == null ? 0 : str15.hashCode())) * 31;
        java.lang.String str16 = this.bmshChgConnState;
        int iHashCode23 = (iHashCode22 + (str16 == null ? 0 : str16.hashCode())) * 31;
        java.lang.String str17 = this.totalConsumptionMileage;
        int iHashCode24 = (iHashCode23 + (str17 == null ? 0 : str17.hashCode())) * 31;
        java.lang.String str18 = this.remainingConsumableMileage;
        int iHashCode25 = (iHashCode24 + (str18 == null ? 0 : str18.hashCode())) * 31;
        java.lang.String str19 = this.highVoltageBatteryStatus;
        int iHashCode26 = (iHashCode25 + (str19 == null ? 0 : str19.hashCode())) * 31;
        java.lang.String str20 = this.cgRemind;
        int iHashCode27 = (iHashCode26 + (str20 == null ? 0 : str20.hashCode())) * 31;
        java.lang.String str21 = this.hvTempLevel;
        int iHashCode28 = (iHashCode27 + (str21 == null ? 0 : str21.hashCode())) * 31;
        java.lang.String str22 = this.hvBatteryHTMStatus;
        int iHashCode29 = (iHashCode28 + (str22 == null ? 0 : str22.hashCode())) * 31;
        java.lang.String str23 = this.hvBatteryOptimizeFlag;
        int iHashCode30 = (iHashCode29 + (str23 == null ? 0 : str23.hashCode())) * 31;
        java.lang.Boolean bool3 = this.batteryHeatModeActive;
        int iHashCode31 = (iHashCode30 + (bool3 == null ? 0 : bool3.hashCode())) * 31;
        java.lang.Boolean bool4 = this.hvBatteryPreHeatingActive;
        int iHashCode32 = (iHashCode31 + (bool4 == null ? 0 : bool4.hashCode())) * 31;
        java.lang.String str24 = this.fuelLowWarning;
        int iHashCode33 = (iHashCode32 + (str24 == null ? 0 : str24.hashCode())) * 31;
        java.lang.String str25 = this.megaWattCharingStatus;
        int iHashCode34 = (iHashCode33 + (str25 == null ? 0 : str25.hashCode())) * 31;
        java.lang.String str26 = this.megaWattCharingHint;
        int iHashCode35 = (iHashCode34 + (str26 == null ? 0 : str26.hashCode())) * 31;
        java.lang.String str27 = this.chargingPileType;
        int iHashCode36 = (iHashCode35 + (str27 == null ? 0 : str27.hashCode())) * 31;
        java.lang.String str28 = this.powerLowStatus;
        return iHashCode36 + (str28 != null ? str28.hashCode() : 0);
    }

    public java.lang.String toString() {
        return "VehicleBatteryStatus(bookChargeSts=" + this.bookChargeSts + ", chargeIAct=" + this.chargeIAct + ", chargeLevel=" + this.chargeLevel + ", chargeSts=" + this.chargeSts + ", chargeUAct=" + this.chargeUAct + ", chargerState=" + this.chargerState + ", dcChargeIAct=" + this.dcChargeIAct + ", dcChargeSts=" + this.dcChargeSts + ", dcDcActvd=" + this.dcDcActvd + ", dcDcConnectStatus=" + this.dcDcConnectStatus + ", disChargeConnectStatus=" + this.disChargeConnectStatus + ", disChargeIAct=" + this.disChargeIAct + ", disChargeSts=" + this.disChargeSts + ", disChargeUAct=" + this.disChargeUAct + ", isCharging=" + this.isCharging + ", isPluggedIn=" + this.isPluggedIn + ", stateOfCharge=" + this.stateOfCharge + ", stateOfHealth=" + this.stateOfHealth + ", statusOfChargerConnection=" + this.statusOfChargerConnection + ", timeToFullyCharged=" + this.timeToFullyCharged + ", timeToTargetDisCharged=" + this.timeToTargetDisCharged + ", voltage=" + this.voltage + ", bmshChgConnState=" + this.bmshChgConnState + ", totalConsumptionMileage=" + this.totalConsumptionMileage + ", remainingConsumableMileage=" + this.remainingConsumableMileage + ", highVoltageBatteryStatus=" + this.highVoltageBatteryStatus + ", cgRemind=" + this.cgRemind + ", hvTempLevel=" + this.hvTempLevel + ", hvBatteryHTMStatus=" + this.hvBatteryHTMStatus + ", hvBatteryOptimizeFlag=" + this.hvBatteryOptimizeFlag + ", batteryHeatModeActive=" + this.batteryHeatModeActive + ", hvBatteryPreHeatingActive=" + this.hvBatteryPreHeatingActive + ", fuelLowWarning=" + this.fuelLowWarning + ", megaWattCharingStatus=" + this.megaWattCharingStatus + ", megaWattCharingHint=" + this.megaWattCharingHint + ", chargingPileType=" + this.chargingPileType + ", powerLowStatus=" + this.powerLowStatus + ")";
    }

    public VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18, java.lang.String str19, java.lang.String str20, java.lang.String str21, java.lang.String str22, java.lang.String str23, java.lang.Boolean bool3, java.lang.Boolean bool4, java.lang.String str24, java.lang.String str25, java.lang.String str26, java.lang.String str27, java.lang.String str28) {
        this.bookChargeSts = num;
        this.chargeIAct = str;
        this.chargeLevel = str2;
        this.chargeSts = num2;
        this.chargeUAct = str3;
        this.chargerState = num3;
        this.dcChargeIAct = str4;
        this.dcChargeSts = str5;
        this.dcDcActvd = str6;
        this.dcDcConnectStatus = str7;
        this.disChargeConnectStatus = str8;
        this.disChargeIAct = str9;
        this.disChargeSts = str10;
        this.disChargeUAct = str11;
        this.isCharging = bool;
        this.isPluggedIn = bool2;
        this.stateOfCharge = num4;
        this.stateOfHealth = str12;
        this.statusOfChargerConnection = num5;
        this.timeToFullyCharged = str13;
        this.timeToTargetDisCharged = str14;
        this.voltage = str15;
        this.bmshChgConnState = str16;
        this.totalConsumptionMileage = str17;
        this.remainingConsumableMileage = str18;
        this.highVoltageBatteryStatus = str19;
        this.cgRemind = str20;
        this.hvTempLevel = str21;
        this.hvBatteryHTMStatus = str22;
        this.hvBatteryOptimizeFlag = str23;
        this.batteryHeatModeActive = bool3;
        this.hvBatteryPreHeatingActive = bool4;
        this.fuelLowWarning = str24;
        this.megaWattCharingStatus = str25;
        this.megaWattCharingHint = str26;
        this.chargingPileType = str27;
        this.powerLowStatus = str28;
    }

    public final java.lang.Integer getBookChargeSts() {
        return this.bookChargeSts;
    }

    public final void setBookChargeSts(java.lang.Integer num) {
        this.bookChargeSts = num;
    }

    public final java.lang.String getChargeIAct() {
        return this.chargeIAct;
    }

    public final void setChargeIAct(java.lang.String str) {
        this.chargeIAct = str;
    }

    public final java.lang.String getChargeLevel() {
        return this.chargeLevel;
    }

    public final void setChargeLevel(java.lang.String str) {
        this.chargeLevel = str;
    }

    public final java.lang.Integer getChargeSts() {
        return this.chargeSts;
    }

    public final void setChargeSts(java.lang.Integer num) {
        this.chargeSts = num;
    }

    public final java.lang.String getChargeUAct() {
        return this.chargeUAct;
    }

    public final void setChargeUAct(java.lang.String str) {
        this.chargeUAct = str;
    }

    public final java.lang.Integer getChargerState() {
        return this.chargerState;
    }

    public final void setChargerState(java.lang.Integer num) {
        this.chargerState = num;
    }

    public final java.lang.String getDcChargeIAct() {
        return this.dcChargeIAct;
    }

    public final void setDcChargeIAct(java.lang.String str) {
        this.dcChargeIAct = str;
    }

    public final java.lang.String getDcChargeSts() {
        return this.dcChargeSts;
    }

    public final void setDcChargeSts(java.lang.String str) {
        this.dcChargeSts = str;
    }

    public final java.lang.String getDcDcActvd() {
        return this.dcDcActvd;
    }

    public final void setDcDcActvd(java.lang.String str) {
        this.dcDcActvd = str;
    }

    public final java.lang.String getDcDcConnectStatus() {
        return this.dcDcConnectStatus;
    }

    public final void setDcDcConnectStatus(java.lang.String str) {
        this.dcDcConnectStatus = str;
    }

    public final java.lang.String getDisChargeConnectStatus() {
        return this.disChargeConnectStatus;
    }

    public final void setDisChargeConnectStatus(java.lang.String str) {
        this.disChargeConnectStatus = str;
    }

    public final java.lang.String getDisChargeIAct() {
        return this.disChargeIAct;
    }

    public final void setDisChargeIAct(java.lang.String str) {
        this.disChargeIAct = str;
    }

    public final java.lang.String getDisChargeSts() {
        return this.disChargeSts;
    }

    public final void setDisChargeSts(java.lang.String str) {
        this.disChargeSts = str;
    }

    public final java.lang.String getDisChargeUAct() {
        return this.disChargeUAct;
    }

    public final void setDisChargeUAct(java.lang.String str) {
        this.disChargeUAct = str;
    }

    public final java.lang.Boolean isCharging() {
        return this.isCharging;
    }

    public final void setCharging(java.lang.Boolean bool) {
        this.isCharging = bool;
    }

    public final java.lang.Boolean isPluggedIn() {
        return this.isPluggedIn;
    }

    public final void setPluggedIn(java.lang.Boolean bool) {
        this.isPluggedIn = bool;
    }

    public final java.lang.Integer getStateOfCharge() {
        return this.stateOfCharge;
    }

    public final void setStateOfCharge(java.lang.Integer num) {
        this.stateOfCharge = num;
    }

    public final java.lang.String getStateOfHealth() {
        return this.stateOfHealth;
    }

    public final void setStateOfHealth(java.lang.String str) {
        this.stateOfHealth = str;
    }

    public final java.lang.Integer getStatusOfChargerConnection() {
        return this.statusOfChargerConnection;
    }

    public final void setStatusOfChargerConnection(java.lang.Integer num) {
        this.statusOfChargerConnection = num;
    }

    public final java.lang.String getTimeToFullyCharged() {
        return this.timeToFullyCharged;
    }

    public final void setTimeToFullyCharged(java.lang.String str) {
        this.timeToFullyCharged = str;
    }

    public final java.lang.String getTimeToTargetDisCharged() {
        return this.timeToTargetDisCharged;
    }

    public final void setTimeToTargetDisCharged(java.lang.String str) {
        this.timeToTargetDisCharged = str;
    }

    public final java.lang.String getVoltage() {
        return this.voltage;
    }

    public final void setVoltage(java.lang.String str) {
        this.voltage = str;
    }

    public final java.lang.String getBmshChgConnState() {
        return this.bmshChgConnState;
    }

    public final void setBmshChgConnState(java.lang.String str) {
        this.bmshChgConnState = str;
    }

    public final java.lang.String getTotalConsumptionMileage() {
        return this.totalConsumptionMileage;
    }

    public final void setTotalConsumptionMileage(java.lang.String str) {
        this.totalConsumptionMileage = str;
    }

    public final java.lang.String getRemainingConsumableMileage() {
        return this.remainingConsumableMileage;
    }

    public final void setRemainingConsumableMileage(java.lang.String str) {
        this.remainingConsumableMileage = str;
    }

    public final java.lang.String getHighVoltageBatteryStatus() {
        return this.highVoltageBatteryStatus;
    }

    public final void setHighVoltageBatteryStatus(java.lang.String str) {
        this.highVoltageBatteryStatus = str;
    }

    public final java.lang.String getCgRemind() {
        return this.cgRemind;
    }

    public final void setCgRemind(java.lang.String str) {
        this.cgRemind = str;
    }

    public final java.lang.String getHvTempLevel() {
        return this.hvTempLevel;
    }

    public final void setHvTempLevel(java.lang.String str) {
        this.hvTempLevel = str;
    }

    public final java.lang.String getHvBatteryHTMStatus() {
        return this.hvBatteryHTMStatus;
    }

    public final void setHvBatteryHTMStatus(java.lang.String str) {
        this.hvBatteryHTMStatus = str;
    }

    public final java.lang.String getHvBatteryOptimizeFlag() {
        return this.hvBatteryOptimizeFlag;
    }

    public final void setHvBatteryOptimizeFlag(java.lang.String str) {
        this.hvBatteryOptimizeFlag = str;
    }

    public /* synthetic */ VehicleBatteryStatus(java.lang.Integer num, java.lang.String str, java.lang.String str2, java.lang.Integer num2, java.lang.String str3, java.lang.Integer num3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.Boolean bool, java.lang.Boolean bool2, java.lang.Integer num4, java.lang.String str12, java.lang.Integer num5, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18, java.lang.String str19, java.lang.String str20, java.lang.String str21, java.lang.String str22, java.lang.String str23, java.lang.Boolean bool3, java.lang.Boolean bool4, java.lang.String str24, java.lang.String str25, java.lang.String str26, java.lang.String str27, java.lang.String str28, int i, int i2, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : num, (i & 2) != 0 ? null : str, (i & 4) != 0 ? null : str2, (i & 8) != 0 ? null : num2, (i & 16) != 0 ? null : str3, (i & 32) != 0 ? null : num3, (i & 64) != 0 ? null : str4, (i & 128) != 0 ? null : str5, (i & com.ecarx.iwallsdk.crypto.param.CryptParam.ECC_KEY_LEN) != 0 ? null : str6, (i & 512) != 0 ? null : str7, (i & com.carlinx.module_show.ui.fragment.ImportMusicFragment.VIDEO_REQUEST_CODE) != 0 ? null : str8, (i & 2048) != 0 ? null : str9, (i & 4096) != 0 ? null : str10, (i & 8192) != 0 ? null : str11, (i & 16384) != 0 ? null : bool, (i & 32768) != 0 ? null : bool2, (i & 65536) != 0 ? null : num4, (i & 131072) != 0 ? null : str12, (i & 262144) != 0 ? null : num5, (i & 524288) != 0 ? null : str13, (i & 1048576) != 0 ? null : str14, (i & 2097152) != 0 ? null : str15, (i & 4194304) != 0 ? null : str16, (i & 8388608) != 0 ? null : str17, (i & 16777216) != 0 ? null : str18, (i & 33554432) != 0 ? null : str19, (i & 67108864) != 0 ? null : str20, (i & 134217728) != 0 ? null : str21, (i & 268435456) != 0 ? null : str22, (i & 536870912) != 0 ? null : str23, (i & 1073741824) != 0 ? false : bool3, (i & com.bumptech.glide.request.target.Target.SIZE_ORIGINAL) != 0 ? false : bool4, (i2 & 1) != 0 ? null : str24, (i2 & 2) != 0 ? null : str25, (i2 & 4) != 0 ? null : str26, (i2 & 8) != 0 ? null : str27, (i2 & 16) != 0 ? null : str28);
    }

    public final java.lang.Boolean getBatteryHeatModeActive() {
        return this.batteryHeatModeActive;
    }

    public final void setBatteryHeatModeActive(java.lang.Boolean bool) {
        this.batteryHeatModeActive = bool;
    }

    public final java.lang.Boolean getHvBatteryPreHeatingActive() {
        return this.hvBatteryPreHeatingActive;
    }

    public final void setHvBatteryPreHeatingActive(java.lang.Boolean bool) {
        this.hvBatteryPreHeatingActive = bool;
    }

    public final java.lang.String getFuelLowWarning() {
        return this.fuelLowWarning;
    }

    public final void setFuelLowWarning(java.lang.String str) {
        this.fuelLowWarning = str;
    }

    public final java.lang.String getMegaWattCharingStatus() {
        return this.megaWattCharingStatus;
    }

    public final void setMegaWattCharingStatus(java.lang.String str) {
        this.megaWattCharingStatus = str;
    }

    public final java.lang.String getMegaWattCharingHint() {
        return this.megaWattCharingHint;
    }

    public final void setMegaWattCharingHint(java.lang.String str) {
        this.megaWattCharingHint = str;
    }

    public final java.lang.String getChargingPileType() {
        return this.chargingPileType;
    }

    public final void setChargingPileType(java.lang.String str) {
        this.chargingPileType = str;
    }

    public final java.lang.String getPowerLowStatus() {
        return this.powerLowStatus;
    }

    public final void setPowerLowStatus(java.lang.String str) {
        this.powerLowStatus = str;
    }
}
