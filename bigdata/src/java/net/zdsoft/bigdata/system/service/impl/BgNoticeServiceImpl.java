package net.zdsoft.bigdata.system.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.system.dao.BgNoticeDao;
import net.zdsoft.bigdata.system.entity.BgNotice;
import net.zdsoft.bigdata.system.service.BgNoticeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bgNoticeService")
public class BgNoticeServiceImpl extends BaseServiceImpl<BgNotice, String>
		implements BgNoticeService {

	@Autowired
	private BgNoticeDao bgNoticeDao;

	@Override
	protected BaseJpaRepositoryDao<BgNotice, String> getJpaDao() {
		return bgNoticeDao;
	}

	@Override
	protected Class<BgNotice> getEntityClass() {
		return BgNotice.class;
	}

	@Override
	public List<BgNotice> findNoticeListByStatus(Integer status, Pagination page) {
		if (page == null)
			return bgNoticeDao.findNoticeListByStatus(status);
		else
			return bgNoticeDao.findNoticeListByStatus(status,
					Pagination.toPageable(page));
	}

	@Override
	public List<BgNotice> findNoticeListByTitle(String title) {
		return bgNoticeDao.findNoticeListByTitle(title);
	}

	@Override
	public List<BgNotice> findNoticeList(Pagination page) {
		if (page == null)
			return bgNoticeDao.findNoticeList();
		else
			return bgNoticeDao.findNoticeList(Pagination.toPageable(page));
	}

}
