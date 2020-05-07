package net.zdsoft.system.entity.server;

import com.alibaba.fastjson.TypeReference;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.utils.SUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/7/15 11:19
 */
@Entity
@Table(name="sys_model_operation")
public class ModelOperation extends BaseEntity<Integer> {

    @Column(name = "moduleid")
    private Integer modelId;// 模块id
    @Column(name="operatorname")
    private String operatorname;
    @Column(name="operheading")
    private String operheading;
    @Column(name="description")
    private String description;
    @Column(name="operatortype")
    private Integer operatortype;
    @Column(name="isactive")
    private boolean isactive;
    @Column(name="orderid")
    private Integer orderid;
    @Column(name="opergroup")
    private Integer opergroup;
    @Column(name="operweight")
    private Integer operweight;


    public static List<ModelOperation> dt(String data) {
        List<ModelOperation> ts = SUtils.dt(data, new TypeReference<List<ModelOperation>>() {
        });
        if (ts == null) {
            ts = new ArrayList<ModelOperation>();
        }
        return ts;

    }
    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getOperatorname() {
        return operatorname;
    }

    public void setOperatorname(String operatorname) {
        this.operatorname = operatorname;
    }

    public String getOperheading() {
        return operheading;
    }

    public void setOperheading(String operheading) {
        this.operheading = operheading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOperatortype() {
        return operatortype;
    }

    public void setOperatortype(Integer operatortype) {
        this.operatortype = operatortype;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getOpergroup() {
        return opergroup;
    }

    public void setOpergroup(Integer opergroup) {
        this.opergroup = opergroup;
    }

    public Integer getOperweight() {
        return operweight;
    }

    public void setOperweight(Integer operweight) {
        this.operweight = operweight;
    }

    @Override
    public String fetchCacheEntitName() {
        return "modelOperation";
    }
}
