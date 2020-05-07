package net.zdsoft.syncdata.dto;

/**
 * 
 * @author weixh
 * @since 2017年11月30日 下午3:57:53
 */
public class ResultDto {
	private String status;
	private String msg;

	public ResultDto() {}
	
	public ResultDto(String status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
