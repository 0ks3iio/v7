package net.zdsoft.bigdata.extend.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by wangdongdong on 2018/7/11 11:09.
 */
@Entity
@Table(name = "bg_stat_stu_evaluation")
public class StatStuEvaluation extends BaseEntity<String> {

    private String unitId;

    private String studentId;

    private String itemName;

    private String itemResult;

    private int orderId;

    private Date statTime;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemResult() {
        return itemResult;
    }

    public void setItemResult(String itemResult) {
        this.itemResult = itemResult;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getStatTime() {
        return statTime;
    }

    public void setStatTime(Date statTime) {
        this.statTime = statTime;
    }

    @Override
    public String fetchCacheEntitName() {
        return "statStuEvaluation";
    }
}
