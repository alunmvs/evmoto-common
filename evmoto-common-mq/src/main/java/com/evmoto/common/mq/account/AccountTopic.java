package com.evmoto.common.mq.account;

/**
 * @author yelingjun
 * @ClassName: AccountTopic
 * @projectName evmoto-common
 * @description:
 * @date: 2026/3/24 21:54
 */
public interface AccountTopic {

    /**
     * 主题
     */
    String ACCOUNT_TOPIC = "account";

    /**
     * 余额变动
     */
    String ACCOUNT_BALANCE_CHANGE = "balanceChange";

    /**
     * 订单支付成功
     */
    String ACCOUNT_ORDER_PAY_SUCCESS = "orderPaySuccess";
}
