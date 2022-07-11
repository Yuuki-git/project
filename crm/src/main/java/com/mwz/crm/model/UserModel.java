package com.mwz.crm.model;

/**
 * @author: yuuki
 * @Date: 2021-04-09 - 04 - 14:32
 * @Description: com.mwz.crm.model
 * @version: 1.0
 */
public class UserModel {
    // private Integer userId;
    private String userIdStr;

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    private String userName;
    private String trueName;

    // public Integer getUserId() {
    //     return userId;
    // }
    //
    // public void setUserId(Integer userId) {
    //     this.userId = userId;
    // }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
