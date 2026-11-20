package com.geely.cockpit.services.controlservice.core.controlImpl;

/* JADX INFO: compiled from: UspControlImpl.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000\u0086\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004JC\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J|\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00120\u0006\"\u0004\b\u0000\u0010\u00122\b\b\u0002\u0010\u0013\u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u001523\u0010\u0016\u001a/\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\u0018¢\u0006\f\b\u0019\u0012\b\b\u0013\u0012\u0004\b\b(\u001a\u0012\u0012\u0012\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u0002H\u0012\u0018\u00010\u001c0\u001b0\u0017H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u001dJ;\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010 \u001a\u00020\t2\u0006\u0010!\u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\"J3\u0010#\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010$\u001a\u00020%2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010&J3\u0010'\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010(\u001a\u00020)2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010*J3\u0010+\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010(\u001a\u00020,2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010-J3\u0010.\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010(\u001a\u00020/2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u00100J3\u00101\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u00102\u001a\u0002032\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u00104J3\u00105\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010(\u001a\u0002062\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u00107J3\u00108\u001a\b\u0012\u0004\u0012\u0002090\u00062\u0006\u0010(\u001a\u0002062\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u00107J3\u0010:\u001a\b\u0012\u0004\u0012\u0002090\u00062\u0006\u0010(\u001a\u00020;2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010<J3\u0010=\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010(\u001a\u00020>2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010?J3\u0010@\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010A\u001a\u00020B2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010CJ3\u0010D\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010(\u001a\u00020E2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010FJ3\u0010G\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010H\u001a\u00020%2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010&J3\u0010I\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010J\u001a\u00020%2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010&J3\u0010K\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010(\u001a\u00020%2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010&J3\u0010L\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010M\u001a\u00020%2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010&J3\u0010N\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010O\u001a\u00020%2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010&J3\u0010P\u001a\b\u0012\u0004\u0012\u00020\t0\u00062\u0006\u0010Q\u001a\u00020R2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010SJ3\u0010T\u001a\b\u0012\u0004\u0012\u00020\t0\u00062\u0006\u0010U\u001a\u00020V2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010WJA\u0010X\u001a\b\u0012\u0004\u0012\u00020\t0\u00062\u0006\u0010 \u001a\u00020\t2\f\u0010Y\u001a\b\u0012\u0004\u0012\u00020[0Z2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\\J;\u0010]\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010 \u001a\u00020\t2\u0006\u0010!\u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\"J;\u0010^\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010 \u001a\u00020\t2\u0006\u0010!\u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\"J5\u0010_\u001a\b\u0012\u0004\u0012\u00020\t0\u00062\b\u0010`\u001a\u0004\u0018\u00010a2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010bJ5\u0010c\u001a\b\u0012\u0004\u0012\u00020\t0\u00062\b\u0010`\u001a\u0004\u0018\u00010d2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010eJ5\u0010f\u001a\b\u0012\u0004\u0012\u00020g0\u00062\b\u0010`\u001a\u0004\u0018\u00010h2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010iJ5\u0010j\u001a\b\u0012\u0004\u0012\u00020\t0\u00062\b\u0010`\u001a\u0004\u0018\u00010k2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010lJ3\u0010m\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010(\u001a\u00020n2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010oJ3\u0010p\u001a\b\u0012\u0004\u0012\u00020q0\u00062\u0006\u0010U\u001a\u00020V2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010WJ3\u0010r\u001a\b\u0012\u0004\u0012\u00020s0\u00062\u0006\u0010`\u001a\u00020t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010uJ3\u0010v\u001a\b\u0012\u0004\u0012\u00020w0\u00062\u0006\u0010`\u001a\u00020x2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010yJ3\u0010z\u001a\b\u0012\u0004\u0012\u00020{0\u00062\u0006\u0010`\u001a\u00020|2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010}J?\u0010~\u001a\b\u0012\u0004\u0012\u00020\u007f0\u00062\u0007\u0010\u0080\u0001\u001a\u00020\t2\b\u0010\u0081\u0001\u001a\u00030\u0082\u00012\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0083\u0001J?\u0010\u0084\u0001\u001a\u0011\u0012\r\u0012\u000b\u0012\u0005\u0012\u00030\u0085\u0001\u0018\u00010Z0\u00062\u0007\u0010\u0086\u0001\u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0087\u0001J6\u0010\u0088\u0001\u001a\t\u0012\u0005\u0012\u00030\u0089\u00010\u00062\u0006\u0010 \u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0087\u0001J7\u0010\u008a\u0001\u001a\t\u0012\u0005\u0012\u00030\u008b\u00010\u00062\u0007\u0010\n\u001a\u00030\u008c\u00012\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u008d\u0001JA\u0010\u008e\u0001\u001a\t\u0012\u0005\u0012\u00030\u008f\u00010\u00062\u0007\u0010`\u001a\u00030\u0090\u00012\b\u0010\u0091\u0001\u001a\u00030\u0082\u00012\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0092\u0001J7\u0010\u0093\u0001\u001a\t\u0012\u0005\u0012\u00030\u0094\u00010\u00062\u0007\u0010`\u001a\u00030\u0090\u00012\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0095\u0001J3\u0010\u0096\u0001\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0Z0\u00062\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0097\u0001J.\u0010\u0098\u0001\u001a\t\u0012\u0005\u0012\u00030\u0099\u00010\u00062\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0097\u0001J7\u0010\u009a\u0001\u001a\t\u0012\u0005\u0012\u00030\u009b\u00010\u00062\u0007\u0010`\u001a\u00030\u009c\u00012\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u009d\u0001J6\u0010\u009e\u0001\u001a\t\u0012\u0005\u0012\u00030\u009f\u00010\u00062\u0006\u0010 \u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0087\u0001J4\u0010 \u0001\u001a\u000f\u0012\u000b\u0012\t\u0012\u0004\u0012\u00020\u007f0¡\u00010\u00062\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0097\u0001J5\u0010¢\u0001\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010 \u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0087\u0001J?\u0010£\u0001\u001a\t\u0012\u0005\u0012\u00030¤\u00010\u00062\u0006\u0010 \u001a\u00020\t2\u0007\u0010¥\u0001\u001a\u00020\u00072\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010¦\u0001JE\u0010§\u0001\u001a\u000b\u0012\u0007\u0012\u0005\u0018\u00010¨\u00010\u00062\t\u0010©\u0001\u001a\u0004\u0018\u00010\t2\t\u0010ª\u0001\u001a\u0004\u0018\u00010\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\"J.\u0010«\u0001\u001a\t\u0012\u0005\u0012\u00030¬\u00010\u00062\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0097\u0001JD\u0010\u00ad\u0001\u001a\t\u0012\u0005\u0012\u00030®\u00010\u00062\t\u0010¯\u0001\u001a\u0004\u0018\u00010\t2\t\u0010°\u0001\u001a\u0004\u0018\u00010\u00072\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010±\u0001J7\u0010²\u0001\u001a\t\u0012\u0005\u0012\u00030³\u00010\u00062\u0007\u0010\n\u001a\u00030´\u00012\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010µ\u0001JC\u0010¶\u0001\u001a\u000f\u0012\u000b\u0012\t\u0012\u0005\u0012\u00030·\u00010Z0\u00062\r\u0010¸\u0001\u001a\b\u0012\u0004\u0012\u00020\t0Z2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010¹\u0001J]\u0010º\u0001\u001a\t\u0012\u0005\u0012\u00030»\u00010\u00062\u0007\u0010¯\u0001\u001a\u00020\t2\b\u0010¼\u0001\u001a\u00030½\u00012\b\u0010¾\u0001\u001a\u00030½\u00012\u0007\u0010¿\u0001\u001a\u00020\u00072\u0007\u0010À\u0001\u001a\u00020\u00072\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010Á\u0001JA\u0010Â\u0001\u001a\t\u0012\u0005\u0012\u00030\u0082\u00010\u00062\t\u0010Ã\u0001\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010Ä\u0001J<\u0010Å\u0001\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0006\u0010 \u001a\u00020\t2\u0006\u0010!\u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\"J.\u0010Æ\u0001\u001a\t\u0012\u0005\u0012\u00030Ç\u00010\u00062\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0097\u0001J7\u0010È\u0001\u001a\t\u0012\u0005\u0012\u00030É\u00010\u00062\u0007\u0010`\u001a\u00030Ê\u00012\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010Ë\u0001J.\u0010Ì\u0001\u001a\t\u0012\u0005\u0012\u00030Í\u00010\u00062\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0097\u0001J7\u0010Î\u0001\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\b\u0010\u0080\u0001\u001a\u00030½\u00012\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010Ï\u0001J-\u0010Ð\u0001\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0097\u0001JD\u0010Ñ\u0001\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J6\u0010Ò\u0001\u001a\t\u0012\u0005\u0012\u00030\u0082\u00010\u00062\u0006\u0010 \u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010\u0087\u0001J9\u0010Ó\u0001\u001a\u000b\u0012\u0007\u0012\u0005\u0018\u00010Ô\u00010\u00062\u0007\u0010`\u001a\u00030Õ\u00012\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010Ö\u0001J=\u0010×\u0001\u001a\t\u0012\u0005\u0012\u00030\u0082\u00010\u00062\u0006\u0010 \u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\"J7\u0010Ø\u0001\u001a\t\u0012\u0005\u0012\u00030Ù\u00010\u00062\u0007\u0010U\u001a\u00030Ç\u00012\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010Ú\u0001J6\u0010Û\u0001\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0007\u0010Ü\u0001\u001a\u00020\u00072\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010Ý\u0001JJ\u0010Þ\u0001\u001a\u000b\u0012\u0007\u0012\u0005\u0018\u00010Ô\u00010\u00062\u0007\u0010ß\u0001\u001a\u00020\t2\u0007\u0010à\u0001\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010á\u0001J7\u0010â\u0001\u001a\t\u0012\u0005\u0012\u00030Ù\u00010\u00062\u0007\u0010U\u001a\u00030Í\u00012\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010ã\u0001J6\u0010ä\u0001\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00062\u0007\u0010`\u001a\u00030å\u00012\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010æ\u0001J\u008c\u0001\u0010ç\u0001\u001a\b\u0012\u0004\u0012\u0002H\u00120\u0006\"\u0004\b\u0000\u0010\u00122\b\b\u0002\u0010\u0013\u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\n\b\u0002\u0010è\u0001\u001a\u00030\u0082\u00012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u001523\u0010\u0016\u001a/\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\u0018¢\u0006\f\b\u0019\u0012\b\b\u0013\u0012\u0004\b\b(\u001a\u0012\u0012\u0012\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u0002H\u0012\u0018\u00010\u001c0\u001b0\u0017H\u0082@ø\u0001\u0000¢\u0006\u0003\u0010é\u0001J!\u0010ê\u0001\u001a\u0005\u0018\u00010\u0085\u00012\u0007\u0010\u0086\u0001\u001a\u00020\t2\n\u0010ë\u0001\u001a\u0005\u0018\u00010ì\u0001H\u0002JH\u0010í\u0001\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010 \u001a\u00020\t2\u0007\u0010î\u0001\u001a\u00020\t2\b\u0010ï\u0001\u001a\u00030\u0082\u00012\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010ð\u0001JD\u0010ñ\u0001\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J?\u0010ò\u0001\u001a\t\u0012\u0005\u0012\u00030ó\u00010\u00062\u0007\u0010ô\u0001\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0003\u0010Ä\u0001R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006õ\u0001"}, d2 = {"Lcom/geely/cockpit/services/controlservice/core/controlImpl/UspControlImpl;", "Lcom/geely/cockpit/services/controlservice/core/callapi/ICarControlApi;", "uspActionExecutor", "Lcom/geely/cockpit/services/controlservice/core/executor/UspActionExecutor;", "(Lcom/geely/cockpit/services/controlservice/core/executor/UspActionExecutor;)V", "acceptCarSharing", "Lcom/geely/cockpit/services/controlservice/core/response/CarControlResult;", "", "shareId", "", "type", "remark", "statusNotify", "Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;", "callback", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;", "(Ljava/lang/String;ILjava/lang/String;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "asyncRequest", "T", "name", "areaType", "Lcom/baselinelibrary/enums/AREAType;", "createSdkRequest", "Lkotlin/Function1;", "Lcom/geely/cockpit/services/controlservice/core/action/BaseAction;", "Lkotlin/ParameterName;", "action", "Lio/reactivex/Observable;", "Lcom/geely/cockpit/services/controlservice/core/response/BaseUspResult;", "(Ljava/lang/String;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lcom/baselinelibrary/enums/AREAType;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "closeLightShow", "Lcom/ecarx/open/http/response/RemoteControlResponse;", "vin", "lightShowId", "(Ljava/lang/String;Ljava/lang/String;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "controlCharge", "chargeParam", "Lcom/geely/cockpit/services/controlservice/service/basic/function/BasicSwitchParam;", "(Lcom/geely/cockpit/services/controlservice/service/basic/function/BasicSwitchParam;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "controlClimate", "param", "Lcom/geely/cockpit/services/controlservice/service/environment/function/EnvClimateParam;", "(Lcom/geely/cockpit/services/controlservice/service/environment/function/EnvClimateParam;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "controlDefrost", "Lcom/geely/cockpit/services/controlservice/service/environment/function/EnvDefrostParam;", "(Lcom/geely/cockpit/services/controlservice/service/environment/function/EnvDefrostParam;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "controlDoor", "Lcom/geely/cockpit/services/controlservice/service/basic/function/BasicLockParam;", "(Lcom/geely/cockpit/services/controlservice/service/basic/function/BasicLockParam;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "controlEngine", "engineParam", "Lcom/geely/cockpit/services/controlservice/service/basic/function/BasicEngineParam;", "(Lcom/geely/cockpit/services/controlservice/service/basic/function/BasicEngineParam;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "controlEnvByPAA", "Lcom/geely/cockpit/services/controlservice/service/environment/function/EnvCombinedParam;", "(Lcom/geely/cockpit/services/controlservice/service/environment/function/EnvCombinedParam;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "controlEnvCombine", "Lcom/ecarx/open/http/entity/response/TempControlSetResponse;", "controlFastEnvCombine", "Lcom/geely/cockpit/services/controlservice/service/environment/function/EnvCombinedFastParam;", "(Lcom/geely/cockpit/services/controlservice/service/environment/function/EnvCombinedFastParam;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "controlPurification", "Lcom/geely/cockpit/services/controlservice/service/environment/function/EnvPurificationParam;", "(Lcom/geely/cockpit/services/controlservice/service/environment/function/EnvPurificationParam;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "controlRefrigerator", "fridgeScheduleStatus", "Lcom/geely/cockpit/services/controlservice/service/reservation/bean/FridgeScheduleStatus;", "(Lcom/geely/cockpit/services/controlservice/service/reservation/bean/FridgeScheduleStatus;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "controlSeat", "Lcom/geely/cockpit/services/controlservice/service/environment/function/EnvSeatParam;", "(Lcom/geely/cockpit/services/controlservice/service/environment/function/EnvSeatParam;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "controlSunroof", "sunroofParam", "controlSunshade", "sunshadeParam", "controlTrunk", "controlWinVentilation", "winVenParam", "controlWindow", "windowParam", "createCarShare", "requestParam", "Lcom/geely/galaxy/carcontrol/core/functiontools/control/bean/CreateCarShareBean;", "(Lcom/geely/galaxy/carcontrol/core/functiontools/control/bean/CreateCarShareBean;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAbnormalVideo", "request", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/AbnormalVideoListRequest;", "(Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/AbnormalVideoListRequest;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteJournalLog", "logs", "", "Lcom/geely/galaxy/carcontrol/core/functiontools/control/bean/Ge_JourneyLogRequest;", "(Ljava/lang/String;Ljava/util/List;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteLightShow", "downloadLightShow", "electronicFenceCreate", "bean", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ElectronicFenceCreateBean;", "(Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ElectronicFenceCreateBean;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "electronicFenceDelete", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ElectronicFenceDeleteBean;", "(Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ElectronicFenceDeleteBean;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "electronicFenceQuery", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ElectronicFenceQueryResponseBean;", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ElectronicFenceQueryBean;", "(Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ElectronicFenceQueryBean;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "electronicFenceUpdate", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ElectronicFenceUpdateBean;", "(Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ElectronicFenceUpdateBean;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findCar", "Lcom/geely/cockpit/services/controlservice/service/basic/function/BasicFindCarParam;", "(Lcom/geely/cockpit/services/controlservice/service/basic/function/BasicFindCarParam;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAbnormalVideoList", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/AbnormalVideoResult;", "getBatteryBasic", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/BatteryBasicResponse;", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/BatteryBasicRequest;", "(Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/BatteryBasicRequest;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBatteryRecord", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/BatteryRecordResponse;", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/BatteryRecordRequest;", "(Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/BatteryRecordRequest;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBatteryReport", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/BatteryReportResponse;", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/BatteryReportRequest;", "(Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/BatteryReportRequest;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBeSharedDetail", "Lcom/geely/galaxy/carcontrol/core/functiontools/control/bean/BeSharedBean;", "recordId", "isOwner", "", "(Ljava/lang/String;ZLcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCacheMessage", "Lcom/geely/cockpit/services/controlservice/core/support/push/PushMessageNew;", "sessionId", "(Ljava/lang/String;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCarShareList", "Lcom/geely/galaxy/carcontrol/core/functiontools/control/bean/CarShareList;", "getChargeSOC", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ReservationGetLastSocResponseBean;", "Lcom/geely/cockpit/services/controlservice/core/callapi/SocType;", "(Lcom/geely/cockpit/services/controlservice/core/callapi/SocType;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getJournalLogList", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/JourneyListBean;", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/JourLogQueryBean;", "isVersionOne", "(Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/JourLogQueryBean;ZLcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getJournalLogTrack", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/JourneyTrack;", "(Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/JourLogQueryBean;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMergeBeforeChargingState", "(Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMergeChargingState", "Lcom/geely/cockpit/services/controlservice/core/callapi/MergeChargeStatus;", "getParkPhoto", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ParkPhotoResponse;", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/VehicleParkPhotoRequest;", "(Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/VehicleParkPhotoRequest;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getParkPosition", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/ParkPositionResponse;", "getSharedUnhandledList", "", "getUnreadShareCount", "getVehicleCapability", "Lcom/geely/cockpit/services/controlservice/bean/PlatformCapacity;", "vehicleType", "(Ljava/lang/String;ILcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getVehicleStatusGDPR", "Lcom/geely/cockpit/services/controlservice/bean/PlatformVehicleStatus;", "latest", "target", "getVehicleSwitch", "Lcom/geely/cockpit/services/controlservice/bean/PlatformVehicleSwitch;", "iovAuthorizationSet", "Lcom/ecarx/open/http/response/AuthorizationResult;", "serviceCode", "authStatus", "(Ljava/lang/String;Ljava/lang/Integer;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "iovChargingConfigLastQuery", "Lcom/ecarx/open/http/entity/LastSetChargeBean;", "Lcom/geely/cockpit/services/controlservice/core/callapi/ScheduleChargeBizType;", "(Lcom/geely/cockpit/services/controlservice/core/callapi/ScheduleChargeBizType;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "iovGetAuthorizationStatus", "Lcom/ecarx/open/http/entity/AuthorizationStatus;", "codeList", "(Ljava/util/List;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "iovGetOperateRecords", "Lcom/ecarx/open/http/response/GDPRRecordInfo;", "startTime", "", "endTime", "pageSize", "pageIndex", "(Ljava/lang/String;JJIILcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "login", "accessToken", "(Ljava/lang/String;Lcom/baselinelibrary/enums/AREAType;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "openLightShow", "queryChargeReservation", "Lcom/geely/cockpit/services/controlservice/service/reservation/bean/ReservationChargeInfo;", "queryRefrigerator", "Lcom/geely/cockpit/services/controlservice/service/reservation/bean/ReservationFridgeInfo;", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/RefrigeratorQueryRequest;", "(Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/RefrigeratorQueryRequest;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "queryTravelReservation", "Lcom/geely/cockpit/services/controlservice/service/reservation/bean/ReservationTravelInfo;", "readBeShared", "(JLcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "readShare", "rejectCarSharing", "sendCarHeartBeat", "sendToCar", "", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/SendToCarBean;", "(Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/SendToCarBean;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setCarOnOffline", "setChargeReservation", "Lcom/ecarx/open/http/entity/ChargingSetBean;", "(Lcom/geely/cockpit/services/controlservice/service/reservation/bean/ReservationChargeInfo;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setChargeSOC", "socValue", "(ILcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setDefaultVehicle", "newVin", com.geely.galaxy.mine.constant.RouterParams.personId, "(Ljava/lang/String;Ljava/lang/String;Lcom/baselinelibrary/enums/AREAType;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setTravelReservation", "(Lcom/geely/cockpit/services/controlservice/service/reservation/bean/ReservationTravelInfo;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setVehicleSwitch", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/VehicleControlSwitchBean;", "(Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/VehicleControlSwitchBean;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncRequest", "cancelable", "(Ljava/lang/String;Lcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;ZLcom/baselinelibrary/enums/AREAType;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "transQueryResultToPushMessage", "responseBean", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/bean/VehicleGetResultResponseVO;", "unbindShare", "sharedUserId", "forceStop", "(Ljava/lang/String;Ljava/lang/String;ZLcom/geely/cockpit/libs/interfaces/services/asynctask/StatusNotify;Lcom/geely/galaxy/carcontrol/core/vehicle/usp/center/callback/ControlCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateShareStatus", "vehicleList", "Lcom/ecarx/open/http/response/TcVehicleProfileResponse;", "needSharedCar", "LibCarControlNew_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public class UspControlImpl implements com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi {
    private final com.geely.cockpit.services.controlservice.core.executor.UspActionExecutor uspActionExecutor;

    /* JADX WARN: Illegal instructions before constructor call */
    public UspControlImpl() {
        com.geely.cockpit.services.controlservice.core.executor.UspActionExecutor uspActionExecutor = null;
        this(uspActionExecutor, 1, uspActionExecutor);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object acceptCarSharing(java.lang.String str, int i, java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Integer>> continuation) {
        return acceptCarSharing$suspendImpl(this, str, i, str2, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object closeLightShow(java.lang.String str, java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return closeLightShow$suspendImpl(this, str, str2, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlCharge(com.geely.cockpit.services.controlservice.service.basic.function.BasicSwitchParam basicSwitchParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return controlCharge$suspendImpl(this, basicSwitchParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlClimate(com.geely.cockpit.services.controlservice.service.environment.function.EnvClimateParam envClimateParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return controlClimate$suspendImpl(this, envClimateParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlDefrost(com.geely.cockpit.services.controlservice.service.environment.function.EnvDefrostParam envDefrostParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return controlDefrost$suspendImpl(this, envDefrostParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlDoor(com.geely.cockpit.services.controlservice.service.basic.function.BasicLockParam basicLockParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return controlDoor$suspendImpl(this, basicLockParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlEngine(com.geely.cockpit.services.controlservice.service.basic.function.BasicEngineParam basicEngineParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return controlEngine$suspendImpl(this, basicEngineParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlEnvByPAA(com.geely.cockpit.services.controlservice.service.environment.function.EnvCombinedParam envCombinedParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return controlEnvByPAA$suspendImpl(this, envCombinedParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlEnvCombine(com.geely.cockpit.services.controlservice.service.environment.function.EnvCombinedParam envCombinedParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.entity.response.TempControlSetResponse>> continuation) {
        return controlEnvCombine$suspendImpl(this, envCombinedParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlFastEnvCombine(com.geely.cockpit.services.controlservice.service.environment.function.EnvCombinedFastParam envCombinedFastParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.entity.response.TempControlSetResponse>> continuation) {
        return controlFastEnvCombine$suspendImpl(this, envCombinedFastParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlPurification(com.geely.cockpit.services.controlservice.service.environment.function.EnvPurificationParam envPurificationParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return controlPurification$suspendImpl(this, envPurificationParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlRefrigerator(com.geely.cockpit.services.controlservice.service.reservation.bean.FridgeScheduleStatus fridgeScheduleStatus, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return controlRefrigerator$suspendImpl(this, fridgeScheduleStatus, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlSeat(com.geely.cockpit.services.controlservice.service.environment.function.EnvSeatParam envSeatParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return controlSeat$suspendImpl(this, envSeatParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlSunroof(com.geely.cockpit.services.controlservice.service.basic.function.BasicSwitchParam basicSwitchParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return controlSunroof$suspendImpl(this, basicSwitchParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlSunshade(com.geely.cockpit.services.controlservice.service.basic.function.BasicSwitchParam basicSwitchParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return controlSunshade$suspendImpl(this, basicSwitchParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlTrunk(com.geely.cockpit.services.controlservice.service.basic.function.BasicSwitchParam basicSwitchParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return controlTrunk$suspendImpl(this, basicSwitchParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlWinVentilation(com.geely.cockpit.services.controlservice.service.basic.function.BasicSwitchParam basicSwitchParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return controlWinVentilation$suspendImpl(this, basicSwitchParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlWindow(com.geely.cockpit.services.controlservice.service.basic.function.BasicSwitchParam basicSwitchParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return controlWindow$suspendImpl(this, basicSwitchParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object createCarShare(com.geely.galaxy.carcontrol.core.functiontools.control.bean.CreateCarShareBean createCarShareBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.String>> continuation) {
        return createCarShare$suspendImpl(this, createCarShareBean, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object deleteAbnormalVideo(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.AbnormalVideoListRequest abnormalVideoListRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.String>> continuation) {
        return deleteAbnormalVideo$suspendImpl(this, abnormalVideoListRequest, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object deleteJournalLog(java.lang.String str, java.util.List<? extends com.geely.galaxy.carcontrol.core.functiontools.control.bean.Ge_JourneyLogRequest> list, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.String>> continuation) {
        return deleteJournalLog$suspendImpl(this, str, list, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object deleteLightShow(java.lang.String str, java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return deleteLightShow$suspendImpl(this, str, str2, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object downloadLightShow(java.lang.String str, java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return downloadLightShow$suspendImpl(this, str, str2, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object electronicFenceCreate(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ElectronicFenceCreateBean electronicFenceCreateBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.String>> continuation) {
        return electronicFenceCreate$suspendImpl(this, electronicFenceCreateBean, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object electronicFenceDelete(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ElectronicFenceDeleteBean electronicFenceDeleteBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.String>> continuation) {
        return electronicFenceDelete$suspendImpl(this, electronicFenceDeleteBean, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object electronicFenceQuery(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ElectronicFenceQueryBean electronicFenceQueryBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ElectronicFenceQueryResponseBean>> continuation) {
        return electronicFenceQuery$suspendImpl(this, electronicFenceQueryBean, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object electronicFenceUpdate(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ElectronicFenceUpdateBean electronicFenceUpdateBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.String>> continuation) {
        return electronicFenceUpdate$suspendImpl(this, electronicFenceUpdateBean, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object findCar(com.geely.cockpit.services.controlservice.service.basic.function.BasicFindCarParam basicFindCarParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return findCar$suspendImpl(this, basicFindCarParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getAbnormalVideoList(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.AbnormalVideoListRequest abnormalVideoListRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.AbnormalVideoResult>> continuation) {
        return getAbnormalVideoList$suspendImpl(this, abnormalVideoListRequest, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getBatteryBasic(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryBasicRequest batteryBasicRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryBasicResponse>> continuation) {
        return getBatteryBasic$suspendImpl(this, batteryBasicRequest, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getBatteryRecord(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryRecordRequest batteryRecordRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryRecordResponse>> continuation) {
        return getBatteryRecord$suspendImpl(this, batteryRecordRequest, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getBatteryReport(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryReportRequest batteryReportRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryReportResponse>> continuation) {
        return getBatteryReport$suspendImpl(this, batteryReportRequest, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getBeSharedDetail(java.lang.String str, boolean z, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.functiontools.control.bean.BeSharedBean>> continuation) {
        return getBeSharedDetail$suspendImpl(this, str, z, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getCacheMessage(java.lang.String str, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.util.List<com.geely.cockpit.services.controlservice.core.support.push.PushMessageNew>>> continuation) {
        return getCacheMessage$suspendImpl(this, str, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getCarShareList(java.lang.String str, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.functiontools.control.bean.CarShareList>> continuation) {
        return getCarShareList$suspendImpl(this, str, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getChargeSOC(com.geely.cockpit.services.controlservice.core.callapi.SocType socType, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ReservationGetLastSocResponseBean>> continuation) {
        return getChargeSOC$suspendImpl(this, socType, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getJournalLogList(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourLogQueryBean jourLogQueryBean, boolean z, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourneyListBean>> continuation) {
        return getJournalLogList$suspendImpl(this, jourLogQueryBean, z, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getJournalLogTrack(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourLogQueryBean jourLogQueryBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourneyTrack>> continuation) {
        return getJournalLogTrack$suspendImpl(this, jourLogQueryBean, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getMergeBeforeChargingState(com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.util.List<java.lang.String>>> continuation) {
        return getMergeBeforeChargingState$suspendImpl(this, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getMergeChargingState(com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.cockpit.services.controlservice.core.callapi.MergeChargeStatus>> continuation) {
        return getMergeChargingState$suspendImpl(this, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getParkPhoto(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleParkPhotoRequest vehicleParkPhotoRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ParkPhotoResponse>> continuation) {
        return getParkPhoto$suspendImpl(this, vehicleParkPhotoRequest, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getParkPosition(java.lang.String str, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ParkPositionResponse>> continuation) {
        return getParkPosition$suspendImpl(this, str, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getSharedUnhandledList(com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.util.List<com.geely.galaxy.carcontrol.core.functiontools.control.bean.BeSharedBean>>> continuation) {
        return getSharedUnhandledList$suspendImpl(this, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getUnreadShareCount(java.lang.String str, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Integer>> continuation) {
        return getUnreadShareCount$suspendImpl(this, str, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getVehicleCapability(java.lang.String str, int i, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.cockpit.services.controlservice.bean.PlatformCapacity>> continuation) {
        return getVehicleCapability$suspendImpl(this, str, i, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getVehicleStatusGDPR(java.lang.String str, java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus>> continuation) {
        return getVehicleStatusGDPR$suspendImpl(this, str, str2, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getVehicleSwitch(com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.cockpit.services.controlservice.bean.PlatformVehicleSwitch>> continuation) {
        return getVehicleSwitch$suspendImpl(this, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object iovAuthorizationSet(java.lang.String str, java.lang.Integer num, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.AuthorizationResult>> continuation) {
        return iovAuthorizationSet$suspendImpl(this, str, num, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object iovChargingConfigLastQuery(com.geely.cockpit.services.controlservice.core.callapi.ScheduleChargeBizType scheduleChargeBizType, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.entity.LastSetChargeBean>> continuation) {
        return iovChargingConfigLastQuery$suspendImpl(this, scheduleChargeBizType, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object iovGetAuthorizationStatus(java.util.List<java.lang.String> list, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.util.List<com.ecarx.open.http.entity.AuthorizationStatus>>> continuation) {
        return iovGetAuthorizationStatus$suspendImpl(this, list, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object iovGetOperateRecords(java.lang.String str, long j, long j2, int i, int i2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.GDPRRecordInfo>> continuation) {
        return iovGetOperateRecords$suspendImpl(this, str, j, j2, i, i2, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object login(java.lang.String str, com.baselinelibrary.enums.AREAType aREAType, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Boolean>> continuation) {
        return login$suspendImpl(this, str, aREAType, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object openLightShow(java.lang.String str, java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return openLightShow$suspendImpl(this, str, str2, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object queryChargeReservation(com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationChargeInfo>> continuation) {
        return queryChargeReservation$suspendImpl(this, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object queryRefrigerator(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.RefrigeratorQueryRequest refrigeratorQueryRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationFridgeInfo>> continuation) {
        return queryRefrigerator$suspendImpl(this, refrigeratorQueryRequest, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object queryTravelReservation(com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationTravelInfo>> continuation) {
        return queryTravelReservation$suspendImpl(this, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object readBeShared(long j, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Integer>> continuation) {
        return readBeShared$suspendImpl(this, j, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object readShare(com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Integer>> continuation) {
        return readShare$suspendImpl(this, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object rejectCarSharing(java.lang.String str, int i, java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Integer>> continuation) {
        return rejectCarSharing$suspendImpl(this, str, i, str2, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object sendCarHeartBeat(java.lang.String str, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Boolean>> continuation) {
        return sendCarHeartBeat$suspendImpl(this, str, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object sendToCar(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.SendToCarBean sendToCarBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Object>> continuation) {
        return sendToCar$suspendImpl(this, sendToCarBean, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object setCarOnOffline(java.lang.String str, java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Boolean>> continuation) {
        return setCarOnOffline$suspendImpl(this, str, str2, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object setChargeReservation(com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationChargeInfo reservationChargeInfo, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.entity.ChargingSetBean>> continuation) {
        return setChargeReservation$suspendImpl(this, reservationChargeInfo, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object setChargeSOC(int i, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return setChargeSOC$suspendImpl(this, i, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object setDefaultVehicle(java.lang.String str, java.lang.String str2, com.baselinelibrary.enums.AREAType aREAType, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Object>> continuation) {
        return setDefaultVehicle$suspendImpl(this, str, str2, aREAType, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object setTravelReservation(com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationTravelInfo reservationTravelInfo, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.entity.ChargingSetBean>> continuation) {
        return setTravelReservation$suspendImpl(this, reservationTravelInfo, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object setVehicleSwitch(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleControlSwitchBean vehicleControlSwitchBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return setVehicleSwitch$suspendImpl(this, vehicleControlSwitchBean, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object unbindShare(java.lang.String str, java.lang.String str2, boolean z, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Integer>> continuation) {
        return unbindShare$suspendImpl(this, str, str2, z, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object updateShareStatus(java.lang.String str, int i, java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Integer>> continuation) {
        return updateShareStatus$suspendImpl(this, str, i, str2, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object vehicleList(java.lang.String str, com.baselinelibrary.enums.AREAType aREAType, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.TcVehicleProfileResponse>> continuation) {
        return vehicleList$suspendImpl(this, str, aREAType, statusNotify, controlCallback, continuation);
    }

    public UspControlImpl(com.geely.cockpit.services.controlservice.core.executor.UspActionExecutor uspActionExecutor) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(uspActionExecutor, "uspActionExecutor");
        this.uspActionExecutor = uspActionExecutor;
    }

    public /* synthetic */ UspControlImpl(com.geely.cockpit.services.controlservice.core.executor.UspActionExecutor uspActionExecutor, int i, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? com.geely.cockpit.services.controlservice.core.executor.UspActionExecutor.INSTANCE : uspActionExecutor);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlChargeV1(com.geely.cockpit.services.controlservice.service.basic.function.BasicChargeCarParam basicChargeCarParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.controlChargeV1(this, basicChargeCarParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlChargingCap(com.geely.cockpit.services.controlservice.service.basic.function.BasicChargingCapParam basicChargingCapParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.controlChargingCap(this, basicChargingCapParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlDoorOpen(com.geely.cockpit.services.controlservice.service.basic.function.BasicSwitchParam basicSwitchParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.controlDoorOpen(this, basicSwitchParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlFragrance(com.geely.cockpit.services.controlservice.service.environment.function.EnvFragranceParam envFragranceParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.controlFragrance(this, envFragranceParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlHood(com.geely.cockpit.services.controlservice.service.basic.function.BasicSwitchParam basicSwitchParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.controlHood(this, basicSwitchParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object controlSeatsMode(com.geely.cockpit.services.controlservice.service.basic.function.BasicSeatsModeParam basicSeatsModeParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.controlSeatsMode(this, basicSeatsModeParam, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object deletePickupFriend(java.lang.String str, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.ServiceResult>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.deletePickupFriend(this, str, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getFragranceInfo(com.geely.cockpit.services.controlservice.service.environment.function.FragranceInfoQueryRequest fragranceInfoQueryRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.FragranceResponse>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.getFragranceInfo(this, fragranceInfoQueryRequest, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getFuelRank(double d, double d2, int i, java.lang.String str, int i2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RankingResponse>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.getFuelRank(this, d, d2, i, str, i2, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getMileageRank(double d, double d2, int i, java.lang.String str, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RankingResponse>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.getMileageRank(this, d, d2, i, str, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getOneVehicleCapability(java.lang.String str, int i, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.snc.basevehicle.bean.GLCapabilityBean>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.getOneVehicleCapability(this, str, i, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getReservationV1(com.geely.cockpit.services.controlservice.core.callapi.ScheduleChargeBizType scheduleChargeBizType, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.entity.ChargingResultBean>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.getReservationV1(this, scheduleChargeBizType, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object getVehiclePosition(com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.getVehiclePosition(this, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object iovChargingReservation(com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.TscRecIhuHistoryResponse>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.iovChargingReservation(this, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object qrLogin(java.lang.String str, java.lang.String str2, java.lang.String str3, com.baselinelibrary.enums.AREAType aREAType, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Object>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.qrLogin(this, str, str2, str3, aREAType, statusNotify, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object sendPeCmd(int i, int i2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.sendPeCmd(this, i, i2, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object sendRcvCmd(byte b, byte b2, byte b3, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.sendRcvCmd(this, b, b2, b3, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object setChargeReservationV1(com.ecarx.open.http.request.RemoteControlRequest remoteControlRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.setChargeReservationV1(this, remoteControlRequest, statusNotify, controlCallback, continuation);
    }

    @Override // com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi
    public java.lang.Object setReservation(com.ecarx.open.http.entity.request.IovChargingSetRequest iovChargingSetRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.entity.ChargingSetBean>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.setReservation(this, iovChargingSetRequest, statusNotify, controlCallback, continuation);
    }

    static /* synthetic */ java.lang.Object syncRequest$default(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, java.lang.String str, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, boolean z, com.baselinelibrary.enums.AREAType aREAType, kotlin.jvm.functions.Function1 function1, kotlin.coroutines.Continuation continuation, int i, java.lang.Object obj) {
        if (obj == null) {
            return uspControlImpl.syncRequest((i & 1) != 0 ? "" : str, statusNotify, (i & 4) != 0 ? null : controlCallback, (i & 8) != 0 ? true : z, (i & 16) != 0 ? null : aREAType, function1, continuation);
        }
        throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: syncRequest");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final <T> java.lang.Object syncRequest(final java.lang.String str, final com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, final com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, final boolean z, com.baselinelibrary.enums.AREAType aREAType, final kotlin.jvm.functions.Function1<? super com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, ? extends io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<T>>> function1, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<T>> continuation) {
        return new com.geely.cockpit.services.controlservice.core.action.usp.UspSyncAction<T>(str, statusNotify, z, controlCallback) { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl$syncRequest$action$1
            @Override // com.geely.cockpit.services.controlservice.core.action.BaseAction
            public io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<T>> requestUspObservable() {
                return (io.reactivex.Observable) function1.invoke(this);
            }
        }.execute$LibCarControlNew_release(this.uspActionExecutor, aREAType, continuation);
    }

    static /* synthetic */ java.lang.Object asyncRequest$default(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, java.lang.String str, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, com.baselinelibrary.enums.AREAType aREAType, kotlin.jvm.functions.Function1 function1, kotlin.coroutines.Continuation continuation, int i, java.lang.Object obj) {
        if (obj != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: asyncRequest");
        }
        if ((i & 1) != 0) {
            str = "";
        }
        java.lang.String str2 = str;
        if ((i & 8) != 0) {
            aREAType = null;
        }
        return uspControlImpl.asyncRequest(str2, statusNotify, controlCallback, aREAType, function1, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final <T> java.lang.Object asyncRequest(final java.lang.String str, final com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, final com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, com.baselinelibrary.enums.AREAType aREAType, final kotlin.jvm.functions.Function1<? super com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, ? extends io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<T>>> function1, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<T>> continuation) {
        return new com.geely.cockpit.services.controlservice.core.action.usp.UspAsyncAction<java.lang.Object, T>(str, statusNotify, controlCallback) { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl$asyncRequest$action$1
            @Override // com.geely.cockpit.services.controlservice.core.action.BaseAction
            public io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<T>> requestUspObservable() {
                return (io.reactivex.Observable) function1.invoke(this);
            }
        }.execute$LibCarControlNew_release(this.uspActionExecutor, aREAType, continuation);
    }

    static /* synthetic */ java.lang.Object login$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, java.lang.String str, com.baselinelibrary.enums.AREAType aREAType, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Boolean>> continuation) {
        return com.geely.cockpit.services.controlservice.core.response.CarControlResult.Companion.buildSuccess$default(com.geely.cockpit.services.controlservice.core.response.CarControlResult.INSTANCE, 0, 1, null);
    }

    static /* synthetic */ java.lang.Object getVehicleCapability$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, int i, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.cockpit.services.controlservice.bean.PlatformCapacity>> continuation) {
        return syncRequest$default(uspControlImpl, "getVehicleCapability", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.bean.PlatformCapacity>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getVehicleCapability.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.bean.PlatformCapacity>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.util.HashMap map = new java.util.HashMap();
                map.put("vin", str);
                map.put("clientType", 2);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getControlCapability(map), new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.bean.parser.UspCapabilityResponseBean, com.geely.cockpit.services.controlservice.bean.PlatformCapacity>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getVehicleCapability.2.1
                    public final com.geely.cockpit.services.controlservice.bean.PlatformCapacity invoke(com.geely.cockpit.services.controlservice.bean.parser.UspCapabilityResponseBean uspCapabilityResponseBean) {
                        return com.geely.cockpit.services.controlservice.bean.parser.PlatformCapabilityParser.INSTANCE.parseUspCapability$LibCarControlNew_release(uspCapabilityResponseBean);
                    }
                });
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object getVehicleStatusGDPR$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, java.lang.String str, java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus>> continuation) {
        final java.lang.String vin = com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin();
        return syncRequest$default(uspControlImpl, "getVehicleStatus", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getVehicleStatusGDPR.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.util.HashMap map = new java.util.HashMap();
                map.put("clientType", 2);
                map.put("dataTypeList", kotlin.collections.CollectionsKt.mutableListOf(new java.lang.String[]{"all"}));
                map.put("statusType", "local");
                map.put("vin", vin);
                map.put("openUserId", com.geely.cockpit.services.controlservice.runtime.CurUserInfo.getUserId());
                io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus>> platformVehicleStatus = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getPlatformVehicleStatus(map);
                final java.lang.String str3 = vin;
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(platformVehicleStatus, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus, com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getVehicleStatusGDPR.2.1
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(1);
                    }

                    public final com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus invoke(com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus platformVehicleStatus2) {
                        if (platformVehicleStatus2 != null) {
                            return platformVehicleStatus2.copy((8388599 & 1) != 0 ? platformVehicleStatus2.vin : str3, (8388599 & 2) != 0 ? platformVehicleStatus2.carFromSource : com.geely.cockpit.services.controlservice.bean.PlatformType.USP.getKey(), (8388599 & 4) != 0 ? platformVehicleStatus2.updateTime : null, (8388599 & 8) != 0 ? platformVehicleStatus2.basicVehicleStatus : null, (8388599 & 16) != 0 ? platformVehicleStatus2.vehicleBatteryStatus : null, (8388599 & 32) != 0 ? platformVehicleStatus2.vehicleClimateStatus : null, (8388599 & 64) != 0 ? platformVehicleStatus2.vehicleDoorCoverStatus : null, (8388599 & 128) != 0 ? platformVehicleStatus2.vehicleEngineStatus : null, (8388599 & com.ecarx.iwallsdk.crypto.param.CryptParam.ECC_KEY_LEN) != 0 ? platformVehicleStatus2.vehicleEnvironmentStatus : null, (8388599 & 512) != 0 ? platformVehicleStatus2.vehicleFragranceStatus : null, (8388599 & com.carlinx.module_show.ui.fragment.ImportMusicFragment.VIDEO_REQUEST_CODE) != 0 ? platformVehicleStatus2.vehicleHeadLightStatus : null, (8388599 & 2048) != 0 ? platformVehicleStatus2.vehicleIndicatorLightStatus : null, (8388599 & 4096) != 0 ? platformVehicleStatus2.vehicleLocationStatus : null, (8388599 & 8192) != 0 ? platformVehicleStatus2.vehicleMaintainStatus : null, (8388599 & 16384) != 0 ? platformVehicleStatus2.vehicleRunningStatus : null, (8388599 & 32768) != 0 ? platformVehicleStatus2.vehicleSafeStatus : null, (8388599 & 65536) != 0 ? platformVehicleStatus2.vehicleTcamStatus : null, (8388599 & 131072) != 0 ? platformVehicleStatus2.vehicleTyreStatus : null, (8388599 & 262144) != 0 ? platformVehicleStatus2.vehicleWindowStatus : null, (8388599 & 524288) != 0 ? platformVehicleStatus2.vehicleBleExtra : null, (8388599 & 1048576) != 0 ? platformVehicleStatus2.vehicleBigdataStatus : null, (8388599 & 2097152) != 0 ? platformVehicleStatus2.vehicleSwitch : null, (8388599 & 4194304) != 0 ? platformVehicleStatus2.shadowTspVehicleStatus : null);
                        }
                        return null;
                    }
                });
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object setDefaultVehicle$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, final java.lang.String str2, com.baselinelibrary.enums.AREAType aREAType, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Object>> continuation) {
        return syncRequest$default(uspControlImpl, "设置默认车辆", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Object>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.setDefaultVehicle.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Object>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.util.HashMap<java.lang.String, java.lang.Object> map = new java.util.HashMap<>();
                java.util.HashMap<java.lang.String, java.lang.Object> map2 = map;
                map2.put("vin", str);
                if (!com.geely.cockpit.services.controlservice.runtime.RuntimeUtilsKt.isGeelyApp()) {
                    map2.put("userAccount", str2);
                }
                baseAction.setupLoggerRequestParam(map);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setDefaultCar(map2), new kotlin.jvm.functions.Function1<java.lang.String, java.lang.Object>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.setDefaultVehicle.2.1
                    public final java.lang.Object invoke(java.lang.String str3) {
                        return str3;
                    }
                });
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object getChargeSOC$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.services.controlservice.core.callapi.SocType socType, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ReservationGetLastSocResponseBean>> continuation) {
        return syncRequest$default(uspControlImpl, "查询SOC", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ReservationGetLastSocResponseBean>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getChargeSOC.2
            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ReservationGetLastSocResponseBean>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ReservationGetLastSocBean((java.lang.Integer) null, (java.lang.String) null, (java.lang.String) null, 1, (java.lang.Integer) null, (java.lang.String) null, com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin(), 55, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "controlBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getReservationLostSoc(json);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object setChargeSOC$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final int i, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return asyncRequest$default(uspControlImpl, "设置SOC", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.setChargeSOC.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ReservationChargeConfigBean((java.lang.Integer) null, (java.lang.String) null, com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin(), 1, java.lang.String.valueOf(i), 3, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "jsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.reservationChargeConfig(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.setChargeSOC.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object queryTravelReservation$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationTravelInfo>> continuation) {
        return syncRequest$default(uspControlImpl, "查询预约出行", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationTravelInfo>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.queryTravelReservation.2
            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationTravelInfo>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ReservationTravelBean reservationTravelBean = new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ReservationTravelBean((java.lang.Integer) null, (java.lang.String) null, (java.lang.Integer) null, (java.lang.Boolean) null, (java.lang.Boolean) null, (java.lang.Long) null, (java.lang.String) null, (java.lang.Boolean) null, (java.lang.Integer) null, (java.lang.Integer) null, (java.lang.String) null, com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin(), 2047, (kotlin.jvm.internal.DefaultConstructorMarker) null);
                com.geely.cockpit.services.controlservice.core.uspapi.UspApiService uspApiService = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE;
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(reservationTravelBean);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "toJson(bean)");
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(uspApiService.queryReservationTravel(json), new kotlin.jvm.functions.Function1<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.QueryReservationTravelResponseBean, com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationTravelInfo>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.queryTravelReservation.2.1
                    public final com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationTravelInfo invoke(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.QueryReservationTravelResponseBean queryReservationTravelResponseBean) {
                        return com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationTravelInfo.INSTANCE.transUspBean(queryReservationTravelResponseBean);
                    }
                });
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object queryChargeReservation$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationChargeInfo>> continuation) {
        return syncRequest$default(uspControlImpl, "查询预约充电", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationChargeInfo>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.queryChargeReservation.2
            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationChargeInfo>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ReservationChargeQueryBean reservationChargeQueryBean = new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ReservationChargeQueryBean((java.lang.Integer) null, (java.lang.String) null, com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin(), 3, (kotlin.jvm.internal.DefaultConstructorMarker) null);
                com.geely.cockpit.services.controlservice.core.uspapi.UspApiService uspApiService = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE;
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(reservationChargeQueryBean);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "toJson(bean)");
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(uspApiService.getReservationChargeQuery(json), new kotlin.jvm.functions.Function1<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.UspChargingResultBean, com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationChargeInfo>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.queryChargeReservation.2.1
                    public final com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationChargeInfo invoke(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.UspChargingResultBean uspChargingResultBean) {
                        return com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationChargeInfo.INSTANCE.transUspBean(uspChargingResultBean != null ? uspChargingResultBean.getChargingTask() : null);
                    }
                });
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object setChargeReservation$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationChargeInfo reservationChargeInfo, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.entity.ChargingSetBean>> continuation) {
        return asyncRequest$default(uspControlImpl, "设置预约出行", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.entity.ChargingSetBean>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.setChargeReservation.2
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.entity.ChargingSetBean>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(reservationChargeInfo.toUspSetBean());
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "jsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.reservationCharge(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.entity.ChargingSetBean>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.setChargeReservation.2.1
                    public final com.ecarx.open.http.entity.ChargingSetBean invoke(java.lang.String str) {
                        com.ecarx.open.http.entity.ChargingSetBean chargingSetBean = new com.ecarx.open.http.entity.ChargingSetBean();
                        chargingSetBean.setSessionId(str);
                        return chargingSetBean;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object setTravelReservation$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationTravelInfo reservationTravelInfo, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.entity.ChargingSetBean>> continuation) {
        return asyncRequest$default(uspControlImpl, "设置预约充电", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.entity.ChargingSetBean>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.setTravelReservation.2
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.entity.ChargingSetBean>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                new java.util.HashMap().put("vin", com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin());
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(reservationTravelInfo.toUspSetBean());
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "jsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setReservationTravel(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.entity.ChargingSetBean>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.setTravelReservation.2.1
                    public final com.ecarx.open.http.entity.ChargingSetBean invoke(java.lang.String str) {
                        com.ecarx.open.http.entity.ChargingSetBean chargingSetBean = new com.ecarx.open.http.entity.ChargingSetBean();
                        chargingSetBean.setSessionId(str);
                        return chargingSetBean;
                    }
                });
            }
        }, continuation, 8, null);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NotImplementedError */
    static /* synthetic */ java.lang.Object iovChargingConfigLastQuery$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.services.controlservice.core.callapi.ScheduleChargeBizType scheduleChargeBizType, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.entity.LastSetChargeBean>> continuation) throws kotlin.NotImplementedError {
        throw new kotlin.NotImplementedError("An operation is not implemented: Not yet implemented");
    }

    static /* synthetic */ java.lang.Object controlEngine$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.cockpit.services.controlservice.service.basic.function.BasicEngineParam basicEngineParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return asyncRequest$default(uspControlImpl, "引擎指令", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlEngine.2
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.util.HashMap<java.lang.String, java.lang.Object> map = new java.util.HashMap<>();
                java.util.HashMap<java.lang.String, java.lang.Object> map2 = map;
                map2.put("vin", com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin());
                map2.put("operation", basicEngineParam.getCmdType().isStop() ? "0" : com.ecarx.open.http.entity.Capability.ConfigConstant.CAR_MODEL_DEFAULT);
                map2.put("duration", "60");
                map2.put("engStrtType", com.ecarx.open.http.entity.Capability.ConfigConstant.CAR_MODEL_DEFAULT);
                map2.put("clientType", 2);
                map2.put("platform", "2.0");
                baseAction.setupLoggerRequestParam(map);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setControlEngine(map2), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlEngine.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object controlCharge$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.cockpit.services.controlservice.service.basic.function.BasicSwitchParam basicSwitchParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return asyncRequest$default(uspControlImpl, "充放电指令", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlCharge.2
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.StopReservationChargeChargeConfigBean((java.lang.Integer) null, basicSwitchParam.getCmdType().isStop() ? "stop" : "start", com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin(), 1, 1, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "jsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.reservationChargeStart(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlCharge.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object controlDoor$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.cockpit.services.controlservice.service.basic.function.BasicLockParam basicLockParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return asyncRequest$default(uspControlImpl, "解闭锁指令", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlDoor.2
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleControlDoorBean((java.lang.Integer) null, (java.lang.String) null, (java.lang.String) null, (java.lang.String) null, 0, (java.lang.Integer) null, java.lang.Integer.valueOf(basicLockParam.getCmdType().getUspProtocolValue()), (java.lang.String) null, com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin(), 175, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonStr");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setControlDoor(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlDoor.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object controlTrunk$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.cockpit.services.controlservice.service.basic.function.BasicSwitchParam basicSwitchParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return asyncRequest$default(uspControlImpl, "后备箱指令", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlTrunk.2
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleControlDoorBean((java.lang.Integer) null, (java.lang.String) null, (java.lang.String) null, (java.lang.String) null, (java.lang.Integer) null, 1, java.lang.Integer.valueOf(basicSwitchParam.getCmdType().getUspProtocolValue()), (java.lang.String) null, com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin(), 143, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonStr");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setControlDoor(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlTrunk.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object findCar$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.cockpit.services.controlservice.service.basic.function.BasicFindCarParam basicFindCarParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return asyncRequest$default(uspControlImpl, "寻车指令", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.findCar.2
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleControlSearchBean((java.lang.Integer) null, (java.lang.String) null, (java.lang.String) null, (java.lang.String) null, basicFindCarParam.getFindCarType().getUspCmdValue(), com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin(), 15, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonStr");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setControlFindCar(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.findCar.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object controlWindow$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.cockpit.services.controlservice.service.basic.function.BasicSwitchParam basicSwitchParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return asyncRequest$default(uspControlImpl, "车窗指令", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlWindow.2
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleControlWindowBean((java.lang.Integer) null, (java.lang.String) null, (java.lang.Integer) null, (java.lang.String) null, kotlin.collections.CollectionsKt.mutableListOf(new java.lang.Integer[]{1}), java.lang.Integer.valueOf(basicSwitchParam.toOpen() ? 1 : 2), (java.lang.String) null, com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin(), 79, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonStr");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setControlWindow(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlWindow.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object controlWinVentilation$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.cockpit.services.controlservice.service.basic.function.BasicSwitchParam basicSwitchParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return asyncRequest$default(uspControlImpl, "透气指令", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlWinVentilation.2
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleControlWindowBean((java.lang.Integer) null, (java.lang.String) null, (java.lang.Integer) null, (java.lang.String) null, kotlin.collections.CollectionsKt.mutableListOf(new java.lang.Integer[]{java.lang.Integer.valueOf(basicSwitchParam.toOpen() ? 4 : 1)}), java.lang.Integer.valueOf(basicSwitchParam.toOpen() ? 1 : 2), (java.lang.String) null, com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin(), 79, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonStr");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setControlWindow(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlWinVentilation.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object controlSunroof$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.cockpit.services.controlservice.service.basic.function.BasicSwitchParam basicSwitchParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return asyncRequest$default(uspControlImpl, "天窗指令", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlSunroof.2
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleControlWindowBean((java.lang.Integer) null, (java.lang.String) null, (java.lang.Integer) null, (java.lang.String) null, kotlin.collections.CollectionsKt.mutableListOf(new java.lang.Integer[]{2}), java.lang.Integer.valueOf(basicSwitchParam.toOpen() ? 1 : 2), (java.lang.String) null, com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin(), 75, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonStr");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setControlWindow(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlSunroof.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object controlSunshade$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.services.controlservice.service.basic.function.BasicSwitchParam basicSwitchParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        final java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleControlWindowBean((java.lang.Integer) null, (java.lang.String) null, (java.lang.Integer) null, (java.lang.String) null, kotlin.collections.CollectionsKt.mutableListOf(new java.lang.Integer[]{kotlin.coroutines.jvm.internal.Boxing.boxInt(3)}), kotlin.coroutines.jvm.internal.Boxing.boxInt(basicSwitchParam.toOpen() ? 1 : 2), (java.lang.String) null, com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin(), 75, (kotlin.jvm.internal.DefaultConstructorMarker) null));
        return asyncRequest$default(uspControlImpl, "遮阳帘指令", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlSunshade.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String str = json;
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str, "gsonStr");
                baseAction.setupLoggerRequestParam(str);
                com.geely.cockpit.services.controlservice.core.uspapi.UspApiService uspApiService = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE;
                java.lang.String str2 = json;
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str2, "gsonStr");
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(uspApiService.setControlWindow(str2), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlSunshade.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str3) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str3);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object controlClimate$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.services.controlservice.service.environment.function.EnvClimateParam envClimateParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        int duration = envClimateParam.getDuration() * 6;
        java.lang.String vin = com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin();
        final java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ControlClimateBean((java.lang.Integer) null, kotlin.coroutines.jvm.internal.Boxing.boxInt(1), (java.lang.String) null, kotlin.coroutines.jvm.internal.Boxing.boxInt(duration), (java.lang.String) null, (java.lang.String) null, (java.lang.Integer) null, (java.lang.Integer) null, (java.lang.String) null, (java.lang.String) null, (java.lang.Integer) null, (java.lang.String) null, java.lang.String.valueOf(envClimateParam.getTemperature()), (java.lang.String) null, kotlin.coroutines.jvm.internal.Boxing.boxInt(envClimateParam.getCmdType().getUspProtocolValue()), (java.lang.String) null, (java.lang.String) null, vin, 45045, (kotlin.jvm.internal.DefaultConstructorMarker) null));
        return asyncRequest$default(uspControlImpl, "空调指令", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlClimate.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String str = json;
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str, "gsonStr");
                baseAction.setupLoggerRequestParam(str);
                com.geely.cockpit.services.controlservice.core.uspapi.UspApiService uspApiService = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE;
                java.lang.String str2 = json;
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str2, "gsonStr");
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(uspApiService.getControlClimate(str2), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlClimate.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str3) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str3);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object controlSeat$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.services.controlservice.service.environment.function.EnvSeatParam envSeatParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        final java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(com.geely.cockpit.services.controlservice.service.environment.function.EnvSeatParam.INSTANCE.toUspRequestBean(envSeatParam));
        return asyncRequest$default(uspControlImpl, envSeatParam.getActionName(), statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlSeat.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String str = json;
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str, "gsonStr");
                baseAction.setupLoggerRequestParam(str);
                com.geely.cockpit.services.controlservice.core.uspapi.UspApiService uspApiService = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE;
                java.lang.String str2 = json;
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str2, "gsonStr");
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(uspApiService.getControlClimate(str2), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlSeat.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str3) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str3);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object controlPurification$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.cockpit.services.controlservice.service.environment.function.EnvPurificationParam envPurificationParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        final java.lang.String vin = com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin();
        return asyncRequest$default(uspControlImpl, "净化指令", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlPurification.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ControlNoEngineBean((java.lang.Integer) null, (java.lang.Integer) null, (java.lang.String) null, (java.lang.Integer) null, (java.lang.String) null, (java.lang.String) null, (java.lang.Integer) null, (java.lang.String) null, (java.lang.String) null, (java.lang.Integer) null, (java.lang.String) null, (java.lang.String) null, (java.lang.String) null, (java.lang.String) null, java.lang.Integer.valueOf(envPurificationParam.getCmdType().getUspProtocolValue()), (java.lang.String) null, 99, vin, 49151, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonStr");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getControlNoEngine(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlPurification.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object controlDefrost$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.cockpit.services.controlservice.service.environment.function.EnvDefrostParam envDefrostParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return asyncRequest$default(uspControlImpl, "除霜指令", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlDefrost.2
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                int duration = envDefrostParam.getDuration() * 6;
                java.lang.String vin = com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin();
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ControlClimateBean((java.lang.Integer) null, 2, (java.lang.String) null, java.lang.Integer.valueOf(duration), (java.lang.String) null, (java.lang.String) null, (java.lang.Integer) null, (java.lang.Integer) null, (java.lang.String) null, (java.lang.String) null, (java.lang.Integer) null, (java.lang.String) null, java.lang.String.valueOf(envDefrostParam.getTemperature()), (java.lang.String) null, java.lang.Integer.valueOf(envDefrostParam.getCmdType().getUspProtocolValue()), (java.lang.String) null, (java.lang.String) null, vin, 45045, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonStr");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getControlClimate(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlDefrost.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object controlFastEnvCombine$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.services.controlservice.service.environment.function.EnvCombinedFastParam envCombinedFastParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.entity.response.TempControlSetResponse>> continuation) {
        final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleControlTemperatureBean vehicleControlTemperatureBeanBuildUspFastCoolRequest = envCombinedFastParam.isFastCool() ? envCombinedFastParam.buildUspFastCoolRequest() : envCombinedFastParam.buildUspFastHeatRequest();
        return asyncRequest$default(uspControlImpl, envCombinedFastParam.getActionName(), statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.entity.response.TempControlSetResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlFastEnvCombine.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.entity.response.TempControlSetResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(vehicleControlTemperatureBeanBuildUspFastCoolRequest);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "controlBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setControlTemperature(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.entity.response.TempControlSetResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlFastEnvCombine.2.1
                    public final com.ecarx.open.http.entity.response.TempControlSetResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.entity.response.TempControlSetResponse tempControlSetResponse = new com.ecarx.open.http.entity.response.TempControlSetResponse();
                        tempControlSetResponse.sessionId = str;
                        return tempControlSetResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object controlEnvByPAA$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.services.controlservice.service.environment.function.EnvCombinedParam envCombinedParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleControlTemperatureBean vehicleControlTemperatureBeanBuildUspPaaRequest$LibCarControlNew_release = envCombinedParam.buildUspPaaRequest$LibCarControlNew_release();
        return asyncRequest$default(uspControlImpl, envCombinedParam.getPaaToRecName(), statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlEnvByPAA.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(vehicleControlTemperatureBeanBuildUspPaaRequest$LibCarControlNew_release);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "controlBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setControlTemperature(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlEnvByPAA.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object controlEnvCombine$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.services.controlservice.service.environment.function.EnvCombinedParam envCombinedParam, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.entity.response.TempControlSetResponse>> continuation) {
        final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleControlTemperatureBean vehicleControlTemperatureBeanBuildUspPaaRequest$LibCarControlNew_release = envCombinedParam.buildUspPaaRequest$LibCarControlNew_release();
        return asyncRequest$default(uspControlImpl, "智能温控指令", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.entity.response.TempControlSetResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlEnvCombine.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.entity.response.TempControlSetResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(vehicleControlTemperatureBeanBuildUspPaaRequest$LibCarControlNew_release);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "controlBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setControlTemperature(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.entity.response.TempControlSetResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlEnvCombine.2.1
                    public final com.ecarx.open.http.entity.response.TempControlSetResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.entity.response.TempControlSetResponse tempControlSetResponse = new com.ecarx.open.http.entity.response.TempControlSetResponse();
                        tempControlSetResponse.sessionId = str;
                        return tempControlSetResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object getCacheMessage$suspendImpl(final com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.util.List<com.geely.cockpit.services.controlservice.core.support.push.PushMessageNew>>> continuation) {
        return syncRequest$default(uspControlImpl, "iovPushmsgCacheQuery", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.util.List<? extends com.geely.cockpit.services.controlservice.core.support.push.PushMessageNew>>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getCacheMessage.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.util.List<com.geely.cockpit.services.controlservice.core.support.push.PushMessageNew>>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.CacheMessageReq((java.lang.Integer) null, str, (java.lang.String) null, 5, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "controlBean");
                baseAction.setupLoggerRequestParam(json);
                io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleGetResultResponseVO>> cacheMessage = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getCacheMessage(str);
                final com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl2 = uspControlImpl;
                final java.lang.String str2 = str;
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(cacheMessage, new kotlin.jvm.functions.Function1<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleGetResultResponseVO, java.util.List<? extends com.geely.cockpit.services.controlservice.core.support.push.PushMessageNew>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getCacheMessage.2.1
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(1);
                    }

                    public final java.util.List<com.geely.cockpit.services.controlservice.core.support.push.PushMessageNew> invoke(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleGetResultResponseVO vehicleGetResultResponseVO) {
                        com.geely.cockpit.services.controlservice.core.support.push.PushMessageNew pushMessageNewTransQueryResultToPushMessage = uspControlImpl2.transQueryResultToPushMessage(str2, vehicleGetResultResponseVO);
                        if (pushMessageNewTransQueryResultToPushMessage != null) {
                            return kotlin.collections.CollectionsKt.listOf(pushMessageNewTransQueryResultToPushMessage);
                        }
                        return null;
                    }
                });
            }
        }, continuation, 24, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final com.geely.cockpit.services.controlservice.core.support.push.PushMessageNew transQueryResultToPushMessage(java.lang.String sessionId, com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleGetResultResponseVO responseBean) {
        java.lang.String operationResult;
        java.lang.Integer intOrNull;
        if (responseBean == null || (operationResult = responseBean.getOperationResult()) == null || (intOrNull = kotlin.text.StringsKt.toIntOrNull(operationResult)) == null) {
            return null;
        }
        int iIntValue = intOrNull.intValue();
        java.lang.String description = responseBean.getDescription();
        java.lang.String str = description == null ? "" : description;
        com.geely.galaxy.carcontrol.core.vehicle.usp.bean.push.Ge_PushServiceResult ge_PushServiceResult = new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.push.Ge_PushServiceResult(iIntValue, (com.geely.galaxy.carcontrol.core.vehicle.usp.bean.push.PushError) null, 2, (kotlin.jvm.internal.DefaultConstructorMarker) null);
        java.lang.String description2 = responseBean.getDescription();
        java.lang.String description3 = responseBean.getDescription();
        java.lang.String str2 = description3 == null ? "" : description3;
        java.lang.String descriptionCode = responseBean.getDescriptionCode();
        java.lang.String str3 = descriptionCode == null ? "" : descriptionCode;
        java.lang.String description4 = responseBean.getDescription();
        java.lang.String str4 = description4 == null ? "" : description4;
        java.lang.String descriptionCode2 = responseBean.getDescriptionCode();
        return new com.geely.cockpit.services.controlservice.core.support.push.PushMessageNew(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.push.Ge_PushBody((java.lang.String) null, ge_PushServiceResult, (com.geely.galaxy.carcontrol.core.vehicle.usp.bean.push.PushError) null, (java.lang.String) null, sessionId, (java.lang.String) null, (java.lang.Long) null, (java.lang.String) null, description2, (java.lang.String) null, 0, (com.geely.galaxy.carcontrol.core.vehicle.usp.bean.push.Ge_PushExtend) null, str2, str3, str4, descriptionCode2 == null ? "" : descriptionCode2, 3821, (kotlin.jvm.internal.DefaultConstructorMarker) null), null, str, 0L, 10, null);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NotImplementedError */
    static /* synthetic */ java.lang.Object iovAuthorizationSet$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, java.lang.String str, java.lang.Integer num, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.AuthorizationResult>> continuation) throws kotlin.NotImplementedError {
        throw new kotlin.NotImplementedError("An operation is not implemented: Not yet implemented");
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NotImplementedError */
    static /* synthetic */ java.lang.Object iovGetAuthorizationStatus$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, java.util.List<java.lang.String> list, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.util.List<com.ecarx.open.http.entity.AuthorizationStatus>>> continuation) throws kotlin.NotImplementedError {
        throw new kotlin.NotImplementedError("An operation is not implemented: Not yet implemented");
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NotImplementedError */
    static /* synthetic */ java.lang.Object iovGetOperateRecords$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, java.lang.String str, long j, long j2, int i, int i2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.GDPRRecordInfo>> continuation) throws kotlin.NotImplementedError {
        throw new kotlin.NotImplementedError("An operation is not implemented: Not yet implemented");
    }

    static /* synthetic */ java.lang.Object sendToCar$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.SendToCarBean sendToCarBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Object>> continuation) {
        return syncRequest$default(uspControlImpl, "发送到车指令", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Object>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.sendToCar.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Object>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(sendToCarBean);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "jsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.sendToCar(json);
            }
        }, continuation, 24, null);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NotImplementedError */
    static /* synthetic */ java.lang.Object vehicleList$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, java.lang.String str, com.baselinelibrary.enums.AREAType aREAType, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.TcVehicleProfileResponse>> continuation) throws kotlin.NotImplementedError {
        throw new kotlin.NotImplementedError("An operation is not implemented: Not yet implemented");
    }

    static /* synthetic */ java.lang.Object openLightShow$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, final java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return asyncRequest$default(uspControlImpl, "打开灯光秀", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.openLightShow.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleLightShowBean((java.lang.Integer) null, 2, (java.lang.String) null, java.lang.Integer.valueOf(java.lang.Integer.parseInt(str2)), str, 5, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "jsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setVehicleLightShow(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.openLightShow.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str3) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str3);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object closeLightShow$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, final java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return syncRequest$default(uspControlImpl, "关闭灯光秀", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.closeLightShow.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleLightShowBean((java.lang.Integer) null, 3, (java.lang.String) null, java.lang.Integer.valueOf(java.lang.Integer.parseInt(str2)), str, 5, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "jsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setVehicleLightShow(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.closeLightShow.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str3) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str3);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object downloadLightShow$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, final java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return syncRequest$default(uspControlImpl, "下载灯光秀", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.downloadLightShow.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleLightShowBean((java.lang.Integer) null, 1, (java.lang.String) null, java.lang.Integer.valueOf(java.lang.Integer.parseInt(str2)), str, 5, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "jsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setVehicleLightShow(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.downloadLightShow.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str3) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str3);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object deleteLightShow$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, final java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return syncRequest$default(uspControlImpl, "删除灯光秀", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.deleteLightShow.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleLightShowBean((java.lang.Integer) null, 4, (java.lang.String) null, java.lang.Integer.valueOf(java.lang.Integer.parseInt(str2)), str, 5, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "jsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.setVehicleLightShow(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.deleteLightShow.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str3) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str3);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object setCarOnOffline$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, final java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Boolean>> continuation) {
        return syncRequest$default(uspControlImpl, "setCarOnOffline", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Boolean>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.setCarOnOffline.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Boolean>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.util.HashMap map = new java.util.HashMap();
                java.util.HashMap map2 = map;
                map2.put("vin", str);
                map2.put("target", str2);
                com.geely.cockpit.services.controlservice.core.uspapi.UspApiService uspApiService = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE;
                java.lang.String string = map.toString();
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "map.toString()");
                return uspApiService.setCarHeartBeat(string);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object sendCarHeartBeat$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Boolean>> continuation) {
        return syncRequest$default(uspControlImpl, "sendCarHeartBeat", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Boolean>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.sendCarHeartBeat.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Boolean>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.util.HashMap map = new java.util.HashMap();
                map.put("vin", str);
                com.geely.cockpit.services.controlservice.core.uspapi.UspApiService uspApiService = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE;
                java.lang.String string = map.toString();
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "map.toString()");
                return uspApiService.setCarHeartBeat(string);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object getCarShareList$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.functiontools.control.bean.CarShareList>> continuation) {
        return syncRequest$default(uspControlImpl, "getCarShareList", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.functiontools.control.bean.CarShareList>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getCarShareList.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.functiontools.control.bean.CarShareList>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getCarShareList(str);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object createCarShare$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.functiontools.control.bean.CreateCarShareBean createCarShareBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.String>> continuation) {
        return syncRequest$default(uspControlImpl, "createCarShare", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.createCarShare.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(createCarShareBean);
                com.geely.cockpit.services.controlservice.core.uspapi.UspApiService uspApiService = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE;
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "jsonParams");
                return uspApiService.createCarShare(json);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object getSharedUnhandledList$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.util.List<com.geely.galaxy.carcontrol.core.functiontools.control.bean.BeSharedBean>>> continuation) {
        return syncRequest$default(uspControlImpl, "getSharedUnhandledList", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.util.List<com.geely.galaxy.carcontrol.core.functiontools.control.bean.BeSharedBean>>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getSharedUnhandledList.2
            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.util.List<com.geely.galaxy.carcontrol.core.functiontools.control.bean.BeSharedBean>>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getSharedUnhandledList();
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object getUnreadShareCount$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Integer>> continuation) {
        return syncRequest$default(uspControlImpl, "getUnreadShareCount", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getUnreadShareCount.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getUnreadShareCount(str);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object readShare$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Integer>> continuation) {
        return syncRequest$default(uspControlImpl, "readShare", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.readShare.2
            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.readShare();
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object unbindShare$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, final java.lang.String str2, boolean z, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Integer>> continuation) {
        return syncRequest$default(uspControlImpl, "unbindShare", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.unbindShare.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.util.HashMap map = new java.util.HashMap();
                map.put("vin", str);
                map.put("sharedUserId", str2);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.unbindShare(map);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object acceptCarSharing$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, java.lang.String str, int i, java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Integer>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.updateShareStatus$default(uspControlImpl, str, i, str2, null, null, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object rejectCarSharing$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, java.lang.String str, int i, java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Integer>> continuation) {
        return com.geely.cockpit.services.controlservice.core.callapi.ICarControlApi.DefaultImpls.updateShareStatus$default(uspControlImpl, str, i, str2, null, null, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object updateShareStatus$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, final int i, final java.lang.String str2, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Integer>> continuation) {
        return syncRequest$default(uspControlImpl, "updateShareStatus", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.updateShareStatus.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.util.HashMap map = new java.util.HashMap();
                map.put("id", str);
                map.put("type", java.lang.Integer.valueOf(i));
                map.put("shareRemark", str2);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.updateShareStatus(map);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object readBeShared$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final long j, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.Integer>> continuation) {
        return syncRequest$default(uspControlImpl, "readBeShared", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.readBeShared.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.Integer>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.readBeShared(j);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object getBeSharedDetail$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, boolean z, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.functiontools.control.bean.BeSharedBean>> continuation) {
        return syncRequest$default(uspControlImpl, "getBeSharedDetail", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.functiontools.control.bean.BeSharedBean>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getBeSharedDetail.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.functiontools.control.bean.BeSharedBean>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                com.geely.cockpit.services.controlservice.core.uspapi.UspApiService uspApiService = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE;
                java.lang.Long longOrNull = kotlin.text.StringsKt.toLongOrNull(str);
                return uspApiService.getBeSharedDetail(longOrNull != null ? longOrNull.longValue() : 0L);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object getAbnormalVideoList$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.AbnormalVideoListRequest abnormalVideoListRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.AbnormalVideoResult>> continuation) {
        return syncRequest$default(uspControlImpl, "getAbnormalVideoList", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.AbnormalVideoResult>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getAbnormalVideoList.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.AbnormalVideoResult>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(abnormalVideoListRequest);
                com.geely.cockpit.services.controlservice.core.uspapi.UspApiService uspApiService = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE;
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "requestJsonStr");
                return uspApiService.getAbnormalVideoList(json);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object deleteAbnormalVideo$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.AbnormalVideoListRequest abnormalVideoListRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.String>> continuation) {
        return syncRequest$default(uspControlImpl, "deleteAbnormalVideo", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.deleteAbnormalVideo.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                com.geely.cockpit.services.controlservice.core.uspapi.UspApiService uspApiService = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE;
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(abnormalVideoListRequest);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "toJson(request)");
                return uspApiService.deleteFileList(json);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object getVehicleSwitch$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.cockpit.services.controlservice.bean.PlatformVehicleSwitch>> continuation) {
        final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleSwitchStatusBean vehicleSwitchStatusBean = new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleSwitchStatusBean((java.lang.Integer) null, (java.lang.String) null, com.geely.cockpit.services.controlservice.runtime.CurVehicle.getVin(), 3, (kotlin.jvm.internal.DefaultConstructorMarker) null);
        return syncRequest$default(uspControlImpl, "getVehicleSwitch", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.bean.PlatformVehicleSwitch>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getVehicleSwitch.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.bean.PlatformVehicleSwitch>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                com.geely.cockpit.services.controlservice.core.uspapi.UspApiService uspApiService = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE;
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(vehicleSwitchStatusBean);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "toJson(requestBean)");
                return uspApiService.getControlVehicleSwitchStatus(json);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object setVehicleSwitch$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleControlSwitchBean vehicleControlSwitchBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return asyncRequest$default(uspControlImpl, "setControlVehicleSwitch", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.setVehicleSwitch.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(vehicleControlSwitchBean);
                com.geely.cockpit.services.controlservice.core.uspapi.UspApiService uspApiService = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE;
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "controlBean");
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(uspApiService.setControlVehicleSwitch(json), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.setVehicleSwitch.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object electronicFenceQuery$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ElectronicFenceQueryBean electronicFenceQueryBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ElectronicFenceQueryResponseBean>> continuation) {
        return syncRequest$default(uspControlImpl, "查询电子围栏", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ElectronicFenceQueryResponseBean>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.electronicFenceQuery.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ElectronicFenceQueryResponseBean>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(electronicFenceQueryBean);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "queryBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.electronicFenceQuery(json);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object electronicFenceCreate$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ElectronicFenceCreateBean electronicFenceCreateBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.String>> continuation) {
        return syncRequest$default(uspControlImpl, "创建电子围栏", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.electronicFenceCreate.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(electronicFenceCreateBean);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "createBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.electronicFenceCreate(json);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object electronicFenceUpdate$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ElectronicFenceUpdateBean electronicFenceUpdateBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.String>> continuation) {
        return syncRequest$default(uspControlImpl, "更新电子围栏", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.electronicFenceUpdate.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(electronicFenceUpdateBean);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "updateBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.electronicFenceUpdate(json);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object electronicFenceDelete$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ElectronicFenceDeleteBean electronicFenceDeleteBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.String>> continuation) {
        return syncRequest$default(uspControlImpl, "删除电子围栏", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.electronicFenceDelete.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(electronicFenceDeleteBean);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "deleteBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.electronicFenceDelete(json);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object deleteJournalLog$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, final java.util.List<? extends com.geely.galaxy.carcontrol.core.functiontools.control.bean.Ge_JourneyLogRequest> list, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.lang.String>> continuation) {
        return syncRequest$default(uspControlImpl, "删除行车日志", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.deleteJournalLog.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<java.lang.String>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.util.List<com.geely.galaxy.carcontrol.core.functiontools.control.bean.Ge_JourneyLogRequest> list2 = list;
                java.util.ArrayList arrayList = new java.util.ArrayList(kotlin.collections.CollectionsKt.collectionSizeOrDefault(list2, 10));
                java.util.Iterator<T> it = list2.iterator();
                while (it.hasNext()) {
                    arrayList.add(((com.geely.galaxy.carcontrol.core.functiontools.control.bean.Ge_JourneyLogRequest) it.next()).id);
                }
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourLogDeleteBean((java.lang.Integer) null, arrayList, (java.lang.String) null, str, 5, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.deleteJournalLog(json);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object getJournalLogList$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourLogQueryBean jourLogQueryBean, boolean z, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourneyListBean>> continuation) {
        return syncRequest$default(uspControlImpl, "查询行车日志", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourneyListBean>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getJournalLogList.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourneyListBean>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(jourLogQueryBean);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getJournalLogList(json);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object getJournalLogTrack$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourLogQueryBean jourLogQueryBean, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourneyTrack>> continuation) {
        return syncRequest$default(uspControlImpl, "getJournalLogTrack", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourneyTrack>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getJournalLogTrack.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.JourneyTrack>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                jourLogQueryBean.setPageSize(100);
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(jourLogQueryBean);
                com.geely.cockpit.services.controlservice.core.uspapi.UspApiService uspApiService = com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE;
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonBean");
                return uspApiService.getJournalLogTrack(json);
            }
        }, continuation, 24, null);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NotImplementedError */
    static /* synthetic */ java.lang.Object getMergeChargingState$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.cockpit.services.controlservice.core.callapi.MergeChargeStatus>> continuation) throws kotlin.NotImplementedError {
        throw new kotlin.NotImplementedError("An operation is not implemented: Not yet implemented");
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NotImplementedError */
    static /* synthetic */ java.lang.Object getMergeBeforeChargingState$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<java.util.List<java.lang.String>>> continuation) throws kotlin.NotImplementedError {
        throw new kotlin.NotImplementedError("An operation is not implemented: Not yet implemented");
    }

    static /* synthetic */ java.lang.Object getParkPhoto$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleParkPhotoRequest vehicleParkPhotoRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ParkPhotoResponse>> continuation) {
        return syncRequest$default(uspControlImpl, "查询驻车拍照", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ParkPhotoResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getParkPhoto.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ParkPhotoResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(vehicleParkPhotoRequest);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getParkPhoto(json);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object getParkPosition$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final java.lang.String str, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ParkPositionResponse>> continuation) {
        return syncRequest$default(uspControlImpl, "查询驻车位置", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ParkPositionResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getParkPosition.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.ParkPositionResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(new com.geely.galaxy.carcontrol.core.vehicle.usp.bean.VehicleParkPhotoRequest((java.lang.Integer) null, (java.lang.String) null, str, 3, (kotlin.jvm.internal.DefaultConstructorMarker) null));
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getParkPosition(json);
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object controlRefrigerator$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.cockpit.services.controlservice.service.reservation.bean.FridgeScheduleStatus fridgeScheduleStatus, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.ecarx.open.http.response.RemoteControlResponse>> continuation) {
        return asyncRequest$default(uspControlImpl, "控制冰箱", statusNotify, controlCallback, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlRefrigerator.2
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.ecarx.open.http.response.RemoteControlResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.controlRefrigerator(com.blankj.utilcode.util.GsonUtils.toJson(fridgeScheduleStatus.toUspSetBean())), new kotlin.jvm.functions.Function1<java.lang.String, com.ecarx.open.http.response.RemoteControlResponse>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.controlRefrigerator.2.1
                    public final com.ecarx.open.http.response.RemoteControlResponse invoke(java.lang.String str) {
                        com.ecarx.open.http.response.RemoteControlResponse remoteControlResponse = new com.ecarx.open.http.response.RemoteControlResponse();
                        remoteControlResponse.setSessionId(str);
                        return remoteControlResponse;
                    }
                });
            }
        }, continuation, 8, null);
    }

    static /* synthetic */ java.lang.Object queryRefrigerator$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.RefrigeratorQueryRequest refrigeratorQueryRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationFridgeInfo>> continuation) {
        return syncRequest$default(uspControlImpl, "查询冰箱", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationFridgeInfo>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.queryRefrigerator.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationFridgeInfo>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(refrigeratorQueryRequest);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.response.BaseUspResultKt.transform(com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.queryRefrigerator(json), new kotlin.jvm.functions.Function1<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.RefrigeratorResponse, com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationFridgeInfo>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.queryRefrigerator.2.1
                    public final com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationFridgeInfo invoke(com.geely.galaxy.carcontrol.core.vehicle.usp.bean.RefrigeratorResponse refrigeratorResponse) {
                        return com.geely.cockpit.services.controlservice.service.reservation.bean.ReservationFridgeInfo.INSTANCE.transUspToPlatformBean(refrigeratorResponse);
                    }
                });
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object getBatteryBasic$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryBasicRequest batteryBasicRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryBasicResponse>> continuation) {
        return syncRequest$default(uspControlImpl, "电池保养基础", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryBasicResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getBatteryBasic.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryBasicResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(batteryBasicRequest);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getBatteryBasic(batteryBasicRequest.getVin());
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object getBatteryReport$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryReportRequest batteryReportRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryReportResponse>> continuation) {
        return syncRequest$default(uspControlImpl, "电池保养报告", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryReportResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getBatteryReport.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryReportResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(batteryReportRequest);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getBatteryReport(batteryReportRequest.getVin(), batteryReportRequest.getQueryDate());
            }
        }, continuation, 24, null);
    }

    static /* synthetic */ java.lang.Object getBatteryRecord$suspendImpl(com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl uspControlImpl, final com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryRecordRequest batteryRecordRequest, com.geely.cockpit.libs.interfaces.services.asynctask.StatusNotify statusNotify, com.geely.galaxy.carcontrol.core.vehicle.usp.center.callback.ControlCallback controlCallback, kotlin.coroutines.Continuation<? super com.geely.cockpit.services.controlservice.core.response.CarControlResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryRecordResponse>> continuation) {
        return syncRequest$default(uspControlImpl, "电池保养记录", statusNotify, controlCallback, false, null, new kotlin.jvm.functions.Function1<com.geely.cockpit.services.controlservice.core.action.BaseAction<?>, io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryRecordResponse>>>() { // from class: com.geely.cockpit.services.controlservice.core.controlImpl.UspControlImpl.getBatteryRecord.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public final io.reactivex.Observable<com.geely.cockpit.services.controlservice.core.response.BaseUspResult<com.geely.galaxy.carcontrol.core.vehicle.usp.bean.BatteryRecordResponse>> invoke(com.geely.cockpit.services.controlservice.core.action.BaseAction<?> baseAction) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseAction, "action");
                java.lang.String json = com.blankj.utilcode.util.GsonUtils.toJson(batteryRecordRequest);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "gsonBean");
                baseAction.setupLoggerRequestParam(json);
                return com.geely.cockpit.services.controlservice.core.uspapi.UspApiService.INSTANCE.getBatteryRecord(batteryRecordRequest.getVin());
            }
        }, continuation, 24, null);
    }
}
