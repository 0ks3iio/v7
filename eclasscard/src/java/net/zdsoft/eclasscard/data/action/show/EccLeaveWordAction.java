package net.zdsoft.eclasscard.data.action.show;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccLeaveWord;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccLeaveWordService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Controller
@RequestMapping("/eccShow/eclasscard/standard")
public class EccLeaveWordAction extends BaseAction {
	public static final String FAMILY_RELATION_MATHER = "52";
	public static final String FAMILY_RELATION_FATHER = "51";
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
	public String leaveWordIndex(String cardId,String receiverId,String view,ModelMap map){
		//1.找到学生家庭中父母，没有提示空
		User user = User.dc(userRemoteService.findOneById(receiverId, true));
		List<FamilyLeaveWordDto> familyDto  = Lists.newArrayList();
		String showId = "";
		String senderId = "";
		String showReceiverName = "";
		if(user!=null){
			senderId = user.getOwnerId();
			List<Family> familys = SUtils.dt(familyRemoteService.findByStudentId(user.getOwnerId()),new TypeReference<List<Family>>() {});
			int index = 0;
			Set<String> ownerIds = EntityUtils.getSet(familys, Family::getId);
			Map<String,String> userNameMap = Maps.newHashMap();
			if(CollectionUtils.isNotEmpty(ownerIds)){
				List<User> users = User.dt(userRemoteService.findByOwnerIds(ownerIds.toArray(new String[0])));
				userNameMap = EntityUtils.getMap(users, User::getOwnerId,User::getUsername);
			}
			for(Family family:familys){
				if(FAMILY_RELATION_MATHER.equals(family.getRelation())){
					FamilyLeaveWordDto dto = new FamilyLeaveWordDto();
					dto.setId(family.getId());
					dto.setName("妈妈");
					String username = "";
					if(userNameMap.containsKey(family.getId())){
						username = userNameMap.get(family.getId());
					}
					if(StringUtils.isEmpty(username)){
						username = "1";
					}
					dto.setUsername(username);
					dto.setLastWord(eccLeaveWordService.findLastWordByCache(senderId+family.getId()));
					dto.setUnReadNum(RedisUtils.incrby(EccConstants.LEAVE_WORD_CACHE_HEAD + family.getId()+senderId+"num", 0));
					familyDto.add(dto);
					if(index == 0){
						showId = family.getId();
						showReceiverName = username;
					}
					index++;
				}
				if(FAMILY_RELATION_FATHER.equals(family.getRelation())){
					FamilyLeaveWordDto dto = new FamilyLeaveWordDto();
					dto.setId(family.getId());
					dto.setName("爸爸");
					String username = "";
					if(userNameMap.containsKey(family.getId())){
						username = userNameMap.get(family.getId());
					}
					if(StringUtils.isEmpty(username)){
						username = "1";
					}
					dto.setUsername(username);
					dto.setLastWord(eccLeaveWordService.findLastWordByCache(senderId+family.getId()));
					dto.setUnReadNum(RedisUtils.incrby(EccConstants.LEAVE_WORD_CACHE_HEAD + family.getId()+senderId+"num", 0));
					familyDto.add(dto);
					if(index == 0){
						showId = family.getId();
						showReceiverName = username;
					}
					index++;
				}
			}
		}
		
		//2.加载父母列表
		map.put("showId", showId);
		map.put("senderId", senderId);
		map.put("showReceiverName", showReceiverName);
		map.put("needFamilys", familyDto);
		if(EccConstants.ECC_VIEW_2.equals(view)){
			return "/eclasscard/standard/verticalshow/studentspace/leavaeWord.ftl";
		}else{
			return "/eclasscard/standard/show/studentspace/leavaeWord.ftl";
		}
	}
	
	
//	@RequestMapping("/leavae/word/list")
//	@ControllerInfo("留言列表")
//    public String leaveWordList(String cardId,String receiverId,String senderId,String view,ModelMap map){
//		//最新20条消息
//		Pagination page = createPagination();
//		List<EccLeaveWord> leaveWords = eccLeaveWordService.findBySenderAndReceiverId(receiverId,senderId,page);
//		map.put("leaveWords", leaveWords);
//		if(EccConstants.ECC_VIEW_2.equals(view)){
//			return "/eclasscard/standard/verticalshow/studentspace/leavaeWordList.ftl";
//		}else{
//			return "/eclasscard/standard/show/studentspace/leavaeWordList.ftl";
//		}
//	}
	
