package net.zdsoft.newgkelective.data.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.NewGkGroupDto;
import net.zdsoft.newgkelective.data.dto.StudentResultDto;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRange;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRangeEx;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain.SectioningSolution;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain.Student;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain.StudentCourse;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain.TakeCourse;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain.TimeSlot;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.solver.SectioningApp;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.shuffling.ShufflingApp;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.shuffling.ShufflingAppDto;
import net.zdsoft.newgkelective.data.optaplanner.common.CalculateSections;
import net.zdsoft.newgkelective.data.optaplanner2.domain.ArrangeCapacityRange;
import net.zdsoft.newgkelective.data.optaplanner2.dto.StudentSubjectDto;
import net.zdsoft.newgkelective.data.service.NewGKStudentRangeExService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 一个分班过程一个线程 
 * @author zhouyz
 * 
 * 1、全固定模式    A人工安排  B混合安排(a:学考安排 b:组合班生成行政班) 
 * 2、半固定模式   A、B混排，先排A(2+x中x与0+3中的3A)再排B---存在一个问题2+x与0+3是不是一起排
 * 3、单科分层  A分层 B不分层---这块可以加上行政班课程单科分层
 * 4、全手动  A人工安排  B混合安排(a:学考安排 b:组合班生成行政班)
 * 5、文理(语数英一起,分层)
 * 6、文理(语数英区分,分层)
 */
@Controller
@RequestMapping("/newgkelective/BathDivide/{divideId}")
public class NewGkElectiveDivideAndArrayAction extends
		NewGkElectiveDivideCommonAction {

	@Autowired
	private NewGKStudentRangeExService newGKStudentRangeExService;
	
	//用于是否走新算法
	private static boolean isChooseNewFunction=true;
	//莱州中学模式 单科分层需要重组行政班
	private static boolean isLZZX=false;
	//设置redis缓存时间 一般不会超过1个小时
	private static int TIME_ONE_HOUR=RedisUtils.TIME_ONE_HOUR;
	/**
	 * 
	 * @param divideId
	 * @param arrNum 单科分层 之前莱州中学模式 ，需要合并功能 所以需要随机开设行政班 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/openClassArrange/saveBath")
	@ControllerInfo(value = "单科开班")
	public String singleMain(@PathVariable String divideId,String arrNum) {

		final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_" + divideId;
		final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_"
				+ divideId + "_mess";
		JSONObject on = new JSONObject();
		final NewGkDivide gkDivide = newGkDivideService.findById(divideId);
		
		if (gkDivide == null) {
			on.put("stat", "error");
			on.put("message", "该分班方案不存在");
			RedisUtils.del(new String[] { key, keyMess });
			return JSON.toJSONString(on);
		}
		if (NewGkElectiveConstant.IF_1.equals(gkDivide.getStat())) {
			on.put("stat", "success");
			on.put("message", "已经分班成功");
			RedisUtils.del(new String[] { key,keyMess });
			return JSON.toJSONString(on);
		}

		// 判断分班 状态
		if (RedisUtils.get(key) == null) {
			//开始排班--不会超过一个小时
			RedisUtils.set(key, "start",TIME_ONE_HOUR);
			RedisUtils.set(keyMess, "进行中",TIME_ONE_HOUR);
		} else {
			on.put("stat", RedisUtils.get(key));
			on.put("message", RedisUtils.get(keyMess));
			if ("success".equals(RedisUtils.get(key))
					|| "error".equals(RedisUtils.get(key))) {
				if("success".equals(RedisUtils.get(key))) {
					RedisUtils.set(key, "success",TIME_ONE_HOUR);
					RedisUtils.set(keyMess, "分班成功",TIME_ONE_HOUR);
					newGkDivideService.updateStat(divideId,
							NewGkElectiveConstant.IF_1);
				}
				RedisUtils.del(new String[] { key, keyMess });
			} 

			return JSON.toJSONString(on);
		}
		

		/**
		 * 分班下选考，学考科目数据
		 */
		List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService
				.findByDivideIdAndSubjectTypeIn(divideId, new String[] {
						NewGkElectiveConstant.SUBJECT_TYPE_A,
						NewGkElectiveConstant.SUBJECT_TYPE_B });
		if (CollectionUtils.isEmpty(openSubjectList)) {
			// 不用分班成功--分班成功
			newGkDivideService.updateStat(divideId, NewGkElectiveConstant.IF_1);
			on.put("stat", "success");
			on.put("message", "没有选考，学考数科目需要分班");
			RedisUtils.del(new String[] { key, keyMess });
			return JSON.toJSONString(on);
		}
		NewGkChoice gkChoice = newGkChoiceService.findOne(gkDivide
				.getChoiceId());
		if (gkChoice == null) {
			on.put("stat", "error");
			on.put("message", "该方案的选课数据不存在");
			RedisUtils.del(new String[] { key,keyMess });
			return JSON.toJSONString(on);
		}
		
		/**----------------------以上验证数据有无-------------------------------------**/
		
		List<String> subjectAIds = new ArrayList<String>();
		List<String> subjectBIds = new ArrayList<String>();

		for (NewGkOpenSubject openSubject : openSubjectList) {
			if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(openSubject
					.getSubjectType())) {
				subjectAIds.add(openSubject.getSubjectId());
			} else {
				subjectBIds.add(openSubject.getSubjectId());
			}
		}
		List<StudentResultDto> stuChooselist = newGkChoResultService
				.findGradeIdList(gkDivide.getUnitId(),gkDivide.getGradeId(),gkDivide.getChoiceId(), null);
		if (CollectionUtils.isEmpty(stuChooselist)) {
			// 不用分班成功--分班成功
			newGkDivideService.updateStat(divideId, NewGkElectiveConstant.IF_1);
			on.put("stat", "success");
			on.put("message", "没有找到学生选课数据，无需分班");
			RedisUtils.del(new String[] { key, keyMess });
			return JSON.toJSONString(on);
		}
		
		
		try {
			int bathA = gkDivide.getBatchCountTypea()==null?3:gkDivide.getBatchCountTypea();
			int bathB = gkDivide.getBatchCountTypeb()==null?4:gkDivide.getBatchCountTypeb(); 
			
			// 单科分层模式 A分层
			if (NewGkElectiveConstant.DIVIDE_TYPE_05.equals(gkDivide
					.getOpenType())) {
				if(isLZZX) {
					if(StringUtils.isNotBlank(arrNum)) {
						//随机分配学生到行政班--同时还有混合班
						int classnum = Integer.parseInt(arrNum);
						//stuChooselist
						String refScoreId=gkDivide.getReferScoreId();
						if(StringUtils.isBlank(refScoreId)){
							refScoreId=newGkReferScoreService.findDefaultIdByGradeId(gkChoice.getUnitId(), gkChoice.getGradeId());
						}
						
						try {
							autoOpenXZB(gkDivide, classnum, refScoreId, stuChooselist);
						} catch (Exception e) {
							on.put("stat", "error");
							on.put("message", "分班行政班失败");
							RedisUtils.del(new String[] { key,keyMess });
							return JSON.toJSONString(on);
						}
					}
				}

				autoSingleLayer2(gkDivide, subjectAIds, subjectBIds,
						stuChooselist, true, false, bathA, bathB);		
				//增加数学
//				List<String> subjectOIds=new ArrayList<>();
//				subjectOIds.add("00000000000000000000000000000007");
//				subjectOIds.add("00000000000000000000000000000008");
//				subjectOIds.add("00000000000000000000000000000009");
//				//autoSingleLayer2ContatinXZBInA(gkDivide, subjectAIds, subjectBIds, subjectOIds, stuChooselist, true, false, bathA, bathB);
//				autoSingleLayer2ContatinXZB(gkDivide, subjectAIds, subjectBIds, subjectOIds, stuChooselist, true, false, bathA, bathB);

			} else if (NewGkElectiveConstant.DIVIDE_TYPE_01.equals(gkDivide
					.getOpenType())) {
				// 全固定模式
				if(bathA!=1){
					System.out.println("选考时间点时间应设置为1，请联系管理员");
				}
				autoCombination(gkDivide, subjectAIds, subjectBIds,
						stuChooselist, bathA, bathB);
				

			} else if (NewGkElectiveConstant.DIVIDE_TYPE_02.equals(gkDivide
					.getOpenType())) {
				//半固定
				autoCombination2(gkDivide, subjectAIds, subjectBIds,
						stuChooselist, bathA, bathB);
			}else{
				on.put("stat", "error");
				on.put("message", "参数错误，请重新操作");
				RedisUtils.del(new String[] { key,keyMess });
				return JSON.toJSONString(on);
			}
		} catch (Exception e) {
			e.printStackTrace();
			on.put("stat", "error");
			on.put("message", "失败");
			RedisUtils.del(new String[] { key, keyMess });
			return error("保存失败！" + e.getMessage());
		}

		return JSON.toJSONString(on);

	}
	
	/**
	 * 单科分层 先自动分行政班
	 * @param divideId
	 * @param xzbNum
	 * @param refScoreId
	 * @param stulist
	 */
	private void autoOpenXZB(NewGkDivide divide,int xzbNum,String refScoreId,List<StudentResultDto> stulist) {
		// 根据先性别平均 后总成绩分数平均
		// 学生(每班学生)
		List<String>[] array =autoStuIdToXzbId(refScoreId,stulist, xzbNum);
		// 新增班级
		List<NewGkClassStudent> insertStudentList = new ArrayList<NewGkClassStudent>();
		List<NewGkDivideClass> insertClassList = new ArrayList<NewGkDivideClass>();
		NewGkDivideClass newGkDivideClass;
		NewGkClassStudent newGkClassStudent;
		int k = 1;
		for (int i = 0; i < array.length; i++) {
			if (CollectionUtils.isNotEmpty(array[i])) {
				// 新增行政班
				newGkDivideClass =initXzbClass(divide.getId(),k);
				k++;
				insertClassList.add(newGkDivideClass);
				for (String stuId : array[i]) {
					newGkClassStudent=initClassStudent(divide.getUnitId(), divide.getId(), newGkDivideClass.getId(), stuId);
					insertStudentList.add(newGkClassStudent);
				}
			}
		}
		// 考虑班级学生重复：理论上新增班级 不会出现重复
		List<NewGkDivideClass> ll = newGkDivideClassService.findClassBySubjectIds(divide.getUnitId(), divide.getId(), NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_1, null, false);
		String[] delClassId=null;
		if(CollectionUtils.isNotEmpty(ll)) {
			delClassId=EntityUtils.getSet(ll, NewGkDivideClass::getId).toArray(new String[] {});
		}
		newGkDivideClassService.saveAllList(divide.getUnitId(), divide.getId(),
				delClassId, insertClassList, insertStudentList, false);
	}

	
	/**
	 * 
	 * @param divideId
	 * @param rangeMap key subjectId+bestType+subjectType
	 */
	private void findRange(String divideId,
			Map<String, Map<String, ArrangeCapacityRange>> rangeMap) {
		List<NewGKStudentRangeEx> rangeExList = newGKStudentRangeExService
				.findByDivideId(divideId);
		if (CollectionUtils.isNotEmpty(rangeExList)) {
			for (NewGKStudentRangeEx rangeEx : rangeExList) {
				ArrangeCapacityRange range = new ArrangeCapacityRange();
				range.setClassNum(rangeEx.getClassNum());	
				range.setAvgNum((rangeEx.getMaximum()+rangeEx.getLeastNum())/2);
				range.setFolNum((rangeEx.getMaximum()-rangeEx.getLeastNum())/2);
				range.setMaxCapacity(rangeEx.getMaximum());
				range.setMinCapacity(rangeEx.getLeastNum());
				
				if (!rangeMap.containsKey(rangeEx.getSubjectType())) {
					rangeMap.put(rangeEx.getSubjectType(),
							new HashMap<String, ArrangeCapacityRange>());
				}
				if (StringUtils.isNotBlank(rangeEx.getRange())) {
					rangeMap.get(rangeEx.getSubjectType()).put(
							rangeEx.getSubjectId() + rangeEx.getRange()+rangeEx.getSubjectType(), range);
				} else {
					rangeMap.get(rangeEx.getSubjectType()).put(
							rangeEx.getSubjectId()+rangeEx.getSubjectType(), range);
				}

			}
		}

	}

	private void makeBestTypeByStuId(List<NewGKStudentRange> stuRangelist,
			Map<String, Map<String, Map<String, String>>> bestTypeMap,boolean isAO) {
		if (CollectionUtils.isEmpty(stuRangelist)) {
			return;
		}
		for (NewGKStudentRange stuRange : stuRangelist) {
			String subjectType=stuRange.getSubjectType();
			if(isAO) {
				if("O".equals(subjectType)) {
					subjectType="A";
				}
			}
			Map<String, Map<String, String>> stuBestMap = bestTypeMap
					.get(subjectType);

			if (stuBestMap == null) {
				stuBestMap = new HashMap<String, Map<String, String>>();
				bestTypeMap.put(subjectType, stuBestMap);
				stuBestMap.put(stuRange.getStudentId(),
						new HashMap<String, String>());
			}
			Map<String, String> subjectBestMap = stuBestMap.get(stuRange
					.getStudentId());
			if (subjectBestMap == null) {
				subjectBestMap = new HashMap<String, String>();
				stuBestMap.put(stuRange.getStudentId(), subjectBestMap);
			}
			stuBestMap.get(stuRange.getStudentId()).put(
					stuRange.getSubjectId(), stuRange.getRange());
		}
	}

//	/**
//	 * 跟stuChooselist是否完全匹配，是否有学生未安排ABCD
//	 * 
//	 * @param stuChooselist
//	 * @return
//	 */
	private List<StudentSubjectDto> initSingleLayerByBestTypes(
			List<StudentResultDto> stuChooselist,
			Map<String, Map<String, String>> bestTypeMap,
			List<String> subjectIds, String subjectType, int bathCount,
			String redisKey, String redisKeyMess) {
		List<StudentSubjectDto> newList = new ArrayList<StudentSubjectDto>();
		StudentSubjectDto studto;
		if(NewGkElectiveConstant.SUBJECT_TYPE_O.equals(subjectType)) {
			//所有学生
			for (StudentResultDto dto : stuChooselist) {
				studto = studentResultDtoToStudentSubjectDto(dto);
				if (bestTypeMap.containsKey(studto.getStuId())) {
					
					studto.setRealChooseSubjectIds(new HashSet<String>());
					studto.getRealChooseSubjectIds().addAll(subjectIds);
					studto.setChooseSubjectIds(new HashSet<String>());

					List<String> canArrangeList = new ArrayList<String>();

					Map<String, String> subjectIdsBestType = bestTypeMap.get(studto
							.getStuId());

					// 需要参与的科目
					for (String ss : subjectIds) {
						if (subjectIdsBestType.containsKey(ss)) {
							String subId = ss + subjectIdsBestType.get(ss);
							canArrangeList.add(subId);// 档次
							studto.getChooseSubjectIds().add(subId);
						} else {
							System.out.println("存在学生"
									+ dto.getStudentName() + "---" + ss
									+ "--行政班科目未分层");
							RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
							RedisUtils.set(redisKeyMess,
									dto.getStudentName()
											+ "存在行政班科目未分层,原因可能数据有所调整",TIME_ONE_HOUR);
							return null;
						}
					}
					if (CollectionUtils.isEmpty(canArrangeList)) {
//							System.out
//									.println(dto.getStudentName() + "---无需要的安排的科目");
						continue;
					}
					if (canArrangeList.size() > bathCount) {
						System.out.println(dto.getStudentName()
								+ "---需要的批次O数至少：" + canArrangeList.size());
						RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
						RedisUtils.set(redisKeyMess, dto.getStudentName()
								+ "需要的批次点至少需要" + canArrangeList.size()
								+ "行政班科目批次时间点数,请联系管理员",TIME_ONE_HOUR);
						return null;
					}

					if (canArrangeList.size() < bathCount) {
						addNull(canArrangeList, bathCount);
					}
					studto.setAllSubjectIds(canArrangeList);
					newList.add(studto);
				} else {
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, dto.getStudentName()
							+ "未找到分层数据,原因可能选课数据有所调整",TIME_ONE_HOUR);
					return null;
				}
			}
			return newList;
		}
		boolean isA = false;
		if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
			isA = true;
		}
		for (StudentResultDto dto : stuChooselist) {
			studto = studentResultDtoToStudentSubjectDto(dto);
			List<NewGkChoResult> result = dto.getResultList();
			if (CollectionUtils.isEmpty(result)) {
				System.out.println(dto.getStudentName() + "---未找到选课数据");
				continue;
			}
			Set<String> chooseSubject = EntityUtils.getSet(result, NewGkChoResult::getSubjectId);
			
			Set<String> needSubjectIds = new HashSet<String>();
			if(isA){
				for (String ss : chooseSubject) {
					if (subjectIds.contains(ss)) {
						needSubjectIds.add(ss);

					} 
				}
			}else{
				for (String ss : subjectIds) {
					if (!chooseSubject.contains(ss)) {
						needSubjectIds.add(ss);
					} 
				}
			}
			if(CollectionUtils.isEmpty(needSubjectIds)){
				//该学生不需要安排
				System.out.println(dto.getStudentName() + "---不需要走班");
				continue;
			}
			

			if (bestTypeMap.containsKey(studto.getStuId())) {
				
				studto.setRealChooseSubjectIds(new HashSet<String>());
				studto.getRealChooseSubjectIds().addAll(chooseSubject);

				studto.setChooseSubjectIds(new HashSet<String>());

				List<String> canArrangeList = new ArrayList<String>();

				Map<String, String> subjectIdsBestType = bestTypeMap.get(studto
						.getStuId());

				if (isA) {
					// 需要参与的科目
					for (String ss : chooseSubject) {
						if (subjectIds.contains(ss)) {
							if (subjectIdsBestType.containsKey(ss)) {
								String subId = ss + subjectIdsBestType.get(ss);
								canArrangeList.add(subId);// 档次
								studto.getChooseSubjectIds().add(subId);
							} else {
								System.out.println("存在学生"
										+ dto.getStudentName() + "---" + ss
										+ "--A科目未分层");
								RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
								RedisUtils.set(redisKeyMess,
										dto.getStudentName()
												+ "存在选考科目未分层,原因可能选课数据有所调整",TIME_ONE_HOUR);
								return null;
							}

						} else {
							studto.getChooseSubjectIds().add(ss);// 不需要安排的科目
						}

					}
					if (CollectionUtils.isEmpty(canArrangeList)) {
//						System.out
//								.println(dto.getStudentName() + "---无需要的安排的科目");
						continue;
					}
					if (canArrangeList.size() > bathCount) {
						System.out.println(dto.getStudentName()
								+ "---需要的批次A数至少：" + canArrangeList.size());
						RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
						RedisUtils.set(redisKeyMess, dto.getStudentName()
								+ "需要的选考至少需要" + canArrangeList.size()
								+ "选考或者学考时间点数,原因可能选课数据有所调整,请联系管理员",TIME_ONE_HOUR);
						return null;
					}

				} else {
					// 需要参与的科目
					for (String ss : chooseSubject) {
						if (subjectIds.contains(ss)) {
							if (subjectIdsBestType.containsKey(ss)) {
								String subId = ss + subjectIdsBestType.get(ss);
								studto.getChooseSubjectIds().add(subId);
							} else {
								studto.getChooseSubjectIds().add(ss);
							}
						} else {
							studto.getChooseSubjectIds().add(ss);// 不需要安排的科目
																	// 没有分档次
						}
					}
					for (String subjectId : subjectIds) {
						if (chooseSubject.contains(subjectId)) {
							// 不安排
						} else {
							if (subjectIdsBestType.containsKey(subjectId)) {
								String subId = subjectId
										+ subjectIdsBestType.get(subjectId);
								canArrangeList.add(subId);// 档次
							} else {
								System.out.println("存在学生"
										+ dto.getStudentName() + "---"
										+ subjectId + "--B科目未分层");
								RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
								RedisUtils.set(redisKeyMess,
										dto.getStudentName()
												+ "存在学考科目未分层,原因可能选课数据有所调整",TIME_ONE_HOUR);
								return null;
							}

						}
					}

					if (CollectionUtils.isEmpty(canArrangeList)) {
//						System.out
//								.println(dto.getStudentName() + "---无需要的安排的科目");
						continue;
					}
					if (canArrangeList.size() > bathCount) {
						System.out.println(dto.getStudentName()
								+ "---需要的批次B数至少：" + canArrangeList.size());
						RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
						RedisUtils.set(redisKeyMess, dto.getStudentName()
								+ "需要的学考考至少需要" + canArrangeList.size()
								+ "学考时间点数,原因可能选课数据有所调整,请联系管理员",TIME_ONE_HOUR);
						return null;
					}

				}

				if (canArrangeList.size() < bathCount) {
					addNull(canArrangeList, bathCount);
				}
				studto.setAllSubjectIds(canArrangeList);
				newList.add(studto);
			} else {
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, dto.getStudentName()
						+ "未找到分层数据,原因可能选课数据有所调整",TIME_ONE_HOUR);
				return null;
			}
		}
		return newList;
	}

	/**
	 * 跟stuChooselist是否完全匹配，是否有学生未安排ABCD
	 * 
	 * @param stuChooselist
	 * @return
	 */
	private List<StudentSubjectDto> initSingleLayerNoBestTypes(
			List<StudentResultDto> stuChooselist, List<String> subjectIds,
			String subjectType, int bathCount, String redisKey,
			String redisKeyMess) {
		List<StudentSubjectDto> newList = new ArrayList<StudentSubjectDto>();
		StudentSubjectDto studto;
		boolean isA = false;
		if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
			isA = true;
		}
		for (StudentResultDto dto : stuChooselist) {
			studto = studentResultDtoToStudentSubjectDto(dto);
			List<NewGkChoResult> result = dto.getResultList();
			if (CollectionUtils.isEmpty(result)) {
				System.out.println(dto.getStudentName() + "---未找到选课数据");
				continue;
			}
			Set<String> chooseSubject = EntityUtils.getSet(result, NewGkChoResult::getSubjectId);
			studto.setRealChooseSubjectIds(new HashSet<String>());
			studto.getRealChooseSubjectIds().addAll(chooseSubject);

			studto.setChooseSubjectIds(new HashSet<String>());

			List<String> canArrangeList = new ArrayList<String>();

			if (isA) {
				// 需要参与的科目
				for (String ss : chooseSubject) {
					studto.getChooseSubjectIds().add(ss);
					if (subjectIds.contains(ss)) {
						canArrangeList.add(ss);
					}
				}
				if (CollectionUtils.isEmpty(canArrangeList)) {
//					System.out.println(dto.getStudentName() + "---无需要的安排的科目");
					continue;
				}
				if (canArrangeList.size() > bathCount) {
					System.out.println(dto.getStudentName() + "---需要的批次A数至少："
							+ canArrangeList.size());
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, dto.getStudentName()
							+ "需要的选考至少需要" + canArrangeList.size()
							+ "选考时间点数,原因可能选课数据有所调整,请联系管理员",TIME_ONE_HOUR);
					return null;
				}

			} else {
				// 需要参与的科目
				studto.getChooseSubjectIds().addAll(chooseSubject);
				for (String subjectId : subjectIds) {
					if (chooseSubject.contains(subjectId)) {
						// 不安排
					} else {
						canArrangeList.add(subjectId);

					}
				}

				if (CollectionUtils.isEmpty(canArrangeList)) {
//					System.out.println(dto.getStudentName() + "---无需要的安排的科目");
					continue;
				}
				if (canArrangeList.size() > bathCount) {
					System.out.println(dto.getStudentName() + "---需要的批次B数至少："
							+ canArrangeList.size());
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, dto.getStudentName()
							+ "需要的学考至少需要" + canArrangeList.size()
							+ "学考时间点数,原因可能选课数据有所调整,请联系管理员",TIME_ONE_HOUR);
					return null;

				}

			}

			if (canArrangeList.size() < bathCount) {
				addNull(canArrangeList, bathCount);
			}
			studto.setAllSubjectIds(canArrangeList);
			newList.add(studto);
		}
		return newList;
	}

	/**
	 * 不够填null
	 * 
	 * @param subjectIds
	 * @param subjectNum
	 */
	private void addNull(List<String> subjectIdsList, int subjectNum) {
		if (subjectIdsList.size() < subjectNum) {
			for (int i = subjectIdsList.size(); i < subjectNum; i++) {
				subjectIdsList.add(null);
			}
		}
	}

	private StudentSubjectDto studentResultDtoToStudentSubjectDto(
			StudentResultDto dto) {
		StudentSubjectDto studto = new StudentSubjectDto();
		studto.setClassId(dto.getClassId());
		studto.setClassName(dto.getClassName());
		studto.setStuId(dto.getStudentId());
		studto.setStuCode(dto.getStudentCode());
		studto.setStuName(dto.getStudentName());
		return studto;
	}

