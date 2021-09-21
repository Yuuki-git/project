package com.mwz.crm.controller;

import com.mwz.crm.base.BaseController;
import com.mwz.crm.base.ResultInfo;
import com.mwz.crm.dao.CusDevPlanMapper;
import com.mwz.crm.dao.SaleChanceMapper;
import com.mwz.crm.enums.StateStatus;
import com.mwz.crm.query.CusDevPlanQuery;
import com.mwz.crm.query.SaleChanceQuery;
import com.mwz.crm.service.CusDevPlanService;
import com.mwz.crm.service.SaleChanceService;
import com.mwz.crm.utils.LoginUserUtil;
import com.mwz.crm.vo.CusDevPlan;
import com.mwz.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-05-30 - 05 - 16:20
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */
@RequestMapping("cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private CusDevPlanService cusDevPlanService;
    /**
     * 进入客户开发页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cusDevPlan";
    }

    @RequestMapping("toCusDevPlan")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request){
        // 通过id查询
        SaleChance saleChance=saleChanceService.selectByPrimaryKey(id);
        // 将数据设置到请求域中
        request.setAttribute("saleChance",saleChance);
        return "cusDevPlan/cusDevPlanData";

    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCusDevPlanList(CusDevPlanQuery cusDevPlanQuery){
        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }

    /**
     * 添加客户开发计划
     * @param cusDevPlan
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlanLiST(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("计划项添加成功");
    }

    @RequestMapping("toAddAndUpdatePage")
    public String toAddAndUpdatePage(Integer sId,HttpServletRequest request,Integer id){
        // 将sId设置到隐藏于中
        request.setAttribute("sId",sId);
        // 通过id查询数据
        CusDevPlan cusDevPlan= cusDevPlanService.selectByPrimaryKey(id);
        // 将数据设置到请求域中
        request.setAttribute("cusDevPlan",cusDevPlan);

        return "cusDevPlan/addAndUpdate";
    }

    /**
     * 更新计划项
     * @param cusDevPlan
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlanLiST(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("计划项更新成功");
    }

    /**
     * 删除客户开发计划
     * @param ids
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlanLiST(Integer ids){
        cusDevPlanService.deleteCusDevPlan(ids);
        return success("计划项删除成功");
    }
}
