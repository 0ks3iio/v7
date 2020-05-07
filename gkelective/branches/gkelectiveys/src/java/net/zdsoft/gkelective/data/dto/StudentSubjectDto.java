package net.zdsoft.gkelective.data.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.gkelective.data.entity.GkResult;
/**
 * 
 * 学生选课情况
 */
public class StudentSubjectDto implements Cloneable{

	/**算法需要用到的值开始-----------------------**/
    private String stuId;
    private String stuCode;
    private String stuName;
	private int sex;
	private String classId; 
	private String className;
	private Combined combined;
	 /* 所有走班科目 */
    private List<String> allSubjectIds;
    /* 选的3门科目 */
    private Set<String> chooseSubjectIds;
    private Map<String,Double> scoreMap;//成绩key:subjectId
    private double avgScore;//组合内，单科排序 以选的三门成绩平均分  //预排自动分配：选择三门总分
    // batch,subjectId,teaClsId
    private Set<String> oldSubjectIds;
    
    //private Set<Integer> noBathSet;//不能排的批次（用于单科排班）
    /**算法需要用到的值结束---------------------**/
	
    private String arrangeId;
   // private GkConditionDto gkConditionDto;
    
    /* 选的走班科目 */
    private List<String> subjectIds;
    private String teaClsId1;
    private String teaClsId2;
    private String teaClsId3;
    private String courseId1;
    private String courseId2;
    private String courseId3;
    
	public String getStuCode() {
		return stuCode;
	}

	public void setStuCode(String stuCode) {
		this.stuCode = stuCode;
	}

	public String getTeaClsId1() {
		return teaClsId1;
	}

	public void setTeaClsId1(String teaClsId1) {
		this.teaClsId1 = teaClsId1;
	}

	public String getTeaClsId2() {
		return teaClsId2;
	}

	public void setTeaClsId2(String teaClsId2) {
		this.teaClsId2 = teaClsId2;
	}

	public String getTeaClsId3() {
		return teaClsId3;
	}

	public void setTeaClsId3(String teaClsId3) {
		this.teaClsId3 = teaClsId3;
	}

	public String getCourseId1() {
		return courseId1;
	}

	public void setCourseId1(String courseId1) {
		this.courseId1 = courseId1;
	}

	public String getCourseId2() {
		return courseId2;
	}

	public void setCourseId2(String courseId2) {
		this.courseId2 = courseId2;
	}

	public String getCourseId3() {
		return courseId3;
	}

	public void setCourseId3(String courseId3) {
		this.courseId3 = courseId3;
	}

	public String getStuId() {
		return stuId;
	}

	public void setStuId(String stuId) {
		this.stuId = stuId;
	}

	/*public GkConditionDto getGkConditionDto() {
		return gkConditionDto;
	}

	public void setGkConditionDto(GkConditionDto gkConditionDto) {
		this.gkConditionDto = gkConditionDto;
	}*/

	/**
     * 获取subjectIds
     * @return subjectIds
     */
    public List<String> getSubjectIds() {
        return subjectIds;
    }

    /**
     * 设置subjectIds
     * @param subjectIds subjectIds
     */
    public void setSubjectIds(List<String> subjectIds) {
        this.subjectIds = subjectIds;
    }

	public String getArrangeId() {
		return arrangeId;
	}

	public void setArrangeId(String arrangeId) {
		this.arrangeId = arrangeId;
	}
	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public static List<GkResult> toEntity(StudentSubjectDto dto) {
		List<GkResult> gkResult = new ArrayList<GkResult>();
		GkResult g1 = new GkResult();
		g1.setCreationTime(new Date());
		g1.setModifyTime(new Date());
		g1.setStudentId(dto.getStuId());
		g1.setSubjectId(dto.getCourseId1());
		g1.setSubjectArrangeId(dto.getArrangeId());
		gkResult.add(g1);
		
		GkResult g2 = new GkResult();
		g2.setCreationTime(new Date());
		g2.setModifyTime(new Date());
		g2.setStudentId(dto.getStuId());
		g2.setSubjectId(dto.getCourseId2());
		g2.setSubjectArrangeId(dto.getArrangeId());
		gkResult.add(g2);
		
		GkResult g3 = new GkResult();
		g3.setCreationTime(new Date());
		g3.setModifyTime(new Date());
		g3.setStudentId(dto.getStuId());
		g3.setSubjectId(dto.getCourseId3());
		g3.setSubjectArrangeId(dto.getArrangeId());
		gkResult.add(g3);
		return gkResult;
	}


	public Combined getCombined() {
		return combined;
	}

	public void setCombined(Combined combined) {
		this.combined = combined;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public List<String> getAllSubjectIds() {
		return allSubjectIds;
	}

	public void setAllSubjectIds(List<String> allSubjectIds) {
		this.allSubjectIds = allSubjectIds;
	}

	public Set<String> getChooseSubjectIds() {
		return chooseSubjectIds;
	}

	public void setChooseSubjectIds(Set<String> chooseSubjectIds) {
		this.chooseSubjectIds = chooseSubjectIds;
	}

//	public Set<Integer> getNoBathSet() {
//		return noBathSet;
//	}
//
//	public void setNoBathSet(Set<Integer> noBathSet) {
//		this.noBathSet = noBathSet;
//	}

	public Set<String> getOldSubjectIds() {
		return oldSubjectIds;
	}

	public void setOldSubjectIds(Set<String> oldSubjectIds) {
		this.oldSubjectIds = oldSubjectIds;
	}

	public Map<String, Double> getScoreMap() {
		return scoreMap;
	}

	public void setScoreMap(Map<String, Double> scoreMap) {
		this.scoreMap = scoreMap;
	}

	public double getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(double avgScore) {
		this.avgScore = avgScore;
	}
	
	 public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public StudentSubjectDto clone() throws CloneNotSupportedException {
		 StudentSubjectDto student = (StudentSubjectDto)super.clone();
    	if (this.allSubjectIds != null) {
    		student.setAllSubjectIds((ArrayList<String>)((ArrayList<String>)allSubjectIds).clone());
    	}
		return student;
	}
}
