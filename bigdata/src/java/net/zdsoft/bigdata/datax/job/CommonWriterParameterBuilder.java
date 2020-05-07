package net.zdsoft.bigdata.datax.job;

import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.entity.JobContentParameter;
import net.zdsoft.bigdata.datax.entity.MetadataTransfer;

import java.util.Map;

/**
 * Created by wangdongdong on 2019/6/17 17:43.
 */
public abstract class CommonWriterParameterBuilder {

    private Map<String, String> frameParamMap;

    /**
     * writer参数
     * @param paramMap
     * @return
     * @throws BigDataBusinessException
     */
    public abstract JobContentParameter getJobContentWriterParameter(Map<String, String> paramMap, MetadataTransfer metadata) throws BigDataBusinessException;

    public Map<String, String> getFrameParamMap() {
        return frameParamMap;
    }

    public void setFrameParamMap(Map<String, String> frameParamMap) {
        this.frameParamMap = frameParamMap;
    }
}
