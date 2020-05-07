package net.zdsoft.stutotality.data.service;

import net.zdsoft.stutotality.data.dto.StutotalityResultDto;
import net.zdsoft.stutotality.data.dto.StutotalityTypeDto;

import java.util.List;

public interface StutotalityResultRemoteService {

    List<StutotalityTypeDto> getStutotalitytypeList(String studentId);

    List<StutotalityResultDto> getStutotalityItemResult(String studentId, String type);

//    StutotalityTypeDto checkCode(String studentId,String codeId);

    StutotalityTypeDto saveCode(String studentId,String codeId);
}
