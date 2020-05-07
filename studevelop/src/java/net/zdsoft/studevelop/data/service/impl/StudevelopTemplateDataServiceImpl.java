package net.zdsoft.studevelop.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.dao.StuDevelopScoreRecordDao;
import net.zdsoft.studevelop.data.dao.StuDevelopSubjectDao;
import net.zdsoft.studevelop.data.dao.StudevelopTemplateItemDao;
import net.zdsoft.studevelop.data.dao.StudevelopTemplateResultDao;
import net.zdsoft.studevelop.data.entity.StuDevelopCateGory;
import net.zdsoft.studevelop.data.entity.StuDevelopProject;
import net.zdsoft.studevelop.data.entity.StuDevelopScoreRecord;
import net.zdsoft.studevelop.data.entity.StuDevelopSubject;
import net.zdsoft.studevelop.data.entity.StudevelopHealthProject;
import net.zdsoft.studevelop.data.entity.StudevelopHealthStudent;
import net.zdsoft.studevelop.data.entity.StudevelopHealthStudentDetail;
import net.zdsoft.studevelop.data.entity.StudevelopTemplate;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateItem;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateResult;
import net.zdsoft.studevelop.data.service.StuDevelopCateGoryService;
import net.zdsoft.studevelop.data.service.StuDevelopProjectService;
import net.zdsoft.studevelop.data.service.StuDevelopSubjectService;
import net.zdsoft.studevelop.data.service.StuHealthStudentDetailService;
import net.zdsoft.studevelop.data.service.StuHealthStudentService;
import net.zdsoft.studevelop.data.service.StuHealthyProjectService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateDataService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateItemService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateResultService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateService;

/**
 * 临时用，处理老数据，将老数据迁移到新表中，包括成绩数据
 * @author weixh
 * 2019年6月18日	
 */
@Service("studevelopTemplateDataService")
public class StudevelopTemplateDataServiceImpl implements StudevelopTemplateDataService {
	@Autowired
	private StuDevelopCateGoryService stuDevelopCateGoryService;
	@Autowired
	private StuDevelopSubjectService stuDevelopSubjectService;
	@Autowired
	private StudevelopTemplateService studevelopTemplateService;
	@Autowired
	private StudevelopTemplateResultService studevelopTemplateResultService;
	@Autowired
	private StudevelopTemplateItemService studevelopTemplateItemService;
	@Autowired
    private StuDevelopProjectService stuDevelopProjectService;
	@Autowired
	private StuDevelopScoreRecordDao stuDevelopScoreRecordDao;
	@Autowired
    private StuHealthyProjectService stuHealthyProjectService;
    @Autowired
    private StuHealthStudentService stuHealthStudentService;
    @Autowired
    private StuHealthStudentDetailService stuHealthStudentDetailService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	@Autowired
	private StuDevelopSubjectDao stuDevelopSubjectDao;
	@Autowired
	private StudevelopTemplateItemDao studevelopTemplateItemDao;
	@Autowired
	private StudevelopTemplateResultDao studevelopTemplateResultDao;
	
