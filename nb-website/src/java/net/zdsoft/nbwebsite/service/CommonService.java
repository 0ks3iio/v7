/*
* Project: v7
* Author : shenke
* @(#) CommonService.java Created on 2016-10-11
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.nbwebsite.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.nbwebsite.constant.WebsiteConstants;
import net.zdsoft.nbwebsite.entity.WebArticle;

/**
 * @description: 
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-11上午11:05:02
 */
public interface CommonService extends BaseService<WebArticle, String>{

	/**
	 * 一般性列表查询（用于后台管理）
	 * @param unitId 单位Id(创建单位)
	 * @param type	文章类型  {@link WebsiteConstants.Type} 参看
	 * @param title	标题
	 * @param commitStartDate 提交时间区间
	 * @param commitEndDate 提交时间区间
	 * @return
	 */
	List<WebArticle> getCommonList(String unitId,String type, String title,Date commitStartDate,Date commitEndDate,String commitState,boolean isManager,String createUserId,Pagination page,boolean isCancel);
	
	/**
	 * 根据文章类型获取数据
	 * @param type 参见 {@link WebsiteConstants.Type}
	 * @param page 分页参数,可以为空(查询全部数据)
	 * @return List 当查询不到数据时返回空
	 */
	List<WebArticle> getArticleByType(String type,Pagination page,boolean isCancel);
	
	/**
	 * 根据单位Id和文章类型获取数据
	 * @param unitId 指定单位Id,可以为空(取全部单位的数据)
	 * @param type	文章类型 参见{@link WebsiteConstants.Type}
	 * @param page 分页参数，可以为空 取全部数据
	 * @return List
	 */
	List<WebArticle> getArticleByUnitIdAndType(String unitId, String type, Pagination page,boolean isCancel);

	/**
	 * 根据单位Id和文章类型获取数据
	 * @param unitIds 指定单位Ids,可以为空(取全部单位的数据)
	 * @param type	文章类型 参见{@link WebsiteConstants.Type}
	 * @param page 分页参数，可以为空 取全部数据
	 * @return List
	 */
	List<WebArticle> getArticleByUnitIdsAndType(String[] unitIds, String type, Pagination page,boolean isCancel);
	
	/**
	 * 根据文章类型获取置顶的数据
	 * @param type 文章类型 参见{@link WebsiteConstants.Type}
	 * @return List
	 */
	List<WebArticle> getArticleOfTopByType(String type,Pagination page,boolean isCancel);

	List<WebArticle> getCommonList(String unitId,String type, String title,Date commitStartDate,Date commitEndDate,String commitState,boolean isManager,String createUserId,Pagination page,boolean isWorkbench,boolean isCancel);
	/**
	 * 点击次数+1
	 * @param id
	 * @return
	 */
	Integer addClickNumber(String id);

	List<WebArticle> saveAllEntitys(WebArticle... webArticles);
	
}
