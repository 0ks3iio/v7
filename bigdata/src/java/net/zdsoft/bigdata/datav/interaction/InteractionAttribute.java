package net.zdsoft.bigdata.datav.interaction;

import net.zdsoft.bigdata.datav.entity.InteractionBinding;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/26 下午3:05
 */
public class InteractionAttribute {

    /**
     * 是否开启交互
     * 即：是否通知其他图表刷新
     */
    private boolean active;
    /**
     * 是否需要被通知其他图表的交互动作
     */
    private boolean beNotified;

    private List<InteractionBinding> bindings;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isBeNotified() {
        return beNotified;
    }

    public void setBeNotified(boolean beNotified) {
        this.beNotified = beNotified;
    }

    public List<InteractionBinding> getBindings() {
        return bindings;
    }

    public void setBindings(List<InteractionBinding> bindings) {
        this.bindings = bindings;
    }
}
