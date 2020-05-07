package net.zdsoft.bigdata.metadata.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.metadata.entity.MetadataTag;

import java.util.List;

/**
 * Created by wangdongdong on 2019/1/7 11:41.
 */
public interface MetadataTagService extends BaseService<MetadataTag, String> {

    List<MetadataTag> findByMetadataId(String metadataId);

    void saveMetadataTag(String metadataId, String[] tagIds);

    List<MetadataTag> findByTagId(String tagId);
}
