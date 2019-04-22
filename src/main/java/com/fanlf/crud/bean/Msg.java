package com.fanlf.crud.bean;

import java.util.HashMap;
import java.util.Map;

public class Msg {
	// 状态码 100--成功，200--失败
	private int code;
	// 提示信息
	private String msg;
	// 用户要返回给浏览器的数据
	private Map<String, Object> extend = new HashMap<String, Object>();

	//定义返回成功的静态方法
	public static Msg success(){
		Msg result = new Msg();
		result.setCode(100);
		result.setMsg("处理成功");
		return result;
	}
	
	//定义返回失败的静态方法
	public static Msg failed(){
		Msg result = new Msg();
		result.setCode(200);
		result.setMsg("处理失败");
		return result;
	}
	
	//定义一个快捷添加查询返回信息的方法
	public Msg add(String key, Object value){
		this.getExtend().put(key, value);
		return this;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getExtend() {
		return extend;
	}

	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}

}
