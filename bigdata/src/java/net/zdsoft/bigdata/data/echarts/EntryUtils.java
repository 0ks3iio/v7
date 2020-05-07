/**
 * FileName: EntryUtils.java
 * Author:   shenke
 * Date:     2018/6/27 下午6:09
 * Descriptor:
 */
package net.zdsoft.bigdata.data.echarts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;

import net.zdsoft.echarts.convert.api.JData;

/**
 * @author shenke
 * @since 2018/6/27 下午6:09
 */
public class EntryUtils {

    public static class EntryMapping {

        public static final EntryMapping entry = new EntryMapping() {
            {
                this.x = "x";
                this.y = "y";
                this.stack = "stack";
                this.toX = "toX";
                this.toY = "toY";
                this.s = "s";
                this.min = "min";
                this.max = "max";
                this.parent = "parent";
                this.value = "value";
                this.category = "category";
            }
        };

        public String x;
        public String y;
        public String stack;
        public String toX;
        public String toY;
        public String s;
        public String min;
        public String max;
        public String parent;
        public String value;
        public String category;
    }

    public static JData parseLinkData(Object value, EntryMapping mapping) throws DataException {
        if (value == null) {
            return null;
        }

        String text = null;
        if (value instanceof String) {
            text = (String) value;
        } else {
            text = JSONObject.toJSONString(value);
        }

        JData data = new JData();
        JSONObject linkData = JSONObject.parseObject(text);
        if (linkData.containsKey("link")) {
            data.setLinkList(parse(linkData.get("link")));
        }
        if (linkData.containsKey("data")) {
            data.setEntryList(parse(linkData.get("data"), mapping));
        }
        return data;
    }

    public static List<JData.Link> parse(Object value) throws DataException {
        try {
            if (value == null) {
                return null;
            }

            String text = null;
            if (value instanceof String) {
                text = (String) value;
            } else {
                text = JSONObject.toJSONString(value);
            }
            List<JData.Link> list;
            DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.getGlobalInstance());

            parser.getExtraProcessors().add(createLinkProcessor());

            JSONLexer lexer = parser.lexer;
            int token = lexer.token();
            if (token == JSONToken.NULL) {
                lexer.nextToken();
                list = null;
            } else if (token == JSONToken.EOF && lexer.isBlankInput()) {
                list = null;
            } else {
                list = new ArrayList<>();
                parser.parseArray(JData.Link.class, list);

                parser.handleResovleTask(list);
            }
            parser.close();

            return list;
        } catch (Exception e) {
            throw new DataException("数据解析异常");
        }

    }

    public static List<JData.Entry> parse(Object value, final EntryMapping mapping) throws DataException {
        try {
            if (value == null) {
                return Collections.emptyList();
            }

            String text = null;
            if (value instanceof String) {
                text = (String) value;
            } else {
                text = JSONObject.toJSONString(value);
            }

            List<JData.Entry> list;
            DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.getGlobalInstance());

            parser.getExtraProcessors().add(createEntryProcessor(mapping));

            JSONLexer lexer = parser.lexer;
            int token = lexer.token();
            if (token == JSONToken.NULL) {
                lexer.nextToken();
                list = null;
            } else if (token == JSONToken.EOF && lexer.isBlankInput()) {
                list = null;
            } else {
                list = new ArrayList<>();
                parser.parseArray(JData.Entry.class, list);

                parser.handleResovleTask(list);
            }
            parser.close();

            return list;
        } catch (Exception e) {
            throw new DataException("数据解析异常");
        }
    }

    private static ExtraProcessor createEntryProcessor(EntryMapping mapping) {
        return (object, key, value1) -> {
            JData.Entry entry = (JData.Entry) object;
            if (StringUtils.equals(key, mapping.x)) {
                entry.setX(value1 == null ? null : value1.toString());
            }
            if (StringUtils.equals(key, mapping.y)) {
                entry.setY(value1);
            }
            if (StringUtils.equals(key, mapping.stack)) {
                entry.setStack(value1 == null ? null : value1.toString());
            }
            if (StringUtils.equals(key, mapping.s)) {
                entry.setName(value1 == null ? null : value1.toString());
            }
            if (StringUtils.equals(key, mapping.toX)) {
                entry.setToX(value1 == null ? null : value1.toString());
            }
            if (StringUtils.equals(key, mapping.toY)) {
                entry.setToY(value1 == null ? null : value1.toString());
            }
            if (StringUtils.equals(key, mapping.parent)) {
                entry.setParent(value1 == null ? null : value1.toString());
            }
            if (StringUtils.equals(key, mapping.value)) {
                entry.setValue(value1);
            }
            if (StringUtils.equals(key, mapping.category)) {
                entry.setCategory(key);
            }
        };
    }

    private static ExtraProcessor createLinkProcessor() {
        return (object, key, value) -> {
            JData.Link link = (JData.Link) object;
            if (StringUtils.equals(key, "target"))  {
                link.setTarget(value == null ? null : value.toString());
            }
            if (StringUtils.equals(key, "source")) {
                link.setSource(value == null ? null : value.toString());
            }
            if (StringUtils.equals(key, "value")) {
                link.setValue(value);
            }
            if (StringUtils.equals(key, "category")) {

            }
        };
    }

    public static class DataException extends Exception {
        public DataException(String message) {
            super(message);
        }
    }

    public static class LinkData {
        private List<JData.Entry> entries;
        private List<JData.Link> links;

        public List<JData.Entry> getEntries() {
            return entries;
        }

        public void setEntries(List<JData.Entry> entries) {
            this.entries = entries;
        }

        public List<JData.Link> getLinks() {
            return links;
        }

        public void setLinks(List<JData.Link> links) {
            this.links = links;
        }
    }
}
