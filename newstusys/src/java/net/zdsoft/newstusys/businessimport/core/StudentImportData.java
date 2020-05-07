package net.zdsoft.newstusys.businessimport.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.newstusys.businessimport.common.DataImportConstants;
import net.zdsoft.newstusys.businessimport.file.ImportFileParser;
import net.zdsoft.newstusys.businessimport.file.ImportFileParserFactory;
import net.zdsoft.newstusys.businessimport.file.ImportFileParserParam;
import net.zdsoft.newstusys.businessimport.param.DataImportParam;
import net.zdsoft.newstusys.businessimport.service.RegionUtils;

public class StudentImportData {
	private static final Logger log = LoggerFactory.getLogger(StudentImportData.class);
	// 替换内容
	private static final String REPLQCE_STRING = "$COLUMNNAME$";

	public static final int minYear = 1900;
	public static final int maxYear = 3000;
	public static int COUNT_BY_PER_TIME = 1000;// 一次处理的记录数

	private Map<String, Map<String, String>> selectMap = new HashMap<String, Map<String, String>>();

	// -----------------------------参数---------------------------
	private String configFilePath;
	private DataImportParam param;
	private String importDataFilePath;// 导入文件的地址

	// -----------------------------配置文件---------------------------
	private String entityClassName; // 配置文件中的object对应的对象类名
	private String xmlObjecDefine;// 配置文件中对象名
	private String xlsSheetName;// 读取xls中的sheet的名称，如果不存在，则默认第1个

	// 监听器列表
	private List<String> listeners = null;

	// node属性
	private Map<String, ImportObjectNode> nameNodeMap;
	private Map<String, ImportObjectNode> defineNodeMap;

	private List<String> dynamicFieldList = new ArrayList<String>();// 动态字段
	private List<String> requiredDefineList;// 必填字段

	// -----------------------------xls数据内容------------------------------

	private String objectDefine;// object名称，导入文件的名称，如：职工基本信息
	private int beginRow = 0;// 列标题开始行
	private int totalRows = 0;// 数据记录数

	// 导入文件的列名称，为了保持一直，列的长度比实际的列多2，可以为空
	private List<String> fileDefineList;
	private List<String> fileNameList;

	// 业务主键(或关联字段业务主键)，以判断文件中的业务主键是否重复
	private Map<String, Integer> primaryMap = new HashMap<String, Integer>();
	private Map<String, List<String>> uniqueDataMap = new HashMap<String, List<String>>();

	// 导入数据的内容，比导入数据多两列，数据的最后第二列保存有错误数据的列名，数据的最后一列保存错误信息，如果为空，或者空串，则表示数据正确
	private List<Object> importDataObjectList = new ArrayList<Object>();// 对象形式的导入数据列表，便于解析
	private List<String[]> errorDataList = new LinkedList<String[]>();// 错误数据
	int sequence = 0;
	
	// 以行号为key，数据行为value，保留用户导入的原始数据，便于生成错误文件
	private Map<Integer, String[]> oriDataMap = new HashMap<Integer, String[]>();
	private Map<String, Integer> colNumMap = new HashMap<String, Integer>();// 列所在的列号

	// ----------------------------------------------------------
	
	public StudentImportData(String configFilePath, DataImportParam param,
			String importDataFilePath) {
		this.configFilePath = configFilePath;
		this.param = param;
		this.importDataFilePath = importDataFilePath;
	}

	/**
	 * 开始进行导入操作
	 */
	public void startImport() throws Exception {
		sequence = 0;
		String objectName = param.getObjectName();

		// -----------处理文件--------------
		String fileType = DataImportConstants.FILE_TYPE_XLS;
		if (null != param.getFileType()) {
			fileType = param.getFileType();
		}
		boolean batchImport = param.isBatchImport();

		// 主要是界面上不重复显示提示信息
		parserConfigObject(objectName);
			// 解析导入数据
		parseData(importDataFilePath, fileType, batchImport);
		// 将数据设置到param中
		param.setImportData(this);
	}

