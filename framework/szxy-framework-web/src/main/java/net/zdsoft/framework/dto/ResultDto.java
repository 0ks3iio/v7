package net.zdsoft.framework.dto;

import com.alibaba.fastjson.JSON;

import net.zdsoft.basedata.dto.BaseDto;

public class ResultDto extends BaseDto {
    private static final long serialVersionUID = 1L;
    private boolean success;
	private String code;
	private String msg;
	private String detailError;
	//仅处理单个业务数据 这里请考虑扩展
	private String businessValue;
	
	public boolean isSuccess() {
		return success;
	}

	public ResultDto setSuccess(boolean success) {
		this.success = success;
		return this;
	}

	public String getCode() {
		return code;
	}

	public ResultDto setCode(String code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public ResultDto setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public String getDetailError() {
		return detailError;
	}

	public ResultDto setDetailError(String detailError) {
		this.detailError = detailError;
		return this;
	}

	public String getBusinessValue() {
		return businessValue;
	}

	public ResultDto setBusinessValue(String businessValue) {
		this.businessValue = businessValue;
		return this;
	}
	
	public String toJSONString(){
		return JSON.toJSONString(this);
	}
	
}
