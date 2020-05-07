/**
 * APServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package net.zdsoft.webservice.bind;

import net.zdsoft.framework.config.Evn;

public class APServiceServiceLocator extends org.apache.axis.client.Service implements net.zdsoft.webservice.bind.APServiceService {

    public APServiceServiceLocator() {
    }


    public APServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public APServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IfAPService
    private java.lang.String IfAPService_address = Evn.getWebUrl()+"/services/IfAPService";

    public java.lang.String getIfAPServiceAddress() {
        return IfAPService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IfAPServiceWSDDServiceName = "IfAPService";

    public java.lang.String getIfAPServiceWSDDServiceName() {
        return IfAPServiceWSDDServiceName;
    }

    public void setIfAPServiceWSDDServiceName(java.lang.String name) {
        IfAPServiceWSDDServiceName = name;
    }

    public net.zdsoft.webservice.bind.APService getIfAPService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IfAPService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIfAPService(endpoint);
    }

    public net.zdsoft.webservice.bind.APService getIfAPService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            net.zdsoft.webservice.bind.IfAPServiceSoapBindingStub _stub = new net.zdsoft.webservice.bind.IfAPServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getIfAPServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIfAPServiceEndpointAddress(java.lang.String address) {
        IfAPService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (net.zdsoft.webservice.bind.APService.class.isAssignableFrom(serviceEndpointInterface)) {
                net.zdsoft.webservice.bind.IfAPServiceSoapBindingStub _stub = new net.zdsoft.webservice.bind.IfAPServiceSoapBindingStub(new java.net.URL(IfAPService_address), this);
                _stub.setPortName(getIfAPServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("IfAPService".equals(inputPortName)) {
            return getIfAPService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ap.eidc.huawei.com/", "APServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ap.eidc.huawei.com/", "IfAPService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("IfAPService".equals(portName)) {
            setIfAPServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