	/**
	 * 针对不同的文件类型分别处理数据文件
	 * 
	 * @param importFile
	 * @param fileType
	 * @param batchImport
	 * @throws Exception
	 */
	private void parseData(String importFile, String fileType,
			boolean batchImport) throws Exception {
		// 默认不需要循环
		String fileExt = importFile.substring(importFile.lastIndexOf(".") + 1);
		if (!fileExt.equalsIgnoreCase(fileType)) {
			throw new Exception("-->导入文件类型不正确，只能选择" + fileType + "文件");
		}

		// 解析参数
		ImportFileParserParam.InParam inParam = (new ImportFileParserParam())
				.getInParamInstance();
		inParam.setBatchImport(batchImport);
		inParam.setBeginRow(beginRow);
		inParam.setParam(param);
		inParam.setXlsSheetName(xlsSheetName);
		inParam.setXmlObjecDefine(xmlObjecDefine);
		inParam.setImportFile(importFile);
		inParam.setHasTitle(param.isHasTitle());

		ImportFileParserParam.OutParam outParam = null; // 出参
		ImportFileParser parser = null;// 解析器

		parser = ImportFileParserFactory.getParser(fileType);
		outParam = parser.parseFile(inParam);
		
		// 标题
		objectDefine = outParam.getObjectDefine();
		if (null == objectDefine)
			objectDefine = xmlObjecDefine;

		// 解析文件数据
		parseFileData(outParam.getFields(), outParam.getRowDatas());
	}

