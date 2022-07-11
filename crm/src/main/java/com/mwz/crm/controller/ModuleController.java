package com.mwz.crm.controller;

import com.mwz.crm.base.BaseController;
import com.mwz.crm.base.ResultInfo;
import com.mwz.crm.model.TreeModel;
import com.mwz.crm.service.ModuleService;
import com.mwz.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-08 - 06 - 13:42
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */
@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;
    @RequestMapping("index")
    public String toModulePage(){
        return "module/module";
    }

    @RequestMapping("toModulePage")
    public String toGrant(HttpServletRequest request,Integer roleId){
        request.setAttribute("roleId",roleId);
        return "role/grant";
    }

    @RequestMapping("moduleList")
    @ResponseBody
    public Map<String,Object> queryModuleList(){
        return moduleService.queryModuleList();
    }

    @RequestMapping("modules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer roleId){
        return moduleService.queryAllModule(roleId);
    }

    @RequestMapping("toAddModule")
    public String toAddModule(Integer grade, Integer parentId, HttpServletRequest request){
        request.setAttribute("grade",grade);
        request.setAttribute("parentId",parentId);
        return "module/addModule";
    }
    @RequestMapping("addModule")
    @ResponseBody
    public ResultInfo addModule(Module module){
        moduleService.addModule(module);
        return success("添加资源成功");
    }

    @RequestMapping("toUpdateModule")
    public String toupdateModule(Integer id,HttpServletRequest request){
        Module module=moduleService.selectByPrimaryKey(id);
        request.setAttribute("module",module);
        return "module/updateModule";
    }
    @RequestMapping("updateModule")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("更新操作成功");
    }
    @RequestMapping("deleteModule")
    @ResponseBody
    public ResultInfo deleteModule(Integer ids){
        moduleService.deleteModule(ids);
        return success("删除操作成功");
    }

}
