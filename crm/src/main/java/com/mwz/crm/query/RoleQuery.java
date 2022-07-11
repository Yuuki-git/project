package com.mwz.crm.query;

import com.mwz.crm.base.BaseQuery;

/**
 * @author: yuuki
 * @Date: 2021-06-07 - 06 - 16:35
 * @Description: com.mwz.crm.query
 * @version: 1.0
 */
public class RoleQuery extends BaseQuery {
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
