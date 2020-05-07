package net.zdsoft.scoremanage.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dto.StudentDto;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.dao.ExamNumDao;
import net.zdsoft.scoremanage.data.entity.ExamNum;
import net.zdsoft.scoremanage.data.service.ExamNumService;

@Service("examNumService")
public class ExamNumServiceImpl extends BaseServiceImpl<ExamNum, String> implements ExamNumService{

	@Autowired
	private ExamNumDao examNumDao;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Override
	protected BaseJpaRepositoryDao<ExamNum, String> getJpaDao() {
		return examNumDao;
	}

	@Override
	protected Class<ExamNum> getEntityClass() {
		return ExamNum.class;
	}

	@Override
	public Map<String, String> findByExamId(String examId,String schoolId) {
		List<ExamNum> list=null;
		if(StringUtils.isNotBlank(schoolId)){
			 list=examNumDao.findByExamIdAndSchoolId(examId,schoolId);
		}else{
			list=examNumDao.findByExamId(examId);
		}
		
		Map<String, String> map=new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(list)){
			for(ExamNum item:list){
				map.put(item.getStudentId(), item.getExamNumber());
			}
		}
		return map;
	}
	
	@Override
	public List<ExamNum> findByExamIdList(String examId,String schoolId) {
		List<ExamNum> list=null;
		if(StringUtils.isNotBlank(schoolId)){
			 list=examNumDao.findByExamIdAndSchoolId(examId,schoolId);
		}else{
			list=examNumDao.findByExamId(examId);
		}
		return list;
	}

	@Override
	public void deleteByexamIdStudent(String examId, String[] stuIds) {
		if(stuIds!=null && stuIds.length>0){
			examNumDao.deleteByexamIdStudent(examId,stuIds);
		}
	}

	@Override
	public void deleteBySchoolIdExamId(String schoolId, String examId) {
		examNumDao.deleteBySchoolIdExamId(schoolId,examId);
	}

	@Override
	public void insertList(List<ExamNum> insertList,String schoolId,String examId) {
		deleteBySchoolIdExamId(schoolId,examId);
		if(CollectionUtils.isNotEmpty(insertList)){
			examNumDao.saveAll(insertList);
		}
	}

	@Override
	public List<ExamNum> saveAllEntitys(ExamNum... examNum) {
		return examNumDao.saveAll(checkSave(examNum));
	}

	@Override
	public void save(String examId, List<StudentDto> studentDtoList) {
		//数据保存:先删除该需要保存学生原来数据，然后新增
		Set<String> stuIds=new HashSet<String>();
		for(StudentDto item:studentDtoList){
			stuIds.add(item.getId());
		}
		Map<String,Student> stuMap=new HashMap<String, Student>();
		if(stuIds.size()>0){
			deleteByexamIdStudent(examId, stuIds.toArray(new String[0]));
			List<Student> stulist = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])), new TR<List<Student>>(){});
			if(CollectionUtils.isNotEmpty(stulist)){
				stuMap=EntityUtils.getMap(stulist, "id",StringUtils.EMPTY);
			}
		}
		List<ExamNum> examNumList=new ArrayList<ExamNum>();
		for(StudentDto item:studentDtoList){
			Student stu = stuMap.get(item.getId());
			if(stu!=null && StringUtils.isNotBlank(item.getExamNum())){
				ExamNum examNum=new ExamNum();
				examNum.setExamNumber(item.getExamNum());
				examNum.setStudentId(item.getId());
				examNum.setExamId(examId);
				examNum.setSchoolId(stu.getSchoolId());
				examNum.setId(UuidUtils.generateUuid());
				examNumList.add(examNum);
			}
		}
		if(CollectionUtils.isNotEmpty(examNumList)){
			saveAllEntitys(examNumList.toArray(new ExamNum[0]));
		}
		
	}

}
