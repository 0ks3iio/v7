package net.zdsoft.bigdata.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.TagRelation;

/**
 * Created by wangdongdong on 2018/5/15 9:35.
 */
public interface TagRelationService extends BaseService<TagRelation, String> {

    /**
     * 根据业务id查询
     * @param businessId
     * @return
     */
    List<TagRelation> findTagRelationByBusinessId(String businessId);

    List<TagRelation> getByTagIds(String[] tagIds);

    /**
     * 根据业务id删除
     * @param businessId
     */
    void deleteByBusinessId(String businessId);

    void deleteByBusinessId(String[] businessIds);

    /**
     * 根据标签id查询
     * @param tagId
     * @return
     */
    List<TagRelation> findByTagId(String tagId);

    List<TagRelation> getByBusinessId(String[] businessIds);

}
