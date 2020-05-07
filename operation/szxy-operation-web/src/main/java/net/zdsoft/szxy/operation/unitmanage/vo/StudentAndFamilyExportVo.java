package net.zdsoft.szxy.operation.unitmanage.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class StudentAndFamilyExportVo extends BaseRowModel {
    @ExcelProperty(value = "序号", index = 0)
    private Integer index;
    @ExcelProperty(value = "学生姓名", index = 1)
    private String studentName;
    @ExcelProperty(value = "学生账号", index = 2)
    private String studentUsername;
    @ExcelProperty(value = "家长姓名", index = 3)
    private String familyName;
    @ExcelProperty(value = "家长账号", index = 4)
    private String familyUsername;
    @ExcelProperty(value = "家长手机号", index = 5)
    private String familyPhone;
}
