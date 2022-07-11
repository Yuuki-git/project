package com.mwz.crm.controller;

import com.mwz.crm.base.BaseController;
import com.mwz.crm.base.ResultInfo;
import com.mwz.crm.dao.DicMapper;
import com.mwz.crm.service.DicService;
import com.mwz.crm.vo.Dic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-12 - 06 - 12:46
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */
@Controller
@RequestMapping("dic")
public class DicController extends BaseController {
    @Resource
    private DicService dicService;
    @RequestMapping("index")
    public String toDicPage(){
        return "dic/dic";
    }
    @GetMapping("selectAllDic")
    @ResponseBody
    public Map<String,Object> queryDicByParam(String dicName){
        return dicService.queryByParam(dicName);
    }
    @RequestMapping("toAddAndUpdatePage")
    public String toAddAndUpdatePage(Integer id, HttpServletRequest request){
        Dic dic = dicService.selectByPrimaryKey(id);
        request.setAttribute("dic",dic);
        return "dic/addAndUpdate";
    }
    @PostMapping("addDic")
    @ResponseBody
    public ResultInfo addDic(Dic dic){
        dicService.addDic(dic);
        return success("添加成功");
    }
    @PostMapping("updateDic")
    @ResponseBody
    public ResultInfo updateDic(Dic dic){
        dicService.updateDic(dic);
        return success("更新成功");
    }
    @PostMapping("deleteDicById")
    @ResponseBody
    public ResultInfo deleteDicById(Integer id){
        dicService.deleteDicById(id);
        return success("删除成功");
    }
}
