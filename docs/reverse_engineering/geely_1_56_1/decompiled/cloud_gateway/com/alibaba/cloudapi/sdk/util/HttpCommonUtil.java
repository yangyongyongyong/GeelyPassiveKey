package com.alibaba.cloudapi.sdk.util;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alipay.sdk.m.s.a;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/* JADX INFO: loaded from: /tmp/geely-dex-out/classes11.dex */
public class HttpCommonUtil {
    public static String buildParamString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            boolean z = true;
            for (String str : params.keySet()) {
                if (z) {
                    z = false;
                } else {
                    sb.append(a.n);
                }
                try {
                    sb.append(str).append("=").append(URLEncoder.encode(params.get(str), SdkConstant.CLOUDAPI_ENCODING.displayName()));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return sb.toString();
    }
}
