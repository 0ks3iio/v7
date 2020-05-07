package net.zdsoft.bigdata.data.dao;

import java.util.List;

import net.zdsoft.bigdata.data.entity.ReportTerm;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wangdongdong on 2018/5/25 10:03.
 */
public interface ReportTermDao extends BaseJpaRepositoryDao<ReportTerm, String> {

    List<ReportTerm> findReportTermsByReportId(String reportId);

    @Transactional
    void deleteByReportId(String reportId);

    Integer countReportTermByTermDatabaseId(String sourceId);

    Integer countReportTermByTermApiId(String sourceId);
}
