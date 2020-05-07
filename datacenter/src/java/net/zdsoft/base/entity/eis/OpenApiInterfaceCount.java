package net.zdsoft.base.entity.eis;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.utils.UuidUtils;

/**
 * @author yangsj  2018年1月5日上午10:03:43
 * 统计调用接口的信息
 */
@Entity
@Table(name = "eis_openapi_interface_count")
public class OpenApiInterfaceCount extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	@Override
	public String fetchCacheEntitName() {
		return "openApiInterfaceCount";
	}
	@Column(nullable = false, length = 32)
	private String ticketKey;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	
	private int count;
	private String interfaceId;
	private String type;
	private String message;
	private String dataType;
	private String pushUrl;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getPushUrl() {
		return pushUrl;
	}

	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

	public String getTicketKey() {
		return ticketKey;
	}

	public void setTicketKey(String ticketKey) {
		this.ticketKey = ticketKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	 //添加进接口统计表 base_Openapi_Interface_count
    public static OpenApiInterfaceCount saveDoInterface(String type,final String uri,final HttpServletRequest request,String version) {
    	 String ticketKey = request.getHeader("ticketKey");
         if (StringUtils.isBlank(ticketKey)) {
             ticketKey = request.getParameter("ticketKey");
         }
         OpenApiInterfaceCount openApiInterfaceCount = new OpenApiInterfaceCount();
         openApiInterfaceCount.setId(UuidUtils.generateUuid());
         openApiInterfaceCount.setCreationTime(new Date());
         openApiInterfaceCount.setTicketKey(ticketKey.trim());
         openApiInterfaceCount.setType(type);
         return openApiInterfaceCount;
    }
}
