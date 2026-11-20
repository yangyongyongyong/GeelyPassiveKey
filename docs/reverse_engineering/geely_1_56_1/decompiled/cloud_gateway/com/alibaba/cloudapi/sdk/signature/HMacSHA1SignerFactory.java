package com.alibaba.cloudapi.sdk.signature;

import android.text.TextUtils;
import android.util.Base64;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: /tmp/geely-dex-out/classes11.dex */
public class HMacSHA1SignerFactory implements ISignerFactory {
    public static final String METHOD = "HmacSHA1";
    private static ISinger singer;

    @Override // com.alibaba.cloudapi.sdk.signature.ISignerFactory
    public ISinger getSigner() {
        if (singer == null) {
            singer = new HMacSHA1Signer();
        }
        return singer;
    }

    private static class HMacSHA1Signer implements ISinger {
        private HMacSHA1Signer() {
        }

        @Override // com.alibaba.cloudapi.sdk.signature.ISinger
        public String sign(String strToSign, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
            if (TextUtils.isEmpty(strToSign)) {
                throw new IllegalArgumentException("strToSign can not be empty");
            }
            if (TextUtils.isEmpty(secretKey)) {
                throw new IllegalArgumentException("secretKey can not be empty");
            }
            Mac mac = Mac.getInstance(HMacSHA1SignerFactory.METHOD);
            byte[] bytes = secretKey.getBytes(SdkConstant.CLOUDAPI_ENCODING);
            mac.init(new SecretKeySpec(bytes, 0, bytes.length, HMacSHA1SignerFactory.METHOD));
            return new String(Base64.encode(mac.doFinal(strToSign.getBytes(SdkConstant.CLOUDAPI_ENCODING)), 0), SdkConstant.CLOUDAPI_ENCODING);
        }
    }

    static String byte2String(byte[] data) {
        StringBuilder sb = new StringBuilder();
        if (data != null) {
            for (byte b : data) {
                sb.append(String.format("%02x", Byte.valueOf(b)));
            }
        }
        return sb.toString();
    }
}
