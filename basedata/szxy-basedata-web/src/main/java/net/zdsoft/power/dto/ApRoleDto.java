package net.zdsoft.power.dto;


import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.entity.user.Role;

/**
 * @author yangsj  2018年6月7日下午4:55:51
 */
public class ApRoleDto {
	
	private Server server;
	private Role role;
	private String type;
	private String roelDescription; //截取后的描述
	public String getRoelDescription() {
		return roelDescription;
	}
	public void setRoelDescription(String roelDescription) {
		this.roelDescription = roelDescription;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
