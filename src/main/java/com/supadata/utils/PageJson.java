package com.supadata.utils;
/**
 * 
 * @author Administrator
 *
 */
public class PageJson {

	private Integer code;//返回的状态1---失败（一般前端传值错误），0---成功，2---服务器错误，3---身份验证信息失效,4---对该接口没有权限
	
	private Long count;
	
	private String msg;//返回说明
	
	private Object data;//返回的数据

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public PageJson(Integer code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public PageJson(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
}
