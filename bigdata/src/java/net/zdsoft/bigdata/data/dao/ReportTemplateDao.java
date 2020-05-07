package net.zdsoft.bigdata.data.dao;

import java.util.List;

import net.zdsoft.bigdata.data.entity.ReportTemplate;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

/**
 * Created by wangdongdong on 2018/5/10.
 */
public interface ReportTemplateDao extends BaseJpaRepositoryDao<ReportTemplate, String> {

    /**
     * 根据单位id查询所有报表模版
     * @param unitId
     * @return
     */
    @Query("FROM ReportTemplate where unitId =?1 order by creationTime desc")
    List<ReportTemplate> getReportTemplatesByUnitId(String unitId);

}
