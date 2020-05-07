package net.zdsoft.tutor.data.action.teacher;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import net.zdsoft.tutor.data.constant.TutorConstants;
import net.zdsoft.tutor.data.dto.TutorSumRecordDto;
import net.zdsoft.tutor.data.entity.TutorRecordDetailed;
import net.zdsoft.tutor.data.entity.TutorRound;
import net.zdsoft.tutor.data.entity.TutorRoundTeacher;
import net.zdsoft.tutor.data.service.TutorRecordDetailedService;
import net.zdsoft.tutor.data.service.TutorRecordService;
import net.zdsoft.tutor.data.service.TutorRoundService;
import net.zdsoft.tutor.data.service.TutorRoundTeacherService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;

@Controller
@RequestMapping("/tutor/allrecord")
public class TutorAllRecordAction extends BaseAction {
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private TutorRecordService tutorRecordService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private TutorRecordDetailedService tutorRecordDetailedService;
	@Autowired
	private TutorRoundService tutorRoundService;
	@Autowired
	private TutorRoundTeacherService tutorRoundTeacherService;
	@RequestMapping("/tab/page")
	public String allrecordTabPage(ModelMap map){
		return "/tutor/teacher/findall/tutorAllRecordIndex.ftl";
	}
	
	@RequestMapping("/query/head")
	public String allrecordTeaHead(String type,ModelMap map){
		//当前的学年学期
    	Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(0, getLoginInfo().getUnitId()), Semester.class);
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>(){});
        List<McodeDetail> recordTypes = SUtils.dt(mcodeRemoteService.findByMcodeIds(TutorConstants.TUTOR_RECORD_TYPE_MCODE_ID), McodeDetail.class);
	    //添加轮次条件
        List<TutorRound> tutorRounds = tutorRoundService.findByUnitId(getLoginInfo().getUnitId());
		map.put("tutorList", tutorRounds);
        map.put("recordTypes", recordTypes);
        map.put("semester", semester); 
		map.put("acadyearList", acadyearList);
		map.put("type", type);
		map.put("isShowTN", true);
		return "/tutor/teacher/findall/tutorAllRecordtab.ftl";
	}
	
