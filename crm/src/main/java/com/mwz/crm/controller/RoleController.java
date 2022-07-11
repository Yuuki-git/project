package com.mwz.crm.controller;

import com.mwz.crm.base.BaseController;
import com.mwz.crm.base.ResultInfo;
import com.mwz.crm.query.RoleQuery;
import com.mwz.crm.service.RoleService;
import com.mwz.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-07 - 06 - 17:16
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */
@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;

    /**
     * 查询角色
     * @param roleQuery
     * @return
     */
    @RequestMapping("roleList")
    @ResponseBody
    public Map<String,Object> queryRoleList(RoleQuery roleQuery){
        return roleService.queryRoleByParams(roleQuery);
    }

    /**
     * 进入页面
     * @return
     */
    @RequestMapping("index")
    public String toRolePge(){return "role/role";}

    @RequestMapping("findRoles")
    @ResponseBody
    public List<Map<String,Object>> findRoles(Integer id){return roleService.findRoles(id);}

    @RequestMapping("addRole")
    @ResponseBody
    public ResultInfo addRoles(Role role){
        roleService.addRoles(role);
        return success("添加成功");
    }

    @RequestMapping("toAddAndUpdatePage")
    public String toAddAndUpdatePage(Integer roleId, HttpServletRequest request){
        Role role=roleService.selectByPrimaryKey(roleId);
        request.setAttribute("role",role);
        return "role/addAndUpdateRole";
    }

    @RequestMapping("updateRole")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.update(role);
        return success("更新操作成功");
    }

    @RequestMapping("deleteRole")
    @ResponseBody
    public ResultInfo deleteRole(Integer[] ids){
        roleService.deleteRole(ids);
        return success("删除操作成功");
    }

    @RequestMapping("grant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] ids){
        roleService.addGrant(roleId,ids);
        return success("授权成功");
    }


}
