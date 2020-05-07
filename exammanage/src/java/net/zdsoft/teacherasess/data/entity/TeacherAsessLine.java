package net.zdsoft.teacherasess.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name="teacherasess_asess_line")
public class TeacherAsessLine extends BaseEntity<String>{
	
	private String unitId;
	private String assessId;
	private String subjectId;
	private String classId;
	private String classType;
	private String className;
	private String lineName;
	private String slice;//分层班类型 0：A  1：B
	private String asessRankId;
	private String convertType;//折算分方案类型1：本次  2：参照
	private String lineType;//单上线或双上线 1:单上线  2：双上线
	private int  lineNum;//上线人数
	
	public TeacherAsessLine() {}
	
	public TeacherAsessLine(String unitId,String assessId,String subjectId,String classId
			,String classType,String className,String lineName,String slice,
			String asessRankId,String convertType,String lineType,int lineNum) {
		this.unitId = unitId;
		this.assessId = assessId;
		this.subjectId = subjectId;
		this.classId = classId;
		this.classType = classType;
		this.className = className;
		this.lineName = lineName;
		this.slice = slice;
		this.asessRankId = asessRankId;
		this.convertType = convertType;
		this.lineType = lineType;
		this.lineNum = lineNum;
	}
	
	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAssessId() {
		return assessId;
	}

	public void setAssessId(String assessId) {
		this.assessId = assessId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getSlice() {
		return slice;
	}

	public void setSlice(String slice) {
		this.slice = slice;
	}

	public String getAsessRankId() {
		return asessRankId;
	}

	public void setAsessRankId(String asessRankId) {
		this.asessRankId = asessRankId;
	}

	public String getConvertType() {
		return convertType;
	}

	public void setConvertType(String convertType) {
		this.convertType = convertType;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public int getLineNum() {
		return lineNum;
	}

	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}

	@Override
	public String fetchCacheEntitName() {
		return "teacherAsessLine";
	}

}
