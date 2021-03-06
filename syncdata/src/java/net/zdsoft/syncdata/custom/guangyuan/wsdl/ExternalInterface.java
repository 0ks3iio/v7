
package net.zdsoft.syncdata.custom.guangyuan.wsdl;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "ExternalInterface", targetNamespace = "cq168webshare", wsdlLocation = "file:/C:/Users/ZDWP01/Desktop/ExternalInterface1.xml")
public class ExternalInterface
    extends Service
{

    private final static URL EXTERNALINTERFACE_WSDL_LOCATION;
    private final static WebServiceException EXTERNALINTERFACE_EXCEPTION;
    private final static QName EXTERNALINTERFACE_QNAME = new QName("cq168webshare", "ExternalInterface");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/C:/Users/ZDWP01/Desktop/ExternalInterface1.xml");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        EXTERNALINTERFACE_WSDL_LOCATION = url;
        EXTERNALINTERFACE_EXCEPTION = e;
    }

    public ExternalInterface() {
        super(__getWsdlLocation(), EXTERNALINTERFACE_QNAME);
    }

    public ExternalInterface(WebServiceFeature... features) {
        super(__getWsdlLocation(), EXTERNALINTERFACE_QNAME, features);
    }

    public ExternalInterface(URL wsdlLocation) {
        super(wsdlLocation, EXTERNALINTERFACE_QNAME);
    }

    public ExternalInterface(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, EXTERNALINTERFACE_QNAME, features);
    }

    public ExternalInterface(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ExternalInterface(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ExternalInterfaceSoap
     */
    @WebEndpoint(name = "ExternalInterfaceSoap")
    public ExternalInterfaceSoap getExternalInterfaceSoap() {
        return super.getPort(new QName("cq168webshare", "ExternalInterfaceSoap"), ExternalInterfaceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ExternalInterfaceSoap
     */
    @WebEndpoint(name = "ExternalInterfaceSoap")
    public ExternalInterfaceSoap getExternalInterfaceSoap(WebServiceFeature... features) {
        return super.getPort(new QName("cq168webshare", "ExternalInterfaceSoap"), ExternalInterfaceSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (EXTERNALINTERFACE_EXCEPTION!= null) {
            throw EXTERNALINTERFACE_EXCEPTION;
        }
        return EXTERNALINTERFACE_WSDL_LOCATION;
    }

}
