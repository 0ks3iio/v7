package net.zdsoft.eclasscard.data.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.DingdingMsgRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.DormBuildingDto;
import net.zdsoft.eclasscard.data.dto.PushDingMsgDto;
import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeSet;
import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeUser;
import net.zdsoft.eclasscard.data.entity.EccDormAttence;
import net.zdsoft.eclasscard.data.entity.EccStuclzAttence;
import net.zdsoft.eclasscard.data.entity.EccStudormAttence;
import net.zdsoft.eclasscard.data.service.EccAttenceNoticeSetService;
import net.zdsoft.eclasscard.data.service.EccAttenceNoticeUserService;
import net.zdsoft.eclasscard.data.service.EccClassAttenceService;
import net.zdsoft.eclasscard.data.service.EccDingMsgPushService;
import net.zdsoft.eclasscard.data.service.EccDormAttenceService;
import net.zdsoft.eclasscard.data.service.EccStuclzAttenceService;
import net.zdsoft.eclasscard.data.service.EccStudormAttenceService;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.DdMsgUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("eccDingMsgPushService")
public class EccDingMsgPushServiceImpl implements EccDingMsgPushService {

	@Autowired
	private EccClassAttenceService eccClassAttenceService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
    private ClassRemoteService classRemoteService;
	@Autowired
    private GradeRemoteService gradeRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private EccStuclzAttenceService eccStuclzAttenceService;
	@Autowired
	private EccDormAttenceService eccDormAttenceService;
	@Autowired
	private EccStudormAttenceService eccStudormAttenceService;
	@Autowired
	private EccAttenceNoticeSetService eccAttenceNoticeSetService;
	@Autowired
	private EccAttenceNoticeUserService eccAttenceNoticeUserService;
	@Autowired
	private DingdingMsgRemoteService dingdingMsgRemoteService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	private static OpenApiOfficeService openApiOfficeService;

    public OpenApiOfficeService getOpenApiOfficeService() {
        if (openApiOfficeService == null) {
            openApiOfficeService = Evn.getBean("openApiOfficeService");
            if(openApiOfficeService == null){
				System.out.println("openApiOfficeService为null，需开启dubbo服务");
			}
        }
        return openApiOfficeService;
    }
	
	@Override
	public void pushDingMsgTaskRun(String[] attIds,String type,String unitId,Integer sectionNumber){
		if(EccConstants.CLASS_ATTENCE_SET_TYPE1.equals(type)){
			if(attIds!=null&& attIds.length>0){
				classAttPushDingMsg(attIds,unitId,sectionNumber);
			}
		}
//		else if(EccConstants.DORM_ATTENCE_SET_TYPE2.equals(type)){
//			if(attIds!=null&& attIds.length>0){
//				dormAttPushDingMsg(attIds,unitId);
//			}
//		}
	}

	private void classAttPushDingMsg(String[] attIds,String unitId,Integer sectionNumber) {
		List<EccAttenceNoticeSet> noticeSets = eccAttenceNoticeSetService.findListBy("unitId", unitId);
		EccAttenceNoticeSet noticeSet = new EccAttenceNoticeSet();
		for(EccAttenceNoticeSet attenceNoticeSet:noticeSets){
			if(EccConstants.CLASS_ATTENCE_SET_TYPE1.equals(attenceNoticeSet.getType())){
				noticeSet = attenceNoticeSet;
			}
		}
		List<EccAttenceNoticeUser> attenceNoticeUsers = eccAttenceNoticeUserService.findByUnitIdAndType(unitId,EccConstants.CLASS_ATTENCE_SET_TYPE1);
		Set<String> otherUserIds = EntityUtils.getSet(attenceNoticeUsers, "userId");
		List<PushDingMsgDto> pushDingMsgDtos = getPushMsgStr(attIds,1);
		
		pushMsg(unitId, 1, pushDingMsgDtos, noticeSet, sectionNumber, otherUserIds,"", null);
	}
	

