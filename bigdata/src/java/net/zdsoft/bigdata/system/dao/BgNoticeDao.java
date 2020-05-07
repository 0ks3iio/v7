package net.zdsoft.bigdata.system.dao;

import java.util.List;

import net.zdsoft.bigdata.system.entity.BgNotice;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface BgNoticeDao extends BaseJpaRepositoryDao<BgNotice, String> {

	@Query("From BgNotice where status =?1 order by modifyTime desc ")
	public List<BgNotice> findNoticeListByStatus(Integer status);
	
	@Query("From BgNotice where status =?1 order by modifyTime desc ")
	public List<BgNotice> findNoticeListByStatus(Integer status,Pageable page);
	
	@Query("From BgNotice order by modifyTime desc ")
	public List<BgNotice> findNoticeList();
	
	@Query("From BgNotice order by modifyTime desc ")
	public List<BgNotice> findNoticeList(Pageable page);
	
	@Query("From BgNotice where title =?1 ")
	public List<BgNotice> findNoticeListByTitle(String title);
	
}
