package net.zdsoft.newstusys.businessimport.core;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.newstusys.businessimport.exception.ConfigurationException;

public class ImportObjectNode implements Cloneable {
	private static final Logger log = LoggerFactory
			.getLogger(ImportObjectNode.class);

	private String define;// 列名中文
	private String name;// 列名标识
	private String unique;// 是否唯一值
	private String required;// 是否必须
	private String exported;// 是否导出,只做导出，不验证
	private String primary; // 是否非空且唯一
	private String type;// 类型
	private String example;// 示例说明
	private int strLength = 0;// String类型的长度
	// 类型为数字型的整数长度
	private int precision;
	// 类型为数字型的小数长度
	private int decimal;
	// 类型为数字型的 是否非负
	private String nonnegative;

	private String mcode; // 微代码
	private String regex;
	private String errorMsg;
	private boolean dynamic;// 是否为动态的
	private String dbrequired; // 如果有该列，则不能为空
	private String defaultValue;// 默认值
	private String dbname;// 数据库字段
	private int displayOrder;// 排序号

	public static int minYear = 1950;
	public static int maxYear = 2020;

	private String nowvalue;

	// 只适用于什么系统,如,家校互联 etoh2
	private String onlyFor;
	private String unionPrimary;

	private boolean checked;// 是否选中
	private boolean disabled;// 是否不可修改

	private String caseSensitive;// 是否大小写敏感
	private boolean defaultChecked;// 下载模板时是否默认选中
	/** 自定义列的待选值列表 by zhangkc 2013-10-22 */
	private String[] selectItems;
	/** 字段类型自定义列的待选值列表*/
	public static final String FIELD_TYPE_SELECT = "Select";
	
	public ImportObjectNode() {

	}

	/**
	 * 解析结点
	 * 
	 * @param fieldElement
	 * @throws ConfigurationException
	 */
	public ImportObjectNode(Element fieldElement) throws ConfigurationException {
		try {
			this.define = fieldElement.attributeValue("define");
			this.name = fieldElement.attributeValue("name");

			this.primary = fieldElement.attributeValue("primary");
			if (primary == null)
				primary = "N";

			this.required = fieldElement.attributeValue("required");
			if (required == null)
				required = "N";
			
			this.exported = fieldElement.attributeValue("exported");
			if (exported == null)
				exported = "N";

			this.dbrequired = fieldElement.attributeValue("dbrequired");

			this.unique = fieldElement.attributeValue("unique");
			if (unique == null)
				unique = "N";

			String _tmp = fieldElement.attributeValue("displayOrder");
			if (StringUtils.isNotBlank(_tmp)) {
				displayOrder = Integer.parseInt(_tmp);
			}

			this.type = fieldElement.attributeValue("type");
			if (type == null) {
				type = "String";
				strLength = 0;
			}

			if (type.indexOf("(") > 0) {
				String fieldtype = type.substring(0, type.indexOf("("));
				String lengthInfo = type.substring(type.indexOf("(") + 1,
						type.indexOf(")"));
				type = fieldtype;
				if (type.equalsIgnoreCase("String")) {
					this.strLength = Integer.parseInt(lengthInfo);
				} else if (type.equalsIgnoreCase("Numeric")
						|| type.equalsIgnoreCase("NonNegativeNumeric")) {
					if (lengthInfo.indexOf(",") > 0) {
						this.precision = Integer.parseInt(lengthInfo.substring(
								0, lengthInfo.indexOf(",")));
						this.decimal = Integer.parseInt(lengthInfo
								.substring(lengthInfo.indexOf(",") + 1));
					} else {
						this.precision = Integer.parseInt(lengthInfo);
						this.decimal = 0;
					}
				}
			} else {
				if (null == fieldElement.attributeValue("stringLength")) {
					this.strLength = 0;
				} else {
					this.strLength = Integer.parseInt(fieldElement
							.attributeValue("stringLength"));
				}
				if (type.indexOf("Numeric") == 0) {
					if (null == fieldElement.attributeValue("precision")) {
						precision = 0;
					} else {
						this.precision = Integer.parseInt(fieldElement
								.attributeValue("precision"));
					}
					if (null == fieldElement.attributeValue("precision")) {
						decimal = 0;
					} else {
						this.decimal = Integer.parseInt(fieldElement
								.attributeValue("fraction"));
					}
					if (null == fieldElement.attributeValue("nonnegative")) {
						nonnegative = "N";
					} else {
						this.nonnegative = fieldElement
								.attributeValue("nonnegative");
					}
				}
			}

			this.example = fieldElement.attributeValue("example");
			if (example == null)
				example = "";

			this.mcode = fieldElement.attributeValue("mcode");
			if (mcode == null)
				mcode = "";

			this.regex = fieldElement.attributeValue("regex");
			this.errorMsg = fieldElement.attributeValue("errorMsg");
			this.defaultValue = fieldElement.attributeValue("defaultValue");

			this.dbname = fieldElement.attributeValue("dbname");
			if (null == dbname)
				dbname = name;

			this.unionPrimary = fieldElement.attributeValue("unionPrimary");

			this.nowvalue = fieldElement.attributeValue("nowvalue");
			if (nowvalue == null)
				nowvalue = "";

			onlyFor = fieldElement.attributeValue("only-for");

			this.caseSensitive = fieldElement.attributeValue("caseSensitive");
			if (caseSensitive == null)
				caseSensitive = "N";

			String dc = fieldElement.attributeValue("defaultChecked");
			if (dc == null) {
				dc = "N";
			}
			if ("Y".equalsIgnoreCase(dc)) {
				this.defaultChecked = true;
			} else {
				this.defaultChecked = false;
			}

		} catch (Exception ex) {
			throw new ConfigurationException("配置文件中的列【" + this.define
					+ "】配置有误！" + ex.getMessage());
		}
	}

