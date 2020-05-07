
package net.zdsoft.webservice.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>staffBindingResponse complex type�� Java �ࡣ
 * 
 * 
 * <pre>
 * &lt;complexType name="staffBindingResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="staffBindingReturn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "staffBindingResponse", propOrder = {
    "staffBindingReturn"
})
public class StaffBindingResponse {

    protected String staffBindingReturn;

    /**
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaffBindingReturn() {
        return staffBindingReturn;
    }

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaffBindingReturn(String value) {
        this.staffBindingReturn = value;
    }

}
