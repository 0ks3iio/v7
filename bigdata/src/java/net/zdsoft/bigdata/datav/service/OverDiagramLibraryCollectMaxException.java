package net.zdsoft.bigdata.datav.service;

/**
 * 超过可收藏组件的最大值
 * @author shenke
 * @since 2018/12/6 下午1:40
 */
public class OverDiagramLibraryCollectMaxException extends Exception {

    public static final int MAX_COLLECT = 20;

    public OverDiagramLibraryCollectMaxException(String message) {
        super(message);
    }
}
