package net.zdsoft.bigdata.data.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import net.zdsoft.framework.converter.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * @author shenke
 * @since 2018/7/31 上午10:54
 */
@Component
public class JacksonDateDeserializer extends JsonDeserializer<Date> {

    @Autowired
    private DateConverter dateConverter;

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return dateConverter.parseDate(jsonParser.getText().trim(), "yyyy-MM-dd HH:mm:ss");
    }
}
