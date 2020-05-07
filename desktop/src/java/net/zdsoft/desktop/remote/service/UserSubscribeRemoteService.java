package net.zdsoft.desktop.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.desktop.entity.UserSubscribe;

/**
 * @author yangsj  2017-6-16上午10:41:31
 */
public interface UserSubscribeRemoteService extends BaseRemoteService<UserSubscribe, String> {

	//获得排行榜的应用列表
	String findRankingList(Integer  remoteParam);
	
	//获得应用的人气
	String findAppPopularity();
}
