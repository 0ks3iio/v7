package net.zdsoft.scoremanage.data.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dao.ExamInfoDao;
import net.zdsoft.scoremanage.data.dto.ExamInfoSearchDto;
import net.zdsoft.scoremanage.data.entity.ExamInfo;
import net.zdsoft.scoremanage.data.entity.JoinexamschInfo;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.JoinexamschInfoService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;

@Service("examInfoService")
public class ExamInfoServiceImpl extends BaseServiceImpl<ExamInfo, String> implements ExamInfoService{

	@Autowired
	private ExamInfoDao examInfoDao;
	@Autowired
	private JoinexamschInfoService joinexamschInfoService;
	@Autowired
	private UnitRemoteService unitService;
	@Autowired
	private GradeRemoteService gradeService;
	
	@Override
	public List<ExamInfo> findByUnitId(String unitId, Pagination page) {
		Integer count = countByUnitId(unitId);
		page.setMaxRowCount(count);
		return examInfoDao.findByUnitId(unitId, Pagination.toPageable(page));
	}
	@Override
	public String findJsonExamList(String unitId) {
		JSONObject json = new JSONObject();
		List<ExamInfo> list = examInfoDao.findByUnitId(unitId);
		List<JSONObject> infolist = new ArrayList<JSONObject>();
		for (ExamInfo e : list) {
			JSONObject info = new JSONObject();
			info.put("id", e.getId());
			info.put("examCode", e.getExamCode());
			String name =e.getExamName();
			info.put("name", name);
			infolist.add(info);
		}
		json.put("infolist", infolist);
		return json.toJSONString();
	}
	@Autowired
	public McodeRemoteService mcodeRemoteService;
	@Override
	public String findJsonGradeList(String unitId, String examId) {
		JSONObject json = new JSONObject();
		ExamInfo exam = findExamInfoOne(examId);
		String ranges = exam.getRanges();
		String[] grades = ranges.split(",");
		Arrays.sort(grades);
		List<JSONObject> infolist = new ArrayList<JSONObject>();		
		for (String gradeCode : grades) {
			String section = gradeCode.trim().substring(0, 1);
			String g = gradeCode.trim().substring(1, 2);
			Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-"+section}), new TR<Map<String, Map<String,McodeDetail>>>(){});
			String gName = findMapMapByMcodeIds.get("DM-RKXD-"+section).get(g).getMcodeContent();
			JSONObject info = new JSONObject();
			info.put("id", gradeCode);
			info.put("name", gName);
			infolist.add(info);
		}
		json.put("infolist", infolist);
		return json.toJSONString();
	}
	
	private Integer countByUnitId(String unitId) {
		Integer count = examInfoDao.countByUnitId(unitId);
		return count == null ? 0 : count;
	}

	@Override
	protected BaseJpaRepositoryDao<ExamInfo, String> getJpaDao() {
		return examInfoDao;
	}

	@Override
	protected Class<ExamInfo> getEntityClass() {
		return ExamInfo.class;
	}

