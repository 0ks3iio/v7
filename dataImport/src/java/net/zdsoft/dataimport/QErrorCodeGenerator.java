package net.zdsoft.dataimport;

import net.zdsoft.dataimport.annotation.ExcelCell;

import java.lang.reflect.Field;

/**
 * QImportError 代码生成工具
 * @author shenke
 * @since 17-8-13 下午3:40
 */
public class QErrorCodeGenerator {

    public static String getJavaCode(Class<?> clazz) {
        String clazzName = "Q" + clazz.getSimpleName() + "Error";
        StringBuffer code = new StringBuffer();
        code.append("import net.zdsoft.dataimport.biz.QImportError;")
                .append("\n\n")
                .append("public class ")
                .append(clazzName)
                .append(" extends QImportError {")
                .append("\n\n")
                .append("\t")
                .append("public static ")
                .append(clazzName)
                .append(" = new ")
                .append(clazzName)
                .append("();")
                .append("\n")
                .append("\t")
                .append("private ")
                .append(clazzName)
                .append(" (){} \n");
        for (Field e : clazz.getDeclaredFields()) {
            if ( e.getAnnotation(ExcelCell.class) != null ) {
                code.append("\n\t")
                        .append("public ImportFieldError ")
                        .append(e.getName())
                        .append(" = ")
                        .append("createFieldError(")
                        .append("\"")
                        .append(e.getName())
                        .append("\");")
                        .append("\n");
            }
        }
        code.append("\n}");
        return code.toString();
    }
}
