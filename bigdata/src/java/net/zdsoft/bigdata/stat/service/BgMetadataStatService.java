package net.zdsoft.bigdata.stat.service;

import java.util.Date;

public interface BgMetadataStatService {

    /**
     * 元数据按日统计
     */
    void metadataStatByDaily(Date today);

}
