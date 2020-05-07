package net.zdsoft.eclasscard.data.action.show;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.StusysSectionTimeSetRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.entity.EccClassAttence;
import net.zdsoft.eclasscard.data.entity.EccSeatItem;
import net.zdsoft.eclasscard.data.entity.EccSeatSet;
import net.zdsoft.eclasscard.data.entity.EccStuclzAttence;
import net.zdsoft.eclasscard.data.entity.EccTeaclzAttence;
import net.zdsoft.eclasscard.data.service.EccClassAttenceService;
import net.zdsoft.eclasscard.data.service.EccClockInService;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccSeatItemService;
import net.zdsoft.eclasscard.data.service.EccSeatSetService;
import net.zdsoft.eclasscard.data.service.EccStuclzAttenceService;
import net.zdsoft.eclasscard.data.service.EccTeaclzAttenceService;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;
@Controller
@RequestMapping("/eccShow/eclasscard/standard")
public class EccClockInStandardAction extends BaseAction {
	
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private EccClockInService eccClockInService;
	@Autowired
	private EccClassAttenceService eccClassAttenceService;
	@Autowired
	private EccStuclzAttenceService eccStuclzAttenceService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private StusysSectionTimeSetRemoteService stusysSectionTimeSetRemoteService;
	@Autowired
	private EccTeaclzAttenceService eccTeaclzAttenceService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	@Autowired
	private EccSeatSetService eccSeatSetService;
	@Autowired
	private EccSeatItemService eccSeatItemService;
	
