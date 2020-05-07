package net.zdsoft.eclasscard.data.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccTeaclzAttenceDao;
import net.zdsoft.eclasscard.data.entity.EccTeaclzAttence;
import net.zdsoft.eclasscard.data.service.EccTeaclzAttenceService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
@Service("eccTeaclzAttenceService")
public class EccTeaclzAttenceServiceImpl extends
		BaseServiceImpl<EccTeaclzAttence, String> implements
		EccTeaclzAttenceService {
	@Autowired
	private EccTeaclzAttenceDao eccTeaclzAttenceDao;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Override
	protected BaseJpaRepositoryDao<EccTeaclzAttence, String> getJpaDao() {
		return eccTeaclzAttenceDao;
	}

	@Override
	protected Class<EccTeaclzAttence> getEntityClass() {
		return EccTeaclzAttence.class;
	}

	@Override
	public EccTeaclzAttence findByAttId(String classAttId) {
		return eccTeaclzAttenceDao.findByAttId(classAttId);
	}

	@Override
	public List<EccTeaclzAttence> findListByAttIds(String[] attIds) {
		return eccTeaclzAttenceDao.findListByAttIds(attIds);
	}

	@Override
	public List<EccTeaclzAttence> findByDeptIdSum(String deptId,
			Date beginDate, Date endDate) {
		List<EccTeaclzAttence> teaclzAttences = Lists.newArrayList();
		if(StringUtils.isBlank(deptId)){
			return teaclzAttences;
		}
		String bDate = DateUtils.date2StringByDay(beginDate);
		String eDate = DateUtils.date2StringByDay(endDate);
		List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByDeptId(deptId),Teacher.class);
		Set<String> teacherIds = EntityUtils.getSet(teachers, "id");
		Map<String,String> teaNameMap = EntityUtils.getMap(teachers, "id","teacherName");
		if(!(teacherIds.size()>0)){
			return teaclzAttences;
		}
		Map<String,EccTeaclzAttence> map = Maps.newHashMap();
		List<EccTeaclzAttence> etcas = eccTeaclzAttenceDao.findByBtTimeInTeaIds(bDate, eDate,teacherIds.toArray(new String[teacherIds.size()]));
		for(EccTeaclzAttence teaclzAttence:etcas){
			EccTeaclzAttence attence = null;
			if(map.containsKey(teaclzAttence.getTeacherId())){
				attence = map.get(teaclzAttence.getTeacherId());
			}
			if(attence==null){
				attence = new EccTeaclzAttence();
				attence.setTeacherId(teaclzAttence.getTeacherId());
			}
			if(EccConstants.CLASS_ATTENCE_STATUS1==teaclzAttence.getStatus()){
				attence.setWqdNum(attence.getWqdNum()+1);
			}else if(EccConstants.CLASS_ATTENCE_STATUS2==teaclzAttence.getStatus()){
				attence.setCdNum(attence.getCdNum()+1);
			}
			map.put(teaclzAttence.getTeacherId(), attence);
		}
		for(String key:map.keySet()){
			EccTeaclzAttence attence = map.get(key);
			if(attence!=null){
				if(teaNameMap.containsKey(attence.getTeacherId())){
					attence.setTeaRealName(teaNameMap.get(attence.getTeacherId()));
				}
				teaclzAttences.add(attence);
			}
		}
		return teaclzAttences;
	}

	@Override
	public List<EccTeaclzAttence> findByTeacherIdSum(String teacherId,
			String bDate, String eDate) {
		return eccTeaclzAttenceDao.findByTeacherIdSum(teacherId,bDate,eDate);
	}

}
