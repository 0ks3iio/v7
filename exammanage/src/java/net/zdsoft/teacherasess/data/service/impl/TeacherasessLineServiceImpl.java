package net.zdsoft.teacherasess.data.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.teacherasess.data.dao.TeacherasessLineDao;
import net.zdsoft.teacherasess.data.dto.ClassDto;
import net.zdsoft.teacherasess.data.entity.TeacherAsessLine;
import net.zdsoft.teacherasess.data.entity.TeacherAsessResult;
import net.zdsoft.teacherasess.data.service.TeacherasessLineService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teacherasessLineService")
public class TeacherasessLineServiceImpl extends BaseServiceImpl<TeacherAsessLine, String> implements TeacherasessLineService{

	@Autowired
	private TeacherasessLineDao teacherasessLineDao;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private TeachClassRemoteService teachClassRemoteService;
	
	@Override
	public void deleteByAssessId(String teacherAsessId) {
		teacherasessLineDao.deleteByAssessId(teacherAsessId);
	}		
	
	@Override
	public Map<String, TeacherAsessLine> getTeacherAsessLineByUnitIdAndAssessIdAndSubjectId(
			String unitId, String assessId, String subjectId) {
		Map<String,TeacherAsessLine> teaAsessMap=new HashMap<>();
		List<TeacherAsessLine> teacherAsessLines=teacherasessLineDao.getTeacherAsessLineByUnitIdAndAssessIdAndSubjectId(unitId, assessId, subjectId);
		teaAsessMap=EntityUtils.getMap(teacherAsessLines, e->e.getClassId()+"_"+e.getAsessRankId()+"_"+e.getLineType()+"_"+e.getConvertType()+"_"+e.getSlice());
		
		return teaAsessMap;
	}	
	
	@Override
	public List<ClassDto> getClassDtoByUnitIdAndAsessIdAndSubjectId(
			String unitId, String asessId, String subjectId) {
		List<ClassDto> classDtos=new ArrayList<>();
		List<String> classIds=teacherasessLineDao.getListByUnitIdAndAsessIdAndSubjectIdAndClassType(unitId, asessId, subjectId, "1");
		List<String> classTeas=teacherasessLineDao.getListByUnitIdAndAsessIdAndSubjectIdAndClassType(unitId, asessId, subjectId, "2");
		List<TeacherAsessLine> teacherAsessLines=teacherasessLineDao.getTeacherAsessLineByUnitIdAndAssessIdAndSubjectId(unitId, asessId, subjectId);
		Map<String,TeacherAsessLine> teaMap=EntityUtils.getMap(teacherAsessLines, e->e.getClassId());
		if(CollectionUtils.isNotEmpty(classIds)){
			//List<Clazz> clsList3 = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>() {});
			for(String cls : classIds){
				ClassDto classDto=new ClassDto();
				classDto.setClassId(cls);
				classDto.setClassName(teaMap.get(cls).getClassName());
				classDtos.add(classDto);
			}
		}
		if(CollectionUtils.isNotEmpty(classTeas)){
			//List<TeachClass> teaClsList = SUtils.dt(teachClassRemoteService.findTeachClassListByIds(classTeas.toArray(new String[0])),new TR<List<TeachClass>>(){});
			for (String cls : classTeas) {
				ClassDto classDto=new ClassDto();
				classDto.setClassId(cls);
				classDto.setClassName(teaMap.get(cls).getClassName());
				classDtos.add(classDto);
			}
		}
		paixu(classDtos);
		return classDtos;
	}
	
	private void paixu(List<ClassDto> classDtos){
		if(CollectionUtils.isNotEmpty(classDtos)){
			Collections.sort(classDtos, new Comparator<ClassDto>(){
				@Override
				public int compare(ClassDto o1, ClassDto o2) {
					return o1.getClassName().compareTo(o2.getClassName());
				}
				
			});
		}
	}

	protected BaseJpaRepositoryDao<TeacherAsessLine, String> getJpaDao() {
		return teacherasessLineDao;
	}
	
	@Override
	protected Class<TeacherAsessLine> getEntityClass() {
		return TeacherAsessLine.class;
	}

}
