package net.zdsoft.bigdata.data.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.dao.ReportTermDao;
import net.zdsoft.bigdata.data.entity.ReportTerm;
import net.zdsoft.bigdata.data.entity.ReportTermTree;
import net.zdsoft.bigdata.data.service.ReportTermService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;

import org.springframework.stereotype.Service;

/**
 * Created by wangdongdong on 2018/5/25 10:05.
 */
@Service
public class ReportTermServiceImpl extends BaseServiceImpl<ReportTerm, String> implements ReportTermService {

    @Resource
    private ReportTermDao reportTermDao;

    @Override
    protected BaseJpaRepositoryDao<ReportTerm, String> getJpaDao() {
        return reportTermDao;
    }

    @Override
    protected Class<ReportTerm> getEntityClass() {
        return ReportTerm.class;
    }

    @Override
    public List<ReportTerm> getReportTermByReportId(String reportId) {
        return reportTermDao.findReportTermsByReportId(reportId);
    }

    @Override
    public void deleteByReportId(String reportId) {
        reportTermDao.deleteByReportId(reportId);
    }

    @Override
    public void saveReportTerms(List<ReportTermTree> reportTermTrees, String reportId) {
        // 删除原有的条件
        deleteByReportId(reportId);
        reportTermTrees.forEach(tree -> saveReportTermTree(tree, reportId, null));
    }

    @Override
    public Integer countBySourceId(Integer sourceType, String sourceId) {
        if (DataSourceType.DB.getValue() == sourceType) {
            return reportTermDao.countReportTermByTermDatabaseId(sourceId);
        }
        return reportTermDao.countReportTermByTermApiId(sourceId);
    }

    private void saveReportTermTree(ReportTermTree tree, String reportId, String cascadeId) {
        if (tree != null) {
            ReportTerm reportTerm = tree.getReportTerm();
            reportTerm.setReportId(reportId);
            reportTerm.setCascadeTermId(cascadeId);
            saveReportTerm(reportTerm);
            if (tree.getChilds() != null) {
                tree.getChilds().forEach(e-> saveReportTermTree(e, reportId, reportTerm.getId()));
            }
        }
    }

    private void saveReportTerm(ReportTerm reportTerm) {
        reportTerm.setModifyTime(new Date());
        reportTerm.setCreationTime(new Date());
        reportTerm.setId(UuidUtils.generateUuid());
        reportTermDao.save(reportTerm);
    }
}
