package net.zdsoft.studevelop.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateOptions;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by luf on 2018/12/14.
 */
public interface StudevelopTemplateOptionsDao extends BaseJpaRepositoryDao<StudevelopTemplateOptions ,String> {

    @Query(" From StudevelopTemplateOptions where templateItemId in ?1  ")
    public List<StudevelopTemplateOptions> getOptionsListByTemplateItemId(String[] itemIds);
}