	/**
	 * 解析文件数据
	 * 
	 * @param fields
	 *            列标题
	 * @param rowDatas
	 *            数据行
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void parseFileData(String[] fieldcols, List<String[]> rowDatas)
			throws Exception {
		// 根据导入字段进行过滤，主键字段不过滤
		Set<String> filterFieldSet = param.getFilterFields();
		try {
			int cols = fieldcols.length;
			fileDefineList = new ArrayList<String>(cols + 2);
			fileNameList = new ArrayList<String>(cols + 2);
			String[] fields =new String[cols];
			//特殊处理过滤*
			for(int i=0;i<cols;i++){
				String col=fieldcols[i];
				if(StringUtils.isNotBlank(col)){
					col =col.replace("*", "");
				}
				fields[i]=col;
			}
			
			// 组织对象
			Method[] methodArr = new Method[cols + 2];// 方法名

			Class clazz = null;
			if (null != entityClassName) {
				clazz = Class.forName(entityClassName);
			}
			Set<Integer> primaryColSet = new HashSet<Integer>();// 主键列
			List<ImportObjectNode> nodeList = new ArrayList<ImportObjectNode>();// 字段列表
			Set<Integer> notImportColSet = new HashSet<Integer>();// 不需要导入的列

			String value = "";

			// 导入文件中的列
			Map<String, String> existColMap = new HashMap<String, String>();
			for (int j = 0; j < cols; j++) {
				value = fields[j].replace("*", "");
				if (defineNodeMap.get(value) != null) {
					String columnName = defineNodeMap.get(value).getName();
					existColMap.put(columnName, value);
				}
			}

			// =======================验证列标题的有效性===========================
			// 文件中的导入列，用于判断文件中的列是否重复
			Set<String> fileColSet = new HashSet<String>();

			// 是否忽略文件中的无效列
			boolean ignoreInvalidCol = param.isIgnoreInvalidCol();

			for (int j = 0; j < cols; j++) {
				value = fields[j];
				if (!value.equals("")) {
					String col = value;
					if (defineNodeMap.get(col) == null) {
						if (!ignoreInvalidCol) {
							throw new Exception("-->导入文件中【" + col
									+ "】是无效列，请参照导入模板！");
						} else {
							// 对于不需要的列过滤掉，不作处理
							notImportColSet.add(j);
						}
					}

					if (filterFieldSet.contains(col)) {
						throw new Exception("-->导入文件中【" + col + "】列不允许导入！");
					}

					if (fileColSet.contains(col)) {
						throw new Exception("-->导入文件中【" + col + "】列重复！");
					} else {
						fileColSet.add(col);
					}

					fileDefineList.add(col);
					ImportObjectNode node = defineNodeMap.get(col);
					nodeList.add(node);
					String columnName = null;
					if (node != null) {
						columnName = node.getName();
					}

					fileNameList.add(columnName);
					colNumMap.put(columnName, j);

					if (node == null)
						continue;

					// 得到方法名称:排除动态字段
					if (!node.isDynamic()) {
						methodArr[j] = getMethod(columnName);
					} else {
						dynamicFieldList.add(node.getName());
					}

					String primary = node.getPrimary();
					if (primary != null && primary.equalsIgnoreCase("Y")) {
						primaryColSet.add(j);
					}
				}
			}

			for (int j = 0; j < requiredDefineList.size(); j++) {
				if (!fileDefineList.contains(requiredDefineList.get(j))) {
					if (!(filterFieldSet.contains(requiredDefineList.get(j)))) {
						throw new Exception("-->导入文件格式错误，找不到必导的列【"
								+ requiredDefineList.get(j) + "】。");
					}
				}
			}

			// =======================处理数据行===========================
			String[] copyOfData;
			String[] oriData;
			totalRows = rowDatas.size();
			for (int i = 0; i < rowDatas.size(); i++) {
				String[] rowValues = rowDatas.get(i);
				int fileRow = i + 2 + beginRow;

				// +1是为了增加最后一列保存错误信息
				String[] importDatas = new String[cols + 2];
				copyOfData = new String[cols + 2];
				oriData = new String[cols + 2];

				Object entityInstance = null;
				if (null != clazz) {
					entityInstance = clazz.newInstance();
				}

				String primaryRow = "";// 主键
				boolean hasError = false;
				for (int j = 0; j < cols; j++) {
					value = rowValues[j];

					ImportObjectNode node = defineNodeMap.get(fileDefineList
							.get(j));

					// 一行记录的主键
					if (primaryColSet.contains(j)) {
						primaryRow += toDispose(value, node);
					}
					importDatas[j] = value;
					copyOfData[j] = value;
					oriData[j] = value;

					// 如果不须导入该列，则跳过
					if (notImportColSet.contains(j))
						continue;

					// 做数据的校验
					if (dataValidator(fileDefineList, importDatas, j, fileRow) == false) {
						hasError = true;
						continue;
					}	

					Method method = methodArr[j];
					if (null != method) {
						method.invoke(entityInstance, importDatas[j]);
					}
				}

				// 如果业务主键冲突，则给出提示
				if (primaryMap.containsKey(primaryRow)) {
					String msg = "列";
					for (Integer num : primaryColSet) {
						msg += "【"
								+ nameNodeMap.get(fileNameList.get(num))
										.getDefine() + "】";
					}
					setRowDataValues(
							importDatas,
							primaryColSet,
							"原导入文件中的第 "
									+ (fileRow)
									+ " 行和第 "
									+ String.valueOf(primaryMap.get(primaryRow))
									+ " 行记录的" + msg + "主键冲突。", fileRow);
					hasError = true;
				} else {
					if (!"".equals(primaryRow)) {
						primaryMap.put(primaryRow, fileRow);
					}
				}

				// 将数据存放到list中
				if (!hasError) {
					Method rn = getMethod("oriDataRowNum");
					if (null != rn) {
						rn.invoke(entityInstance, fileRow+"");
					}
					importDataObjectList.add(entityInstance);

					oriDataMap.put(importDataObjectList.size() - 1, oriData);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("文件解析错误，可能是格式不对，错误代码：" + ex.getMessage());
		}
	}

	// 处理数据：如果大小写不敏感，则将字符串中的小写字母转化为大写字母；将括号的全角转换为半角
	private String toDispose(String s, ImportObjectNode node) {
		if (org.apache.commons.lang.StringUtils.isBlank(s))
			return s;

		String s1 = null;
		String caseSensitive = node.getCaseSensitive();
		if (caseSensitive != null && caseSensitive.equalsIgnoreCase("Y")) {
			s1 = s;
		} else {
			s1 = s.toUpperCase();
		}

		s1 = s1.replaceAll("（", "(");
		s1 = s1.replaceAll("）", ")");

		return s1;
	}

	private Method getMethod(String propertyName) {
		return getMethod(propertyName, String.class);
	}

	/**
	 * 取方法名称
	 * 
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Method getMethod(String propertyName, Class clazz) {
		if (propertyName == null || propertyName.length() == 0) {
			return null;
		}
		if (entityClassName == null || propertyName.length() == 0) {
			return null;
		}

		String propertyNameUpper = propertyName.substring(0, 1).toUpperCase()
				+ propertyName.substring(1);

		String methodName = "set" + propertyNameUpper;
		Method method = null;
		try {
			method = Class.forName(entityClassName)
					.getMethod(methodName, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return method;
	}

	/** ************************解析配置文件********************** */
	/**
	 * 解析配置文件
	 */
	private void parserConfigObject(String objectName) throws Exception {
		ImportXML importXML = new ImportXML(configFilePath, param);
		ImportObject importObject = importXML.getConfigObject();

		listeners = importXML.getListeners();
		entityClassName = importXML.getEntityClassName();
		xmlObjecDefine = importXML.getObjecDefine();
		xlsSheetName = importXML.getXlsSheetName();

		requiredDefineList = importObject.getRequiredDefineList();
		selectMap = importObject.getSelectMap();
		defineNodeMap = importObject.getDefineNodeMap();
		nameNodeMap = importObject.getNameNodeMap();
	}

