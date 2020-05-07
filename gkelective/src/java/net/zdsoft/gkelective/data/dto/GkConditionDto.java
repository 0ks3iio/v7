package net.zdsoft.gkelective.data.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.zdsoft.gkelective.data.entity.GkCondition;

/**
 * 组合科目
 */
public class GkConditionDto {

    private String id;
    private Set<String> subjectIds;// (科目id courseId)
    private String conditionName;
    private String gkType;
    // 显示用
    private String subjectNames;// 全称
    private String subShortNames;// 简称
    private String[] subNames;
    private String subjectIdStr;
    private int clsMax;// 最大开班数
    private int inDep = 1;// 是否独立开班
    private boolean newData;

    private String roundsId;
    private String name;
    private Integer num;// 每班人数-组合用；人数下限-单科开班用
    private Integer claNum;// 开班数
    private Integer sumNum;// 总学生人数
    private Integer maxNum;// 人数上限（单科开班用）
    private String type;
    private boolean limitSubject;
    
    public boolean isLimitSubject() {
		return limitSubject;
	}

	public void setLimitSubject(boolean limitSubject) {
		this.limitSubject = limitSubject;
	}

	private List<GkConditionDto> dtos = new ArrayList<GkConditionDto>();

    public void convertFrom(GkCondition gc){
    	if(gc == null){
    		return;
    	}
    	this.setRoundsId(gc.getRoundsId());
    	this.setClaNum(gc.getClaNum());
    	this.setId(gc.getId());
    	this.setName(gc.getName());
    	this.setNum(gc.getNum());
    	this.setRoundsId(gc.getRoundsId());
    	this.setSumNum(gc.getSumNum());
    	this.setType(gc.getType());
    	this.setGkType(gc.getGkType());
    	this.setMaxNum(gc.getMaxNum());
    }
    
    public GkCondition convertTo(GkCondition gc){
    	if(gc == null){
    		gc = new GkCondition();
    	}
    	gc.setRoundsId(this.getRoundsId());
    	gc.setClaNum(this.getClaNum());
    	gc.setId(this.getId());
    	gc.setName(this.getName());
    	gc.setNum(this.getNum());
    	gc.setRoundsId(this.getRoundsId());
    	gc.setSumNum(this.getSumNum());
    	gc.setType(this.getType());
    	gc.setGkType(this.gkType);
    	gc.setMaxNum(this.getMaxNum());
    	return gc;
    }
    
	public Integer getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}

	public List<GkConditionDto> getDtos() {
		return dtos;
	}

	public void setDtos(List<GkConditionDto> dtos) {
		this.dtos = dtos;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(Set<String> subjectIds) {
        this.subjectIds = subjectIds;
    }

    public String getSubjectNames() {
        return subjectNames;
    }

    public void setSubjectNames(String subjectNames) {
        this.subjectNames = subjectNames;
    }

    public String getSubjectIdStr() {
        return subjectIdStr;
    }

    public void setSubjectIdStr(String subjectIdStr) {
        this.subjectIdStr = subjectIdStr;
    }

    public int getClsMax() {
        return clsMax;
    }

    public void setClsMax(int clsMax) {
        this.clsMax = clsMax;
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

    public Integer getSumNum() {
        return sumNum;
    }

    public void setSumNum(Integer sumNum) {
        this.sumNum = sumNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getInDep() {
        return inDep;
    }

    public void setInDep(int inDep) {
        this.inDep = inDep;
    }

    public boolean isNewData() {
        return newData;
    }

    public String getSubShortNames() {
		return subShortNames;
	}

	public void setSubShortNames(String subShortNames) {
		this.subShortNames = subShortNames;
	}

	public void setNewData(boolean newData) {
        this.newData = newData;
    }

    public String[] getSubNames() {
        return subNames;
    }

    public void setSubNames(String[] subNames) {
        this.subNames = subNames;
    }

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public String getGkType() {
		return gkType;
	}

	public void setGkType(String gkType) {
		this.gkType = gkType;
	}

	public String getRoundsId() {
		return roundsId;
	}

	public void setRoundsId(String roundsId) {
		this.roundsId = roundsId;
	}

}
