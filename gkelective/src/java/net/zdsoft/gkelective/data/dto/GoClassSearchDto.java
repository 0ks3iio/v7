package net.zdsoft.gkelective.data.dto;
/**
 * @author yuzy
 * @version 创建时间：2017-6-8 上午11:48:46
 * 
 */
public class GoClassSearchDto {
	
	public String searchViewTypeRedio;
	public String searchGkType;
	public String searchBatch;
	public String searchCourseId;
	
	public String searchArrange;//默认全部   0：未安排--方案那边筛选
	public String searchTeacherId;
	
	public String getSearchViewTypeRedio() {
		return searchViewTypeRedio;
	}
	public void setSearchViewTypeRedio(String searchViewTypeRedio) {
		this.searchViewTypeRedio = searchViewTypeRedio;
	}
	public String getSearchGkType() {
		return searchGkType;
	}
	public void setSearchGkType(String searchGkType) {
		this.searchGkType = searchGkType;
	}
	public String getSearchBatch() {
		return searchBatch;
	}
	public void setSearchBatch(String searchBatch) {
		this.searchBatch = searchBatch;
	}
	public String getSearchCourseId() {
		return searchCourseId;
	}
	public void setSearchCourseId(String searchCourseId) {
		this.searchCourseId = searchCourseId;
	}
	public String getSearchArrange() {
		return searchArrange;
	}
	public void setSearchArrange(String searchArrange) {
		this.searchArrange = searchArrange;
	}
	public String getSearchTeacherId() {
		return searchTeacherId;
	}
	public void setSearchTeacherId(String searchTeacherId) {
		this.searchTeacherId = searchTeacherId;
	}
	
}
