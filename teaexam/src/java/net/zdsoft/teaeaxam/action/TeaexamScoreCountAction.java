package net.zdsoft.teaeaxam.action;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.teaeaxam.constant.TeaexamConstant;
import net.zdsoft.teaeaxam.dto.TeaexamCountDto;
import net.zdsoft.teaeaxam.entity.ExamCenterSchool;
import net.zdsoft.teaeaxam.entity.TeaexamInfo;
import net.zdsoft.teaeaxam.entity.TeaexamRegisterInfo;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;
import net.zdsoft.teaeaxam.entity.TeaexamSubjectLine;
import net.zdsoft.teaeaxam.service.ExamCenterSchoolService;
import net.zdsoft.teaeaxam.service.TeaexamInfoService;
import net.zdsoft.teaeaxam.service.TeaexamRegisterInfoService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectLineService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectService;

@Controller
@RequestMapping("/teaexam/scoreCount")
public class TeaexamScoreCountAction extends BaseAction{
	@Autowired
	private TeaexamInfoService teaexamInfoService;
	@Autowired
	private TeaexamSubjectService teaexamSubjectService;
	@Autowired
	private TeaexamRegisterInfoService teaexamRegisterInfoService;
	@Autowired
	private TeaexamSubjectLineService teaexamSubjectLineService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private ExamCenterSchoolService examCenterSchoolService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	
	@RequestMapping("/index/page")
	public String indexPage(HttpServletRequest req, ModelMap map){
		LoginInfo info = getLoginInfo();
		String unitId = info.getUnitId();
		int year = NumberUtils.toInt(req.getParameter("year")); 
		int type = NumberUtils.toInt(req.getParameter("type"));;
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		if(year == 0) {
			year = nowy;
		}
		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		map.put("year", year);
		map.put("type", type);
		List<TeaexamInfo> teaexamInfoList = teaexamInfoService.findByInfoYearType(unitId, year, type);
		Set<String> examIdSet = new HashSet<String>();
		for(TeaexamInfo exam : teaexamInfoList){
			examIdSet.add(exam.getId());
		}
		if(CollectionUtils.isNotEmpty(examIdSet)){			
			List<TeaexamRegisterInfo> regList = teaexamRegisterInfoService.findByExamIdIn(examIdSet.toArray(new String[0]));
			Set<String> examIdSet2 = new HashSet<String>();
			for(TeaexamRegisterInfo reg : regList){
				if(StringUtils.isNotBlank(reg.getGradeCode())){
					examIdSet2.add(reg.getExamId());
				}
			}
			List<TeaexamSubject> subList = new ArrayList<TeaexamSubject>();
			if(CollectionUtils.isNotEmpty(examIdSet)){
				subList = teaexamSubjectService.findByExamIds(examIdSet.toArray(new String[0]));
			}
			Date date = new Date();
			for(TeaexamInfo exam : teaexamInfoList){
				String subNames = "";
				for(TeaexamSubject sub : subList){
					if(exam.getId().equals(sub.getExamId())){
						String sectionName = "";
						if(1==sub.getSection()){
							sectionName = "小学";
						} else if (0 == sub.getSection()) {
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
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        		String startTimeStr = sdf.format(exam.getExamStart());
        		String endTimeStr = sdf.format(exam.getExamEnd());
        		String nowStr = sdf.format(date);
        		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        		try {
        			Date endTime = sdf2.parse(endTimeStr);
					Date startTime = sdf2.parse(startTimeStr);
					Date nowTime = sdf2.parse(nowStr);
					if (startTime.getTime() > nowTime.getTime()) {
						exam.setStatus(TeaexamConstant.EXAM_COUNT_STATUS_1);
					} else if (startTime.getTime() <= nowTime.getTime()
							&& endTime.getTime() >= nowTime.getTime()) {
						exam.setStatus(TeaexamConstant.EXAM_COUNT_STATUS_2);
					} else if (endTime.getTime() < nowTime.getTime()) {
						if (examIdSet2.contains(exam.getId())) {
							exam.setStatus(TeaexamConstant.EXAM_COUNT_STATUS_4);
						} else {
							exam.setStatus(TeaexamConstant.EXAM_COUNT_STATUS_3);
						}
					}
        		} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			map.put("teaexamInfoList", teaexamInfoList);
		}
		return "/teaexam/scoreCount/scoreCountMain.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/countScore")
	public String countScore(String examId){
		try{
			long s = System.currentTimeMillis();
			List<TeaexamSubject> subList = teaexamSubjectService.findByExamIds(new String[]{examId});
			Set<String> subIdSet = new HashSet<String>();
			for(TeaexamSubject sub : subList){
				subIdSet.add(sub.getId());
			}
			if(CollectionUtils.isNotEmpty(subIdSet)){
				List<TeaexamSubjectLine> lineList = teaexamSubjectLineService.findBySubjectIds(subIdSet.toArray(new String[0]));
				List<TeaexamRegisterInfo> regList = teaexamRegisterInfoService.findByExamIdIn(new String[]{examId});
				Map<String, List<TeaexamSubjectLine>> lineMap = EntityUtils.getListMap(lineList, TeaexamSubjectLine::getSubjectInfoId, Function.identity());
				int t = 0;
				Iterator<TeaexamRegisterInfo> it = regList.iterator();
				while(it.hasNext()){
					TeaexamRegisterInfo reg = it.next();
					if(reg.getStatus() != TeaexamConstant.STATUS_PASS 
							|| StringUtils.isEmpty(reg.getSeatNo())
							|| !lineMap.containsKey(reg.getSubjectInfoId())) {
						it.remove();
						continue;
					}
					Float yxMinSore = null;
					Float hgMinSore = null;
					List<TeaexamSubjectLine> lineList2 = lineMap.get(reg.getSubjectInfoId());
					for(TeaexamSubjectLine line : lineList2){
						if(reg.getSubjectInfoId().equals(line.getSubjectInfoId()) && TeaexamConstant.SCORE_GRADE_YX.equals(line.getGradeCode())){
							yxMinSore = line.getMinScore();
						}
						if(reg.getSubjectInfoId().equals(line.getSubjectInfoId()) && TeaexamConstant.SCORE_GRADE_HG.equals(line.getGradeCode())){
							hgMinSore = line.getMinScore();
						}
					}
					if(null!=reg.getScore()) {
						if(null!=yxMinSore && null!=hgMinSore){
							if(reg.getScore() >= yxMinSore){
								reg.setGradeCode(TeaexamConstant.SCORE_GRADE_YX);
							}else if(reg.getScore() >= hgMinSore){
								reg.setGradeCode(TeaexamConstant.SCORE_GRADE_HG);
							}else{
								reg.setGradeCode(TeaexamConstant.SCORE_GRADE_BHG);
							}
							t++;
						}
					} else {
						reg.setGradeCode(TeaexamConstant.SCORE_GRADE_BHG);
					}
				}
				if(t==0){
					return error("未维护分数线或成绩，无法统计！");
				}
				if(CollectionUtils.isNotEmpty(regList)){
					teaexamRegisterInfoService.saveAll(regList.toArray(new TeaexamRegisterInfo[0]));
				}
			}else{
				return error("没有考试科目！");
			}
			long e = System.currentTimeMillis();
			System.out.println("统计耗时=="+(e-s)+"ms");
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
	
	@RequestMapping("/showDetailTab")
	public String showDetailTab(String examId, int year, int type, ModelMap map){
		map.put("examId", examId);
		map.put("type", type);
		map.put("year", year);
		return "/teaexam/scoreCount/showDetailTab.ftl";
	}
	
	@ControllerInfo("成绩概况")
	@RequestMapping("/showScoreGeneral")
	public String showScoreGeneral(String examId, ModelMap map){
		TeaexamInfo exam = teaexamInfoService.findOne(examId);
		List<TeaexamSubject> subList = teaexamSubjectService.findByExamIds(new String[]{examId});
		if(CollectionUtils.isNotEmpty(subList)){
			List<TeaexamRegisterInfo> regList = teaexamRegisterInfoService.findByExamIdIn(new String[]{examId});
			for(TeaexamSubject sub : subList){
				int yxNum = 0;
				int hgNum = 0;
				int bhgNum = 0;
				int examNum = 0;
				for(TeaexamRegisterInfo reg : regList){
					if(sub.getId().equals(reg.getSubjectInfoId()) 
							&& reg.getStatus()==2 && StringUtils.isNotEmpty(reg.getSeatNo()) 
							&& StringUtils.isNotBlank(reg.getGradeCode())
							&& reg.getScore() != null && reg.getScore() > 0){
						if(StringUtils.isNotBlank(reg.getGradeCode()) && TeaexamConstant.SCORE_GRADE_YX.equals(reg.getGradeCode())){
							yxNum++;
						}else if(StringUtils.isNotBlank(reg.getGradeCode()) && TeaexamConstant.SCORE_GRADE_HG.equals(reg.getGradeCode())){
							hgNum++;
						}else if(StringUtils.isNotBlank(reg.getGradeCode()) && TeaexamConstant.SCORE_GRADE_BHG.equals(reg.getGradeCode())){
							bhgNum++;
						}
						examNum++;
					}
				}
				sub.setExamNum(examNum);
				sub.setYxCount(yxNum);
				sub.setHgCount(hgNum+yxNum);
				sub.setBhgCount(bhgNum);
				if(sub.getSection()==1){
					sub.setSubjectName(sub.getSubjectName()+"（小学）");
				} else if (0 == sub.getSection()) {
					sub.setSubjectName(sub.getSubjectName()+"（学前）");
				}else if(sub.getSection()==2){
					sub.setSubjectName(sub.getSubjectName()+"（初中）");
				}else if(sub.getSection()==3){
					sub.setSubjectName(sub.getSubjectName()+"（高中）");
				}
			}
		}
		map.put("examName", exam.getExamName());
		map.put("subList", subList);
		map.put("examId", examId);
		return "/teaexam/scoreCount/showScoreGeneral.ftl";
	}
	
	@RequestMapping("/exportGeneral")
	public void exportGeneral(String examId, HttpServletResponse response){
		TeaexamInfo exam = teaexamInfoService.findOne(examId);
		List<TeaexamSubject> subList = teaexamSubjectService.findByExamIds(new String[]{examId});
		if(CollectionUtils.isNotEmpty(subList)){
			List<TeaexamRegisterInfo> regList = teaexamRegisterInfoService.findByExamIdIn(new String[]{examId});
			for(TeaexamSubject sub : subList){
				int yxNum = 0;
				int hgNum = 0;
				int bhgNum = 0;
				int examNum = 0;
				for(TeaexamRegisterInfo reg : regList){
					if(sub.getId().equals(reg.getSubjectInfoId()) 
							&& reg.getStatus()==2 && StringUtils.isNotEmpty(reg.getSeatNo()) 
							&& StringUtils.isNotBlank(reg.getGradeCode())
							&& reg.getScore() != null && reg.getScore() > 0){
						if(StringUtils.isNotBlank(reg.getGradeCode()) && TeaexamConstant.SCORE_GRADE_YX.equals(reg.getGradeCode())){
							yxNum++;
						}else if(StringUtils.isNotBlank(reg.getGradeCode()) && TeaexamConstant.SCORE_GRADE_HG.equals(reg.getGradeCode())){
							hgNum++;
						}else if(StringUtils.isNotBlank(reg.getGradeCode()) && TeaexamConstant.SCORE_GRADE_BHG.equals(reg.getGradeCode())){
							bhgNum++;
						}
						examNum++;
					}
				}
				sub.setExamNum(examNum);
				sub.setYxCount(yxNum);
				sub.setHgCount(hgNum+yxNum);
				sub.setBhgCount(bhgNum);
				if(sub.getSection()==1){
					sub.setSubjectName(sub.getSubjectName()+"（小学）");
				} else if (0 == sub.getSection()) {
					sub.setSubjectName(sub.getSubjectName()+"（学前）");
				}else if(sub.getSection()==2){
					sub.setSubjectName(sub.getSubjectName()+"（初中）");
				}else if(sub.getSection()==3){
					sub.setSubjectName(sub.getSubjectName()+"（高中）");
				}
			}
		}		
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		int i=1;
		for(TeaexamSubject item : subList){
			Map<String,String> sMap = new HashMap<String,String>();
			sMap.put("序号", String.valueOf(i));
			sMap.put("学科", item.getSubjectName());
			sMap.put("参与考试人数", String.valueOf(item.getExamNum()));
			sMap.put("优秀人数", String.valueOf(item.getYxCount()));
			sMap.put("合格人数", String.valueOf(item.getHgCount()));
			sMap.put("不合格人数", String.valueOf(item.getBhgCount()));	
			i++;
			recordList.add(sMap);
		}
		sheetName2RecordListMap.put(getObjectName1(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList1();
		titleMap.put(getObjectName1(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile(exam.getExamName()+"成绩概况", titleMap, sheetName2RecordListMap, response);	
	}
	
	public String getObjectName1() {
		return "成绩概况";
	}
	
	public List<String> getRowTitleList1() {
		List<String> tis = new ArrayList<String>();
		tis.add("序号");
		tis.add("学科");
		tis.add("参与考试人数");
		tis.add("优秀人数");
		tis.add("合格人数");
		tis.add("不合格人数");
		return tis;
	}
	
	@ControllerInfo("成绩详情")
	@RequestMapping("/showScoreDetail")
	public String showScoreDetail(String examId, String subjectId, String schoolId, ModelMap map){
		TeaexamInfo exam = teaexamInfoService.findOne(examId);
		List<TeaexamSubject> subList = teaexamSubjectService.findByExamIds(new String[]{examId});;
		List<TeaexamSubject> subDataList = new ArrayList<>();
		
		for(TeaexamSubject sub : subList){
			if(sub.getSection()==1){
				sub.setSubjectName(sub.getSubjectName()+"（小学）");
			} else if (0 == sub.getSection()) {
				sub.setSubjectName(sub.getSubjectName()+"（学前）");
			}else if(sub.getSection()==2){
				sub.setSubjectName(sub.getSubjectName()+"（初中）");
			}else if(sub.getSection()==3){
				sub.setSubjectName(sub.getSubjectName()+"（高中）");
			}
			if(StringUtils.isNotEmpty(subjectId)) {
				if(sub.getId().equals(subjectId)) {
					subDataList.add(sub);
				}
			} else {
				subDataList.add(sub);
			}
		}
		List<TeaexamRegisterInfo> regListTemp = teaexamRegisterInfoService.findBy(examId, subjectId, null, TeaexamConstant.STATUS_PASS+"", null, null, null);
//				findByExamIdIn(new String[]{examId});
		List<TeaexamRegisterInfo> regList = new ArrayList<TeaexamRegisterInfo>();
		for(TeaexamRegisterInfo reg : regListTemp){
			if(reg.getStatus()==2 && StringUtils.isNotEmpty(reg.getSeatNo()) 
					&& StringUtils.isNotBlank(reg.getGradeCode())
					&& reg.getScore() != null && reg.getScore() > 0){
				regList.add(reg);
			}
		}
		// 计算各合计人数和总分
		Map<String, Integer> regMap = new HashMap<String, Integer>();
		Map<String, Integer> regTotalMap = new HashMap<String, Integer>();
		Map<String, Float> regTotalScoreMap = new HashMap<String, Float>();
		if(CollectionUtils.isNotEmpty(regList)){ 
			String defaultSchId = BaseConstants.ZERO_GUID;
			for(TeaexamRegisterInfo reg : regList){
				if(reg.getScore() == null) {
					reg.setScore(0f);
				}
				String[] schIds = new String[] {reg.getSchoolId(), defaultSchId};
				for(String schId : schIds) {
					if(regMap.containsKey(schId+reg.getSubjectInfoId()+reg.getGradeCode())){
						regMap.put(schId+reg.getSubjectInfoId()+reg.getGradeCode(),regMap.get(schId+reg.getSubjectInfoId()+reg.getGradeCode())+1);
					}else{
						regMap.put(schId+reg.getSubjectInfoId()+reg.getGradeCode(),1);
					}
					if(regTotalMap.containsKey(schId+reg.getSubjectInfoId())){
						regTotalMap.put(schId+reg.getSubjectInfoId(),regTotalMap.get(schId+reg.getSubjectInfoId())+1);
					}else{
						regTotalMap.put(schId+reg.getSubjectInfoId(),1);
					}
					if(regTotalScoreMap.containsKey(schId+reg.getSubjectInfoId())){
						regTotalScoreMap.put(schId+reg.getSubjectInfoId(), regTotalScoreMap.get(schId+reg.getSubjectInfoId())+reg.getScore());
					}else{
						regTotalScoreMap.put(schId+reg.getSubjectInfoId(), reg.getScore());
					}
				}
			}
		}
		String schoolIds = exam.getSchoolIds();
		List<TeaexamCountDto> dtoListTemp = new ArrayList<TeaexamCountDto>();
		if(StringUtils.isNotBlank(schoolIds)){
			String[] schIdArr = schoolIds.split(",");
			List<Unit> schlList = SUtils.dt(unitRemoteService.findListByIds(schIdArr), new TR<List<Unit>>(){});
			Map<String, String> schNameMap = new HashMap<String, String>();
			for(Unit sch : schlList){
				schNameMap.put(sch.getId(), sch.getUnitName());
			}
			List<ExamCenterSchool> css = examCenterSchoolService.findCenterSchool();
			if(css == null) {
				css = new ArrayList<>();
			}
			Map<String, ExamCenterSchool> schCenter = new HashMap<>();
			if(CollectionUtils.isNotEmpty(css)) {
				for(ExamCenterSchool cs : css) {
					if(StringUtils.equals(cs.getType(), TeaexamConstant.TYPE_NORMAL_SCHOOL)) {
						schCenter.put(cs.getId(), cs);
					} else if(CollectionUtils.isNotEmpty(cs.getUnitIds())) {
						for(String unitId : cs.getUnitIds()) {
							schCenter.put(unitId, cs);
						}
					}
				}
			}
			// 没有设置的全在前面显示
			int index = 0;
			for(String schId : schIdArr){
				ExamCenterSchool cs = schCenter.get(schId);
				if(cs != null) {
					continue;
				}
				cs = new ExamCenterSchool();
				cs.setType(TeaexamConstant.TYPE_NORMAL_SCHOOL);
				cs.setId(schId);
				cs.setCenterSchoolName(schNameMap.get(schId));
				css.add(index++, cs);
			}
			map.put("schlList", css);
			ExamCenterSchool all = new ExamCenterSchool();
			all.setType(TeaexamConstant.TYPE_NORMAL_SCHOOL);
			all.setId(BaseConstants.ZERO_GUID);
			all.setCenterSchoolName("全体");
			css.add(all);
			for(TeaexamSubject sub : subDataList){
				for(ExamCenterSchool cs : css){
					String[] schIds; 
					boolean isCen = false;
					if(StringUtils.equals(cs.getType(), TeaexamConstant.TYPE_NORMAL_SCHOOL)) {
						schIds = new String[] {cs.getId()};
					} else if(CollectionUtils.isEmpty(cs.getUnitIds())){
						continue;
					} else {
						isCen = true;
						schIds = cs.getUnitIds().toArray(new String[0]);
					}
					int yxNum = 0;
					int hgNum = 0;
					int bhgNum = 0;
					int countNum = 0;
					float sumScore = 0;
					for (String schId : schIds) {
						if (null != regMap.get(schId + sub.getId() + TeaexamConstant.SCORE_GRADE_YX)) {
							yxNum += regMap.get(schId + sub.getId() + TeaexamConstant.SCORE_GRADE_YX);
						}
						if (null != regMap.get(schId + sub.getId() + TeaexamConstant.SCORE_GRADE_HG)) {
							hgNum += regMap.get(schId + sub.getId() + TeaexamConstant.SCORE_GRADE_HG);
						}
						if (null != regMap.get(schId + sub.getId() + TeaexamConstant.SCORE_GRADE_BHG)) {
							bhgNum += regMap.get(schId + sub.getId() + TeaexamConstant.SCORE_GRADE_BHG);
						}
						if (null != regTotalMap.get(schId + sub.getId())) {
							countNum += regTotalMap.get(schId + sub.getId());
						} 
						Float sum = regTotalScoreMap.get(schId+sub.getId());
						if(sum != null) {
							sumScore += sum;
						}
					}
					TeaexamCountDto dto = new TeaexamCountDto();
					dto.setSubjectName(sub.getSubjectName());
					dto.setYxNum(yxNum);
					dto.setHgNum(hgNum+yxNum);
					dto.setBhgNum(bhgNum);
					dto.setSchoolName(cs.getCenterSchoolName());
					dto.setCountNum(countNum);
					dto.setCenterSch(isCen);
					if(dto.getCountNum() != 0) {
						dto.setAvgScore(sumScore / dto.getCountNum());
						dto.setYxPer(dto.getYxNum()*100.0f/dto.getCountNum());
						dto.setHgPer(dto.getHgNum()*100.0f/dto.getCountNum());
						dto.setBhgPer(dto.getBhgNum()*100.0f/dto.getCountNum());
					}
					dto.setSchoolId(cs.getId());
					dto.setSubjectId(sub.getId());
					dtoListTemp.add(dto);
				}
			}
		}
		List<TeaexamCountDto> dtoList = new ArrayList<TeaexamCountDto>();
		if(StringUtils.isNotBlank(schoolId) && StringUtils.isBlank(subjectId)){
			for(TeaexamCountDto dto : dtoListTemp){
				if(schoolId.equals(dto.getSchoolId())){
					dtoList.add(dto);
				}
			}
		}else if(StringUtils.isBlank(schoolId) && StringUtils.isNotBlank(subjectId)){
			for(TeaexamCountDto dto : dtoListTemp){
				if(subjectId.equals(dto.getSubjectId())){
					dtoList.add(dto);
				}
			}
		}else if(StringUtils.isNotBlank(schoolId) && StringUtils.isNotBlank(subjectId)){
			for(TeaexamCountDto dto : dtoListTemp){
				if(subjectId.equals(dto.getSubjectId()) && schoolId.equals(dto.getSchoolId())){
					dtoList.add(dto);
				}
			}
		}
		else if(StringUtils.isBlank(schoolId) && StringUtils.isBlank(subjectId)){
			dtoList = dtoListTemp;
		}
		map.put("dtoList", dtoList);
		map.put("examName", exam.getExamName());
		map.put("subList", subList);
		map.put("examId", examId);
		map.put("subjectId", subjectId);
		map.put("schoolId", schoolId);
		return "/teaexam/scoreCount/showScoreDetail.ftl";
	}
	
	@RequestMapping("/centerSchool/list")
	public String centerIndex(String examId, String cenSchId,String subjectId, ModelMap map) {
		ExamCenterSchool cs = examCenterSchoolService.findCenterSchoolById(cenSchId);
		if(cs == null || CollectionUtils.isEmpty(cs.getUnitIds())) {
			return error("中心校不存在或没有下属学校！");
		}
		TeaexamInfo exam = teaexamInfoService.findOne(examId);
		List<TeaexamSubject> subDataList = teaexamSubjectService.findByExamIds(new String[]{examId});;
		for(TeaexamSubject sub : subDataList){
			if(sub.getSection()==1){
				sub.setSubjectName(sub.getSubjectName()+"（小学）");
			} else if (0 == sub.getSection()) {
				sub.setSubjectName(sub.getSubjectName()+"（学前）");
			}else if(sub.getSection()==2){
				sub.setSubjectName(sub.getSubjectName()+"（初中）");
			}else if(sub.getSection()==3){
				sub.setSubjectName(sub.getSubjectName()+"（高中）");
			}
		}
		List<TeaexamRegisterInfo> regListTemp = teaexamRegisterInfoService.findBy(examId, subjectId, null, TeaexamConstant.STATUS_PASS+"", null, null, null);
		regListTemp = regListTemp.stream().filter(e->cs.getUnitIds().contains(e.getSchoolId()) && StringUtils.isNotEmpty(e.getGradeCode())).collect(Collectors.toList());
		Map<String, List<TeaexamRegisterInfo>> regMap = EntityUtils.getListMap(regListTemp, TeaexamRegisterInfo::getSchoolId, Function.identity());
		List<Unit> schlList = Unit.dt(unitRemoteService.findListByIds(cs.getUnitIds().toArray(new String[0])));
		List<TeaexamCountDto> dtoList = new ArrayList<TeaexamCountDto>();
		for(Unit sch : schlList) {
			dtoList.addAll(getCountsByRegList(regMap.get(sch.getId()), subDataList, sch.getUnitName()));
		}
		map.put("examId", examId);
		map.put("subjectId", subjectId);
		map.put("cenSchId", cenSchId);
		map.put("examName", exam.getExamName());
		map.put("dtoList", dtoList);
		map.put("subList", subDataList);
		map.put("cenSchName", cs.getCenterSchoolName());
		return "/teaexam/scoreCount/centerDetail.ftl";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/centerSchool/export")
	public void exportCenter(String examId, String cenSchId,String subjectId, ModelMap map, HttpServletResponse response) {
		centerIndex(examId, cenSchId, subjectId, map);
		String csname = (String) map.get("cenSchName");
		List<TeaexamCountDto> dtoList = (List<TeaexamCountDto>) map.get("dtoList");
		statExport(dtoList, csname+"成绩详情", response);
	}
	
	public String getObjectName2() {
		return "成绩详情";
	}
	
	public List<String> getRowTitleList2() {
		List<String> tis = new ArrayList<String>();
		tis.add("序号");
		tis.add("学科");
		tis.add("单位");
		tis.add("参与考试人数");
		tis.add("优秀人数");
		tis.add("优秀率");
		tis.add("合格人数");
		tis.add("合格率");
		tis.add("不合格人数");
		tis.add("不合格率");
		tis.add("平均分");
		return tis;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/exportdetail")
	public void exportDetail(String examId, String schoolId, String subjectId, 
			ModelMap map, HttpServletResponse response){
		showScoreDetail(examId, subjectId, schoolId, map);
		String exName = (String) map.get("examName");
		List<TeaexamCountDto> dtoList = (List<TeaexamCountDto>) map.get("dtoList");
		statExport(dtoList, exName+"成绩详情", response);
	}
	
	/**
	 * 统计信息导出
	 * @param dtoList
	 * @param fileName
	 * @param response
	 */
	private void statExport(List<TeaexamCountDto> dtoList, String fileName, HttpServletResponse response) {
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList = new ArrayList<Map<String, String>>();
		int i=1;
		DecimalFormat df = new DecimalFormat("#.##");
		for(TeaexamCountDto item : dtoList){
			Map<String,String> sMap = new HashMap<String,String>();
			sMap.put("序号", String.valueOf(i));
			sMap.put("学科", item.getSubjectName());
			sMap.put("单位", item.getSchoolName());
			sMap.put("参与考试人数", String.valueOf(item.getCountNum()));
			sMap.put("优秀人数", String.valueOf(item.getYxNum()));
			sMap.put("优秀率", df.format(item.getYxPer())+"%");
			sMap.put("合格人数", String.valueOf(item.getHgNum()));
			sMap.put("合格率", df.format(item.getHgPer())+"%");
			sMap.put("不合格人数", String.valueOf(item.getBhgNum()));	
			sMap.put("不合格率", df.format(item.getBhgPer())+"%");
			sMap.put("平均分", df.format(item.getAvgScore())+"");
			i++;
			recordList.add(sMap);
		}
		sheetName2RecordListMap.put(getObjectName2(),recordList);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList2();
		titleMap.put(getObjectName2(), tis);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile(fileName, titleMap, sheetName2RecordListMap, response);	
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/exportAll")
	public void exportAll(String examId, ModelMap map, HttpServletResponse response){
		TeaexamInfo exam = teaexamInfoService.findOne(examId);
		showScoreDetail(examId, null, null, map);
		List<TeaexamCountDto> dtoList = (List<TeaexamCountDto>) map.get("dtoList");
		List<Map<String,String>> recordList2 = new ArrayList<Map<String, String>>();
		int i=1;
		DecimalFormat df = new DecimalFormat("#.##");
		for(TeaexamCountDto item : dtoList){
			Map<String,String> sMap = new HashMap<String,String>();
			sMap.put("序号", String.valueOf(i));
			sMap.put("学科", item.getSubjectName());
			sMap.put("单位", item.getSchoolName());
			sMap.put("参与考试人数", String.valueOf(item.getCountNum()));
			sMap.put("优秀人数", String.valueOf(item.getYxNum()));
			sMap.put("优秀率", df.format(item.getYxPer())+"%");
			sMap.put("合格人数", String.valueOf(item.getHgNum()));
			sMap.put("合格率", df.format(item.getHgPer())+"%");
			sMap.put("不合格人数", String.valueOf(item.getBhgNum()));	
			sMap.put("不合格率", df.format(item.getBhgPer())+"%");
			sMap.put("平均分", df.format(item.getAvgScore())+"");
			i++;
			recordList2.add(sMap);
		}
		
		List<TeaexamSubject> subList2 = (List<TeaexamSubject>) map.get("subList");
		if(CollectionUtils.isNotEmpty(subList2)){
			List<TeaexamRegisterInfo> regList2 = teaexamRegisterInfoService.findByExamIdIn(new String[]{examId});
			for(TeaexamSubject sub : subList2){
				int yxNum = 0;
				int hgNum = 0;
				int bhgNum = 0;
				int examNum = 0;
				for(TeaexamRegisterInfo reg : regList2){
					if(sub.getId().equals(reg.getSubjectInfoId()) && reg.getStatus()==2 && StringUtils.isNotBlank(reg.getGradeCode())){
						if(StringUtils.isNotBlank(reg.getGradeCode()) && TeaexamConstant.SCORE_GRADE_YX.equals(reg.getGradeCode())){
							yxNum++;
						}else if(StringUtils.isNotBlank(reg.getGradeCode()) && TeaexamConstant.SCORE_GRADE_HG.equals(reg.getGradeCode())){
							hgNum++;
						}else if(StringUtils.isNotBlank(reg.getGradeCode()) && TeaexamConstant.SCORE_GRADE_BHG.equals(reg.getGradeCode())){
							bhgNum++;
						}
						examNum++;
					}
				}
				sub.setExamNum(examNum);
				sub.setYxCount(yxNum);
				sub.setHgCount(hgNum+yxNum);
				sub.setBhgCount(bhgNum);
				if(sub.getSection()==1){
					sub.setSubjectName(sub.getSubjectName());
				} else if (0 == sub.getSection()) {
					sub.setSubjectName(sub.getSubjectName());
				}else if(sub.getSection()==2){
					sub.setSubjectName(sub.getSubjectName());
				}else if(sub.getSection()==3){
					sub.setSubjectName(sub.getSubjectName());
				}
			}
		}		
		Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
		List<Map<String,String>> recordList1 = new ArrayList<Map<String, String>>();
		int p=1;
		for(TeaexamSubject item : subList2){
			Map<String,String> sMap = new HashMap<String,String>();
			sMap.put("序号", String.valueOf(p));
			sMap.put("学科", item.getSubjectName());
			sMap.put("参与考试人数", String.valueOf(item.getExamNum()));
			sMap.put("优秀人数", String.valueOf(item.getYxCount()));
			sMap.put("合格人数", String.valueOf(item.getHgCount()));
			sMap.put("不合格人数", String.valueOf(item.getBhgCount()));	
			p++;
			recordList1.add(sMap);
		}
		sheetName2RecordListMap.put("成绩概况",recordList1);
		sheetName2RecordListMap.put("成绩详情",recordList2);
		Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
		List<String> tis = getRowTitleList1();
		List<String> tis2 = getRowTitleList2();
		titleMap.put("成绩概况", tis);
		titleMap.put("成绩详情", tis2);
		ExportUtils ex = ExportUtils.newInstance();
		ex.exportXLSFile(exam.getExamName()+"成绩情况", titleMap, sheetName2RecordListMap, response);	
	}

	// ===============学校端 成绩查询统计=========================
	@RequestMapping("/schIndex/page")
	public String schIndex(HttpServletRequest req, ModelMap map) {
		int year = NumberUtils.toInt(req.getParameter("year")); 
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		if(year == 0) {
			year = nowy;
		}
		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		map.put("year", year);
		List<TeaexamInfo> examList = teaexamInfoService.findByInfoYearTypeSchoolId(year, TeaexamConstant.EXAM_INFOTYPE_0, getLoginInfo().getUnitId());
		map.put("examList", examList);
		return "/teaexam/scoreCount/schoolIndex.ftl";
	}
	
	@RequestMapping("/sch/showDetail")
	public String schList(HttpServletRequest req, ModelMap map) {
		String examId = req.getParameter("examId");
		String type = req.getParameter("resultType");
		LoginInfo li = getLoginInfo();
		List<TeaexamRegisterInfo> regList = teaexamRegisterInfoService.findBy(examId, null, li.getUnitId(), TeaexamConstant.STATUS_PASS+"", null, null, null);
		List<TeaexamSubject> subList2 = teaexamSubjectService.findByExamIds(new String[] { examId });
		subList2.stream().sorted((a,b)-> {
			if(a.getSection() == b.getSection()) {
				return a.getSubjectName().compareTo(b.getSubjectName());
			}
			return a.getSection()-b.getSection();
		}) ;
		Map<String, TeaexamSubject> subNameMap = new HashMap<>();
		for (TeaexamSubject sub : subList2) {
			if(sub.getSection()==1){
				sub.setSubjectName(sub.getSubjectName()+"（小学）");
			} else if (0 == sub.getSection()) {
				sub.setSubjectName(sub.getSubjectName()+"（学前）");
			}else if(sub.getSection()==2){
				sub.setSubjectName(sub.getSubjectName()+"（初中）");
			}else if(sub.getSection()==3){
				sub.setSubjectName(sub.getSubjectName()+"（高中）");
			}
			subNameMap.put(sub.getId(), sub);
		} 
		boolean hasSub = MapUtils.isNotEmpty(subNameMap);
		if ("1".equals(type)) {
			List<String> tIds = EntityUtils.getList(regList, TeaexamRegisterInfo::getTeacherId);
			List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(tIds.toArray(new String[0])), new TR<List<Teacher>>(){});
			List<String> dpIds = EntityUtils.getList(teacherList, Teacher::getDeptId);
			Map<String, String> deptNameMap = EntityUtils.getMap(Dept.dt(deptRemoteService.findListByIds(dpIds.toArray(new String[0]))), 
					Dept::getId, Dept::getDeptName);
			Map<String, Teacher> teaMap = EntityUtils.getMap(teacherList, Teacher::getId);
			for(Iterator<TeaexamRegisterInfo> it = regList.iterator();it.hasNext();){
				TeaexamRegisterInfo reg = it.next();
				Teacher tea = teaMap.get(reg.getTeacherId());
				if(tea == null) {
					it.remove();
				}
				reg.setTeacherName(tea.getTeacherName());
				reg.setUserName(deptNameMap.get(tea.getDeptId()));
				reg.setIdentityCard(tea.getIdentityCard());
				if (hasSub && subNameMap.containsKey(reg.getSubjectInfoId())) {
					TeaexamSubject sb = subNameMap.get(reg.getSubjectInfoId());
					reg.setSubName(sb.getSubjectName());
					reg.setStatus(sb.getSection());
				} 
			}
			regList.sort((a,b)->{
				return a.getStatus()==b.getStatus()?StringUtils.trimToEmpty(a.getSubName()).compareTo(b.getSubName()):(a.getStatus()-b.getStatus());
			});
			map.put("regList", regList);
			return "/teaexam/scoreCount/schScoreList.ftl";
		} else {
			map.put("dtoList", getCountsByRegList(regList, subList2, li.getUnitName()));
			return "/teaexam/scoreCount/schStatList.ftl";
		}
	}
	
	/**
	 * 计算各合计人数和总分
	 * @param regList
	 * @param subList
	 * @return
	 */
	private List<TeaexamCountDto> getCountsByRegList(List<TeaexamRegisterInfo> regList, 
			List<TeaexamSubject> subList, String schName){
		List<TeaexamCountDto> dtoList = new ArrayList<TeaexamCountDto>();
		if(CollectionUtils.isEmpty(regList) || CollectionUtils.isEmpty(subList)) {
			return dtoList;
		}
		Map<String, Integer> regMap = new HashMap<String, Integer>();
		Map<String, Integer> regTotalMap = new HashMap<String, Integer>();
		Map<String, Float> regTotalScoreMap = new HashMap<String, Float>();
		for(TeaexamRegisterInfo reg : regList){
			if(StringUtils.isEmpty(reg.getGradeCode())
					|| !(reg.getScore() != null && reg.getScore() > 0)) {
				continue;
			}
			if(reg.getScore() == null) {
				reg.setScore(0f);
			}
			if(regMap.containsKey(reg.getSubjectInfoId()+reg.getGradeCode())){
				regMap.put(reg.getSubjectInfoId()+reg.getGradeCode(),regMap.get(reg.getSubjectInfoId()+reg.getGradeCode())+1);
			}else{
				regMap.put(reg.getSubjectInfoId()+reg.getGradeCode(),1);
			}
			if(regTotalMap.containsKey(reg.getSubjectInfoId())){
				regTotalMap.put(reg.getSubjectInfoId(),regTotalMap.get(reg.getSubjectInfoId())+1);
			}else{
				regTotalMap.put(reg.getSubjectInfoId(),1);
			}
			if(regTotalScoreMap.containsKey(reg.getSubjectInfoId())){
				regTotalScoreMap.put(reg.getSubjectInfoId(), regTotalScoreMap.get(reg.getSubjectInfoId())+reg.getScore());
			}else{
				regTotalScoreMap.put(reg.getSubjectInfoId(), reg.getScore());
			}
		}
		List<String> subIds = EntityUtils.getList(regList, TeaexamRegisterInfo::getSubjectInfoId);
		for(TeaexamSubject sub : subList){
			if(!subIds.contains(sub.getId())) {
				continue;
			}
			int yxNum = 0;
			int hgNum = 0;
			int bhgNum = 0;
			int countNum = 0;
			float sumScore = 0;
			if (null != regMap.get(sub.getId() + TeaexamConstant.SCORE_GRADE_YX)) {
				yxNum = regMap.get(sub.getId() + TeaexamConstant.SCORE_GRADE_YX);
			}
			if (null != regMap.get(sub.getId() + TeaexamConstant.SCORE_GRADE_HG)) {
				hgNum = regMap.get(sub.getId() + TeaexamConstant.SCORE_GRADE_HG);
			}
			if (null != regMap.get(sub.getId() + TeaexamConstant.SCORE_GRADE_BHG)) {
				bhgNum = regMap.get(sub.getId() + TeaexamConstant.SCORE_GRADE_BHG);
			}
			if (null != regTotalMap.get(sub.getId())) {
				countNum = regTotalMap.get(sub.getId());
			} 
			Float sum = regTotalScoreMap.get(sub.getId());
			if(sum != null) {
				sumScore = sum;
			}
			TeaexamCountDto dto = new TeaexamCountDto();
			dto.setSubjectName(sub.getSubjectName());
			dto.setYxNum(yxNum);
			dto.setHgNum(hgNum+yxNum);
			dto.setBhgNum(bhgNum);
			dto.setCountNum(countNum);
			if(dto.getCountNum() != 0) {
				dto.setAvgScore(sumScore / dto.getCountNum());
				dto.setYxPer(dto.getYxNum()*100.0f/dto.getCountNum());
				dto.setHgPer(dto.getHgNum()*100.0f/dto.getCountNum());
				dto.setBhgPer(dto.getBhgNum()*100.0f/dto.getCountNum());
			}
			dto.setSchoolName(schName);
			dto.setSubjectId(sub.getId());
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/sch/export")
	public void exportSch(HttpServletRequest req, ModelMap map, HttpServletResponse resp) {
		schList(req, map);
		String type = req.getParameter("resultType");
		String examId = req.getParameter("examId");
		TeaexamInfo exam = teaexamInfoService.findOne(examId);
		if("1".equals(type)) {
			List<TeaexamRegisterInfo> regList = (List<TeaexamRegisterInfo>) map.get("regList");
			for(TeaexamRegisterInfo info : regList) {
				if(TeaexamConstant.SCORE_GRADE_YX.equals(info.getGradeCode())) {
					info.setGradeCode("优秀");
				} else if(TeaexamConstant.SCORE_GRADE_HG.equals(info.getGradeCode())) {
					info.setGradeCode("合格");
				} else {
					info.setGradeCode("不合格");
				}
			}
			String fileName = exam.getExamName()+"原始成绩";
			String[] pros = new String[] { "teacherName", "userName", "identityCard", "subName", "score", "gradeCode" };
			String[] titles = new String[] { "教师姓名", "部门", "身份证号", "考试科目", "考试成绩", "考试评价" };
			Map records = new HashMap<>();
			records.put(fileName.toString(), regList);
			ExportUtils.newInstance().exportXLSFile(titles, pros, records, fileName, resp);
		} else {
			List<TeaexamCountDto> dtoList = (List<TeaexamCountDto>) map.get("dtoList");
			statExport(dtoList, exam.getExamName()+"统计分析", resp);
		}
	}
	
}
