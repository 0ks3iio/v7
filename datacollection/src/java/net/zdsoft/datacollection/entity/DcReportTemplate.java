package net.zdsoft.datacollection.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "dc_report_template")
public class DcReportTemplate extends BaseEntity<String> {

	@Column(length = 500)
	private String templateName;
	// 用于数据库保存
	@Column(length = 2000)
	private String templateContent;
	// 用于文件保存
	@Column(length = 500)
	private String templatePath;
	@Column(length = 500)
	private String templateImage;
	@Column(length = 1000)
	private String description;

	@Override
	public String fetchCacheEntitName() {
		return null;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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

	public String getTemplateImage() {
		return templateImage;
	}

	public void setTemplateImage(String templateImage) {
		this.templateImage = templateImage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
