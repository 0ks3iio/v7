package net.zdsoft.gkelective.data.dto;
/**
 * @author yuzy
 * @version 创建时间：2017-6-23 上午11:19:26
 * 
 */
public class GkStuExceptionDto {

	private String stuName;
	private String stuCode;
	private boolean isSuccess = true; 
	private String msg;
	
	public String getStuCode() {
		return stuCode;
	}
	public void setStuCode(String stuCode) {
		this.stuCode = stuCode;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
