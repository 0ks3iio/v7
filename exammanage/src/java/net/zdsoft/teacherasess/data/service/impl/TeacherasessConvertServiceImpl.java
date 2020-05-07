package net.zdsoft.teacherasess.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.teacherasess.data.dao.TeacherasessConvertDao;
import net.zdsoft.teacherasess.data.dto.GroupDto;
import net.zdsoft.teacherasess.data.dto.TeaConvertDto;
import net.zdsoft.teacherasess.data.dto.TeaConvertExamDto;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvert;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertExam;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertGroup;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertExamService;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertGroupService;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teacherasessConvertService")
public class TeacherasessConvertServiceImpl extends BaseServiceImpl<TeacherasessConvert, String> implements TeacherasessConvertService{
	@Autowired
	private TeacherasessConvertDao teacherasessConvertDao;
	@Autowired
	private TeacherasessConvertExamService teacherasessConvertExamService;
	@Autowired
	private TeacherasessConvertGroupService teacherasessConvertGroupService;
	@Autowired
	private EmExamInfoService emExamInfoService;
	@Autowired
	private GradeRemoteService gradeRemoteService;
	
	@Override
	public String saveDto(TeaConvertDto dto) {
		TeacherasessConvert convert = new TeacherasessConvert();
		convert.setAcadyear(dto.getAcadyear());
		convert.setCreationTime(new Date());
		convert.setExamNum(dto.getExamDtos().size());
		convert.setGradeId(dto.getGradeId());
		convert.setIsDeleted(0);
		convert.setUnitId(dto.getUnitId());
		convert.setName(dto.getConvertName());
		if(StringUtils.equals(dto.getStatus(), "2")) {
			convert.setStatus("1");//对比中
		}else{
			convert.setStatus("0");
		}
		convert.setXuankaoType(dto.getXuankaoType());
		checkSave(convert);
		this.save(convert);
		List<TeacherasessConvertExam> exams = new ArrayList<>();
		TeacherasessConvertExam e;
		for (TeaConvertExamDto dd : dto.getExamDtos()) {
			e = new TeacherasessConvertExam();
			dtoToEntity(dd,e);
			e.setConvertId(convert.getId());
			e.setUnitId(convert.getUnitId());
			e.setId(UuidUtils.generateUuid());
			exams.add(e);
		}
		teacherasessConvertExamService.saveAll(exams.toArray(new TeacherasessConvertExam[0]));
		
		if(CollectionUtils.isNotEmpty(dto.getGroupList())){
			List<TeacherasessConvertGroup> groups=new ArrayList<>();
			TeacherasessConvertGroup g;
			for(GroupDto group:dto.getGroupList()){
				g=new TeacherasessConvertGroup();
				g.setId(UuidUtils.generateUuid());
				g.setConvertId(convert.getId());
				g.setUnitId(convert.getUnitId());
				g.setExamIds(group.getExamId1()+","+group.getExamId2());
				g.setScales(group.getScale1()+","+group.getScale2());
				g.setCreationTime(new Date());
				//g.setAcadyear(dto.getAcadyear());待定 需不需要
				groups.add(g);
			}
			teacherasessConvertGroupService.saveAll(groups.toArray(new TeacherasessConvertGroup[0]));
		}
		return convert.getId();
	}
	
