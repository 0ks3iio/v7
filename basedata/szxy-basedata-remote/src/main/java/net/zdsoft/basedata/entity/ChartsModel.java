package net.zdsoft.basedata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="base_charts_model")
public class ChartsModel extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	@Column(nullable = false)
	private Integer modelId;
	@Column(nullable = false)
	private Integer chartsId;
	private Integer isUsing;
	@Column(nullable = false)
	private String type;
	private String tagType;

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public Integer getChartsId() {
		return chartsId;
	}

	public void setChartsId(Integer chartsId) {
		this.chartsId = chartsId;
	}

	public Integer getIsUsing() {
		return isUsing;
	}

	public void setIsUsing(Integer isUsing) {
		this.isUsing = isUsing;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String fetchCacheEntitName() {
		return "chartsModel";
	}

}
