/**
 * FileName: JSONParseTest.java
 * Author:   shenke
 * Date:     2018/5/29 下午4:01
 * Descriptor:
 */
package net.zdsoft;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import org.junit.Test;

/**
 * @author shenke
 * @since 2018/5/29 下午4:01
 */
public class JSONParseTest {

    @Test
    public void _test() {
        String str = "[\n" +
                "  {\n" +
                "    \"th1\":\"排行\",\n" +
                "    \"th2\":\"市名\",\n" +
                "    \"th3\":\"总数\",\n" +
                "    \"th4\":\"休学\",\n" +
                "    \"th5\":\"复学\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"th1\": 1,\n" +
                "    \"th2\": \"乌鲁木齐\",\n" +
                "    \"th3\": 100,\n" +
                "    \"th4\": 80,\n" +
                "    \"th5\": 20\n" +
                "  },\n" +
                "  {\n" +
                "    \"th1\": 2,\n" +
                "    \"th2\": \"台北\",\n" +
                "    \"th3\": 100,\n" +
                "    \"th4\": 80,\n" +
                "    \"th5\": 20\n" +
                "  }\n" +
                "]";


        int features = JSON.DEFAULT_PARSER_FEATURE;
        features |= Feature.OrderedField.getMask();
        DefaultJSONParser parser = new DefaultJSONParser(str, new JSONScanner(str, features), ParserConfig.getGlobalInstance());

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

        array.forEach(System.out::println);

    }

}
