package com.lvshou.fitnessroom.base.network;
/**
 * 请求响应数据基类
 *
 */
public class APIResponse<T>
{
	public static final String RET_SUCCESS = "success"; //请求成功
	public static final int  STATE_SUCCESS = 1; //请求成功
	public static final int  STATE_SUCCESS2 = 0; //请求成功

	public static final String STATE_SUCCESS_STR = "0"; //请求成功
	public static final String STATE_SUCCESS2_STR = "1"; //请求成功

	private String msg;
	public T data;

}