	/** **********************内容校验************************* */
	/**
	 * 数据校验
	 * @param i
	 *            实际的列号
	 * @param rowNum
	 * @param define
	 *            数据名称数组
	 * @param value
	 *            数据内容数组
	 */
	private boolean dataValidator(List<String> defines, String[] importDatas,
			int i, int rowNum) {
		String define = defines.get(i);
		String value = importDatas[i];
		ImportObjectNode node = defineNodeMap.get(define);
		if (node != null) {
			try {
				// 验证数据类型是否正确
				value = verifyType(node, value);

				// 验证微代码
				importDatas[i] = verifySelect(node, value);

				// 验证必输字段是否为空
				verifyRequired(node, value);

				// 验证规则
				veryfyValidate(node, value);

				// 置默认值
				if (org.apache.commons.lang.StringUtils.isBlank(value)
						&& null != node.getDefaultValue()) {
					importDatas[i] = node.getDefaultValue();
				}
			} catch (Exception e) {
				setDataValues(importDatas, String.valueOf(i), e.getMessage(),
						define, rowNum);
				return false;
			}
		}
		return true;
	}

	/**
	 * 设置错误信息
	 * @param msg
	 * @param define
	 * @param rowNum
	 * @param values
	 */
	private void setDataValues(String[] importDatas, String columnNum,
			String msg, String define, int rowNum) {
		if (msg.indexOf(REPLQCE_STRING) >= 0) {
			msg = msg.replace(REPLQCE_STRING, "【" + define + "】列");
		}
//		if (importDatas[importDatas.length - 2] == null) {
//			importDatas[importDatas.length - 2] = "";
//		}
//
//		importDatas[importDatas.length - 2] += "|" + columnNum + "|";
//
//		if (importDatas[importDatas.length - 1] == null) {
//			importDatas[importDatas.length - 1] = msg;
//		} else {
//			importDatas[importDatas.length - 1] += " " + msg;
//		}
		addError((++sequence)+"", "第"+rowNum+"行", importDatas[NumberUtils.toInt(columnNum)], msg, sequence, errorDataList);
	}

	/**
	 * 设置行数据错误信息
	 * @param msg
	 * @param rowNum
	 * @param columnNum
	 */
	private void setRowDataValues(String[] importDatas,
			Set<Integer> columnNumSet, String msg, int rowNum) {
//		if (importDatas[importDatas.length - 2] == null) {
//			importDatas[importDatas.length - 2] = "";
//		}
//		for (Integer num : columnNumSet) {
//			importDatas[importDatas.length - 2] += "|" + num + "|";
//		}
//
//		if (importDatas[importDatas.length - 1] == null) {
//			importDatas[importDatas.length - 1] = msg;
//		} else {
//			importDatas[importDatas.length - 1] += " " + msg;
//		}
		addError((++sequence)+"", "第"+rowNum+"行", "", msg, sequence, errorDataList);
	}
	
	/**
	 * 添加错误
	 */
	private void addError(String da1, String da2, String da3, String da4, 
			int sequence, List<String[]> errorDataList) {
		String[] errorData=new String[4];
		sequence++;
        errorData[0]=StringUtils.trimToEmpty(da1);
        errorData[1]=StringUtils.trimToEmpty(da2);
        errorData[2]=StringUtils.trimToEmpty(da3);
        errorData[3]=StringUtils.trimToEmpty(da4);
        errorDataList.add(errorData);
	}

