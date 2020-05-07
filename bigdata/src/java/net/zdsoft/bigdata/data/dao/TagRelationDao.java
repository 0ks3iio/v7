package net.zdsoft.bigdata.data.dao;

import java.util.List;

import net.zdsoft.bigdata.data.entity.TagRelation;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * Created by wangdongdong on 2018/5/15.
 */
public interface TagRelationDao extends BaseJpaRepositoryDao<TagRelation, String> {

    /**
     * 根据业务id查询
     * @param businessId（图标或报表id）
     * @return
     */
    List<TagRelation> getTagRelationsByBusinessId(String businessId);

    /**
     * 根据业务id删除
     * @param businessId
     */
    void deleteTagRelationByBusinessId(String businessId);

    void deleteByBusinessIdIn(String[] businessIds);

    /**
     * 根据标签id查询
     * @param tagId
     * @return
     */
    List<TagRelation> getTagRelationsByTagId(String tagId);

    List<TagRelation> findTagRelationsByTagIdIn(String[] tagIds);

    List<TagRelation> getAllByBusinessIdIn(String[] businessIds);

}
