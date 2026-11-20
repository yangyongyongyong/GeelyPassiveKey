package com.geely.galaxy.network.impl;

/* JADX INFO: compiled from: HttpExecutorImpl.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J,\u0010\u0003\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00050\u0004\"\u0004\b\u0000\u0010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\nH\u0016J,\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00050\f\"\u0004\b\u0000\u0010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\nH\u0016¨\u0006\r"}, d2 = {"Lcom/geely/galaxy/network/impl/HttpExecutorImpl;", "Lcom/geely/galaxy/network/interfacex/IHttpExecutor;", "()V", "execute", "Lio/reactivex/Observable;", "T", "request", "Lcom/geely/rxhttp/base/IBaseRequest;", "", "typeOfT", "Ljava/lang/reflect/Type;", "executeSync", "Lcom/geely/rxhttp/base/IHttpResponse;", "com.geely.galaxy.network"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final class HttpExecutorImpl implements com.geely.galaxy.network.interfacex.IHttpExecutor {
    @Override // com.geely.galaxy.network.interfacex.IHttpExecutor
    public <T> io.reactivex.Observable<T> execute(final com.geely.rxhttp.base.IBaseRequest<java.lang.String> request, final java.lang.reflect.Type typeOfT) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(typeOfT, "typeOfT");
        io.reactivex.Observable<T> observableCreate = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.network.impl.HttpExecutorImpl$$ExternalSyntheticLambda0
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.network.impl.HttpExecutorImpl.execute$lambda$0(request, typeOfT, observableEmitter);
            }
        });
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCreate, "create {\n            val…)\n            }\n        }");
        return observableCreate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void execute$lambda$0(com.geely.rxhttp.base.IBaseRequest iBaseRequest, java.lang.reflect.Type type, io.reactivex.ObservableEmitter observableEmitter) {
        java.lang.String strFromJson;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iBaseRequest, "$request");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(type, "$typeOfT");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.base.IHttpResponse iHttpResponseExecRequest = com.geely.rxhttp.SWHttpComponent.Companion.getInstance().execRequest(iBaseRequest);
        java.lang.Integer httpCode = iHttpResponseExecRequest.getHttpCode();
        if (httpCode != null && httpCode.intValue() == 200) {
            try {
                if (type == java.lang.String.class) {
                    strFromJson = iHttpResponseExecRequest.getBody();
                } else {
                    java.lang.String body = iHttpResponseExecRequest.getBody();
                    if (body == null) {
                        body = "";
                    }
                    strFromJson = com.geely.galaxy.network.util.NetExtendKt.fromJson(body, type);
                }
                if (strFromJson != null) {
                    observableEmitter.onNext(strFromJson);
                    observableEmitter.onComplete();
                    return;
                } else {
                    observableEmitter.onError(new com.geely.galaxy.network.exception.NetWorkErrorException(500, "无法解析response body,请确认服务端接口状态(" + iBaseRequest.getPath() + ")"));
                    return;
                }
            } catch (java.lang.Exception e) {
                observableEmitter.onError(e);
                return;
            }
        }
        java.lang.Integer httpCode2 = iHttpResponseExecRequest.getHttpCode();
        int iIntValue = httpCode2 != null ? httpCode2.intValue() : 500;
        observableEmitter.onError(new com.geely.galaxy.network.exception.NetWorkErrorException(iIntValue, iHttpResponseExecRequest.getMessage() + "[" + iHttpResponseExecRequest.getHttpMessage() + "](" + iBaseRequest.getPath() + ")"));
    }

    @Override // com.geely.galaxy.network.interfacex.IHttpExecutor
    public <T> com.geely.rxhttp.base.IHttpResponse<T> executeSync(com.geely.rxhttp.base.IBaseRequest<java.lang.String> request, java.lang.reflect.Type typeOfT) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(typeOfT, "typeOfT");
        final com.geely.rxhttp.base.IHttpResponse iHttpResponseExecRequest = com.geely.rxhttp.SWHttpComponent.Companion.getInstance().execRequest(request);
        java.lang.Integer httpCode = iHttpResponseExecRequest.getHttpCode();
        if (httpCode != null && httpCode.intValue() == 200) {
            try {
                java.lang.String body = iHttpResponseExecRequest.getBody();
                if (body == null) {
                    body = "";
                }
                final java.lang.Object objFromJson = com.geely.galaxy.network.util.NetExtendKt.fromJson(body, typeOfT);
                return new com.geely.rxhttp.base.IHttpResponse<T>() { // from class: com.geely.galaxy.network.impl.HttpExecutorImpl.executeSync.1
                    public boolean isSuccessful() {
                        return com.geely.rxhttp.base.IHttpResponse.DefaultImpls.isSuccessful(this);
                    }

                    public java.lang.String getBody() {
                        return iHttpResponseExecRequest.getBody();
                    }

                    public int getCode() {
                        return iHttpResponseExecRequest.getCode();
                    }

                    public T getData() {
                        return objFromJson;
                    }

                    public java.util.Map<java.lang.String, java.lang.String> getHeaders() {
                        return iHttpResponseExecRequest.getHeaders();
                    }

                    public java.lang.Integer getHttpCode() {
                        return iHttpResponseExecRequest.getHttpCode();
                    }

                    public java.lang.String getHttpMessage() {
                        return iHttpResponseExecRequest.getHttpMessage();
                    }

                    public java.lang.String getMessage() {
                        return iHttpResponseExecRequest.getMessage();
                    }
                };
            } catch (java.lang.Exception unused) {
            }
        }
        return new com.geely.rxhttp.base.IHttpResponse<T>() { // from class: com.geely.galaxy.network.impl.HttpExecutorImpl.executeSync.2
            public T getData() {
                return null;
            }

            public boolean isSuccessful() {
                return com.geely.rxhttp.base.IHttpResponse.DefaultImpls.isSuccessful(this);
            }

            public java.lang.String getBody() {
                return iHttpResponseExecRequest.getBody();
            }

            public int getCode() {
                return iHttpResponseExecRequest.getCode();
            }

            public java.util.Map<java.lang.String, java.lang.String> getHeaders() {
                return iHttpResponseExecRequest.getHeaders();
            }

            public java.lang.Integer getHttpCode() {
                return iHttpResponseExecRequest.getHttpCode();
            }

            public java.lang.String getHttpMessage() {
                return iHttpResponseExecRequest.getHttpMessage();
            }

            public java.lang.String getMessage() {
                return iHttpResponseExecRequest.getMessage();
            }
        };
    }
}
