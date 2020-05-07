package net.zdsoft.system.service.mcode;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.system.entity.mcode.McodeDetail;

public interface McodeDetailService extends BaseService<McodeDetail,String> {

    List<McodeDetail> saveAll(String mcodeId, List<McodeDetail> list);

    void deteleOne(McodeDetail mcodeDetail);

    // 刷新缓存
    void doRefreshCache(String... mcodeId);

    // 刷新缓存
    void doRefreshCacheAll();

    /**
     * 根据mcodeId和thisId值，检索单个微代码 缓存
     * 
     * @param mcodeId
     * @param thisId
     * @return
     */
    McodeDetail findByMcodeAndThisId(String mcodeId, String thisId);

    /**
     * 根据mcodeIds，检索所有的微代码 缓存
     * 
     * @param mcodeIds
     * @return
     */
    List<McodeDetail> findByMcodeIds(String... mcodeIds);

    /**
     * 根据mcodeId检索 缓存
     * 
     * @param mcodeId
     * @return key thisId
     */
    Map<String, McodeDetail> findMapByMcodeId(String mcodeId);

    /**
     * 缓存
     * 
     * @param mcodeIds
     * @return key mcodeId thisId
     */
    Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds(String... mcodeIds);

    /**
     * 缓存
     * 
     * @param mcodeIds
     * @return key mcodeId
     */
    Map<String, List<McodeDetail>> findMapListByMcodeIds(String... mcodeIds);

    /**
     * 根据mcodeIds，检索所有的微代码 缓存
     * 
     * @param mcodeId
     * @return
     */
    List<McodeDetail> findByMcodeId(final String mcodeId, Pagination page);

    /**
     * 开启/关停
     * 
     * @param isUsing
     * @param id
     */
    void updateIsUsingById(int isUsing, String id);

}
