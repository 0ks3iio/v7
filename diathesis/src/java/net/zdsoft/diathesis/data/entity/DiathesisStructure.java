package net.zdsoft.diathesis.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author: panlf
 * @Date: 2019/3/27 18:04
 */
@Entity
@Table(name="newdiathesis_structure")
public class DiathesisStructure extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	
	private String unitId;
    private String projectId;
    /**
     * 行号
     */
    private Integer colNo;
    /**
     * 标题
     */
    private String title;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 是否显示 0: 可以不显示  1:必须显示
     */
    private Integer isShow;

    /**
     * 是否必填  0：非必填  1：必填
     */
    private Integer isMust;
    /**
     * 备注
     */
    private String remark;

    /**
     * 是否统计
     */
    private Integer isCount;

   /* @Transient
    private List<DiathesisOption> optionList;


    public List<DiathesisOption> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<DiathesisOption> optionList) {
        this.optionList = optionList;
    }*/

    public Integer getIsCount() {
        return isCount;
    }

    public void setIsCount(Integer isCount) {
        this.isCount = isCount;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getColNo() {
        return colNo;
    }

    public void setColNo(Integer colNo) {
        this.colNo = colNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIsMust() {
		return isMust;
	}

	public void setIsMust(Integer isMust) {
		this.isMust = isMust;
	}

	@Override
    public DiathesisStructure clone()  {

        DiathesisStructure structure = new DiathesisStructure();
        structure.setId(this.getId());
        structure.setUnitId(this.getUnitId());
        structure.setProjectId(this.getProjectId());
        structure.setColNo(this.getColNo());
        structure.setTitle(this.getTitle());
        structure.setDataType(this.getDataType());
        structure.setIsShow(this.getIsShow());
        structure.setRemark(this.getRemark());
        structure.setIsMust(this.getIsMust());
        return structure;
    }

    @Override
    public String fetchCacheEntitName() {
        return "diathesisStructure";
    }
}
