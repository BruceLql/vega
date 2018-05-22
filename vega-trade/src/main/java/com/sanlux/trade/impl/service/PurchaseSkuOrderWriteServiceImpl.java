package com.sanlux.trade.impl.service;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sanlux.trade.enums.PurchaseSkuOrderStatus;
import com.sanlux.trade.impl.dao.PurchaseOrderDao;
import com.sanlux.trade.impl.dao.PurchaseSkuOrderDao;
import com.sanlux.trade.impl.manager.PurchaseOrderManager;
import com.sanlux.trade.model.PurchaseOrder;
import com.sanlux.trade.model.PurchaseSkuOrder;
import com.sanlux.trade.service.PurchaseSkuOrderWriteService;
import io.terminus.boot.rpc.common.annotation.RpcProvider;
import io.terminus.common.model.Response;
import io.terminus.common.utils.Arguments;
import io.terminus.parana.item.model.Sku;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

/**
 * Code generated by terminus code gen
 * Desc: 写服务实现类
 * Date: 2016-08-09
 */
@Slf4j
@Service
@RpcProvider
public class PurchaseSkuOrderWriteServiceImpl implements PurchaseSkuOrderWriteService {

    @Autowired
    private PurchaseSkuOrderDao purchaseSkuOrderDao;
    @Autowired
    private PurchaseOrderDao purchaseOrderDao;
    @Autowired
    private PurchaseOrderManager purchaseOrderManager;


    @Override
    public Response<Long> createPurchaseSkuOrder(PurchaseSkuOrder purchaseSkuOrder) {
        try {
            purchaseSkuOrderDao.create(purchaseSkuOrder);
            return Response.ok(purchaseSkuOrder.getId());
        } catch (Exception e) {
            log.error("create purchaseSkuOrder failed, purchaseSkuOrder:{}, cause:{}", purchaseSkuOrder,
                    Throwables.getStackTraceAsString(e));
            return Response.fail("purchase.sku.order.create.fail");
        }
    }

    @Override
    public Response<Boolean> updatePurchaseSkuOrder(PurchaseSkuOrder purchaseSkuOrder) {
        try {
            return Response.ok(purchaseSkuOrderDao.update(purchaseSkuOrder));
        } catch (Exception e) {
            log.error("update purchaseSkuOrder failed, purchaseSkuOrder:{}, cause:{}", purchaseSkuOrder,
                    Throwables.getStackTraceAsString(e));
            return Response.fail("purchase.sku.order.update.fail");
        }
    }

    @Override
    public Response<Boolean> deletePurchaseSkuOrderById(Long purchaseSkuOrderId) {
        try {
            return Response.ok(purchaseSkuOrderDao.delete(purchaseSkuOrderId));
        } catch (Exception e) {
            log.error("delete purchaseSkuOrder failed, purchaseSkuOrderId:{}, cause:{}", purchaseSkuOrderId,
                    Throwables.getStackTraceAsString(e));
            return Response.fail("purchase.sku.order.delete.fail");
        }
    }

    @Override
    public Response<Boolean> deleteByPurchaseOrderId(Long purchaseOrderId) {
        try {
            return Response.ok(purchaseSkuOrderDao.deleteByPurchaseOrderId(purchaseOrderId));
        } catch (Exception e) {
            log.error("delete purchaseSkuOrder failed, purchaseOrderId:{}, cause:{}", purchaseOrderId,
                    Throwables.getStackTraceAsString(e));
            return Response.fail("purchase.sku.order.delete.fail");
        }
    }

    @Override
    public Response<Integer> changePurchaseSkuOrder(Sku sku, Integer quantity, Long purchaseId, Long userId,
                                                    String userName) {
        try {
            PurchaseOrder purchaseOrder = purchaseOrderDao.findById(purchaseId);
            if(Arguments.isNull(purchaseOrder)){
                log.error("change purchase sku order where sku:{} quantity:{} purchase order id:{} user id:{} " +
                        "user name:{} fail,error:{}",sku,quantity,purchaseId,userId,userName,"purchase.order.not.exist");

                return Response.fail("purchase.order.not.exist");
            }

            return Response.ok(doChangePurchaseSkuOrder(sku,quantity,purchaseOrder,userId,userName));

        }catch (IllegalStateException e){
            log.error("change purchase sku order where sku:{} quantity:{} purchase order id:{} user id:{} user name:{} " +
                    "fail,error:{}",sku,quantity,purchaseId,userId,userName,e.getMessage());
            return Response.fail(e.getMessage());
        }catch (Exception e){
            log.error("change purchase sku order where sku:{} quantity:{} purchase order id:{} user id:{} user name:{} " +
                    "fail,cause:{}",sku,quantity,purchaseId,userId,userName,Throwables.getStackTraceAsString(e));
            return Response.fail("purchase.sku.order.update.fail");
        }
    }



