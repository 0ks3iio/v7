package net.zdsoft.familydear.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.entity
 * @ClassName: FamdearMonth
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/24 9:18
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/24 9:18
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Entity
@Table(name = "FAMDEAR_MONTH")
public class FamdearMonth extends BaseEntity<String> {
    private String id;
    private String year;
    private String createUserId;
    private Date createTime;
    private String unitId;
    private String activityId;
    @Transient
    private String activityName;
    private String arrangeId;
    @Transient
    private String arrangeMame;
    private String deptId;
    private String type;
    @Transient
    private String typeStr;
    private String activityForm;
    private Date activityTime;
    private Date activityEndTime;
    @Transient
    private String activityTimeStr;
    private String place;
    private String partUserIds;
    @Transient
    private String partUserNames;
    private Integer partnum;
    private String activityContent;
    private String mark;
    private String state;
    @Transient
    private List<FamDearAttachment> attachmentList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String fetchCacheEntitName() {
        return "famdearMonth";
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }


    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getArrangeId() {
        return arrangeId;
    }

    public void setArrangeId(String arrangeId) {
        this.arrangeId = arrangeId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivityForm() {
        return activityForm;
    }

    public void setActivityForm(String activityForm) {
        this.activityForm = activityForm;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }

    public Integer getPartnum() {
        return partnum;
    }

    public void setPartnum(Integer partnum) {
        this.partnum = partnum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getActivityTime() {
        return activityTime;
    }

    public Date getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(Date activityEndTime) {
        this.activityEndTime = activityEndTime;
    }


    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public String getActivityTimeStr() {
        return activityTimeStr;
    }

    public void setActivityTimeStr(String activityTimeStr) {
        this.activityTimeStr = activityTimeStr;
    }

    public String getPartUserIds() {
        return partUserIds;
    }

    public void setPartUserIds(String partUserIds) {
        this.partUserIds = partUserIds;
    }

    public String getPartUserNames() {
        return partUserNames;
    }

    public void setPartUserNames(String partUserNames) {
        this.partUserNames = partUserNames;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getArrangeMame() {
        return arrangeMame;
    }

    public void setArrangeMame(String arrangeMame) {
        this.arrangeMame = arrangeMame;
    }

    public List<FamDearAttachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<FamDearAttachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamdearMonth that = (FamdearMonth) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(year, that.year) &&
                Objects.equals(createUserId, that.createUserId) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(unitId, that.unitId) &&
                Objects.equals(activityId, that.activityId) &&
                Objects.equals(arrangeId, that.arrangeId) &&
                Objects.equals(deptId, that.deptId) &&
                Objects.equals(type, that.type) &&
                Objects.equals(activityForm, that.activityForm) &&
                Objects.equals(activityTime, that.activityTime) &&
                Objects.equals(place, that.place) &&
                Objects.equals(partnum, that.partnum) &&
                Objects.equals(activityContent, that.activityContent) &&
                Objects.equals(mark, that.mark);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, year, createUserId, createTime, unitId, activityId, arrangeId, deptId, type, activityForm, activityTime, place, partnum, activityContent, mark);
    }
}
