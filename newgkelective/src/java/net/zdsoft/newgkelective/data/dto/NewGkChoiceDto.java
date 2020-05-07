package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkChoResult;


public class NewGkChoiceDto {
    private List<ChoiceCourseDto> choiceCourseDtoList = new ArrayList<ChoiceCourseDto>();

	private List<CourseCategoryDto> courseCategoryDtoList = new ArrayList<>();// 向页面输出课程列表

	private List<ChoiceSubjectDto> banCourseList = new ArrayList<>(); // 禁选列表

	private List<ChoiceSubjectDto> recommendList = new ArrayList<>(); // 推荐列表

	private Integer selectNum;// 选课人数

	private Integer noSelectNum; // 未选课人数

	private String choiceId; // 选课Id

	private String gradeId; // 年级Id

	private Integer chooseNum; // 选择科目数量

	private Date startTime; // 开始时间

	private Date endTime; // 结束时间

	private String notice; // 选课公告

	private String choiceName; // 选课名称

	private String jsonObject; // 选课结果图标需要使用
	private boolean isDefault;
	private int timeState;
	private boolean warning;
	
	private Integer statShow;//选课结果是否开放
    private Integer noticeShow;// 选课提示是否开放
	private Integer showNum;//人以下的选课组合进行提示
	private Date showTime;//提示开始时间
	private Integer showSamesele;
	private String referScoreId;
	private String hintContent;//提示文字
	
	//学生端展示选课内容
	private List<NewGkChoResult> resultList = new ArrayList<NewGkChoResult>();//已选课程
	private List<String> wantToSubjectList = new ArrayList<String>();//优先调剂到
	private List<String> noWantToSubjectList = new ArrayList<String>();//明确不选

	public String getHintContent() {
		return hintContent;
	}

	public void setHintContent(String hintContent) {
		this.hintContent = hintContent;
	}

	public Integer getStatShow() {
		return statShow;
	}

	public void setStatShow(Integer statShow) {
		this.statShow = statShow;
	}

	public Integer getShowNum() {
		return showNum;
	}

	public void setShowNum(Integer showNum) {
		this.showNum = showNum;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public String getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}

	public String getChoiceName() {
		return choiceName;
	}

	public Integer getSelectNum() {
		return selectNum;
	}

	public void setSelectNum(Integer selectNum) {
		this.selectNum = selectNum;
	}

	public Integer getNoSelectNum() {
		return noSelectNum;
	}

	public void setNoSelectNum(Integer noSelectNum) {
		this.noSelectNum = noSelectNum;
	}

	public void setChoiceName(String choiceName) {
		this.choiceName = choiceName;
	}

	public String getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(String choiceId) {
		this.choiceId = choiceId;
	}

	public List<ChoiceSubjectDto> getBanCourseList() {
		return banCourseList;
	}

	public void setBanCourseList(List<ChoiceSubjectDto> banCourseList) {
		this.banCourseList = banCourseList;
	}

	public List<ChoiceSubjectDto> getRecommendList() {
		return recommendList;
	}

	public void setRecommendList(List<ChoiceSubjectDto> recommendList) {
		this.recommendList = recommendList;
	}

    public List<CourseCategoryDto> getCourseCategoryDtoList() {
        return courseCategoryDtoList;
    }

    public void setCourseCategoryDtoList(List<CourseCategoryDto> courseCategoryDtoList) {
        this.courseCategoryDtoList = courseCategoryDtoList;
    }

    public List<ChoiceCourseDto> getChoiceCourseDtoList() {
        return choiceCourseDtoList;
    }

    public void setChoiceCourseDtoList(List<ChoiceCourseDto> choiceCourseDtoList) {
        this.choiceCourseDtoList = choiceCourseDtoList;
    }

    public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public Integer getChooseNum() {
		return chooseNum;
	}

	public void setChooseNum(Integer chooseNum) {
		this.chooseNum = chooseNum;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public int getTimeState() {
		return timeState;
	}

	public void setTimeState(int timeState) {
		this.timeState = timeState;
	}

	public boolean isWarning() {
		return warning;
	}

	public void setWarning(boolean warning) {
		this.warning = warning;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

    public Integer getNoticeShow() {
        return noticeShow;
    }

    public void setNoticeShow(Integer noticeShow) {
        this.noticeShow = noticeShow;
    }

	public List<NewGkChoResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<NewGkChoResult> resultList) {
		this.resultList = resultList;
	}

	public List<String> getWantToSubjectList() {
		return wantToSubjectList;
	}

	public void setWantToSubjectList(List<String> wantToSubjectList) {
		this.wantToSubjectList = wantToSubjectList;
	}

	public List<String> getNoWantToSubjectList() {
		return noWantToSubjectList;
	}

	public void setNoWantToSubjectList(List<String> noWantToSubjectList) {
		this.noWantToSubjectList = noWantToSubjectList;
	}

	public String getReferScoreId() {
		return referScoreId;
	}

	public void setReferScoreId(String referScoreId) {
		this.referScoreId = referScoreId;
	}

	public Integer getShowSamesele() {
		return showSamesele;
	}

	public void setShowSamesele(Integer showSamesele) {
		this.showSamesele = showSamesele;
	}

	@Override
	public String toString() {
		return "NewGkChoiceDto{" +
				"choiceCourseDtoList=" + choiceCourseDtoList +
				", courseCategoryDtoList=" + courseCategoryDtoList +
				", banCourseList=" + banCourseList +
				", recommendList=" + recommendList +
				", selectNum=" + selectNum +
				", noSelectNum=" + noSelectNum +
				", choiceId='" + choiceId + '\'' +
				", gradeId='" + gradeId + '\'' +
				", chooseNum=" + chooseNum +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", notice='" + notice + '\'' +
				", choiceName='" + choiceName + '\'' +
				", jsonObject='" + jsonObject + '\'' +
				", isDefault=" + isDefault +
				", timeState=" + timeState +
				", warning=" + warning +
				", statShow=" + statShow +
				", noticeShow=" + noticeShow +
				", showNum=" + showNum +
				", showTime=" + showTime +
				", showSamesele=" + showSamesele +
				", referScoreId='" + referScoreId + '\'' +
				", hintContent='" + hintContent + '\'' +
				", resultList=" + resultList +
				", wantToSubjectList=" + wantToSubjectList +
				", noWantToSubjectList=" + noWantToSubjectList +
				'}';
	}
}