	/**
	 * 成绩数据处理
	 */
	private void getSubData(List<StudevelopTemplate> toTemps, List<StudevelopTemplateItem> toTempItems,
			List<StudevelopTemplateResult> toResults, List<String> gradeIds, 
			String unitId, String acadyear,
			String semester, String toSemester, Date now) {
		// subject科目-->category二级科目
		// project 维度：期中期末学习态度
		// score sub/category/project -->score
		List<StuDevelopSubject> subs = stuDevelopSubjectDao.stuDevelopSubjectListByGradeIds(unitId, acadyear, semester, gradeIds.toArray(new String[0]));
		if(CollectionUtils.isEmpty(subs)) {
			return;
		}
		List<StuDevelopProject> pros = stuDevelopProjectService.stuDevelopProjectList(unitId, acadyear, semester, 
				gradeIds.toArray(new String[0]));
		if(CollectionUtils.isEmpty(pros)) {
			return;
		}
		List<StuDevelopCateGory> cates = stuDevelopCateGoryService.findListBySubjectIdIn(EntityUtils.getList(subs, StuDevelopSubject::getId).toArray(new String[0]));
		//<oldId,newId>
		Set<String> subIds = EntityUtils.getSet(subs, StuDevelopSubject::getId);
		Set<String> catIds = EntityUtils.getSet(cates, StuDevelopCateGory::getId);
		Map<String, StudevelopTemplate> gTmpMap = new HashMap<>();
		//<oldId,newId>
		Map<String, StudevelopTemplateItem> proIdMap = new HashMap<>();
		int i = 0;
		for(StuDevelopProject pro : pros) {
			StudevelopTemplate tmp = gTmpMap.get(pro.getGradeId());
			if(tmp == null) {
				tmp = new StudevelopTemplate();
				tmp.setId(UuidUtils.generateUuid());
				tmp.setAcadyear(acadyear);
				tmp.setSemester(toSemester);
				tmp.setGradeId(pro.getGradeId());
				tmp.setCode(StuDevelopConstant.TEMPLATE_CODE_GRADE);
				tmp.setCreationTime(now);
				tmp.setUnitId(unitId);
				toTemps.add(tmp);
				gTmpMap.put(pro.getGradeId(), tmp);
			}
			StudevelopTemplateItem item = new StudevelopTemplateItem();
			item.setId(UuidUtils.generateUuid());
			item.setItemName(pro.getProjectName());
			if (StuDevelopConstant.PROJECT_STATE_SUB.equals(pro.getState())) {
				item.setObjectType(StuDevelopConstant.TEMPLATE_OBJECT_TYPE_SUBJECT);
			} else if(StuDevelopConstant.PROJECT_STATE_CAT.equals(pro.getState())) {
				item.setObjectType(StuDevelopConstant.TEMPLATE_OBJECT_TYPE_CATEGORY);
			}
			item.setModifyTime(now);
			item.setCreationTime(now);
            item.setTemplateId(tmp.getId());
            item.setIsClosed(StuDevelopConstant.HEALTH_IS_NOT_CLOSED);
            item.setSingleOrInput(StuDevelopConstant.TEMPLATE_MAINTAIN_INPUT);
            item.setUnitId(unitId);
            item.setSortNumber(i++);
            toTempItems.add(item);
            proIdMap.put(pro.getId(), item);
		}
		List<StuDevelopScoreRecord> scores = stuDevelopScoreRecordDao.findByProjectIds(acadyear, semester, EntityUtils.getList(pros, StuDevelopProject::getId).toArray(new String[0]));
		if(CollectionUtils.isNotEmpty(scores)) {
			for(StuDevelopScoreRecord score : scores) {
				if(!subIds.contains(score.getSubjectId())
						|| (StringUtils.isNotEmpty(score.getCategoryId()) && !catIds.contains(score.getCategoryId()))
						|| !proIdMap.containsKey(score.getProjectId())) {
					System.out.println("["+score.getId()+"]成绩数据匹配不上 过滤掉");
					continue;// 数据匹配不上 过滤掉
				}
				StudevelopTemplateResult result = new StudevelopTemplateResult();
				result.setSubjectId(score.getSubjectId());
				result.setCategoryId(score.getCategoryId());
				StudevelopTemplateItem item = proIdMap.get(score.getProjectId());
				result.setTemplateItemId(item.getId());
				result.setTemplateId(item.getTemplateId());
				result.setId(UuidUtils.generateUuid());
				result.setResult(score.getScore());
				result.setStudentId(score.getStudentId());
				result.setAcadyear(acadyear);
				result.setSemester(toSemester);
				result.setCreationTime(now);
				result.setModifyTime(now);
				toResults.add(result);
			}
		}
	}
	
