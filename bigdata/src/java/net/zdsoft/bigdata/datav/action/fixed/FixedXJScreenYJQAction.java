package net.zdsoft.bigdata.datav.action.fixed;

import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 新疆固定大屏 <b>一家亲</b>
 * @author shenke
 * @since 2019/5/25 上午9:46
 */
@Controller
@RequestMapping("/bigdata/datav/fixed/screen")
public class FixedXJScreenYJQAction extends BigdataBaseAction {

    @Autowired
    private MysqlClientService mysqlClientService;

    @RequestMapping("xjyjq")
    public String execute() {
        return "/bigdata/datav/fixed/xj/xjyjq.ftl";
    }

}