//	private void autoArrangeOrBestTypes(String divideId, String subjectType,
//			List<StudentSubjectDto> students,
//			Map<String, ArrangeCapacityRange> rangeMap, int bath,
//			Map<String, String> relateSubjectId,
//			Map<String, Course> subjectNameMap, int startbath, String redisKey,
//			String redisKeyMess) {
//		List<ArrangeClass> arrangeClassList = new ArrayList<ArrangeClass>();
//		List<ArrangeStudent> arrangeStudentList = ArrangeDtoConverter
//				.convertToArrangeStudent(students);
//		ArrangeSingleSolver solver = new ArrangeSingleSolver(bath, bath,
//				arrangeStudentList, arrangeClassList, redisKey);
//
//		solver.addListener(new SolverListener() {
//
//			@Override
//			public void solveStarted() {
//			}
//
//			@Override
//			public void solveCancelled() {
//			}
//
//			@Override
//			public void solveFinished(
//					Map<String, List<Room>>[] bottleArray,
//					Map<String, Set<Integer>> classIdAdditionalSubjectIndexSetMap) {
//				long start = System.currentTimeMillis();
//				// classIdAdditionalSubjectIndexSetMap 不存在2+x
//				makeBath(divideId, subjectType, bottleArray, relateSubjectId,
//						subjectNameMap, startbath, redisKey, redisKeyMess);
//				long end = System.currentTimeMillis();
//				System.out.println("保存" + subjectType + "耗时：" + (end - start)
//						/ 1000 + "s");
//
//			}
//
//			@Override
//			public void onError(Exception e) {
//				System.out.println(e.getMessage());
//				RedisUtils.set(redisKey, "error");
//				RedisUtils.set(redisKeyMess, e.getMessage());
//				System.out.println(redisKey+"保存失败");
//			}
//
//		});
//		solver.solve(rangeMap);
//	}
//
//	private void makeBath(String divideId, String subjectType,
//			Map<String, List<Room>>[] roomCombineArray,
//			Map<String, String> newSubjectIdAndOld,
//			Map<String, Course> subjectNameMap, int stattBath, String redisKey,
//			String redisKeyMess) {
//		boolean flag = false;
//		if (roomCombineArray != null) {
//			flag = jadge(roomCombineArray);
//		}
//		String mess = "";
//		if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
//			mess = "选考";
//		} else {
//			mess = "学考";
//		}
//		if (!flag) {
//			// 没有要保存的数据
//			System.out.println("没有数据保存");
//			RedisUtils.set(redisKey, "success");
//			RedisUtils.set(redisKeyMess, mess + ":没有数据保存");
//			return;
//		} else {
//			List<NewGkDivideClass> divideClassList = new ArrayList<NewGkDivideClass>();
//			List<NewGkClassStudent> classStuList = new ArrayList<NewGkClassStudent>();
//
//			Map<String, Integer> subjectNumMap = new HashMap<String, Integer>();
//			String subjectId = null;
//			List<Room> roomList = null;
//
//			for (int i = 0; i < roomCombineArray.length; i++) {
//				for (Map.Entry<String, List<Room>> cellBottleEntry : roomCombineArray[i]
//						.entrySet()) {
//					String newsubjectId = cellBottleEntry.getKey();
//					roomList = cellBottleEntry.getValue();
//					if (CollectionUtils.isEmpty(roomList)) {
//						continue;
//					}
//
//					subjectId = newSubjectIdAndOld.get(newsubjectId);
//					String type = "";
//					if (newsubjectId.equals(subjectId)) {
//						// 没有分层
//					} else {
//						type = newsubjectId.substring(
//								newsubjectId.length() - 1,
//								newsubjectId.length());
//					}
//
//					for (Room room : roomList) {
//						List<StudentSubjectDto> studentList = room
//								.getStudentList();
//						if (CollectionUtils.isEmpty(studentList)) {
//							continue;
//						}
//						Set<String> stuIdSet = EntityUtils.getSet(
//								room.getStudentList(), "stuId");
//						toDivideClass(divideId, subjectId, type, subjectType,
//								stuIdSet, subjectNumMap, i + stattBath,
//								subjectNameMap, divideClassList, classStuList);
//					}
//				}
//
//			}
//			try {
//				long start = System.currentTimeMillis();
//				String[] delClassId = null;
//				Set<String> delClassIdSet = new HashSet<String>();
//				List<NewGkDivideClass> list = newGkDivideClassService
//						.findByDivideIdAndClassType(
//								divideId,
//								new String[] { NewGkElectiveConstant.CLASS_TYPE_2 },
//								false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
//
//				if (CollectionUtils.isNotEmpty(list)) {
//					for (NewGkDivideClass cc : list) {
//						if (cc.getSubjectType().equals(subjectType)) {
//							delClassIdSet.add(cc.getId());
//						}
//					}
//					if (CollectionUtils.isNotEmpty(delClassIdSet)) {
//						delClassId = delClassIdSet.toArray(new String[] {});
//					}
//				}
//				newGkDivideClassService.saveAllList(null, divideClassList,
//						classStuList, delClassId);
//
//				RedisUtils.set(redisKey, "success");
//				RedisUtils.set(redisKeyMess, mess + ":保存成功");
//				long end = System.currentTimeMillis();
//
//				System.out.println(subjectType + "-----saveBatchs耗时："
//						+ (end - start) / 1000 + "s");
//			} catch (Exception e) {
//				e.printStackTrace();
//				RedisUtils.set(redisKey, "error");
//				RedisUtils.set(redisKeyMess, mess + ":保存失败");
//			}
//
//		}
//
//	}
//
	public void toDivideClass(NewGkDivide divide, String subjectId,
			String bestType, String subjectType, Set<String> stuids,
			Map<String, Integer> subjectNumMap, int batch,
			Map<String, Course> subjectNameMap,
			List<NewGkDivideClass> divideClassList,
			List<NewGkClassStudent> classStuList) {// 批次

		NewGkDivideClass classDto = initNewGkDivideClass(divide.getId());
		NewGkClassStudent teachClassStu;
		classDto.setSubjectIds(subjectId);
		classDto.setSubjectType(subjectType);
		classDto.setBatch(batch+"");
		// 教学班名称
		String fencengtype;
		if (StringUtils.isBlank(bestType)) {
			fencengtype = "";
		} else {
			fencengtype = "(" + bestType + ")";
			classDto.setBestType(bestType);
		}
		if (!subjectNumMap.containsKey(subjectId)) {
			subjectNumMap.put(subjectId, 1);
		}
		int subNum = subjectNumMap.get(subjectId);
		classDto.setClassName(subjectNameMap.get(subjectId).getSubjectName()
				+ subjectType + fencengtype + subNum + "班");
		subjectNumMap.put(subjectId, subNum + 1);
		divideClassList.add(classDto);

		// 教学班下学生
		for (String stuid : stuids) {
			teachClassStu=initClassStudent(divide.getUnitId(), divide.getId(), classDto.getId(), stuid);
			classStuList.add(teachClassStu);
		}
	}

	private NewGkDivideClass initNewGkDivideClass(String divideId) {
		NewGkDivideClass classDto = new NewGkDivideClass();
		classDto.setId(UuidUtils.generateUuid());
		classDto.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
		classDto.setIsHand(NewGkElectiveConstant.IF_0);
		classDto.setDivideId(divideId);
		classDto.setCreationTime(new Date());
		classDto.setClassType(NewGkElectiveConstant.CLASS_TYPE_2);
		return classDto;
	}
//
//	/**
//	 * 判断roomCombineArray是不是为空 是返回true
//	 * 
//	 * @param roomCombineArray
//	 * @return
//	 */
//	private boolean jadge(Map<String, List<Room>>[] roomCombineArray) {
//		// 如果room没有
//		boolean flag = false;
//		if (roomCombineArray == null || roomCombineArray.length == 0) {
//			flag = false;
//		} else {
//			for (int i = 0; i < roomCombineArray.length; i++) {
//				Map<String, List<Room>> arrMap = roomCombineArray[i];
//				if (arrMap == null || arrMap.size() <= 0) {
//					continue;
//				}
//				for (String key : arrMap.keySet()) {
//					if (CollectionUtils.isNotEmpty(arrMap.get(key))) {
//						flag = true;
//						break;
//					}
//				}
//				if (flag) {
//					break;
//				}
//			}
//		}
//		return flag;
//	}
//
	private Map<String, String> toMakeSubjectId(Set<String> subjectAll,
			String[] bestTypes) {
		Map<String, String> map = new HashMap<String, String>();
		for (String s : subjectAll) {
			map.put(s, s);
			for (String tt : bestTypes) {
				map.put(s + tt, s);
			}
		}
		return map;
	}

	/**
	 * 纯组合模式
	 * @param divideId
	 * @param subjectAIds
	 * @param subjectBIds
	 * @param stuChooselist
	 * @param bathA
	 * @param bathB
	 * @return
	 */
	private String autoCombination(NewGkDivide divide, List<String> subjectAIds,
			List<String> subjectBIds, List<StudentResultDto> stuChooselist,
			int bathA, int bathB) {
		final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_" + divide.getId();
		final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_"
				+ divide.getId() + "_mess";
		Set<String> subjectAll = new HashSet<String>();
		subjectAll.addAll(subjectAIds);
		subjectAll.addAll(subjectBIds);
		Map<String, String> relateSubjectId = new HashMap<String, String>();
		for (String ss : subjectAll) {
			relateSubjectId.put(ss, ss);
		}

		List<Course> courseList = SUtils.dt(courseRemoteService
				.findListByIds(subjectAll.toArray(new String[] {})),
				new TR<List<Course>>() {
				});
		Map<String, Course> subjectNameMap = EntityUtils.getMap(courseList, e->e.getId());

		Map<String, Map<String, ArrangeCapacityRange>> rangeMap = new HashMap<String, Map<String, ArrangeCapacityRange>>();
		// 范围 每个科目 人数范围
		findRange(divide.getId(), rangeMap);
		// 重组结果
		List<NewGkDivideClass> gkDivideClassList = newGkDivideClassService
				.findByDivideIdAndClassType(divide.getUnitId(),
						divide.getId(),
						new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		if (CollectionUtils.isEmpty(gkDivideClassList)) {
			RedisUtils.set(key, "error",TIME_ONE_HOUR);
			RedisUtils.set(keyMess,
					"没有安排学生组合数据，选择这个分班模式，必须要安排学生位于3+0，或者2+x,使得学生最后x在同一个时间上课",TIME_ONE_HOUR);
			return null;
		}

		// 2+x 3+0 // 验证一下学生数据是不是正确，有没有在2个班级内
		String errorMess = checkStudentIfRight(gkDivideClassList, stuChooselist,true);
		if (StringUtils.isNotBlank(errorMess)) {
			RedisUtils.set(key, "error",TIME_ONE_HOUR);
			RedisUtils.set(keyMess, errorMess,TIME_ONE_HOUR);
			return null;
		}
		//默认生成行政班
		saveXzbClass(divide,gkDivideClassList);
		Map<String, ArrangeCapacityRange> rangeAMap = rangeMap
				.get(NewGkElectiveConstant.SUBJECT_TYPE_A);
		Map<String, ArrangeCapacityRange> rangeBMap = rangeMap
				.get(NewGkElectiveConstant.SUBJECT_TYPE_B);

		// 3+0
		Set<String> threeStuId = new HashSet<String>();
		// 2+x 
		Map<String, String> grouClassIdByStuId = new HashMap<String, String>();
		Map<String, NewGkDivideClass> twoGroupClassMap = new HashMap<String, NewGkDivideClass>();
		int twoClassNum=0;
		for (NewGkDivideClass group : gkDivideClassList) {
			if (NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(group
					.getSubjectType())) {
				threeStuId.addAll(group.getStudentList());
			} else {
				for (String ss : group.getStudentList()) {
					grouClassIdByStuId.put(ss, group.getId());
				}
				twoGroupClassMap.put(group.getId(), group);
				twoClassNum++;
			}
		}
		// A
		List<StudentSubjectDto> stuAList = initStudentNotGroup(stuChooselist,
				threeStuId, grouClassIdByStuId, twoGroupClassMap, subjectAIds,
				NewGkElectiveConstant.SUBJECT_TYPE_A, bathA, key, keyMess);
		if("error".equals(RedisUtils.get(key))) {
			return null;
		}
		// B
		List<StudentSubjectDto> stuBList = initStudentNotGroup(stuChooselist,
				threeStuId, grouClassIdByStuId, twoGroupClassMap, subjectBIds,
				NewGkElectiveConstant.SUBJECT_TYPE_B, bathB, key, keyMess);
		if("error".equals(RedisUtils.get(key))) {
			return null;
		}
		
		if (CollectionUtils.isEmpty(stuAList)
				&& CollectionUtils.isEmpty(stuBList)) {
	
			RedisUtils.set(key, "success",TIME_ONE_HOUR);
			RedisUtils.set(keyMess, "不存在学生需要安排教学班",TIME_ONE_HOUR);
			return null;
		}
		final int twoClassNum1=twoClassNum;
		new Thread(new Runnable() {

			@Override
			public void run() {
				List<NewGkDivideClass> insertClassList=new ArrayList<NewGkDivideClass>();
				List<NewGkClassStudent> insertStuList=new ArrayList<NewGkClassStudent>();
				Set<String> delClassIds=new HashSet<String>();
				if (CollectionUtils.isEmpty(stuAList)) {
					System.out.println("不存在学生需要安排选考");
				} else {
					makeLeftAInOneBath(divide,
							NewGkElectiveConstant.SUBJECT_TYPE_A, stuAList, rangeAMap,
							bathA,subjectNameMap, 0, key, keyMess,insertClassList,insertStuList,delClassIds);
				}
				if("error".equals(RedisUtils.get(key))) {
					return;
				}
				if (CollectionUtils.isEmpty(stuBList)) {
					System.out.println("不存在学生需要安排学考");
				} else {
					//去除B
					Map<String, Map<String, ArrangeCapacityRange>> rangeMap2 = new HashMap<String, Map<String, ArrangeCapacityRange>>();
					// 范围 每个科目 人数范围
					findRange2(divide.getId(), rangeMap2,false);
					Map<String, ArrangeCapacityRange> rangeBMap2=rangeMap2.get(NewGkElectiveConstant.SUBJECT_TYPE_B);
					if(!isChooseNewFunction){
						autoArrangeOrBestTypes2(divide,
								NewGkElectiveConstant.SUBJECT_TYPE_B, stuBList, rangeBMap2,
								bathB, relateSubjectId, subjectNameMap, bathA, key,
								keyMess,insertClassList,insertStuList,delClassIds);
					}else{
						autoArrangeOrBestTypes2New(divide,
								NewGkElectiveConstant.SUBJECT_TYPE_B, stuBList, rangeBMap2,
								bathB, relateSubjectId, subjectNameMap, bathA, key,
								keyMess,twoClassNum1,insertClassList,insertStuList,delClassIds);
					}

				}
				
				if("error".equals(RedisUtils.get(key))) {
					return;
				}
				try {
					long start = System.currentTimeMillis();
					String[] delClassId=null;
					if (CollectionUtils.isNotEmpty(delClassIds)) {
						delClassId = delClassIds.toArray(new String[] {});
						
					}
					
					newGkDivideClassService.saveAllList(divide.getUnitId(), divide.getId(),
							delClassId, insertClassList, insertStuList, true);

					long end = System.currentTimeMillis();

					System.out.println("-----saveBatchs耗时："
							+ (end - start) / 1000 + "s");
					RedisUtils.set(keyMess, "分班成功",TIME_ONE_HOUR);
					RedisUtils.set(key, "success",TIME_ONE_HOUR);
				}catch (Exception e) {
					e.printStackTrace();
					RedisUtils.set(keyMess, "分班失败",TIME_ONE_HOUR);
					RedisUtils.set(key, "error",TIME_ONE_HOUR);
				}
			}
		}).start();

		return null;

	}

	private void saveXzbClass(NewGkDivide divide,List<NewGkDivideClass> gkDivideClassList) {
		List<NewGkDivideClass> divideClassList = new ArrayList<NewGkDivideClass>();
		List<NewGkClassStudent> classStuList = new ArrayList<NewGkClassStudent>();
		List<NewGkDivideClass> oldXzbDivideClassList = newGkDivideClassService
				.findByDivideIdAndClassType(divide.getUnitId(),
						divide.getId(),
						new String[] { NewGkElectiveConstant.CLASS_TYPE_1 }, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		String[] deleClassIds=null;
		if(CollectionUtils.isNotEmpty(oldXzbDivideClassList)){
			Set<String> deleIds=EntityUtils.getSet(oldXzbDivideClassList, e->e.getId());
			deleClassIds=deleIds.toArray(new String[]{});
		}
		
		int index=1;
		NewGkClassStudent teachClassStu;
		NewGkDivideClass xzbClass;
		for(NewGkDivideClass newClass:gkDivideClassList){
			xzbClass = initXzbClass(divide.getId(),index);
			index++;
			xzbClass.setRelateId(newClass.getId());
			divideClassList.add(xzbClass);
			//学生
			for (String stuid : newClass.getStudentList()) {
				teachClassStu = initClassStudent(divide.getUnitId(), divide.getId(), xzbClass.getId(), stuid);
				classStuList.add(teachClassStu);
			}
		}
		newGkDivideClassService.saveAllList(divide.getUnitId(), divide.getId(), deleClassIds, divideClassList, classStuList, false);
	}

	private NewGkDivideClass initXzbClass(String divideId,int index) {
		NewGkDivideClass xzbClass=new NewGkDivideClass();
		xzbClass.setId(UuidUtils.generateUuid());
		xzbClass.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
		xzbClass.setIsHand(NewGkElectiveConstant.IF_1);
		xzbClass.setDivideId(divideId);
		xzbClass.setCreationTime(new Date());
		xzbClass.setClassType(NewGkElectiveConstant.CLASS_TYPE_1);
		if(index/10 == 0) {
			xzbClass.setClassName("0"+index+"班");
		}else {
			xzbClass.setClassName(index+"班");
		}
		return xzbClass;
	}

	private void makeLeftAInOneBath(NewGkDivide divide, String subjectType,
			List<StudentSubjectDto> stuAList,
			Map<String, ArrangeCapacityRange> rangeAMap, int bathA,
			Map<String, Course> subjectNameMap, int startIndex, String redisKey,
			String redisKeyMess,
			List<NewGkDivideClass> insertClassList,List<NewGkClassStudent> insertStuList,
			Set<String> delClassIds) {
		Map<String,List<String>> stuIdsBySubjectId=new HashMap<String,List<String>>();
		for(StudentSubjectDto stu:stuAList){
			//需要安排的科目
			if(CollectionUtils.isEmpty(stu.getAllSubjectIds())){
				//没有需要安排的科目
				continue;
			}
			//学生理论剩下的科目只有一个
			if(stu.getAllSubjectIds().size()>1){
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, "由学生"+stu.getStuName()+"选课数据可能有所调整，需要走班时间不够,请联系研发人员调整选考时间点数！",TIME_ONE_HOUR);
				return;
			}
			String subjectId = stu.getAllSubjectIds().get(0);
			if(!stuIdsBySubjectId.containsKey(subjectId)){
				stuIdsBySubjectId.put(subjectId, new ArrayList<String>());
			}
			stuIdsBySubjectId.get(subjectId).add(stu.getStuId());
		}
		
		List<NewGkDivideClass> divideClassList = new ArrayList<NewGkDivideClass>();
		List<NewGkClassStudent> classStuList = new ArrayList<NewGkClassStudent>();

		Map<String, Integer> subjectNumMap = new HashMap<String, Integer>();
		String subjectId = null;
		List<String> stuIds;
		for(Entry<String, List<String>> item:stuIdsBySubjectId.entrySet()){
			subjectId=item.getKey();
			stuIds = item.getValue();
			ArrangeCapacityRange range = rangeAMap.get(subjectId+subjectType);
			if(range==null){
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, "科目没有设置限制范围："+subjectNameMap.get(subjectId).getSubjectName(),TIME_ONE_HOUR);
				return;
			}
			if(range.getClassNum()<=0){
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, "科目没有开设班级数："+subjectNameMap.get(subjectId).getSubjectName(),TIME_ONE_HOUR);
				return;
			}
			List<String>[] array = new List[range.getClassNum()];
			for(int i=0;i<range.getClassNum();i++){
				array[i]=new ArrayList<String>();
			}
			openClass2(range.getClassNum(), stuIds, array);
			for (int i = 0; i < array.length; i++) {
				if (CollectionUtils.isNotEmpty(array[i])) {
					List<String> sIdList = array[i];
					Set<String> stuIdSet = new HashSet<String>();
					stuIdSet.addAll(sIdList);
					toDivideClass(divide, subjectId, null, subjectType,
							stuIdSet, subjectNumMap, startIndex+1,
							subjectNameMap, divideClassList, classStuList);
				}
			}
			
			
		}

		
	
		Set<String> delClassIdSet = new HashSet<String>();
		List<NewGkDivideClass> list = newGkDivideClassService
				.findByDivideIdAndClassType(
						divide.getUnitId(),
						divide.getId(),
						new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);

		if (CollectionUtils.isNotEmpty(list)) {
			for (NewGkDivideClass cc : list) {
				if (cc.getSubjectType().equals(subjectType)) {
					delClassIdSet.add(cc.getId());
				}
			}
		}
		
		
		if(CollectionUtils.isNotEmpty(divideClassList)) {
			insertClassList.addAll(divideClassList);
		}
		if(CollectionUtils.isNotEmpty(classStuList)) {
			insertStuList.addAll(classStuList);
		}
		if (CollectionUtils.isNotEmpty(delClassIdSet)) {
			delClassIds.addAll(delClassIdSet);
		}

	}
	
	private void openClass2(int classNum, List<String> stuList,
			List<String>[] array) {
		boolean flag = true;// 顺排：true,逆排 ：false
		int index = 0;
		if (CollectionUtils.isNotEmpty(stuList)) {
			for (int i = 0; i < stuList.size(); i++) {
				if (flag) {
					array[index].add(stuList.get(i));
					index++;
					if (index == classNum) {
						flag = false;
					}
				} else {
					index--;
					array[index].add(stuList.get(i));
					if (index == 0) {
						flag = true;
					}
				}
			}
		}
	}

	private List<StudentSubjectDto> initStudentNotGroup(
			List<StudentResultDto> stuChooselist, Set<String> threeStuId,
			Map<String, String> grouClassIdByStuId,
			Map<String, NewGkDivideClass> twoGroupClassMap,
			List<String> subjectIds, String subjectType, int bathCount,
			String redisKey, String redisKeyMess) {
		List<StudentSubjectDto> newDtoList = new ArrayList<StudentSubjectDto>();
		boolean isA = false;
		if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
			isA = true;
		}
		StudentSubjectDto studto;
		for (StudentResultDto result : stuChooselist) {
			if (threeStuId.contains(result.getStudentId())) {
				continue;
			}
			if (CollectionUtils.isEmpty(result.getResultList())) {
				System.out.println(result.getStudentName() + "--没有选课数据");
				continue;
			}
			Set<String> chooseSubjectId = EntityUtils.getSet(
					result.getResultList(), e->e.getSubjectId());
			studto = studentResultDtoToStudentSubjectDto(result);

			studto.setRealChooseSubjectIds(new HashSet<String>());
			studto.getRealChooseSubjectIds().addAll(chooseSubjectId);
			studto.setChooseSubjectIds(new HashSet<String>());
			studto.getChooseSubjectIds().addAll(chooseSubjectId);

			List<String> canArrangeList = new ArrayList<String>();

			if (isA) {
				// 需要参与的科目
				String groupClassId = grouClassIdByStuId.get(result.getStudentId());
				String chooseSubjectIds="";//需要排除的科目数据
				if(StringUtils.isNotBlank(groupClassId)){
					NewGkDivideClass group = twoGroupClassMap.get(groupClassId);
					if(!NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(group.getSubjectType())){
						//2+x
						chooseSubjectIds = group.getSubjectIds();
					}
					studto.setGroupClassId(groupClassId);
				}else{
					//未安排的行政班的学生
					System.out.println(result.getStudentName()
							+ "---未安排到任一组合班");
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, result.getStudentName()
							+ "未安排到任一组合班,请在手动分班处随机分派在组合，可以是混合班中",TIME_ONE_HOUR);
					return null;
				}
				for (String ss : chooseSubjectId) {
					if(chooseSubjectIds.indexOf(ss)>=0){
						//走行政班
						continue;
					}
					if (subjectIds.contains(ss)) {
						canArrangeList.add(ss);
					}
				}
				
				if (CollectionUtils.isEmpty(canArrangeList)) {
//					System.out.println(result.getStudentName() + "---没有需要的安排的科目");
					continue;
				}
				if (canArrangeList.size() > bathCount) {
					System.out.println(result.getStudentName()
							+ "---需要的批次A数至少：" + canArrangeList.size());
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, result.getStudentName()
							+ "需要的选考至少需要" + canArrangeList.size()
							+ "选考时间点数,原因可能选课数据有所调整,请联系管理员",TIME_ONE_HOUR);
					return null;
				}

			} else {
				for (String subjectId : subjectIds) {
					if (chooseSubjectId.contains(subjectId)) {
						// 不安排
					} else {
						canArrangeList.add(subjectId);
					}
				}

				if (CollectionUtils.isEmpty(canArrangeList)) {
//					System.out.println(result.getStudentName() + "---无需要的安排的科目");
					continue;
				}
				if (canArrangeList.size() > bathCount) {
					System.out.println(result.getStudentName()
							+ "---需要的批次B数至少：" + canArrangeList.size());
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, result.getStudentName()
							+ "需要的学考至少需要" + canArrangeList.size()
							+ "学考时间点数,原因可能选课数据有所调整,请联系管理员",TIME_ONE_HOUR);
					return null;

				}

			}
			if (canArrangeList.size() < bathCount) {
				addNull(canArrangeList, bathCount);
			}
			studto.setAllSubjectIds(canArrangeList);
			newDtoList.add(studto);

		}

		return newDtoList;
	}

	private String checkStudentIfRight(
			List<NewGkDivideClass> gkDivideClassList,
			List<StudentResultDto> stuChooselist,boolean isAllTwo) {
		Set<String> gIds = new HashSet<String>();// 班级下人数为0
		String errorStr = "";// 返回报错信息
		Map<String, String> stuNameMap = new HashMap<String, String>();// 用于报错提示
		Set<String> allStuId = new HashSet<String>();
		Set<String> arrangeStuId = new HashSet<String>();
		if (CollectionUtils.isNotEmpty(gkDivideClassList)) {

			Map<String, Set<String>> subjectIdsMap = new HashMap<String, Set<String>>();// 同组合下学生
			List<NewGkChoResult> resultList;
			for (StudentResultDto d : stuChooselist) {
				stuNameMap.put(d.getStudentId(), d.getStudentName());
				resultList = d.getResultList();
				if (CollectionUtils.isNotEmpty(resultList)) {
					Set<String> chooseSubjectIds = EntityUtils.getSet(
							resultList, e->e.getSubjectId());
					String ids = keySort(chooseSubjectIds);
					if (!subjectIdsMap.containsKey(ids)) {
						subjectIdsMap.put(ids, new HashSet<String>());
						subjectIdsMap.get(ids).add(d.getStudentId());
					} else {
						subjectIdsMap.get(ids).add(d.getStudentId());
					}
					List<String> idsList = keySort2(chooseSubjectIds);
					if (CollectionUtils.isNotEmpty(idsList)) {
						for (String key : idsList) {
							if (!subjectIdsMap.containsKey(key)) {
								subjectIdsMap.put(key, new HashSet<String>());
							}
							subjectIdsMap.get(key).add(d.getStudentId());
						}
					}
					allStuId.add(d.getStudentId());
				}
			}

			Set<String> stusIds = null;
			for (NewGkDivideClass dd : gkDivideClassList) {
				if (BaseConstants.ZERO_GUID.equals(dd.getSubjectIds())) {
					if(isAllTwo){
						return "存在混合班级，请将混合班级的学生移动到2+x或3+0中";
					}else{
						if (CollectionUtils.isNotEmpty(dd.getStudentList())) {
							arrangeStuId.addAll(dd.getStudentList());
						}else {
							System.out.println(dd.getClassName()+"没有学生");
							gIds.add(dd.getId());
						}
						
						continue;
					}
				}

				if (subjectIdsMap.containsKey(dd.getSubjectIds())) {
					stusIds = subjectIdsMap.get(dd.getSubjectIds());
					if (CollectionUtils.isNotEmpty(dd.getStudentList())) {
						// 交集
						if (!(CollectionUtils.intersection(dd.getStudentList(),
								stusIds).size() == dd.getStudentList().size())) {
							errorStr = errorStr + "," + dd.getClassName();
						}else{
							arrangeStuId.addAll(dd.getStudentList());
						}
					} else {
						System.out.println(dd.getClassName()+"没有学生");
						gIds.add(dd.getId());
					}

				} else {
					// 改组合不存在
					if (CollectionUtils.isNotEmpty(dd.getStudentList())) {
						errorStr = errorStr + "," + dd.getClassName();
					} else {
						gIds.add(dd.getId());
					}
				}
			}
		}
		if (StringUtils.isNotBlank(errorStr)) {
			errorStr = errorStr.substring(1);
			errorStr = "数据验证失败！在" + errorStr + "中存在学生选课数据与班级的组合科目信息不对应";
			return errorStr;
		} else {
			// 判断学生是否有在超过两个班级
			Map<String, String> stumap = checkStuClassNum(gkDivideClassList);
			if (stumap != null && stumap.size() > 0) {
				for (String s : stumap.keySet()) {
					if (stuNameMap.containsKey(s)
							&& StringUtils.isNotBlank(stumap.get(s))) {
						errorStr = errorStr + "，" + stuNameMap.get(s) + "("
								+ stumap.get(s) + ")";
					}
				}
				errorStr = "数据验证失败！存在部分学生安排在超过一个的组合班级，有" + errorStr.substring(1) + "。";
				return errorStr;
			}

		}
		if (arrangeStuId.size() != allStuId.size()) {
			errorStr = "存在学生数据有误，原因学生选课数据有所调整，或者未安排！";
			return errorStr;
		}
		if (gIds.size() > 0) {
			errorStr = "存在" + gIds.size() + "个组合班级下学生数量为0，请先删除！";
			return errorStr;
		}
		return errorStr;
	}
	
