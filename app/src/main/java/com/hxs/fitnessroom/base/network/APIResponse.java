package com.hxs.fitnessroom.base.network;
/**
 * 请求响应数据基类
 *
 */
public class APIResponse<T>
{

	private static final String STATE_SUCCESS = "200"; //请求成功
	private static final String STATE_TOKEN_EXPIRED = "401"; //登录过期

	public String msg;
	public String code;
	public T data;

    public boolean isSuccess()
    {
        return STATE_SUCCESS.equals(code);
    }

}
