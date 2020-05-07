package net.zdsoft.cache.admin.service.impl;

import com.google.common.collect.Lists;
import net.zdsoft.cache.admin.CacheAdminConstant;
import net.zdsoft.cache.admin.RedisApplication;
import net.zdsoft.cache.admin.core.CKey;
import net.zdsoft.cache.admin.core.CacheTreeNode;
import net.zdsoft.cache.admin.core.QueryType;
import net.zdsoft.cache.admin.service.ViewService;
import net.zdsoft.framework.entity.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.DataType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author shenke
 * @since 2017.07.11
 */
@Service("viewService")
public class ViewServiceImpl extends RedisApplication implements ViewService {

    @Override
    public List<CKey> getCKeyByDataTypeAndKey(DataType dataType, QueryType queryType, String queryKey, int dbIndex, Pagination pagination) {
        //lock.lock();
        try {
            if ( KEY_CACHE_REFRESH_ING ) {
                //condition.await();
            }
            List<CKey> cKeyList = keyCache.get(getRedis() + DEFAULT_SEPARATOR + dbIndex);
            List<CKey> cKeys;
            if ( dataType == null && StringUtils.isBlank(queryKey) ) {
                cKeys = cKeyList;
            } else {
                cKeys = getQueryKeys(cKeyList,dataType,queryType,queryKey);
            }
            pagination.setMaxRowCount(cKeys.size());
            pagination.initialize();
            int fromIndex = (pagination.getPageIndex() - 1) * pagination.getPageSize();
            int toIndex =  pagination.getPageIndex()*pagination.getPageSize() > cKeys.size() ? cKeys.size(): pagination.getPageIndex()*pagination.getPageSize();

            return cKeys.subList(fromIndex, toIndex);
        } catch (Exception e){

        } finally {
           //lock.unlock();
        }
        return Lists.newArrayList();
    }

    @Override
    public List<CKey> getCKey(int dbIndex, Pagination pagination) {
        return getCKeyByDataType(null,dbIndex,pagination);
    }

    @Override
    public List<CKey> getCKeyByDataType(DataType dataType, int dbIndex, Pagination pagination) {
        return getCKeyByDataTypeAndKey(dataType,null,null,dbIndex, pagination);
    }

    private List<CKey> filterByDataType(List<CKey> allKeys, DataType dataType) {

        if ( dataType == null ) {
            return allKeys;
        }
        List<CKey> cKeyList = Lists.newArrayList();
        for (CKey key : allKeys) {
            if ( dataType.equals(key.getDataType()) ) {
                cKeyList.add(key);
            }
        }
        return cKeyList;
    }

    protected List<CKey> getQueryKeys(List<CKey> allKeys, DataType dataType, QueryType queryType, String queryValue) {
        List<CKey> queryKeyList = Lists.newArrayList();
        allKeys = filterByDataType(allKeys,dataType);
        if ( queryType == null ) {
            queryType = QueryType.UNKOWN;
        }
        for (CKey key : allKeys) {
            if ( StringUtils.isBlank(queryValue) ) {
                queryKeyList.add(key);
                continue;
            }
            switch ( queryType ) {
                case HEAD:
                    if ( key.getKey().startsWith(queryValue) ) {
                        queryKeyList.add(key);
                    }
                    break;
                case END:
                    if ( key.getKey().endsWith(queryValue) ) {
                        queryKeyList.add(key);
                    }
                    break;
                case MIDDLE:
                    if ( key.getKey().contains(queryValue) ) {
                        queryKeyList.add(key);
                    }
                    break;
                default:
                    if ( key.getKey().equals(queryValue) ) {
                        queryKeyList.add(key);
                    }
            }
        }
        return queryKeyList;
    }

    @Override
    public Set<CacheTreeNode> getLeftTree(boolean refresh) {
        if ( refresh || (treeCache.isEmpty() && !redisServerCache.isEmpty()) ) {
            // init tree
            treeCache.clear();
            for (Map.Entry<String, Map<String, String>> serverEntry : redisServerCache.entrySet()) {
                Map<String,String> server = serverEntry.getValue();
                String host = server.get(CacheAdminConstant.PROPERTIES_REDIS_HOST);
                String port = server.get(CacheAdminConstant.PROPERTIES_REDIS_PORT);
                String dbNumber = server.get(CacheAdminConstant.SERVER_CACHE_KEY_DB_NUMBER);
                CacheTreeNode cacheTreeNode = new CacheTreeNode();
                cacheTreeNode.setName(host+DEFAULT_SEPARATOR+port);
                cacheTreeNode.setParent(Boolean.TRUE);
                cacheTreeNode.setHost(host);
                cacheTreeNode.setPort(port);
                cacheTreeNode.setDbIndex("0");
                if ( dbNumber != null) {
                    cacheTreeNode.setChildren(getDBTreeNode(Integer.valueOf(dbNumber),host,port));
                }
                treeCache.add(cacheTreeNode);
            }
        }
        return treeCache;
    }

    Set<CacheTreeNode> getDBTreeNode(int dbNumber,String host, String port) {
        Set<CacheTreeNode> dbNodes = new TreeSet<CacheTreeNode>();
        for ( int i=0; i<=dbNumber; i++ ) {
            CacheTreeNode dbNode = new CacheTreeNode();
            dbNode.setName("db-"+String.valueOf(i));
            dbNode.setParent(Boolean.FALSE);
            dbNode.setHost(host);
            dbNode.setPort(port);
            dbNode.setDbIndex(String.valueOf(i));
            dbNodes.add(dbNode);
        }
        return dbNodes;
    }

}
