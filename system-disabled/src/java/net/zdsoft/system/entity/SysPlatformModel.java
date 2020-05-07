package net.zdsoft.system.entity;

import com.google.common.collect.Lists;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.system.entity.server.Model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sys_platform_model")
public class SysPlatformModel extends BaseEntity<Integer>{

	private static final long serialVersionUID = 1L;
	
	private String mid;
	private Integer parentid;
	private Integer orderid;
	private String name;
	private String type;
	private String url;
	private Integer width;
	private Integer height;
	private Integer subsystem;
	private String usertype;
	private Integer unitclass;
	private Integer isassigned;
	private String description;
	private String win;
	private String pbcommon;
	private String limit;
	private String version;
	private String filelist;
	private String reldir;
	private String mainfile;
	private String parm;
	private Integer uselevel;
	private Integer actionenable;
	private Integer mark;
	private String common;
	private String dataSubsystems;
	private Integer platform;

	private String picture;
	
	private Integer openType;

	
	public Integer getOpenType() {
		return openType;
	}

	public void setOpenType(Integer openType) {
		this.openType = openType;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	@Override
	public String fetchCacheEntitName() {
		return "SysPlatformModel";
	}

	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public Integer getParentid() {
		return parentid;
	}
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	public Integer getOrderid() {
		return orderid;
	}
	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getSubsystem() {
		return subsystem;
	}
	public void setSubsystem(Integer subsystem) {
		this.subsystem = subsystem;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public Integer getUnitclass() {
		return unitclass;
	}
	public void setUnitclass(Integer unitclass) {
		this.unitclass = unitclass;
	}
	public Integer getIsassigned() {
		return isassigned;
	}
	public void setIsassigned(Integer isassigned) {
		this.isassigned = isassigned;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getWin() {
		return win;
	}
	public void setWin(String win) {
		this.win = win;
	}
	public String getPbcommon() {
		return pbcommon;
	}
	public void setPbcommon(String pbcommon) {
		this.pbcommon = pbcommon;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFilelist() {
		return filelist;
	}
	public void setFilelist(String filelist) {
		this.filelist = filelist;
	}
	public String getReldir() {
		return reldir;
	}
	public void setReldir(String reldir) {
		this.reldir = reldir;
	}
	public String getMainfile() {
		return mainfile;
	}
	public void setMainfile(String mainfile) {
		this.mainfile = mainfile;
	}
	public String getParm() {
		return parm;
	}
	public void setParm(String parm) {
		this.parm = parm;
	}
	public Integer getUselevel() {
		return uselevel;
	}
	public void setUselevel(Integer uselevel) {
		this.uselevel = uselevel;
	}
	public Integer getActionenable() {
		return actionenable;
	}
	public void setActionenable(Integer actionenable) {
		this.actionenable = actionenable;
	}
	public Integer getMark() {
		return mark;
	}
	public void setMark(Integer mark) {
		this.mark = mark;
	}
	public String getCommon() {
		return common;
	}
	public void setCommon(String common) {
		this.common = common;
	}
	public String getDataSubsystems() {
		return dataSubsystems;
	}
	public void setDataSubsystems(String dataSubsystems) {
		this.dataSubsystems = dataSubsystems;
	}
	public Integer getPlatform() {
		return platform;
	}
	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public static Model turn2Model(SysPlatformModel platformModel){
		Model model = new Model();
		model.setId(platformModel.getId());
		model.setName(platformModel.getName());
		model.setPicture(platformModel.getPicture());
		model.setParentId(platformModel.getParentid());
		model.setMid(platformModel.getMid());
		model.setDisplayOrder(platformModel.getOrderid());
		model.setSubSystem(platformModel.getSubsystem());
		model.setUnitClass(platformModel.getUnitclass());
		model.setType(platformModel.getType());
		model.setUserType(platformModel.getUsertype());
		model.setUrl(platformModel.getUrl());
		model.setOpenType(platformModel.getOpenType());
		model.setVersion(platformModel.getVersion());
		return model;
	}

	public static List<Model> turn2Models(List<SysPlatformModel> platformModels){
		List<Model> modelList = Lists.newArrayList();
		if (platformModels != null){
			for (SysPlatformModel platformModel : platformModels) {
				modelList.add(turn2Model(platformModel));
			}
		}
		return modelList;
	}
	
}
