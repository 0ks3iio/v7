package net.zdsoft.desktop.dto;

import java.util.List;

public class FlowchartDto {
	/**
	 * 序号
	 */
	private String serialNumber;
	/**
	 * 模块名称
	 */
	private String title;
	/**
	 * 提示
	 */
	private String prompt;
	/**
	 * 拼接全路径
	 */
	private String url;
	/**
	 * 模块权限
	 */
	private boolean authority;
	/**
	 * 样式
	 */
	private String style;
	
	private List<FlowchartDto> dtos;
	
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isAuthority() {
		return authority;
	}
	public void setAuthority(boolean authority) {
		this.authority = authority;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public List<FlowchartDto> getDtos() {
		return dtos;
	}
	public void setDtos(List<FlowchartDto> dtos) {
		this.dtos = dtos;
	}
}
