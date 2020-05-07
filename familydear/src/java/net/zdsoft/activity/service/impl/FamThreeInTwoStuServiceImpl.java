package net.zdsoft.activity.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.activity.dao.FamThreeInTwoStuDao;
import net.zdsoft.activity.entity.FamDearThreeInTwoStu;
import net.zdsoft.activity.entity.FamDearThreeInTwoStuMember;
import net.zdsoft.activity.service.FamThreeInTwoStuMemberService;
import net.zdsoft.activity.service.FamThreeInTwoStuService;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.remote.utils.BusinessUtils;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.familydear.util.FamilyDearImportUtil;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

@Service("FamThreeInTwoStuService")
public class FamThreeInTwoStuServiceImpl extends BaseServiceImpl<FamDearThreeInTwoStu,String> implements FamThreeInTwoStuService {

	@Autowired
	private FamThreeInTwoStuDao famThreeInTwoStuDao;
	@Autowired
	private FamThreeInTwoStuMemberService famThreeInTwoStuMemberService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	
	@Override
	public List<FamDearThreeInTwoStu> getDearStuByIdentityCard(
			String identitycard, String id) {
		return famThreeInTwoStuDao.getDearStuByIdentityCard(identitycard, id);
	}

	@Override
	public List<FamDearThreeInTwoStu> getDearStuByUnitIdAndTeacherId(
			String unitId, String teacherId) {
		return famThreeInTwoStuDao.getDearStuByUnitIdAndTeacherId(unitId, teacherId);
	}

	@Override
	public List<FamDearThreeInTwoStu> getFamDearThreeInTwoStuByStuName(
			String unitId, String stuName) {
		return famThreeInTwoStuDao.getFamDearThreeInTwoStuByStuName(unitId, stuName);
	}