	public void saveTempData(String unitId, String acadyear, String semester) {
		List<Grade> grades = SUtils.dt(gradeRemoteService.findBySchoolIdAndIsGraduate(unitId, 0), Grade.class);
		if(CollectionUtils.isEmpty(grades)) {
			return;
		}
		String toSemester = semester;
		Date now = new Date();
		List<StudevelopTemplate> toTemps = new ArrayList<>();
		List<StudevelopTemplateItem> toTempItems = new ArrayList<>();
		List<StudevelopTemplateResult> toResults = new ArrayList<>();
		//=====================成绩数据================================
		getSubData(toTemps, toTempItems, toResults, EntityUtils.getList(grades, Grade::getId), 
				unitId, acadyear, semester, toSemester, now);
		
		// ====体能/身体健康/心理素质================================
		getHealthData(toTemps, toTempItems, toResults, 
				unitId, acadyear, semester, toSemester, now);
		
		if(toTemps.size() == 0) {
			return;
		}
		System.out.println("迁移数据：模板数量="+toTemps.size()+"，项目数量="+toTempItems.size()+"，成绩数量="+toResults.size());
		List<StudevelopTemplate> oldTemps = studevelopTemplateService.findListBy(new String[] {"unitId", "acadyear", "semester"}, 
				new String[] {unitId, acadyear, toSemester});
		if(CollectionUtils.isNotEmpty(oldTemps)) {
			String[] oldTpIds = EntityUtils.getList(oldTemps, StudevelopTemplate::getId).toArray(new String[0]);
			studevelopTemplateService.deleteAll(oldTemps.toArray(new StudevelopTemplate[0]));
			studevelopTemplateItemDao.deleteByTemplateIds(oldTpIds);
			studevelopTemplateResultDao.deleteByTemplateIds(oldTpIds);
		}
		studevelopTemplateService.saveAll(toTemps.toArray(new StudevelopTemplate[0]));
		if (toTempItems.size() > 0) {
			studevelopTemplateItemService.saveAll(toTempItems.toArray(new StudevelopTemplateItem[0]));
		}
		if (toResults.size() > 0) {
			studevelopTemplateResultService.saveAll(toResults.toArray(new StudevelopTemplateResult[0]));
		}
	}
	