	@Override
	public List<ExamInfo> findExamInfoList(final String unitId, final ExamInfoSearchDto searchDto, Pagination page) {
		List<ExamInfo> findExamInfoList=new ArrayList<ExamInfo>();
		Integer count = 0;
		if(page!=null){
			count = countExamInfoList(unitId,searchDto);
		}
		/**
		 * <option value="1">本单位设定的考试</option>
			<option value="2">直属教育局设定的考试</option>
			<option value="3">参与的校校联考</option>
		 */
		if("1".equals(searchDto.getSearchType())){
			Specification<ExamInfo> specification = findExamListSpecification(searchDto.getSearchType(),unitId, searchDto);
			if(page!=null){
				Pageable pageable = Pagination.toPageable(page);
				Page<ExamInfo> findAll = examInfoDao.findAll(specification, pageable);
				page.setMaxRowCount((int)findAll.getTotalElements());
				findExamInfoList = findAll.getContent();
			}else{
				findExamInfoList = examInfoDao.findAll(specification);
			}
		}else if("2".equals(searchDto.getSearchType())){
			Specification<ExamInfo> specification = findExamListSpecification(searchDto.getSearchType(),unitId, searchDto);
	        if(page!=null){
				Pageable pageable = Pagination.toPageable(page);
				Page<ExamInfo> findAll = examInfoDao.findAll(specification, pageable);
				page.setMaxRowCount((int)findAll.getTotalElements());
				findExamInfoList = findAll.getContent();
			}else{
				findExamInfoList = examInfoDao.findAll(specification);
			}
		}else if("3".equals(searchDto.getSearchType())){
			if(page!=null){
				page.setMaxRowCount(count);
				//增加是否新高类型查询
				if(StringUtils.isNotBlank(searchDto.getIsgkExamType())){
					findExamInfoList = examInfoDao.findExamInfoJoinList(unitId,searchDto.getSearchAcadyear(),searchDto.getSearchSemester(),searchDto.getIsgkExamType(),Pagination.toPageable(page));
				}else{
					findExamInfoList = examInfoDao.findExamInfoJoinList(unitId,searchDto.getSearchAcadyear(),searchDto.getSearchSemester(),Pagination.toPageable(page));
				}
			}else{
				if(StringUtils.isNotBlank(searchDto.getIsgkExamType())){
					findExamInfoList = examInfoDao.findExamInfoJoinList(unitId,searchDto.getSearchAcadyear(),searchDto.getSearchSemester(),searchDto.getIsgkExamType());
				}else{
					findExamInfoList = examInfoDao.findExamInfoJoinList(unitId,searchDto.getSearchAcadyear(),searchDto.getSearchSemester());
				}
				
			}
		}else{
			//所有
			if(page==null){
				Specification<ExamInfo> specification = findExamListSpecification("1",unitId, searchDto);
				List<ExamInfo> findExamInfoList1 = examInfoDao.findAll(specification);
				Set<String> examIds=new HashSet<String>();
				if(CollectionUtils.isNotEmpty(findExamInfoList1)){
					findExamInfoList.addAll(findExamInfoList1);
					for(ExamInfo exam:findExamInfoList1){
						examIds.add(exam.getId());
					}
				}
				//Unit findOne = SUtils.dc(unitService.findById(unitId), Unit.class);
				//String parentUnitId = findOne.getParentId();
				//specification = findExamListSpecification("2",parentUnitId, searchDto);
				specification = findExamListSpecification("2",unitId, searchDto);
				List<ExamInfo> findExamInfoList2 = examInfoDao.findAll(specification);
				if(CollectionUtils.isNotEmpty(findExamInfoList2)){
					findExamInfoList.addAll(findExamInfoList2);
				}
				List<ExamInfo> findExamInfoList3 =null;
				if(StringUtils.isNotBlank(searchDto.getIsgkExamType())){
					findExamInfoList3 = examInfoDao.findExamInfoJoinList(unitId,searchDto.getSearchAcadyear(),searchDto.getSearchSemester(),searchDto.getIsgkExamType());
				}else{
					findExamInfoList3 = examInfoDao.findExamInfoJoinList(unitId,searchDto.getSearchAcadyear(),searchDto.getSearchSemester());
					
				}
				
				if(CollectionUtils.isNotEmpty(findExamInfoList3)){
					for(ExamInfo exam:findExamInfoList3){
						if(examIds.contains(exam.getId())){
							continue;
						}
						findExamInfoList.add(exam);
					}
				}
			}
		}
		if(CollectionUtils.isNotEmpty(findExamInfoList)){
			for (ExamInfo item : findExamInfoList) {
				String string = ScoreDataConstants.allTklx.get(item.getExamUeType());
				if(StringUtils.isBlank(string)){
					item.setExamUeTypeName("");
				}else{
					item.setExamUeTypeName(string);
				}
			}
		}
		findOtherMsg(findExamInfoList);
		return findExamInfoList;
	}

