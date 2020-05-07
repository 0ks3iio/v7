package net.zdsoft.api.base.dto;

import java.io.Serializable;


public class ResultBean<T> implements Serializable{
	private static final long serialVersionUID =1L;

	public static final int SUCCESS = 1;//成功

	public static final int CHECK_FAIL = -1;//数据校验异常

	public static final int NO_PERMISSION = -2;//无权限

	public static final int UNKNOWN_EXCEPTION = -99;//未定义异常


	private String msg ="";

	private int code = SUCCESS;

	private T data;

	public int getCode() {
		return code;
	}

	public ResultBean<T> setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public ResultBean<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public T getData() {
		return data;
	}

	public ResultBean<T> setData(T data) {
		this.data = data;
		return this;
	}

	public ResultBean(){
		super();
	}

	public ResultBean(T data){
		super();
		this.data = data;
	}

	public ResultBean(Throwable e){
		super();
		this.msg = e.getMessage();
		this.code = UNKNOWN_EXCEPTION;
	}
}
