package net.zdsoft.bigdata.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.ReportTerm;
import net.zdsoft.bigdata.data.entity.ReportTermTree;

/**
 * Created by wangdongdong on 2018/5/25 10:04.
 */
public interface ReportTermService extends BaseService<ReportTerm, String> {

    List<ReportTerm> getReportTermByReportId(String reportId);

    void deleteByReportId(String reportId);

    void saveReportTerms(List<ReportTermTree> reportTermTrees, String reportId);

    /**
     * 根据数据源类型查询数量
     * @param sourceType (1、DB 2、api)
     * @param sourceId
     * @return
     */
    Integer countBySourceId(Integer sourceType, String sourceId);
}
