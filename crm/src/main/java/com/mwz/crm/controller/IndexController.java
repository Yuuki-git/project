package com.mwz.crm.controller;

import com.mwz.crm.base.BaseController;
import com.mwz.crm.service.PermissionService;
import com.mwz.crm.service.UserService;
import com.mwz.crm.utils.LoginUserUtil;
import com.mwz.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: yuuki
 * @Date: 2021-04-05 - 04 - 15:31
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */

@Controller
public class IndexController extends BaseController {


     @Resource
     private UserService userService;

     @Resource
     private PermissionService permissionService;
    /**
     * 登录
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {

        return "index";
    }

    /**
     * 系统欢迎页
     * @return
     */
    @RequestMapping("/welcome")
    public String welcome() {

        return "welcome";
    }

    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request) {

        //获取cookie中的userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //查询用户对象,设置session作用域
        User user = userService.selectByPrimaryKey(userId);//通过id返回user对象
         request.getSession().setAttribute("user",user);

        List<String> permissions= permissionService.queryUserHasRoleHasPermissionByUserId(userId);
        // 将集合设置到session作用域中,request只在一次请求中有效
        request.getSession().setAttribute("permissions",permissions);

        return "main";
    }


}
