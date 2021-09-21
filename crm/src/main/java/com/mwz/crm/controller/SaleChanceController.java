package com.mwz.crm.controller;

import com.mwz.crm.annoation.RequiredPermission;
import com.mwz.crm.base.BaseController;
import com.mwz.crm.base.ResultInfo;
import com.mwz.crm.enums.StateStatus;
import com.mwz.crm.query.SaleChanceQuery;
import com.mwz.crm.service.SaleChanceService;
import com.mwz.crm.utils.CookieUtil;
import com.mwz.crm.utils.LoginUserUtil;
import com.mwz.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-05-15 - 05 - 11:41
 * @Description: com.mwz.crm.controller
 * @version: 1.0
 */
@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 若flag为空
     * 营销机会多条件分页查询
     * 若不为空且为一
     * 为用户开发计划查询
     * @param saleChanceQuery
     * @return
     */
    @RequiredPermission(code="101001")
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChanceList(SaleChanceQuery saleChanceQuery,Integer flag,HttpServletRequest request){
        if(flag!=null&&flag==1){
            // 查询客户开发计划
            saleChanceQuery.setState(StateStatus.STATED.getType());
            // 设置指派人
            // 从cookie中拿取用户id
            Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    /**
     * 进入营销机会的页面
     * @return
     */
    @RequiredPermission(code="1010")
    @RequestMapping("index")
    public String index(){
        return "saleChance/saleChance";
    }

    @RequiredPermission(code="101002")
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request){
        // 从cookie获取当前用户名
        String userName= CookieUtil.getCookieValue(request,"userName");
        saleChance.setCreateMan(userName);

        saleChanceService.addSaleChance(saleChance);
        return success("营销机会添加成功");
    }

    /**
     * 添加和修改页面
     * @return
     */
    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer id,HttpServletRequest request)
    {
        if(null!=id){
            // 查询数据
            SaleChance saleChance=saleChanceService.selectByPrimaryKey(id);
            // 将数据设置到请求域中
            request.setAttribute("saleChance",saleChance);
        }
        return "saleChance/addAndUpdate";
    }
    @RequiredPermission(code="101004")
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){

        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会更新成功");
    }

    /**
     * 删除营销机会
     * @param ids
     * @return
     */
    @RequiredPermission(code="101003")
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        saleChanceService.deleteBatch(ids);
        return success("删除营销机会记录成功");
    }

    /**
     *
     * @param id
     * @return
     */
    @PostMapping("updateDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("更新开发状态成功");
    }


}