	@ResponseBody
	@RequestMapping("/leavae/word/history")
	@ControllerInfo("留言翻页")
	public String leaveWordHistory(String senderId,String receiverId,String cardId,Date lastTime,ModelMap map) {
		Pagination page = createPagination();
		if(lastTime==null){
			lastTime = new Date();
		}
		List<EccLeaveWord> leaveWords = eccLeaveWordService.findBySenderAndReceiverId(receiverId,senderId,lastTime,page);
		if(page.getPageIndex() == 1){
			Family family= SUtils.dc(familyRemoteService.findOneById(receiverId),Family.class);
			User user = User.dc(userRemoteService.findByOwnerId(receiverId));
			Set<String> ownerIds =Sets.newHashSet();
			ownerIds.add(receiverId);
			if(family!=null && user != null){
				List<User> userList = null;
				if ( StringUtils.isNotBlank(family.getMobilePhone()) ) {
					userList = SUtils.dt(userRemoteService.findByMobilePhones(User.OWNER_TYPE_FAMILY, family.getMobilePhone()), User.class);
					userList = userList.stream().filter(u -> {
						return u.getUserState().equals(Integer.valueOf(1)) && PWD.decode(u.getPassword()).equals(PWD.decode(user.getPassword()));
					}).collect(Collectors.toList());
				}
				ownerIds = EntityUtils.getSet(userList,User::getOwnerId);
			}
			for(String oId:ownerIds){
				eccLeaveWordService.updateStatus(oId, senderId, new Date());//更新已读状态
				RedisUtils.del(EccConstants.LEAVE_WORD_CACHE_HEAD + oId + senderId+"num");
			}
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
	
	@ResponseBody
	@RequestMapping("/leavae/word/send")
	@ControllerInfo("发送留言")
	public String sendWord(String senderId,String receiverId,String cardId,String content,HttpServletRequest request,ModelMap map) {
		Date now = new Date();
		try{
			if(StringUtils.isNotBlank(senderId) && StringUtils.isNotBlank(receiverId) && StringUtils.isNotBlank(cardId)
					&& StringUtils.isNotBlank(content)){
				String basePath = request.getScheme()+"://"+request.getServerName()
						+ ":" + request.getServerPort() + request.getContextPath();
				EccInfo eccInfo = eccInfoService.findOne(cardId);
				EccLeaveWord leaveWord = new EccLeaveWord();
				leaveWord.setId(UuidUtils.generateUuid());
				leaveWord.setContent(content);
				leaveWord.setCreationTime(now);
				leaveWord.setModifyTime(now);
				leaveWord.setReceiverId(receiverId);
				leaveWord.setSenderId(senderId);
				leaveWord.setState(0);
				if(eccInfo!=null)leaveWord.setUnitId(eccInfo.getUnitId());
				eccLeaveWordService.saveLeaveWord(leaveWord,EccConstants.ECC_LEAVE_WORD_1,basePath);
			}else{
				return error("发送失败！参数错误");
			}
		}catch (Exception e) {
			e.printStackTrace();
			return error("发送失败！"+e.getMessage());
		}
		return successByValue(DateUtils.date2StringByMinute(now));
	}
	
	@ResponseBody
	@RequestMapping("/leavae/word/class")
	@ControllerInfo("班級学生留言提醒")
	public String leaveWordClass(String cardId,ModelMap map) {
		List<EccLeaveWord> returnLeaveWords = Lists.newArrayList();
		EccInfo eccInfo = eccInfoService.findOne(cardId);
		if (StringUtils.isNotBlank(eccInfo.getClassId())) {
			List<Student> students = SUtils.dt(studentRemoteService.findByClassIds(eccInfo.getClassId()),new TR<List<Student>>() {});
			Set<String> stuIds = EntityUtils.getSet(students, Student::getId);
			Map<String,String> stuNameMap = EntityUtils.getMap(students, Student::getId,Student::getStudentName);
			if(CollectionUtils.isNotEmpty(stuIds)){
				List<EccLeaveWord> leaveWords = eccLeaveWordService.findByReceiverIdsNotRead(stuIds.toArray(new String[stuIds.size()]));
				Set<String> receiveStuIds = Sets.newHashSet();
				for(EccLeaveWord word:leaveWords){
					String showmsg = "同学，你收到一条留言~";
					String stuId = word.getReceiverId();
					if(receiveStuIds.contains(stuId)){
						continue;
					}
					if(stuNameMap.containsKey(stuId)){
						showmsg = stuNameMap.get(stuId)+showmsg;
					}
					receiveStuIds.add(stuId);
					word.setRemindStr(showmsg);
					word.setTimeStr(DateUtils.date2StringByMinute(word.getCreationTime()));
					returnLeaveWords.add(word);
				}
			}
		}
		return Json.toJSONString(returnLeaveWords);
	}
}
