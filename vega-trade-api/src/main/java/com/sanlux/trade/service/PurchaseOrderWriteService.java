package com.sanlux.trade.service;

import com.sanlux.trade.model.PurchaseOrder;
import io.terminus.common.model.Response;

/**
 * Code generated by terminus code gen
 * Desc: 写服务
 * Date: 2016-08-09
 */

public interface PurchaseOrderWriteService {

    /**
     * 创建PurchaseOrder
     * @param purchaseOrder 采购单
     * @return 主键id
     */
    Response<Long> createPurchaseOrder(PurchaseOrder purchaseOrder);

    /**
     * 更新PurchaseOrder
     * @param purchaseOrder 采购单
     * @return 是否成功
     */
    Response<Boolean> updatePurchaseOrder(PurchaseOrder purchaseOrder);

    /**
     * 根据主键id删除PurchaseOrder 同时删除关联的商品
     * @param purchaseOrderId 采购单id
     * @return 是否成功
     */
    Response<Boolean> deletePurchaseOrderById(Long purchaseOrderId);
}