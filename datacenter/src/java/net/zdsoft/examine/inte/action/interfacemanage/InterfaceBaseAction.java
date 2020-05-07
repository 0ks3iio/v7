package net.zdsoft.examine.inte.action.interfacemanage;

import java.util.List;

import net.zdsoft.base.entity.eis.Developer;
import net.zdsoft.base.entity.eis.OpenApiApply;
import net.zdsoft.base.entity.eis.OpenApiInterfaceType;
import net.zdsoft.base.enums.ApplyStatusEnum;
import net.zdsoft.base.service.OpenApiApplyService;
import net.zdsoft.base.service.OpenApiInterfaceTypeService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.openapi.remote.openapi.constant.OpenApiConstants;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class InterfaceBaseAction extends BaseAction{

	@Autowired
	protected OpenApiApplyService openApiApplyService;
	@Autowired
	protected OpenApiInterfaceTypeService openApiInterfaceTypeService;
	
	/**
     * 是否存在正在使用和待审核的接口
     * @param openApiInterface
     * @return
     */
	protected boolean isNotExistOpenApiApply(String type) {
		int[] status = {ApplyStatusEnum.PASS_VERIFY.getValue(),ApplyStatusEnum.IN_VERIFY.getValue()};
		List<OpenApiApply> applys = openApiApplyService.findByTypeAndStatusIn(type,status);
		return CollectionUtils.isEmpty(applys);
	}
	
	/**
	 * 是否是接口类型
	 * @param classify
	 * @return
	 */
	protected boolean isInterfaceType(Integer classify) {
		return classify == OpenApiInterfaceType.INTERFACE_TYPE || classify == OpenApiInterfaceType.PUBLIC_TYPE ;
	}
	
	/**
	 * 是否是结果类型
	 * @param classify
	 * @return
	 */
	protected boolean isResultType(Integer classify) {
		return classify == OpenApiInterfaceType.RESULT_TYPE || classify == OpenApiInterfaceType.PUBLIC_TYPE ;
	}
	
	protected List<OpenApiInterfaceType> getResultTypes() {
		return getAllTypes(OpenApiInterfaceType.RESULT_TYPE);
	}
	
	protected List<OpenApiInterfaceType> getInterfaceTypes() {
		return getAllTypes(OpenApiInterfaceType.INTERFACE_TYPE);
    }
	/**
	 * type = 1: 得到接口类型
	 * type = 2： 得到结果类型
	 * @param type
	 */
	private List<OpenApiInterfaceType> getAllTypes(int type) {
		Integer[] types = {type,OpenApiInterfaceType.PUBLIC_TYPE};
		List<OpenApiInterfaceType>  openApiInterfaceTypes = openApiInterfaceTypeService.findByClassifyIn(types);
		return openApiInterfaceTypes;
	} 
	
	/**
	 * 从session 中获取开发者信息
	 * @return
	 */
    protected Developer getDeveloper() {
        return (Developer) getRequest().getSession().getAttribute(OpenApiConstants.DEVELOPER_SESSION);
    }
}
