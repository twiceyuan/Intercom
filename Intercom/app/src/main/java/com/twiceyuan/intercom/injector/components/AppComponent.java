package com.twiceyuan.intercom.injector.components;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.twiceyuan.intercom.App;
import com.twiceyuan.intercom.injector.AppContext;
import com.twiceyuan.intercom.injector.modules.ApiModule;
import com.twiceyuan.intercom.injector.modules.AppModule;
import com.twiceyuan.intercom.service.ImageHostService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by twiceYuan on 1/9/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    void inject(App app);

    @AppContext
    Context context();

    App application();

    ImageHostService imageHostService();

    FirebaseAuth auth();

    DatabaseReference ref();
}
