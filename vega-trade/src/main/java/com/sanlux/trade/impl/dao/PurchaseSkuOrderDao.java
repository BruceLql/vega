package com.sanlux.trade.impl.dao;

import com.google.common.collect.ImmutableMap;
import com.sanlux.trade.enums.PurchaseSkuOrderStatus;
import com.sanlux.trade.model.PurchaseSkuOrder;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Code generated by terminus code gen
 * Desc: Dao类
 * Date: 2016-08-09
 */
@Repository
public class PurchaseSkuOrderDao extends MyBatisDao<PurchaseSkuOrder> {

    public PurchaseSkuOrder findByPurchaseOrderIdAndSkuId(Long purchaseId,Long skuId){
        return getSqlSession().selectOne(sqlId("findByPurchaseOrderIdAndSkuId"), ImmutableMap.of("purchaseId", purchaseId, "skuId", skuId));
    }


    public List<PurchaseSkuOrder> findByPurchaseOrderIdAndSkuIds(Long purchaseId,List<Long> skuIds){
        return getSqlSession().selectList(sqlId("findByPurchaseOrderIdAndSkuIds"), ImmutableMap.of("purchaseId", purchaseId, "skuIds", skuIds));
    }


    /**
     * 修改数量
     */
    public Boolean updateQuantity(Long id,Integer quantity) {
        return getSqlSession().update(sqlId("updateQuantity"), ImmutableMap.of("id",id,"quantity",quantity))>0;
    }


    public Boolean deleteByPurchaseOrderIdAndSkuIds(Long purchaseId,List<Long> skuIds){
        return getSqlSession().delete(sqlId("deleteByPurchaseOrderIdAndSkuIds"),ImmutableMap.of("purchaseId",purchaseId,"skuIds",skuIds))>0;
    }


    public List<PurchaseSkuOrder> finByPurchaseOrderId(Long purchaseId){
        return getSqlSession().selectList(sqlId("finByPurchaseOrderId"),purchaseId);
    }

    public List<PurchaseSkuOrder> finByPurchaseOrderIdAndStatus(Long purchaseId,Integer status){
        return getSqlSession().selectList(sqlId("finByPurchaseOrderIdAndStatus"),ImmutableMap.of("purchaseId",purchaseId,"status",status));
    }

    public List<PurchaseSkuOrder> finByPurchaseOrderIdAndStatusAndShopId(Long purchaseId,Integer status,Long shopId){
        return getSqlSession().selectList(sqlId("finByPurchaseOrderIdAndStatusAndShopId"),ImmutableMap.of("purchaseId",purchaseId,"status",status,"shopId",shopId));
    }

    public List<Long> findShopIdsByPurchaseOrderId(Long purchaseId){
        return getSqlSession().selectList(sqlId("findShopIdsByByPurchaseOrderId"),purchaseId);
    }

    public Boolean batchUpdateStatus(List<Long> ids,Integer status){
        return getSqlSession().update(sqlId("batchUpdateStatus"),ImmutableMap.of("ids",ids,"status",status))>0;
    }

    public Boolean deleteByPurchaseOrderId(Long purchaseOrderId){
        return getSqlSession().delete(sqlId("deleteByPurchaseOrderId"),purchaseOrderId)>0;
    }

    public Boolean updateQuantityAndStatusByPurchaseOrderId(Integer quantity,Integer status,Long purchaseOrderId){
        return getSqlSession().update(sqlId("updateQuantityAndStatusByPurchaseOrderId"),ImmutableMap.of("purchaseOrderId",
                purchaseOrderId,"status", status,"quantity",quantity))>0;
    }



}
