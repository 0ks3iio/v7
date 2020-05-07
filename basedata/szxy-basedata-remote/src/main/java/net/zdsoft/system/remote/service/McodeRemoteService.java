package net.zdsoft.system.remote.service;

public interface McodeRemoteService {

    /**
     * 根据McodeId...获取数据字典信息
     * 
     * @param mcodeIds
     * @return List&lt;McodeDetail&gt;
     */
    public String findByMcodeIds(String... mcodeIds);

    public String findAllByMcodeIds(String... mcodeIds);

    public String findByMcodeContentLike(String mcodeId, String content);

    /**
     * 根据mcodeId和thisId获取单位数据字典信息
     * 
     * @param mcodeId
     * @param thisId
     * @return McodeDetail
     */
    public String findByMcodeAndThisId(String mcodeId, String thisId);

    public String findMapByMcodeIds(String... mcodeIds);

    public String findMapMapByMcodeIds(String... mcodeIds);

    // 刷新缓存
    void doRefreshCache(String... mcodeId);

    // 刷新缓存
    void doRefreshCacheAll();

    public String findMapByMcodeId(String mcodeId);

}
