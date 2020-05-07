package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="newgkelective_report")
public class NewGkReport extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String openAcadyear;
	private String gradeId;
	private String operator;
	private int isChosen;
	private int isDivide;
	private int isBasic;
	//分班参数
	private int xzbNumber;
	private int jxbNumber;
	private int threeStunumber;
	private int twoStunumber;
	private int noStunumber;
	//选课时间
	private Date chooseStartTime;
	private Date chooseEndTime;
	
	private Date creationTime;
	private Date modifyTime;

	@Override
	public String fetchCacheEntitName() {
		return "newGkReport";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getIsChosen() {
		return isChosen;
	}

	public void setIsChosen(int isChosen) {
		this.isChosen = isChosen;
	}

	public int getIsDivide() {
		return isDivide;
	}

	public void setIsDivide(int isDivide) {
		this.isDivide = isDivide;
	}

	public int getIsBasic() {
		return isBasic;
	}

	public void setIsBasic(int isBasic) {
		this.isBasic = isBasic;
	}

	public int getXzbNumber() {
		return xzbNumber;
	}

	public void setXzbNumber(int xzbNumber) {
		this.xzbNumber = xzbNumber;
	}

	public int getJxbNumber() {
		return jxbNumber;
	}

	public void setJxbNumber(int jxbNumber) {
		this.jxbNumber = jxbNumber;
	}

	public int getThreeStunumber() {
		return threeStunumber;
	}

	public void setThreeStunumber(int threeStunumber) {
		this.threeStunumber = threeStunumber;
	}

	public int getTwoStunumber() {
		return twoStunumber;
	}

	public void setTwoStunumber(int twoStunumber) {
		this.twoStunumber = twoStunumber;
	}

	public int getNoStunumber() {
		return noStunumber;
	}

	public void setNoStunumber(int noStunumber) {
		this.noStunumber = noStunumber;
	}

	public Date getChooseStartTime() {
		return chooseStartTime;
	}

	public void setChooseStartTime(Date chooseStartTime) {
		this.chooseStartTime = chooseStartTime;
	}

	public Date getChooseEndTime() {
		return chooseEndTime;
	}

	public void setChooseEndTime(Date chooseEndTime) {
		this.chooseEndTime = chooseEndTime;
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

	public String getOpenAcadyear() {
		return openAcadyear;
	}

	public void setOpenAcadyear(String openAcadyear) {
		this.openAcadyear = openAcadyear;
	}
	
	
}
