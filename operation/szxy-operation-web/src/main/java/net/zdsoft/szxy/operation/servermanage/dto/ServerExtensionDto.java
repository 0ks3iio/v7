package net.zdsoft.szxy.operation.servermanage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerExtensionDto {
    private String id;
    private String name;
    private Integer usingNature;
    private Date expireTime;
    private Integer usingState;
    private String serverCode;
    private Long dayApart;
}
