package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Map;

@Entity
@Table(name = "exammanage_option")
public class EmOption extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;

    private String examId;
    private String examRegionId; //考区id
    private String optionAdd;
    private String optionSchoolId;
    private String optionCode;//考点编号
    private int optionStudentCount;//考点参考学生数
    private int optionPlaceCount;//考场数
    private String optionName;
    @Transient
    private String[] lkxzSelect;
    @Transient
    private Map<String, String> lkxzSelectMap;

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getExamRegionId() {
        return examRegionId;
    }

    public void setExamRegionId(String examRegionId) {
        this.examRegionId = examRegionId;
    }

    public String getOptionAdd() {
        return optionAdd;
    }

    public void setOptionAdd(String optionAdd) {
        this.optionAdd = optionAdd;
    }

    public String getOptionSchoolId() {
        return optionSchoolId;
    }

    public void setOptionSchoolId(String optionSchoolId) {
        this.optionSchoolId = optionSchoolId;
    }

    public int getOptionStudentCount() {
        return optionStudentCount;
    }

    public void setOptionStudentCount(int optionStudentCount) {
        this.optionStudentCount = optionStudentCount;
    }

    public int getOptionPlaceCount() {
        return optionPlaceCount;
    }

    public void setOptionPlaceCount(int optionPlaceCount) {
        this.optionPlaceCount = optionPlaceCount;
    }

    public String[] getLkxzSelect() {
        return lkxzSelect;
    }

    public void setLkxzSelect(String[] lkxzSelect) {
        this.lkxzSelect = lkxzSelect;
    }

    public Map<String, String> getLkxzSelectMap() {
        return lkxzSelectMap;
    }

    public void setLkxzSelectMap(Map<String, String> lkxzSelectMap) {
        this.lkxzSelectMap = lkxzSelectMap;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emOption";
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

}
