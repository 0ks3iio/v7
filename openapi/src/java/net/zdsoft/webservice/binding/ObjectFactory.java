
package net.zdsoft.webservice.binding;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.zdsoft.webservice.binding package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _StaffBindingResponse_QNAME = new QName("http://binding.webservice.zdsoft.net", "staffBindingResponse");
    private final static QName _StaffBinding_QNAME = new QName("http://binding.webservice.zdsoft.net", "staffBinding");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.zdsoft.webservice.binding
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StaffBinding }
     * 
     */
    public StaffBinding createStaffBinding() {
        return new StaffBinding();
    }

    /**
     * Create an instance of {@link StaffBindingResponse }
     * 
     */
    public StaffBindingResponse createStaffBindingResponse() {
        return new StaffBindingResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StaffBindingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://binding.webservice.zdsoft.net", name = "staffBindingResponse")
    public JAXBElement<StaffBindingResponse> createStaffBindingResponse(StaffBindingResponse value) {
        return new JAXBElement<StaffBindingResponse>(_StaffBindingResponse_QNAME, StaffBindingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StaffBinding }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://binding.webservice.zdsoft.net", name = "staffBinding")
    public JAXBElement<StaffBinding> createStaffBinding(StaffBinding value) {
        return new JAXBElement<StaffBinding>(_StaffBinding_QNAME, StaffBinding.class, null, value);
    }

}
