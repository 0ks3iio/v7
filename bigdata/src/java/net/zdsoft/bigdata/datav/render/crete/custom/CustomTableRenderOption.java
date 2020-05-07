package net.zdsoft.bigdata.datav.render.crete.custom;

import net.zdsoft.bigdata.datav.render.crete.RenderOption;

/**
 * @author shenke
 * @since 2018/10/14 13:15
 */
public class CustomTableRenderOption extends RenderOption<TableInnerOption> {

    public static CustomTableRenderOption of(TableInnerOption tableInnerOption) {
        CustomTableRenderOption customTableRenderOption = new CustomTableRenderOption();
        customTableRenderOption.setOp(tableInnerOption);
        return customTableRenderOption;
    }
}