    @Override
    public Response<Long> changeTempPurchaseSkuOrder(Sku sku, Integer quantity,Long userId,
                                                    String userName) {
        try {
            PurchaseOrder purchaseOrder = purchaseOrderDao.findByUserForTemp(userId);

            return Response.ok(purchaseOrderManager.changeTempPurchaseSkuOrder(sku,quantity,purchaseOrder,userId,userName));

        }catch (IllegalStateException e){
            log.error("change temp purchase sku order where sku:{} quantity:{}  user id:{} user name:{} " +
                    "fail,error:{}",sku,quantity,userId,userName,e.getMessage());
            return Response.fail(e.getMessage());
        }catch (Exception e){
            log.error("change purchase sku order where sku:{} quantity:{} user id:{} user name:{} " +
                    "fail,cause:{}",sku,quantity,userId,userName,Throwables.getStackTraceAsString(e));
            return Response.fail("purchase.sku.order.update.fail");
        }
    }




    @Override
    public Response<Boolean> batchChangePurchaseSkuOrder(Map<Sku,Integer> skuQuantityMap, Long purchaseId, Long userId,
                                                    String userName) {
        try {
            PurchaseOrder purchaseOrder = purchaseOrderDao.findById(purchaseId);
            if(Arguments.isNull(purchaseOrder)){
                log.error("batch change purchase sku order where purchase order id: {} fail ,error:{}",purchaseId,"purchase.order.not.exist");

                return Response.fail("purchase.order.not.exist");
            }

            List<Long> skuIds = getSkuIds(skuQuantityMap);

            List<PurchaseSkuOrder> exists = purchaseSkuOrderDao.findByPurchaseOrderIdAndSkuIds(purchaseOrder.getId(), skuIds);

            Map<Long,PurchaseSkuOrder> skuIdAndPurchaseSkuOrderMap = transToSkuIdAndPurchaseSkuOrderMap(exists);

            List<PurchaseSkuOrder> purchaseSkuOrderUpdates = Lists.newArrayList();
            List<PurchaseSkuOrder> purchaseSkuOrderCreates = Lists.newArrayList();


            for (Sku sku : skuQuantityMap.keySet()){

                Integer quantity = skuQuantityMap.get(sku);
                //已存在
                if(Arguments.notNull(skuIdAndPurchaseSkuOrderMap.get(sku.getId()))){

                    PurchaseSkuOrder exist = skuIdAndPurchaseSkuOrderMap.get(sku.getId());
                    PurchaseSkuOrder update = new PurchaseSkuOrder();
                    update.setId(exist.getId());
                    update.setQuantity(exist.getQuantity()+quantity);
                    purchaseSkuOrderUpdates.add(update);

                }else {
                    PurchaseSkuOrder purchaseSkuOrder = new PurchaseSkuOrder();
                    purchaseSkuOrder.setPurchaseId(purchaseOrder.getId());
                    purchaseSkuOrder.setBuyerId(userId);
                    purchaseSkuOrder.setBuyerName(userName);
                    purchaseSkuOrder.setCreatedAt(new Date());
                    purchaseSkuOrder.setStatus(PurchaseSkuOrderStatus.NO_CHOOSE.value());
                    purchaseSkuOrder.setQuantity(quantity);
                    purchaseSkuOrder.setShopId(sku.getShopId());
                    purchaseSkuOrder.setShopName("");
                    purchaseSkuOrder.setSkuId(sku.getId());
                    purchaseSkuOrder.setUpdatedAt(new Date());
                    purchaseSkuOrder.setCreatedAt(new Date());
                    purchaseSkuOrderCreates.add(purchaseSkuOrder);
                }

            }


            purchaseOrderManager.batchchangePurchaseSkuOrer(purchaseSkuOrderUpdates, purchaseSkuOrderCreates);

            return Response.ok(Boolean.TRUE);

        }catch (Exception e){
            log.error("bat ch change purchase sku order where skuAndquantityMap:{} purchase order id:{} user id:{} user name:{} " +
                    "fail,cause:{}",skuQuantityMap,purchaseId,userId,userName,Throwables.getStackTraceAsString(e));
            return Response.fail("purchase.sku.order.update.fail");
        }
    }

    private Map<Long, PurchaseSkuOrder> transToSkuIdAndPurchaseSkuOrderMap(List<PurchaseSkuOrder> exists) {

        if(CollectionUtils.isEmpty(exists)){
            exists = Lists.newArrayList();
        }
        Map<Long, PurchaseSkuOrder> map = Maps.newHashMap();
        for (PurchaseSkuOrder skuOrder : exists){
            map.put(skuOrder.getSkuId(),skuOrder);
        }

        return map;
    }

