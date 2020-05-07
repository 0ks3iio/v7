package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.MultiReportDetail;

import java.util.List;

public interface MultiReportDetailService extends BaseService<MultiReportDetail, String>{
	/**
	 * 根据reportId获取明细list
	 * @param reportId
	 * @return
	 */
	public List<MultiReportDetail> findMultiReportDetailsByReportId(String reportId);
	
	/**
	 * 根据reportId 删除组合报告明细
	 * @param reportId
	 */
	public void deleteMultiReportDetailsByReportId(String reportId);
	
	/**
	 * 修改组合报告明细
	 * @param multiReportDetail
	 */
	public void updateMultiReportDetail(MultiReportDetail multiReportDetail);
	
	/**
	 * 获取最大排序号
	 * @param reportId
	 * @return
	 */
	 public Integer getMaxOrderIdByReportId(String reportId);

	String findReportIdById(String componentId);
}