	private Specification<ExamInfo> findExamListSpecification(String searchType,final String unitId, final ExamInfoSearchDto searchDto) {
		Specification<ExamInfo> specification = null;
		if("1".equals(searchType)){
			specification = new Specification<ExamInfo>() {
			    @Override
			    public Predicate toPredicate(Root<ExamInfo> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
			        List<Predicate> ps = Lists.newArrayList();

			        ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
			        ps.add(cb.equal(root.get("acadyear").as(String.class), searchDto.getSearchAcadyear()));
			        ps.add(cb.equal(root.get("semester").as(String.class), searchDto.getSearchSemester()));
			        
			        ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
			        //增加一个是否新高类型
			        if(StringUtils.isNotBlank(searchDto.getIsgkExamType())){
			        	ps.add(cb.equal(root.get("isgkExamType").as(String.class), searchDto.getIsgkExamType()));
			        }
			      
			        List<Order> orderList = new ArrayList<Order>();
			        orderList.add(cb.desc(root.get("examCode").as(String.class)));

			        cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
			        return cq.getRestriction();
			    }
			};
		}else if("2".equals(searchType)){
			Unit findOne = SUtils.dc(unitService.findOneById(unitId), Unit.class);
			final String parentUnitId = findOne.getParentId();
			specification = new Specification<ExamInfo>() {
	            @Override
	            public Predicate toPredicate(Root<ExamInfo> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
	                List<Predicate> ps = Lists.newArrayList();
	                ps.add(cb.equal(root.get("unitId").as(String.class), parentUnitId));
	                ps.add(cb.equal(root.get("acadyear").as(String.class), searchDto.getSearchAcadyear()));
	                ps.add(cb.equal(root.get("semester").as(String.class), searchDto.getSearchSemester()));
	                ps.add(cb.notEqual(root.get("examUeType").as(String.class), ScoreDataConstants.TKLX_3));
	                
	                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
	                //增加一个是否新高类型
			        if(StringUtils.isNotBlank(searchDto.getIsgkExamType())){
			        	ps.add(cb.equal(root.get("isgkExamType").as(String.class), searchDto.getIsgkExamType()));
			        }
	                List<Order> orderList = new ArrayList<Order>();
	                orderList.add(cb.desc(root.get("examCode").as(String.class)));

	                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
	                return cq.getRestriction();
	            }
	        };
		}else if("3".equals(searchType)){
			
		}
		return specification;
	}
	
	private void findOtherMsg(List<ExamInfo> findExamInfoList) {
		if(CollectionUtils.isNotEmpty(findExamInfoList)){
			for (ExamInfo item : findExamInfoList) {
				String examNameNew  = item.getExamName();
				if("1".equals(item.getIsgkExamType())){
					examNameNew += "<新高考>";
				}
				if(ScoreDataConstants.TKLX_3.equals(item.getExamUeType())){
					examNameNew += "（校校）";
				}
				item.setExamNameOther(examNameNew);
			}
		}
	}

	private Integer countExamInfoList(String unitId, ExamInfoSearchDto searchDto) {
		Integer count = null;
		if("1".equals(searchDto.getSearchType())){
//			count = examInfoDao.countExamInfoList(unitId,searchDto.getSearchAcadyear(),searchDto.getSearchSemester());
		}else if("2".equals(searchDto.getSearchType())){
//			Unit findOne = SUtils.dc(unitService.findById(unitId), Unit.class);
//			unitId = findOne.getParentId();
//			count = examInfoDao.countExamInfoListNot(unitId,searchDto.getSearchAcadyear(),searchDto.getSearchSemester());
		}else if("3".equals(searchDto.getSearchType())){
			if(StringUtils.isNotBlank(searchDto.getIsgkExamType())){
				count = examInfoDao.countExamInfoJoinList(unitId,searchDto.getSearchAcadyear(),searchDto.getSearchSemester(),searchDto.getIsgkExamType());
			}else{
				count = examInfoDao.countExamInfoJoinList(unitId,searchDto.getSearchAcadyear(),searchDto.getSearchSemester());
			}
			
		}
		return count == null ? 0 : count;
	}

	@Override
	public List<String> findExamCodeMax() {
		return examInfoDao.findExamCodeMax();
	}

	@Override
	public void deleteAllIsDeleted(String... ids) {
		examInfoDao.updateIsDelete(ids);
	}

	@Override
	public void saveExamInfoOne(ExamInfo examInfo, List<JoinexamschInfo> joinexamschInfoAddList) {
		joinexamschInfoService.deleteByExamInfoId(examInfo.getId());
		joinexamschInfoService.saveAllEntitys(joinexamschInfoAddList.toArray(new JoinexamschInfo[0]));
		this.saveAllEntitys(examInfo);
	}

