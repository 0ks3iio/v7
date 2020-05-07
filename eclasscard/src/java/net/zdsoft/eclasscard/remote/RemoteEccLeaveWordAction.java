package net.zdsoft.eclasscard.remote;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.FamilyLeaveWordDto;
import net.zdsoft.eclasscard.data.entity.EccLeaveWord;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccLeaveWordService;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.WeiKeyUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mobile/open/eclasscard")
public class RemoteEccLeaveWordAction extends MobileAction{

	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private EccLeaveWordService eccLeaveWordService;
	@Autowired
	private FamilyRemoteService familyRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	
	@RequestMapping("/leavae/word/index")
	@ControllerInfo("留言首页")
	public String leaveWordIndex(String token,String familyId,ModelMap map) {
//		if (StringUtils.isBlank(token)) {
//			return errorFtl(map, "token为空");
//		}
//		String familyId = "";
//		try {
//			familyId = WeiKeyUtils.decodeByDes(token);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}// base_user.owner_id
		if (StringUtils.isNotBlank(token)) {
			try {
				familyId = WeiKeyUtils.decodeByDes(token);
			} catch (Exception e) {
				e.printStackTrace();
			}// base_user.owner_id
		}
		Family family= SUtils.dc(familyRemoteService.findOneById(familyId),Family.class);
		if (family == null) {
			return errorFtl(map, "找不到相关用户信息，请联系管理员！");
		}
		User user = User.dc(userRemoteService.findByOwnerId(familyId));
		List<User> userList = null;
		if ( StringUtils.isNotBlank(family.getMobilePhone()) ) {
			userList = SUtils.dt(userRemoteService.findByMobilePhones(User.OWNER_TYPE_FAMILY, family.getMobilePhone()), User.class);
			userList = userList.stream().filter(u -> {
				return u.getUserState().equals(Integer.valueOf(1)) && PWD.decode(u.getPassword()).equals(PWD.decode(user.getPassword()));
						}).collect(Collectors.toList());
		}
		List<Family> families = SUtils.dt(familyRemoteService.findListByIds(EntityUtils.<String,User>getList(userList,"ownerId").toArray(new String[0])),Family.class);
        //得到所有的孩子  userLists
		Set<String> studentIdList = EntityUtils.getSet(families, Family::getStudentId);
		studentIdList.add(family.getStudentId());
		List<Student> studentList = Student.dt(studentRemoteService.findListByIds(studentIdList.toArray(new String[0])));
		if (CollectionUtils.isEmpty(studentList)) {
			return errorFtl(map, "找不到相关孩子信息！");
		}
		map.put("senderId", family.getId());
		if(studentList.size() == 1){
			Student student = studentList.get(0);
			map.put("closepage", true);
			studentLeaveWord(family,student,map);
			return "/eclasscard/mobileh5/leaveword/leaveword.ftl";
		}else{
			List<FamilyLeaveWordDto> familyDto  = Lists.newArrayList();
			for(Student student:studentList){
				FamilyLeaveWordDto dto = new FamilyLeaveWordDto();
				dto.setId(student.getId());
				dto.setName(student.getStudentName());
				String time = DateUtils.date2StringByMinute(student.getModifyTime());
				dto.setPicUrl(EccUtils.showPictureUrl(student.getFilePath(), student.getSex(),time));
				dto.setLastWord(eccLeaveWordService.findLastWordByCache(student.getId()+family.getId()));
				dto.setUnReadNum(RedisUtils.incrby(EccConstants.LEAVE_WORD_CACHE_HEAD + student.getId() + family.getId()+"num", 0));
				familyDto.add(dto);
			}
			
			map.put("familyDto", familyDto);
			return "/eclasscard/mobileh5/leaveword/childList.ftl";
		}
	}
	
	@RequestMapping("/leavae/word/detail")
	@ControllerInfo("留言首页")
	public String leaveWordDetail(String studentId,String familyId,ModelMap map) {
		Family family= SUtils.dc(familyRemoteService.findOneById(familyId),Family.class);
		Student student=SUtils.dc(studentRemoteService.findOneById(studentId),Student.class);
		map.put("closepage", false);
		map.put("senderId", family.getId());
		studentLeaveWord(family,student,map);
		return "/eclasscard/mobileh5/leaveword/leaveword.ftl";
	}
	private void studentLeaveWord(Family family,Student student, ModelMap map) {
		map.put("studentName", student.getStudentName());
		map.put("receiverId", student.getId());
		User user = User.dc(userRemoteService.findByOwnerId(family.getId()));
		if(user!=null){
			map.put("showReceiverName", user.getUsername());
		}
		String time = DateUtils.date2StringByMinute(student.getModifyTime());
		map.put("studentPic", EccUtils.showPictureUrl(student.getFilePath(), student.getSex(),time));
	}

	@ResponseBody
	@RequestMapping("/leavae/word/send")
	@ControllerInfo("发送留言")
	public String sendWord(String senderId,String receiverId,String content,ModelMap map) {
		Date now = new Date();
		try{
			if(StringUtils.isNotBlank(senderId) && StringUtils.isNotBlank(receiverId)
					&& StringUtils.isNotBlank(content)){
				Family family= SUtils.dc(familyRemoteService.findOneById(senderId),Family.class);
				EccLeaveWord leaveWord = new EccLeaveWord();
				leaveWord.setId(UuidUtils.generateUuid());
				leaveWord.setContent(content);
				leaveWord.setCreationTime(now);
				leaveWord.setModifyTime(now);
				leaveWord.setReceiverId(receiverId);
				leaveWord.setSenderId(senderId);
				leaveWord.setState(0);
				if(family!=null)leaveWord.setUnitId(family.getSchoolId());
				eccLeaveWordService.saveLeaveWord(leaveWord,EccConstants.ECC_LEAVE_WORD_2,"");
			}else{
				return error("发送失败！参数错误");
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("发送失败！"+e.getMessage());
		}
		return Json.toJSONString(DateUtils.date2StringByMinute(now));
	}
	
	@ResponseBody
	@RequestMapping("/leavae/word/history")
	@ControllerInfo("留言翻页")
	public String leaveWordHistory(String senderId,String receiverId,Date lastTime,HttpServletRequest request,ModelMap map) {
		Pagination page = createPagination(request);
		if(lastTime==null){
			lastTime = new Date();
		}
		List<EccLeaveWord> leaveWords = eccLeaveWordService.findBySenderAndReceiverId(receiverId,senderId,lastTime,page);
		if(page.getPageIndex() == 1){
			eccLeaveWordService.updateStatus(receiverId, senderId, new Date());//更新已读状态
			RedisUtils.del(EccConstants.LEAVE_WORD_CACHE_HEAD +receiverId + senderId+"num");
		}
		for(EccLeaveWord word:leaveWords){
			if(senderId.equals(word.getSenderId())){
				word.setSender(true);
			}else{
				word.setSender(false);
			}
			word.setMaxPage(page.getMaxPageIndex());
			word.setTimeStr(DateUtils.date2StringByMinute(word.getCreationTime()));
		}
		return Json.toJSONString(leaveWords);
	}
}
