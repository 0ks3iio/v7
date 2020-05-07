package net.zdsoft.stuwork.data.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.entity.DateInfo;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.DateInfoRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.constants.StuworkConstants;
import net.zdsoft.stuwork.data.dao.DyStuPunishmentDao;
import net.zdsoft.stuwork.data.entity.DyBusinessOption;
import net.zdsoft.stuwork.data.entity.DyStuPunishment;
import net.zdsoft.stuwork.data.service.DyBusinessOptionService;
import net.zdsoft.stuwork.data.service.DyStuPunishmentService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
@Service("dyStuPunishmentService")
public class DyStuPunishmentServiceImpl extends BaseServiceImpl<DyStuPunishment, String> implements DyStuPunishmentService{
    @Autowired
	private DyStuPunishmentDao dyStuPunishmentDao;	
	@Override
	protected BaseJpaRepositoryDao<DyStuPunishment, String> getJpaDao() {
		return dyStuPunishmentDao;
	}	
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private DyBusinessOptionService dyBusinessOptionService;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private DateInfoRemoteService dateInfoRemoteService;
	
	@Override
	protected Class<DyStuPunishment> getEntityClass() {
		return DyStuPunishment.class;
	}
	@Override
	public Map<String,Float> findMapBy(String unitId,String acadyear,String semester,int week){
		Map<String,Float> classIdOfScoreMap=new HashMap<String,Float>();
		List<DateInfo> dateInfoList=SUtils.dt(dateInfoRemoteService.findByWeek(unitId, acadyear, Integer.parseInt(semester), week),new TR<List<DateInfo>>(){});
		if(CollectionUtils.isNotEmpty(dateInfoList)){
			Date startTime=dateInfoList.get(0).getInfoDate();
			Date endTime=dateInfoList.get(dateInfoList.size()-1).getInfoDate();
			
			List<DyStuPunishment> punishList=dyStuPunishmentDao.getALL(unitId, startTime, endTime);
			Set<String> studentIds=EntityUtils.getSet(punishList, "studentId");
			Map<String,String> stuIdOfClaIdMap=EntityUtils.getMap(SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])),new TR<List<Student>>(){}),"id","classId");
			if(CollectionUtils.isNotEmpty(punishList)){
				for(DyStuPunishment punish:punishList){
					String classId=stuIdOfClaIdMap.get(punish.getStudentId());
					Float score=classIdOfScoreMap.get(classId);
					if(score==null) score=0f;
					classIdOfScoreMap.put(classId, score+punish.getScore());
				}
			}
		}
		return classIdOfScoreMap;
	}
	
	@Override
	public List<DyStuPunishment> findByAll(final String unitId, String acadyear, String semester, final String punishTypeId,
			final Date startTime, final Date endTime, final String[] studentIds, Pagination page) {
		Specification<DyStuPunishment> specification = new Specification<DyStuPunishment>() {
			@Override
			public Predicate toPredicate(Root<DyStuPunishment> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				if(null!=acadyear){
					ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
				}
				if(null!=semester){
					ps.add(cb.equal(root.get("semester").as(String.class), semester));
				}
                if(null!=startTime){
                	ps.add(cb.greaterThanOrEqualTo(root.<Date>get("punishDate"), startTime));
                }
                if(null!=endTime){
                	ps.add(cb.lessThanOrEqualTo(root.<Date>get("punishDate"), endTime));
                }
                if(StringUtils.isNotBlank(punishTypeId)){
                	ps.add(cb.equal(root.get("punishTypeId").as(String.class), punishTypeId));
                }
                if(null!=studentIds && studentIds.length>0){
                	//cq.where(cb.in(root.get("studentId").in(studentIds)));
                	/*In<String> in = cb.in(root.get("studentId").as(String.class));
                    for (int i = 0; i < studentIds.length; i++) {
                        in.value(studentIds[i]);
                    }
                    ps.add(in);*/
                    
                	List<Predicate> inPredicates = Lists.newArrayList();
                    if (studentIds.length <= 1000) {
            			ps.add(root.<DyStuPunishment> get("studentId").in((String[]) studentIds));
            		} else {
            			int cyc = studentIds.length / 1000 + (studentIds.length % 1000 == 0 ? 0 : 1);
            			for (int i = 0; i < cyc; i++) {
            				int max = (i + 1) * 1000;
            				if (max > studentIds.length)
            					max = studentIds.length;
            				Predicate p = root.<DyStuPunishment> get("studentId").in((Object[]) ArrayUtils.subarray(studentIds, i * 1000, max));
            				inPredicates.add(p);
            			}
            			if ( cb != null ) {
            				ps.add(cb.or(inPredicates.toArray(new Predicate[inPredicates.size()])));
            			} else {
            				ps.addAll(inPredicates);
            			}
            		}
                    
                }
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.desc(root.<Date>get("punishDate")));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }			
		};
		if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<DyStuPunishment> findAll = dyStuPunishmentDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            return findAll.getContent();
        }
        else {
            return dyStuPunishmentDao.findAll(specification);
        }
	}
	

	@Override
	public String dealImport(String unitId, String importType, List<String[]> datas) {
		Json importResultJson=new Json();
		List<String> semesterList = new ArrayList<>();
		semesterList.add("第一学期");
		semesterList.add("第二学期");
		List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), String.class);
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount = 0;
		String[] errorData;
		Set<String> stuCodeSet = new HashSet<String>();
		List<DyStuPunishment> insertList=new ArrayList<DyStuPunishment>();
		for(String[] arr : datas){
			stuCodeSet.add(arr[3]);
        }
		List<Student> stuList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(unitId, stuCodeSet.toArray(new String[0])), new TR<List<Student>>() {});
        Map<String, String> stuCodeNameMap = new HashMap<String, String>();
        Map<String, String> stuCodeIdMap = new HashMap<String, String>();
        List<DyBusinessOption> dyBusinessOptionList = dyBusinessOptionService.findListByUnitIdAndType(unitId, StuworkConstants.BUSINESS_TYPE_1);
		Map<String, String> punishNameScoreMap = new HashMap<String, String>();
		Map<String, String> punishNameIdMap = new HashMap<String, String>();
		Map<String, String> punishNameTypeMap = new HashMap<String, String>();
		Map<String, String> punishNameZfTypeMap = new HashMap<String, String>();
        for(DyBusinessOption item : dyBusinessOptionList){
        	punishNameScoreMap.put(item.getOptionName(), String.valueOf(item.getScore()));
        	punishNameIdMap.put(item.getOptionName(), item.getId());
        	punishNameTypeMap.put(item.getOptionName(), String.valueOf(item.getIsCustom()));
        	punishNameZfTypeMap.put(item.getOptionName(), String.valueOf(item.getHasScore()));
		}
        for(Student stu : stuList){
			stuCodeNameMap.put(stu.getStudentCode(), stu.getStudentName());
			stuCodeIdMap.put(stu.getStudentCode(), stu.getId());
        }
		
		for(String[] arr : datas){
        	DyStuPunishment item = new DyStuPunishment();
        	if (StringUtils.isBlank(arr[0])) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学年";
				errorData[2]="";
				errorData[3]="学年为空";
				errorDataList.add(errorData);
				continue;
			} else {
        		if (!acadyearList.contains(arr[0])) {
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学年";
					errorData[2]="";
					errorData[3]="学年不正确，格式为2018-2019";
					errorDataList.add(errorData);
					continue;
				}
			}
			if (StringUtils.isBlank(arr[1])) {
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学期";
				errorData[2]="";
				errorData[3]="学期为空";
				errorDataList.add(errorData);
				continue;
			} else {
				if (!semesterList.contains(arr[1])) {
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="学期";
					errorData[2]="";
					errorData[3]="学期不正确";
					errorDataList.add(errorData);
					continue;
				} else {
					if (StringUtils.equals("第一学期", arr[1])) {
						arr[1] = "1";
					}
					if (StringUtils.equals("第二学期", arr[1])) {
						arr[1] = "2";
					}
				}
			}
        	if(StringUtils.isBlank(arr[2])){
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学生姓名";
				errorData[2]="";
				errorData[3]="学生姓名不能为空";
				errorDataList.add(errorData);
				continue;
        	}
        	if(StringUtils.isBlank(arr[3])){
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学号";
				errorData[2]="";
				errorData[3]="学号不能为空";
				errorDataList.add(errorData);
				continue;
        	}else{
        		if(StringUtils.isBlank(stuCodeNameMap.get(arr[3]))){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="学号";
    				errorData[2]=arr[1];
    				errorData[3]="不存在该学号所属的学生";
    				errorDataList.add(errorData);
    				continue;
        		}else{
        			if(StringUtils.isNotBlank(arr[2]) && !arr[2].equals(stuCodeNameMap.get(arr[3]))){
        				errorData = new String[4];
        				errorData[0]=errorDataList.size()+1+"";
        				errorData[1]="学号";
        				errorData[2]="姓名："+arr[0]+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;学号："+arr[1];
        				errorData[3]="学生姓名与该学号不匹配";
        				errorDataList.add(errorData);
        				continue;
        			}
        		}
        	}
        	if(StringUtils.isBlank(arr[5])){
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="违纪原因";
				errorData[2]="";
				errorData[3]="违纪原因不能为空";
				errorDataList.add(errorData);
				continue;
        	}
        	if(StringUtils.isNotBlank(arr[7])){
        		String date_string=arr[7];
        	    //SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        	    
        	    boolean dateflag=true;
        	    Date date = null;
        	    try{
        	    	//date = format.parse(date_string);
        	    	date = DateUtils.parseDate(date_string.trim(), "yyyyMMdd");
        	    }catch (ParseException e){
        	        dateflag=false;
        	    }finally{
        	        System.out.println("日期是否满足要求"+dateflag);
        	    }
        	    if(dateflag){
        	    	item.setPunishDate(date);
        	    }else{
        	    	errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="违纪时间";
    				errorData[2]=arr[7];
    				errorData[3]="违纪时间不符合格式要求";
    				errorDataList.add(errorData);
    				continue;
        	    }
        	}else{
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="违纪时间";
				errorData[2]="";
				errorData[3]="违纪时间不能为空";
				errorDataList.add(errorData);
				continue;
        	}
        	if(StringUtils.isNotBlank(arr[4])){
        		if(null == punishNameIdMap.get(arr[4])){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="违纪类型";
    				errorData[2]=arr[2];
    				errorData[3]="不存在对应的违纪类型";
    				errorDataList.add(errorData);
    				continue;
        		}else{
        			if("1".equals(punishNameZfTypeMap.get(arr[4]))){
        				if(StuworkConstants.PUNISH_CUSTOM_TYPE_1.equals(punishNameTypeMap.get(arr[4]))){
        					if(StringUtils.isBlank(arr[6])){
        		        		errorData = new String[4];
        						errorData[0]=errorDataList.size()+1+"";
        						errorData[1]="分数";
        						errorData[2]="";
        						errorData[3]="违纪类型为自定义折分时分数不能为空！";
        						errorDataList.add(errorData);
        						continue;
        		        	}
        					/*boolean numFlag = true;
        					for (int i = arr[4].length();--i>=0;){  
        						if(!Character.isDigit(arr[4].charAt(i))){
        							numFlag = false;
        						}
        					}
        					if (!numFlag){
    							errorData = new String[4];
          	    				errorData[0]=errorDataList.size()+1+"";
          	    				errorData[1]="分数";
          	    				errorData[2]=arr[4];
          	    				errorData[3]="请输入正确的分数，0-999999并且可以保留两位小数的数字！";
          	    				errorDataList.add(errorData);
          	    				continue;  
    						}*/
        					if(!(Float.parseFloat(arr[6])>=0 && Float.parseFloat(arr[6])<=999999)){
        						errorData = new String[4];
        	    				errorData[0]=errorDataList.size()+1+"";
        	    				errorData[1]="分数";
        	    				errorData[2]=arr[4];
        	    				errorData[3]="请输入正确的分数，0-999999并且可以保留一位小数的数字！";
        	    				errorDataList.add(errorData);
        	    				continue;
        					}
            				item.setScore(Float.parseFloat(arr[6]));
            			}else{
            				item.setScore(Float.parseFloat(punishNameScoreMap.get(arr[4])));
            			}
        			}else{
        				item.setScore(0);
        			}       			
        			//item.setPunishTypeId(punishNameIdMap.get(arr[2]));
        		}
        	}else{
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="违纪类型";
				errorData[2]="";
				errorData[3]="违纪类型不能为空";
				errorDataList.add(errorData);
				continue;
        	}
        	item.setUnitId(unitId);
        	item.setAcadyear(arr[0]);
        	item.setSemester(arr[1]);
        	item.setStudentId(stuCodeIdMap.get(arr[3]));
        	item.setPunishTypeId(punishNameIdMap.get(arr[4]));
        	item.setPunishContent(arr[5]);
        	item.setPunishName(arr[4]);
        	//item.setPunishDate(arr[4]);
        	insertList.add(item);
        	successCount++;
        }
		
		if("1".equals(importType)){//根据学年学期覆盖更新       先删除再新增
			Set<String> acadSemStringSet = new HashSet<>();
			for(DyStuPunishment punishment : insertList){
				acadSemStringSet.add(punishment.getAcadyear()+","+punishment.getSemester());
			}
			for(String acadSem : acadSemStringSet){
				String acadyear = acadSem.split(",")[0];
				String semester = acadSem.split(",")[1];
				dyStuPunishmentDao.deleteByUnitIdAndAcadyearAndSemester(unitId, acadyear, semester);
			}
		}
		
		if(CollectionUtils.isNotEmpty(insertList)){
			DyStuPunishment[] inserts = insertList.toArray(new DyStuPunishment[0]);
			checkSave(inserts);
			saveAll(inserts);
		}
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
				
	}

	@Override
	public List<DyStuPunishment> getScortByStudentId(String studentId) {
		return dyStuPunishmentDao.getScoreByStudentId(studentId);
	}

	@Override
	public List<DyStuPunishment> getScortByStudentIdIn(final String[] studentIds) {
//		Specification<DyStuPunishment> specification = new Specification<DyStuPunishment>() {
//			@Override
//			public Predicate toPredicate(Root<DyStuPunishment> root,
//					CriteriaQuery<?> cq, CriteriaBuilder cb) {
//				List<Predicate> ps = Lists.newArrayList();
//                if(null!=studentIds && studentIds.length>0){
//                	queryIn("studentId", studentIds, root, ps, null);  				
//                }
//                return cb.or(ps.toArray(new Predicate[0]));
//            }			
//		};
//		return dyStuPunishmentDao.findAll(specification);
		List<DyStuPunishment> list=new ArrayList<>();
		if(studentIds!=null && studentIds.length>0) {
			if(studentIds.length<=1000) {
				return dyStuPunishmentDao.getListByStudentIdIn(studentIds);
			}else {
				int cyc = studentIds.length / 1000 + (studentIds.length % 1000 == 0 ? 0 : 1);
				for (int i = 0; i < cyc; i++) {
					int max = (i + 1) * 1000;
					if (max > studentIds.length)
						max = studentIds.length;
					List<DyStuPunishment> list1 = dyStuPunishmentDao.getListByStudentIdIn(ArrayUtils.subarray(studentIds, i * 1000, max));
					if(CollectionUtils.isNotEmpty(list1)) {
						list.addAll(list1);
					}
				}
			}
		}
		return list;
       
	}

	@Override
	public List<DyStuPunishment> findByUnitIdAndPunishTypeIds(String unitId, String[] punishTypeIds) {
		if(StringUtils.isBlank(unitId) || ArrayUtils.isEmpty(punishTypeIds)){
			return new ArrayList<DyStuPunishment>();
		}
		return dyStuPunishmentDao.findByUnitIdAndPunishTypeIdIn(unitId, punishTypeIds);
	}
	@Override
	public List<DyStuPunishment> findByUnitIdAndStuIdIn(String unitId, String acadyear, String semester,String[] studentIds){
		if(ArrayUtils.isEmpty(studentIds)){
			return dyStuPunishmentDao.findByUnitIdAnd(unitId, acadyear, semester);
		}
		return dyStuPunishmentDao.findByUnitIdAndStuIdIn(unitId, acadyear, semester, studentIds);
	}
	@Override
	public void deleteByUnitIdAndAcadyearAndSemester(String unitId, String acadyear, String semester) {
		dyStuPunishmentDao.deleteByUnitIdAndAcadyearAndSemester(unitId, acadyear, semester);
	}

}