	@RequestMapping("/classClockIn/index")
	public String classClockInIndex(String cardId,String view,HttpServletRequest request,String id,ModelMap map){
		EccClassAttence classAttence = eccClassAttenceService.findOne(id);
		if(classAttence==null){
			return "redirect:/eccShow/eclasscard/standard/showIndex?cardId="+cardId+"&view="+view;
		}
		List<EccStuclzAttence> eccStuclzAttences = eccStuclzAttenceService.findListByAttIdWithMaster(classAttence.getId());
		EccTeaclzAttence teaclzAttence = eccTeaclzAttenceService.findByAttId(classAttence.getId());
		if(teaclzAttence==null){
			teaclzAttence = new EccTeaclzAttence();
			teaclzAttence.setClassAttId(classAttence.getId());
			teaclzAttence.setClockDate(classAttence.getClockDate());
			teaclzAttence.setId(UuidUtils.generateUuid());
			teaclzAttence.setStatus(EccConstants.CLASS_ATTENCE_STATUS1);
			teaclzAttence.setTeacherId(classAttence.getTeacherId());
		}
		Map<String,String> stuNameMap = Maps.newHashMap();
		Map<String,Integer> stuSexMap = Maps.newHashMap();
		Map<String,Student> stuMap = Maps.newHashMap();
		Set<String> ownerIds = EntityUtils.getSet(eccStuclzAttences, EccStuclzAttence::getStudentId);
		List<Student> allAtudents = SUtils.dt(studentRemoteService.findListByIds(ownerIds.toArray(new String[ownerIds.size()])),new TR<List<Student>>() {});
		for(Student student:allAtudents){
			stuNameMap.put(student.getId(), student.getStudentName());
			stuSexMap.put(student.getId(), student.getSex());
			stuMap.put(student.getId(), student);
		}
		ownerIds.add(classAttence.getTeacherId());
		Map<String,User> userMap = Maps.newHashMap();
		ownerIds.remove(null);
		if(ownerIds.size()>0){
			List<User> users = SUtils.dt(userRemoteService.findByOwnerIds(ownerIds.toArray(new String[ownerIds.size()])),new TR<List<User>>() {});
			userMap = EntityUtils.getMap(users, User::getOwnerId);
		}
		if(userMap.containsKey(classAttence.getTeacherId())){
			User user = userMap.get(classAttence.getTeacherId());
			if(user!=null){
				classAttence.setTeacherName(user.getUsername());
				classAttence.setTeacherRealName(user.getRealName());
			}
		}
		int clockNum = 0;
		int leaveNum = 0;
		int notClockNum = 0;
		int lateNum = 0;
		
		boolean isSeatSet=false;//是否设置座位表
		int rowNumbers=0;//行
		int colNumbers=0;//列
		String spaceNo=null;
		Map<String,EccSeatItem> seatItemMap=new HashMap<String, EccSeatItem>();
		EccSeatSet seatSet = eccSeatSetService.findOneByUnitIdAndClassId(classAttence.getUnitId(), classAttence.getClassId());
		if(seatSet!=null) {
			List<EccSeatItem> eccSeatItemList = eccSeatItemService.findListBySeatId(seatSet.getUnitId(), seatSet.getId());
			if(CollectionUtils.isNotEmpty(eccSeatItemList)) {
				isSeatSet=true;
				rowNumbers=seatSet.getRowNumber();
				colNumbers=seatSet.getColNumber();
				spaceNo=seatSet.getSpaceNum()==null?"":seatSet.getSpaceNum();
				seatItemMap=EntityUtils.getMap(eccSeatItemList, e->e.getStudentId());
			}
		}
		for(EccStuclzAttence attence:eccStuclzAttences){
			if(userMap.containsKey(attence.getStudentId())){
				User user = userMap.get(attence.getStudentId());
				if(user!=null){
					attence.setStuUserName(user.getUsername());
				}
			}
			if(stuMap.containsKey(attence.getStudentId())){
				Student student = stuMap.get(attence.getStudentId());
				if(student!=null){
					String time = DateUtils.date2StringByMinute(student.getModifyTime());
					if(time==null)time = DateUtils.date2StringByMinute(new Date());
					attence.setShowPictrueUrl(EccUtils.showPictureUrl(student.getFilePath(), student.getSex(),time));
				}
			}
			if(stuNameMap.containsKey(attence.getStudentId())){
				attence.setStuRealName(stuNameMap.get(attence.getStudentId()));
			}
			if(stuSexMap.containsKey(attence.getStudentId())){
				attence.setSex(stuSexMap.get(attence.getStudentId()));
			}else{
				attence.setSex(1);
			}
			if(EccConstants.CLASS_ATTENCE_STATUS1==attence.getStatus()){
				notClockNum++;
			}else if(EccConstants.CLASS_ATTENCE_STATUS2==attence.getStatus()){
				lateNum++;
			}else if(EccConstants.CLASS_ATTENCE_STATUS3==attence.getStatus()){
				leaveNum++;
			}else if(EccConstants.CLASS_ATTENCE_STATUS4==attence.getStatus()){
				clockNum++;
			}
			if(seatItemMap.containsKey(attence.getStudentId())) {
				attence.setColNo(seatItemMap.get(attence.getStudentId()).getColNum());
				attence.setRowNo(seatItemMap.get(attence.getStudentId()).getRowNum());
			}
		}
		map.put("sumNum", eccStuclzAttences.size());//应到人数
		map.put("clockNum", clockNum);//实到人数
		map.put("leaveNum", leaveNum);//请假人数
		map.put("notClockNum", notClockNum);//未到人数
		map.put("lateNum", lateNum);//迟到人数
		map.put("classAttence", classAttence);
		map.put("teaclzAttence", teaclzAttence);
		map.put("eccStuclzAttences", eccStuclzAttences);
		
		String[] spaceNoArr=new String[] {};
		if(StringUtils.isNotBlank(spaceNo)) {
			spaceNoArr=spaceNo.split(",");
		}
		//走座位表
		if(isSeatSet) {
			map.put("isSeatSet", isSeatSet);
			map.put("rowNumbers", rowNumbers);
			map.put("colNumbers", colNumbers);
			map.put("spaceNoArr", spaceNoArr);
			if(EccConstants.ECC_VIEW_2.equals(view)){
				return "/eclasscard/standard/verticalshow/eccClassClockInSeat.ftl";
			}else{
				return "/eclasscard/standard/show/eccClassClockInSeat.ftl";
			}
		}
		
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/eccClassClockIn.ftl";
		}else{
			return "/eclasscard/standard/show/eccClassClockIn.ftl";
		}
	}
	
}
