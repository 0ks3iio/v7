package net.zdsoft.bigdata.data.dto;

import java.util.Map;

/**
 * Created by wangdongdong on 2018/11/28 17:36.
 */
public class OptionDto {


    private String code;

    private Integer status;
    
    private Integer mobility;

    private Map<String, String> frameParamMap;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMobility() {
		return mobility;
	}

	public void setMobility(Integer mobility) {
		this.mobility = mobility;
	}

	public Map<String, String> getFrameParamMap() {
        return frameParamMap;
    }

    public void setFrameParamMap(Map<String, String> frameParamMap) {
        this.frameParamMap = frameParamMap;
    }
}
