package net.zdsoft.system.service.mcode;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.system.entity.mcode.McodeList;

public interface McodeListService extends BaseService<McodeList, String> {
    List<McodeList> findByMcodeIdOrMaintain(int subsystem, int maintain);

    List<McodeList> findByName(String mcodeName, Pagination page);
}
