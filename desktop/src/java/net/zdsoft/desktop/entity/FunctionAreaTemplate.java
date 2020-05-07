package net.zdsoft.desktop.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author shenke
 * @since 2017/2/6 9:40
 */
@Entity
@Table(name = "desktop_function_area_template")
public class FunctionAreaTemplate extends BaseEntity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String fetchCacheEntitName() {
		return this.getClass().getSimpleName();
	}

	private String templateContent;
	private String templatePath;
	private Integer fixedColumns;
	private Date modifyTime;
	private String templateName;
	private String imageUrl;

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public Integer getFixedColumns() {
		return fixedColumns;
	}

	public void setFixedColumns(Integer fixedColumns) {
		this.fixedColumns = fixedColumns;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
}
