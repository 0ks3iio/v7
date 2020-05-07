package net.zdsoft.teacherasess.data.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.teacherasess.data.dao.TeacherasessDao;
import net.zdsoft.teacherasess.data.entity.TeacherAsess;
import net.zdsoft.teacherasess.data.entity.TeacherAsessCheck;
import net.zdsoft.teacherasess.data.entity.TeacherAsessLine;
import net.zdsoft.teacherasess.data.entity.TeacherAsessRank;
import net.zdsoft.teacherasess.data.entity.TeacherAsessResult;
import net.zdsoft.teacherasess.data.entity.TeacherAsessSet;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvert;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertResult;
import net.zdsoft.teacherasess.data.service.TeacherasessCheckService;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertResultService;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertService;
import net.zdsoft.teacherasess.data.service.TeacherasessLineService;
import net.zdsoft.teacherasess.data.service.TeacherasessRankService;
import net.zdsoft.teacherasess.data.service.TeacherasessResultService;
import net.zdsoft.teacherasess.data.service.TeacherasessService;
import net.zdsoft.teacherasess.data.service.TeacherasessSetService;

@Service("teacherasessService")
public class TeacherasessServiceImpl extends BaseServiceImpl<TeacherAsess, String> implements TeacherasessService {

	@Autowired
	private TeacherasessDao teacherasessDao;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private TeacherasessResultService teacherasessResultService;
	@Autowired
	private ClassTeachingRemoteService classTeachingRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeacherasessConvertResultService teacherasessConvertResultService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuRemoteService;
	@Autowired
	private TeacherasessConvertService teacherasessConvertService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;

