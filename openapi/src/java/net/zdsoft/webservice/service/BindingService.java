package net.zdsoft.webservice.service;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
/**
 * cxf开发的webservice服务，暂时不用
 * @author user
 *
 */
@WebService(targetNamespace="http://binding.webservice.zdsoft.net")
//绑定为soap1.2，使用soap1.2协议
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public interface BindingService {
	@WebResult(name = "staffBindingReturn",partName="staffBindingReturn")
	public String staffBinding(@WebParam(name="staffBindReq")String message);

}