	/**
	 * 对字段的一些施加约束
	 * 
	 * @param isUpdate
	 *            如果只是以业务主键进行更新，则required='Y'时表示required='N',
	 *            required='N'。primary不能为空
	 * @param i
	 * @param fieldCustomMap
	 */
	public ImportObjectNode wrappedNode(boolean isUpdate, int i,
			Map<String, ImportObjectNode> fieldCustomMap, Map<String, String[]> selectFieldMap) {
		ImportObjectNode node = null;
		try {
			node = this.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.toString());
		}

		if (node.displayOrder == 0) {
			node.displayOrder = i;
		}

		if (isUpdate) {
			if ("Y".equals(node.primary)) {
				node.required = "Y";
			} else {
				node.required = "N";
			}
		}

		// 模板下载时使用
		if ("Y".equals(node.required) || "Y".equals(node.exported)) {
			node.checked = true;
			node.disabled = true;
		}
		
		// 下拉框
		if(ImportObjectNode.FIELD_TYPE_SELECT.equals(node.type) && MapUtils.isNotEmpty(selectFieldMap)){
			node.selectItems = selectFieldMap.get(node.getDefine());
		}
		
		// 用户自定义的字段属性覆盖根据xml配置，目前主要提供checked和disabled
		if (null != fieldCustomMap) {
			ImportObjectNode customNode = fieldCustomMap.get(node.name);
			if (null != customNode) {
				node.checked = customNode.isChecked();
				node.disabled = customNode.isDisabled();

				if (node.checked) {
					node.required = "Y";// 必填，在导入时使用
				}
			}
		}

		if (node.dbrequired == null) {
			node.dbrequired = node.required;// 如果为空，则取required,目的：兼容旧的配置
		}

