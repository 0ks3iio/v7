/**
 * FileName: TableConvert.java
 * Author:   shenke
 * Date:     2018/5/28 下午5:35
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.convert;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.zdsoft.bigdata.data.code.ChartCategory;
import net.zdsoft.bigdata.data.echarts.Table;
import net.zdsoft.bigdata.data.echarts.TableOptionWrap;
import net.zdsoft.bigdata.data.manager.api.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 * [
 *      {
 *      "th1":"排行",
 *      "th2":"市名",
 *      "th3":"总数",
 *      "th4":"休学",
 *      "th5":"复学"
 *      },
 *      {
 *      "th1":1,
 *      "th2":"乌鲁木齐",
 *      "th3":100,
 *      "th4":80,
 *      "th5":20
 *      },
 *      {
 *      "th1":2,
 *      "th2":"台北",
 *      "th3":100,
 *      "th4":80,
 *      "th5":20
 *      }
 * ]
 * </p>
 *
 * @author shenke
 * @since 2018/5/28 下午5:35
 */
@Component
public class TableConvert implements DataFormatConvert<IChart<IChart.ChartConfig, Result>, TableOptionWrap> {

    private Logger logger = LoggerFactory.getLogger(TableConvert.class);

    @Override
    public TableOptionWrap convert(IChart<IChart.ChartConfig, Result> config) {
        if (!ChartCategory.table.getChartType().equals(config.getConfig().getChartType())) {
            return null;
        }

        JSONArray array = (JSONArray) config.getQueryResult().getValue();
        Table table = new Table();
        List<Object> tHead = new ArrayList<>();
        List<Table.Tr> trList = new ArrayList<>(array.size() - 1);
        LinkedHashSet<String> headers = new LinkedHashSet<>();
        for (int i = 0, length = array.size(); i < length; i++) {
            //第一个是标题行
            JSONObject val = (JSONObject) array.get(i);
            if (i == 0) {
                val.entrySet().stream().forEach(entry -> {
                    headers.add(entry.getKey());
                    tHead.add(entry.getValue());
                });
            }
            else {
                trList.add(parseTr(val, headers));
            }
        }
        table.setThead(tHead);
        table.setTrs(trList);

        TableOptionWrap wrap = new TableOptionWrap();
        wrap.option(table);
        if (logger.isDebugEnabled()) {
            logger.debug("table is {}", table);
        }
        return wrap;
    }

    private Table.Tr parseTr(JSONObject val, Set<String> headerKey) {
        Table.Tr tr = new Table.Tr();
        List<Object> tds = new ArrayList<>(val.size());
        headerKey.forEach(key -> {
            tds.add(val.get(key));
        });
        tr.setTds(tds);
        return tr;
    }
}