	@Override
	public List<TeacherAsess> findByUnitIdAndAcayearWithMaster(String unitId,
			String acadyear) {
		List<TeacherAsess> teacherAsesses=teacherasessDao.findByUnitIdAndAcayearWithMaster(unitId, acadyear);
		if(CollectionUtils.isNotEmpty(teacherAsesses)){
			Set<String> asessIds = EntityUtils.getSet(teacherAsesses, TeacherAsess::getConvertId);
			Set<String> asessIdss = EntityUtils.getSet(teacherAsesses, TeacherAsess::getReferConvertId);
			asessIds.addAll(asessIdss);
			List<TeacherasessConvert> teacherasessConverts=teacherasessConvertService.findListByIdIn(asessIds.toArray(new String[0]));
			Map<String,TeacherasessConvert> teaMap=EntityUtils.getMap(teacherasessConverts, TeacherasessConvert::getId);
			Map<String, Grade> gradeMap = EntityUtils.getMap(SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(unitId, acadyear), new TR<List<Grade>>() {}), Grade::getId);
			Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
			for (TeacherAsess teacherAsess : teacherAsesses) {
				if(StringUtils.equals(semester.getAcadyear(), teacherAsess.getAcadyear())){
					teacherAsess.setConvert(true);
				}
				if(gradeMap.containsKey(teacherAsess.getGradeId())){
					teacherAsess.setGradeName(gradeMap.get(teacherAsess.getGradeId()).getGradeName());
				}
				if(teaMap.containsKey(teacherAsess.getConvertId())){
					teacherAsess.setConvertName(teaMap.get(teacherAsess.getConvertId()).getName()+(teacherAsess.getIsDeleted()==1?"(已删除)":""));
				}
				if(teaMap.containsKey(teacherAsess.getReferConvertId())){
					teacherAsess.setReferConvertName(teaMap.get(teacherAsess.getReferConvertId()).getName()+(teacherAsess.getIsDeleted()==1?"(已删除)":""));
				}
			}
		}
		return teacherAsesses;
	}

	@Override
	public List<TeacherAsess> saveAllEntitys(TeacherAsess... asesses) {
		return teacherasessDao.saveAll(checkSave(asesses));
	}

	@Override
	public void deleteByIdIn(String unitId,String... id) {
		teacherasessResultService.deleteByAssessId(unitId, id);
		teacherasessDao.deleteByIdIn(id);
	}
	@Autowired
	private TeacherasessSetService teacherasessSetService;
	@Autowired
	private TeacherasessLineService teacherasessLineService;
	@Autowired
	private TeacherasessRankService teacherasessRankService;
	@Autowired
	private TeacherasessCheckService teacherasessCheckService;
	
	private void sortList(List<TeacherAsessCheck> subLineScorelist,List<TeacherAsessCheck> clsChecks){
		SortUtils.ASC(subLineScorelist, "lineScore");
		float oldScore = 0f;
		int oldRank = 0;
		for (int i = 0; i < subLineScorelist.size(); i++) {
			TeacherAsessCheck e = subLineScorelist.get(i);
			if(i == 0) {
				oldRank = 1;
				oldScore = e.getLineScore();
			}else {
				if(e.getLineScore() > oldScore) {
					oldScore = e.getLineScore();
					oldRank = i+1;
				}
			}
			e.setLineRank(oldRank);
			clsChecks.add(e);
		}
	}
	private List<TeacherAsessCheck> getSortAndScoreList(List<TeacherAsessCheck> subLineScorelist, String lineId) {
		List<TeacherAsessCheck> list = new ArrayList<>();
		for (TeacherAsessCheck teacherAsessCheck : subLineScorelist) {
			if(StringUtils.equals(teacherAsessCheck.getAsessRankId(), lineId)) {
				list.add(teacherAsessCheck);
			}
		}
		//分越高，名次也越大（低）
		SortUtils.DESC(list, "lineScore");
		float oldScore = 0f;
		int oldRank = 0;
		for (int i = 0; i < list.size(); i++) {
			TeacherAsessCheck e = list.get(i);
			if(i == 0) {
				oldRank = 1;
				oldScore = e.getLineScore();
				
			}else {
				if(e.getLineScore() < oldScore) {
					oldScore = e.getLineScore();
					oldRank = i+1;
				}
			}
			e.setLineRank(oldRank);
		}
		return list;
	}
	
	private static Map<String, Integer> SortRankMap(List<TeacherasessConvertResult> result,Map<String,TeacherasessConvertResult> allMap) {
		Map<String, Integer> sumRankMap = new HashMap<>();
		if(CollectionUtils.isEmpty(result)) {
			return sumRankMap;
		}
		Set<String> stuIds = EntityUtils.getSet(result, TeacherasessConvertResult::getStudentId);
		List<TeacherasessConvertResult> list = new ArrayList<>();
		for (String stuId : stuIds) {
			if(allMap.containsKey(stuId)) {
				list.add(allMap.get(stuId));
			}
		}
		SortUtils.DESC(list, "score");//按照降序排序
		int i = 0;
		float oldScore = 0f;
		for (int j = 0; j < list.size(); j++) {
			TeacherasessConvertResult e = list.get(j);
			if(i == 0) {
				i = 1;
				oldScore = e.getScore();
			}else {
				if(e.getScore() == oldScore) {
				}else {
					i = j+1;
					oldScore = e.getScore();
				}
			}
			sumRankMap.put(e.getStudentId(), i);
		}
		return sumRankMap;
	}
	
	private void statClassABData(String teacherAsessId) {
		TeacherAsess teacherAsess = this.findOne(teacherAsessId);
		//班级的AB班设置
		List<TeacherAsessSet> classSetList = teacherasessSetService.
				findByUnitIdAndAsessId(teacherAsess.getUnitId(), teacherAsessId);
		Map<String, List<TeacherAsessSet>> clsSetMap = new HashMap<>();
		for (TeacherAsessSet classSet : classSetList) {
			if(!clsSetMap.containsKey(classSet.getSubjectId())) {
				clsSetMap.put(classSet.getSubjectId(), new ArrayList<>());
			}
			clsSetMap.get(classSet.getSubjectId()).add(classSet);
		}
		//A、B、C线得分设置
		List<TeacherAsessRank> lineSetList = teacherasessRankService.findByUnitIdAndAssessId(teacherAsess.getUnitId(), teacherAsessId);
		Map<String, List<TeacherAsessRank>> lineSetMap = new HashMap<>();
		for (TeacherAsessRank lineSet : lineSetList) {
			if(!lineSetMap.containsKey(lineSet.getSubjectId())) {
				lineSetMap.put(lineSet.getSubjectId(), new ArrayList<>());
			}
			lineSetMap.get(lineSet.getSubjectId()).add(lineSet);
		}
		
		//班级上线人数统计结果
		List<TeacherAsessLine> clsLines = new ArrayList<>();
		//班级各科目上线得分统计结果
		List<TeacherAsessCheck> clsChecks = new ArrayList<>();
		
		List<TeacherasessConvertResult> resultAllNew=teacherasessConvertResultService.findListByConvertId(teacherAsess.getConvertId(), Constant.GUID_ZERO, null);
		Map<String,TeacherasessConvertResult> newMap = EntityUtils.getMap(resultAllNew, TeacherasessConvertResult::getStudentId);
		List<TeacherasessConvertResult> resultAllOld=teacherasessConvertResultService.findListByConvertId(teacherAsess.getReferConvertId(), Constant.GUID_ZERO, null);
		Map<String,TeacherasessConvertResult> oldMap = EntityUtils.getMap(resultAllOld, TeacherasessConvertResult::getStudentId);
		//统计已经设置了A、B分层的科目班级
		for (String subjectId : clsSetMap.keySet()) {
			List<TeacherAsessSet> clslist = clsSetMap.get(subjectId);
			if(CollectionUtils.isEmpty(clslist)) {
				System.out.println("未设置班级分层、该科目不统计："+subjectId);
				continue;
			}
			List<TeacherAsessRank> linelist = lineSetMap.get(subjectId);
			if(CollectionUtils.isEmpty(linelist)) {
				System.out.println("未设置得分线、该科目不统计："+subjectId);
				continue;
			}
			
			List<TeacherasessConvertResult> resultSubNew=teacherasessConvertResultService.findListByConvertId(teacherAsess.getConvertId(), subjectId, null);
			List<TeacherasessConvertResult> resultSubOld=teacherasessConvertResultService.findListByConvertId(teacherAsess.getReferConvertId(), subjectId, null);
			
			List<TeacherAsessCheck> subLineScorelist = new ArrayList<>();
			
			Map<String, Integer> sumNewRankMap = new HashMap<>(); 
			Map<String, Integer> sumOldRankMap = new HashMap<>();
			
			if(!StringUtils.equals(subjectId, Constant.GUID_ZERO)) {
				sumNewRankMap = SortRankMap(resultSubNew, newMap);
				sumOldRankMap = SortRankMap(resultSubOld, oldMap);
			}
			
			for (TeacherAsessSet clsSet : clslist) {
				//单个科目单个班级开始统计
				Set<String> stuIds = new HashSet<>();
				if(StringUtils.equals(clsSet.getClassType(), "1")) {
					List<Student> stulist = SUtils.dt(studentRemoteService.findByClassIds(clsSet.getClassId()), new TR<List<Student>>() {});
					stuIds = EntityUtils.getSet(stulist, Student::getId);
				}else {
					List<TeachClassStu> teachClassStus = SUtils.dt(teachClassStuRemoteService.findByClassIds(new String[] {clsSet.getClassId()}), new TR<List<TeachClassStu>>() {});
					stuIds = EntityUtils.getSet(teachClassStus, TeachClassStu::getStudentId);
				}
				if(stuIds == null || stuIds.size() == 0) {
					continue;
				}
				//本次折算分方案结果记录
				List<TeacherasessConvertResult> resultNew=teacherasessConvertResultService.findByUnitIdAndConvertIdAndSubjectIdAndStudentIdIn(teacherAsess.getUnitId(), teacherAsess.getConvertId(), subjectId, stuIds.toArray(new String[0]));
				//参照方案结果记录
				List<TeacherasessConvertResult> resultOld=teacherasessConvertResultService.findByUnitIdAndConvertIdAndSubjectIdAndStudentIdIn(teacherAsess.getUnitId(), teacherAsess.getReferConvertId(), subjectId, stuIds.toArray(new String[0]));
				//上线人数Map
				Map<String, Integer> lineNumMap = new HashMap<>();
				Map<String, Integer> lineNumMap2 = new HashMap<>();
				if(StringUtils.equals("0", clsSet.getSlice())) {
					//属于A层班
					SortUtils.DESC(linelist, "aslice");
					for (TeacherasessConvertResult e : resultNew) {
						for (TeacherAsessRank line : linelist) {
							if(line.getAslice() >= e.getRank()) {
								//满足单上线排名要求
								if(!lineNumMap.containsKey(line.getId()+"_1")) {
									lineNumMap.put(line.getId()+"_1",0);
								}
								lineNumMap.put(line.getId()+"_1", lineNumMap.get(line.getId()+"_1")+1);
								//满足双上线排名要求
								if(sumNewRankMap.containsKey(e.getStudentId())
										&& line.getAslice() >= sumNewRankMap.get(e.getStudentId())) {
//									System.out.println("subjectId="+subjectId);
									if("00000000000000000000000000000007".equals(subjectId)
											&& clsSet.getClassId().equals("2c9180846d1d02b5016d1d23ea5b664a")&&line.getAslice()==30) {
										System.out.println(e.getStudentId()+">>>"+e.getRank()+">>>>"+sumNewRankMap.get(e.getStudentId()));
									}
									if(!lineNumMap2.containsKey(line.getId()+"_1")) {
										lineNumMap2.put(line.getId()+"_1",0);
									}
									lineNumMap2.put(line.getId()+"_1", lineNumMap2.get(line.getId()+"_1")+1);
								}
							}
						}
					}
					for (TeacherasessConvertResult e : resultOld) {
						for (TeacherAsessRank line : linelist) {
							if(line.getAslice() >= e.getRank()) {
								//满足单上线排名要求
								if(!lineNumMap.containsKey(line.getId()+"_2")) {
									lineNumMap.put(line.getId()+"_2",0);
								}
								lineNumMap.put(line.getId()+"_2", lineNumMap.get(line.getId()+"_2")+1);
								//满足双上线排名要求
								if(sumOldRankMap.containsKey(e.getStudentId())
										&& line.getAslice() >= sumOldRankMap.get(e.getStudentId())) {
									if(!lineNumMap2.containsKey(line.getId()+"_2")) {
										lineNumMap2.put(line.getId()+"_2",0);
									}
									lineNumMap2.put(line.getId()+"_2", lineNumMap2.get(line.getId()+"_2")+1);
								}
							}
						}
					}
				}else {
					// 属于B层板
					SortUtils.DESC(linelist, "bslice");
					for (TeacherasessConvertResult e : resultNew) {
						for (TeacherAsessRank line : linelist) {
							if(line.getBslice() >= e.getRank()) {
								//满足单上线排名要求
								if(!lineNumMap.containsKey(line.getId()+"_1")) {
									lineNumMap.put(line.getId()+"_1",0);
								}
								lineNumMap.put(line.getId()+"_1", lineNumMap.get(line.getId()+"_1")+1);
								//满足双上线排名要求
								if(sumNewRankMap.containsKey(e.getStudentId())
										&& line.getAslice() >= sumNewRankMap.get(e.getStudentId())) {
									if(!lineNumMap2.containsKey(line.getId()+"_1")) {
										lineNumMap2.put(line.getId()+"_1",0);
									}
									lineNumMap2.put(line.getId()+"_1", lineNumMap2.get(line.getId()+"_1")+1);
								}
							}
						}
					}
					for (TeacherasessConvertResult e : resultOld) {
						for (TeacherAsessRank line : linelist) {
							if(line.getBslice() >= e.getRank()) {
								//满足单上线排名要求
								if(!lineNumMap.containsKey(line.getId()+"_2")) {
									lineNumMap.put(line.getId()+"_2",0);
								}
								lineNumMap.put(line.getId()+"_2", lineNumMap.get(line.getId()+"_2")+1);
								//满足双上线排名要求
								if(sumOldRankMap.containsKey(e.getStudentId())
										&& line.getAslice() >= sumOldRankMap.get(e.getStudentId())) {
									if(!lineNumMap2.containsKey(line.getId()+"_2")) {
										lineNumMap2.put(line.getId()+"_2",0);
									}
									lineNumMap2.put(line.getId()+"_2", lineNumMap2.get(line.getId()+"_2")+1);
								}
							}
						}
					}
				}
				for (TeacherAsessRank line : linelist) {
					//单上线
					int num1 = lineNumMap.containsKey(line.getId()+"_1")?lineNumMap.get(line.getId()+"_1"):0;
					int num2 = lineNumMap.containsKey(line.getId()+"_2")?lineNumMap.get(line.getId()+"_2"):0;
					String str = "";
					String str1 = "";
					if(StringUtils.equals(clsSet.getSlice(), "0")) {
						str = line.getAslice()+"(A班单上线)";
						str1 = line.getAslice()+"(A班双上线)";
					}else {
						str = line.getAslice()+"(B班单上线)";
						str1 = line.getAslice()+"(B班双上线)";

					}
					TeacherAsessLine clsOneLine1 = new TeacherAsessLine(teacherAsess.getUnitId(),
							teacherAsessId,subjectId,clsSet.getClassId(),clsSet.getClassType(),
							clsSet.getClassName(),str,clsSet.getSlice(),line.getId(),"1","1",num1);
					clsOneLine1.setId(UuidUtils.generateUuid());
					TeacherAsessLine clsOneLine2 = new TeacherAsessLine(teacherAsess.getUnitId(),
							teacherAsessId,subjectId,clsSet.getClassId(),clsSet.getClassType(),
							clsSet.getClassName(),str,clsSet.getSlice(),line.getId(),"2","1",num2);
					clsOneLine2.setId(UuidUtils.generateUuid());
					clsLines.add(clsOneLine1);
					clsLines.add(clsOneLine2);
					if(!StringUtils.equals(subjectId, Constant.GUID_ZERO)) {
						//单科双上线统计
						int num3 = lineNumMap2.containsKey(line.getId()+"_1")?lineNumMap2.get(line.getId()+"_1"):0;
						int num4 = lineNumMap2.containsKey(line.getId()+"_2")?lineNumMap2.get(line.getId()+"_2"):0;
						TeacherAsessLine clsOneLine3 = new TeacherAsessLine(teacherAsess.getUnitId(),
								teacherAsessId,subjectId,clsSet.getClassId(),clsSet.getClassType(),
								clsSet.getClassName(),str1,clsSet.getSlice(),line.getId(),"1","2",num3);
						clsOneLine3.setId(UuidUtils.generateUuid());
						TeacherAsessLine clsOneLine4 = new TeacherAsessLine(teacherAsess.getUnitId(),
								teacherAsessId,subjectId,clsSet.getClassId(),clsSet.getClassType(),
								clsSet.getClassName(),str1,clsSet.getSlice(),line.getId(),"2","2",num4);
						clsOneLine4.setId(UuidUtils.generateUuid());
						clsLines.add(clsOneLine3);
						clsLines.add(clsOneLine4);
						//单科统计时的得分计算，不统计名次线得分排名
						float lineScore = (num1-num2)*2f*0.4f+(num3-num4)*2f*0.6f;
						TeacherAsessCheck clsLineScore = new TeacherAsessCheck(teacherAsess.getUnitId(),teacherAsessId,subjectId,clsSet.getClassId(),
								clsSet.getClassType(),clsSet.getClassName(),line.getId(),lineScore,0);
						subLineScorelist.add(clsLineScore);
					}else {
						//总分统计时的得分计算，不统计名次线得分排名
						float lineScore = (num1-num2)*2f;
						TeacherAsessCheck clsLineScore = new TeacherAsessCheck(teacherAsess.getUnitId(),teacherAsessId,subjectId,clsSet.getClassId(),
								clsSet.getClassType(),clsSet.getClassName(),line.getId(),lineScore,0);
						subLineScorelist.add(clsLineScore);
					}
				}
			}
			//计算每个科目各个班级线排名，同时计算每个科目的得分与排名
			Map<String, Integer> clsLineRankMap = new HashMap<>();
			for (TeacherAsessRank line : linelist) {
				List<TeacherAsessCheck> list = getSortAndScoreList(subLineScorelist,line.getId());
				for (TeacherAsessCheck teacherAsessCheck : list) {
					teacherAsessCheck.setId(UuidUtils.generateUuid());
					clsChecks.add(teacherAsessCheck);
					clsLineRankMap.put(teacherAsessCheck.getClassId()+","+teacherAsessCheck.getAsessRankId(), teacherAsessCheck.getLineRank());
				}
			}
			List<TeacherAsessCheck> sumList = new ArrayList<>();
			//计算每个科目的得分
			for (TeacherAsessSet clsSet : clslist) {
				float sumScore = 0f;
				for (TeacherAsessRank line : linelist) {
					//获得在该线下的该班级的排名
					int clsLineRank = clsLineRankMap.containsKey(clsSet.getClassId()+","+line.getId())?clsLineRankMap.get(clsSet.getClassId()+","+line.getId()):0;
					float scale = line.getScale()/100f;
					sumScore = sumScore + clsLineRank*scale;
				}
				TeacherAsessCheck clsLineScore = new TeacherAsessCheck(teacherAsess.getUnitId(),teacherAsessId,subjectId,clsSet.getClassId(),
						clsSet.getClassType(),clsSet.getClassName(),subjectId,sumScore,0);
				clsLineScore.setId(UuidUtils.generateUuid());
				sumList.add(clsLineScore);
			}
			//计算每个科目的得分排名
			sortList(sumList,clsChecks);
		}
		//先删除原有的数据
		teacherasessLineService.deleteByAssessId(teacherAsessId);
		teacherasessCheckService.deleteByAssessId(teacherAsessId);
		if(CollectionUtils.isNotEmpty(clsLines)) {
			teacherasessLineService.saveAll(clsLines.toArray(new TeacherAsessLine[0]));
		}
		
		if(CollectionUtils.isNotEmpty(clsChecks)) {
			teacherasessCheckService.saveAll(clsChecks.toArray(new TeacherAsessCheck[0]));
		}
		
	}
	
	@Override
	public void dealWithTeacherAsessResult(String unitId, String acadyear,
			String semester, String teacherAsessId) {
		List<TeacherAsessResult> teacherAsessResults=new ArrayList<TeacherAsessResult>();
		TeacherAsess teacherAsess = this.findOneWithMaster(teacherAsessId);
		List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, teacherAsess.getGradeId()),new TR<List<Clazz>>() {});
		Set<String> classIds = EntityUtils.getSet(classList, Clazz::getId);
		Set<String> teacherIds = EntityUtils.getSet(classList, Clazz::getTeacherId);
		Map<String, String> teacherNameMap = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIds.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
		List<Student> studentList =  SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[0])), new TR<List<Student>>() {});
		Map<String,Set<String>> studentMap=new HashMap<String,Set<String>>();//行政班id 对应的一批学生id
		for (Student student : studentList) {
			if(!studentMap.containsKey(student.getClassId())){
				studentMap.put(student.getClassId(), new HashSet<>());
			}
			studentMap.get(student.getClassId()).add(student.getId());
		}
		for (Clazz clazz : classList) {
			TeacherAsessResult teacherAsessResult=new TeacherAsessResult();
			Set<String> studeSet=studentMap.get(clazz.getId());
			if(studeSet == null) {
				continue;
			}
			List<TeacherasessConvertResult> teacherasessConvertResultNews=teacherasessConvertResultService.findByUnitIdAndConvertIdAndSubjectIdAndStudentIdIn(unitId, teacherAsess.getConvertId(), "00000000000000000000000000000000", studeSet.toArray(new String[0]));
			List<TeacherasessConvertResult> teacherasessConvertResultOlds=teacherasessConvertResultService.findByUnitIdAndConvertIdAndSubjectIdAndStudentIdIn(unitId, teacherAsess.getReferConvertId(), "00000000000000000000000000000000", studeSet.toArray(new String[0]));
			
			int i=getUpNum(studeSet, teacherasessConvertResultNews, teacherasessConvertResultOlds);//班级进步人数
			
			int studentSize=teacherasessConvertResultNews.size();//本次行政班有效人数
			int studentSizeOld=teacherasessConvertResultOlds.size();//原始行政班有效人数
			int oldGankTotal=0;//年级名次之和
			int newGankTotal=0;
			for (TeacherasessConvertResult teacherasessConvertResult : teacherasessConvertResultNews) {
				newGankTotal+=teacherasessConvertResult.getRank();
			}
			for (TeacherasessConvertResult teacherasessConvertResult : teacherasessConvertResultOlds) {
				oldGankTotal+=teacherasessConvertResult.getRank();
			}
			int old=0;
			if(studentSizeOld!=0){
				old=oldGankTotal/studentSizeOld;
			}
			int news=0;
			float upScale=0.0f;
			if(studentSize!=0){
				news=newGankTotal/studentSize;
				upScale=(float)(i*100/studentSize);
			}
			teacherAsessResult.setId(UuidUtils.generateUuid());
			teacherAsessResult.setConvertParam(news);
			teacherAsessResult.setReferConvertParam(old);
			teacherAsessResult.setUnitId(unitId);
			teacherAsessResult.setAssessId(teacherAsessId);
			teacherAsessResult.setClassId(clazz.getId());
			teacherAsessResult.setClassType("1");//行政班
			teacherAsessResult.setClassName(clazz.getClassNameDynamic());
			teacherAsessResult.setSubjcetId("00000000000000000000000000000000");
			teacherAsessResult.setTeacherId(clazz.getTeacherId());
			teacherAsessResult.setTeacherName(teacherNameMap.get(clazz.getTeacherId()));
			teacherAsessResult.setUpStuNum(i);
			
			teacherAsessResult.setUpScale(upScale);
//			teacherAsessResult.setAsessScore((old-news)+i);//修改成 原始参照考试的班级所有学生年级名次之和÷班级有效考试人数－本次考试的班级所有学生年级名次之和÷班级有效考试人数
			teacherAsessResult.setAsessScore(old-news);
			teacherAsessResults.add(teacherAsessResult);
		}
		setAllRank(teacherAsessResults);
		
        //处理单科教学班
        List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73YSY(unitId),Course.class);
        Set<String> courseIdSets = EntityUtils.getSet(courseList, Course::getId);
        for (String string : courseIdSets) {
        	List<TeacherAsessResult> tList=new ArrayList<TeacherAsessResult>();
        	List<ClassTeaching> classTeachingList=SUtils.dt(classTeachingRemoteService.findByClassIdsSubjectIds(unitId,acadyear,semester,
        			classIds.toArray(new String[0]),new String[]{string},false), new TR<List<ClassTeaching>>(){});//取单科行政班
        	if(CollectionUtils.isNotEmpty(classTeachingList)){
        		Set<String> classIdSets = EntityUtils.getSet(classTeachingList, ClassTeaching::getClassId);
        		Map<String, String> clsMap = new HashMap<String, String>();
    			List<Clazz> clsList3 = SUtils.dt(classRemoteService.findClassListByIds(classIdSets.toArray(new String[0])), new TR<List<Clazz>>() {});
    			for(Clazz cls : clsList3){
    				clsMap.put(cls.getId(), cls.getClassNameDynamic());
    			}
        		List<Student> stuList =  SUtils.dt(studentRemoteService.findByClassIds(classIdSets.toArray(new String[0])), new TR<List<Student>>() {});
        		Map<String,Set<String>> stuMap=new HashMap<String,Set<String>>();
        		for (Student student : stuList) {
        			if(!stuMap.containsKey(student.getClassId())){
        				stuMap.put(student.getClassId(), new HashSet<String>());
        			}
        			stuMap.get(student.getClassId()).add(student.getId());
        		}
        		Map<String,ClassTeaching> classTeachingMap=EntityUtils.getMap(classTeachingList, ClassTeaching::getClassId);
        		Set<String> teacherIdSet = EntityUtils.getSet(classTeachingList, ClassTeaching::getTeacherId);
        		Map<String, String> teacherMap = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIdSet.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
        		if(MapUtils.isNotEmpty(stuMap)){
        			for (String classId : classIdSets) {
        				TeacherAsessResult asessResult=new TeacherAsessResult();
        				
        				if(stuMap.containsKey(classId)){
        					Set<String> studeSet=stuMap.get(classId);
        					List<TeacherasessConvertResult> teacherasessConvertResultNews=teacherasessConvertResultService.findByUnitIdAndConvertIdAndSubjectIdAndStudentIdIn(unitId, teacherAsess.getConvertId(), string, studeSet.toArray(new String[0]));
        					List<TeacherasessConvertResult> teacherasessConvertResultOlds=teacherasessConvertResultService.findByUnitIdAndConvertIdAndSubjectIdAndStudentIdIn(unitId, teacherAsess.getReferConvertId(), string, studeSet.toArray(new String[0]));
        					
        					int i=getUpNum(studeSet, teacherasessConvertResultNews, teacherasessConvertResultOlds);//班级进步人数
        					
        					int studentSize=teacherasessConvertResultNews.size();//本次行政班有效人数
        					int studentSizeOld=teacherasessConvertResultOlds.size();//原始行政班有效人数
        					int oldGankTotal=0;//年级名次之和
        					int newGankTotal=0;
        					for (TeacherasessConvertResult teacherasessConvertResult : teacherasessConvertResultNews) {
        						newGankTotal+=teacherasessConvertResult.getRank();
        					}
        					for (TeacherasessConvertResult teacherasessConvertResult : teacherasessConvertResultOlds) {
        						oldGankTotal+=teacherasessConvertResult.getRank();
        					}
        					int old=0;
        					if(studentSizeOld!=0){
        						old=oldGankTotal/studentSizeOld;
        					}
        					int news=0;
        					float upScale=0.0f;
                			if(studentSize!=0){
                				news=newGankTotal/studentSize;
                				upScale=(float)(i*100/studentSize);
                			}
        					asessResult.setId(UuidUtils.generateUuid());
        					asessResult.setUnitId(unitId);
        					asessResult.setAssessId(teacherAsessId);
        					asessResult.setClassId(classId);
        					asessResult.setClassType("1");//行政班
        					asessResult.setClassName(clsMap.get(classId));
        					asessResult.setSubjcetId(string);
        					asessResult.setTeacherId(classTeachingMap.get(classId).getTeacherId());
        					asessResult.setTeacherName(teacherMap.get(asessResult.getTeacherId()));
        					asessResult.setUpStuNum(i);//进步人数
    						asessResult.setUpScale(upScale);
        					asessResult.setConvertParam(news);
        					asessResult.setReferConvertParam(old);
        					asessResult.setAsessScore(old-news);
        					tList.add(asessResult);
        				}
        			}
        		}
        	}
        	List<TeachClass> teaClaList = SUtils.dt(teachClassRemoteService.findTeachClassList(unitId,acadyear,semester,
        			string,new String[]{teacherAsess.getGradeId()},true), TeachClass.class);//取单科教学班
        	if(CollectionUtils.isNotEmpty(teaClaList)){
        		Set<String> teaClaSets = EntityUtils.getSet(teaClaList, TeachClass::getId);
        		List<TeachClassStu> teachStuList = SUtils.dt(teachClassStuRemoteService.findByClassIds(teaClaSets.toArray(new String[0])), TeachClassStu.class);
        		Map<String,Set<String>> stuMap=new HashMap<String,Set<String>>();
        		for (TeachClassStu student : teachStuList) {
        			if(!stuMap.containsKey(student.getClassId())){
        				Set<String> students=new HashSet<String>();
        				students.add(student.getStudentId());
        				stuMap.put(student.getClassId(), students);
        			}else{
        				Set<String> students=stuMap.get(student.getClassId());
        				students.add(student.getStudentId());
        				stuMap.put(student.getClassId(), students);
        			}
        		}
        		Set<String> teacherIdSet = EntityUtils.getSet(teaClaList, TeachClass::getTeacherId);
        		Map<String, String> teacherMap = SUtils.dt(teacherRemoteService.findPartByTeacher(teacherIdSet.toArray(new String[0])),new TypeReference<Map<String, String>>(){});
        		for (TeachClass teachClass : teaClaList) {
        			TeacherAsessResult asessResult=new TeacherAsessResult();
        			Set<String> studeSet=stuMap.get(teachClass.getId());
        			List<TeacherasessConvertResult> teacherasessConvertResultNews = new ArrayList<>();
        			List<TeacherasessConvertResult> teacherasessConvertResultOlds = new ArrayList<>();
        			if(CollectionUtils.isNotEmpty(studeSet)) {
	        			teacherasessConvertResultNews=teacherasessConvertResultService.findByUnitIdAndConvertIdAndSubjectIdAndStudentIdIn(unitId, teacherAsess.getConvertId(), string, studeSet.toArray(new String[0]));
	        			teacherasessConvertResultOlds=teacherasessConvertResultService.findByUnitIdAndConvertIdAndSubjectIdAndStudentIdIn(unitId, teacherAsess.getReferConvertId(), string, studeSet.toArray(new String[0]));
        			}
        			int i=getUpNum(studeSet, teacherasessConvertResultNews, teacherasessConvertResultOlds);//班级进步人数
        			
        			int studentSize=teacherasessConvertResultNews.size();//本次行政班有效人数
        			int studentSizeOld=teacherasessConvertResultOlds.size();//参考行政班有效人数
        			int oldGankTotal=0;//年级名次之和
        			int newGankTotal=0;
        			for (TeacherasessConvertResult teacherasessConvertResult : teacherasessConvertResultNews) {
        				newGankTotal+=teacherasessConvertResult.getRank();
        			}
        			for (TeacherasessConvertResult teacherasessConvertResult : teacherasessConvertResultOlds) {
        				oldGankTotal+=teacherasessConvertResult.getRank();
        			}
        			int old=0;
        			if(studentSizeOld!=0){
        				old=oldGankTotal/studentSizeOld;
        			}
        			int news=0;
        			float upScale=0.0f;
        			if(studentSize!=0){
        				news=newGankTotal/studentSize;
        				upScale=(float)(i*100/studentSize);
        			}
        			asessResult.setId(UuidUtils.generateUuid());
        			asessResult.setUnitId(unitId);
        			asessResult.setAssessId(teacherAsessId);
        			asessResult.setClassId(teachClass.getId());
        			asessResult.setClassType("2");//教学班
        			asessResult.setClassName(teachClass.getName());
        			asessResult.setSubjcetId(string);
        			asessResult.setTeacherId(teachClass.getTeacherId());
        			asessResult.setTeacherName(teacherMap.get(asessResult.getTeacherId()));
        			asessResult.setConvertParam(news);
        			asessResult.setReferConvertParam(old);
        			asessResult.setUpStuNum(i);//进步人数
					asessResult.setUpScale(upScale);
        			asessResult.setAsessScore(old-news);
        			tList.add(asessResult);
				}
        	}
            setAllRank(tList);
            teacherAsessResults.addAll(tList);
		}
        
        //TODO
        statClassABData(teacherAsessId);
        if(CollectionUtils.isNotEmpty(teacherAsessResults)){
        	teacherasessResultService.deleteByAssessId(unitId, new String[]{teacherAsessId});
        	teacherasessResultService.saveAll(teacherAsessResults.toArray(new TeacherAsessResult[0]));
        	this.updateforStatus("2", teacherAsessId);
        }
        
	}
	
	
	public void setTeacherasessSetService(TeacherasessSetService teacherasessSetService) {
		this.teacherasessSetService = teacherasessSetService;
	}

	public void setTeacherasessLineService(TeacherasessLineService teacherasessLineService) {
		this.teacherasessLineService = teacherasessLineService;
	}

	public void setTeacherasessRankService(TeacherasessRankService teacherasessRankService) {
		this.teacherasessRankService = teacherasessRankService;
	}

	public void setTeacherasessCheckService(TeacherasessCheckService teacherasessCheckService) {
		this.teacherasessCheckService = teacherasessCheckService;
	}

	/**
	 * 获取班级进步人数
	 * @param studeSet
	 * @param newMap
	 * @param oldMap
	 * @return
	 */
	public int getUpNum(Set<String> studeSet,List<TeacherasessConvertResult> teacherasessConvertResultNews,
			List<TeacherasessConvertResult> teacherasessConvertResultOlds){
		Map<String,TeacherasessConvertResult> newMap=EntityUtils.getMap(teacherasessConvertResultNews, TeacherasessConvertResult::getStudentId);
		Map<String,TeacherasessConvertResult> oldMap=EntityUtils.getMap(teacherasessConvertResultOlds, TeacherasessConvertResult::getStudentId);
		int i=0;//班级进步人数
		for (String studentId : studeSet) {
			int newG=0;
			int old=0;
			if(MapUtils.isNotEmpty(oldMap)&&oldMap.containsKey(studentId)){
				old=oldMap.get(studentId).getRank();
			}
			if(MapUtils.isNotEmpty(newMap)&&newMap.containsKey(studentId)){
				newG=newMap.get(studentId).getRank();
			}
			if(old!=0&&newG!=0&&newG<old){
				i++;
			}
		}
		return i;
	}
	/**
	 * 获取3个排名 rank
	 * @param list
	 */
	public void setAllRank(List<TeacherAsessResult> list){
		//总名次排名表
		list.sort(new Comparator<TeacherAsessResult>() {
            @Override
            public int compare(TeacherAsessResult s1, TeacherAsessResult s2) {
                return -Double.compare(s1.getAsessScore(), s2.getAsessScore());
            }
        });
		int index = 0;// 排名
        float lastScore = -1;// 最近一次的分
 
        for (int i = 0; i < list.size(); i++) {
        	TeacherAsessResult s = list.get(i);
            if (Float.compare(lastScore, s.getAsessScore())!=0) { // 如果考核分和上一名的考核分不相同,那么排名+1
                lastScore = s.getAsessScore();
                index=i+1;
            }
           s.setRank(index);
        }
        //进步人次排名表
        list.sort(new Comparator<TeacherAsessResult>(){
			@Override
			public int compare(TeacherAsessResult o1, TeacherAsessResult o2) {
				return -Float.compare(o1.getUpScale(), o2.getUpScale());
			}
        });
        index = 0;//重置
        float lastUpScale = -1;
        for (int i = 0; i < list.size(); i++) {
        	TeacherAsessResult s = list.get(i);
            if (Float.compare(lastUpScale, s.getUpScale())!=0) { // 如果考核分和上一名的考核分不相同,那么排名+1
            	lastUpScale = s.getUpScale();
                index=i+1;
            }
            s.setUpScaleRank(index);
        }
        //最终考核报名表
        for (TeacherAsessResult teacherAsessResult : list) {
        	teacherAsessResult.setScore((float)(teacherAsessResult.getRank()*0.6)+(float)(teacherAsessResult.getUpScaleRank()*0.4));
		}
        list.sort(new Comparator<TeacherAsessResult>(){
			@Override
			public int compare(TeacherAsessResult o1, TeacherAsessResult o2) {
				return Float.compare(o1.getScore(), o2.getScore());
			}
        });
        index = 0;//重置
        lastScore = -1;
        for (int i = 0; i < list.size(); i++) {
        	TeacherAsessResult s = list.get(i);
            if (Float.compare(lastScore, s.getScore())!=0) { // 如果考核分和上一名的考核分不相同,那么排名+1
            	lastScore = s.getScore();
                index=i+1;
            }
            s.setScoreRank(index);
        }
	}
	
	@Override
	public void updateforStatus(String status, String id) {
		teacherasessDao.updateforStatus(status, id);
	}

	@Override
	protected BaseJpaRepositoryDao<TeacherAsess, String> getJpaDao() {
		return teacherasessDao;
	}

	@Override
	protected Class<TeacherAsess> getEntityClass() {
		return TeacherAsess.class;
	}

}
