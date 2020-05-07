package net.zdsoft.system.entity.config;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "sys_option")
public class SystemIni extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;
    
    public static final String FILE_URL                 = "FILE.URL";
    public static final String FILE_PATH                = "FILE.PATH";
    
    public static final String LOGIN_WECHAT_QQ          = "LOGIN.WECHAT.QQ";                 //是否启用微信或QQ登录
    public static final String LOGIN_LOGO_NAME          = "LOGIN.LOGO.NAME";                 //登录页LOGO名
    public static final String LOGIN_LOGO_PICTURE_URL   = "LOGIN.LOGO.PICTURE.URL";          //登录页LOGO图片地址
    public static final String LOGIN_BG_PICTURE_URL     = "LOGIN.BG.PICTURE.URL";            //登录页背景图片地址
    public static final String SYSTEM_SHOW_SCHEDULE="SYSTEM.SHOW.SCHEDULE";//桌面手机端课表是否提前显示
    
    private String iniid;
    private String name;
    private String dvalue;
    private String description;
    private String nowvalue;
    private Integer viewable;
    private String validatejs;
    private Integer orderid;
    private String subsystemid;
    private Integer coercive;
    private Integer valueType;

    @Override
    public String fetchCacheEntitName() {
        return "systemIni";
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

    public String getValidatejs() {
        return validatejs;
    }

    public void setValidatejs(String validatejs) {
        this.validatejs = validatejs;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getSubsystemid() {
        return subsystemid;
    }

    public void setSubsystemid(String subsystemid) {
        this.subsystemid = subsystemid;
    }

    public Integer getCoercive() {
        return coercive;
    }

    public void setCoercive(Integer coercive) {
        this.coercive = coercive;
    }

    public Integer getValueType() {
        return valueType;
    }

    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }

}
