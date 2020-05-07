package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectGroupColor;

/**
 * 手动安排的数据
 * @author zhouyz
 *
 */
public class NewGkGroupDto {
	
	private String conditionName;//例如政史地
	private String subjectIds;//例如政史地
	private String subjectType;
	private List<NewGkDivideClass> gkGroupClassList;
	private Integer allNumber;//总人数
	private Integer leftNumber;//剩余总人数
	private Integer maleNumber;//男生人数
	private Integer femaleNumber;//女生人数
	
	private int notexists=0;//是否不存在该组合  0:存在 1:不存在

	private List<NewGkSubjectGroupColor> colorList = new ArrayList<>();
	
	private List<DivideClassEditSaveDto> saveDto=new ArrayList<>();
	
	
	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public String getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}

	public List<NewGkDivideClass> getGkGroupClassList() {
		return gkGroupClassList;
	}

	public void setGkGroupClassList(List<NewGkDivideClass> gkGroupClassList) {
		this.gkGroupClassList = gkGroupClassList;
	}

	public Integer getAllNumber() {
		return allNumber;
	}

	public void setAllNumber(Integer allNumber) {
		this.allNumber = allNumber;
	}

	public Integer getLeftNumber() {
		return leftNumber;
	}

	public void setLeftNumber(Integer leftNumber) {
		this.leftNumber = leftNumber;
	}

	public Integer getMaleNumber() {
		return maleNumber;
	}

	public void setMaleNumber(Integer maleNumber) {
		this.maleNumber = maleNumber;
	}

	public Integer getFemaleNumber() {
		return femaleNumber;
	}

	public void setFemaleNumber(Integer femaleNumber) {
		this.femaleNumber = femaleNumber;
	}

	public int getNotexists() {
		return notexists;
	}

	public void setNotexists(int notexists) {
		this.notexists = notexists;
	}

	public List<NewGkSubjectGroupColor> getColorList() {
		return colorList;
	}

	public void setColorList(List<NewGkSubjectGroupColor> colorList) {
		this.colorList = colorList;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public List<DivideClassEditSaveDto> getSaveDto() {
		return saveDto;
	}

	public void setSaveDto(List<DivideClassEditSaveDto> saveDto) {
		this.saveDto = saveDto;
	}
	
	
}
