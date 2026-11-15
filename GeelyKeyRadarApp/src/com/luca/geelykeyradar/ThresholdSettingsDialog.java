package com.luca.geelykeyradar;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** 原生阈值设置弹窗：固定字段标签、独立说明、单位、推荐值、就地校验和可滚动内容。 */
final class ThresholdSettingsDialog {
    interface OnSaveListener {
        /** 只有三项输入校验通过且用户明确保存时调用，不因填推荐值或取消写配置。 */
        void onSave(float unlockDbm, float lockDbm, long dwellMs);
    }

    private static final int COLOR_BG = Color.rgb(8, 17, 31);
    private static final int COLOR_CARD = Color.rgb(17, 28, 46);
    private static final int COLOR_BORDER = Color.rgb(48, 65, 87);
    private static final int COLOR_INPUT_BORDER = Color.rgb(86, 105, 128);
    private static final int COLOR_TEXT = Color.rgb(229, 231, 235);
    private static final int COLOR_MUTED = Color.rgb(148, 163, 184);
    private static final int COLOR_BLUE = Color.rgb(56, 189, 248);
    private static final int COLOR_PURPLE = Color.rgb(196, 181, 253);
    private static final int COLOR_TEAL = Color.rgb(94, 234, 212);
    private static final int COLOR_ERROR = Color.rgb(248, 113, 113);

    private final Activity activity;
    private final OnSaveListener onSave;
    private final Dialog dialog;
    private final Field unlock;
    private final Field lock;
    private final Field dwell;
    private final TextView summary;
    private final TextView formMessage;
    private boolean showErrors;
    private boolean fillingRecommended;

    /** 构建独立弹窗，字段与布局不再继续堆入主Activity，且不依赖新的UI库。 */
    private ThresholdSettingsDialog(Activity activity, float unlockDbm, float lockDbm,
                                    long dwellMs, OnSaveListener onSave) {
        this.activity = activity;
        this.onSave = onSave;
        dialog = new Dialog(activity);
        dialog.setOwnerActivity(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout root = column();
        root.setBackground(rounded(COLOR_BG, 22, COLOR_BORDER));
        root.setClipToOutline(true);
        root.setFocusableInTouchMode(true);

        LinearLayout header = column();
        header.setPadding(dp(20), dp(20), dp(20), dp(14));
        TextView eyebrow = text("无感进出 · 触发设置", 13, COLOR_BLUE, false);
        header.addView(eyebrow);
        TextView title = text("自定义靠近 / 离开", 22, COLOR_TEXT, true);
        title.setAccessibilityHeading(true);
        header.addView(title, spaced(5));
        header.addView(text("共 3 项设置，向下滑动查看全部", 13, COLOR_MUTED, false), spaced(6));
        root.addView(header);

        ScrollView scroll = new ScrollView(activity);
        scroll.setFillViewport(false);
        scroll.setClipToPadding(false);
        scroll.setVerticalScrollBarEnabled(true);
        scroll.setScrollbarFadingEnabled(false);
        LinearLayout content = column();
        content.setPadding(dp(20), 0, dp(20), dp(16));
        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));

        LinearLayout intro = column();
        intro.setPadding(dp(14), dp(12), dp(14), dp(12));
        intro.setBackground(rounded(Color.rgb(15, 35, 54), 12, Color.rgb(35, 77, 105)));
        intro.addView(text("先看懂信号，再调触发时机", 14, COLOR_BLUE, true));
        intro.addView(text("dBm 越接近 0，信号越强。-70 比 -75 更强；信号受车型、口袋和遮挡影响，不等于固定米数。",
                13, COLOR_TEXT, false), spaced(6));
        content.addView(intro, spaced(0));

