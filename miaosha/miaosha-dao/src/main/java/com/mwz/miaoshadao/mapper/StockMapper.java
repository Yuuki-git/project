package com.mwz.miaoshadao.mapper;

import com.mwz.miaoshadao.dao.Stock;
import com.mwz.miaoshadao.dao.StockExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StockMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Stock record);

    int insertSelective(Stock record);

    Stock selectByPrimaryKey(Integer id);

    Stock selectByPrimaryKeyForUpdate(Integer id);

    int updateByPrimaryKeySelective(Stock record);

    int updateByPrimaryKey(Stock record);

    // 版本号 or 状态
    int updateByOptimistic(Stock record);
}