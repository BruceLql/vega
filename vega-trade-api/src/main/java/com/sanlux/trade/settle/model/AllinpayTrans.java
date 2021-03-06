package com.sanlux.trade.settle.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Code generated by terminus code gen
 * Desc: Model类
 * Date: 2016-10-20
 */
@Data
public class AllinpayTrans implements Serializable {


    private static final long serialVersionUID = -8741801840999017948L;
    private Long id;
    
    /**
     * 交易类型(ZF:支付成功、TH:退款成功,资金已经退款至持卡人账户、CX:冲销成功,未能成功退款,资金已经退回给商户)
     */
    private String transCodeMsg;
    
    /**
     * 结算日期
     */
    private String transDate;
    
    /**
     * 商户号
     */
    private String sellerAccount;
    
    /**
     * 交易时间
     */
    private Date tradeAt;
    
    /**
     * 商户订单号
     */
    private String transOutOrderNo;
    
    /**
     * 通联交易流水
     */
    private String tradeNo;
    
    /**
     * 交易总金额
     */
    private String totalFee;
    
    /**
     * 交易服务费
     */
    private String serviceFee;
    
    /**
     * 交易服务费率
     */
    private String serviceFeeRatio;
    
    /**
     * 清算金额(分)
     */
    private String settlementFee;
    
    /**
     * 货币代码(156:人民币)
     */
    private String currency;
    
    /**
     * 商户原始订单金额(分)
     */
    private String orderOriginFee;
    
    /**
     * 备注信息
     */
    private String memo;
    
    /**
     * 创建时间
     */
    private Date createdAt;
    
    /**
     * 修改时间
     */
    private Date updatedAt;
}