        unlock = field(content, "01  靠近解锁信号", "信号达到此值或更强，才满足解锁条件。", "≥", "dBm",
                "推荐 " + ThresholdSettingsForm.number(VehicleIdentityStore.DEFAULT_UNLOCK_DBM) + " dBm · 范围 -90 至 -40",
                "更负通常更早解锁；更接近 0，需要更靠近。", COLOR_BLUE, true);
        lock = field(content, "02  离开上锁信号", "信号等于此值或更弱，开始离开确认计时。", "≤", "dBm",
                "推荐 " + ThresholdSettingsForm.number(VehicleIdentityStore.DEFAULT_LOCK_DBM) + " dBm · 范围 -98 至 -46",
                "更负通常要走得更远；至少比解锁低 6 dB。", COLOR_PURPLE, true);
        dwell = field(content, "03  离开确认时长", "弱信号持续达到此时长，才满足上锁确认。", "", "秒",
                "推荐 " + ThresholdSettingsForm.seconds(VehicleIdentityStore.DEFAULT_LOCK_DWELL_MS) + " 秒 · 范围 1.5 至 20",
                "更长可减少短暂遮挡误锁，但锁车也会更晚。", COLOR_TEAL, false);
        unlock.input.setHint(ThresholdSettingsForm.number(VehicleIdentityStore.DEFAULT_UNLOCK_DBM));
        lock.input.setHint(ThresholdSettingsForm.number(VehicleIdentityStore.DEFAULT_LOCK_DBM));
        dwell.input.setHint(ThresholdSettingsForm.seconds(VehicleIdentityStore.DEFAULT_LOCK_DWELL_MS));
        unlock.input.setText(ThresholdSettingsForm.number(unlockDbm));
        lock.input.setText(ThresholdSettingsForm.number(lockDbm));
        dwell.input.setText(ThresholdSettingsForm.seconds(dwellMs));
        unlock.input.setNextFocusForwardId(lock.input.getId());
        lock.input.setNextFocusForwardId(dwell.input.getId());
        unlock.input.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        lock.input.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        dwell.input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        dwell.input.setOnEditorActionListener((view, action, event) -> {
            if (action != EditorInfo.IME_ACTION_DONE) return false;
            save();
            return true;
        });

        TextView protection = text("靠近信号含遮挡补偿，离开信号不补偿。信号回升会重新确认离开；近场、行驶和插线保护仍生效。dBm 填正数自动按负值保存。",
                13, COLOR_MUTED, false);
        content.addView(protection, spaced(12));

