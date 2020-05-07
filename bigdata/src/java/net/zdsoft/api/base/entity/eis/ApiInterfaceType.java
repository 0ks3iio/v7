package net.zdsoft.api.base.entity.eis;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 接口类型的表
 * @author yangsj
 */
@Entity
@Table(name = "bg_openapi_interface_type")
public class ApiInterfaceType extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	@Override
	public String fetchCacheEntitName() {
		return "apiInterfaceType";
	}
	public static final int INTERFACE_TYPE  = 1;  //接口类型
	public static final int RESULT_TYPE = 2;  //结果类型
	public static final int PUBLIC_TYPE = 3;  //公共类型 （即时 接口类型，也是 结果类型）
	
	private String  type;
	private String  typeName;
	private Integer classify; //类别   1：接口类型 2：结果类型
	private String  metadataId; //元数据表结构

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

	public String getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(String metadataId) {
		this.metadataId = metadataId;
	}
}
