package net.zdsoft.syncdata.custom.lasa.constant;

import net.zdsoft.framework.config.Evn;

public class LaSaCAConstant {
	//地址参数的定义
	private static final String LS_START_URL = Evn.getString("sync_data_lasa_ca_url") + "/idm/rest";
	public static final String CA_AUTHID_VALUE = Evn.getString("sync_data_ca_authid"); //得到authid
	public static final String CA_QUERY_USER_URL = LS_START_URL + "/QueryUserInfoDetail"; //查找用户信息
	public static final String CA_QUERY_UNIT_URL = LS_START_URL + "/QueryOrganizationVO"; //查找单位信息
	//常量的定义
	public static final int CA_USER_SYN_FLAG = 0; //用户
	public static final int CA_UNIT_SYN_FLAG = 1; //机构 （部门）
	public static final int CA_UNITANDUSER_SYN_FLAG = 2; //用户 + 机构 （部门） 
	public static final int CA_USER_ADD_AND_MODIFY  = 11; //用户添加修改
	public static final int CA_USER_DELETE = 13; //  用户删除
	public static final int CA_USER_REMOVE_POWER = 51; //解除权限（目前还未用到）
	public static final int CA_UNIT_ADD  = 41;   //机构添加
	public static final int CA_UNIT_DELETE = 43; //机构删除
	public static final int CA_UNIT_MODIFY = 42; //机构修改
	//参数名称定义
	public static final String CA_USERIDCODE_NAME = "userIdCode";
	public static final String CA_AUTHID_NAME = "authId";
	public static final String CA_SYNTYPE_NAME = "synType";
	public static final String CA_OPERATEID_NAME = "operateID";
	public static final String CA_STATUS_NAME = "status";
	
	public static final int CA_FAILED_STATUS = 500;
	public static final int CA_SUCCESS_STATUS = 0;
	
	/**
	 * 数据的解析
	 */
	public static final String CA_RESOLVE_DATA_NAME = "info";
}