        View divider = new View(activity);
        divider.setBackgroundColor(COLOR_BORDER);
        root.addView(divider, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(1)));
        LinearLayout footer = column();
        footer.setPadding(dp(20), dp(12), dp(20), dp(16));
        footer.setBackgroundColor(COLOR_CARD);
        LinearLayout preview = new LinearLayout(activity);
        preview.setGravity(Gravity.CENTER_VERTICAL);
        summary = text("", 13, COLOR_MUTED, false);
        preview.addView(summary, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        Button recommended = action("填推荐值", false);
        recommended.setTextSize(13);
        recommended.setContentDescription("填入推荐值，点击保存后才生效");
        recommended.setOnClickListener(view -> fillRecommended());
        LinearLayout.LayoutParams recommendedLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        recommendedLp.leftMargin = dp(8);
        preview.addView(recommended, recommendedLp);
        footer.addView(preview);
        formMessage = text("", 13, COLOR_ERROR, false);
        formMessage.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);
        formMessage.setVisibility(View.GONE);
        footer.addView(formMessage, spaced(6));
        LinearLayout actions = new LinearLayout(activity);
        Button cancel = action("取消", false);
        cancel.setOnClickListener(view -> dialog.dismiss());
        Button apply = action("保存并生效", true);
        apply.setOnClickListener(view -> save());
        LinearLayout.LayoutParams cancelLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        cancelLp.rightMargin = dp(10);
        actions.addView(cancel, cancelLp);
        actions.addView(apply, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f));
        footer.addView(actions, spaced(10));
        root.addView(footer);
        dialog.setContentView(root);
        watch(unlock);
        watch(lock);
        watch(dwell);
        refreshValidation();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setDimAmount(0.6f);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                    | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            int width = Math.min(activity.getResources().getDisplayMetrics().widthPixels - dp(32), dp(560));
            int height = Math.min((int) (activity.getResources().getDisplayMetrics().heightPixels * 0.88f), dp(760));
            window.setLayout(width, height);
        }
        root.requestFocus();
        dialog.show();
    }

    /** 展示弹窗，不读写车辆身份或调用车控；初值来自实际服务或持久化配置。 */
    static Dialog show(Activity activity, float unlockDbm, float lockDbm, long dwellMs, OnSaveListener onSave) {
        if (activity.isFinishing() || activity.isDestroyed()) return null;
        return new ThresholdSettingsDialog(activity, unlockDbm, lockDbm, dwellMs, onSave).dialog;
    }

    private final class Field {
        final EditText input;
        final TextView error;
        final LinearLayout inputBox;
        final int accent;

        /** 保存一个字段的输入与错误控件，聚焦/错误状态只改本字段边框。 */
        Field(EditText input, TextView error, LinearLayout inputBox, int accent) {
            this.input = input;
            this.error = error;
            this.inputBox = inputBox;
            this.accent = accent;
        }

        /** 错误文字固定关联当前字段，清除错误后恢复聚焦状态而不是只显示一个红框。 */
        void showError(String message) {
            error.setText(message);
            error.setVisibility(message.isEmpty() ? View.GONE : View.VISIBLE);
            updateBorder();
        }

        /** 保留清晰的键盘焦点边界；无错误时不把正常输入误着色为失败。 */
        void updateBorder() {
            int stroke = error.getVisibility() == View.VISIBLE ? COLOR_ERROR : (input.hasFocus() ? accent : COLOR_INPUT_BORDER);
            inputBox.setBackground(rounded(COLOR_BG, 12, stroke));
        }
    }

    /** 创建有永久标签、说明、单位和推荐值的字段卡片，提示不依赖会消失的placeholder。 */
    private Field field(LinearLayout parent, String title, String description, String comparison, String unit,
                        String recommendation, String effect, int accent, boolean signed) {
        LinearLayout card = column();
        card.setPadding(dp(14), dp(14), dp(14), dp(14));
        card.setBackground(rounded(COLOR_CARD, 16, COLOR_BORDER));
        TextView label = text(title, 16, accent, true);
        label.setAccessibilityHeading(true);
        card.addView(label);
        card.addView(text(description, 14, COLOR_TEXT, false), spaced(6));
        LinearLayout inputBox = new LinearLayout(activity);
        inputBox.setGravity(Gravity.CENTER_VERTICAL);
        inputBox.setPadding(dp(12), 0, dp(12), 0);
        inputBox.setMinimumHeight(dp(56));
        inputBox.setBackground(rounded(COLOR_BG, 12, COLOR_INPUT_BORDER));
        if (!comparison.isEmpty()) {
            TextView operator = text(comparison, 22, COLOR_MUTED, false);
            operator.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
            inputBox.addView(operator, new LinearLayout.LayoutParams(dp(28), LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        EditText input = new EditText(activity);
        input.setId(View.generateViewId());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
                | (signed ? InputType.TYPE_NUMBER_FLAG_SIGNED : 0));
        input.setSingleLine(true);
        input.setSelectAllOnFocus(true);
        input.setTextSize(24);
        input.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        input.setTextColor(COLOR_TEXT);
        input.setHintTextColor(COLOR_MUTED);
        input.setBackgroundColor(Color.TRANSPARENT);
        input.setPadding(dp(4), dp(10), dp(4), dp(10));
        input.setMinimumWidth(0);
        label.setLabelFor(input.getId());
        inputBox.addView(input, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        TextView unitView = text(unit, 14, COLOR_MUTED, false);
        inputBox.addView(unitView);
        card.addView(inputBox, spaced(10));
        TextView error = text("", 13, COLOR_ERROR, false);
        error.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);
        error.setVisibility(View.GONE);
        card.addView(error, spaced(6));
        card.addView(text(recommendation, 12, COLOR_MUTED, false), spaced(9));
        card.addView(text(effect, 13, COLOR_MUTED, false), spaced(4));
        parent.addView(card, spaced(12));
        Field field = new Field(input, error, inputBox, accent);
        input.setOnFocusChangeListener((view, focused) -> field.updateBorder());
        return field;
    }

    /** 输入变化只更新预览和已显示的字段错误，未点击保存不会触碰配置。 */
    private void watch(Field field) {
        field.input.addTextChangedListener(new TextWatcher() {
            /** 输入前无需读写配置。 */
            @Override
            public void beforeTextChanged(CharSequence value, int start, int count, int after) {
            }

            /** 文本中间状态不触发保存。 */
            @Override
            public void onTextChanged(CharSequence value, int start, int before, int count) {
            }

            /** 刷新表单预览，推荐值批量填入时只在最后归约一次。 */
            @Override
            public void afterTextChanged(Editable value) {
                if (fillingRecommended) return;
                formMessage.setVisibility(View.GONE);
                refreshValidation();
            }
        });
    }

    /** 校验三个字段并就地显示错误，错误情况下不关闭弹窗、不覆盖旧配置。 */
    private ThresholdSettingsForm.Result refreshValidation() {
        ThresholdSettingsForm.Result result = ThresholdSettingsForm.validate(
                unlock.input.getText().toString(), lock.input.getText().toString(), dwell.input.getText().toString());
        summary.setText(result.summary());
        if (showErrors) {
            unlock.showError(result.unlockError);
            lock.showError(result.lockError);
            dwell.showError(result.dwellError);
        }
        return result;
    }

    /** 只填入推荐草稿，用户可以继续编辑或取消，必须明确保存后才改变阈值。 */
    private void fillRecommended() {
        fillingRecommended = true;
        unlock.input.setText(ThresholdSettingsForm.number(VehicleIdentityStore.DEFAULT_UNLOCK_DBM));
        lock.input.setText(ThresholdSettingsForm.number(VehicleIdentityStore.DEFAULT_LOCK_DBM));
        dwell.input.setText(ThresholdSettingsForm.seconds(VehicleIdentityStore.DEFAULT_LOCK_DWELL_MS));
        fillingRecommended = false;
        showErrors = false;
        unlock.showError("");
        lock.showError("");
        dwell.showError("");
        refreshValidation();
        formMessage.setTextColor(COLOR_TEAL);
        formMessage.setText("推荐值已填入，点击保存后才生效");
        formMessage.setVisibility(View.VISIBLE);
    }

    /** 用户明确保存时统一走校验和单次回调，首个错误字段获得焦点，失败留在当前弹窗。 */
    private void save() {
        showErrors = true;
        ThresholdSettingsForm.Result result = refreshValidation();
        if (!result.isValid()) {
            Field first = !result.unlockError.isEmpty() ? unlock : (!result.lockError.isEmpty() ? lock : dwell);
            first.input.requestFocus();
            return;
        }
        try {
            onSave.onSave(result.unlockDbm, result.lockDbm, result.dwellMs);
            dialog.dismiss();
        } catch (RuntimeException failed) {
            formMessage.setTextColor(COLOR_ERROR);
            formMessage.setText("保存失败，请稍后重试；当前输入已保留");
            formMessage.setVisibility(View.VISIBLE);
        }
    }

    /** 构造与主页一致的纵向容器，父布局通过权重给滚动内容分配可用高度。 */
    private LinearLayout column() {
        LinearLayout view = new LinearLayout(activity);
        view.setOrientation(LinearLayout.VERTICAL);
        return view;
    }

    /** 文字可随系统字号换行，不固定说明区高度，也不依赖颜色表达字段含义。 */
    private TextView text(String value, int sp, int color, boolean bold) {
        TextView view = new TextView(activity);
        view.setText(value);
        view.setTextSize(sp);
        view.setTextColor(color);
        view.setTypeface(Typeface.DEFAULT, bold ? Typeface.BOLD : Typeface.NORMAL);
        view.setLineSpacing(dp(2), 1.08f);
        return view;
    }

    /** 字段与操作区统一外边距，长文字按实际高度排版。 */
    private LinearLayout.LayoutParams spaced(int top) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = dp(top);
        return params;
    }

    /** 可点击按钮保留水波反馈与至少48dp触控高度，不使用无法辨认的无文字图标。 */
    private Button action(String label, boolean primary) {
        Button button = new Button(activity);
        button.setText(label);
        button.setTextSize(15);
        button.setAllCaps(false);
        button.setTextColor(primary ? COLOR_BG : COLOR_TEXT);
        button.setMinHeight(dp(48));
        button.setMinimumHeight(dp(48));
        button.setMinWidth(0);
        button.setMinimumWidth(0);
        button.setPadding(dp(12), dp(10), dp(12), dp(10));
        button.setStateListAnimator(null);
        GradientDrawable background = rounded(primary ? COLOR_BLUE : COLOR_CARD, 12,
                primary ? COLOR_BLUE : COLOR_BORDER);
        button.setBackground(new RippleDrawable(ColorStateList.valueOf(Color.argb(50, 255, 255, 255)), background, null));
        return button;
    }

    /** 使用相同圆角与边框层级，输入聚焦时仅替换边框色。 */
    private GradientDrawable rounded(int color, int radius, int stroke) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        drawable.setStroke(dp(1), stroke);
        return drawable;
    }

    /** 根据设备密度换算布局尺寸，文本另用sp遵循用户字号。 */
    private int dp(int value) {
        return Math.round(value * activity.getResources().getDisplayMetrics().density);
    }
}
