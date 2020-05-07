package net.zdsoft.desktop.msg.entity;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * TODO
 * 此实体类 未于数据库做映射（有些字段待议，仅供测试）
 * Created by shenke on 2017/3/15.
 */
public class MessageGroup extends BaseEntity<String> {

    public static final String TYPE_SEPARATOR = ";";

    @Override
    public String fetchCacheEntitName() {
        return "messageGroup";
    }

    private String groupName; //分组名称
    private String msgType; //该分组包含的消息类型
    private Integer displayOrder;   //排序信息

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
}
