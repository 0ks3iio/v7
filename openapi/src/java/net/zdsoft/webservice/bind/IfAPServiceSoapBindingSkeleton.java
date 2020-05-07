/**
 * IfAPServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package net.zdsoft.webservice.bind;

public class IfAPServiceSoapBindingSkeleton implements net.zdsoft.webservice.bind.APService, org.apache.axis.wsdl.Skeleton {
    private net.zdsoft.webservice.bind.APService impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DeptBindReq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deptBinding", _params, new javax.xml.namespace.QName("", "DeptBindingReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://ap.eidc.huawei.com/", "DeptBinding"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("deptBinding") == null) {
            _myOperations.put("deptBinding", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deptBinding")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "CorpBindReq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("corpBinding", _params, new javax.xml.namespace.QName("", "corpBindingReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://ap.eidc.huawei.com/", "corpBinding"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("corpBinding") == null) {
            _myOperations.put("corpBinding", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("corpBinding")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "StaffBindReq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("staffBinding", _params, new javax.xml.namespace.QName("", "staffBindingReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://ap.eidc.huawei.com/", "staffBinding"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("staffBinding") == null) {
            _myOperations.put("staffBinding", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("staffBinding")).add(_oper);
    }

    public IfAPServiceSoapBindingSkeleton() {
        this.impl = new net.zdsoft.webservice.bind.IfAPServiceSoapBindingImpl();
    }

    public IfAPServiceSoapBindingSkeleton(net.zdsoft.webservice.bind.APService impl) {
        this.impl = impl;
    }
    public java.lang.String deptBinding(java.lang.String deptBindReq) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.deptBinding(deptBindReq);
        return ret;
    }

    public java.lang.String corpBinding(java.lang.String corpBindReq) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.corpBinding(corpBindReq);
        return ret;
    }

    public java.lang.String staffBinding(java.lang.String staffBindReq) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.staffBinding(staffBindReq);
        return ret;
    }

}
