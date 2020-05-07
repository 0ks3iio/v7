/**
 * FileName: FastJSONArrayParseEx.java
 * Author:   shenke
 * Date:     2018/5/29 下午4:12
 * Descriptor:
 */
package net.zdsoft.bigdata.data.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;

/**
 * @author shenke
 * @since 2018/5/29 下午4:12
 */
final public class FastJSONArrayParseEx {

    private static final int ORDERED_FEATURE = JSON.DEFAULT_PARSER_FEATURE | Feature.OrderedField.getMask();

    /**
     * 顺序解析
     * @param text
     * @return
     */
    public static JSONArray parseArray(String text) {
        if (text == null) {
            return null;
        }
        DefaultJSONParser parser = new DefaultJSONParser(text,
                new JSONScanner(text, ORDERED_FEATURE), ParserConfig.getGlobalInstance());

        JSONArray array;

        JSONLexer lexer = parser.lexer;
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken();
            array = null;
        } else if (lexer.token() == JSONToken.EOF) {
            array = null;
        } else {
            array = new JSONArray();
            parser.parseArray(array);

            parser.handleResovleTask(array);
        }

        parser.close();
        return array;
    }
}
