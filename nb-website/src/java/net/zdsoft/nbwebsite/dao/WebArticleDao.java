/*
* Project: v7
* Author : shenke
* @(#) WebArticleDao.java Created on 2016-10-11
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.nbwebsite.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.nbwebsite.entity.WebArticle;

/**
 * @description: 
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-11上午11:07:34
 */
@Repository
public interface WebArticleDao extends BaseJpaRepositoryDao<WebArticle, String>{
    
	public static final String SQL_AFTER=" and commitState != 0 and isDeleted= 0";
	
	public static final String SQL_AFTERDATE=" and commitTime>=?3 and commitTime <=?4";
	/**
	 * @param commitUnitId
	 * @param type
	 * @return
	 */
	@Query("select count(w)  From WebArticle as w  Where commitUnitId in (?1) "+SQL_AFTER)
	String findAllRelease(List<String> unitIdsList);

	/**
	 * @param commitUnitId
	 * @param type
	 * @return
	 */
	@Query("select count(w) From WebArticle as w  Where commitUnitId = ?1 and type = ?2 and commitState =2 and isDeleted= 0")
	String findAdoptReleaseByType(String commitUnitId, String type);

	/**
	 * @param commitUnitId
	 * @param type
	 * @return
	 */
	@Query("select w From WebArticle as w  Where commitUnitId = ?1 and type = ?2"+SQL_AFTER)
	List<WebArticle> findClickReleaseByType(String commitUnitId, String type);

	/**
	 * @param commitUnitId
	 * @param type
	 * @return
	 */
	@Query("select count(w) From WebArticle as w  Where commitUserId = ?1 and type = ?2 and commitState =2 and isDeleted= 0")
	String findPersonAdoptReleaseByType(String userId, String type);

	/**
	 * @param userId
	 * @param type
	 * @return
	 */
	@Query("select count(w)  From WebArticle as w  Where commitUserId = ?1 and type = ?2"+SQL_AFTER)
	String findPersonReleaseByType(String userId, String type);

	/**
	 * @param userId
	 * @param type
	 * @return
	 */
	@Query("select w From WebArticle as w  Where commitUserId = ?1 and type = ?2"+SQL_AFTER)
	List<WebArticle> findPersonClickReleaseByType(String userId, String type);

	/**
	 * @param commitUnitId
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("select count(w) From WebArticle as w  Where commitUnitId = ?1 and type = ?2 and commitState =2 and isDeleted= 0 and commitTime>=?3 and commitTime <=?4")
	String findAdoptReleaseByTypeDate(String commitUnitId, String type,
			Date startDate, Date endDate);

	/**
	 * @param commitUnitId
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("select count(w)  From WebArticle as w  Where commitUnitId = ?1 and type = ?2 and commitTime>=?3 and commitTime <=?4"+SQL_AFTER)
	String findReleaseByTypeDate(String commitUnitId, String type,
			Date startDate, Date endDate);

	/**
	 * @param commitUnitId
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("select w From WebArticle as w  Where commitUnitId = ?1 and type = ?2 and commitTime>=?3 and commitTime <=?4 "+SQL_AFTER)
	List<WebArticle> findClickReleaseByTypeDate(String commitUnitId,
			String type, Date startDate, Date endDate);

	/**
	 * @param userId
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("select count(w) From WebArticle as w  Where commitUserId = ?1 and type = ?2 and commitState =2 and isDeleted= 0 and commitTime>=?3 and commitTime <=?4")
	String findPersonAdoptReleaseByTypeDate(String userId, String type,
			Date startDate, Date endDate);

	/**
	 * @param userId
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("select count(w)  From WebArticle as w  Where commitUserId = ?1 and type = ?2"+SQL_AFTER+SQL_AFTERDATE)
	String findPersonReleaseByTypeDate(String userId, String type,
			Date startDate, Date endDate);

	/**
	 * @param userId
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("select w From WebArticle as w  Where commitUserId = ?1 and type = ?2"+SQL_AFTER+SQL_AFTERDATE)
	List<WebArticle> findPersonClickReleaseByTypeDate(String userId,
			String type, Date startDate, Date endDate);

	/**
	 * @param commitId
	 * @return
	 */
	@Query("select count(w)  From WebArticle as w  Where commitUserId = ?1 "+SQL_AFTER)
	String findPersonReleaseByCommitId(String commitId);

