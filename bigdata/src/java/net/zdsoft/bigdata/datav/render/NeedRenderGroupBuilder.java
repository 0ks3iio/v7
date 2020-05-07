package net.zdsoft.bigdata.datav.render;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2018/9/28 19:11
 */
public class NeedRenderGroupBuilder {

    public static  <T> T buildGroup(List<DiagramParameter> parameters, GroupKey key, Class<T> type) {
        StringBuilder groupBuilder = new StringBuilder();

        List<DiagramParameter> dps = parameters.stream().filter(dp -> key.name().equals(dp.getGroupKey()))
                .collect(Collectors.toList());
        if (dps.isEmpty()) {
            return null;
        }
        groupBuilder.append("{");
        dps.forEach(dp -> {
            if (dp.getValue() != null) {
                groupBuilder.append("\"").append(dp.getKey()).append("\"").append(":")
                        .append("\"").append(dp.getValue()).append("\"").append(",");
            }
        });
        String groupJsonString = null;
        if (groupBuilder.lastIndexOf(",") == groupBuilder.length()-1) {
            groupBuilder.replace(groupBuilder.lastIndexOf(","), groupBuilder.lastIndexOf(",")+1, "}");
        } else {
            groupBuilder.append("}");
        }
        groupJsonString = groupBuilder.toString();

        return JSONObject.parseObject(groupJsonString, type);
    }

    /**
     * 根据Array进行分组
     * @param parameters
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> buildGroups(List<DiagramParameter> parameters, GroupKey key, Class<T> type) {
        StringBuilder groupBuilder = new StringBuilder();
        List<DiagramParameter> dps = parameters.stream().filter(dp -> key.name().equals(dp.getGroupKey()))
                .collect(Collectors.toList());
        if (dps.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, List<DiagramParameter>> everyArray = new HashMap<>();
        for (DiagramParameter dp : dps) {
            if (dp.getArrayName() != null) {
                everyArray.computeIfAbsent(dp.getArrayName(), k-> new ArrayList<>()).add(dp);
            }
        }
        groupBuilder.append("[");
        for (Map.Entry<String, List<DiagramParameter>> entry : everyArray.entrySet()) {
            groupBuilder.append("{");
            entry.getValue().forEach(dp -> {
                if (dp.getValue() != null) {
                    groupBuilder.append("\"").append(dp.getKey()).append("\"").append(":")
                            .append("\"").append(dp.getValue()).append("\"").append(",");
                }
            });
            groupBuilder.append("\"arrayName\":").append("\"").append(entry.getKey()).append("\"");
            groupBuilder.append("},");
        }
        String groupJsonString = null;
        if (groupBuilder.lastIndexOf(",") == groupBuilder.length()-1) {
            groupBuilder.replace(groupBuilder.lastIndexOf(","), groupBuilder.lastIndexOf(",")+1, "]");
        } else {
            groupBuilder.append("]");
        }
        groupJsonString = groupBuilder.toString();
        return JSONObject.parseArray(groupJsonString, type);
    }
}
