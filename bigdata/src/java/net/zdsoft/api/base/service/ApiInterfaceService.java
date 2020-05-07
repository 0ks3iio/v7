package net.zdsoft.api.base.service;

import java.util.List;

import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;

public interface ApiInterfaceService extends BaseService<ApiInterface, String> {
    List<String> findInterfaceType();

    List<ApiInterface> findInterfaces(String type);

    ApiInterface findByUri(String uri);
    /**
     * 批量修改启用状态
     * @param status
     * @param ids
     */
    void updateUsingByIds(int status,String[] ids);
    /**
     * 单个修改启用状态
     * @param status
     * @param ids
     */
    void updateUsingById(int status,String id);

    /**
     * 获取用户拥有的权限接口
     * @param ticketKeyId
     * @return
     */
	List<ApiInterface> findInterfacesTkList(String ticketKeyId);
	
	
	public List<ApiInterface> getByType(String type);

    public List<ApiInterface> getByResultType(String type);
    
	
	public List<ApiInterface> findByTypeAndDataType(String type, Integer valueOf);

	
	public void updateInterfaceById(int isUsing, String interId);

	List<ApiInterface> getAllInterfaces(String typeName, Integer isUsing, Integer dataType,
			Pagination page);

	List<ApiInterface> findByIsUsing(int isUsing);

	void deleteByType(String type);

	void updatetTypeNameByType(String typeName, String type);

	void updatetTypeNameAndType(String typeName, String newType, String oldType);

	List<String> findDistinctTypeByDataType(Integer dataType);

	List<ApiInterface> findByTypAndDataType(String type,
			Integer valueOf);

	void updateResultType(String newType, String oldType);

	void deleteByResultType(String resultType);

	List<ApiInterface> findByUriIn(String[] uris);

	List<ApiInterface> findByDataType(Integer dataType);

	List<ApiInterface> findByResultTypeAndDataType(String resultType, Integer valueOf);

	List<ApiInterface> findByType(String type);
}
