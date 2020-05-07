package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.ChartBusinessType;
import net.zdsoft.bigdata.data.dao.MultiReportDao;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.entity.MultiReport;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.metadata.entity.FolderDetail;
import net.zdsoft.bigdata.metadata.service.FolderDetailService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("multiReportService")
public class MultiReportServiceImpl extends
        BaseServiceImpl<MultiReport, String> implements MultiReportService {

    @Autowired
    private MultiReportDao multiReportDao;
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
        return multiReportDao;
    }

    @Override
    protected Class<MultiReport> getEntityClass() {
        return MultiReport.class;
    }

    @Override
    public List<MultiReport> findMultiReportsByUnitIdAndType(String unitId,
                                                             Integer type) {
        return multiReportDao.findMultiReportsByUnitIdAndType(unitId, type);
    }

    @Override
    public List<MultiReport> findMultiReports(String unitId, String userId, String name, Integer type, Pagination pagination) {
        if (org.apache.commons.lang3.StringUtils.isBlank(name)) {
            name = "";
        }
        name = "%" + name + "%";
        Page<MultiReport> page = null;
        Pageable pageable = pagination == null ? null : pagination.toPageable();
        page = multiReportDao.getCurrentUserQueryReportChart(unitId, userId, name, type, pageable);
        if (pagination != null) {
            pagination.setMaxRowCount((int) page.getTotalElements());
        }
        return page.getContent();
    }


    @Override
    public List<MultiReport> findMultiReportsByUnitIdAndType(String unitId, Integer type, String[] tagIds, String name) {

        if (StringUtils.isBlank(name)) {
            name = "";
        }
        name = "%" + name + "%";
        if (ArrayUtils.isNotEmpty(tagIds)) {
            return multiReportDao.getCurrentUserEditMultiReport(unitId, tagIds, type, name);
        }

        return multiReportDao.getCurrentUserEditMultiReport(unitId, type, name);

    }

    @Override
    public List<MultiReport> findMultiReportsByUserIdAndType(
            String creatorUserId, Integer type) {
        return multiReportDao.findMultiReportsByUserIdAndType(creatorUserId,
                type);
    }

    @Override
    public void saveMultiReport(MultiReport multiReport) {
        multiReport.setModifyTime(new Date());
        String reportName = multiReport.getType() == MultiReport.BOARD ? "看板" : "报告";
        if (StringUtils.isBlank(multiReport.getId())) {
            multiReport.setId(UuidUtils.generateUuid());
            multiReport.setCreationTime(new Date());
            save(multiReport);
            //业务日志埋点  新增
            LogDto logDto = new LogDto();
            logDto.setBizCode("insert-multiReport");
            logDto.setDescription("数据" + reportName + " " + multiReport.getName());
            logDto.setNewData(multiReport);
            logDto.setBizName("数据" + reportName + "设置");
            bigLogService.insertLog(logDto);
        } else {
            MultiReport oldMultiReport = multiReportDao.findById(multiReport.getId()).get();
            update(multiReport, multiReport.getId(), new String[]{"name",
                    "orderId", "remark", "modifyTime", "orderType"});
            //业务日志埋点  修改
            LogDto logDto = new LogDto();
            logDto.setBizCode("update-multiReport");
            logDto.setDescription("数据" + reportName + " " + oldMultiReport.getName());
            logDto.setOldData(oldMultiReport);
            logDto.setNewData(multiReport);
            logDto.setBizName("数据" + reportName + "设置");
            bigLogService.updateLog(logDto);
        }
    }

    @Override
    public Integer getMaxOrderIdByUnitIdAndType(String unitId, Integer type) {
        return multiReportDao.getMaxOrderIdByUnitIdAndType(unitId, type);
    }

    @Override
    public Integer getMaxOrderIdByUserIdAndType(String creatorUserId,
                                                Integer type) {
        return multiReportDao.getMaxOrderIdByUserIdAndType(creatorUserId, type);
    }

    @Override
    public List<MultiReport> findAll(String unitId, Integer type) {
        return multiReportDao.findAllByUnitIdAndType(unitId, type);
    }

    @Override
    public void saveMultiReport(MultiReport multiReport, String[] tags, String[] orderUnits, String[] orderUsers) {
        // 更新标签关系
        tagService.updateTagRelationByBusinessId(tags, multiReport.getId());
        //更新chart
        this.saveMultiReport(multiReport);
        //更新订阅关系
        //单位订阅
        subscribeService.addAuthorization(new String[]{multiReport.getId()}, orderUnits, orderUsers, multiReport.getUnitId(), multiReport.getOrderType());
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
    }

    @Override
    public void deleteMultiReport(String reportId) {
        this.delete(reportId);
        multiReportDetailService.deleteMultiReportDetailsByReportId(reportId);
        // 删除文件夹关联信息
        folderDetailService.deleteByBusinessId(reportId);
        // 删除订阅
        subscribeService.deleteByBusinessId(new String[]{reportId});
        // 删除标签关系
        tagRelationService.deleteByBusinessId(reportId);
    }

    @Override
    public void deleteMultiReportByIds(String[] ids) {
        for (String id : ids) {
            this.deleteMultiReport(id);
        }
    }

}
