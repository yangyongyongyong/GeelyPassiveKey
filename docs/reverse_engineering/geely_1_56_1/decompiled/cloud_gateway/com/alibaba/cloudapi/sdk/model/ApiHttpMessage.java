package com.alibaba.cloudapi.sdk.model;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.m.u.i;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: /tmp/geely-dex-out/classes11.dex */
public abstract class ApiHttpMessage {
    protected byte[] body;
    protected String bodyStr;
    protected Map<String, List<String>> headers = new HashMap();

    public String getBodyStr() {
        return this.bodyStr;
    }

    public byte[] getBody() {
        return this.body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public void addHeader(String name, String value) {
        String lowerCase = name.trim().toLowerCase();
        if (this.headers.containsKey(lowerCase)) {
            this.headers.get(lowerCase).add(value);
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(value.trim());
        this.headers.put(lowerCase, arrayList);
    }

    public String getFirstHeaderValue(String name) {
        if (!this.headers.containsKey(name) || this.headers.get(name).size() <= 0) {
            return null;
        }
        return this.headers.get(name).get(0);
    }

    public void parse(JSONObject message) {
        JSONObject jSONObject = message.getJSONObject("header");
        for (Map.Entry<String, Object> entry : jSONObject.entrySet()) {
            if (entry.getValue() instanceof String) {
                addHeader(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof List) {
                Iterator it = ((List) entry.getValue()).iterator();
                while (it.hasNext()) {
                    addHeader(entry.getKey(), (String) it.next());
                }
            }
        }
        String string = jSONObject.getString("content-type");
        Charset charsetForName = SdkConstant.CLOUDAPI_ENCODING;
        if (string != null) {
            try {
                String[] strArrSplit = string.toLowerCase().split(i.b);
                for (int i = 0; i < strArrSplit.length; i++) {
                    if (strArrSplit[i].contains("charset")) {
                        String str = strArrSplit[i];
                        charsetForName = Charset.forName(str.substring(str.indexOf("=")));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String string2 = message.getString("body");
        this.bodyStr = string2;
        this.body = string2.getBytes(charsetForName);
    }
}
