package com.twiceyuan.intercom.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import rx.Observable;

/**
 * Created by twiceYuan on 8/5/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class ViewUtil {

    public static Observable<String> observeText(EditText editText) {
        TextWatcher textWatcher[] = new TextWatcher[1];
        Observable<String> textObservable = Observable.create(subscriber -> {
            editText.addTextChangedListener(textWatcher[0] = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    subscriber.onNext(s.toString());
                }
            });
        });
        textObservable.doOnUnsubscribe(() -> {
            if (textWatcher[0] != null) {
                editText.removeTextChangedListener(textWatcher[0]);
            }
        });
        return textObservable;
    }
}
