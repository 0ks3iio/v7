/**
 * FileName: INumberConvert.java
 * Author:   shenke
 * Date:     2018/5/29 下午4:36
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.convert;

import net.zdsoft.bigdata.data.code.ChartCategory;
import net.zdsoft.bigdata.data.echarts.INumber;
import net.zdsoft.bigdata.data.echarts.INumberOptionWrap;
import net.zdsoft.bigdata.data.manager.api.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author shenke
 * @since 2018/5/29 下午4:36
 */
@Component
public class INumberConvert implements DataFormatConvert<IChart<IChart.ChartConfig, Result>, INumberOptionWrap> {

    private Logger logger = LoggerFactory.getLogger(INumberConvert.class);

    @Override
    public INumberOptionWrap convert(IChart<IChart.ChartConfig, Result> config) {
        if (!ChartCategory.indicators.getChartType().equals(config.getConfig().getChartType())) {
            return null;
        }
        JSONArray array = (JSONArray) config.getQueryResult().getValue();
        if (array.size() > 2) {
            logger.error("INumber Convert error: 数据格式错误，JSONArray至多包含两个JSON对象");
            return new INumberOptionWrap().success(false).message("数据格式错误，JSONArray至多包含两个JSON对象");
        }
        INumber number = null;
        INumber.Ration ration = null;
        for (int i = 0; i < 2; i++) {
            JSONObject val = (JSONObject) array.get(i);
            if (i == 0) {
                number = JSONObject.parseObject(val.toJSONString(), INumber.class);
            }
            else {
                ration = JSONObject.parseObject(val.toJSONString(), INumber.Ration.class);
            }
        }
        number.setRation(ration);
        INumberOptionWrap wrap = new INumberOptionWrap();
        return wrap.option(number);
    }
}
