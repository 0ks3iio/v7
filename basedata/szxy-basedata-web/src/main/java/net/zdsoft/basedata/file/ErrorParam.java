/*
* Project: v7
* Author : shenke
* @(#) ErrorParam.java Created on 2016-8-18
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.file;

/**
 * @description: TODO
 * @author: shenke
 * @version: 1.0
 * @date: 2016-8-18上午10:00:37
 */
public class ErrorParam {

	private int index;
	private String[] rowDatas;
	private String errorMsg;
	
	
	
	public ErrorParam() {
		super();
	}
	public ErrorParam(int index, String[] rowDatas, String errorMsg) {
		super();
		this.index = index;
		this.rowDatas = rowDatas;
		this.errorMsg = errorMsg;
	}
	public int getIndex() {
		return index;
	}
	public ErrorParam setIndex(int index) {
		this.index = index;
		return this;
	}
	public String[] getRowDatas() {
		return rowDatas;
	}
	public ErrorParam setRowDatas(String[] rowDatas) {
		this.rowDatas = rowDatas;
		return this;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public ErrorParam setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		return this;
	}
	
	
}
