package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.DiagramLibrary;

import java.util.List;

/**
 * @author shenke
 * @since 2018/12/6 下午1:32
 */
public interface DiagramLibraryService extends BaseService<DiagramLibrary, String> {

    /**
     * 获取某个用户的所有收藏
     * @param userId
     * @return
     */
    List<DiagramLibrary> getCollectLibraryByUserId(String userId);

    /**
     * 收藏某个组件
     * @param diagramId
     * @param userId
     * @param libraryName
     */
    void collectDiagram(String diagramId, String userId, String libraryName) throws OverDiagramLibraryCollectMaxException;

    /**
     * 更新收藏组件的名称
     * @param libraryId
     * @param name
     */
    void updateCollectLibraryName(String libraryId, String name);

    /**
     * 删除某个收藏
     * @param libraryId
     */
    void deleteCollect(String libraryId);

    String add2Screen(String libraryId, String screenId) throws DiagramLibraryDeletedException;
}
