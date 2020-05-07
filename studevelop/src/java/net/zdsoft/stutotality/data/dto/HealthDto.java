package net.zdsoft.stutotality.data.dto;

import net.zdsoft.stutotality.data.entity.*;

import java.util.List;

public class HealthDto {

	private List<StutotalityHealthOption> optionList;

	private List<StutotalityHealth> healthList;

	private List<StutotalityReward> rewardList;

	private List<StutotalityItem> interestList;

	private List<StutotalitySchoolNotice> noticeList;
	//全优生占比
	private Float scale;
    //达标科目
    private StutotalityItem stutotalityItem;
    //达标线
    private Float score;

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Float getScale() {
        return scale;
    }

    public void setScale(Float scale) {
        this.scale = scale;
    }

    public StutotalityItem getStutotalityItem() {
        return stutotalityItem;
    }

    public void setStutotalityItem(StutotalityItem stutotalityItem) {
        this.stutotalityItem = stutotalityItem;
    }

    public List<StutotalitySchoolNotice> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<StutotalitySchoolNotice> noticeList) {
		this.noticeList = noticeList;
	}

	public List<StutotalityItem> getInterestList() {
		return interestList;
	}

	public void setInterestList(List<StutotalityItem> interestList) {
		this.interestList = interestList;
	}

	public List<StutotalityReward> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<StutotalityReward> rewardList) {
		this.rewardList = rewardList;
	}

	public List<StutotalityHealthOption> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<StutotalityHealthOption> optionList) {
		this.optionList = optionList;
	}

	public List<StutotalityHealth> getHealthList() {
		return healthList;
	}

	public void setHealthList(List<StutotalityHealth> healthList) {
		this.healthList = healthList;
	}



}
