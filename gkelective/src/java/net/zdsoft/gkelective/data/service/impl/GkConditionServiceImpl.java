package net.zdsoft.gkelective.data.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dao.GkConditionDao;
import net.zdsoft.gkelective.data.dao.GkGroupClassDao;
import net.zdsoft.gkelective.data.dao.GkGroupClassStuDao;
import net.zdsoft.gkelective.data.dao.GkRelationshipDao;
import net.zdsoft.gkelective.data.dao.GkSubjectArrangeDao;
import net.zdsoft.gkelective.data.dao.GkSubjectDao;
import net.zdsoft.gkelective.data.dto.GkConditionDto;
import net.zdsoft.gkelective.data.dto.StudentSubjectDto;
import net.zdsoft.gkelective.data.entity.GkBatch;
import net.zdsoft.gkelective.data.entity.GkCondition;
import net.zdsoft.gkelective.data.entity.GkGroupClass;
import net.zdsoft.gkelective.data.entity.GkGroupClassStu;
import net.zdsoft.gkelective.data.entity.GkRelationship;
import net.zdsoft.gkelective.data.entity.GkResult;
import net.zdsoft.gkelective.data.entity.GkRounds;
import net.zdsoft.gkelective.data.entity.GkSubject;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.service.GkBatchService;
import net.zdsoft.gkelective.data.service.GkConditionService;
import net.zdsoft.gkelective.data.service.GkGroupClassService;
import net.zdsoft.gkelective.data.service.GkResultService;
import net.zdsoft.gkelective.data.service.GkRoundsService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gkConditionService")
public class GkConditionServiceImpl  extends BaseServiceImpl<GkCondition, String> implements GkConditionService{

	@Autowired
	private GkConditionDao gkConditionDao;
	@Autowired
	private CourseRemoteService courseService;
	@Autowired
	private GkRelationshipDao gkRelationshipDao;
	@Autowired
	private GkResultService gkResultService;
	@Autowired
	private GkSubjectArrangeDao gkSubjectArrangeDao;
	@Autowired
	private GkRoundsService gkRoundsService;
	@Autowired
	private GkBatchService gkBatchService;
	@Autowired
	private GkGroupClassDao gkGroupClassDao;
	@Autowired
	private GkGroupClassStuDao gkGroupClassStuDao;
	@Autowired
	private TeachClassRemoteService teachClassService;
	@Autowired
	private TeachClassStuRemoteService teachClassStuService;
	@Autowired
	private GkSubjectDao gkSubjectDao;
	@Autowired
	private GkSubjectArrangeService gkSubjectArrangeService;
	@Autowired
	private GkGroupClassService gkGroupClassService;
	@Override
	protected BaseJpaRepositoryDao<GkCondition, String> getJpaDao() {
		return gkConditionDao;
	}

	@Override
	protected Class<GkCondition> getEntityClass() {
		return GkCondition.class;
	}

	public void deleteAllConditions(String roundsId, String type){
		List<String> cids = null;
		int step=0;
		if (GkElectveConstants.GKCONDITION_GROUP_1.equals(type)) {
			//2017年6月19日组合班下没有批次概念
//			cids = EntityUtils.getList(gkConditionDao.findByRoundsId(roundsId),"id");
//			step = GkElectveConstants.STEP_2;
		} else {
			cids = EntityUtils.getList(gkConditionDao.findByRoundsIdAndType(roundsId, type),"id");
			step = GkElectveConstants.STEP_4;
		}
		if(CollectionUtils.isNotEmpty(cids)){
			gkRelationshipDao.deleteByTypePrimaryId(GkElectveConstants.RELATIONSHIP_TYPE_02, cids.toArray(new String[0]));
			gkConditionDao.deleteByIds(cids.toArray(new String[0]));
		}
		gkBatchService.deleteByRoundsIdAndType(roundsId, type);
		gkRoundsService.updateStep(step, roundsId);
	}
	
	public void deleteByIds(String... ids){
		gkRelationshipDao.deleteByTypePrimaryId(GkElectveConstants.RELATIONSHIP_TYPE_02, ids);
		gkConditionDao.deleteByIds(ids);
	}
	
