package net.zdsoft.system.entity.config;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sys_systemini_unit")
public class UnitIni extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;
    
    private String iniid;
    private String name;
    private String dvalue;
    private String description;
    private String nowvalue;
    private Integer viewable;
    private String flag;
    private String unitid;
    private String validatejs;
    private Integer coercive;
    private Integer orderid;

	/**
     * 单位使用的班牌版本
     */
    public static final String ECC_USE_VERSION = "ECLASSCARD.USE.VERSION";
    public static final String ECC_USE_VERSION_STANDARD = "ECLASSCARD.STANDARD";
    public static final String ECC_USE_VERSION_HW = "ECLASSCARD.HW";

    public static final String ECC_USE_OPEN_CLOSE = "ECLASSCARD.USE.OPEN.CLOSE";
    /**
     * 班牌厂家类型
     */
    public static final String ECC_USE_FACTORY_TYPE="ECLASSCARD.USE.FACTORY.TYPE";
    /**
     * 对接希沃、华瑞安、深圳一德、海康二代班牌--暂时只是定制
     */
    public static final String ECC_USE_FACTORY_1="1";//华瑞安
    public static final String ECC_USE_FACTORY_2="2";//希沃
    public static final String ECC_USE_FACTORY_3="3";//海康一代
    public static final String ECC_USE_FACTORY_4="4";//海康二代
    public static final String ECC_USE_FACTORY_5="5";//深圳一德
    public static final String ECC_USE_FACTORY_6="6";//冠品信
    public static final String ECC_USE_FACTORY_99="99";//其他
    /**
     * 是否显示综合评价
     */
    public static final String ECC_USE_SHOW_ZHPJ="ECLASSCARD.USE.SHOW.ZHPJ";
    
    @Override
    public String fetchCacheEntitName() {
        return "sysSysteminiUnit";
    }
    
	public String getIniid() {
		return iniid;
	}

	public void setIniid(String iniid) {
		this.iniid = iniid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDvalue() {
		return dvalue;
	}

	public void setDvalue(String dvalue) {
		this.dvalue = dvalue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNowvalue() {
		return nowvalue;
	}

	public void setNowvalue(String nowvalue) {
		this.nowvalue = nowvalue;
	}

	public Integer getViewable() {
		return viewable;
	}

	public void setViewable(Integer viewable) {
		this.viewable = viewable;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

	public String getValidatejs() {
		return validatejs;
	}

	public void setValidatejs(String validatejs) {
		this.validatejs = validatejs;
	}

	public Integer getCoercive() {
		return coercive;
	}

	public void setCoercive(Integer coercive) {
		this.coercive = coercive;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

}
