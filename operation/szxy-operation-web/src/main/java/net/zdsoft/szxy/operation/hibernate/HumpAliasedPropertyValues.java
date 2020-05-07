package net.zdsoft.szxy.operation.hibernate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/4 下午6:57
 */
public class HumpAliasedPropertyValues implements PropertyValues {

    private List<PropertyValue> propertyValueList;

    public HumpAliasedPropertyValues(List<PropertyValue> propertyValueList) {
        this.propertyValueList = propertyValueList;
    }

    @Override
    public PropertyValue[] getPropertyValues() {
        return propertyValueList.toArray(new PropertyValue[0]);
    }

    @Override
    public PropertyValue getPropertyValue(String propertyName) {
        return propertyValueList.stream()
                .filter(e-> StringUtils.equals(e.getName(), propertyName))
                .findFirst().orElse(null);
    }

    @Override
    public PropertyValues changesSince(PropertyValues old) {
        return null;
    }

    @Override
    public boolean contains(String propertyName) {
        return propertyValueList.stream().anyMatch(e -> StringUtils.equals(e.getName(), propertyName));
    }

    @Override
    public boolean isEmpty() {
        return propertyValueList == null || propertyValueList.isEmpty();
    }
}
