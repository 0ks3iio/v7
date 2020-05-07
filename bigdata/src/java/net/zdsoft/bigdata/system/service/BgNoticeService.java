package net.zdsoft.bigdata.system.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.system.entity.BgNotice;
import net.zdsoft.framework.entity.Pagination;

public interface BgNoticeService extends BaseService<BgNotice, String> {

	/**
	 * 根据状态获取公告list
	 * 
	 * @param status
	 * @param page
	 * @return
	 */
	public List<BgNotice> findNoticeListByStatus(Integer status, Pagination page);

	public List<BgNotice> findNoticeList(Pagination page);

	/**
	 * 根据标题获取公告list
	 * 
	 * @param title
	 * @return
	 */
	public List<BgNotice> findNoticeListByTitle(String title);
}
