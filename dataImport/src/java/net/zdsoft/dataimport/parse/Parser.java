package net.zdsoft.dataimport.parse;

import net.zdsoft.dataimport.biz.Reply;
import net.zdsoft.dataimport.core.DataExcel;
import net.zdsoft.dataimport.exception.ImportParseException;

import java.io.InputStream;

/**
 * @author shenke
 * @since 2017.07.31
 */
public interface Parser {

    /**
     * 仅仅解析Excel相关数据， 业务对象相关的数据后续处理
     * @param inputStream
     * @return
     */
    DataExcel parse(InputStream inputStream, Reply reply, ExcelParser.VerifyHeader verifyHeader) throws ImportParseException;
}