	/**
	 * 
	 * @param attIds
	 * @param type 1.上课  2.寝室
	 * @return
	 */
	private List<PushDingMsgDto> getPushMsgStr(String[] attIds,int type) {
		List<PushDingMsgDto> pushDingMsgDtos = Lists.newArrayList();
		Map<String,String> clzGradeMap =Maps.newHashMap();
		Map<String,String> clzTeaMap =Maps.newHashMap();
		Map<String,String> gradeTeaMap =Maps.newHashMap();
		Map<String,String> userOwnerMap =Maps.newHashMap();
		Map<String,String> classMap = Maps.newHashMap();
		Map<String,String> stuAttMap = Maps.newHashMap();
		Set<String> stuIds = Sets.newHashSet();
		if(1==type){
			List<EccStuclzAttence> eccStuclzAttences = eccStuclzAttenceService.findByClassAttIdsNeedPush(attIds);
			stuIds = EntityUtils.getSet(eccStuclzAttences, "studentId");stuIds.remove(null);
			stuAttMap = EntityUtils.getMap(eccStuclzAttences, "studentId", "classAttId");
		}else{
			List<EccStudormAttence> studormAttences = eccStudormAttenceService.findByDormAttIdsNeedPush(attIds);
			stuIds = EntityUtils.getSet(studormAttences, "studentId");stuIds.remove(null);
			stuAttMap = EntityUtils.getMap(studormAttences, "studentId", "dormAttId");
		}
		if(stuIds.size()>0){
			List<Student> students = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[stuIds.size()])),new TR<List<Student>>() {});
			Set<String> clzIds = EntityUtils.getSet(students, "classId");
			if(clzIds.size()>0){
				List<Clazz> classes = SUtils.dt(classRemoteService.findListByIds(clzIds.toArray(new String[0])),Clazz.class);
				Set<String> gradeIds = EntityUtils.getSet(classes, "gradeId");gradeIds.remove(null);
				clzGradeMap = EntityUtils.getMap(classes, "id", "gradeId");
				clzTeaMap = EntityUtils.getMap(classes, "id", "teacherId");
				Set<String> cteaIds = EntityUtils.getSet(classes, "teacherId");
				if(gradeIds.size()>0){
					List<Grade> grades = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[gradeIds.size()])),new TR<List<Grade>>() {});
					Set<String> gteaIds = EntityUtils.getSet(grades, "teacherId");
					gradeTeaMap = EntityUtils.getMap(grades, "id", "teacherId");
					cteaIds.addAll(gteaIds);
				}
				cteaIds.remove(null);
				if(cteaIds.size()>0){
					List<User> users = SUtils.dt(userRemoteService.findByOwnerIds(cteaIds.toArray(new String[cteaIds.size()])),new TR<List<User>>() {});
					userOwnerMap = EntityUtils.getMap(users, "ownerId", "id");
				}
				if(CollectionUtils.isNotEmpty(classes)){
					for (Clazz c : classes) {
						classMap.put(c.getId(), c.getClassNameDynamic());
					}
				}
			}
			for(Student student:students){
				PushDingMsgDto dingMsgDto = new PushDingMsgDto();
				dingMsgDto.setStudentId(student.getId());
				dingMsgDto.setStudentName(student.getStudentName());
				dingMsgDto.setClassId(student.getClassId());
				//行政班
				if(StringUtils.isNotBlank(dingMsgDto.getClassId())&&classMap.containsKey(dingMsgDto.getClassId())){
					dingMsgDto.setClassAllName(classMap.get(dingMsgDto.getClassId()));
				}else{
					dingMsgDto.setClassAllName("班级");
				}
				if(StringUtils.isNotBlank(dingMsgDto.getClassId())&&clzTeaMap.containsKey(dingMsgDto.getClassId())){
					dingMsgDto.setClassTeaId(clzTeaMap.get(dingMsgDto.getClassId()));
				}
				//班主任userId
				if(StringUtils.isNotBlank(dingMsgDto.getClassTeaId())&&userOwnerMap.containsKey(dingMsgDto.getClassTeaId())){
					dingMsgDto.setClassTeaUserId(userOwnerMap.get(dingMsgDto.getClassTeaId()));
				}
				
				if(StringUtils.isNotBlank(dingMsgDto.getClassId())&&clzGradeMap.containsKey(dingMsgDto.getClassId())){
					dingMsgDto.setGradeId(clzGradeMap.get(dingMsgDto.getClassId()));
				}
				if(StringUtils.isNotBlank(dingMsgDto.getGradeId())&&gradeTeaMap.containsKey(dingMsgDto.getGradeId())){
					dingMsgDto.setGradeTeaId(gradeTeaMap.get(dingMsgDto.getGradeId()));
				}
				//年级组长userId
				if(StringUtils.isNotBlank(dingMsgDto.getGradeTeaId())&&userOwnerMap.containsKey(dingMsgDto.getGradeTeaId())){
					dingMsgDto.setGradeTeaUserId(userOwnerMap.get(dingMsgDto.getGradeTeaId()));
				}
				if(stuAttMap.containsKey(student.getId())){
					dingMsgDto.setAttId(stuAttMap.get(student.getId()));
				}
				pushDingMsgDtos.add(dingMsgDto);
			}
		}
		
		return pushDingMsgDtos;
	}
	
	@Override
	public void dormAttPushDingMsg(List<EccDormAttence> dormAttences,
			String unitId, Set<String> otherUserIds,EccAttenceNoticeSet noticeSet,String periodStr,Set<String> studentIds) {
		Set<String> dormAttenceIds = EntityUtils.getSet(dormAttences, "id");
		List<PushDingMsgDto> pushDingMsgDtos = getPushMsgStr(dormAttenceIds.toArray(new String[0]),2);
		pushMsg(unitId, 2, pushDingMsgDtos, noticeSet, 0, otherUserIds,periodStr,studentIds);
	}

	private void pushMsg(String unitId,int type,List<PushDingMsgDto> pushDingMsgDtos,EccAttenceNoticeSet noticeSet,int sectionNumber,Set<String> otherUserIds,String periodStr,Set<String> studentIds){
		
		Set<String> gradeIds = EntityUtils.getSet(pushDingMsgDtos, "gradeId");//年级Ids
		Set<String> attIds = EntityUtils.getSet(pushDingMsgDtos, "attId");//考勤Ids
		Set<String> gtUserIds = EntityUtils.getSet(pushDingMsgDtos, "gradeTeaUserId");//年级组长userIds
		gtUserIds.remove(null);
		Set<String> ctUserIds = EntityUtils.getSet(pushDingMsgDtos, "classTeaUserId");//班主任userIds
		ctUserIds.remove(null);
		Set<String> allUserIds = Sets.newHashSet();//所有接收者userIds
		allUserIds.addAll(ctUserIds);
		allUserIds.addAll(gtUserIds);
		allUserIds.addAll(otherUserIds);
		List<User> users = SUtils.dt(userRemoteService.findListByIds(allUserIds.toArray(new String[allUserIds.size()])),new TR<List<User>>() {});
		Map<String,String> dingIdMap = EntityUtils.getMap(users, "id","dingdingId");//接收者key:userId,val:dingdingId
		Map<String,Set<String>> gradeMsgMap = Maps.newHashMap();//年级组长 key:userId,val:classIds
		Map<String,StringBuffer> gClassMap = Maps.newHashMap();// key:classId,val:班级名（学生1，学生2）
		Map<String,StringBuffer> classMsgMap = Maps.newHashMap();//班主任 key:userId,val:学生1，学生2
		Map<String,Map<String,StringBuffer>> gradeDormStuNameMap =  Maps.newHashMap();
		if(type==2){
			// 单位下当天申请通校的学生
			Set<String> txStudentIds = Sets.newHashSet();
			if(getOpenApiOfficeService()!=null){
				String jsonStr1 = getOpenApiOfficeService().getHwStuLeavesByUnitId(unitId, null, "3", "2", new Date());
				String jsonStr2 = getOpenApiOfficeService().getHwStuLeavesByUnitId(unitId, null, "4", null, new Date());
				JSONArray strings1 = EccUtils.getResultArray(jsonStr1, "studentIds");
				JSONArray strings2 = EccUtils.getResultArray(jsonStr2, "studentIds");
				for (int i = 0; i < strings1.size(); i++) {
					txStudentIds.add(strings1.get(i).toString());
				}
				for (int i = 0; i < strings2.size(); i++) {
					txStudentIds.add(strings2.get(i).toString());
				}
				studentIds.addAll(txStudentIds);
			}
		}
		for(PushDingMsgDto msgDto:pushDingMsgDtos){
			if(studentIds!=null&&studentIds.contains(msgDto.getStudentId())){
				continue;
			}
			if(gClassMap.containsKey(msgDto.getClassId())){
				StringBuffer buff = gClassMap.get(msgDto.getClassId());
				if(buff!=null){
					buff.append("、"+msgDto.getStudentName());
				}
			}else{
				gClassMap.put(msgDto.getClassId(), new StringBuffer(msgDto.getClassAllName()+"("+msgDto.getStudentName()));
			}
			if(gradeDormStuNameMap.containsKey(msgDto.getGradeId())){
				Map<String,StringBuffer> dormStuNameMap = gradeDormStuNameMap.get(msgDto.getGradeId());
				if(dormStuNameMap.containsKey(msgDto.getAttId())){
					StringBuffer buff = dormStuNameMap.get(msgDto.getAttId());
					if(buff!=null){
						buff.append("、"+msgDto.getStudentName());
					}
				}else{
					dormStuNameMap.put(msgDto.getAttId(),new StringBuffer("(" + msgDto.getStudentName()));
				}
			}else{
				Map<String,StringBuffer> dormStuNameMap = Maps.newHashMap();
				dormStuNameMap.put(msgDto.getAttId(),new StringBuffer("(" + msgDto.getStudentName()));
				gradeDormStuNameMap.put(msgDto.getGradeId(),dormStuNameMap);
			}
			if(gradeMsgMap.containsKey(msgDto.getGradeTeaUserId())){
				Set<String> clzIds = gradeMsgMap.get(msgDto.getGradeTeaUserId());
				if(CollectionUtils.isEmpty(clzIds)){
					clzIds.add(msgDto.getClassId());
				}
			}else{
				Set<String> clzIds = Sets.newHashSet();
				clzIds.add(msgDto.getClassId());
				gradeMsgMap.put(msgDto.getGradeTeaUserId(),clzIds);
			}
			if(classMsgMap.containsKey(msgDto.getClassTeaUserId())){
				StringBuffer buff = classMsgMap.get(msgDto.getClassTeaUserId());
				if(buff!=null){
					buff.append("、"+msgDto.getStudentName());
				}
			}else{
				classMsgMap.put(msgDto.getClassTeaUserId(),new StringBuffer(msgDto.getStudentName()));
			}
		}
		if(noticeSet.isSend()){
			JSONArray msgarr = new JSONArray();
			String typeTag = "寝室";
			String content = "您好，"+DateUtils.date2StringByDay(new Date())+",";
			if(type==1){
				typeTag = "上课";
				content+=EccUtils.getSectionName(sectionNumber)+"课,学生考勤缺课名单：";
			}else{
				content+=periodStr+",寝室考勤未打卡名单";
			}
			if(otherUserIds.size()>0){
				if(type==1){
					List<JSONObject> jsons = Lists.newArrayList();
					StringBuffer stucon = new StringBuffer("");
					for(StringBuffer value:gClassMap.values()){
						if(value!=null&&value.length()>0){
							stucon.append(value+");");
						}
					}
					String userIds = "";
					for(String userId:otherUserIds){
						String dingId = "";
						if(dingIdMap.containsKey(userId)){
							dingId = dingIdMap.get(userId);
						}
						if(StringUtils.isNotBlank(dingId)){
							if(StringUtils.isBlank(userIds)){
								userIds = dingId;
							}else{
								userIds+="|"+dingId;
							}
						}
					}
					if(stucon!=null&&stucon.length()>0&&StringUtils.isNotBlank(userIds)){
						String contentStr = content+stucon.toString();
						jsons.add(DdMsgUtils.toDingDingTextJson(userIds, "", contentStr));
						fillWeiKeJson("上课考勤未打卡人员", otherUserIds, contentStr,msgarr);
					}
					dingdingMsgRemoteService.addDingDingMsgs(unitId, jsons);
				}else if(CollectionUtils.isNotEmpty(gradeIds)&&CollectionUtils.isNotEmpty(attIds)){
					fillDormOtherDDMsg(gradeDormStuNameMap,otherUserIds,gradeIds,attIds,dingIdMap,periodStr,unitId,msgarr);
				}
			}
			if(noticeSet.isSendGradeMaster()){//年级组长
				List<JSONObject> jsons = Lists.newArrayList();
				for(String gtUserId:gtUserIds){
					String userIds = "";
					if(dingIdMap.containsKey(gtUserId)){
						userIds = dingIdMap.get(gtUserId);
					}
					StringBuffer stucon = new StringBuffer("");
					if(gradeMsgMap.containsKey(gtUserId)){
						Set<String> clzIds = gradeMsgMap.get(gtUserId);
						clzIds.remove(null);
						if(CollectionUtils.isNotEmpty(clzIds)){
							for(String classId:clzIds){
								if(gClassMap.containsKey(classId)&&StringUtils.isNotBlank(gClassMap.get(classId))){
									stucon.append(gClassMap.get(classId)+");");
								}	
							}
						}
						
					}
					if(stucon!=null&&stucon.length()>0&&StringUtils.isNotBlank(userIds)){
						String contentStr = content+stucon.toString();
						jsons.add(DdMsgUtils.toDingDingTextJson(userIds, "", contentStr));
					
						fillWeiKeJson("年级"+typeTag+"考勤未打卡人员", otherUserIds, contentStr,msgarr);
					}
				}
				dingdingMsgRemoteService.addDingDingMsgs(unitId, jsons);
			}
			if(noticeSet.isSendClassMaster()){//班主任
				List<JSONObject> jsons = Lists.newArrayList();
				for(String ctUserId:ctUserIds){
					String userIds = "";
					if(dingIdMap.containsKey(ctUserId)){
						userIds = dingIdMap.get(ctUserId);
					}
					StringBuffer stucon = new StringBuffer("");
					if(classMsgMap.containsKey(ctUserId)){
						stucon = classMsgMap.get(ctUserId);
					}
					if(stucon!=null&&stucon.length()>0){
						String contentStr = content+stucon.toString();
						jsons.add(DdMsgUtils.toDingDingTextJson(userIds, "", contentStr));
						fillWeiKeJson("班级"+typeTag+"考勤未打卡人员", otherUserIds, contentStr,msgarr);
					}
				}
				dingdingMsgRemoteService.addDingDingMsgs(unitId, jsons);
			}
			try {
				if(getOpenApiOfficeService()!=null && msgarr.size()>0){
					getOpenApiOfficeService().pushWeikeMessage("", msgarr.toJSONString());
				}
			} catch (Exception e) {
				 e.printStackTrace();
				return;
			}
		}
	}

	private void fillDormOtherDDMsg(
			Map<String, Map<String, StringBuffer>> gradeDormStuNameMap,Set<String> otherUserIds,Set<String> gradeIds,Set<String> attIds,Map<String,String> dingIdMap,
			String periodStr,String unitId,JSONArray msgarr) {
		List<JSONObject> jsons = Lists.newArrayList();
		List<Grade> grades = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[gradeIds.size()])),new TR<List<Grade>>() {});
		Map<String,String> gradeNameMap = EntityUtils.getMap(grades, "id","gradeName");
		List<EccDormAttence> dormAttences = eccDormAttenceService.findListByIdIn(attIds.toArray(new String[attIds.size()]));
		Map<String,String> attPlaceMap = EntityUtils.getMap(dormAttences, "id","placeId");
		Map<String,String> attPlaceNameMap = Maps.newHashMap();
    	String jsonStr = stuworkRemoteService.getBuildingSbyUnitId(unitId);
    	List<DormBuildingDto> buildingDtos = SUtils.dt(jsonStr,new TR<List<DormBuildingDto>>() {});
		Map<String,String> dormBuildMap = EntityUtils.getMap(buildingDtos, "buildingId","buildingName");
		for(String key:attPlaceMap.keySet()){
			if(attPlaceMap.get(key) != null&&dormBuildMap.containsKey(attPlaceMap.get(key))){
				attPlaceNameMap.put(key, dormBuildMap.get(attPlaceMap.get(key)));
			}
		}
		
		for(String key : gradeDormStuNameMap.keySet()){
			StringBuffer stucon = new StringBuffer("您好，"+DateUtils.date2StringByDay(new Date())+","+periodStr+","+gradeNameMap.get(key)+" 未打卡名单：");
			Map<String,StringBuffer> dormStuNameMap = gradeDormStuNameMap.get(key);
			if(dormStuNameMap==null)continue;
			for(String attKey:dormStuNameMap.keySet()){
				StringBuffer value = dormStuNameMap.get(attKey);
				if(value!=null&&value.length()>0 && attPlaceNameMap.containsKey(attKey)){
					stucon.append(attPlaceNameMap.get(attKey)).
					append(value+");");
				}
			}
			String userIds = "";
			for(String userId:otherUserIds){
				String dingId = "";
				if(dingIdMap.containsKey(userId)){
					dingId = dingIdMap.get(userId);
				}
				if(StringUtils.isNotBlank(dingId)){
					if(StringUtils.isBlank(userIds)){
						userIds = dingId;
					}else{
						userIds+="|"+dingId;
					}
				}
			}
			if(stucon!=null&&stucon.length()>0&&StringUtils.isNotBlank(userIds)){
				jsons.add(DdMsgUtils.toDingDingTextJson(userIds, "", stucon.toString()));
				fillWeiKeJson("寝室考勤未打卡人员", otherUserIds, stucon.toString(),msgarr);
			}
		}
		dingdingMsgRemoteService.addDingDingMsgs(unitId, jsons);
	}
	
	private void fillWeiKeJson(String title,Set<String> uIds,String content,JSONArray msgarr){
		// 往办公公众号推送消息
		JSONArray userIds = new JSONArray();
		String[] rowsContent = new String[] { content };
		for(String id:uIds){
			userIds.add(id);
		}
		JSONObject msg = new JSONObject();
		msg.put("userIdArray", userIds);

		msg.put("msgTitle", title);

		msg.put("rowsContent", rowsContent);

		msgarr.add(msg);
	}

}
