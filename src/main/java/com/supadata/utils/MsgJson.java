package com.supadata.utils;

public class MsgJson {

	private Integer code;//返回的状态0---成功，1---失败（一般前端传值错误），2---服务器错误，3---身份验证信息失效,4---对该接口没有权限

	private String msg;//返回说明

	private Object data;//返回的数据

	private Long count;

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
