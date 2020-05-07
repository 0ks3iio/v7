package net.zdsoft.bigdata.monitor.action;

import net.zdsoft.bigdata.frame.data.elastic.EsClientService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:zhujy
 * @since:2019/6/11 10:20
 */
@Controller
@RequestMapping(value = "/bigdata/monitor/")
public class MonitorSqlAnalyseLogController {

    @Autowired
    private EsClientService esClientService;

    private static final Logger logger = LoggerFactory
            .getLogger(MonitorSqlAnalyseLogController.class);

    @RequestMapping("sql/index")
    public String index(ModelMap map,
                        String duration,
                        String dbType) {

        List<Json> jsonList = getsqlAnalyseLogList(duration, dbType, 50);
        map.put("list", jsonList);
        map.put("duration",duration);
        map.put("dbType",dbType);
        return "/bigdata/monitor/log/sqlAnalyseLog.ftl";
    }


    private List<Json> getsqlAnalyseLogList(String duration, String dbType, int size) {
        try {
            List<String> resultFieldList = new ArrayList<String>();

            resultFieldList.add("id");
            resultFieldList.add("businessName");
            resultFieldList.add("dbType");
            resultFieldList.add("sql");
            resultFieldList.add("operationTime");
            resultFieldList.add("duration");

            Json page = new Json();
            page.put("pageSize", size);
            page.put("pageIndex", 1);

            List<Json> paramList = new ArrayList<Json>();
            if (StringUtils.isNotBlank(dbType) && !"all".equals(dbType)) {
                Json queryParam1 = new Json();
                queryParam1.put("type", "must");
                queryParam1.put("field", "dbType");
                queryParam1.put("value", dbType);
                paramList.add(queryParam1);
            }
            List<Json> rangeList = new ArrayList<Json>();
            Json rangeParam1 = new Json();
            rangeParam1.put("type", "gte");
            rangeParam1.put("field", "duration");
            rangeParam1.put("value", duration);
            rangeList.add(rangeParam1);

            Json sortParam = new Json();
            sortParam.put("sort_field", "operationTime");
            sortParam.put("sort_type", "desc");

            List<Json> logList = esClientService.rangeQuery(
                    "sql_analyse_log", "sql_analyse_log", paramList, rangeList, resultFieldList, sortParam,page);
            return logList;
        } catch (Exception e) {
            logger.error("出错了:"+e.getMessage());
            return new ArrayList<Json>();
        }

    }
}
