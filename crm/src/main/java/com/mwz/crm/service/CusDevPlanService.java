package com.mwz.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mwz.crm.base.BaseService;
import com.mwz.crm.dao.CusDevPlanMapper;
import com.mwz.crm.query.CusDevPlanQuery;
import com.mwz.crm.query.SaleChanceQuery;
import com.mwz.crm.utils.AssertUtil;
import com.mwz.crm.vo.CusDevPlan;
import com.mwz.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yuuki
 * @Date: 2021-06-01 - 06 - 10:27
 * @Description: com.mwz.crm.service
 * @version: 1.0
 */
@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {
    @Resource
    private CusDevPlanMapper cusDevPlanMapper;

    /**
     * 多条件查询客户开发计划
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String,Object> map=new HashMap<>();
        //开启分页
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());

        // 得到对应的营销机会的列表
        List<CusDevPlan> cusDevPlanList=cusDevPlanMapper.selectByParams(cusDevPlanQuery);


        //得到分页对象
        //将集合传给分页对象
        PageInfo<CusDevPlan> pageInfo=new PageInfo<>(cusDevPlanList);
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加客户开发计划
     * 1、参数校验
     *      营销机会id  非空且存在
     *      计划项内容   非空
     *      计划时间    非空
     * 2、设置默认值
     *      是否有效    有效
     *      创建时间    当前时间
     *      修改时间    当前时间
     *  3、执行操作，判断受影响的行数
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan){
        checkCusDevPlanParams(cusDevPlan);
        //设置有效
        cusDevPlan.setIsValid(1);
        //设置创建时间
        cusDevPlan.setCreateDate(new Date());
        //设置修改时间
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan)!=1,"客户开发计划添加失败！");
    }

    /**
     * 更新开发计划
     *      1参数校验
     *          计划id 非空且存在
     *          营销机会id  非空且存在
     *          计划项内容   非空
     *          计划时间    非空
     *      2、设置默认值
     *
     *          修改时间    当前时间
     *        3、执行操作，判断受影响的行数
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        // 判断计划id非空且存在
            Integer id=cusDevPlan.getId();
            AssertUtil.isTrue(null==id||cusDevPlanMapper.selectByPrimaryKey(id)==null,"计划id不能为空");
            checkCusDevPlanParams(cusDevPlan);
            cusDevPlan.setUpdateDate(new Date());
            AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)!=1,"更新操作失败！");

    }

    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
       // 营销机会id 非空企鹅存在
        Integer sId=cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(null==sId || cusDevPlanMapper.selectByPrimaryKey(sId)==null,"数据不存在");
        // 计划项非空
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"计划项不能为空");
        // 计划时间非空
        AssertUtil.isTrue(null==cusDevPlan.getPlanDate(),"计划时间不能为空");
    }

    /**
     * 删除客户计划
     * 1。参数校验
     *      id非空 且数据存在
     * 2.修改相应的选项
     * 3.执行操作判断受影响的行数
     *
     * @param ids
     */
    public void deleteCusDevPlan(Integer ids) {
        AssertUtil.isTrue(null==ids,"数据不存在");
        CusDevPlan cusDevPlan=cusDevPlanMapper.selectByPrimaryKey(ids);
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)!=1,"删除操作失败！");

    }
}
