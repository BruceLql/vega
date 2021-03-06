package com.sanlux.trade.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.terminus.common.utils.JsonMapper;
import io.terminus.parana.common.constants.JacksonType;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * Code generated by terminus code gen
 * Desc: 积分商品订单Model类
 * Date: 2016-11-07
 */
@Data
public class IntegrationOrder implements Serializable {


    private static final long serialVersionUID = 1007117338640619044L;

    private static final ObjectMapper objectMapper = JsonMapper.nonEmptyMapper().getMapper();


    private Long id;
    
    /**
     * 购买用户ID
     */
    private Long buyerId;
    
    /**
     * 购买用户名称
     */
    private String buyerName;
    
    /**
     * 电话号码
     */
    private String buyerPhone;
    
    /**
     * 积分商品ID
     */
    private Long itemId;
    
    /**
     * 积分商品名称
     */
    private String itemName;
    
    /**
     * 商品图片
     */
    private String itemImage;
    
    /**
     * 积分单价
     */
    private Integer integrationPrice;
    
    /**
     * 花费积分
     */
    private Integer integrationFee;
    
    /**
     * 数量
     */
    private Integer quantity;
    
    /**
     * 订单状态, 2:已完成, 1:待发货
     */
    private Integer status;
    
    /**
     * 收货地址信息
     */
    private String addressInfoJson;
    
    /**
     * 快递公司
     */
    private String deliveryCompany;
    
    /**
     * 快递单号
     */
    private String deliveryNo;

    /**
     * 扩展信息字段,不存数据库
     */
    private Map<String, String> extra;
    
    /**
     * 扩展信息字段,存数据库
     */
    private String extraJson;
    
    /**
     * 创建时间
     */
    private Date createdAt;
    
    /**
     * 更新时间
     */
    private Date updatedAt;

    public void setExtraJson(String extraJson) throws Exception{
        this.extraJson = extraJson;
        if(Strings.isNullOrEmpty(extraJson)){
            this.extra= Collections.emptyMap();
        } else{
            this.extra = objectMapper.readValue(extraJson, JacksonType.MAP_OF_STRING);
        }
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
        if(extra ==null ||extra.isEmpty()){
            this.extraJson = null;
        }else{
            try {
                this.extraJson = objectMapper.writeValueAsString(extra);
            } catch (Exception e) {
                //ignore this exception
            }
        }
    }
}
