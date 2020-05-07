package net.zdsoft.framework.config;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitBasicColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitEntityNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;

public class WpImprovedNamingStrategy extends ImplicitNamingStrategyJpaCompliantImpl {

	
	@Override
	public Identifier determinePrimaryTableName(ImplicitEntityNameSource source) {
		// TODO Auto-generated method stub
		return convert(super.determinePrimaryTableName(source));
	}

	@Override
	public Identifier determineBasicColumnName(ImplicitBasicColumnNameSource source) {
		return convert(super.determineBasicColumnName(source));
	}

	private Identifier convert(Identifier identifier) {
		if (identifier == null || StringUtils.isBlank(identifier.getText())) {
			return identifier;
		}

		String regex = "([a-z])([A-Z])";
		String replacement = "$1_$2";
		String newName = identifier.getText().replaceAll(regex, replacement).toLowerCase();
		return Identifier.toIdentifier(newName);
	}

}