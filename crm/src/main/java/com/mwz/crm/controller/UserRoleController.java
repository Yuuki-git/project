package com.mwz.crm.controller;

import com.mwz.crm.base.BaseController;
import com.mwz.crm.service.UserRoleService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author: yuuki
 * @Date: 2021-06-08 - 06 - 10:47
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */
@Controller
public class UserRoleController extends BaseController {
    @Resource
    private UserRoleService userRoleService;
}
