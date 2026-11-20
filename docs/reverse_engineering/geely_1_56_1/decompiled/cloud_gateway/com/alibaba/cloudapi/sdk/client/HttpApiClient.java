package com.alibaba.cloudapi.sdk.client;

import com.alibaba.cloudapi.sdk.enums.HttpConnectionModel;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.exception.SdkException;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.alibaba.cloudapi.sdk.signature.SignerFactoryManager;
import com.alibaba.cloudapi.sdk.util.ApiRequestMaker;
import com.alibaba.cloudapi.sdk.util.HttpCommonUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/* JADX INFO: loaded from: /tmp/geely-dex-out/classes11.dex */
public class HttpApiClient extends BaseApiClient {
    OkHttpClient client;

    protected HttpApiClient() {
    }

    public void init(HttpClientBuilderParams buildParam) {
        if (buildParam == null) {
            throw new SdkException("buildParam must not be null");
        }
        buildParam.check();
        this.appKey = buildParam.getAppKey();
        this.appSecret = buildParam.getAppSecret();
        this.extraHeaderToSign = buildParam.getExtraHeaderToSign();
        this.host = buildParam.getHost();
        this.scheme = buildParam.getScheme();
        OkHttpClient.Builder builderConnectTimeout = new OkHttpClient.Builder().readTimeout(buildParam.getReadTimeout(), TimeUnit.MILLISECONDS).writeTimeout(buildParam.getWriteTimeout(), TimeUnit.MILLISECONDS).connectTimeout(buildParam.getConnectionTimeout(), TimeUnit.MILLISECONDS);
        if (buildParam.getEventListenerFactory() != null) {
            builderConnectTimeout.eventListenerFactory(buildParam.getEventListenerFactory());
        }
        if (buildParam.getSocketFactory() != null) {
            builderConnectTimeout.socketFactory(buildParam.getSocketFactory());
        }
        builderConnectTimeout.retryOnConnectionFailure(buildParam.isHttpConnectionRetry());
        if (buildParam.getInterceptor() != null) {
            builderConnectTimeout.interceptors().add(buildParam.getInterceptor());
        }
        if (this.scheme == Scheme.HTTPS) {
            builderConnectTimeout.sslSocketFactory(buildParam.getSslSocketFactory(), buildParam.getX509TrustManager()).hostnameVerifier(buildParam.getHostnameVerifier());
        }
        this.client = builderConnectTimeout.build();
        SignerFactoryManager.init();
        this.isInit = true;
    }

    @Override // com.alibaba.cloudapi.sdk.client.BaseApiClient
    protected ApiResponse sendSyncRequest(ApiRequest apiRequest) {
        checkIsInit();
        try {
            return getApiResponse(apiRequest, this.client.newCall(buildRequest(apiRequest)).execute());
        } catch (IOException e) {
            return new ApiResponse(500, "Read response occur error", e);
        }
    }

    @Override // com.alibaba.cloudapi.sdk.client.BaseApiClient
    protected void sendAsyncRequest(final ApiRequest apiRequest, final ApiCallback apiCallback) {
        checkIsInit();
        this.client.newCall(buildRequest(apiRequest)).enqueue(new Callback() { // from class: com.alibaba.cloudapi.sdk.client.HttpApiClient.1
            public void onFailure(Call call, IOException e) {
                apiCallback.onFailure(apiRequest, e);
            }

            public void onResponse(Call call, Response response) throws IOException {
                ApiCallback apiCallback2 = apiCallback;
                ApiRequest apiRequest2 = apiRequest;
                apiCallback2.onResponse(apiRequest2, HttpApiClient.this.getApiResponse(apiRequest2, response));
            }
        });
    }

    @Override // com.alibaba.cloudapi.sdk.client.BaseApiClient
    protected void getAsyncRequest(ApiRequest apiRequest) {
        checkIsInit();
        this.client.newCall(buildRequest(apiRequest));
    }

    private Request buildRequest(ApiRequest request) {
        RequestBody requestBodyCreate;
        if (request.getHttpConnectionMode() == HttpConnectionModel.SINGER_CONNECTION) {
            request.setHost(this.host);
            request.setScheme(this.scheme);
        }
        ApiRequestMaker.make(request, this.appKey, this.appSecret, this.extraHeaderToSign);
        if (request.getFormParams() != null && request.getFormParams().size() > 0) {
            requestBodyCreate = RequestBody.create(MediaType.parse(request.getMethod().getRequestContentType()), HttpCommonUtil.buildParamString(request.getFormParams()));
        } else {
            requestBodyCreate = (request.getBody() == null || request.getBody().length <= 0) ? null : RequestBody.create(MediaType.parse(request.getMethod().getRequestContentType()), request.getBody());
        }
        if (request.getRequestBody() != null) {
            requestBodyCreate = request.getRequestBody();
        }
        return new Request.Builder().method(request.getMethod().getValue(), requestBodyCreate).url(request.getUrl()).headers(getHeadersFromMap(request.getHeaders())).build();
    }

    private Headers getHeadersFromMap(Map<String, List<String>> map) {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            for (String str : entry.getValue()) {
                arrayList.add(entry.getKey());
                arrayList.add(str);
            }
        }
        return Headers.of((String[]) arrayList.toArray(new String[arrayList.size()]));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ApiResponse getApiResponse(ApiRequest request, Response response) throws IOException {
        ApiResponse apiResponse = new ApiResponse(response.code());
        apiResponse.setHeaders(response.headers().toMultimap());
        apiResponse.setBody(response.body().bytes());
        apiResponse.setContentType(response.header("content-type", ""));
        return apiResponse;
    }

    private Map<String, String> getSimpleMapFromRequest(Headers headers) {
        Map multimap = headers.toMultimap();
        HashMap map = new HashMap();
        for (Map.Entry entry : multimap.entrySet()) {
            map.put(entry.getKey(), ((List) entry.getValue()).get(0));
        }
        return map;
    }
}
