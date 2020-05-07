package net.zdsoft.tutor.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;







import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.tutor.data.dao.TutorResultDao;
import net.zdsoft.tutor.data.entity.TutorRecord;
import net.zdsoft.tutor.data.entity.TutorResult;
import net.zdsoft.tutor.data.service.TutorRecordService;
import net.zdsoft.tutor.data.service.TutorResultService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author yangsj  2017年9月11日下午8:28:33
 */
@Service
public class TutorResultServiceImpl extends BaseServiceImpl<TutorResult, String> implements TutorResultService {
    @Autowired
    private TutorResultDao tutorResultDao;
    @Autowired
    private TutorRecordService tutorRecordService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<TutorResult, String> getJpaDao() {
		// TODO Auto-generated method stub
		return tutorResultDao;
	}

	@Override
	protected Class<TutorResult> getEntityClass() {
		// TODO Auto-generated method stub
		return TutorResult.class;
	}

	@Override
	public List<TutorResult> findByUnitId(String unitId) {
		// TODO Auto-generated method stub
		return tutorResultDao.findByUnitId(unitId);
	}

	@Override
	public void deleteByRoundId(String tutorRoundId) {
		// TODO Auto-generated method stub
		tutorResultDao.deleteByRoundId(tutorRoundId);
	}

	@Override
	public List<TutorResult> findByTeacherId(String tid) {
		// TODO Auto-generated method stub
		return tutorResultDao.findByTeacherId(tid);
	}

	@Override
	public List<TutorResult> findByRoundId(String tutorRoundId) {
		// TODO Auto-generated method stub
		return tutorResultDao.findByRoundId(tutorRoundId);
	}

	@Override
	public TutorResult findByStudentId(String studentId) {
		// TODO Auto-generated method stub
		return tutorResultDao.findByStudentId(studentId);
	}
    
	@Override
	public String save(TutorResult tutorResult,String param,String teacherId) {
		if(RedisUtils.hasLocked(teacherId)){
			 try{
				 List<TutorResult> tutorResults1 = findByTeacherId(teacherId);
				 if(tutorResults1.size()>= Integer.valueOf(param)) {
					 return "error";
				 }else {
					 tutorResultDao.save(tutorResult);
					 return "success";
				 }
			  }catch(Exception e){
				  e.printStackTrace();
			  }finally{
				  RedisUtils.unLock(teacherId);
			  }
		}
		return "retry";
	}
	
	

	@Override
	public List<TutorResult> findByTeacherIds(String[] tids) {
		return tutorResultDao.findByTeacherIds(tids);
	}

	@Override
	public List<TutorResult> findByStudentIds(String[] studentIds) {
		return tutorResultDao.findByStudentIds(studentIds);
	}

	@Override
	public void updateTutor(String[] updateStuIds, String teacherId,String tutorId) {
		tutorId = StringUtils.isNotBlank(tutorId)?tutorId:"";
		tutorResultDao.updateTutor(updateStuIds,teacherId,tutorId,new Date());
		tutorRecordService.updateTeacherByStuIds(teacherId,updateStuIds,new Date());
	}