	/**
	 * @param commitUnitId
	 * @return
	 */
	@Query("select count(w) From WebArticle as w  Where commitUnitId = ?1  and commitState =2 and isDeleted= 0")
	String findAdoptReleaseByUnitId(String commitUnitId);

	/**
	 * @param commitUnitId
	 * @return
	 */
	@Query("select count(w)  From WebArticle as w  Where commitUnitId = ?1 "+SQL_AFTER)
	String findAllReleaseByUnitId(String commitUnitId);

	/**
	 * @param commitUnitId
	 * @return
	 */
	@Query("select w From WebArticle as w  Where commitUnitId = ?1 "+SQL_AFTER)
	List<WebArticle> findClickReleaseByUnitId(String commitUnitId);

	/**
	 * @param commitUnitId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("select count(w)  From WebArticle as w  Where commitUnitId = ?1  and commitTime>=?2 and commitTime <=?3"+SQL_AFTER)
	String findReleaseByUnitIdDate(String commitUnitId, Date startDate,
			Date endDate);

	/**
	 * @param commitUnitId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("select count(w) From WebArticle as w  Where commitUnitId = ?1  and commitState =2 and isDeleted= 0 and commitTime>=?2 and commitTime <=?3")
	String findAdoptReleaseByUnitIdDate(String commitUnitId, Date startDate,
			Date endDate);

	/**
	 * @param commitUnitId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("select w From WebArticle as w  Where commitUnitId = ?1  and commitTime>=?2 and commitTime <=?3 "+SQL_AFTER)
	List<WebArticle> findClickReleaseByUnitIdDate(String commitUnitId,
			Date startDate, Date endDate);

	/**
	 * @param commitUnitId
	 * @return
	 */
	@Query("select count(w)  From WebArticle as w  Where commitUnitId = ?1 "+SQL_AFTER)
	String findAllRelease(String commitUnitId);

	/**
	 * @param commitUnitId
	 * @param type
	 * @return
	 */
	@Query("select count(w) From WebArticle as w  Where commitUnitId = ?1 and type = ?2"+SQL_AFTER)
	String findAllReleaseByUnitIdType(String commitUnitId, String type);

	/**
	 * @param unitIdsList
	 * @return
	 */
	@Query("select count(w) From WebArticle as w  Where commitUnitId in (?1) and commitState =2 and isDeleted= 0")
	String findAllAdoptRelease(List<String> unitIdsList);

	/**
	 * @param unitIdsList
	 * @return
	 */
	@Query("select w From WebArticle as w  Where commitUnitId in (?1) "+SQL_AFTER)
	List<WebArticle> findAllClickRelease(List<String> unitIdsList);

	/**
	 * @param unitIdsList
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("select count(w) From WebArticle as w  Where commitUnitId in (?1)  and commitState =2 and isDeleted= 0 and commitTime>=?2 and commitTime <=?3")
	String findAllAdoptReleaseByDate(List<String> unitIdsList, Date startDate,
			Date endDate);

	/**
	 * @param unitIdsList
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("select count(w)  From WebArticle as w  Where commitUnitId in (?1)  and commitTime>=?2 and commitTime <=?3"+SQL_AFTER)
	String findAllReleaseByDate(List<String> unitIdsList, Date startDate,
			Date endDate);

	/**
	 * @param unitIdsList
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("select w From WebArticle as w  Where commitUnitId in (?1)  and commitTime>=?2 and commitTime <=?3 "+SQL_AFTER)
	List<WebArticle> findAllClickReleaseByDate(List<String> unitIdsList,
			Date startDate, Date endDate);

    
	
	
	
}
