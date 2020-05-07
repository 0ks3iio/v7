package net.zdsoft.system.dao.mcode;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.mcode.McodeDetail;

public interface McodeDetailDao extends BaseJpaRepositoryDao<McodeDetail, String> {

    @Query("from McodeDetail where mcodeId in ?1 and isUsing = 1 order by mcodeId, displayOrder")
    List<McodeDetail> findByMcodeId(String... mcodeId);

    @Query("from McodeDetail where mcodeId = ?1 and isUsing = 1 and mcodeContent like ?2 order by displayOrder")
    List<McodeDetail> findByMcodeContentLike(String mcodeId, String content);

    McodeDetail findByMcodeIdAndThisId(String mcodeId, String thisId);

    @Query("from McodeDetail where mcodeId in ?1 order by mcodeId, displayOrder")
    List<McodeDetail> findAllByMcodeIds(String... mcodeIds);

    @Modifying
    @Query("update McodeDetail set isUsing = ?1 where id = ?2")
    void updateIsUsingById(int isUsing, String id);
}
