package net.zdsoft.eclasscard.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccStudormAttenceDao;
import net.zdsoft.eclasscard.data.dto.AttanceSearchDto;
import net.zdsoft.eclasscard.data.entity.EccAttenceDormGrade;
import net.zdsoft.eclasscard.data.entity.EccAttenceDormPeriod;
import net.zdsoft.eclasscard.data.entity.EccDormAttence;
import net.zdsoft.eclasscard.data.entity.EccStuLeaveInfo;
import net.zdsoft.eclasscard.data.entity.EccStudormAttence;
import net.zdsoft.eclasscard.data.service.EccAttenceDormGradeService;
import net.zdsoft.eclasscard.data.service.EccAttenceDormPeriodService;
import net.zdsoft.eclasscard.data.service.EccDormAttenceService;
import net.zdsoft.eclasscard.data.service.EccStuLeaveInfoService;
import net.zdsoft.eclasscard.data.service.EccStudormAttenceService;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import net.zdsoft.stuwork.remote.service.StuworkRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("eccStudormAttenceService")
public class EccStudormAttenceServiceImpl extends
		BaseServiceImpl<EccStudormAttence, String> implements
		EccStudormAttenceService {
	@Autowired
	private EccStudormAttenceDao eccStudormAttenceDao;
	@Autowired
	private EccAttenceDormGradeService eccAttenceDormGradeService;
	@Autowired
	private EccDormAttenceService eccDormAttenceService;
	@Autowired
	private StuworkRemoteService stuworkRemoteService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private EccStuLeaveInfoService eccStuLeaveInfoService;
	@Autowired
	private EccAttenceDormPeriodService eccAttenceDormPeriodService;
	private static OpenApiOfficeService openApiOfficeService;

    public OpenApiOfficeService getOpenApiOfficeService() {
        if (openApiOfficeService == null) {
            openApiOfficeService = Evn.getBean("openApiOfficeService");
            if(openApiOfficeService == null){
				System.out.println("openApiOfficeService为null，需开启dubbo服务");
			}
        }
        return openApiOfficeService;
    }
	
	@Override
	protected BaseJpaRepositoryDao<EccStudormAttence, String> getJpaDao() {
		return eccStudormAttenceDao;
	}

	@Override
	protected Class<EccStudormAttence> getEntityClass() {
		return EccStudormAttence.class;
	}
	@Override
	public List<EccStudormAttence> findByInAttIdInit(String[] dormAttIds) {
		return eccStudormAttenceDao.findByInAttIdInit(dormAttIds);
	}
	@Override
	public void updateStatus(String id,int status){
		eccStudormAttenceDao.updateStatus(status,id);
	}
	@Override
	public List<EccStudormAttence> findCheckByCon(String unitId,final String studentId,String startTime,String endTime,Pagination page){
		//通过以上条件 以及查询条件 获取list  得到dorm_att_ids 即 此表的ids
		List<EccDormAttence>  dormAttenceList=eccDormAttenceService.findStatByCon(unitId,startTime,endTime);
		final Set<String> dormAttIds=new HashSet<String>();
		Set<String> periodIds=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(dormAttenceList) && StringUtils.isNotBlank(studentId)){
			for(EccDormAttence att:dormAttenceList){
				dormAttIds.add(att.getId());
				periodIds.add(att.getPeriodId());
			}
		}else{
			return null;
		}
		List<EccAttenceDormPeriod> periodList=eccAttenceDormPeriodService.findListByIds(periodIds.toArray(new String[0]));
		Map<String,EccAttenceDormPeriod> periodMap=EntityUtils.getMap(periodList,"id");
		//得到key-dorm_att_id   value-考勤时段
		Map<String,String> strTimeMap=new HashMap<String, String>();
		Map<String,Date> dateMap=new HashMap<String, Date>();
		for(EccDormAttence att:dormAttenceList){
			dateMap.put(att.getId(), att.getClockDate());
			EccAttenceDormPeriod period=periodMap.get(att.getPeriodId());
			if(period!=null){
				strTimeMap.put(att.getId(),period.getBeginTime()+"-"+period.getEndTime());
			}
		}
		List<EccStudormAttence> stuAttList=null;
		Specification<EccStudormAttence> specification = new Specification<EccStudormAttence>() {
            @Override
            public Predicate toPredicate(Root<EccStudormAttence> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                if (dormAttIds.size()> 0) {
                    In<String> in = cb.in(root.get("dormAttId").as(String.class));
                    for (String dormAttId:dormAttIds) {
                        in.value(dormAttId);
                    }
                    ps.add(in);
                }
                if(StringUtils.isNotBlank(studentId)){
                	ps.add(cb.equal(root.get("studentId").as(String.class), studentId));
                }
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("clockDate").as(Date.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<EccStudormAttence> findAll = eccStudormAttenceDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            stuAttList = findAll.getContent();
        }
        else {
        	stuAttList = eccStudormAttenceDao.findAll(specification);
        }
		
        if(CollectionUtils.isNotEmpty(stuAttList)){
        	for(EccStudormAttence att:stuAttList){
        		att.setPeriodTime(strTimeMap.get(att.getDormAttId()));
        		att.setDateTime(dateMap.get(att.getDormAttId()));
        	}
        }
        return stuAttList;
	}
	@Override
	public List<EccStudormAttence> findStatByDto(String unitId,AttanceSearchDto attDto,String acadyear,String semesterStr){
		//通过以上条件 以及查询条件 获取list  得到dorm_att_ids 即 此表的ids
		List<EccDormAttence>  dormAttenceList=eccDormAttenceService.findStatByCon(unitId,DateUtils.date2String(attDto.getStartTime(),"yyyy-MM-dd"),
													DateUtils.date2String(attDto.getEndTime(),"yyyy-MM-dd"));
		Set<String> dormAttIds=new HashSet<String>();
		for(EccDormAttence att:dormAttenceList){
			dormAttIds.add(att.getId());
		}
		List<EccStudormAttence> stuAttList=null;
		String classId=attDto.getClassId();
		if(CollectionUtils.isNotEmpty(dormAttIds)){
			stuAttList=eccStudormAttenceDao.findListByCon(dormAttIds.toArray(new String[0]), classId);
		}
		List<EccStudormAttence> factStuAttList=new ArrayList<EccStudormAttence>();
		if(CollectionUtils.isNotEmpty(stuAttList)){
			List<Student> studentList=SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}),new TR<List<Student>>(){});
			Map<String,String> studentNameMap=EntityUtils.getMap(studentList, "id", "studentName");	
			//获取key-studentid  value-roomName
			List<String[]> strList=SUtils.dt(stuworkRemoteService.findStuRoomNameByClassIds(unitId, acadyear, semesterStr, new String[]{classId}),new TR<List<String[]>>(){});
			Map<String,String> roomNameMap=new HashMap<String, String>();
			for(String[] strs:strList){
				roomNameMap.put(strs[0], strs[2]);
			}
			Map<String,List<EccStudormAttence>> stuIdListMap=new HashMap<String, List<EccStudormAttence>>();
			for(EccStudormAttence stuAtt:stuAttList){
				List<EccStudormAttence> list=stuIdListMap.get(stuAtt.getStudentId());
				if(CollectionUtils.isEmpty(list)){
					list=new ArrayList<EccStudormAttence>();
				}
				list.add(stuAtt);
				stuIdListMap.put(stuAtt.getStudentId(), list);
			}
			Set<Entry<String, List<EccStudormAttence>>> entries=stuIdListMap.entrySet();
			for(Entry<String, List<EccStudormAttence>> entry:entries){
				String studentId=entry.getKey();
				EccStudormAttence stuAtt=new EccStudormAttence();
				stuAtt.setStudentId(studentId);
				stuAtt.setStudentName(studentNameMap.get(studentId));
				stuAtt.setRoomName(roomNameMap.get(studentId));
				for(EccStudormAttence everAtt:entry.getValue()){
					if(everAtt.getStatus()==1){//未刷卡
						stuAtt.setNoNum(1+stuAtt.getNoNum());
					}else if(everAtt.getStatus()==2){//请假
						stuAtt.setLeaveNum(1+stuAtt.getLeaveNum());
					}else if(everAtt.getStatus()==3){//已签到
						stuAtt.setInNum(1+stuAtt.getInNum());
					}
				}
				factStuAttList.add(stuAtt);
			}
		}
		return factStuAttList;
	}
	@Override
	public List<EccStudormAttence> findListByDto(String unitId,AttanceSearchDto attDto,String acadyear,String semesterStr,Pagination page){
		List<EccStudormAttence> stuAttList=null;
		try {
			//通过buildingId获取所有的学生id
			Set<String> allstudentIds=new HashSet<String>();
			final int status=attDto.getAttStatus();
			if(StringUtils.isNotBlank(attDto.getBuildingId())){
				List<String> studentIdsList=SUtils.dt(stuworkRemoteService.findStuIdsByBuiId(unitId, attDto.getBuildingId(), acadyear, semesterStr),new TR<List<String>>(){});
				for(String studentId:studentIdsList){
					allstudentIds.add(studentId);
				}
			}
			//通过学生id获取所有的班级
			List<Student> studentList=SUtils.dt(studentRemoteService.findListByIds(allstudentIds.toArray(new String[0])),new TR<List<Student>>(){});
			Set<String> classIds=new HashSet<String>();
			for(Student stu:studentList){
				classIds.add(stu.getClassId());
			}
			//通过班级获取所有的年级id
			List<Clazz> clazzList=SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[0])),new TR<List<Clazz>>(){});
			Set<String> gradeIds=new HashSet<String>();
			for(Clazz clazz:clazzList){
				gradeIds.add(clazz.getGradeId());
			}
			Map<String,String> stuIdClassIdMap=EntityUtils.getMap(studentList, "id", "classId");
			Map<String,String> clIdOfgIdMap=EntityUtils.getMap(clazzList, "id","gradeId");	
			//每个寝室楼的  年级对应的 学生ids
			//Map<String,Set<String>> gIdofStuIdsMap=new HashMap<String, Set<String>>();
			Map<String,String> stuIdofgIdMap=new HashMap<String,String>();
			for(Student stu:studentList){
				//学生id 对应 年级id
				stuIdofgIdMap.put(stu.getId(), clIdOfgIdMap.get(stu.getClassId()));
			}
			List<Grade> gradeList=SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[0])),new TR<List<Grade>>(){});
			String gradeCode=attDto.getGradeCode();
			//得到gradeCode 对应gradeId
			Map<String,String>  gradeIdCodeMap=new HashMap<String, String>();
			Set<String> gradeCodes=new HashSet<String>();
			for(Grade grade:gradeList){
				if(StringUtils.isNotBlank(gradeCode)){
					if(grade.getGradeCode().equals(gradeCode)){
						gradeIds.clear();
						gradeIds.add(grade.getId());
						gradeCodes.add(gradeCode);
						gradeIdCodeMap.put(gradeCode,grade.getId());
						break;
					}else{
						gradeIds.clear();
					}
				}else{
					gradeCodes.add(grade.getGradeCode());
					gradeIdCodeMap.put(grade.getGradeCode(),grade.getId());
				}
			}
			
			Map<String,String> gradeNameMap=EntityUtils.getMap(gradeList, "id", "gradeName");
			
			Set<Integer> types=new HashSet<Integer>();//attDto.getSearchDate();
			Map<String, Integer> typeMap=eccDormAttenceService.findDormAttType(unitId, gradeIds.toArray(new String[0]), attDto.getSearchDate());
			Set<Entry<String, Integer>> entries=typeMap.entrySet();
			for(Entry<String, Integer> entry:entries){
				types.add(entry.getValue());
			}
			//通过type 以及 查询条件gradeCode(可为空)  来获取list  得到periodIds 
			List<EccAttenceDormGrade>  dormGradeList=eccAttenceDormGradeService.findListByCon(unitId, types.toArray(new Integer[0]), gradeCodes.toArray(new String[0]));
			Set<String>  periodIds=new HashSet<String>();
			//年级Id 对应时段ids
			Map<String,Set<String>> pIdGradeCosMap=new HashMap<String, Set<String>>();
			if(CollectionUtils.isNotEmpty(dormGradeList)){
				for(EccAttenceDormGrade dormGrade:dormGradeList){
					String periodId=dormGrade.getPeriodId();
					periodIds.add(periodId);
					
					Set<String> periodIdSets=pIdGradeCosMap.get(gradeIdCodeMap.get(dormGrade.getGrade()));
					if(CollectionUtils.isEmpty(periodIdSets)){
						periodIdSets=new HashSet<String>();
					}
					periodIdSets.add(periodId);
					pIdGradeCosMap.put(gradeIdCodeMap.get(dormGrade.getGrade()), periodIdSets);
				}
			}
			//为了得到对应的学生
			final Set<String> stuIdsLast=new HashSet<String>();
			if(StringUtils.isNotBlank(gradeCode)){
				String gradeId=gradeIdCodeMap.get(gradeCode);
				for(String stuId:allstudentIds){
					if(stuIdofgIdMap.get(stuId).equals(gradeId)){
						stuIdsLast.add(stuId);
					}
				}
			}else{
				stuIdsLast.addAll(allstudentIds);
			}
			//学生Id 对应时段ids
			Map<String,Set<String>> stuIdofPIdsMap=new HashMap<String, Set<String>>();
			for(String stuId:stuIdsLast){
				String gradeId=stuIdofgIdMap.get(stuId);
				stuIdofPIdsMap.put(stuId,pIdGradeCosMap.get(gradeId));
			}
			
			//判断明天是否为上课日  不是1  则需去和考勤表判断is_next_day_attence
			Map<String, Integer> typeMap1=eccDormAttenceService.findDormAttType(unitId, gradeIds.toArray(new String[0]), DateUtils.addDay(attDto.getSearchDate(),1));
			List<EccAttenceDormPeriod> periodList=eccAttenceDormPeriodService.findListByIds(periodIds.toArray(new String[0]));
			Map<String,EccAttenceDormPeriod> periodMap=EntityUtils.getMap(periodList,"id");
			
			Map<String,Set<String>> gIdOfPidsMap=new HashMap<String, Set<String>>();//最终结果的map
			Set<Entry<String, Integer>> entries1=typeMap1.entrySet();
			for(Entry<String, Integer> entry:entries1){//这个年级对应的所有考勤时段
				String gId=entry.getKey();
				int type=entry.getValue();
				if(type!=1){
					Set<String> pIds=pIdGradeCosMap.get(gId);
					if(CollectionUtils.isNotEmpty(pIds)){
						for(String pId:pIds){//判断是否为is_next_day_attence  true
							EccAttenceDormPeriod period=periodMap.get(pId);
							if(period!=null && period.isNextDayAttence()){
								Set<String> pids=gIdOfPidsMap.get(gId);
								if(CollectionUtils.isEmpty(pids)){
									pids=new HashSet<String>();
								}
								pids.add(pId);
								gIdOfPidsMap.put(gId, pids);//所有明日为休息日的情况
							}
						}
					}
				}
			}
			
			//通过以上条件 以及查询条件 获取list  得到dorm_att_ids 即 此表的ids用来判断是否需要初始化数据
			List<EccDormAttence>  dormAttenceList=eccDormAttenceService.findListByCon(unitId,attDto.getPeriodId(), DateUtils.date2String
															(attDto.getSearchDate(),"yyyy-MM-dd"),attDto.getBuildingId(),periodIds.toArray(new String[0]));
			//初始化表1 数据
			if(StringUtils.isNotBlank(attDto.getBuildingId())&&StringUtils.isBlank(attDto.getGradeCode())){
				setFirstAtt(unitId,attDto.getBuildingId(),DateUtils.date2String(attDto.getSearchDate(),"yyyy-MM-dd"),
						dormAttenceList,periodIds,attDto.getPeriodId());
			}
			
			//重新获取数据
			List<EccDormAttence>  dormAttenceList1=eccDormAttenceService.findListByCon(unitId,attDto.getPeriodId(), DateUtils.date2String
					(attDto.getSearchDate(),"yyyy-MM-dd"),attDto.getBuildingId(),periodIds.toArray(new String[0]));
			final Set<String>  dormAttIds=new HashSet<String>();
			if(CollectionUtils.isNotEmpty(dormAttenceList1)){
				for(EccDormAttence dormAttence:dormAttenceList1){
					dormAttIds.add(dormAttence.getId());
				}
			}else{
				return null;
			}
