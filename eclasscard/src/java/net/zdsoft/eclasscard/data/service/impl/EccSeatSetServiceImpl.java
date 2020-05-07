package net.zdsoft.eclasscard.data.service.impl;

import net.zdsoft.eclasscard.data.dto.EccSeatSetDto;
import net.zdsoft.eclasscard.data.service.EccSeatItemService;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccSeatSetDao;
import net.zdsoft.eclasscard.data.entity.EccSeatSet;
import net.zdsoft.eclasscard.data.service.EccSeatSetService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.Date;

@Service("eccSeatSetService")
public class EccSeatSetServiceImpl extends BaseServiceImpl<EccSeatSet,String> implements EccSeatSetService{

	@Autowired
	private EccSeatSetDao eccSeatSetDao;
	@Autowired
	private EccSeatItemService eccSeatItemService;

	@Override
	protected BaseJpaRepositoryDao<EccSeatSet, String> getJpaDao() {
		return eccSeatSetDao;
	}

	@Override
	protected Class<EccSeatSet> getEntityClass() {
		return EccSeatSet.class;
	}

	@Override
	public EccSeatSet findOneByUnitIdAndClassId(String unitId, String classId) {
		return eccSeatSetDao.findOneByUnitIdAndClassId(unitId,classId);
	}

	@Override
	public void saveSeatSet(EccSeatSetDto seatSetDto) {
		EccSeatSet saveEntity=findOneByUnitIdAndClassId(seatSetDto.getUnitId(),seatSetDto.getClassId());
		if (saveEntity==null){
			//新增
			saveEntity=new EccSeatSet();
			saveEntity.setId(UuidUtils.generateUuid());
			saveEntity.setCreationTime(new Date());
			saveEntity.setUnitId(seatSetDto.getUnitId());
			saveEntity.setClassId(seatSetDto.getClassId());
			saveEntity.setClassType(seatSetDto.getClassType());
		}
		saveEntity.setColNumber(seatSetDto.getColNumber());
		saveEntity.setRowNumber(seatSetDto.getRowNumber());
        Integer sp1 = seatSetDto.getSpace1();
        Integer sp2 = seatSetDto.getSpace2();
        if (sp1>=sp2){
            throw new RuntimeException("过道1设置必须小于过道2");
        }
        if (sp2>=seatSetDto.getColNumber()){
            throw new RuntimeException("过道2设置不能大于列数");
        }
        saveEntity.setSpaceNum(sp1 +","+sp2);
		saveEntity.setModifyTime(new Date());
		eccSeatSetDao.save(saveEntity);
	}

	@Override
	public EccSeatSetDto findDtoByUnitIdAndClassId(String unitId, String classId,boolean isDefault) {
		EccSeatSet seatSet = findOneByUnitIdAndClassId(unitId, classId);
		EccSeatSetDto seatSetDto=null;
		if(seatSet==null){
			if(isDefault) {
				//返回一个默认的数据
				seatSetDto=EccSeatSetDto.defaultInstance();
			}
			
		}else{
			seatSetDto = new EccSeatSetDto();
			String[] space = seatSet.getSpaceNum().split(",");
			seatSetDto.setSpace1(Integer.parseInt(space[0]));
			seatSetDto.setSpace2(Integer.parseInt(space[1]));
			seatSetDto.setColNumber(seatSet.getColNumber());
			seatSetDto.setRowNumber(seatSet.getRowNumber());
			seatSetDto.setId(seatSet.getId());
		}
		return seatSetDto;
	}

	@Override
	public void deleteByClassId(String classId) {
		if (StringUtils.isBlank(classId)){
			return;
		}
		eccSeatSetDao.deleteByClassId(classId);
		eccSeatItemService.deleteByClassId(classId);
	}
}
