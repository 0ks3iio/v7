package net.zdsoft.eclasscard.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.eclasscard.data.dto.EccFaceSetDto;
import net.zdsoft.eclasscard.data.entity.EccFaceSet;

public interface EccFaceSetService {
	
	public void saveEccFaceSetList(EccFaceSetDto faceSetDto);
	
	public List<EccFaceSet> findEccFaceSetListByUnitId(String unitId);
	//key:infoId value:classIds,classNames,allNums
	public Map<String, String[]> findEccFaceSetListByInfoIds(String unitId, String[] infoIds);

	public List<EccFaceSet> findEccFaceSetListByInfoId(String unitId, String infoId);
	
}
