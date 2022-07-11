package com.mwz.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mwz.crm.base.BaseService;
import com.mwz.crm.dao.SaleChanceMapper;
import com.mwz.crm.enums.DevResult;
import com.mwz.crm.enums.StateStatus;
import com.mwz.crm.query.SaleChanceQuery;
import com.mwz.crm.utils.AssertUtil;
import com.mwz.crm.utils.PhoneUtil;
import com.mwz.crm.vo.CusDevPlan;
import com.mwz.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.ACTIVITY_REQUIRED;
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
 * @Date: 2021-05-15 - 05 - 11:39
 * @Description: com.mwz.crm.service
 * @version: 1.0
 */
@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * Layui要求返回数据的格式:
     * {
     *     code:
     *     msg:
     *     count:总数量
     *     date:[数据列表]
     * }
     * @param saleChanceQuery
     * @return
     */
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        Map<String,Object> map=new HashMap<>();
        //开启分页
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());

        // 得到对应的营销机会的列表
        List<SaleChance> saleChanceList=saleChanceMapper.selectByParams(saleChanceQuery);


        //得到分页对象
        //将集合传给分页对象
        PageInfo<SaleChance> pageInfo=new PageInfo<>(saleChanceList);
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     *  添加营销机会
     *      1.参数校验
     *      customerName 客户名称 非空
     *      linkMan        联系人 非空
     *      linkPhone       联系电话 非空手机号
     *      2.设置相关参数的默认值
     *          createMan创建人 当前登录用户名
     *          assignMan指派人 如果未设置指派人 默认 状态未分配 0 指派时间 null
     *          如果设置了指派人 1 状态已分配 指派时间系统当前时间
     *          devResult 0 未开发 1开发中 2开发成功 3开发失败
     *          开发状态 默认未开发 设置分配人 那就是开发中
     *          is_valid是否有效 默认有效（1） 0是无效
     *          createDate创建时间
     *              默认系统当前时间
     *          updateDate
     *              默认系统当前时间
     *         3.实行添加操作，判断受影响的行数 如果不是1就是失败
     * @param saleChance
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
            // 参数校验
            checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
            // 设置默认状态
            // is_valid是否有效 默认有效（1） 0是无效
            saleChance.setIsValid(1);
            //  createDate创建时间
            saleChance.setCreateDate(new Date());
            //  updateDate
            saleChance.setUpdateDate(new Date());
            // 判断指派人是否为空  assignMan指派人 如果未设置指派人 默认 状态未分配 0 指派时间 null
            //  如果设置了指派人 1 状态已分配 指派时间系统当前时间
            // *          开发状态 默认未开发 设置分配人 那就是开发中
            if(StringUtils.isBlank(saleChance.getAssignMan())){
                saleChance.setState(StateStatus.UNSTATE.getType());
                saleChance.setAssignTime(null);
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            }else{
                saleChance.setState(StateStatus.STATED.getType());
                saleChance.setAssignTime(new Date());
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }
            // 执行添加操作，判断受影响的行数
            AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)!=1,"添加营销机会失败");
    }

    /**
     *    1.参数校验
     *      customerName 客户名称 非空
     *      linkMan        联系人 非空
     *      linkPhone       联系电话 非空手机号

     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        // 客户名称非空
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名称不能为空");
        // 联系人非空
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        // 联系电话不能为空
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系电话不能为空");
        // 判断电话格式是否正确
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"联系电话格式不正确");
    }

    /**
     * 更新音效机会
     *    1.参数校验
     *      ID          非空且在数据库中存在
     *      customerName 客户名称 非空
     *      linkMan        联系人 非空
     *      linkPhone       联系电话 非空手机号
     *    2.参数更新
     *      upDateTime 设置为当前系统时间
     *      assignMan指派人
     *          之前未设置指派人
     *              更新未设置指派人
     *                  无需更改
     *               更新后设置指派人
     *                  状态已分配 指派时间系统当前时间  开发中
     *          之前设置指派人
     *              更新未设置指派人
     *                  装态未分配 指派时间null 未开发
     *
     *              更新后设置指派人
     *                  前后一致
     *                      无需更改
     *                  前后不同
     *                      指派时间改为系统当前时间
     *    3.执行更新操作，判断受影响的行数
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        //   ID          非空且在数据库中存在
        AssertUtil.isTrue(null==saleChance.getId(),"待更新记录不存在!");
        // 得到数据库中的记录
        SaleChance temp=selectByPrimaryKey(saleChance.getId());
        // 判断记录是否村子啊
        AssertUtil.isTrue(temp==null,"待更新记录不存在!");
        // 参数校验
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        // 更新参数
        // 设置更新时间
        saleChance.setUpdateDate(new Date());
        // 指派人
        if(StringUtils.isBlank(temp.getAssignMan())){   // 修改前无值
            if(StringUtils.isBlank(saleChance.getAssignMan())){ // 修改后无值
            }else {                                     // 修改后有值
                saleChance.setAssignTime(new Date());
                saleChance.setState(StateStatus.STATED.getType());
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }
        }else {if(StringUtils.isBlank(saleChance.getAssignMan())){   //修改前有值，修改后无值
            saleChance.setAssignTime(null);
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }else {                                                     // 修改前有值，修改后无值
            if(temp.getAssignMan().equals(saleChance.getAssignMan())){  //前后值相同
                saleChance.setAssignTime(temp.getAssignTime());
            }else {
                saleChance.setAssignTime(new Date());
            }

        }

        }
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)!=1,"营销机会更新失败");
    }

    /**
     * 删除（修改）
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        // 判断id是否为空
        AssertUtil.isTrue(null==ids||ids.length<1,"待删除记录不存在！");
        // 实行操作判断删除的行数是否相等
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)!=ids.length,"删除营销机会记录失败");
    }

    /**
     * 更新开发状态
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id,Integer devResult) {
        AssertUtil.isTrue(null==id,"数据不存在");
        SaleChance saleChance=saleChanceMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(saleChance == null,"数据不存在");
        saleChance.setDevResult(devResult);
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)!=1,"开发状态更新操作失败！");
    }
}
