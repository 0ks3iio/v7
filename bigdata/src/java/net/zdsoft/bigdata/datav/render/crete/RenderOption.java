package net.zdsoft.bigdata.datav.render.crete;

import net.zdsoft.bigdata.datav.interaction.InteractionAttribute;

/**
 * @author shenke
 * @since 2018/9/27 15:09
 */
public class RenderOption<T> {

    private T op;
    private BasicAttribute attribute;
    private Integer diagramType;
    private InteractionAttribute interactionAttribute;
    private boolean render = true;

    public T getOp() {
        return op;
    }

    public void setOp(T op) {
        this.op = op;
    }

    public BasicAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(BasicAttribute attribute) {
        this.attribute = attribute;
    }

    public Integer getDiagramType() {
        return diagramType;
    }

    public RenderOption<T> diagramType(Integer diagramType) {
        this.diagramType = diagramType;
        return this;
    }

    public InteractionAttribute getInteractionAttribute() {
        return interactionAttribute;
    }

    public void setInteractionAttribute(InteractionAttribute interactionAttribute) {
        this.interactionAttribute = interactionAttribute;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }
}
