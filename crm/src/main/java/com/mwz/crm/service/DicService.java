package com.mwz.crm.service;

import com.github.pagehelper.PageInfo;
import com.mwz.crm.base.BaseMapper;
import com.mwz.crm.base.BaseService;
import com.mwz.crm.dao.DicMapper;
import com.mwz.crm.utils.AssertUtil;
import com.mwz.crm.vo.Dic;
import org.apache.commons.lang3.StringUtils;
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
 * @Date: 2021-06-12 - 06 - 12:43
 * @Description: com.mwz.crm.service
 * @version: 1.0
 */
@Service
public class DicService extends BaseService<Dic,Integer> {

    @Resource
    private DicMapper dicMapper;


    /**
     * 查询
     * @param dicName
     * @return map
     */
    public Map<String, Object> queryByParam(String dicName) {
        Map<String, Object> map =new HashMap<>();
        List<Dic> dicList=dicMapper.queryByParam(dicName);
        PageInfo<Dic> pageInfo=new PageInfo<>(dicList);
        map.put("code",0);
        map.put("msg","ok");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加操作
     * 参数校验
     * 设置默认值
     * 执行操作
     * @param dic
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addDic(Dic dic) {
        checkAddParams(dic.getDataDicName(),dic.getDataDicValue());
        dic.setIsValid((byte) 1);
        dic.setCreateDate(new Date());
        dic.setUpdateDate(new Date());
        AssertUtil.isTrue(dicMapper.insertSelective(dic)<1,"添加操作失败");
    }

    private void checkAddParams(String dataDicName, String dataDicValue) {
        AssertUtil.isTrue(StringUtils.isBlank(dataDicName),"字典名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(dataDicValue),"字典值不能为空");
    }

    /**
     * 更新
     * @param dic
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDic(Dic dic) {
        Integer id=dic.getId();
        AssertUtil.isTrue(id==null||dicMapper.selectByPrimaryKey(id)==null,"待更新记录不存在");
        checkAddParams(dic.getDataDicName(),dic.getDataDicValue());
        dic.setUpdateDate(new Date());
        AssertUtil.isTrue(dicMapper.updateByPrimaryKeySelective(dic)<1,"更新操作失败");
    }

    /**
     * 删除
     * @param id
     */
    public void deleteDicById(Integer id) {
        AssertUtil.isTrue(id==null||dicMapper.selectByPrimaryKey(id)==null,"待删除记录不存在");
        AssertUtil.isTrue(dicMapper.deleteById(id)<1,"删除失败");
    }

    // public List<Map<String, Object>> selectDicByName(String dicName) {
    //     /*查询操作*/
    //     List<Map<String, Object>> list = dicMapper.selectDicByName(dicName);
    //     AssertUtil.isTrue(list == null,"list为空!");
    //     return list;
    // }
}
