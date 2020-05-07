package net.zdsoft.api.examine.inte.action;

import java.util.List;

import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.entity.eis.ApiApply;
import net.zdsoft.api.base.entity.eis.ApiInterfaceType;
import net.zdsoft.api.base.enums.ApplyStatusEnum;
import net.zdsoft.api.base.service.ApiApplyService;
import net.zdsoft.api.base.service.ApiInterfaceTypeService;
import net.zdsoft.api.openapi.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.framework.action.BaseAction;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class InterBaseAction extends BaseAction{

	@Autowired
	protected ApiApplyService openApiApplyService;
	@Autowired
	protected ApiInterfaceTypeService openApiInterfaceTypeService;
	
	
	protected boolean isNotExistApplyByType(String type) {
		int[] status = {ApplyStatusEnum.PASS_VERIFY.getValue(),ApplyStatusEnum.IN_VERIFY.getValue()};
		List<ApiApply> applys = openApiApplyService.findByTypeAndStatusIn(type, status);
		return CollectionUtils.isEmpty(applys);
	}
	
	/**
     * 是否存在正在使用和待审核的接口
     * @param openApiInterface
     * @return
     */
	protected boolean isNotExistOpenApiApply(String interfaceId) {
		int[] status = {ApplyStatusEnum.PASS_VERIFY.getValue(),ApplyStatusEnum.IN_VERIFY.getValue()};
		List<ApiApply> applys = openApiApplyService.findByInterfaceIdAndStatusIn(interfaceId,status);
		return CollectionUtils.isEmpty(applys);
	}
	
	/**
	 * 是否是接口类型
	 * @param classify
	 * @return
	 */
	protected boolean isInterfaceType(Integer classify) {
		return classify == ApiInterfaceType.INTERFACE_TYPE || classify == ApiInterfaceType.PUBLIC_TYPE ;
	}
	
	/**
	 * 是否是结果类型
	 * @param classify
	 * @return
	 */
	protected boolean isResultType(Integer classify) {
		return classify == ApiInterfaceType.RESULT_TYPE || classify == ApiInterfaceType.PUBLIC_TYPE ;
	}
	
	protected List<ApiInterfaceType> getResultTypes() {
		return getAllTypes(ApiInterfaceType.RESULT_TYPE);
	}
	
	protected List<ApiInterfaceType> getInterfaceTypes() {
		return getAllTypes(ApiInterfaceType.INTERFACE_TYPE);
    }
	/**
	 * type = 1: 得到接口类型
	 * type = 2： 得到结果类型
	 * @param type
	 */
	private List<ApiInterfaceType> getAllTypes(int type) {
		Integer[] types = {type,ApiInterfaceType.PUBLIC_TYPE};
		List<ApiInterfaceType>  openApiInterfaceTypes = openApiInterfaceTypeService.findByClassifyIn(types);
		return openApiInterfaceTypes;
	} 
	
	/**
	 * 从session 中获取开发者信息
	 * @return
	 */
    protected ApiDeveloper getDeveloper() {
        return (ApiDeveloper) getRequest().getSession().getAttribute(OpenApiConstants.DEVELOPER_SESSION);
    }
}