	public void saveConditions(List<GkConditionDto> dtos, String type){
		if(CollectionUtils.isEmpty(dtos)){
			return;
		}
		Date now = new Date();
		List<GkCondition> ins = new ArrayList<GkCondition>();
		List<GkCondition> ups = new ArrayList<GkCondition>();
		List<GkRelationship> ships = new ArrayList<GkRelationship>();
		List<String> delIds = new ArrayList<String>();
		// <subId, stuNum> 重新单科开班的科目及总人数
		Map<String, Integer> sumMap = new HashMap<String, Integer>();
		List<String> singleSubIds = new ArrayList<String>();
		GkCondition con;
		GkRelationship ship;
		String roundsId = null;
		String groupType = null;
		for(GkConditionDto dto : dtos){
			if(StringUtils.isEmpty(dto.getId())
					&& dto.getInDep() == 0){
				continue;
			}
			con = new GkCondition();
			con = dto.convertTo(con);
			roundsId = con.getRoundsId();
			groupType = con.getType();
			con.setModifyTime(now);
			if(StringUtils.isEmpty(con.getId())){
				con.setCreationTime(now);
				con.setId(UuidUtils.generateUuid());
				ins.add(con);
				String[] subIds = StringUtils.split(dto.getSubjectIdStr(), ",");
				for(String sid : subIds){
					ship = new GkRelationship(con.getId(), 
							GkElectveConstants.RELATIONSHIP_TYPE_02, sid);
					ship.setId(UuidUtils.generateUuid());
					ships.add(ship);
					ship = null;
				}
			} else {
				if(dto.getInDep() != 0){
					ups.add(con);
				} else {
					delIds.add(con.getId());
					String[] subIds = StringUtils.split(dto.getSubjectIdStr(), ",");
					for(String sid : subIds){
						singleSubIds.add(sid);
						addToNumMap(sumMap, sid, con.getSumNum());
					}
				}
			}
		}
		GkRounds rou = gkRoundsService.findRoundById(roundsId);
		if(delIds.size() > 0){
			List<GkConditionDto> dtos2 = new ArrayList<GkConditionDto>();
			List<GkCondition> gcs = gkConditionDao.findByRoundsIdAndType(roundsId, GkElectveConstants.GKCONDITION_SINGLE_0);
			if (CollectionUtils.isNotEmpty(gcs)) {
				Map<String, GkConditionDto> gkcMap = new HashMap<String, GkConditionDto>();
				for (GkCondition gc : gcs) {
					GkConditionDto exDto = new GkConditionDto();
					exDto.convertFrom(gc);
					gkcMap.put(gc.getId(), exDto);
				}
				gcs.clear();
				// 获取开班条件对应课程
				List<GkRelationship> singles = gkRelationshipDao
						.findByTypePrimaryIdIn(
								GkElectveConstants.RELATIONSHIP_TYPE_02, gkcMap
										.keySet().toArray(new String[0]));
				if (CollectionUtils.isNotEmpty(singles)) {
					// 重新整理单科条件人数
					for (GkRelationship sh : singles) {
						GkConditionDto gc = gkcMap.get(sh.getPrimaryId());
						if (!sumMap.containsKey(sh.getRelationshipTargetId())) {
							gkcMap.remove(sh.getPrimaryId());
							continue;
						}
						gc.setSumNum(gc.getSumNum()
								+ sumMap.get(sh.getRelationshipTargetId()));
						gc.setSubjectIdStr(sh.getRelationshipTargetId());
						dtos2.add(gc);
						sumMap.remove(sh.getRelationshipTargetId());
					}
				}
			}
			if (sumMap.size() > 0) {
				Iterator<Entry<String, Integer>> subIt = sumMap.entrySet()
						.iterator();
				GkConditionDto scon;
				while (subIt.hasNext()) {
					Entry<String, Integer> en = subIt.next();
					String sub = en.getKey();
					int sum = en.getValue();
					scon = new GkConditionDto();
					scon.setId(UuidUtils.generateUuid());
					scon.setRoundsId(roundsId);
					scon.setType(GkElectveConstants.GKCONDITION_SINGLE_0);
					scon.setSumNum(sum);
					scon.setNewData(true);
					dtos2.add(scon);

					ship = new GkRelationship(scon.getId(),
							GkElectveConstants.RELATIONSHIP_TYPE_02, sub);
					ship.setId(UuidUtils.generateUuid());
					ships.add(ship);

					ship = null;
					scon = null;
				}
			}
			if (CollectionUtils.isNotEmpty(dtos2)) {
				dealClsNum(dtos2, rou.getSubjectArrangeId(), singleSubIds);
				GkCondition gc;
				for(GkConditionDto gcd : dtos2){
					gc = new GkCondition();
					gc = gcd.convertTo(gc);
					gc.setModifyTime(now);
					if(gcd.isNewData()){
						gc.setCreationTime(now);
						ins.add(gc);
					} else {
						ups.add(gc);
					}
				}
			}
			// 删除不用的组合开班数据
			gkConditionDao.deleteByIds(delIds.toArray(new String[0]));
			gkRelationshipDao.deleteByTypePrimaryId(GkElectveConstants.RELATIONSHIP_TYPE_02, delIds.toArray(new String[0]));
		}
		
		if(GkElectveConstants.GKCONDITION_SINGLE_0.equals(type)){
			List<GkCondition> findByRoundsIdAndType = gkConditionDao.findByRoundsIdAndType(roundsId, type);
			if(CollectionUtils.isNotEmpty(findByRoundsIdAndType)){
				gkConditionDao.deleteAll(findByRoundsIdAndType);
			}
		}
		if(ins.size() > 0){
			gkConditionDao.saveAll(checkSave(ins.toArray(new GkCondition[0])));
			if(ships.size() > 0){
				gkRelationshipDao.saveAll(ships);
			}
		}
		if(ups.size() > 0){
			gkConditionDao.saveAll(checkSave(ups.toArray(new GkCondition[0])));
		}
		//下一步在开班成功的时候
//		if(StringUtils.isNotEmpty(roundsId)){
//			int step = GkElectveConstants.STEP_3;
//			if(GkElectveConstants.GKCONDITION_SINGLE_0.equals(groupType)){
//				step = GkElectveConstants.STEP_4;
//			}
//			gkRoundsService.updateStep(step, roundsId);
//		}
		//单科保存 步骤到4(如果组合是空)
		if(StringUtils.isNotEmpty(roundsId)){
			if(GkElectveConstants.GKCONDITION_SINGLE_0.equals(groupType)){
				int step = GkElectveConstants.STEP_4;
				gkRoundsService.updateStep(step, roundsId);
			}
		}
	}
	
