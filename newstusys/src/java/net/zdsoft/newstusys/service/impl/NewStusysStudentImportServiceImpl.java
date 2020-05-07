package net.zdsoft.newstusys.service.impl;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.basedata.remote.utils.BusinessUtils;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.newstusys.businessimport.core.ImportObjectNode;
import net.zdsoft.newstusys.businessimport.core.StudentImportData;
import net.zdsoft.newstusys.businessimport.dao.StudentimportDao;
import net.zdsoft.newstusys.businessimport.entity.StudentImport;
import net.zdsoft.newstusys.businessimport.param.DataImportParam;
import net.zdsoft.newstusys.businessimport.param.DataImportViewParam;
import net.zdsoft.newstusys.constants.BaseStudentConstants;
import net.zdsoft.newstusys.entity.BaseStudent;
import net.zdsoft.newstusys.entity.BaseStudentEx;
import net.zdsoft.newstusys.entity.StudentResume;
import net.zdsoft.newstusys.service.BaseStudentExService;
import net.zdsoft.newstusys.service.NewStusysStudentImportService;
import net.zdsoft.newstusys.service.StudentResumeService;
import net.zdsoft.newstusys.utils.ObjectUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;

/**
 * 学生导入处理
 * 
 * @author weixh
 * @since 2018年3月5日 下午5:24:23
 */
@Service("newStusysStudentImportService")
public class NewStusysStudentImportServiceImpl implements NewStusysStudentImportService {
	private Logger logger = LoggerFactory.getLogger(getClass()); 
	
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private FamilyRemoteService familyRemoteService;
	@Autowired
	private StudentResumeService studentResumeService;
	@Autowired
	private RegionRemoteService regionRemoteService;
	@Autowired
	private BaseStudentExService baseStudentExService;
	@Autowired
	private StudentimportDao studentimportDao;
	@Autowired
	private UserRemoteService userRemoteService;
	
