package net.zdsoft.framework.config;

import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import net.zdsoft.basedata.constant.BaseConstants;
import org.springframework.stereotype.Component;

/**
 * Freemarker的全局参数放到这里
 * 
 * @author linqz
 */
@Component
public class BaseDataFreeMarkerConfigurer extends FreemarkerConfigureEx {

	@Override
	public Map<String, Object> getExtendFreemarkerVariables(Configuration freemarkerConfiguration) {
		 Map<String, Object> map = new HashMap<>();
		 map.put("mcodeSetting", McodeSetting.newInstance());
		 map.put("biBigData", BIBigData.newInstance());
		 map.put("PCKC", BaseConstants.PC_KC);
		return map;
	}
}
