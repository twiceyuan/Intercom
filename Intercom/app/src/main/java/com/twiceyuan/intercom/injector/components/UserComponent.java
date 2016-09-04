package com.twiceyuan.intercom.injector.components;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.twiceyuan.intercom.App;
import com.twiceyuan.intercom.injector.AppContext;
import com.twiceyuan.intercom.injector.UserScope;
import com.twiceyuan.intercom.injector.modules.UserModule;
import com.twiceyuan.intercom.service.ImageHostService;

import dagger.Component;

/**
 * Created by twiceYuan on 8/8/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
@UserScope
@Component(dependencies = AppComponent.class, modules = UserModule.class)
public interface UserComponent {
    FirebaseUser user();

    @AppContext
    Context context();

    App application();

    ImageHostService imageHostService();

    FirebaseAuth auth();

    DatabaseReference ref();
}
