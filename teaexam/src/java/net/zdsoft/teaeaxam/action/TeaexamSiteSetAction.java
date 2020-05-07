package net.zdsoft.teaeaxam.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.remote.utils.PJHeadUrlUtils;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.teaeaxam.constant.TeaexamConstant;
import net.zdsoft.teaeaxam.dto.TeaexamSubjectSettingDto;
import net.zdsoft.teaeaxam.entity.TeaexamInfo;
import net.zdsoft.teaeaxam.entity.TeaexamRegisterInfo;
import net.zdsoft.teaeaxam.entity.TeaexamSite;
import net.zdsoft.teaeaxam.entity.TeaexamSiteSetting;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;
import net.zdsoft.teaeaxam.service.TeaexamInfoService;
import net.zdsoft.teaeaxam.service.TeaexamRegisterInfoService;
import net.zdsoft.teaeaxam.service.TeaexamSiteService;
import net.zdsoft.teaeaxam.service.TeaexamSiteSettingService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectService;

/**
 * 考场安排
 * @author weixh
 * 2018年10月25日	
 */
@Controller
@RequestMapping("/teaexam/siteSet")
public class TeaexamSiteSetAction extends TeaExamBaseAction {
	@Autowired
	private TeaexamInfoService teaexamInfoService;
	@Autowired
	private TeaexamSubjectService teaexamSubjectService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private TeaexamSiteSettingService teaexamSiteSettingService;
	@Autowired
	private TeaexamSiteService teaexamSiteService;
	@Autowired
	private TeaexamRegisterInfoService teaexamRegisterInfoService;
	@Autowired
	private SchoolRemoteService schoolRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	
	private boolean hasNoCard = false;// 有学生没考号
	private boolean hasCard = false;// 有考号
	
	@ControllerInfo("考试信息列表")
	@RequestMapping("/index/page")
	public String examIndex(ModelMap map, HttpServletRequest request) {
		int year = NumberUtils.toInt(request.getParameter("year"));
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		if(year == 0) {
			year = nowy;
		}
		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		map.put("year", year);
		
		List<TeaexamInfo> teaexamInfoList = teaexamInfoService.findByInfoYearType(getLoginInfo().getUnitId(), year, TeaexamConstant.EXAM_INFOTYPE_0);
		teaexamInfoList = teaexamInfoList.stream().filter(e->e.getState() == 2).collect(Collectors.toList());
		Set<String> examIdSet = new HashSet<String>();
		for(TeaexamInfo exam : teaexamInfoList){
			examIdSet.add(exam.getId());
		}
		List<TeaexamSubject> subList = new ArrayList<TeaexamSubject>();
		if(CollectionUtils.isNotEmpty(examIdSet)){
			subList = teaexamSubjectService.findByExamIds(examIdSet.toArray(new String[0]));
			if (CollectionUtils.isNotEmpty(subList)) {
				Map<String, McodeDetail> mm = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-XD"), new TR<Map<String, McodeDetail>>(){});
				for (TeaexamInfo exam : teaexamInfoList) {
					StringBuilder subNames = new StringBuilder();
					Iterator<TeaexamSubject> sit = subList.iterator();
					for (;sit.hasNext();) {
						TeaexamSubject sub = sit.next();
						if (exam.getId().equals(sub.getExamId())) {
							subNames.append(sub.getSubjectName()+"("+mm.get(sub.getSection()+"").getMcodeContent()+"),");
							sit.remove();
						}
					}
					if (StringUtils.isNotBlank(subNames)) {
						subNames.setLength(subNames.length()-1);
					}
					exam.setSubNames(subNames.toString());
				} 
			}
		}
		map.put("teaexamInfoList", teaexamInfoList);
		return "/teaexam/siteSet/examInfoList.ftl";
	}
	
	@ControllerInfo("设置tab")
	@RequestMapping("/setIndex/{examId}/page")
	public String setIndex(@PathVariable String examId, ModelMap map, HttpServletRequest request) {
		String type = StringUtils.trimToNull(request.getParameter("type"));
		String canEdit = StringUtils.trimToNull(request.getParameter("canEdit"));
		int year = NumberUtils.toInt(request.getParameter("year"));
		map.put("examId", examId);
		map.put("type", type);
		map.put("canEdit", canEdit);
		map.put("year", year);
		return "/teaexam/siteSet/setHead.ftl";
	}
	
	@ControllerInfo("科目考场设置")
	@RequestMapping("/setIndex/{examId}/room/page")
	public String roomIndex(@PathVariable String examId, ModelMap map, HttpServletRequest request) {
		map.put("examId", examId);
		int totalStu = 0;
		List<TeaexamSubject> subList = teaexamSubjectService.findByExamIds(new String[] {examId});
		List<TeaexamRegisterInfo> infoList = teaexamRegisterInfoService
				.findByStatusAndExamIdIn(TeaexamConstant.STATUS_PASS, new String[] { examId });
		List<TeaexamSiteSetting> setList = teaexamSiteSettingService.findSettingByExamId(examId);
		Set<String> schIds = EntityUtils.getSet(setList, TeaexamSiteSetting::getSchoolId);
		Map<String, String> schNames = EntityUtils.getMap(
				SUtils.dt(schoolRemoteService.findListByIds(schIds.toArray(new String[0])), new TR<List<School>>() {
				}), School::getId, School::getSchoolName);
		// <schoolId,value> 考点
		Map<String, TeaexamSite> siteMap = EntityUtils
				.getMap(teaexamSiteService.findBySchoolIds(schIds.toArray(new String[0])), TeaexamSite::getSchoolId);
		// 学科考点
		Map<String, List<TeaexamSiteSetting>> setMap = EntityUtils.getListMap(setList,
				TeaexamSiteSetting::getSubjectInfoId, Function.identity());
		// 学科报名信息
		Map<String, List<TeaexamRegisterInfo>> infoMap = EntityUtils.getListMap(infoList,
				TeaexamRegisterInfo::getSubjectInfoId, Function.identity());
		List<TeaexamSubjectSettingDto> dtos = new ArrayList<>();
		TeaexamSubjectSettingDto dto ; 
		for(TeaexamSubject sub : subList) {
			dto = new TeaexamSubjectSettingDto();
			dto.setSubject(sub);
			// 学生人数
			int all = 0;
			int has = 0;
			int no = 0;
			List<TeaexamRegisterInfo> infos = infoMap.get(sub.getId());
			if(CollectionUtils.isNotEmpty(infos)) {
				for(TeaexamRegisterInfo tea : infos) {
					all++;
					if(StringUtils.isNotBlank(tea.getSeatNo())) {
						has ++;
					} else {
						no ++;
					}
				}
			}
			dto.setHasCount(has);
			dto.setNoCount(no);
			dto.setStuCount(all);
			totalStu += all;
			// 考点
			List<TeaexamSiteSetting> sets = setMap.get(sub.getId());
			if(CollectionUtils.isNotEmpty(sets)) {
				Iterator<TeaexamSiteSetting> setIt = sets.iterator();
				while(setIt.hasNext()) {
					TeaexamSiteSetting set = setIt.next();  
					TeaexamSite site = siteMap.get(set.getSchoolId());
					if(site == null) {
						setIt.remove();
						continue;
					}	
					set.setPerNum(site.getCapacity());
					set.setSchoolName(schNames.get(set.getSchoolId()));
				}
				dto.setSites(sets);
			}
			dtos.add(dto);
			dto = null;
		}
		
		map.put("totalStu", totalStu);
		map.put("dtos", dtos);
		return "/teaexam/siteSet/subRoomList.ftl";
	}
	