	private String verifyRequired(ImportObjectNode node, String value)
			throws Exception {
		// 以数据库是否必须为准，可能出现如下四种情况：
		// required（必导列） dbrequired（内容不能为空）
		// Y Y 都必须
		// Y N 如导入成绩时，成绩列必须有，但成绩内容可能为空
		// N Y 如更新导入时，姓名为非必导列，但如果导入，则不能为空
		// N N 都不必须
		if (node.getDbrequired() != null)
			if (node.getDbrequired().equalsIgnoreCase("Y")) {
				if (value == null || value.equals("")) {
					throw new Exception(REPLQCE_STRING + "内容不能为空。");
				}
			}
		return value;
	}

	/**
	 * 校验下拉项，如微代码
	 * 
	 * @param node
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private String verifySelect(ImportObjectNode node, String value)
			throws Exception {
		if (value == null || value.trim().equals(""))
			return "";
		String selectId = node.getMcode();
		if(ImportObjectNode.FIELD_TYPE_SELECT.equals(node.getType())){	//非微代码下拉
			selectId = node.getDefine();
		}else if("Region".equalsIgnoreCase(node.getMcode())) {			//Region微代码下拉
			String thisId = RegionUtils.getRegionCodeByFullName(value);
			if (thisId == null)
				throw new Exception(REPLQCE_STRING + "行政区划不匹配。");
			return thisId;
		}
		if (org.apache.commons.lang.StringUtils.isEmpty(selectId)) {
			return value;
		}
		String thisId = selectMap.get(node.getDefine()).get(value);
		if (org.apache.commons.lang.StringUtils.isEmpty(thisId)) {
			throw new Exception(REPLQCE_STRING + "下拉值不匹配。");
		}
		return thisId;
	}

	/**
	 * 数据的规则校验
	 * 
	 * @param node
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private String veryfyValidate(ImportObjectNode node, String value)
			throws Exception {
		if (value == null || value.trim().equals("")) {
			return "";
		}
		// 正则表达式验证
		String regex = node.getRegex();
		if (regex != null && !regex.equals("")) {
			if ("email".equals(regex)) {
				Pattern p = Pattern
						.compile("[\\w\u4e00-\u9fa5]+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
				Matcher m = p.matcher(value);
				if (!(m.matches())) {
					throw new Exception(REPLQCE_STRING + node.getErrorMsg());
				}
			} else {
				Pattern pa = Pattern.compile(regex);
				if (!pa.matcher(value).matches()) {
					throw new Exception(REPLQCE_STRING + node.getErrorMsg());
				}
			}
		}

		String unique = node.getUnique();
		String name = node.getName();
		if (unique != null && unique.equalsIgnoreCase("Y")) {
			List<String> listOfUnique = uniqueDataMap.get(name);
			String upperValue = toDispose(value, node);
			if (null == listOfUnique) {
				listOfUnique = new ArrayList<String>();
				listOfUnique.add(upperValue);
				uniqueDataMap.put(name, listOfUnique);
			} else {
				if (listOfUnique.contains(upperValue)) {
					throw new Exception(node.getDefine() + "内容和导入文件中其他条记录的内容重复。");
				} else {
					listOfUnique.add(upperValue);
					uniqueDataMap.put(name, listOfUnique);
				}
			}
		}
		return value;
	}

	/**
	 * 验证数据格式
	 * 
	 * @param node
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private String verifyType(ImportObjectNode node, String value)
			throws Exception {
		if (value == null || value.trim().equals(""))
			return "";

		String msg = "";

		String type = node.getType();

		// 字符串的最大长度
		int strLength = node.getStrLength();

		// 小数位最大的位数
		int fraction = node.getDecimal();

		// 是否非负
		String nonnegative = node.getNonnegative();

		// 整数位的最大位数
		int precision = node.getPrecision();

		if (type != null) {
			if (type.equalsIgnoreCase("String")
					|| type.toLowerCase().indexOf("string") == 0) {
				if (strLength == 0)
					return value;
				if (Validators.isString(value, 0, strLength)
						&& net.zdsoft.framework.utils.StringUtils
								.getRealLength(value) <= strLength)
					return value;
				else
					throw new Exception(REPLQCE_STRING + "内容超出了最大长度("
							+ strLength + ")。");
			} else if (type.equalsIgnoreCase("Integer")
					|| type.equalsIgnoreCase("Long")) {
				if (Validators.isNumber(value))
					return value;
				else
					return msg;
			} else if (type.equalsIgnoreCase("Datetime")) {
				if (isDateTime(value))
					return value;
				else
					throw new Exception(REPLQCE_STRING + "不是有效的日期类型。");
			}
			// 只有年和月的类型的
			else if (type.equalsIgnoreCase("YearMonth")) {
				if (value.indexOf("-") > 0) {
					// 如果是2007-1这类的,改成2007-1-1
					if (value.indexOf("-", value.indexOf("-") + 1) < 0) {
						value = value + "-1";
					}
					String[] s = value.split("-");
					if (s.length != 3) {
						throw new Exception(REPLQCE_STRING + "不是有效的日期类型。");
					}
					String year = s[0];
					String month = s[1];
					if (month.length() == 1)
						month = "0" + month;
					String day = s[2];
					if (day.length() == 1)
						day = "0" + day;
					if (year.length() == 2 && Validators.isNumber(year)) {
						if (Integer.parseInt(year) < 20) {
							value = "20" + year + "-" + month + "-" + day;
						} else {
							value = "19" + year + "-" + month + "-" + day;
						}
					} else if (year.length() == 4 && Validators.isNumber(year)) {
						value = year + "-" + month + "-" + day;
					}
				} else if (value.indexOf("/") > 0) {
					if (value.indexOf("/", value.indexOf("/") + 1) < 0) {
						value = value + "/1";
					}
					String[] s = value.split("/");
					if (s.length != 3) {
						throw new Exception(REPLQCE_STRING + "不是有效的日期类型。");
					}
					String year = s[0];
					String month = s[1];
					if (month.length() == 1)
						month = "0" + month;
					String day = s[2];
					if (day.length() == 1)
						day = "0" + day;
					if (year.length() == 2 && Validators.isNumber(year)) {
						if (Integer.parseInt(year) < 20) {
							value = "20" + year + "/" + month + "/" + day;
						} else {
							value = "19" + year + "/" + month + "/" + day;
						}
					} else if (year.length() == 4 && Validators.isNumber(year)) {
						value = year + "/" + month + "/" + day;
					}
				} else if (value.trim().length() == 6) {
					value = value + "01";
					if (Validators.isNumber(value)) {
						value = value.substring(0, 4) + "-"
								+ value.substring(4, 6) + "-"
								+ value.substring(6);
					}
				} else if (value.trim().length() == 8) {
					if (Validators.isNumber(value)) {
						value = value.substring(0, 4) + "-"
								+ value.substring(4, 6) + "-"
								+ value.substring(6);
					}
				}
				if (isDate(value)) {
					return value;
				} else {
					throw new Exception(REPLQCE_STRING + "不是有效的日期类型。");
				}
			} else if (type.equalsIgnoreCase("Date")) {
				if (value.indexOf("-") > 0) {
					String[] s = value.split("-");
					if (s.length != 3) {
						throw new Exception(REPLQCE_STRING + "不是有效的日期类型。");
					}
					String year = s[0];
					String month = s[1];
					if (month.length() == 1)
						month = "0" + month;
					String day = s[2];
					if (day.length() == 1)
						day = "0" + day;
					if (year.length() == 2 && Validators.isNumber(year)) {
						if (Integer.parseInt(year) < 20) {
							value = "20" + year + "-" + month + "-" + day;
						} else {
							value = "19" + year + "-" + month + "-" + day;
						}
					} else if (year.length() == 4 && Validators.isNumber(year)) {
						value = year + "-" + month + "-" + day;
					}
				} else if (value.indexOf("/") > 0) {
					String[] s = value.split("/");
					if (s.length != 3) {
						throw new Exception(REPLQCE_STRING + "不是有效的日期类型。");
					}
					String year = s[0];
					String month = s[1];
					if (month.length() == 1)
						month = "0" + month;
					String day = s[2];
					if (day.length() == 1)
						day = "0" + day;

					if (year.length() == 2 && Validators.isNumber(year)) {
						if (Integer.parseInt(year) < 20) {
							value = "20" + year + "/" + month + "/" + day;
						} else {
							value = "19" + year + "/" + month + "/" + day;
						}
					} else if (year.length() == 4 && Validators.isNumber(year)) {
						value = year + "/" + month + "/" + day;
					}
				} else if (value.trim().length() == 8) {
					if (Validators.isNumber(value)) {
						value = value.substring(0, 4) + "-"
								+ value.substring(4, 6) + "-"
								+ value.substring(6);
					}
				}
				if (isDate(value))
					return value;
				else
					throw new Exception(REPLQCE_STRING + "不是有效的日期类型。");
			} else if (type.equalsIgnoreCase("Timestamp")) {
				if (Validators.isTime(value))
					return "";
				else
					throw new Exception(REPLQCE_STRING + "不是有效的日期类型。");
			} else if (type.indexOf("Numeric") == 0) {
				if ("N".equalsIgnoreCase(nonnegative)) {
					if (!Validators.isNumeric(value, fraction)) {
						throw new Exception(REPLQCE_STRING + "不是有效的数字类型。");
					}
				} else {
					if (!Validators.isNonNegativeNumeric(value, fraction)) {
						throw new Exception(REPLQCE_STRING + "不是有效的非负数字类型。");
					}
				}
				// 判断长度时过滤+ -
				int beginIndex = 0;
				if (value.indexOf("+") >= 0 || value.indexOf("-") >= 0) {
					beginIndex = 1;
				}
				// 如果数值包括小数点：分别判断整数和小数的位数是否超过指定的长度
				if (value.indexOf(".") >= 0) {
					if (value.substring(beginIndex, value.indexOf("."))
							.length() > precision) {
						throw new Exception(REPLQCE_STRING
								+ "数字不符合要求，请控制在整数位不能大于" + precision
								+ "位，小数位不能大于" + fraction + "位。");
					}
					// 如果不包括小数点：只判断整数的位数是否超过了指定的长度
				} else if (org.apache.commons.lang.StringUtils
						.isNotBlank(value)) {
					if (value.substring(beginIndex, value.length()).length() > precision) {
						throw new Exception(REPLQCE_STRING
								+ "数字不符合要求，请控制在整数位不能大于" + precision + "位。");
					}
				}

				if (value == null || value.trim().equals("")) {
					value = "0";
				}
				return value;
			}
		}
		return value;
	}

	/**
	 * 是否是有效的日期时间格式
	 */
	private boolean isDateTime(String str) {
		if (isEmpty(str) || str.length() > 20) {
			return false;
		}

		String[] items = str.split(" ");

		if (items.length != 2) {
			return false;
		}

		return isDate(items[0]) && Validators.isTime(items[1]);
	}

