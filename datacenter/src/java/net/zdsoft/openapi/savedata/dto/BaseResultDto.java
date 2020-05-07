package net.zdsoft.openapi.savedata.dto;


import com.alibaba.fastjson.JSON;

public class BaseResultDto {
	    private static final long serialVersionUID = 1L;
	    private boolean success;
		private String code;  // 1--成功  -1 失败  -2 有失败的
		private String msg;
		private String detailError;
		private String errorIndex;
		
		public boolean isSuccess() {
			return success;
		}

		public BaseResultDto setSuccess(boolean success) {
			this.success = success;
			return this;
		}

		public String getCode() {
			return code;
		}

		public BaseResultDto setCode(String code) {
			this.code = code;
			return this;
		}

		public String getMsg() {
			return msg;
		}

		public BaseResultDto setMsg(String msg) {
			this.msg = msg;
			return this;
		}

		public String getDetailError() {
			return detailError;
		}

		public BaseResultDto setDetailError(String detailError) {
			this.detailError = detailError;
			return this;
		}

		public String toJSONString(){
			return JSON.toJSONString(this);
		}

		public String getErrorIndex() {
			return errorIndex;
		}

		public void setErrorIndex(String errorIndex) {
			this.errorIndex = errorIndex;
		}
}
