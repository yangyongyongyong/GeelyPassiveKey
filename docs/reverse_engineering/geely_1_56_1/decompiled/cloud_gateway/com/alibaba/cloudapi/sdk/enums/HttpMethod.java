package com.alibaba.cloudapi.sdk.enums;

import com.alibaba.cloudapi.sdk.constant.HttpConstant;
import com.android.volley.toolbox.HttpClientStack;

/* JADX INFO: loaded from: /tmp/geely-dex-out/classes11.dex */
public enum HttpMethod {
    GET("GET", HttpConstant.CLOUDAPI_CONTENT_TYPE_FORM, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    POST_FORM("POST", HttpConstant.CLOUDAPI_CONTENT_TYPE_FORM, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    POST_BODY("POST", HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    PUT_FORM("PUT", HttpConstant.CLOUDAPI_CONTENT_TYPE_FORM, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    PUT_BODY("PUT", HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    PATCH_FORM(HttpClientStack.HttpPatch.METHOD_NAME, HttpConstant.CLOUDAPI_CONTENT_TYPE_FORM, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    PATCH_BODY(HttpClientStack.HttpPatch.METHOD_NAME, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    DELETE("DELETE", HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    HEAD("HEAD", HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON);

    private String acceptContentType;
    private String requestContentType;
    private String value;

    HttpMethod(String value, String requestContentType, String acceptContentType) {
        this.value = value;
        this.requestContentType = requestContentType;
        this.acceptContentType = acceptContentType;
    }

    public String getValue() {
        return this.value;
    }

    public String getRequestContentType() {
        return this.requestContentType;
    }

    public String getAcceptContentType() {
        return this.acceptContentType;
    }
}
