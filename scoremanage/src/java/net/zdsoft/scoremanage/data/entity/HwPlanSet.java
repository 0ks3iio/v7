package net.zdsoft.scoremanage.data.entity;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 汇总表方案科目设置
 * @author niuchao
 * @date 2019/11/5 9:32
 */
@Entity
@Table(name = "scoremanage_hw_plan_set")
public class HwPlanSet extends BaseEntity<String> {
    @ColumnInfo(displayName = "单位id")
    private String unitId;
    @ColumnInfo(displayName = "学期")
    private String hwPlanId;
    @ColumnInfo(displayName = "科目")
    private String objKey;
    @ColumnInfo(displayName = "成绩类型")
    private String objVal;
    @ColumnInfo(displayName = "创建时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date creationTime;
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnInfo(displayName = "修改时间")
    private Date modifyTime;
    @ColumnInfo(displayName = "操作人")
    private String operator;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getHwPlanId() {
        return hwPlanId;
    }

    public void setHwPlanId(String hwPlanId) {
        this.hwPlanId = hwPlanId;
    }

    public String getObjKey() {
        return objKey;
    }

    public void setObjKey(String objKey) {
        this.objKey = objKey;
    }

    public String getObjVal() {
        return objVal;
    }

    public void setObjVal(String objVal) {
        this.objVal = objVal;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String fetchCacheEntitName() {
        return "hwPlanSet";
    }
}