	@Override
	public ExamInfo findExamInfoOne(String id) {
		ExamInfo examInfo = this.findOne(id);
		List<JoinexamschInfo> joinexamschInfoList = joinexamschInfoService.findByExamInfoId(id);
		Map<String,String> map = new HashMap<String,String>();
		Set<String> schoolIds=new HashSet<String>();
		for (JoinexamschInfo item : joinexamschInfoList) {
			schoolIds.add(item.getSchoolId());
		}
		List<Unit> units = SUtils.dt(unitService.findListByIds(schoolIds.toArray(new String[0])),  new TR<List<Unit>>(){});
		Map<String, Unit> unitMap = EntityUtils.getMap(units, "id");
		for (JoinexamschInfo item : joinexamschInfoList) {
			Unit unit = unitMap.get(item.getSchoolId());
			if(unit!=null){
				map.put(item.getSchoolId(), unit.getUnitName());
			}else{
				map.put(item.getSchoolId(), "未找到");
			}
		}
		examInfo.setLkxzSelectMap(map);
		return examInfo;
	}

	@Override
	public List<ExamInfo> findNotDeletedByIdIn(String... ids) {
		List<ExamInfo> findNotDeletedByIdIn = examInfoDao.findNotDeletedByIdIn(ids);
		findOtherMsg(findNotDeletedByIdIn);
		return findNotDeletedByIdIn;
	}

	@Override
	public List<ExamInfo> findExamInfoListRange(String unitId, String acadyear, String semester) {
		List<ExamInfo> findExamInfoListRange = examInfoDao.findExamInfoListRange(unitId,acadyear,semester);
		findOtherMsg(findExamInfoListRange);
		return findExamInfoListRange;
	}
	@Override
	public List<ExamInfo> findExamInfoList(String unitId, String acadyear, String semester) {
		List<ExamInfo> findExamInfoList = examInfoDao.findExamInfoList(unitId,acadyear,semester);
		findOtherMsg(findExamInfoList);
		return findExamInfoList;
	}

	@Override
	public List<ExamInfo> findExamInfoListRange(String unitId, String acadyear, String semester, String searchType) {
		List<ExamInfo> findExamInfoList=new ArrayList<ExamInfo>();
		if("2".equals(searchType)){
			Unit findOne = SUtils.dc(unitService.findOneById(unitId), Unit.class);
			unitId = findOne.getParentId();
			findExamInfoList = examInfoDao.findExamInfoListNotRange(unitId,acadyear,semester);
		}else if("3".equals(searchType)){
			findExamInfoList = examInfoDao.findExamInfoJoinListRange(unitId,acadyear,semester);
		}
		findOtherMsg(findExamInfoList);
		return findExamInfoList;
	}

	@Override
	public List<ExamInfo> saveAllEntitys(ExamInfo... examInfo) {
		return examInfoDao.saveAll(checkSave(examInfo));
	}
	@Override
	public List<ExamInfo> findExamInfoListAll(final String unitId, String acadyear, String semester, final String gradeId) {
		List<ExamInfo> findExamInfoList=new ArrayList<ExamInfo>();
		/**
		 * <option value="1">本单位设定的考试</option>
			<option value="2">直属教育局设定的考试</option>
			<option value="3">参与的校校联考</option>
		 */
		Grade grade = RedisUtils.getObject("scoremanage_grade_id_"+gradeId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<Grade>(){}, new RedisInterface<Grade>(){
			@Override
			public Grade queryData() {
				return SUtils.dt(gradeService.findOneById(gradeId), new TypeReference<Grade>(){});
			}
		});
		int examAcadyear = NumberUtils.toInt(StringUtils.substringBefore(acadyear, "-"));
		int openAcadyear = NumberUtils.toInt(StringUtils.substringBefore(grade.getOpenAcadyear(), "-"));
		int difCode = examAcadyear-openAcadyear+1;
		if( difCode < 1 || difCode > grade.getSchoolingLength()){
			return findExamInfoList;
		}
		String gradeCode = grade.getSection()+""+difCode;
		Unit findOne = RedisUtils.getObject("scoremanage_unit_id_"+unitId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<Unit>(){}, new RedisInterface<Unit>(){
			@Override
			public Unit queryData() {
				return SUtils.dc(unitService.findOneById(unitId), Unit.class);
			}
		});
		String parentUnitId = findOne.getParentId();
		Set<String> unitIds = new HashSet<String>();
		unitIds.add(unitId);
		unitIds.add(parentUnitId);
		//本单位设定的考试、直属教育局设定的考试
		List<ExamInfo> list2 = examInfoDao.findExamInfoListAll(unitIds.toArray(new String[0]),acadyear,semester,gradeCode);
		findExamInfoList.addAll(list2);
		//参与的校校联考
		list2 = examInfoDao.findExamInfoJoinListAll(unitId,acadyear,semester);
		if(CollectionUtils.isNotEmpty(list2)){
			Set<String> examIds = EntityUtils.getSet(list2, "id");
			list2 = examInfoDao.findExamInfoListAll(examIds.toArray(new String[0]),gradeCode);
			findExamInfoList.addAll(list2);
		}
		findOtherMsg(findExamInfoList);
		return findExamInfoList;
	}
	
