package com.zln.lovecottage;

import com.szh.commonBase.BaseApplication;
import com.zln.lovecottage.item.UserItem;

/**
 * Created by Songzhihang on 2019/1/6.
 */
public class MyApplication extends BaseApplication<UserItem> {


    private UserItem userItem;

    @Override
    public UserItem getUser() {
        return userItem;
    }

    @Override
    public void setUser(UserItem user) {
        this.userItem = user;
    }
}
