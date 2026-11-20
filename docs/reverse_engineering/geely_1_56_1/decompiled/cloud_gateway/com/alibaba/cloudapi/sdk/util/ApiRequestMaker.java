package com.alibaba.cloudapi.sdk.util;

import android.text.TextUtils;
import android.util.Base64;
import com.alibaba.cloudapi.sdk.constant.HttpConstant;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

/* JADX INFO: loaded from: /tmp/geely-dex-out/classes11.dex */
public class ApiRequestMaker {
    public static void make(ApiRequest request, String appKey, String appSecret, String[] extraHeaderToSign) {
        request.setPath(combinePathParam(request.getPath(), request.getPathParams()));
        StringBuilder sbAppend = new StringBuilder().append(request.getScheme().getValue()).append(request.getHost()).append(request.getPath());
        if (request.getQuerys() != null && request.getQuerys().size() > 0) {
            sbAppend.append("?").append(HttpCommonUtil.buildParamString(request.getQuerys()));
        }
        request.setUrl(sbAppend.toString());
        Date date = request.getCurrentDate() == null ? new Date() : request.getCurrentDate();
        request.addHeader(HttpConstant.CLOUDAPI_HTTP_HEADER_DATE, getHttpDateHeaderValue(date));
        request.addHeader(SdkConstant.CLOUDAPI_X_CA_TIMESTAMP, String.valueOf(date.getTime()));
        request.addHeader(SdkConstant.CLOUDAPI_X_CA_NONCE, UUID.randomUUID().toString());
        request.addHeader(HttpConstant.CLOUDAPI_HTTP_HEADER_USER_AGENT, SdkConstant.CLOUDAPI_USER_AGENT);
        request.addHeader("host", request.getHost());
        request.addHeader(SdkConstant.CLOUDAPI_X_CA_KEY, appKey);
        request.addHeader(SdkConstant.CLOUDAPI_X_CA_VERSION, "1");
        request.addHeader("content-type", request.getMethod().getRequestContentType());
        request.addHeader(HttpConstant.CLOUDAPI_HTTP_HEADER_ACCEPT, request.getMethod().getAcceptContentType());
        if (!TextUtils.isEmpty(request.getSignatureMethod())) {
            request.addHeader(SdkConstant.CLOUDAPI_X_CA_SIGNATURE_METHOD, request.getSignatureMethod());
        }
        if (request.getBody() != null && request.getBody().length > 0) {
            request.addHeader(HttpConstant.CLOUDAPI_HTTP_HEADER_CONTENT_MD5, base64AndMD5(request.getBody()));
        }
        request.addHeader(SdkConstant.CLOUDAPI_X_CA_SIGNATURE, SignUtil.sign(request, appSecret, extraHeaderToSign));
        for (String str : request.getHeaders().keySet()) {
            List<String> list = request.getHeaders().get(str);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    list.set(i, new String(list.get(i).getBytes(SdkConstant.CLOUDAPI_ENCODING), SdkConstant.CLOUDAPI_HEADER_ENCODING));
                }
            }
            request.getHeaders().put(str, list);
        }
    }

    private static String combinePathParam(String path, Map<String, String> pathParams) {
        if (pathParams == null) {
            return path;
        }
        for (String str : pathParams.keySet()) {
            path = path.replace("[" + str + "]", pathParams.get(str));
        }
        return path;
    }

    private static String getHttpDateHeaderValue(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(date);
    }

    private static String base64AndMD5(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes can not be null");
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(bytes);
            byte[] bArrDigest = messageDigest.digest();
            byte[] bArrEncode = Base64.encode(bArrDigest, 0);
            byte[] bArr = new byte[24];
            for (int i = 0; i < 24; i++) {
                bArr[i] = bArrEncode[i];
            }
            return new String(bArr, SdkConstant.CLOUDAPI_ENCODING);
        } catch (NoSuchAlgorithmException unused) {
            throw new IllegalArgumentException("unknown algorithm MD5");
        }
    }
}
