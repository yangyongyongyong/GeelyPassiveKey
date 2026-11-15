package com.luca.geelykeyradar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;
import java.util.Locale;

/** 原生Canvas RSSI实时曲线，不依赖第三方图表库。 */
final class RssiChartView extends View {
    private static final int COLOR_GRID = Color.rgb(38, 54, 77);
    private static final int COLOR_TEXT = Color.rgb(148, 163, 184);
    private static final int COLOR_RAW = Color.rgb(56, 189, 248);
    private static final int COLOR_SMOOTH = Color.rgb(250, 204, 21);
    private static final int COLOR_CONNECT = Color.rgb(168, 85, 247);
    private static final int COLOR_UNLOCK = Color.rgb(34, 197, 94);
    private static final int COLOR_LOCK = Color.rgb(248, 113, 113);
    private static final float MIN_DBM = -105f;
    private static final float MAX_DBM = -35f;

    private final RssiChartSeries series = new RssiChartSeries();
    private final Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint rawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint smoothPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint connectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint unlockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint lockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint connectHaloPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint unlockHaloPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint lockHaloPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint connectTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint unlockTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint lockTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float connectDbm = -95f;
    private float unlockDbm = -79f;
    private float lockDbm = -85f;

    /** 初始化固定样式，曲线颜色与电源状态卡保持一致。 */
    RssiChartView(Context context) {
        this(context, null);
    }

