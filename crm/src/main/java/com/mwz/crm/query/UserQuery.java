package com.mwz.crm.query;

import com.mwz.crm.base.BaseQuery;

/**
 * @author: yuuki
 * @Date: 2021-06-01 - 06 - 17:08
 * @Description: com.mwz.crm.query
 * @version: 1.0
 */
public class UserQuery extends BaseQuery {
    private String userName;
    private String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;
}
