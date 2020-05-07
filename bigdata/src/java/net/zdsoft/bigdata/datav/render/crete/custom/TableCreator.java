package net.zdsoft.bigdata.datav.render.crete.custom;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.NeedRenderGroupBuilder;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;

import java.util.List;
import java.util.Optional;

/**
 * @author shenke
 * @since 2018/10/14 13:14
 */
public class TableCreator implements RenderOptionCreator<CustomTableRenderOption> {

    @Override
    public CustomTableRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {

        if (!DiagramEnum.TABLE.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        TableInnerOption tableInnerOption = ObjectParseUtils.parseTable(result);
        tableInnerOption.setTableBodyStyle(parseBody(earlyParameters));
        tableInnerOption.setTableHeaderStyle(parse(earlyParameters));
        return CustomTableRenderOption.of(tableInnerOption);
    }

    private TableHeaderStyle parse(List<EarlyParameter> earlyParameters) {
        Optional<EarlyParameter> earlyParameter = earlyParameters.stream().filter(e-> GroupKey.table_header.name().equals(e.getGroupKey()))
                .findFirst();
        if (earlyParameter.isPresent()) {
            return NeedRenderGroupBuilder.buildGroup(earlyParameter.get().getEarlyParameters(), GroupKey.table_header, TableHeaderStyle.class);
        }
        return null;
    }

    private TableBodyStyle parseBody(List<EarlyParameter> earlyParameters) {
        Optional<EarlyParameter> earlyParameter = earlyParameters.stream().filter(e-> GroupKey.table_body.name().equals(e.getGroupKey()))
                .findFirst();
        if (earlyParameter.isPresent()) {
            return NeedRenderGroupBuilder.buildGroup(earlyParameter.get().getEarlyParameters(), GroupKey.table_body, TableBodyStyle.class);
        }
        return null;
    }
}
