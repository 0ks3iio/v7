package net.zdsoft.desktop.config;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import net.zdsoft.framework.config.FreemarkerConfigureEx;
import net.zdsoft.framework.utils.FreemarkerUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author shenke
 * @since 2017/2/9 9:48
 */
@Component
public class DeskTopFreeMarkerConfigurerEx extends FreemarkerConfigureEx{

    @Override
    public Map<String, Object> getExtendFreemarkerVariables(Configuration freemarkerConfiguration) {
        Map<String,Object> map = Maps.newHashMap();
        try {
            map.put("JSONUtils", JSONUtils.class.newInstance());
            map.put("JSONObject", JSONObject.class.newInstance());
            map.put("freemarkerUtils",FreemarkerUtils.class.newInstance());
            map.put("UuidUtils", UuidUtils.class.newInstance());
            map.put("StringUtils", StringUtils.class.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