	/**
	 * 重新计算开班条件的每班人数和开班数
	 * @param gcs
	 * @param arrangeId
	 * @param subIds
	 */
	private void dealClsNum(List<GkConditionDto> gcs, String arrangeId, List<String> subIds){
		GkSubjectArrange ak = gkSubjectArrangeDao.getOne(arrangeId);
//		int clsStuNum = ak.getClaNum()==null?0:ak.getClaNum();// 默认班级容纳数
		int clsStuNum = 50;// 默认班级容纳数 2017年6月15日暂时写死
		for(GkConditionDto gc : gcs){
			dealConditionNum(gc, clsStuNum);
		}
	}
	
	/**
	 * 计算条件中的每班人数和开班数，以每班人数没准
	 * @param clsStuNum 默认每班人数
	 */
	private void dealConditionNum(GkConditionDto gc, int clsStuNum){
		if(gc.getNum() == null || gc.getNum() < 1){
			gc.setNum(clsStuNum);
		}
		int cx = gc.getClaNum()==null?0:gc.getClaNum();
		boolean reNum = false;
		if (gc.getNum() !=null && gc.getNum() > 0) {
			cx = getPerNum(gc.getSumNum(), gc.getNum());
			gc.setClaNum(cx);
		} else if(cx > 0){
			reNum = true;
		}
		if(reNum && cx > 0){
			gc.setNum(getPerNum(gc.getSumNum(), cx));
		}
	}
	
	/**
	 * 获取结果
	 */
	private int getPerNum(int sum, int per){
		if(per == 0){
			return 0;
		}
		int pn = sum/per;
		if(sum%per != 0){
			pn ++;
		}
		return pn;
	}
	
