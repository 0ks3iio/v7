package net.zdsoft.bigdata.metadata.dao;

import net.zdsoft.bigdata.metadata.entity.Folder;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderDao extends BaseJpaRepositoryDao<Folder, String> {

    List<Folder> findAllByParentIdAndStatusOrderByOrderId(String parentId, Integer status);

    List<Folder> findAllByParentIdAndFolderTypeAndStatusOrderByOrderId(String parentId, Integer folderType, Integer status);

    List<Folder> findAllByUsage(Integer usage);

    @Query(value = "select max(ORDER_ID) from BG_FOLDER where PARENT_ID = :parentId",
            nativeQuery = true)
    Integer getMaxOrderIdByParentId(@Param("parentId")String parentId);
}
