package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "exammanage_place")
public class EmPlace extends BaseEntity<String> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String examId;
    private String schoolId;
    private String examPlaceCode;
    private String placeId;
    private Integer count;//考场总人数
    private String optionId;//考点id
    @Transient
    private int stuNum;//实际安排人数
    @Transient
    private String placeName;
    @Transient
    private String regionName;//考区名称
    @Transient
    private String optionName;//考点名称
    @Transient
    private String examRegionCode;//考区编号
    @Transient
    private String optionCode;//考点编号
    @Transient
    private String buildName;
    @Transient
    private String stuNumRange;//考号范围
    @Transient
    private String hasCheck;//
    @Transient
    private String canCheck;//
    @Transient
    private String groupId;
    @Transient
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        if (groupId == null) {
            return "";
        }
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getHasCheck() {
        return hasCheck;
    }

    public void setHasCheck(String hasCheck) {
        this.hasCheck = hasCheck;
    }

    public String getCanCheck() {
        return canCheck;
    }

    public void setCanCheck(String canCheck) {
        this.canCheck = canCheck;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getExamPlaceCode() {
        return examPlaceCode;
    }

    public void setExamPlaceCode(String examPlaceCode) {
        this.examPlaceCode = examPlaceCode;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public int getStuNum() {
        return stuNum;
    }

    public void setStuNum(int stuNum) {
        this.stuNum = stuNum;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emPlace";
    }

    public String getStuNumRange() {
        return stuNumRange;
    }

    public void setStuNumRange(String stuNumRange) {
        this.stuNumRange = stuNumRange;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getExamRegionCode() {
        return examRegionCode;
    }

    public void setExamRegionCode(String examRegionCode) {
        this.examRegionCode = examRegionCode;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

}
