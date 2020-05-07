package net.zdsoft.studevelop.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.studevelop.data.dao.StudevelopDutySituationDao;
import net.zdsoft.studevelop.data.entity.StudevelopDutySituation;
import net.zdsoft.studevelop.data.service.StudevelopDutySituationService;

import net.zdsoft.system.entity.mcode.McodeDetail;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("StudevelopDutySituationService")
public class StudevelopDutySituationServiceImpl extends BaseServiceImpl<StudevelopDutySituation, String> implements StudevelopDutySituationService{

	@Autowired
	private StudevelopDutySituationDao dutySituationDao;
	@Autowired
	private StudentRemoteService studentRemoteService;

	@Override
	public List<StudevelopDutySituation> findListByCls(String acadyear,
			String semester, String[] array) {
		return dutySituationDao.findListByCls(acadyear,semester,array);
	}

	@Override
	public String saveImportDutySituationDatas(String unitId, List<String[]> rowDatas ,StudevelopDutySituation dutySituation) {

		String classId = dutySituation.getClassId();
		List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classId), new TypeReference<List<Student>>(){});

		Map<String,Student> studentMap = EntityUtils.getMap(studentList,"studentCode");
		String acadyear = dutySituation.getAcadyear();
		String semester = dutySituation.getSemester();
		List<String> studentIds = EntityUtils.getList(studentList,"id");
		List<StudevelopDutySituation> dutySituationList =findListByCls(dutySituation.getAcadyear(),dutySituation.getSemester(),studentIds.toArray(new String[0]));

		Map<String ,StudevelopDutySituation> dutySituationMap = EntityUtils.getMap(dutySituationList,"studentId");
		//错误数据序列号
		int sequence = 0;
		List<String[]> errorDataList=new ArrayList<String[]>();
		if(CollectionUtils.isEmpty(rowDatas)){
			return errorResult("0", "", "", "没有导入数据",
					sequence, 0, 0, errorDataList);
		}
		int i=0;
