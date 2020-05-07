package net.zdsoft.newgkelective.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.BatchClassDto;
import net.zdsoft.newgkelective.data.dto.NewGkChoResultDto;
import net.zdsoft.newgkelective.data.dto.NewGkConditionDto;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;
import net.zdsoft.newgkelective.data.entity.NewGkClassBatch;
import net.zdsoft.newgkelective.data.entity.NewGkClassStudent;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;
import net.zdsoft.newgkelective.data.entity.NewGkOpenSubject;
import net.zdsoft.newgkelective.data.service.NewGkChoResultService;
import net.zdsoft.newgkelective.data.service.NewGkClassBatchService;
import net.zdsoft.newgkelective.data.service.NewGkDivideClassService;
import net.zdsoft.newgkelective.data.utils.CombineAlgorithmInt;

/**
 * 班级时间点设置
 * 
 * @author weixh
 * @since 2018年6月12日 上午10:08:13
 */
@Controller
@RequestMapping("/newgkelective/clsBatch/{divideId}")
public class NewGkClassBatchAction extends NewGkElectiveDivideCommonAction {
	@Autowired
	private NewGkClassBatchService newGkClassBatchService;
	@Autowired
	private NewGkDivideClassService newGkDivideClassService;
	@Autowired
	private NewGkChoResultService newGkChoResultService;

	@ControllerInfo("时间点首页")
	@RequestMapping("/index")
	public String index(@PathVariable String divideId, ModelMap map) {
		map.put("divideId", divideId);
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		map.put("gradeId", divide.getGradeId());
		map.put("canEdit", canEditDivide(divide));
		map.put("maxCount", divide.getBatchCountTypea());
		
		List<NewGkDivideClass> allClsList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), 
				divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_0}, 
				true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
		List<NewGkDivideClass> threeClsList=new ArrayList<>();
		List<NewGkDivideClass> twoClsList =new ArrayList<>();
		
		if(CollectionUtils.isNotEmpty(allClsList)) {
			Iterator<NewGkDivideClass> cit = allClsList.iterator();
			List<String> cids = new ArrayList<String>();
			Set<String> subIds = new HashSet<String>();
			Set<String> stuIds = new HashSet<String>();
			while(cit.hasNext()) {
				NewGkDivideClass dc = cit.next();
				if(NewGkElectiveConstant.SUBJECT_TYPE_3.equals(dc.getSubjectType())) {
					threeClsList.add(dc);
				}else if(NewGkElectiveConstant.SUBJECT_TYPE_2.equals(dc.getSubjectType())){
					twoClsList.add(dc);
				}else {
					continue;
				}
				
				cids.add(dc.getId());
				String[] sids = StringUtils.split(dc.getSubjectIds(), ",");
				if(ArrayUtils.isNotEmpty(sids)) {
					CollectionUtils.addAll(subIds, sids);
				}
				if (CollectionUtils.isNotEmpty(dc.getStudentList())) {
					dc.setStudentCount(dc.getStudentList().size());
					CollectionUtils.addAll(stuIds, dc.getStudentList().iterator());
				}
			}
			List<Course> courses = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])),
					new TR<List<Course>>() {
					});
			List<Student> stus =Student.dt(studentRemoteService.findPartStudByGradeId(divide.getUnitId(), null, null, stuIds.toArray(new String[0])));
