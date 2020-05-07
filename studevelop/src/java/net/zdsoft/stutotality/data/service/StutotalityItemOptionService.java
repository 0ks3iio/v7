package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stutotality.data.entity.StutotalityItemOption;

import java.util.List;

public interface StutotalityItemOptionService extends BaseService<StutotalityItemOption,String> {

    List<StutotalityItemOption> getListByItemId(String itemId);

    List<StutotalityItemOption> findByItemIds(String [] itemIds);

    List<StutotalityItemOption> findByItemIdsWithMaster(String [] itemIds);

    List<StutotalityItemOption> findListByUnitId(String unitId);

    void saveStutotalityItemOption(StutotalityItemOption stutotalityItemOption);

    void deleteByUnitIdAndItemId(String unitId, String[] itemIds);

    void removeStutotalityById(String id);
}
