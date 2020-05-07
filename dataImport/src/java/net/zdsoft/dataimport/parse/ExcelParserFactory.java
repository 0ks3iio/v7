package net.zdsoft.dataimport.parse;

/**
 * @author shenke
 * @since 2017.07.31
 */
public class ExcelParserFactory {

    private volatile static ExcelParser excelParser;

    public static Parser getParser() {

        try {
            if ( excelParser != null ) {
            }
            else {
                synchronized (ExcelParserFactory.class){
                    if ( excelParser == null ) {
                        excelParser = new ExcelParser();
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return excelParser;
    }
}
