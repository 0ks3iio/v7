package net.zdsoft.basedata.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;

@Entity
@Table(name="base_region")
public class Region extends BaseEntity<String>{

	private static final long serialVersionUID = -5530280145433605527L;
	private String fullCode;
	private String fullName;
	private String regionCode;
	private String regionName;
	private String type;
	
	private String latitude;//经度
	private String longitude;//纬度
	private String locationLatitude;//定位经度
	private String locationLongitude;//定位纬度
	private int zoomLevel;//缩放级别（地图显示专用）
	
	/**
	 * 系统默认用的类型，行政用行政区划
	 */
	public static final String TYPE_1 = "1";
	
	/**
	 * 统计用的行政区划
	 */
	public static final String TYPE_2 = "2";
	
	public static List<Region> dt(String data) {
		List<Region> ts = SUtils.dt(data, new TypeReference<List<Region>>() {
		});
		if (ts == null)
			ts = new ArrayList<Region>();
		return ts;

	}

	public static List<Region> dt(String data, Pagination page) {
		JSONObject json = JSONObject.parseObject(data);
		List<Region> ts = SUtils.dt(json.getString("data"), new TypeReference<List<Region>>() {
		});
		if (ts == null)
			ts = new ArrayList<Region>();
		if (json.containsKey("count"))
			page.setMaxRowCount(json.getInteger("count"));
		return ts;

	}

	public static Region dc(String data) {
		return SUtils.dc(data, Region.class);
	}

	@Override
	public String fetchCacheEntitName() {
		return "region";
	}

	public String getFullCode() {
		return fullCode;
	}

	public void setFullCode(String fullCode) {
		this.fullCode = fullCode;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLocationLatitude() {
		return locationLatitude;
	}

	public void setLocationLatitude(String locationLatitude) {
		this.locationLatitude = locationLatitude;
	}

	public String getLocationLongitude() {
		return locationLongitude;
	}

	public void setLocationLongitude(String locationLongitude) {
		this.locationLongitude = locationLongitude;
	}

	public int getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

}
