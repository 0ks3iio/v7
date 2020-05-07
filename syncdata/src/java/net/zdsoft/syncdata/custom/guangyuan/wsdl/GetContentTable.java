
package net.zdsoft.syncdata.custom.guangyuan.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="deptNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="moduleNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="page" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="pagesize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="isall" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "deptNo",
    "moduleNo",
    "page",
    "pagesize",
    "isall",
    "token"
})
@XmlRootElement(name = "GetContentTable")
public class GetContentTable {

    protected String deptNo;
    protected String moduleNo;
    protected int page;
    protected int pagesize;
    protected boolean isall;
    @XmlElement(name = "Token")
    protected String token;

    /**
     * ��ȡdeptNo���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeptNo() {
        return deptNo;
    }

    /**
     * ����deptNo���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeptNo(String value) {
        this.deptNo = value;
    }

    /**
     * ��ȡmoduleNo���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModuleNo() {
        return moduleNo;
    }

    /**
     * ����moduleNo���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModuleNo(String value) {
        this.moduleNo = value;
    }

    /**
     * ��ȡpage���Ե�ֵ��
     * 
     */
    public int getPage() {
        return page;
    }

    /**
     * ����page���Ե�ֵ��
     * 
     */
    public void setPage(int value) {
        this.page = value;
    }

    /**
     * ��ȡpagesize���Ե�ֵ��
     * 
     */
    public int getPagesize() {
        return pagesize;
    }

    /**
     * ����pagesize���Ե�ֵ��
     * 
     */
    public void setPagesize(int value) {
        this.pagesize = value;
    }

    /**
     * ��ȡisall���Ե�ֵ��
     * 
     */
    public boolean isIsall() {
        return isall;
    }

    /**
     * ����isall���Ե�ֵ��
     * 
     */
    public void setIsall(boolean value) {
        this.isall = value;
    }

    /**
     * ��ȡtoken���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * ����token���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

}
