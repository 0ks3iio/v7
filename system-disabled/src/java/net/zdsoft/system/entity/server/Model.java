package net.zdsoft.system.entity.server;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;

@Entity
@Table(name = "sys_model")
public class Model extends BaseEntity<Integer> {
    private static final long serialVersionUID = 1L;

    public final static int PARENT_ID_DIRECT_SUBSYSTEM = -1;

    @ColumnInfo(hide = true, nullable = false, displayName = "编号")
    private String modelId;

    private String mid;// 模块id
    @Column(name = "parentid")
    private Integer parentId;
    @Column(name = "orderid")
    private Integer displayOrder;
    private String name;// 模块名称
    private String type;
    private String picture;
    private String url;

    @Column(name = "subsystem")
    private Integer subSystem;// 所属应用系统id
    @Column(name = "usertype")
    private String userType;
    @Column(name = "unitclass")
    private Integer unitClass;
    private Integer mark;

    private String version;
    private String parm;// 参数不为空时为客户端model

    public static List<Model> dt(String data) {
        List<Model> ts = SUtils.dt(data, new TypeReference<List<Model>>() {
        });
        if (ts == null) {
            ts = new ArrayList<Model>();
        }
        return ts;

    }

    public static List<Model> dt(String data, Pagination page) {
        JSONObject json = JSONObject.parseObject(data);
        List<Model> ts = SUtils.dt(json.getString("data"), new TypeReference<List<Model>>() {
        });
        if (ts == null) {
            ts = new ArrayList<Model>();
        }
        if (json.containsKey("count")) {
            page.setMaxRowCount(json.getInteger("count"));
        }
        return ts;

    }

    public static Model dc(String data) {
        return SUtils.dc(data, Model.class);
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    private Integer openType;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(Integer subSystem) {
        this.subSystem = subSystem;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getUnitClass() {
        return unitClass;
    }

    public void setUnitClass(Integer unitClass) {
        this.unitClass = unitClass;
    }

    @Override
    public String fetchCacheEntitName() {
        return "model";
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getParm() {
        return parm;
    }
    public Integer getMark() {
        return mark;
    }

    public void setParm(String parm) {
        this.parm = parm;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

}
