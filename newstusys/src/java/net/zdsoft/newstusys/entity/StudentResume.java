package net.zdsoft.newstusys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * Created by Administrator on 2018/3/1.
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "student_resume")
public class StudentResume extends BaseEntity<String> {
    @Id
    @Column(name="resumeid")
    private String id;
    private String schid;			//学校id
    private String stuid;			//学生id
    private Date startdate;			//起始日期
    private Date enddate;			//终止日期
    private String authenticator;	//证明人
    /**
     * 学校名称
     */
    private String schoolname;
    private String duty;			//所在学校担任职务
    private String remark;			//备注
    private long updatestamp;

    @Transient
    private boolean empty;
    public static void main(String[] args) {
		System.out.println("net.zdsoft.newstusys.entity.StudentResume=="+StudentResume.class.isInterface());
	}
    public boolean isEmpty(){
        if(getStartdate() == null && getEnddate()==null && StringUtils.isEmpty(getSchoolname())){
            return true;
        }
        return false;
    }
	
    public String fetchCacheEntitName() {
        return "studentResume";
    }

    public String getId() {
		return id;
	}

	public void setId(String resumeid) {
		this.id = resumeid;
	}

	public String getSchid() {
		return schid;
	}


	public void setSchid(String schid) {
		this.schid = schid;
	}


	public String getStuid() {
		return stuid;
	}


	public void setStuid(String stuid) {
		this.stuid = stuid;
	}


	public Date getStartdate() {
		return startdate;
	}


	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}


	public Date getEnddate() {
		return enddate;
	}


	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}


	public String getAuthenticator() {
		return authenticator;
	}


	public void setAuthenticator(String authenticator) {
		this.authenticator = authenticator;
	}


	public String getSchoolname() {
		return schoolname;
	}


	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}


	public String getDuty() {
		return duty;
	}


	public void setDuty(String duty) {
		this.duty = duty;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public long getUpdatestamp() {
		return updatestamp;
	}


	public void setUpdatestamp(long updatestamp) {
		this.updatestamp = updatestamp;
	}
    
}
