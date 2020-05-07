package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmStatSpaceDao;
import net.zdsoft.exammanage.data.entity.EmStatSpace;
import net.zdsoft.exammanage.data.service.EmStatSpaceService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("emStatSpaceService")
public class EmStatSpaceServiceImpl extends BaseServiceImpl<EmStatSpace, String> implements EmStatSpaceService {

    @Autowired
    private EmStatSpaceDao emStatSpaceDao;

    @Override
    protected BaseJpaRepositoryDao<EmStatSpace, String> getJpaDao() {
        return emStatSpaceDao;
    }

    @Override
    protected Class<EmStatSpace> getEntityClass() {
        return EmStatSpace.class;
    }

    @Override
    public void deleteByStatObjectId(String... statObjectId) {
        if (statObjectId != null && statObjectId.length > 0) {
            emStatSpaceDao.deleteByStatObjectIdIn(statObjectId);
        }
    }

    @Override
    public List<EmStatSpace> saveAllEntitys(EmStatSpace... emStatSpace) {
        return emStatSpaceDao.saveAll(checkSave(emStatSpace));
    }

    @Override
    public List<EmStatSpace> findByStatRangeIdIn(String[] rangeIds) {
        if (rangeIds == null || rangeIds.length <= 0) {
            return new ArrayList<EmStatSpace>();
        }
        return emStatSpaceDao.findByStatRangeIdIn(rangeIds);
    }

    @Override
    public List<EmStatSpace> findBySpaceItemIdAndStatRangeIdIn(String spaceItemId, String[] rangeIds) {
        return emStatSpaceDao.findBySpaceItemIdAndStatRangeIdIn(spaceItemId, rangeIds);
    }

    @Override
    public List<EmStatSpace> findByStatRangeIdAndSpaceItemIdIn(String statRangeId,
                                                               String[] spaceItemIds) {
        return emStatSpaceDao.findByStatRangeIdAndSpaceItemIdIn(statRangeId, spaceItemIds);
    }

    @Override
    public List<EmStatSpace> findByObjectIdAndIsConAndRangeIdIn(String statObjectId, String isCon, String[] statRangeIds) {

        return emStatSpaceDao.findByStatObjectIdAndIsConAndStatRangeIdIn(statObjectId, isCon, statRangeIds);

    }
}