		return node;
	}

	public boolean verifyType(String value) {
		if (value == null || value.trim().equals(""))
			return true;
		if (type != null) {
			try {
				if (type.equalsIgnoreCase("String")) {
					if (strLength == 0)
						return true;
					if (Validators.isString(value, 0, strLength))
						return true;
					else
						return false;
				} else if (type.equalsIgnoreCase("Integer")
						|| type.equalsIgnoreCase("Long")) {
					if (Validators.isNumber(value))
						return true;
					else
						return false;
				} else if (type.equalsIgnoreCase("Datetime")) {
					if (isDateTime(value))
						return true;
					else
						return false;
				} else if (type.equalsIgnoreCase("Date")) {
					if (isDate(value))
						return true;
					else
						return false;
				} else if (type.equalsIgnoreCase("Timestamp")) {
					if (Validators.isTime(value))
						return true;
					else
						return false;
				} else if (type.indexOf("Numeric") == 0) {
					if (!Validators.isNumeric(value, decimal)) {
						return false;
					}
					if (value.indexOf(".") >= 0) {
						if (value.substring(0, value.indexOf(".")).length() > precision) {
							return false;
						}
					}
					return true;

				}
			} catch (Exception ex) {
				return false;
			}
		}
		return false;
	}

	public boolean isDateTime(String str) {
		if (isEmpty(str) || str.length() > 20) {
			return false;
		}

		String[] items = str.split(" ");

		if (items.length != 2) {
			return false;
		}

		return isDate(items[0]) && Validators.isTime(items[1]);
	}

	public static boolean isNumber(String str, int min, int max) {
		if (!Validators.isNumber(str)) {
			return false;
		}

		int number = Integer.parseInt(str);
		return number >= min && number <= max;
	}

	public boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public boolean isDate(String str) {
		if (isEmpty(str) || str.length() > 10) {
			return false;
		}

		String[] items = str.split("-");

		if (items.length != 3) {
			return false;
		}

		if (!isNumber(items[0], minYear, maxYear) || !isNumber(items[1], 1, 12)) {
			return false;
		}

		int year = Integer.parseInt(items[0]);
		int month = Integer.parseInt(items[1]);

		return isNumber(items[2], 1,
				DateUtils.getMaxDayOfMonth(year, month - 1));
	}

	@Override
	protected ImportObjectNode clone() throws CloneNotSupportedException {
		return (ImportObjectNode) super.clone();
	}

	/**
	 * @return the define
	 */
	public String getDefine() {
		return define;
	}

	/**
	 * @param define
	 *            the define to set
	 */
	public void setDefine(String define) {
		this.define = define;
	}

	/**
	 * @return the example
	 */
	public String getExample() {
		return example;
	}

	/**
	 * @param example
	 *            the example to set
	 */
	public void setExample(String example) {
		this.example = example;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the required
	 */
	public String getRequired() {
		return required;
	}

	/**
	 * @param required
	 *            the required to set
	 */
	public void setRequired(String required) {
		this.required = required;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the unique
	 */
	public String getUnique() {
		return unique;
	}

	/**
	 * @param unique
	 *            the unique to set
	 */
	public void setUnique(String unique) {
		this.unique = unique;
	}

	/**
	 * @return the strLength
	 */
	public int getStrLength() {
		return strLength;
	}

	/**
	 * @param strLength
	 *            the strLength to set
	 */
	public void setStrLength(int strLength) {
		this.strLength = strLength;
	}

	/**
	 * @return the decimal
	 */
	public int getDecimal() {
		return decimal;
	}

	/**
	 * @param decimal
	 *            the decimal to set
	 */
	public void setDecimal(int decimal) {
		this.decimal = decimal;
	}

	/**
	 * @return the precision
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * @param precision
	 *            the precision to set
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public String getNowvalue() {
		return nowvalue;
	}

	public void setNowvalue(String nowvalue) {
		this.nowvalue = nowvalue;
	}

	public String getOnlyFor() {
		return onlyFor;
	}

	public void setOnlyFor(String onlyFor) {
		this.onlyFor = onlyFor;
	}

	public String getDbrequired() {
		return dbrequired;
	}

	public void setDbrequired(String dbrequired) {
		this.dbrequired = dbrequired;
	}

	public String getMcode() {
		return mcode;
	}

	public void setMcode(String mcode) {
		this.mcode = mcode;
	}

	/**
	 * 获取exported
	 * @return exported
	 */
	public String getExported() {
	    return exported;
	}

	/**
	 * 设置exported
	 * @param exported exported
	 */
	public void setExported(String exported) {
	    this.exported = exported;
	}

	public String getPrimary() {
		return primary;
	}

	public void setPrimary(String primary) {
		this.primary = primary;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getUnionPrimary() {
		return unionPrimary;
	}

	public void setUnionPrimary(String unionPrimary) {
		this.unionPrimary = unionPrimary;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(String caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean isDefaultChecked() {
		return defaultChecked;
	}

	public void setDefaultChecked(boolean defaultChecked) {
		this.defaultChecked = defaultChecked;
	}

	public String getNonnegative() {
		return nonnegative;
	}

	public void setNonnegative(String nonnegative) {
		this.nonnegative = nonnegative;
	}

	public String[] getSelectItems() {
		return selectItems;
	}

	public void setSelectItems(String[] selectItems) {
		this.selectItems = selectItems;
	}


}
