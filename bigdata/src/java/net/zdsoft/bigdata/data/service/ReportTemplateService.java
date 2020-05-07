package net.zdsoft.bigdata.data.service;

import java.io.IOException;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.ReportTemplate;

/**
 * Created by wangdongdong on 2018/5/10.
 */
public interface ReportTemplateService extends BaseService<ReportTemplate, String> {

    /**
     * 根据单位id查询
     * @param unitId
     * @return
     */
    List<ReportTemplate> findReportTemplatesByUnitId(String unitId);

    void saveReportTemplate(ReportTemplate template) throws IOException;

    void deleteReportTemplate(String templateId, String unitId);

}
