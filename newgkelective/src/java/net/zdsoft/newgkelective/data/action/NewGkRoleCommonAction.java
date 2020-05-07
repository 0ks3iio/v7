package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CustomRoleRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;

public class NewGkRoleCommonAction extends BaseAction{
	@Autowired
	UserRemoteService userRemoteService;
	@Autowired
	ClassRemoteService classRemoteService;
	@Autowired
	GradeRemoteService gradeRemoteService;
	@Autowired
	CustomRoleRemoteService customRoleRemoteService;
	
	public static final String SUBSYSTEM_86="86";
	public static final String EDUCATION_CODE ="86_edu_admin";
	public static final String ROLE_ADMIN="1";//管理员
	public static final String ROLE_GRADE="2";//年级老师
	public static final String ROLE_CLASS="3";//班主任

	/**
	 * 
	 * @param userId
	 * @return 管理员：1;年级老师 2;班主任 :3
	 */
	public Map<String,List<String>> findRoleByUserId(String unitId,String userId) {
		Map<String,List<String>> roleTypeMap = RedisUtils.getObject("REDIS.NK.ROLE."+userId, RedisUtils.TIME_HALF_MINUTE, new TypeReference<Map<String,List<String>>>(){}, new RedisInterface<Map<String,List<String>>>(){
			@Override
			public Map<String,List<String>> queryData() {
				Map<String,List<String>> map=new HashMap<>();
				if(isAdmin(unitId,userId)) {
					map.put(ROLE_ADMIN,new ArrayList<String>());
					return map;
				}
				User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
				String teacherId=user.getOwnerId();
				if(StringUtils.isEmpty(teacherId)) {
					return null;
				}
				List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId),Grade.class);
				if(CollectionUtils.isEmpty(gradeList)) {
					return new HashMap<>();
				}
				List<String> gradeIds=new ArrayList<>();
				List<String> gradeIds2=new ArrayList<>();
				for(Grade g:gradeList) {
					if(teacherId.equals(g.getTeacherId())) {
						gradeIds.add(g.getId());
					}else {
						gradeIds2.add(g.getId());
					}
				}
				if(CollectionUtils.isNotEmpty(gradeIds)) {
					map.put(ROLE_GRADE,gradeIds);
				}
				if(CollectionUtils.isEmpty(gradeIds2)) {
					return map;
				}
				List<String> clazzIds=new ArrayList<>();
				List<Clazz> clazzAllList = SUtils.dt(classRemoteService.findBySchoolIdInGradeIds(unitId,gradeIds2.toArray(new String[] {})), Clazz.class);
				if(CollectionUtils.isNotEmpty(clazzAllList)) {
					for(Clazz c:clazzAllList) {
						if(teacherId.equals(c.getTeacherId()) || teacherId.equals(c.getViceTeacherId())) {
							clazzIds.add(c.getId());
						}
					}
				}
				if(CollectionUtils.isNotEmpty(clazzIds)) {
					map.put(ROLE_CLASS,clazzIds);
				}
				return map;
			}
			/**
			 * 判断用户是不是教务管理员
			 * @param userId
			 * @return
			 */
			private boolean isAdmin(String unitId,String userId) {
				return customRoleRemoteService.checkUserRole(unitId, SUBSYSTEM_86, EDUCATION_CODE, userId);
			}
		});
		return roleTypeMap;
	}
}
