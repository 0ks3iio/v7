package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.MultiReport;
import net.zdsoft.framework.entity.Pagination;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MultiReportService extends BaseService<MultiReport, String> {

    /**
     * 根据单位和类型获取组合报告
     *
     * @param unitId
     * @param type
     * @return
     */
    public List<MultiReport> findMultiReportsByUnitIdAndType(String unitId, Integer type);


    /**
     * 获取用户的报告和看板
     *
     * @param unitId
     * @param userId
     * @param name
     * @param type
     * @param pagination
     * @return
     */
    public List<MultiReport> findMultiReports(String unitId, String userId, String name, Integer type, Pagination pagination);

    /**
     * 根据单位和类型 标签和名称查询
     *
     * @param unitId
     * @param type
     * @return
     */
    public List<MultiReport> findMultiReportsByUnitIdAndType(String unitId, Integer type, String[] tagIds, String name);

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

    /**
     * 查询单位下的报告->批量授权用
     *
     * @param unitId
     * @return
     */
    List<MultiReport> findAll(String unitId, Integer type);

    /**
     * 保存看板报告
     *
     * @param multiReport
     * @param tags
     * @param orderUnits
     * @param orderUsers
     */
    void saveMultiReport(MultiReport multiReport, String[] tags, String[] orderUnits, String[] orderUsers);

    void deleteMultiReport(String reportId);

    void deleteMultiReportByIds(String[] ids);
}
