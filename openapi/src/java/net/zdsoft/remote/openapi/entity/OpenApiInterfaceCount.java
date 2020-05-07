package net.zdsoft.remote.openapi.entity;

import java.util.Date;

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
@Table(name = "base_openapi_interface_count")
public class OpenApiInterfaceCount extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	private String ticketKey;
	private String type;
	private String uri;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	
	private String version;
    
	
	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
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


	public String getUri() {
		return uri;
	}


	public void setUri(String uri) {
		this.uri = uri;
	}


	public Date getCreationTime() {
		return creationTime;
	}


	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}


	@Override
	public String fetchCacheEntitName() {
		// TODO Auto-generated method stub
		return "openApiInterfaceCount";
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
         openApiInterfaceCount.setUri(uri);
         openApiInterfaceCount.setVersion(version);
         return openApiInterfaceCount;
    }
}
