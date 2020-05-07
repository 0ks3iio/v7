package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exammanage_place_group")
public class EmPlaceGroup extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;
    private String examId;
    private String schoolId;
    private String groupId;
    private String examPlaceId;

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getExamPlaceId() {
        return examPlaceId;
    }

    public void setExamPlaceId(String examPlaceId) {
        this.examPlaceId = examPlaceId;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emPlaceGroup";
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

}
