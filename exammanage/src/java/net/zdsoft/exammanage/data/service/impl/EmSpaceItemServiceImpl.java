package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmSpaceItemDao;
import net.zdsoft.exammanage.data.entity.EmSpaceItem;
import net.zdsoft.exammanage.data.service.EmSpaceItemService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("emSpaceItemService")
public class EmSpaceItemServiceImpl extends BaseServiceImpl<EmSpaceItem, String> implements EmSpaceItemService {

    @Autowired
    private EmSpaceItemDao emSpaceItemDao;

    @Override
    protected BaseJpaRepositoryDao<EmSpaceItem, String> getJpaDao() {
        return emSpaceItemDao;
    }

    @Override
    protected Class<EmSpaceItem> getEntityClass() {
        return EmSpaceItem.class;
    }

    @Override
    public Map<String, List<EmSpaceItem>> findMapByStatParmId(String statParmId) {
        List<EmSpaceItem> itemList = emSpaceItemDao.findByStatParmId(statParmId);
        if (CollectionUtils.isNotEmpty(itemList)) {
            Map<String, List<EmSpaceItem>> map = new HashMap<String, List<EmSpaceItem>>();
            for (EmSpaceItem item : itemList) {
                if (!map.containsKey(item.getParmType())) {
                    map.put(item.getParmType(), new ArrayList<EmSpaceItem>());
                }
                map.get(item.getParmType()).add(item);
            }

            return map;
        } else {
            return new HashMap<String, List<EmSpaceItem>>();
        }
    }

    @Override
    public void deleteByStatParmId(String statParmId) {

        emSpaceItemDao.deleteByStatParmId(statParmId);
    }

    @Override
    public void saveAllEntity(List<EmSpaceItem> emSpaceItemList) {
        if (CollectionUtils.isEmpty(emSpaceItemList)) {
            return;
        }
        emSpaceItemDao.saveAll(checkSave(emSpaceItemList.toArray(new EmSpaceItem[]{})));
    }

    @Override
    public List<EmSpaceItem> findByStatParmIdIn(String[] statParmIds) {
        if (statParmIds == null || statParmIds.length < 0) {
            return new ArrayList<EmSpaceItem>();
        }
        return emSpaceItemDao.findByStatParmIdIn(statParmIds);
    }

    @Override
    public List<EmSpaceItem> findByNameAndIdIn(String name, String[] ids) {
        return emSpaceItemDao.findByNameAndIdIn(name, ids);
    }

    @Override
    public List<EmSpaceItem> findByParmTypeAndIdIn(String parmType, String[] ids) {
        return emSpaceItemDao.findByParmTypeAndIdIn(parmType, ids);
    }

    /**
     * 根据名称查询
     *
     * @param ids
     * @return
     */
    @Override
    public List<EmSpaceItem> findByIds(String[] ids) {
        return emSpaceItemDao.findByIds(ids);
    }

    ;

}
