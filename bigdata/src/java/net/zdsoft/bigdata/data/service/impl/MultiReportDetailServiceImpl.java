package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.MultiReportDetailDao;
import net.zdsoft.bigdata.data.entity.MultiReportDetail;
import net.zdsoft.bigdata.data.service.MultiReportDetailService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("multiReportDetailService")
public class MultiReportDetailServiceImpl extends
		BaseServiceImpl<MultiReportDetail, String> implements
		MultiReportDetailService {

	@Autowired
	private MultiReportDetailDao multiReportDetailDao;

	@Override
	protected BaseJpaRepositoryDao<MultiReportDetail, String> getJpaDao() {
		return multiReportDetailDao;
	}

	@Override
	protected Class<MultiReportDetail> getEntityClass() {
		return MultiReportDetail.class;
	}

	@Override
	public List<MultiReportDetail> findMultiReportDetailsByReportId(
			String reportId) {
		return multiReportDetailDao.findMultiReportDetailsByReportId(reportId);
	}

	@Override
	public void deleteMultiReportDetailsByReportId(String reportId) {
		multiReportDetailDao.deleteMultiReportDetailsByReportId(reportId);
	}

	@Override
	public void updateMultiReportDetail(MultiReportDetail multiReportDetail) {

		if (StringUtils.isBlank(multiReportDetail.getId())) {
			multiReportDetail.setId(UuidUtils.generateUuid());
			multiReportDetail.setBusinessId(Constant.GUID_ZERO);
			save(multiReportDetail);
		} else {
			if ("richText".equals(multiReportDetail.getBusinessType())) {
				update(multiReportDetail, multiReportDetail.getId(),
						new String[] { "orderId", "content" });
			} else {
				update(multiReportDetail, multiReportDetail.getId(),
						new String[] { "orderId" });
			}
		}
	}

	@Override
	public Integer getMaxOrderIdByReportId(String reportId) {
		return multiReportDetailDao.getMaxOrderIdByReportId(reportId);
	}

	@Override
	public String findReportIdById(String componentId) {
		return multiReportDetailDao.findReportIdById(componentId);
	}
}
