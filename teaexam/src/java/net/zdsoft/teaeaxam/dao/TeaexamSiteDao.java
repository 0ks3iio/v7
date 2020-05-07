package net.zdsoft.teaeaxam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teaeaxam.entity.TeaexamSite;

public interface TeaexamSiteDao extends BaseJpaRepositoryDao<TeaexamSite, String>{
    @Query("From TeaexamSite where unionCode like ?1||'%'")
	public List<TeaexamSite> findByUnionCode(String unionCode);
    
    @Query("FROM TeaexamSite where schoolId IN ?1")
    public List<TeaexamSite> findBySchoolIds(String[] schoolIds);
}