	@Override
	public List<FamDearThreeInTwoStu> getDearStuByUnitIdAndOthers(
			String unitId,String userId, String stuName, String ganbName, String stuPhone,
			boolean isAdmin, Pagination page) {
		List<Teacher> teacherList = new ArrayList<>();
		Set<String> teacherIdSet = new HashSet<>();
		if(isAdmin){
			if(StringUtils.isNotBlank(ganbName)){
				try {
					teacherList = SUtils.dt(teacherRemoteService.findByTeacherNameLike(java.net.URLDecoder.decode(ganbName, "UTF-8")), Teacher.class);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (CollectionUtils.isNotEmpty(teacherList)) {
					Iterator<Teacher> iterator = teacherList.iterator();
					while (iterator.hasNext()) {
						Teacher tea = iterator.next();
						if (!unitId.equals(tea.getUnitId())) {
							iterator.remove();
						}
					}
				}

				if (CollectionUtils.isEmpty(teacherList)) {
					return new ArrayList<>();
				}
				
				if (CollectionUtils.isNotEmpty(teacherList)) {
					teacherIdSet = teacherList.stream().map(Teacher::getId).collect(Collectors.toSet());
				}
			}
		}else{
			User user=userRemoteService.findOneObjectById(userId);
			if(StringUtils.isNotBlank(ganbName)){
				try {
					teacherList = SUtils.dt(teacherRemoteService.findByTeacherNameLike(java.net.URLDecoder.decode(ganbName, "UTF-8")), Teacher.class);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (CollectionUtils.isNotEmpty(teacherList)) {
					Iterator<Teacher> iterator = teacherList.iterator();
					while (iterator.hasNext()) {
						Teacher tea = iterator.next();
						if (!user.getOwnerId().equals(tea.getId())) {
							iterator.remove();
						}
					}
				}
				if (CollectionUtils.isEmpty(teacherList)) {
					return new ArrayList<>();
				}else{
					teacherIdSet.add(user.getOwnerId());
				}
			}else{
				teacherIdSet.add(user.getOwnerId());
			}
		}
		final  Set<String> teacherIds = teacherIdSet;
		Pageable pageable = Pagination.toPageable(page);
		Specification specification = new Specification<FamDearThreeInTwoStu>() {

			@Override
			public Predicate toPredicate(Root<FamDearThreeInTwoStu> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (StringUtils.isNotEmpty(stuName)) {
					try {
						predicates.add(criteriaBuilder.like(root.get("stuname").as(String.class), "%" + java.net.URLDecoder.decode(stuName, "UTF-8") + "%"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				predicates.add(criteriaBuilder.equal(root.get("unitId").as(String.class), unitId));
				predicates.add(criteriaBuilder.equal(root.get("isDelete").as(String.class), "0"));
				if (StringUtils.isNotEmpty(stuPhone)) {
					predicates.add(criteriaBuilder.like(root.get("linkPhone").as(String.class), "%" +stuPhone+ "%"));
				}
				
				if (CollectionUtils.isNotEmpty(teacherIds)) {
					CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("teacherId").as(String.class));
					for (String id : teacherIds) {
						in.value(id);
					}
					predicates.add(in);
				}
				List<Order> orderList = new ArrayList<>();
				orderList.add(criteriaBuilder.desc(root.get("createTime").as(Date.class)));
				orderList.add(criteriaBuilder.asc(root.get("id").as(String.class)));
				criteriaQuery.where(predicates.toArray(new Predicate[0])).orderBy(orderList);
				return criteriaQuery.getRestriction();

			}
		};
		Page<FamDearThreeInTwoStu> findAll =famThreeInTwoStuDao.findAll(specification  ,pageable);
		page.setMaxRowCount((int) findAll.getTotalElements());
		List<FamDearThreeInTwoStu> famDearThreeInTwoStus=findAll.getContent();
		SetThings(famDearThreeInTwoStus);
		return famDearThreeInTwoStus;
	}
	
	private void SetThings(List<FamDearThreeInTwoStu> famDearThreeInTwoStus){
		if(CollectionUtils.isNotEmpty(famDearThreeInTwoStus)){
			Set<String> TeacherIdSet=new HashSet<>();
			for (FamDearThreeInTwoStu fam : famDearThreeInTwoStus) {
				TeacherIdSet.add(fam.getTeacherId());
			}
			Map<String, Map<String, McodeDetail>> mcodeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeIds(new String[]{"DM-XB","DM-MZ"}),
	                new TypeReference<Map<String, Map<String, McodeDetail>>>() {
	                });
	        Map<String, McodeDetail> XBMap = mcodeMap.get("DM-XB");
	        Map<String, McodeDetail> MZMap = mcodeMap.get("DM-MZ");
			List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(TeacherIdSet.toArray(new String[0])), new TR<List<Teacher>>(){});
			Map<String,Teacher> teacherMap=EntityUtils.getMap(teacherList, e->e.getId());
			Set<String> deptSet=new HashSet<>();
			for (Teacher teacher : teacherList) {
				deptSet.add(teacher.getDeptId());
			}
			List<Dept> deptList=SUtils.dt(deptRemoteService.findListByIds(deptSet.toArray(new String[0])), new TR<List<Dept>>(){});
			Map<String,Dept> deptMap=EntityUtils.getMap(deptList, e->e.getId());
			for (FamDearThreeInTwoStu fam : famDearThreeInTwoStus) {
				McodeDetail xb=XBMap.get(fam.getSex());
				if(xb!=null){
					fam.setSex(xb.getMcodeContent());
				}
				McodeDetail mz=MZMap.get(fam.getNation());
				if(mz!=null){
					fam.setNation(mz.getMcodeContent());
				}
				if(StringUtils.isNotBlank(fam.getTeacherId())){
					if(MapUtils.isNotEmpty(teacherMap)&&teacherMap.containsKey(fam.getTeacherId())){
						Teacher teacher=teacherMap.get(fam.getTeacherId());
						fam.setTeacherName(teacher.getTeacherName());
						if(teacher!=null&&MapUtils.isNotEmpty(deptMap)&&deptMap.containsKey(teacher.getDeptId())){
							Dept dept=deptMap.get(teacher.getDeptId());
							fam.setDeptName(dept.getDeptName());
						}
					}
				}
			}
		}
	}

	@Override
	public void saveFamilyMember(FamDearThreeInTwoStu famDearThreeInTwoStu) {
		String id = famDearThreeInTwoStu.getId();
		List<FamDearThreeInTwoStuMember> memberList = famDearThreeInTwoStu.getStuTempList();
		List<FamDearThreeInTwoStuMember> list = new ArrayList<>();
		famThreeInTwoStuDao.save(famDearThreeInTwoStu);
		famThreeInTwoStuMemberService.deleteBystuId(id);
		if (CollectionUtils.isEmpty(memberList)) {
			return;
		}
		
		for (FamDearThreeInTwoStuMember fmDearThreeInTwoStuMember : memberList) {
			if (fmDearThreeInTwoStuMember != null & StringUtils.isNotEmpty(fmDearThreeInTwoStuMember.getName())) {
				fmDearThreeInTwoStuMember.setId(UuidUtils.generateUuid());
				fmDearThreeInTwoStuMember.setStuId(id);
				list.add(fmDearThreeInTwoStuMember);
			}
		}
		famThreeInTwoStuMemberService.saveAll(list.toArray(new FamDearThreeInTwoStuMember[0]));
		
	}

	@Override
	public void saveCadre(String objId, String teacherId) {
		FamDearThreeInTwoStu famDearThreeInTwoStu=this.findOne(objId);
		famDearThreeInTwoStu.setTeacherId(teacherId);
		this.save(famDearThreeInTwoStu);
	}
	
	public String saveImportDate(List<String[]> rowDatas, LoginInfo loginInfo) {

		//错误数据序列号
		int sequence = 0;
		List<String[]> errorDataList=new ArrayList<String[]>();
		if(CollectionUtils.isEmpty(rowDatas)){
			return FamilyDearImportUtil.errorResult("0", "", "", "没有导入数据",
					sequence, 0, 0, errorDataList);
		}

		Map<String, Map<String, String>> map = getMcodeMap();
		Map<String, String> sexMap = map.get("DM-XB");
		Map<String, String> nationMap = map.get("DM-MZ");
		List<String> identityCardList = new ArrayList<>();
		for(int m=0;m<rowDatas.size();m++){
			String[] datas= rowDatas.get(m);
			String identityCardTea = StringUtils.trimToEmpty(datas[1]);
			String identityCardStu = StringUtils.trimToEmpty(datas[4]);
			try {
				identityCardStu = FamilyDearImportUtil.verifyType("学生身份证号",identityCardStu,"String-18",false,null,null);
				identityCardTea = FamilyDearImportUtil.verifyType("干部身份证号",identityCardTea,"String-18",false,null,null);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			String reMessage = BusinessUtils.validateIdentityCard(identityCardStu, false);
			BusinessUtils.validateIdentityCard(identityCardTea, false);
			if (StringUtils.isEmpty(reMessage)) {
				identityCardList.add(identityCardStu);
			}
		}
		List<FamDearThreeInTwoStu> objectList = findListByIn("identityCard", identityCardList.toArray());
		Map<String, FamDearThreeInTwoStu> identityMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(objectList)) {
			for (FamDearThreeInTwoStu famDearThreeInTwoStu : objectList) {
				if(StringUtils.equals(famDearThreeInTwoStu.getIsDelete(), "0")){
					identityMap.put(famDearThreeInTwoStu.getIdentityCard(), famDearThreeInTwoStu);
				}
			}
		}
		List<FamDearThreeInTwoStu> famDearThreeInTwoStus = new ArrayList<>();
		int i=1;
		int totalSize = rowDatas.size();
		int successCount=0;
		List<Teacher> teacherInLists = SUtils.dt(teacherRemoteService.findByUnitId(loginInfo.getUnitId()),new TR<List<Teacher>>() {});
		Map<String, Teacher> teacherInsMap = new LinkedHashMap<String, Teacher>();
		if (CollectionUtils.isNotEmpty(teacherInLists)) {
			for (Teacher teacher : teacherInLists) {
				teacherInsMap.put(teacher.getIdentityCard(),teacher);
			}
		}
		Set<String> cardSet=new HashSet<>();
		for(int m=0;m<rowDatas.size();m++){
			i++;
			int j=sequence+1;
			try {
				FamDearThreeInTwoStu famDearThreeInTwoStu = new FamDearThreeInTwoStu();
				String[] datas= rowDatas.get(m);
				String teaName = StringUtils.trimToEmpty(datas[0]);
				teaName = FamilyDearImportUtil.verifyType("关联干部姓名",teaName,"String-200",true,null,null);
				String identityCardTea = StringUtils.trimToEmpty(datas[1]);
				identityCardTea = FamilyDearImportUtil.verifyType("干部身份证号",identityCardTea,"String-18",false,null,null);
				if (StringUtils.isNotEmpty(identityCardTea)) {
                    String reMessage = BusinessUtils.validateIdentityCard(identityCardTea, false);
                    if (StringUtils.isNotEmpty(reMessage)) {
                        throw new RuntimeException("干部身份证件号不符合身份证规则");
                    }

                }
				
				if(StringUtils.isBlank(identityCardTea)){
					String[] errorData=new String[4];
					sequence++;
	            	errorData[0]=String.valueOf(sequence);
	                errorData[1]="第"+i+"行";
	                errorData[2]="";
	                errorData[3]="干部身份证不能为空";
	                errorDataList.add(errorData);
	                continue;
				}else if (!teacherInsMap.containsKey(identityCardTea)) {
					String[] errorData=new String[4];
					sequence++;
	            	errorData[0]=String.valueOf(sequence);
	                errorData[1]="第"+i+"行";
	                errorData[2]="";
	                errorData[3]="系统不存在该干部身份证";
	                errorDataList.add(errorData);
	                continue;
	            }else{
	            	String teaNameXt=teacherInsMap.get(identityCardTea).getTeacherName();
	            	if(!StringUtils.equals(teaName, teaNameXt)){
	            		String[] errorData=new String[4];
						sequence++;
		            	errorData[0]=String.valueOf(sequence);
		                errorData[1]="第"+i+"行";
		                errorData[2]="";
		                errorData[3]="身份证和干部姓名不匹配";
		                errorDataList.add(errorData);
		                continue;
	            	}else{
	            		famDearThreeInTwoStu.setTeacherId(teacherInsMap.get(identityCardTea).getId());;
	            	}
	            }

				String stuName = StringUtils.trimToEmpty(datas[2]);
				stuName = FamilyDearImportUtil.verifyType("学生姓名",stuName,"String-200",true,null,null);
				if(StringUtils.isBlank(stuName)){
					String[] errorData=new String[4];
					sequence++;
	            	errorData[0]=String.valueOf(sequence);
	                errorData[1]="第"+i+"行";
	                errorData[2]="";
	                errorData[3]="学生姓名不能为空";
	                errorDataList.add(errorData);
	                continue;
				}else{
					famDearThreeInTwoStu.setStuname(stuName);
				}

//
				String sex = StringUtils.trimToEmpty(datas[3]);
				sex = FamilyDearImportUtil.verifyType("性别",sex,"String-200",false,null,null);
                if (StringUtils.isNotEmpty(sex)) {
                    String sexVal = sexMap.get(sex);
                    famDearThreeInTwoStu.setSex(sexVal);
                }else{
                	String[] errorData=new String[4];
					sequence++;
	            	errorData[0]=String.valueOf(sequence);
	                errorData[1]="第"+i+"行";
	                errorData[2]="";
	                errorData[3]="性别不能为空";
	                errorDataList.add(errorData);
	                continue;
                }


				String identityCard = StringUtils.trimToEmpty(datas[4]);
				identityCard = FamilyDearImportUtil.verifyType("学生身份证号",identityCard,"String-18",false,null,null);
                if (StringUtils.isNotEmpty(identityCard)) {
                    String reMessage = BusinessUtils.validateIdentityCard(identityCard, false);
                    if (StringUtils.isNotEmpty(reMessage)) {
                        throw new RuntimeException("学生身份证件号不符合身份证规则");
                    }
                    
                    if(cardSet.contains(identityCard)){
                    	String[] errorData=new String[4];
                        sequence++;
                        errorData[0]= String.valueOf(j);
                        errorData[1]="第"+i+"行";
                        errorData[2]=identityCard;
                        errorData[3]="该导入的文件中存在学生身份证号相同！";
                        errorDataList.add(errorData);
                        continue;
                    }
                    
                    FamDearThreeInTwoStu obj = identityMap.get(identityCard);
                    if (obj != null) {
                        famDearThreeInTwoStu.setId(obj.getId());
                        famDearThreeInTwoStu.setBirthday(obj.getBirthday());
                        famDearThreeInTwoStu.setDormitoryNum(obj.getDormitoryNum());
                        famDearThreeInTwoStu.setEmail(obj.getEmail());
                        famDearThreeInTwoStu.setHeadmasterPhone(obj.getHeadmasterPhone());
                        famDearThreeInTwoStu.setHomeAddress(obj.getHomeAddress());
                        famDearThreeInTwoStu.setIntake(obj.getIntake());
                        famDearThreeInTwoStu.setLeaderPhone(obj.getLeaderPhone());
                        famDearThreeInTwoStu.setPoliticCountenance(obj.getPoliticCountenance());
                        famDearThreeInTwoStu.setResume(obj.getResume());
                        famDearThreeInTwoStu.setSchoolSystem(obj.getSchoolSystem());
                        famDearThreeInTwoStu.setShowContent(obj.getShowContent());
                        famDearThreeInTwoStu.setSituation(obj.getSituation());
                    }
                    famDearThreeInTwoStu.setIdentityCard(identityCard);
                }else{
                	String[] errorData=new String[4];
					sequence++;
	            	errorData[0]=String.valueOf(sequence);
	                errorData[1]="第"+i+"行";
	                errorData[2]=identityCard;
	                errorData[3]="学生身份证号不能为空";
	                errorDataList.add(errorData);
	                continue;
                }

				String nation = StringUtils.trimToEmpty(datas[5]);
				nation = FamilyDearImportUtil.verifyType("民族",nation,"String-200",true,null,null);
				if(StringUtils.isBlank(nation)){
					String[] errorData=new String[4];
					sequence++;
	            	errorData[0]=String.valueOf(sequence);
	                errorData[1]="第"+i+"行";
	                errorData[2]="";
	                errorData[3]="民族不能为空";
	                errorDataList.add(errorData);
	                continue;
				}else{
					String nationVal = nationMap.get(nation);
					famDearThreeInTwoStu.setNation(nationVal);
				}
				
				String school = StringUtils.trimToEmpty(datas[6]);
				school = FamilyDearImportUtil.verifyType("所在学校",school,"String-50",true,null,null);
				if(StringUtils.isNotBlank(school)){
					famDearThreeInTwoStu.setSchool(school);
				}else{
					String[] errorData=new String[4];
					sequence++;
	            	errorData[0]=String.valueOf(sequence);
	                errorData[1]="第"+i+"行";
	                errorData[2]="";
	                errorData[3]="所在学校不能为空";
	                errorDataList.add(errorData);
	                continue;
				}
				
				String sdept = StringUtils.trimToEmpty(datas[7]);
				sdept = FamilyDearImportUtil.verifyType("所在系部",sdept,"String-50",true,null,null);
				if(StringUtils.isNotBlank(sdept)){
					famDearThreeInTwoStu.setSdept(sdept);
				}else{
					String[] errorData=new String[4];
					sequence++;
	            	errorData[0]=String.valueOf(sequence);
	                errorData[1]="第"+i+"行";
	                errorData[2]="";
	                errorData[3]="所在系部不能为空";
	                errorDataList.add(errorData);
	                continue;
				}
				
				String major = StringUtils.trimToEmpty(datas[8]);
				major = FamilyDearImportUtil.verifyType("所学专业",major,"String-50",true,null,null);
				if(StringUtils.isNotBlank(major)){
					famDearThreeInTwoStu.setMajor(major);
				}else{
					String[] errorData=new String[4];
					sequence++;
	            	errorData[0]=String.valueOf(sequence);
	                errorData[1]="第"+i+"行";
	                errorData[2]="";
	                errorData[3]="所学专业不能为空";
	                errorDataList.add(errorData);
	                continue;
				}


				String mobilePhone = StringUtils.trimToEmpty(datas[9]);
				mobilePhone = FamilyDearImportUtil.verifyType("学生联系电话",mobilePhone,"Integer-30",false,null,null);
                if (StringUtils.isNotEmpty(mobilePhone)) {
                    if (Validators.isNumber(mobilePhone.trim())) {
                    	famDearThreeInTwoStu.setLinkPhone(mobilePhone);
                    }else{
//					throw new RuntimeException("手机号码不符合规则！");
                        String[] errorData=new String[4];
                        sequence++;
                        errorData[0]= String.valueOf(j);
                        errorData[1]="第"+i+"行";
                        errorData[2]=mobilePhone;
                        errorData[3]="学生联系电话不符合规则！";
                        errorDataList.add(errorData);
                        continue;
                    }
                }else{
                	String[] errorData=new String[4];
					sequence++;
	            	errorData[0]=String.valueOf(sequence);
	                errorData[1]="第"+i+"行";
	                errorData[2]="";
	                errorData[3]="学生联系电话不能为空";
	                errorDataList.add(errorData);
	                continue;
                }
				
				successCount++;
				famDearThreeInTwoStus.add(famDearThreeInTwoStu);
			}catch (RuntimeException re){
				re.printStackTrace();
				FamilyDearImportUtil.addError(j+"", "第"+i+"行", "", re.getMessage(), sequence, errorDataList);
				continue;
			} catch(Exception e) {
				e.printStackTrace();
				FamilyDearImportUtil.addError(j+"", "第"+i+"行", "", "数据整理出错。", sequence, errorDataList);
				continue;
			}
		}

		if (CollectionUtils.isNotEmpty(famDearThreeInTwoStus)) {
			for (FamDearThreeInTwoStu famDearThreeInTwoStu : famDearThreeInTwoStus) {
				famDearThreeInTwoStu.setUnitId(loginInfo.getUnitId());
				famDearThreeInTwoStu.setId(StringUtils.isNotBlank(famDearThreeInTwoStu.getId())?famDearThreeInTwoStu.getId():UuidUtils.generateUuid());
				famDearThreeInTwoStu.setCreateTime(new Date());
				famDearThreeInTwoStu.setCreateUserId(loginInfo.getUserId());
				famDearThreeInTwoStu.setIsDelete("0");
			}
			saveAll(famDearThreeInTwoStus.toArray(new FamDearThreeInTwoStu[0]));
		}
		return result(totalSize,successCount,totalSize-successCount,errorDataList);
	}
	
	private String  result(int totalCount ,int successCount , int errorCount ,List<String[]> errorDataList){
		Json importResultJson=new Json();
		importResultJson.put("totalCount", totalCount);
		importResultJson.put("successCount", successCount);
		importResultJson.put("errorCount", errorCount);
		importResultJson.put("errorData", errorDataList);
		return importResultJson.toJSONString();
	}
	
	private Map<String, Map<String, String>> getMcodeMap() {
		String[] mcodes = {"DM-XB","DM-MZ"};
		List<McodeDetail> mds = SUtils.dt(mcodeRemoteService.findByMcodeIds(mcodes), new TR<List<McodeDetail>>() {} );
		Map<String, Map<String, String>> map = new HashMap<>();

		for (McodeDetail detail : mds) {
			Map<String, String> mcodeContentMap = map.get(detail.getMcodeId());
			if (mcodeContentMap == null) {
				mcodeContentMap = new HashMap<>();
				map.put(detail.getMcodeId(), mcodeContentMap);
			}
			mcodeContentMap.put(detail.getMcodeContent(), detail.getThisId());
		}

		return map;
	}

	@Override
	protected BaseJpaRepositoryDao<FamDearThreeInTwoStu, String> getJpaDao() {
		return famThreeInTwoStuDao;
	}

	@Override
	protected Class<FamDearThreeInTwoStu> getEntityClass() {
		return FamDearThreeInTwoStu.class;
	}

}
