package net.zdsoft.bigdata.dataAnalysis.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.MultiReport;

import java.util.List;

public interface ReceptionReportService extends BaseService<MultiReport, String> {

    /**
     * 根据用户和类型 名称查询
     *
     * @param userId
     * @param type
     * @return
     */
    public List<MultiReport> findReceptionReportsByUserIdAndType(String userId, Integer type, String name);

    /**
     * 根据创建用户和类型获取报告
     *
     * @param creatorUserId
     * @param type
     * @return
     */
    public List<MultiReport> findMultiReportsByUserIdAndType(String creatorUserId, Integer type);

    /**
     * 保存组合报告
     *
     * @param multiReport
     */
    public void saveMultiReport(MultiReport multiReport);

    /**
     * 根据单位和类型获取组合报告最大排序
     *
     * @param unitId
     * @param type
     * @return
     */
    public Integer getMaxOrderIdByUnitIdAndType(String unitId, Integer type);

    /**
     * 根据创建用户和类型获取报告最大排序
     *
     * @param creatorUserId
     * @param type
     * @return
     */
    public Integer getMaxOrderIdByUserIdAndType(String creatorUserId, Integer type);

    void deleteMultiReport(String reportId);

    void deleteMultiReportByIds(String[] ids);
}
