package com.alibaba.cloudapi.sdk.util;

import cn.hutool.setting.AbsSetting;
import com.alibaba.cloudapi.sdk.constant.HttpConstant;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.exception.SdkException;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.signature.ISignerFactory;
import com.alibaba.cloudapi.sdk.signature.ISinger;
import com.alibaba.cloudapi.sdk.signature.SignerFactoryManager;
import com.alipay.sdk.m.s.a;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/* JADX INFO: loaded from: /tmp/geely-dex-out/classes11.dex */
public class SignUtil {
    public static String sign(ApiRequest request, String secret, String[] extraHeaderToSign) {
        try {
            String strBuildStringToSign = buildStringToSign(request, extraHeaderToSign);
            ISignerFactory iSignerFactoryFindSignerFactory = SignerFactoryManager.findSignerFactory(request.getSignatureMethod());
            if (iSignerFactoryFindSignerFactory == null) {
                throw new SdkException("unsupported signature method:" + request.getSignatureMethod());
            }
            ISinger signer = iSignerFactoryFindSignerFactory.getSigner();
            if (signer == null) {
                throw new SdkException("Oops!");
            }
            try {
                return signer.sign(strBuildStringToSign, secret);
            } catch (Exception e) {
                throw new SdkException(e);
            }
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    private static String buildStringToSign(ApiRequest apiRequest, String[] extraHeaderToSign) {
        StringBuilder sb = new StringBuilder();
        sb.append(apiRequest.getMethod().getValue()).append(SdkConstant.CLOUDAPI_LF);
        if (apiRequest.getFirstHeaderValue(HttpConstant.CLOUDAPI_HTTP_HEADER_ACCEPT) != null) {
            sb.append(apiRequest.getFirstHeaderValue(HttpConstant.CLOUDAPI_HTTP_HEADER_ACCEPT));
        }
        sb.append(SdkConstant.CLOUDAPI_LF);
        if (apiRequest.getFirstHeaderValue(HttpConstant.CLOUDAPI_HTTP_HEADER_CONTENT_MD5) != null) {
            sb.append(apiRequest.getFirstHeaderValue(HttpConstant.CLOUDAPI_HTTP_HEADER_CONTENT_MD5));
        }
        sb.append(SdkConstant.CLOUDAPI_LF);
        if (apiRequest.getFirstHeaderValue("content-type") != null) {
            sb.append(apiRequest.getFirstHeaderValue("content-type"));
        }
        sb.append(SdkConstant.CLOUDAPI_LF);
        if (apiRequest.getFirstHeaderValue(HttpConstant.CLOUDAPI_HTTP_HEADER_DATE) != null) {
            sb.append(apiRequest.getFirstHeaderValue(HttpConstant.CLOUDAPI_HTTP_HEADER_DATE));
        }
        sb.append(SdkConstant.CLOUDAPI_LF);
        sb.append(buildHeaders(apiRequest, extraHeaderToSign));
        sb.append(buildResource(apiRequest));
        return sb.toString();
    }

    private static String buildResource(ApiRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getPath());
        TreeMap treeMap = new TreeMap();
        if (request.getQuerys() != null && request.getQuerys().size() > 0) {
            treeMap.putAll(request.getQuerys());
        }
        if (request.getFormParams() != null && request.getFormParams().size() > 0) {
            treeMap.putAll(request.getFormParams());
        }
        if (treeMap.size() > 0) {
            sb.append("?");
            boolean z = true;
            for (String str : treeMap.keySet()) {
                if (z) {
                    z = false;
                } else {
                    sb.append(a.n);
                }
                sb.append(str);
                String str2 = (String) treeMap.get(str);
                if (str2 != null && !"".equals(str2)) {
                    sb.append("=").append(str2);
                }
            }
        }
        return sb.toString();
    }

    private static String buildHeaders(ApiRequest apiRequest, String[] extraHeaderToSign) {
        TreeMap treeMap = new TreeMap();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, List<String>> entry : apiRequest.getHeaders().entrySet()) {
            if (entry.getKey().startsWith(SdkConstant.CLOUDAPI_CA_HEADER_TO_SIGN_PREFIX_SYSTEM)) {
                if (i != 0) {
                    sb.append(AbsSetting.DEFAULT_DELIMITER);
                }
                i++;
                sb.append(entry.getKey());
                treeMap.put(entry.getKey(), apiRequest.getFirstHeaderValue(entry.getKey()));
            }
            if (extraHeaderToSign != null && extraHeaderToSign.length > 0) {
                for (String str : extraHeaderToSign) {
                    if (str.equals(entry.getKey())) {
                        if (i != 0) {
                            sb.append(AbsSetting.DEFAULT_DELIMITER);
                        }
                        i++;
                        sb.append(entry.getKey());
                        treeMap.put(entry.getKey(), apiRequest.getFirstHeaderValue(entry.getKey()));
                    }
                }
            }
        }
        apiRequest.addHeader(SdkConstant.CLOUDAPI_X_CA_SIGNATURE_HEADERS, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        for (Map.Entry entry2 : treeMap.entrySet()) {
            sb2.append((String) entry2.getKey()).append(':').append((String) entry2.getValue()).append(SdkConstant.CLOUDAPI_LF);
        }
        return sb2.toString();
    }
}
