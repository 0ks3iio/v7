package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmSpaceItem;

import java.util.List;
import java.util.Map;

public interface EmSpaceItemService extends BaseService<EmSpaceItem, String> {

    /**
     * key:parmType
     *
     * @param statParmId
     * @return
     */
    Map<String, List<EmSpaceItem>> findMapByStatParmId(String statParmId);

    public void deleteByStatParmId(String statParmId);

    public void saveAllEntity(List<EmSpaceItem> emSpaceItemList);

    List<EmSpaceItem> findByStatParmIdIn(String[] statParmIds);

    /**
     * 根据名称查询
     *
     * @param name
     * @param ids
     * @return
     */
    List<EmSpaceItem> findByNameAndIdIn(String name, String[] ids);

    /**
     * 根据名称查询
     *
     * @param ids
     * @return
     */
    List<EmSpaceItem> findByIds(String[] ids);

    /**
     * 根据参数类型查询
     *
     * @param name
     * @param ids
     * @return
     */
    List<EmSpaceItem> findByParmTypeAndIdIn(String parmType, String[] ids);


}