	/**
	 * 封装开班课程到dto中
	 * @param gcs 开班条件主表数据
	 * @return
	 */
	private List<GkConditionDto> findConditionSub(List<GkCondition> gcs){
		List<GkConditionDto> dtos = new ArrayList<GkConditionDto>();
		if(CollectionUtils.isEmpty(gcs)){
			return dtos;
		}
		Map<String, GkConditionDto> gkcMap = new HashMap<String, GkConditionDto>(); 
		GkConditionDto dto;
		for(GkCondition gc : gcs){
			dto = new GkConditionDto();
			dto.convertFrom(gc);
			dto.setSubjectNames(gc.getName());
			dto.setSubShortNames(gc.getName());
			dtos.add(dto);
			
			gkcMap.put(gc.getId(), dto);
			dto = null;
		}
		// 获取开班条件对应课程
		List<GkRelationship> ships = gkRelationshipDao.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_02, gkcMap.keySet().toArray(new String[0]));
		if(CollectionUtils.isNotEmpty(ships)){
			for(GkRelationship ship : ships){
				GkConditionDto gc = gkcMap.get(ship.getPrimaryId());
				Set<String> gsids = gc.getSubjectIds();
				if(gsids == null){
					gsids = new HashSet<String>();
				}
				gsids.add(ship.getRelationshipTargetId());
				gc.setSubjectIds(gsids);
			}
		}
		return dtos;
	}
	
	public List<GkConditionDto> findGroupByGkResult(String gkId,String[] stuids){
		Set<String> subIds = new HashSet<String>();
		List<GkConditionDto> dtos = new ArrayList<GkConditionDto>();
		List<GkResult> results;
		if(stuids!=null){
			results = gkResultService.findGkByStuId(stuids, gkId);
		}else{
			results = gkResultService.findByArrangeId(gkId);
		}
		if(CollectionUtils.isEmpty(results)){
			return dtos;
		}
		// <studentId, StudentSubjectDto>
		Map<String, StudentSubjectDto> stuSubs = new HashMap<String, StudentSubjectDto>();
		for(GkResult re : results){
			if(!subIds.contains(re.getSubjectId())){
				subIds.add(re.getSubjectId());
			}
			// 学生选课数据处理
			StudentSubjectDto ssDto = stuSubs.get(re.getStudentId());
			if(ssDto == null){
				ssDto = new StudentSubjectDto();
				ssDto.setStuId(re.getStudentId());
				ssDto.setSubjectIds(new ArrayList<String>());
			}
			List<String> sids = ssDto.getSubjectIds();
			sids.add(re.getSubjectId());
			ssDto.setSubjectIds(sids);
			stuSubs.put(re.getStudentId(), ssDto);
		}
		Iterator<Entry<String, StudentSubjectDto>>  stuSubIt = stuSubs.entrySet().iterator();
		// 学生课程数据集合
		List<StudentSubjectDto> stuSubList = new ArrayList<StudentSubjectDto>();
		while(stuSubIt.hasNext()){
			StudentSubjectDto dto = stuSubIt.next().getValue();
			// 整理组合开班数据时，将只选了一门走班课程的学生过滤掉
			if(dto.getSubjectIds().size() < 2){
				continue;
			}
			List<String> sids = dto.getSubjectIds();
			Collections.sort(sids);// 学生课程id排序
			dto.setSubjectIds(sids);
			stuSubList.add(dto);
		}
		stuSubIt = null;
		
		// 全匹配放入组合
		// 组合，<subIdsStr, stuNum>
		Map<String, Integer> subStuNumMap = new HashMap<String, Integer>();
		Iterator<StudentSubjectDto> ssIt = stuSubList.iterator();
		while(ssIt.hasNext()){
			StudentSubjectDto dto = ssIt.next();
			List<String> subList = dto.getSubjectIds();
			String subIdStr = GkElectveConstants.listToStr(subList);
			addToNumMap(subStuNumMap, subIdStr, 1);// 组合人数+1
			ssIt.remove();
		}
		createDtoByNum(subStuNumMap, gkId, GkElectveConstants.GKCONDITION_GROUP_1, BaseConstants.SUBJECT_TYPE_A, dtos);
		if (CollectionUtils.isNotEmpty(dtos)) {
			// sub name
			dealWithSubName(dtos, subIds);
		}
		return dtos;
	}
	
	/**
	 * 获得组合班学生及课程 List<studentId+subjectId>
	 * @return
	 */
	private List<String> findTeachStuByArrangeId(String roundsId){
		List<String> stuSubs = new ArrayList<String>();
		List<GkBatch> batchs = gkBatchService.findByRoundsId(roundsId, GkElectveConstants.GKCONDITION_GROUP_1);
		for(GkBatch gc : batchs){
			stuSubs.add(gc.getTeachClassId());
		}
		Map<String, TeachClass> clsMap=new HashMap<String, TeachClass>();
		List<TeachClassStu> stus =new ArrayList<TeachClassStu>();
		if(CollectionUtils.isNotEmpty(stuSubs)){
			clsMap = EntityUtils.getMap(SUtils.dt(teachClassService.findTeachClassListByIds(stuSubs.toArray(new String[0])), new TR<List<TeachClass>>(){}),"id");
			stus = SUtils.dt(teachClassStuService.findByClassIds(stuSubs.toArray(new String[0])), new TR<List<TeachClassStu>>(){});
				
		}
		stuSubs.clear();
		if(MapUtils.isEmpty(clsMap) || CollectionUtils.isEmpty(stus)){
			return stuSubs;
		}
		for(TeachClassStu st : stus){
			TeachClass tc = clsMap.get(st.getClassId());
			if(tc == null){
				continue;
			}
			if(!stuSubs.contains(st.getStudentId()+tc.getCourseId())){
				stuSubs.add(st.getStudentId()+tc.getCourseId());
			}
		}
		return stuSubs;
	}
	
	/**
	 * 根据学生选课数据获取
	 * @param roundsId 非空
	 * @param arrangeId 非空
	 * @param type 开班类型，非空
	 * @param subIds 非空，用于后面课程名称显示
	 * @param openClass 是否开学考班
	 * @param subNum 选课数
	 * @param openClassType TODO
	 * @param tsids 走班课程id集合
	 */
	@SuppressWarnings("unchecked")
	private List<GkConditionDto> findConditionDefault(String roundsId, String arrangeId, String type, 
			Set<String> subIds, List<GkSubject> gkSubs, String openClass, int subNum, String openClassType){
		List<GkConditionDto> dtos = new ArrayList<GkConditionDto>();
		if(CollectionUtils.isEmpty(gkSubs)){
			return dtos;
		}
		boolean isGroupData = GkElectveConstants.GKCONDITION_GROUP_1.equals(type);// 是否取组合数据
		
		boolean openStu = GkElectveConstants.TRUE_STR.equals(openClass);// 是否开学考班
		List<String> tsids = new ArrayList<String>();// 走班科目
		List<String> asids = new ArrayList<String>();// 全部科目
		for(GkSubject gks : gkSubs){
			asids.add(gks.getSubjectId());
			if(GkElectveConstants.USE_TRUE == gks.getTeachModel()){
				tsids.add(gks.getSubjectId());
			}
		}
		// 组合开班时走班课程少于3门，或者没有走班科目
		if(CollectionUtils.isEmpty(tsids)){
			return dtos;
		}
		
		// 已经保存的学生选课信息，单科统计时要先过滤掉 studentId+subjectId---目前没有组合开班
//		List<String> groupSubIds = new ArrayList<String>();
		Set<String> preStuIds = new HashSet<String>();//3+0
		//key:subjectId
		Map<String,List<String>> preTwoStuIds=new HashMap<String, List<String>>();//2+0
		//组合班所有人
		Set<String> canStuId=new HashSet<String>();//组合班学生---走行政班 排班学生范围来自组合班
//		if(!isGroupData){
//			//目前没有组合排班 所以这边返回是空
//			groupSubIds = findTeachStuByArrangeId(roundsId);
//		} else {
			// 预排班的数据3+0
		//包括学生
		List<GkGroupClass> allList = gkGroupClassService.findGkGroupClassBySubjectIds(null,roundsId);
		if(CollectionUtils.isNotEmpty(allList)){
			for(GkGroupClass g:allList){
				if(CollectionUtils.isEmpty(g.getStuIdList())){
					continue;
				}
				if(GkElectveConstants.GROUP_TYPE_1.equals(g.getGroupType())){
					//3+0
					preStuIds.addAll(g.getStuIdList());
					canStuId.addAll(g.getStuIdList());
				}else if(GkElectveConstants.GROUP_TYPE_2.equals(g.getGroupType())){
					//2+x
					String subjectIds = g.getSubjectIds();
					String[] suubjectArr = subjectIds.split(",");
			
					for(String ss:suubjectArr){
						if(StringUtils.isNotBlank(ss)){
							if(!preTwoStuIds.containsKey(ss)){
								preTwoStuIds.put(ss, new ArrayList<String>());
							}
							preTwoStuIds.get(ss).addAll(g.getStuIdList());
							canStuId.addAll(g.getStuIdList());
						}
					}
				
				}else{
					canStuId.addAll(g.getStuIdList());
				}
			}
		}
//			List<GkGroupClass> groups = gkGroupClassDao.findByRoundsIdAndGroupType(roundsId, GkElectveConstants.GROUP_TYPE_1);
//			if (CollectionUtils.isNotEmpty(groups)) {
//				List<String> gids = EntityUtils.getList(groups, "id");
//				List<GkGroupClassStu> gs = gkGroupClassStuDao
//						.findByGroupClassIdIn(gids.toArray(new String[0]));
//				preStuIds = EntityUtils.getSet(gs, "studentId");
//			}
//			//2+x
//			List<GkGroupClass> groups2 = gkGroupClassService.findByRoundsIdType(roundsId, GkElectveConstants.GROUP_TYPE_2);
//			if (CollectionUtils.isNotEmpty(groups2)) {
//				for(GkGroupClass g:groups2){
//					String subjectIds = g.getSubjectIds();
//					String[] suubjectArr = subjectIds.split(",");
//					if(CollectionUtils.isNotEmpty(g.getStuIdList())){
//						for(String ss:suubjectArr){
//							if(StringUtils.isNotBlank(ss)){
//								if(!preTwoStuIds.containsKey(ss)){
//									preTwoStuIds.put(ss, new ArrayList<String>());
//								}
//								preTwoStuIds.get(ss).addAll(g.getStuIdList());
//								
//							}
//						}
//					}
//				}
//			}
//		}
		
		List<GkResult> results = gkResultService.findByArrangeId(arrangeId);
		// <stuId, StudentSubjectDto>
		Map<String, StudentSubjectDto> stuSubs = new HashMap<String, StudentSubjectDto>();
		List<String> stuIds = new ArrayList<String>();
		for(GkResult re : results){
			if(!asids.contains(re.getSubjectId())){
				continue;
			}
//			if(!isGroupData && 
//					(!tsids.contains(re.getSubjectId()) 
//							|| groupSubIds.contains(re.getStudentId()+re.getSubjectId()))){
//				continue;// 学生选课不走班或者已进入某个组合班中----已进入某个组合班目前是没有
//			}
			if(!isGroupData && 
					(!tsids.contains(re.getSubjectId()))){
				continue;// 学生选课不走班或者已进入某个组合班中----已进入某个组合班目前是没有
			}
			
			if(GkElectveConstants.TRUE_STR.equals(openClassType)){
				//走行政班
				if(!canStuId.contains(re.getStudentId())){
					continue;//未安排学生
				}
				if(preStuIds.contains(re.getStudentId())){
					continue;// 该学生已被预排班 3+0
				}
			}
			
//			if(GkElectveConstants.TRUE_STR.equals(openClassType) && preStuIds.contains(re.getStudentId())){
//				continue;// 该学生已被预排班 3+0
//			}
			
			if(!stuIds.contains(re.getStudentId())){
				stuIds.add(re.getStudentId());
			}
			if(!subIds.contains(re.getSubjectId())){
				subIds.add(re.getSubjectId());
			}
			
			// 学生选课数据处理
			StudentSubjectDto ssDto = stuSubs.get(re.getStudentId());
			if(ssDto == null){
				ssDto = new StudentSubjectDto();
				ssDto.setStuId(re.getStudentId());
				ssDto.setSubjectIds(new ArrayList<String>());
			}
			List<String> sids = ssDto.getSubjectIds();
			sids.add(re.getSubjectId());
			ssDto.setSubjectIds(sids);
			stuSubs.put(re.getStudentId(), ssDto);
		}
		
		
		
		
		Iterator<Entry<String, StudentSubjectDto>>  stuSubIt = stuSubs.entrySet().iterator();
		// 学生课程数据集合
		List<StudentSubjectDto> stuSubList = new ArrayList<StudentSubjectDto>();
		// 学考数据
		List<StudentSubjectDto> stuSubList2 = new ArrayList<StudentSubjectDto>();
		while(stuSubIt.hasNext()){
			StudentSubjectDto dto = stuSubIt.next().getValue();
			// 整理组合开班数据时
			List<String> sids = dto.getSubjectIds();
			if(isGroupData){
				// 不足3门走班课程的学生过滤掉
				if(sids.size() < subNum){
					continue;
				}
				List<String> les = ListUtils.retainAll(sids, tsids);// 获取学生走班学科
				if(CollectionUtils.isEmpty(les)){
					continue;// 全不走班
				}
			}
			
			Collections.sort(sids);// 学生课程id排序
			dto.setSubjectIds(sids);
			stuSubList.add(dto);
			if (!isGroupData && openStu) {
				stuIds.remove(dto.getStuId());
				List<String> xsids = ListUtils.removeAll(
						tsids, sids);// 学考
				if (CollectionUtils.isNotEmpty(xsids)) {
					StudentSubjectDto dto2 = new StudentSubjectDto();
					dto2.setStuId(dto.getStuId());
					dto2.setSubjectIds(xsids);
					stuSubList2.add(dto2);
					dto2 = null;
				}
			}
		}
		if(openStu && stuIds.size() > 0){// 学考
			StudentSubjectDto dto2;
			for(String stuId : stuIds){
				dto2 = new StudentSubjectDto();
				dto2.setStuId(stuId);
				dto2.setSubjectIds(tsids);
				stuSubList2.add(dto2);
				dto2 = null;
			}
		}
		stuSubIt = null;
		
		// 全匹配放入组合中，否则放入单科中
		// 组合，<subIdsStr, stuNum>
		Map<String, Integer> subStuNumMap = new HashMap<String, Integer>();
		// 单科，<subId, stuNum>
		Map<String, Integer> singleSubStuNumMap = new HashMap<String, Integer>();
		// 单科学考，<subId, stuNum>
		Map<String, Integer> singleStudySubStuNumMap = new HashMap<String, Integer>();
		Iterator<StudentSubjectDto> ssIt = stuSubList.iterator();
		while(ssIt.hasNext()){
			StudentSubjectDto dto = ssIt.next();
			List<String> subList = dto.getSubjectIds();
			String subIdStr = GkElectveConstants.listToStr(subList);
			boolean hasMapIn = false;// 课程map中已有这些科目了，无需进行匹配
			if(isGroupData && subStuNumMap.containsKey(subIdStr)){
				hasMapIn = true;
			}
			
			// 只有组合开班需要进行匹配，单科开班直接添加人数就可以了
			// ========================组合待匹配迭代开始====================================
			if (isGroupData && !hasMapIn) {
				Iterator<StudentSubjectDto> ossIt = stuSubList.iterator();
				while (ossIt.hasNext()) {
					StudentSubjectDto odto = ossIt.next();// 待匹配
					if (StringUtils.equals(dto.getStuId(), odto.getStuId())) {
						continue;// 同一个学生
					}
					List<String> osubList = odto.getSubjectIds();
					// 走班的选课数不同
					if (osubList.size() != subList.size()) {
						continue;
					}
					String osubIdStr = GkElectveConstants
							.listToStr(osubList);
					if (StringUtils.equals(subIdStr, osubIdStr)) {// 选课全一致
						addToNumMap(subStuNumMap, subIdStr, 1);// 组合人数+1
						break;
					}
				}
			}
			// =========================匹配迭代结束========================
			if(isGroupData){// 组合
				if(hasMapIn){
					addToNumMap(subStuNumMap, subIdStr, 1);
				}
			} else {// 单科直接添加人数
				for (String sid : subList) {
					if(preTwoStuIds.containsKey(sid) && GkElectveConstants.TRUE_STR.equals(openClassType)){
						if(preTwoStuIds.get(sid).contains(dto.getStuId())){
							continue;// 该学生已被预排班 2+x
						}
					}
					addToNumMap(singleSubStuNumMap, sid, 1);
				}
			}
			ssIt.remove();
		}
		ssIt = null;
		if (stuSubList2.size() > 0) {
			ssIt = stuSubList2.iterator();
			while(ssIt.hasNext()){
				StudentSubjectDto dto = ssIt.next();
				List<String> subList = dto.getSubjectIds();
				for (String sid : subList) {
					if(!subIds.contains(sid)){
						subIds.add(sid);
					}
					addToNumMap(singleStudySubStuNumMap, sid, 1);
				}
			}
		}
		if (isGroupData) {
			createDtoByNum(subStuNumMap, roundsId, type, BaseConstants.SUBJECT_TYPE_A, dtos);
		} else {
			createDtoByNum(singleSubStuNumMap, roundsId, type, BaseConstants.SUBJECT_TYPE_A, dtos);
			if(singleStudySubStuNumMap.size() > 0){
				createDtoByNum(singleStudySubStuNumMap, roundsId, type, BaseConstants.SUBJECT_TYPE_B, dtos);
			}
		}
		return dtos;
	}
	
	public List<GkConditionDto> findByRoundsIdAndTypeWithDefault(String roundsId, String type, String openClassType){
		List<GkConditionDto> dtos = new ArrayList<GkConditionDto>();
		List<GkCondition> gcs = this.findByGkConditions(roundsId, type);
		if(CollectionUtils.isNotEmpty(gcs)){// 已经有开班条件
			dtos = findConditionSub(gcs);
			for(GkConditionDto gc : dtos){
				Set<String> gsids = gc.getSubjectIds();
				if(CollectionUtils.isEmpty(gsids)){
					continue;
				}
				List<String> sids = new ArrayList<String>();
				for(String gs : gsids){
					sids.add(gs);
				}
				Collections.sort(sids);
				gc.setSubjectIdStr(GkElectveConstants.listToStr(sids));
			}
		} else {
			int groupNum = gkBatchService.findByRoundsId(roundsId, GkElectveConstants.GKCONDITION_GROUP_1).size();
			if(GkElectveConstants.GKCONDITION_GROUP_1.equals(type) && groupNum > 0){
				return dtos;
			}
			// 已经存在单科数据了，直接返回空列
			int sgNum = gkConditionDao.findNumByRoundsIdAndType(roundsId,
					GkElectveConstants.GKCONDITION_SINGLE_0);
			if(sgNum > 0){
				return dtos;
			}
			GkRounds rounds=gkRoundsService.findRoundById(roundsId);
			GkSubjectArrange ak = gkSubjectArrangeDao.getOne(rounds.getSubjectArrangeId());
//			int clsStuNum = ak.getClaNum()==null?0:ak.getClaNum();// 默认班级容纳数
			List<GkSubject> gkSubs = gkSubjectDao.findByRoundsId(roundsId);
			//List<GkSubject> gkSubs = gkSubjectDao.findByRoundsIdAndTeachModel(roundsId, GkSubject.TEACH_TYPE1);
			// 显示课程名称用
			Set<String> subIds = new HashSet<String>();
			dtos = findConditionDefault(roundsId, rounds.getSubjectArrangeId(), type, subIds, gkSubs, rounds.getOpenClass(), ak.getSubjectNum(), openClassType);
//			dealWithSubNum(dtos, clsStuNum);
			dealWithSubName(dtos, subIds);
		}
		Collections.sort(dtos, new Comparator<GkConditionDto>(){

			public int compare(GkConditionDto o1, GkConditionDto o2) {
				if(StringUtils.isNotEmpty(o1.getGkType()) && 
						StringUtils.isNotEmpty(o2.getGkType()) 
						&& !o1.getGkType().equals(o2.getGkType())){
					return o1.getGkType().compareTo(o2.getGkType());
				}
				if(o1.getSumNum() != o2.getSumNum()){
					return o2.getSumNum()-o1.getSumNum();
				}
				return StringUtils.trimToEmpty(o1.getSubShortNames()).compareTo(o2.getSubShortNames());
			}
		});
		return dtos;
	}
	
	/**
	 * 科目名称显示
	 */
	private void dealWithSubName(List<GkConditionDto> dtos, Set<String> subIds){
		if (CollectionUtils.isNotEmpty(dtos) && CollectionUtils.isNotEmpty(subIds)) {
			// 科目名称显示
			List<Course> courses = SUtils.dt(courseService
					.findBySubjectIdIn(subIds.toArray(new String[0])),
					new TR<List<Course>>() {
					});
			if (CollectionUtils.isNotEmpty(courses)) {
				Map<String, Course> cnm = new HashMap<String, Course>();
				for (Course cu : courses) {
					cnm.put(cu.getId(), cu);
				}
				for (GkConditionDto gc : dtos) {
					Set<String> gsids = gc.getSubjectIds();
					if (CollectionUtils.isEmpty(gsids)) {
						continue;
					}
					//按顺序
					List<String> gsidsList = new ArrayList<String>(gsids);
					Collections.sort(gsidsList);
					String sn = StringUtils.trimToEmpty(gc.getSubjectNames());// 全称
					String sns = StringUtils.trimToEmpty(gc.getSubShortNames());// 简称
					for (String sid : gsidsList) {
						if (StringUtils.isNotBlank(sn)) {
							sn += "、";
						}
						sn += StringUtils.trimToEmpty(cnm.get(sid).getSubjectName());
						sns += StringUtils.trimToEmpty(cnm.get(sid).getShortName());
					}
					if(GkElectveConstants.GKCONDITION_SINGLE_0.equals(gc.getType())){
						sn += StringUtils.trimToEmpty(gc.getGkType());
					}
					gc.setSubjectNames(sn);
					gc.setSubShortNames(sns);
				}
			}
		}
	}
	
	/**
	 * 创建组装dto数据
	 * @param numMap
	 * @param gkId
	 * @param type
	 * @param gkType 选考学考类型
	 * @param dtos 非空
	 */
	private void createDtoByNum(Map<String, Integer> numMap, String roundsId, String type, 
			String gkType, List<GkConditionDto> dtos){
		if(MapUtils.isEmpty(numMap)){
			return;
		}
		Iterator<Entry<String, Integer>> numIt = numMap.entrySet().iterator();
		GkConditionDto dto;
		while(numIt.hasNext()){
			Entry<String, Integer> en = numIt.next();
			dto = new GkConditionDto();
			dto.setRoundsId(roundsId);
			dto.setType(type);
			dto.setGkType(gkType);
			dto.setSumNum(en.getValue());
			dto.setSubjectIdStr(en.getKey());
			Set<String> sids = new HashSet<String>();
			CollectionUtils.addAll(sids, en.getKey().split(","));
			dto.setSubjectIds(sids);
			dtos.add(dto);
			dto = null;
		}
	}
	
	/**
	 * 根据选课科目及数量组装condition数据，包含每班人数及开班数
	 * @param dtos
	 * @param numMap
	 * @param type
	 */
	private void dealWithSubNum(
			List<GkConditionDto> dtos, int clsStuNum){
		for(GkConditionDto dto : dtos){
			int clsMaxInt = 0;
			dealConditionNum(dto, clsStuNum);
			dto.setClsMax(clsMaxInt);
		}
	}
	
	/**
	 * 追加数量
	 * @param numMap
	 * @param key
	 * @param addNum
	 */
	private void addToNumMap(Map<String, Integer> numMap, String key, int addNum){
		Integer num = numMap.get(key);
		if(num == null){
			num = 0;
		}
		num+=addNum;
		numMap.put(key, num);
	}
	
	@Override
	public List<GkCondition> findByGkConditions(String roundsId, String type) {
		List<GkCondition> list=new ArrayList<GkCondition>();
		if(StringUtils.isNotEmpty(type)){
			list=gkConditionDao.findByRoundsIdAndType(roundsId,type);
		}else{
			list=gkConditionDao.findByRoundsId(roundsId);
		}
		if(CollectionUtils.isNotEmpty(list)){
			return list;
		}else{
			return new ArrayList<GkCondition>();
		}
	}

	@Override
	public List<GkConditionDto> findByGkConditionDtos(
			 String roundsId, String type) {
		List<GkCondition> list = findByGkConditions(roundsId,type);
		if(CollectionUtils.isNotEmpty(list)){
			Map<String, GkCondition> gkConditionMap = new HashMap<String, GkCondition>();
			List<GkConditionDto> returnList = new ArrayList<GkConditionDto>();
			Map<String,GkConditionDto> map=new HashMap<String,GkConditionDto>();
			Set<String> ids=new HashSet<String>();
			for(GkCondition gk:list){
				ids.add(gk.getId());
				gkConditionMap.put(gk.getId(), gk);
			}
			List<GkRelationship> reList = gkRelationshipDao.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_02, ids.toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(reList)){
				for(GkRelationship ship:reList){
					if(!map.containsKey(ship.getPrimaryId())){
						GkConditionDto dto = new GkConditionDto();
						//GkConditionDto中id,subjectIds,num
						dto.setId(ship.getPrimaryId());
						dto.setSubjectIds(new HashSet<String>());
						dto.setGkType(gkConditionMap.get(ship.getPrimaryId()).getGkType());
						dto.setConditionName(gkConditionMap.get(ship.getPrimaryId()).getName());
						dto.setClaNum(gkConditionMap.get(ship.getPrimaryId()).getClaNum());
						dto.setNum(gkConditionMap.get(ship.getPrimaryId()).getNum()==null?0:gkConditionMap.get(ship.getPrimaryId()).getNum());
						//单科需要上限
						dto.setMaxNum(gkConditionMap.get(ship.getPrimaryId()).getMaxNum()==null?0:gkConditionMap.get(ship.getPrimaryId()).getMaxNum());
						map.put(ship.getPrimaryId(), dto);
					}
					map.get(ship.getPrimaryId()).getSubjectIds().add(ship.getRelationshipTargetId());
				}
				if(map.size()>0){
					 Collection<GkConditionDto> valueCollection = map.values();
					 returnList= new ArrayList<GkConditionDto>(valueCollection);
				}
			}
			return returnList;
		}else{
			return new ArrayList<GkConditionDto>();
		}

	}
	
	public void deleteByRoundsId(String roundsId){
		 List<GkCondition> glist = gkConditionDao.findByRoundsId(roundsId);
		 if(CollectionUtils.isNotEmpty(glist)){
			 Set<String> ids = EntityUtils.getSet(glist, "id");
			 gkRelationshipDao.deleteByTypePrimaryId(GkElectveConstants.RELATIONSHIP_TYPE_02, ids.toArray(new String[0]));
		 }
		gkConditionDao.deleteByRoundsId(roundsId);
	}

	@Override
	public void deleteAllArrange(String roundsId,GkSubjectArrange gkArrange) {
		List<String> cids;
		int step=0;
		cids = EntityUtils.getList(gkConditionDao.findByRoundsId(roundsId),"id");
		step = GkElectveConstants.STEP_1;
		if(CollectionUtils.isNotEmpty(cids)){
			gkRelationshipDao.deleteByTypePrimaryId(GkElectveConstants.RELATIONSHIP_TYPE_02, cids.toArray(new String[0]));
			gkConditionDao.deleteByIds(cids.toArray(new String[0]));
		}
		gkBatchService.deleteByRoundsIdAndType(roundsId, GkElectveConstants.GKCONDITION_GROUP_1);
		gkRoundsService.updateStep(step, roundsId);
		 //数据结束该项目解锁
		if(GkElectveConstants.USE_TRUE==gkArrange.getIsLock()){
			gkArrange.setIsLock(GkElectveConstants.USE_FALSE);
			gkArrange.setModifyTime(new Date());
			gkSubjectArrangeService.save(gkArrange);
		}
	}
}
