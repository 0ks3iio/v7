//package net.zdsoft.scoremanage.data.service.impl;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import net.zdsoft.basedata.constant.SpagoBIConstants;
//import net.zdsoft.basedata.constant.SystemIniConstants;
//import net.zdsoft.basedata.entity.Clazz;
//import net.zdsoft.basedata.entity.Grade;
//import net.zdsoft.basedata.entity.Unit;
//import net.zdsoft.basedata.job.AbstractTaskJobService;
//import net.zdsoft.basedata.job.TaskErrorException;
//import net.zdsoft.basedata.job.TaskJobDataParam;
//import net.zdsoft.basedata.job.TaskJobReply;
//import net.zdsoft.basedata.remote.service.ClassRemoteService;
//import net.zdsoft.basedata.remote.service.GradeRemoteService;
//import net.zdsoft.basedata.remote.service.UnitRemoteService;
//import net.zdsoft.framework.config.SpagoBIRemote;
//import net.zdsoft.framework.entity.TR;
//import net.zdsoft.framework.utils.EntityUtils;
//import net.zdsoft.framework.utils.SUtils;
//import net.zdsoft.framework.utils.UrlUtils;
//import net.zdsoft.framework.utils.UuidUtils;
//import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
//import net.zdsoft.scoremanage.data.entity.ExamInfo;
//import net.zdsoft.scoremanage.data.entity.Filtration;
//import net.zdsoft.scoremanage.data.entity.JoinexamschInfo;
//import net.zdsoft.scoremanage.data.entity.ScoreConversion;
//import net.zdsoft.scoremanage.data.entity.ScoreInfo;
//import net.zdsoft.scoremanage.data.entity.ScoreStatistic;
//import net.zdsoft.scoremanage.data.service.ExamInfoService;
//import net.zdsoft.scoremanage.data.service.FiltrationService;
//import net.zdsoft.scoremanage.data.service.JoinexamschInfoService;
//import net.zdsoft.scoremanage.data.service.ScoreConversionService;
//import net.zdsoft.scoremanage.data.service.ScoreInfoService;
//import net.zdsoft.scoremanage.data.service.ScoreStatisticService;
//import net.zdsoft.system.remote.service.SystemIniRemoteService;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.alibaba.fastjson.JSONObject;
//
//@Service
//public class ScoreStatisticJobServiceImpl extends AbstractTaskJobService{
//
//	@Autowired
//	private ExamInfoService examInfoService;
//	@Autowired
//	private ScoreStatisticService scoreStatisticService;
//	@Autowired
//	private ScoreInfoService scoreInfoService;
//	@Autowired
//	private FiltrationService filtrationService;
//	@Autowired
//	private UnitRemoteService unitService;
//	@Autowired
//	private JoinexamschInfoService joinexamschInfoService;
//	@Autowired
//	private ClassRemoteService classService;
//	@Autowired
//	private GradeRemoteService gradeService;
//	@Autowired
//	private SystemIniRemoteService systemIniRemoteService;
//	@Autowired
//	private ScoreConversionService scoreConversionService;
//
//	@Override
//	public void jobDatas(TaskJobDataParam param, TaskJobReply reply) throws TaskErrorException {
//		try{
//			Map<String, String> customParamMap = param.getCustomParamMap();
//			String unitId = customParamMap.get("unitId");
//			String examId = customParamMap.get("examId");
//			ExamInfo examInfo = examInfoService.findOne(examId);
//			if(examInfo == null){
//				reply.addActionError("考试已不存在！");
//				return;
//			}
//			scoreStatisticService.deleteByExamId(examInfo.getId());
//
//			Set<String> unitIds = new HashSet<String>();
//			if(ScoreDataConstants.TKLX_0.equals(examInfo.getExamUeType())){
//				//校内考试
//				unitIds.add(unitId);
//			}else if(ScoreDataConstants.TKLX_1.equals(examInfo.getExamUeType()) ){
//				//直属
//				List<Unit> unitList = SUtils.dt(unitService.findDirectUnits(unitId, Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>(){});
//				for (Unit unit : unitList) {
//					unitIds.add(unit.getId());
//				}
//			}else if(ScoreDataConstants.TKLX_3.equals(examInfo.getExamUeType())){
//				//校校
//				List<JoinexamschInfo> findByExamInfoId = joinexamschInfoService.findByExamInfoId(examInfo.getId());
//				for (JoinexamschInfo item : findByExamInfoId) {
//					unitIds.add(item.getSchoolId());
//				}
//			}else{
//				//未找到
//
//			}
//			if(CollectionUtils.isEmpty(unitIds)){
//				reply.addActionError("未找到单位");
//				return;
//			}
//			List<ScoreConversion> scoreConversionList = null;
//			if("1".equals(examInfo.getIsgkExamType())){
//				//如果是73考试则计算等级赋分制度
//				scoreConversionList = scoreConversionService.findListByExamId(examId, true);
//			}
//			List<Filtration> fttList = filtrationService.findListBySchoolIds(examId, unitIds.toArray(new String[0]));//找不计入统计的学生
//			Set<String> studentIds = new HashSet<String>();
//			for (Filtration item : fttList) {
//				studentIds.add(item.getStudentId());
//			}
//			//注：目前只适合统计分数类型，等第不统计
//			//根据科目分组，分数降序--统考范围
////			List<ScoreInfo> scoreInfoList = scoreInfoService.findListByExamId(examInfo.getId(), ScoreDataConstants.ACHI_SCORE,
////					(examInfo.getIsTotalScore() != null && Integer.valueOf(examInfo.getIsTotalScore()) == 0?"0":null), studentIds.toArray(new String[0]));
//			List<ScoreInfo> scoreInfoList = scoreInfoService.findListByExamId(examInfo.getId(), ScoreDataConstants.ACHI_SCORE,
//					null, studentIds.toArray(new String[0]));
//			//根据总分降序--统考范围
////			List<ScoreInfo> sifList = scoreInfoService.findScoreInfoListRanking(examInfo.getId(), (examInfo.getIsTotalScore() != null && Integer.valueOf(examInfo.getIsTotalScore())== 0?"0":null),
////					studentIds.toArray(new String[0]));
//			List<ScoreInfo> sifList = scoreInfoService.findScoreInfoListRanking(examInfo.getId(), null,
//			studentIds.toArray(new String[0]));
//			Set<String> classIds = new HashSet<String>();
//			for (ScoreInfo item : sifList) {
//				classIds.add(item.getClassId());
//			}
//			List<Clazz> findByIdIn = SUtils.dt(classService.findListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>(){});
//			Set<String> gradeIds = new HashSet<String>();
//			for (Clazz clazz : findByIdIn) {
//				gradeIds.add(clazz.getGradeId());
//			}
//			List<Grade> grades = SUtils.dt(gradeService.findListByIds(gradeIds.toArray(new String[0])), new TR<List<Grade>>(){});
//			Map<String, Grade> findByIdInMap = EntityUtils.getMap(grades, "id");
//			Map<String,String> classGradeCode = new HashMap<String, String>();//key classId ----value gradeCode
//			for (Clazz item : findByIdIn) {
//				classGradeCode.put(item.getId(), findByIdInMap.get(item.getGradeId()).getGradeCode());
//			}
//			//根据科目分组，分数降序
//			Map<String,List<ScoreInfo>> scoreInfoMap = new HashMap<String,List<ScoreInfo>>();//key unitId
//			//
//			Map<String,Map<String,ScoreInfo>> sifMap = new HashMap<String,Map<String,ScoreInfo>>();//key unitId studentId
//
//			//科目排名--班级范围
//			Map<String,Map<String,Map<String,Integer>>> courseMapType0 = new HashMap<String,Map<String,Map<String,Integer>>>();//key classId courseId studentId
//			//科目排名--学校范围
//			Map<String,Map<String,Map<String,Map<String,Integer>>>> courseMapType1 = new HashMap<String, Map<String,Map<String,Map<String,Integer>>>>();//key unitId gradeCode courseId studentId
//			//科目排名--统考范围
//			Map<String,Map<String,Map<String,Integer>>> courseMapType2 = new HashMap<String,Map<String,Map<String, Integer>>>();//key gradeCode courseId studentId
//			//科目排名--统考范围--为73计算赋分准备
//			Map<String,Map<String,List<ScoreInfo>>> courseMapType3 = new HashMap<String, Map<String,List<ScoreInfo>>>();
//
//			Map<String, Integer> key1Map = null;
//			Map<String,Map<String,Integer>> key2Map = null;
//			Map<String,Map<String,Map<String,Integer>>> key3Map = null;
//			Map<String,List<ScoreInfo>> key4Map = null;
//			List<ScoreInfo> linScoreList = null;
//			if(CollectionUtils.isNotEmpty(scoreInfoList)){
//				for (ScoreInfo item : scoreInfoList) {
//					List<ScoreInfo> list = scoreInfoMap.get(item.getUnitId());
//					if(list == null){
//						unitIds.add(item.getUnitId());
//						list = new ArrayList<ScoreInfo>();
//						scoreInfoMap.put(item.getUnitId(), list);
//					}
//					list.add(item);
//					//科目排名--班级范围
//					key2Map = courseMapType0.get(item.getClassId());
//					if(key2Map == null){
//						key2Map = new HashMap<String,Map<String,Integer>>();
//						courseMapType0.put(item.getClassId(), key2Map);
//						key1Map = new HashMap<String, Integer>();
//						key2Map.put(item.getSubjectId(), key1Map);
//						key1Map.put(item.getStudentId(), 1);
//					}else{
//						key1Map = key2Map.get(item.getSubjectId());
//						if(key1Map == null){
//							key1Map = new HashMap<String, Integer>();
//							key2Map.put(item.getSubjectId(), key1Map);
//							key1Map.put(item.getStudentId(), 1);
//						}else{
//							key1Map.put(item.getStudentId(), key1Map.size()+1);
//						}
//					}
//					//科目排名--学校范围
//					key3Map = courseMapType1.get(item.getUnitId());
//					if(key3Map == null){
//						key3Map = new HashMap<String,Map<String,Map<String,Integer>>>();
//						courseMapType1.put(item.getUnitId(), key3Map);
//						key2Map = new HashMap<String,Map<String,Integer>>();
//						key3Map.put(classGradeCode.get(item.getClassId()), key2Map);
//						key1Map = new HashMap<String, Integer>();
//						key2Map.put(item.getSubjectId(), key1Map);
//						key1Map.put(item.getStudentId(), 1);
//					}else{
//						key2Map = key3Map.get(classGradeCode.get(item.getClassId()));
//						if(key2Map == null){
//							key2Map = new HashMap<String, Map<String,Integer>>();
//							key3Map.put(classGradeCode.get(item.getClassId()), key2Map);
//							key1Map = new HashMap<String, Integer>();
//							key2Map.put(item.getSubjectId(), key1Map);
//							key1Map.put(item.getStudentId(), 1);
//						}else{
//							key1Map = key2Map.get(item.getSubjectId());
//							if(key1Map == null){
//								key1Map = new HashMap<String, Integer>();
//								key2Map.put(item.getSubjectId(), key1Map);
//								key1Map.put(item.getStudentId(), 1);
//							}else{
//								key1Map.put(item.getStudentId(), key1Map.size()+1);
//							}
//						}
//					}
//					//科目排名--统考范围
//					key2Map = courseMapType2.get(classGradeCode.get(item.getClassId()));
//					if(key2Map == null){
//						key2Map = new HashMap<String, Map<String,Integer>>();
//						courseMapType2.put(classGradeCode.get(item.getClassId()), key2Map);
//						key1Map = new HashMap<String, Integer>();
//						key2Map.put(item.getSubjectId(), key1Map);
//						key1Map.put(item.getStudentId(), 1);
//					}else{
//						key1Map = key2Map.get(item.getSubjectId());
//						if(key1Map == null){
//							key1Map = new HashMap<String, Integer>();
//							key2Map.put(item.getSubjectId(), key1Map);
//							key1Map.put(item.getStudentId(), 1);
//						}else{
//							key1Map.put(item.getStudentId(), key1Map.size()+1);
//						}
//					}
//					//73
//					key4Map = courseMapType3.get(classGradeCode.get(item.getClassId()));
//					if(key4Map == null){
//						key4Map = new HashMap<String, List<ScoreInfo>>();
//						courseMapType3.put(classGradeCode.get(item.getClassId()), key4Map);
//						linScoreList = new ArrayList<ScoreInfo>();
//						key4Map.put(item.getSubjectId(), linScoreList);
//						linScoreList.add(item);
//					}else{
//						linScoreList = key4Map.get(item.getSubjectId());
//						if(linScoreList == null){
//							linScoreList = new ArrayList<ScoreInfo>();
//							key4Map.put(item.getSubjectId(), linScoreList);
//						}
//						linScoreList.add(item);
//					}
//				}
//			}
//			List<ScoreInfo> saveScoreList = new ArrayList<ScoreInfo>();
//			if("1".equals(examInfo.getIsgkExamType())){
//				//如果是73考试则计算等级赋分制度
//				if(CollectionUtils.isNotEmpty(scoreConversionList)){
//					for(Map.Entry<String, Map<String, List<ScoreInfo>>> entry : courseMapType3.entrySet()){
//						key4Map = entry.getValue();
//						for(Map.Entry<String, List<ScoreInfo>> entry2 : key4Map.entrySet()){
//							linScoreList = entry2.getValue();
//							int countStu = linScoreList.size();
////							System.out.println(entry2.getKey()+","+countStu);
//							int oldNumStu = 0;
//							int numStu = 0;
//							for (ScoreConversion item : scoreConversionList) {
//								numStu += Math.round(countStu * (double)(item.getBalance()/100.0));
//								if(numStu == 0){
//									numStu+=1;
//								}
////								System.out.println(numStu);
//								if(countStu < numStu){
//									numStu = countStu;
//								}
//								for(int i = oldNumStu ; i < numStu ; i++){
//									int intScore = item.getScore().intValue();
////									System.out.println(linScoreList.get(i).getStudentId()+","+linScoreList.get(i).getSubjectId()+","+linScoreList.get(i).getScore());
//									if(item.getScore()-intScore == 0){
//										linScoreList.get(i).setToScore(String.valueOf(intScore));
//									}else{
//										linScoreList.get(i).setToScore(String.valueOf(item.getScore()));
//									}
//									saveScoreList.add(linScoreList.get(i));
//								}
//								oldNumStu = numStu;
//								if(numStu == countStu){
//									break;
//								}
//							}
//						}
//					}
//				}
//			}
//			//总分排名--班级范围
//			Map<String,Map<String,Integer>> allMapType0 = new HashMap<String, Map<String,Integer>>();//key classId studentId
//			//总分排名--学校范围
//			Map<String,Map<String,Map<String,Integer>>> allMapType1 = new HashMap<String, Map<String,Map<String,Integer>>>();//key unitId gradeCode studentId
//			//总分排名--统考范围
//			Map<String,Map<String,Integer>> allMapType2 = new HashMap<String,Map<String, Integer>>();//key gradeCode studentId
//
//			Map<String, ScoreInfo> sifLMap = null;
//			if(CollectionUtils.isNotEmpty(sifList)){
//				for (ScoreInfo item : sifList) {
//					sifLMap = sifMap.get(item.getUnitId());
//					if(sifLMap == null){
//						sifLMap = new HashMap<String, ScoreInfo>();
//						sifMap.put(item.getUnitId(), sifLMap);
//					}
//					sifLMap.put(item.getStudentId(), item);
//					//总分排名--班级范围
//					key1Map = allMapType0.get(item.getClassId());
//					if(key1Map == null){
//						key1Map = new HashMap<String, Integer>();
//						allMapType0.put(item.getClassId(), key1Map);
//						key1Map.put(item.getStudentId(), 1);
//					}else{
//						key1Map.put(item.getStudentId(), key1Map.size()+1);
//					}
//					//总分排名--学校范围
//					key2Map = allMapType1.get(item.getUnitId());
//					if(key2Map == null){
//						key2Map = new HashMap<String, Map<String,Integer>>();
//						allMapType1.put(item.getUnitId(), key2Map);
//						key1Map = new HashMap<String, Integer>();
//						key1Map.put(item.getStudentId(), 1);
//						key2Map.put(classGradeCode.get(item.getClassId()), key1Map);
//					}else{
//						key1Map = key2Map.get(classGradeCode.get(item.getClassId()));
//						if(key1Map == null){
//							key1Map = new HashMap<String, Integer>();
//							key2Map.put(classGradeCode.get(item.getClassId()), key1Map);
//							key1Map.put(item.getStudentId(), 1);
//						}else{
//							key1Map.put(item.getStudentId(), key1Map.size()+1);
//						}
//					}
//					//总分排名--统考范围
//					key1Map = allMapType2.get(classGradeCode.get(item.getClassId()));
//					if(key1Map == null){
//						key1Map = new HashMap<String, Integer>();
//						allMapType2.put(classGradeCode.get(item.getClassId()), key1Map);
//						key1Map.put(item.getStudentId(), 1);
//					}else{
//						key1Map.put(item.getStudentId(), key1Map.size()+1);
//					}
//				}
//			}
//			List<ScoreInfo> linList = null;
//			if(ScoreDataConstants.TKLX_0.equals(examInfo.getExamUeType())){
//				//校内考试
//				linList = scoreInfoMap.get(unitId);
//				sifLMap = sifMap.get(unitId);
//				key3Map = courseMapType1.get(unitId);
//				key2Map = allMapType1.get(unitId);
//				try{
//					doStatisticUnit(examInfo, classGradeCode, courseMapType0, courseMapType2, key2Map, key3Map, allMapType0, allMapType2, sifLMap,
//							linList);
//				}catch (Exception e) {
//					e.printStackTrace();
//				}
//			}else if(ScoreDataConstants.TKLX_1.equals(examInfo.getExamUeType()) || ScoreDataConstants.TKLX_3.equals(examInfo.getExamUeType())){
//				//直属 or 校校
//				for (String id : unitIds) {
//					linList = scoreInfoMap.get(id);
//					sifLMap = sifMap.get(id);
//					key3Map = courseMapType1.get(id);
//					key2Map = allMapType1.get(id);
//					try{
//						doStatisticUnit(examInfo, classGradeCode, courseMapType0, courseMapType2, key2Map, key3Map, allMapType0, allMapType2, sifLMap,
//								linList);
//					}catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}else{
//				//未找到
//
//			}
//			if(CollectionUtils.isNotEmpty(saveScoreList)){
//				scoreInfoService.saveAllEntitys(saveScoreList.toArray(new ScoreInfo[0]));
//			}
//			try{
//				System.out.println("准备启动kettle转换");
//				long currentTimeMillis = System.currentTimeMillis();
//				String biUrlCol = systemIniRemoteService.findValue(SystemIniConstants.BI_URL);
//				String biUrl = biUrlCol;
////				String biUrl = null;
////				if(StringUtils.isNotBlank(biUrlCol)){
////					String[] split = biUrlCol.split(",");
////					biUrl = split[0];
////				}
//				String generateUuid = UuidUtils.generateUuid();
//				String controlKettleUrl = SpagoBIRemote.getControlKettleUrl(generateUuid,biUrl,SpagoBIConstants.JOB_SCOREDATA,SpagoBIConstants.OPERATE_START, null);
//				System.out.println(controlKettleUrl);
//				String readContent = UrlUtils.readContent(controlKettleUrl);
//				JSONObject parseObject = JSONObject.parseObject(readContent);
//				System.out.println(parseObject.getString(SpagoBIConstants.RETURN_MSG));
//				if(parseObject.getBooleanValue(SpagoBIConstants.RETURN_SUCCESS)){
//					try{
//						//模拟同步
//						Thread.sleep(500);
//						String queryUrl = SpagoBIRemote.queryControlKettleStatus(generateUuid,biUrl,SpagoBIConstants.JOB_SCOREDATA);
//						while(true){
//							readContent = UrlUtils.readContent(queryUrl);
//							parseObject = JSONObject.parseObject(readContent);
//							System.out.println(parseObject.getString(SpagoBIConstants.RETURN_MSG));
//							if(parseObject.getBooleanValue(SpagoBIConstants.RETURN_SUCCESS)){
//								if(parseObject.getIntValue(SpagoBIConstants.RETURN_CODE)==0){
//									break;
//								}
//							}else{
//								System.out.println("kettle查询状态出错");
//								break;
//							}
//							Thread.sleep(1000);
//						}
//					}catch(Exception e){
//						e.printStackTrace();
//						System.out.println("kettle查询状态连接出错");
//					}
//				}else{
//					reply.addActionError("远程转换出错，请联系管理员");
//					System.out.println("kettle远程转换出错，请联系管理员");
//				}
//				System.out.println("转换耗时："+(System.currentTimeMillis()-currentTimeMillis));
//			}catch(Exception e){
//				e.printStackTrace();
//				reply.addActionError("远程转换连接出错，请联系管理员");
//			}
//			reply.addActionMessage("统计成功！");
//		}catch(Exception e){
//			e.printStackTrace();
//			throw new TaskErrorException("统计发生错误，请联系管理员");
//		}
//	}
//	private void doStatisticUnit(final ExamInfo examInfo, Map<String, String> classGradeCode,
//			Map<String, Map<String, Map<String, Integer>>> courseMapType0, Map<String, Map<String, Map<String, Integer>>> courseMapType2,
//			Map<String, Map<String, Integer>> key2Map, Map<String, Map<String, Map<String, Integer>>> key3Map,
//			Map<String, Map<String, Integer>> allMapType0, Map<String, Map<String, Integer>> allMapType2, Map<String, ScoreInfo> sifLMap,
//			List<ScoreInfo> linList) {
//		List<ScoreStatistic> finAddList = new ArrayList<ScoreStatistic>();
//		ScoreStatistic sst = null;
//		ScoreInfo scoreInfo = null;
//		if(linList!=null)
//		for (ScoreInfo item : linList) {
//			scoreInfo = sifLMap.get(item.getStudentId());
//			if(scoreInfo != null){
//				sst = new  ScoreStatistic();
//				sst.setAcadyear(item.getAcadyear());
//				sst.setSemester(item.getSemester());
//				sst.setUnitId(item.getUnitId());
//				sst.setExamId(item.getExamId());
//				sst.setSubjectId(item.getSubjectId());
//				sst.setStudentId(item.getStudentId());
//				sst.setClassId(item.getClassId());
//				sst.setTeachClassId(item.getTeachClassId());
//				sst.setScoreType(item.getScoreStatus());
//				sst.setScore(item.getScore());
//				sst.setAllScore(scoreInfo.getAllScore());
//			}
//			if(sst!=null){
//				//班级范围
//				sst = EntityUtils.copyProperties(sst, ScoreStatistic.class);
//				sst.setCourseRanking(courseMapType0.get(sst.getClassId()).get(sst.getSubjectId()).get(sst.getStudentId()));
//				sst.setAllRanking(allMapType0.get(sst.getClassId()).get(sst.getStudentId()));
//				sst.setType(ScoreDataConstants.STATISTIC_TYPE0);
//				finAddList.add(sst);
//				//学校范围
//				sst = EntityUtils.copyProperties(sst, ScoreStatistic.class);
//				sst.setCourseRanking(key3Map.get(classGradeCode.get(sst.getClassId())).get(sst.getSubjectId()).get(sst.getStudentId()));
//				sst.setAllRanking(key2Map.get(classGradeCode.get(sst.getClassId())).get(sst.getStudentId()));
//				sst.setType(ScoreDataConstants.STATISTIC_TYPE1);
//				finAddList.add(sst);
//			}
//			if(ScoreDataConstants.TKLX_0.equals(examInfo.getExamUeType())){
//				//校内考试 -- 不存 统考范围的数据数据
//			}else if(ScoreDataConstants.TKLX_1.equals(examInfo.getExamUeType()) || ScoreDataConstants.TKLX_3.equals(examInfo.getExamUeType())){
//				//直属 or 校校
//				//统考范围
//				if(sst!=null){
//					sst = EntityUtils.copyProperties(sst, ScoreStatistic.class);
//					sst.setCourseRanking(courseMapType2.get(classGradeCode.get(sst.getClassId())).get(sst.getSubjectId()).get(sst.getStudentId()));
//					sst.setAllRanking(allMapType2.get(classGradeCode.get(sst.getClassId())).get(sst.getStudentId()));
//					sst.setType(ScoreDataConstants.STATISTIC_TYPE2);
//					finAddList.add(sst);
//				}
//			}
//		}
//
//		if(CollectionUtils.isNotEmpty(finAddList)){
//			synchronized (this) {
//				scoreStatisticService.saveAllEntitys(finAddList.toArray(new ScoreStatistic[0]));
//			}
//		}
//	}
//
//}
