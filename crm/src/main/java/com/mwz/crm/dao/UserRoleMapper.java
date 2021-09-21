package com.mwz.crm.dao;

import com.mwz.crm.base.BaseMapper;
import com.mwz.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    // 根据用户id查询数量
    Integer countUserRoleByUserId(Integer userId);

    // 根据用户id删除对应的记录
    Integer deleteUserRoleByUserId(Integer userId);
}