/**
 * IfAPServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package net.zdsoft.webservice.bind;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.webservice.service.SuCdStaffBindService;

public class IfAPServiceSoapBindingImpl implements net.zdsoft.webservice.bind.APService{
	SuCdStaffBindService suCdStaffBindService = Evn.getBean("suCdStaffBindService");
    public java.lang.String deptBinding(java.lang.String deptBindReq) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String corpBinding(java.lang.String corpBindReq) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String staffBinding(java.lang.String staffBindReq) throws java.rmi.RemoteException {
    	return  suCdStaffBindService.dealBindMsg(staffBindReq);
    }

}
