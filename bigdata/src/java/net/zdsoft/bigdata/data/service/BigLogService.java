package net.zdsoft.bigdata.data.service;

import net.zdsoft.bigdata.data.dto.LogDto;

/**
 * @author:zhujy
 * @since:2019/6/3 13:24
 */
public interface BigLogService {

    void insertLog(LogDto logDto);

    void updateLog(LogDto logDto);

    void deleteLog(LogDto logDto);
}