	private void dtoToEntity(TeaConvertExamDto dd, TeacherasessConvertExam e) {
		e.setExamId(dd.getExamId());
		e.setScale(NumberUtils.toFloat(dd.getScale()));
		e.setAcadyear(dd.getAcadyear());
	}
	@Override
	public TeaConvertDto findDtoByConvertId(String convertId) {
		TeacherasessConvert e = this.findOne(convertId);
		Map<String, Grade> gradeMap = EntityUtils.getMap(SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(e.getUnitId(), e.getAcadyear()), new TR<List<Grade>>() {}), Grade::getId);
		List<TeacherasessConvertExam> convertExams = teacherasessConvertExamService.findListByConvertIdInWithMaster(new String[] {convertId});
		Set<String> examIds = EntityUtils.getSet(convertExams, TeacherasessConvertExam::getExamId);
		Map<String, EmExamInfo> infoMap = emExamInfoService.findMapByIdIn(examIds.toArray(new String[0]));
		TeaConvertDto dto = new TeaConvertDto();
		TeaConvertExamDto examDto;
		List<TeaConvertExamDto> examDtos;
			if(!gradeMap.containsKey(e.getGradeId())) {
				return dto;
			}
			dto.setAcadyear(e.getAcadyear());
			dto.setConvertId(e.getId());
			dto.setStatus(e.getStatus());
			dto.setConvertName(e.getName());
			dto.setCreationTime(e.getCreationTime());
			dto.setGradeId(e.getGradeId());
			dto.setGradeName(gradeMap.get(e.getGradeId()).getGradeName());
			examDtos = new ArrayList<>();
			for (TeacherasessConvertExam exam : convertExams) {
				if(StringUtils.equals(exam.getConvertId(), e.getId())) {
					examDto = new TeaConvertExamDto();
					examDto.setExamId(exam.getExamId());
					examDto.setExamName(infoMap.get(exam.getExamId()).getExamName());
					examDto.setScale(exam.getScale()+"");
					examDtos.add(examDto);
				}
			}
			dto.setExamNum(examDtos.size());
			dto.setExamDtos(examDtos);
		return dto;
	}
	@Override
	public List<TeaConvertDto> findDtoListByAcadyearWithMaster(String unitId, String acadyear) {
		List<TeaConvertDto> dtolist = new ArrayList<>();
		List<TeacherasessConvert> convertList = teacherasessConvertDao.findByUnitIdAndAcadyearWithMaster(unitId,acadyear);
		if(CollectionUtils.isEmpty(convertList)) {
			return dtolist;
		}
		Set<String> convertIds = EntityUtils.getSet(convertList, TeacherasessConvert::getId);
		Map<String, Grade> gradeMap = EntityUtils.getMap(SUtils.dt(gradeRemoteService.findByUnitIdAndCurrentAcadyear(unitId, acadyear), new TR<List<Grade>>() {}), Grade::getId);
		List<TeacherasessConvertExam> convertExams = teacherasessConvertExamService.findListByConvertIdInWithMaster(convertIds.toArray(new String[0]));
		Set<String> examIds = EntityUtils.getSet(convertExams, TeacherasessConvertExam::getExamId);
		Map<String, EmExamInfo> infoMap = emExamInfoService.findMapByIdIn(examIds.toArray(new String[0]));
		TeaConvertDto dto;
		TeaConvertExamDto examDto;
		List<TeaConvertExamDto> examDtos;
		for (TeacherasessConvert e : convertList) {
			if(!gradeMap.containsKey(e.getGradeId())) {
				continue;
			}
			dto = new TeaConvertDto();
			dto.setAcadyear(e.getAcadyear());
			dto.setConvertId(e.getId());
			dto.setStatus(e.getStatus());
			dto.setConvertName(e.getName());
			dto.setCreationTime(e.getCreationTime());
			dto.setGradeId(e.getGradeId());
			dto.setGradeName(gradeMap.get(e.getGradeId()).getGradeName());
			examDtos = new ArrayList<>();
			for (TeacherasessConvertExam exam : convertExams) {
				if(StringUtils.equals(exam.getConvertId(), e.getId())) {
					examDto = new TeaConvertExamDto();
					examDto.setExamId(exam.getExamId());
					examDto.setExamName(infoMap.get(exam.getExamId()).getExamName());
					examDto.setScale(exam.getScale()+"");
					examDtos.add(examDto);
				}
			}
			dto.setExamNum(examDtos.size());
			dto.setExamDtos(examDtos);
			dtolist.add(dto);
		}
		return dtolist;
	}
		
	@Override
	public List<TeacherasessConvert> findListByAcadyearAndGradeId(
			String unitId, String acadyear, String gradeId) {
		List<TeacherasessConvert> teacherasessConverts=new ArrayList<TeacherasessConvert>();
		teacherasessConverts=teacherasessConvertDao.findListByAcadyearAndGradeId(unitId, acadyear, gradeId);
		return teacherasessConverts;
	}














	@Override
	protected BaseJpaRepositoryDao<TeacherasessConvert, String> getJpaDao() {
		return teacherasessConvertDao;
	}
	
	@Override
	protected Class<TeacherasessConvert> getEntityClass() {
		return TeacherasessConvert.class;
	}
	
}
