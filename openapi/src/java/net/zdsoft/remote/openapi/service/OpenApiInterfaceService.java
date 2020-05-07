package net.zdsoft.remote.openapi.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.remote.openapi.entity.OpenApiInterface;

public interface OpenApiInterfaceService extends BaseService<OpenApiInterface, String> {
    List<String> findInterfaceType();

    List<OpenApiInterface> findInterfaces(String type);

    OpenApiInterface findByUri(String uri);
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
	List<OpenApiInterface> findInterfacesTkList(String ticketKeyId);
	
	
	public List<OpenApiInterface> getByType(String type);

    public List<OpenApiInterface> getByResultType(String type);
    
    public List<String[]> findDistinctType();

	/**
	 * @param dataType
	 * @return
	 */
	public List<String[]> findDistinctTypeByDataType(Integer dataType);

	
	public List<OpenApiInterface> findByTypeAndDataType(String type, Integer valueOf);

	
	public void updateInterfaceById(int isUsing, String interId);

	List<OpenApiInterface> getAllInterfaces(String typeName, Integer isUsing, Integer dataType,
			Pagination page);

	List<OpenApiInterface> findByIsUsing(int isUsing);

	void deleteByType(String type);

	void updatetTypeNameByType(String typeName, String type);

	void updatetTypeNameAndType(String typeName, String newType, String oldType);

	List<String> findDistinctResultTypeByDataType(Integer dataType);

	List<OpenApiInterface> findByResultTypAndDataType(String type,
			Integer valueOf);

	void updateResultType(String newType, String oldType);

	void deleteByResultType(String resultType);

	List<OpenApiInterface> findByUriIn(String[] uris);
}