	@ControllerInfo("科目考场设置")
	@RequestMapping("/setIndex/{examId}/room/edit")
	public String roomEdit(@PathVariable String examId, ModelMap map, HttpServletRequest request) {
		map.put("examId", examId);
		String subInfoId = StringUtils.trimToNull(request.getParameter("subInfoId"));
		TeaexamSubject sub = teaexamSubjectService.findOne(subInfoId);
		if(sub == null) {
			return errorFtl(map, "考试科目不存在！");
		}
		Unit edu = Unit.dc(unitRemoteService.findOneById(getLoginInfo().getUnitId()));
		List<TeaexamSite> sites = teaexamSiteService.findByUnionCode(edu.getUnionCode(), null);
		if(CollectionUtils.isEmpty(sites)) {
			return errorFtl(map, "没有可选择的考点考场数据！");
		}
		// 报名数
		List<TeaexamRegisterInfo> infos = teaexamRegisterInfoService.findByStatusAndRoomNo(TeaexamConstant.STATUS_PASS+"", null, subInfoId, examId);
		int allCount = CollectionUtils.size(infos);
		List<TeaexamRegisterInfo> hasList = infos.stream().filter(e->StringUtils.isNotEmpty(e.getSeatNo())).collect(Collectors.toList());
		int hasCount = CollectionUtils.size(hasList);
		int noCount = allCount - hasCount;
		// 考场数 得到已经被其他科目使用的考场数
		List<TeaexamSiteSetting> sets = teaexamSiteSettingService.findSettingByExamId(examId);
		// 该科目设置
		Map<String, TeaexamSiteSetting> setMap = EntityUtils.getMap(sets.stream().filter(e->subInfoId.equals(e.getSubjectInfoId())).collect(Collectors.toList()), TeaexamSiteSetting::getSchoolId);
		Map<String, Integer> siteNum = new HashMap<>();
		for(TeaexamSiteSetting set : sets) {
			if(subInfoId.equals(set.getSubjectInfoId())) {// 本学段科目的
				continue;
			}
			Integer num = siteNum.get(set.getSchoolId());
			if(num == null) {
				num = 0;
			}
			num += set.getRoomNum();
			siteNum.put(set.getSchoolId(), num);
		}
		// 各考点已安排教师的考场数
		Map<String, Integer> schNum = new HashMap<>();
		if(CollectionUtils.isNotEmpty(infos)) {
			List<String> schRms = new ArrayList<>();
			for(TeaexamRegisterInfo info : infos) {
				if(schRms.contains(info.getLocationId()+StringUtils.trimToEmpty(info.getRoomNo()))) {
					continue;
				}
				schRms.add(info.getLocationId()+StringUtils.trimToEmpty(info.getRoomNo()));
				Integer count = schNum.get(info.getLocationId());
				if(count == null) {
					count = 0;
				}
				count++;
				schNum.put(info.getLocationId(), count);
			}
		}
		Set<String> schIds = EntityUtils.getSet(sites, TeaexamSite::getSchoolId);
		List<Unit> schs = Unit.dt(unitRemoteService.findListByIds(schIds.toArray(new String[0])));
		Map<String, String> schNames = EntityUtils.getMap(schs, Unit::getId, Unit::getUnitName);
		for(TeaexamSite site : sites) {
			site.setSiteName(schNames.get(site.getSchoolId()));
			TeaexamSiteSetting set = setMap.get(site.getSchoolId());
			if(set != null) {
				site.setSetNum(set.getRoomNum());
			}
			Integer num = siteNum.get(site.getSchoolId());
			if(num == null) {
				num = 0;
			}
			site.setValidNum(site.getSiteNum() - num);// 可用数量，这个数量里面包含本科目之前已经保存的数量
			Integer unum = schNum.get(site.getSchoolId());
			if(unum == null) {
				unum = 0;
			}
			site.setUsedNum(unum);
		}
		Iterator<TeaexamSite> sit = sites.iterator();
		while(sit.hasNext()) {
			TeaexamSite set = sit.next();
			if(set.getValidNum() <= 0 && set.getSetNum() == 0) {// 过滤：本科目没维护过数据且没有可用考场的考点
				sit.remove();
			}
		}
		map.put("subInfoId", subInfoId);
		map.put("allCount", allCount);
		map.put("hasCount", hasCount);
		map.put("noCount", noCount);
		map.put("sub", sub);
		map.put("sites", sites);
		return "/teaexam/siteSet/siteSetEdit.ftl";
	}
	
