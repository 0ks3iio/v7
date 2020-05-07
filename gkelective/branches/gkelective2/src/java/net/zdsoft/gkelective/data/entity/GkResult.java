package net.zdsoft.gkelective.data.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 新高考7选三学生信息
 * 
 * @author zhouyz
 * 
 */
@Entity
@Table(name = "gkelective_result")
public class GkResult extends BaseEntity<String> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String subjectArrangeId;
    private String subjectId;
    private String studentId;
    private Date creationTime;
    private Date modifyTime;
    private int status;//是否审核 
    @Transient
    private String remark;
    
    @Transient
    private String gkType; //A 选考  B 学考
    @Transient
    private String[] courseIds;
    @Transient
    private String[] courseNames;
    @Transient
    private Double[] courseScores;
    @Transient
    private Integer[] statues;
    @Transient
    private String[] ids;
    @Transient
    private String stucode;
    @Transient
    private String className;
    @Transient
    private String stuName;
    @Transient
    private Map<String, GkResult> subjectId2Name = new HashMap<String, GkResult>();
    @Transient
    private String stuSex;
    @Transient
    private String classId;
    
    public Double[] getCourseScores() {
		return courseScores;
	}

	public void setCourseScores(Double[] courseScores) {
		this.courseScores = courseScores;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getStuSex() {
		return stuSex;
	}

	public void setStuSex(String stuSex) {
		this.stuSex = stuSex;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
    public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
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
    
    public Integer[] getStatues() {
		return statues;
	}

	public void setStatues(Integer[] statues) {
		this.statues = statues;
	}

    @Override
    public String fetchCacheEntitName() {
        return "gkResult";
    }

    public Map<String, GkResult> getSubjectId2Name() {
        return subjectId2Name;
    }

    public void setSubjectId2Name(Map<String, GkResult> subjectId2Name) {
        this.subjectId2Name = subjectId2Name;
    }

	public String getGkType() {
		return gkType;
	}

	public void setGkType(String gkType) {
		this.gkType = gkType;
	}

}
