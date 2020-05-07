package net.zdsoft.desktop.action;

import static net.zdsoft.desktop.constant.DeskTopConstant.PERIOD_AM;
import static net.zdsoft.desktop.constant.DeskTopConstant.PERIOD_MORN;
import static net.zdsoft.desktop.constant.DeskTopConstant.PERIOD_NIGHT;
import static net.zdsoft.desktop.constant.DeskTopConstant.PERIOD_PM;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.OperationLog;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseScheduleRemoteService;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.utils.PJHeadUrlUtils;
import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.desktop.entity.DesktopCalendar;
import net.zdsoft.desktop.entity.UserApp;
import net.zdsoft.desktop.entity.UserSubscribe;
import net.zdsoft.desktop.msg.dto.MessageDto;
import net.zdsoft.desktop.msg.entity.Message;
import net.zdsoft.desktop.msg.service.MessageService;
import net.zdsoft.desktop.msg.utils.MessageUtils;
import net.zdsoft.desktop.service.DesktopCalendarService;
import net.zdsoft.desktop.service.UserAppService;
import net.zdsoft.desktop.service.UserSubscribeService;
import net.zdsoft.desktop.utils.Utils;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import net.zdsoft.system.entity.config.SystemIni;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.ModelRemoteService;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

/**
 * 标准版桌面功能区
 *
 * @author shenke
 * @since 2017.05.09
 */
@RequestMapping("/desktop/app")
@Controller
public class DesktopStandardAppDataAction extends DeskTopBaseAction {

	public static final String ODD_WEEK = "（单）";
	public static final String EVEN_WEEK = "（双）";

	@Autowired
	private MessageService messageService;
	@Autowired
	private UserAppService userAppService;
	@Autowired
	private ModelRemoteService modelRemoteService;
	@Autowired
	private ServerRemoteService serverRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private CourseScheduleRemoteService courseScheduleRemoteService;
	@Autowired
	private DesktopCalendarService desktopCalendarService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	@Autowired
	private UserSubscribeService userSubscribeService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;