	@ControllerInfo("科目考场设置")
	@RequestMapping("/setIndex/{examId}/room/save")
	@ResponseBody
	public String siteSetSave(@PathVariable String examId, TeaexamSubjectSettingDto dto, ModelMap map, HttpServletRequest request) {
		String subInfoId = StringUtils.trimToNull(request.getParameter("subInfoId"));
		if(dto == null || CollectionUtils.isEmpty(dto.getSites())) {
			return error("没有可保存的数据！");
		}
		for(TeaexamSiteSetting set : dto.getSites()) {
			set.setExamId(examId);
			set.setSubjectInfoId(subInfoId);
		}
		try {
			ResultDto result = teaexamSiteSettingService.saveSet(dto.getSites(), subInfoId);
			if(StringUtils.isEmpty(result.getMsg())) {
				result.setMsg("保存成功！");
			}
			return success(result.getMsg());
		} catch (RuntimeException re) {
			log.error(re.getMessage(), re);
			return error("保存失败："+re.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("保存失败！");
		}
	}
	
	@ControllerInfo("考号设置")
	@RequestMapping("/setIndex/{examId}/cardno/page")
	public String cardNoIndex(@PathVariable String examId, ModelMap map, HttpServletRequest request) {
		map.put("examId", examId);
		List<TeaexamRegisterInfo> infoList = teaexamRegisterInfoService
				.findByStatusAndExamIdIn(TeaexamConstant.STATUS_PASS, new String[] { examId });
		if(CollectionUtils.isEmpty(infoList)) {
			return errorFtl(map, "考试下还没有审核通过的考生！");
		}
		examCardList("", examId, null, null, TeaexamConstant.STATUS_PASS+"", null, null, map, request);
		map.put("hasNoCard", hasNoCard);
		map.put("hasCard", hasCard);
		return "/teaexam/siteSet/cardNoList.ftl";
	}
	
	@ControllerInfo("考号生成")
	@RequestMapping("/setIndex/{examId}/cardno/generate")
	@ResponseBody
	public String createCardNo(@PathVariable String examId, ModelMap map, HttpServletRequest request) {
		String type = request.getParameter("type");
		try {
			teaexamRegisterInfoService.saveAutoCardNo(examId, type);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("生成失败！");
		}
		return success("生成成功！");
	}
	
	@ControllerInfo("考场考生设置")
	@RequestMapping("/setIndex/{examId}/arrange/index")
	public String arrangeIndex(@PathVariable String examId, ModelMap map, HttpServletRequest request) {
		map.put("examId", examId);
		List<TeaexamSubject> subList = teaexamSubjectService.findByExamIds(new String[] {examId});
		if(CollectionUtils.isEmpty(subList)) {
			return errorFtl(map, "没有可安排的考试科目！");
		}
		int all = teaexamRegisterInfoService.findCountByStatus(examId, TeaexamConstant.STATUS_PASS);
		if(all == 0) {
			return errorFtl(map, "没有可安排的考生！");
		}
		String status = RedisUtils.lpop(TeaexamConstant.AUTO_ARRANGE_PREFIX 
				+ TeaexamConstant.AUTO_ARRANGE_TYPE_EXAM + examId + TeaexamConstant.AUTO_ARRANGE_POSTFIX_STATUS);
		if(TeaexamConstant.AUTO_ARRANGE_STATUS_ING.equals(status)) {
			map.put("status", "1");
		} else if(TeaexamConstant.AUTO_ARRANGE_STATUS_FAIL.equals(status)) {
			map.put("status", "-1");
		} else {
			map.put("status", "9");
		}
		int count = teaexamRegisterInfoService.findCountByArrange(examId);
		if(count == 0) {
			return "/teaexam/siteSet/autoArrange.ftl";
		}
		String subInfoId = StringUtils.trimToNull(request.getParameter("subInfoId"));
		if(StringUtils.isEmpty(subInfoId)) {
			subInfoId = subList.get(0).getId();
		}
		map.put("subInfoId", subInfoId);
		map.put("subList", subList);
		return "/teaexam/siteSet/arrangeIndex.ftl";
	}
	
	@ControllerInfo("自动编排")
	@RequestMapping("/setIndex/{examId}/arrange/saveAuto")
	@ResponseBody
	public String arrangeAuto(@PathVariable String examId, ModelMap map, HttpServletRequest request) {
		String subInfoId = StringUtils.trimToNull(request.getParameter("subInfoId"));
		String key;
		String status;
		if (StringUtils.isNotEmpty(subInfoId)) {
			key = TeaexamConstant.AUTO_ARRANGE_PREFIX + TeaexamConstant.AUTO_ARRANGE_TYPE_SUB
					+ subInfoId + TeaexamConstant.AUTO_ARRANGE_POSTFIX_STATUS;
			status = RedisUtils.lpop(key);
			if (TeaexamConstant.AUTO_ARRANGE_STATUS_ING.equals(status)) {
				return JSON.toJSONString(new ResultDto().setSuccess(true).setCode("11").setMsg("科目安排正在自动分配中..."));
			} 
		}
		key = TeaexamConstant.AUTO_ARRANGE_PREFIX 
				+ TeaexamConstant.AUTO_ARRANGE_TYPE_EXAM + examId + TeaexamConstant.AUTO_ARRANGE_POSTFIX_STATUS;
		
		status = RedisUtils.lpop(key);
		if(TeaexamConstant.AUTO_ARRANGE_STATUS_ING.equals(status)) {
			return JSON.toJSONString(new ResultDto().setSuccess(true).setCode("11").setMsg("考试安排正在自动分配中..."));
		}
		try {
			ArrangeThread at = new ArrangeThread(examId, subInfoId);
			at.start();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			String str = TeaexamConstant.AUTO_ARRANGE_TYPE_SUB + subInfoId;
			if(StringUtils.isEmpty(subInfoId)) {
				str = TeaexamConstant.AUTO_ARRANGE_TYPE_EXAM + examId;
			}
			RedisUtils.lpush(TeaexamConstant.AUTO_ARRANGE_PREFIX 
					+ str
					+ TeaexamConstant.AUTO_ARRANGE_POSTFIX_STATUS, TeaexamConstant.AUTO_ARRANGE_STATUS_FAIL);
			return error("自动分配失败，请重试");
		}
		return JSON.toJSONString(new ResultDto().setSuccess(true).setCode("11").setMsg("正在自动分配中..."));
	}
	
	class ArrangeThread extends Thread {
		private String examId;
		private String subInfoId;
		
		public ArrangeThread(String examId, String subInfoId) {
			super();
			this.examId = examId;
			this.subInfoId = subInfoId;
		}

		public String getExamId() {
			return examId;
		}

		public void setExamId(String examId) {
			this.examId = examId;
		}

		public String getSubInfoId() {
			return subInfoId;
		}

		public void setSubInfoId(String subInfoId) {
			this.subInfoId = subInfoId;
		}

		@Override
		public void run() {
			teaexamRegisterInfoService.saveAutoArrange(examId, subInfoId);
		}
		
	}
	
	@ControllerInfo("自动编排")
	@RequestMapping("/setIndex/{examId}/arrange/autoMsg")
	@ResponseBody
	public String getArrangeMsg(@PathVariable String examId, ModelMap map, HttpServletRequest request) {
		String subInfoId = StringUtils.trimToNull(request.getParameter("subInfoId"));
		String str = TeaexamConstant.AUTO_ARRANGE_TYPE_SUB + subInfoId;
		if(StringUtils.isEmpty(subInfoId)) {
			str = TeaexamConstant.AUTO_ARRANGE_TYPE_EXAM + examId;
		}
		String status = RedisUtils.get(TeaexamConstant.AUTO_ARRANGE_PREFIX + str + TeaexamConstant.AUTO_ARRANGE_POSTFIX_STATUS);
		if(TeaexamConstant.AUTO_ARRANGE_STATUS_ING.equals(status)) {
			return JSON.toJSONString(new ResultDto().setSuccess(true).setCode("11").setMsg("科目正在自动分配中..."));
		} else if(TeaexamConstant.AUTO_ARRANGE_STATUS_FAIL.equals(status)) {
			return JSON.toJSONString(new ResultDto().setSuccess(true).setCode("2").setMsg("自动分配失败，请重试"));
		}
		String msg = RedisUtils.get(TeaexamConstant.AUTO_ARRANGE_PREFIX + str + TeaexamConstant.AUTO_ARRANGE_POSTFIX_MSG);
		if(StringUtils.isNotEmpty(msg)) {
			return success(msg);
		}
		return success("");
	}
	
	@ControllerInfo("考生设置")
	@RequestMapping("/setIndex/{examId}/siteList")
	public String siteIndex(@PathVariable String examId, ModelMap map, HttpServletRequest request) {
		map.put("examId", examId);
		String subInfoId = StringUtils.trimToNull(request.getParameter("subInfoId"));
		map.put("subInfoId", subInfoId);
		List<TeaexamSiteSetting> rooms = teaexamRegisterInfoService.findRoomByExamIdSubId(examId, subInfoId);
		map.put("rooms", rooms);
		return "/teaexam/siteSet/sitePage.ftl";
	}
	
	@ControllerInfo("考生设置")
	@RequestMapping("/setIndex/{examId}/teaList")
	public String roomTeaList(@PathVariable String examId, ModelMap map, HttpServletRequest request) {
		map.put("examId", examId);
		String subInfoId = StringUtils.trimToNull(request.getParameter("subInfoId"));
		map.put("subInfoId", subInfoId);
		List<TeaexamSiteSetting> rooms = teaexamRegisterInfoService.findRoomByExamIdSubId(examId, subInfoId);
		map.put("rooms", rooms);
		String roomNo = StringUtils.trimToNull(request.getParameter("roomNo"));
		if(StringUtils.isEmpty(roomNo)) {
			roomNo = rooms.get(0).getRoomNo();
		}
		map.put("roomNo", roomNo);
		List<TeaexamRegisterInfo> infos = teaexamRegisterInfoService.findByStatusAndRoomNo(TeaexamConstant.STATUS_PASS + "", roomNo, subInfoId, examId);
		if (CollectionUtils.isNotEmpty(infos)) {
			Set<String> tids = EntityUtils.getSet(infos, TeaexamRegisterInfo::getTeacherId);
			Map<String, Teacher> tm = EntityUtils.getMap(
					Teacher.dt(teacherRemoteService.findListByIds(tids.toArray(new String[0]))), Teacher::getId);
			tids = EntityUtils.getSet(infos, TeaexamRegisterInfo::getSchoolId);
			Map<String, String> um = EntityUtils.getMap(
					Unit.dt(unitRemoteService.findListByIds(tids.toArray(new String[0]))), Unit::getId,
					Unit::getUnitName);
			Iterator<TeaexamRegisterInfo> it = infos.iterator();
			for (;it.hasNext();) {
				TeaexamRegisterInfo info = it.next();
				if(!tm.containsKey(info.getTeacherId())) {
					it.remove();
					continue;
				}
				Teacher t = tm.get(info.getTeacherId());
				info.setIdentityCard(t.getIdentityCard());
				info.setTeacherName(t.getTeacherName());
				info.setSchName(um.get(info.getSchoolId()));
			} 
		}
		map.put("infos", infos);
		return "/teaexam/siteSet/teaList.ftl";
	}
	
	@ControllerInfo("考生设置")
	@RequestMapping("/setIndex/{examId}/changeRmList")
	public String changeRoomPage(@PathVariable String examId, ModelMap map, HttpServletRequest request) {
		map.put("examId", examId);
		String subInfoId = StringUtils.trimToNull(request.getParameter("subInfoId"));
		map.put("subInfoId", subInfoId);
		String rmNo = StringUtils.trimToNull(request.getParameter("oldRoomNo"));
		map.put("oldRoomNo", rmNo);
		TeaexamSubject sub = teaexamSubjectService.findOne(subInfoId);
		// 更换考场的考生信息处理
		String type = StringUtils.trimToNull(request.getParameter("type"));// 类型，1表示合并考场
		String[] teaId = new String[0];
		String teaIds;
		if(BaseConstants.ONE_STR.equals(type)) {
			List<TeaexamRegisterInfo> infos = teaexamRegisterInfoService.findByStatusAndRoomNo(TeaexamConstant.STATUS_PASS+"", rmNo, subInfoId, examId);
			if(CollectionUtils.isEmpty(infos)) {
				return errorFtl(map, "该考场下没有可操作的考生！");
			}
			teaId = EntityUtils.getList(infos, TeaexamRegisterInfo::getId).toArray(new String[0]);
			teaIds = StringUtils.join(teaId, ",").replaceAll(" ", "");
		} else {
			teaIds = StringUtils.trimToNull(request.getParameter("teaIds"));// info.id
			teaId = StringUtils.split(teaIds, ",");
			if(ArrayUtils.isEmpty(teaId)) {
				return errorFtl(map, "没有选择要更换考场的学生！");
			}
		}
		
		List<TeaexamSiteSetting> sets = teaexamSiteSettingService.findChangeSettingBySubInfoId(examId, subInfoId, teaId.length, rmNo);
		map.put("type", type);
		map.put("sub", sub);
		map.put("teaNum", teaId.length);
		map.put("teaIds", teaIds);
		map.put("sets", sets);
		return "/teaexam/siteSet/changeRoom.ftl";
	}
	
	@ControllerInfo("考生设置")
	@RequestMapping("/setIndex/{examId}/saveChangeRm")
	@ResponseBody
	public String saveChangeRm(@PathVariable String examId, TeaexamRegisterInfo info, ModelMap map, HttpServletRequest request) {
		map.put("examId", examId);
		try {
			teaexamRegisterInfoService.saveChangeRm(info);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("保存失败！");
		}
		return success("保存成功！");
	}
	
	@ControllerInfo("考场查询")
	@RequestMapping("/query/page")
	public String queryExamHead(HttpServletRequest request, ModelMap map) {
		int year = NumberUtils.toInt(request.getParameter("year"));
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		if(year == 0) {
			year = nowy;
		}
		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		map.put("year", year);
		return "/teaexam/siteSet/query/queryExamHead.ftl";
	}
	
	@RequestMapping("/query/list")
	public String queryExamList(int year, ModelMap map) {
		List<TeaexamInfo> teaexamInfoListTemp = teaexamInfoService.findByInfoYearType(getLoginInfo().getUnitId(), year, TeaexamConstant.EXAM_INFOTYPE_0);
		List<TeaexamInfo> teaexamInfoList = new ArrayList<TeaexamInfo>();
		Set<String> examIdSet = new HashSet<String>();
		for(TeaexamInfo exam : teaexamInfoListTemp){
			if(exam.getState()==2){
				examIdSet.add(exam.getId());
				teaexamInfoList.add(exam);
			}
		}
		List<TeaexamSubject> subList = new ArrayList<TeaexamSubject>();
		if(CollectionUtils.isNotEmpty(examIdSet)){
			subList = teaexamSubjectService.findByExamIds(examIdSet.toArray(new String[0]));
		}
		for(TeaexamInfo exam : teaexamInfoList){
			String subNames = "";
			for(TeaexamSubject sub : subList){
				if(exam.getId().equals(sub.getExamId())){
					String sectionName = "";
					if(1==sub.getSection()){
						sectionName = "小学";
					}else if(0==sub.getSection()){
						sectionName = "学前";
					}else if(2==sub.getSection()){
						sectionName = "初中";
					}else if(3==sub.getSection()){
						sectionName = "高中";
					}
					subNames = subNames+ sub.getSubjectName()+"("+sectionName+")" + ",";
				}
			}
			if(StringUtils.isNotBlank(subNames)){
				subNames = subNames.substring(0, subNames.length()-1);
			}
			exam.setSubNames(subNames);
		}		
		map.put("teaexamInfoList", teaexamInfoList);
		return "/teaexam/siteSet/query/queryExamList.ftl";
	}

	@RequestMapping("/query/siteQueryTab")
	public String siteQueryTab(String examId, int year, String index, ModelMap map) {
		map.put("examId", examId);
		map.put("index", index);
		map.put("year", year);
		return "/teaexam/siteSet/query/siteQueryTab.ftl";
	}
	
	@RequestMapping("/query/mPasteList")
	public String mPasteList(String examId, String roomNo, String subId, ModelMap map) {
		mCommons(examId, roomNo, subId, "1", map);
		return "/teaexam/siteSet/query/mPasteList.ftl";
	}
	
	public void mCommons(String examId, String roomNo, String subId, String type, ModelMap map){
		List<TeaexamRegisterInfo> regList = teaexamRegisterInfoService.findByStatusAndExamIdIn(2,new String[]{examId});//已审核通过的
		Set<String> subIdSet = new HashSet<String>();
		Set<String> roomNoSet = new HashSet<String>();
		Map<String, String> roomNoLocationIdMap = new HashMap<String, String>(); 
		Set<String> locationIdSet = new HashSet<String>();
		Map<String, String> locationIdSchIdMap = new HashMap<String, String>();
		Set<String> schIdSet = new HashSet<String>();
		for(TeaexamRegisterInfo reg : regList){
			locationIdSet.add(reg.getLocationId());
			schIdSet.add(reg.getLocationId());
		}
		List<TeaexamSite> siteList = new ArrayList<TeaexamSite>();
		if(CollectionUtils.isNotEmpty(locationIdSet)){
			siteList = teaexamSiteService.findListByIds(locationIdSet.toArray(new String[0]));
		}
		for(TeaexamSite site : siteList){
			schIdSet.add(site.getSchoolId());
			locationIdSchIdMap.put(site.getId(), site.getSchoolId());
		}
		List<School> schList = new ArrayList<School>();
		if(CollectionUtils.isNotEmpty(schIdSet)){
			schList = SUtils.dt(schoolRemoteService.findListByIds(schIdSet.toArray(new String[0])), new TR<List<School>>() {});
		}
		Map<String, String> schNameMap = new HashMap<String, String>();
		for(School sch : schList){
			schNameMap.put(sch.getId(), sch.getSchoolName());
		}
		for(TeaexamRegisterInfo reg : regList){
			subIdSet.add(reg.getSubjectInfoId());
			if (StringUtils.isNotEmpty(reg.getRoomNo())) {
				roomNoSet.add(reg.getRoomNo());
				roomNoLocationIdMap.put(reg.getRoomNo(), schNameMap.get(reg.getLocationId()));
			}
		}
		List<String> roomNoList = new ArrayList<String>();
		roomNoList.addAll(roomNoSet);
		Collections.sort(roomNoList, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o2.compareToIgnoreCase(o2);
            }
        });
		if(StringUtils.isBlank(roomNo) && CollectionUtils.isNotEmpty(roomNoList)){
			roomNo = roomNoList.get(0);
		}
		List<TeaexamSubject> subList = new ArrayList<TeaexamSubject>();
		if(CollectionUtils.isNotEmpty(subIdSet)){
			subList = teaexamSubjectService.findListByIds(subIdSet.toArray(new String[0]));			
		}
		TeaexamInfo exam = teaexamInfoService.findOne(examId);		
		List<TeaexamRegisterInfo> regList2 = new ArrayList<TeaexamRegisterInfo>();
		if(StringUtils.isNotBlank(roomNo)){
			regList2 = teaexamRegisterInfoService.findByStatusAndRoomNo("2", roomNo, subId, examId);
		}
		Set<String> teaIdSet = new HashSet<String>();
		Set<String> schIdSet2 = new HashSet<String>();
		for(TeaexamRegisterInfo reg : regList2){
			teaIdSet.add(reg.getTeacherId());
			schIdSet2.add(reg.getSchoolId());
		}
		List<Teacher> teacherList = new ArrayList<Teacher>();
		if(CollectionUtils.isNotEmpty(teaIdSet)){
			teacherList = SUtils.dt(teacherRemoteService.findListByIds(teaIdSet.toArray(new String[0])), new TR<List<Teacher>>() {}); 
		}
        Map<String, String> teacherNameMap = new HashMap<String, String>();
		for(Teacher teacher : teacherList){
			teacherNameMap.put(teacher.getId(), teacher.getTeacherName());
        }
		Map<String, String> subNameMap = new HashMap<String, String>();
        for(TeaexamSubject sub : subList){
        	String subctionName = "";
			if(sub.getSection()==1){
				subctionName = "小学";
			}else if(0==sub.getSection()){
				subctionName = "学前";
			}else if(sub.getSection()==2){
				subctionName = "初中";
			}else if(sub.getSection()==3){
				subctionName = "高中";
			}
			subNameMap.put(sub.getId(), sub.getSubjectName()+"("+subctionName+")");
        }
        List<School> schList2 = new ArrayList<School>();
        if(CollectionUtils.isNotEmpty(schIdSet2)){
        	schList2 = SUtils.dt(schoolRemoteService.findListByIds(schIdSet2.toArray(new String[0])), new TR<List<School>>() {});
		}
        Map<String, String> schNameMap2 = new HashMap<String, String>();
        for(School sch : schList2){
        	schNameMap2.put(sch.getId(), sch.getSchoolName());
        }
        StringBuffer sbName = new StringBuffer();
        for(TeaexamRegisterInfo reg : regList2){
        	reg.setTeacherName(teacherNameMap.get(reg.getTeacherId()));
        	reg.setSubName(subNameMap.get(reg.getSubjectInfoId()));
        	reg.setSchName(schNameMap2.get(reg.getSchoolId()));
        	if(StringUtils.isNotEmpty(reg.getSubName()) && sbName.indexOf(reg.getSubName()) == -1) {
        		if(sbName.length() > 0) {
        			sbName.append("、");
        		}
        		sbName.append(reg.getSubName());
        	}
		}
        if("1".equals(type)){
        	String batchId = "";
            if(CollectionUtils.isNotEmpty(roomNoList)){
            	for(String n : roomNoList){
            		batchId = batchId + n + ",";
            	}
            }
            map.put("batchId", batchId);
        }
        