//	@ResponseBody
//	@RequestMapping("/openClassArrange/checkAllInGroup")
//	@ControllerInfo(value = "验证2+x,3+0")
//	public String checkAllInGroup(@PathVariable String divideId) {
//		NewGkDivide gkDivide = newGkDivideService.findById(divideId);
//		ChosenSearchDto searchdto = new ChosenSearchDto();
//		searchdto.setGradeId(gkDivide.getGradeId());
//		searchdto.setUnitId(gkDivide.getUnitId());
//		List<StudentResultDto> stuChooselist = newGkChoResultService
//				.findChosenList(gkDivide.getChoiceId(), searchdto);
//		List<NewGkDivideClass> gkDivideClassList = newGkDivideClassService
//				.findByDivideIdAndClassType(divideId,
//						new String[] { NewGkElectiveConstant.CLASS_TYPE_0 },
//						true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
//		boolean isAllTwo=false;
//		if(gkDivide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_01)){
//			isAllTwo=true;
//		}
//		if (CollectionUtils.isEmpty(gkDivideClassList)) {
//			if(isAllTwo){
//				return error("没有安排学生组合数据，选择这个分班模式，必须要安排学生位于3+0，或者2+x,使得学生最后x在同一个时间上课");
//			}else{
//				return error("没有安排学生组合数据，选择这个分班模式，必须要安排学生位于3+0，或者2+x,或者混合");
//			}
//			
//		}
//		// 2+x 3+0 // 验证一下学生数据是不是正确，有没有在2个班级内
//		String errorMess = checkStudentIfRight(gkDivideClassList, stuChooselist,isAllTwo);
//		if (StringUtils.isNotBlank(errorMess)) {
//			return error(errorMess);
//		}
//		//是不是都是3+0 无需走到下一步
//		int notThree=0;
//		for(NewGkDivideClass item:gkDivideClassList){
//			if(!NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(item.getSubjectType())){
//				notThree++;
//			}
//		}
//		if(notThree==0){
//			try{
//				saveXzbClass(divideId, gkDivideClassList);
//				newGkDivideService.updateStat(divideId, NewGkElectiveConstant.IF_1);
//				return success("allArrange");
//			}catch(Exception e){
//				return error("全部为3+0组合，直接保存失败");
//			}
//		}
//		return success("");
//	}
//	
//	@RequestMapping("/singleList2/page")
//	@ControllerInfo(value = "分班下一步-全固定模式")
//	public String showSingleList2(@PathVariable String divideId, ModelMap map){
//		NewGkDivide divide = newGkDivideService.findById(divideId);
//		if(divide == null || (divide.getIsDeleted() != null && divide.getIsDeleted() == 1)) {
//    		return errorFtl(map, "分班记录不存在或已被删除！");
//    	}
//		
//		NewGkChoice choice = newGkChoiceService.findById(divide.getChoiceId());
//		if(choice == null || (choice.getIsDeleted() != null && choice.getIsDeleted() == 1)) {
//    		return errorFtl(map, "选课记录不存在或已被删除！");
//    	}
//		List<String> subjectIdList = newGkChoRelationService.findByChoiceIdAndObjectType(choice.getId(), NewGkElectiveConstant.CHOICE_TYPE_01);
//		if(CollectionUtils.isEmpty(subjectIdList)){
//			return errorFtl(map, "选课的科目记录不存在或已被删除！");
//		}
//		//allChoose 小于chooseNum 暂时不考虑
//		int allChoose = subjectIdList.size();
//		int chooseNum=choice.getChooseNum()==null?3:choice.getChooseNum();
//		int bNum=allChoose-chooseNum;
//		if(NewGkElectiveConstant.DIVIDE_TYPE_01.equals(divide.getOpenType())){
//			chooseNum=1;
//		}
//		int subjectANums=0;
//		int subjectBNums=0;
//		/**
//		 * 分班下选考，学考科目数据
//		 */
//		List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService
//				.findByDivideIdAndSubjectTypeIn(divideId, new String[] {
//						NewGkElectiveConstant.SUBJECT_TYPE_A,
//						NewGkElectiveConstant.SUBJECT_TYPE_B });
//		Set<String> subIds=new HashSet<String>();
//		List<String> subjectAIds = new ArrayList<String>();
//		List<String> subjectBIds = new ArrayList<String>();
//
//		for (NewGkOpenSubject openSubject : openSubjectList) {
//			if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(openSubject
//					.getSubjectType())) {
//				subjectAIds.add(openSubject.getSubjectId());
//			} else {
//				subjectBIds.add(openSubject.getSubjectId());
//			}
//			subIds.add(openSubject.getSubjectId());
//		}
//		
//		List<Course> courseList = SUtils.dt(courseRemoteService
//				.findListByIds(subIds.toArray(new String[] {})),
//				new TR<List<Course>>() {
//				});
//		Map<String, Course> subjectNameMap = EntityUtils.getMap(courseList,
//				"id");
//		
//		List<NewGkDivideClass> gkDivideClassList = newGkDivideClassService
//				.findByDivideIdAndClassType(divideId,
//						new String[] { NewGkElectiveConstant.CLASS_TYPE_0 },
//						true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
//		// 3+0
//		Set<String> threeStuId = new HashSet<String>();
//		// 2+x
//		Map<String, String> grouClassIdByStuId = new HashMap<String, String>();
//		Map<String, NewGkDivideClass> twoGroupClassMap = new HashMap<String, NewGkDivideClass>();
//		for (NewGkDivideClass group : gkDivideClassList) {
//			if (NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(group
//					.getSubjectType())) {
//				threeStuId.addAll(group.getStudentList());
//			} else {
//				for (String ss : group.getStudentList()) {
//					grouClassIdByStuId.put(ss, group.getId());
//				}
//				twoGroupClassMap.put(group.getId(), group);
//			}
//		}
//		Map<String,Integer> stuNumABySubjectId=new HashMap<String,Integer>();
//		Map<String,Integer> stuNumBBySubjectId=new HashMap<String,Integer>();
//		
//		//学生选课
//		ChosenSearchDto searchdto = new ChosenSearchDto();
//		searchdto.setGradeId(divide.getGradeId());
//		searchdto.setUnitId(divide.getUnitId());
//		List<StudentResultDto> stuChooselist = newGkChoResultService
//				.findChosenList(divide.getChoiceId(), searchdto);
//		
//		for(StudentResultDto s:stuChooselist){
//			List<NewGkChoResult> rList = s.getResultList();
//			if(CollectionUtils.isEmpty(rList)){
//				System.out.println(s.getStudentName()+"没有选课");
//				continue;
//			}
//			if(threeStuId.contains(s.getStudentId())){
//				continue;
//			}
//			String groupId = grouClassIdByStuId.get(s.getStudentId());
//			NewGkDivideClass groupClass = twoGroupClassMap.get(groupId);
//			if(groupClass==null){
//				System.out.println(s.getStudentName()+"没有安排组合，一般不会出现这个问题，除非选课数据有所调整");
//				continue;
//			}
//			String subjectIds = groupClass.getSubjectIds();
//			Set<String> chooseSet = EntityUtils.getSet(rList, "subjectId");
//			for(String ss:chooseSet){
//				if(subjectIds.indexOf(ss)>-1){
//					//走行政班
//				}else{
//					if(subjectAIds.contains(ss)){
//						if(!stuNumABySubjectId.containsKey(ss)){
//							stuNumABySubjectId.put(ss, 1);
//						}else{
//							stuNumABySubjectId.put(ss, stuNumABySubjectId.get(ss)+1);
//						}
//					}
//				}
//			}
//			for(String bId:subjectBIds){
//				if(!chooseSet.contains(bId)){
//					if(!stuNumBBySubjectId.containsKey(bId)){
//						stuNumBBySubjectId.put(bId, 1);
//					}else{
//						stuNumBBySubjectId.put(bId, stuNumBBySubjectId.get(bId)+1);
//					}
//				}
//			}
//			
//		}
//		List<NewGKStudentRangeEx> exList = newGKStudentRangeExService.findByDivideId(divideId);
//		Map<String,NewGKStudentRangeEx> exBySubjectMap=new HashMap<String,NewGKStudentRangeEx>();
//		if(CollectionUtils.isNotEmpty(exList)){
//			for(NewGKStudentRangeEx e:exList){
//				exBySubjectMap.put(e.getSubjectId()+e.getSubjectType(), e);
//			}
//		}
//		
//		
//		
//		List<NewGKStudentRangeEx> aExList=new ArrayList<NewGKStudentRangeEx>();
//		List<NewGKStudentRangeEx> bExList=new ArrayList<NewGKStudentRangeEx>();
//		NewGKStudentRangeEx ex = null;
//		for(String s:subjectAIds){
//			Course subject = subjectNameMap.get(s);
//			if(subject==null){
//				continue;
//			}
//			ex = new NewGKStudentRangeEx();
//			ex.setSubjectName(subject.getSubjectName());
//			ex.setSubjectId(subject.getId());
//			ex.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
//			ex.setStuNum(stuNumABySubjectId.get(s)==null?0:stuNumABySubjectId.get(s));
//			if(exBySubjectMap.containsKey(subject.getId()+NewGkElectiveConstant.SUBJECT_TYPE_A)){
//				NewGKStudentRangeEx olsEx = exBySubjectMap.get(subject.getId()+NewGkElectiveConstant.SUBJECT_TYPE_A);
//				ex.setLeastNum(olsEx.getLeastNum());
//				ex.setMaximum(olsEx.getMaximum());
//				ex.setClassNum(olsEx.getClassNum());
//			}
//			aExList.add(ex);
//			subjectANums++;
//		}
//		for(String s:subjectBIds){
//			Course subject = subjectNameMap.get(s);
//			if(subject==null){
//				continue;
//			}
//			ex = new NewGKStudentRangeEx();
//			ex.setSubjectName(subject.getSubjectName());
//			ex.setSubjectId(subject.getId());
//			ex.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_B);
//			ex.setStuNum(stuNumBBySubjectId.get(s)==null?0:stuNumBBySubjectId.get(s));
//			if(exBySubjectMap.containsKey(subject.getId()+NewGkElectiveConstant.SUBJECT_TYPE_B)){
//				NewGKStudentRangeEx olsEx = exBySubjectMap.get(subject.getId()+NewGkElectiveConstant.SUBJECT_TYPE_B);
//				ex.setLeastNum(olsEx.getLeastNum());
//				ex.setMaximum(olsEx.getMaximum());
//				ex.setClassNum(olsEx.getClassNum());
//			}
//			bExList.add(ex);
//			subjectBNums++;
//		}
//		
//		
//		map.put("aExList", aExList);
//		map.put("bExList", bExList);
//		//判断是否分班完成
//		boolean canEdit = false;
//		
//		if(!NewGkElectiveConstant.IF_1.equals(divide.getStat())){
//			//判断是否正在分班
//			if(isNowDivide(divideId) ){
//				map.put("haveDivideIng", true);
//			}else{
//				map.put("haveDivideIng", false);
//				canEdit = true;
//			}
//		}
//		map.put("canEdit", canEdit);
//		
//		boolean allTwo=false;
//		if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_01)){
//			allTwo=true;
//		}
//		if(allTwo){
//			//默认一个批次
//			divide.setBatchCountTypea(1);
//		}
//		map.put("divide", divide);
//		map.put("allTwo", allTwo);
//		//默认浮动人数
//		map.put("defaultNum", divide.getMaxGalleryful());
//		
//		if(subjectANums>0 && subjectANums<chooseNum){
//			divide.setBatchCountTypea(subjectANums);
//		}else{
//			divide.setBatchCountTypea(chooseNum);
//		}
//		if(subjectBNums>0 && subjectBNums<bNum){
//			divide.setBatchCountTypeb(subjectBNums);
//		}else{
//			divide.setBatchCountTypeb(bNum);
//		}
//		return "/newgkelective/divideGroup/singleArrangeList2.ftl";
//	}
//	
//	
	/**
	 * 单科分层模式
	 * @param divide
	 * @param subjectAIds
	 * @param subjectBIds
	 * @param stuChooselist
	 * @param isA
	 * @param isB
	 * @param bathA
	 * @param bathB
	 * @return
	 */
		private void autoSingleLayer2(NewGkDivide divide, List<String> subjectAIds,
				List<String> subjectBIds, List<StudentResultDto> stuChooselist,
				boolean isA, boolean isB, int bathA, int bathB) {
			String divideId=divide.getId();
			final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_" + divideId;
			final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_"
					+ divideId + "_mess";
			
			Set<String> subjectAll = new HashSet<String>();
			subjectAll.addAll(subjectAIds);
			subjectAll.addAll(subjectBIds);
			// 用于后面保存数据用的
			Map<String, String> relateSubjectId = toMakeSubjectId(subjectAll,
					new String[] { "A", "B", "C", "D","E","F","G","H" });
			List<Course> courseList = SUtils.dt(courseRemoteService
					.findListByIds(subjectAll.toArray(new String[] {})),
					new TR<List<Course>>() {
					});
			Map<String, Course> subjectNameMap = EntityUtils.getMap(courseList,
					e->e.getId());

			Map<String, Map<String, ArrangeCapacityRange>> rangeMap = new HashMap<String, Map<String, ArrangeCapacityRange>>();
			//TODO? 范围 每个科目 人数范围  不默认加上AB区分
			findRange2(divideId, rangeMap,false);

			// 1:选择学生选课数据--取自abcde--同时获得安排的班级具体的科目范围
			// 跟stuChooselist是否完全匹配，是否有学生未安排ABCD
			List<NewGKStudentRange> stuRangelist = newGKStudentRangeService
					.findByDivideId(divide.getUnitId(), divideId);
			// 每一个学生的 分层情况
			//key:subjectType k2:stuId k3:subjectId v:range(层次)
			Map<String, Map<String, Map<String, String>>> bestTypeMap = new HashMap<String, Map<String, Map<String, String>>>();
			makeBestTypeByStuId(stuRangelist, bestTypeMap,false);

			
			List<StudentSubjectDto> stuDtoListA=new ArrayList<StudentSubjectDto>();
			List<StudentSubjectDto> stuDtoListB=new ArrayList<StudentSubjectDto>();
			
			//单科分层 原行政班班级数量
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(),divide.getGradeId()), new TR<List<Clazz>>(){});
			int xzbNum = clazzList.size();
			
			//组装A,B数据
			/**********A数据*********/
			if (isA) {
			// A分层
				Map<String, Map<String, String>> bestTypeA = bestTypeMap
					.get(NewGkElectiveConstant.SUBJECT_TYPE_A);
				if (bestTypeA == null || bestTypeA.size() <= 0) {
					System.out.println("A分层没有数据，不需要要安排");
				} else {
					// 组装 学生的 选课科目 为 加批次 物理 --> 物理B
					stuDtoListA = initSingleLayerByBestTypes(
							stuChooselist, bestTypeA, subjectAIds,
							NewGkElectiveConstant.SUBJECT_TYPE_A, bathA, key,
							keyMess);
					if (CollectionUtils.isEmpty(stuDtoListA)) {
						if ("error".equals(RedisUtils.get(key))) {
							RedisUtils.set(key, "error",TIME_ONE_HOUR);
							RedisUtils.set(keyMess, "学生分层数据不完整",TIME_ONE_HOUR);
							return;
						}else{
							System.out.println("A:无需安排");
						}
					}
				}
	
			} else {
				stuDtoListA = initSingleLayerNoBestTypes(
						stuChooselist, subjectAIds,
						NewGkElectiveConstant.SUBJECT_TYPE_A, bathA, key, keyMess);
				
				if (CollectionUtils.isEmpty(stuDtoListA)) {
					if ("error".equals(RedisUtils.get(key))) {
						return;
					}else{
	//					RedisUtils.set(key, "success");
						System.out.println("A:无需安排");
	//					return null;
					}
				}
				
			}
			/**********B数据*********/
			if (isB) {
				Map<String, Map<String, String>> bestTypeB = bestTypeMap
						.get(NewGkElectiveConstant.SUBJECT_TYPE_B);
				if (bestTypeB == null || bestTypeB.size() <= 0) {
					System.out.println("B分层没有数据");
				} else {
					stuDtoListB = initSingleLayerByBestTypes(
							stuChooselist, bestTypeB, subjectBIds,
							NewGkElectiveConstant.SUBJECT_TYPE_B, bathB, key,
							keyMess);
						if (CollectionUtils.isEmpty(stuDtoListB)) {
							if ("error".equals(RedisUtils.get(key))) {
								return ;
						}
					}
				} 
			}else {
				stuDtoListB = initSingleLayerNoBestTypes(
						stuChooselist, subjectBIds,
						NewGkElectiveConstant.SUBJECT_TYPE_B, bathB, key, keyMess);
				if (CollectionUtils.isEmpty(stuDtoListB)) {
					if ("error".equals(RedisUtils.get(key))) {
						return ;
					}
				}	
			}
			
			if(CollectionUtils.isEmpty(stuDtoListA) && CollectionUtils.isEmpty(stuDtoListB)) {
				System.out.println("AB没有数据需要安排教学班");
				RedisUtils.set(key, "success",TIME_ONE_HOUR);
				RedisUtils.set(keyMess, "没有数据需要安排教学班",TIME_ONE_HOUR);
			}	
		
			final List<StudentSubjectDto> stuDtoListA1=stuDtoListA;
			final List<StudentSubjectDto> stuDtoListB1=stuDtoListB;
			
			 new Thread(new Runnable() {

				@Override
				public void run() {
					List<NewGkDivideClass> insertClassList=new ArrayList<NewGkDivideClass>();
					List<NewGkClassStudent> insertStuList=new ArrayList<NewGkClassStudent>();
					Set<String> delClassIds=new HashSet<String>();
					if(CollectionUtils.isNotEmpty(stuDtoListA1)){
						Map<String, ArrangeCapacityRange> rangeAMap=rangeMap.get(NewGkElectiveConstant.SUBJECT_TYPE_A);
						if (rangeAMap == null || rangeAMap.size() <= 0) {
							System.out.println("A科目要求参数没有数据");
							RedisUtils.set(keyMess, "存在选考科目限制条件未安排",TIME_ONE_HOUR);
							RedisUtils.set(key, "error",TIME_ONE_HOUR);
							return;
						} 
						if(!isChooseNewFunction){
							autoArrangeOrBestTypes2(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_A, stuDtoListA1,
									rangeAMap, bathA, relateSubjectId, subjectNameMap,
									0, key, keyMess,insertClassList,insertStuList,
									delClassIds);
						}else{
							autoArrangeOrBestTypes2New(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_A, stuDtoListA1,
									rangeAMap, bathA, relateSubjectId, subjectNameMap,
									0, key, keyMess,xzbNum,insertClassList,insertStuList,
									delClassIds);
						}
					}else{
						System.out.println("A没有数据需要安排教学班");
					}
					
					if("error".equals(RedisUtils.get(key))) {
						System.out.println("A分班失败");
						return;
					}
				
				
					if(CollectionUtils.isNotEmpty(stuDtoListB1)){
						Map<String, ArrangeCapacityRange> rangeBMap = rangeMap
								.get(NewGkElectiveConstant.SUBJECT_TYPE_B);
						if (rangeBMap == null || rangeBMap.size() <= 0) {
							System.out.println("B科目要求参数没有数据");
							RedisUtils.set(keyMess, "存在学考科目限制条件未安排",TIME_ONE_HOUR);
							RedisUtils.set(key, "error",TIME_ONE_HOUR);
							System.out.println("B科目要求参数没有数据");
							return;
						} 
						if(!isChooseNewFunction){
							autoArrangeOrBestTypes2(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_B, stuDtoListB1,
									rangeBMap, bathB, relateSubjectId, subjectNameMap,
									bathA, key, keyMess,insertClassList,insertStuList,
									delClassIds);
						}else{
							autoArrangeOrBestTypes2New(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_B, stuDtoListB1,
									rangeBMap, bathB, relateSubjectId, subjectNameMap,
									bathA, key, keyMess,xzbNum,insertClassList,insertStuList,
									delClassIds);
						}
			
						
					}else{
						System.out.println("B没有数据需要安排教学班");
					}
					if("error".equals(RedisUtils.get(key))) {
						System.out.println("B分班失败");
						return;
					}
					try {
						long start = System.currentTimeMillis();
						String[] delClassId=null;
						if (CollectionUtils.isNotEmpty(delClassIds)) {
							delClassId = delClassIds.toArray(new String[] {});
							
						}
						
						newGkDivideClassService.saveAllList(divide.getUnitId(), divideId,
								delClassId, insertClassList, insertStuList, true);
	
						long end = System.currentTimeMillis();
	
						System.out.println("-----saveBatchs耗时："
								+ (end - start) / 1000 + "s");
						RedisUtils.set(keyMess, "分班成功",TIME_ONE_HOUR);
						RedisUtils.set(key, "success",TIME_ONE_HOUR);
					}catch (Exception e) {
						e.printStackTrace();
						RedisUtils.set(keyMess, "分班失败",TIME_ONE_HOUR);
						RedisUtils.set(key, "error",TIME_ONE_HOUR);
					}
				}
				
					
			}).start();
			
			return;
		}
		
		
		private void autoSingleLayer2ContatinXZB(NewGkDivide divide, List<String> subjectAIds,
				List<String> subjectBIds,List<String> subjectOIds, List<StudentResultDto> stuChooselist,
				boolean isA, boolean isB, int bathA, int bathB) {
			
			String divideId=divide.getId();
			final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_" + divideId;
			final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_"
					+ divideId + "_mess";
			
			Set<String> subjectAll = new HashSet<String>();
			subjectAll.addAll(subjectAIds);
			subjectAll.addAll(subjectBIds);
			subjectAll.addAll(subjectOIds);
			// 用于后面保存数据用的
			Map<String, String> relateSubjectId = toMakeSubjectId(subjectAll,
					new String[] { "A", "B", "C", "D" });
			
			List<Course> courseList = SUtils.dt(courseRemoteService
					.findListByIds(subjectAll.toArray(new String[] {})),
					new TR<List<Course>>() {
					});
			Map<String, Course> subjectNameMap = EntityUtils.getMap(courseList,
					e->e.getId());
			//key:subjectType(A,B,O) subjectId
			Map<String, Map<String, ArrangeCapacityRange>> rangeMap = new HashMap<String, Map<String, ArrangeCapacityRange>>();
			// 范围 每个科目 人数范围  不默认加上AB区分
			findRange2(divideId, rangeMap,false);

			// 1:选择学生选课数据--取自abcde--同时获得安排的班级具体的科目范围
			// 跟stuChooselist是否完全匹配，是否有学生未安排ABCD
			List<NewGKStudentRange> stuRangelist = newGKStudentRangeService
					.findByDivideId(divide.getUnitId(), divideId);
			//key:subjectType studentId
			Map<String, Map<String, Map<String, String>>> bestTypeMap = new HashMap<String, Map<String, Map<String, String>>>();
			makeBestTypeByStuId(stuRangelist, bestTypeMap,false);

			
			List<StudentSubjectDto> stuDtoListA=new ArrayList<StudentSubjectDto>();
			List<StudentSubjectDto> stuDtoListB=new ArrayList<StudentSubjectDto>();
			List<StudentSubjectDto> stuDtoListO=new ArrayList<StudentSubjectDto>();
			
			//单科分层 原行政班班级数量
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(),divide.getGradeId()), new TR<List<Clazz>>(){});
			int xzbNum = clazzList.size();
			int bathO1=0;
			if(CollectionUtils.isNotEmpty(subjectOIds)) {
				//行政班单科分层
				//只针对有选课的所有学生 默认批次数subjectOIds.size()
				Map<String, Map<String, String>> bestTypeO = bestTypeMap
						.get(NewGkElectiveConstant.SUBJECT_TYPE_O);
				if (bestTypeO == null || bestTypeO.size() <= 0) {
					System.out.println("O分层没有数据，不需要要安排");
				}else {
					bathO1=subjectOIds.size();
					stuDtoListO = initSingleLayerByBestTypes(
							stuChooselist, bestTypeO, subjectOIds,
							NewGkElectiveConstant.SUBJECT_TYPE_O, bathO1, key,
							keyMess);
				}
				
			}
			
			//组装A,B数据
			/**********A数据*********/
			if (isA) {
			// A分层
				Map<String, Map<String, String>> bestTypeA = bestTypeMap
					.get(NewGkElectiveConstant.SUBJECT_TYPE_A);
				if (bestTypeA == null || bestTypeA.size() <= 0) {
					System.out.println("A分层没有数据，不需要要安排");
				} else {
					stuDtoListA = initSingleLayerByBestTypes(
							stuChooselist, bestTypeA, subjectAIds,
							NewGkElectiveConstant.SUBJECT_TYPE_A, bathA, key,
							keyMess);
					if (CollectionUtils.isEmpty(stuDtoListA)) {
						if ("error".equals(RedisUtils.get(key))) {
							RedisUtils.set(key, "error",TIME_ONE_HOUR);
							RedisUtils.set(keyMess, "学生分层数据不完整",TIME_ONE_HOUR);
							return;
						}else{
							System.out.println("A:无需安排");
						}
					}
				}
	
			} else {
				stuDtoListA = initSingleLayerNoBestTypes(
						stuChooselist, subjectAIds,
						NewGkElectiveConstant.SUBJECT_TYPE_A, bathA, key, keyMess);
				
				if (CollectionUtils.isEmpty(stuDtoListA)) {
					if ("error".equals(RedisUtils.get(key))) {
						return;
					}else{
	//					RedisUtils.set(key, "success");
						System.out.println("A:无需安排");
	//					return null;
					}
				}
				
			}
			/**********B数据*********/
			if (isB) {
				Map<String, Map<String, String>> bestTypeB = bestTypeMap
						.get(NewGkElectiveConstant.SUBJECT_TYPE_B);
				if (bestTypeB == null || bestTypeB.size() <= 0) {
					System.out.println("B分层没有数据");
				} else {
					stuDtoListB = initSingleLayerByBestTypes(
							stuChooselist, bestTypeB, subjectBIds,
							NewGkElectiveConstant.SUBJECT_TYPE_B, bathB, key,
							keyMess);
						if (CollectionUtils.isEmpty(stuDtoListB)) {
							if ("error".equals(RedisUtils.get(key))) {
								return ;
						}
					}
				} 
			}else {
				stuDtoListB = initSingleLayerNoBestTypes(
						stuChooselist, subjectBIds,
						NewGkElectiveConstant.SUBJECT_TYPE_B, bathB, key, keyMess);
				if (CollectionUtils.isEmpty(stuDtoListB)) {
					if ("error".equals(RedisUtils.get(key))) {
						return ;
					}
				}	
			}
			
			if(CollectionUtils.isEmpty(stuDtoListA) && CollectionUtils.isEmpty(stuDtoListB)) {
				System.out.println("AB没有数据需要安排教学班");
				RedisUtils.set(key, "success",TIME_ONE_HOUR);
				RedisUtils.set(keyMess, "没有数据需要安排教学班",TIME_ONE_HOUR);
			}	
		
			final List<StudentSubjectDto> stuDtoListA1=stuDtoListA;
			final List<StudentSubjectDto> stuDtoListB1=stuDtoListB;
			final List<StudentSubjectDto> stuDtoListO1=stuDtoListO;
			final int bathO=bathO1;
			 new Thread(new Runnable() {

				@Override
				public void run() {
					List<NewGkDivideClass> insertClassList=new ArrayList<NewGkDivideClass>();
					List<NewGkClassStudent> insertStuList=new ArrayList<NewGkClassStudent>();
					Set<String> delClassIds=new HashSet<String>();
					if(CollectionUtils.isNotEmpty(stuDtoListO1)) {
						Map<String, ArrangeCapacityRange> rangeOMap=rangeMap.get(NewGkElectiveConstant.SUBJECT_TYPE_O);
						if (rangeOMap == null || rangeOMap.size() <= 0) {
							System.out.println("O科目要求参数没有数据");
							RedisUtils.set(keyMess, "存在行政班科目限制条件未安排",TIME_ONE_HOUR);
							RedisUtils.set(key, "error",TIME_ONE_HOUR);
							return;
						} 
						if(!isChooseNewFunction){
							autoArrangeOrBestTypes2(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_O, stuDtoListO1,
									rangeOMap, bathO, relateSubjectId, subjectNameMap,
									0, key, keyMess,insertClassList,insertStuList,
									delClassIds);
						}else{
							autoArrangeOrBestTypes2New(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_O, stuDtoListO1,
									rangeOMap, bathO, relateSubjectId, subjectNameMap,
									0, key, keyMess,xzbNum,insertClassList,insertStuList,
									delClassIds);
						}
					}else{
						System.out.println("行政班没有数据需要安排教学班");
					}
					if(CollectionUtils.isNotEmpty(stuDtoListA1)){
						Map<String, ArrangeCapacityRange> rangeAMap=rangeMap.get(NewGkElectiveConstant.SUBJECT_TYPE_A);
						if (rangeAMap == null || rangeAMap.size() <= 0) {
							System.out.println("A科目要求参数没有数据");
							RedisUtils.set(keyMess, "存在选考科目限制条件未安排",TIME_ONE_HOUR);
							RedisUtils.set(key, "error",TIME_ONE_HOUR);
							return;
						} 
						if(!isChooseNewFunction){
							autoArrangeOrBestTypes2(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_A, stuDtoListA1,
									rangeAMap, bathA, relateSubjectId, subjectNameMap,
									0, key, keyMess,insertClassList,insertStuList,
									delClassIds);
						}else{
							autoArrangeOrBestTypes2New(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_A, stuDtoListA1,
									rangeAMap, bathA, relateSubjectId, subjectNameMap,
									0, key, keyMess,xzbNum,insertClassList,insertStuList,
									delClassIds);
						}
					}else{
						System.out.println("A没有数据需要安排教学班");
					}
					
					if("error".equals(RedisUtils.get(key))) {
						System.out.println("A分班失败");
						return;
					}
				
				
					if(CollectionUtils.isNotEmpty(stuDtoListB1)){
						Map<String, ArrangeCapacityRange> rangeBMap = rangeMap
								.get(NewGkElectiveConstant.SUBJECT_TYPE_B);
						if (rangeBMap == null || rangeBMap.size() <= 0) {
							System.out.println("B科目要求参数没有数据");
							RedisUtils.set(keyMess, "存在学考科目限制条件未安排",TIME_ONE_HOUR);
							RedisUtils.set(key, "error",TIME_ONE_HOUR);
							System.out.println("B科目要求参数没有数据");
							return;
						} 
						if(!isChooseNewFunction){
							autoArrangeOrBestTypes2(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_B, stuDtoListB1,
									rangeBMap, bathB, relateSubjectId, subjectNameMap,
									bathA, key, keyMess,insertClassList,insertStuList,
									delClassIds);
						}else{
							autoArrangeOrBestTypes2New(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_B, stuDtoListB1,
									rangeBMap, bathB, relateSubjectId, subjectNameMap,
									bathA, key, keyMess,xzbNum,insertClassList,insertStuList,
									delClassIds);
						}
			
						
					}else{
						System.out.println("B没有数据需要安排教学班");
					}
					if("error".equals(RedisUtils.get(key))) {
						System.out.println("B分班失败");
						return;
					}
					try {
						long start = System.currentTimeMillis();
						String[] delClassId=null;
						if (CollectionUtils.isNotEmpty(delClassIds)) {
							delClassId = delClassIds.toArray(new String[] {});
							
						}
						
						newGkDivideClassService.saveAllList(divide.getUnitId(), divideId,
								delClassId, insertClassList, insertStuList, true);
	
						long end = System.currentTimeMillis();
	
						System.out.println("-----saveBatchs耗时："
								+ (end - start) / 1000 + "s");
						RedisUtils.set(keyMess, "分班成功",TIME_ONE_HOUR);
						RedisUtils.set(key, "success",TIME_ONE_HOUR);
					}catch (Exception e) {
						e.printStackTrace();
						RedisUtils.set(keyMess, "分班失败",TIME_ONE_HOUR);
						RedisUtils.set(key, "error",TIME_ONE_HOUR);
					}
				}
				
					
			}).start();
			
			return;
		}
		
		
		private void autoSingleLayer2ContatinXZBInA(NewGkDivide divide, List<String> subjectAIds,
				List<String> subjectBIds,List<String> subjectOIds, List<StudentResultDto> stuChooselist,
				boolean isA, boolean isB, int bathA, int bathB) {
			//行政班科目与A一起批次
			String divideId=divide.getId();
			final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_" + divideId;
			final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_"
					+ divideId + "_mess";
			
			Set<String> subjectAll = new HashSet<String>();
			subjectAll.addAll(subjectAIds);
			subjectAll.addAll(subjectBIds);
			subjectAll.addAll(subjectOIds);
			// 用于后面保存数据用的
			Map<String, String> relateSubjectId = toMakeSubjectId(subjectAll,
					new String[] { "A", "B", "C", "D" });
			
			List<Course> courseList = SUtils.dt(courseRemoteService
					.findListByIds(subjectAll.toArray(new String[] {})),
					new TR<List<Course>>() {
					});
			Map<String, Course> subjectNameMap = EntityUtils.getMap(courseList,
					e->e.getId());
			//key:subjectType(A,B,O) subjectId
			Map<String, Map<String, ArrangeCapacityRange>> rangeMap = new HashMap<String, Map<String, ArrangeCapacityRange>>();
			// 范围 每个科目 人数范围  不默认加上AB区分
			findRange2(divideId, rangeMap,true);

			// 1:选择学生选课数据--取自abcde--同时获得安排的班级具体的科目范围
			// 跟stuChooselist是否完全匹配，是否有学生未安排ABCD
			List<NewGKStudentRange> stuRangelist = newGKStudentRangeService
					.findByDivideId(divide.getUnitId(), divideId);
			//key:subjectType(A,O,B) studentId 
			Map<String, Map<String, Map<String, String>>> bestTypeMap = new HashMap<String, Map<String, Map<String, String>>>();
			
			makeBestTypeByStuId(stuRangelist, bestTypeMap,false);

			
			List<StudentSubjectDto> stuDtoListA=new ArrayList<StudentSubjectDto>();
			List<StudentSubjectDto> stuDtoListB=new ArrayList<StudentSubjectDto>();
			
			//单科分层 原行政班班级数量
			List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(),divide.getGradeId()), new TR<List<Clazz>>(){});
			int xzbNum = clazzList.size();
			int bathO1=0;
			
			//组装A,B数据
			/**********A数据*********/
			if (isA) {
				// A分层+O
				Map<String, Map<String, String>> bestTypeA = bestTypeMap
					.get(NewGkElectiveConstant.SUBJECT_TYPE_A);
				if (bestTypeA == null || bestTypeA.size() <= 0) {
					System.out.println("A分层没有数据，不需要要安排");
				} else {
					stuDtoListA = initSingleLayerByBestTypes(
							stuChooselist, bestTypeA, subjectAIds,
							NewGkElectiveConstant.SUBJECT_TYPE_A, bathA, key,
							keyMess);
					Map<String, Map<String, String>> bestTypeO =bestTypeMap.get(NewGkElectiveConstant.SUBJECT_TYPE_O);
					if(bestTypeA != null && bestTypeO.size() >0) {
						int ii=subjectOIds.size();
						bathA=bathA+ii;
						for(StudentSubjectDto itemDto:stuDtoListA) {
							List<String> canArrangeList = itemDto.getAllSubjectIds();
							Map<String, String> subjectIdsBestType = bestTypeO.get(itemDto
									.getStuId());

							// 需要参与的科目
							for (String ss : subjectOIds) {
								if (subjectIdsBestType.containsKey(ss)) {
									String subId = ss + subjectIdsBestType.get(ss);
									canArrangeList.add(subId);// 档次
									itemDto.getChooseSubjectIds().add(subId);
								} else {
									System.out.println("存在学生"
											+ itemDto.getStuName() + "---" + ss
											+ "--行政班科目未分层");
									RedisUtils.set(key, "error",TIME_ONE_HOUR);
									RedisUtils.set(keyMess,
											itemDto.getStuName()
													+ "存在行政班科目未分层,原因可能数据有所调整",TIME_ONE_HOUR);
									return ;
								}
							}
							if (CollectionUtils.isEmpty(canArrangeList)) {
//									System.out
//											.println(dto.getStudentName() + "---无需要的安排的科目");
								continue;
							}
							if (canArrangeList.size() > bathA) {
								System.out.println(itemDto.getStuName()
										+ "---需要的A至少：" + canArrangeList.size());
								RedisUtils.set(key, "error",TIME_ONE_HOUR);
								RedisUtils.set(keyMess, itemDto.getStuName()
										+ "需要的批次点至少需要" + canArrangeList.size()
										+ "A批次时间点数,请联系管理员",TIME_ONE_HOUR);
								return ;
							}
							itemDto.setAllSubjectIds(canArrangeList);
						}
					}
					if (CollectionUtils.isEmpty(stuDtoListA)) {
						if ("error".equals(RedisUtils.get(key))) {
							RedisUtils.set(key, "error",TIME_ONE_HOUR);
							RedisUtils.set(keyMess, "学生分层数据不完整",TIME_ONE_HOUR);
							return;
						}else{
							System.out.println("A:无需安排");
						}
					}
				}
	
			} else {
				stuDtoListA = initSingleLayerNoBestTypes(
						stuChooselist, subjectAIds,
						NewGkElectiveConstant.SUBJECT_TYPE_A, bathA, key, keyMess);
				
				if (CollectionUtils.isEmpty(stuDtoListA)) {
					if ("error".equals(RedisUtils.get(key))) {
						return;
					}else{
	//					RedisUtils.set(key, "success");
						System.out.println("A:无需安排");
	//					return null;
					}
				}
				
			}
			/**********B数据*********/
			if (isB) {
				Map<String, Map<String, String>> bestTypeB = bestTypeMap
						.get(NewGkElectiveConstant.SUBJECT_TYPE_B);
				if (bestTypeB == null || bestTypeB.size() <= 0) {
					System.out.println("B分层没有数据");
				} else {
					stuDtoListB = initSingleLayerByBestTypes(
							stuChooselist, bestTypeB, subjectBIds,
							NewGkElectiveConstant.SUBJECT_TYPE_B, bathB, key,
							keyMess);
						if (CollectionUtils.isEmpty(stuDtoListB)) {
							if ("error".equals(RedisUtils.get(key))) {
								return ;
						}
					}
				} 
			}else {
				stuDtoListB = initSingleLayerNoBestTypes(
						stuChooselist, subjectBIds,
						NewGkElectiveConstant.SUBJECT_TYPE_B, bathB, key, keyMess);
				if (CollectionUtils.isEmpty(stuDtoListB)) {
					if ("error".equals(RedisUtils.get(key))) {
						return ;
					}
				}	
			}
			
			if(CollectionUtils.isEmpty(stuDtoListA) && CollectionUtils.isEmpty(stuDtoListB)) {
				System.out.println("AB没有数据需要安排教学班");
				RedisUtils.set(key, "success",TIME_ONE_HOUR);
				RedisUtils.set(keyMess, "没有数据需要安排教学班",TIME_ONE_HOUR);
			}	
		
			final List<StudentSubjectDto> stuDtoListA1=stuDtoListA;
			final List<StudentSubjectDto> stuDtoListB1=stuDtoListB;
			final int bathA1=bathA;
			 new Thread(new Runnable() {

				@Override
				public void run() {
					List<NewGkDivideClass> insertClassList=new ArrayList<NewGkDivideClass>();
					List<NewGkClassStudent> insertStuList=new ArrayList<NewGkClassStudent>();
					Set<String> delClassIds=new HashSet<String>();
					if(CollectionUtils.isNotEmpty(stuDtoListA1)){
						Map<String, ArrangeCapacityRange> rangeAMap=rangeMap.get(NewGkElectiveConstant.SUBJECT_TYPE_A);
						if (rangeAMap == null || rangeAMap.size() <= 0) {
							System.out.println("A科目要求参数没有数据");
							RedisUtils.set(keyMess, "存在选考科目限制条件未安排",TIME_ONE_HOUR);
							RedisUtils.set(key, "error",TIME_ONE_HOUR);
							return;
						} 
						if(!isChooseNewFunction){
							autoArrangeOrBestTypes2(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_A, stuDtoListA1,
									rangeAMap, bathA1, relateSubjectId, subjectNameMap,
									0, key, keyMess,insertClassList,insertStuList,
									delClassIds);
						}else{
							autoArrangeOrBestTypes2New(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_A, stuDtoListA1,
									rangeAMap, bathA1, relateSubjectId, subjectNameMap,
									0, key, keyMess,xzbNum,insertClassList,insertStuList,
									delClassIds);
						}
					}else{
						System.out.println("A没有数据需要安排教学班");
					}
					
					if("error".equals(RedisUtils.get(key))) {
						System.out.println("A分班失败");
						return;
					}
				
				
					if(CollectionUtils.isNotEmpty(stuDtoListB1)){
						Map<String, ArrangeCapacityRange> rangeBMap = rangeMap
								.get(NewGkElectiveConstant.SUBJECT_TYPE_B);
						if (rangeBMap == null || rangeBMap.size() <= 0) {
							System.out.println("B科目要求参数没有数据");
							RedisUtils.set(keyMess, "存在学考科目限制条件未安排",TIME_ONE_HOUR);
							RedisUtils.set(key, "error",TIME_ONE_HOUR);
							System.out.println("B科目要求参数没有数据");
							return;
						} 
						if(!isChooseNewFunction){
							autoArrangeOrBestTypes2(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_B, stuDtoListB1,
									rangeBMap, bathB, relateSubjectId, subjectNameMap,
									bathA1, key, keyMess,insertClassList,insertStuList,
									delClassIds);
						}else{
							autoArrangeOrBestTypes2New(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_B, stuDtoListB1,
									rangeBMap, bathB, relateSubjectId, subjectNameMap,
									bathA1, key, keyMess,xzbNum,insertClassList,insertStuList,
									delClassIds);
						}
			
						
					}else{
						System.out.println("B没有数据需要安排教学班");
					}
					if("error".equals(RedisUtils.get(key))) {
						System.out.println("B分班失败");
						return;
					}
					try {
						long start = System.currentTimeMillis();
						String[] delClassId=null;
						if (CollectionUtils.isNotEmpty(delClassIds)) {
							delClassId = delClassIds.toArray(new String[] {});
							
						}
						
						newGkDivideClassService.saveAllList(divide.getUnitId(), divideId,
								delClassId, insertClassList, insertStuList, true);
	
						long end = System.currentTimeMillis();
	
						System.out.println("-----saveBatchs耗时："
								+ (end - start) / 1000 + "s");
						RedisUtils.set(keyMess, "分班成功",TIME_ONE_HOUR);
						RedisUtils.set(key, "success",TIME_ONE_HOUR);
					}catch (Exception e) {
						e.printStackTrace();
						RedisUtils.set(keyMess, "分班失败",TIME_ONE_HOUR);
						RedisUtils.set(key, "error",TIME_ONE_HOUR);
					}
				}
				
					
			}).start();
			
			return;
		}
		
