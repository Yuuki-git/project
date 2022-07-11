package com.mwz.crm.service;

import com.mwz.crm.base.BaseService;
import com.mwz.crm.dao.UserRoleMapper;
import com.mwz.crm.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: yuuki
 * @Date: 2021-06-08 - 06 - 10:46
 * @Description: com.mwz.crm.service
 * @version: 1.0
 */
@Service
public class UserRoleService extends BaseService<UserRole,Integer> {
    @Resource
    private UserRoleMapper userRoleMapper;
}
