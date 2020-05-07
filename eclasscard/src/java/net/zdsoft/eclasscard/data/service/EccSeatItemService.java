package net.zdsoft.eclasscard.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccSeatItem;

import java.util.List;

public interface EccSeatItemService extends BaseService<EccSeatItem, String> {
	
	public List<EccSeatItem> findListBySeatId(String unitId,String seatId);

    void deleteByClassId(String classId);
    /**
     * 导入保存
     * @param unitId
     * @param seatId
     * @param rowNums
     * @param delStuIds
     * @param seatItems
     */
    public void saveAndDelete(String unitId, String seatId, Integer[] rowNums, String[] delStuIds, EccSeatItem[] seatItems);
}
