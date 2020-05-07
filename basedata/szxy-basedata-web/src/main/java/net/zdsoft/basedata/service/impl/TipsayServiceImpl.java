package net.zdsoft.basedata.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.constant.TipsayConstants;
import net.zdsoft.basedata.dao.TipsayDao;
import net.zdsoft.basedata.dao.TipsayJdbcDao;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.dto.TipsayDto;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachPlanEx;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Tipsay;
import net.zdsoft.basedata.entity.TipsayEx;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.CourseScheduleService;
import net.zdsoft.basedata.service.CourseService;
import net.zdsoft.basedata.service.CustomRoleService;
import net.zdsoft.basedata.service.DateInfoService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachPlanExService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.basedata.service.TipsayExService;
import net.zdsoft.basedata.service.TipsayService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import net.zdsoft.system.remote.service.SmsRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@Service("tipsayService")
public class TipsayServiceImpl extends BaseServiceImpl<Tipsay, String>
		implements TipsayService {

	@Autowired
	private TipsayDao tipsayDao;
	@Autowired
	private TipsayExService tipsayExService;
	@Autowired
	private CourseScheduleService courseScheduleService;
	@Autowired
	private DateInfoService dateInfoService;
	@Autowired
	private UserService userService;
	@Autowired
	private ClassService classService;
	@Autowired
	private TeachClassService teachClassService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TipsayJdbcDao tipsayJdbcDao;
	
	@Autowired
	private TeachPlanExService teachPlanExService;
	@Autowired
	public CustomRoleService customRoleService;
	@Autowired
	private SmsRemoteService smsRemoteService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	
	private static OpenApiOfficeService openApiOfficeService;

	@Override
	protected BaseJpaRepositoryDao<Tipsay, String> getJpaDao() {
		return tipsayDao;
	}

	@Override
	protected Class<Tipsay> getEntityClass() {
		return Tipsay.class;
	}

	@Override
	public List<Tipsay> findTipsayListWithMaster(String schoolId,
			String acadyear, Integer semester, Integer weekOfWorkTime,String teacherId) {
		if(StringUtils.isBlank(teacherId)) {
			return findBySemesterWeekOfWorkTime(schoolId, acadyear, semester,
					weekOfWorkTime, TipsayConstants.IF_0);
		}else {
			return findBySemesterTeacher(schoolId, acadyear, semester,
					weekOfWorkTime, TipsayConstants.IF_0,teacherId);
		}
		

	}
	/**
	 * 某个教师申请的数据
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param weekOfWorkTime
	 * @param isDeleted
	 * @param applyTeacherId
	 * @return
	 */
	private List<Tipsay> findBySemesterTeacher(String schoolId,
			String acadyear, Integer semester, Integer weekOfWorkTime,
			Integer isDeleted,String teacherId) {
		Integer[] deletedArr = new Integer[] { TipsayConstants.IF_0,
				TipsayConstants.IF_1 };
		if (isDeleted != null) {
			deletedArr = new Integer[] { isDeleted };
		}
		if (weekOfWorkTime == null) {
			return tipsayDao.findBySemesterTeacher(schoolId, acadyear, semester,
					deletedArr,teacherId,"%"+teacherId+"%");
		} else {
			return tipsayDao.findByWeekOfWorkTimeTeacher(schoolId, acadyear, semester,
					weekOfWorkTime, deletedArr,teacherId,"%"+teacherId+"%");
		}
	}
	
	private List<Tipsay> findBySemesterWeekOfWorkTime(String schoolId,
			String acadyear, Integer semester, Integer weekOfWorkTime,
			Integer isDeleted) {
		Integer[] deletedArr = new Integer[] { TipsayConstants.IF_0,
				TipsayConstants.IF_1 };
		if (isDeleted != null) {
			deletedArr = new Integer[] { isDeleted };
		}
		if (weekOfWorkTime == null) {
			return tipsayDao.findBySemester(schoolId, acadyear, semester,
					deletedArr);
		} else {
			return tipsayDao.findByWeekOfWorkTime(schoolId, acadyear, semester,
					weekOfWorkTime, deletedArr);
		}
	}

	@Override
	public void saveAll(Tipsay tipsay, TipsayEx tipsayEx,
			CourseSchedule courseSchedule) {
		tipsayDao.save(tipsay);
		if (tipsayEx != null) {
			tipsayExService.save(tipsayEx);
		}
		if(courseSchedule!=null) {
			if (TipsayConstants.TIPSAY_TYPE_01.equals(tipsay.getType())) {
				//管理员直接安排
				if (TipsayConstants.TIPSAY_STATE_1.equals(tipsay.getState())) {
					// 最终同意
					// 保存课表
					courseSchedule.setTeacherId(tipsay.getNewTeacherId());
					//删除courseSchedule 辅助老师
					teachPlanExService.deleteByTeacherIdAndPrimaryTableIdIn(null, new String[] {courseSchedule.getId()});
					courseScheduleService.saveAllEntitys(new CourseSchedule[] {courseSchedule});
					// 通知完全
					sendMessageNew(tipsay,"2");
				}
			}else if(TipsayConstants.TIPSAY_TYPE_02.equals(tipsay.getType())) {
				if (TipsayConstants.TIPSAY_STATE_1.equals(tipsay.getState())) {
					// 最终同意
					// 保存课表
					courseSchedule.setTeacherId(tipsay.getNewTeacherId());
					//删除courseSchedule 辅助老师
					teachPlanExService.deleteByTeacherIdAndPrimaryTableIdIn(null, new String[] {courseSchedule.getId()});
					courseScheduleService.saveAllEntitys(new CourseSchedule[] {courseSchedule});
					// 通知完全
					sendMessageNew(tipsay,"2");
				}else if (TipsayConstants.TIPSAY_STATE_2.equals(tipsay.getState())) {
					sendMessageNew(tipsay,"3");
				}
			}else {
				//管理员安排老师
				if (TipsayConstants.TIPSAY_STATE_1.equals(tipsay.getState())) {
					// 最终同意
					// 保存课表
					courseSchedule.setTeacherId(tipsay.getNewTeacherId());
					//删除courseSchedule 辅助老师
					teachPlanExService.deleteByTeacherIdAndPrimaryTableIdIn(null, new String[] {courseSchedule.getId()});
					courseScheduleService.saveAllEntitys(new CourseSchedule[] {courseSchedule});
					// 通知完全
					sendMessageNew(tipsay,"2");
				}else if (TipsayConstants.TIPSAY_STATE_2.equals(tipsay.getState())) {
					sendMessageNew(tipsay,"3");
				}
			}
			
		}
		
	}

	@Override
	public void deleteByTipsayId(String tipsayId) {
		tipsayDao.updateById(tipsayId);
	}

	@Override
	public void deleteTipsayOrSaveCourseSchedule(Tipsay tipsay,
			CourseSchedule courseSchedule) {
		deleteByTipsayId(tipsay.getId());
		if (courseSchedule != null) {
			if (StringUtils.isNotBlank(tipsay.getTeacherExIds())) {
				// 保存辅助老师
				String[] tt = tipsay.getTeacherExIds().split(",");
				List<TeachPlanEx> exList=new ArrayList<>();
				TeachPlanEx ex;
				//还原courseSchedule 辅助老师
				for (String t : tt) {
					ex=new TeachPlanEx();
					ex.setTeacherId(t);
					ex.setId(UuidUtils.generateUuid());
					ex.setAcadyear(courseSchedule.getAcadyear());
					ex.setSemester(courseSchedule.getSemester());
					ex.setPrimaryTableId(courseSchedule.getId());
					ex.setType("2");
					ex.setUnitId(courseSchedule.getSchoolId());
					exList.add(ex);
				}
				teachPlanExService.saveAll(exList.toArray(new TeachPlanEx[] {}));
			}
			courseSchedule.setTeacherId(tipsay.getTeacherId());
			courseScheduleService
					.saveAllEntitys(new CourseSchedule[] { courseSchedule });
			sendMessageNew(tipsay, "4");
		} else {
			//不需要撤销
		}

	}
	
	
	/**
	 * type:
	 * 1：申请--通知管理员  
	 * 2:审核或者安排同意 --任课老师
	 * 3:不同意  有具体内容
	 * 4:通过的信息被退回 有具体内容
	 * 5:未通过或者审核的信息被撤销 不发送短信 有具体内容
	 * @param tipsay
	 * @param type
	 */
	public void sendMessageNew(Tipsay tipsay, String type) {
		String content=null;
		//申请
		if("1".equals(type)) {
			//tipsay 只含schoolId与operator
			//XX老师有申请代课信息需要审核或安排
			List<String> userIdlist = findUserRole(tipsay.getSchoolId());
			if(CollectionUtils.isNotEmpty(userIdlist)) {
				Teacher teacher = teacherService.findOne(tipsay.getOperator());
				if(teacher!=null) {
					content=teacher.getTeacherName()+"老师有申请代（管）课信息需要审核或安排。";
					pushMessage(null, userIdlist.toArray(new String[] {}), content, "代课管理消息");
				}
			}
		}else if("2".equals(type)){
			//您的课程信息已被更改，请查看代课详细
			Set<String> allTeacherIds = new HashSet<String>();
			if (StringUtils.isNotBlank(tipsay.getTeacherId())) {
				allTeacherIds.add(tipsay.getTeacherId());
			}
			if (StringUtils.isNotBlank(tipsay.getTeacherExIds())) {
				String[] exTeacher = tipsay.getTeacherExIds().split(",");
				List<String> tts = Arrays.asList(exTeacher);
				allTeacherIds.addAll(tts);
			}
			if (StringUtils.isNotBlank(tipsay.getNewTeacherId())) {
				allTeacherIds.add(tipsay.getNewTeacherId());
			}
			if(CollectionUtils.isNotEmpty(allTeacherIds)) {
				content="您的课程信息已被更改，请查看代管课详细。";
				pushMessage( allTeacherIds.toArray(new String[] {}),null, content, "代课管理消息");
			}
			if (StringUtils.isNotBlank(tipsay.getClassId())) {
				Clazz clazz = classService.findOne(tipsay.getClassId());
				if (clazz != null && StringUtils.isNotBlank(clazz.getTeacherId())) {
					pushMessage(new String[]{clazz.getTeacherId()},null, "您所属班级有新的" + ("1".equals(tipsay.getType()) ? "代" : "管") + "课信息", "所属班级" + ("1".equals(tipsay.getType()) ? "代" : "管") + "课管理消息");
				}
			}
		}else if("3".equals(type) || "4".equals(type)){
			Set<String> allTeacherIds = new HashSet<String>();
			Set<String> oldTeacherIds = new HashSet<String>();
			if (StringUtils.isNotBlank(tipsay.getTeacherId())) {
				allTeacherIds.add(tipsay.getTeacherId());
				oldTeacherIds.add(tipsay.getTeacherId());
			}
			if (StringUtils.isNotBlank(tipsay.getTeacherExIds())) {
				String[] exTeacher = tipsay.getTeacherExIds().split(",");
				List<String> tts = Arrays.asList(exTeacher);
				allTeacherIds.addAll(tts);
				oldTeacherIds.addAll(tts);
			}
			if (StringUtils.isNotBlank(tipsay.getNewTeacherId())) {
				allTeacherIds.add(tipsay.getNewTeacherId());
			}
			if (CollectionUtils.isEmpty(allTeacherIds)) {
				return;
			}
			List<User> userlist = userService.findUsersListByOwnerIds(
					allTeacherIds.toArray(new String[] {}), false);
			if (CollectionUtils.isEmpty(userlist)) {
				return;
			}
			List<Teacher> teacherList = teacherService.findListByIdIn(allTeacherIds
					.toArray(new String[] {}));
			Map<String, Teacher> teacherMap = EntityUtils.getMap(teacherList, e->e.getId());

			String oldTeacherName = "";
			String newTeacherName = "";
			if (teacherMap.containsKey(tipsay.getNewTeacherId())) {
				newTeacherName = teacherMap.get(tipsay.getNewTeacherId())
						.getTeacherName();
			}
			for (String s : oldTeacherIds) {
				if (teacherMap.containsKey(s)) {
					oldTeacherName = oldTeacherName + ","
							+ teacherMap.get(s).getTeacherName();
				}
			}
			if (StringUtils.isNotBlank(oldTeacherName)) {
				oldTeacherName = oldTeacherName.substring(1);
			}
			// 2018-01-01上午第一节,高一一班语文课
			content = makeScheduleMess(tipsay);// 组装的消息
			// 2018-01-01上午第一节,高一一班语文课(原老师为:B老师)
			content = content + "（原老师：" + oldTeacherName + "）";
			if("3".equals(type)) {
				//直接发给申请老师
				allTeacherIds=new HashSet<>();
				allTeacherIds.add(tipsay.getOperator());
				//您申请的:2018-01-01,上午第一节,高一一班语文课(原老师为:B老师),由xx老师代课，审核不通过
				content = "您申请的："+content + "，由" + newTeacherName + "老师代（管）课，审核不通过。";
			}else if("4".equals(type)) {
				//2018-01-01,上午第一节,高一一班语文课(原老师为:B老师),由xx老师代课，被退回
				content = content + "，由" + newTeacherName + "老师代（管）课，被撤销。";
				if (StringUtils.isNotBlank(tipsay.getClassId())) {
					Clazz clazz = classService.findOne(tipsay.getClassId());
					if (clazz != null && StringUtils.isNotBlank(clazz.getTeacherId())) {
						pushMessage(new String[]{clazz.getTeacherId()},null, "您所属班级" + ("1".equals(tipsay.getType()) ? "代" : "管") + "课已撤销。", "所属班级" + ("1".equals(tipsay.getType()) ? "代" : "管") + "课变动");
					}
				}
			}
			pushMessage(allTeacherIds.toArray(new String[] {}),null, content, "代课管理消息");
		}
		
	}
	
	

	/**
	 * 可以直接传入teacherIds 也可以userIds
	 * @param teacherIds
	 * @param userIds
	 * @param content
	 * @param title
	 */
	public void pushMessage(String[] teacherIds,String[] userIds,String content,String title) {
		Set<String> sendUserIds=new HashSet<>();
		if(teacherIds!=null && teacherIds.length>0) {
			List<User> userlist = userService.findUsersListByOwnerIds(teacherIds, false);
			if (CollectionUtils.isNotEmpty(userlist)) {
				sendUserIds.addAll(EntityUtils.getSet(userlist, e->e.getId()));
			}
		}
		if(userIds!=null && userIds.length>0) {
			sendUserIds.addAll(Arrays.asList(userIds));
		}
		if(CollectionUtils.isEmpty(sendUserIds)) {
			System.out.println("未找到对应用户");
			return;
		}
		//放到线程 不影响事务
		new Thread(new Runnable() {
            public void run() {
            	OpenApiOfficeService openApiOfficeService = null;// 接口
        		try {
        			openApiOfficeService = getOpenApiOfficeService();
        			if(openApiOfficeService==null) {
        				System.out.println("请检查6.0 OpenApiOfficeService:net.zdsoft.remote.openapi.service.OpenApiOfficeService == null");
        			}else {
        				String[] rowsContent = new String[] {content};
        				JSONArray userIdsJson = new JSONArray();
        				for (String uId : sendUserIds) {
        					userIdsJson.add(uId);
        				}
        				// 往办公公众号推送消息
        				JSONArray msgarr = new JSONArray();
        				JSONObject msg = new JSONObject();
        				msg.put("userIdArray", userIdsJson);
        	
        				msg.put("msgTitle", title);
        	
        				msg.put("rowsContent", rowsContent);
        				msgarr.add(msg);
        				try {
        					openApiOfficeService.pushWeikeMessage("", msgarr.toJSONString());
        					System.out.println("推送成功");
        				} catch (Exception e) {
        					System.out.println("推送报错");
        					e.printStackTrace();
        				}
        			}
        		} catch (Exception e) {
        			System.out.println("请检查6.0 OpenApiOfficeService:net.zdsoft.remote.openapi.service.OpenApiOfficeService == null]");
        		}
        		if(isSendMessage()) {
        			try {
        				List<User> allUserlist = userService.findListByIdIn(sendUserIds.toArray(new String[] {}));
        				Set<String> phoneset=new HashSet<>();
        				for(User u:allUserlist) {
        					if(StringUtils.isNotBlank(u.getMobilePhone())) {
        						phoneset.add(u.getMobilePhone());
        					}
        				}

        				if(CollectionUtils.isNotEmpty(phoneset)) {
        					String sms=smsRemoteService.sendSms(phoneset.toArray(new String[0]), content, null);
        					System.out.println("发送短信结果："+sms);
        				}
        			}catch (Exception e) {
        				System.out.println("发送短信报错");
        				e.printStackTrace();
        			}
        		}
            }
        }).start();
		
		
		
	}

	

	
	/**
	 * type:1:新增并且直接同意 
		2：新增待审核 3：审核同意 4：审核不同意 
		5：管理员安排
		6：撤销
	 * @param tipsay
	 * @param type
	 */
	@Deprecated
	public void sendMessageOld(Tipsay tipsay, String type) {
		
		if("5".equals(type) || "1".equals(type) || "3".equals(type)) {
			// 只推送给原老师，代课老师
			Set<String> allTeacherIds = new HashSet<String>();
			Set<String> oldTeacherIds = new HashSet<String>();
			if (StringUtils.isNotBlank(tipsay.getTeacherId())) {
				allTeacherIds.add(tipsay.getTeacherId());
				oldTeacherIds.add(tipsay.getTeacherId());
			}
			if (StringUtils.isNotBlank(tipsay.getTeacherExIds())) {
				String[] exTeacher = tipsay.getTeacherExIds().split(",");
				List<String> tts = Arrays.asList(exTeacher);
				allTeacherIds.addAll(tts);
				oldTeacherIds.addAll(tts);
			}
			if (StringUtils.isNotBlank(tipsay.getNewTeacherId())) {
				allTeacherIds.add(tipsay.getNewTeacherId());
			}
			if (CollectionUtils.isEmpty(allTeacherIds)) {
				return;
			}
			sendMessageAll(allTeacherIds.toArray(new String[] {}));
		}else {
			if("4".equals(type)) {
				sendMessageNoAgreen(tipsay);
			}
		}
		
		
		
//		OpenApiOfficeService openApiOfficeService = null;// 接口
//		try {
//			openApiOfficeService = Evn.getBean("openApiOfficeService");
//		} catch (Exception e) {
//			System.out.println("请检查6.0 OpenApiOfficeService:net.zdsoft.remote.openapi.service.OpenApiOfficeService == null]");
//			//logger.error("请检查6.0 net.zdsoft.remote.openapi.service.OpenApiOfficeService == null]");
//			// e.printStackTrace();
//			return;
//		}
//		// 组装json
//		/**
//		 * @param appCode
//		 *            应用code WeikeAppConstant中定义(默认往办公公众号推送消息,公文的code会推送到公文的公众号,
//		 *            若有其他公众号添加推送则需要修改代码)
//		 * @param userIds
//		 *            接收人(数字校园userId)
//		 * @param parm
//		 *            推送消息参数 ( msgTitle--微课消息列表页标题， headContent--头部内容，
//		 *            bodyTitle--正文标题， bodySubTitle--正文副标题,
//		 *            rowsContent--正文每行文本内容数组, footContent--底部文字,
//		 *            jumpType--跳转类型，url--跳转h5界面的url， ext--跳转原生界面所需要的参数)
//		 */
//
//		// 只推送给原老师，代课老师
//
//		Set<String> allTeacherIds = new HashSet<String>();
//		Set<String> oldTeacherIds = new HashSet<String>();
//		if (StringUtils.isNotBlank(tipsay.getTeacherId())) {
//			allTeacherIds.add(tipsay.getTeacherId());
//			oldTeacherIds.add(tipsay.getTeacherId());
//		}
//		if (StringUtils.isNotBlank(tipsay.getTeacherExIds())) {
//			String[] exTeacher = tipsay.getTeacherExIds().split(",");
//			List<String> tts = Arrays.asList(exTeacher);
//			allTeacherIds.addAll(tts);
//			oldTeacherIds.addAll(tts);
//		}
//		if (StringUtils.isNotBlank(tipsay.getNewTeacherId())) {
//			allTeacherIds.add(tipsay.getNewTeacherId());
//		}
//		if (CollectionUtils.isEmpty(allTeacherIds)) {
//			return;
//		}
//		List<User> userlist = userService.findUsersListByOwnerIds(
//				allTeacherIds.toArray(new String[] {}), false);
//		if (CollectionUtils.isEmpty(userlist)) {
//			return;
//		}
//
//		List<Teacher> teacherList = teacherService.findListByIdIn(allTeacherIds
//				.toArray(new String[] {}));
//		Map<String, Teacher> teacherMap = EntityUtils.getMap(teacherList, e->e.getId());
//
//		String[] rowsContent = null;
//
//		JSONArray userIds = new JSONArray();
//		for (User u : userlist) {
//			userIds.add(u.getId());
//		}
//		String oldTeacherName = "";
//		String newTeacherName = "";
//		if (teacherMap.containsKey(tipsay.getNewTeacherId())) {
//			newTeacherName = teacherMap.get(tipsay.getNewTeacherId())
//					.getTeacherName();
//		}
//		for (String s : oldTeacherIds) {
//			if (teacherMap.containsKey(s)) {
//				oldTeacherName = oldTeacherName + ","
//						+ teacherMap.get(s).getTeacherName();
//			}
//		}
//		if (StringUtils.isNotBlank(oldTeacherName)) {
//			oldTeacherName = oldTeacherName.substring(1);
//		}
//
//		// 2018-01-01,上午第一节,高一一班语文课
//		String content = makeScheduleMess(tipsay);// 组装的消息
//		// 2018-01-01,上午第一节,高一一班语文课(原上课老师为:B老师)
//		content = content + "(原上课老师:" + oldTeacherName + ")";
//		if ("1".equals(type)) {
//			// 2018-01-01,上午第一节,高一一班语文课(原上课老师为:B老师),将有A老师代课
//			content = content + ",将由" + newTeacherName + "老师代课";
//			
//		} else if("2".equals(type)){
//			content = content + ",由" + newTeacherName + "老师代课信息正提交审核中";
//		}else if("3".equals(type)){
//			content = content + ",由" + newTeacherName + "老师代课信息，审核通过";
//		}else if("4".equals(type)){
//			content = content + ",由" + newTeacherName + "老师代课信息， 审核不通过";
//		}else if("5".equals(type)){
//			content = content + ",经管理员安排由" + newTeacherName + "老师代课";
//		}else if("6".equals(type)){
//			content = content + ",由" + newTeacherName + "老师代课信息被取消，由原老师继续上课";
//		}
//		if (StringUtils.isNotBlank(tipsay.getRemark())) {
//			rowsContent = new String[] { content,
//					"备注：" + tipsay.getRemark() };
//		} else {
//			rowsContent = new String[] { content };
//		}
//		// 往办公公众号推送消息
//		JSONArray msgarr = new JSONArray();
//		JSONObject msg = new JSONObject();
//		msg.put("userIdArray", userIds);
//
//		msg.put("msgTitle", "代课管理消息");
//
//		msg.put("rowsContent", rowsContent);
//
//		msgarr.add(msg);
//
//		try {
//			openApiOfficeService.pushWeikeMessage("", msgarr.toJSONString());
//		} catch (Exception e) {
//			System.out.println("推送报错");
//			 e.printStackTrace();
//			return;
//		}

	}
	
	// 2018-01-01,上午第一节,高一一班语文课
	private String makeScheduleMess(Tipsay tipsay) {
		String returnStr = "";
		String dateStr = makeDateStr(tipsay);
		if (StringUtils.isNotBlank(dateStr)) {
			returnStr = returnStr + dateStr ;
		}
		String ss = BaseConstants.PERIOD_INTERVAL_Map.get(tipsay
				.getPeriodInterval());
		if (StringUtils.isNotBlank(ss)) {
			returnStr = returnStr + ss;
		}
		returnStr = returnStr + "第" + tipsay.getPeriod() + "节，";
		String className = "";
		if (tipsay.getClassType() == 1) {
			Map<String, Clazz> classNameMap = classService
					.findByIdInMapName(tipsay.getClassId());
			if (classNameMap.size() > 0) {
				className = classNameMap.get(tipsay.getClassId())
						.getClassNameDynamic();
			}
		} else {
			TeachClass ttClass = teachClassService.findOne(tipsay.getClassId());
			if (ttClass != null) {
				className = ttClass.getName();
			}
		}
		if (StringUtils.isNotBlank(className)) {
			returnStr = returnStr + className;
		}
		String subjectName = "";
		if(StringUtils.isNotBlank(tipsay.getSubjectId())){
			Course course = courseService.findOne(tipsay.getSubjectId());
			if (course != null) {
				subjectName = course.getSubjectName() + "课";
			}
			if (StringUtils.isNotBlank(subjectName)) {
				returnStr = returnStr + subjectName;
			}
		}
		
		return returnStr;
	}

	// 2018-01-01
	private String makeDateStr(Tipsay tipsay) {
		List<DateInfo> dateList = dateInfoService.findByWeek(
				tipsay.getSchoolId(), tipsay.getAcadyear(),
				tipsay.getSemester(), tipsay.getWeekOfWorktime());
		Date chooseDate = null;
		for (DateInfo d : dateList) {
			if (d.getWeekday() - 1 == tipsay.getDayOfWeek()) {
				chooseDate = d.getInfoDate();
				break;
			}
		}
		String chooseDateStr = "";
		if (chooseDate != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			chooseDateStr = formatter.format(chooseDate);
		}
		return chooseDateStr;
	}

	/**
	 * 辅助方法 原因：手机端 pc端都要用到
	 */
	@Override
	public String checkTimeByTeacher(List<String> ttIds,
			CourseSchedule courseSchedule) {

		// 老师这节课有没有在其他的课程上课
		CourseScheduleDto dto = new CourseScheduleDto();
		dto.setSchoolId(courseSchedule.getSchoolId());
		dto.setAcadyear(courseSchedule.getAcadyear());
		dto.setSemester(courseSchedule.getSemester());
		dto.setWeekOfWorktime(courseSchedule.getWeekOfWorktime());
		dto.setDayOfWeek(courseSchedule.getDayOfWeek());
		dto.setPeriod(courseSchedule.getPeriod());
		dto.setPeriodInterval(courseSchedule.getPeriodInterval());
		List<CourseSchedule> list2 = courseScheduleService
				.getByCourseScheduleDto(dto);
		if (CollectionUtils.isNotEmpty(list2)) {
			courseScheduleService.makeTeacherSet(list2);
			for (CourseSchedule cc : list2) {
				if(courseSchedule.getId().equals(cc.getId()) && CollectionUtils.intersection(cc.getTeacherIds(), ttIds)
						.size() > 0){
					return "该老师本来就是在这节课上课";
				}
				if (CollectionUtils.intersection(cc.getTeacherIds(), ttIds)
						.size() > 0) {
					return "原来教师在这节课已经在其他班级上课";
				}
			}
		}

		return null;
	}
	
	@Override
	public List<TipsayDto> tipsayToTipsayDto(Map<String, String> dateByWeek,
			List<Tipsay> tipsayList,boolean needHeadPic) {
		Map<String,String> typeMap = TipsayConstants.typeMap;
		Map<String, String> tipsayTypeMap = TipsayConstants.tipsayTypeMap;
		List<TipsayDto> dtoList = new ArrayList<TipsayDto>();
		TipsayDto dto;
		Set<String> classIds = new HashSet<String>();
		Set<String> teacherIds = new HashSet<String>();
		Set<String> subjectIds=new HashSet<String>();
		for (Tipsay item : tipsayList) {
			classIds.add(item.getClassId());
			teacherIds.addAll(playTeacherIds(item));
			if (StringUtils.isNotBlank(item.getNewTeacherId())) {
				teacherIds.add(item.getNewTeacherId());
			}
			subjectIds.add(item.getSubjectId());
		}
		Map<String, Teacher> teacherMap = new HashMap<String, Teacher>();
		Map<String, String> imgByTeacherId = new HashMap<String, String>();
		if (CollectionUtils.isNotEmpty(teacherIds)) {
			List<Teacher> teacherList = teacherService.findListByIdIn(teacherIds
					.toArray(new String[] {}));
			teacherMap = EntityUtils.getMap(teacherList, e->e.getId());
			if(needHeadPic){
				List<User> userlist = userService.findUsersListByOwnerIds(
						teacherIds.toArray(new String[] {}), true);
				if (CollectionUtils.isNotEmpty(userlist)) {
					for (User u : userlist) {
						if (StringUtils.isNotBlank(u.getAvatarUrl())) {
							imgByTeacherId.put(u.getOwnerId(), u.getAvatarUrl());
						}
					}
				}
			}
			
		}
		Map<String,Course> courseMap=new HashMap<String,Course>();
		if(CollectionUtils.isNotEmpty(subjectIds)){
			List<Course> courseList = courseService.findListByIdIn(subjectIds.toArray(new String[]{}));
			courseMap=EntityUtils.getMap(courseList, e->e.getId());
		}
		Map<String, String> classNameMap=new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(classIds)){
			classNameMap = getClassNameMap(classIds.toArray(new String[]{}));
		}

		for (Tipsay item : tipsayList) {
			dto = new TipsayDto();
			dto.setTipsay(item);
			dto.setTipsayId(item.getId());

			if (StringUtils.isNotBlank(item.getTeacherId())
					&& teacherMap.containsKey(item.getNewTeacherId())) {
				dto.setNewTeacherName(teacherMap.get(item.getNewTeacherId())
						.getTeacherName());
				dto.setNewTeacherSex(teacherMap.get(item.getNewTeacherId())
						.getSex());
				// 头像路径
				if (imgByTeacherId.containsKey(item.getNewTeacherId())) {
					dto.setNewImg(imgByTeacherId.get(item.getNewTeacherId()));
				}
			}
			String tName = "";
			if (StringUtils.isNotBlank(item.getTeacherId())
					&& teacherMap.containsKey(item.getTeacherId())) {
				dto.setOldTeacherSex(teacherMap.get(item.getTeacherId())
						.getSex());
				tName = tName + ","
						+ teacherMap.get(item.getTeacherId()).getTeacherName();
				if (imgByTeacherId.containsKey(item.getTeacherId())) {
					dto.setOldImg(imgByTeacherId.get(item.getTeacherId()));
				}
			}
			if (StringUtils.isNotBlank(item.getTeacherExIds())) {
				String[] exIds = item.getTeacherExIds().split(",");
				for (String s : exIds) {
					if (StringUtils.isNotBlank(s) && teacherMap.containsKey(s)) {
						if (dto.getOldTeacherSex() == null) {
							dto.setOldTeacherSex(teacherMap.get(s).getSex());
						}
						if (imgByTeacherId.containsKey(item.getTeacherId())
								&& StringUtils.isBlank(dto.getOldImg())) {
							dto.setOldImg(imgByTeacherId.get(item
									.getTeacherId()));
						}
						tName = tName + "," + teacherMap.get(s);
					}
				}

			}
			if (StringUtils.isNotBlank(tName)) {
				tName = tName.substring(1);
			}
			dto.setOldTeacherName(tName);
			if (classNameMap.containsKey(item.getClassId())) {
				dto.setClassName(classNameMap.get(item.getClassId()));
			}
			String dateStr=dateByWeek.get(item.getWeekOfWorktime() + "_"+ item.getDayOfWeek());
			dto.setDateStr(dateStr);
			dto.setTimeStr(makeTimeStr(item.getDayOfWeek()+"",item.getPeriodInterval(),item.getPeriod()));

			dto.setRemark(item.getRemark());
			if(courseMap.containsKey(item.getSubjectId())){
				dto.setSubjectName(courseMap.get(item.getSubjectId()).getSubjectName());
			}
			
			dto.setTypeName(typeMap.get(item.getType()));
			dto.setTipsayTypeName(tipsayTypeMap.get(item.getTipsayType()));
			dtoList.add(dto);
		}

		return dtoList;
	}
	
	@Override
	public Map<String, String> getClassNameMap(String[] classIds) {
		Map<String, String> classNameMap = new HashMap<String, String>();
		if (classIds!=null && classIds.length>0) {
			List<Clazz> clazzList = classService.findClassListByIds(classIds);
			if (CollectionUtils.isNotEmpty(clazzList)) {
				for (Clazz clazz : clazzList) {
					classNameMap
							.put(clazz.getId(), clazz.getClassNameDynamic());
				}
			}
			List<TeachClass> teachList = teachClassService.findListByIdIn(classIds);
			if (CollectionUtils.isNotEmpty(teachList)) {
				for (TeachClass teachClass : teachList) {
					classNameMap.put(teachClass.getId(), teachClass.getName());
				}
			}
		}
		return classNameMap;
	}
	@Override
	public boolean checkCourseSchedule(CourseSchedule courseSchedule,
			Tipsay tipsay,Boolean isContainTeacher) {
		// 课表id取到的值与（主表周次，星期，上下午，节次，班级id,科目id，现老师（一个）一致）
		if (StringUtils.equals(tipsay.getAcadyear(),
				courseSchedule.getAcadyear())
				&& tipsay.getSemester() == courseSchedule.getSemester()
				&& StringUtils.equals(tipsay.getSchoolId(),
						courseSchedule.getSchoolId())
				&& StringUtils.equals(tipsay.getClassId(),
						courseSchedule.getClassId())
				&& tipsay.getClassType() == courseSchedule.getClassType()
				&& tipsay.getWeekOfWorktime() == courseSchedule
						.getWeekOfWorktime()
				&& tipsay.getDayOfWeek() == courseSchedule.getDayOfWeek()
				&& tipsay.getPeriod() == courseSchedule.getPeriod()
				&& StringUtils.equals(tipsay.getPeriodInterval(),
						courseSchedule.getPeriodInterval())
				&& StringUtils.equals(tipsay.getSubjectId(),
						courseSchedule.getSubjectId())) {
			if(isContainTeacher) {
				// 教师的一致性 当前教师id 是tipsay 新教师
				Set<String> oldTSet = courseSchedule.getTeacherIds();
				Set<String> newTSet = new HashSet<String>();
				newTSet.add(tipsay.getNewTeacherId());
				if (CollectionUtils.isEmpty(newTSet)
						|| CollectionUtils.isEmpty(oldTSet)) {
					return false;
				}

				if (newTSet.size() == oldTSet.size()
						&& CollectionUtils.intersection(newTSet, oldTSet).size() == oldTSet
								.size()) {
					return true;
				}
				return false;
			}else {
				return true;
			}
			
		} else {
			return false;
		}

	}
	
	private Set<String> playTeacherIds(Tipsay tipsay) {
		Set<String> teacherIds = new HashSet<String>();
		if (StringUtils.isNotBlank(tipsay.getTeacherId())) {
			teacherIds.add(tipsay.getTeacherId());
		}
		if (StringUtils.isNotBlank(tipsay.getTeacherExIds())) {
			String[] exIds = tipsay.getTeacherExIds().split(",");
			for (String s : exIds) {
				if (StringUtils.isNotBlank(s)) {
					teacherIds.add(s);
				}
			}
		}
		return teacherIds;
	}
	
	private String makeTimeStr(String weekDay,String periodInterval, int period) {
		String returnStr = "";
		//增加周次
		String weekDayStr=BaseConstants.dayOfWeekMap.get(weekDay);
		if (StringUtils.isNotBlank(weekDayStr)) {
			returnStr = returnStr + weekDayStr;
		}
		String ss = BaseConstants.PERIOD_INTERVAL_Map.get(periodInterval);
		if (StringUtils.isNotBlank(ss)) {
			returnStr = returnStr + ss;
		}
		returnStr = returnStr + "第" + period + "节";
		return returnStr;
	}
	
	
	// 撤销前提：
	// 1、代课时间没有过去
	// 2、课表id取到的值与（主表周次，星期，上下午，节次，班级id,科目id，现老师（一个）一致）
	// 3、判断原老师1,2在这节课有没有课
	@Override
	public String checkCanDeleted(Tipsay tipsay, CourseSchedule courseSchedule) {
		
		List<CourseSchedule> ll = new ArrayList<CourseSchedule>();
		ll.add(courseSchedule);
		courseScheduleService.makeTeacherSet(ll);
		if (!checkCourseSchedule(ll.get(0), tipsay,true)) {
			return "代课/管课对应的课已经改变，不能撤销";
		}
		//代课时间没有过去
		Date nowDate=new Date();
		List<DateInfo> list = dateInfoService.findByWeek(tipsay.getSchoolId(), tipsay.getAcadyear(), tipsay.getSemester(), tipsay.getWeekOfWorktime());
		Date chooseDate=null;
		for(DateInfo d:list){
			if(d.getWeekday()-1==tipsay.getDayOfWeek()){
				chooseDate=d.getInfoDate();
			}
		}
		if(chooseDate==null){
			return "在节假日设置未找到对应代课时间";
		}
		if(DateUtils.compareForDay(nowDate, chooseDate) >=0){
			return "时间已经过去，不能操作";
		}
		Set<String> ttIdset = playTeacherIds(tipsay);
		// 验证的老师
		if(CollectionUtils.isEmpty(ttIdset)){
			return null;
		}
		List<String> ttIds = new ArrayList(ttIdset);
		return checkTimeByTeacher(ttIds, courseSchedule);
	}

	@Override
	public String checkTimesByTeacher(String acadyear, String semester, int minWorkTime, int maxWorkTime,
			String newTeacherId, List<CourseSchedule> scheduleList) {
		List<CourseSchedule> newList=new ArrayList<>();
		if(minWorkTime==maxWorkTime && minWorkTime!=0) {
			newList = courseScheduleService.findCourseScheduleListByTeacherId(acadyear, Integer.parseInt(semester),newTeacherId,minWorkTime);
		}else {
			newList=courseScheduleService.findCourseScheduleListByTeacherId(acadyear, Integer.parseInt(semester),newTeacherId,null);
			if(CollectionUtils.isNotEmpty(newList)) {
				newList = newList.stream().filter(e->(e.getWeekOfWorktime()>=minWorkTime && e.getWeekOfWorktime()<=maxWorkTime)).collect(Collectors.toList());
			}
		}
		if(CollectionUtils.isNotEmpty(newList)) {
			Set<String> newTimes=EntityUtils.getSet(newList, e->e.getWeekOfWorktime()+"_"+e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod());
			Set<String> oldTimes=EntityUtils.getSet(scheduleList,  e->e.getWeekOfWorktime()+"_"+e.getDayOfWeek()+"_"+e.getPeriodInterval()+"_"+e.getPeriod());
			Set<String> sameTime=(Set<String>) CollectionUtils.intersection(newTimes, oldTimes).stream().collect(Collectors.toSet());
			if(CollectionUtils.isNotEmpty(sameTime)) {
				Map<String, String> dMap = BaseConstants.dayOfWeekMap2;
				Map<String, String> pMap = BaseConstants.PERIOD_INTERVAL_Map;
				for(String s:sameTime) {
					String[] arr=s.split("_");
					String ss="第"+arr[0]+"周"+dMap.get(arr[1])+pMap.get(arr[2])+"第"+arr[3]+"节";
					return ss;
				}
				
			}
		}
		return null;
	}

	@Override
	public void saveAllApplyList(String unitId,List<Tipsay> tipsayList, List<TipsayEx> tipsayExList, List<CourseSchedule> scheduleList,
			String newTeacherId,String oldTeacherId,String applyType) {
		if(CollectionUtils.isNotEmpty(tipsayList)) {
			this.saveAll(tipsayList.toArray(new Tipsay[] {}));
		}
		if(CollectionUtils.isNotEmpty(tipsayExList)) {
			tipsayExService.saveAll(tipsayExList.toArray(new TipsayEx[] {}));
		}
		if(TipsayConstants.AUDITOR_TYPE_1.equals(applyType)) {
			//同意
			if(CollectionUtils.isNotEmpty(scheduleList)) {
				Set<String> ids=EntityUtils.getSet(scheduleList, e->e.getId());
				//删除courseSchedule 辅助老师
				teachPlanExService.deleteByTeacherIdAndPrimaryTableIdIn(null, ids.toArray(new String[] {}));
				courseScheduleService.saveAllEntitys(scheduleList.toArray(new CourseSchedule[] {}));
			}
			//消息推送
			Set<String> allTeacherIds=new HashSet<>();
			Set<String> classIds=new HashSet<>();
			for(Tipsay tt:tipsayList) {
				if (StringUtils.isNotBlank(tt.getTeacherId())) {
					allTeacherIds.add(tt.getTeacherId());
				}
				if (StringUtils.isNotBlank(tt.getTeacherExIds())) {
					String[] exTeacher = tt.getTeacherExIds().split(",");
					List<String> tts = Arrays.asList(exTeacher);
					allTeacherIds.addAll(tts);
				}
				if (StringUtils.isNotBlank(tt.getNewTeacherId())) {
					allTeacherIds.add(tt.getNewTeacherId());
				}
				if (StringUtils.isNotBlank(tt.getClassId())) {
					classIds.add(tt.getClassId());
				}
			}
			if(CollectionUtils.isNotEmpty(allTeacherIds)) {
				String content="您的课程信息已被更改，请查看代管课详细。";
				pushMessage( allTeacherIds.toArray(new String[] {}),null, content, "代课管理消息");
			}
			if (CollectionUtils.isNotEmpty(classIds)) {
				List<Clazz> clazzList = classService.findClassListByIds(classIds.toArray(new String[0]));
				String[] headMasters = clazzList.stream().filter(e -> StringUtils.isNotBlank(e.getTeacherId())).map(e -> e.getTeacherId()).toArray(String[]::new);
				pushMessage(headMasters, null, "您所属班级有新的教务安排代管课信息", "所属班级代管课管理消息");
			}
		}else {
			//消息推送  通知管理员
			//申请人就是oldTeacherId
			Tipsay tipsay = new Tipsay();
			tipsay.setSchoolId(unitId);
			tipsay.setOperator(oldTeacherId);
			sendMessageNew(tipsay, "1");
		}
		
	}
	//您的课程信息已被更改，请查看代课详细
	private void sendMessageAll(String[] teacherIds) {
		if(teacherIds==null) {
			return;
		}
		String mess="您的课程信息已被更改，请查看代管课详细";
		OpenApiOfficeService openApiOfficeService = null;// 接口
		try {
			openApiOfficeService = Evn.getBean("openApiOfficeService");
		} catch (Exception e) {
			System.out.println("请检查6.0 OpenApiOfficeService:net.zdsoft.remote.openapi.service.OpenApiOfficeService == null]");
			//logger.error("请检查6.0 net.zdsoft.remote.openapi.service.OpenApiOfficeService == null]");
			// e.printStackTrace();
			return;
		}
		String[] rowsContent = new String[] { mess };
		List<User> userlist = userService.findUsersListByOwnerIds(teacherIds, false);
		if (CollectionUtils.isEmpty(userlist)) {
			return;
		}

		JSONArray userIds = new JSONArray();
		for (User u : userlist) {
			userIds.add(u.getId());
		}
		// 往办公公众号推送消息
		JSONArray msgarr = new JSONArray();
		JSONObject msg = new JSONObject();
		msg.put("userIdArray", userIds);

		msg.put("msgTitle", "代课管理消息");

		msg.put("rowsContent", rowsContent);

		msgarr.add(msg);
		try {
			openApiOfficeService.pushWeikeMessage("", msgarr.toJSONString());
		} catch (Exception e) {
			System.out.println("推送报错");
			 e.printStackTrace();
			return;
		}
	}
	
	private void sendMessageNoAgreen(Tipsay tipsay) {
		//不同意
		OpenApiOfficeService openApiOfficeService = null;// 接口
		try {
			openApiOfficeService = Evn.getBean("openApiOfficeService");
		} catch (Exception e) {
			System.out.println("请检查6.0 OpenApiOfficeService:net.zdsoft.remote.openapi.service.OpenApiOfficeService == null]");
			//logger.error("请检查6.0 net.zdsoft.remote.openapi.service.OpenApiOfficeService == null]");
			// e.printStackTrace();
			return;
		}
		// 只推送给原老师，代课老师

		Set<String> allTeacherIds = new HashSet<String>();
		Set<String> oldTeacherIds = new HashSet<String>();
		if (StringUtils.isNotBlank(tipsay.getTeacherId())) {
			allTeacherIds.add(tipsay.getTeacherId());
			oldTeacherIds.add(tipsay.getTeacherId());
		}
		if (StringUtils.isNotBlank(tipsay.getTeacherExIds())) {
			String[] exTeacher = tipsay.getTeacherExIds().split(",");
			List<String> tts = Arrays.asList(exTeacher);
			allTeacherIds.addAll(tts);
			oldTeacherIds.addAll(tts);
		}
		if (StringUtils.isNotBlank(tipsay.getNewTeacherId())) {
			allTeacherIds.add(tipsay.getNewTeacherId());
		}
		if (CollectionUtils.isEmpty(allTeacherIds)) {
			return;
		}
		List<User> userlist = userService.findUsersListByOwnerIds(
				allTeacherIds.toArray(new String[] {}), false);
		if (CollectionUtils.isEmpty(userlist)) {
			return;
		}

		List<Teacher> teacherList = teacherService.findListByIdIn(allTeacherIds
				.toArray(new String[] {}));
		Map<String, Teacher> teacherMap = EntityUtils.getMap(teacherList, e->e.getId());

		

		
		String oldTeacherName = "";
		String newTeacherName = "";
		if (teacherMap.containsKey(tipsay.getNewTeacherId())) {
			newTeacherName = teacherMap.get(tipsay.getNewTeacherId())
					.getTeacherName();
		}
		for (String s : oldTeacherIds) {
			if (teacherMap.containsKey(s)) {
				oldTeacherName = oldTeacherName + ","
						+ teacherMap.get(s).getTeacherName();
			}
		}
		if (StringUtils.isNotBlank(oldTeacherName)) {
			oldTeacherName = oldTeacherName.substring(1);
		}

		// 2018-01-01,上午第一节,高一一班语文课
		String content = makeScheduleMess(tipsay);// 组装的消息
		// 2018-01-01,上午第一节,高一一班语文课(原上课老师为:B老师)
		content = content + "(原上课老师：" + oldTeacherName + ")";
		
		content = "您申请的："+content + ",由" + newTeacherName + "老师代（管）课信息， 审核不通过";
		String[] rowsContent = new String[] {content};
		JSONArray userIds = new JSONArray();
		boolean f=false;
		for (User u : userlist) {
			if(u.getOwnerId().equals(tipsay.getOperator())) {
				userIds.add(u.getId());
				f=true;
				break;
			}
		}
		if(!f) {
			return;
		}
		// 往办公公众号推送消息
		JSONArray msgarr = new JSONArray();
		JSONObject msg = new JSONObject();
		msg.put("userIdArray", userIds);

		msg.put("msgTitle", "代课管理消息");

		msg.put("rowsContent", rowsContent);

		msgarr.add(msg);

		try {
			openApiOfficeService.pushWeikeMessage("", msgarr.toJSONString());
		} catch (Exception e) {
			System.out.println("推送报错");
			 e.printStackTrace();
			return;
		}

	}
	
	/**
	 * 暂时只推送到oldTeacherId,newTeacherId 暂不考虑辅助教师
	 * @param oldTeacherId
	 * @param newTeacherId
	 * @param scheduleList
	 */
	private void sendMessage2_111(String oldTeacherId, String newTeacherId, List<Tipsay> tipsayList) {
		Set<String> allTeacherIds = new HashSet<String>();
		
		for(Tipsay t:tipsayList) {
			if (StringUtils.isNotBlank(t.getTeacherId())) {
				allTeacherIds.add(t.getTeacherId());
			}
			if (StringUtils.isNotBlank(t.getTeacherExIds())) {
				String[] exTeacher = t.getTeacherExIds().split(",");
				List<String> tts = Arrays.asList(exTeacher);
				allTeacherIds.addAll(tts);
			}
			if (StringUtils.isNotBlank(t.getNewTeacherId())) {
				allTeacherIds.add(t.getNewTeacherId());
			}
		}
		if(CollectionUtils.isEmpty(allTeacherIds)) {
			return;
		}
		sendMessageAll(allTeacherIds.toArray(new String[] {}));
		
		
//		OpenApiOfficeService openApiOfficeService = null;// 接口
//		try {
//			openApiOfficeService = Evn.getBean("openApiOfficeService");
//		} catch (Exception e) {
//			System.out.println("请检查6.0 OpenApiOfficeService:net.zdsoft.remote.openapi.service.OpenApiOfficeService == null]");
//			//logger.error("请检查6.0 net.zdsoft.remote.openapi.service.OpenApiOfficeService == null]");
//			// e.printStackTrace();
//			return;
//		}
//		// 组装json
//		/**
//		 * @param appCode
//		 *            应用code WeikeAppConstant中定义(默认往办公公众号推送消息,公文的code会推送到公文的公众号,
//		 *            若有其他公众号添加推送则需要修改代码)
//		 * @param userIds
//		 *            接收人(数字校园userId)
//		 * @param parm
//		 *            推送消息参数 ( msgTitle--微课消息列表页标题， headContent--头部内容，
//		 *            bodyTitle--正文标题， bodySubTitle--正文副标题,
//		 *            rowsContent--正文每行文本内容数组, footContent--底部文字,
//		 *            jumpType--跳转类型，url--跳转h5界面的url， ext--跳转原生界面所需要的参数)
//		 */
//
//		// 只推送给原老师，代课老师
//
//		Set<String> allTeacherIds = new HashSet<String>();
//		allTeacherIds.add(oldTeacherId);
//		allTeacherIds.add(newTeacherId);
//		
//		List<User> userlist = userService.findUsersListByOwnerIds(
//				allTeacherIds.toArray(new String[] {}), false);
//		if (CollectionUtils.isEmpty(userlist)) {
//			return;
//		}
//
//		List<Teacher> teacherList = teacherService.findListByIdIn(allTeacherIds
//				.toArray(new String[] {}));
//		Map<String, Teacher> teacherMap = EntityUtils.getMap(teacherList, e->e.getId());
//
//		String[] rowsContent = null;
//
//		JSONArray userIds = new JSONArray();
//		for (User u : userlist) {
//			userIds.add(u.getId());
//		}
//		String oldTeacherName = "";
//		String newTeacherName = "";
//		if (teacherMap.containsKey(newTeacherId)) {
//			newTeacherName = teacherMap.get(newTeacherId)
//					.getTeacherName();
//		}
//		if (teacherMap.containsKey(oldTeacherId)) {
//			oldTeacherName =  teacherMap.get(oldTeacherId).getTeacherName();
//		}
//
//		// 从日期(第几周)至日期(第几周)内，时间星期几第几节课等共几节课，原来老师 由新老师代课
//		String content = makeScheduleMessAll(tipsayList);// 组装的消息
//		content = content + "(原上课老师:" + oldTeacherName + "),将由" + newTeacherName + "老师代课";
//		rowsContent = new String[] { content };
//		// 往办公公众号推送消息
//		JSONArray msgarr = new JSONArray();
//		JSONObject msg = new JSONObject();
//		msg.put("userIdArray", userIds);
//
//		msg.put("msgTitle", "代课管理消息");
//
//		msg.put("rowsContent", rowsContent);
//
//		msgarr.add(msg);
//
//		try {
//			openApiOfficeService.pushWeikeMessage("", msgarr.toJSONString());
//		} catch (Exception e) {
//			System.out.println("推送报错");
//			 e.printStackTrace();
//			return;
//		}
		
	}
	// 从日期(第几周)至日期(第几周)内，时间星期几第几节课等共几节课
	private String makeScheduleMessAll(List<Tipsay> tipsayList) {
		//tipsayLis获取开始时间结束时间
		int minWeek=0;
		int minWeekDay=0;
		int maxWeek=0;
		int maxWeekDay=0;
		//每一周 存在的时间点
		Set<String> timeStrSet=new HashSet<>();
		//[0_1]
		List<String> timeKeyStr=new ArrayList<>();
		Map<String,List<Integer>> timePeriodMap=new HashMap<>();
		Tipsay minTipsay=null;
		Tipsay maxTipsay=null;
		for(Tipsay t:tipsayList) {
			String key=t.getDayOfWeek()+"_"+t.getPeriodInterval()+"_"+t.getPeriod();
			if(!timeStrSet.contains(key)) {
				timeKeyStr.add(t.getDayOfWeek()+"_"+t.getPeriodInterval());
				List<Integer> ll = timePeriodMap.get(t.getDayOfWeek()+"_"+t.getPeriodInterval());
				if(ll==null) {
					ll=new ArrayList<>();
					timePeriodMap.put(t.getDayOfWeek()+"_"+t.getPeriodInterval(), ll);
				}
				if(!ll.contains(t.getPeriod())) {
					ll.add(t.getPeriod());
				}
			}
			if(minWeek==0) {
				minWeek=t.getWeekOfWorktime();
				maxWeek=t.getWeekOfWorktime();
				minWeekDay=t.getDayOfWeek();
				maxWeekDay=t.getDayOfWeek();
				minTipsay=t;
				maxTipsay=t;
			}else {
				if(t.getWeekOfWorktime()<minWeek) {
					minWeek=t.getWeekOfWorktime();
					minWeekDay=t.getDayOfWeek();
					minTipsay=t;
				}else if(t.getWeekOfWorktime()==minWeek){
					if(t.getDayOfWeek()<minWeekDay) {
						minWeekDay=t.getDayOfWeek();
						minTipsay=t;
					}
				}
				
				if(t.getWeekOfWorktime()>maxWeek) {
					maxWeek=t.getWeekOfWorktime();
					maxWeekDay=t.getDayOfWeek();
					maxTipsay=t;
				}else if(t.getWeekOfWorktime()==maxWeek){
					if(t.getDayOfWeek()>maxWeekDay) {
						maxWeekDay=t.getDayOfWeek();
						maxTipsay=t;
					}
				}
			}
		}
		String minDate="";
		String maxDate="";
		if(minTipsay!=null) {
			minDate=makeDateStr(minTipsay);
			minDate=minDate+"(第"+minTipsay.getWeekOfWorktime()+"周)";
		}
		if(maxTipsay!=null ) {
			if(minWeek==maxWeek) {
				maxDate=minDate;
			}else{
				maxDate=makeDateStr(maxTipsay);
				maxDate=maxDate+"(第"+maxTipsay.getWeekOfWorktime()+"周)";
			}
		}
		//排序
		Collections.sort(timeKeyStr);
		String times="";
		Map<String, String> periodIntevalMap = BaseConstants.PERIOD_INTERVAL_Map;
		Map<String, String> weekDayMap = BaseConstants.dayOfWeekMap2;
		for(String s:timeKeyStr) {
			List<Integer> periodList = timePeriodMap.get(s);
			Collections.sort(periodList);
			String[] arr = s.split("_");
			String oo="";;
			for(Integer ii:periodList) {
				oo=oo+"、"+ii;
			}
			oo=oo.substring(1);
			times=times+weekDayMap.get(arr[0])+periodIntevalMap.get(arr[1])+"第"+oo+"节;";
		}
		return minDate+"至"+maxDate+"时间内，在"+times+"时间范围内，其中有"+tipsayList.size()+"节课（具体课程请从课表查看）";
	}

	@Override
	public List<Tipsay> findByCondition(String schoolId, String acadyear, Integer semester, String[] dates, String type, String[] teacherIds) {
		List<Tipsay> tipsayList = tipsayJdbcDao.findByCondition(schoolId, acadyear, semester, dates, type, teacherIds);
		if(tipsayList==null){
			return new ArrayList<Tipsay>();
		}
		return tipsayList;
	}
	
	/**
	 * 所有管理员
	 * @param unitId
	 * @return
	 */
	@Override
	public List<String> findUserRole(String unitId) {
		return customRoleService.findUserIdListByUserRole(unitId, TipsayConstants.SUBSYSTEM_86, TipsayConstants.EDUCATION_CODE);
	}

	@Override
	public void saveApplySelfList(String unitId, String applyTeacherId, List<Tipsay> tiplayList) {
		if(CollectionUtils.isNotEmpty(tiplayList)) {
			saveAll(tiplayList.toArray(new Tipsay[tiplayList.size()]));
			Tipsay tipsay=new Tipsay(); 
			tipsay.setSchoolId(unitId);
			tipsay.setOperator(applyTeacherId);
			sendMessageNew(tipsay, "1");
		}
		
	}
	
	public Boolean isSendMessage() {
		String value=systemIniRemoteService.findValue(TipsayConstants.SEND_MESSAGE);
		if("1".equals(value)) {
			return true;
		}else {
			return false;
		}
	}
	
	public OpenApiOfficeService getOpenApiOfficeService() {
		if(openApiOfficeService==null) {
			openApiOfficeService=Evn.getBean("openApiOfficeService");
			if(openApiOfficeService==null) {
				System.out.println("openApiOfficeService为null,需要开启dubbo服务");
			}
		}
		return openApiOfficeService;
	}

	@Override
	public void updateByTeacherId(String teacherId) {
		List<Tipsay> tipsayList = tipsayDao.findByTeacherId(teacherId, "%"+teacherId+"%");
		if(CollectionUtils.isNotEmpty(tipsayList)){
			for (Tipsay tipsay : tipsayList) {
				if(teacherId.equals(tipsay.getNewTeacherId())){
					tipsay.setNewTeacherId(BaseConstants.ZERO_GUID);
				}
				if(teacherId.equals(tipsay.getTeacherId())){
					tipsay.setTeacherId(null);
				}
				if(StringUtils.isNotBlank(tipsay.getTeacherExIds()) && tipsay.getTeacherExIds().contains(teacherId)){
					List<String> exList = Stream.of(tipsay.getTeacherExIds().split(",")).collect(Collectors.toList());
					exList.remove(teacherId);
					if(CollectionUtils.isNotEmpty(exList)){
						tipsay.setTeacherExIds(exList.stream().collect(Collectors.joining(",")));
					}else{
						tipsay.setTeacherExIds(null);
					}
				}
			}
			this.saveAll(tipsayList.toArray(new Tipsay[tipsayList.size()]));
		}
	}

	@Override
	public void deleteByClassId(String classId) {
		tipsayDao.deleteByClassId(classId);
	}

	@Override
	public List<Tipsay> findBySubjectId(String subjectId) {
		return tipsayDao.findBySubjectId(subjectId);
	}

}