	/**
	 * 是否是有效的数字
	 */
	private boolean isNumber(String str, int min, int max) {
		if (!Validators.isNumber(str)) {
			return false;
		}

		int number = Integer.parseInt(str);
		return number >= min && number <= max;
	}

	/**
	 * 是否为空
	 */
	private boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 是否有失效的日期格式
	 */
	private boolean isDate(String str) {
		if (isEmpty(str) || str.length() > 10) {
			return false;
		}

		String[] items = null;
		if (str.indexOf("-") > 0) {
			items = str.split("-");
		} else if (str.indexOf("/") > 0) {
			items = str.split("/");
		}

		if (items == null)
			return false;

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

	// ---------------------------------对外开放--------------------------------
	public List<String> getFileDefineList() {
		return fileDefineList;
	}

	public List<String[]> getErrorDataList() {
		return errorDataList;
	}

	public List<String> getListeners() {
		return listeners;
	}

	public List<String> getListOfImportDataName() {
		return fileNameList;
	}

	public String getObjectDefine() {
		return objectDefine;
	}

	public Map<Integer, String[]> getOriDataMap() {
		return oriDataMap;
	}

	public Map<String, Integer> getColNumMap() {
		return colNumMap;
	}

	public List<Object> getListOfImportDataObject() {
		return importDataObjectList;
	}

	public List<String> getDynamicFieldList() {
		return dynamicFieldList;
	}

	public Map<String, ImportObjectNode> getMapOfNodesName() {
		return nameNodeMap;
	}

	public int getBeginRow() {
		return beginRow;
	}

	public void setBeginRow(int beginRow) {
		this.beginRow = beginRow;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

}
