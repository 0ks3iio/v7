package net.zdsoft.activity.dto;

import java.util.Date;

public class FamilyActureDto {
	
	private int donateMoneyCount;//捐款
	private int donateObjectCount;//捐物
	private int seekMedicalCount;//就医
	private int seekStudyCount;//求学
	private int seekEmployCount;//就业
	private int developProductCount;//发展生产
	private int otherThingCount;//其他
	private int totalCount;//
	private int ganbuCount;//干部数
	private int benefitPeopleCount;//惠及群众数
	
	private Date tempDate;
	
	public int getDonateMoneyCount() {
		return donateMoneyCount;
	}
	public void setDonateMoneyCount(int donateMoneyCount) {
		this.donateMoneyCount = donateMoneyCount;
	}
	public int getDonateObjectCount() {
		return donateObjectCount;
	}
	public void setDonateObjectCount(int donateObjectCount) {
		this.donateObjectCount = donateObjectCount;
	}
	public int getSeekMedicalCount() {
		return seekMedicalCount;
	}
	public void setSeekMedicalCount(int seekMedicalCount) {
		this.seekMedicalCount = seekMedicalCount;
	}
	public int getSeekStudyCount() {
		return seekStudyCount;
	}
	public void setSeekStudyCount(int seekStudyCount) {
		this.seekStudyCount = seekStudyCount;
	}
	public int getSeekEmployCount() {
		return seekEmployCount;
	}
	public void setSeekEmployCount(int seekEmployCount) {
		this.seekEmployCount = seekEmployCount;
	}
	public int getDevelopProductCount() {
		return developProductCount;
	}
	public void setDevelopProductCount(int developProductCount) {
		this.developProductCount = developProductCount;
	}
	public int getOtherThingCount() {
		return otherThingCount;
	}
	public void setOtherThingCount(int otherThingCount) {
		this.otherThingCount = otherThingCount;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getGanbuCount() {
		return ganbuCount;
	}
	public void setGanbuCount(int ganbuCount) {
		this.ganbuCount = ganbuCount;
	}
	public int getBenefitPeopleCount() {
		return benefitPeopleCount;
	}
	public void setBenefitPeopleCount(int benefitPeopleCount) {
		this.benefitPeopleCount = benefitPeopleCount;
	}
	public Date getTempDate() {
		return tempDate;
	}
	public void setTempDate(Date tempDate) {
		this.tempDate = tempDate;
	}

}
