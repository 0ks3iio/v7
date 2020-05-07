/**
 * APServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package net.zdsoft.webservice.bind;

public interface APServiceService extends javax.xml.rpc.Service {
    public java.lang.String getIfAPServiceAddress();

    public net.zdsoft.webservice.bind.APService getIfAPService() throws javax.xml.rpc.ServiceException;

    public net.zdsoft.webservice.bind.APService getIfAPService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
