package net.zdsoft.bigdata.datav.render.crete.custom;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;

import java.util.List;
import java.util.Optional;

/**
 * @author shenke
 * @since 2018/10/31 上午11:22
 */
public class SingleImageCreator implements RenderOptionCreator<CustomImageRenderOption> {


    @Override
    public CustomImageRenderOption create(Result result, Diagram diagram,
                                          List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.SINGLE_IMAGE.getType().equals(diagram.getDiagramType())
                && !DiagramEnum.SHUFFLING_IMAGE.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        List<ImageOption> option = JSONObject.parseArray(result.getValue().toString(), ImageOption.class);
        CustomImageRenderOption renderOption = new CustomImageRenderOption();
        renderOption.setOp(option);
        if (DiagramEnum.SHUFFLING_IMAGE.getType().equals(diagram.getDiagramType())) {
            Optional<EarlyParameter> earlyParameter = earlyParameters.stream().filter(e-> GroupKey.shuffling_style.name().equals(e.getGroupKey())).findAny();
            earlyParameter.ifPresent(earlyParameter1 -> earlyParameter1.getEarlyParameters().stream().findFirst().ifPresent(p -> {
                if (p.getValue() != null) {
                    renderOption.setShowPoint(Boolean.valueOf(p.getValue()));
                }
            }));
        }

        return renderOption;
    }
}