	@Override
	public String doImport(String unitId, List<String[]> datas) {
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		String[] errorData=null;
		
		List<Student> studentList=SUtils.dt(studentRemoteService.findBySchoolIdIn(null,new String[]{unitId}), new TR<List<Student>>(){});
		List<Teacher> teacherList=SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>(){});
		//key - student
		Map<String,Student> studentMap = EntityUtils.getMap(studentList, "studentCode"); 
		//key - teacher
		Map<String,Teacher> teacherMap = EntityUtils.getMap(teacherList, "teacherCode"); 
		Map<String,TutorResult> stuResultMap = Maps.newHashMap();
		List<TutorResult> tutorResults = Lists.newArrayList();
		for(int i =0;i< datas.size();i++){
			String[] dataArr = datas.get(i);
			int length=dataArr.length;
			String studentCode=length>0?dataArr[0]:"";
			if(StringUtils.isNotBlank(studentCode)){
				studentCode=studentCode.trim();
			}else{
				studentCode="";
			}
			Student	stu=studentMap.get(studentCode);
			if(stu==null){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学号";
				errorData[2]=studentCode;
				errorData[3]="学号有误";
				errorDataList.add(errorData);
				continue;
			}
			String studentName=length>1?dataArr[1]:"";
			if(StringUtils.isNotBlank(studentName)){
				studentName=studentName.trim();
			}else{
				studentName="";
			}
			if(!stu.getStudentName().equals(studentName)){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="姓名";
				errorData[2]=studentName;
				errorData[3]="学生姓名有误，或该姓名和学号不匹配";
				errorDataList.add(errorData);
				continue;
			}
			String teacherCode=length>2?dataArr[2]:"";
			if(StringUtils.isNotBlank(teacherCode)){
				teacherCode=teacherCode.trim();
			}else{
				teacherCode="";
			}
			Teacher	teacher=teacherMap.get(teacherCode);
			if(teacher==null){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="教师编号";
				errorData[2]=teacherCode;
				errorData[3]="教师编号有误";
				errorDataList.add(errorData);
				continue;
			}
			String teacherName=length>3?dataArr[3]:"";
			if(StringUtils.isNotBlank(teacherName)){
				teacherName=teacherName.trim();
			}else{
				teacherName="";
			}
			if(!teacher.getTeacherName().equals(teacherName)){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="教师姓名";
				errorData[2]=teacherName;
				errorData[3]="教师姓名有误，或该教师姓名和教师编号不匹配";
				errorDataList.add(errorData);
				continue;
			}
			if(stuResultMap.containsKey(stu.getId())){
				TutorResult tutorResult = stuResultMap.get(stu.getId());
				tutorResult.setTeacherId(teacher.getId());
				stuResultMap.put(tutorResult.getStudentId(), tutorResult);
			}else{
				TutorResult tutorResult = new TutorResult();
				tutorResult.setId(UuidUtils.generateUuid());
				tutorResult.setRoundId("");
				tutorResult.setState(TutorResult.STATE_NORMAL);
				tutorResult.setUnitId(unitId);
				tutorResult.setStudentId(stu.getId());
				tutorResult.setTeacherId(teacher.getId());
				tutorResult.setCreationTime(new Date());
				tutorResult.setModifyTime(new Date());
				stuResultMap.put(tutorResult.getStudentId(), tutorResult);
			}
		}
		for(String key : stuResultMap.keySet()){
			tutorResults.add(stuResultMap.get(key));
		}
		List<TutorResult> oldTutorResults = findByUnitId(unitId);
		Map<String,TutorResult> oldTutorMap = EntityUtils.getMap(oldTutorResults, "studentId");
		List<TutorResult> insertList = Lists.newArrayList();
		List<TutorResult> updateList = Lists.newArrayList();
		for(TutorResult result:tutorResults){
			if(oldTutorMap.containsKey(result.getStudentId())){
				updateList.add(result);
			}else{
				insertList.add(result);
			}
		}
		if(insertList.size()>0){
			saveAll(insertList.toArray(new TutorResult[insertList.size()]));
		}
		if(updateList.size()>0){
			updateAll(updateList);
		}
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", datas.size()-errorDataList.size());
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}

	@Override
	public void updateAll(List<TutorResult> updateList) {
		Map<String,String> stuTeaIdMap = EntityUtils.getMap(updateList, "studentId","teacherId");
		Set<String> stuIds = EntityUtils.getSet(updateList, "studentId");
		List<TutorResult> results = findByStudentIds(stuIds.toArray(new String[stuIds.size()]));
		for(TutorResult result:results){
			if(stuTeaIdMap.containsKey(result.getStudentId())&&StringUtils.isNotBlank(stuTeaIdMap.get(result.getStudentId()))){
				result.setTeacherId(stuTeaIdMap.get(result.getStudentId()));
			}
		}
		if(results.size()>0){
			saveAll(results.toArray(new TutorResult[results.size()]));
		}
		List<TutorRecord> tutorRecords = tutorRecordService.findByStudentIds(stuIds.toArray(new String[stuIds.size()]));
		for(TutorRecord record:tutorRecords){
			if(stuTeaIdMap.containsKey(record.getStudentId())&&StringUtils.isNotBlank(stuTeaIdMap.get(record.getStudentId()))){
				record.setTeacherId(stuTeaIdMap.get(record.getStudentId()));
			}
		}
		if(tutorRecords.size()>0){
			tutorRecordService.saveAll(tutorRecords.toArray(new TutorRecord[tutorRecords.size()]));
		}
	}

	@Override
	public List<TutorResult> findByStudentIdDel(String studentId) {
		return tutorResultDao.findByStudentIdDel(studentId);
	}

	@Override
	public void updateStateByRoundId(Integer state, String... roundIds) {
		tutorResultDao.updateStateByRoundId(state,roundIds);
	}

	/**
	@Override
	public TutorResult findByStudentIdAndSection(String studentId,
			Integer section) {
		return tutorResultDao.findByStudentIdAndSection(studentId,section);
	}
	@Override
	public List<TutorResult> findByStudentIdDelAndSection(String studentId,
			Integer section) {
		return tutorResultDao.findByStudentIdDelAndSection(studentId,section);
	}
	@Override
	public List<TutorResult> findByUnitIdAndSection(String unitId,
			Integer section) {
		return tutorResultDao.findByUnitIdAndSection(unitId,section);
	}
	
	 */

}
