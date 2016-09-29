package com.twiceyuan.intercom.common

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

import rx.Observable

/**
 * Created by twiceYuan on 8/5/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
object ViewUtil {

    fun observeText(editText: EditText): Observable<String> {
        val textWatcher = arrayOfNulls<TextWatcher>(1)
        val textObservable = Observable.create<String> { subscriber ->
            textWatcher[0] = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable) {
                    subscriber.onNext(s.toString())
                }
            }
            editText.addTextChangedListener(textWatcher[0])
        }
        textObservable.doOnUnsubscribe {
            if (textWatcher[0] != null) {
                editText.removeTextChangedListener(textWatcher[0])
            }
        }
        return textObservable
    }
}
