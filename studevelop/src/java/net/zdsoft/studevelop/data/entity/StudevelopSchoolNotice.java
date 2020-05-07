package net.zdsoft.studevelop.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/8.
 */
@Entity
@Table(name="studevelop_school_notice")
public class StudevelopSchoolNotice extends BaseEntity<String> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2765782641158014425L;
	private String unitId;
    private String acadyear;
    private String semester;
    /**
     * 学段
     */
    private String schoolSection;
    /**
     * 假期注意事项
     */
    private String notice;
    private Date creationTime;
    private Date modifyTime;
    /**
     * 实际上课天数
     */
    private Float studyDate;
    private String registerBegin;//注册日期
	private String studyBegin;//下学期正式上课日期

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getAcadyear() {
        return acadyear;
    }

    public void setAcadyear(String acadyear) {
        this.acadyear = acadyear;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSchoolSection() {
        return schoolSection;
    }

    public void setSchoolSection(String schoolSection) {
        this.schoolSection = schoolSection;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
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

    @Override
    public String fetchCacheEntitName() {

        return "studevelopSchoolNotice";
    }


	public Float getStudyDate() {
		return studyDate;
	}

	public void setStudyDate(Float studyDate) {
		this.studyDate = studyDate;
	}

	public String getRegisterBegin() {
		return registerBegin;
	}

	public void setRegisterBegin(String registerBegin) {
		this.registerBegin = registerBegin;
	}

	public String getStudyBegin() {
		return studyBegin;
	}

	public void setStudyBegin(String studyBegin) {
		this.studyBegin = studyBegin;
	}


}