//		
//		
//		//浙师大附中数据 语数英分层 单科A分层 B1(学考 4节课)批次 B2(高二学考 2节课)
//		private String autoSingleLayer3(NewGkDivide divide, List<String> subjectAIds,
//				List<String> subjectBIds, List<StudentResultDto> stuChooselist,
//				boolean isA, boolean isB, int bathANum, int bathBNum) {
//			String divideId=divide.getId();
//			final String keyA1 = NewGkElectiveConstant.DIVIDE_CLASS + "_" + divideId
//					+ "_A";
//			final String keyO1 = NewGkElectiveConstant.DIVIDE_CLASS + "_" + divideId
//					+ "_O";
//			final String keyB1 = NewGkElectiveConstant.DIVIDE_CLASS + "_" + divideId
//					+ "_B_1";
//			final String keyB2 = NewGkElectiveConstant.DIVIDE_CLASS + "_" + divideId
//					+ "_B_1";
//			final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_" + divideId;
//			final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_"
//					+ divideId + "_mess";
//			
//			cicleRunnable2(divideId, key, new String[]{keyA1,keyO1,keyB1,keyB2}, keyMess);
//			
//			int bathA1=bathANum;
//			int bathO1=3;
//			int bathB2=2;//高二学考
//			int bathB1=bathBNum-1;//暂时就是
//			
//			
//			//AB开设科目
//			Set<String> subjectAll = new HashSet<String>();
//			subjectAll.addAll(subjectAIds);
//			subjectAll.addAll(subjectBIds);
//			
//			List<Course> courseList = SUtils.dt(courseRemoteService
//					.findListByIds(subjectAll.toArray(new String[] {})),
//					new TR<List<Course>>() {
//					});
//			
//			//语数英科目
//			List<Course> courseList2 = SUtils.dt( courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()),new TR<List<Course>>() {
//			});
//			String[] subjectYSY = EntityUtils.getSet(courseList2, "id").toArray(new String[]{});
//			courseList.addAll(courseList2);
//			Set<String> subjectAll2 = EntityUtils.getSet(courseList,"id");
//			
//			Map<String, Course> subjectNameMap = EntityUtils.getMap(courseList,
//					"id");
//			// 用于后面保存数据用的
//			Map<String, String> relateSubjectId = toMakeSubjectId(subjectAll2,
//					new String[] { "A", "B", "C", "D" });
//	
//			Map<String, Map<String, ArrangeCapacityRange>> rangeMap = new HashMap<String, Map<String, ArrangeCapacityRange>>();
//			// 范围 每个科目 人数范围  不默认加上AB区分
//			findRange2(divideId, rangeMap);
//
//			// 1:选择学生选课数据--取自abcde--同时获得安排的班级具体的科目范围
//			// 跟stuChooselist是否完全匹配，是否有学生未安排ABCD
//			List<NewGKStudentRange> stuRangelist = newGKStudentRangeService
//					.findByDivideId(divideId);
//			//暂时key包括:A,O
//			Map<String, Map<String, Map<String, String>>> bestTypeMap = new HashMap<String, Map<String, Map<String, String>>>();
//			makeBestTypeByStuId(stuRangelist, bestTypeMap);
//			
//			
//			//学生高二学考特殊数据
//			Map<String,Set<String>> subjectBByStuId=new HashMap<String,Set<String>>();
//			List<NewGkChoiceEx> choiceExList=newGkChoiceExService.findListBy("choiceId", divide.getChoiceId());
//			if(CollectionUtils.isNotEmpty(choiceExList)){
//				for (NewGkChoiceEx newGkChoiceEx : choiceExList) {
//					if(!subjectBByStuId.containsKey(newGkChoiceEx.getStudentId())){
//						subjectBByStuId.put(newGkChoiceEx.getStudentId(), new HashSet<String>());
//					}
//					subjectBByStuId.get(newGkChoiceEx.getStudentId()).add(newGkChoiceEx.getSubjectId());
//				}
//			}
//			
//			List<StudentSubjectDto> stuDtoListA=new ArrayList<StudentSubjectDto>();
//			List<StudentSubjectDto> stuDtoListO=new ArrayList<StudentSubjectDto>();
//			List<StudentSubjectDto> stuDtoListB1=new ArrayList<StudentSubjectDto>();
//			List<StudentSubjectDto> stuDtoListB2=new ArrayList<StudentSubjectDto>();
//			
//			//单科分层 原行政班班级数量
//			List<Clazz> clazzList = SUtils.dt(classRemoteService.findByGradeIdSortAll(divide.getGradeId()), new TR<List<Clazz>>(){});
//			int xzbNum = clazzList.size();
//			
//	
//			// A分层
//			Map<String, Map<String, String>> bestTypeA = bestTypeMap
//					.get(NewGkElectiveConstant.SUBJECT_TYPE_A);
//			if (bestTypeA == null || bestTypeA.size() <= 0) {
//				//应该是不需要安排的  选考科目设置都不上课
////					RedisUtils.set(keyA, "error");
////					RedisUtils.set(keyMess, "存在选考科目限制条件未安排");
////					RedisUtils.set(key, "error");
//				System.out.println("A分层没有数据，不需要要安排");
//			} else {
//				stuDtoListA = initSingleLayerByBestTypes(
//						stuChooselist, bestTypeA, subjectAIds,
//						NewGkElectiveConstant.SUBJECT_TYPE_A, bathA1, keyA1,
//						keyMess);
//				if (CollectionUtils.isEmpty(stuDtoListA)) {
//					if ("error".equals(RedisUtils.get(keyA1))) {
//						RedisUtils.set(key, "error");
//						return null;
//					}else{
//						RedisUtils.set(keyA1, "success");
//						return null;
//					}
//				}
//			}
//
//			
//			if(CollectionUtils.isNotEmpty(stuDtoListA)){
//				Map<String, ArrangeCapacityRange> rangeAMap=rangeMap.get(NewGkElectiveConstant.SUBJECT_TYPE_A);
//				if (rangeAMap == null || rangeAMap.size() <= 0) {
//					System.out.println("A科目要求参数没有数据");
//					RedisUtils.set(keyA1, "error");
//					RedisUtils.set(keyMess, "存在选考科目限制条件未安排");
//					RedisUtils.set(key, "error");
//					return null;
//				} 
//			
//				autoArrangeOrBestTypes2New(divideId,
//						NewGkElectiveConstant.SUBJECT_TYPE_A, stuDtoListA,
//						rangeAMap, bathA1, relateSubjectId, subjectNameMap,
//						0, keyA1, keyMess,xzbNum);
//			
//			}else{
//				System.out.println("A没有数据需要安排教学班");
//				RedisUtils.set(keyA1, "success");
////				RedisUtils.set(keyMess, "选考完成");
//			}
//			//语数英
//			Map<String, Map<String, String>> bestTypeO = bestTypeMap
//					.get(NewGkElectiveConstant.SUBJECT_TYPE_O);
//			if(bestTypeO==null || bestTypeO.size()<=0){
//				System.out.println("语数英没有数据需要安排教学班");
//				RedisUtils.set(keyO1, "success");
//			}else{
//				for(Entry<String, Map<String, String>> item:bestTypeO.entrySet()){
//					String stuId = item.getKey();
//					Map<String, String> valueMap = item.getValue();
//					if(valueMap!=null && valueMap.size()>0){
//						StudentSubjectDto dto=new StudentSubjectDto();
//						dto.setStuId(stuId);
//						dto.setStuName("暂时没有取"+stuId);
//						dto.setAllSubjectIds(new ArrayList<String>());
//						for(Entry<String, String> kk:valueMap.entrySet()){
//							dto.getAllSubjectIds().add(kk.getKey()+kk.getValue());
//						}
//						stuDtoListO.add(dto);
//					}
//				}
//			}
//			if(CollectionUtils.isNotEmpty(stuDtoListO)){
//				Map<String, ArrangeCapacityRange> rangeOMap=rangeMap.get(NewGkElectiveConstant.SUBJECT_TYPE_O);
//				if (rangeOMap == null || rangeOMap.size() <= 0) {
//					System.out.println("语数英科目要求参数没有数据");
//					RedisUtils.set(keyA1, "error");
//					RedisUtils.set(keyMess, "存在语数英科目限制条件未安排");
//					RedisUtils.set(key, "error");
//					return null;
//				} 
//			
//				autoArrangeOrBestTypes2New(divideId,
//						NewGkElectiveConstant.SUBJECT_TYPE_O, stuDtoListO,
//						rangeOMap, bathO1, relateSubjectId, subjectNameMap,
//						0, keyO1, keyMess,xzbNum);
//			}else{
//				System.out.println("语数英没有数据需要安排教学班");
//				RedisUtils.set(keyO1, "success");
//			}
//			
//			initSingleLayerNoBestTypes2(subjectBByStuId,
//					stuChooselist, subjectBIds,bathB1,bathB2,stuDtoListB1,stuDtoListB2,keyB1,keyB2,keyMess);
//			if(CollectionUtils.isNotEmpty(stuDtoListB1)){
//				Map<String, ArrangeCapacityRange> rangeBMap = rangeMap
//						.get(NewGkElectiveConstant.SUBJECT_TYPE_B);
//				if (rangeBMap == null || rangeBMap.size() <= 0) {
//					System.out.println("B科目要求参数没有数据");
//					RedisUtils.set(keyB1, "error");
//					RedisUtils.set(keyMess, "存在学考科目限制条件未安排");
//					RedisUtils.set(key, "error");
//					System.out.println("B科目要求参数没有数据");
//					return null;
//				} 
//				autoArrangeOrBestTypes2New(divideId,
//						NewGkElectiveConstant.SUBJECT_TYPE_B, stuDtoListB1,
//						rangeBMap, bathB1, relateSubjectId, subjectNameMap,
//						bathA1, keyB1, keyMess,xzbNum);
////				
//			}else{
//				if ("error".equals(RedisUtils.get(keyB1))) {
//					RedisUtils.set(key, "error");
//					return null;
//				}
//				else{
//					RedisUtils.set(keyB1, "success");
//				}
//				System.out.println("B没有数据需要安排教学班");
//				RedisUtils.set(keyB1, "success");
////				RedisUtils.set(keyMess, "学考完成");
//			}
//			if(CollectionUtils.isNotEmpty(stuDtoListB2)){
//				Map<String, ArrangeCapacityRange> rangeCMap = rangeMap
//						.get("C");
//				if (rangeCMap == null || rangeCMap.size() <= 0) {
//					System.out.println("C科目要求参数没有数据");
//					RedisUtils.set(keyB2, "error");
//					RedisUtils.set(keyMess, "存在学考科目限制条件未安排");
//					RedisUtils.set(key, "error");
//					System.out.println("C科目要求参数没有数据");
//					return null;
//				} 
//				autoArrangeOrBestTypes2New(divideId,
//						"C", stuDtoListB2,
//						rangeCMap, bathB2, relateSubjectId, subjectNameMap,
//						bathA1, keyB2, keyMess,xzbNum);
////				
//			}else{
//				if ("error".equals(RedisUtils.get(keyB2))) {
//					RedisUtils.set(key, "error");
//					return null;
//				}
//				else{
//					RedisUtils.set(keyB2, "success");
//				}
//				System.out.println("B(高二学考)没有数据需要安排教学班");
//				RedisUtils.set(keyB1, "success");
////				RedisUtils.set(keyMess, "学考完成");
//			}
//			
//
//			return null;
//		}
//		
//		
//		private String initSingleLayerNoBestTypes2(
//				Map<String, Set<String>> subjectBByStuId,
//				List<StudentResultDto> stuChooselist, List<String> subjectBIds,
//				int bathB1, int bathB2, List<StudentSubjectDto> stuDtoListB1,
//				List<StudentSubjectDto> stuDtoListB2, String keyB1,
//				String keyB2, String keyMess) {
//			for(StudentResultDto stu:stuChooselist){
//				StudentSubjectDto studto1 = studentResultDtoToStudentSubjectDto(stu);
//				StudentSubjectDto studto2 = studentResultDtoToStudentSubjectDto(stu);
//				List<NewGkChoResult> result = stu.getResultList();
//				if (CollectionUtils.isEmpty(result)) {
//					System.out.println(stu.getStudentName() + "---未找到选课数据");
//					continue;
//				}
//				Set<String> chooseSubject = EntityUtils.getSet(result, "subjectId");
//				studto1.setRealChooseSubjectIds(new HashSet<String>());
//				studto2.setRealChooseSubjectIds(new HashSet<String>());
//				studto1.getRealChooseSubjectIds().addAll(chooseSubject);
//				studto2.getRealChooseSubjectIds().addAll(chooseSubject);
//
//				studto1.setChooseSubjectIds(new HashSet<String>());
//				studto2.setChooseSubjectIds(new HashSet<String>());
//				
//				List<String> canArrangeList1= new ArrayList<String>();
//				List<String> canArrangeList2 = new ArrayList<String>();
//				
//				// 需要参与的科目
//				studto1.getChooseSubjectIds().addAll(chooseSubject);
//				studto2.getChooseSubjectIds().addAll(chooseSubject);
//				Set<String> sset = subjectBByStuId.get(stu.getStudentId());
//				if(CollectionUtils.isEmpty(sset)){
//					sset=new HashSet<String>();
//				}
//				for (String subjectId : subjectBIds) {
//					if (chooseSubject.contains(subjectId)) {
//						// 不安排
//					} else {
//						if(sset.contains(subjectId)){
//							canArrangeList2.add(subjectId);
//						}else{
//							canArrangeList1.add(subjectId);
//						}
//
//					}
//				}
//
//				if (CollectionUtils.isEmpty(canArrangeList1) && CollectionUtils.isEmpty(canArrangeList2) ) {
////						System.out.println(dto.getStudentName() + "---无需要的安排的科目");
//					continue;
//				}
//				if(CollectionUtils.isNotEmpty(canArrangeList1)){
//					if (canArrangeList1.size() > bathB1) {
//						System.out.println(stu.getStudentName() + "---需要的批次B数至少："
//								+ canArrangeList1.size());
//						RedisUtils.set("keyB1", "error");
//						RedisUtils.set(keyMess, stu.getStudentName()
//								+ "需要的学考至少需要" + canArrangeList1.size()
//								+ "学考时间点数,原因可能选课数据有所调整,请联系管理员");
//						return null;
//
//					}else{
//						if (canArrangeList1.size() < bathB1) {
//							addNull(canArrangeList1, bathB1);
//						}
//						studto1.setAllSubjectIds(canArrangeList1);
//						stuDtoListB1.add(studto1);
//					}
//				}
//				if(CollectionUtils.isNotEmpty(canArrangeList2)){
//					if (canArrangeList2.size() > bathB2) {
//						System.out.println(stu.getStudentName() + "---需要的批次特殊B（高二学考）数至少："
//								+ canArrangeList2.size());
//						RedisUtils.set("keyB2", "error");
//						RedisUtils.set(keyMess, stu.getStudentName()
//								+ "需要的学考至少需要" + canArrangeList2.size()
//								+ "学考（高二学考）时间点数,原因可能选课数据有所调整,请联系管理员");
//						return null;
//
//					}else{
//						if (canArrangeList2.size() < bathB2) {
//							addNull(canArrangeList2, bathB2);
//						}
//						studto2.setAllSubjectIds(canArrangeList2);
//						stuDtoListB2.add(studto2);
//					}
//				}
//				
//				
//			}
//			return "ok";
//		}

		/**
		 * 
		 * @param divideId
		 * @param rangeMap key subjectId+bestType
		 */
		private void findRange2(String divideId,
				Map<String, Map<String, ArrangeCapacityRange>> rangeMap,boolean isAo) {
			List<NewGKStudentRangeEx> rangeExList = newGKStudentRangeExService
					.findByDivideId(divideId);
			if (CollectionUtils.isNotEmpty(rangeExList)) {
				for (NewGKStudentRangeEx rangeEx : rangeExList) {
					ArrangeCapacityRange range = new ArrangeCapacityRange();
//					range.setAvgNum(rangeEx.getMaximum());
//					range.setFolNum(rangeEx.getLeastNum());
//					range.setClassNum(rangeEx.getClassNum());
//					range.setMaxCapacity(rangeEx.getMaximum()+rangeEx.getLeastNum());
//					range.setMinCapacity(rangeEx.getMaximum()-rangeEx.getLeastNum());
					range.setClassNum(rangeEx.getClassNum());	
					range.setAvgNum((rangeEx.getMaximum()+rangeEx.getLeastNum())/2);
					range.setFolNum((rangeEx.getMaximum()-rangeEx.getLeastNum())/2);
					range.setMaxCapacity(rangeEx.getMaximum());
					range.setMinCapacity(rangeEx.getLeastNum());
					String subjectType=rangeEx.getSubjectType();
					if(isAo) {
						if(subjectType.contains("O")) {
							subjectType="A";
						}
					}
					if (!rangeMap.containsKey(subjectType)) {
						rangeMap.put(subjectType,
								new HashMap<String, ArrangeCapacityRange>());
					}
					if (StringUtils.isNotBlank(rangeEx.getRange())) {
						rangeMap.get(subjectType).put(
								rangeEx.getSubjectId() + rangeEx.getRange(), range);
					} else {
						rangeMap.get(subjectType).put(
								rangeEx.getSubjectId(), range);
					}

				}
			}
			
		}
		/**
		 * 纯混
		 * @param divideId
		 * @param subjectType
		 * @param students
		 * @param rangeMap
		 * @param bath
		 * @param relateSubjectId
		 * @param subjectNameMap
		 * @param startbath
		 * @param redisKey
		 * @param redisKeyMess
		 */
		private void autoArrangeOrBestTypes2(NewGkDivide divide, String subjectType,
				List<StudentSubjectDto> students,
				Map<String, ArrangeCapacityRange> rangeMap, int bath,
				Map<String, String> relateSubjectId,
				Map<String, Course> subjectNameMap, int startbath, String redisKey,
				String redisKeyMess,
				List<NewGkDivideClass> insertClassList,List<NewGkClassStudent> insertStuList,
				Set<String> delClassIds) {
			String subjectTypeName;
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)){
				subjectTypeName="选考";
			}else{
				subjectTypeName="学考";
			}
			boolean flag=false;
			//组装数据
			SectioningSolution sectioningSolution=new SectioningSolution();
			sectioningSolution.setId(1);
			sectioningSolution.setAppName(subjectTypeName+"分班排课");
			
			Map<String, Integer> courseSectionSizeMeanMap=new HashMap<String, Integer>();
			Map<String, Integer> courseSectionSizeMarginMap=new HashMap<String, Integer>();
			Map<String, Integer> courseSectionSizeClassMap=new HashMap<String, Integer>();
			for(Entry<String, ArrangeCapacityRange> range:rangeMap.entrySet()){
				String subId=range.getKey();
				ArrangeCapacityRange arrangeRange=range.getValue();
				courseSectionSizeMeanMap.put(subId, arrangeRange.getAvgNum());
				courseSectionSizeMarginMap.put(subId, arrangeRange.getFolNum());
				courseSectionSizeClassMap.put(subId, arrangeRange.getClassNum());
			}
			List<TimeSlot> timeSlotList=new ArrayList<TimeSlot>();
			Map<Integer,TimeSlot> timeSlotMap=new HashMap<Integer,TimeSlot>();
			TimeSlot timeSlot;
			for(int i=1;i<=bath;i++){
				timeSlot=new TimeSlot();
				timeSlot.setId(i);
				timeSlot.setTimeSlotName(subjectTypeName+i);
				timeSlot.setStudentCourseList(new ArrayList<StudentCourse>());
				timeSlotList.add(timeSlot);
				timeSlotMap.put(i, timeSlot);
			}
			sectioningSolution.setTimeSlotList(timeSlotList);
			int studentIndex=1;
			List<Student> studentList=new ArrayList<Student>();
			Student student;
			List<String> allNeedArray;
			List<StudentCourse> studentCourseList=new ArrayList<StudentCourse>();
			StudentCourse studentCourse;
			List<TakeCourse> courseList=new ArrayList<TakeCourse>();
			Map<String,TakeCourse> takeCourseMap=new HashMap<String,TakeCourse>();
			TakeCourse takeCourse;
			int courseIndex=1;
			for(StudentSubjectDto stu:students){
				student=new Student();
				student.setId(studentIndex);
				student.setStudentName(stu.getStuName());
				student.setStudentId(stu.getStuId());
				List<TimeSlot> oneTimeSlotList=new ArrayList<TimeSlot>();
				oneTimeSlotList.addAll(timeSlotList);
				allNeedArray = stu.getAllSubjectIds();
				int subIndex=1;
				List<StudentCourse> oneStudentCourse=new ArrayList<StudentCourse>();
				for(String subId:allNeedArray){
					if(subId==null){
						continue;
					}
					if(courseSectionSizeClassMap.get(subId) !=null && courseSectionSizeClassMap.get(subId)<=0){
						continue;//说明科目下没有班级不需要处理
					}
					studentCourse=new StudentCourse();
					takeCourse = takeCourseMap.get(subId);
					if(takeCourse==null){
						takeCourse=new TakeCourse();
						takeCourse.setId(courseIndex);
						String realSubjectId = relateSubjectId.get(subId);
						String bestType="";
						if(!realSubjectId.equals(subId)){
							bestType=subId.substring(subId.length()-1);
						}
						takeCourse.setCourseName(subjectNameMap.get(realSubjectId).getSubjectName()+bestType);
						takeCourse.setCourseId(subId);
						if(!courseSectionSizeMarginMap.containsKey(subId)){
							RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
							RedisUtils.set(redisKeyMess, takeCourse.getCourseName()+"没有设置平均班级人数",TIME_ONE_HOUR);
							System.out.println(takeCourse.getCourseName()+"没有设置平均班级人数");
							flag=true;
							break;
						}
						takeCourse.setSectionSizeMargin(courseSectionSizeMarginMap.get(subId));
						takeCourse.setSectionSizeMean(courseSectionSizeMeanMap.get(subId));
						courseIndex++;
						courseList.add(takeCourse);
						takeCourseMap.put(subId, takeCourse);
					}
					studentCourse.setCourse(takeCourse);
					studentCourse.setId(subIndex);
					subIndex++;
					studentCourse.setStudent(student);
					//所有批次
					studentCourse.setTimeSlotDomain(new ArrayList<TimeSlot>());
					studentCourse.getTimeSlotDomain().addAll(timeSlotList);
					
					studentCourseList.add(studentCourse);
					oneStudentCourse.add(studentCourse);
				}
				if(flag) {
					
				}
				//随机初始化给值studentCourse.setTimeSlot();
				//oneTimeSlotList
				Collections.shuffle(oneTimeSlotList);
				
				for (int j=0;j<oneStudentCourse.size();j++) {
					TimeSlot oneTime = oneTimeSlotList.get(j);
					oneStudentCourse.get(j).setTimeSlot(oneTime);
					oneTime.getStudentCourseList().add(oneStudentCourse.get(j));
				}

				studentList.add(student);
				studentIndex++;
			}

			sectioningSolution.setStudentList(studentList);
			sectioningSolution.setCourseList(courseList);
			
			sectioningSolution.setStudentCourseList(studentCourseList);

			SectioningApp sectionApp=new SectioningApp();
			try {
				SectioningSolution result = sectionApp.solve(sectioningSolution, false,Evn.isDevModel());
				makeAndSave(divide, result, rangeMap, startbath, subjectType, relateSubjectId, subjectNameMap,null,insertClassList,insertStuList,delClassIds);
//				RedisUtils.set(redisKey, "success");
//				RedisUtils.set(redisKeyMess, subjectTypeName+"分班成功");
			} catch (IOException e) {
				e.printStackTrace();
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, subjectTypeName+"分班失败",TIME_ONE_HOUR);
			}catch (Exception e) {
				e.printStackTrace();
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, subjectTypeName+"分班失败",TIME_ONE_HOUR);
			}
				
		}
		/**
		 * 
		 * @param divideId
		 * @param result
		 * @param rangeMap
		 * @param startbath
		 * @param subjectType
		 * @param relateSubjectId
		 * @param subjectNameMap
		 * @param twoClassList 2+x 2可以哪个批次   半固定模式需要这个参数保存 否则不保存
		 */
		private void makeAndSave(NewGkDivide divide,SectioningSolution result,Map<String, ArrangeCapacityRange> rangeMap,
				int startbath,String subjectType,Map<String, String> relateSubjectId,
				Map<String, Course> subjectNameMap,List<NewGkDivideClass> twoClassList,
				List<NewGkDivideClass> insertClassList,List<NewGkClassStudent> insertStuList,
				Set<String> delClassIds) {			
			List<TimeSlot> list = result.getTimeSlotList();
			List<NewGkDivideClass> divideClassList = new ArrayList<NewGkDivideClass>();
			List<NewGkClassStudent> classStuList = new ArrayList<NewGkClassStudent>();
			Map<String, Integer> subjectNumMap = new HashMap<String, Integer>();
			startbath = 0;
			for(TimeSlot t:list){
				List<StudentCourse> studentCourseList = t.getStudentCourseList();
				if(CollectionUtils.isEmpty(studentCourseList)){
					continue;
				}
				Map<TakeCourse,List<StudentCourse>> stuByCourseMap=new HashMap<TakeCourse,List<StudentCourse>>();
				for(StudentCourse s:studentCourseList){
					if(!stuByCourseMap.containsKey(s.getCourse())){
						stuByCourseMap.put(s.getCourse(), new ArrayList<StudentCourse>());
					}
					stuByCourseMap.get(s.getCourse()).add(s);
				}
				for(Entry<TakeCourse, List<StudentCourse>> item:stuByCourseMap.entrySet()){
					TakeCourse subject=item.getKey();
					String subId=subject.getCourseId();
					String realSubjectId = relateSubjectId.get(subId);
					String bestType="";
					if(!realSubjectId.equals(subId)){
						bestType=subId.substring(subId.length()-1);
					}
					ArrangeCapacityRange range = rangeMap.get(subId);
					List<StudentCourse> stuList = item.getValue();
					List<Integer> arrlist = CalculateSections.calculateSectioning2(stuList.size(), range.getAvgNum(), range.getFolNum());
					int maxIndex=0;
					for(Integer ii:arrlist){
						List<StudentCourse> chooseList = subList(stuList, maxIndex, maxIndex+ii);
						if(CollectionUtils.isEmpty(chooseList)){
							break;
						}
						Set<String> stuids = new HashSet<String>();
						for(StudentCourse s:chooseList){
							stuids.add(s.getStudentId());
						}
						toDivideClass(divide, realSubjectId, bestType, subjectType, stuids, subjectNumMap, startbath+t.getId(), subjectNameMap, divideClassList, classStuList);
						maxIndex=maxIndex+ii;
					}
				}
			}
			//分班结果重新修改名称divideClassList--教学班的
			if(CollectionUtils.isNotEmpty(divideClassList)){
				//根据subjectId,subjectType(都是一样的),bestType排序
				Collections.sort(divideClassList, new Comparator<NewGkDivideClass>() {
					@Override
					public int compare(NewGkDivideClass o1, NewGkDivideClass o2) {
						
						if(o1.getSubjectIds().equals(o2.getSubjectIds())){
							if(o1.getSubjectType().equals(o1.getSubjectType())){
								if(StringUtils.isBlank(o1.getBestType()) || StringUtils.isBlank(o2.getBestType()) ){
									return 0;
								}else{
									return o1.getBestType().compareTo(o2.getBestType());
								}
							}
							return o1.getSubjectType().compareTo(o2.getSubjectType());
						}
						return o1.getSubjectIds().compareTo(o2.getSubjectIds());
					}
				});
				
				Map<String,Integer> subjectNumMap2=new HashMap<String,Integer>();
				
				for(NewGkDivideClass ss:divideClassList){
					String subjectId=ss.getSubjectIds();
					if(!subjectNumMap2.containsKey(subjectId)){
						subjectNumMap2.put(subjectId,1);
					}else{
						subjectNumMap2.put(subjectId,subjectNumMap2.get(subjectId)+1);
					}
					String sss="";
					if (StringUtils.isNotBlank(ss.getBestType())) {
						sss = "(" + ss.getBestType() + ")";
					} 
					String name=subjectNameMap.get(subjectId).getSubjectName()
							+ subjectType + sss + subjectNumMap2.get(subjectId) + "班";
					ss.setClassName(name);
					ss.setOrderId(subjectNumMap2.get(subjectId));//同科目A排序 或者同科目B排序
				}
			}
			
			Set<String> delClassIdSet = new HashSet<String>();
			List<NewGkDivideClass> oldlist = newGkDivideClassService
					.findByDivideIdAndClassType(
							divide.getUnitId(),
							divide.getId(),
							new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);

			if (CollectionUtils.isNotEmpty(list)) {
				for (NewGkDivideClass cc : oldlist) {
					if (cc.getSubjectType().equals(subjectType)) {
						delClassIdSet.add(cc.getId());
					}
				}
				
			}
			if(CollectionUtils.isNotEmpty(twoClassList)){
				divideClassList.addAll(twoClassList);
			}
			if(CollectionUtils.isNotEmpty(divideClassList)) {
				insertClassList.addAll(divideClassList);
			}
			if(CollectionUtils.isNotEmpty(classStuList)) {
				insertStuList.addAll(classStuList);
			}
			if (CollectionUtils.isNotEmpty(delClassIdSet)) {
				delClassIds.addAll(delClassIdSet);
			}

		}
		
		// 纯组合模式 
		private String autoCombination2(NewGkDivide divide, List<String> subjectAIds,
				List<String> subjectBIds, List<StudentResultDto> stuChooselist,
				int bathA, int bathB) {
			final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_" + divide.getId();
			final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_"
					+ divide.getId() + "_mess";
			
			Set<String> subjectAll = new HashSet<String>();
			subjectAll.addAll(subjectAIds);
			subjectAll.addAll(subjectBIds);
			Map<String, String> relateSubjectId = new HashMap<String, String>();
			for (String ss : subjectAll) {
				relateSubjectId.put(ss, ss);
			}

			List<Course> courseList = SUtils.dt(courseRemoteService
					.findListByIds(subjectAll.toArray(new String[] {})),
					new TR<List<Course>>() {
					});
			Map<String, Course> subjectNameMap = EntityUtils.getMap(courseList,
					e->e.getId());

			Map<String, Map<String, ArrangeCapacityRange>> rangeMap = new HashMap<String, Map<String, ArrangeCapacityRange>>();
			// 范围 每个科目 人数范围  不加入subjectType
			findRange2(divide.getId(), rangeMap,false);
			// 重组结果
			List<NewGkDivideClass> gkDivideClassList = newGkDivideClassService
					.findByDivideIdAndClassType(divide.getUnitId(),
							divide.getId(),
							new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			//学生数据必须都要分派在各种组合，剩余也要随机到混合
			
			// 2+x 3+0  验证一下学生数据是不是正确
			String errorMess = checkStudentIfRight(gkDivideClassList, stuChooselist,false);
			if (StringUtils.isNotBlank(errorMess)) {
				RedisUtils.set(key, "error",TIME_ONE_HOUR);
				RedisUtils.set(keyMess, errorMess,TIME_ONE_HOUR);
				return null;
			}
			//默认生成行政班
			saveXzbClass(divide,gkDivideClassList);
			
			Map<String, ArrangeCapacityRange> rangeAMap = rangeMap
					.get(NewGkElectiveConstant.SUBJECT_TYPE_A);
			Map<String, ArrangeCapacityRange> rangeBMap = rangeMap
					.get(NewGkElectiveConstant.SUBJECT_TYPE_B);

			// 3+0
			Set<String> threeStuId = new HashSet<String>();
			// 2+x 包括混合
			Map<String, String> grouClassIdByStuId = new HashMap<String, String>();
			Map<String, NewGkDivideClass> twoGroupClassMap = new HashMap<String, NewGkDivideClass>();
			List<NewGkDivideClass> twoClassList=new ArrayList<NewGkDivideClass>();
			int roomClassNum=0;
			for (NewGkDivideClass group : gkDivideClassList) {
				if (NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(group
						.getSubjectType())) {
					threeStuId.addAll(group.getStudentList());
				} else {
					if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(group
							.getSubjectType())){
						twoClassList.add(group);
					}
					for (String ss : group.getStudentList()) {
						grouClassIdByStuId.put(ss, group.getId());
					}
					twoGroupClassMap.put(group.getId(), group);
					roomClassNum++;
				}
			}
			// A  groupClassId 已经的安排的组合班id
			List<StudentSubjectDto> stuAList = initStudentNotGroup(stuChooselist,
					threeStuId, grouClassIdByStuId, twoGroupClassMap, subjectAIds,
					NewGkElectiveConstant.SUBJECT_TYPE_A, bathA, key, keyMess);
			if("error".equals(RedisUtils.get(key))) {
				return null;
			}
			// B
			List<StudentSubjectDto> stuBList = initStudentNotGroup(stuChooselist,
					threeStuId, grouClassIdByStuId, twoGroupClassMap, subjectBIds,
					NewGkElectiveConstant.SUBJECT_TYPE_B, bathB, key, keyMess);
			if("error".equals(RedisUtils.get(key))) {
				return null;
			}
			if (CollectionUtils.isEmpty(stuAList)
					&& CollectionUtils.isEmpty(stuBList)) {
				
				RedisUtils.set(key, "success",TIME_ONE_HOUR);
				RedisUtils.set(keyMess, "不存在学生需要安排教学班",TIME_ONE_HOUR);
				return null;
			}
			
			final int roomClassNum1=roomClassNum;
			new Thread(new Runnable() {

				@Override
				public void run() {
					
					List<NewGkDivideClass> insertClassList=new ArrayList<NewGkDivideClass>();
					List<NewGkClassStudent> insertStuList=new ArrayList<NewGkClassStudent>();
					Set<String> delClassIds=new HashSet<String>();
					if (CollectionUtils.isEmpty(stuBList)) {
						System.out.println("不存在学生需要安排学考");
					} else {
						// 排B
						if(!isChooseNewFunction){
							autoArrangeOrBestTypes2(divide,
								NewGkElectiveConstant.SUBJECT_TYPE_B, stuBList, rangeBMap,
								bathB, relateSubjectId, subjectNameMap, bathA, key,
								keyMess,insertClassList,insertStuList,delClassIds);
						}else{
							autoArrangeOrBestTypes2New(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_B, stuBList, rangeBMap,
									bathB, relateSubjectId, subjectNameMap, bathA, key,
									keyMess,roomClassNum1,insertClassList,insertStuList,delClassIds);
						}

						
					}
					if("error".equals(RedisUtils.get(key))) {
						return ;
					}
					if (CollectionUtils.isEmpty(stuAList)) {
						RedisUtils.set(key, "success",TIME_ONE_HOUR);
						RedisUtils.set(keyMess, "不存在学生需要安排选考",TIME_ONE_HOUR);
					} else {
						// 排A
						if(!isChooseNewFunction){
							autoArrangeOrBestTypes3(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_A, stuAList, rangeAMap,
									bathA, relateSubjectId, subjectNameMap, 0, key,
									keyMess,twoClassList,insertClassList,insertStuList,delClassIds);
						}else{
							int maxRoomCount=gkDivideClassList.size()-twoClassList.size();
							
							autoArrangeOrBestTypes3New(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_A, stuAList, rangeAMap,
									bathA, relateSubjectId, subjectNameMap, 0, key,
									keyMess,twoClassList,maxRoomCount,insertClassList,insertStuList,delClassIds);
						}
						
						
					}
					if("error".equals(RedisUtils.get(key))) {
						return ;
					}
					try {
						long start = System.currentTimeMillis();
						String[] delClassId=null;
						if (CollectionUtils.isNotEmpty(delClassIds)) {
							delClassId = delClassIds.toArray(new String[] {});
							
						}
						
						newGkDivideClassService.saveAllList(divide.getUnitId(), divide.getId(),
								delClassId, insertClassList, insertStuList, true);
	
						long end = System.currentTimeMillis();
	
						System.out.println("-----saveBatchs耗时："
								+ (end - start) / 1000 + "s");
						RedisUtils.set(keyMess, "分班成功",TIME_ONE_HOUR);
						RedisUtils.set(key, "success",TIME_ONE_HOUR);
					}catch (Exception e) {
						e.printStackTrace();
						RedisUtils.set(keyMess, "分班失败",TIME_ONE_HOUR);
						RedisUtils.set(key, "error",TIME_ONE_HOUR);
					}
				}
			}).start();

			return null;

		}
		/**
		 * 2+x x与混合一起
		 * @param divideId
		 * @param subjectType
		 * @param students
		 * @param rangeMap
		 * @param bath
		 * @param relateSubjectId
		 * @param subjectNameMap
		 * @param startbath
		 * @param redisKey
		 * @param redisKeyMess
		 * @param twoClassList
		 */
		private void autoArrangeOrBestTypes3(NewGkDivide divide, String subjectType,
				List<StudentSubjectDto> students,
				Map<String, ArrangeCapacityRange> rangeMap, int bath,
				Map<String, String> relateSubjectId,
				Map<String, Course> subjectNameMap, int startbath, String redisKey,
				String redisKeyMess,List<NewGkDivideClass> twoClassList,
				List<NewGkDivideClass> insertClassList,List<NewGkClassStudent> insertStuList,
				Set<String> delClassIds) {
			String subjectTypeName;
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)){
				subjectTypeName="选考";
			}else{
				subjectTypeName="学考";
			}
			//组装数据
			SectioningSolution sectioningSolution=new SectioningSolution();
			sectioningSolution.setId(1);
			sectioningSolution.setAppName(subjectTypeName+"分班排课");
			
			Map<String, Integer> courseSectionSizeMeanMap=new HashMap<String, Integer>();
			Map<String, Integer> courseSectionSizeMarginMap=new HashMap<String, Integer>();
			Map<String, Integer> courseSectionSizeClassMap=new HashMap<String, Integer>();
			for(Entry<String, ArrangeCapacityRange> range:rangeMap.entrySet()){
				String subId=range.getKey();
				ArrangeCapacityRange arrangeRange=range.getValue();
				courseSectionSizeMeanMap.put(subId, arrangeRange.getAvgNum());
				courseSectionSizeMarginMap.put(subId, arrangeRange.getFolNum());
				courseSectionSizeClassMap.put(subId, arrangeRange.getClassNum());
			}
			List<TimeSlot> timeSlotList=new ArrayList<TimeSlot>();
			Map<Integer,TimeSlot> timeSlotMap=new HashMap<Integer,TimeSlot>();
			TimeSlot timeSlot;
			for(int i=1;i<=bath;i++){
				timeSlot=new TimeSlot();
				timeSlot.setId(i);
				timeSlot.setTimeSlotName(subjectTypeName+i);
				timeSlot.setStudentCourseList(new ArrayList<StudentCourse>());
				timeSlotList.add(timeSlot);
				timeSlotMap.put(i, timeSlot);
			}
			sectioningSolution.setTimeSlotList(timeSlotList);
			Map<String,List<TimeSlot>> groupTimeMap=new HashMap<String,List<TimeSlot>>();
			if(CollectionUtils.isNotEmpty(twoClassList)){
				//暂时先不考虑bath<3
				for(NewGkDivideClass clazz:twoClassList){
					List<TimeSlot> oneTimeSlotList=new ArrayList<TimeSlot>();
					oneTimeSlotList.addAll(timeSlotList);
					Collections.shuffle(oneTimeSlotList);
					//去除后2个
					List<TimeSlot> chooseTimeList = subList(oneTimeSlotList, 0, oneTimeSlotList.size()-2);
					String baths="";
					for(TimeSlot t:timeSlotList){
						if(!chooseTimeList.contains(t)){
							//剩余两个
							baths=baths+","+t.getId();
						}
					}
					if(StringUtils.isNotBlank(baths)){
						baths=baths.substring(1);
					}
					clazz.setBatch(baths);
					clazz.setModifyTime(new Date());
//					for(TimeSlot t:chooseTimeList){
//						System.out.println(clazz.getClassName()+"-----"+t.getId());
//					}
					
					groupTimeMap.put(clazz.getId(), chooseTimeList);
				}
			}
			
					
					
					
			int studentIndex=1;
			List<Student> studentList=new ArrayList<Student>();
			Student student;
			List<String> allNeedArray;
			List<StudentCourse> studentCourseList=new ArrayList<StudentCourse>();
			StudentCourse studentCourse;
			List<TakeCourse> courseList=new ArrayList<TakeCourse>();
			Map<String,TakeCourse> takeCourseMap=new HashMap<String,TakeCourse>();
			TakeCourse takeCourse;
			int courseIndex=1;
			for(StudentSubjectDto stu:students){
				student=new Student();
				student.setId(studentIndex);
				student.setStudentName(stu.getStuName());
				student.setStudentId(stu.getStuId());
				List<TimeSlot> oneTimeSlotList=new ArrayList<TimeSlot>();
				if(groupTimeMap.containsKey(stu.getGroupClassId())){
					if(CollectionUtils.isNotEmpty(groupTimeMap.get(stu.getGroupClassId()))){
						oneTimeSlotList.addAll(groupTimeMap.get(stu.getGroupClassId()));
					}
				}else{
					oneTimeSlotList.addAll(timeSlotList);
				}

				allNeedArray = stu.getAllSubjectIds();
				int subIndex=1;
				List<StudentCourse> oneStudentCourse=new ArrayList<StudentCourse>();
				for(String subId:allNeedArray){
					if(subId==null){
						continue;
					}
					if(courseSectionSizeClassMap.get(subId) !=null && courseSectionSizeClassMap.get(subId)<=0){
						continue;//说明科目下没有班级不需要处理
					}
					
					studentCourse=new StudentCourse();
					takeCourse = takeCourseMap.get(subId);
					if(takeCourse==null){
						takeCourse=new TakeCourse();
						takeCourse.setId(courseIndex);
						String realSubjectId = relateSubjectId.get(subId);
						String bestType="";
						if(!realSubjectId.equals(subId)){
							bestType=subId.substring(subId.length()-1);
						}
						takeCourse.setCourseName(subjectNameMap.get(realSubjectId).getSubjectName()+bestType);
						takeCourse.setCourseId(subId);
						if(!courseSectionSizeMarginMap.containsKey(subId)){
							RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
							RedisUtils.set(redisKeyMess, takeCourse.getCourseName()+"没有设置平均班级人数",TIME_ONE_HOUR);
							System.out.println(takeCourse.getCourseName()+"没有设置平均班级人数");
							return;
						}
						takeCourse.setSectionSizeMargin(courseSectionSizeMarginMap.get(subId));
						takeCourse.setSectionSizeMean(courseSectionSizeMeanMap.get(subId));
						courseIndex++;
						courseList.add(takeCourse);
						takeCourseMap.put(subId, takeCourse);
					}
					studentCourse.setCourse(takeCourse);
					studentCourse.setId(subIndex);
					subIndex++;
					studentCourse.setStudent(student);
					//所有批次
					studentCourse.setTimeSlotDomain(new ArrayList<TimeSlot>());
					studentCourse.getTimeSlotDomain().addAll(oneTimeSlotList);
					
					studentCourseList.add(studentCourse);
					oneStudentCourse.add(studentCourse);
				}
				//随机初始化给值studentCourse.setTimeSlot();
				//oneTimeSlotList
				
				if(CollectionUtils.isEmpty(oneTimeSlotList) || oneTimeSlotList.size()<oneStudentCourse.size()){
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, student.getStudentName()+subjectType+"设置选考或者学考时间点数量不对，分班失败,请联系管理员",TIME_ONE_HOUR);
					return ;
				}
				Collections.shuffle(oneTimeSlotList);
				for (int j=0;j<oneStudentCourse.size();j++) {
					TimeSlot oneTime = oneTimeSlotList.get(j);
					oneStudentCourse.get(j).setTimeSlot(oneTime);
					oneTime.getStudentCourseList().add(oneStudentCourse.get(j));
				}

				studentList.add(student);
				studentIndex++;
			}

			sectioningSolution.setStudentList(studentList);
			sectioningSolution.setCourseList(courseList);
			
			sectioningSolution.setStudentCourseList(studentCourseList);

			SectioningApp sectionApp=new SectioningApp();
			try {
				SectioningSolution result = sectionApp.solve(sectioningSolution, false,false);
				makeAndSave(divide, result, rangeMap, startbath, subjectType, relateSubjectId, subjectNameMap,twoClassList,insertClassList,insertStuList,delClassIds);
			} catch (IOException e) {
				e.printStackTrace();
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, subjectTypeName+"分班失败",TIME_ONE_HOUR);
				
			}catch (Exception e) {
				e.printStackTrace();
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, subjectTypeName+"分班失败",TIME_ONE_HOUR);
				
			}
			

			
		}
		
		
		
		
		
		
		
		
		/**
		 * 2+x x与混合一起
		 */
		private void autoArrangeOrBestTypes3New(NewGkDivide divide, String subjectType,
				List<StudentSubjectDto> students,
				Map<String, ArrangeCapacityRange> rangeMap, int bath,
				Map<String, String> relateSubjectId,
				Map<String, Course> subjectNameMap, int startbath, String redisKey,
				String redisKeyMess,List<NewGkDivideClass> twoClassList,int maxRoomCount,
				List<NewGkDivideClass> insertClassList,List<NewGkClassStudent> insertStuList,
				Set<String> delClassIds) {
			String subjectTypeName;
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)){
				subjectTypeName="选考";
			}else{
				subjectTypeName="学考";
			}
			//组装数据
			
			ShufflingApp shufflingApp=new ShufflingApp();
			
			ShufflingAppDto shufflingAppDto = new ShufflingAppDto();
			shufflingAppDto.setTimeSlotCount(bath);
			shufflingAppDto.setMaxRoomCount(maxRoomCount);
			List<List<String>> sectionSizeList = new ArrayList<List<String>>();
			Map<String, Integer> courseSectionSizeClassMap=new HashMap<String, Integer>();//页面维护开设班级数量
			for(Entry<String, ArrangeCapacityRange> range:rangeMap.entrySet()){
				String subId=range.getKey();
				ArrangeCapacityRange arrangeRange=range.getValue();
				List<String> arr=new ArrayList<String>();
				arr.add(subId);
				arr.add(""+arrangeRange.getAvgNum());
				arr.add(""+arrangeRange.getFolNum());
				courseSectionSizeClassMap.put(subId, arrangeRange.getClassNum());
				sectionSizeList.add(arr);
			}
			shufflingAppDto.setSectionSizeList(sectionSizeList);
			
			
			
			Map<String,Integer> classMap=new HashMap<String,Integer>();
			if(CollectionUtils.isNotEmpty(twoClassList)){
				//暂时先不考虑bath<3
				for(NewGkDivideClass clazz:twoClassList){
					//暂时就固定一个时间 只考虑bath==3
					//从1。。。bath 随机取一个值 作为X部分
					Random rd = new Random(); //创建一个Random类对象实例
			        int x = rd.nextInt(bath)+1; //生成1-3之间的随机数，rd.nextInt(3)表示生成0-2之间的数，+1就可以得到1-3的数了
			        classMap.put(clazz.getId(), x);
			        String xxx="";
			        for(int ii=1;ii<=bath;ii++){
			        	if(x!=ii){
			        		xxx=xxx+","+ii;
			        	}
			        }
			        if(StringUtils.isNotBlank(xxx)){
			        	xxx=xxx.substring(1);//如果bath==4 那么这个地方会有3个参数
			        }
			        clazz.setBatch(xxx);
				}
			}

			List<List<String>> studentCourseSelectionList=new ArrayList<List<String>>();
			List<List<String>> pre1xList=new ArrayList<List<String>>();
			for(StudentSubjectDto stu:students){
				List<String> arr=new ArrayList<String>();
				arr.add(stu.getStuId());
				List<String> allNeedArray = stu.getAllSubjectIds();
				for(String subId:allNeedArray){
					if(subId==null){
						continue;
					}
					if(courseSectionSizeClassMap.get(subId) !=null && courseSectionSizeClassMap.get(subId)<=0){
						continue;//说明科目下没有班级不需要处理
					}
					if(!courseSectionSizeClassMap.containsKey(subId)){
						
						String realSubjectId = relateSubjectId.get(subId);
						String bestType="";
						if(!realSubjectId.equals(subId)){
							bestType=subId.substring(subId.length()-1);
						}
						String name = subjectNameMap.get(realSubjectId).getSubjectName()+bestType;
						RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
						RedisUtils.set(redisKeyMess, name+"没有设置平均班级人数",TIME_ONE_HOUR);
						return;
					}
					arr.add(subId);
				}
				if(arr.size()<=1){
					continue;
				}
				if(arr.size()-1>bath){
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, stu.getStuName()+"设置"+subjectTypeName+"时间点数量不对，分班失败,请联系管理员",TIME_ONE_HOUR);
					return ;
				}
				
				if(classMap.containsKey(stu.getGroupClassId())){
					if(arr.size()-1!=1){
						RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
						RedisUtils.set(redisKeyMess, stu.getStuName()+"设置"+subjectTypeName+"时间点数量不对，分班失败,请联系管理员",TIME_ONE_HOUR);
						return ;
					}
					List<String> arr2=new ArrayList<String>();
					arr2.add(stu.getStuId());
					arr2.add(arr.get(1));
					arr2.add("T"+classMap.get(stu.getGroupClassId()));
					pre1xList.add(arr2);
				}else{
					//去除2+x
					studentCourseSelectionList.add(arr);
				}
				
				
				
			}
			if(CollectionUtils.isEmpty(studentCourseSelectionList)){
				RedisUtils.set(redisKey, "success",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, subjectTypeName+"分班成功",TIME_ONE_HOUR);
				return;
			}
			shufflingAppDto.setStudentCourseSelectionList(studentCourseSelectionList);
			shufflingAppDto.setPre1XList(pre1xList);
	
			try {
				List<List<String>> result = shufflingApp.solve(shufflingAppDto);
				makeAndSaveNew(divide, result, rangeMap, startbath, subjectType, relateSubjectId, subjectNameMap,twoClassList,insertClassList,insertStuList,delClassIds);
			} catch (IOException e) {
				e.printStackTrace();
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, subjectTypeName+"分班失败",TIME_ONE_HOUR);
				return ;
			}catch (Exception e) {
				e.printStackTrace();
				RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
				RedisUtils.set(redisKeyMess, subjectTypeName+"分班失败",TIME_ONE_HOUR);
				return ;
			}
					

			
			
		}
		
		
		/**
		 * 
		 * @param divideId
		 * @param List<List<String>> stuId,courseName, corusename2,batch
		 * @param rangeMap
		 * @param startbath
		 * @param subjectType
		 * @param relateSubjectId
		 * @param subjectNameMap
		 * @param twoClassList 2+x 2可以哪个批次   半固定模式需要这个参数保存 否则不保存
		 */
		private void makeAndSaveNew(NewGkDivide divide,List<List<String>> result,Map<String, ArrangeCapacityRange> rangeMap,
				int startbath,String subjectType,Map<String, String> relateSubjectId,
				Map<String, Course> subjectNameMap,List<NewGkDivideClass> twoClassList,
				List<NewGkDivideClass> insertClassList,List<NewGkClassStudent> insertStuList,
				Set<String> delClassIds) {	
			String subjectTypeName="";
			if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
				subjectTypeName="选";
			}else if(NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType)) {
				subjectTypeName="学";
			}
			//分班，按时间点+教学班ID
			//resultStudentList: {<studentID> <选课> <教学班ID> <时间点>}
			Map<String, List<List<String>>> jxbList=new HashMap<String, List<List<String>>>();
			for(List<String> ee:result){
				//教学班ID:subjectId+数字
				String key=ee.get(3)+"_"+ee.get(2)+"_"+ee.get(1);//T1+"_"+教学班ID+"_"+subjectId
				if(!jxbList.containsKey(key)){
					jxbList.put(key, new ArrayList<List<String>>());
				}
				jxbList.get(key).add(ee);
			}
			List<NewGkDivideClass> divideClassList = new ArrayList<NewGkDivideClass>();
			List<NewGkClassStudent> classStuList = new ArrayList<NewGkClassStudent>();
			
			Map<String, Integer> subjectNumMap = new HashMap<String, Integer>();
			startbath=0;
			//组装数据
			for(Entry<String, List<List<String>>> item:jxbList.entrySet()){
				//T1+"_"+教学班ID+"_"+subjectId
				String key=item.getKey();
				key.substring(0, key.length()-1);
				String[] tt = key.split("_");
				int tBath=Integer.parseInt(tt[0].substring(1));
				String subId=tt[2];
				
				String realSubjectId = relateSubjectId.get(subId);
				String bestType="";
				if(!realSubjectId.equals(subId)){
					bestType=subId.substring(subId.length()-1);
				}
				Set<String> stuids = new HashSet<String>();
				for(List<String> stu:item.getValue()){
					stuids.add(stu.get(0));
				}
				
				toDivideClass(divide, realSubjectId, bestType, subjectType, stuids, subjectNumMap, startbath+tBath, subjectNameMap, divideClassList, classStuList);
			}
			

			//分班结果重新修改名称divideClassList--教学班的
			if(CollectionUtils.isNotEmpty(divideClassList)){
				//根据subjectId,subjectType(都是一样的),bestType排序
				Collections.sort(divideClassList, new Comparator<NewGkDivideClass>() {
					@Override
					public int compare(NewGkDivideClass o1, NewGkDivideClass o2) {
						
						if(o1.getSubjectIds().equals(o2.getSubjectIds())){
							if(o1.getSubjectType().equals(o1.getSubjectType())){
								if(StringUtils.isBlank(o1.getBestType()) || StringUtils.isBlank(o2.getBestType()) ){
									return 0;
								}else{
									return o1.getBestType().compareTo(o2.getBestType());
								}
							}
							return o1.getSubjectType().compareTo(o2.getSubjectType());
						}
						return o1.getSubjectIds().compareTo(o2.getSubjectIds());
					}
				});
				
				Map<String,Integer> subjectNumMap2=new HashMap<String,Integer>();
				
				for(NewGkDivideClass ss:divideClassList){
					String subjectId=ss.getSubjectIds();
					if(!subjectNumMap2.containsKey(subjectId)){
						subjectNumMap2.put(subjectId,1);
					}else{
						subjectNumMap2.put(subjectId,subjectNumMap2.get(subjectId)+1);
					}
					String sss="";
					if (StringUtils.isNotBlank(ss.getBestType())) {
						sss = "(" + ss.getBestType() + ")";
					} 
					String name=subjectNameMap.get(subjectId).getSubjectName()
							+ subjectTypeName + sss + subjectNumMap2.get(subjectId) + "班";
					ss.setClassName(name);
					ss.setOrderId(subjectNumMap2.get(subjectId));
				}
			}
			

			Set<String> delClassIdSet = new HashSet<String>();
			List<NewGkDivideClass> oldlist = newGkDivideClassService
					.findByDivideIdAndClassType(
							divide.getUnitId(),
							divide.getId(),
							new String[] { NewGkElectiveConstant.CLASS_TYPE_2 }, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);

			if (CollectionUtils.isNotEmpty(result)) {
				for (NewGkDivideClass cc : oldlist) {
					if (cc.getSubjectType().equals(subjectType)) {
						delClassIdSet.add(cc.getId());
					}
				}
			}
			if(CollectionUtils.isNotEmpty(twoClassList)){
				divideClassList.addAll(twoClassList);
			}
			
			if(CollectionUtils.isNotEmpty(divideClassList)) {
				insertClassList.addAll(divideClassList);
			}
			if(CollectionUtils.isNotEmpty(classStuList)) {
				insertStuList.addAll(classStuList);
			}
			if (CollectionUtils.isNotEmpty(delClassIdSet)) {
				delClassIds.addAll(delClassIdSet);
			}
			
		}
		
		private void autoArrangeOrBestTypes2New(NewGkDivide divide, String subjectType,
				List<StudentSubjectDto> students,
				Map<String, ArrangeCapacityRange> rangeMap, int bath,
				Map<String, String> relateSubjectId,
				Map<String, Course> subjectNameMap, int startbath, String redisKey,
				String redisKeyMess,int maxRoomCount,
				List<NewGkDivideClass> insertClassList,List<NewGkClassStudent> insertStuList,
				Set<String> delClassIds) {
			
				String subjectTypeName;
				if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)){
					subjectTypeName="选考";
				}else if(NewGkElectiveConstant.SUBJECT_TYPE_O.equals(subjectType)){
					subjectTypeName="语数英";
				}else if("C".equals(subjectType)){
					subjectTypeName="高二学考";
				}else{
					subjectTypeName="学考";
				}
				ShufflingApp shufflingApp=new ShufflingApp();
				
				ShufflingAppDto shufflingAppDto = new ShufflingAppDto();
				shufflingAppDto.setTimeSlotCount(bath);
				shufflingAppDto.setMaxRoomCount(maxRoomCount);
				List<List<String>> sectionSizeList = new ArrayList<List<String>>();
				Map<String, Integer> courseSectionSizeClassMap=new HashMap<String, Integer>();//页面维护开设班级数量
				//根据人数范围给定 获得最大可容纳学生人数
				Map<String,Integer> maxStuIdBySubId=new HashMap<>();
				int allOpenClassNum=0;
				for(Entry<String, ArrangeCapacityRange> range:rangeMap.entrySet()){
					String subId=range.getKey();
					ArrangeCapacityRange arrangeRange=range.getValue();
					List<String> arr=new ArrayList<String>();
					arr.add(subId);
					arr.add(""+arrangeRange.getAvgNum());
					arr.add(""+arrangeRange.getFolNum());
					
					arr.add(""+arrangeRange.getClassNum());
					allOpenClassNum=allOpenClassNum+arrangeRange.getClassNum();
					courseSectionSizeClassMap.put(subId, arrangeRange.getClassNum());
					sectionSizeList.add(arr);
					
					maxStuIdBySubId.put(subId, (arrangeRange.getAvgNum()+arrangeRange.getFolNum())*arrangeRange.getClassNum());
				}
				shufflingAppDto.setSectionSizeList(sectionSizeList);
				
				
				List<List<String>> studentCourseSelectionList=new ArrayList<List<String>>();
				List<List<String>> pre1xList=new ArrayList<List<String>>();
				Map<String,Integer> stuNumBySubId=new HashMap<>();
				for(StudentSubjectDto stu:students){
					List<String> arr=new ArrayList<String>();
					arr.add(stu.getStuId());
					List<String> allNeedArray = stu.getAllSubjectIds();
					for(String subId:allNeedArray){
						if(subId==null){
							continue;
						}
//						if(courseSectionSizeClassMap.get(subId) !=null && courseSectionSizeClassMap.get(subId)<=0){
//							continue;//说明科目下没有班级不需要处理
//						}
						if(!stuNumBySubId.containsKey(subId)) {
							stuNumBySubId.put(subId, 1);
						}else {
							stuNumBySubId.put(subId, stuNumBySubId.get(subId)+1);
						}
						if(!courseSectionSizeClassMap.containsKey(subId)){
							
							String realSubjectId = relateSubjectId.get(subId);
							String bestType="";
							if(!realSubjectId.equals(subId)){
								bestType=subId.substring(subId.length()-1);
							}
							String name = subjectNameMap.get(realSubjectId).getSubjectName()+bestType;
							RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
							RedisUtils.set(redisKeyMess, name+"没有设置平均班级人数",TIME_ONE_HOUR);
							return;
						}
						arr.add(subId);
					}
					if(arr.size()<=1){
						continue;
					}
					if(arr.size()-1>bath){
						RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
						RedisUtils.set(redisKeyMess, stu.getStuName()+"设置"+subjectTypeName+"时间点数量不对，分班失败,请联系管理员",TIME_ONE_HOUR);
						return ;
					}
					
					studentCourseSelectionList.add(arr);
				}
				for(Entry<String, Integer> uu:stuNumBySubId.entrySet()) {
					String realSubjectId=relateSubjectId.get(uu.getKey());
					String bestType="";
					if(!realSubjectId.equals(uu.getKey())){
						bestType=uu.getKey().substring(uu.getKey().length()-1);
					}
					String name = subjectNameMap.get(realSubjectId).getSubjectName()+bestType;
					if(!maxStuIdBySubId.containsKey(uu.getKey())) {
						
						RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
						RedisUtils.set(redisKeyMess, subjectTypeName+":"+name+"未设置范围",TIME_ONE_HOUR);
						return ;
					}
					if(uu.getValue()>maxStuIdBySubId.get(uu.getKey())) {
						RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
						RedisUtils.set(redisKeyMess, subjectTypeName+":"+name+"设置范围不合理，实际需要安排的学生人数："+uu.getValue()+",参数范围控制最大人数："+maxStuIdBySubId.get(uu.getKey()),TIME_ONE_HOUR);
						return ;
					}
				}
				if(CollectionUtils.isEmpty(studentCourseSelectionList)){
					RedisUtils.set(redisKey, "success",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, subjectTypeName+"分班成功",TIME_ONE_HOUR);
					return;
				}
				//设置班级数量超过行政班数量
				if(allOpenClassNum>maxRoomCount*bath) {
					shufflingAppDto.setMaxRoomCount((allOpenClassNum-1)/bath+1);
				}
				shufflingAppDto.setStudentCourseSelectionList(studentCourseSelectionList);
				shufflingAppDto.setPre1XList(pre1xList);

				try {
					List<List<String>> result = shufflingApp.solve(shufflingAppDto);
					
					makeAndSaveNew(divide, result, rangeMap, startbath, subjectType, relateSubjectId, subjectNameMap,null,insertClassList,insertStuList,delClassIds);
					//RedisUtils.set(redisKey, "success");
					//RedisUtils.set(redisKeyMess, subjectTypeName+"分班成功");
				} catch (IOException e) {
					e.printStackTrace();
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, subjectTypeName+"分班失败",TIME_ONE_HOUR);
					return ;
				}catch (Exception e) {
					e.printStackTrace();
					RedisUtils.set(redisKey, "error",TIME_ONE_HOUR);
					RedisUtils.set(redisKeyMess, subjectTypeName+"分班失败",TIME_ONE_HOUR);
					return ;
				}

			
		}
		
		
		@ResponseBody
		@RequestMapping("/openClassArrange/checkAllInGroup")
		@ControllerInfo(value = "验证2+x,3+0")
		public String checkAllInGroup(@PathVariable String divideId) {
			NewGkDivide gkDivide = newGkDivideService.findById(divideId);
			List<StudentResultDto> stuChooselist = newGkChoResultService
					.findGradeIdList(gkDivide.getUnitId(),gkDivide.getGradeId(),gkDivide.getChoiceId(), null);
			List<NewGkDivideClass> gkDivideClassList = newGkDivideClassService
					.findByDivideIdAndClassTypeWithMaster(gkDivide.getUnitId(),
							divideId,
							new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			boolean isAllTwo=false;
			if(gkDivide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_01)){
				isAllTwo=true;
			}
			if (CollectionUtils.isEmpty(gkDivideClassList)) {
				if(isAllTwo){
					return error("没有安排学生组合数据，选择这个分班模式，必须要安排学生位于3+0，或者2+x，使得学生最后x在同一个时间上课");
				}else if(gkDivide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_06)){
					return error("没有安排学生组合数据，选择这个分班模式，必须要安排学生位于3+0，或者混合");
				} else {
					return error("没有安排学生组合数据，选择这个分班模式，必须要安排学生位于3+0，或者2+x，或者混合");
				}
				
			}
			// 2+x 3+0 // 验证一下学生数据是不是正确，有没有在2个班级内
			String errorMess = checkStudentIfRight(gkDivideClassList, stuChooselist,isAllTwo);
			if (StringUtils.isNotBlank(errorMess)) {
				return error(errorMess);
			}
			//是不是都是3+0 无需走到下一步
			int notThree=0;
			for(NewGkDivideClass item:gkDivideClassList){
				if(!NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(item.getSubjectType())){
					notThree++;
				}
			}
			if(notThree==0){
				try{
					saveXzbClass(gkDivide, gkDivideClassList);
					newGkDivideService.updateStat(divideId, NewGkElectiveConstant.IF_1);
					return success("allArrange");
				}catch(Exception e){
					return error("全部为3+0组合，直接保存失败");
				}
			}
			return success("");
		}
		
		@RequestMapping("/arrangeAList/page")
		@ControllerInfo(value = "分班下一步-全固定模式安排学生选考下一步")
		public String showArrangeAList(@PathVariable String divideId, ModelMap map){
			NewGkDivide divide = newGkDivideService.findOne(divideId);
			if(divide == null || (divide.getIsDeleted() != null && divide.getIsDeleted() == 1)) {
	    		return errorFtl(map, "分班记录不存在或已被删除！");
	    	}
			map.put("gradeId", divide.getGradeId());
			NewGkChoice choice = newGkChoiceService.findOne(divide.getChoiceId());
			if(choice == null || (choice.getIsDeleted() != null && choice.getIsDeleted() == 1)) {
	    		return errorFtl(map, "选课记录不存在或已被删除！");
	    	}
			List<String> subjectIdList = newGkChoRelationService.findByChoiceIdAndObjectType(choice.getUnitId(), choice.getId(), NewGkElectiveConstant.CHOICE_TYPE_01);
			if(CollectionUtils.isEmpty(subjectIdList)){
				return errorFtl(map, "选课的科目记录不存在或已被删除！");
			}
			
			if(!divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_01)) {
				return showSingleList2(divideId, map);
			}
			//自动安排A--暂时不验证
			//列表：科目：人数
			List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService
					.findByDivideIdAndSubjectTypeIn(divideId, new String[] {
							NewGkElectiveConstant.SUBJECT_TYPE_A});
			Set<String> subIds=EntityUtils.getSet(openSubjectList, NewGkOpenSubject::getSubjectId);
			
			List<NewGkDivideClass> gkDivideClassList = newGkDivideClassService
					.findByDivideIdAndClassType(divide.getUnitId(),
							divideId,
							new String[] { NewGkElectiveConstant.CLASS_TYPE_0 ,NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			
			Map<String, NewGkDivideClass> twoGroupClassMap = new HashMap<String, NewGkDivideClass>();
			List<NewGkDivideClass> twoClassList=new ArrayList<>();
			List<NewGkDivideClass> allClassList=new ArrayList<>();

			for (NewGkDivideClass group : gkDivideClassList) {
				if(NewGkElectiveConstant.CLASS_TYPE_0.equals(group.getClassType())) {
					if (NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(group
							.getSubjectType())) {
						continue;
					} else if(NewGkElectiveConstant.SUBJTCT_TYPE_0.equals(group
							.getSubjectType())){
						//存在混合
						return errorFtl(map, "存在不是三科或者两科的组合！");
					}else if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(group
							.getSubjectType())){
						twoClassList.add(group);
						allClassList.add(group);
						twoGroupClassMap.put(group.getId(), group);
					}else {
						return errorFtl(map, "存在不是三科或者两科的组合！");
					}
				}else {
					//教学班
					if(NewGkElectiveConstant.SUBJECT_TYPE_A.equals(group.getSubjectType())) {
						allClassList.add(group);
					}
				}
				
			}
			
			if(CollectionUtils.isEmpty(twoClassList)) {
				//没有2科组合
				return showSingleList3(divideId, map);
			}
			List<Course> courseList = SUtils.dt(courseRemoteService
					.findListByIds(subIds.toArray(new String[] {})),
					new TR<List<Course>>() {
					});
			Map<String, Course> subjectNameMap = EntityUtils.getMap(courseList,
					Course::getId);
			// 2+x
			Map<String, String> grouClassIdByStuId = new HashMap<String, String>();
			//jxb
			Map<String, Integer> subjectIdByStuId = new HashMap<String, Integer>();
			newGkDivideClassService.toMakeStudentList(divide.getUnitId(),divideId, allClassList);
			Map<String,List<NewGkDivideClass>> subjectByClass=new HashMap<>();
			//已经安排的科目下的学生id
			Map<String,Set<String>> arrangeStuMap=new HashMap<>();
			for(NewGkDivideClass  group : allClassList) {
				if(NewGkElectiveConstant.CLASS_TYPE_0.equals(group.getClassType())) {
					if(CollectionUtils.isNotEmpty(group.getStudentList())){
						for (String string : group.getStudentList()) {
							grouClassIdByStuId.put(string, group.getId());
						}
					}
				}else {
					String key=group.getSubjectIds();
					if(!subjectByClass.containsKey(key)) {
						subjectByClass.put(key, new ArrayList<>());
					}
					subjectByClass.get(key).add(group);
					if(CollectionUtils.isNotEmpty(group.getStudentList())){
						group.setStudentCount(group.getStudentList().size());
						if(!subjectIdByStuId.containsKey(group.getSubjectIds())) {
							subjectIdByStuId.put(group.getSubjectIds(), 0);
						}
						subjectIdByStuId.put(group.getSubjectIds(), subjectIdByStuId.get(group.getSubjectIds())+group.getStudentList().size());
						if(!arrangeStuMap.containsKey(group.getSubjectIds())) {
							arrangeStuMap.put(group.getSubjectIds(), new HashSet<>());
						}
						arrangeStuMap.get(group.getSubjectIds()).addAll(group.getStudentList());
					}
				}
			}
			

			//学生选课
			List<StudentResultDto> stuChooselist = newGkChoResultService
					.findGradeIdList(divide.getUnitId(),divide.getGradeId(),divide.getChoiceId(), null);
			
			Map<String,Integer> stuNumABySubjectId=new HashMap<String,Integer>();
			
			Map<String,Set<String>> allSubjectStuId=new HashMap<>();
			for(StudentResultDto s:stuChooselist){
				List<NewGkChoResult> rList = s.getResultList();
				if(CollectionUtils.isEmpty(rList)){
					System.out.println(s.getStudentName()+"没有选课");
					continue;
				}
				if(!grouClassIdByStuId.containsKey(s.getStudentId())){
					continue;
				}
				//2+x
				String groupId = grouClassIdByStuId.get(s.getStudentId());
				NewGkDivideClass groupClass = twoGroupClassMap.get(groupId);
				if(groupClass==null){
					System.out.println(s.getStudentName()+"没有安排组合，一般不会出现这个问题，除非选课数据有所调整");
					continue;
				}
				String subjectIds = groupClass.getSubjectIds();
				Set<String> chooseSet = EntityUtils.getSet(rList, NewGkChoResult::getSubjectId);
				int ii=0;
				for(String ss:chooseSet){
					if(subjectIds.indexOf(ss)>-1){
						//走行政班2+x中2
					}else{
						if(subIds.contains(ss)){
							if(!allSubjectStuId.containsKey(ss)) {
								allSubjectStuId.put(ss, new HashSet<>());
							}
							allSubjectStuId.get(ss).add(s.getStudentId());
							if(!stuNumABySubjectId.containsKey(ss)){
								stuNumABySubjectId.put(ss, 1);
							}else{
								stuNumABySubjectId.put(ss, stuNumABySubjectId.get(ss)+1);
							}
							ii++;
						}
					}
				}
				if(ii>1){
					System.out.println(s.getStudentName()+"学生安排时间超过1个时间点");
					return errorFtl(map, s.getStudentName()+"需要超过一个时间点");
				}
			}
			List<NewGkGroupDto> gDtoList=new ArrayList<>();
			NewGkGroupDto ex = null;
			boolean haserror=false;
			for(String s:subIds){
				Course subject = subjectNameMap.get(s);
				if(subject==null){
					continue;
				}
				ex = new NewGkGroupDto();
				ex.setConditionName(subject.getSubjectName());
				ex.setSubjectIds(subject.getId());
				int allnum=stuNumABySubjectId.get(s)==null?0:stuNumABySubjectId.get(s);
				ex.setAllNumber(allnum);
				int arrangeStuNum = subjectIdByStuId.get(s)==null?0:subjectIdByStuId.get(s);
				ex.setLeftNumber(allnum-arrangeStuNum);
				ex.setGkGroupClassList(new ArrayList<>());
				Set<String> allStuSet = allSubjectStuId.get(s);
				if(CollectionUtils.isEmpty(allStuSet)) {
					allStuSet=new HashSet<>();
				}
				boolean flag=false;
				if(CollectionUtils.isNotEmpty(subjectByClass.get(s))) {
					for(NewGkDivideClass groupClassItem:subjectByClass.get(s)) {
						if(CollectionUtils.isNotEmpty(groupClassItem.getStudentList())) {
							if(CollectionUtils.union(groupClassItem.getStudentList(), allStuSet).size()>allStuSet.size()) {
								//数据有误
								groupClassItem.setNotexists(1);
								if(!flag) {
									flag=true;
								}
							}
						}
						
						ex.getGkGroupClassList().add(groupClassItem);
					}
					
				}
				if(flag) {
					if(!haserror) {
						haserror=true;
					}
					ex.setNotexists(1);
				}
				gDtoList.add(ex);
			}
			map.put("divideId", divideId);
			map.put("gDtoList", gDtoList);
			//判断是否分班完成
			boolean canEdit = false;
			
			if(!NewGkElectiveConstant.IF_1.equals(divide.getStat())){
				//判断是否正在分班
				if(isNowDivide(divideId) ){
					map.put("haveDivideIng", true);
				}else{
					map.put("haveDivideIng", false);
					canEdit = true;
				}
			}
			map.put("isCanEdit", canEdit);
			map.put("haserror", haserror);
			
			return "/newgkelective/divideGroup/arrangeListA.ftl";
		}
		
		private Map<String,List<String>> findStuChoose(String unitId,String choiceId) {
			List<NewGkChoResult> list = newGkChoResultService.findByChoiceIdAndKindType(unitId, NewGkElectiveConstant.KIND_TYPE_01,choiceId);
			Map<String,List<String>> subjectIdsByStuId=new HashMap<String, List<String>>();
			if(CollectionUtils.isNotEmpty(list)) {
				subjectIdsByStuId=EntityUtils.getListMap(list, "studentId", "subjectId");
			}
			
			return subjectIdsByStuId;
		}
	
		
		@RequestMapping("/singleList2/page")
		@ControllerInfo(value = "分班下一步-AB")
		public String showSingleList2(@PathVariable String divideId, ModelMap map){
			NewGkDivide divide = newGkDivideService.findById(divideId);
			if(divide == null || (divide.getIsDeleted() != null && divide.getIsDeleted() == 1)) {
	    		return errorFtl(map, "分班记录不存在或已被删除！");
	    	}
			
			NewGkChoice choice = newGkChoiceService.findById(divide.getChoiceId());
			if(choice == null || (choice.getIsDeleted() != null && choice.getIsDeleted() == 1)) {
	    		return errorFtl(map, "选课记录不存在或已被删除！");
	    	}
			List<String> subjectIdList = newGkChoRelationService.findByChoiceIdAndObjectType(choice.getUnitId(), choice.getId(), NewGkElectiveConstant.CHOICE_TYPE_01);
			if(CollectionUtils.isEmpty(subjectIdList)){
				return errorFtl(map, "选课的科目记录不存在或已被删除！");
			}
			//allChoose 小于chooseNum 暂时不考虑
			int allChoose = subjectIdList.size();
			int chooseNum=choice.getChooseNum()==null?3:choice.getChooseNum();
			int bNum=allChoose-chooseNum;
			if(NewGkElectiveConstant.DIVIDE_TYPE_01.equals(divide.getOpenType())){
				chooseNum=1;
			}
			int subjectANums=0;
			int subjectBNums=0;
			/**
			 * 分班下选考，学考科目数据
			 */
			List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService
					.findByDivideIdAndSubjectTypeIn(divideId, new String[] {
							NewGkElectiveConstant.SUBJECT_TYPE_A,
							NewGkElectiveConstant.SUBJECT_TYPE_B });
			Set<String> subIds=new HashSet<String>();
			List<String> subjectAIds = new ArrayList<String>();
			List<String> subjectBIds = new ArrayList<String>();

			for (NewGkOpenSubject openSubject : openSubjectList) {
				if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(openSubject
						.getSubjectType())) {
					subjectAIds.add(openSubject.getSubjectId());
				} else {
					subjectBIds.add(openSubject.getSubjectId());
				}
				subIds.add(openSubject.getSubjectId());
			}
			
			List<Course> courseList = SUtils.dt(courseRemoteService
					.findListByIds(subIds.toArray(new String[] {})),
					new TR<List<Course>>() {
					});
			Map<String, Course> subjectNameMap = EntityUtils.getMap(courseList,
					Course::getId);
			
			List<NewGkDivideClass> gkDivideClassList = newGkDivideClassService
					.findByDivideIdAndClassType(divide.getUnitId(),
							divideId,
							new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			// 3+0
			Set<String> threeStuId = new HashSet<String>();
			// 2+x
			Map<String, String> grouClassIdByStuId = new HashMap<String, String>();
			Map<String, NewGkDivideClass> twoGroupClassMap = new HashMap<String, NewGkDivideClass>();
			for (NewGkDivideClass group : gkDivideClassList) {
				if (NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(group
						.getSubjectType())) {
					threeStuId.addAll(group.getStudentList());
				} else {
					for (String ss : group.getStudentList()) {
						grouClassIdByStuId.put(ss, group.getId());
					}
					twoGroupClassMap.put(group.getId(), group);
				}
			}
			Map<String,Integer> stuNumABySubjectId=new HashMap<String,Integer>();
			Map<String,Integer> stuNumBBySubjectId=new HashMap<String,Integer>();
			
			//学生选课
//			ChosenSearchDto searchdto = new ChosenSearchDto();
//			searchdto.setGradeId(divide.getGradeId());
//			searchdto.setUnitId(divide.getUnitId());
//			List<StudentResultDto> stuChooselist = newGkChoResultService
//					.findChosenList(divide.getChoiceId(), searchdto);
			Map<String, List<String>> stuChooseSubjectMap = findStuChoose(divide.getUnitId(),divide.getChoiceId());
			List<String> subList;
			for(Entry<String, List<String>> item:stuChooseSubjectMap.entrySet()){
				subList = item.getValue();
				if(CollectionUtils.isEmpty(subList)){
					System.out.println(item.getKey()+"没有选课");
					continue;
				}
				if(threeStuId.contains(item.getKey())){
					continue;
				}
				String groupId = grouClassIdByStuId.get(item.getKey());
				NewGkDivideClass groupClass = twoGroupClassMap.get(groupId);
				if(groupClass==null){
					System.out.println(item.getKey()+"没有安排组合，一般不会出现这个问题，除非选课数据有所调整");
					continue;
				}
				String subjectIds = groupClass.getSubjectIds();
				for(String ss:subList){
					if(subjectIds.indexOf(ss)>-1){
						//走行政班
					}else{
						if(subjectAIds.contains(ss)){
							if(!stuNumABySubjectId.containsKey(ss)){
								stuNumABySubjectId.put(ss, 1);
							}else{
								stuNumABySubjectId.put(ss, stuNumABySubjectId.get(ss)+1);
							}
						}
					}
				}
				for(String bId:subjectBIds){
					if(!subList.contains(bId)){
						if(!stuNumBBySubjectId.containsKey(bId)){
							stuNumBBySubjectId.put(bId, 1);
						}else{
							stuNumBBySubjectId.put(bId, stuNumBBySubjectId.get(bId)+1);
						}
					}
				}
				
			}
			List<NewGKStudentRangeEx> exList = newGKStudentRangeExService.findByDivideId(divideId);
			Map<String,NewGKStudentRangeEx> exBySubjectMap=new HashMap<String,NewGKStudentRangeEx>();
			if(CollectionUtils.isNotEmpty(exList)){
				for(NewGKStudentRangeEx e:exList){
					exBySubjectMap.put(e.getSubjectId()+e.getSubjectType(), e);
				}
			}
			
			
			
			List<NewGKStudentRangeEx> aExList=new ArrayList<NewGKStudentRangeEx>();
			List<NewGKStudentRangeEx> bExList=new ArrayList<NewGKStudentRangeEx>();
			NewGKStudentRangeEx ex = null;
			for(String s:subjectAIds){
				Course subject = subjectNameMap.get(s);
				if(subject==null){
					continue;
				}
				ex = new NewGKStudentRangeEx();
				ex.setSubjectName(subject.getSubjectName());
				ex.setSubjectId(subject.getId());
				ex.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
				ex.setStuNum(stuNumABySubjectId.get(s)==null?0:stuNumABySubjectId.get(s));
				if(exBySubjectMap.containsKey(subject.getId()+NewGkElectiveConstant.SUBJECT_TYPE_A)){
					NewGKStudentRangeEx olsEx = exBySubjectMap.get(subject.getId()+NewGkElectiveConstant.SUBJECT_TYPE_A);
					ex.setLeastNum(olsEx.getLeastNum());
					ex.setMaximum(olsEx.getMaximum());
					ex.setClassNum(olsEx.getClassNum());
				}
				aExList.add(ex);
				subjectANums++;
			}
			for(String s:subjectBIds){
				Course subject = subjectNameMap.get(s);
				if(subject==null){
					continue;
				}
				ex = new NewGKStudentRangeEx();
				ex.setSubjectName(subject.getSubjectName());
				ex.setSubjectId(subject.getId());
				ex.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_B);
				ex.setStuNum(stuNumBBySubjectId.get(s)==null?0:stuNumBBySubjectId.get(s));
				if(exBySubjectMap.containsKey(subject.getId()+NewGkElectiveConstant.SUBJECT_TYPE_B)){
					NewGKStudentRangeEx olsEx = exBySubjectMap.get(subject.getId()+NewGkElectiveConstant.SUBJECT_TYPE_B);
					ex.setLeastNum(olsEx.getLeastNum());
					ex.setMaximum(olsEx.getMaximum());
					ex.setClassNum(olsEx.getClassNum());
				}
				bExList.add(ex);
				subjectBNums++;
			}
			
			
			map.put("aExList", aExList);
			map.put("bExList", bExList);
			//判断是否分班完成
			boolean canEdit = false;
			
			if(!NewGkElectiveConstant.IF_1.equals(divide.getStat())){
				//判断是否正在分班
				if(isNowDivide(divideId) ){
					map.put("haveDivideIng", true);
				}else{
					map.put("haveDivideIng", false);
					canEdit = true;
				}
			}
			map.put("canEdit", canEdit);
			
			boolean allTwo=false;
			if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_01)){
				allTwo=true;
			}
			if(allTwo){
				//默认一个批次
				divide.setBatchCountTypea(1);
			}
			map.put("divide", divide);
			map.put("allTwo", allTwo);
			//默认浮动人数
			map.put("defaultNum", divide.getMaxGalleryful());
			
			if(subjectANums>0 && subjectANums<chooseNum){
				divide.setBatchCountTypea(subjectANums);
			}else{
				divide.setBatchCountTypea(chooseNum);
			}
			if(subjectBNums>0 && subjectBNums<bNum){
				divide.setBatchCountTypeb(subjectBNums);
			}else{
				divide.setBatchCountTypeb(bNum);
			}
			return "/newgkelective/divideGroup/singleArrangeList2.ftl";
		}
		
		@RequestMapping("/singleList3/page")
		@ControllerInfo(value = "分班下一步-手动分班--B")
		public String showSingleList3(@PathVariable String divideId, ModelMap map){
			NewGkDivide divide = newGkDivideService.findById(divideId);
			if(divide == null || (divide.getIsDeleted() != null && divide.getIsDeleted() == 1)) {
	    		return errorFtl(map, "分班记录不存在或已被删除！");
	    	}
			map.put("gradeId", divide.getGradeId());
			NewGkChoice choice = newGkChoiceService.findById(divide.getChoiceId());
			if(choice == null || (choice.getIsDeleted() != null && choice.getIsDeleted() == 1)) {
	    		return errorFtl(map, "选课记录不存在或已被删除！");
	    	}
			List<String> subjectIdList = newGkChoRelationService.findByChoiceIdAndObjectType(choice.getUnitId(), choice.getId(), NewGkElectiveConstant.CHOICE_TYPE_01);
			if(CollectionUtils.isEmpty(subjectIdList)){
				return errorFtl(map, "选课的科目记录不存在或已被删除！");
			}
			//allChoose 小于chooseNum 暂时不考虑
			int allChoose = subjectIdList.size();
			int chooseNum=choice.getChooseNum()==null?3:choice.getChooseNum();
			int bNum=allChoose-chooseNum;
			if(NewGkElectiveConstant.DIVIDE_TYPE_01.equals(divide.getOpenType())){
				chooseNum=1;
			}
			int subjectBNums=0;
			/**
			 * 分班下选考，学考科目数据
			 */
			List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService
					.findByDivideIdAndSubjectTypeIn(divideId, new String[] {
							NewGkElectiveConstant.SUBJECT_TYPE_B });
			Set<String> subIds=new HashSet<String>();
			List<String> subjectBIds = new ArrayList<String>();

			for (NewGkOpenSubject openSubject : openSubjectList) {
				subjectBIds.add(openSubject.getSubjectId());
				subIds.add(openSubject.getSubjectId());
			}
			
			List<Course> courseList = SUtils.dt(courseRemoteService
					.findListByIds(subIds.toArray(new String[] {})),
					new TR<List<Course>>() {
					});
			Map<String, Course> subjectNameMap = EntityUtils.getMap(courseList,
					Course::getId);
			
			List<NewGkDivideClass> gkDivideClassList = newGkDivideClassService
					.findByDivideIdAndClassType(divide.getUnitId(),
							divideId,
							new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			// 3+0
			Set<String> threeStuId = new HashSet<String>();

			for (NewGkDivideClass group : gkDivideClassList) {
				if (NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(group
						.getSubjectType())) {
					threeStuId.addAll(group.getStudentList());
				} 
			}

			Map<String,Integer> stuNumBBySubjectId=new HashMap<String,Integer>();
			
			//学生选课
//			ChosenSearchDto searchdto = new ChosenSearchDto();
//			searchdto.setGradeId(divide.getGradeId());
//			searchdto.setUnitId(divide.getUnitId());
//			List<StudentResultDto> stuChooselist = newGkChoResultService
//					.findChosenList(divide.getChoiceId(), searchdto);
//			
			Map<String, List<String>> stuChooseSubjectMap = findStuChoose(divide.getUnitId(),divide.getChoiceId());
			List<String> subList;
			for(Entry<String, List<String>> item:stuChooseSubjectMap.entrySet()){
				subList = item.getValue();
				if(CollectionUtils.isEmpty(subList)){
					System.out.println(item.getKey()+"没有选课");
					continue;
				}
				if(threeStuId.contains(item.getKey())){
					continue;
				}
				
				for(String bId:subjectBIds){
					if(!subList.contains(bId)){
						if(!stuNumBBySubjectId.containsKey(bId)){
							stuNumBBySubjectId.put(bId, 1);
						}else{
							stuNumBBySubjectId.put(bId, stuNumBBySubjectId.get(bId)+1);
						}
					}
				}
				
			}
			List<NewGKStudentRangeEx> exList = newGKStudentRangeExService.findByDivideId(divideId);
			Map<String,NewGKStudentRangeEx> exBySubjectMap=new HashMap<String,NewGKStudentRangeEx>();
			if(CollectionUtils.isNotEmpty(exList)){
				for(NewGKStudentRangeEx e:exList){
					exBySubjectMap.put(e.getSubjectId()+e.getSubjectType(), e);
				}
			}
			
			
			
			List<NewGKStudentRangeEx> bExList=new ArrayList<NewGKStudentRangeEx>();
			NewGKStudentRangeEx ex = null;
		
			for(String s:subjectBIds){
				Course subject = subjectNameMap.get(s);
				if(subject==null){
					continue;
				}
				ex = new NewGKStudentRangeEx();
				ex.setSubjectName(subject.getSubjectName());
				ex.setSubjectId(subject.getId());
				ex.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_B);
				ex.setStuNum(stuNumBBySubjectId.get(s)==null?0:stuNumBBySubjectId.get(s));
				if(exBySubjectMap.containsKey(subject.getId()+NewGkElectiveConstant.SUBJECT_TYPE_B)){
					NewGKStudentRangeEx olsEx = exBySubjectMap.get(subject.getId()+NewGkElectiveConstant.SUBJECT_TYPE_B);
					ex.setLeastNum(olsEx.getLeastNum());
					ex.setMaximum(olsEx.getMaximum());
					ex.setClassNum(olsEx.getClassNum());
				}
				bExList.add(ex);
				subjectBNums++;
			}
			
			map.put("bExList", bExList);
			//判断是否分班完成
			boolean canEdit = false;
			
			if(!NewGkElectiveConstant.IF_1.equals(divide.getStat())){
				//判断是否正在分班
				if(isNowDivide(divideId) ){
					map.put("haveDivideIng", true);
				}else{
					map.put("haveDivideIng", false);
					canEdit = true;
				}
			}else {
				map.put("haveDivideIng", true);
			}
			map.put("canEdit", canEdit);
			
			boolean allTwo=false;
			if(divide.getOpenType().equals(NewGkElectiveConstant.DIVIDE_TYPE_01)){
				allTwo=true;
			}
			if(allTwo){
				//默认一个批次
				divide.setBatchCountTypea(1);
			}
			map.put("divide", divide);
			map.put("allTwo", allTwo);
			//默认浮动人数
			map.put("defaultNum", divide.getMaxGalleryful());
			
			if(subjectBNums>0 && subjectBNums<bNum){
				divide.setBatchCountTypeb(subjectBNums);
			}else{
				divide.setBatchCountTypeb(bNum);
			}
			return "/newgkelective/divideGroup/singleArrangeList3.ftl";
		}
		
		@ResponseBody
		@RequestMapping("/openClassArrange/checkAllInGroupA")
		@ControllerInfo(value = "验证2+x中学生是否都安排")
		public String checkAllInGroupA(@PathVariable String divideId) {
			//所有科目
			List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), 
					divideId,
					new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_2}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			//一个学生只能安排在一个班级 超出就是学生数据不对
			//key:studentId value:jxbclassId
			Map<String,NewGkDivideClass> studentJxbClass=new HashMap<>();
			Set<String> threeStuId=new HashSet<>();
			Map<String,NewGkDivideClass> studentZhbClass=new HashMap<>();
			for(NewGkDivideClass n:classList) {
				if(CollectionUtils.isEmpty(n.getStudentList())) {
					continue;
				}
				if(NewGkElectiveConstant.CLASS_TYPE_2.equals(n.getClassType()) && 
						NewGkElectiveConstant.SUBJECT_TYPE_A.equals(n.getSubjectType())) {
					
					for(String s:n.getStudentList()) {
						if(studentJxbClass.containsKey(s)) {
							//数据不对 学生在两个教学班
							Student stuItem = SUtils.dc(studentRemoteService.findOneById(s), Student.class);
							if(stuItem!=null) {
								return error(stuItem.getStudentName()+"存在学生属于多个教学班("+studentJxbClass.get(s).getClassName()+","+n.getClassName()+")");
							}else {
								return error("存在学生属于多个教学班");
							}
							
						}else {
							studentJxbClass.put(s, n);
						}
					}
					
				}else if(NewGkElectiveConstant.CLASS_TYPE_0.equals(n.getClassType())) {
					if(NewGkElectiveConstant.SUBJECT_TYPE_3.equals(n.getSubjectType())) {
						//完全不用安排
						threeStuId.addAll(n.getStudentList());
					}else if(NewGkElectiveConstant.SUBJECT_TYPE_2.equals(n.getSubjectType())) {
						for(String s:n.getStudentList()) {
							if(studentZhbClass.containsKey(s)) {
								//数据不对 学生在两个班级
								Student stuItem = SUtils.dc(studentRemoteService.findOneById(s), Student.class);
								if(stuItem!=null) {
									return error(stuItem.getStudentName()+"存在学生属于多个组合班("+studentZhbClass.get(s).getClassName()+","+n.getClassName()+")");
								}else {
									return error("存在学生属于多个组合班");
								}
							}else {
								studentZhbClass.put(s, n);
							}
						}
					}else {
						//数据不对 不应该有混合
						return error("存在学生属于多个混合班");
					}
				}
			}
			
			
			
			NewGkDivide gkDivide = newGkDivideService.findById(divideId);
			//所有选课结果
			List<StudentResultDto> stuChooselist = newGkChoResultService.findGradeIdList(gkDivide.getUnitId(),gkDivide.getGradeId(),gkDivide.getChoiceId(), null);
			List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService
					.findByDivideIdAndSubjectTypeIn(divideId, new String[] {
							NewGkElectiveConstant.SUBJECT_TYPE_A});
			Set<String> subIds=EntityUtils.getSet(openSubjectList,NewGkOpenSubject::getSubjectId);
			for(StudentResultDto sd:stuChooselist) {
				if(CollectionUtils.isEmpty(sd.getResultList())) {
					continue;
				}
				if(threeStuId.contains(sd.getStudentId())){
					continue;
				}
				NewGkDivideClass zhb = studentZhbClass.get(sd.getStudentId());
				if(zhb==null) {
					continue;
				}
				List<NewGkChoResult> rList = sd.getResultList();
				Set<String> chooseSet = EntityUtils.getSet(rList, NewGkChoResult::getSubjectId);
				int ii=0;
				for(String ss:chooseSet){
					
					if(zhb.getSubjectIds().indexOf(ss)>-1){
						//走行政班2+x中2
					}else{
						if(subIds.contains(ss)) {
							if(studentJxbClass.containsKey(sd.getStudentId())) {
								if(!studentJxbClass.get(sd.getStudentId()).getSubjectIds().equals(ss)) {
									//不符合
									return error(sd.getStudentName()+"学生不应该属于"+studentJxbClass.get(sd.getStudentId()).getClassName());
								}
							}else {
								//没有安排
//								return error(sd.getStudentName()+"学生没有安排走班选考");
								return error("还有学生未完成走班选考");
								
							}
							ii++;
						}
						
						
					}
				}
				if(ii>1){
					return error(sd.getStudentName()+"需要超过一个时间点，不合理");
				}
			}
			
			return success("");
		}
		
		@ResponseBody
		@RequestMapping("/openClassArrange/saveBathOnlyB")
		@ControllerInfo(value = "只开B")
		public String singleMainB(@PathVariable String divideId) {
			//前提已经A开完毕--全混 除3科
			final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_" + divideId;
			final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_"
					+ divideId + "_mess";

			JSONObject on = new JSONObject();
			final NewGkDivide gkDivide = newGkDivideService.findById(divideId);
			
			if (gkDivide == null) {
				on.put("stat", "error");
				on.put("message", "该分班方案不存在");
				RedisUtils.del(new String[] { key,keyMess });
				return JSON.toJSONString(on);
			}
			if (NewGkElectiveConstant.IF_1.equals(gkDivide.getStat())) {
				on.put("stat", "success");
				on.put("message", "已经分班成功");
				RedisUtils.del(new String[] { key, keyMess });
				return JSON.toJSONString(on);
			}

			// 判断分班 状态
			if (RedisUtils.get(key) == null) {
				// AB都刚开始排班
				RedisUtils.set(key, "start",TIME_ONE_HOUR);
				RedisUtils.set(keyMess, "进行中",TIME_ONE_HOUR);
			} else {
				on.put("stat", RedisUtils.get(key));
				on.put("message", RedisUtils.get(keyMess));
				if ("success".equals(RedisUtils.get(key))
						|| "error".equals(RedisUtils.get(key))) {
					RedisUtils.del(new String[] { key, keyMess });
					if ("success".equals(RedisUtils.get(key))){
						RedisUtils.set(key, "success",TIME_ONE_HOUR);
						RedisUtils.set(keyMess, "分班成功",TIME_ONE_HOUR);
						newGkDivideService.updateStat(divideId,
								NewGkElectiveConstant.IF_1);
					}
				} 

				return JSON.toJSONString(on);
			}

			/**
			 * 分班下选考，学考科目数据
			 */
			List<NewGkOpenSubject> openSubjectList = newGkOpenSubjectService
					.findByDivideIdAndSubjectTypeIn(divideId, new String[] {
							NewGkElectiveConstant.SUBJECT_TYPE_B });
			if (CollectionUtils.isEmpty(openSubjectList)) {
				// 重组结果
				List<NewGkDivideClass> gkDivideClassList = newGkDivideClassService
						.findByDivideIdAndClassType(gkDivide.getUnitId(),
								divideId,
								new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
				if (CollectionUtils.isNotEmpty(gkDivideClassList)) {
					saveXzbClass(gkDivide, gkDivideClassList);
				}
				
				// 不用分班成功--分班成功
				newGkDivideService.updateStat(divideId, NewGkElectiveConstant.IF_1);
				on.put("stat", "success");
				on.put("message", "没有学考科目需要分班");
				RedisUtils.del(new String[] { key, keyMess });
				return JSON.toJSONString(on);
			}
			NewGkChoice gkChoice = newGkChoiceService.findOne(gkDivide
					.getChoiceId());
			if (gkChoice == null) {
				on.put("stat", "error");
				on.put("message", "该方案的选课数据不存在");
				RedisUtils.del(new String[] { key,  keyMess });
				return JSON.toJSONString(on);
			}
			List<String> subjectAIds = new ArrayList<String>();
			List<String> subjectBIds = new ArrayList<String>();

			for (NewGkOpenSubject openSubject : openSubjectList) {
				if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(openSubject
						.getSubjectType())) {
					subjectAIds.add(openSubject.getSubjectId());
				} else {
					subjectBIds.add(openSubject.getSubjectId());
				}
			}
			List<StudentResultDto> stuChooselist = newGkChoResultService
					.findGradeIdList(gkDivide.getUnitId(),gkDivide.getGradeId(),gkDivide.getChoiceId(), null);
			if (CollectionUtils.isEmpty(stuChooselist)) {
				// 不用分班成功--分班成功
				newGkDivideService.updateStat(divideId, NewGkElectiveConstant.IF_1);
				on.put("stat", "success");
				on.put("message", "没有找到学生选课数据，无需分班");
				RedisUtils.del(new String[] { key, keyMess });
				return JSON.toJSONString(on);
			}
			
			
			try {
				int bathB = gkDivide.getBatchCountTypeb()==null?4:gkDivide.getBatchCountTypeb(); 
				autoArrangeB(gkDivide, subjectBIds, bathB, stuChooselist);
				
			} catch (Exception e) {
				e.printStackTrace();
				on.put("stat", "error");
				on.put("message", "失败");
				RedisUtils.del(new String[] { key, keyMess });
				return error("保存失败！" + e.getMessage());
			}

			return JSON.toJSONString(on);
		}
		
		private String autoArrangeB(NewGkDivide divide,List<String> subjectBIds,int bathB,List<StudentResultDto> stuChooselist) {
			final String key = NewGkElectiveConstant.DIVIDE_CLASS + "_" + divide.getId();
			final String keyMess = NewGkElectiveConstant.DIVIDE_CLASS + "_"
					+ divide.getId() + "_mess";
			
			Map<String, String> relateSubjectId = new HashMap<String, String>();
			for (String ss : subjectBIds) {
				relateSubjectId.put(ss, ss);
			}

			List<Course> courseList = SUtils.dt(courseRemoteService
					.findListByIds(subjectBIds.toArray(new String[] {})),
					new TR<List<Course>>() {
					});
			Map<String, Course> subjectNameMap = EntityUtils.getMap(courseList,
					Course::getId);
			// 重组结果
			List<NewGkDivideClass> gkDivideClassList = newGkDivideClassService
					.findByDivideIdAndClassType(divide.getUnitId(),
							divide.getId(),
							new String[] { NewGkElectiveConstant.CLASS_TYPE_0 }, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			if (CollectionUtils.isEmpty(gkDivideClassList)) {
				RedisUtils.set(key, "error",TIME_ONE_HOUR);
				RedisUtils.set(keyMess,
						"没有安排学生组合数据",TIME_ONE_HOUR);
				return null;
			}
			
			saveXzbClass(divide, gkDivideClassList);


			// 3+0
			Set<String> threeStuId = new HashSet<String>();
			// 2+x 
			Map<String, String> grouClassIdByStuId = new HashMap<String, String>();
			Map<String, NewGkDivideClass> twoGroupClassMap = new HashMap<String, NewGkDivideClass>();
			int twoClassNum=0;
			for (NewGkDivideClass group : gkDivideClassList) {
				if (NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(group
						.getSubjectType())) {
					threeStuId.addAll(group.getStudentList());
				}
			}

			// B
			List<StudentSubjectDto> stuBList = initStudentNotGroup(stuChooselist,
					threeStuId, grouClassIdByStuId, twoGroupClassMap, subjectBIds,
					NewGkElectiveConstant.SUBJECT_TYPE_B, bathB, key, keyMess);
			
			if (CollectionUtils.isEmpty(stuBList)) {
				
				RedisUtils.set(key, "success",TIME_ONE_HOUR);
				RedisUtils.set(keyMess, "不存在学生需要安排学考",TIME_ONE_HOUR);
			} else {
				//去除B
				Map<String, Map<String, ArrangeCapacityRange>> rangeMap2 = new HashMap<String, Map<String, ArrangeCapacityRange>>();
				// 范围 每个科目 人数范围
				findRange2(divide.getId(), rangeMap2,false);
				Map<String, ArrangeCapacityRange> rangeBMap2=rangeMap2.get(NewGkElectiveConstant.SUBJECT_TYPE_B);
				
				new Thread(new Runnable() {

					@Override
					public void run() {
						List<NewGkDivideClass> insertClassList=new ArrayList<NewGkDivideClass>();
						List<NewGkClassStudent> insertStuList=new ArrayList<NewGkClassStudent>();
						Set<String> delClassIds=new HashSet<String>();
						
						if(!isChooseNewFunction){
							autoArrangeOrBestTypes2(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_B, stuBList, rangeBMap2,
									bathB, relateSubjectId, subjectNameMap, 0, key,
									keyMess,insertClassList,insertStuList,delClassIds);
						}else{
							autoArrangeOrBestTypes2New(divide,
									NewGkElectiveConstant.SUBJECT_TYPE_B, stuBList, rangeBMap2,
									bathB, relateSubjectId, subjectNameMap, 0, key,
									keyMess,twoClassNum,insertClassList,insertStuList,delClassIds);
						}

						if("error".equals(RedisUtils.get(key))) {
							return;
						}
						try {
							long start = System.currentTimeMillis();
							String[] delClassId=null;
							if (CollectionUtils.isNotEmpty(delClassIds)) {
								delClassId = delClassIds.toArray(new String[] {});
								
							}
							
							newGkDivideClassService.saveAllList(divide.getUnitId(), divide.getId(),
									delClassId, insertClassList, insertStuList, true);

							long end = System.currentTimeMillis();

							System.out.println("-----saveBatchsB耗时："
									+ (end - start) / 1000 + "s");
							RedisUtils.set(keyMess, "分班成功",TIME_ONE_HOUR);
							RedisUtils.set(key, "success",TIME_ONE_HOUR);
						}catch (Exception e) {
							e.printStackTrace();
							RedisUtils.set(keyMess, "分班失败",TIME_ONE_HOUR);
							RedisUtils.set(key, "error",TIME_ONE_HOUR);
						}
					}
				}).start();
				

			}
			return null;
		}
		
}

