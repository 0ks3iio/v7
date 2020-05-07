package net.zdsoft.bigdata.dataAnalysis.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.ChartBusinessType;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.entity.MultiReport;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.dataAnalysis.dao.ReceptionReportDao;
import net.zdsoft.bigdata.dataAnalysis.service.ReceptionReportService;
import net.zdsoft.bigdata.metadata.entity.FolderDetail;
import net.zdsoft.bigdata.metadata.service.FolderDetailService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("receptionReportService")
public class ReceptionReportServiceImpl extends
		BaseServiceImpl<MultiReport, String> implements ReceptionReportService {

	@Autowired
	private ReceptionReportDao receptionReportDao;
    @Resource
    private TagService tagService;
    @Resource
    private TagRelationService tagRelationService;
    @Resource
    private FolderDetailService folderDetailService;
    @Resource
	private MultiReportDetailService multiReportDetailService;
    @Resource
	private SubscribeService subscribeService;
    @Resource
	private BigLogService bigLogService;

	@Override
	protected BaseJpaRepositoryDao<MultiReport, String> getJpaDao() {
		return receptionReportDao;
	}

	@Override
	protected Class<MultiReport> getEntityClass() {
		return MultiReport.class;
	}

	@Override
	public List<MultiReport> findReceptionReportsByUserIdAndType(String userId, Integer type, String name) {

		if (StringUtils.isBlank(name)) {
			name = "";
		}
		name = "%" + name + "%";

		return receptionReportDao.getCurrentUserEditMultiReport(userId, type, name);

	}

	@Override
	public List<MultiReport> findMultiReportsByUserIdAndType(
			String creatorUserId, Integer type) {
		return receptionReportDao.findMultiReportsByUserIdAndType(creatorUserId,
				type);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveMultiReport(MultiReport multiReport) {
		multiReport.setModifyTime(new Date());
		String reportName=multiReport.getType()==MultiReport.BOARD?"看板":"报告";
		if (StringUtils.isBlank(multiReport.getId())) {
			multiReport.setId(UuidUtils.generateUuid());
			multiReport.setCreationTime(new Date());
			save(multiReport);
			//业务日志埋点  新增
			LogDto logDto=new LogDto();
			logDto.setBizCode("insert-multiReport");
			logDto.setDescription("数据"+reportName+" "+multiReport.getName());
			logDto.setNewData(multiReport);
			logDto.setBizName("数据"+reportName+"设置");
			bigLogService.insertLog(logDto);
		} else {
			MultiReport oldMultiReport = receptionReportDao.findById(multiReport.getId()).get();
			update(multiReport, multiReport.getId(), new String[] { "name",
					"orderId", "remark", "modifyTime", "orderType" });
			// 删除之前文件夹关系
			folderDetailService.deleteByBusinessId(multiReport.getId());
			// 保存文件夹关系
			FolderDetail folderDetail = new FolderDetail();
			folderDetail.setBusinessId(multiReport.getId());
			folderDetail.setBusinessName(multiReport.getName());
			if (multiReport.getType() == MultiReport.BOARD) {
				folderDetail.setBusinessType(ChartBusinessType.DATA_BOARD.getBusinessType().toString());
			} else {
				folderDetail.setBusinessType(ChartBusinessType.DATA_REPORT.getBusinessType().toString());
			}
			folderDetail.setCreationTime(new Date());
			folderDetail.setOperatorId(multiReport.getCreatorUserId());
			folderDetail.setOrderId(multiReport.getOrderId());
			folderDetail.setFolderId(multiReport.getFolderId());
			folderDetail.setId(UuidUtils.generateUuid());
			folderDetail.setOrderType(multiReport.getOrderType());
			folderDetail.setUnitId(multiReport.getUnitId());
			folderDetailService.save(folderDetail);
			//业务日志埋点  修改
			LogDto logDto=new LogDto();
			logDto.setBizCode("update-multiReport");
			logDto.setDescription("数据"+reportName+" "+oldMultiReport.getName());
			logDto.setOldData(oldMultiReport);
			logDto.setNewData(multiReport);
			logDto.setBizName("数据"+reportName+"设置");
			bigLogService.updateLog(logDto);
		}
	}

	@Override
	public Integer getMaxOrderIdByUnitIdAndType(String unitId, Integer type) {
		return receptionReportDao.getMaxOrderIdByUnitIdAndType(unitId, type);
	}

	@Override
	public Integer getMaxOrderIdByUserIdAndType(String creatorUserId,
			Integer type) {
		return receptionReportDao.getMaxOrderIdByUserIdAndType(creatorUserId, type);
	}

	@Override
	public void deleteMultiReport(String reportId) {
		this.delete(reportId);
		multiReportDetailService.deleteMultiReportDetailsByReportId(reportId);
		// 删除文件夹关联信息
		folderDetailService.deleteByBusinessId(reportId);
	}

	@Override
	public void deleteMultiReportByIds(String[] ids) {
		for (String id : ids) {
			this.deleteMultiReport(id);
		}
	}

}
