package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.ScreenDemo;

import java.util.List;

/**
 * @author yangkj
 * @since 2019/6/19 17:31
 */
public interface ScreenDemoService extends BaseService<ScreenDemo,String> {

    List<ScreenDemo> findAllByOrderId();
}
