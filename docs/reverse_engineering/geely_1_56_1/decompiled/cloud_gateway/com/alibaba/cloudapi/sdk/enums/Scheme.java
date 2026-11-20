package com.alibaba.cloudapi.sdk.enums;

/* JADX INFO: loaded from: /tmp/geely-dex-out/classes11.dex */
public enum Scheme {
    HTTP("HTTP://"),
    HTTPS("HTTPS://"),
    WEBSOCKET("WS://");

    String value;

    Scheme(String name) {
        this.value = name;
    }

    public String getValue() {
        return this.value;
    }
}
