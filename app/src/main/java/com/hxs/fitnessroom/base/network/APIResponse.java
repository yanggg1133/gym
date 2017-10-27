package com.hxs.fitnessroom.base.network;
/**
 * 请求响应数据基类
 *
 */
public class APIResponse<T>
{

	private static final String STATE_SUCCESS = "200"; //请求成功
	private static final String STATE_TOKEN_EXPIRED = "401"; //登录过期

    public static final String error_fail = "600";// 订单失败，也可以是出货不成功
    public static final String error_order_is_not_settled = "607";// 你还有一笔未结算订单
    public static final String error_not_deposit = "609";// 你还没交押金！
    public static final String error_insufficient_balance = "610";// 余额不足请充值！


	public String msg;
	public String code;
	public T data;

    public boolean isSuccess()
    {
        return STATE_SUCCESS.equals(code);
    }

}
