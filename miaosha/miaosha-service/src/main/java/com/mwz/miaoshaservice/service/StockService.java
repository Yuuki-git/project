package com.mwz.miaoshaservice.service;


import com.mwz.miaoshadao.dao.Stock;

public interface StockService {
    /**
     * 查询库存:通过缓存查询库存
     * 缓存命中:返回库存
     * 缓存未命中:查询数据库写入缓存并返回
     * @param id
     * @return
     */
    Integer getStockCount(int id);

    /**
     * 通过查询数据库获取库存
     * @param id
     * @return
     */
    int getStockCountByDB(int id);

    /**
     * 通过查询缓存获得剩余库存
     * @param id
     * @return
     */
    Integer getStockCountByCache(int id);

    /**
     * 将库存插入缓存
     * @param id
     * @param count
     */
    void setStockCountCache(int id,int count);

    /**
     * 删除库存缓存
     * @param id
     */
    void delStockCountCache(int id);

    /**
     * 根据库存ID查询数据库库存信息
     * @param id
     * @return
     */
    Stock getStockById(int id);

    /**
     * 根据库存ID查询数据库库存信息(悲观锁)
     * @param id
     * @return
     */
    Stock getStockByIdForUpdate(int id);

    /**
     * 更新数据库库存信息
     * @param stock
     * @return
     */
    int updateStockById(Stock stock);

    /**
     * 更新数据库库存信息(乐观锁)
     * @param stock
     * @return
     */
    int updateStockByOptimistic(Stock stock);
}