//			List<Student> stus = Student.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])));
			Map<String, Integer> stuSex = EntityUtils.getMap(stus, "id", "sex");
			Map<String, String> cnMap = EntityUtils.getMap(courses, "id", "subjectName");

			// 根据组合班下面的教学班获取时间点
			Map<String,Map<String,String>> timeByClass=new HashMap<>();
			if(CollectionUtils.isNotEmpty(cids)) {
				List<NewGkDivideClass> reDcs = newGkDivideClassService.findByRelateIdsWithMaster(cids.toArray(new String[0]));
				if(CollectionUtils.isNotEmpty(reDcs)) {
					for(NewGkDivideClass dc : reDcs) {
						Map<String, String> timeBySubject = timeByClass.get(dc.getRelateId());
						if(timeBySubject == null) {
							timeBySubject = new HashMap<>();
							timeByClass.put(dc.getRelateId(), timeBySubject);
						}
						timeBySubject.put(dc.getSubjectIds(), dc.getBatch());
					}
				}
			}

			for(NewGkDivideClass dc : allClsList) {
				int gc = 0;
				int bc = 0;
				if (CollectionUtils.isNotEmpty(dc.getStudentList())) {
					for(String stuid : dc.getStudentList()) {
						if(stuSex.containsKey(stuid)) {
							Integer sex = stuSex.get(stuid);
							if(sex != null) {
								if(sex == BaseConstants.MALE) {
									bc++;
								} else if(sex == BaseConstants.FEMALE) {
									gc++;
								}
							}
						}
					}
				}
				dc.setBoyCount(bc);
				dc.setGirlCount(gc);
				String[] sids = StringUtils.split(dc.getSubjectIds(), ",");
				StringBuilder strs = new StringBuilder();
				if(ArrayUtils.isNotEmpty(sids)) {
					if(timeByClass.containsKey(dc.getId()) ) {
						Map<String, String> mmap = timeByClass.get(dc.getId());
						for(String subid : sids) {
							if(cnMap.containsKey(subid)) {
								if(strs.length() > 0) {
									strs.append("、");
								}
								if(mmap.containsKey(subid) && StringUtils.isNotBlank(mmap.get(subid))) {
									strs.append(cnMap.get(subid)+"(时间点"+mmap.get(subid)+")");
								}else {
									strs.append(cnMap.get(subid));
								}
								
							}
						}
					}else {
						for(String subid : sids) {
							if(cnMap.containsKey(subid)) {
								if(strs.length() > 0) {
									strs.append("、");
								}
								strs.append(cnMap.get(subid));
							}
						}
					}
				}
				dc.setRelateName(strs.toString());
			}
		}
		map.put("threeClsList", threeClsList);
		map.put("twoClsList", twoClsList);
		return "/newgkelective/clsBatch/batchIndex.ftl";
	}
	
	/**
	 * 是否智能分班中
	 * @param divideId
	 * @return
	 */
	private boolean canEditDivide(NewGkDivide divide){
		if (NewGkElectiveConstant.IF_1.equals(divide.getStat())) {
			return false;
		}
		return !isNowDivide(divide.getId());
	}
	
	/**
	 * 获取班级下教学班各科目时间点
	 * @param clsIds
	 * @param clsBa
	 * @param subNames
	 */
	private void getClsBatchStr(String[] clsIds, Map<String, StringBuilder> clsBa, Map<String, String> subNames) {
		if(ArrayUtils.isEmpty(clsIds) || MapUtils.isEmpty(subNames)) {
			return;
		}
		List<NewGkDivideClass> reDcs = newGkDivideClassService.findByRelateIdsWithMaster(clsIds);
		
		if(CollectionUtils.isNotEmpty(reDcs)) {
			Collections.sort(reDcs, new Comparator<NewGkDivideClass>() {

				@Override
				public int compare(NewGkDivideClass o1, NewGkDivideClass o2) {
					return (o1.getRelateId()+o1.getBatch()).compareTo(o2.getRelateId()+o2.getBatch());
				}
			});
			for(NewGkDivideClass dc : reDcs) {
				StringBuilder sb = clsBa.get(dc.getRelateId());
				if(sb == null) {
					sb = new StringBuilder();
					clsBa.put(dc.getRelateId(), sb);
				}
				String sn = subNames.get(dc.getSubjectIds());
				if(StringUtils.isEmpty(sn)) {
					continue;
				}
				if(sb.length() > 0) {
					sb.append("、");
				}
				sb.append(sn);
				if(StringUtils.isNotEmpty(dc.getBatch())) {
					sb.append("(时间点"+dc.getBatch()+")");
				}
			}
		}
	}
	
	@ControllerInfo("行政班科目时间点")
	@RequestMapping("/clsBatch/page")
	public String clsBatchIndex(@PathVariable String divideId, ModelMap map) {
		map.put("divideId", divideId);
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		List<NewGkDivideClass> hhClsList = newGkDivideClassService.findByDivideIdAndClassTypeSubjectTypeWithMaster(divide.getUnitId(), 
				divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_0}, 
				false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.SUBJTCT_TYPE_0);
		boolean canEdit = canEditDivide(divide);
		if(canEdit) {
			List<NewGkDivideClass> temps = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), 
					divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, 
					false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			if(CollectionUtils.isNotEmpty(temps)) {
				List<String> rids = EntityUtils.getList(temps, NewGkDivideClass::getRelateId);
				if(CollectionUtils.isEmpty(rids) || rids.contains(null)) {
					canEdit = false;
				}
			}
		}
		map.put("canEdit", canEdit);
		// 班级科目下的学生数或班级下总人数
		Map<String, Integer> courseIdCountMap= new HashMap<String, Integer>();
		List<Course> courseList = null;
		if(CollectionUtils.isNotEmpty(hhClsList)) {
			Set<String> ids = EntityUtils.getSet(hhClsList, NewGkDivideClass::getId);
			// 班级学生
			List<NewGkClassStudent> csList = newGkClassStudentService.findListByClassIds(divide.getUnitId(),divideId,ids.toArray(new String[]{}));
			Map<String, String> stuIds = EntityUtils.getMap(csList, NewGkClassStudent::getStudentId, NewGkClassStudent::getClassId);
			// 学生选课数据
			List<NewGkChoResult> resultList = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(divide.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId(), 
					stuIds.keySet().toArray(new String[0]));
			Set<String> cids = new HashSet<String>();
			if(CollectionUtils.isNotEmpty(resultList)){
				Map<String, List<String>> csids = new HashMap<String, List<String>>();
				for(NewGkChoResult result : resultList){
					if(!cids.contains(result.getSubjectId())) {
						cids.add(result.getSubjectId());
					}
		            String cid = stuIds.get(result.getStudentId());
					Integer csnum = courseIdCountMap.get(cid+result.getSubjectId());
			    	if(csnum == null){
			    		csnum = 0;
			    	}
			    	courseIdCountMap.put(cid+result.getSubjectId(), ++csnum);
			    	
			    	List<String> sids = csids.get(cid);
			    	if(sids == null){
			    		sids = new ArrayList<String>();
			    		csids.put(cid, sids);
			    	}
			    	if (!sids.contains(result.getStudentId())) {
						sids.add(result.getStudentId());
					}
					courseIdCountMap.put(cid, sids.size());
				}
				csids = null;
			}
			courseList = SUtils.dt(courseRemoteService.findListByIds(cids.toArray(new String[0])),
					new TR<List<Course>>() {
					});
			// 行政班科目时间点组装
			Map<String, String> subNames = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
			Map<String, StringBuilder> clsBa = new HashMap<String, StringBuilder>();
			getClsBatchStr(ids.toArray(new String[0]), clsBa, subNames);
			for(NewGkDivideClass dc : hhClsList) {
				if(clsBa.containsKey(dc.getId())) {
					dc.setRelateName(clsBa.get(dc.getId()).toString());
				}
			}
		}	
		Collections.sort(hhClsList, (x,y)->x.getClassName().compareTo(y.getClassName()));
		map.put("hhClsList", hhClsList);
		map.put("courseList", courseList);
		map.put("courseIdCountMap", courseIdCountMap);
		int bc = 0;
		if(divide.getBatchCountTypea() != null) {
			bc = divide.getBatchCountTypea().intValue();
		}
		map.put("batchCount", bc);
		return "/newgkelective/clsBatch/clsBatchList.ftl";
	}
	
	
	@SuppressWarnings("unchecked")
	@ControllerInfo("走班科目时间点")
	@RequestMapping("/subBatch/page")
	public String subBatchIndex(@PathVariable String divideId, ModelMap map) {
		map.put("divideId", divideId);
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		boolean canEdit = canEditDivide(divide);
		if (canEdit) {
			List<NewGkDivideClass> temps = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(),
					divideId, new String[] { NewGkElectiveConstant.CLASS_TYPE_2 },
					false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			if (CollectionUtils.isNotEmpty(temps)) {
				List<String> rids = EntityUtils.getList(temps, e->e.getRelateId());
				// 教学班关联 的组合班 ；说明存在了 合班 情况 
				if (CollectionUtils.isEmpty(rids) || rids.contains(null)) {
					canEdit = false;
				}
			} 
		}
		map.put("canEdit", canEdit);
		
		// 组合班
		List<NewGkDivideClass> hhClsList = newGkDivideClassService.findByDivideIdAndClassTypeSubjectType(divide.getUnitId(), 
				divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_0}, 
				false,NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.SUBJTCT_TYPE_0);
		
		
		Map<String, String> courseNameMap = new HashMap<String, String>();
		Map<String, String> clsXzbMap = new HashMap<String, String>();
		Map<String, String> clsXzbBasMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(hhClsList)) {
//			List<NewGkDivideClass> ss = hhClsList.subList(3, 4);
//			hhClsList = null;
//			hhClsList = ss;// TODO
			Set<String> ids = EntityUtils.getSet(hhClsList, e->e.getId());
			List<NewGkClassStudent> csList = newGkClassStudentService.findListByClassIds(divide.getUnitId(),divideId,ids.toArray(new String[]{}));
			Map<String, String> stuIds = EntityUtils.getMap(csList, NewGkClassStudent::getStudentId, NewGkClassStudent::getClassId);
			// 学生选课数据整理
			List<NewGkChoResult> resultList = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(divide.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId(), 
					stuIds.keySet().toArray(new String[0]));
			Map<String,NewGkChoResultDto> dtoMap=new HashMap<String,NewGkChoResultDto>();
			NewGkChoResultDto dto=new NewGkChoResultDto();	
			Set<String> cids = new HashSet<String>();// courseIds
			if(CollectionUtils.isNotEmpty(resultList)){
				// <classId, List<studentIds>> 组合班 对应的 学生
				Map<String, List<String>> csids = new HashMap<String, List<String>>();
				for(NewGkChoResult result : resultList){
					if(!cids.contains(result.getSubjectId())) {
						cids.add(result.getSubjectId());
					}
					
					if(!dtoMap.containsKey(result.getStudentId())){
						dto = new NewGkChoResultDto();
		                dto.setChooseSubjectIds(new HashSet<String>());
		                dto.setStudentId(result.getStudentId());
		                dtoMap.put(result.getStudentId(), dto);
		            }
		            dtoMap.get(result.getStudentId()).getChooseSubjectIds().add(result.getSubjectId());
		            
		            String cid = stuIds.get(result.getStudentId());
		            List<String> sids = csids.get(cid);
			    	if(sids == null){
			    		sids = new ArrayList<String>();
			    		csids.put(cid, sids);
			    	}
			    	if (!sids.contains(result.getStudentId())) {
						sids.add(result.getStudentId());
					}
				}
				// 班级走班科目时间点；获取保存数据
				List<NewGkClassBatch> clsBaList = newGkClassBatchService.findByDivideClsIdsWithMaster(ids.toArray(new String[]{}));
				Map<String, List<NewGkClassBatch>> clsSubMap = new HashMap<String, List<NewGkClassBatch>>();
				if(CollectionUtils.isNotEmpty(clsBaList)) {
					for(NewGkClassBatch cb : clsBaList) {
						List<NewGkClassBatch> cbs = clsSubMap.get(cb.getDivideClassId()+cb.getSubjectIds());
						if(cbs == null) {
							cbs = new ArrayList<NewGkClassBatch>();
							clsSubMap.put(cb.getDivideClassId()+cb.getSubjectIds(), cbs);
						}
						cbs.add(cb);
					}
				}
				
				List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(cids.toArray(new String[0])),
						new TR<List<Course>>() {
						});
				courseNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
				// 行政班科目时间点 ;找出 混合班 的 组合班  关联的教学班，找出混合班的 行政班 科目 
				List<NewGkDivideClass> reDcs = newGkDivideClassService.findByRelateIdsWithMaster(ids.toArray(new String[]{}));
				Map<String, List<String>> clsXzbIdsMap = new HashMap<String, List<String>>();
				Map<String, List<NewGkDivideClass>> dcm = new HashMap<String, List<NewGkDivideClass>>();
				for(NewGkDivideClass dc : reDcs) {
					if(courseNameMap.containsKey(dc.getSubjectIds())) {
						// 班级 批次1  是 什么科目 。
						clsXzbMap.put(dc.getRelateId()+dc.getBatch(), courseNameMap.get(dc.getSubjectIds()));
						// 获取 组合班 对应的 行政班科目 
						List<String> sids = clsXzbIdsMap.get(dc.getRelateId());
						if(sids == null) {
							sids = new ArrayList<String>();
							clsXzbIdsMap.put(dc.getRelateId(), sids);
						}
						sids.add(dc.getSubjectIds());
						// 组合班  开出的  教学班科目 
						List<NewGkDivideClass> dcs = dcm.get(dc.getRelateId());
						if(dcs == null) {
							dcs = new ArrayList<NewGkDivideClass>();
							dcm.put(dc.getRelateId(), dcs);
						}
						dcs.add(dc);
					}
				}
				
				// 三科目选择结果
				Integer[] cSize = new Integer[cids.size()];
		        for(int i = 0;i < cids.size();i++){
		        	cSize[i] = i;
		        }
		        
		        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize,3);
		        // 排列 组合的结果 比如 7选3 所有组合 
		        Integer[][] result = combineAlgorithm.getResutl();
		        List<NewGkConditionDto> newConditionList3=newGkChoiceService.findSubRes(courseList, dtoMap, result, 3);
		        Map<String, NewGkConditionDto> clsSubCons = new HashMap<String, NewGkConditionDto>();
		        if(CollectionUtils.isNotEmpty(newConditionList3)) {
		        	for(NewGkConditionDto con : newConditionList3) {
		        		Set<String> sids = con.getStuIds();
		        		if(CollectionUtils.isEmpty(sids)) {
		        			continue;
		        		}
		        		for(String stuId : sids) {
		        			String cid = stuIds.get(stuId);
		        			NewGkConditionDto cdto = clsSubCons.get(cid+con.getSubjectIdstr());
		        			if(cdto == null) {
		        				cdto = new NewGkConditionDto();
		        				EntityUtils.copyProperties(con, cdto);
		        				cdto.setSumNum(0);
		        				clsSubCons.put(cid+con.getSubjectIdstr(), cdto);
		        			}
		        			cdto.setSumNum(cdto.getSumNum()+1);
		        		}
		        	}
		        }
		        // 选考 批次集合 
		        List<String> bas = new ArrayList<String>();
		        if(divide.getBatchCountTypea() != null && divide.getBatchCountTypea() > 0) {
		        	int ca = divide.getBatchCountTypea().intValue();
		        	for(int i=1;i<=ca;i++) {
		        		bas.add(i+"");
		        	}
		        }
		        for(NewGkDivideClass dc : hhClsList) {
		        	if(csids.containsKey(dc.getId())) {
		        		dc.setStudentCount(csids.get(dc.getId()).size());
		        	}
		        	List<NewGkConditionDto> conList = new ArrayList<NewGkConditionDto>();
		        	Iterator<Entry<String, NewGkConditionDto>> conit = clsSubCons.entrySet().iterator();
		        	
		        	// 对应的 行政班科目
		        	List<String> sids = clsXzbIdsMap.get(dc.getId());
		        	String xzStrs = "";
        			boolean hasSubs = false;
        			Map<String, String> subBaMap = new HashMap<String, String>();
		        	if (CollectionUtils.isNotEmpty(sids)) {
		        		hasSubs = true;
		        		String[] sidAr = sids.toArray(new String[0]);
						Arrays.sort(sidAr);
						xzStrs = StringUtils.join(sidAr, ",");
						
						List<NewGkDivideClass> dcs = dcm.get(dc.getId());
						for(NewGkDivideClass ndc : dcs) {
							clsXzbBasMap.put(dc.getId()+xzStrs+ndc.getBatch(), ndc.getSubjectIds());
							subBaMap.put(ndc.getSubjectIds(), ndc.getBatch());
						}
        			}
		        	while(conit.hasNext()) {
		        		Entry<String, NewGkConditionDto> cen = conit.next();
		        		if(cen.getKey().startsWith(dc.getId())) {
		        			NewGkConditionDto cdto = cen.getValue();
							if (StringUtils.equals(cdto.getSubjectIdstr(), xzStrs)) {
								cdto.setBeXzbSub(true);
							} else {
								List<NewGkClassBatch> cbs = clsSubMap.get(dc.getId()+cdto.getSubjectIdstr());
								if(CollectionUtils.isNotEmpty(cbs)) {
			        				for(NewGkClassBatch cb : cbs) {
			        					clsXzbBasMap.put(dc.getId()+cdto.getSubjectIdstr()+cb.getBatch(), cb.getSubjectId());
			        				}
			        			} else if(hasSubs && CollectionUtils.intersection(cdto.getSubjectIds(), sids).size() > 0){
			        				// 如果组合中有行政班科目，则该科目默认为行政班科目的时间点
			        				List<String> tids = (List<String>) CollectionUtils.removeAll(cdto.getSubjectIds(), subBaMap.keySet());
			        				List<String> tbas = new ArrayList<String>();
			        				tbas.addAll(bas);
			        				for(String sid : cdto.getSubjectIds()) {
			        					if(subBaMap.containsKey(sid)) {
			        						clsXzbBasMap.put(dc.getId()+cdto.getSubjectIdstr()+subBaMap.get(sid), sid);
			        						tbas.remove(subBaMap.get(sid));
			        					}
			        				}
			        				if(CollectionUtils.isNotEmpty(tids)) {
			        					int sum=tids.size();
			        					if(tbas.size()<tids.size()) {
			        						sum = tbas.size();
			        					}
			        					for(int i=0;i<sum;i++) {
			        						clsXzbBasMap.put(dc.getId()+cdto.getSubjectIdstr()+tbas.get(i), tids.get(i));
			        					}
			        				}
			        			}
							}
							conList.add(cdto);
		        			conit.remove();
		        		}
		        	}
		        	dc.setNewDtoList(conList);
		        }
			}
		}
		Collections.sort(hhClsList, (x,y)->x.getClassName().compareTo(y.getClassName()));
		map.put("clsXzbBasMap", clsXzbBasMap);
		map.put("clsXzbMap", clsXzbMap);
		map.put("courseNameMap", courseNameMap);
		map.put("hhClsList", hhClsList);
		map.put("batchCount", divide.getBatchCountTypea());
		return "/newgkelective/clsBatch/subBatchList.ftl";
	}
	
	
	@ControllerInfo("组合科目时间点保存")
	@RequestMapping("/clsBatchEdit/page")
	public String clsBatchEdit(@PathVariable String divideId, String divideClsId, ModelMap map) {
		map.put("divideId", divideId);
		map.put("divideClsId", divideClsId);
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		int bc = 0;
		if(divide.getBatchCountTypea() != null) {
			bc = divide.getBatchCountTypea().intValue();
		}
		if(bc == 0) {
			return errorFtl(map, "没有可维护的时间点！");
		}
		List<NewGkClassStudent> csList = newGkClassStudentService.findListByClassIds(divide.getUnitId(),divideId,new String[]{divideClsId});
		Set<String> stuIds = EntityUtils.getSet(csList, NewGkClassStudent::getStudentId);
		List<NewGkChoResult> resultList = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(divide.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId(), 
				stuIds.toArray(new String[0]));
		Set<String> cids = EntityUtils.getSet(resultList, NewGkChoResult::getSubjectId);
		List<NewGkOpenSubject> aOpenSubjectList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeIn(divideId,
				new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A});
		Set<String> openSubjectIds = EntityUtils.getSet(aOpenSubjectList, NewGkOpenSubject::getSubjectId);
		cids = cids.stream().filter(e->openSubjectIds.contains(e)).collect(Collectors.toSet());
		List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(cids.toArray(new String[0])),
				new TR<List<Course>>() {
				});
		for(Course cc : courseList) {
			int num=0;
			Iterator<NewGkChoResult> rit = resultList.iterator();
			while(rit.hasNext()) {
				NewGkChoResult res = rit.next();
				if(cc.getId().equals(res.getSubjectId())) {
					num++;
					rit.remove();
				}
			}
			cc.setFullMark(num);// 选该科目的学生数
		}
		// 人数 从大到小排
		Collections.sort(courseList, new Comparator<Course>() {

			public int compare(Course o1, Course o2) {
				int n1 = 0;
				if(o1.getFullMark() != null) {
					n1 = o1.getFullMark().intValue();
				}
				int n2 = 0;
				if(o2.getFullMark() != null) {
					n2 = o2.getFullMark().intValue();
				}
				return n2-n1;
			}
		});
		cids.clear();
		
		List<NewGkDivideClass> dcs = newGkDivideClassService.findListBy("relateId", divideClsId);
		Map<String, NewGkDivideClass> batchClsMap = new HashMap<String, NewGkDivideClass>();
		if (CollectionUtils.isNotEmpty(dcs)) {
			cids = EntityUtils.getSet(dcs, "subjectIds");
			batchClsMap = EntityUtils.getMap(dcs, "batch");
		}
		
		List<String[]> batchs = new ArrayList<String[]>();
		for(int i=1; i<=bc; i++) {
			String[] bas = new String[4];
			bas[0] = i+"";
			if(batchClsMap.containsKey(i+"")) {
				NewGkDivideClass dc = batchClsMap.get(i+"");
				bas[1] = dc.getSubjectIds();
				bas[2] = dc.getId();
				bas[3] = dc.getClassName();
			} else {
				bas[1] = "";
				bas[2] = "";
				bas[3] = "";
			}
			batchs.add(bas);
		}
		map.put("batchs", batchs);
		if (cids.size() > 0) {
			map.put("cids", ArrayUtils.toString(cids.toArray(new String[0])));
		}
		map.put("courseList", courseList);
		return "/newgkelective/clsBatch/clsBatchEdit.ftl";
	}
	
	
	@ResponseBody
	@ControllerInfo("行政班科目保存")
	@RequestMapping("/clsSave")
	public String clsSave(@PathVariable String divideId, String divideClsId, NewGkDivideClass divideCls) {
		try {
			NewGkDivide divide = newGkDivideService.findOne(divideId);
			newGkClassBatchService.saveClsBatch(divide, divideClsId, divideCls);
		} catch (Exception e) {
			log.error("行政班科目保存失败："+e.getMessage(), e);
			return error("保存失败！");
		}
		return success("保存成功！");
	}
	
	@ResponseBody
	@ControllerInfo("组合科目时间点保存")
	@RequestMapping("/subBatchSave")
	public String subBatchSave(@PathVariable String divideId, BatchClassDto dto) {
		List<NewGkClassBatch> clsBatchs = dto.getClsBatchs();
		if(CollectionUtils.isEmpty(clsBatchs)) {
			return error("没有可保存的信息");
		}
		//key1:classId_subjectIds key2:subjectId 时间点
		Map<String,Map<String,String>> bathByclassIdAndSubjectIds=new HashMap<String,Map<String,String>>();
		
		String unitId = getLoginInfo().getUnitId();
		Iterator<NewGkClassBatch> it = clsBatchs.iterator();
		while(it.hasNext()) {
			NewGkClassBatch ba = it.next();
			if(ba == null || StringUtils.isEmpty(ba.getBatch()) || StringUtils.isEmpty(ba.getSubjectId())) {
				it.remove();
				continue;
			}
			ba.setUnitId(unitId);
			ba.setDivideId(divideId);
			ba.setId(UuidUtils.generateUuid());
			String key=ba.getDivideClassId()+"_"+ba.getSubjectIds();
			
			if(!bathByclassIdAndSubjectIds.containsKey(key)) {
				bathByclassIdAndSubjectIds.put(key, new HashMap<>());
			}
			bathByclassIdAndSubjectIds.get(key).put(ba.getSubjectId(), ba.getBatch());
		}
		
		
		NewGkDivide divide = newGkDivideService.findOne(divideId);
		//1:混合班
		List<NewGkDivideClass> hhClsList = newGkDivideClassService.findByDivideIdAndClassTypeSubjectType(unitId, 
				divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_0}, 
				true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.SUBJTCT_TYPE_0);
		//key:studentId,value:classId
		Map<String,String> stuClassIdMap=new HashMap<>();
		Set<String> stuIds=new HashSet<>();
		if(CollectionUtils.isEmpty(hhClsList)) {
			return error("没有找到混合班数据");
		}
		for(NewGkDivideClass cc:hhClsList) {
			if(CollectionUtils.isNotEmpty(cc.getStudentList())) {
				stuIds.addAll(cc.getStudentList());
				for(String s:cc.getStudentList()) {
					stuClassIdMap.put(s, cc.getId());
				}
			}
		}
		if(CollectionUtils.isEmpty(stuIds)) {
			return error("没有找到混合班下学生数据");
		}
		
		//2: 学生选课数据
		List<NewGkChoResult> resultList = newGkChoResultService.findByKindTypeAndChoiceIdAndStudentIds(divide.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01,divide.getChoiceId(), 
				stuIds.toArray(new String[0]));
		Map<String,Set<String>> stuSubjectId=new HashMap<>();
		for(NewGkChoResult r:resultList) {
			if(!stuSubjectId.containsKey(r.getStudentId())) {
				stuSubjectId.put(r.getStudentId(), new HashSet<>());
			}
			stuSubjectId.get(r.getStudentId()).add(r.getSubjectId());
		}

		//2:已安排时间点
		List<NewGkDivideClass> temps = newGkDivideClassService.findByDivideIdAndClassTypeSubjectType(unitId, 
				divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, 
				true,NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.SUBJECT_TYPE_A);
		if(CollectionUtils.isEmpty(temps)) {
			//没有数据无需保存学生
			try {
				newGkClassBatchService.saveBatchs(clsBatchs, divideId, true,null,divide.getUnitId());
			}catch (Exception e) {
				log.error("时间点保存失败："+e.getMessage(), e);
				return error("保存失败！");
			}
			return success("保存成功！");
		}else {
			//学生+科目---已存在班级
			//key:studentId,key2:subjectId,[classId,bath]
			Map<String,NewGkDivideClass> jxbMap=new HashMap<>();
			//key:hhbId key2:subjectId
			Map<String,Map<String,NewGkDivideClass>> hhbJxbMap=new HashMap<>();
			Map<String,Map<String,String[]>> stuJxbMap=new HashMap<>();
			for(NewGkDivideClass cc:temps) {
				if(CollectionUtils.isEmpty(cc.getStudentList())) {
					cc.setStudentList(new ArrayList<>());
				}else {
					for(String ss:cc.getStudentList()) {
						if(!stuJxbMap.containsKey(ss)) {
							stuJxbMap.put(ss, new HashMap<>());
						}
						stuJxbMap.get(ss).put(cc.getSubjectIds(),new String[] {cc.getId(),cc.getBatch()});
					}
				}
				jxbMap.put(cc.getId(), cc);
				
				if(StringUtils.isNotBlank(cc.getRelateId())) {
					if(!hhbJxbMap.containsKey(cc.getRelateId())) {
						hhbJxbMap.put(cc.getRelateId(), new HashMap<>());
					}
					hhbJxbMap.get(cc.getRelateId()).put(cc.getSubjectIds(), cc);
				}
				
			}
			
			for(String stuId:stuIds) {
				//学生所在混合班
				String xzbId=stuClassIdMap.get(stuId);
				//学生的选课数据
				Set<String> chooseIds = stuSubjectId.get(stuId);
				String subjectIds=keySort(chooseIds);
				Map<String, String> subjectTimeMap = bathByclassIdAndSubjectIds.get(xzbId+"_"+subjectIds);
				//学生已存在教学班
				Map<String, String[]> item = stuJxbMap.get(stuId);
				Set<String> arrangeSubject=new HashSet<>();//已经安排的科目
				//符合要求遗留 不符合要求去除
				if(item==null || item.size()<=0) {
					//不存在数据
				}else {
					if(subjectTimeMap==null || subjectTimeMap.size()<=0) {
						//找不到学生选课组合的时间点 去除已经保存的学生数据
						for(Entry<String, String[]> tt:item.entrySet()) {
							String[] arr = tt.getValue();
							jxbMap.get(arr[0]).getStudentList().remove(stuId);
						}
						continue;
					}else {
						//去除不符合的学生数据
						for(Entry<String, String[]> tt:item.entrySet()) {
							String[] arr = tt.getValue();
							if(subjectTimeMap.containsKey(tt.getKey())) {
								if(!arr[1].equals(subjectTimeMap.get(tt.getKey()))) {
									jxbMap.get(arr[0]).getStudentList().remove(stuId);
								}else {
									arrangeSubject.add(tt.getKey());
								}
							}else {
								//科目不存在，移除
								jxbMap.get(arr[0]).getStudentList().remove(stuId);
							}
						}
					}
					
				}
				//混合班级内未安排的学生默认进入本班的数据
				if(subjectTimeMap==null || subjectTimeMap.size()<=0) {
					continue;
				}
				if(!hhbJxbMap.containsKey(xzbId)) {
					continue;
				}
				Map<String, NewGkDivideClass> xzbJxb = hhbJxbMap.get(xzbId);
				for(String s:chooseIds) {
					if(arrangeSubject.contains(s)) {
						//已安排
						continue;
					}else {
						
						if(xzbJxb.containsKey(s)) {
							//混合班中需要的批次
							String batch = subjectTimeMap.get(s);
							if(batch.equals(xzbJxb.get(s).getBatch())) {
								xzbJxb.get(s).getStudentList().add(stuId);
							}
						}
					}
				}
				
				
			}
			
			//temps 先删除学生数据，然后批量新增学生数据
			try {
				newGkClassBatchService.saveBatchs(clsBatchs, divideId, true,temps,divide.getUnitId());
			} catch (Exception e) {
				log.error("时间点保存失败："+e.getMessage(), e);
				return error("保存失败！");
			}
			return success("保存成功！");
			
			
		}

	}
	
	@InitBinder
	public void initBinder(WebDataBinder wb) throws Exception {
		wb.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}
	
	@ResponseBody
	@ControllerInfo("删除安排数据")
	@RequestMapping("/deleteDivideAllJxbClass")
	public String deleteDivideAllJxbClass(@PathVariable String divideId){
		try {
			List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassType(this.getLoginInfo().getUnitId(), divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
			String[] ids=null;
			if(CollectionUtils.isNotEmpty(classList)) {
				ids = EntityUtils.getSet(classList, NewGkDivideClass::getId).toArray(new String[] {});
			}
			newGkClassBatchService.deleteByDivideId(this.getLoginInfo().getUnitId(), divideId, ids);
		}catch (Exception e) {
			log.error("重新安排："+e.getMessage(), e);
			return error("重新安排失败！");
		}
		return success("保存成功！");
	}
}
