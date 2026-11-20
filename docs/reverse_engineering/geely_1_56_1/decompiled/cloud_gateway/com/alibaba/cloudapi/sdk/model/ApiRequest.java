package com.alibaba.cloudapi.sdk.model;

import com.alibaba.cloudapi.sdk.enums.HttpConnectionModel;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.enums.WebSocketApiType;
import com.alibaba.cloudapi.sdk.exception.SdkException;
import com.alibaba.fastjson.JSONObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import okhttp3.RequestBody;

/* JADX INFO: loaded from: /tmp/geely-dex-out/classes11.dex */
public class ApiRequest extends ApiHttpMessage {
    private Date currentDate;
    private String host;
    private HttpMethod method;
    private String path;
    private RequestBody requestBody;
    private Scheme scheme;
    private String signatureMethod;
    private String url;
    private HttpConnectionModel httpConnectionMode = HttpConnectionModel.SINGER_CONNECTION;
    private WebSocketApiType webSocketApiType = WebSocketApiType.COMMON;
    private Map<String, String> pathParams = new HashMap();
    private Map<String, String> querys = new HashMap();
    private Map<String, String> formParams = new HashMap();
    private boolean isBase64BodyViaWebsocket = false;

    public ApiRequest(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    public ApiRequest(HttpMethod method, String path, byte[] body) {
        this.method = method;
        this.path = path;
        this.body = body;
    }

    public ApiRequest(HttpMethod method, String path, byte[] body, RequestBody requestBody) {
        this.method = method;
        this.path = path;
        this.body = body;
        this.requestBody = requestBody;
    }

    public ApiRequest(JSONObject message) {
        parse(message);
    }

    public Scheme getScheme() {
        return this.scheme;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public String getHost() {
        return this.host;
    }

    public String getPath() {
        return this.path;
    }

    public Map<String, String> getPathParams() {
        return this.pathParams;
    }

    public Map<String, String> getQuerys() {
        return this.querys;
    }

    public RequestBody getRequestBody() {
        return this.requestBody;
    }

    public Map<String, String> getFormParams() {
        return this.formParams;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addParam(String name, String value, ParamPosition position, boolean isRequired) {
        Map<String, String> map;
        if (value == null) {
            if (isRequired) {
                throw new SdkException(String.format("param %s is not nullable, please check your codes", name));
            }
            return;
        }
        int i = AnonymousClass1.$SwitchMap$com$alibaba$cloudapi$sdk$enums$ParamPosition[position.ordinal()];
        if (i == 1) {
            addHeader(name, value);
            return;
        }
        if (i == 2) {
            map = this.pathParams;
        } else if (i == 3) {
            map = this.querys;
        } else if (i == 4) {
            map = this.formParams;
        } else {
            throw new SdkException("unknown param position: " + position);
        }
        if (value instanceof String) {
            map.put(name, value);
        } else {
            map.put(name, value.toString());
        }
    }

    /* JADX INFO: renamed from: com.alibaba.cloudapi.sdk.model.ApiRequest$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$alibaba$cloudapi$sdk$enums$ParamPosition;

        static {
            int[] iArr = new int[ParamPosition.values().length];
            $SwitchMap$com$alibaba$cloudapi$sdk$enums$ParamPosition = iArr;
            try {
                iArr[ParamPosition.HEAD.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$alibaba$cloudapi$sdk$enums$ParamPosition[ParamPosition.PATH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$alibaba$cloudapi$sdk$enums$ParamPosition[ParamPosition.QUERY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$alibaba$cloudapi$sdk$enums$ParamPosition[ParamPosition.BODY.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPathParams(Map<String, String> pathParams) {
        this.pathParams = pathParams;
    }

    public void setQuerys(Map<String, String> querys) {
        this.querys = querys;
    }

    public void setFormParams(Map<String, String> formParams) {
        this.formParams = formParams;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isBase64BodyViaWebsocket() {
        return this.isBase64BodyViaWebsocket;
    }

    public void setBase64BodyViaWebsocket(boolean base64BodyViaWebsocket) {
        this.isBase64BodyViaWebsocket = base64BodyViaWebsocket;
    }

    public String getSignatureMethod() {
        return this.signatureMethod;
    }

    public void setSignatureMethod(String signatureMethod) {
        this.signatureMethod = signatureMethod;
    }

    public HttpConnectionModel getHttpConnectionMode() {
        return this.httpConnectionMode;
    }

    public void setHttpConnectionMode(HttpConnectionModel httpConnectionMode) {
        this.httpConnectionMode = httpConnectionMode;
    }

    @Override // com.alibaba.cloudapi.sdk.model.ApiHttpMessage
    public void parse(JSONObject message) {
        super.parse(message);
    }

    public WebSocketApiType getWebSocketApiType() {
        return this.webSocketApiType;
    }

    public void setWebSocketApiType(WebSocketApiType webSocketApiType) {
        this.webSocketApiType = webSocketApiType;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public Date getCurrentDate() {
        return this.currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public ApiRequest duplicate() {
        ApiRequest apiRequest = new ApiRequest(this.method, this.path, this.body);
        apiRequest.scheme = this.scheme;
        if (this.host != null) {
            apiRequest.host = new String(this.host);
        }
        if (this.url != null) {
            apiRequest.url = new String(this.url);
        }
        HashMap map = new HashMap();
        apiRequest.pathParams = map;
        map.putAll(this.pathParams);
        apiRequest.headers = new HashMap();
        apiRequest.headers.putAll(this.headers);
        HashMap map2 = new HashMap();
        apiRequest.querys = map2;
        map2.putAll(this.querys);
        HashMap map3 = new HashMap();
        apiRequest.formParams = map3;
        map3.putAll(this.formParams);
        if (this.signatureMethod != null) {
            apiRequest.signatureMethod = new String(this.signatureMethod);
        }
        apiRequest.webSocketApiType = this.webSocketApiType;
        apiRequest.httpConnectionMode = this.httpConnectionMode;
        apiRequest.isBase64BodyViaWebsocket = this.isBase64BodyViaWebsocket;
        return apiRequest;
    }
}
