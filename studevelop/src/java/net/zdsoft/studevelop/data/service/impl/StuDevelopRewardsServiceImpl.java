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
import net.zdsoft.studevelop.data.dao.StuDevelopRewardsDao;
import net.zdsoft.studevelop.data.entity.StuDevelopRewards;
import net.zdsoft.studevelop.data.service.StuDevelopRewardsService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Service("stuDevelopRewardsService")
public class StuDevelopRewardsServiceImpl extends BaseServiceImpl<StuDevelopRewards, String> implements StuDevelopRewardsService{
    @Autowired
	private StuDevelopRewardsDao stuDevelopRewardsDao;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
	@Override
	protected BaseJpaRepositoryDao<StuDevelopRewards, String> getJpaDao() {
		return stuDevelopRewardsDao;
	}

	@Override
	protected Class<StuDevelopRewards> getEntityClass() {
		return StuDevelopRewards.class;
	}

	@Override
	public List<StuDevelopRewards> findListByAcaAndSemAndUnitId(
			String acadyear, String semester, String unitId, String classId,
			String studentId, String rewardslevel, Pagination page) {
		Set<String> stuIdSet = new HashSet<String>();
		if(StringUtils.isNotBlank(classId)){
			List<Student> stuList = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), new TR<List<Student>>(){});
			for(Student stu : stuList){
				stuIdSet.add(stu.getId());
			}
		}
		
		Pageable pageable = Pagination.toPageable(page);
		if(StringUtils.isBlank(classId) && StringUtils.isBlank(studentId) && StringUtils.isBlank(rewardslevel)){
			//根据单位查找
			List<StuDevelopRewards> stuDevelopRewardsList = stuDevelopRewardsDao.findListByAcaAndSemAndUnitId(acadyear, semester, unitId, pageable);
			int count = stuDevelopRewardsDao.findListByAcaAndSemAndUnitId(acadyear, semester, unitId).size();
			page.setMaxRowCount(count);
			return stuDevelopRewardsList;
		}else if(!StringUtils.isBlank(classId) && StringUtils.isBlank(studentId) && StringUtils.isBlank(rewardslevel)){
			//根据班级查找
			if(CollectionUtils.isEmpty(stuIdSet)){
				return new ArrayList<StuDevelopRewards>();
			}			
			List<StuDevelopRewards> stuDevelopRewardsList = stuDevelopRewardsDao.findByStuIds(acadyear, semester, stuIdSet.toArray(new String[0]), pageable);
			int count = stuDevelopRewardsDao.findByStuIds(acadyear, semester, stuIdSet.toArray(new String[0])).size();
			page.setMaxRowCount(count);
			return stuDevelopRewardsList;
		}else if(!StringUtils.isBlank(studentId) && StringUtils.isBlank(rewardslevel)){
			//根据学生查找		
			List<StuDevelopRewards> stuDevelopRewardsList = stuDevelopRewardsDao.findByStuId(acadyear, semester, studentId, pageable);
			int count = stuDevelopRewardsDao.findByStuId(acadyear, semester, studentId).size();
			page.setMaxRowCount(count);
			return stuDevelopRewardsList;
		}else if(StringUtils.isBlank(classId) && StringUtils.isBlank(studentId) && !StringUtils.isBlank(rewardslevel)){
			//单位加级别
			List<StuDevelopRewards> stuDevelopRewardsList = stuDevelopRewardsDao.findByUnitIdAndLevel(acadyear, semester, unitId, rewardslevel, pageable);
			int count = stuDevelopRewardsDao.findByUnitIdAndLevel(acadyear, semester, unitId, rewardslevel).size();
			page.setMaxRowCount(count);
			return stuDevelopRewardsList;
		}else if(!StringUtils.isBlank(classId) && StringUtils.isBlank(studentId) && !StringUtils.isBlank(rewardslevel)){
			//班级加级别
			if(CollectionUtils.isEmpty(stuIdSet)){
				return new ArrayList<StuDevelopRewards>();
			}			
			List<StuDevelopRewards> stuDevelopRewardsList = stuDevelopRewardsDao.findByStuidsAndLevel(acadyear, semester, rewardslevel, stuIdSet.toArray(new String[0]), pageable);
			int count = stuDevelopRewardsDao.findByStuidsAndLevel(acadyear, semester, rewardslevel, stuIdSet.toArray(new String[0])).size();
			page.setMaxRowCount(count);
			return stuDevelopRewardsList;
		}else if(!StringUtils.isBlank(studentId) && !StringUtils.isBlank(rewardslevel)){
			//学生加级别
			List<StuDevelopRewards> stuDevelopRewardsList = stuDevelopRewardsDao.findByStuidAndLevel(acadyear, semester, studentId, rewardslevel, pageable);
			int count = stuDevelopRewardsDao.findByStuidAndLevel(acadyear, semester, studentId, rewardslevel).size();
			page.setMaxRowCount(count);
			return stuDevelopRewardsList;
		}else{
			return new ArrayList<StuDevelopRewards>();
		}
	}

	@Override
	public StuDevelopRewards findById(String id) {
		return stuDevelopRewardsDao.findById(id).orElse(null);
	}

	@Override
	public void deleteById(String id) {
		stuDevelopRewardsDao.deleteById(id);	
	}

	@Override
	public List<StuDevelopRewards> findByAcaAndSemAndStuId(String acadyear,
			String semester, String studentId) {
		return stuDevelopRewardsDao.findByAcaAndSemAndStuId(acadyear, semester, studentId);
	}

	@Override
	public String doImport(String unitId, List<String[]> datas,
			String acadyear, String semester) {
		Json importResultJson=new Json();
		List<String[]> errorDataList=new ArrayList<String[]>();
		int successCount  =0;
		String[] errorData=null;
		Set<String> stuCodeSet = new HashSet<String>();
		List<StuDevelopRewards> insertList=new ArrayList<StuDevelopRewards>();
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
		List<McodeDetail> McodeDetailList = SUtils.dt(mcodeRemoteService.findByMcodeIds(new String[]{"DM-JLJB","DM-XSHJLB"}), new TR<List<McodeDetail>>() {});
		Map<String, String> rewardslevelMap = new HashMap<String, String>();
		Map<String, String> rewardstypeMap = new HashMap<String, String>();
		for(McodeDetail mcode : McodeDetailList){
			if("DM-JLJB".equals(mcode.getMcodeId())){
				rewardslevelMap.put(mcode.getMcodeContent(), mcode.getThisId());
			}else{
				rewardstypeMap.put(mcode.getMcodeContent(), mcode.getThisId());
			}
		}
		for(String[] arr : datas){
			StuDevelopRewards item = new StuDevelopRewards();
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
				errorData[1]="奖励名称";
				errorData[2]="";
				errorData[3]="奖励名称不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isNotBlank(arr[5])){
        		if(null == rewardslevelMap.get(arr[5])){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="奖励级别";
    				errorData[2]=arr[2];
    				errorData[3]="不存在对应的奖励级别";
    				errorDataList.add(errorData);
    				continue;
        		}
			}else{
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="奖励级别";
				errorData[2]="";
				errorData[3]="奖励级别不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isNotBlank(arr[6])){
        		if(null == rewardstypeMap.get(arr[6])){
        			errorData = new String[4];
    				errorData[0]=errorDataList.size()+1+"";
    				errorData[1]="奖励类别";
    				errorData[2]=arr[2];
    				errorData[3]="不存在对应的奖励类别";
    				errorDataList.add(errorData);
    				continue;
        		}
			}else{
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="奖励类别";
				errorData[2]="";
				errorData[3]="奖励类别不能为空";
				errorDataList.add(errorData);
				continue;
			}
			Date rewardsdate = null;
			if(StringUtils.isNotBlank(arr[8])){
				try {
					rewardsdate = DateUtils.parseDate(arr[8].trim(), "yyyyMMdd");
				} catch (ParseException e1) {
					e1.printStackTrace();
					errorData = new String[4];
					errorData[0]=errorDataList.size()+1+"";
					errorData[1]="获奖日期";
					errorData[2]=arr[0];
					errorData[3]="获奖日期格式不对";
					errorDataList.add(errorData);
					continue;
				}
			}else{
				errorData = new String[4];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="获奖日期";
				errorData[2]="";
				errorData[3]="获奖日期不能为空";
				errorDataList.add(errorData);
				continue;
			}
			if(StringUtils.isBlank(arr[10])){
				errorData = new String[10];
				errorData[0]=errorDataList.size()+1+"";
				errorData[1]="颁奖单位";
				errorData[2]="";
				errorData[3]="颁奖单位不能为空";
				errorDataList.add(errorData);
				continue;
			}
			item.setStuid(stuCodeIdMap.get(arr[1]));
			item.setSchid(unitId);
			item.setRewardsname(arr[4]);
			item.setRewardslevel(rewardslevelMap.get(arr[5]));
			item.setRewardstype(rewardstypeMap.get(arr[6]));
			item.setFilecode(arr[7]);
			item.setRewardsdate(rewardsdate);
			if (StringUtils.isNotEmpty(arr[9])) {
				item.setRewardPosition(NumberUtils.toInt(arr[9]));
			}
			item.setRewardsunit(arr[10]);
			item.setRewardsreason(arr[11]);
			if (StringUtils.isNotEmpty(arr[12])) {
				item.setMoney(NumberUtils.toDouble(arr[12]));
			}
			item.setRemark(arr[13]);
			item.setAcadyear(acadyear);
			item.setSemester(semester);
			item.setId(UuidUtils.generateUuid());
			insertList.add(item);
        	successCount++;
		}		
		if (CollectionUtils.isNotEmpty(insertList)) {
		    if ( insertList.size() > 1000 ) {
		        int loopNumber = insertList.size()/1000;
		        for (int i=0; i<loopNumber; i++ ) {
		            List<StuDevelopRewards> list1 = insertList.subList(i * 1000, (i+1)*1000);
		            if ( list1 != null ) {
		            	StuDevelopRewards[] inserts = list1.toArray(new StuDevelopRewards[0]);
		            	saveAll(inserts);
		            }
		            if ( i+1 == loopNumber &&  insertList.size() -(1000 * loopNumber) > 0) {
		                List<StuDevelopRewards> list2 = insertList.subList((i+1) * 1000, insertList.size());
		                if ( list2 != null ) {
		                	StuDevelopRewards[] inserts = list2.toArray(new StuDevelopRewards[0]);
		                	saveAll(inserts);
		                }
		           }
		       }
		     } else {
		    	  StuDevelopRewards[] inserts = insertList.toArray(new StuDevelopRewards[0]);
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
