package net.zdsoft.teaeaxam.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.teaeaxam.entity.TeaexamSite;

public interface TeaexamSiteService extends BaseService<TeaexamSite, String>{

	List<TeaexamSite> findByUnionCode(String unionCode, Pagination page);
	
	List<TeaexamSite> findBySchoolIds(String[] schoolIds);
}
