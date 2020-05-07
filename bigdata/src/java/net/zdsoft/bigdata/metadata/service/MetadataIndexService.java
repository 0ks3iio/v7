package net.zdsoft.bigdata.metadata.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.metadata.entity.MetadataIndex;

import java.util.List;

/**
 * Created by wangdongdong on 2019/1/7 11:41.
 */
public interface MetadataIndexService extends BaseService<MetadataIndex, String> {

    List<MetadataIndex> findByMetadataId(String metadataId);

    void saveMetadataIndex(MetadataIndex metadataIndex) throws BigDataBusinessException;
}
