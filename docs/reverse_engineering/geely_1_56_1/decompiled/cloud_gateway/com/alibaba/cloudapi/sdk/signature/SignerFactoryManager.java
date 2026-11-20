package com.alibaba.cloudapi.sdk.signature;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: /tmp/geely-dex-out/classes11.dex */
public class SignerFactoryManager {
    private static Map<String, ISignerFactory> factoryMap = new HashMap(2);

    public static void init() {
        registerSignerFactory(HMacSHA256SignerFactory.METHOD, new HMacSHA256SignerFactory());
        registerSignerFactory(HMacSHA1SignerFactory.METHOD, new HMacSHA1SignerFactory());
    }

    public static ISignerFactory registerSignerFactory(String method, ISignerFactory factory) {
        if (TextUtils.isEmpty(method)) {
            throw new IllegalArgumentException("method can not be empty");
        }
        if (factory == null) {
            throw new IllegalArgumentException("factory can not be null");
        }
        return factoryMap.put(method, factory);
    }

    public static ISignerFactory findSignerFactory(String method) {
        if (TextUtils.isEmpty(method)) {
            method = HMacSHA256SignerFactory.METHOD;
        }
        return factoryMap.get(method);
    }
}
