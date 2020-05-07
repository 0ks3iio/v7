package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "exammanage_option_school")
public class EmOptionSchool extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;

    private String joinSchoolId;
    private int joinStudentCount;
    private String optionId;
    private String examId;
    @Transient
    private String optionName;

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


    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getJoinSchoolId() {
        return joinSchoolId;
    }

    public void setJoinSchoolId(String joinSchoolId) {
        this.joinSchoolId = joinSchoolId;
    }

    public int getJoinStudentCount() {
        return joinStudentCount;
    }

    public void setJoinStudentCount(int joinStudentCount) {
        this.joinStudentCount = joinStudentCount;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emOptionSchool";
    }

}
