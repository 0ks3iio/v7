package net.zdsoft.bigdata.datav.render.crete.custom;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import net.zdsoft.bigdata.data.manager.api.Result;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2018/10/14 13:05
 */
class ObjectParseUtils {

    static List<SimpleTabOption.Tab> parseTabs(Result result) {
        List<SimpleTabOption.Tab> tabs = JSONObject.parseArray(toJSONString(result.getValue()), SimpleTabOption.Tab.class);
        if (CollectionUtils.isNotEmpty(tabs)) {
            return tabs;
        }
        return null;
    }

    static TitleInnerOption parseTitle(Result result) {
        List<TitleInnerOption> titles = JSONObject.parseArray(toJSONString(result.getValue()), TitleInnerOption.class);
        if (titles.size()>0) {
            return titles.get(0);
        }
        return null;
    }

    static TableInnerOption parseTable(Result result) {
        List<JSONObject> items = JSONObject.parseObject(toJSONString(result.getValue()),
                new TypeReference<List<JSONObject>>(){}, Feature.OrderedField);
        TableInnerOption tableInnerOption = new TableInnerOption();
        int counter = 0;
        List<List<String>> ths = new ArrayList<>();
        for (JSONObject item : items) {
            if (counter == 0) {
                tableInnerOption.setHeads(parseToString(item));
            } else {
                ths.add(parseToString(item));
            }
            counter++;
        }
        tableInnerOption.setItems(ths);
        return tableInnerOption;
    }

    static AbstractNumber parseUpNumber(Result result) {
        String text = toJSONString(result.getValue());
        if (text.contains("[")) {
            List<UpNumberInnerOption> numberInnerOptions = JSONObject.parseArray(text, UpNumberInnerOption.class);
            return  numberInnerOptions.get(0);
        } else {
            UpNumberInnerOption numberInnerOption = JSONObject.parseObject(text, UpNumberInnerOption.class);
            return numberInnerOption;
        }
    }

    static ProportionalInnerRenderOption parseProportional(Result result) {
        List<ProportionalInnerRenderOption> proportionalInnerRenderOptions = JSONObject.parseArray(toJSONString(result.getValue()), ProportionalInnerRenderOption.class);
        if (proportionalInnerRenderOptions.isEmpty()) {
            return null;
        }
        return proportionalInnerRenderOptions.get(0);
    }

    static List<IndexInnerOptionSeries> parseIndex(Result result) {
        List<IndexInnerOptionSeries> indexInnerOptions = JSONObject.parseArray(toJSONString(result.getValue()), IndexInnerOptionSeries.class);
        return indexInnerOptions;
    }

    static StateCardInnerOption parseStateCard(Result result) {
        List<StateCardInnerOptionSeries> series = JSONObject.parseArray(toJSONString(result.getValue()), StateCardInnerOptionSeries.class);
        StateCardInnerOption stateCardInnerOption = new StateCardInnerOption();
        stateCardInnerOption.setSeries(series);
        return stateCardInnerOption;
    }

    private static List<String> parseToString(JSONObject object) {
        List<Td<String>> tdList = new ArrayList<>(object.size());
        for (Map.Entry<String, Object> entry : object.entrySet()) {
            Td<String> td = new Td();
            td.setName(entry.getKey());
            if (entry.getValue() != null) {
                td.setValue(entry.getValue().toString());
            }
            tdList.add(td);
        }
        Collections.sort(tdList);
        return tdList.stream().map(Td::getValue).collect(Collectors.toList());
    }

    private static String toJSONString(Object value) {
        String text ;
        if (!(value instanceof String)) {
            text = JSONObject.toJSONString(value);
        }
        else {
            text = (String) value;
        }
        return text;
    }
}
