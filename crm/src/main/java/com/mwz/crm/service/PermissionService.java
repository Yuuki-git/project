package com.mwz.crm.service;

import com.mwz.crm.base.BaseService;
import com.mwz.crm.dao.PermissionMapper;
import com.mwz.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: yuuki
 * @Date: 2021-06-09 - 06 - 10:19
 * @Description: com.mwz.crm.service
 * @version: 1.0
 */
@Service
public class PermissionService extends BaseService<Permission,Integer> {
    @Resource
    private PermissionMapper permissionMapper;

    /***
     * 通过查询用户拥有的角色，角色拥有的资源，得到用户拥有的资源列表（资源授权码）
     * @param userId
     * @return
     */
    public List<String> queryUserHasRoleHasPermissionByUserId(Integer userId) {
        return permissionMapper.queryUserHasRoleHasPermissionByUserId(userId);
    }
}
