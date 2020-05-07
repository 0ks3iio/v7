package net.zdsoft.studevelop.data.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.dao.StuDevelopPunishmentDao;
import net.zdsoft.studevelop.data.entity.StuDevelopPunishment;
import net.zdsoft.studevelop.data.service.StuDevelopPunishmentService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Service("stuDevelopPunishmentService")
public class StuDevelopPunishmentServiceImpl extends BaseServiceImpl<StuDevelopPunishment, String> implements StuDevelopPunishmentService{
    @Autowired
	private StuDevelopPunishmentDao stuDevelopPunishmentDao;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    
	@Override
	protected BaseJpaRepositoryDao<StuDevelopPunishment, String> getJpaDao() {
		return stuDevelopPunishmentDao;
	}

	@Override
	protected Class<StuDevelopPunishment> getEntityClass() {
		return StuDevelopPunishment.class;
	}

	@Override
	public List<StuDevelopPunishment> findListByAll(String acadyear,
			String semester, String unitId, String classId, String studentId, String punishtype, Pagination page) {
		Set<String> stuIdSet = new HashSet<String>();
		if(StringUtils.isNotBlank(classId)){
			List<Student> stuList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>(){});
			for(Student stu : stuList){
				stuIdSet.add(stu.getId());
			}
		}
		Pageable pageable = Pagination.toPageable(page);
		if(StringUtils.isBlank(classId) && StringUtils.isBlank(studentId) && StringUtils.isBlank(punishtype)){
			//根据单位查找
			List<StuDevelopPunishment> stuDevelopPunishmentList = stuDevelopPunishmentDao.findListBySchId(acadyear, semester, unitId, pageable);
			int count = stuDevelopPunishmentDao.findListBySchId(acadyear, semester, unitId).size();
			page.setMaxRowCount(count);
			return stuDevelopPunishmentList;
		}else if(!StringUtils.isBlank(classId) && StringUtils.isBlank(studentId) && StringUtils.isBlank(punishtype)){
			//根据班级查找
			if(CollectionUtils.isEmpty(stuIdSet)){
				return new ArrayList<StuDevelopPunishment>();
			}
			List<StuDevelopPunishment> stuDevelopPunishmentList = stuDevelopPunishmentDao.findByStuIds(acadyear, semester, stuIdSet.toArray(new String[0]), pageable);
			int count = stuDevelopPunishmentDao.findByStuIds(acadyear, semester, stuIdSet.toArray(new String[0])).size();
			page.setMaxRowCount(count);
			return stuDevelopPunishmentList;			
		}else if(!StringUtils.isBlank(studentId) && StringUtils.isBlank(punishtype)){
			//根据学生查找
			List<StuDevelopPunishment> stuDevelopPunishmentList = stuDevelopPunishmentDao.findByStuId(acadyear, semester, studentId, pageable);
			int count = stuDevelopPunishmentDao.findByStuId(acadyear, semester, studentId).size();
			page.setMaxRowCount(count);
			return stuDevelopPunishmentList;
		}else if(StringUtils.isBlank(classId) && StringUtils.isBlank(studentId) && !StringUtils.isBlank(punishtype)){
			//单位加类型
			List<StuDevelopPunishment> stuDevelopPunishmentList = stuDevelopPunishmentDao.findByUnitIdAndLevel(acadyear, semester, unitId, punishtype, pageable);
			int count = stuDevelopPunishmentDao.findByUnitIdAndLevel(acadyear, semester, unitId, punishtype).size();
			page.setMaxRowCount(count);
			return stuDevelopPunishmentList;
		}else if(!StringUtils.isBlank(classId) && StringUtils.isBlank(studentId) && !StringUtils.isBlank(punishtype)){
			//班级加类型
			if(CollectionUtils.isEmpty(stuIdSet)){
				return new ArrayList<StuDevelopPunishment>();
			}
			List<StuDevelopPunishment> stuDevelopPunishmentList = stuDevelopPunishmentDao.findByStuidsAndLevel(acadyear, semester, punishtype, stuIdSet.toArray(new String[0]), pageable);
			int count = stuDevelopPunishmentDao.findByStuidsAndLevel(acadyear, semester, punishtype, stuIdSet.toArray(new String[0])).size();
			page.setMaxRowCount(count);
			return stuDevelopPunishmentList;
		}else if(!StringUtils.isBlank(studentId) && !StringUtils.isBlank(punishtype)){
			//学生加类型
			List<StuDevelopPunishment> stuDevelopPunishmentList = stuDevelopPunishmentDao.findByStuidAndLevel(acadyear, semester, studentId, punishtype, pageable);
			int count = stuDevelopPunishmentDao.findByStuidAndLevel(acadyear, semester, studentId, punishtype).size();
			page.setMaxRowCount(count);
			return stuDevelopPunishmentList;
		}else{
			return new ArrayList<StuDevelopPunishment>();
		}
		
	}

	@Override
	public StuDevelopPunishment findById(String id) {
		return stuDevelopPunishmentDao.findById(id).orElse(null);
	}

	@Override
	public void deleteById(String id) {
		stuDevelopPunishmentDao.deleteById(id);		
	}

	@Override
	public List<StuDevelopPunishment> findByAcaAndSemAndStuId(String acadyear,
			String semester, String studentId) {
		return stuDevelopPunishmentDao.findByAcaAndSemAndStuId(acadyear, semester, studentId);
	}

	@Override
	public String doImport(String unitId, List<String[]> datas,
			String acadyear, String semester, String ownerId) {
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount  =0;
		String[] errorData=null;
		Set<String> stuCodeSet = new HashSet<String>();
		List<StuDevelopPunishment> insertList=new ArrayList<StuDevelopPunishment>();
		for(String[] arr : datas){
			stuCodeSet.add(arr[1]);
        }
		List<Student> stuList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(unitId, stuCodeSet.toArray(new String[0])), new TR<List<Student>>() {});
        Map<String, String> stuCodeNameMap = new HashMap<String, String>();
        Map<String, String> stuCodeIdMap = new HashMap<String, String>();
		for(Student stu : stuList){
			stuCodeNameMap.put(stu.getStudentCode(), stu.getStudentName());
			stuCodeIdMap.put(stu.getStudentCode(), stu.getId());
        }
		List<McodeDetail> McodeDetailList = SUtils.dt(mcodeRemoteService.findByMcodeIds(new String[]{"DM-CFMC","DM-CCDW"}), new TR<List<McodeDetail>>() {});
		Map<String, String> punishTypeMap = new HashMap<String, String>();
		Map<String, String> punishUnitMap = new HashMap<String, String>();
		for(McodeDetail mcode : McodeDetailList){
			if("DM-CFMC".equals(mcode.getMcodeId())){
				punishTypeMap.put(mcode.getMcodeContent(), mcode.getThisId());
			}else{
				punishUnitMap.put(mcode.getMcodeContent(), mcode.getThisId());
			}
		}
		for(String[] arr : datas){
			StuDevelopPunishment item = new StuDevelopPunishment();
			if(StringUtils.isBlank(arr[0])){
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学生姓名";
				errorData[2]="";
				errorData[3]="学生姓名不能为空";
				errorDataList.add(errorData);
				continue;
        	}
			if(StringUtils.isBlank(arr[1])){
        		errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="学号";
				errorData[2]="";
				errorData[3]="学号不能为空";
				errorDataList.add(errorData);
				continue;
        	}else{
        		if(StringUtils.isBlank(stuCodeNameMap.get(arr[1]))){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="学号";
    				errorData[2]=arr[1];
    				errorData[3]="不存在该学号所属的学生";
    				errorDataList.add(errorData);
    				continue;
        		}else{
        			if(StringUtils.isNotBlank(arr[0]) && !arr[0].equals(stuCodeNameMap.get(arr[1]))){
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
			if(StringUtils.isBlank(arr[4])){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="惩处名称";
				errorData[2]="";
				errorData[3]="惩处名称不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isNotBlank(arr[5])){
        		if(null == punishTypeMap.get(arr[5])){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="惩处类型";
    				errorData[2]=arr[2];
    				errorData[3]="不存在对应的惩处类型";
    				errorDataList.add(errorData);
    				continue;
        		}
			}else{
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="惩处类型";
				errorData[2]="";
				errorData[3]="惩处类型不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isNotBlank(arr[6])){
        		if(null == punishUnitMap.get(arr[6])){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="惩处单位";
    				errorData[2]=arr[2];
    				errorData[3]="不存在对应的惩处单位";
    				errorDataList.add(errorData);
    				continue;
        		}
			}else{
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="惩处单位";
				errorData[2]="";
				errorData[3]="惩处单位不能为空";
				errorDataList.add(errorData);
				continue;
			}
			Date punishdate = null;
			if(StringUtils.isNotBlank(arr[9])){
				try {
					punishdate = DateUtils.parseDate(arr[9].trim(), "yyyyMMdd");
				} catch (ParseException e1) {
					e1.printStackTrace();
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="惩处日期";
					errorData[2]=arr[0];
					errorData[3]="惩处日期格式不对";
					errorDataList.add(errorData);
					continue;
				}
			}else{
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="惩处日期";
				errorData[2]="";
				errorData[3]="惩处日期不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isBlank(arr[10])){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="惩处文号";
				errorData[2]="";
				errorData[3]="惩处文号不能为空";
				errorDataList.add(errorData);
				continue;
			}
			Date canceldate = null;
			if(StringUtils.isNotBlank(arr[11])){
				try {
					canceldate = DateUtils.parseDate(arr[11].trim(), "yyyyMMdd");
				} catch (ParseException e1) {
					e1.printStackTrace();
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="撤销日期";
					errorData[2]=arr[0];
					errorData[3]="撤销日期格式不对";
					errorDataList.add(errorData);
					continue;
				}
			}
			if(StringUtils.isNotBlank(arr[9]) && StringUtils.isNotBlank(arr[11])){				
				try {
					punishdate = DateUtils.parseDate(arr[9].trim(), "yyyyMMdd");
					canceldate = DateUtils.parseDate(arr[11].trim(), "yyyyMMdd");
					if(punishdate.getTime()>canceldate.getTime()){
						errorData = new String[4];
						errorData[0]=errorDataList.size()+1+"";
						errorData[1]="撤销日期";
						errorData[2]=arr[0];
						errorData[3]="惩处日期不能大于撤销日期";
						errorDataList.add(errorData);
						continue;
					}
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			if(StringUtils.isBlank(arr[13])){
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="惩处原因";
				errorData[2]="";
				errorData[3]="惩处原因不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isNotBlank(arr[7])){
				try {
					int t = Integer.parseInt(arr[7]);
				} catch (Exception e1) {
					e1.printStackTrace();
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="惩处个人扣分";
					errorData[2]="";
					errorData[3]="惩处个人扣分格式不对，必须是整数";
					errorDataList.add(errorData);
					continue;
				}				
			}
			if(StringUtils.isNotBlank(arr[8])){
				try {
					int p = Integer.parseInt(arr[8]);
				} catch (Exception e1) {
					e1.printStackTrace();
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="惩处班级扣分";
					errorData[2]="";
					errorData[3]="惩处班级扣分格式不对，必须是整数";
					errorDataList.add(errorData);
					continue;
				}	
			}
			if(null != canceldate){
				item.setCanceldate(canceldate);
			}
			item.setStuid(stuCodeIdMap.get(arr[1]));
			item.setSchid(unitId);
			item.setAcadyear(acadyear);
			item.setSemester(semester);
			item.setOperateUserId(ownerId);
			item.setPunishname(arr[4]);
			item.setPunishType(punishTypeMap.get(arr[5]));
			item.setPunishUnit(punishUnitMap.get(arr[6]));
			if(StringUtils.isNotBlank(arr[7])){
				item.setSelfScore(Integer.parseInt(arr[7]));
			}
			item.setPunishdate(punishdate);
			item.setPunishfilecode(arr[10]);
			item.setCancelfilecode(arr[12]);
			item.setPunishreason(arr[13]);
			if(StringUtils.isNotBlank(arr[8])){
				item.setClassScore(Integer.parseInt(arr[8]));
			}
			item.setRemark(arr[14]);
			item.setId(UuidUtils.generateUuid());
			insertList.add(item);
        	successCount++;
		}			
		if (CollectionUtils.isNotEmpty(insertList)) {
		    if ( insertList.size() > 1000 ) {
		        int loopNumber = insertList.size()/1000;
		        for (int i=0; i<loopNumber; i++ ) {
		            List<StuDevelopPunishment> list1 = insertList.subList(i * 1000, (i+1)*1000);
		            if ( list1 != null ) {
		            	StuDevelopPunishment[] inserts = list1.toArray(new StuDevelopPunishment[0]);
		            	saveAll(inserts);
		            }
		            if ( i+1 == loopNumber &&  insertList.size() -(1000 * loopNumber) > 0) {
		                List<StuDevelopPunishment> list2 = insertList.subList((i+1) * 1000, insertList.size());
		                if ( list2 != null ) {
		                	StuDevelopPunishment[] inserts = list2.toArray(new StuDevelopPunishment[0]);
		                	saveAll(inserts);
		                }
		           }
		       }
		     } else {
		    	  StuDevelopPunishment[] inserts = insertList.toArray(new StuDevelopPunishment[0]);
		    	  saveAll(inserts);
		    }
		}
		importResultJson.put("totalCount", datas.size());
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorDataList.size());
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}

}
