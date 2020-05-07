package net.zdsoft.eclasscard.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.eclasscard.data.entity.EccClassAttence;
import net.zdsoft.eclasscard.data.entity.EccStuclzAttence;
import net.zdsoft.eclasscard.data.service.EccClassAttenceService;
import net.zdsoft.eclasscard.data.service.EccStuclzAttenceService;
import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;


@Controller
@RequestMapping("/mobile/open/eclasscard")
public class RemoteEclasscardAction extends MobileAction{
	
	
	@Autowired
	private EccClassAttenceService eccClassAttenceService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private EccStuclzAttenceService eccStuclzAttenceService;
	
	@RequestMapping("/classSign")
	public String classSign(String syncUserId,Date date,ModelMap map){
		User user = null;
		if(StringUtils.isNotBlank(syncUserId)) {
			user =  SUtils.dc(userRemoteService.findOneById(syncUserId),User.class);
			if (user == null || user.getIsDeleted()!=0) {
				return errorFtl(map, "用户不存在或者已删除");
			}
			
		}else {
			return errorFtl(map, "微课传输的用户id为空");
		}
		
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(date==null) {
			date = new Date();
		}
		List<EccClassAttence> classAttences = eccClassAttenceService.findListByStuAtt(user.getOwnerId(), date, user.getUnitId());
		map.put("date", DateUtils.date2StringByDay(date));
		map.put("classAttences", classAttences);
		map.put("syncUserId", syncUserId);		
		return "/eclasscard/mobileh5/eccClassAttance/classSign.ftl";
	}

	
	@RequestMapping("/classStudentSign")
	public String classStudentSign(String id,String syncUserId,ModelMap map){
		
		if(StringUtils.isNotBlank(id)) {
			EccClassAttence classAttence = eccClassAttenceService.findByIdFillName(id);
			if(classAttence!=null&&classAttence.getClassType()>0){
				List<EccStuclzAttence> eccStuclzAttences = eccStuclzAttenceService.findListByClassAttId(classAttence.getId(),classAttence.getClassType(),classAttence.getClassId());
				Map<String,User> userMap = Maps.newHashMap();
				Set<String> ownerIds = EntityUtils.getSet(eccStuclzAttences, "studentId");
				if(ownerIds.size()>0){
					List<User> users = SUtils.dt(userRemoteService.findByOwnerIds(ownerIds.toArray(new String[ownerIds.size()])),new TR<List<User>>() {});
					userMap = EntityUtils.getMap(users, "ownerId");
				}
			
				for(EccStuclzAttence attence:eccStuclzAttences){
					if(userMap.containsKey(attence.getStudentId())){
						User user = userMap.get(attence.getStudentId());
						if(user!=null){
							attence.setStuUserName(user.getUsername());
						}
					}
				}
				map.put("eccStuclzAttences", eccStuclzAttences);
			}else{
				classAttence = new EccClassAttence();
			}
			map.put("classAttence", classAttence);
			map.put("syncUserId", syncUserId);
		}else {
			return errorFtl(map, "班级id为空");
		}
		
		return "/eclasscard/mobileh5/eccClassAttance/classStudentSign.ftl";
	}
	
}
