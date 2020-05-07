
package net.zdsoft.syncdata.custom.guangyuan.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="myHeader" type="{cq168webshare}MySoapHeader" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "myHeader"
})
@XmlRootElement(name = "CheckLogin")
public class CheckLogin {

    protected MySoapHeader myHeader;

    /**
     * ��ȡmyHeader���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link MySoapHeader }
     *     
     */
    public MySoapHeader getMyHeader() {
        return myHeader;
    }

    /**
     * ����myHeader���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link MySoapHeader }
     *     
     */
    public void setMyHeader(MySoapHeader value) {
        this.myHeader = value;
    }

}
