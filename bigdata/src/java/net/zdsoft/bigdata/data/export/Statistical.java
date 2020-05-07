package net.zdsoft.bigdata.data.export;

import net.zdsoft.bigdata.data.ChartBusinessType;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.datav.service.ScreenService;
import net.zdsoft.bigdata.extend.data.service.EventService;
import net.zdsoft.bigdata.metadata.service.FolderDetailService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 图表和大屏统计
 * @author shenke
 * @since 2018/12/18 下午4:46
 */
@Component
public class Statistical {

    @Resource
    private ScreenService screenService;
    @Resource
    private ChartService chartService;
    @Resource
    private EventService eventService;
    @Resource
    private DataModelService dataModelService;
    @Resource
    private ApiService apiService;
    @Resource
    private DsFileService dsFileService;
    @Resource
    private DatabaseService databaseService;
    @Resource
    private EtlJobService etlJobService;
    @Resource
    private NosqlDatabaseService nosqlDatabaseService;
    @Resource
    private FolderDetailService folderDetailService;

    /**
     * 获取大屏总数
     */
    public long countScreen() {
        return screenService.countAll();
    }

    /**
     * 获取某段时间创建的大屏数量
     */
    public long countScreenByDate(Date start, Date end) {
        return screenService.countByTime(start, end);
    }

    /**
     * 获取图表总数
     */
    public long countChart(){
        return chartService.count(null, null, ChartBusinessType.CHART.getBusinessType());
    }



    /**
     * 获取某段时间创建的图表总数
     */
    public long countChartByDate(Date start, Date end) {
        return chartService.count(start, end, ChartBusinessType.CHART.getBusinessType());
    }

    /**
     * 获取报表数量
     */
    public long countReport() {
        return chartService.count(null, null, ChartBusinessType.REPORT.getBusinessType());
    }

    /**
     * 获取某段时间段内创建的报表数量
     */
    public long countReportByDate(Date start, Date end) {
        return chartService.count(start, end, ChartBusinessType.REPORT.getBusinessType());
    }

    /**
     * 获取事件数量
     */
    public long countEvent() {
        return eventService.count(null, null);
    }

    /**
     * 获取某段时间段内创建的事件数量
     */
    public long countEventByDate(Date start, Date end) {
        return eventService.count(start, end);
    }

    /**
     * 获取模型数量
     */
    public long countModel() {
        return dataModelService.count(null, null);
    }

    /**
     * 获取某段时间段内创建的模型数量
     */
    public long countModelByDate(Date start, Date end) {
        return dataModelService.count(start, end);
    }

    /**
     * 获取API数量
     */
    public long countApi() {
        return apiService.count(null, null);
    }

    /**
     * 获取某段时间段内创建的API数量
     */
    public long countApiByDate(Date start, Date end) {
        return apiService.count(start, end);
    }

    /**
     * 获取数据库数量
     */
    public long countDatabase() {
        return databaseService.count(null, null) + dsFileService.count(null, null) + nosqlDatabaseService.count(null, null);
    }

    /**
     * 获取某段时间段内创建的数据库数量
     */
    public long countDatabaseByDate(Date start, Date end) {
        return databaseService.count(start, end) + dsFileService.count(start, end) + nosqlDatabaseService.count(start, end);
    }

    /**
     * 获取Job数量
     */
    public long countJob() {
        return etlJobService.count(null, null);
    }

    /**
     * 获取某段时间段内创建的Job数量
     */
    public long countJobByDate(Date start, Date end) {
        return etlJobService.count(start, end);
    }

    /**
     * 获取用户有权限查看的报表
     * @return
     */
    public long countUserReport(String unitId, String userId, Integer businessType){
        if (businessType == ChartBusinessType.COCKPIT.getBusinessType()) {
            return screenService.countScreensForQuery(unitId, userId);
        }
        return folderDetailService.countAllAuthorityFolderDetail(unitId, userId, businessType);
    }

}
