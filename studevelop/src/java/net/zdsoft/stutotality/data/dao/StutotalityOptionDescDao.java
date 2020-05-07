package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalityOptionDesc;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * stutotality_option_desc 
 * @author 
 * 
 */
public interface StutotalityOptionDescDao extends BaseJpaRepositoryDao<StutotalityOptionDesc, String>{
    @Query("from StutotalityOptionDesc where optionId in (?1)")
    public List<StutotalityOptionDesc> findListByOptionIds (String[] optionIds);
}
