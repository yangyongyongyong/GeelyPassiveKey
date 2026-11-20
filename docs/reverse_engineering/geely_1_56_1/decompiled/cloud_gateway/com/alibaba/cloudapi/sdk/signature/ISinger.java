package com.alibaba.cloudapi.sdk.signature;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/* JADX INFO: loaded from: /tmp/geely-dex-out/classes11.dex */
public interface ISinger {
    String sign(String strToSign, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException;
}
