package net.zdsoft.stuwork.data.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.stuwork.data.dao.DyPermissionDao;
import net.zdsoft.stuwork.data.entity.DyPermission;
import net.zdsoft.stuwork.data.service.DyPermissionService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("dyPermissionService")
public class DyPermissionServiceImpl  extends BaseServiceImpl<DyPermission, String> implements DyPermissionService{

	@Autowired
	private DyPermissionDao dyPermissionDao;
	@Autowired
    private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private TeachClassRemoteService  teachClassRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<DyPermission, String> getJpaDao() {
		return dyPermissionDao;
	}

	@Override
	protected Class<DyPermission> getEntityClass() {
		return DyPermission.class;
	}

	@Override
	public List<DyPermission> findListByUnitId(String unitId,String classsType,String permissionType) {
		return dyPermissionDao.findListByUnitId(unitId ,classsType,permissionType);
	}

	@Override
	public void savePermission(String[] classIds, String[] userIds,String unitId,boolean isAll,String classsType,String permissionType) {
		if(!isAll){
			dyPermissionDao.deleteByClassId(classIds[0] ,classsType,permissionType );
		}
		List<DyPermission> permissions = Lists.newArrayList();
		if(userIds!=null&&userIds.length>0){
			List<DyPermission> dyPermissions = dyPermissionDao.findListByUserIds(classsType,permissionType,userIds);
			Map<String,Set<String>> userClassMap= Maps.newHashMap();
			for(DyPermission dyPermission:dyPermissions){
				Set<String> claIds = Sets.newHashSet();
				if(userClassMap.containsKey(dyPermission.getUserId())){
					claIds = userClassMap.get(dyPermission.getUserId());
				}
				claIds.add(dyPermission.getClassId());
				userClassMap.put(dyPermission.getUserId(), claIds);
			}
			for(String userId:userIds){
				Set<String> clzIds = userClassMap.get(userId);
				for(String classId:classIds){
					if(CollectionUtils.isNotEmpty(clzIds)&&clzIds.contains(classId)){
						continue;
					}
					DyPermission permission = new DyPermission();
					permission.setId(UuidUtils.generateUuid());
					permission.setClassId(classId);
					permission.setUnitId(unitId);
					permission.setUserId(userId);
					permission.setClassType(classsType);
					permission.setPermissionType(permissionType);
					permissions.add(permission);
				}
			}
		}
		if(permissions.size()>0){
			saveAll(permissions.toArray(new DyPermission[permissions.size()]));
		}
		
		
	}

	@Override
	public Set<String> findClassSetByUserId(String userId) {
		List<DyPermission> dyPermissions = dyPermissionDao.findListByUserId(userId ,DyPermission.CLASS_TYPE_NORMAL,DyPermission.PERMISSION_TYPE_STUWORK);
		Set<String> classIds = EntityUtils.getSet(dyPermissions, "classId");
		User user = SUtils.dt(userRemoteService.findOneById(userId),new TR<User>() {});
		if(user!=null){
			List<Clazz> clazzs = SUtils.dt(classRemoteService.findByTeacherId(user.getOwnerId()),new TR<List<Clazz>>() {});
			for(Clazz clazz:clazzs){
				classIds.add(clazz.getId());
			}
			List<Grade> grades = SUtils.dt(gradeRemoteService.findByTeacherId(user.getOwnerId()),new TR<List<Grade>>() {});
			Set<String> gradeIds = EntityUtils.getSet(grades, "id");
			if(gradeIds.size()>0){
				List<Clazz> clazzGrades = SUtils.dt(classRemoteService.findByInGradeIds(gradeIds.toArray(new String[0])),new TR<List<Clazz>>() {});
				for(Clazz clazz:clazzGrades){
					classIds.add(clazz.getId());
				}
			}
		}
		return classIds;
	}

	@Override
	public Set<String> findClassSetByUserIdClaType(String userId, String classType, String permissionType) {
		List<DyPermission> dyPermissions = dyPermissionDao.findListByUserId(userId ,classType,permissionType);
		Set<String> classIds = dyPermissions.stream().map(DyPermission::getClassId).collect(Collectors.toSet());
		User user = SUtils.dt(userRemoteService.findOneById(userId),new TR<User>() {});
		Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1,user.getUnitId()), Semester.class);
		if(user!=null){
			List<Grade> grades = SUtils.dt(gradeRemoteService.findByTeacherId(user.getOwnerId()), new TR<List<Grade>>() {
			});
			Set<String> gradeIds = grades.stream().map(Grade::getId).collect(Collectors.toSet());

			if(DyPermission.CLASS_TYPE_NORMAL.equals(classType)) {
				List<Clazz> clazzs = SUtils.dt(classRemoteService.findByTeacherId(user.getOwnerId()), new TR<List<Clazz>>() {
				});
				List<ClassTeaching>   classTeachingList = SUtils.dt(classTeachingRemoteService.findClassTeachingListByTeacherId(user.getUnitId(),user.getOwnerId()) ,ClassTeaching.class) ;
				if(CollectionUtils.isNotEmpty( clazzs)){
					for (Clazz clazz : clazzs) {
						classIds.add(clazz.getId());
					}
				}

				if(CollectionUtils.isNotEmpty( classTeachingList)){
					for (ClassTeaching classTeaching : classTeachingList) {
						classIds.add(classTeaching.getClassId());
					}
				}
				if (gradeIds.size() > 0) {
					List<Clazz> clazzGrades = SUtils.dt(classRemoteService.findByInGradeIds(gradeIds.toArray(new String[0])), new TR<List<Clazz>>() {
					});
					for (Clazz clazz : clazzGrades) {
						classIds.add(clazz.getId());
					}
				}
			}else{
				List<TeachClass> teachClassList = SUtils.dt(teachClassRemoteService.findListByTeacherId(user.getOwnerId(),semester.getAcadyear(),String.valueOf(semester.getSemester())) , TeachClass.class);
				classIds.addAll(teachClassList.stream().map(TeachClass::getId).collect(Collectors.toList()));
				if (gradeIds.size() > 0) {
					List<TeachClass> teaClaList = SUtils.dt(teachClassRemoteService.findTeachClassList(user.getUnitId(),semester.getAcadyear(),String.valueOf(semester.getSemester()),null,gradeIds.toArray(new String[0]),true), TeachClass.class);
					classIds.addAll(teaClaList.stream().map(TeachClass::getId).collect(Collectors.toList()));
				}

			}
		}

		return classIds;
	}

}
