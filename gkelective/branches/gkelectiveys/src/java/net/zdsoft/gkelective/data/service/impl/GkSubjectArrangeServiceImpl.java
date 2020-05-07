package net.zdsoft.gkelective.data.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dao.GkAllocationDao;
import net.zdsoft.gkelective.data.dao.GkResultDao;
import net.zdsoft.gkelective.data.dao.GkRoundsDao;
import net.zdsoft.gkelective.data.dao.GkStuConversionDao;
import net.zdsoft.gkelective.data.dao.GkStuRemarkDao;
import net.zdsoft.gkelective.data.dao.GkSubjectArrangeDao;
import net.zdsoft.gkelective.data.dto.GkSubjectArrangeDto;
import net.zdsoft.gkelective.data.entity.GkRelationship;
import net.zdsoft.gkelective.data.entity.GkRounds;
import net.zdsoft.gkelective.data.entity.GkSubject;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.entity.GkTeachPlacePlan;
import net.zdsoft.gkelective.data.service.GkBatchService;
import net.zdsoft.gkelective.data.service.GkConditionService;
import net.zdsoft.gkelective.data.service.GkRelationshipService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;
import net.zdsoft.gkelective.data.service.GkSubjectService;
import net.zdsoft.gkelective.data.service.GkTeachPlacePlanService;
import net.zdsoft.gkelective.data.service.GkTimetableLimitArrangService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

