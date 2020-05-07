package net.zdsoft.eclasscard.data.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccStuclzAttenceDao;
import net.zdsoft.eclasscard.data.dto.ClassAttNumSumDto;
import net.zdsoft.eclasscard.data.entity.EccClassAttence;
import net.zdsoft.eclasscard.data.entity.EccStuclzAttence;
import net.zdsoft.eclasscard.data.service.EccClassAttenceService;
import net.zdsoft.eclasscard.data.service.EccStuclzAttenceService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.service.OpenApiNewElectiveService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
@Service("eccStuclzAttenceService")
public class EccStuclzAttenceServiceImpl extends BaseServiceImpl<EccStuclzAttence, String>
		implements EccStuclzAttenceService {
	public static final String KEY = "eclasscard.class.stuattence.";
	@Autowired
	private EccStuclzAttenceDao eccStuclzAttenceDao;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
    private ClassRemoteService classRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private EccClassAttenceService eccClassAttenceService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	  private static OpenApiNewElectiveService openApiNewElectiveService;

	    public OpenApiNewElectiveService getOpenApiNewElectiveService() {
	        if (openApiNewElectiveService == null) {
	        	openApiNewElectiveService = Evn.getBean("openApiNewElectiveService");
	            if(openApiNewElectiveService == null){
					System.out.println("openApiNewElectiveService为null，需开启dubbo服务");
				}
	        }
	        return openApiNewElectiveService;
	    }
	@Override
	protected BaseJpaRepositoryDao<EccStuclzAttence, String> getJpaDao() {
		return eccStuclzAttenceDao;
	}

	@Override
	protected Class<EccStuclzAttence> getEntityClass() {
		return EccStuclzAttence.class;
	}

	@Override
	public EccStuclzAttence findByStuIdClzAttId(String studentId,
			String classAttId) {
		return eccStuclzAttenceDao.findByStuIdClzAttId(studentId, classAttId);
	}

	@Override
	public List<EccStuclzAttence> findListByClassAttId(String classAttId,
			int classType, String classId,String type) {
		List<EccStuclzAttence> eccStuclzAttences  = findListByAttIdWithMaster(classAttId);
		if("1".equals(type)&&CollectionUtils.isEmpty(eccStuclzAttences)){
			eccStuclzAttences = Lists.newArrayList();
			List<Student> students = Lists.newArrayList();
			
			if(EccConstants.CLASS_TYPE_NORMAL==(classType)){
				students = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}),new TR<List<Student>>() {});
			}else if(EccConstants.CLASS_TYPE_4==(classType)){
				List<String> stuIds = getOpenApiNewElectiveService().getStusByClassId(classId);
				if(stuIds.size()>0){
					students = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])),new TR<List<Student>>() {});
				}
			}else{
				List<TeachClassStu> teachClassStus = SUtils.dt(teachClassStuRemoteService.findStudentByClassIds(new String[]{classId}),new TR<List<TeachClassStu>>() {});
				Set<String> stuIds = EntityUtils.getSet(teachClassStus,TeachClassStu::getStudentId);
				if(stuIds.size()>0){
					students = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])),new TR<List<Student>>() {});
				}
			}
			for(Student student:students){
				EccStuclzAttence stuclzAttence = new EccStuclzAttence();
				stuclzAttence.setClassAttId(classAttId);
				stuclzAttence.setClassId(classId);
				stuclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS1);
				stuclzAttence.setId(UuidUtils.generateUuid());
				stuclzAttence.setStudentId(student.getId());
				eccStuclzAttences.add(stuclzAttence);
			}
			if(CollectionUtils.isNotEmpty(eccStuclzAttences)){//加锁查询，防止生成重复数据
				if(RedisUtils.hasLocked(EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+classAttId)){
					try{
						List<EccStuclzAttence> escAttences = findListByAttIdWithMaster(classAttId);
						if(CollectionUtils.isEmpty(escAttences)){
							saveAll(eccStuclzAttences.toArray(new EccStuclzAttence[eccStuclzAttences.size()]));
						}
					}finally{
						RedisUtils.unLock(EccConstants.CLOCK_IN_REDIS_LOCK_PREFIX+classAttId);
					}
				}
			}
		}
		fillNameData(eccStuclzAttences);
		return eccStuclzAttences;
	}
	
	@Override
	public List<EccStuclzAttence> findListByClassAttId(String classAttId,
			int classType, String classId) {
		List<EccStuclzAttence> eccStuclzAttences  = eccStuclzAttenceDao.findByClzAttId(classAttId);
		fillNameData(eccStuclzAttences);
		return eccStuclzAttences;
	}
	
	
	private void fillNameData(List<EccStuclzAttence> eccStuclzAttences){
		if(CollectionUtils.isNotEmpty(eccStuclzAttences)){
			Set<String> studentIds = EntityUtils.getSet(eccStuclzAttences, "studentId");
			if(studentIds.size()>0){
				List<Student> students = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[studentIds.size()])),Student.class);
				Map<String,Student> stuMap = EntityUtils.getMap(students, "id");
				Map<String,String> clzNameMap = Maps.newHashMap();
				Map<String,String> clzTeaNameMap = Maps.newHashMap();
				Set<String> classIds = EntityUtils.getSet(students, "classId");
				if(classIds.size()>0){
					List<Clazz> classes = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])),Clazz.class);
					Set<String> teacherIds = EntityUtils.getSet(classes, "teacherId");
					if(teacherIds.size()>0){
						List<Teacher> teachers = SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[0])),Teacher.class);
						Map<String,String> teacherMap = EntityUtils.getMap(teachers, "id","teacherName");
						for(Clazz clazz:classes){
							if(teacherMap.containsKey(clazz.getTeacherId())){
								clazz.setTeacherName(teacherMap.get(clazz.getTeacherId()));
							}
						}
					}
					clzNameMap = EntityUtils.getMap(classes, "id","classNameDynamic");
					clzTeaNameMap = EntityUtils.getMap(classes, "id","teacherName");
					
				}
				for(EccStuclzAttence stuclzAttence:eccStuclzAttences){
					if(stuMap.containsKey(stuclzAttence.getStudentId())){
						Student student = stuMap.get(stuclzAttence.getStudentId());
						if(student!=null){
							stuclzAttence.setStuRealName(student.getStudentName());
							stuclzAttence.setStuCode(student.getStudentCode());
							if(clzNameMap.containsKey(student.getClassId())){
								stuclzAttence.setClassName(clzNameMap.get(student.getClassId()));
							}
							if(clzTeaNameMap.containsKey(student.getClassId())){
								stuclzAttence.setTeacherName(clzTeaNameMap.get(student.getClassId()));
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void updateStatus(String[] ids,int status) {
		eccStuclzAttenceDao.updateStatus(status,ids);
	}

	@Override
	public List<ClassAttNumSumDto>  findByClassIdSum(String classId,Date beginDate,Date endDate) {
		List<ClassAttNumSumDto> numSumDtos = Lists.newArrayList();
		if(StringUtils.isBlank(classId)){
			return numSumDtos;
		}
		List<Student> students = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}),new TR<List<Student>>() {});
		Set<String> studentIds = EntityUtils.getSet(students, "id");
		Map<String,String> stuNameMap =  EntityUtils.getMap(students, "id","studentName");
		String bDate = DateUtils.date2StringByDay(beginDate);
		String eDate = DateUtils.date2StringByDay(endDate);
		if(studentIds.size()>0){
			Map<String,ClassAttNumSumDto> map = Maps.newHashMap();
			List<EccStuclzAttence> stuclzAttences = eccStuclzAttenceDao.findByClassIdSum(studentIds.toArray(new String[studentIds.size()]),bDate,eDate);
			Set<String> classAttIds = EntityUtils.getSet(stuclzAttences, "classAttId");
			Map<String,EccClassAttence> classAttMap = Maps.newHashMap();
			if(classAttIds.size()>0){
				List<EccClassAttence>  classAttences = eccClassAttenceService.findByIdsIsOver(classAttIds.toArray(new String[classAttIds.size()]));
				classAttMap = EntityUtils.getMap(classAttences, "id");
			}else{
				return numSumDtos;
			}
			for(EccStuclzAttence attence:stuclzAttences){
				if(classAttMap.containsKey(attence.getClassAttId())){
					EccClassAttence classAttence = classAttMap.get(attence.getClassAttId());
					if(attence.getClockDate()==null&&classAttence!=null&&
							(DateUtils.date2StringByDay(beginDate).compareTo(DateUtils.date2StringByDay(classAttence.getClockDate()))>0
							||DateUtils.date2StringByDay(endDate).compareTo(DateUtils.date2StringByDay(classAttence.getClockDate()))<0)){
						continue;
					}
					ClassAttNumSumDto numSumDto = null;
					if(map.containsKey(attence.getStudentId())){
						numSumDto = map.get(attence.getStudentId());
					}
					if(numSumDto==null){
						numSumDto = new ClassAttNumSumDto();
						numSumDto.setStudentId(attence.getStudentId());
					}
					if(EccConstants.CLASS_ATTENCE_STATUS1==attence.getStatus()){
						numSumDto.setQkNum(numSumDto.getQkNum()+1);
					}else if(EccConstants.CLASS_ATTENCE_STATUS2==attence.getStatus()){
						numSumDto.setCdNum(numSumDto.getCdNum()+1);
					}else if(EccConstants.CLASS_ATTENCE_STATUS3==attence.getStatus()){
						numSumDto.setQjNum(numSumDto.getQjNum()+1);
					}
					map.put(attence.getStudentId(), numSumDto);
				}
			}
			for (String key : map.keySet()) {
				numSumDtos.add(map.get(key));
			}
			for(ClassAttNumSumDto sumDto:numSumDtos){
				if(stuNameMap.containsKey(sumDto.getStudentId())){
					sumDto.setStudentName(stuNameMap.get(sumDto.getStudentId()));
				}
			}
		}
		return numSumDtos;
	}

	@Override
	public List<EccStuclzAttence> findByStudentIdSum(String studentId,
			String bDate, String eDate) {
		return eccStuclzAttenceDao.findByStudentIdSum(studentId, bDate, eDate);
	}

	@Override
	public List<EccStuclzAttence> findByClassAttIdsNeedPush(String[] attIds) {
		return eccStuclzAttenceDao.findByClassAttIdsNeedPush(attIds);
	}

	@Override
	public List<EccStuclzAttence> findListLeaveStudent(
			String[] classAttenceIds, String[] studentIds) {
		return eccStuclzAttenceDao.findListLeaveStudent(classAttenceIds,studentIds);
	}
	@Override
	public List<EccStuclzAttence> findListByAttIdWithMaster(String classAttId) {
		return eccStuclzAttenceDao.findByClzAttId(classAttId);
	}
	
	@Override
	public boolean findIsInitWithMaster(String classAttId) {
		Integer num = RedisUtils.getObject(KEY + classAttId +".isinit", 0, new TypeReference<Integer>() {
	        }, new RedisInterface<Integer>() {
	            @Override
	            public Integer queryData() {
	            	List<EccStuclzAttence> attences = eccStuclzAttenceDao.findByClzAttId(classAttId);
	            	if(attences.size() == 0){
	            		return null;
	            	}
	            	return attences.size();
	            }
	        });
		if(num!=null && num >0){
			return true;
		}
		return false;
	}
	@Override
	public Set<String> findListStuByAttIds(String[] attIds) {
		return eccStuclzAttenceDao.findListStuByAttIds(attIds);
	}

}