	@Override
	public List<ExamInfo> findExamInfoByGradeId(String unitId,String gradeId,String examType){
		Set<String> acadyears=new HashSet<String>();
		Grade grade=SUtils.dc(gradeService.findOneById(gradeId),Grade.class);
		if(grade==null){
			return new ArrayList<ExamInfo>();
		}
		if(StringUtils.isNotBlank(grade.getOpenAcadyear())){
			int yearOne=Integer.parseInt(grade.getOpenAcadyear().split("-")[0]);
			int yearTwo=yearOne+1;
			for(int i=0;i<grade.getSchoolingLength();i++){
				yearOne+=1;
				yearTwo+=1;
				acadyears.add(yearOne+"-"+yearTwo);
			}
		} 
		return examInfoDao.findExamInfoByAcadyears(unitId, examType, acadyears.toArray(new String[0]));
	}

	@Override
	public List<ExamInfo> findExamInfoByDayTBefore(final String unitId) {
		/*Calendar theCa = Calendar.getInstance();
		theCa.setTime(new Date());
		theCa.add(theCa.DATE, -30);
		Date date = theCa.getTime();
		SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
		String str = sdf.format(date);
		return examInfoDao.findExamInfoByDayTBefore(unitId, date);*/
		
		Specification<ExamInfo> specification = new Specification<ExamInfo>(){
			@Override
			public Predicate toPredicate(Root<ExamInfo> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Calendar theCa = Calendar.getInstance();
				theCa.setTime(new Date());
				theCa.add(theCa.DATE, -30);
				Date date = theCa.getTime();
				List<Predicate> ps = new ArrayList<Predicate>();
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				ps.add(cb.greaterThan(
						root.get("examStartDate").as(Date.class),
						date));
				cq.where(ps.toArray(new Predicate[ps.size()]));
				return cq.getRestriction();
			}			
		};
		List<ExamInfo> examInfoList = findAll(specification);
		return examInfoList;
	}

	@Override
	public List<ExamInfo> findExamInfoByDayTMoreBefore(final String unitId,
			final String acadyear, final String semester, final String examType) {
		Specification<ExamInfo> specification = new Specification<ExamInfo>(){
			@Override
			public Predicate toPredicate(Root<ExamInfo> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Calendar theCa = Calendar.getInstance();
				theCa.setTime(new Date());
				theCa.add(theCa.DATE, -30);
				Date date = theCa.getTime();
				List<Predicate> ps = new ArrayList<Predicate>();
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
				ps.add(cb.equal(root.get("semester").as(String.class), semester));
				ps.add(cb.lessThanOrEqualTo(
						root.get("examStartDate").as(Date.class),
						date));
				if (!Validators.isEmpty(examType)) {
                    ps.add(cb.like(root.get("examType").as(String.class), examType));
                }
				cq.where(ps.toArray(new Predicate[ps.size()]));
				return cq.getRestriction();
			}			
		};
		List<ExamInfo> examInfoList = findAll(specification);
		return examInfoList;
	}
	
	
}
