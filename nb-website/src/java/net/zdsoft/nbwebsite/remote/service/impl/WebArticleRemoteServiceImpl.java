package net.zdsoft.nbwebsite.remote.service.impl;




import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.nbwebsite.entity.WebArticle;
import net.zdsoft.nbwebsite.remote.service.WebArticleRemoteService;
import net.zdsoft.nbwebsite.service.WebArticleService;

/**
 * @author yangsj  2017-2-13下午4:20:42
 */
@Service("webArticleRemoteService")
public class WebArticleRemoteServiceImpl extends BaseRemoteServiceImpl<WebArticle> implements WebArticleRemoteService {
    
	@Autowired
	private WebArticleService webArticleService;
	
	@Override
	protected BaseService<WebArticle, String> getBaseService() {
		// TODO Auto-generated method stub
		return webArticleService;
	}

	@Override
	public String findAllRelease(List<String> unitIdsList) {
		// TODO Auto-generated method stub
		return webArticleService.findAllRelease(unitIdsList);
	}

	@Override
	public String findAdoptReleaseByType(String commitUnitId, String type) {
		// TODO Auto-generated method stub
		return webArticleService.findAdoptReleaseByType(commitUnitId,type);
	}

	@Override
	public String findClickReleaseByType(String commitUnitId, String type) {
		// TODO Auto-generated method stub
		return webArticleService.findClickReleaseByType(commitUnitId,type);
	}

	@Override
	public String findPersonAdoptReleaseByType(String userId,
			String type) {
		// TODO Auto-generated method stub
		return webArticleService.findPersonAdoptReleaseByType(userId,type);
	}

	@Override
	public String findPersonReleaseByType(String userId, String type) {
		// TODO Auto-generated method stub
		return webArticleService.findPersonReleaseByType(userId,type);
	}

	@Override
	public String findPersonClickReleaseByType(String userId,
			String type) {
		// TODO Auto-generated method stub
		return webArticleService.findPersonClickReleaseByType(userId,type);
	}

	@Override
	public String findAdoptReleaseByTypeDate(String commitUnitId, String type,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleService.findAdoptReleaseByTypeDate(commitUnitId,type,startDate,endDate);
	}

	@Override
	public String findReleaseByTypeDate(String commitUnitId, String type,Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleService.findReleaseByTypeDate(commitUnitId,type,startDate,endDate);
	}

	@Override
	public String findClickReleaseByTypeDate(String commitUnitId, String type,Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleService.findClickReleaseByTypeDate(commitUnitId,type,startDate,endDate);
	}

	@Override
	public String findPersonAdoptReleaseByTypeDate(String userId,
			String type, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleService.findPersonAdoptReleaseByTypeDate(userId,type,startDate,endDate);
	}

	@Override
	public String findPersonReleaseByTypeDate(String userId, String type,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleService.findPersonReleaseByTypeDate(userId,type,startDate,endDate);
	}

	@Override
	public String findPersonClickReleaseByTypeDate(String userId,
			String type, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleService.findPersonClickReleaseByTypeDate(userId,type,startDate,endDate);
	}

	@Override
	public String findPersonReleaseByCommitId(String commitId) {
		// TODO Auto-generated method stub
		return webArticleService.findPersonReleaseByCommitId(commitId);
	}

	@Override
	public String findAdoptReleaseByUnitId(String commitUnitId) {
		// TODO Auto-generated method stub
		return webArticleService.findAdoptReleaseByUnitId(commitUnitId);
	}

	@Override
	public String findAllReleaseByUnitId(String commitUnitId) {
		// TODO Auto-generated method stub
		return webArticleService.findAllReleaseByUnitId(commitUnitId);
	}

	@Override
	public String findClickReleaseByUnitId(String commitUnitId) {
		// TODO Auto-generated method stub
		return webArticleService.findClickReleaseByUnitId(commitUnitId);
	}

	@Override
	public String findReleaseByUnitIdDate(String commitUnitId, Date startDate,
			Date endDate) {
		// TODO Auto-generated method stub
		return webArticleService.findReleaseByUnitIdDate(commitUnitId,startDate,endDate);
	}

	@Override
	public String findAdoptReleaseByUnitIdDate(String commitUnitId,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleService.findAdoptReleaseByUnitIdDate(commitUnitId,startDate,endDate);
	}

	@Override
	public String findClickReleaseByUnitIdDate(String commitUnitId,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return webArticleService.findClickReleaseByUnitIdDate(commitUnitId,startDate,endDate);
	}

	@Override
	public String findAllRelease(String commitUnitId) {
		// TODO Auto-generated method stub
		return webArticleService.findAllRelease(commitUnitId);
	}

	@Override
	public List<WebArticle> findArticles(String createUserId,String title, String type, String auditUserId,
			String commitState, Date startDate, Date endDate,boolean isWorkbench, Pagination page) {
		// TODO Auto-generated method stub
		return webArticleService.findArticles(createUserId,title,type,auditUserId,commitState,startDate,endDate,isWorkbench,page);
	}

	@Override
	public String findAllReleaseByUnitIdType(String commitUnitId, String type) {
		// TODO Auto-generated method stub
		return webArticleService.findAllReleaseByUnitIdType(commitUnitId,type);
	}

	

}