//			Set<String> stuIdsNoAtt=getStuLeaves(unitId, attDto.getSearchDate());
			//初始化表 2数据
			setFirstStu(dormAttIds.toArray(new String[0]),dormAttenceList1,stuIdsLast,stuIdClassIdMap,gIdOfPidsMap,stuIdofgIdMap,stuIdofPIdsMap);
			
			Specification<EccStudormAttence> specification = new Specification<EccStudormAttence>() {
	            @Override
	            public Predicate toPredicate(Root<EccStudormAttence> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
	                List<Predicate> ps = new ArrayList<Predicate>();
	                if (dormAttIds.size()> 0) {
	                    In<String> in = cb.in(root.get("dormAttId").as(String.class));
	                    for (String dormAttId:dormAttIds) {
	                        in.value(dormAttId);
	                    }
	                    ps.add(in);
	                }
	                if (stuIdsLast.size()> 0) {
	                	In<String> in = cb.in(root.get("studentId").as(String.class));
	                	for (String stuId:stuIdsLast) {
	                		in.value(stuId);
	                	}
	                	ps.add(in);
	                }
	                if(status!=0){
	                	ps.add(cb.equal(root.get("status").as(Integer.class), status));
	                }
	                List<Order> orderList = new ArrayList<Order>();
	                orderList.add(cb.asc(root.get("classId").as(String.class)));
	                orderList.add(cb.asc(root.get("clockDate").as(Date.class)));

	                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
	                return cq.getRestriction();
	            }
	        };
	        if (page != null) {
	            Pageable pageable = Pagination.toPageable(page);
	            Page<EccStudormAttence> findAll = eccStudormAttenceDao.findAll(specification, pageable);
	            page.setMaxRowCount((int) findAll.getTotalElements());
	            stuAttList = findAll.getContent();
	        }
	        else {
	        	stuAttList = eccStudormAttenceDao.findAll(specification);
	        }
			if(CollectionUtils.isNotEmpty(stuAttList)){
				Map<String,String> studentNameMap=EntityUtils.getMap(studentList, "id", "studentName");	
				Map<String,Clazz> classMap=EntityUtils.getMap(clazzList, "id");	
				
				//获取所有班主任
				Set<String> teacherIds=new HashSet<String>();
				for(Clazz clazz:clazzList){
					if(StringUtils.isNotBlank(clazz.getTeacherId())){
						teacherIds.add(clazz.getTeacherId());
					}
				}
				Map<String,String> teacherNameMap=null;
				if(CollectionUtils.isNotEmpty(teacherIds)){
					List<Teacher> teacherList=SUtils.dt(teacherRemoteService.findListByIds(teacherIds.toArray(new String[0])),new TypeReference<List<Teacher>>(){});
					teacherNameMap=EntityUtils.getMap(teacherList, "id","teacherName");
				}else{
					teacherNameMap=new HashMap<String, String>();
				}
				JSONArray array=JSONArray.parseArray(stuworkRemoteService.findStuIdRnameMapByBuiId(unitId, attDto.getBuildingId(), acadyear, semesterStr));
				Map<String,String> roomNameMap=new HashMap<String, String>();
				for(int i=0;i<array.size();i++){
					JSONObject json=array.getJSONObject(i);
					roomNameMap.put(json.getString("studentId"), json.getString("roomName"));
				}
				
				//请假详情信息
				Set<String> attIdSet=EntityUtils.getSet(stuAttList, "id");
				List<EccStuLeaveInfo> eccStuLeaveInfos = eccStuLeaveInfoService.findByStuDormAttIdIn(attIdSet.toArray(new String[0]));
				Set<String> leaveIds = EntityUtils.getSet(eccStuLeaveInfos, "leaveId");
				Map<String,String> contentMap=new HashMap<String, String>();
			
				Map<String,String> attContentMap= Maps.newHashMap();
				Set<String> firstSet = Sets.newHashSet();
				
				if(getOpenApiOfficeService()!=null){
					JSONArray arrayLeave=JSONArray.parseArray(getOpenApiOfficeService().getHwStuLeaveContent(leaveIds.toArray(new String[0])));
					for(int i=0;i<arrayLeave.size();i++){
						JSONObject json=arrayLeave.getJSONObject(i);
						contentMap.put(json.getString("studentId")+"_"+json.getString("leaveId"), json.getString("content"));
					}
					
					for (EccStuLeaveInfo eccStuLeaveInfo : eccStuLeaveInfos) {
						if (attContentMap.containsKey(eccStuLeaveInfo.getStuDormAttId())) {
							attContentMap.put(eccStuLeaveInfo.getStuDormAttId(), 
									attContentMap.get(eccStuLeaveInfo.getStuDormAttId()) + "<br/>" +
											contentMap.get(eccStuLeaveInfo.getStudentId()+"_"+eccStuLeaveInfo.getLeaveId()));
						} else {
							attContentMap.put(eccStuLeaveInfo.getStuDormAttId(), 
									contentMap.get(eccStuLeaveInfo.getStudentId()+"_"+eccStuLeaveInfo.getLeaveId()));
						}
						if (EccConstants.ECC_IS_FIRST_1.equals(eccStuLeaveInfo.getIsFirst())) {
							firstSet.add(eccStuLeaveInfo.getStuDormAttId());
						}
					}
				}
				
				for(EccStudormAttence stuAtt:stuAttList){
					stuAtt.setStudentName(studentNameMap.get(stuAtt.getStudentId()));
					Clazz clazz=classMap.get(stuAtt.getClassId());
					if(clazz!=null){
						stuAtt.setClassName(clazz.getClassName());
						stuAtt.setTeacherName(teacherNameMap.get(clazz.getTeacherId()));
						stuAtt.setGradeName(gradeNameMap.get(clazz.getGradeId()));
						stuAtt.setClassCode(clazz.getClassCode());
						stuAtt.setAcadyear(clazz.getAcadyear());
						stuAtt.setSection(clazz.getSection());
					}
					stuAtt.setRoomName(roomNameMap.get(stuAtt.getStudentId()));
					
					stuAtt.setContent(attContentMap.get(stuAtt.getId()));
					if (firstSet.contains(stuAtt.getId())) {
						stuAtt.setIsFirst(EccConstants.ECC_IS_FIRST_1);
					} else {
						stuAtt.setIsFirst(EccConstants.ECC_IS_FIRST_0);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return stuAttList;
	}
	//获取通校学生
	public Set<String> getStuLeaves(String unitId,Date searchDate){
		Set<String> stuIdsNoAtt=new HashSet<String>();
		if(getOpenApiOfficeService()==null){
			return stuIdsNoAtt;
		}
		String jsonStr2 = getOpenApiOfficeService().getHwStuLeavesByUnitId(unitId, null, "4", null, searchDate);
		JSONArray strings2 = EccUtils.getResultArray(jsonStr2, "studentIds");
		for (int i = 0; i < strings2.size(); i++) {
			stuIdsNoAtt.add(strings2.get(i).toString());
		}
		return stuIdsNoAtt;
	}	
	public void setFirstStu(String[] dormAttIds,List<EccDormAttence>  dormAttenceList,Set<String> stuIdsLast, Map<String,String> stuIdClassIdMap,
			Map<String,Set<String>> gIdOfPidsMap,Map<String,String> stuIdofgIdMap,Map<String,Set<String>> stuIdofPIdsMap){
		List<EccStudormAttence> stuAttList=eccStudormAttenceDao.findByInAttIds(dormAttIds);
		//用以判断 此学生对于此考勤是否有数据
		Map<String,EccStudormAttence> stuIdAttIdMap=new HashMap<String, EccStudormAttence>();
		if(CollectionUtils.isNotEmpty(stuAttList)){
			for(EccStudormAttence stuAtt:stuAttList){
				stuIdAttIdMap.put(stuAtt.getStudentId()+"_"+stuAtt.getDormAttId(), stuAtt);
			}
		}
		List<EccStudormAttence> stuAttListLast=new ArrayList<EccStudormAttence>();
		for(String studentId:stuIdsLast){
//			if(stuIdsNoAtt.contains(studentId)){
//				continue;
//			}
			for(EccDormAttence dormAttence:dormAttenceList){
				EccStudormAttence stuAtt=stuIdAttIdMap.get(studentId+"_"+dormAttence.getId());
				if(stuAtt==null){
					Set<String> pIds=stuIdofPIdsMap.get(studentId);
					if(CollectionUtils.isNotEmpty(pIds)){
						for(String pId:pIds){
							if(pId.equals(dormAttence.getPeriodId())){
								Set<String> pIds1=gIdOfPidsMap.get(stuIdofgIdMap.get(studentId));
								if(CollectionUtils.isNotEmpty(pIds1)){
									if(pIds1.contains(dormAttence.getPeriodId())){
										continue;//此年级下 此考勤时段第二日为休息日 所以不生成这条数据。
									}
								}
								EccStudormAttence stuAttLast=new EccStudormAttence();
								stuAttLast.setDormAttId(dormAttence.getId());
								stuAttLast.setStudentId(studentId);
								stuAttLast.setClassId(stuIdClassIdMap.get(studentId));
								stuAttLast.setStatus(EccConstants.DORM_ATTENCE_STATUS1);
								stuAttLast.setId(UuidUtils.generateUuid());
								stuAttListLast.add(stuAttLast);
							}
						}
					}
				}
			}
		}
		if(CollectionUtils.isNotEmpty(stuAttListLast)){
			saveAll(stuAttListLast.toArray(new EccStudormAttence[0]));
		}

	}
	public void setFirstAtt(String unitId,String buildingId,String searchDateStr,List<EccDormAttence>  dormAttenceList,
			Set<String>  periodIds,String pId){
		Map<String,EccDormAttence> dormAttMap=new HashMap<String, EccDormAttence>();
		if(CollectionUtils.isNotEmpty(dormAttenceList)){
			for(EccDormAttence dormAttence:dormAttenceList){
				dormAttMap.put(dormAttence.getPeriodId()+"_"+dormAttence.getPlaceId()+"_"+
								DateUtils.date2String(dormAttence.getClockDate(),"yyyy-MM-dd"),dormAttence);
			}
		}
		if(StringUtils.isNotBlank(pId)){
			periodIds.clear();
			periodIds.add(pId);
		}
		List<EccDormAttence> insertList=new ArrayList<EccDormAttence>();
		for(String periodId:periodIds){
			EccDormAttence dormAtt=dormAttMap.get(periodId+"_"+buildingId+"_"+searchDateStr);
			if(dormAtt==null){
				dormAtt=new EccDormAttence();
				dormAtt.setId(UuidUtils.generateUuid());
				dormAtt.setUnitId(unitId);
				dormAtt.setPeriodId(periodId);
				dormAtt.setPlaceId(buildingId);
				dormAtt.setUnitId(unitId);
				dormAtt.setOver(false);
				dormAtt.setClockDate(DateUtils.string2Date(searchDateStr));
				insertList.add(dormAtt);
			}
		}
		if(CollectionUtils.isNotEmpty(insertList)){
			eccDormAttenceService.saveAll(insertList.toArray(new EccDormAttence[0]));
		}	
	}

	@Override
	public List<EccStudormAttence> findByDormAttIdsNeedPush(String[] attIds) {
		return eccStudormAttenceDao.findByDormAttIdsNeedPush(attIds);
	}

	@Override
	public List<EccStudormAttence> findListLeaveStudent(
			String[] dormAttenceIds, String[] studentIds) {
		return eccStudormAttenceDao.findListLeaveStudent(dormAttenceIds,studentIds);
	} 
}
