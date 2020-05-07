package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exammanage_joinexamsch_info")
public class EmJoinexamschInfo extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;

    private String schoolId;
    private String examId;

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emJoinexamschInfo";
    }

}
