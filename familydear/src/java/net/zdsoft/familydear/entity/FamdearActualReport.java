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
 * @ClassName: FamdearActualReportEntity
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/24 9:17
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/24 9:17
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Entity
@Table(name = "famdear_actual_report")
public class FamdearActualReport extends BaseEntity<String> {
    private String year;
    private String createUserId;
    @Transient
    private String createUserName;
    private Date createTime;
    @Transient
    private String createTimeStr;
    private String unitId;
    private String activityId;
    @Transient
    private String activityName;
    @Transient
    private String villageName;
    @Transient
    private String activityTimeStr;
    @Transient
    private int imageNum;

    private String arrangeId;
    @Transient
    private String arrangeName;
    private Date arriveTime;
    private Date backTime;
    private String activityFrom;
    @Transient
    private String[] activityFroms;
    private Float donateMoney;
    private String donateMark;
    private Integer donateObjectnum;
    private Float conversionAmount;
    private String donateobjMark;
    private Integer seekMedical;
    private Integer seekStudy;
    private Integer seekEmploy;
    private Integer developProduct;
    private Integer otherThingsnum;
    private Integer benefitPeople;
    private Integer walkPeople;
    private String place;
    private String activityContent;
    private String activityTitle;
    private String ticketTitle;
    private String ticketUpload;
    private String mark;
    private String state;
    @Transient
    private String stateStr;
    @Transient
    private List<AttachmentDto> activityTitles;
    @Transient
    private List<AttachmentDto> ticketTitles;

    @Override
    public String fetchCacheEntitName() {
        return "famdearActualReport";
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

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
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

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getActivityTimeStr() {
        return activityTimeStr;
    }

    public void setActivityTimeStr(String activityTimeStr) {
        this.activityTimeStr = activityTimeStr;
    }

    public int getImageNum() {
        return imageNum;
    }

    public void setImageNum(int imageNum) {
        this.imageNum = imageNum;
    }

    public String getArrangeId() {
        return arrangeId;
    }

    public void setArrangeId(String arrangeId) {
        this.arrangeId = arrangeId;
    }

    public Date getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Date getBackTime() {
        return backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    public String getActivityFrom() {
        return activityFrom;
    }

    public void setActivityFrom(String activityFrom) {
        this.activityFrom = activityFrom;
    }

    public String[] getActivityFroms() {
        return activityFroms;
    }

    public void setActivityFroms(String[] activityFroms) {
        this.activityFroms = activityFroms;
    }

    public Float getDonateMoney() {
        return donateMoney;
    }

    public void setDonateMoney(Float donateMoney) {
        this.donateMoney = donateMoney;
    }

    public String getDonateMark() {
        return donateMark;
    }

    public void setDonateMark(String donateMark) {
        this.donateMark = donateMark;
    }

    public Integer getDonateObjectnum() {
        return donateObjectnum;
    }

    public void setDonateObjectnum(Integer donateObjectnum) {
        this.donateObjectnum = donateObjectnum;
    }

    public Float getConversionAmount() {
        return conversionAmount;
    }

    public void setConversionAmount(Float conversionAmount) {
        this.conversionAmount = conversionAmount;
    }

    public String getDonateobjMark() {
        return donateobjMark;
    }

    public void setDonateobjMark(String donateobjMark) {
        this.donateobjMark = donateobjMark;
    }

    public Integer getSeekMedical() {
        return seekMedical;
    }

    public void setSeekMedical(Integer seekMedical) {
        this.seekMedical = seekMedical;
    }

    public Integer getSeekStudy() {
        return seekStudy;
    }

    public void setSeekStudy(Integer seekStudy) {
        this.seekStudy = seekStudy;
    }

    public Integer getSeekEmploy() {
        return seekEmploy;
    }

    public void setSeekEmploy(Integer seekEmploy) {
        this.seekEmploy = seekEmploy;
    }

    public Integer getDevelopProduct() {
        return developProduct;
    }

    public void setDevelopProduct(Integer developProduct) {
        this.developProduct = developProduct;
    }

    public Integer getOtherThingsnum() {
        return otherThingsnum;
    }

    public void setOtherThingsnum(Integer otherThingsnum) {
        this.otherThingsnum = otherThingsnum;
    }

    public Integer getBenefitPeople() {
        return benefitPeople;
    }

    public void setBenefitPeople(Integer benefitPeople) {
        this.benefitPeople = benefitPeople;
    }

    public Integer getWalkPeople() {
        return walkPeople;
    }

    public void setWalkPeople(Integer walkPeople) {
        this.walkPeople = walkPeople;
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

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    public String getTicketUpload() {
        return ticketUpload;
    }

    public void setTicketUpload(String ticketUpload) {
        this.ticketUpload = ticketUpload;
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

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public List<AttachmentDto> getActivityTitles() {
        return activityTitles;
    }

    public void setActivityTitles(List<AttachmentDto> activityTitles) {
        this.activityTitles = activityTitles;
    }

    public List<AttachmentDto> getTicketTitles() {
        return ticketTitles;
    }

    public void setTicketTitles(List<AttachmentDto> ticketTitles) {
        this.ticketTitles = ticketTitles;
    }

    public String getArrangeName() {
        return arrangeName;
    }

    public void setArrangeName(String arrangeName) {
        this.arrangeName = arrangeName;
    }
}
