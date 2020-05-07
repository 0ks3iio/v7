package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.Order;

public interface OrderRemoteService extends BaseRemoteService<Order,String>{

	/**
	 * 根据serverID和用户/单位id数组，检索订购的记录
	 * @param serverId
	 * @param customerIds
	 * @return
	 */
	public String findByServer(Integer serverId, String[] customerIds);
	
	/**
	 * 取有效时间内的个人或单位订购商品列表
	 * @param customerId
	 * @param customerType
	 * @param formattedTime
	 * @param formattedTime2
	 * @return
	 */
	public String findByCustomerId(String customerId, Integer customerType, String formattedTime,
			String formattedTime2);

}
