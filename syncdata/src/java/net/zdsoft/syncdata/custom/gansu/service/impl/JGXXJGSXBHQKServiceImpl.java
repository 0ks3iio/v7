package net.zdsoft.syncdata.custom.gansu.service.impl;

import net.zdsoft.syncdata.custom.gansu.dao.JGXXJGSXBHQKDao;
import net.zdsoft.syncdata.custom.gansu.entity.JGXXJGSXBHQK;
import net.zdsoft.syncdata.custom.gansu.service.JGXXJGSXBHQKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/8/20 11:02
 */
@Service("jGXXJGSXBHQKService")
public class JGXXJGSXBHQKServiceImpl  implements JGXXJGSXBHQKService {
    @Autowired
    private JGXXJGSXBHQKDao jGXXJGSXBHQKDao;

    @Override
    public List<JGXXJGSXBHQK> getList() {
        return jGXXJGSXBHQKDao.getList();
    }

}