	/**
	 * 处理获得身心健康数据
	 */
	private void getHealthData(List<StudevelopTemplate> toTemps, List<StudevelopTemplateItem> toTempItems,
			List<StudevelopTemplateResult> toResults, 
			String unitId, String acadyear,
			String semester, String toSemester, Date now) {
		// studoc_health_project--> type=2 身体，=1 心理素质
		// healthstu.id==healthstudeteail.healthStudentId+projectId-->结果
		StudevelopHealthProject heaPropars = new StudevelopHealthProject();
		heaPropars.setAcadyear(acadyear);
		heaPropars.setSemester(semester);
		heaPropars.setSchoolId(unitId);
		List<StudevelopHealthProject> heaPros = stuHealthyProjectService.getProjectByAcadyearSemesterSection(heaPropars);
		if (CollectionUtils.isNotEmpty(heaPros)) {
			Map<String, StudevelopTemplate> secTmpMap = new HashMap<>();
			//<oldId,newId>
			Map<String, StudevelopTemplateItem> proIdMap = new HashMap<>();
			int i=0;
			for(StudevelopHealthProject pro : heaPros) {
				StudevelopTemplate tmp = secTmpMap.get(pro.getSchSection());
				if(tmp == null) {
					tmp = new StudevelopTemplate();
					tmp.setId(UuidUtils.generateUuid());
					tmp.setAcadyear(acadyear);
					tmp.setSemester(toSemester);
					tmp.setSection(pro.getSchSection());
					tmp.setCode(StuDevelopConstant.TEMPLATE_CODE_HEALTH);
					tmp.setCreationTime(now);
					tmp.setUnitId(unitId);
					toTemps.add(tmp);
					secTmpMap.put(pro.getSchSection(), tmp);
				}
				StudevelopTemplateItem item = new StudevelopTemplateItem();
				item.setId(UuidUtils.generateUuid());
				item.setItemName(pro.getProjectName()+StringUtils.trimToEmpty(pro.getProjectUnit()));
				if (StuDevelopConstant.HEALTH_TYPE_TN.equals(pro.getProjectType())) {
					item.setObjectType(StuDevelopConstant.TEMPLATE_OBJECT_TYPE_TN);
				} else if(StuDevelopConstant.HEALTH_TYPE_STZB.equals(pro.getProjectType())) {
					item.setObjectType(StuDevelopConstant.TEMPLATE_OBJECT_TYPE_STZB);
				} else if(StuDevelopConstant.HEALTH_TYPE_XLSZ.equals(pro.getProjectType())) {
					item.setObjectType(StuDevelopConstant.TEMPLATE_OBJECT_TYPE_XLSZ);
				}
				item.setModifyTime(now);
				item.setCreationTime(now);
	            item.setTemplateId(tmp.getId());
	            item.setIsClosed(StuDevelopConstant.HEALTH_IS_NOT_CLOSED);
	            item.setSingleOrInput(StuDevelopConstant.TEMPLATE_MAINTAIN_INPUT);
	            item.setUnitId(unitId);
	            item.setSortNumber(i++);
	            toTempItems.add(item);
	            proIdMap.put(pro.getId(), item);
			}
			List<String> hpIds = EntityUtils.getList(heaPros, StudevelopHealthProject::getId);
			List<StudevelopHealthStudent> hstus = stuHealthStudentService.findListBy(
					new String[] { "schoolId", "acadyear", "semester" }, new String[] { unitId, acadyear, semester });
			Map<String, StudevelopHealthStudent> hsMap = EntityUtils.getMap(hstus, StudevelopHealthStudent::getId);
			List<StudevelopHealthStudentDetail> details = stuHealthStudentDetailService.findListByIn("projectId", hpIds.toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(details)) {
				for(StudevelopHealthStudentDetail detail : details) {
					if(!hsMap.containsKey(detail.getHealthStudentId())
							|| !proIdMap.containsKey(detail.getProjectId())) {
						continue;
					}
					StudevelopHealthStudent hs = hsMap.get(detail.getHealthStudentId());
					StudevelopTemplateItem item = proIdMap.get(detail.getProjectId());
					StudevelopTemplateResult result = new StudevelopTemplateResult();
					result.setTemplateItemId(item.getId());
					result.setTemplateId(item.getTemplateId());
					result.setId(UuidUtils.generateUuid());
					result.setResult(detail.getProjectValue());
					result.setStudentId(hs.getStudentId());
					result.setAcadyear(acadyear);
					result.setSemester(toSemester);
					result.setCreationTime(now);
					result.setModifyTime(now);
					toResults.add(result);
				}
			}
		}
	}

	
	public void saveCopyTempData(String unitId, String acadyear, boolean force) {
		String semester = "1";
		String toSemester = "2";
		List<StudevelopTemplate> oldTemps = studevelopTemplateService.findListBy(new String[] {"unitId", "acadyear", "semester"}, 
				new String[] {unitId, acadyear, semester});
		String msg = "数据拷贝失败["+unitId+":"+acadyear+"]：";
		if(CollectionUtils.isEmpty(oldTemps)) {
			System.out.println(msg+"1学期没有模板数据");
			return;
		}
		Map<String, StudevelopTemplate> ocodeTps = new HashMap<>();
		Map<String, StudevelopTemplate> oldTpIdMap = new HashMap<>();
		for(StudevelopTemplate tp : oldTemps) {
			if(StuDevelopConstant.TEMPLATE_CODE_THOUGHT.equals(tp.getCode())) {// 思想素质的暂不处理
				continue;
			}
			ocodeTps.put(StringUtils.trimToEmpty(tp.getSection()) 
					+ StringUtils.trimToEmpty(tp.getGradeId()) + tp.getCode(), tp);
			oldTpIdMap.put(tp.getId(), tp);
		}
		List<StudevelopTemplateItem> oldItems = studevelopTemplateItemService.findListByIn("templateId", oldTpIdMap.keySet().toArray(new String[0]));
		if(CollectionUtils.isEmpty(oldItems)) {
			System.out.println(msg+"1学期没有项目数据");
			return;
		}
		Map<String, StudevelopTemplateItem> ocodeItemMap = new HashMap<>();
		Map<String, StudevelopTemplateItem> oitemIdMap = new HashMap<>();
		for(StudevelopTemplateItem item : oldItems) {
			item.setItemName(item.getItemName().replaceAll(" ", "").replaceAll(" ", ""));
			StudevelopTemplate tp = oldTpIdMap.get(item.getTemplateId());
			String key = StringUtils.trimToEmpty(tp.getSection()) 
					+ StringUtils.trimToEmpty(tp.getGradeId()) + tp.getCode();
			ocodeItemMap.put(key+item.getItemName(), item);
			oitemIdMap.put(item.getId(), item);
		}
		List<StudevelopTemplateResult> oldRes = studevelopTemplateResultService.findListByIn("templateId", oldTpIdMap.keySet().toArray(new String[0]));
		if(CollectionUtils.isEmpty(oldRes)) {
			System.out.println(msg+"1学期没有结果数据");
			return;
		}
		List<StudevelopTemplate> newTemps = studevelopTemplateService.findListBy(new String[] {"unitId", "acadyear", "semester"}, 
				new String[] {unitId, acadyear, toSemester});
		Date now = new Date();
//		List<StudevelopTemplate> toTemps = new ArrayList<>();
//		List<StudevelopTemplateItem> toTempItems = new ArrayList<>();
		List<StudevelopTemplateResult> toResults = new ArrayList<>();
		if(CollectionUtils.isEmpty(newTemps)) {
			System.out.println(msg+"2学期没有模板数据");
			return;// 新项目没有设置，返回
//			for(StudevelopTemplate tp : oldTemps) {
//				StudevelopTemplate nt = new StudevelopTemplate();
//				EntityUtils.copyProperties(tp, nt);
//				nt.setId(UuidUtils.generateUuid());
//				nt.setSemester(toSemester);
//				nt.setCreationTime(now);
//				nt.setModifyTime(now);
//				toTemps.add(nt);
//			}
		}
		List<StuDevelopSubject> osubs = stuDevelopSubjectService.findListBy(new String[] {"unitId", "acadyear", "semester"}, 
				new String[] {unitId, acadyear, semester});
		Map<String, StuDevelopSubject> osubCodeMap = new HashMap<>();
		Map<String, StuDevelopSubject> osubIdMap = new HashMap<>();
		Map<String, StuDevelopCateGory> ocatCodeMap = new HashMap<>();
		Map<String, StuDevelopCateGory> ocatIdMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(osubs)) {
			for(StuDevelopSubject sub : osubs) {
				sub.setName(sub.getName().replaceAll(" ", "").replaceAll(" ", ""));
				osubCodeMap.put(sub.getGradeId()+sub.getName(), sub);
				osubIdMap.put(sub.getId(), sub);
			}			List<StuDevelopCateGory> ocats = stuDevelopCateGoryService.findListByIn("subjectId", osubIdMap.keySet().toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(ocats)) {
				for(StuDevelopCateGory oc : ocats) {
					oc.setCategoryName(oc.getCategoryName().replaceAll(" ", "").replaceAll(" ", ""));
					ocatCodeMap.put(oc.getSubjectId()+oc.getCategoryName(), oc);
					ocatIdMap.put(oc.getId(), oc);
				}
			}
		}
		Map<String, StudevelopTemplate> ncodeTps = new HashMap<>();
		Map<String, StudevelopTemplate> nTpIdMap = new HashMap<>();
		for(StudevelopTemplate tp : newTemps) {
			if(StuDevelopConstant.TEMPLATE_CODE_THOUGHT.equals(tp.getCode())) {// 思想素质的暂不处理
				continue;
			}
			ncodeTps.put(StringUtils.trimToEmpty(tp.getSection()) 
					+ StringUtils.trimToEmpty(tp.getGradeId()) + tp.getCode(), tp);
			nTpIdMap.put(tp.getId(), tp);
		}
		List<StudevelopTemplateItem> nItems = studevelopTemplateItemService.findListByIn("templateId", nTpIdMap.keySet().toArray(new String[0]));
		if(CollectionUtils.isEmpty(nItems)) {
			System.out.println(msg+"2学期没有项目数据");
			return;// 项目没有设置，返回
//			for(StudevelopTemplateItem oi : oldItems) {
//				StudevelopTemplate otp = oldTpIdMap.get(oi.getTemplateId());
//				StudevelopTemplate ntp = ncodeTps.get(StringUtils.trimToEmpty(otp.getSection()) 
//					+ StringUtils.trimToEmpty(otp.getGradeId()) + otp.getCode());
//				if(ntp == null) {
//					
//				}
//				StudevelopTemplateItem ni = new StudevelopTemplateItem();
//				EntityUtils.copyProperties(oi, ni);
//				ni.setId(UuidUtils.generateUuid());
//			}
		}
		List<StudevelopTemplateResult> nRes = studevelopTemplateResultService.findListByIn("templateId", nTpIdMap.keySet().toArray(new String[0]));
		// key:stuId+itemId+subId+catId
		Map<String, StudevelopTemplateResult> nrMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(nRes)) {
			if (!force) {
				System.out.println(msg + "2学期已经有结果数据");
				return;// 已有数据
			}
			for(StudevelopTemplateResult res : nRes) {
				nrMap.put(res.getStudentId() + res.getTemplateItemId() 
				+ StringUtils.trimToEmpty(res.getSubjectId())+StringUtils.trimToEmpty(res.getCategoryId()), res);
			}
		}
		List<StuDevelopSubject> nsubs = stuDevelopSubjectService.findListBy(new String[] {"unitId", "acadyear", "semester"}, 
				new String[] {unitId, acadyear, toSemester});
		Map<String, StuDevelopSubject> nsubCodeMap = new HashMap<>();
		Map<String, StuDevelopSubject> nsubIdMap = new HashMap<>();
		Map<String, StuDevelopCateGory> ncatCodeMap = new HashMap<>();
		Map<String, StuDevelopCateGory> ncatIdMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(nsubs)) {
			for(StuDevelopSubject sub : nsubs) {
				sub.setName(sub.getName().replaceAll(" ", "").replaceAll(" ", ""));
				nsubCodeMap.put(sub.getGradeId()+sub.getName(), sub);
				nsubIdMap.put(sub.getId(), sub);
			}
			List<StuDevelopCateGory> ncats = stuDevelopCateGoryService.findListByIn("subjectId", nsubIdMap.keySet().toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(ncats)) {
				for(StuDevelopCateGory nc : ncats) {
					nc.setCategoryName(nc.getCategoryName().replaceAll(" ", "").replaceAll(" ", ""));
					ncatCodeMap.put(nc.getSubjectId()+nc.getCategoryName(), nc);
					ncatIdMap.put(nc.getId(), nc);
				}
			}
		}
		Map<String, StudevelopTemplateItem> ncodeItemMap = new HashMap<>();
		Map<String, StudevelopTemplateItem> nitemIdMap = new HashMap<>();
		for(StudevelopTemplateItem item : nItems) {
			item.setItemName(item.getItemName().replaceAll(" ", "").replaceAll(" ", ""));
			StudevelopTemplate tp = nTpIdMap.get(item.getTemplateId());
			String key = StringUtils.trimToEmpty(tp.getSection()) 
					+ StringUtils.trimToEmpty(tp.getGradeId()) + tp.getCode();
			ncodeItemMap.put(key+item.getItemName(), item);
			nitemIdMap.put(item.getId(), item);
		}
		for(StudevelopTemplateResult or : oldRes) {
			StudevelopTemplate ot = oldTpIdMap.get(or.getTemplateId());
			String key = StringUtils.trimToEmpty(ot.getSection()) 
					+ StringUtils.trimToEmpty(ot.getGradeId()) + ot.getCode();
			StudevelopTemplateItem oi = oitemIdMap.get(or.getTemplateItemId());
			StudevelopTemplate nt = ncodeTps.get(key);
			if(nt == null) {// 新模板没有的学段/年级/类型数据
				continue;
			}
			StudevelopTemplateItem ni = ncodeItemMap.get(key+oi.getItemName());
			if(ni == null) {// 同类型模板下没有同名项目
				continue;
			}
			StudevelopTemplateResult nr = new StudevelopTemplateResult();
			EntityUtils.copyProperties(or, nr);
			nr.setTemplateId(ni.getTemplateId());
			nr.setTemplateItemId(ni.getId());
			nr.setSemester(toSemester);
			if(StringUtils.isNotEmpty(or.getSubjectId())) {
				StuDevelopSubject os = osubIdMap.get(or.getSubjectId());
				if(os == null) {
					nr = null;
					continue;// 科目不存在
				}
				StuDevelopSubject ns = nsubCodeMap.get(os.getGradeId()+os.getName());
				if(ns == null) {
					nr = null;
					continue;// 新科目不存在
				}
				nr.setSubjectId(ns.getId());
				
				if(StringUtils.isNotEmpty(or.getCategoryId())) {
					StuDevelopCateGory oc = ocatIdMap.get(or.getCategoryId());
					if(oc == null) {// 科目类别不存在
						nr = null;
						continue;
					}
					StuDevelopCateGory nc = ncatCodeMap.get(nr.getSubjectId()+oc.getCategoryName());
					if(nc == null) {// 新科目类别不存在
						nr = null;
						continue;
					}
					nr.setCategoryId(nc.getId());
				}
			}
			String rkey = nr.getStudentId() + nr.getTemplateItemId() 
			+ StringUtils.trimToEmpty(nr.getSubjectId())+StringUtils.trimToEmpty(nr.getCategoryId());
			StudevelopTemplateResult exnr = nrMap.get(rkey);// 判断目标成绩是否已存在
			if(exnr != null) {
				nr.setId(exnr.getId());
				nr.setCreationTime(exnr.getCreationTime());
			} else {
				nr.setId(UuidUtils.generateUuid());
				nr.setCreationTime(now);
			}
			nr.setModifyTime(now);
			toResults.add(nr);
		}
		if(toResults.size() > 0) {
			System.out.println("数据拷贝成功：数量="+toResults.size());
			studevelopTemplateResultService.saveAll(toResults.toArray(new StudevelopTemplateResult[0]));
		}
	}
	
	
}
