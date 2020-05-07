package net.zdsoft.background.common;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;

public class ConfigRuleSet extends RuleSetBase{
	public void addRuleInstances(Digester digester) {
		String pattern = "config";
		digester.addObjectCreate(pattern, ServicesConfig.class);
		digester.addSetProperties(pattern);

		pattern = "config/service";
		digester.addObjectCreate(pattern, ServiceConfig.class);
		digester.addSetProperties(pattern);
		digester.addSetNext(pattern, "addServiceConfig",
				ServiceConfig.class.getName());

		pattern = "config/service/param";
		digester.addObjectCreate(pattern, ServiceParam.class);
		digester.addSetProperties(pattern);
		digester.addSetNext(pattern, "addParam", ServiceParam.class.getName());
	}
}
