package com.twiceyuan.intercom.ui.view.account;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.twiceyuan.intercom.BuildConfig;
import com.twiceyuan.intercom.R;
import com.twiceyuan.intercom.ui.base.BaseFragment;

/**
 * Created by twiceYuan on 8/3/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class InputAccountFragment extends BaseFragment {

    EditText mEtEmail;
    EditText mEdPassword;

    private TextView btnConfirm;
    private TextView btnCancel;
    private InfoCallback mInfoCallback;

    public static InputAccountFragment newInstance() {
        return new InputAccountFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_account, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEdPassword = bind(R.id.et_password);
        mEtEmail = bind(R.id.et_email);
        btnConfirm = bind(R.id.tv_confirm);
        btnCancel = bind(R.id.tv_cancel);

        if (BuildConfig.DEBUG) {
            mEtEmail.setText("twiceyuan@foxmail.com");
            mEdPassword.setText("dudebai");
        }

        btnCancel.setOnClickListener(v -> getActivity().finish());
        btnConfirm.setOnClickListener(v -> {
            if (mInfoCallback != null) getAccountInfo(mInfoCallback);
        });
    }

    public void setOnSubmitListener(InfoCallback callback) {
        mInfoCallback = callback;
    }

    public void getAccountInfo(@NonNull InfoCallback callback) {
        String email = "";
        String password = "";
        try {
            email = mEtEmail.getText().toString();
        } catch (Exception ignored) {
        }
        try {
            password = mEdPassword.getText().toString();
        } catch (Exception ignored) {
        }
        callback.call(email, password);
    }

    public void setAccountInfo(String email, String password) {
        mEtEmail.setText(email);
        mEdPassword.setText(password);
    }

    public interface InfoCallback {
        void call(String email, String password);
    }

    public void moveToLast() {
        if (mEdPassword.length() != 0) {
            mEdPassword.requestFocus();
            mEdPassword.setSelection(mEdPassword.length());
        } else {
            mEtEmail.requestFocus();
            mEtEmail.setSelection(mEtEmail.length());
        }
    }
}
