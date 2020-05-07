package net.zdsoft.stuwork.data.dto;

public class DyCollectDto {

	private String studentId;
	/**
	 * 品德表现登记
	 */
	private String evatr;
	/**
	 * 品德表现折分
	 */
	private Float evaScore;
	/**
	 * 学生干部折分
	 */
	private Float stuPerScore;
	/**
	 * 社团折分
	 */
	private Float groupScore;
	/**
	 * 社会实践折分
	 */
	private Float tryScore;
	/**
	 * 其它奖励
	 */
	private Float otherScore;
	/**
	 * 处罚折分
	 */
	private Float punishScore;
	//艺术节 个人和团体
	private Float yPerScore;
	private Float yGroupScore;
	//体育节 个人和团体
	private Float tPerScore;
	private Float tGroupScore;
	//外语节 个人和团体
	private Float wPerScore;
	private Float wGroupScore;
	//科技节 个人和团体
	private Float kPerScore;
	private Float kGroupScore;
	//文化节 个人和团体
	private Float wenPerScore;
	private Float wenGroupScore;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getEvatr() {
		if(evatr==null){
			return "";
		}
		return evatr;
	}

	public void setEvatr(String evatr) {
		this.evatr = evatr;
	}

	public Float getEvaScore() {
		if(evaScore==null){
			return 0.0f;
		}
		return evaScore;
	}

	public void setEvaScore(Float evaScore) {
		this.evaScore = evaScore;
	}

	public Float getStuPerScore() {
		if(stuPerScore==null){
			return 0.0f;
		}
		return stuPerScore;
	}

	public void setStuPerScore(Float stuPerScore) {
		this.stuPerScore = stuPerScore;
	}

	public Float getGroupScore() {
		if(groupScore==null){
			return 0.0f;
		}
		return groupScore;
	}

	public void setGroupScore(Float groupScore) {
		this.groupScore = groupScore;
	}

	public Float getTryScore() {
		if(tryScore==null){
			return 0.0f;
		}
		return tryScore;
	}

	public void setTryScore(Float tryScore) {
		this.tryScore = tryScore;
	}

	public Float getOtherScore() {
		if(otherScore==null){
			return 0.0f;
		}
		return otherScore;
	}

	public void setOtherScore(Float otherScore) {
		this.otherScore = otherScore;
	}

	public Float getPunishScore() {
		if(punishScore==null){
			return 0.0f;
		}
		return punishScore;
	}

	public void setPunishScore(Float punishScore) {
		this.punishScore = punishScore;
	}

	public Float getyPerScore() {
		if(yPerScore==null){
			return 0.0f;
		}
		return yPerScore;
	}

	public void setyPerScore(Float yPerScore) {
		this.yPerScore = yPerScore;
	}

	public Float getyGroupScore() {
		if(yGroupScore==null){
			return 0.0f;
		}
		return yGroupScore;
	}

	public void setyGroupScore(Float yGroupScore) {
		this.yGroupScore = yGroupScore;
	}

	public Float gettPerScore() {
		if(tPerScore==null){
			return 0.0f;
		}
		return tPerScore;
	}

	public void settPerScore(Float tPerScore) {
		this.tPerScore = tPerScore;
	}

	public Float gettGroupScore() {
		if(tGroupScore==null){
			return 0.0f;
		}
		return tGroupScore;
	}

	public void settGroupScore(Float tGroupScore) {
		this.tGroupScore = tGroupScore;
	}

	public Float getwPerScore() {
		if(wPerScore==null){
			return 0.0f;
		}
		return wPerScore;
	}

	public void setwPerScore(Float wPerScore) {
		this.wPerScore = wPerScore;
	}

	public Float getwGroupScore() {
		if(wGroupScore==null){
			return 0.0f;
		}
		return wGroupScore;
	}

	public void setwGroupScore(Float wGroupScore) {
		this.wGroupScore = wGroupScore;
	}

	public Float getkPerScore() {
		if(kPerScore==null){
			return 0.0f;
		}
		return kPerScore;
	}

	public void setkPerScore(Float kPerScore) {
		this.kPerScore = kPerScore;
	}

	public Float getkGroupScore() {
		if(kGroupScore==null){
			return 0.0f;
		}
		return kGroupScore;
	}

	public void setkGroupScore(Float kGroupScore) {
		this.kGroupScore = kGroupScore;
	}

	public Float getWenPerScore() {
		if(wenPerScore==null){
			return 0.0f;
		}
		return wenPerScore;
	}

	public void setWenPerScore(Float wenPerScore) {
		this.wenPerScore = wenPerScore;
	}

	public Float getWenGroupScore() {
		if(wenGroupScore==null){
			return 0.0f;
		}
		return wenGroupScore;
	}

	public void setWenGroupScore(Float wenGroupScore) {
		this.wenGroupScore = wenGroupScore;
	}
	
		
}
