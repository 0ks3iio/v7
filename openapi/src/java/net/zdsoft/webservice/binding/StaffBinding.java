
package net.zdsoft.webservice.binding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>staffBinding complex type�� Java �ࡣ
 * 
 * 
 * <pre>
 * &lt;complexType name="staffBinding">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="staffBindReq" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "staffBinding", propOrder = {
    "staffBindReq"
})
public class StaffBinding {

    protected String staffBindReq;

    /**
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaffBindReq() {
        return staffBindReq;
    }

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaffBindReq(String value) {
        this.staffBindReq = value;
    }

}
