package net.zdsoft.savedata.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StusysSectionTimeSet;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.StusysSectionTimeSetRemoteService;
import net.zdsoft.framework.exception.BusinessErrorException;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.savedata.entity.CheckWorkData;
import net.zdsoft.savedata.service.BusinessSyncSaveService;
import net.zdsoft.savedata.service.CheckWorkDataService;
import net.zdsoft.eclasscard.remote.service.EclasscardRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("businessSyncSaveService")
public class BusinessSyncSaveServiceImpl implements BusinessSyncSaveService{
	
	private Logger log = Logger.getLogger(BusinessSyncSaveServiceImpl.class);

	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private EclasscardRemoteService eclasscardRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private CheckWorkDataService checkWorkDataService;
	@Autowired
	private StusysSectionTimeSetRemoteService stusysSectionTimeSetRemoteService;
	
	@Override
	public void saveCheckWork(CheckWorkData[] checkWorkDatas, Map<String, String> returnMsg) throws Exception {
		try {
			List<CheckWorkData> saveCheckWorks = new ArrayList<>();
			int errorNum = 0;
			StringBuilder errorMsg = new StringBuilder();
			String unitId = null; 
			for (int j = 0; j < checkWorkDatas.length; j++) {
				 CheckWorkData checkWorkData = checkWorkDatas[j];
				 unitId = checkWorkData.getUnitIdent();
				 if(StringUtils.isNotBlank(unitId)){
					 break;
				 }
			}
			Semester semester = getCurrentSemester();
			String acadyear = semester.getAcadyear();
			Integer sInteger = semester.getSemester();
			List<StusysSectionTimeSet> sectionTimeSets = getUnitSectionTimeSets(unitId, acadyear, sInteger);
			for (int j = 0; j < checkWorkDatas.length; j++) {
			   CheckWorkData checkWorkData = checkWorkDatas[j];
			   //数据库中非空的字段
			   String id = checkWorkData.getId();
			   if(StringUtils.isBlank(id)) {
				   id = UuidUtils.generateUuid();
				   checkWorkData.setId(id);
			   }
			   saveCheckWorks.add(checkWorkData);
			   if(doPushEclassCard(checkWorkData,sectionTimeSets)){
				    errorNum++;
				    errorMsg.append(j +",");
				    continue;
			   }
			}
			getReturnMsg(returnMsg, errorNum, errorMsg);
			if(CollectionUtils.isNotEmpty(saveCheckWorks)) {
				checkWorkDataService.saveAll(saveCheckWorks.toArray(new CheckWorkData[0]));
			}
		} catch (Exception e) {
			log.error("保存第三方考勤数据失败"+ e.getMessage());
			throw new Exception("保存第三方考勤数据失败:"+e.getMessage()); 
		}
	}
	
	/**
	 * 获取当前的学年学期
	 * @return
	 */
	private Semester getCurrentSemester() {
		String remoteSemester= "kade.remote.semester";
		Semester semester = Semester
				.dc(RedisUtils.get(remoteSemester, new RedisInterface<String>() {
					@Override
					public String queryData() {
						return semesterRemoteService.getCurrentSemester(SemesterRemoteService.RET_PRE);
					}
				}));
		return semester;
	}
	
	/**
	 * 得到是否成功保存考勤数据
	 * @param checkWorkData
	 * @param sectionTimeSets
	 * @return
	 */
	private boolean doPushEclassCard(CheckWorkData checkWorkData,
			List<StusysSectionTimeSet> sectionTimeSets) {
		 boolean isError = Boolean.FALSE;
		 String result = pushEclassCard(checkWorkData,sectionTimeSets);
	     if("0".equals(result)){
		   for (int i = 0; i < 3; i++) {
			   result = pushEclassCard(checkWorkData,sectionTimeSets);
			   if ("-1".equals(result) || (i == 2 && "0".equals(result)) ) {
				    isError = Boolean.TRUE;
				    break;
			   }
		   }
	     }
	     if("-1".equals(result)){
	    	 isError = Boolean.TRUE;
	     }
		 return isError;
	}
	/**
	 * 推送电子班牌的数据
	 * @param saveCheckWorks
	 */
	private String pushEclassCard(CheckWorkData checkWorkData,List<StusysSectionTimeSet> sectionTimeSets) {
		String unitId = checkWorkData.getUnitIdent();
        String placeId = checkWorkData.getPlaceIdent();
        //得到当前的课次
		Date attendanceDate = checkWorkData.getAttendanceDate();
		Integer sectionNumber;
		try {
			sectionNumber = getSectionNumber(attendanceDate,sectionTimeSets);
		} catch (BusinessErrorException e) {
			sectionNumber = null;
		}
		String cardNumber = checkWorkData.getUserIdent();
		Student student = getStudent(unitId, cardNumber);
		String studentId = student.getId();
		String key = "kade.remote." + studentId + placeId + sectionNumber;
		//过滤掉重复添加的考勤数据
		String result = RedisUtils.get(key);
		if (StringUtils.isNotBlank(result)){
			return result;
		}else{
			try {
				result = String.valueOf(eclasscardRemoteService.stuClassAttance(unitId, studentId, placeId, sectionNumber, attendanceDate));
//				result = "1";
			} catch (Exception e) {
				result = "-1";
				log.error("调用保存考勤数据的接口失败：------------" + e.getMessage());
			}
			if("1".equals(result)){
				RedisUtils.setObject(key, String.valueOf(result), RedisUtils.TIME_ONE_HOUR);
			}
		}
		return result;
	}

