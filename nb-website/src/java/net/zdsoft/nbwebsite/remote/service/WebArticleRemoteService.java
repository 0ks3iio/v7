package net.zdsoft.nbwebsite.remote.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.nbwebsite.entity.WebArticle;

/**
 * @author yangsj  2017-2-13下午4:18:58
 */
public interface WebArticleRemoteService extends BaseRemoteService{

	/**
	 * @param unitIdsList
	 * @param string
	 * @return
	 */
	String findAllRelease(List<String> unitIdsList);
    
	/**
	 * @param commitUnitId
	 * @param string
	 * @return
	 */
	String findAdoptReleaseByType(String commitUnitId, String type);

	/**
	 * @param commitUnitId
	 * @param string
	 * @return
	 */
	String findClickReleaseByType(String commitUnitId, String type);

	/**
	 * @param commitUnitId
	 * @param string
	 * @return
	 */
	String findPersonAdoptReleaseByType(String userId, String type);

	/**
	 * @param commitUnitId
	 * @param string
	 * @return
	 */
	String findPersonReleaseByType(String userId, String type);

	/**
	 * @param commitUnitId
	 * @param string
	 * @return
	 */
	String findPersonClickReleaseByType(String userId, String type);

	/**
	 * @param commitUnitId
	 * @param string
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findAdoptReleaseByTypeDate(String commitUnitId, String type,
			Date startDate, Date endDate);

	/**
	 * @param commitUnitId
	 * @param string
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findReleaseByTypeDate(String commitUnitId, String string,
			Date startDate, Date endDate);

	/**
	 * @param commitUnitId
	 * @param string
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findClickReleaseByTypeDate(String commitUnitId, String string,
			Date startDate, Date endDate);

	/**
	 * @param userId
	 * @param string
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findPersonAdoptReleaseByTypeDate(String userId, String type,
			Date startDate, Date endDate);

	/**
	 * @param userId
	 * @param string
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findPersonReleaseByTypeDate(String userId, String type,
			Date startDate, Date endDate);

	/**
	 * @param userId
	 * @param string
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findPersonClickReleaseByTypeDate(String userId, String type,
			Date startDate, Date endDate);

	/**
	 * @param id
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
			String commitState, Date startDate, Date endDate,boolean isWorkbench, Pagination page);

	/**
	 * @param commitUnitId
	 * @param thisId
	 * @return
	 */
	String findAllReleaseByUnitIdType(String commitUnitId, String thisId);

	

}
