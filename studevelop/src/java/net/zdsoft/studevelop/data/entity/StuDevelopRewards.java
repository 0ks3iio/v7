package net.zdsoft.studevelop.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="student_rewards")
public class StuDevelopRewards extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String schid;			//学校id
    private String stuid;			//学生id
    private String acadyear;
    private String semester;
    
    private String rewardsname;		//奖励名称
    private String rewardslevel;	//奖励级别码
    private String rewardstype;		//获奖类别码
    private String filecode;		//奖励文号
    private Date rewardsdate;		//获奖日期
    private Integer rewardPosition;// 获奖名次
    private String rewardsunit;		//颁奖单位
    private String rewardsreason;	//奖励原因
    private Double money;			//奖励金额
    private String remark;// 备注
    protected long updatestamp;	//更新戳
    
    //辅助字段
    @Transient
    private String stuName;

	@Override
	public String fetchCacheEntitName() {
		return "stuDevelopRewards";
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

	public String getRewardsname() {
		return rewardsname;
	}

	public void setRewardsname(String rewardsname) {
		this.rewardsname = rewardsname;
	}

	public String getRewardslevel() {
		return rewardslevel;
	}

	public void setRewardslevel(String rewardslevel) {
		this.rewardslevel = rewardslevel;
	}

	public String getRewardstype() {
		return rewardstype;
	}

	public void setRewardstype(String rewardstype) {
		this.rewardstype = rewardstype;
	}

	public String getFilecode() {
		return filecode;
	}

	public void setFilecode(String filecode) {
		this.filecode = filecode;
	}

	public Date getRewardsdate() {
		return rewardsdate;
	}

	public void setRewardsdate(Date rewardsdate) {
		this.rewardsdate = rewardsdate;
	}

	public Integer getRewardPosition() {
		return rewardPosition;
	}

	public void setRewardPosition(Integer rewardPosition) {
		this.rewardPosition = rewardPosition;
	}

	public String getRewardsunit() {
		return rewardsunit;
	}

	public void setRewardsunit(String rewardsunit) {
		this.rewardsunit = rewardsunit;
	}

	public String getRewardsreason() {
		return rewardsreason;
	}

	public void setRewardsreason(String rewardsreason) {
		this.rewardsreason = rewardsreason;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public long getUpdatestamp() {
		return updatestamp;
	}

	public void setUpdatestamp(long updatestamp) {
		this.updatestamp = updatestamp;
	}
	
}