    /** 支持系统布局实例化时读取自定义View。 */
    RssiChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMinimumHeight(dp(210));
        gridPaint.setColor(COLOR_GRID);
        gridPaint.setStrokeWidth(dp(1));
        textPaint.setColor(COLOR_TEXT);
        textPaint.setTextSize(dp(10));
        rawPaint.setColor(COLOR_RAW);
        rawPaint.setStrokeWidth(dp(2));
        rawPaint.setStyle(Paint.Style.STROKE);
        smoothPaint.setColor(COLOR_SMOOTH);
        smoothPaint.setStrokeWidth(dp(2));
        smoothPaint.setStyle(Paint.Style.STROKE);
        connectPaint.setColor(COLOR_CONNECT);
        connectPaint.setStrokeWidth(dp(2));
        connectPaint.setStyle(Paint.Style.STROKE);
        connectPaint.setPathEffect(new DashPathEffect(new float[]{dp(4), dp(5)}, 0));
        unlockPaint.setColor(COLOR_UNLOCK);
        unlockPaint.setStrokeWidth(dp(2));
        unlockPaint.setStyle(Paint.Style.STROKE);
        unlockPaint.setPathEffect(new DashPathEffect(new float[]{dp(8), dp(5)}, 0));
        lockPaint.setColor(COLOR_LOCK);
        lockPaint.setStrokeWidth(dp(2));
        lockPaint.setStyle(Paint.Style.STROKE);
        lockPaint.setPathEffect(new DashPathEffect(new float[]{dp(8), dp(5)}, 0));
        connectHaloPaint.setColor(COLOR_CONNECT);
        connectHaloPaint.setAlpha(42);
        connectHaloPaint.setStrokeWidth(dp(6));
        connectHaloPaint.setStyle(Paint.Style.STROKE);
        connectHaloPaint.setPathEffect(new DashPathEffect(new float[]{dp(4), dp(5)}, 0));
        unlockHaloPaint.setColor(COLOR_UNLOCK);
        unlockHaloPaint.setAlpha(48);
        unlockHaloPaint.setStrokeWidth(dp(6));
        unlockHaloPaint.setStyle(Paint.Style.STROKE);
        unlockHaloPaint.setPathEffect(new DashPathEffect(new float[]{dp(8), dp(5)}, 0));
        lockHaloPaint.setColor(COLOR_LOCK);
        lockHaloPaint.setAlpha(48);
        lockHaloPaint.setStrokeWidth(dp(6));
        lockHaloPaint.setStyle(Paint.Style.STROKE);
        lockHaloPaint.setPathEffect(new DashPathEffect(new float[]{dp(8), dp(5)}, 0));
        connectTextPaint.setColor(COLOR_CONNECT);
        connectTextPaint.setTextSize(dp(11));
        unlockTextPaint.setColor(COLOR_UNLOCK);
        unlockTextPaint.setTextSize(dp(11));
        lockTextPaint.setColor(COLOR_LOCK);
        lockTextPaint.setTextSize(dp(11));
    }

    /** 更新界面显示的连接/解锁/上锁阈值线并在必要时重绘。 */
    void setThresholds(float unlock, float lock, float connect) {
        unlockDbm = unlock;
        lockDbm = lock;
        connectDbm = connect;
        invalidate();
    }

    /** 添加实时原始与平滑样本，并请求重绘。 */
    void addSample(long nowMs, int rawDbm, float smoothDbm) {
        series.add(nowMs, rawDbm, smoothDbm);
        invalidate();
    }

    /** 添加无信号断点，保留采样窗口但不连出虚假曲线。 */
    void addGap(long nowMs) {
        series.addGap(nowMs);
        invalidate();
    }

    /** 返回窗口内有效样本数、范围与更新时间，供卡片摘要展示。 */
    String summary(long nowMs) {
        int count = series.validCount(nowMs);
        if (count == 0) return "暂无有效RSSI样本 · 最近60秒";
        return String.format(Locale.ROOT, "最近60秒：%d个样本 · 最小%d dBm · 最大%d dBm",
                count, series.minDbm(nowMs), series.maxDbm(nowMs));
    }

    /** 绘制网格、阈值线、原始曲线和平滑曲线；断点处分段处理。 */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float left = dp(34);
        float top = dp(12);
        float right = getWidth() - dp(8);
        float bottom = getHeight() - dp(22);
        if (right <= left || bottom <= top) return;

        for (int dbm = -100; dbm <= -40; dbm += 10) {
            float y = yFor(dbm, top, bottom);
            canvas.drawLine(left, y, right, y, gridPaint);
            canvas.drawText(String.valueOf(dbm), dp(2), y + dp(3), textPaint);
        }
        float connectY = yFor(connectDbm, top, bottom);
        float unlockY = yFor(unlockDbm, top, bottom);
        float lockY = yFor(lockDbm, top, bottom);
        canvas.drawLine(left, connectY, right, connectY, connectHaloPaint);
        canvas.drawLine(left, unlockY, right, unlockY, unlockHaloPaint);
        canvas.drawLine(left, lockY, right, lockY, lockHaloPaint);
        canvas.drawLine(left, connectY, right, connectY, connectPaint);
        canvas.drawLine(left, unlockY, right, unlockY, unlockPaint);
        canvas.drawLine(left, lockY, right, lockY, lockPaint);
        canvas.drawText("连接 " + Math.round(connectDbm), left + dp(4), connectY - dp(3), connectTextPaint);
        canvas.drawText("解锁 " + Math.round(unlockDbm), left + dp(4), unlockY - dp(3), unlockTextPaint);
        canvas.drawText("上锁 " + Math.round(lockDbm), left + dp(4), lockY - dp(3), lockTextPaint);
        canvas.drawText("-60s", left, getHeight() - dp(5), textPaint);
        canvas.drawText("现在", right - dp(24), getHeight() - dp(5), textPaint);

        long nowMs = android.os.SystemClock.elapsedRealtime();
        List<RssiChartSeries.Sample> samples = series.snapshot(nowMs);
        drawSeries(canvas, samples, nowMs, left, right, top, bottom, false, rawPaint);
        drawSeries(canvas, samples, nowMs, left, right, top, bottom, true, smoothPaint);
    }

    /** 将一条曲线按时间位置映射到Canvas，遇到无效样本时结束当前Path。 */
    private void drawSeries(Canvas canvas, List<RssiChartSeries.Sample> samples, long nowMs,
                            float left, float right, float top, float bottom,
                            boolean smooth, Paint paint) {
        Path path = new Path();
        boolean drawing = false;
        long windowStart = nowMs - RssiChartSeries.WINDOW_MS;
        for (RssiChartSeries.Sample sample : samples) {
            boolean usable = smooth ? sample.hasSmooth() : sample.valid;
            if (!usable) {
                drawing = false;
                continue;
            }
            float fraction = Math.max(0f, Math.min(1f,
                    (sample.timeMs - windowStart) / (float) RssiChartSeries.WINDOW_MS));
            float x = left + fraction * (right - left);
            float y = yFor(smooth ? sample.smoothDbm : sample.rawDbm, top, bottom);
            if (!drawing) {
                path.moveTo(x, y);
                drawing = true;
            } else {
                path.lineTo(x, y);
            }
        }
        canvas.drawPath(path, paint);
    }

    /** 将dBm映射到固定-105..-35范围，范围外的值截断在边界。 */
    private float yFor(float dbm, float top, float bottom) {
        float bounded = Math.max(MIN_DBM, Math.min(MAX_DBM, dbm));
        return bottom - (bounded - MIN_DBM) / (MAX_DBM - MIN_DBM) * (bottom - top);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
