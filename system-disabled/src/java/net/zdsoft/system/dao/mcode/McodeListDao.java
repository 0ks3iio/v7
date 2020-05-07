package net.zdsoft.system.dao.mcode;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.mcode.McodeList;

public interface McodeListDao extends BaseJpaRepositoryDao<McodeList, String> {

    @Query("from McodeList where isUsing = 1 and subsystem = ?1")
    List<McodeList> findBySubsystem(int subsystem);

    @Query("from McodeList where isUsing = 1 and subsystem = ?1 and maintain = ?2")
    List<McodeList> findBySubsystemAndMaintain(int subsystem, int maintain);

}
