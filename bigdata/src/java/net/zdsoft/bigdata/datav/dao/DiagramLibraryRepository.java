package net.zdsoft.bigdata.datav.dao;

import net.zdsoft.bigdata.datav.entity.DiagramLibrary;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2018/12/6 下午1:31
 */
@Repository
public interface DiagramLibraryRepository extends BaseJpaRepositoryDao<DiagramLibrary, String> {

    List<DiagramLibrary> getDiagramLibrariesByUserId(String userId);

    int countByUserId(String userId);

    @Modifying
    @Query(
            value = "update DiagramLibrary set name=?1 where id = ?2"
    )
    void updateLibraryName(String name, String libraryId);
}
