package net.zdsoft.szxy.operation.autoconfigure;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitBasicColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitEntityNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;

/**
 * 为了和原有7。0的保持一致，以后config这部分的代码需要单独抽取出来
 * @author shenke
 * @since 2019/1/22 下午2:18
 */
public class SzxyNamingStrategy extends ImplicitNamingStrategyJpaCompliantImpl {

    @Override
    public Identifier determinePrimaryTableName(ImplicitEntityNameSource source) {
        return convert(super.determinePrimaryTableName(source));
    }

    @Override
    public Identifier determineBasicColumnName(ImplicitBasicColumnNameSource source) {
        return convert(super.determineBasicColumnName(source));
    }

    private Identifier convert(Identifier identifier) {
        if (StringUtils.isBlank(identifier.getText())) {
            return identifier;
        }
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";
        String newName = identifier.getText().replaceAll(regex, replacement).toLowerCase();
        return Identifier.toIdentifier(newName);
    }
}
