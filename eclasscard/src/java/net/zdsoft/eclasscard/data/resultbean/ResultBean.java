package net.zdsoft.eclasscard.data.resultbean;

import java.io.Serializable;

public class ResultBean<T> implements Serializable{

	private static final long serialVersionUID =1L;

	public static final boolean SUCCESS = true;

	public static final boolean FAIL = false;
	private String msg ="";

	private boolean success = SUCCESS;

	private T data;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
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
		this.success = FAIL;
	}
}
