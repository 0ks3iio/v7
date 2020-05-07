package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmStatSpace;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface EmStatSpaceDao extends BaseJpaRepositoryDao<EmStatSpace, String> {

    void deleteByStatObjectIdIn(String[] statObjectId);

    List<EmStatSpace> findByStatRangeIdIn(String[] rangeIds);

    List<EmStatSpace> findByStatRangeIdAndSpaceItemIdIn(String statRangeId,
                                                        String[] spaceItemIds);


    List<EmStatSpace> findByStatObjectIdAndIsConAndStatRangeIdIn(String statObjectId, String isCon, String[] statRangeIds);


    public List<EmStatSpace> findBySpaceItemIdAndStatRangeIdIn(String spaceItemId, String[] rangeIds);

}
