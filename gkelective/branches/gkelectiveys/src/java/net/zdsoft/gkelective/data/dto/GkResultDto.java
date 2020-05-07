package net.zdsoft.gkelective.data.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import net.zdsoft.gkelective.data.entity.GkResult;

public class GkResultDto {
    private GkResult gkResult;
    private List<GkResult> gkResults = new ArrayList<GkResult>();
	private String subjectArrangeId;
    private String studentId;
    private String classId;
    private Date creationTime;
    private Date modifyTime;
    
    @Transient
    private String[] courseIds;
    @Transient
    private String[] courseNames;
    @Transient
    private String stucode;
    @Transient
    private String className;
    @Transient
    private String stuName;
    
	public List<GkResult> getGkResults() {
		return gkResults;
	}

	public void setGkResults(List<GkResult> gkResults) {
		this.gkResults = gkResults;
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

    public String getSubjectArrangeId() {
        return subjectArrangeId;
    }

    public void setSubjectArrangeId(String subjectArrangeId) {
        this.subjectArrangeId = subjectArrangeId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String[] getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(String[] courseIds) {
        this.courseIds = courseIds;
    }

    public String getStucode() {
        return stucode;
    }

    public void setStucode(String stucode) {
        this.stucode = stucode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String[] getCourseNames() {
        return courseNames;
    }

    public void setCourseNames(String[] courseNames) {
        this.courseNames = courseNames;
    }

	public GkResult getGkResult() {
		return gkResult;
	}

	public void setGkResult(GkResult gkResult) {
		this.gkResult = gkResult;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
	
}