        map.put("subName", sbName.toString());
		map.put("examId", examId);
		map.put("examName", exam.getExamName());
		map.put("subList", subList);
		map.put("roomNoList", roomNoList);
		map.put("subId", subId);
		map.put("roomNoLocationIdMap", roomNoLocationIdMap);
		map.put("regList2", regList2);
		map.put("roomNo", roomNo);
	}
	
	@RequestMapping("/query/mBatchPrint")
	public String mBatchPrint(String examId, String subId, String batchId, ModelMap map){
		String batchIdLeft = "";
		String doNotPrint = "0";
		if(StringUtils.isNotBlank(batchId)){
			String[] batchIds = batchId.split(",");
			int i = 0;
			for (; i < batchIds.length; i++) {
				if (StringUtils.isNotBlank(batchIds[i])) {
					String roomNo = batchIds[i];
					mCommons(examId, roomNo, subId, "2", map);
					i++;
					break;
				}
			}

			if (i < batchIds.length) {
				StringBuilder sb = new StringBuilder();
				for (int j = i; j < batchIds.length; j++) {
					sb.append(",");
					sb.append(batchIds[j]);
				}
				batchIdLeft = sb.toString();
			}
		} else {
			doNotPrint = "1";
			map.put("examId", examId);
			map.put("examName", "");
			map.put("subList", new ArrayList<>());
			map.put("roomNoList", new ArrayList<>());
			map.put("roomNo", "");
			map.put("subId", subId);
			map.put("roomNoLocationIdMap", new HashMap<String, String>());
			map.put("regList2", new ArrayList<>());
		}
		batchId = batchIdLeft;
		map.put("batchId", batchId);
		map.put("doNotPrint", doNotPrint);
		return "/teaexam/siteSet/query/mPasteListPrint.ftl";
	}
	
	public void zCommons(String examId, String roomNo, String subId, ModelMap map){
		List<TeaexamRegisterInfo> regList = teaexamRegisterInfoService.findByStatusAndExamIdIn(2,new String[]{examId});//已审核通过的
		Set<String> subIdSet = new HashSet<String>();
		Set<String> roomNoSet = new HashSet<String>();
		Map<String, String> roomNoLocationIdMap = new HashMap<String, String>(); 
		Set<String> locationIdSet = new HashSet<String>();
		Map<String, String> locationIdSchIdMap = new HashMap<String, String>();
		Set<String> schIdSet = new HashSet<String>();
		for(TeaexamRegisterInfo reg : regList){
			locationIdSet.add(reg.getLocationId());
			schIdSet.add(reg.getLocationId());
		}
		List<TeaexamSite> siteList = new ArrayList<TeaexamSite>();
		if(CollectionUtils.isNotEmpty(locationIdSet)){
			siteList = teaexamSiteService.findListByIds(locationIdSet.toArray(new String[0]));
		}
		for(TeaexamSite site : siteList){
			schIdSet.add(site.getSchoolId());
			locationIdSchIdMap.put(site.getId(), site.getSchoolId());
		}
		List<School> schList = new ArrayList<School>();
		if(CollectionUtils.isNotEmpty(schIdSet)){
			schList = SUtils.dt(schoolRemoteService.findListByIds(schIdSet.toArray(new String[0])), new TR<List<School>>() {});
		}
		Map<String, String> schNameMap = new HashMap<String, String>();
		for(School sch : schList){
			schNameMap.put(sch.getId(), sch.getSchoolName());
		}
		for(TeaexamRegisterInfo reg : regList){
			subIdSet.add(reg.getSubjectInfoId());
			if (StringUtils.isNotEmpty(reg.getRoomNo())) {
				roomNoSet.add(reg.getRoomNo());
				roomNoLocationIdMap.put(reg.getRoomNo(), schNameMap.get(reg.getLocationId()));
			}
		}
		List<String> roomNoList = new ArrayList<String>();
		roomNoList.addAll(roomNoSet);
		Collections.sort(roomNoList, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o2.compareToIgnoreCase(o2);
            }
        });
		if(StringUtils.isBlank(roomNo) && CollectionUtils.isNotEmpty(roomNoList)){
			roomNo = roomNoList.get(0);
		}
		List<TeaexamSubject> subList = new ArrayList<TeaexamSubject>();
		if(CollectionUtils.isNotEmpty(subIdSet)){
			subList = teaexamSubjectService.findListByIds(subIdSet.toArray(new String[0]));			
		}else{
			roomNo = "";
		}
		TeaexamInfo exam = teaexamInfoService.findOne(examId);	
		List<TeaexamRegisterInfo> regList2 = new ArrayList<TeaexamRegisterInfo>();
		if(StringUtils.isNotBlank(roomNo)){
			regList2 = teaexamRegisterInfoService.findByStatusAndRoomNo("2", roomNo, subId, examId);
		}
		Set<String> teaIdSet = new HashSet<String>();
		Set<String> schIdSet2 = new HashSet<String>();
		for(TeaexamRegisterInfo reg : regList2){
			teaIdSet.add(reg.getTeacherId());
			schIdSet2.add(reg.getSchoolId());
		}
		List<Teacher> teacherList = new ArrayList<Teacher>();
		if(CollectionUtils.isNotEmpty(teaIdSet)){
			teacherList = SUtils.dt(teacherRemoteService.findListByIds(teaIdSet.toArray(new String[0])), new TR<List<Teacher>>() {}); 
		}
        Map<String, String> teacherNameMap = new HashMap<String, String>();
		for(Teacher teacher : teacherList){
			teacherNameMap.put(teacher.getId(), teacher.getTeacherName());
        }
		Map<String, String> subNameMap = new HashMap<String, String>();
        for(TeaexamSubject sub : subList){
        	String subctionName = "";
			if(sub.getSection()==1){
				subctionName = "小学";
			}else if(0==sub.getSection()){
				subctionName = "学前";
			}else if(sub.getSection()==2){
				subctionName = "初中";
			}else if(sub.getSection()==3){
				subctionName = "高中";
			}
			subNameMap.put(sub.getId(), sub.getSubjectName()+"("+subctionName+")");
        }
        List<School> schList2 = new ArrayList<School>();
        if(CollectionUtils.isNotEmpty(schIdSet2)){
        	schList2 = SUtils.dt(schoolRemoteService.findListByIds(schIdSet2.toArray(new String[0])), new TR<List<School>>() {});
		}
        Map<String, String> schNameMap2 = new HashMap<String, String>();
        for(School sch : schList2){
        	schNameMap2.put(sch.getId(), sch.getSchoolName());
        }
        for(TeaexamRegisterInfo reg : regList2){
        	reg.setTeacherName(teacherNameMap.get(reg.getTeacherId()));
        	reg.setSubName(subNameMap.get(reg.getSubjectInfoId()));
        	reg.setSchName(schNameMap2.get(reg.getSchoolId()));
		}
        String batchId = "";
        if(CollectionUtils.isNotEmpty(roomNoList)){
        	for(String n : roomNoList){
        		batchId = batchId + n + ",";
        	}
        }      
        
		map.put("examId", examId);
		map.put("examName", exam.getExamName());
		map.put("subList", subList);
		map.put("roomNoList", roomNoList);
		map.put("roomNo", roomNo);
		map.put("subId", subId);
		map.put("roomNoLocationIdMap", roomNoLocationIdMap);
		map.put("regList2", regList2);
		map.put("batchId", batchId);
	}
	
	@RequestMapping("/query/zBatchPrint")
	public String zBatchPrint(String examId, String subId, String batchId, ModelMap map){
		String batchIdLeft = "";
		String doNotPrint = "0";
		if(StringUtils.isNotBlank(batchId)){
			String[] batchIds = batchId.split(",");
			int i = 0;
			for (; i < batchIds.length; i++) {
				if (StringUtils.isNotBlank(batchIds[i])) {
					String roomNo = batchIds[i];
					zCommons(examId, roomNo, subId, map);
					i++;
					break;
				}
			}

			if (i < batchIds.length) {
				StringBuilder sb = new StringBuilder();
				for (int j = i; j < batchIds.length; j++) {
					sb.append(",");
					sb.append(batchIds[j]);
				}
				batchIdLeft = sb.toString();
			}
		} else {
			doNotPrint = "1";
			map.put("examId", examId);
			map.put("examName", "");
			map.put("subList", new ArrayList<>());
			map.put("roomNoList", new ArrayList<>());
			map.put("roomNo", "");
			map.put("subId", subId);
			map.put("roomNoLocationIdMap", new HashMap<String, String>());
			map.put("regList2", new ArrayList<>());
		}
		batchId = batchIdLeft;
		map.put("batchId", batchId);
		map.put("doNotPrint", doNotPrint);
		return "/teaexam/siteSet/query/zPasteListPrint.ftl";
	}
	
	@RequestMapping("/query/zPasteList")
	public String zPasteList(String examId, String roomNo, String subId, ModelMap map) {
		zCommons(examId, roomNo, subId, map);
		return "/teaexam/siteSet/query/zPasteList.ftl";
	}
	
	@RequestMapping("/query/examCardList")
	public String examCardList(String type, String examId, String subId, String schId, String status, String teacherName, String identityCard, ModelMap map,HttpServletRequest request) {
		if(StringUtils.isNotBlank(examId)){		
			try {
				TeaexamInfo exam = teaexamInfoService.findOne(examId);
				String schoolIds = exam.getSchoolIds();
				if(StringUtils.isNotBlank(schoolIds)){			
						String[] schoolIdArr = schoolIds.split(",");
						List<Unit> unitList2 = new ArrayList<Unit>();
						if(schoolIdArr.length>0){
							unitList2 = SUtils.dt(unitRemoteService.findListByIds(schoolIdArr), new TR<List<Unit>>(){});
						}
						List<TeaexamSubject> subList2 = teaexamSubjectService.findByExamIds(new String[]{examId});
						Pagination page = createPagination();
						List<Teacher> teacherList2 = new ArrayList<Teacher>();
						Set<String> teaIdSet2 = new HashSet<String>();
						String state = "";
						if(StringUtils.isNotBlank(identityCard)){
							teacherList2 = SUtils.dt(teacherRemoteService.findByIdentityCardNo(new String[]{identityCard}), new TR<List<Teacher>>(){});
							for(Teacher t : teacherList2){
								teaIdSet2.add(t.getId());
							}
							state = "1";
							map.put("searchCon", identityCard);
						}
						if(StringUtils.isNotBlank(teacherName)){
							teacherList2 = SUtils.dt(teacherRemoteService.findByTeacherNameLike(teacherName), new TR<List<Teacher>>(){});
							for(Teacher t : teacherList2){
								teaIdSet2.add(t.getId());
							}
							state = "2";
							map.put("searchCon", teacherName);
						}
						
						List<TeaexamRegisterInfo> registerInfoList = teaexamRegisterInfoService.findBy(examId, "", schId, TeaexamConstant.STATUS_PASS + "", state, teaIdSet2.toArray(new String[0]), page);
						Set<String> teaIdSet = new HashSet<String>();
						Set<String> unitIdSet = new HashSet<String>();
						Set<String> subIdSet = new HashSet<String>();
						Set<String> opIdSet = new HashSet<String>();
						for(TeaexamRegisterInfo reg : registerInfoList){
							teaIdSet.add(reg.getTeacherId());
							unitIdSet.add(reg.getSchoolId());
							subIdSet.add(reg.getSubjectInfoId());
							opIdSet.add(reg.getOperatorId());
							if(StringUtils.isEmpty(reg.getCardNo())) {
								hasNoCard = true;
							} else {
								hasCard = true;
							}
						}
						
						Map<String, String> subNameMap = new HashMap<String, String>();
						List<TeaexamSubject> subList = new ArrayList<TeaexamSubject>();
						if(CollectionUtils.isNotEmpty(subIdSet)){
							subList = teaexamSubjectService.findListByIds(subIdSet.toArray(new String[0]));
							for(TeaexamSubject sub : subList){
								String subctionName = "";
								if(sub.getSection()==1){
									subctionName = "小学";
								}else if(0==sub.getSection()){
									subctionName = "学前";
								}else if(sub.getSection()==2){
									subctionName = "初中";
								}else if(sub.getSection()==3){
									subctionName = "高中";
								}
								subNameMap.put(sub.getId(), sub.getSubjectName()+"("+subctionName+")");
							}
						}
						List<Unit> unitList = new ArrayList<Unit>();
						if(CollectionUtils.isNotEmpty(unitIdSet)){
							unitList = SUtils.dt(unitRemoteService.findListByIds(unitIdSet.toArray(new String[0])), new TR<List<Unit>>(){});
						}
						Map<String, String> teacherUnitNameMap = new HashMap<String, String>();
						for(Unit unit : unitList){
							teacherUnitNameMap.put(unit.getId(), unit.getUnitName());
						}
						Map<String, McodeDetail> sexMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-XB"),new TypeReference<Map<String,McodeDetail>>(){});
						Map<String, McodeDetail> mzMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-MZ"),new TypeReference<Map<String,McodeDetail>>(){});
						List<TeaexamRegisterInfo> regList2 = new ArrayList<TeaexamRegisterInfo>();
						List<TeaexamRegisterInfo> regNewList = new ArrayList<TeaexamRegisterInfo>();
						if(CollectionUtils.isNotEmpty(teaIdSet)){	
//							System.out.println(teaIdSet.size());
//							List<String> tids = new ArrayList<>(teaIdSet);
//							tids = tids.subList(0, 1256);//TODO
							List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teaIdSet.toArray(new String[0])), new TR<List<Teacher>>(){}); 
							Map<String, String> teacherNameMap = new HashMap<String, String>();
							Map<String, Integer> teacherSexMap = new HashMap<String, Integer>();
							Map<String, String> teacherNationMap = new HashMap<String, String>();
							Map<String, String> teacherIdCardMap = new HashMap<String, String>();
							for(Teacher teacher : teacherList){
								teacherNameMap.put(teacher.getId(), teacher.getTeacherName());
								teacherSexMap.put(teacher.getId(), teacher.getSex());
								teacherNationMap.put(teacher.getId(), teacher.getNation());
								teacherIdCardMap.put(teacher.getId(), teacher.getIdentityCard());
							}
							Set<String> teaIdSet3 = new HashSet<String>();
							for(TeaexamRegisterInfo reg : registerInfoList){
								reg.setTeacherName(teacherNameMap.get(reg.getTeacherId()));
								Integer sex = teacherSexMap.get(reg.getTeacherId());
								if (sex != null && sexMap.containsKey(sex.toString())) {
									reg.setSex(sexMap.get(String.valueOf(sex)).getMcodeContent());
								}
								String na = teacherNationMap.get(reg.getTeacherId());
								if(StringUtils.isNotEmpty(na) && mzMap.containsKey(na)){
									reg.setNation(mzMap.get(na).getMcodeContent());
								}else{
									reg.setNation("");
								}
								reg.setIdentityCard(teacherIdCardMap.get(reg.getTeacherId()));
								reg.setUnitName(teacherUnitNameMap.get(reg.getSchoolId()));
				                if(!teaIdSet3.contains(reg.getTeacherId())){
				                	regList2.add(reg);
				                }
				                teaIdSet3.add(reg.getTeacherId());
							}
							Map<String, Set<String>> subIdMap = new HashMap<String, Set<String>>();
							for(String teacherId : teaIdSet3){
								Set<String> subIdSet2 = new HashSet<String>();
								for(TeaexamRegisterInfo reg : registerInfoList){
									if(teacherId.equals(reg.getTeacherId())){
										subIdSet2.add(reg.getSubjectInfoId());
									}
								}
								subIdMap.put(teacherId, subIdSet2);
							}
							for(TeaexamRegisterInfo reg : regList2){
								Set<String> subIdSet3 = subIdMap.get(reg.getTeacherId());
								String subName = "";
								for(String subId2 : subIdSet3){
									subName = subName + subNameMap.get(subId2)+",";
								}
								if(StringUtils.isNotBlank(subName)){
									subName = subName.substring(0, subName.length()-1);
								}
								reg.setSubName(subName);
							}
						
				            //假分页
//				            page.setMaxRowCount(regList2.size());
//				            Integer pageSize = page.getPageSize();
//				            Integer pageIndex = page.getPageIndex();
//				            for (int i = pageSize * (pageIndex - 1); i < regList2.size(); i++) {
//				                if (i < pageSize * pageIndex && i >= pageSize * (pageIndex - 1)) {
//				                	regNewList.add(regList2.get(i));
//				                } else {
//				                    break;
//				                }
//				            }
							regNewList.addAll(regList2);
							
						}
						
						map.put("subList", subList2);
						map.put("subId", subId);
						map.put("unitList", unitList2);
						map.put("schId", schId);
						map.put("status", status);
						map.put("registerInfoList", regNewList);
						map.put("Pagination", page);
						map.put("examId", examId);
						if(StringUtils.isBlank(type)){
							type = "1";
						}
						map.put("type", type);
						sendPagination(request, map, page);
				}
			} catch (Exception e) {
				e.printStackTrace();
//				return errorFtl(map, "报错："+e.getMessage());
			}
		}
		return "/teaexam/siteSet/query/examCardList.ftl";
	}
	
	@RequestMapping("/query/examCardExport")
	public void examCardExport(String examId, String subId, String schId, String status, String teacherName, String identityCard, HttpServletResponse response){
		if(StringUtils.isNotBlank(examId)){		
			TeaexamInfo exam = teaexamInfoService.findOne(examId);
			String schoolIds = exam.getSchoolIds();
			if(StringUtils.isNotBlank(schoolIds)){			
				List<Teacher> teacherList2 = new ArrayList<Teacher>();
				Set<String> teaIdSet2 = new HashSet<String>();
				String state = "";
				if(StringUtils.isNotBlank(identityCard)){
					teacherList2 = SUtils.dt(teacherRemoteService.findByIdentityCardNo(new String[]{identityCard}), new TR<List<Teacher>>(){});
					for(Teacher t : teacherList2){
						teaIdSet2.add(t.getId());
					}
					state = "1";
				}
				if(StringUtils.isNotBlank(teacherName)){
					teacherList2 = SUtils.dt(teacherRemoteService.findByTeacherNameLike(teacherName), new TR<List<Teacher>>(){});
					for(Teacher t : teacherList2){
						teaIdSet2.add(t.getId());
					}
					state = "2";
				}
				
				List<TeaexamRegisterInfo> registerInfoList = teaexamRegisterInfoService.findBy(examId, "", schId, "2", state, teaIdSet2.toArray(new String[0]), null);
				
				
				Set<String> teaIdSet = new HashSet<String>();
				Set<String> unitIdSet = new HashSet<String>();
				Set<String> subIdSet = new HashSet<String>();
				Set<String> opIdSet = new HashSet<String>();
				for(TeaexamRegisterInfo reg : registerInfoList){
					teaIdSet.add(reg.getTeacherId());
					unitIdSet.add(reg.getSchoolId());
					subIdSet.add(reg.getSubjectInfoId());
					opIdSet.add(reg.getOperatorId());
				}
				
				Map<String, String> subNameMap = new HashMap<String, String>();
				List<TeaexamSubject> subList = new ArrayList<TeaexamSubject>();
				if(CollectionUtils.isNotEmpty(subIdSet)){
					subList = teaexamSubjectService.findListByIds(subIdSet.toArray(new String[0]));
					for(TeaexamSubject sub : subList){
						String subctionName = "";
						if(sub.getSection()==1){
							subctionName = "小学";
						}else if(0==sub.getSection()){
							subctionName = "学前";
						}else if(sub.getSection()==2){
							subctionName = "初中";
						}else if(sub.getSection()==3){
							subctionName = "高中";
						}
						subNameMap.put(sub.getId(), sub.getSubjectName()+"("+subctionName+")");
					}
				}
				List<Unit> unitList = new ArrayList<Unit>();
				if(CollectionUtils.isNotEmpty(unitIdSet)){
					unitList = SUtils.dt(unitRemoteService.findListByIds(unitIdSet.toArray(new String[0])), new TR<List<Unit>>(){});
				}
				Map<String, String> teacherUnitNameMap = new HashMap<String, String>();
				for(Unit unit : unitList){
					teacherUnitNameMap.put(unit.getId(), unit.getUnitName());
				}
				Map<String, McodeDetail> sexMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-XB"),new TypeReference<Map<String,McodeDetail>>(){});
				Map<String, McodeDetail> mzMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId("DM-MZ"),new TypeReference<Map<String,McodeDetail>>(){});
				List<TeaexamRegisterInfo> regList2 = new ArrayList<TeaexamRegisterInfo>();
				if(CollectionUtils.isNotEmpty(teaIdSet)){				
					List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teaIdSet.toArray(new String[0])), new TR<List<Teacher>>(){}); 
					Map<String, String> teacherNameMap = new HashMap<String, String>();
					Map<String, Integer> teacherSexMap = new HashMap<String, Integer>();
					Map<String, String> teacherNationMap = new HashMap<String, String>();
					Map<String, String> teacherIdCardMap = new HashMap<String, String>();
					for(Teacher teacher : teacherList){
						teacherNameMap.put(teacher.getId(), teacher.getTeacherName());
						teacherSexMap.put(teacher.getId(), teacher.getSex());
						teacherNationMap.put(teacher.getId(), teacher.getNation());
						teacherIdCardMap.put(teacher.getId(), teacher.getIdentityCard());
					}
					Set<String> teaIdSet3 = new HashSet<String>();
					for(TeaexamRegisterInfo reg : registerInfoList){
						reg.setTeacherName(teacherNameMap.get(reg.getTeacherId()));
						Integer sex = teacherSexMap.get(reg.getTeacherId());
						if (sex != null && sexMap.containsKey(String.valueOf(sex))) {
							reg.setSex(sexMap.get(String.valueOf(sex)).getMcodeContent());
						}
						String nation = teacherNationMap.get(reg.getTeacherId());
						if(StringUtils.isNotEmpty(nation) && mzMap.containsKey(nation)){							
							reg.setNation(mzMap.get(nation).getMcodeContent());
						}else{
							reg.setNation("");
						}
						reg.setIdentityCard(teacherIdCardMap.get(reg.getTeacherId()));
						reg.setUnitName(teacherUnitNameMap.get(reg.getSchoolId()));
                        if(!teaIdSet3.contains(reg.getTeacherId())){
                        	regList2.add(reg);
                        }
                        teaIdSet3.add(reg.getTeacherId());
					}
					Map<String, Set<String>> subIdMap = new HashMap<String, Set<String>>();
					for(String teacherId : teaIdSet3){
						Set<String> subIdSet2 = new HashSet<String>();
						for(TeaexamRegisterInfo reg : registerInfoList){
							if(teacherId.equals(reg.getTeacherId())){
								subIdSet2.add(reg.getSubjectInfoId());
							}
						}
						subIdMap.put(teacherId, subIdSet2);
					}
					for(TeaexamRegisterInfo reg : regList2){
						Set<String> subIdSet3 = subIdMap.get(reg.getTeacherId());
						String subName = "";
						for(String subId2 : subIdSet3){
							subName = subName + subNameMap.get(subId2)+",";
						}
						if(StringUtils.isNotBlank(subName)){
							subName = subName.substring(0, subName.length()-1);
						}
						reg.setSubName(subName);
					}
				}
				Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
				List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
				int i = 1;
				for(TeaexamRegisterInfo item : regList2){
					Map<String,String> sMap = new HashMap<String,String>();
					sMap.put("序号", String.valueOf(i));
					sMap.put("教师姓名", item.getTeacherName());
					sMap.put("性别", item.getSex());
					sMap.put("民族", item.getNation());
					sMap.put("考号", item.getCardNo());
					sMap.put("身份证号", item.getIdentityCard());		
					sMap.put("单位", item.getUnitName());
					sMap.put("报名科目", item.getSubName());	
					i++;
					recordList.add(sMap);
				}
				sheetName2RecordListMap.put(getObjectName(),recordList);
				Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
				List<String> tis = getRowTitleList();
				titleMap.put(getObjectName(), tis);
				ExportUtils ex = ExportUtils.newInstance();
				ex.exportXLSFile("考生名单", titleMap, sheetName2RecordListMap, response);	
			}
		}
	}
	
	public String getObjectName() {
		return "考生名单";
	}
	
	public List<String> getRowTitleList() {
		List<String> tis = new ArrayList<String>();
		tis.add("序号");
		tis.add("教师姓名");
		tis.add("性别");
		tis.add("民族");
		tis.add("考号");
		tis.add("身份证号");
		tis.add("单位");
		tis.add("报名科目");
		return tis;
	}
	
	@RequestMapping("/query/examCardEdit")
	public String examCardEdit(String examId, String teacherId, ModelMap map,HttpServletRequest request){
		String userId = "";
		if(StringUtils.isBlank(teacherId)){
			teacherId = getLoginInfo().getOwnerId();
			userId = getLoginInfo().getUserId();
		}
		List<TeaexamRegisterInfo> regListTemp = teaexamRegisterInfoService.findByTeacherIdAndExamIdIn(teacherId, new String[]{examId});
		List<TeaexamRegisterInfo> regList = new ArrayList<TeaexamRegisterInfo>();
		for(TeaexamRegisterInfo reg : regListTemp){
			if(reg.getStatus()==2){
				regList.add(reg);
			}
		}
		TeaexamInfo exam = teaexamInfoService.findOne(examId);
		Teacher teacher = SUtils.dc(teacherRemoteService.findOneById(teacherId), Teacher.class); 
		Unit unit = SUtils.dc(unitRemoteService.findOneById(teacher.getUnitId()), Unit.class);
		Set<String> subIdSet = new HashSet<String>();
		Set<String> setIdSet = new HashSet<String>();
		Set<String> schIdSet = new HashSet<String>();
		for(TeaexamRegisterInfo reg : regList){
			subIdSet.add(reg.getSubjectInfoId());
			setIdSet.add(reg.getLocationId());
			schIdSet.add(reg.getLocationId());
		}
		List<Unit> unitList = new ArrayList<Unit>();
		if(CollectionUtils.isNotEmpty(schIdSet)){
			unitList = SUtils.dt(unitRemoteService.findListByIds(schIdSet.toArray(new String[0])), new TR<List<Unit>>(){});
		}
		Map<String, String> unitMap = new HashMap<String, String>();
		for(Unit u : unitList){
			unitMap.put(u.getId(), u.getUnitName());
		}
		List<TeaexamSubject> subList = new ArrayList<TeaexamSubject>();
		Map<String, String> subMap = new HashMap<String, String>();
		Map<String, Date> startMap = new HashMap<String, Date>();
		Map<String, Date> endMap = new HashMap<String, Date>();
		if(CollectionUtils.isNotEmpty(subIdSet)){
			subList = teaexamSubjectService.findListByIds(subIdSet.toArray(new String[0]));	
			for(TeaexamSubject sub : subList){
				String sectionName = "";
				if(sub.getSection()==1){
					sectionName = "（小学）";
				}else if(0==sub.getSection()){
					sectionName = "（学前）";
				}else if(sub.getSection()==2){
					sectionName = "（初中）";
				}else if(sub.getSection()==3){
					sectionName = "（高中）";
				}
				subMap.put(sub.getId(), sub.getSubjectName()+sectionName);
				startMap.put(sub.getId(), sub.getStartTime());
				endMap.put(sub.getId(), sub.getEndTime());
			}
		}
		String cardNo = "";
		if(CollectionUtils.isNotEmpty(regList)){
			cardNo = regList.get(0).getCardNo();
			userId = regList.get(0).getTeaUserId();
		}
		for(TeaexamRegisterInfo reg : regList){
			reg.setStartTime(startMap.get(reg.getSubjectInfoId()));
			reg.setEndTime(endMap.get(reg.getSubjectInfoId()));
			reg.setUnitName(unitMap.get(reg.getLocationId()));
			reg.setSubName(subMap.get(reg.getSubjectInfoId()));
		}
		User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);	
		String avatarUrl = "";
		if(null!=user){			
			avatarUrl = PJHeadUrlUtils.getShowAvatarUrl(request.getContextPath(), user.getAvatarUrl(), getFileURL());
		}
		map.put("examName", exam.getExamName());
		map.put("cardNo", cardNo);
		map.put("teacherName", teacher.getTeacherName());
		map.put("unitName", unit.getUnitName());
		map.put("idCard", teacher.getIdentityCard());
		map.put("avatarUrl", avatarUrl);
		map.put("subList", subList);
		map.put("regList", regList);
		return "/teaexam/siteSet/query/examCardEdit.ftl";
	}
}
