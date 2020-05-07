package net.zdsoft.basedata.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author shenke
 * @since 2017.06.27
 */
@Table(name = "sys_product_param")
@Entity
public class SysProductParam extends BaseEntity<String> {

    @Override
    public String fetchCacheEntitName() {
        return "sysProductParam";
    }

    public static final String COMPANY_CHINESE_NAME = "company.chinese.name";
    public static final String COMPANY_ENGLISH_NAME = "company.english.name";
    public static final String COMPANY_COPYRIGHT = "company.copyright";
    public static final String COMPANY_URL = "company.url";
    public static final String COMMERCE_TELEPHONE = "commerce.telephone";
    public static final String SUPPORT_TELEPHONE = "support.telephone";
    public static final String CUSTOM_SERVICE_TELEPHONE = "custom.service.telephone";
    public static final String EMAIL = "email";
    public static final String SHOW_COMPANY_LOGO = "show.company.logo";
    public static final String SHOW_COMPANY_WEBSITE_LINK = "show.company.website.link";

    private String paramCode;
    private String paramName;
    private String paramValue;
    private int displayOrder;
    private String description;

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
