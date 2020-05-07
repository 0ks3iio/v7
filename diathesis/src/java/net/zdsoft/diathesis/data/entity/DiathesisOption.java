package net.zdsoft.diathesis.data.entity;

import net.zdsoft.framework.entity.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * @Author: panlf
 * @Date: 2019/3/27 17:50
 *
 *  选项值
 */
@Entity
@Table(name="newdiathesis_option")
public class DiathesisOption extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 单位id
     */
    private String unitId;
    /**
     * 列标题id
     */
    private String structureId;
    /**
     * 选项内容
     */
    @NotBlank(message = "选项名称不能为空")
    @Length(max = 10,message = "选项名称不能超过10字")
    private String contentTxt;
    /**
     * 排序号
     */
    private Integer colNo;

    /**
     * 单选分值
     */
    private String score;

    @Override
    public DiathesisOption clone() {
        DiathesisOption option = new DiathesisOption();
        option.setId(this.getId());
        option.setUnitId(this.getUnitId());
        option.setProjectId(this.getProjectId());
        option.setColNo(this.getColNo());
        option.setStructureId(this.getStructureId());
        option.setContentTxt(this.getContentTxt());
        option.setScore(this.getScore());
        return option;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public String getContentTxt() {
        return contentTxt;
    }

    public void setContentTxt(String contentTxt) {
        this.contentTxt = contentTxt;
    }
    

    public Integer getColNo() {
		return colNo;
	}

	public void setColNo(Integer colNo) {
		this.colNo = colNo;
	}

	@Override
    public String fetchCacheEntitName() {
        return "diathesisOption";
    }

}
