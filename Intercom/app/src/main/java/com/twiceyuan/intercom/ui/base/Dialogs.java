package com.twiceyuan.intercom.ui.base;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.twiceyuan.intercom.R;
import com.twiceyuan.intercom.common.DP;
import com.twiceyuan.intercom.common.Toaster;

import rx.functions.Action1;

/**
 * Created by twiceYuan on 3/22/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * 通用对话框整理
 */
@SuppressWarnings("unused")
public class Dialogs {

    /**
     * 输入结果回调接口
     */
    public interface OnTextInput {
        void onInput(String content);
    }

    /**
     * 确定回调接口
     */
    public interface OnConfirm {
        void onConfirm();
    }

    /**
     * 取消回调接口
     */
    public interface OnCancel {
        void cancel();
    }

    /**
     * 无需 Context 的输入对话框
     *
     * @param title       标题
     * @param hint        提示
     * @param onTextInput 输入结果回调
     */
    @SuppressWarnings("unused")
    public static void inputDialog(
            Activity activity,
            @Nullable String title, @Nullable String hint,
            Action1<EditText> inputCallback,
            OnTextInput onTextInput) {

        EditText inputText = new EditText(activity);
        LinearLayout container = new LinearLayout(activity);

        int paddingPx = DP.dp2px(16);
        container.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        container.addView(inputText);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        container.setLayoutParams(params);
        inputText.setLayoutParams(params);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (title != null) builder.setTitle(title);
        if (hint != null) inputText.setHint(hint);

        if (inputCallback != null) {
            inputCallback.call(inputText);
        }

        builder.setView(container);
        builder.setPositiveButton("确定", (dialog, which) -> {
            onTextInput.onInput(inputText.getText().toString());
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 带 Null 值判断的输入框
     *
     * @param activity    context
     * @param title       标题
     * @param defaultText 默认文字
     * @param nullHind    输入为空时的提示
     * @param onTextInput 输入完毕回调
     */
    public static void inputWithNull(
            Activity activity, String title, String defaultText, String nullHind, OnTextInput onTextInput) {

        EditText editText = new EditText(activity);
        LinearLayout container = new LinearLayout(activity);

        int paddingPx = DP.dp2px(16);
        container.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        container.addView(editText);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        container.setLayoutParams(params);
        editText.setLayoutParams(params);
        editText.setText(defaultText);
        editText.setSelection(defaultText.length());

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(container)
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            String input = editText.getText().toString();
            if (TextUtils.isEmpty(input)) {
                Toaster.s(nullHind);
                return;
            }
            onTextInput.onInput(input);
            alertDialog.dismiss();
        });
    }

    public static void simpleMessage(Activity activity, String message, OnConfirm onConfirm, OnCancel onCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton("确定", (dialog, which) -> {
            if (onConfirm != null) onConfirm.onConfirm();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            if (onCancel != null) onCancel.cancel();
        });
        builder.show();
    }

    public static void simpleMessage(Activity activity, String title, String message, OnConfirm confirm, OnCancel cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", (dialog, which) -> {
            if (confirm != null) confirm.onConfirm();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            if (cancel != null) cancel.cancel();
        });
        builder.show();
    }

    public static void simpleMessage(Activity activity, String message, OnConfirm onConfirm) {
        simpleMessage(activity, message, onConfirm, null);
    }

    /**
     * 单选对话框
     *
     * @param array            可选的选项, 字符串数组形式
     * @param positionCallback 选中的位置回调
     */
    public static void singleChoice(Activity context, Action1<Integer> positionCallback, String... array) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setAdapter(new ArrayAdapter<>(
                        context,
                        R.layout.item_dialog_single_choice,
                        array), (d, which) -> {
                    if (positionCallback != null) {
                        positionCallback.call(which);
                    }
                }).create();
        dialog.show();
    }
}
