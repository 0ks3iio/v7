package net.zdsoft.system.remote.service;

public interface McodeDetailRemoteService {

    /**
     * 根据mcodeIds，检索所有的微代码 缓存
     * @param mcodeIds
     * @return
     */
    String findByMcodeIds(String[] mcodeIds);
    /**
     * @param mcodeIds
     * @return key mcodeId
     */
    String findMapListByMcodeIds(String[] mcodeIds);
}
