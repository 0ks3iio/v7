package net.zdsoft.bigdata.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
/**
 * 组合报表明细（包括看板和报告）
 * @author user
 *
 */
@Entity
@Table(name = "bg_multi_report_detail")
public class MultiReportDetail extends BaseEntity<String> {

	private static final long serialVersionUID = 7929408922173587799L;

	private String reportId;
	
	private String businessId;
	
	private String businessType;//业务类型 图表 多维 报表 富文本  大屏中保存的组件
	
	private String businessName;
	
	private String width;
	
	private String height;
	
	private int orderId;
	
	private String content;
	
	@Override
	public String fetchCacheEntitName() {
		return "multiReportDetail";
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
