package com.mwz.crm.dao;

import com.mwz.crm.base.BaseMapper;
import com.mwz.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    //查询所有的角色
    public List<Map<String,Object>> queryAllRoles(Integer id);
}