//		DateFormatUtils
		StudevelopDutySituation studevelopDutySituation;

		List<StudevelopDutySituation> situationList = new ArrayList<>();
		int totalSize = rowDatas.size();
		int successCount=0;
		for(int m=0;m<rowDatas.size();m++){
			i++;
			int j=sequence+1;
			try {
				String[] datas= rowDatas.get(m);
				String studentName = StringUtils.trimToEmpty(datas[0]);
				if(StringUtils.isEmpty(studentName)){
					addError(j+"","第"+i+"行","","学生姓名不能为空",sequence,errorDataList);
					continue;
				}
				String classInnerCode = StringUtils.trimToEmpty(datas[1]);
				String stuCode = StringUtils.trimToEmpty(datas[2]);
				if(StringUtils.isEmpty(stuCode)){
					addError(j+"","第"+i+"行","","学生学号不能为空",sequence,errorDataList);
					continue;
				}
				Student student = studentMap.get(stuCode);
				if(student == null){
					addError(j+"","第"+i+"行","","学生学号在该班级下不存在",sequence,errorDataList);
					continue;
				}
				if(!studentName.equals(student.getStudentName())){
					addError(j+"","第"+i+"行","","该班级下学生学号对应的学生姓名与导入文件中对应姓名不匹配",sequence,errorDataList);
					continue;
				}
				if(StringUtils.isNotEmpty(classInnerCode) && !classInnerCode.equals(student.getClassInnerCode())){
					addError(j+"","第"+i+"行","","该班级下学生学号对应的学生编号与导入文件中对应编号不匹配",sequence,errorDataList);
					continue;
				}

				String dutyName = StringUtils.trimToEmpty(datas[3]);
				if(StringUtils.isEmpty(dutyName)){
					addError(j+"","第"+i+"行","","学生所任职务不能为空",sequence,errorDataList);
					continue;
				}
				studevelopDutySituation = dutySituationMap.get(student.getId());
				if(studevelopDutySituation == null){
					studevelopDutySituation = new StudevelopDutySituation();
					studevelopDutySituation.setId(UuidUtils.generateUuid());
					studevelopDutySituation.setStudentId(student.getId());
					studevelopDutySituation.setAcadyear(acadyear);
					studevelopDutySituation.setSemester(semester);
				}

				verifyType("所任职务",dutyName,"String-30",true,null,"请输入正确的所任职务(不能超过30个字符)");
				studevelopDutySituation.setDutyName(dutyName);

				String startTime = StringUtils.trimToEmpty(datas[4]);

				startTime = verifyType("开始时间",startTime,"Date",false,null,null);
				Date start=null;
				if(StringUtils.isNotEmpty(startTime)){
					start = org.apache.commons.lang3.time.DateUtils.parseDate(startTime ,"yyyy-MM-dd");
				}
				studevelopDutySituation.setOpenTime(start);
				String endTime = StringUtils.trimToEmpty(datas[5]);
				endTime = verifyType("结束时间",endTime,"Date",false,null,null);
				Date end = null;
				if(StringUtils.isNotEmpty(endTime)){
					end = org.apache.commons.lang3.time.DateUtils.parseDate(endTime ,"yyyy-MM-dd");
				}
				//起始时间在 结束时间之后
				if(start!= null && end != null && start.after(end)){
					throw new RuntimeException("开始时间应该小于结束时间！");
				}
				studevelopDutySituation.setEndTime(end);

				String content = StringUtils.trimToEmpty(datas[6]);
				content = verifyType("工作内容",content,"String-200",false,null,null);
				studevelopDutySituation.setDutyContent(content);

				String situation = StringUtils.trimToEmpty(datas[7]);
				situation=verifyType("工作内容",situation,"String-300",false,null,null);
				studevelopDutySituation.setDutySituation(situation);

				String remark = StringUtils.trimToEmpty(datas[8]);
				remark =verifyType("工作内容",remark,"String-200",false,null,null);
				studevelopDutySituation.setRemark(remark);

				situationList.add(studevelopDutySituation);
				successCount++;
			} catch (RuntimeException re) {
				re.printStackTrace();
				addError(j+"", "第"+i+"行", "", re.getMessage(), sequence, errorDataList);
				continue;
			}  catch (Exception e) {
				e.printStackTrace();
				addError(j+"", "第"+i+"行", "", "数据整理出错。", sequence, errorDataList);
				continue;
			}
		}

		if(situationList.size() > 0){
			saveAll(situationList.toArray(new StudevelopDutySituation[0]));
		}



		return result(totalSize, successCount, totalSize-successCount, errorDataList);
	}


	/**
	 * 数据内容简单格式校验
	 * @param fieldName 字段名称
	 * @param value 内容
	 * @param typeStr
	 * @param require 是否必填
	 * @param regex 正则
	 * @param errorMsg 正则校验不通过时返回的提示信息
	 * @return
	 * @throws Exception
	 */
	private String verifyType(String fieldName, String value, String typeStr, boolean require, String regex, String errorMsg) throws Exception{
		if(require && StringUtils.isEmpty(value)) {
			throw new RuntimeException(fieldName + "不能为空。");
		}
		if(StringUtils.isEmpty(value)) {
			return value;
		}
		if(StringUtils.isEmpty(typeStr) && StringUtils.isEmpty(regex)) {
			return value;
		}
		String[] ts = typeStr.split("-");
		String type = ts[0];
		if (type.equalsIgnoreCase("String")
				|| type.toLowerCase().indexOf("string") == 0) {
			int strLength = NumberUtils.toInt(ts[1]);
			if (strLength == 0)
				return value;
			if (Validators.isString(value, 0, strLength)
					&& net.zdsoft.framework.utils.StringUtils
					.getRealLength(value) <= strLength)
				return value;
			else
				throw new RuntimeException(fieldName + "内容超出了最大长度("
						+ strLength + ")。");
		} else if (type.equalsIgnoreCase("Integer")
				|| type.equalsIgnoreCase("Long")) {
			if (Validators.isNumber(value))
				return value;
			else
				return "";
//		} else if (type.equalsIgnoreCase("Datetime")) {
//			if (isDateTime(value))
//				return value;
//			else
//				throw new RuntimeException(fieldName + "不是有效的日期类型。");
//		}
			// 只有年和月的类型的
		} else if (type.equalsIgnoreCase("YearMonth")) {
			if (value.indexOf("-") > 0) {
				// 如果是2007-1这类的,改成2007-1-1
				if (value.indexOf("-", value.indexOf("-") + 1) < 0) {
					value = value + "-1";
				}
				String[] s = value.split("-");
				if (s.length != 3) {
					throw new RuntimeException(fieldName + "不是有效的日期类型。");
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
					throw new RuntimeException(fieldName + "不是有效的日期类型。");
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
				value = value.replaceAll("/", "-");
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
			if (DateUtils.isDate(value)) {
				return value;
			} else {
				throw new RuntimeException(fieldName + "不是有效的日期类型。");
			}
		} else if (type.equalsIgnoreCase("Date")) {
			if (value.indexOf("-") > 0) {
				String[] s = value.split("-");
				if (s.length != 3) {
					throw new RuntimeException(fieldName + "不是有效的日期类型。");
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
					throw new RuntimeException(fieldName + "不是有效的日期类型。");
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
				value = value.replaceAll("/", "-");
			} else if (value.trim().length() == 8) {
				if (Validators.isNumber(value)) {
					value = value.substring(0, 4) + "-"
							+ value.substring(4, 6) + "-"
							+ value.substring(6);
				}
			}
			if (DateUtils.isDate(value))
				return value;
			else
				throw new RuntimeException(fieldName + "不是有效的日期类型。");
		}
//		else if (type.equalsIgnoreCase("Timestamp")) {
//			if (Validators.isTime(value))
//				return "";
//			else
//				throw new Exception(fieldName + "不是有效的日期类型。");
//		} else if (type.indexOf("Numeric") == 0) {
//			if ("N".equalsIgnoreCase(nonnegative)) {
//				if (!Validators.isNumeric(value, fraction)) {
//					throw new RuntimeException(fieldName + "不是有效的数字类型。");
//				}
//			} else {
//				if (!Validators.isNonNegativeNumeric(value, fraction)) {
//					throw new RuntimeException(fieldName + "不是有效的非负数字类型。");
//				}
//			}
//			// 判断长度时过滤+ -
//			int beginIndex = 0;
//			if (value.indexOf("+") >= 0 || value.indexOf("-") >= 0) {
//				beginIndex = 1;
//			}
//			// 如果数值包括小数点：分别判断整数和小数的位数是否超过指定的长度
//			if (value.indexOf(".") >= 0) {
//				if (value.substring(beginIndex, value.indexOf("."))
//						.length() > precision) {
//					throw new RuntimeException(fieldName
//							+ "数字不符合要求，请控制在整数位不能大于" + precision
//							+ "位，小数位不能大于" + fraction + "位。");
//				}
//				// 如果不包括小数点：只判断整数的位数是否超过了指定的长度
//			} else if (StringUtils
//					.isNotBlank(value)) {
//				if (value.substring(beginIndex, value.length()).length() > precision) {
//					throw new RuntimeException(fieldName
//							+ "数字不符合要求，请控制在整数位不能大于" + precision + "位。");
//				}
//			}
//
//			if (value == null || value.trim().equals("")) {
//				value = "0";
//			}
//			return value;
//		}

		if (StringUtils.isEmpty(regex)) {
			return value;
		}
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		if(!matcher.matches()) {
			return errorMsg;
		}
		return value;
	}

	/**
	 * 结果信息
	 * @param totalCount
	 * @param successCount
	 * @param errorCount
	 * @param errorDataList
	 * @return
	 */
	private String  result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList){
		Json importResultJson=new Json();
		importResultJson.put("totalCount", totalCount);
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorCount);
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}
	/**
	 * 返回错误消息
	 */
	private String errorResult(String da1, String da2, String da3, String da4,
							   int sequence, int total, int success, List<String[]> errorDataList) {
		addError(da1, da2, da3, da4, sequence, errorDataList);
		return result(total, success, total-success, errorDataList);
	}
	/**
	 * 添加错误
	 */
	private void addError(String da1, String da2, String da3, String da4,
						  int sequence, List<String[]> errorDataList) {
		String[] errorData=new String[4];
		sequence++;
		errorData[0]= StringUtils.trimToEmpty(da1);
		errorData[1]=StringUtils.trimToEmpty(da2);
		errorData[2]=StringUtils.trimToEmpty(da3);
		errorData[3]=StringUtils.trimToEmpty(da4);
		errorDataList.add(errorData);
	}
	@Override
	public StudevelopDutySituation findById(String id) {
		return dutySituationDao.findById(id).orElse(null);
	}
	
	@Override
	public List<StudevelopDutySituation> getDutySituationList(String stuId,
			String acadyear, String semester) {
		return dutySituationDao.getStudocDutySituationList(stuId, acadyear, semester);
	}
	
	@Override
	protected BaseJpaRepositoryDao<StudevelopDutySituation, String> getJpaDao() {
		return dutySituationDao;
	}

	@Override
	protected Class<StudevelopDutySituation> getEntityClass() {
		return StudevelopDutySituation.class;
	}

	
}
