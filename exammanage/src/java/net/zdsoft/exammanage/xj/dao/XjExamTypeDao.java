package net.zdsoft.exammanage.xj.dao;

import net.zdsoft.exammanage.xj.entity.XjexamType;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yangsj  2017年10月12日下午4:35:58
 */
public interface XjExamTypeDao extends BaseJpaRepositoryDao<XjexamType, String> {

    /**
     * @param typeKeys
     * @return
     */
    @Query("From XjexamType  Where typeKey in ?1 ")
    List<XjexamType> findByTypeKeys(String[] typeKeys);

}
