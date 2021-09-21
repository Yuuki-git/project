package com.mwz.crm.controller;

import com.mwz.crm.base.BaseController;
import com.mwz.crm.base.ResultInfo;
import com.mwz.crm.dao.UserMapper;
import com.mwz.crm.exceptions.ParamsException;
import com.mwz.crm.model.UserModel;
import com.mwz.crm.query.UserQuery;
import com.mwz.crm.service.UserService;
import com.mwz.crm.utils.LoginUserUtil;
import com.mwz.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import sun.misc.Request;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-04-06 - 04 - 0:39
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;
    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){
        ResultInfo resultInfo=new ResultInfo();

        /*try {
            UserModel userModel= userService.userLogin(userName,userPwd);
            // 将对象设置到resultInfo中
            resultInfo.setResult(userModel);
        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
        }catch (Exception e){
            resultInfo.setCode(300);
            resultInfo.setMsg("操作失败");
        }*/
        UserModel userModel= userService.userLogin(userName,userPwd);
        // 将对象设置到resultInfo中
        resultInfo.setResult(userModel);
        return resultInfo;
    }
    @PostMapping("updateUserPwd")
    @ResponseBody
    public ResultInfo updateUserPwd(HttpServletRequest request,String userPwd,String newPwd,String repeatPwd){
        ResultInfo resultInfo=new ResultInfo();
        /*try {
            //从cookie中获取用户id
            Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
            userService.updateUserPwd(userId,userPwd,newPwd,repeatPwd);
        }catch (ParamsException p){
            p.printStackTrace();
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(300);
            resultInfo.setMsg("密码修改失败！");
        }*/
        //从cookie中获取用户id
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updateUserPwd(userId,userPwd,newPwd,repeatPwd);
        return resultInfo;
    }

    /**
     * 进入修改密码的界面
     * @return
     */
    @RequestMapping("updatePWDPage")
    public String toPasswordPage(){
        return "user/password";
    }


    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales(){
        return userService.queryAllSales();
    }

    /**
     * 进入用户管理页面
     * @return
     */
    @RequestMapping("index")
    public String toUserPage(){return "user/user";}

    /**
     *
     * @param userQuery
     * @return
     */
    @RequestMapping("userList")
    @ResponseBody
    public Map<String,Object> queryUserList(UserQuery userQuery){
        return userService.queryUser(userQuery);
    }

    /**
     * 添加用hu
     * @param user
     */
    @PostMapping("addUser")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("添加用户成功");
    }

    @RequestMapping("toAddUserPage")
    public String toAddUserPage(Integer id,HttpServletRequest request){
        User user=userService.selectByPrimaryKey(id);
        request.setAttribute("userModel",user);
        return "user/addAndUpdateUser";
    }

    @RequestMapping("updateUser")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("更新操作成功");
    }

    @RequestMapping("deleteUsers")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
            userService.deleteUser(ids);
            return success("删除操作成功");
    }
}
