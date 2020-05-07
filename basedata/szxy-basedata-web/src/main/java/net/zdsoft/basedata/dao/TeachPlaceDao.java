/* 
 * @(#)TeachPlaceDao.java    Created on 2017-3-2
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeachPlaceDao extends BaseJpaRepositoryDao<TeachPlace, String> {
	@Query("select id,placeName From TeachPlace Where id in (?1) ")
	public List<Object[]> findPartPlaByIds(String[] ids);
	
	public void deleteByIdIn(String[] ids);

	
	public List<TeachPlace> findByUnitIdIn(String[] unitIds);
}