@Service("gkSubjectArrangeService")
public class GkSubjectArrangeServiceImpl extends BaseServiceImpl<GkSubjectArrange, String> implements
        GkSubjectArrangeService {

    @Autowired
    private GkSubjectArrangeDao gkSubjectArrangeDao;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private GkSubjectService gkSubjectService;
    @Autowired
    private GkRelationshipService gkRelationshipService;
    @Autowired
    private GkBatchService gkBatchService;
    @Autowired
    private GkResultDao gkResultDao;
    @Autowired
    private GkConditionService gkConditionService;
    @Autowired
    private GkRoundsDao gkRoundsDao;
    @Autowired
	private GkTimetableLimitArrangService gkTimetableLimitArrangService;
    @Autowired
    private GkAllocationDao gkAllocationDao;
    @Autowired
    private GkStuRemarkDao gkStuRemarkDao;
    @Autowired
    private GkStuConversionDao gkStuConversionDao;
    @Autowired
    private GkTeachPlacePlanService gkTeachPlacePlanService;
    @Override
    protected BaseJpaRepositoryDao<GkSubjectArrange, String> getJpaDao() {
        return gkSubjectArrangeDao;
    }

    @Override
    protected Class<GkSubjectArrange> getEntityClass() {
        return GkSubjectArrange.class;
    }

    @Override
    public void saveDto(GkSubjectArrangeDto arrangeDto) throws Exception {
    	this.checkSave(arrangeDto.getGsaEnt());
    	this.save(arrangeDto.getGsaEnt());
    	if(ArrayUtils.isNotEmpty(arrangeDto.getSubjectIds())){
    		String primaryId=arrangeDto.getGsaEnt().getId();
    		//先做删除
    		gkRelationshipService.deleteByPrimaryId(primaryId,GkElectveConstants.RELATIONSHIP_TYPE_03);
    		
    		List<GkRelationship> shipList = new ArrayList<GkRelationship>();
    		for(String subid :arrangeDto.getSubjectIds()){
    			GkRelationship ent=new GkRelationship();
    			ent.setPrimaryId(primaryId);
    			ent.setRelationshipTargetId(subid);
    			ent.setRelationshipType(GkElectveConstants.RELATIONSHIP_TYPE_03);
    			ent.setId(UuidUtils.generateUuid());
    			shipList.add(ent);
    		}
    		gkRelationshipService.saveAll(shipList);
    	}
        if(CollectionUtils.isNotEmpty(arrangeDto.getGksubList())){
        	List<GkSubject>  gksList = arrangeDto.getGksubList();
        	for(GkSubject gks:gksList){
        		if(StringUtils.isBlank(gks.getId())){
        			gks.setId(UuidUtils.generateUuid());
        		}else{
        			gkRelationshipService.deleteByPrimaryId(gks.getId(),GkElectveConstants.RELATIONSHIP_TYPE_01);
        		}
        		List<GkRelationship> shipList = new ArrayList<GkRelationship>();
        		if(gks.getTeacherIds()!=null && gks.getTeacherIds().length>0){
        			for(String teaId :gks.getTeacherIds()){
        				GkRelationship gkRelationship = new GkRelationship();
                       gkRelationship.setId(UuidUtils.generateUuid());
                       gkRelationship.setPrimaryId(gks.getId());
                       gkRelationship.setRelationshipTargetId(teaId);
                       gkRelationship.setRelationshipType(GkElectveConstants.RELATIONSHIP_TYPE_01);
                       shipList.add(gkRelationship);
        			}
        			gkRelationshipService.saveAll(shipList);
        		}
        	}
        	gkSubjectService.saveAll(gksList.toArray(new GkSubject[0]));
        }
    }

    @Override
    public GkSubjectArrangeDto findByGradeId(String gradeId) {
        GkSubjectArrangeDto dto = new GkSubjectArrangeDto();
        GkSubjectArrange arrange = gkSubjectArrangeDao.findByGradeId(gradeId);
        if(arrange!=null){
	        Grade grade = SUtils.dt(gradeRemoteService.findOneById(gradeId), new TypeReference<Grade>() {});
	        arrange.setArrangeName(grade.getOpenAcadyear().substring(0, 4)+"级"+grade.getGradeName()+"新高考项目");
	        dto.setGsaEnt(arrange);
        }
        return dto;
    }

   

    @Override
    public List<GkSubjectArrangeDto> findByUnitIdIsUsing(String unitId, Integer isUsing) {
        List<GkSubjectArrange> list = new ArrayList<GkSubjectArrange>();
        if (isUsing == null) {
            list = gkSubjectArrangeDao.findByUnitId(unitId);
        }
        else {
            list = gkSubjectArrangeDao.findByUnitIdIsUsing(unitId, isUsing);
        }
        
        return this.toDtoOrder(list);
    }
    
    public List<GkSubjectArrangeDto> toDtoOrder(List<GkSubjectArrange> ents){
    	List<GkSubjectArrangeDto> dtos=new ArrayList<GkSubjectArrangeDto>();
    	if(CollectionUtils.isNotEmpty(ents)){
    		Set<String> gids=EntityUtils.getSet(ents, "gradeId");
    		List<Grade> gradeList = SUtils.dt(gradeRemoteService.findListByIds(gids.toArray(new String[0])),new TR<List<Grade>>(){});
    		Map<String,Grade> gmap= EntityUtils.getMap(gradeList, "id");
    		for(GkSubjectArrange ent:ents){
    			GkSubjectArrangeDto dto=new GkSubjectArrangeDto();
    			Grade g =gmap.get(ent.getGradeId());
    			if(g!=null && StringUtils.isNotBlank(g.getOpenAcadyear())){
    				dto.setOpenAcadyear(g.getOpenAcadyear().substring(0, 4));
    				ent.setArrangeName(dto.getOpenAcadyear()+"级"+g.getGradeName()+"新高考项目");
    			}else{
    				dto.setOpenAcadyear("0");//防止排序空异常添加默认值
    			}
    			dto.setGsaEnt(ent);
    			dtos.add(dto);
    		}
    		
    		if(CollectionUtils.isNotEmpty(dtos)){
    			SortUtils.DESC(dtos, "openAcadyear");
    		}
    	}
		return dtos;
    }
    
    @Override
    public void deletedById(String id) {
    	//走班设置
    	GkSubjectArrange ent=gkSubjectArrangeDao.findById(id).orElse(null);
    	List<GkRounds> gkrlist =gkRoundsDao.findBySubjectArrangeId(id);
    	
    	List<GkTeachPlacePlan> planList = gkTeachPlacePlanService.findBySubjectArrangeId(id, true);
    	
    	if(CollectionUtils.isNotEmpty(planList)){
    		for(GkTeachPlacePlan g:planList){
    			gkTeachPlacePlanService.deleteByPlanId(g.getId());
    		}
    	}
    	
    	if(CollectionUtils.isNotEmpty(gkrlist)){
    		for(GkRounds rent :gkrlist){
    			//删除班级调班信息
    			gkStuConversionDao.deleteByRoundsId(rent.getId());
//    			//删除开班结果插入的课程信息
//    			gkBatchService.clearCourseSchedule(rent, ent);
    			//删除开班结果---批次表 groupClass 教学班
    			gkBatchService.deleteByRoundsId(rent.getId());
    			//删除开班条件
    			gkConditionService.deleteByRoundsId(rent.getId());
//    			//删除教学班上课时间
//    			gkTimetableLimitArrangService.deleteByRoundsId(rent.getAcadyear(), rent.getSemester(), rent.getId());
    			//删除课程是否走班信息--对应关联表
    			gkSubjectService.deleteByRoundsId(rent.getId());
    			//删除轮次
    			rent.setIsDeleted(1);
    			gkRoundsDao.update(rent, new String[] { "isDeleted"});
    		}
    	}

    	//学生备注和排班成绩信息
    	gkStuRemarkDao.deleteByArrangeId(id);
    	//开班排序条件
    	//gkAllocationDao.deleteBySubjectArrangeId(id);
    	//学生选课结果
    	gkResultDao.deleteByArrangeIdIn(new String[]{id});
    	//选课信息
    	gkRelationshipService.deleteByPrimaryId(id, GkElectveConstants.RELATIONSHIP_TYPE_03);
    	//删除项目
    	ent.setIsDeleted(1);
    	gkSubjectArrangeDao.update(ent,new String[] { "isDeleted"});
    }

	@Override
	public GkSubjectArrange findArrangeById(String id) {
		GkSubjectArrange findOne = gkSubjectArrangeDao.findById(id).orElse(null);
		Grade grade = SUtils.dt(gradeRemoteService.findOneById(findOne.getGradeId()), new TypeReference<Grade>() {});
		findOne.setArrangeName(grade.getOpenAcadyear().substring(0, 4)+"级"+grade.getGradeName()+"新高考项目");
		return findOne;
	}

	@Override
	public GkSubjectArrange findEntByGradeId(String gradeId) {
		return gkSubjectArrangeDao.findByGradeId(gradeId);
	}

}
