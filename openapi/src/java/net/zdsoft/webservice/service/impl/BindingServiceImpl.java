package net.zdsoft.webservice.service.impl;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

import net.zdsoft.webservice.service.BindingService;
import net.zdsoft.webservice.service.SuCdStaffBindService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("bindingService")
@WebService(targetNamespace="http://binding.webservice.zdsoft.net")
//绑定为soap1.2，使用soap1.2协议
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class BindingServiceImpl implements BindingService {
	@Autowired
	private SuCdStaffBindService suCdStaffBindService;
	@WebResult(name = "staffBindingReturn",partName="staffBindingReturn")
	public String staffBinding(@WebParam(name="staffBindReq")String message) {
		return  suCdStaffBindService.dealBindMsg(message);
	}
	
}
