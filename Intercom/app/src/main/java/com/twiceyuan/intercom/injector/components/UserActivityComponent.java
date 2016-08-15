package com.twiceyuan.intercom.injector.components;


import com.twiceyuan.intercom.injector.ActivitySingleton;
import com.twiceyuan.intercom.injector.modules.ActivityModule;
import com.twiceyuan.intercom.injector.modules.GoogleClientModule;
import com.twiceyuan.intercom.ui.view.social.ChatActivity;
import com.twiceyuan.intercom.ui.view.social.ConversationsActivity;
import com.twiceyuan.intercom.ui.view.social.ProfileActivity;

import dagger.Component;

/**
 * Created by twiceYuan on 1/10/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
@ActivitySingleton
@Component(dependencies = {UserComponent.class}, modules = {ActivityModule.class, GoogleClientModule.class})
public interface UserActivityComponent extends ActivityComponent {

    void inject(ConversationsActivity activity);
    void inject(ChatActivity activity);
    void inject(ProfileActivity activity);
}
