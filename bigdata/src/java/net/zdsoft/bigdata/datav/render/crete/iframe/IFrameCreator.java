package net.zdsoft.bigdata.datav.render.crete.iframe;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupIFrame;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.NeedRenderGroupBuilder;
import net.zdsoft.bigdata.datav.render.crete.RenderOption;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;

import java.util.List;
import java.util.Optional;

/**
 * @author shenke
 * @since 2018/12/11 下午4:10
 */
public class IFrameCreator implements RenderOptionCreator<RenderOption<IFrame>> {

    @Override
    public RenderOption<IFrame> create(Result result, Diagram diagram,
                                       List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.I_FRAME.getType().equals(diagram.getDiagramType())) {
            return null;
        }

        RenderOption<IFrame> renderOption = new RenderOption<>();
        Optional<EarlyParameter> earlyParameter = earlyParameters.stream().filter(e->"iframe_common".equals(e.getGroupKey())).findAny();
        if (earlyParameter.isPresent()) {
            GroupIFrame url = NeedRenderGroupBuilder.buildGroup(earlyParameter.get().getEarlyParameters(),
                    GroupKey.iframe_common, GroupIFrame.class);
            if (url != null) {
                IFrame iFrame = new IFrame();
                iFrame.setUrl(url.getUrl());
                renderOption.setOp(iFrame);
            }
        } else {
            renderOption.setOp(new IFrame());
        }

        return renderOption;
    }
}
