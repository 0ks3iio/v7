package net.zdsoft.desktop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "desktop_function_area_user")
public class FunctionAreaUser extends BaseEntity<String> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Integer LAYOUT_TYPE_RIGHT = 1;
	public static final Integer LAYOUT_TYPE_LEFT = 2;
	public static final Integer LAYOUT_TYPE_MIDDLE = 3;

	/**
	 * 状态 正常
	 */
	public static final Integer STATE_NORMAL = 1;

	public static final String STATE_NORMAL_STRING = "1";

	/**
	 * 状态 异常
	 */
	public static final Integer STATE_ILLEGAL = -1;

	public static final String STATE_ILLEGAL_STRING = "-1";

	@Override
	public String fetchCacheEntitName() {
		return "functionAreaUser";
	}

	@Column(length = 32, nullable = false)
	private String functionAreaId;
	@Column(length = 32, nullable = false)
	private String customerId;
	private Integer customerType;
	private Integer state;
	/**
	 * 按照Bootstrap的12等分进行设置，最大值为12，最小值为1
	 */
	private Integer columns;
	/**
	 * 高度设置，一般是图表才需要
	 */
	private Integer height;
	private Integer displayOrder;

	//所属布局类型 2
	private Integer layoutType;
	//动态参数
	//@Transient
	private String dynamicParam;

	public String getFunctionAreaId() {
		return functionAreaId;
	}

	public void setFunctionAreaId(String functionAreaId) {
		this.functionAreaId = functionAreaId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getColumns() {
		return columns;
	}

	public void setColumns(Integer columns) {
		this.columns = columns;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(Integer layoutType) {
		this.layoutType = layoutType;
	}

	public String getDynamicParam() {
		return dynamicParam;
	}

	public void setDynamicParam(String dynamicParam) {
		this.dynamicParam = dynamicParam;
	}

}
