package net.zdsoft.bigdata.datav.enu;

/**
 * @author shenke
 * @since 2018/9/26 17:16
 */
public enum Root {
    /**
     * 根节点
     */
    ROOT(1);
    private Integer root;

    Root(Integer root) {
        this.root = root;
    }

    public Integer getRoot() {
        return root;
    }
}