	public String saveUpdateDatas(String unitId, int beginRow, String filePath) {
		int successCount=0;
		int totalSize = 0;
		int sequence = 0;
		List<String[]> errorDataList=new ArrayList<String[]>();
		
		if(StringUtils.isBlank(unitId)){
			return errorResult("0", "", "", "参数丢失，无法保存", 
					sequence, totalSize, 0, errorDataList);
		}
		Semester semes = Semester.dc(semesterRemoteService.getCurrentSemester(1));
		if(semes == null) {
			return errorResult("0", "", "", "当前学年学期没有维护", 
					sequence, totalSize, 0, errorDataList);
		}
		List<Clazz> clsList = Clazz.dt(classRemoteService.findByIdCurAcadyear(unitId, semes.getAcadyear()));
		if(CollectionUtils.isEmpty(clsList)) {
			return errorResult("0", "", "", "没有维护班级信息", 
					sequence, totalSize, 0, errorDataList);
		}
		Set<String> classIdSet = new HashSet<String>();
		Map<String, Clazz> clsNameMap = new HashMap<String, Clazz>();
		for(Clazz cl : clsList) {
			classIdSet.add(cl.getId());
			clsNameMap.put(cl.getClassNameDynamic().replaceAll("（", "(").replaceAll("）", ")"), cl);
		}
		
		DataImportViewParam vparam = new DataImportViewParam(
				"student_import.xml", "student_import", null);
		vparam.setHasSubtitle(false);
		vparam.setHasTitle(false);
		DataImportParam param = new DataImportParam(vparam, null);
		param.setObjectName(vparam.getObjectName());
		param.setConstraintFields(new HashMap<String, Map<String, String>>());
        param.setFilterFields(new HashSet<>());
        param.setDynamicFields(new ArrayList<>());
		StudentImportData imdata = new StudentImportData("student_import.xml", param, filePath);
		try {
			imdata.setBeginRow(1);
			imdata.startImport();
			errorDataList.addAll(imdata.getErrorDataList());
			List<Object> dataList = imdata.getListOfImportDataObject();
			List<String> colNames = imdata.getListOfImportDataName();
			totalSize = imdata.getTotalRows();
			Set<String> idcards = new HashSet<String>();
			for(Object obj : dataList) {
				StudentImport stu = (StudentImport) obj;
				if(StringUtils.isNotEmpty(stu.getIdentitycard())) {
					idcards.add(stu.getIdentitycard());
				}
			}
			
			Map<String, Student> idcardStuMap = null;
			if (idcards.size() > 0) {
				List<Student> idcardStuList = Student
						.dt(studentRemoteService.findByIdentityCards(idcards.toArray(new String[0])));
				idcardStuMap = EntityUtils.getMap(idcardStuList, Student::getIdentityCard);
			} else {
				idcardStuMap = new HashMap<String, Student>();
			}
			Map<String, Map<String, Student>> classInnerIdMap = new HashMap<String, Map<String, Student>>();
			if (colNames.contains("classorderid")) {
				List<Student> sList = Student
						.dt(studentRemoteService.findByClassIds(classIdSet.toArray(new String[0])));
				for (Student s : sList) {
					if (StringUtils.isBlank(s.getClassInnerCode())) {
						continue;
					}
					Map<String, Student> sMap = classInnerIdMap.get(s.getClassId());
					if (sMap == null) {
						sMap = new HashMap<String, Student>();
						classInnerIdMap.put(s.getClassId(), sMap);
					}
					sMap.put(s.getClassInnerCode(), s);
				} 
			}
			
			List<StudentImport> updates = new ArrayList<>();
			List<Family> fams = new ArrayList<Family>();
			List<String> delResumeStuIds = new ArrayList<String>(); 
			List<StudentResume> resList = new ArrayList<StudentResume>();
			for (int i = 0; i < dataList.size(); i++) {
				StudentImport stu = (StudentImport) dataList.get(i);
				List<Family> tempFams = new ArrayList<Family>();
				int ori = NumberUtils.toInt(stu.getOriDataRowNum());
				int index = ori - 2;
				
				try {
					if (colNames.contains("classid")) {
						if (StringUtils.isEmpty(stu.getClassid())) {
							addError(index + "", "第" + ori + "行", "", "班级名称不能为空。", sequence, errorDataList);
							continue;
						} 
					}
					if(StringUtils.isNotBlank(stu.getClassid())) {
						Clazz cls = clsNameMap.get(stu.getClassid().replaceAll("（", "(").replaceAll("）", ")"));
						if(cls == null) {
							addError(index+"", "第"+ori+"行", "", "班级在本校中不存在。", sequence, errorDataList);
							continue;
						}
						stu.setClassid(cls.getId());
						stu.setEnrollyear(cls.getAcadyear());
					}
					
					String idcard = stu.getIdentitycard();
					Student exStu = idcardStuMap.get(idcard);
					if(exStu == null || exStu.getIsDeleted() == 1) {
						addError(index+"", "第"+ori+"行", idcard, "该证件编号对应的学生在本校中不存在或已被删除。", sequence, errorDataList);
						continue;
					}
					if(exStu.getIsLeaveSchool() == 1) {
						addError(index+"", "第"+ori+"行", idcard, "该证件编号对应的学生在本校中已离校。", sequence, errorDataList);
						continue;
					}
					if(!exStu.getStudentName().equals(stu.getStuname())) {
						addError(index+"", "第"+ori+"行", idcard, "系统中已经存在此证件编号的学生["
								+ exStu.getStudentName()
								+ "]，和导入文件不匹配。", sequence, errorDataList);
						continue;
					}
					if(!exStu.getSchoolId().equals(unitId)) {
						addError(index+"", "第"+ori+"行", idcard, "此证件编号的学生不是本校的在校生，不能更新。", sequence, errorDataList);
						continue;
					}
					if(StringUtils.isEmpty(stu.getIdentitycardType())) {
						stu.setIdentitycardType(exStu.getIdentitycardType());
					}
					if(StringUtils.isEmpty(stu.getIdentitycardType())) {
						stu.setIdentitycardType(BaseStudentConstants.IDCARDTYPE_ID);
					}
					if(BaseStudentConstants.IDCARDTYPE_ID.equals(stu.getIdentitycardType())) {
						String str = BusinessUtils.validateIdentityCard(idcard, false);
						if(StringUtils.isNotEmpty(str)) {
							addError(index+"", "第"+ori+"行", idcard, str, sequence, errorDataList);
							continue;
						}
					}
					stu.setSchid(unitId);
					stu.setStuid(exStu.getId());
					
					if(StringUtils.isNotEmpty(stu.getCardnumber())) {
						exStu = Student.dc(studentRemoteService.findByCardNumber(unitId, stu.getCardnumber()));
						if (exStu != null && !org.apache.commons.lang3.StringUtils.equals(stu.getStuid(), exStu.getId())) {
							addError(index + "", "第" + ori + "行", stu.getCardnumber(),
									"系统中已经存在此一卡通卡号的学生[" + exStu.getStudentName() + "]，和导入文件不匹配。", sequence, errorDataList);
							continue;
						} 
					}
					
					dealFamData(stu, tempFams);
					if (tempFams.size() > 0) {
						fams.addAll(tempFams);
					}
					int ol = resList.size();
					dealStuResume(stu.getResumeStr(), unitId, stu.getStuid(), resList);
					if(resList.size() > ol) {
						delResumeStuIds.add(stu.getStuid());
					}
					
					successCount ++;
					updates.add(stu);
				} catch (RuntimeException re) {
					re.printStackTrace();
					addError(index+"", "第"+ori+"行", "", re.getMessage(), sequence, errorDataList);
					continue;
				} catch (Exception e) {
					e.printStackTrace();
					addError(index+"", "第"+ori+"行", "", "数据整理出错。", sequence, errorDataList);
					logger.error(e.getMessage(), e);
					continue;
				}
			}
			List<String> stuSqlList = new ArrayList<>();
			List<String> stuExSqlList = new ArrayList<>();
			if(updates.size() > 0) {
				for(StudentImport stu : updates) {
					buildSql(stu, imdata, param.getObjectName(), stuSqlList, stuExSqlList);
				}
				
				if (stuSqlList.size() > 0) {
					studentimportDao.batchUpdate(stuSqlList.toArray(new String[0]));
				}
				if (stuExSqlList.size() > 0) {
					studentimportDao.batchUpdate(stuExSqlList.toArray(new String[0]));
				}
				if(fams.size() > 0) {
					familyRemoteService.saveAll(SUtils.s(fams));
				}
				if(delResumeStuIds.size() > 0) {
					studentResumeService.deleteResumeByStuId(delResumeStuIds.toArray(new String[0]));
				}
				if(resList.size() > 0) {
					studentResumeService.saveAll(resList.toArray(new StudentResume[0]));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return errorResult("0", "", "", "导入失败："+e.getMessage(), 
					sequence, totalSize, 0, errorDataList);
		}
		return result(totalSize, successCount, totalSize-successCount, errorDataList);
	}
	
	/**
	 * 更新学生信息时的家长信息处理
	 * @param stu
	 * @param tempFams
	 * @throws Exception
	 */
	private void dealFamData(StudentImport stu, List<Family> tempFams) throws Exception{
		String[] array = new String[46];
		int fcol = 23;
		array[fcol++] = stu.getFamilyRealName();
		array[fcol++] = stu.getRelation();
		array[fcol++] = stu.getFamilyMobilephone();
		
		array[fcol++] = stu.getRealname1();
		array[fcol++] = stu.getBackground1();
		array[fcol++] = stu.getCulture1();
		array[fcol++] = stu.getCompany1();
		array[fcol++] = stu.getDuty1();
		array[fcol++] = stu.getLinkphone1();
		array[fcol++] = stu.getIdentitycardType1();
		array[fcol++] = stu.getIdentitycard1();
		array[fcol++] = stu.getCountry1();
		array[fcol++] = stu.getBirthday1();
		
		array[fcol++] = stu.getRealname2();
		array[fcol++] = stu.getBackground2();
		array[fcol++] = stu.getCulture2();
		array[fcol++] = stu.getCompany2();
		array[fcol++] = stu.getDuty2();
		array[fcol++] = stu.getLinkphone2();
		array[fcol++] = stu.getIdentitycardType2();
		array[fcol++] = stu.getIdentitycard2();
		array[fcol++] = stu.getCountry2();
		array[fcol++] = stu.getBirthday2();
		dealStuFam(array, stu.getSchid(), stu.getStuid(), false, null, tempFams, false);
	}
	
	/**
	 * 非学生表数据
	 * @param objectName
	 * @return
	 */
	private Set<String> getNotStuFieldSet(String objectName){
		Set<String> notStuFieldSet = new HashSet<String>();
		//只用来导入判断， 并不入库
		notStuFieldSet.add("familyRealName");//
		notStuFieldSet.add("relation");//
		notStuFieldSet.add("familyMobilephone");
		
		notStuFieldSet.add("realname1");
		notStuFieldSet.add("background1");//
		notStuFieldSet.add("culture1");//
		notStuFieldSet.add("company1");
		notStuFieldSet.add("duty1");
		notStuFieldSet.add("linkphone1");//
		notStuFieldSet.add("identitycard1");//
		notStuFieldSet.add("identitycardType1");//
		notStuFieldSet.add("birthday1");//
		notStuFieldSet.add("country1");//
		notStuFieldSet.add("realname2");
		notStuFieldSet.add("background2");//
		notStuFieldSet.add("culture2");//
		notStuFieldSet.add("company2");
		notStuFieldSet.add("duty2");
		notStuFieldSet.add("linkphone2");//
		notStuFieldSet.add("identitycard2");//
		notStuFieldSet.add("identitycardType2");//
		notStuFieldSet.add("birthday2");//
		notStuFieldSet.add("country2");//
		
		notStuFieldSet.add("resumeStr");//
		return notStuFieldSet;
	}
	
	/**
	 * 组织sql
	 * 
	 * @param stu
	 * @param importData
	 * @param objectName
	 * @param stuSqlList
	 * @param stuExSqlList
	 * @param ini 
	 * @param fatherSqlList
	 * @param motherSqlList
	 * @return
	 * @throws ParseException
	 */
	private void buildSql(StudentImport stu, StudentImportData importData,
			String objectName, List<String> stuSqlList, List<String> stuExSqlList) throws ParseException {

		Map<String, ImportObjectNode> nodeMap = importData.getMapOfNodesName();
		List<String> colNames = importData.getListOfImportDataName();// 导入列
		Set<String> notStus = getNotStuFieldSet(objectName);
		//去掉user的账号
		Iterator<String> it = colNames.iterator();
        while(it.hasNext()){
        	String col = it.next();
            if (notStus.contains(col) 
            		|| col.equals("stuUserName") || col.equals("stuUserPassword") || col.equals("familyUserName") ||
            		col.equals("familyUserPassword") || col.equals("familyRealName") || col.equals("familyMobilephone")
            		|| col.equals("relation")) {
            	it.remove();
            }
        }

		StringBuffer stuSql = new StringBuffer();
		StringBuffer stuExSql = new StringBuffer();

		// 覆盖数据 
		stuSql.append("UPDATE base_student SET school_id='").append(stu.getSchid()).append("', event_source = 0, is_leave_school=0, modify_time = ")
				.append(getFormattedTimeForSql());
		stuExSql.append("update base_student_ex set school_id='").append(stu.getSchid()).append("', is_deleted=0, modify_time = ") 
				.append(getFormattedTimeForSql());
		for (String col : colNames) {
			if (StringUtils.isEmpty(col))
				continue;
			ImportObjectNode node = nodeMap.get(col);
			String dbcol = node.getDbname();
			if(StringUtils.isEmpty(dbcol)) {
				continue;
			}
			Object obj = ObjectUtils.getProperty(stu, col);
			String inValue = String.valueOf(obj);
			String outValue = getValueForSQL(node, inValue);
			if("YearMonth".equalsIgnoreCase(node.getType())){
				outValue = getFormattedDateByDayForSQL(inValue);
			} else if("toschooldate".equals(col) || "toSchoolDate".equals(col)) {
				//年月(yyyyMM)，  模板不能设置为Date类型，  在此再特意处理一次
//				inValue="2010-10-1";
				outValue = getFormattedDateByDayForSQL(inValue);
			}
			stuSql.append("," + dbcol + " = " + outValue);
			// 户口性质
			if("nativeType".equals(col) && !colNames.contains("registerType")) {
				stuSql.append(",register_Type = " + outValue);
			}
			if("classid".equals(col)) {
				stuSql.append(",enroll_year = '"+stu.getEnrollyear()+"'");
			}
		}

		// 学生基本信息
		stuSql.append(" WHERE id = '" + stu.getStuid()
						+ "' AND is_deleted = 0");
		stuSqlList.add(stuSql.toString());
		if (stuExSql.length() > 0) {
			stuExSql.append(" WHERE id = '" + stu.getStuid() + "'");
			stuExSqlList.add(stuExSql.toString());
		}
	}
	
	protected String getValueForSQL(ImportObjectNode node, String inValue) {
        String outValue = null;
        String type = node.getType();
        // 数值
        if (type.equalsIgnoreCase("Integer") || type.equalsIgnoreCase("Long")
                || type.indexOf("Numeric") == 0) {
            if (StringUtils.isNotBlank(inValue)) {
                outValue = inValue;
            }
            // 日期
        } else if (type.equalsIgnoreCase("Date")) {
            if (StringUtils.isNotBlank(inValue)) {
                outValue = getFormattedDateByDayForSQL(inValue);
            }
            // 字符串
        } else {
            if (StringUtils.isNotBlank(inValue)) {
                outValue = "'" + inValue + "'";
            }
        }
        return outValue;
    }
	
	/**
     * 仅支持 yyyy-MM-dd 格式的字符串日期
     * 
     * @param dateStr
     * @return
     */
    protected static String getFormattedDateByDayForSQL(String dateStr) {
        return "to_date('" + dateStr + "','yyyy-mm-dd')";
    }

    protected static String getFormattedTimeForSql(Date date) {
        String formattedTime = "to_date('" + DateUtils.date2StringBySecond(date)
                + "','yyyy-mm-dd hh24:mi:ss')";
        return formattedTime;
    }

    protected static String getFormattedTimeForSql() {
        return getFormattedTimeForSql(new Date());
    }
	
	@Override
	public String saveImportStuDatas(String unitId, List<String[]> rowDatas) {
		//错误数据序列号
        int sequence = 0;
		List<String[]> errorDataList=new ArrayList<String[]>();
        if(CollectionUtils.isEmpty(rowDatas)){
        	return errorResult("0", "", "", "没有导入数据", 
					sequence, 0, 0, errorDataList);
        }
        int totalSize =rowDatas.size();
		if(StringUtils.isBlank(unitId)){
			return errorResult("0", "", "", "参数丢失，无法保存", 
					sequence, totalSize, 0, errorDataList);
		}
		Semester semes = Semester.dc(semesterRemoteService.getCurrentSemester(1));
		if(semes == null) {
			return errorResult("0", "", "", "当前学年学期没有维护", 
					sequence, totalSize, 0, errorDataList);
		}
		List<Clazz> clsList = Clazz.dt(classRemoteService.findByIdCurAcadyear(unitId, semes.getAcadyear()));
		if(CollectionUtils.isEmpty(clsList)) {
			return errorResult("0", "", "", "没有维护班级信息", 
					sequence, totalSize, 0, errorDataList);
		}
		Set<String> classIdSet = new HashSet<String>();
		Map<String, Clazz> clsNameMap = new HashMap<String, Clazz>();
		for(Clazz cl : clsList) {
			classIdSet.add(cl.getId());
			clsNameMap.put(cl.getClassNameDynamic().replaceAll("（", "(").replaceAll("）", ")"), cl);
		}
		Set<String> idcards = new HashSet<String>();
//		Set<String> stucodes = new HashSet<String>();
		for (String[] array : rowDatas) {
			if(ArrayUtils.isEmpty(array)) {
				continue;
			}
			if (array.length > 10 && StringUtils.isNotEmpty(array[10])) {
				idcards.add(array[10]);
			}
//			if (array.length > 11 && StringUtils.isNotEmpty(array[11])) {
//				stucodes.add(array[11]);
//			}
		}
//		Map<String, Student> codeStuMap = null;
//		if (stucodes.size() > 0) {
//			List<Student> codeStuList = Student
//					.dt(studentRemoteService.findBySchIdStudentCodes(unitId, stucodes.toArray(new String[0])));
//			codeStuMap = EntityUtils.getMap(codeStuList, Student::getStudentCode);
//		} else {
//			codeStuMap = new HashMap<String, Student>();
//		}
		Map<String, Student> idcardStuMap = null;
		if (idcards.size() > 0) {
			List<Student> idcardStuList = Student
					.dt(studentRemoteService.findByIdentityCards(idcards.toArray(new String[0])));
			idcardStuMap = EntityUtils.getMap(idcardStuList, Student::getIdentityCard);
		} else {
			idcardStuMap = new HashMap<String, Student>();
		}
		Map<String, Map<String, Student>> classInnerIdMap = new HashMap<String, Map<String, Student>>();
		List<Student> sList = Student.dt(studentRemoteService.findByClassIds(classIdSet.toArray(new String[0])));
		for(Student s : sList){
			if (StringUtils.isBlank(s.getClassInnerCode())){
				continue;
			}
			Map<String, Student> sMap = classInnerIdMap.get(s.getClassId());
			if (sMap == null){
				sMap = new HashMap<String, Student>();
				classInnerIdMap.put(s.getClassId(), sMap);
			}
			sMap.put(s.getClassInnerCode(), s);
		}
		String[] mcode = {"DM-XB","DM-MZ", "DM-ZZMM", "DM-COUNTRY", "DM-GATQ", 
				"DM-SFZJLX", "DM-XSLB", "DM-GX", "DM-WHCD"};
		List<McodeDetail> mds = SUtils.dt(mcodeRemoteService.findByMcodeIds(mcode), new TR<List<McodeDetail>>() {} );
		// <mcodeId+thisId, thisId>
		Map<String, String> mdmap = new HashMap<String, String>();
		for(McodeDetail md : mds) {
			mdmap.put(md.getMcodeId()+md.getMcodeContent(), md.getThisId());
		}
		List<Region> regions = Region.dt(regionRemoteService.findByType(Region.TYPE_1));
		if(CollectionUtils.isNotEmpty(regions)){
			for(Region reg : regions) {
				mdmap.put("REGION"+reg.getFullCode(), reg.getFullCode());
				mdmap.put("REGION_NAME"+reg.getFullName(), reg.getFullCode());
			}
		}
		
		
		List<BaseStudent> saves = new ArrayList<>();
		List<BaseStudent> inserts = new ArrayList<>();
		List<BaseStudent> updates = new ArrayList<>();
		List<BaseStudentEx> exList = new ArrayList<BaseStudentEx>();
		List<Family> fams = new ArrayList<Family>();
		List<User> users = new ArrayList<>();
		List<String> delResumeStuIds = new ArrayList<String>(); 
		List<StudentResume> resList = new ArrayList<StudentResume>();
		
		
//		stucodes.clear();
		idcards.clear();
//		Set<String> clsInCodes = new HashSet<String>();
		int successCount=0;
		boolean isAdd=true;
		boolean recUser=false;// 离校生恢复用户数据
		//判断
		int i=2;
		BaseStudent stu;
		for (String[] array : rowDatas) {
			isAdd=true;
			recUser=false;
			i++;
			int j = sequence+1;
			stu = new BaseStudent();
			List<Family> tempFams = new ArrayList<Family>();
			try {
				stu.setClassName(StringUtils.trimToNull(array[0]));//titles.add("*班级");
				if(StringUtils.isEmpty(stu.getClassName())) {
					addError(j+"", "第"+i+"行", "", "班级名称不能为空。", sequence, errorDataList);
					continue;
				}
				Clazz cid = clsNameMap.get(stu.getClassName().replaceAll("（", "(").replaceAll("）", ")"));
				if(cid == null) {
					addError(j+"", "第"+i+"行", "", "班级在本校中不存在。", sequence, errorDataList);
					continue;
				}
				stu.setClassId(cid.getId());
				stu.setEnrollYear(cid.getAcadyear());
				
				String sn = StringUtils.trimToNull(array[1]);//titles.add("*姓名");
				sn = verifyType("姓名", sn, "String-30", true, null, null);
				stu.setStudentName(sn);
				
				stu.setOldName(StringUtils.trimToNull(array[2]));//titles.add("曾用名");
				if(StringUtils.isNotEmpty(stu.getOldName())) {
					verifyType("曾用名", stu.getOldName(), "String-30", false, null, null);
				}
				
				stu.setSexStr(StringUtils.trimToNull(array[3]));//titles.add("性别");
//				if(StringUtils.isEmpty(stu.getSexStr())) {
//					addError(j+"", "第"+i+"行", "", "性别不能为空。", sequence, errorDataList);
//					continue;
//				}
				if (StringUtils.isNotBlank(stu.getSexStr())) {
					String navsex = mdmap.get("DM-XB" + stu.getSexStr());
					if (navsex == null) {
						addError(j + "", "第" + i + "行", "", "性别的值无效。", sequence, errorDataList);
						continue;
					}
					stu.setSex(NumberUtils.toInt(navsex));
				}
				String bstr = StringUtils.trimToNull(array[4]);//titles.add("出生日期");
				bstr = verifyType("出生日期", bstr, "Date-10", false, null, null);
				stu.setBirthday(DateUtils.string2Date(bstr));
				
				String na = StringUtils.trimToNull(array[5]);//titles.add("民族");
				if(StringUtils.isNotEmpty(na)) {
					String nav = mdmap.get("DM-MZ"+na);
					if(nav == null) {
						addError(j+"", "第"+i+"行", "", "民族的值无效。", sequence, errorDataList);
						continue;
					}
					stu.setNation(nav);
				}
				
				na = StringUtils.trimToNull(array[6]);//titles.add("政治面貌");
				if(StringUtils.isNotEmpty(na)) {
					String nav = mdmap.get("DM-ZZMM"+na);
					if(nav == null) {
						addError(j+"", "第"+i+"行", "", "政治面貌的值无效。", sequence, errorDataList);
						continue;
					}
					stu.setBackground(nav);
				}
				
				na = StringUtils.trimToNull(array[7]);//titles.add("国籍");
				if(StringUtils.isNotEmpty(na)) {
					String nav = mdmap.get("DM-COUNTRY"+na);
					if(nav == null) {
						addError(j+"", "第"+i+"行", "", "国籍的值无效。", sequence, errorDataList);
						continue;
					}
					stu.setCountry(nav);
				}
				
				na = StringUtils.trimToNull(array[8]);//titles.add("港澳台侨外");
				if(StringUtils.isNotEmpty(na)) {
					String nav = mdmap.get("DM-GATQ"+na);
					if(nav == null) {
						addError(j+"", "第"+i+"行", "", "港澳台侨外的值无效。", sequence, errorDataList);
						continue;
					}
					stu.setCompatriots(NumberUtils.toInt(nav));
				}
				
				na = StringUtils.trimToNull(array[9]);//titles.add("证件类型");
				if(StringUtils.isNotEmpty(na)) {
					String nav = mdmap.get("DM-SFZJLX"+na);
					if(nav == null) {
						addError(j+"", "第"+i+"行", "", "证件类型的值无效。", sequence, errorDataList);
						continue;
					}
					stu.setIdentitycardType(nav);
				} else {
					stu.setIdentitycardType(BaseStudentConstants.IDCARDTYPE_ID);
				}
				
				String idcard = StringUtils.trimToNull(array[10]);//titles.add("*证件编号");// 10
				idcard = verifyType("证件编号", idcard, "String-30", true, null, null);
				if(idcards.contains(idcard)) {
					addError(j+"", "第"+i+"行", idcard, "证件编号和导入文件中其他条记录的内容重复。", sequence, errorDataList);
					continue;
				}
				if(StringUtils.isNotBlank(stu.getIdentitycardType())) {
					String str = BusinessUtils.validateTypeIdentityCard(stu.getIdentitycardType(),idcard, false);
					if(StringUtils.isNotEmpty(str)) {
						addError(j+"", "第"+i+"行", idcard, str, sequence, errorDataList);
						continue;
					}
				}
				idcards.add(idcard);
				Student exStu = idcardStuMap.get(idcard);
				if(exStu != null) {
					if(exStu.getIsLeaveSchool() == 0 && !exStu.getSchoolId().equals(unitId)) {
						addError(j+"", "第"+i+"行", idcard, "证件编号对应的学生已在其他学校。", sequence, errorDataList);
						continue;
					}
					if(!exStu.getStudentName().equals(stu.getStudentName())) {
						addError(j+"", "第"+i+"行", idcard, "系统中已经存在此证件编号的学生["
								+ exStu.getStudentName()
								+ "]，和导入文件不匹配。", sequence, errorDataList);
						continue;
					}
					recUser = exStu.getIsLeaveSchool() == 1;
					isAdd = false;
					stu.setId(exStu.getId());
					stu.setCreationTime(exStu.getCreationTime());
					stu.setDirId(exStu.getDirId());
					stu.setFilePath(exStu.getFilePath());
					stu.setUnitiveCode(exStu.getUnitiveCode());
				} else {
					stu.setId(UuidUtils.generateUuid());
					stu.setCreationTime(new Date());
				}
				stu.setIdentityCard(idcard);
				
				String sc = StringUtils.trimToNull(array[11]);//titles.add("学号");// 11
				sc = verifyType("学号", sc, "String-30", false, null, null);
//				if(stucodes.contains(sc)) {
//					addError(j+"", "第"+i+"行", sc, "学号和导入文件中其他条记录的内容重复。", sequence, errorDataList);
//					continue;
//				}
//				stucodes.add(sc);
//				exStu = codeStuMap.get(sc);
//				if(exStu != null) {
//					if(exStu.getIsLeaveSchool() == 0
//							&& !stu.getId().equals(exStu.getId())) {
//						addError(j+"", "第"+i+"行", sc, "系统中已经存在此学号的学生["
//								+ exStu.getStudentName()
//								+ "]，和导入文件不匹配。", sequence, errorDataList);
//						continue;
//					}
//				}
				stu.setStudentCode(sc);
				
				String classInnerId = StringUtils.trimToNull(array[12]);//titles.add("班内编号");// 12
				classInnerId = verifyType("班内编号", classInnerId, "String-3", false, "[1-9][0-9]{0,2}", "班内编号只能是大于0的三位整数。");
//				if(StringUtils.isNotEmpty(classInnerId)) {
//					if(clsInCodes.contains(stu.getClassId()+classInnerId)) {
//						addError(j+"", "第"+i+"行", classInnerId, "班内编号和导入文件中同班级其他条记录的内容重复。", sequence, errorDataList);
//						continue;
//					}
//					clsInCodes.add(stu.getClassId()+classInnerId);
//					Map<String, Student> sMap = classInnerIdMap.get(stu.getClassId());
//					if (sMap != null){
//						Student stuInnerId = sMap.get(classInnerId);
//						if (stuInnerId != null){
//							if (!stuInnerId.getId().equalsIgnoreCase(stu.getId())){
//								addError(j+"", "第"+i+"行", classInnerId, "该班中已经存在此班内编号的学生["
//												+ stuInnerId.getStudentName()
//												+ "]，和导入文件不匹配。", sequence, errorDataList);
//								continue;
//							}
//						}
//					}
//				}
				stu.setClassInnerCode(classInnerId);
				
				na = StringUtils.trimToNull(array[13]);//titles.add("学生类别");
				if(StringUtils.isNotEmpty(na)) {
					String nav = mdmap.get("DM-XSLB"+na);
					if(nav == null) {
						addError(j+"", "第"+i+"行", "", "学生类别的值无效。", sequence, errorDataList);
						continue;
					}
					stu.setStudentType(nav);
				}
				
				stu.setOldSchoolName(StringUtils.trimToNull(array[14]));//titles.add("原毕业学校");
				if(StringUtils.isNotEmpty(stu.getOldSchoolName())) {
					verifyType("原毕业学校", stu.getOldSchoolName(), "String-30", false, null, null);
				}
				
				String ed = StringUtils.trimToNull(array[15]);//titles.add("入学年月");
				ed = verifyType("入学年月", ed, "YearMonth", false, null, null);
				stu.setToSchoolDate(DateUtils.string2Date(ed));
				
				String str16 = StringUtils.trimToNull(array[16]);//titles.add("一卡通卡号");
				str16 = verifyType("一卡通卡号", str16, "String-30", false, null, null);
				if (StringUtils.isNotEmpty(str16)) {
					exStu = Student.dc(studentRemoteService.findByCardNumber(unitId, str16));
					if (exStu != null && !org.apache.commons.lang3.StringUtils.equals(stu.getId(), exStu.getId())) {
						addError(j + "", "第" + i + "行", str16,
								"系统中已经存在此一卡通卡号的学生[" + exStu.getStudentName() + "]，和导入文件不匹配。", sequence, errorDataList);
						continue;
					} 
				}
				stu.setCardNumber(str16);
				
				String str17 = StringUtils.trimToNull(array[17]);//titles.add("户籍省县");17
				if(StringUtils.isNotEmpty(str17)) {
					String nav1 = mdmap.get("REGION"+str17);
					String nav2 = mdmap.get("REGION_NAME"+str17);
					String nav = nav1 == null ? nav2:nav1;
					if(nav == null) {
						addError(j+"", "第"+i+"行", "", "户籍省县的值无效，请按照对照文件进行填写。", sequence, errorDataList);
						continue;
					}
					stu.setRegisterPlace(nav);
				}
				
				String str18 = StringUtils.trimToNull(array[18]);//titles.add("户籍镇/街");18
				if(StringUtils.isNotEmpty(str18)) {
					str18 = verifyType("户籍镇/街", str18, "String-100", false, null, null);
					stu.setRegisterStreet(str18);
				}
				
				String str19 = StringUtils.trimToNull(array[19]);//titles.add("籍贯");19
				if (StringUtils.isNotEmpty(str19)) {
					String nav1 = mdmap.get("REGION" + str19);
					String nav2 = mdmap.get("REGION_NAME" + str19);
					String nav = nav1 == null ? nav2:nav1;
					if (nav == null) {
						addError(j + "", "第" + i + "行", "", "籍贯的值无效，请按照对照文件进行填写。", sequence, errorDataList);
						continue;
					}
					stu.setNativePlace(nav);
				}
				
				String str20 = StringUtils.trimToNull(array[20]);//titles.add("家庭住址");20
				if(StringUtils.isNotEmpty(str20)) {
					str20 = verifyType("家庭住址", str20, "String-60", false, null, null);
					stu.setHomeAddress(str20);
				}
				
				String str21 = StringUtils.trimToNull(array[21]);//titles.add("家庭邮编");21
				if(StringUtils.isNotEmpty(str21)) {
					str21 = verifyType("家庭邮编", str21, "String-6", false, "[0-9]{6}", "家庭邮编只能是六位数字。");
					stu.setPostalcode(str21);
				}
				
				String str22 = StringUtils.trimToNull(array[22]);//titles.add("家庭电话");22
				if(StringUtils.isNotEmpty(str22)) {
					str22 = verifyType("家庭电话", str22, "String-20", false, "^[0-9]{1,20}$", "请输入正确的家庭电话(不能超过20位数字)。");
					stu.setFamilyMobile(str22);
				}
				
				// 处理学生家长信息
				dealStuFam(array, unitId, stu.getId(), isAdd, mdmap, tempFams, true);
				
				String str38 = StringUtils.trimToNull(array[46]);//titles.add("特长爱好");38
				if(StringUtils.isNotEmpty(str38)) {
					str38 = verifyType("特长爱好", str38, "String-1000", false, null, null);
					stu.setStrong(str38);
				}
				
				String str39 = StringUtils.trimToNull(array[47]);//titles.add("获奖情况");39
				if(StringUtils.isNotEmpty(str39)) {
					str39 = verifyType("获奖情况", str39, "String-1000", false, null, null);
					stu.setRewardRemark(str39);
				}
				
				String str40 = StringUtils.trimToNull(array[48]);//titles.add("简历");40
				if(StringUtils.isNotEmpty(str40)) {
					int ol = resList.size();
					dealStuResume(str40, unitId, stu.getId(), resList);
					if(!isAdd && resList.size() > ol) {
						delResumeStuIds.add(stu.getId());
					}
				}
				
				stu.setIsDeleted(0);
				stu.setIsLeaveSchool(0);
				stu.setNowState(BaseStudentConstants.NOWSTATE_DJ);
				stu.setSchoolId(unitId);
				BaseStudentEx ex = null;
				if(isAdd) {
					// 添加附表信息 
					inserts.add(stu);
					ex = new BaseStudentEx();
					ex.setId(stu.getId());
				} else {
					updates.add(stu);
					ex = baseStudentExService.findOne(stu.getId());
					if(ex == null) {
						ex = new BaseStudentEx();
						ex.setId(stu.getId());
					}
				}
				// 离校生恢复用户
				if(recUser) {
					List<String> ownerIds = new ArrayList<>();
					ownerIds.add(stu.getId());
					if (tempFams.size() == 0) {
						List<Family> tfs = SUtils.dt(familyRemoteService.findByStudentId(stu.getId()),
								new TR<List<Family>>() {
								});
						for (Family fam : tfs) {
							fam.setIsLeaveSchool(0);
							fam.setSchoolId(unitId);
							fam.setModifyTime(new Date());
							tempFams.add(fam);
						} 
					}
					if (tempFams.size() > 0) {
						ownerIds.addAll(EntityUtils.getList(tempFams, Family::getId));
					}
					List<User> exUs = User.dt(userRemoteService.findByOwnerIds(ownerIds.toArray(new String[0])));
					if(CollectionUtils.isNotEmpty(exUs)) {
						for(User us : exUs) {
							us.setUserState(User.USER_MARK_NORMAL);
							us.setUnitId(unitId);
							us.setModifyTime(new Date());
						}
						users.addAll(exUs);
					}
				}
				ex.setSchoolId(unitId);
				ex.setModifyTime(new Date());
				ex.setIsDeleted(0);
				exList.add(ex);
				saves.add(stu);
				if (tempFams.size() > 0) {
					fams.addAll(tempFams);
				}
				successCount ++;
			} catch (RuntimeException re) {
				re.printStackTrace();
				addError(j+"", "第"+i+"行", "", re.getMessage(), sequence, errorDataList);
				continue;
			} catch (Exception e) {
				e.printStackTrace();
				addError(j+"", "第"+i+"行", "", "数据整理出错。", sequence, errorDataList);
				logger.error(e.getMessage(), e);
				continue;
			}
		}
		
		if(saves.size() > 0) {
			List<Student> saveStus = EntityUtils.copyProperties(saves, BaseStudent.class, Student.class);
			studentRemoteService.saveAllEntitys(SUtils.s(saveStus));
		}
		if(exList.size() > 0) {
			baseStudentExService.saveAll(exList.toArray(new BaseStudentEx[0]));
		}
		if(fams.size() > 0) {
			familyRemoteService.saveAll(SUtils.s(fams));
		}
		if(users.size() > 0) {
			userRemoteService.saveAll(users.toArray(new User[0]));
		}
		if(delResumeStuIds.size() > 0) {
			studentResumeService.deleteResumeByStuId(delResumeStuIds.toArray(new String[0]));
		}
		if(resList.size() > 0) {
			studentResumeService.saveAll(resList.toArray(new StudentResume[0]));
		}
		return result(totalSize, successCount, totalSize-successCount, errorDataList);
	}
	
	/**
	 * 学生家长信息整理
	 * @param array 导入文件行数据
	 * @param unitId
	 * @param stuId
	 * @param isAdd 是否新增学生
	 * @param mdmap 微代码map
	 * @param tempFams 存放待保存家长信息
	 * @param needMdCheck TODO
	 * @throws Exception
	 */
	private void dealStuFam(String[] array, String unitId, String stuId, 
			boolean isAdd, Map<String, String> mdmap, List<Family> tempFams, boolean needMdCheck) throws Exception {
		Map<String, Family> famMap = new HashMap<String, Family>();
		Map<String, Family> thisFamMap = new HashMap<String, Family>();
		// 学生已有家长信息
		if(!isAdd) {
			List<Family> tfs = SUtils.dt(familyRemoteService.findByStudentId(stuId), new TR<List<Family>>() {});
			if(CollectionUtils.isNotEmpty(tfs)) {
				for(Family fam : tfs) {
					if(fam.getIsGuardian() == null) {
						fam.setIsGuardian(0);
					}
					famMap.put(fam.getRelation(), fam);
				}
			}
		}
		int idCol = 33;// 父亲身份证号
		// 父亲信息整理
		Family fafam = null;
		String str26 = StringUtils.trimToNull(array[26]);//titles.add("父亲姓名");26
		if(StringUtils.isNotEmpty(str26)) {
			str26 = verifyType("父亲姓名", str26, "String-30", false, null, null);
			
			String str27 = StringUtils.trimToNull(array[27]);//titles.add("父亲政治面貌");27
			if (needMdCheck && StringUtils.isNotEmpty(str27)) {
				String nav = mdmap.get("DM-ZZMM" + str27);
				if (nav == null) {
					throw new RuntimeException("父亲政治面貌的值无效。");
				}
				str27 = nav;
			}
			String str28 = StringUtils.trimToNull(array[28]);//titles.add("父亲文化程度");28
			if (needMdCheck && StringUtils.isNotEmpty(str28)) {
				String nav = mdmap.get("DM-WHCD" + str28);
				if (nav == null) {
					throw new RuntimeException("父亲文化程度的值无效。");
				}
				str28 = nav;
			}
			String str29 = StringUtils.trimToNull(array[29]);//titles.add("父亲单位");29
			str29 = verifyType("父亲单位", str29, "String-60", false, null, null);
			String str30 = StringUtils.trimToNull(array[30]);//titles.add("父亲职务");30
			str30 = verifyType("父亲职务", str30, "String-20", false, null, null);
			String str31 = StringUtils.trimToNull(array[31]);//titles.add("父亲手机号");31
			str31 = verifyType("父亲手机号", str31, "String-20", false, "^1[0-9]{10}$", "请输入正确的父亲手机号(以1开头的11位数字)。");
			
			String str32 = StringUtils.trimToNull(array[32]);//titles.add("父亲身份类型");32
			if (needMdCheck && StringUtils.isNotEmpty(str32)) {
				String nav = mdmap.get("DM-SFZJLX" + str32);
				if (nav == null) {
					throw new RuntimeException("父亲证件类型的值无效。");
				}
				str32 = nav;
			}
			
			String idcard33 = StringUtils.trimToNull(array[idCol]);//titles.add("父亲身份证号");32
			idcard33 = verifyType("父亲证件号", idcard33, "String-30", false, "^[a-zA-Z0-9]+$", "请输入正确的证件号(字母数字)。");
			
			String str34 = StringUtils.trimToNull(array[34]);//父亲国籍
			if (needMdCheck && StringUtils.isNotEmpty(str34)) {
				String nav = mdmap.get("DM-COUNTRY" + str34);
				if (nav == null) {
					throw new RuntimeException("父亲国籍的值无效。");
				}
				str34 = nav;
			}
			
			String str35 = StringUtils.trimToNull(array[35]);//父亲出生日期
			str35 = verifyType("出生日期", str35, "Date-10", false, null, null);
			
			fafam = famMap.get(BaseStudentConstants.RELATION_FATHER);
			if(fafam == null) {
				fafam = new Family();
				fafam.setStudentId(stuId);
				fafam.setId(UuidUtils.generateUuid());
				fafam.setCreationTime(new Date());
				fafam.setRelation(BaseStudentConstants.RELATION_FATHER);
				fafam.setIsGuardian(0);
			}
			fafam.setIdentityCard(idcard33);
			fafam.setSex(BaseStudentConstants.SEX_MALE);
			fafam.setSchoolId(unitId);
			fafam.setRealName(str26);
			fafam.setPoliticalStatus(str27);
			fafam.setCulture(str28);
			fafam.setCompany(str29);
			fafam.setDuty(str30);
			fafam.setMobilePhone(str31);
			fafam.setLinkPhone(str31);
			fafam.setIdentitycardType(str32);
			fafam.setCountry(str34);
			fafam.setBirthday(DateUtils.string2Date(str35));
			fafam.setModifyTime(new Date());
			fafam.setIsDeleted(0);
			fafam.setEventSource(0);
			fafam.setOpenUserStatus(1);
			fafam.setIsLeaveSchool(0);
			thisFamMap.put(BaseStudentConstants.RELATION_FATHER, fafam);
		}
		
		// 母亲信息整理
		Family mafam = null;
		idCol=36;
		String str32 = StringUtils.trimToNull(array[idCol++]);//titles.add("母亲姓名");32+1
		if(StringUtils.isNotEmpty(str32)) {
			str32 = verifyType("母亲姓名", str32, "String-30", false, null, null);
			String str33 = StringUtils.trimToNull(array[37]);//titles.add("母亲政治面貌");33+1
			if (needMdCheck && StringUtils.isNotEmpty(str33)) {
				String nav = mdmap.get("DM-ZZMM" + str33);
				if (nav == null) {
					throw new RuntimeException("母亲政治面貌的值无效。");
				}
				str33 = nav;
			}
			String str34 = StringUtils.trimToNull(array[38]);//titles.add("母亲文化程度");34+1
			if (needMdCheck && StringUtils.isNotEmpty(str34)) {
				String nav = mdmap.get("DM-WHCD" + str34);
				if (nav == null) {
					throw new RuntimeException("母亲文化程度的值无效。");
				}
				str34 = nav;
			}
			String str35 = StringUtils.trimToNull(array[39]);//titles.add("母亲单位");35+1
			str35 = verifyType("母亲单位", str35, "String-60", false, null, null);
			String str36 = StringUtils.trimToNull(array[40]);//titles.add("母亲职务");36+1
			str36 = verifyType("母亲职务", str36, "String-20", false, null, null);
			String str37 = StringUtils.trimToNull(array[41]);//titles.add("母亲手机号");37+1
			str37 = verifyType("母亲手机号", str37, "String-20", false, "^1[0-9]{10}$", "请输入正确的母亲手机号(以1开头的11位数字)。");
			
			String str42 = StringUtils.trimToNull(array[42]);//titles.add("母亲身份类型");42
			if (needMdCheck && StringUtils.isNotEmpty(str42)) {
				String nav = mdmap.get("DM-SFZJLX" + str42);
				if (nav == null) {
					throw new RuntimeException("母亲证件类型的值无效。");
				}
				str42 = nav;
			}
			
			String idcard39 = StringUtils.trimToNull(array[43]);//titles.add("母亲身份证号");38+1
			idcard39 = verifyType("母亲证件号", idcard39, "String-30", false, "^[a-zA-Z0-9]+$", "请输入正确的证件号(字母数字)。");
			
            String str44 = StringUtils.trimToNull(array[44]);//母亲国籍
			if (needMdCheck && StringUtils.isNotEmpty(str44)) {
				String nav = mdmap.get("DM-COUNTRY" + str44);
				if (nav == null) {
					throw new RuntimeException("母亲国籍的值无效。");
				}
				str44 = nav;
			}
			
			String str45 = StringUtils.trimToNull(array[45]);//母亲出生日期
			str45 = verifyType("出生日期", str45, "Date-10", false, null, null);
			
			mafam = famMap.get(BaseStudentConstants.RELATION_MOTHER);
			if(mafam == null) {
				mafam = new Family();
				mafam.setId(UuidUtils.generateUuid());
				mafam.setCreationTime(new Date());
				mafam.setRelation(BaseStudentConstants.RELATION_MOTHER);
				mafam.setStudentId(stuId);
				mafam.setIsGuardian(0);
			}
			mafam.setIdentityCard(idcard39);
			mafam.setSex(BaseStudentConstants.SEX_FEMALE);
			mafam.setSchoolId(unitId);
			mafam.setRealName(str32);
			mafam.setPoliticalStatus(str33);
			mafam.setCulture(str34);
			mafam.setCompany(str35);
			mafam.setDuty(str36);
			mafam.setMobilePhone(str37);
			mafam.setLinkPhone(str37);
			mafam.setIdentitycardType(str42);
			mafam.setCountry(str44);
			mafam.setBirthday(DateUtils.string2Date(str45));
			mafam.setModifyTime(new Date());
			mafam.setIsDeleted(0);
			mafam.setEventSource(0);
			mafam.setOpenUserStatus(1);
			mafam.setIsLeaveSchool(0);
			thisFamMap.put(BaseStudentConstants.RELATION_MOTHER, mafam);
		}
		
		// 监护人信息整理
		Family gfam = null;
		String str24 = StringUtils.trimToNull(array[24]);//titles.add("监护人与学生关系");24
		if(StringUtils.isNotEmpty(str24)) {
			String nav = str24;
			if (needMdCheck) {
				nav = mdmap.get("DM-GX" + str24);
				if (nav == null) {
					throw new RuntimeException("监护人与学生关系的值无效。");
				} 
			}
			Family tf = thisFamMap.get(nav);
			if(tf != null) {
				tf.setIsGuardian(1);
			}	
			String str23 = StringUtils.trimToNull(array[23]);//titles.add("监护人");23
			str23 = verifyType("监护人", str23, "String-30", false, null, null);
			if(tf != null) {
				if(StringUtils.isNotEmpty(str23)) {
					tf.setRealName(str23);
				}
			} else if(StringUtils.isEmpty(str23)) {
				throw new RuntimeException("监护人不能为空。");
			}
			
			String str25 = StringUtils.trimToNull(array[25]);//titles.add("监护人联系电话");25
			verifyType("监护人联系电话", str25, "String-20", false, "^[0-9]{1,20}$", "请输入正确的监护人联系电话(不能超过20位数字)。");
			if(tf != null) {
				if(StringUtils.isNotEmpty(str25)) {
					tf.setMobilePhone(str25);
					tf.setLinkPhone(str25);
				}
			}
			if (tf == null) {
				gfam = famMap.get(nav);
				if (gfam == null) {
					gfam = new Family();
					gfam.setId(UuidUtils.generateUuid());
					gfam.setCreationTime(new Date());
					gfam.setRelation(nav);
					gfam.setStudentId(stuId);
				}
				gfam.setIsGuardian(1);
				gfam.setSchoolId(unitId);
				gfam.setRealName(str23);
				gfam.setMobilePhone(str25);
				gfam.setLinkPhone(str25);
				gfam.setModifyTime(new Date());
				gfam.setIsDeleted(0);
				gfam.setEventSource(0);
				gfam.setOpenUserStatus(1);
				gfam.setIsLeaveSchool(0);
			}
		}
		if(fafam != null) {
			tempFams.add(fafam);
		}
		if(mafam != null) {
			tempFams.add(mafam);
		}
		if(gfam != null) {
			tempFams.add(gfam);
		}
	}

	/**
	 * 处理学生简历信息
	 * @param resValue
	 * @param unitId
	 * @param stuId
	 * @param resList
	 * @throws Exception
	 */
	private void dealStuResume(String resValue, String unitId, String stuId, 
			List<StudentResume> resList) throws Exception {
		if(StringUtils.isEmpty(resValue)) {
			return;
		}
		resValue = resValue.replaceAll("，", ",").replaceAll("；", ";")
				.replaceAll(" ", "").replaceAll(" ", "");
		String[] resumeStrs = resValue.split(";");
		//XX年XX月至XX年XX月，XX学校
		StudentResume sr;
		for(String resArr : resumeStrs) {
			String[] res = resArr.split(",");
			
			String[] dates = res[0].replaceAll("年", "-").replaceAll("月", "").split("至");
			String start = dates[0];
			String end = null;
			if(dates.length != 1) {
				end = dates[1];
			}
			start = verifyType("简历中的开始年月", start, "YearMonth", true, null, null);
			end = verifyType("简历中的结束年月", end, "YearMonth", true, null, null);
			Date s = DateUtils.string2Date(start);
			Date e = DateUtils.string2Date(end);
			if(e.before(s)) {
				throw new RuntimeException("简历中的结束年月不能早于开始年月。");
			}
			String schName = null;
			if(res.length != 1) {
				schName = res[1];
			}
			schName = verifyType("简历中的学校", schName, "String-60", true, null, null);
			
			sr = new StudentResume();
			sr.setSchid(unitId);
			sr.setStuid(stuId);
			sr.setSchoolname(schName);
			sr.setStartdate(s);
			sr.setEnddate(e);
			sr.setUpdatestamp(System.currentTimeMillis());
			resList.add(sr);
		}
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
		value = verifyType(fieldName, value, typeStr);
		
		if (StringUtils.isEmpty(regex)) {
    		return value;
    	}
    	java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
    	Matcher matcher = pattern.matcher(value);
    	if(!matcher.matches()) {
    		throw new RuntimeException(fieldName + errorMsg);
    	}
		return value;
	}
	
	/**
	 * 校验数据类型
	 * @param fieldName
	 * @param value
	 * @param typeStr
	 * @return
	 */
	private String verifyType(String fieldName, String value, String typeStr) {
		if(StringUtils.isEmpty(typeStr)) {
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
		return value;
	}
	
	
	/**
	 * 返回错误消息
	 */
	private String errorResult(String da1, String da2, String da3, String da4, 
			int sequence, int total, int success, List<String[]> errorDataList) {
		addError(da1, da2, da3, da4, sequence, errorDataList);
        return result(total, success, 1, errorDataList);
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

}
