package com.mwz.crm.dao;

import com.mwz.crm.base.BaseMapper;
import com.mwz.crm.model.TreeModel;
import com.mwz.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    // 查询所有的资源列表
    public List<TreeModel> queryAllModules();
    List<Module> queryModuleList();

   Module selectByGradeAndModule(@Param("grade") Integer grade, @Param("moduleName") String moduleName);

   Module selectByGradeAndUrl(@Param("grade") Integer grade,@Param("url") String url);

   Module selectByOptValue(String optValue);

    Integer deleteByPrimaryKeyAndParentId(Integer ids);

    Integer selectByParentId(Integer ids);
}