package net.zdsoft.api.base.service;

public interface ApiBaseEntityService {
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
	String up(String ticketKey, String jsonStr, String type, int dealType);

}
