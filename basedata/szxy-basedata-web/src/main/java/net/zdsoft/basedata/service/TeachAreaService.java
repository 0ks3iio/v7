package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.TeachArea;

public interface TeachAreaService extends BaseService<TeachArea, String> {

    /**
     * 通用方法 根据单位找未删除校区 排序：order by areaCode
     * 
     * @param unitId
     * @return
     */
    List<TeachArea> findByUnitId(String unitId);

    void deleteAllIsDeleted(String... ids);

    List<TeachArea> saveAllEntitys(TeachArea... teachArea);

	List<TeachArea> findByUnitIdIn(String[] uidList);

}
