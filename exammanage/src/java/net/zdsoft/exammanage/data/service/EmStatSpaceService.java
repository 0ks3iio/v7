package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmStatSpace;

import java.util.List;

public interface EmStatSpaceService extends BaseService<EmStatSpace, String> {

    public void deleteByStatObjectId(String... statObjectId);

    public List<EmStatSpace> saveAllEntitys(EmStatSpace... emStatSpace);

    public List<EmStatSpace> findByStatRangeIdIn(String[] rangeIds);

    public List<EmStatSpace> findBySpaceItemIdAndStatRangeIdIn(String spaceItemId, String[] rangeIds);

    public List<EmStatSpace> findByStatRangeIdAndSpaceItemIdIn(String statRangeId,
                                                               String[] spaceItemIds);

    public List<EmStatSpace> findByObjectIdAndIsConAndRangeIdIn(String statObjectId, String isCon, String[] statRangeIds);
}