    @Override
    public Response<Boolean> batchDeleteByPurchaseOrderIdAndySkuId(List<Long> skuIds, Long purchaseOrderId) {
        try {

            PurchaseOrder purchaseOrder = purchaseOrderDao.findById(purchaseOrderId);
            if(Arguments.isNull(purchaseOrder)){
                return Response.fail("purchase.order.not.exist");
            }
            purchaseOrderManager.batchaDeletePurchaseSkuOrder(skuIds, purchaseOrder);

            return Response.ok(Boolean.TRUE);

        }catch (Exception e){
            log.error("batch delete purchase sku order by purchase order id:{} sku ids:{} fail,cause:{}",purchaseOrderId,
                    skuIds,Throwables.getStackTraceAsString(e));
            return Response.fail("delete.purchase.sku.order.fail");
        }
    }


    @Override
    public Response<Boolean> batchUpdateStatus(List<Long> skuIds, Integer status) {
        try {
            if (CollectionUtils.isEmpty(skuIds)){
                return Response.ok(Boolean.TRUE);
            }

            purchaseSkuOrderDao.batchUpdateStatus(skuIds, status);

            return Response.ok(Boolean.TRUE);

        }catch (Exception e){
            log.error("batch update purchase sku order status by ids:{} status:{} fail,cause:{}",skuIds,status,
                    Throwables.getStackTraceAsString(e));
            return Response.fail("purchase.sku.order.update.fail");
        }
    }

    @Override
    public Response<Boolean> resetQuantityByPurchaseOrderId(Long purchaseOrderId) {
        try {
            purchaseSkuOrderDao.updateQuantityAndStatusByPurchaseOrderId(0,PurchaseSkuOrderStatus.NO_CHOOSE.value(),purchaseOrderId);
            return Response.ok(Boolean.TRUE);
        }catch (Exception e){
            log.error("batch update purchase sku order status to :{} quantity:{} by purchaseOrderId:{} fail,cause:{}",
                    PurchaseSkuOrderStatus.NO_CHOOSE.value(),0,purchaseOrderId,
                    Throwables.getStackTraceAsString(e));
            return Response.fail("purchase.sku.order.update.fail");
        }
    }


    /**
     * 更改购物车数量
     * @param sku         商品id
     * @param quantity    商品数量
     * @param userId      买家id
     * @return 操作后购物车内数量
     */
    private Integer doChangePurchaseSkuOrder(Sku sku, Integer quantity, PurchaseOrder purchaseOrder, Long userId,
                                             String userName) {


        PurchaseSkuOrder exist = purchaseSkuOrderDao.findByPurchaseOrderIdAndSkuId(purchaseOrder.getId(), sku.getId());
        //已存在
        if(Arguments.notNull(exist)){
            PurchaseSkuOrder update = new PurchaseSkuOrder();
            update.setId(exist.getId());
            //如果数量减少为0则标记未选中
            if(exist.getQuantity()+quantity==0){
                update.setQuantity(0);
                update.setStatus(PurchaseSkuOrderStatus.NO_CHOOSE.value());//未选中
                purchaseSkuOrderDao.update(update);
                //purchaseOrderManager.deletePurchaseSkuOrder(purchaseOrder,exist);
            }else {
                //更新数量
                update.setQuantity(exist.getQuantity()+quantity);
                update.setStatus(PurchaseSkuOrderStatus.CHOOSED.value());//未选中
                purchaseSkuOrderDao.update(update);            }
        }else {
            PurchaseSkuOrder purchaseSkuOrder = new PurchaseSkuOrder();
            purchaseSkuOrder.setPurchaseId(purchaseOrder.getId());
            purchaseSkuOrder.setBuyerId(userId);
            purchaseSkuOrder.setBuyerName(userName);
            purchaseSkuOrder.setCreatedAt(new Date());
            if(quantity.equals(0)){
                purchaseSkuOrder.setStatus(PurchaseSkuOrderStatus.NO_CHOOSE.value());
            }else {
                purchaseSkuOrder.setStatus(PurchaseSkuOrderStatus.CHOOSED.value());
            }
            purchaseSkuOrder.setQuantity(quantity);
            purchaseSkuOrder.setShopId(sku.getShopId());
            purchaseSkuOrder.setShopName("");
            purchaseSkuOrder.setSkuId(sku.getId());
            purchaseSkuOrder.setUpdatedAt(new Date());
            purchaseSkuOrder.setCreatedAt(new Date());
            purchaseOrderManager.createPurchaseSkuOrer(purchaseSkuOrder, purchaseOrder);
        }

        return purchaseOrderDao.findById(purchaseOrder.getId()).getSkuQuantity();

    }


    private List<Long> getSkuIds(Map<Sku,Integer> skuQuantityMap){

        List<Long> skuIds = Lists.newArrayList();
        for (Sku sku: skuQuantityMap.keySet()){
            skuIds.add(sku.getId());
        }
        return skuIds;
    }


}
