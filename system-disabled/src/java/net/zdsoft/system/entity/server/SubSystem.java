package net.zdsoft.system.entity.server;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;

@Entity
@Table(name = "sys_subsystem")
public class SubSystem extends BaseEntity<Integer> {
    private static final long serialVersionUID = 6066285096235674862L;

    @ColumnInfo(displayName = "名称", nullable = false)
    private String name;
    @ColumnInfo(displayName = "编号", nullable = false)
    private String code;
    @ColumnInfo(displayName = "链接地址", nullable = false, regexTip = "请输入正确的URL地址", regex = "/[(http|ftp|https):\\/\\/]*[\\w\\-_]+(\\.[\\w\\-_]+)*([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?/")
    @Column(name = "index_url")
    private String url;
    @ColumnInfo(displayName = "排序号", vtype = ColumnInfo.VTYPE_INT, nullable = false)
    @Column(name = "orderid")
    private Integer displayOrder;
    @ColumnInfo(displayName = "所属目录", nullable = false, vtype = ColumnInfo.VTYPE_SELECT, vsql = "select -1, '【第一级】' from dual union select id, name from sys_subsystem where parentid = -1 and id <> -1")
    @Column(name = "parentid")
    private Integer parentId;
    @Column(name = "source")
    private String source;
    @Column(name = "image")
    private String image;
    private String type;

    /**
     * 服务类型 对应serverType
     */
    public static final int SERVER_TYPE_GROUP_FREE = 1; // 集团订购免费使用
    public static final int SERVER_TYPE_GROUP_PERSONAL_OPEN = 2; // 集团订购个人开通
    public static final int SERVER_TYPE_PERSONAL_OPEN = 3; // 个人订购
    public static final int SERVER_TYPE_FREE = 4; // 免费
    public static final String SUBSYSTEM_NAME_ARCHIVE = "archive";// 公文子系统产品标识

    /**
     * 来源 对应Source
     */
    public static final String SOURCE_LOCAL = "1"; // 本地
    public static final String SOURCE_THIRD_PART_NORMAL = "2"; // 第三方
    public static final String SOURCE_THIRD_PART_SPECIAL = "3"; // 第三方特殊
    
    public static List<SubSystem> dt(String data) {
		List<SubSystem> ts = SUtils.dt(data, new TypeReference<List<SubSystem>>() {
		});
		if (ts == null)
			ts = new ArrayList<SubSystem>();
		return ts;

	}

	public static List<SubSystem> dt(String data, Pagination page) {
		JSONObject json = JSONObject.parseObject(data);
		List<SubSystem> ts = SUtils.dt(json.getString("data"), new TypeReference<List<SubSystem>>() {
		});
		if (ts == null)
			ts = new ArrayList<SubSystem>();
		if (json.containsKey("count"))
			page.setMaxRowCount(json.getInteger("count"));
		return ts;

	}

	public static SubSystem dc(String data) {
		return SUtils.dc(data, SubSystem.class);
	}
	

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public String fetchCacheEntitName() {
        return "subsystem";
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
