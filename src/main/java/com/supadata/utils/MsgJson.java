package com.supadata.utils;

import com.github.pagehelper.PageInfo;
import com.supadata.pojo.StudentCard;

public class MsgJson {

	private static final Integer CODE_SUCCESS = 0;

	private static final Integer CODE_FAIL = 1;

	private Integer code;//返回的状态0---成功，1---失败（一般前端传值错误），2---服务器错误，3---身份验证信息失效,4---对该接口没有权限

	private String msg;//返回说明

	private Object data;//返回的数据

	private Long count;

	public static MsgJson fail(String msg) {
		return new MsgJson(CODE_FAIL,msg);
	}

	public static MsgJson success(String msg) {
		return new MsgJson(CODE_SUCCESS,msg);
	}

	public static MsgJson success(Object data, String msg) {
		return new MsgJson(CODE_SUCCESS,msg,data);
	}
	public void setCode(Integer status) {
		this.code = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String message) {
		this.msg = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public MsgJson() {
	}

	public MsgJson(Integer status, String message, Object data) {
		super();
		this.code = status;
		this.msg = message;
		this.data = data;
	}

	public MsgJson(Integer status, String message) {
		super();
		this.code = status;
		this.msg = message;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
