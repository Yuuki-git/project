package com.mwz.crm.dao;

import com.mwz.crm.base.BaseMapper;
import com.mwz.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer>{
        public Integer selectCountByRoleId(Integer roleId);

        Integer deleteByRoleId(Integer roleId);

        // 查询角色的所有资源id集合
        List<Integer> queryRoleHasModuleIdsByRoleId(Integer roleId);

        //通过用户ID查询对应的资源列表（资源权限码）
    List<String> queryUserHasRoleHasPermissionByUserId(Integer userId);

    Integer selectByModuleId(Integer ids);

    Integer deleteByModuleId(Integer ids);
}