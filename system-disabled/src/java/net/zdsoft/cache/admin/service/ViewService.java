package net.zdsoft.cache.admin.service;

import net.zdsoft.cache.admin.core.CKey;
import net.zdsoft.cache.admin.core.CacheTreeNode;
import net.zdsoft.cache.admin.core.QueryType;
import net.zdsoft.framework.entity.Pagination;
import org.springframework.data.redis.connection.DataType;

import java.util.List;
import java.util.Set;

/**
 * @author shenke
 * @since 2017.07.11
 */
public interface ViewService {

    List<CKey> getCKey(int dbIndex, Pagination pagination);

    List<CKey> getCKeyByDataType(DataType dataType, int DBIndex, Pagination pagination);

    List<CKey> getCKeyByDataTypeAndKey(DataType dataType, QueryType queryType, String queryKey, int dbBIndex, Pagination pagination);

    Set<CacheTreeNode> getLeftTree(boolean refresh);

    //void addServer(String host, int port, String password);
}
