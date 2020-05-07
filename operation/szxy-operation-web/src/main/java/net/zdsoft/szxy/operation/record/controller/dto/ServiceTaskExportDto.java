package net.zdsoft.szxy.operation.record.controller.dto;

import lombok.Data;

import java.io.ByteArrayOutputStream;

/**
 * @description:
 * @author: Fubicheng
 * @create: 2019-04-17 15:17
 **/
@Data
public class ServiceTaskExportDto {
    private ByteArrayOutputStream outputStream;
    private String fileName;
}
