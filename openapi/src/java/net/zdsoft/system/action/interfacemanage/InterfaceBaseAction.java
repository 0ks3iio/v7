package net.zdsoft.system.action.interfacemanage;

import java.util.List;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.entity.OpenApiApply;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceType;
import net.zdsoft.remote.openapi.enums.ApplyStatusEnum;
import net.zdsoft.remote.openapi.remote.service.OpenApiApplyRemoteService;
import net.zdsoft.remote.openapi.remote.service.OpenApiInterfaceTypeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class InterfaceBaseAction extends BaseAction{

	@Autowired
	protected OpenApiApplyRemoteService openApiApplyRemoteService;
	@Autowired
	protected OpenApiInterfaceTypeRemoteService openApiInterfaceTypeRemoteService;
	
	/**
     * 是否存在正在使用和待审核的接口
     * @param openApiInterface
     * @return
     */
	protected boolean isNotExistOpenApiApply(String type) {
		int[] status = {ApplyStatusEnum.PASS_VERIFY.getValue(),ApplyStatusEnum.IN_VERIFY.getValue()};
		List<OpenApiApply> applys = SUtils.dt(openApiApplyRemoteService.findByTypeAndStatusIn(type,status), OpenApiApply.class);
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
		List<OpenApiInterfaceType>  openApiInterfaceTypes = SUtils.dt(openApiInterfaceTypeRemoteService.findByClassifyIn(types), 
				OpenApiInterfaceType.class);
		return openApiInterfaceTypes;
	} 
}
