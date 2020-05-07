package net.zdsoft.tutor.data.dto;

import net.zdsoft.tutor.data.entity.TutorRound;

/**
 * @author yangsj  2017年9月11日下午7:19:46
 * 
 */
public class TutorSetupDto {
    
	private TutorRound tutorRound;
	private Integer canChooseTeaN; 
	private Integer selectedStuN;
	private Integer noSelectedStuN;
	
	private Integer state;  //当前的状态 -1 -- 已结束  0 -- 正在进行  1 -- 未开始

	public TutorRound getTutorRound() {
		return tutorRound;
	}

	public void setTutorRound(TutorRound tutorRound) {
		this.tutorRound = tutorRound;
	}

	

	public Integer getCanChooseTeaN() {
		return canChooseTeaN;
	}

	public void setCanChooseTeaN(Integer canChooseTeaN) {
		this.canChooseTeaN = canChooseTeaN;
	}

	public Integer getSelectedStuN() {
		return selectedStuN;
	}

	public void setSelectedStuN(Integer selectedStuN) {
		this.selectedStuN = selectedStuN;
	}

	public Integer getNoSelectedStuN() {
		return noSelectedStuN;
	}

	public void setNoSelectedStuN(Integer noSelectedStuN) {
		this.noSelectedStuN = noSelectedStuN;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	
	
	
	
	
	
}
