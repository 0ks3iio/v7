package net.zdsoft.gkelective.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;
/**
 * 开班条件
 *@author zhouyz
 */
@Entity
@Table(name="gkelective_condition")
public class GkCondition extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roundsId;
	private String name;
	private Integer num;//每班人数-组合用；人数下限-单科开班用
	private Integer claNum;// 开班数
	private Integer sumNum;// 总学生人数
	private String type;//开班类型 1组合 0 单科
	private String gkType;// A选考B学考
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private Integer maxNum;//人数上限（单科开班用）
	
	public Integer getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getClaNum() {
		return claNum;
	}
	public void setClaNum(Integer claNum) {
		this.claNum = claNum;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRoundsId() {
		return roundsId;
	}
	public void setRoundsId(String roundsId) {
		this.roundsId = roundsId;
	}
	@Override
	public String fetchCacheEntitName() {
		return "gkCondition";
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
	public Integer getSumNum() {
		return sumNum;
	}
	public void setSumNum(Integer sumNum) {
		this.sumNum = sumNum;
	}
	public String getGkType() {
		return gkType;
	}
	public void setGkType(String gkType) {
		this.gkType = gkType;
	}
	
}