//	@RequestMapping("/teacher/recordsum/list")
//	public String teaRecordSumList(String tutorId,String acadyear,String semester,ModelMap map){
//		List<TutorSumRecordDto> sumRecordDtos = Lists.newArrayList();
//		
//		Set<String> teacherIds = new HashSet<String>();
//		if(StringUtils.isNotBlank(tutorId)) {
//			List<TutorRoundTeacher> listTRT =tutorRoundTeacherService.findByRoundId(tutorId);
//			teacherIds = EntityUtils.getSet(listTRT, "teacherId");
//		}else {
//			List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByUnitId(getLoginInfo().getUnitId()), 
//					Teacher.class);
//			teacherIds = EntityUtils.getSet(teachers, "id");
//		}
////		List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByDeptId(deptId),Teacher.class);
////		Set<String> teacherIds = EntityUtils.getSet(teachers, "id");
//		List<Teacher> teachers = SUtils.dt(teacherRemoteService.findListByIds
//				(teacherIds.toArray(new String[teacherIds.size()])),Teacher.class);
//		Map<String,String> teaNameMap = EntityUtils.getMap(teachers, "id","teacherName");
//		if(teacherIds.size()>0){
//			List<TutorRecord> listTR = tutorRecordService.findByTIdsAndSemester(teacherIds.toArray(new String[teacherIds.size()]), acadyear, semester);
//			List<TutorRecordDetailed> listTRD = tutorRecordDetailedService.findByTIdsAndSemester(teacherIds.toArray(new String[teacherIds.size()]), acadyear, semester);
//			Map<String, List<TutorRecord>> trMap = EntityUtils.getListMap(listTR, "teacherId", null);
//			Map<String, List<TutorRecordDetailed>> trdMap = EntityUtils.getListMap(listTRD, "teacherId", null);
//			for(String key:trMap.keySet()){
//				List<TutorRecord> records = trMap.get(key);  //过滤一下就可以了
//				List<TutorRecordDetailed> detaileds = (trdMap.isEmpty()? null :trdMap.get(key));
//				if(CollectionUtils.isNotEmpty(records)){
//					TutorSumRecordDto recordDto = new TutorSumRecordDto();
//					recordDto.setTeacherId(key);
//					if(teaNameMap.containsKey(key)){
//						recordDto.setTeacherName(teaNameMap.get(key));
//					}
//					//加一个判断 根据 tacherid + detailedId  得出是同一个
//					for(TutorRecord record:records){
//						if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID01.equals(record.getRecordType())){
//							recordDto.setXsgkNum(recordDto.getXsgkNum()+1);
//						}else if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID03.equals(record.getRecordType())){
//							recordDto.setSsgtNum(recordDto.getSsgtNum()+1);
//						}else if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID04.equals(record.getRecordType())){
//							recordDto.setJxgtNum(recordDto.getJxgtNum()+1);
//						}else if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID05.equals(record.getRecordType())){
//							recordDto.setZthdNum(recordDto.getZthdNum()+1);
//						}else if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID07.equals(record.getRecordType())){
//							recordDto.setPjxsNum(recordDto.getPjxsNum()+1);
//						}
//					}
//					//统计其它的三项（不包含学生的）
//                    if(CollectionUtils.isNotEmpty(detaileds)) {
//                    	for (TutorRecordDetailed tutorRecordDetailed : detaileds) {
//                    		if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID02.equals(tutorRecordDetailed.getRecordType())){
//                    			recordDto.setGzjhNum(recordDto.getGzjhNum()+1);
//                    		}else if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID06.equals(tutorRecordDetailed.getRecordType())){
//                    			recordDto.setTkxxNum(recordDto.getTkxxNum()+1);
//                    		}else if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID08.equals(tutorRecordDetailed.getRecordType())){
//                    			recordDto.setDszjNum(recordDto.getDszjNum()+1);
//                    		}
//						}
//                    }
//					sumRecordDtos.add(recordDto);
//				}
//			}
//		}
//		map.put("sumRecordDtos", sumRecordDtos);
//		return "/tutor/teacher/findall/tutorAllRecordTeaList.ftl";
//	}
	
	
	@RequestMapping("/teacher/recordsum/list")
	public String teaRecordSumList(String tutorId,String acadyear,String semester,ModelMap map){
		List<TutorSumRecordDto> sumRecordDtos = Lists.newArrayList();
		Set<String> teacherIds = new HashSet<String>();
		if(StringUtils.isNotBlank(tutorId)) {
			List<TutorRoundTeacher> listTRT =tutorRoundTeacherService.findByRoundId(tutorId);
			teacherIds = EntityUtils.getSet(listTRT, TutorRoundTeacher::getTeacherId);
		}else {
			List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByUnitId(getLoginInfo().getUnitId()), 
					Teacher.class);
			teacherIds = EntityUtils.getSet(teachers, Teacher::getId);
		}
		List<Teacher> teachers = SUtils.dt(teacherRemoteService.findListByIds
				(teacherIds.toArray(new String[teacherIds.size()])),Teacher.class);
		Map<String,String> teaNameMap = EntityUtils.getMap(teachers, Teacher::getId, Teacher::getTeacherName);
		if(teacherIds.size()>0){
			List<TutorRecordDetailed> listTRD = tutorRecordDetailedService.findByTIdsAndSemester(teacherIds.toArray(new String[teacherIds.size()]), acadyear, semester);
			Map<String, List<TutorRecordDetailed>> trdMap = EntityUtils.getListMap(listTRD, TutorRecordDetailed::getTeacherId, Function.identity());
			for(String key:trdMap.keySet()){
				List<TutorRecordDetailed> detaileds = (trdMap.isEmpty()? null :trdMap.get(key));
				//统计其它的三项（不包含学生的）
                if(CollectionUtils.isNotEmpty(detaileds)) {
                	TutorSumRecordDto recordDto = new TutorSumRecordDto();
                	recordDto.setTeacherId(key);
					if(teaNameMap.containsKey(key)){
						recordDto.setTeacherName(teaNameMap.get(key));
					}
                	for (TutorRecordDetailed tutorRecordDetailed : detaileds) {
                		if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID01.equals(tutorRecordDetailed.getRecordType())){
							recordDto.setXsgkNum(recordDto.getXsgkNum()+1);
						}else if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID03.equals(tutorRecordDetailed.getRecordType())){
							recordDto.setSsgtNum(recordDto.getSsgtNum()+1);
						}else if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID04.equals(tutorRecordDetailed.getRecordType())){
							recordDto.setJxgtNum(recordDto.getJxgtNum()+1);
						}else if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID05.equals(tutorRecordDetailed.getRecordType())){
							recordDto.setZthdNum(recordDto.getZthdNum()+1);
						}else if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID07.equals(tutorRecordDetailed.getRecordType())){
							recordDto.setPjxsNum(recordDto.getPjxsNum()+1);
						}else if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID02.equals(tutorRecordDetailed.getRecordType())){
                			recordDto.setGzjhNum(recordDto.getGzjhNum()+1);
                		}else if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID06.equals(tutorRecordDetailed.getRecordType())){
                			recordDto.setTkxxNum(recordDto.getTkxxNum()+1);
                		}else if(TutorConstants.TUTOR_RECORD_TYPE_THIS_ID08.equals(tutorRecordDetailed.getRecordType())){
                			recordDto.setDszjNum(recordDto.getDszjNum()+1);
                		}
					}
                	sumRecordDtos.add(recordDto);
                }
			}
		}
		map.put("sumRecordDtos", sumRecordDtos);
		return "/tutor/teacher/findall/tutorAllRecordTeaList.ftl";
	}
}
