package com.geely.rxhttp;

/* JADX INFO: compiled from: SWGatewayComponent.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes1.dex */
@kotlin.Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u0000 \u00062\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0006B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0007"}, d2 = {"Lcom/geely/rxhttp/ContentType;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "(Ljava/lang/String;I)V", "Form", "Json", "Default", "Companion", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
public enum ContentType {
    Form,
    Json,
    Default;


    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.geely.rxhttp.ContentType.Companion INSTANCE = new com.geely.rxhttp.ContentType.Companion(null);

    /* JADX INFO: compiled from: SWGatewayComponent.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006¨\u0006\u0007"}, d2 = {"Lcom/geely/rxhttp/ContentType$Companion;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "()V", "convert", "Lcom/geely/rxhttp/ContentType;", "str", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final com.geely.rxhttp.ContentType convert(java.lang.String str) {
            java.lang.String str2 = str;
            int i = 0;
            if (str2 == null || str2.length() == 0) {
                return com.geely.rxhttp.ContentType.Default;
            }
            com.geely.rxhttp.ContentType[] contentTypeArrValues = com.geely.rxhttp.ContentType.values();
            int length = contentTypeArrValues.length;
            while (i < length) {
                com.geely.rxhttp.ContentType contentType = contentTypeArrValues[i];
                i++;
                if (kotlin.text.StringsKt.equals(contentType.name(), str, true)) {
                    return contentType;
                }
            }
            return null;
        }
    }
}
