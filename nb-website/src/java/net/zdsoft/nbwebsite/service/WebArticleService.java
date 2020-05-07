package net.zdsoft.nbwebsite.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.nbwebsite.entity.WebArticle;

/**
 * @author yangsj  2017-2-13下午4:09:18
 */
public interface WebArticleService extends BaseService<WebArticle, String>{

	/**
	 * @param unitIdsList
	 * @param string
	 * @return
	 */
	String findAllRelease(List<String> unitIdsList);

	/**
	 * @param commitUnitId
	 * @param type
	 * @return
	 */
	String findAdoptReleaseByType(String commitUnitId, String type);

	/**
	 * @param commitUnitId
	 * @param type
	 * @return
	 */
	String findClickReleaseByType(String commitUnitId, String type);

	/**
	 * @param commitUnitId
	 * @param type
	 * @return
	 */
	String findPersonAdoptReleaseByType(String userId, String type);

	/**
	 * @param commitUnitId
	 * @param type
	 * @return
	 */
	String findPersonReleaseByType(String userId, String type);

	/**
	 * @param commitUnitId
	 * @param type
	 * @return
	 */
	String findPersonClickReleaseByType(String userId, String type);

	/**
	 * @param commitUnitId
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findAdoptReleaseByTypeDate(String commitUnitId, String type,
			Date startDate, Date endDate);

	/**
	 * @param commitUnitId
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findReleaseByTypeDate(String commitUnitId, String type,
			Date startDate, Date endDate);

	/**
	 * @param commitUnitId
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findClickReleaseByTypeDate(String commitUnitId, String type,
			Date startDate, Date endDate);

	/**
	 * @param userId
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findPersonAdoptReleaseByTypeDate(String userId, String type,
			Date startDate, Date endDate);

	/**
	 * @param userId
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findPersonReleaseByTypeDate(String userId, String type,
			Date startDate, Date endDate);

	/**
	 * @param userId
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findPersonClickReleaseByTypeDate(String userId, String type,
			Date startDate, Date endDate);

	/**
	 * @param commitId
	 * @return
	 */
	String findPersonReleaseByCommitId(String commitId);

	/**
	 * @param commitUnitId
	 * @return
	 */
	String findAdoptReleaseByUnitId(String commitUnitId);

	/**
	 * @param commitUnitId
	 * @return
	 */
	String findAllReleaseByUnitId(String commitUnitId);

	/**
	 * @param commitUnitId
	 * @return
	 */
	String findClickReleaseByUnitId(String commitUnitId);

	/**
	 * @param commitUnitId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findReleaseByUnitIdDate(String commitUnitId, Date startDate,
			Date endDate);

	/**
	 * @param commitUnitId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findAdoptReleaseByUnitIdDate(String commitUnitId, Date startDate,
			Date endDate);

	/**
	 * @param commitUnitId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findClickReleaseByUnitIdDate(String commitUnitId, Date startDate,
			Date endDate);

	/**
	 * @param commitUnitId
	 * @return
	 */
	String findAllRelease(String commitUnitId);

	/**
	 * @param title
	 * @param type
	 * @param auditUserId
	 * @param commitState
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @return
	 */
	List<WebArticle> findArticles(String createUserId,String title, String type, String auditUserId,
			String commitState, Date startDate, Date endDate, boolean isWorkbench, Pagination page);

	/**
	 * @param commitUnitId
	 * @param type
	 * @return
	 */
	String findAllReleaseByUnitIdType(String commitUnitId, String type);

	/**
	 * @param unitIdsList
	 */
	String findAllAdoptRelease(List<String> unitIdsList);

	/**
	 * @param unitIdsList
	 */
	String findAllClickRelease(List<String> unitIdsList);

	/**
	 * @param unitIdsList
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findAllAdoptReleaseByDate(List<String> unitIdsList, Date startDate,
			Date endDate);

	/**
	 * @param unitIdsList
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findAllReleaseByDate(List<String> unitIdsList, Date startDate,
			Date endDate);

	/**
	 * @param unitIdsList
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findAllClickReleaseByDate(List<String> unitIdsList, Date startDate,
			Date endDate);
	

}
