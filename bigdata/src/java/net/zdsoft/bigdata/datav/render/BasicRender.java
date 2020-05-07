package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupBasic;
import net.zdsoft.bigdata.datav.render.crete.BasicAttribute;
import net.zdsoft.bigdata.datav.render.crete.RenderOption;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/27 15:06
 */
public class  BasicRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        RenderOption renderOption = (RenderOption) ro;


        GroupBasic groupBasic = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.basic, GroupBasic.class);
        if (groupBasic == null) {
            return;
        }

        BasicAttribute basicAttribute = new BasicAttribute();
        basicAttribute.setX(groupBasic.getX());
        basicAttribute.setY(groupBasic.getY());
        basicAttribute.setBackgroundColor(groupBasic.getBackgroundColor());
        basicAttribute.setHeight(groupBasic.getHeight());
        basicAttribute.setWidth(groupBasic.getWidth());
        basicAttribute.setLevel(groupBasic.getLevel());
        basicAttribute.setBorder(groupBasic.getBorder());
        basicAttribute.setLock(Integer.valueOf(1).equals(diagram.getLock()));
        renderOption.setAttribute(basicAttribute);
    }

    @Override
    public boolean supportOthers() {
        return true;
    }
}
