package net.zdsoft.remote.openapi.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 接口类型的表
 * @author yangsj
 */
@Entity
@Table(name = "base_openapi_interface_type")
public class OpenApiInterfaceType extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	public static final int INTERFACE_TYPE  = 1;  //接口类型
	public static final int RESULT_TYPE = 2;  //结果类型
	public static final int PUBLIC_TYPE = 3;  //公共类型 （即时 接口类型，也是 结果类型）
	
	private String type;
	private String typeName;
	private Integer classify; //类别   1：接口类型 2：结果类型
	
	@Override
	public String fetchCacheEntitName() {
		return "openApiInterfaceType";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getClassify() {
		return classify;
	}

	public void setClassify(Integer classify) {
		this.classify = classify;
	}
    
}
