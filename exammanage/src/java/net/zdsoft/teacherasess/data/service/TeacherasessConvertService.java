package net.zdsoft.teacherasess.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.teacherasess.data.dto.TeaConvertDto;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvert;

public interface TeacherasessConvertService extends BaseService<TeacherasessConvert, String>{

	public List<TeaConvertDto> findDtoListByAcadyearWithMaster(String unitId, String acadyear);

	public String saveDto(TeaConvertDto dto);

	public TeaConvertDto findDtoByConvertId(String convertId);
	
	public List<TeacherasessConvert> findListByAcadyearAndGradeId(String unitId,String acadyear,String gradeId);
}