	/**
	 * 得到sectionNumer
	 * @param attendanceDate
	 * @param sectionTimeSets
	 * @return
	 * @throws BusinessErrorException 
	 */
	private Integer getSectionNumber(Date attendanceDate,
			List<StusysSectionTimeSet> sectionTimeSets) throws BusinessErrorException {
		Integer sectionNumber = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String dateString = sdf.format(attendanceDate); 
			Date time = sdf.parse(dateString);
			if(CollectionUtils.isNotEmpty(sectionTimeSets)){
				for (StusysSectionTimeSet stusysSectionTimeSet : sectionTimeSets) {
					Date beginTime = sdf.parse(stusysSectionTimeSet.getBeginTime());
					Date endTime = sdf.parse(stusysSectionTimeSet.getEndTime());
				    if(time.before(endTime) && time.after(beginTime)){
					   sectionNumber = stusysSectionTimeSet.getSectionNumber();
					   break;
				    }
				}
			}
		} catch (ParseException e) {
			throw new BusinessErrorException("attendanceDate转换日期出错格式不对，请调整HH:mm");
		}
		return sectionNumber;
	}
	
	/**
	 * 得到刷卡的学生
	 * @param unitId
	 * @param cardNum
	 * @return
	 */
	private Student getStudent (String unitId,String cardNumber){
		List<Student> students = getStudents(unitId);
		Map<String, Student> cardNumMap = EntityUtils.getMap(students, Student::getCardNumber);
		Student student;
		if(cardNumMap == null || cardNumMap.isEmpty() || cardNumMap.get(cardNumber)== null){
			student = SUtils.dc(studentRemoteService.findByCardNumber(unitId, cardNumber), Student.class);
			RedisUtils.del(getKey("kade.remote.student", unitId));
		}else{
			student = cardNumMap.get(cardNumber);
		}
		return student;
	}
	
	/**
	 * 获取所有的班级
	 * @param unitId
	 * @return
	 */
	 private List<Student> getStudents(String unitId){	
	    	String studentKey = getKey("kade.remote.student", unitId);
	    	List<Student> students = SUtils.dt(RedisUtils.get(studentKey, new RedisInterface<String>() {
				@Override
				public String queryData() {
					return studentRemoteService.findBySchoolId(unitId);
				}
	         })
	        ,Student.class);
	    	return students;
	 }
	 
	 /**
		 * 得到缓存的key
		 */
	private String getKey(String name, String unitId) {
		return getKey(name,unitId,null,null);
	}
		
	private String getKey(String name,String unitId, String acadyear, Integer semester){
		StringBuilder key = new StringBuilder();
		key.append(name);
		if(StringUtils.isNotBlank(unitId))
		   key.append(unitId);
		if(StringUtils.isNotBlank(acadyear))
		   key.append(acadyear);
		if(semester != null)
		   key.append(semester);
		return key.toString();
	}
	
	/**
	 * 从缓存中获取当前单位的节次
	 */
	private List<StusysSectionTimeSet> getUnitSectionTimeSets (String unitId,String acadyear, Integer semester){
		String sectionTimeKey = getKey("kade.remote.sectionTime", unitId, acadyear, semester);
		List<StusysSectionTimeSet> times = SUtils.dt(RedisUtils.get(sectionTimeKey, new RedisInterface<String>() {
					@Override
					public String queryData() {
						return stusysSectionTimeSetRemoteService
								.findByAcadyearAndSemesterAndUnitId(acadyear, semester, unitId, null, true);
					}
		         })
		       , StusysSectionTimeSet.class);
		return times;
	}
	
	private void getReturnMsg(Map<String, String> returnMsg, int errorNum,
			StringBuilder errorMsg) {
		if(returnMsg != null) {
			returnMsg.put(BaseSaveConstant.PROVING_BASE_SAVE_KEY, String.valueOf(errorNum));
			if(errorNum > 0){
				returnMsg.put(BaseSaveConstant.PROVING_BASE_SAVE_ERROR_KEY, errorMsg.toString());
			}
		}
	}
	private void getReturnMsg(Map<String, String> returnMsg, StringBuilder errorMsg) {
		int errorNum = 0;
		if(!MapUtils.isEmpty(returnMsg)) {
			if(StringUtils.isNotBlank(errorMsg.toString())){
				String[] errorIds = errorMsg.toString().split(",");
				errorNum = errorIds.length;
			}
		}
		getReturnMsg(returnMsg,errorNum,errorMsg);
	}

}
