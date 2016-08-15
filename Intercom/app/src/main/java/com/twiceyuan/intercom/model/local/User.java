package com.twiceyuan.intercom.model.local;

import com.google.firebase.auth.FirebaseUser;
import com.twiceyuan.intercom.common.Check;

/**
 * Created by twiceYuan on 8/2/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 用户资料，存储时以用户 Uid 作为键
 */
public class User {

    public static final String USERNAME  = "username";
    public static final String EMAIL     = "email";
    public static final String PHOTO_URL = "photoUrl";
    public static final String GLOBAL_ID = "globalId";

    public String username;
    public String email;
    public String photoUrl;
    public String globalId;

    public static class Builder {

        private User mUser = new User();

        public Builder from(FirebaseUser user) {
            mUser.email = user.getEmail();
            mUser.username = user.getDisplayName();
            Check.ifNull(mUser.username, () -> mUser.username = mUser.email.split("@")[0]);
            Check.notNull(user.getPhotoUrl(), url -> mUser.photoUrl = url.toString());
            return this;
        }

        public User build() {
            return mUser;
        }
    }
}
