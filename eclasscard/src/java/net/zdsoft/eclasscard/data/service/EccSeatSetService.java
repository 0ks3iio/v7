package net.zdsoft.eclasscard.data.service;

import net.zdsoft.eclasscard.data.dto.EccSeatSetDto;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccSeatSet;

public interface EccSeatSetService extends BaseService<EccSeatSet, String> {
	
	public EccSeatSet findOneByUnitIdAndClassId(String unitId,String classId);

    void saveSeatSet(EccSeatSetDto seatSetDto);

    /**
     * 返回封装之后的对象
     * @param unitId
     * @param classId
     * @param isDefault true如果为空,则会返回一个默认设置
     * @return
     */
    EccSeatSetDto findDtoByUnitIdAndClassId(String unitId, String classId,boolean isDefault);

    /**
     * 删除 seatSet 和 seatItem 2个表
     * @param classId
     */
    void deleteByClassId(String classId);
}