	@ControllerInfo(ignoreLog=1, value="常用操作")
	@RequestMapping("/showCommonUseApp")
	@ResponseBody
	public String showCommonUseApp(String userId, String num) {
		String s = RedisUtils.get("my.common.use.app." + userId);
		if (StringUtils.isNotBlank(s))
			return s;
		if (StringUtils.isBlank(userId)) {
			userId = getUserId();
		}
		//有权限的所有模块
		List<Model> sessionModelList = SUtils.dt(modelRemoteService.findByUserId(userId), Model.class);
		Map<Integer, Model> sessionModelMap = EntityUtils.getMap(sessionModelList, Model::getId);

		User user = User.dc(userRemoteService.findOneById(userId, true));
		List<UserApp> userAppList = userAppService.findListBy("userId", userId);
		Map<Integer, UserApp> userAppMap = EntityUtils.getMap(userAppList, UserApp::getModelId);
		// 过滤模块
		Map<Integer, Model> appUseful = Maps.newHashMap();
		for (UserApp userApp : userAppList) {
			appUseful.put(userApp.getModelId(), sessionModelMap.get(userApp.getModelId()) );
		}
		EntityUtils.removeEmptyElement(appUseful);
		List<Integer> subSystemIds = appUseful.values().stream().map(Model::getSubSystem).collect(Collectors.toList());
		List<Server> serverList = SUtils.dt(serverRemoteService.findListByIn("subId", subSystemIds.toArray(new Integer[0])), Server.class);
		Map<Integer, Server> serverMap = EntityUtils.getMap(serverList, Server::getSubId);
		List<JSONObject> apps = Lists.newArrayList();
		boolean isSecondUrl = sysOptionRemoteService.isSecondUrl(getRequest().getServerName());
		// 拼接model url
		String filePath = sysOptionRemoteService.findValue(net.zdsoft.system.constant.Constant.FILE_PATH);
		for (Map.Entry<Integer, Model> modelEntry : appUseful.entrySet()) {
			Model model = modelEntry.getValue();
			JSONObject app = new JSONObject();
			app.put("name", model.getName());
			Server server = serverMap.get(model.getSubSystem());
			model.setOpenType(model.getOpenType() == null ? DeskTopConstant.MODEL_OPEN_TYPE_IFRAME : model.getOpenType());
			if (server != null) {
				String url = isSecondUrl ? server.getSecondUrl() : server.getUrl();
				String modelFullURL = UrlUtils.ignoreLastRightSlash(url) + "/"
						+ UrlUtils.ignoreFirstLeftSlash(model.getUrl());
				boolean isSplice = "7".equals(StringUtils.trim(model.getVersion()))
						|| DeskTopConstant.MODEL_OPEN_TYPE_DIV.equals(model.getOpenType());
				String fullUrl = isSplice ? modelFullURL : (UrlUtils.ignoreLastRightSlash(url)
						+ "/" + DeskTopConstant.UNIFY_LOGIN_URL
						+ "?url=" + modelFullURL + "&uid=" + user.getUsername());
				app.put("url", fullUrl);
			}
			app.put("icon", model.getPicture());
			String picture = model.getPicture();
			// 设置图片
			if (StringUtils.indexOf(picture, ".") < 0) {
				picture = picture + "_b.png";
			} else {
				picture = picture.substring(0, picture.indexOf(".")) + "_b."
						+ picture.substring(picture.indexOf(".") + 1);
			}
			picture = iconExists(filePath, picture) ? picture : "/default/images/default_b.png";
			String iconUrlString = UrlUtils.ignoreLastRightSlash(getFileURL())
					+ "/store/modelpic/" + UrlUtils.ignoreFirstLeftSlash(picture);
			app.put("icon", iconUrlString);
			app.put("openType", model.getOpenType());
			app.put("id", model.getId());
			app.put("displayOrder", userAppMap.get(model.getId()).getDisplayOrder());
			apps.add(app);
		}
		Collections.sort(apps, new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject o1, JSONObject o2) {
				return NumberUtils.toInt(o1.get("displayOrder").toString())
						- NumberUtils.toInt(o2.get("displayOrder").toString());
			}
		});
		JSONObject json = new JSONObject();
		json.put("title", "我的常用操作");
		json.put("col", "6");
		json.put("template", "1");
		json.put("apps", apps);
		RedisUtils.set("my.common.use.app." + userId,
				json.toJSONString(), 60);
		return json.toJSONString();
	}

	private boolean iconExists(String filePath, String picture) {
		try {
			return new File(filePath + File.separator + "store"
					+ File.separator + "modelpic" + File.separator + picture)
					.exists();
		} catch (Exception e) {
			return false;
		}
	}

	@ResponseBody
	@ControllerInfo(ignoreLog=1, value="通知公告")
	@RequestMapping("/showOfficeNotice")
	public String showOfficeNotice(String userId, String groupType, Integer num) {
		JSONObject json = new JSONObject();
		// Integer layOutType = getLayOut();
		try {
			JSONObject remoteMsgParam = new JSONObject();
			remoteMsgParam.put("userId", userId);
			remoteMsgParam.put("bulletinType",
					DeskTopConstant.OFFICE_BULLETIN_TYPE);
			num = (num == null?12:num);
			remoteMsgParam.put("pageNum", num);
			OpenApiOfficeService openApiOfficeService = this
					.<OpenApiOfficeService> getDubboService("openApiOfficeService");
			String msg = null;
			String notice = null;
			if (openApiOfficeService != null) {
				msg = openApiOfficeService.remoteBulletinDetails(remoteMsgParam
						.toJSONString());
				// 获取通知
				remoteMsgParam.put("bulletinType",
						DeskTopConstant.OFFICE_BULLETIN_TYPE_NOTICE);
				notice = openApiOfficeService
						.remoteBulletinDetails(remoteMsgParam.toString());
			}
			Map<String, Object> msgMap = getCommonParameter(msg,"bulletinDetails");
			Map<String, Object> noticeMap = getCommonParameter(notice,"bulletinDetails");
			// 解析通知
			List<JSONObject> noticeJSONObjectList = (List<JSONObject>) noticeMap.get("jsonObjectList");
			Model messageModel = (Model) msgMap.get("model");
			// 公告的jsonObject集合
			Server server = (Server) msgMap.get("server");
			String serverUrl = server != null ? sysOptionRemoteService.isSecondUrl(getRequest().getServerName()) ? server.getSecondUrl() : server.getUrl() : null;
			User user = getUser(userId);

			String isMoreUrl = getIsMoreUrl(messageModel, serverUrl, user);
			List<JSONObject> jsonObjects = (List<JSONObject>) msgMap.get("jsonObjectList");
			String singleMessageUrl1 = UrlUtils.ignoreLastRightSlash(serverUrl)
					+ "/office/desktop/app/info-viewDetail.action?bulletinId=";
			// 合并通知和公告
			jsonObjects = EntityUtils.merge(jsonObjects, noticeJSONObjectList);
			Collections.sort(jsonObjects, new Comparator<JSONObject>() {
				@Override
				public int compare(JSONObject o1, JSONObject o2) {
					try {
						Date o1Date = DateUtils.parseDate(
								o1.getString("createTime"), "yy-MM-dd HH:mm:ss");
						Date o2Date = DateUtils.parseDate(
								o2.getString("createTime"), "yy-MM-dd HH:mm:ss");
						return o1Date != null ? o2Date != null ? o2Date
								.after(o1Date) ? 1 : -1 : -1 : o1Date
								.before(o2Date) ? 1 : -1;
					} catch (ParseException e) {
						return -1;
					}
				}
			});
			//控制数量
			getInterceptNum(num, jsonObjects);

			if (CollectionUtils.isNotEmpty(jsonObjects)) {
				for (JSONObject jsonObject2 : jsonObjects) {
					String bulletinId = jsonObject2.getString("bulletinId");
					String urlString = singleMessageUrl1 + bulletinId;
					String titleString = jsonObject2.getString("title");
					jsonObject2.put("title", titleString);
					jsonObject2.put("titleString", titleString);
					jsonObject2.put("url", urlString);
					Date o2Date = DateUtils.parseDate(
							jsonObject2.getString("createTime"), "yy-MM-dd HH:mm:ss");
					jsonObject2.put("createTime", net.zdsoft.framework.utils.DateUtils.date2String(o2Date,"yy-MM-dd"));

				}
			} else {
				json.put("messageEmpty", "暂无内容");
			}
			String firstUrl = serverUrl + "/" + DeskTopConstant.UNIFY_LOGIN_URL
					+ "?uid=" + user.getUsername();
			json.put("firstUrl", firstUrl);
			json.put("openType", messageModel.getOpenType());
			json.put("serverName", server.getName());
			json.put("modelId", messageModel.getId());
			json.put("modelName", messageModel.getName());
			json.put("subId", server.getSubId());
			json.put("isMoreUrl", isMoreUrl);
			json.put("col", 3);
			json.put("officeNotice", jsonObjects);
		} catch (Exception e) {
			LOG.error("暂无消息");
		} finally {
			json.put("title", "通知公告");
		}
		return json.toJSONString();
	}

	@ResponseBody
	@ControllerInfo(ignoreLog=1, value="待办事项")
	@RequestMapping("/showTodo")
	public String showTodo(String userId, Integer num) {
		JSONObject json = new JSONObject();
		num = (num == null?8:num);
		try {
			JSONObject remoteMsgParam = new JSONObject();
			remoteMsgParam.put("userId", userId);
			OpenApiOfficeService openApiOfficeService = this
					.<OpenApiOfficeService> getDubboService("openApiOfficeService");
			String msg = null;
			if (openApiOfficeService != null) {

				msg = openApiOfficeService.remoteTodoWorkDetails(remoteMsgParam
						.toJSONString());
			}
			List<JSONObject> jsonObjects = SUtils.dt(msg,
					new TypeReference<List<JSONObject>>() {
					});
			//控制数量
			getInterceptNum(num, jsonObjects);

			List<Integer> modelIdsList = new ArrayList<Integer>();
			for (JSONObject jsonObject : jsonObjects) {
				Integer modelId = Integer.valueOf(jsonObject
						.getString("modelId"));
				modelIdsList.add(modelId);
			}
			// 查出所有的model
			List<Model> allModels = SUtils.dt(
					modelRemoteService.findListByIds(modelIdsList.toArray(new Integer[0])), Model.class);
			Map<Integer, Model> modelId = EntityUtils.getMap(allModels, Model::getId);
			// 得到server和modelId的map集合
			Map<Integer, Server> modelServerMap = new HashMap<Integer, Server>();
			Set<Integer> subSystemId = EntityUtils.getSet(allModels, Model::getSubSystem);
			List<Server> serverList = SUtils.dt(serverRemoteService.findListByIn("subId", subSystemId.toArray(new Integer[0])), Server.class);
			Map<Integer, Server> subIdServer = EntityUtils.getMap(serverList, Server::getSubId);
			for (Model model : allModels) {
				Server server = subIdServer.get(model.getSubSystem());
				modelServerMap.put(model.getId(), server);
			}
			json = new JSONObject();
			User user = getUser(userId);

			boolean isSecondUrl = sysOptionRemoteService.isSecondUrl(getRequest().getServerName());
			for (JSONObject jsonObject : jsonObjects) {
				Server server = modelServerMap.get(Integer.valueOf(jsonObject
						.getString("modelId")));
				String serverUrl = server != null ? isSecondUrl ? server.getSecondUrl() : server.getUrl() : null;
				Model model = modelId.get(Integer.valueOf(jsonObject
						.getString("modelId")));
				String singleMessageUrl = UrlUtils
						.ignoreLastRightSlash(serverUrl)
						+ "/"
						+ UrlUtils.ignoreFirstLeftSlash(model.getUrl());
				jsonObject.put("modelName", model.getName());
				String urlString = serverUrl + "/"
						+ DeskTopConstant.UNIFY_LOGIN_URL + "?uid="
						+ user.getUsername() + "&" + "url=" + singleMessageUrl;
				jsonObject.put("url", urlString);
				jsonObject.put("serverName", server.getName());

				jsonObject.put("modelId", model.getId());

				jsonObject.put("subId", server.getSubId());

				jsonObject.put("openType", model.getOpenType());
			}
			json.put("messageEmpty", "暂无内容");
			json.put("col", 3);
			json.put("todo", jsonObjects);
		} catch (Exception e) {
			LOG.error("暂无消息");
		} finally {
			json.put("title", "待办事项");
		}
		return json.toJSONString();
	}

	@ResponseBody
	@ControllerInfo(ignoreLog=1, value="我的消息")
	@RequestMapping("/showMessageNative")
	public String showMessageNative(String userId, String groupType, Integer num) {
		List<MessageDto> messageDtos = Lists.newArrayList();
		num = (num == null?12:num);
		Model messageModel = SUtils.dc(
				modelRemoteService.findOneById(new Integer(69052), true),
				Model.class);
		Server server = SUtils.dc(serverRemoteService.findOneBy("subId", messageModel.getSubSystem()), Server.class);
		String serverUrl = server != null ? sysOptionRemoteService.isSecondUrl(getRequest().getServerName()) ? server.getSecondUrl() : server.getUrl() : null;
		User user = getUser(userId);
		String isMoreUrl = getIsMoreUrl(messageModel, serverUrl, user);
		String singleMessageUrl = UrlUtils.ignoreLastRightSlash(serverUrl)
				+ "/" + messageModel.getUrl()
				+ "?desktopIn=2&{officeMsgReceiving.id}=";
		List<Message> messages = null;
		try {
			messages = messageService.findByUserId(userId, num);
		} catch (Exception e) {
			System.out.println(ExceptionUtils.getStackTrace(e));
		}

		JSONObject json = new JSONObject();
		if (CollectionUtils.isNotEmpty(messages)) {
			for (Message message : messages) {
				MessageDto messageDto = new MessageDto(
						MessageUtils.searchMessageType(message.getMessageType()));
				messageDto.setMessage(message);
				messageDto.setSendTime(DateFormatUtils.format(
						message.getSendTime(), "MM-dd"));
				messageDto.setUrl(serverUrl + "/"
						+ DeskTopConstant.UNIFY_LOGIN_URL + "?uid="
						+ user.getUsername() + "&" + "url=" + singleMessageUrl
						+ message.getId());
				messageDto.setModelId(messageModel.getId().toString());
				messageDto.setModelName(messageModel.getName());
				messageDto.setServerName(server.getName());
				messageDto.setSubId(server.getSubId());
				messageDtos.add(messageDto);
			}
		} else {
			json.put("messageEmpty", "暂无消息");
		}
		json.put("serverName", server.getName());
		json.put("modelId", 69052);
		json.put("modelName", messageModel.getName());
		json.put("isMoreUrl", isMoreUrl);
		json.put("col", 3);
		json.put("title", "我的消息");
		json.put("messageDtos", messageDtos);
		return json.toJSONString();
	}

	@ControllerInfo(ignoreLog=1, value="我的应用")
	@RequestMapping("/showCommonUseApp2")
	@ResponseBody
	public String showCommonUseApp2(String unitId, @RequestParam(required = false) String num, String userId, int ownerType, int unitClass) {

//		String results = RedisUtils.get("my.thrid.app." + getLoginInfo().getUserId());
//		if ( StringUtils.isNotBlank(results) ) {
//			return results;
//		}


		List<Server> serverList1 = SUtils.dt(serverRemoteService
				.findByOwnerTypeAndUnitIdAndUnitClass(ownerType, unitId, unitClass), Server.class);
		// 默认全添加
		for (Server server : serverList1) {
			userSubscribeService.addUserSubscribe(server.getId().toString(), userId);
		}
		List<Server> serverList = findShowServers(ownerType, unitId, unitClass, userId);

		SortUtils.ASC(serverList, "orderId");
		JSONObject json = new JSONObject();
		json.put("title", "我的应用");
		json.put("col", "6");
		json.put("template", "1");
		JSONArray apps = new JSONArray();
		if (CollectionUtils.isNotEmpty(serverList)) {
			SortUtils.ASC(serverList, "orderId");
			for (Server ss : serverList) {
				if (StringUtils.isNotBlank(num)) {
					if (apps.size() >= NumberUtils.toInt(num))
						break;
				}
				JSONObject app = new JSONObject();
				app.put("name", ss.getName());
				app.put("fullName", ss.getName());
				app.put("url", Utils.getPrefix(getRequest())
						+ "/recommendApp/wrapper/" + ss.getId());
				app.put("icon", UrlUtils.ignoreLastRightSlash(getFileURL())
						+ "/" + UrlUtils.ignoreFirstLeftSlash(ss.getIconUrl()));
				app.put("openType", DeskTopConstant.MODEL_OPEN_TYPE_NEW);
				app.put("id", ss.getId());
				apps.add(app);
			}
		}
		json.put("apps", apps);
//		RedisUtils.set("my.thrid.app."  + getLoginInfo().getUserId(), json.toJSONString());
		return json.toJSONString();
	}

    @ControllerInfo(ignoreLog=1, value="课表")
    @ResponseBody
    @RequestMapping("/showTimetable")
    public String showTimetable(String userId, String unitId) {
        String s = null;//RedisUtils.get("timetable." + getSession().getId() + "." + userId);
        if (StringUtils.isBlank(s)) {
            User user = User.dc(userRemoteService.findOneById(userId, true));
            JSONObject json = new JSONObject();
            int type = 1;
            String ownerId = null;
            if (getIntValue(user.getOwnerType()) == User.OWNER_TYPE_TEACHER) {
                json.put("title", "我的课程表");
                type = 1;
                ownerId = user.getOwnerId();
            }
            if (getIntValue(user.getOwnerType()) == User.OWNER_TYPE_STUDENT) {
                json.put("title", "我的课程表");
                type = 2;
				ownerId = user.getOwnerId();
            }
            if (getIntValue(user.getOwnerType()) == User.OWNER_TYPE_FAMILY) {
                json.put("title", "孩子课程表");
                type = 2;
                Family family = familyRemoteService.findOneObjectById(user.getOwnerId());
                ownerId = family.getStudentId();
            }
	        json.put("timeIntervalMap",new HashMap<String,Integer>(){
	        	{
	        		put(PERIOD_MORN,0);
	        		put(PERIOD_AM,4);
	        		put(PERIOD_PM,3);
	        		put(PERIOD_NIGHT,0);
	        	}
	        });
            Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, unitId), Semester.class);
            if(semester == null){
                return json.toJSONString();
            }
            
            
            boolean isweekEnd=false;
            json.put("weekEnd",isweekEnd);

            Calendar calendar = Calendar.getInstance();
            DateInfo dateInfo = SUtils.dc(dateInfoRemoteService.findByDate(unitId, semester.getAcadyear(), semester.getSemester(), calendar.getTime()), DateInfo.class);
            Integer week=2;
            if(dateInfo == null){
            	String val =systemIniRemoteService.findValue(SystemIni.SYSTEM_SHOW_SCHEDULE);
            	if(!"Y".equals(val)){
            		return json.toJSONString();//不能查看默认可以查看
				}
            }else{
            	week = dateInfo.getWeek();
            }
                
            //已经填充 班级名称
            List<CourseSchedule> tcs = SUtils.dt(courseScheduleRemoteService.findCourseScheduleListByPerId(semester.getAcadyear(), semester.getSemester(), week, ownerId, type + ""), CourseSchedule.class);
			// 是否存在单双周问题
			List<CourseSchedule> timetableCourseScheduleTmp = SUtils.dt(courseScheduleRemoteService.findCourseScheduleListByPerId(semester.getAcadyear(), semester.getSemester(), week + 1, ownerId, type + ""), CourseSchedule.class);
			if (CollectionUtils.isEmpty(timetableCourseScheduleTmp)) {
				timetableCourseScheduleTmp = SUtils.dt(courseScheduleRemoteService.findCourseScheduleListByPerId(semester.getAcadyear(), semester.getSemester(), week - 1, ownerId, type + ""), CourseSchedule.class);
			}
			if (CollectionUtils.isNotEmpty(timetableCourseScheduleTmp)) {
				for (CourseSchedule one : timetableCourseScheduleTmp) {
					if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(one.getWeekType()) || Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(one.getWeekType())) {
						tcs.add(one);
					}
				}
			}


            Set<String> classIds = EntityUtils.getSet(tcs, CourseSchedule::getClassId);
            List<Clazz> classes = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])),Clazz.class);
            List<TeachClass> teachClasses = SUtils.dt(teachClassRemoteService.findListByIds(classIds.toArray(new String[0])),TeachClass.class);
            Set<String> gradeIds=new HashSet<>();
            if(CollectionUtils.isNotEmpty(classes)) {
            	gradeIds.addAll(EntityUtils.getSet(classes, Clazz::getGradeId));
            }
            if(CollectionUtils.isNotEmpty(teachClasses)) {
            	for(TeachClass tt:teachClasses) {
            		if(StringUtils.isNotBlank(tt.getGradeId())) {
            			gradeIds.addAll(Arrays.asList(tt.getGradeId().split(",")));
            		}
            	}
            }
            
            Grade gdent = SUtils.dc(gradeRemoteService.findTimetableMaxRangeBySchoolId(null,gradeIds.toArray(new String[0])),Grade.class);
            if(Integer.valueOf(5).compareTo(gdent.getWeekDays())<0){
            	isweekEnd=true;
            }
            json.put("weekEnd",isweekEnd);
            
            Map<String, Integer> timeIntervalMap = new LinkedHashMap<String, Integer>();
            timeIntervalMap.put(PERIOD_MORN, gdent.getMornPeriods());
            timeIntervalMap.put(PERIOD_AM, gdent.getAmLessonCount());
            timeIntervalMap.put(PERIOD_PM, gdent.getPmLessonCount());
            timeIntervalMap.put(PERIOD_NIGHT, gdent.getNightLessonCount());
            
            Map<String, List<CourseSchedule>> map = new HashMap<String, List<CourseSchedule>>();
            Set<String> subjectIds = EntityUtils.getSet(tcs, CourseSchedule::getSubjectId);
            for (CourseSchedule tc : tcs) {
            	//过滤掉虚拟课程
            	if(tc.getClassType()==1 && BaseConstants.SUBJECT_TYPE_VIRTUAL.equals(tc.getSubjectType())) {
            		continue;
            	}
                int period = tc.getPeriod();
                String periodInverval = tc.getPeriodInterval();
                if ( periodInverval.equals(PERIOD_MORN) ) {
                    if ( timeIntervalMap.get(PERIOD_MORN) == 0 ) {
                        continue;
                    }
                }
                if ( periodInverval.equals(PERIOD_AM) ) {
                    if ( timeIntervalMap.get(PERIOD_AM) == 0 ) {
                        continue;
                    } else {
                        period = period + timeIntervalMap.get(PERIOD_MORN);
                    }
                }
                if ( periodInverval.equals(PERIOD_PM) ) {
                    if ( timeIntervalMap.get(PERIOD_PM) == 0 ) {
                        continue;
                    } else {
                        period = period + timeIntervalMap.get(PERIOD_MORN) + timeIntervalMap.get(PERIOD_AM);
                    }
                }
                if ( periodInverval.equals(PERIOD_NIGHT) ) {
                    if ( timeIntervalMap.get(PERIOD_NIGHT) == 0 ) {
                        continue;
                    } else {
                        period = period + timeIntervalMap.get(PERIOD_MORN) + timeIntervalMap.get(PERIOD_AM) + timeIntervalMap.get(PERIOD_PM);
                    }
                }
                List<CourseSchedule> ts = map.get(period+"");
                if (ts == null) {
                    ts = new ArrayList<CourseSchedule>();
                    map.put(period+"", ts);
                }
                ts.add(tc);
            }

            //utils
            class PairT<T,O> {
                private T shortName;
                private O fullName;

                public PairT(T shortName, O fullName) {
                    this.shortName = shortName;
                    this.fullName = fullName;
                }

                public T getShortName() {
                    return shortName;
                }

                public O getFullName() {
                    return fullName;
                }
            }
            JSONArray courseArray = JSONArray.parseArray(courseRemoteService.findListByIds(subjectIds.toArray(new String[0])));
            Map<String, PairT<String,String>> subjectMap = new HashMap<String, PairT<String, String>>();
            for(int i = 0; i < courseArray.size(); i ++){
                JSONObject courseObject = courseArray.getJSONObject(i);
                subjectMap.put(courseObject.getString("id"), new PairT<String,String>(courseObject.getString("shortName"),courseObject.getString("subjectName")));
            }

            JSONObject values = new JSONObject();
            int allCourseNum = 0;
            for (Map.Entry<String, Integer> entry : timeIntervalMap.entrySet()) {
                allCourseNum += entry.getValue();
            }
            for (int i = 1; i < allCourseNum+1; i++) {
                JSONObject course = new JSONObject();
                List<CourseSchedule> ts = map.get(i + "");
                if (ts == null)
                    continue;
                for (CourseSchedule t : ts) {
                    String weekday = String.valueOf(t.getDayOfWeek() + 1);
                    if ( subjectMap.get(t.getSubjectId()) == null ) {
                    	continue;
                    }
                    String c = subjectMap.get(t.getSubjectId()).getShortName();
                    if (c != null) {
						if (Integer.valueOf(CourseSchedule.WEEK_TYPE_ODD).equals(t.getWeekType())) {
							c = c + ODD_WEEK;
						}
						if (Integer.valueOf(CourseSchedule.WEEK_TYPE_EVEN).equals(t.getWeekType())) {
							c = c + EVEN_WEEK;
						}
					}

					if (course.get("courseName" + weekday) == null) {
						course.put("courseName" + weekday, c == null ? "" : c);
						String clazz=t.getClassName();
						course.put("className" + weekday, clazz == null ? "" : clazz);
						course.put("placeName" + weekday, t.getPlaceName());
						course.put("bgColor" + weekday, StringUtils.trimToEmpty(t.getBgColor()));
						course.put("bdColor" + weekday, StringUtils.trimToEmpty(t.getBorderColor()));
					} else {
						course.put("courseNameRe" + weekday, c == null ? "" : c);
						String clazz=t.getClassName();
						course.put("classNameRe" + weekday, clazz == null ? "" : clazz);
						course.put("placeNameRe" + weekday, t.getPlaceName());
						course.put("bgColorRe" + weekday, StringUtils.trimToEmpty(t.getBgColor()));
						course.put("bdColorRe" + weekday, StringUtils.trimToEmpty(t.getBorderColor()));
					}
				}
                values.put("p" + i, course);
            }
           
            json.put("values", values);
            json.put("timeIntervalMap",timeIntervalMap);
            json.put("allCourseNum",allCourseNum);
            s = json.toJSONString();
            RedisUtils.set("timetable." + getSession().getId() + "." + userId, s, 120);
        }
        return s;
    }
	@ResponseBody
	@ControllerInfo(ignoreLog=1, value="行事历")
	@RequestMapping("/showBehaveCalendar")
	public String showBehaveCalendar(String userId, String groupType,
			Integer num) {
		JSONObject json = new JSONObject();
		try {
			JSONObject remoteMsgParam = new JSONObject();
			remoteMsgParam.put("userId", userId);
			remoteMsgParam.put("bulletinType",DeskTopConstant.OFFICE_BEHAVECALENDAR_TYPE);
			num = (num == null?12:num);
			remoteMsgParam.put("pageNum", num);
			OpenApiOfficeService openApiOfficeService = this
					.<OpenApiOfficeService> getDubboService("openApiOfficeService");
			String msg = null;
			if (openApiOfficeService != null) {

				msg = openApiOfficeService.remoteBulletinDetails(remoteMsgParam
						.toJSONString());
			}
			Map<String, Object> msgMap = getCommonParameter(msg,"bulletinDetails");
			Model messageModel = (Model) msgMap.get("model");
			// 公告的jsonObject集合
			Server server = (Server) msgMap.get("server");
			String serverUrl = server != null ? sysOptionRemoteService.isSecondUrl(getRequest().getServerName()) ? server.getSecondUrl() : server.getUrl() : null;
			User user = getUser(userId);

			String isMoreUrl = getIsMoreUrl(messageModel, serverUrl, user);
			List<JSONObject> jsonObjects = (List<JSONObject>) msgMap.get("jsonObjectList");
			String singleMessageUrl1 = UrlUtils.ignoreLastRightSlash(serverUrl)
					+ "/office/desktop/app/info-viewDetail.action?bulletinId=";
			json = new JSONObject();
			if (CollectionUtils.isNotEmpty(jsonObjects)) {
				for (JSONObject jsonObject2 : jsonObjects) {
					String bulletinId = jsonObject2.getString("bulletinId");
					String urlString = singleMessageUrl1 + bulletinId;
					String titleString = jsonObject2.getString("title");
					jsonObject2.put("title", titleString);
					jsonObject2.put("titleString", titleString);
					jsonObject2.put("url", urlString);
					Date o2Date = DateUtils.parseDate(
							jsonObject2.getString("createTime"), "yy-MM-dd HH:mm:ss");
					jsonObject2.put("createTime", net.zdsoft.framework.utils.DateUtils.date2String(o2Date, "yy-MM-dd"));
				}
			} else {
				json.put("messageEmpty", "暂无内容");
			}
			String firstUrl = serverUrl + "/" + DeskTopConstant.UNIFY_LOGIN_URL
					+ "?uid=" + user.getUsername();
			json.put("firstUrl", firstUrl);
			json.put("openType", messageModel.getOpenType());
			json.put("serverName", server.getName());
			json.put("modelId", messageModel.getId());
			json.put("modelName", messageModel.getName());
			json.put("subId", server.getSubId());
			json.put("isMoreUrl", isMoreUrl);
			json.put("col", 3);
			json.put("behaveCalendar", jsonObjects);
		} catch (Exception e) {
			LOG.error("暂无消息");
		} finally {
			json.put("title", "行事历");
		}
		return json == null ? StringUtils.EMPTY : json.toJSONString();
	}

	@ResponseBody
	@ControllerInfo(ignoreLog=1, value="日历")
	@RequestMapping("/showCalendar")
	public String showCalendar(String userId) {
		if (StringUtils.isBlank(userId)) {
			userId = getLoginInfo().getUserId();
		}
		String calendarJSON = RedisUtils.get("my.calendar." + userId);
		if ( StringUtils.isNotBlank(calendarJSON) ) {
			return calendarJSON;
		}
		// 查找前三天的备忘录
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String timeString = net.zdsoft.framework.utils.DateUtils.date2String(new Date(), "yyyy-MM-dd");
		Date todayTime = null;
		try {
			todayTime = df.parse(timeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<DesktopCalendar> threeConstants = desktopCalendarService
				.findConstantsByTimeSlot(userId, todayTime,
						DateUtils.addDays(todayTime, 3));
		for (DesktopCalendar desktopCalendar : threeConstants) {
			String showTimeString = net.zdsoft.framework.utils.DateUtils.date2String(desktopCalendar.getBeginTime(), "MM/dd HH:mm");
			desktopCalendar.setShowTime(showTimeString);
		}
		JSONObject json = new JSONObject();
		json.put("calendars", threeConstants);
		json.put("col", 3);
		json.put("title", "日程表");
		RedisUtils.set("my.calendar." + userId, json.toJSONString());
		return json.toJSONString();
	}

	@ResponseBody
	@ControllerInfo(ignoreLog=1, value="我孩子的作业")
	@RequestMapping("/showHomeWork")
	public String showHomeWork(String userId,  Integer num,
			HttpServletRequest request) {
		String url = Evn.getString(Constant.HOME_WORK_URL);
		JSONObject json = new JSONObject();
		try {
			final User du = getUser(userId);
			Student student = null;
			List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
			// 得到家长的所有studentid
			if (Objects.equals(User.OWNER_TYPE_FAMILY, du.getOwnerType())) {
				Family family = SUtils.dc(
						familyRemoteService.findOneById(du.getOwnerId()),
						Family.class);
				//当前账号的学生
				student = Student.dc(studentRemoteService.findOneById(family.getStudentId()));
				List<User> userList = null;
				if ( StringUtils.isNotBlank(family.getMobilePhone()) ) {
					userList = SUtils.dt(userRemoteService.findByMobilePhones(User.OWNER_TYPE_FAMILY, family.getMobilePhone()), User.class);
					userList = userList.stream().filter(u -> {
						return u.getUserState().equals(Integer.valueOf(1)) && PWD.decode(u.getPassword()).equals(PWD.decode(du.getPassword()));
								}).collect(Collectors.toList());
				}
				List<Family> families = SUtils.dt(familyRemoteService.findListByIds(EntityUtils.<String,User>getList(userList,"ownerId").toArray(new String[0])),Family.class);
                //得到所有的孩子  userLists
 				List<String> studentIdList = EntityUtils.getList(families, Family::getStudentId);

				List<Student> studentList = Student.dt(studentRemoteService.findListByIds(studentIdList.toArray(new String[0])));
				Map<String, String> studentNameMap = EntityUtils.getMap(studentList, Student::getId, Student::getStudentName);

 				List<User> userList2 = SUtils.dt(userRemoteService.findByOwnerIds(studentIdList.toArray(new String[0])), User.class);
 				Map<String, User> userMap = EntityUtils.getMap(userList2, User::getOwnerId);

 				String headUrl = request.getContextPath();
 				for (User user : userList2) {
 					user.setAvatarUrl(PJHeadUrlUtils.getShowAvatarUrl(headUrl, user.getAvatarUrl(), getFileURL()));
				}

				for (int i = 0; i < families.size(); i++) {
					JSONObject jsonObject = new JSONObject();
					// 拼接头像url，并放入avatarUrl中
					User user1 = userMap.get(families.get(i).getStudentId());
					jsonObject.put("studentId", user1.getId());

					jsonObject.put("avatarUrl", user1.getAvatarUrl());
					// 孩子的名字
					jsonObject.put("studentName", studentNameMap.get(families.get(i)
							.getStudentId()));
					jsonObjects.add(jsonObject);
				}

			}

			List<User> userList1 = SUtils.dt(userRemoteService.findByOwnerIds(ArrayUtils.toArray(student.getId())), User.class);
			String userIdStudent = null;
			if(!CollectionUtils.isEmpty(userList1)){
				userIdStudent = userList1.get(0).getId();
			}

			List<JSONObject> studentGuidingCaseList = getHomeWorkList(url,
					userIdStudent, num);

			// 拼接详情页地址
			getHomeWorkUrl(url, studentGuidingCaseList, userIdStudent);

			// 把数据放在json中
			json.put("showStudentId", userIdStudent);
			json.put("studentName", student.getStudentName());
			json.put("col", 3);
			json.put("students", jsonObjects);
			json.put("homeWork", studentGuidingCaseList);
			json.put("url", url);
			json.put("num", num.toString());
		} catch (Exception e) {
			LOG.error("暂无消息");
		} finally {
			json.put("title", "我孩子的作业");
		}
		return json == null ? StringUtils.EMPTY : json.toJSONString();
	}

	@ControllerInfo(ignoreLog=1, value="我孩子的作业")
	@RequestMapping("/changeShowHome")
	public String changeShowHome(String studentId, String url, String num,
			HttpServletRequest request, ModelMap map) {
		try {
			List<JSONObject> studentGuidingCaseList = getHomeWorkList(url,
					studentId, Integer.valueOf(num));
			// 拼接详情页地址
			getHomeWorkUrl(url, studentGuidingCaseList, studentId);
			// 把数据放在json中
			map.put("data", studentGuidingCaseList);

		} catch (Exception e) {
			LOG.error("暂无消息");
		}
		return "/desktop/homepage/template/msg/show-home-work.ftl";
	}

	@ResponseBody
	@ControllerInfo(ignoreLog=1, value="周工作安排")
	@RequestMapping("/showWeeklyWork")
	public String showWeeklyWork(String userId, String groupType, Integer num) {
		JSONObject json = new JSONObject();
		try {
			JSONObject remoteMsgParam = new JSONObject();
			remoteMsgParam.put("userId", userId);
			remoteMsgParam.put("size", num);
			OpenApiOfficeService openApiOfficeService = this
					.<OpenApiOfficeService> getDubboService("openApiOfficeService");
			String msg = null;
			if (openApiOfficeService != null) {
				msg = openApiOfficeService
						.remoteOfficeWorkArrangeOutlines(remoteMsgParam
								.toJSONString());
			}
			Map<String, Object> msgMap = getCommonParameter(msg,"data");
			Model messageModel = (Model) msgMap.get("model");
			Server server = (Server) msgMap.get("server");
			String serverUrl = server != null ? sysOptionRemoteService.isSecondUrl(getRequest().getServerName()) ? server.getSecondUrl() : server.getUrl() : null;
			User user = getUser(userId);
			//更多的url
			String isMoreUrl = getIsMoreUrl(messageModel, serverUrl, user);
			List<JSONObject> jsonObjects = (List<JSONObject>) msgMap.get("jsonObjectList");
			String singleMessageUrl1 = isMoreUrl +"?workOutlineId=";
			json = new JSONObject();
			if (CollectionUtils.isNotEmpty(jsonObjects)) {
				for (JSONObject jsonObject2 : jsonObjects) {
					String arrangeQuery =jsonObject2.getString("arrangeQuery");
					String officeWorkId = jsonObject2.getString("officeWorkId");
					String urlString = singleMessageUrl1 + officeWorkId+"&{arrangeQuery}=" + arrangeQuery;
					jsonObject2.put("url", urlString);
					String titleString = jsonObject2.getString("title");
					jsonObject2.put("titleString", titleString);
					String startTime= jsonObject2.getString("start");
					String endTime = jsonObject2.getString("end");
					String showTime = startTime+"-"+endTime;
					jsonObject2.put("showTime", showTime);
				}
			} else {
				json.put("messageEmpty", "暂无内容");
			}
			json.put("serverName", server.getName());
			json.put("subId", server.getSubId());
			json.put("openType", 2);
			json.put("modelName", messageModel.getName());
			json.put("modelId", messageModel.getId());
			json.put("isMoreUrl", isMoreUrl);
			json.put("col", 3);
			json.put("weeklyWork", jsonObjects);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("暂无消息");
		} finally {
			json.put("title", "周工作安排");
		}
		return json == null ? StringUtils.EMPTY : json.toJSONString();
	}



	@ResponseBody
	@ControllerInfo(ignoreLog=1, value="不在岗教职工")
	@RequestMapping("/showNoTeachingStaff")
	public String showNoTeachingStaff(String userId, String groupType,
			Integer num) {
		JSONObject json = new JSONObject();
		try {
			JSONObject remoteMsgParam = new JSONObject();
			remoteMsgParam.put("userId", userId);
			OpenApiOfficeService openApiOfficeService = this
					.<OpenApiOfficeService> getDubboService("openApiOfficeService");
			String msg = null;
			if (openApiOfficeService != null) {

				msg = openApiOfficeService.getAbsenceUsers(remoteMsgParam
						.toJSONString());
			}
			List<JSONObject> jsonObject = SUtils.dt(msg, JSONObject.class);
			if (CollectionUtils.isEmpty(jsonObject)) {
				json.put("messageEmpty", "暂无内容");
			}
			json.put("col", 3);
			json.put("noTeachingStaff", jsonObject);
		} catch (Exception e) {
			LOG.error("暂无消息");
		} finally {
			json.put("title", "不在岗教职工");
		}
		return json == null ? StringUtils.EMPTY : json.toJSONString();
	}
    
	
//	@ResponseBody
//	@ControllerInfo(ignoreLog=1, value="openapi调用接口次数")
//	@RequestMapping("/showOpenapiCount")
//	public String showOpenapiCount(String userId) {
//		if (StringUtils.isBlank(userId)) {
//			userId = getLoginInfo().getUserId();
//		}
////		String openapiJSON = RedisUtils.get("my.openapi." + getLoginInfo().getUserId());
////		if ( StringUtils.isNotBlank(openapiJSON) ) {
////			return openapiJSON;
////		}
//		//查找
//		List<OperationLog> operationLogs = operationLogDataSyncService.findByDescription(DeskTopConstant.OPENAPI_DESCRIPTION);
//		Set<String> ticketKeys =EntityUtils.getSet(operationLogs, "parameter");
//		List<Developer> developers = new ArrayList<Developer>();
//		if(CollectionUtils.isNotEmpty(ticketKeys)) {
//			for (String ticketKey : ticketKeys) {
//				Developer developer = developerService.findByTicketKey(ticketKey);
//				developers.add(developer);
//			}
//		}
//		//全部的接口信息
//		List<Interface> interfaces = interfaceService.findAll();
//		//默认取前一周的接口次数
//		Calendar c = Calendar.getInstance();
//		String endTime=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//		try {
//			c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(endTime));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		int week=c.get(Calendar.WEEK_OF_YEAR); 
//		c.set(Calendar.WEEK_OF_YEAR,week-1); 
//		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
//		
//		JSONObject map = new JSONObject();
//		map.put("developers", developers);
//		map.put("interfaces", interfaces);
//		map.put("title", "openapi统计图");
//		map.put("startTime", startTime);
//		map.put("endTime", endTime);
//		return map.toJSONString();
//	}
	
	
//	@ControllerInfo(ignoreLog=1, value="显示openapi调用接口次数")
//	@RequestMapping("/changeOpenapiCount")
//	public String changeShowOpenapiCount(String ticketKey,String start,String end,String interfaceId,ModelMap map) {
//         
//		JSONObject jsonObject = new JSONObject();
//		String[] legendDatas = null;
//		Integer[][] loadingDataInt = null;
//		String showFormat = "yyyy-MM-dd";
////		Integer avgNum = DeskTopConstant.OPENAPI_AVERAGE;
//		String[] xAxisDatas = null;
//	        try {
//	            Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(start);// 定义起始日期
//	            Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(end);// 定义结束日期
//	            
//	            Calendar c = Calendar.getInstance();
//	            
//	            //判断两个日期相差多少，超过35 天的用周表示，超过98月的用月份表示，超过365天的用年表示
//	            Integer showNum;
//	            Integer showDateForm;
//	            //把相差的天数12等分
//	            int days = (int) ((d2.getTime() - d1.getTime()) / (1000*3600*24));
//	            c.setTime(d1);
//	            int year1 = c.get(Calendar.YEAR);
//	            c.setTime(d2);
//	            int year2 = c.get(Calendar.YEAR);
//	            if(days >= 365) {
//	            	showFormat = "yyyy";
//	            	showNum = year2 - year1;
//	            	showDateForm = Calendar.YEAR;
//	            }else if(days >= 98) {
//	            	c.setTime(d1);
//	            	int month1 = c.get(Calendar.MONTH);
//	            	c.setTime(d2);
//	            	int month2 = c.get(Calendar.MONTH);
//	            	showFormat = "yyyy-MM";
//	            	showNum = year1 == year2 ? month2 - month1: month2 - month1+12;
//	            	showDateForm = Calendar.MONTH;
//	            }
////	            else if(days >= 35){
////	            	c.setTime(d1);
////	            	int week1 = c.get(Calendar.WEEK_OF_YEAR);
////	            	c.setTime(d2);
////	            	int week2 = c.get(Calendar.WEEK_OF_YEAR);
////	            	if(year1 != year2) {
////	            		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
////	            		c.setTime(sdf.parse(year1+"-12-31"));
////	            		int week =  c.get(Calendar.WEEK_OF_YEAR);
////	            		week2 = week != 53 ?52+week2 : 53+week2;
////	            	}
////	            	showFormat = "一年中第w个星期";
////	            	showNum = week2 - week1;
////	            	showDateForm = Calendar.WEEK_OF_YEAR;
////	            }
//	            else {
//	            	showFormat = "yyyy-MM-dd";
//	            	showNum = days;
//	            	showDateForm = Calendar.DATE;
//	            }
//	            
//	            xAxisDatas = new String[showNum+1];
//	            
//	            xAxisDatas[0] = new SimpleDateFormat(showFormat).format(d1);
//	            for (int i = 1; i < xAxisDatas.length-1; i++) {
//					c.setTime(new SimpleDateFormat(showFormat).parse(xAxisDatas[i-1]));
//					int num1=c.get(showDateForm); 
//					c.set(showDateForm,num1+1); 
//					String dayAfter=new SimpleDateFormat(showFormat).format(c.getTime());
//					xAxisDatas[i] = dayAfter;
//				}
//	            xAxisDatas[xAxisDatas.length-1] = new SimpleDateFormat(showFormat).format(d2);
//	        }catch (Exception e) {
//				// TODO: handle exception
//			}
//		legendDatas = new String[] { "次数" };
//		loadingDataInt = new Integer[1][xAxisDatas.length];
//		
//		Interface interface1 = interfaceService.findOne(interfaceId);
//		String uri = interface1.getUri();
//		//转化格式
//		if(uri.contains("{id}")) {
//			uri= uri.replace("{id}", "id");
//		}
//		
//		//查找出当前接口和调用的日志信息
//		List<OperationLog> operationLogs = operationLogDataSyncService.findByParameterAndDescription(ticketKey,DeskTopConstant.OPENAPI_DESCRIPTION+uri);
//		if(CollectionUtils.isNotEmpty(operationLogs)) {
//			for (int j = 0; j < xAxisDatas.length; j++) {
//				List<OperationLog> operationLogs1 = getUseOpenapiCount(xAxisDatas, operationLogs, j,start,end,showFormat);
//				int num = operationLogs1.size();
//				loadingDataInt[0][j] = num;
//			}
//		}else {
//			for (int j = 0; j < xAxisDatas.length; j++) {
//				int num = 0;
//				loadingDataInt[0][j] = num;
//			}
//		}
//		jsonObject.put("xAxisData", xAxisDatas);
//		jsonObject.put("legendData", legendDatas);
//		// loadingData数据顺序和xAxisData相对
//		jsonObject.put("loadingData", loadingDataInt);
//
//		JSONObject map1 = new JSONObject();
//		map1.put("jsonData", jsonObject);
//		map1.put("col", 12);
//		map1.put("title", "教师统计图");
//		map1.put("differentColors", true);
//		map1.put("barW", 50);
//		map1.put("showLegend", true);
//		map1.put("showToolBox", true);
//		map1.put("titleSize", 30);
//		map1.put("colors",
//				"['#b2d8fc','#bcdf69', '#deb968', '#e0796a', '#ee99c3','#a599ef',  '#ffa96e', '#cb92f3','#6791e7', '#89e7b2', '#efd87a']");
//		map1.put("type", "histogram");
//		
//		JSONObject json = JSONObject.parseObject( map1.toJSONString());
//		if (json.containsKey("jsonData")) {
//			String o = JSONUtils.toJSONString(json.get("jsonData"));
//			json.put("jsonData", o);
//		}
//		map.put("data", json);
//		return "/desktop/homepage/template/echarts/openapi-showCount.ftl";
//	}

	/**
	 * @param xAxisDatas
	 * @param operationLogs
	 * @param j
	 * @return
	 */
	private List<OperationLog> getUseOpenapiCount(String[] xAxisDatas, List<OperationLog> operationLogs, int j,String start,String end, String showFormat) {
			operationLogs = EntityUtils.filter(operationLogs,new EntityUtils.Filter<OperationLog>(){
				@Override
				public boolean doFilter(OperationLog operationLog) {
					try {
						Date o1Date;
						Date o2Date;
						if(j == 0) {
							o1Date = DateUtils.parseDate(
									start, "yyyy-MM-dd");
						}else {
							o1Date = DateUtils.parseDate(
									xAxisDatas[j], showFormat);
						}
						if(j == (xAxisDatas.length-1)) {
							Calendar c = Calendar.getInstance();
							try {
								c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(end));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
							int day=c.get(Calendar.DATE); 
							c.set(Calendar.DATE,day+1); 
						    String	endShowTime=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
							o2Date = DateUtils.parseDate(
									endShowTime, "yyyy-MM-dd");
						}else {
							o2Date = DateUtils.parseDate(
									xAxisDatas[j+1], showFormat);
						}
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String dateString = df.format(operationLog.getLogTime());
						Date logTime = df.parse(dateString);
						return ! (o1Date.before(logTime) && o2Date.after(logTime));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return false;
				}
			});
		return operationLogs;
	}
	/**
	 * @param studentGuidingCaseList
	 */
	private void getHomeWorkUrl(String url,
			List<JSONObject> studentGuidingCaseList, String studentId) {
		// 拼接详情页地址
		String homeWorkDetailsUrl = UrlUtils.ignoreLastRightSlash(url)
				+ "/course/guidingCase/getStudentGuidingCaseInfo.htm?caseId=";
		for (JSONObject jsonObject : studentGuidingCaseList) {

			String urlString = homeWorkDetailsUrl
					+ jsonObject.getString("caseId") + "&studentId="
					+ studentId;

			jsonObject.put("homeWorkUrl", urlString);

			jsonObject.put("studentId", studentId);

		}
	}

	/**
	 * @param url
	 * @param studentId
	 * @return
	 * @throws IOException
	 */
	private List<JSONObject> getHomeWorkList(String url, String studentId,
			Integer num) throws IOException {
		String dataUrl = UrlUtils.ignoreLastRightSlash(url)
				+ "/course/guidingCase/getStudentGuidingCase.htm?studentId="+studentId;
		num = (num == null?0:num);
		dataUrl = dataUrl + "&num=" + num;
		String data = UrlUtils.readContent(dataUrl, getRequest().getSession()
				.getId(), "utf8");

		JSONObject json1 = JSONObject.parseObject(data);

		List<JSONObject> studentGuidingCaseList = null;
		studentGuidingCaseList = SUtils.dt(
				json1.getString("studentGuidingCaseList"), JSONObject.class);

		return studentGuidingCaseList;
	}

	//得到用户（user）
	private User getUser(String userId) {
		User user = SUtils.dc(userRemoteService.findOneById(userId, true),
				User.class);
		return user;
	}
	/**
	 * 得到更多的url
	 */
	private String getIsMoreUrl(Model messageModel, String serverUrl,
			User user) {
		String singleMessageUrl = UrlUtils.ignoreLastRightSlash(serverUrl)
				+ "/" + messageModel.getUrl();
		String isMoreUrl = serverUrl + "/"
				+ DeskTopConstant.UNIFY_LOGIN_URL + "?uid="
				+ user.getUsername() + "&" + "url=" + singleMessageUrl;
		return isMoreUrl;
	}
	/**
	 * 	得到当前的模块（messageModel）
	 * @param jsonObject
	 * @return
	 */
	private Model getModel(JSONObject jsonObject){
		String modelId = jsonObject.getString("modelId");
		Model messageModel = SUtils.dc(
				modelRemoteService.findOneById(new Integer(modelId), true),
				Model.class);
		return messageModel;

	}

	/**
	 * 得到三个公共的值 jsonObjectList,model,server
	 */
	private <K,O> Map<String, Object> getCommonParameter(String msg,String condition){
		Map<String,Object> map = new HashMap<String,Object>();
		List<JSONObject> jsonObjectList = null;
		JSONObject jsonObject = null;
		Model model = null;
		Server server = null;
		if (StringUtils.isNotBlank(msg)) {
			jsonObject = SUtils.dc(msg,JSONObject.class);
			model = getModel(jsonObject);
			server = SUtils.dc(serverRemoteService.findOneBy("subId", model.getSubSystem()), Server.class);
		}
		if (condition == null) {
			jsonObjectList = SUtils.dt(msg,new TypeReference<List<JSONObject>>() {});
		}else {
			String showDetails = jsonObject.getString(condition);
			jsonObjectList = SUtils.dt(showDetails,JSONObject.class);
		}
		map.put("jsonObjectList", jsonObjectList);
		map.put("model", model);
		map.put("server", server);
		return map;
	}
	/**
	 * @param num
	 * @param jsonObjects
	 * 进行数量的截取
	 */
	private void getInterceptNum(Integer num, List<JSONObject> jsonObjects) {
		if(jsonObjects.size()>num){
			   List<JSONObject> list1 = new ArrayList<JSONObject>();
			   list1 = jsonObjects.subList(num, jsonObjects.size());
			   list1.clear();
			   jsonObjects.removeAll(list1);
		}
	}

	private List<Server> findShowServers(int ownerType, String unitId, int unitClass, String userId){
        List<Server> serverList = SUtils.dt(serverRemoteService.findByOwnerTypeAndUnitIdAndUnitClass(ownerType, unitId, unitClass), Server.class);
        List<Integer> serverIds1 = EntityUtils.getList(serverList, Server::getId);
        List<UserSubscribe> userSubscribes = userSubscribeService.findByUserId(userId);
        List<Integer> serverIds2 = EntityUtils.getList(userSubscribes, UserSubscribe::getServerId);
        //取两个集合的交集
        List<Integer> showServerIds = (List<Integer>) CollectionUtils.intersection(serverIds1, serverIds2);
        List<Server> showServerList = SUtils.dt(serverRemoteService.findListByIds(showServerIds.toArray(new Integer[showServerIds.size()])), Server.class);

        return showServerList;


    }
}
