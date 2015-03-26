package com.soget.soget_client.model;

/**
 * Created by wonmook on 2015-03-25.
 */
public class Friend{
    public static enum FRIEND{FRIEND, FRIENDSENT, FRIENDRECEIVE};
    private User userInfo;
    private FRIEND type;

    public Friend(User userInfo, FRIEND type) {
        this.userInfo = userInfo;
        this.type = type;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }

    public FRIEND getType() {
        return type;
    }

    public void setType(FRIEND type) {
        this.type = type;
    }
}
