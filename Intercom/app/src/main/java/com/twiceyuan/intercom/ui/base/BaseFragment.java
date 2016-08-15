package com.twiceyuan.intercom.ui.base;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by twiceYuan on 8/3/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * BaseFragment 基类
 */
public class BaseFragment extends Fragment {

    protected <T extends View> T bind(@IdRes int id) {
        if (getView() == null) return null;
        //noinspection unchecked
        return (T) getView().findViewById(id);
    }
}
