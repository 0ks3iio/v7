package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.entity.ReportTemplate;
import net.zdsoft.bigdata.data.entity.ReportTermTree;
import net.zdsoft.framework.entity.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author ke_shen@126.com
 * @since 2018/4/17 下午3:55
 */
public interface ChartService extends BaseService<Chart, String> {

    long count(Date start, Date end, Integer type);

    /** 根据unitId查询所有的chart */
    List<Chart> getChartsByUnitId(String unitId);

    /** 根据unitId和报表类型查询所有的chart */
    List<Chart> getChartsByUnitIdAndChartType(String unitId, Integer chartType);

    /** 根据Id获取属于大屏的图表 */
    Chart getChartPartOfCockpitById(String id);

    /** 根据Id获取所有属于大屏的图表 */
    List<Chart> getChartsPartOfCockpitByIds(String... ids);

    /**
     * 获取某单位下指定businessType的图表
     * 可能是基本图表也可能是大屏使用的图表
     * 这个根据modifyTime进行排序 order by creationTime desc
     * @see net.zdsoft.bigdata.data.ChartBusinessType
     */
    List<Chart> getChartsByUnitIdAndBusinessTypeAndSort(String unitId, int businessType);

    List<Chart> getChartsByUnitIdAndBusinessTypeAndSort(String unitId, int businessType, Pagination pagination);

    /**
     * 根据Id 获取图表，分页，并按照modifyTime排序
     * @param ids id array
     * @param pageable 分页参数
     * @return chart List
     */
    Page<Chart> getChartsByIds(String[] ids, Pageable pageable);

    /**
     * 保存chart同时更新Tag
     * 还会更新TagRelation  关联关系表
     * @see net.zdsoft.bigdata.data.entity.Tag
     * @see net.zdsoft.bigdata.data.entity.TagRelation
     * @param chart 图表
     * @param tagNames tagName列表 tagName可能是系统已经存在的也可能是新增的
     */
    void saveChartAngTags(Chart chart, List<String> tagNames);

    void saveChart(Chart chart, String[] tags, String[] orderUnits, String[] orderUsers);

    List<Chart> findByIds(String[] ids, Pagination pagination);


    /**
     * 获取某个用户可见的chart
     *  编辑页面包含私有图表，查询页面不包含私有图表
     *  编辑页面包含{@link net.zdsoft.bigdata.data.OrderType#UNIT_ORDER_USER_AUTHORIZATION} 但是个人并未授权的图表
     *
     * @param userId 用户Id
     * @param pagination 分页参数
     * @param editPage 是否编辑页面
     * @param isReport 是否是报表
     * @return
     */
    List<Chart> getCurrentUserCharts(String userId, String unitId, String[] tagIds, String name, Pagination pagination, boolean editPage, boolean isReport, Boolean isForCockpit);

    /**
     * 删除报表
     * @param reportId
     */
    void deleteReport(String reportId, String templateId);

    /**
     * 删除图表
     * @param chartId 图表ID
     */
    void deleteChart(String chartId);

    /**
     * 批量删除
     * @param chartIds
     */
    void batchDelete(String[] chartIds);

    /**
     * 保存报表
     * @param chart
     * @param reportTemplate
     * @param reportTermTrees
     * @param tags
     */
    void saveReport(Chart chart, ReportTemplate reportTemplate, List<ReportTermTree> reportTermTrees, String[] tags, String[] orderUnits, String [] orderUsers) throws IOException;

    /**
     * 查找是标题的chart
     * @see net.zdsoft.bigdata.data.code.ChartType#TEXT_TITLE
     * @param ids id数组
     * @return
     */
    List<Chart> getTextChartByIds(String[] ids);

    /**
     * 查找是自定义类型图表的Chart（这种Chart和TextChart类似，在Cockpit页面定义）
     * @param ids
     * @return
     */
    List<Chart> getOtherChartByIds(String[] ids);
}
