package net.zdsoft.base.service;

public interface BaseEntityService {
	/**
	 * 处理模型增删改
	 * 业务id没有匹配--增
	 * 业务id匹配成功--改
	 * isDeleted--删
	 * @param ticketKey
	 * @param json
	 * @param type
	 * @param dealType 0:增删改  1：增  2：改删
	 */
	public void dealModelSave(String ticketKey,String json,String type,int dealType);
}
