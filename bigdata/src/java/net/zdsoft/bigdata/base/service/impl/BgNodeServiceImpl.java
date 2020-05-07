package net.zdsoft.bigdata.base.service.impl;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.base.dao.BgNodeDao;
import net.zdsoft.bigdata.base.entity.Node;
import net.zdsoft.bigdata.base.service.BgNodeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

/**
 * @author yangkj
 * @since 2019/5/20 16:07
 */
@Service("bgNodeService")
public class BgNodeServiceImpl extends BaseServiceImpl<Node,String> implements BgNodeService {

    private Logger log = Logger.getLogger(BgNodeServiceImpl.class);

    @Autowired
    private BgNodeDao bgNodeDao;

    @Override
    protected BaseJpaRepositoryDao<Node, String> getJpaDao() {
        return bgNodeDao;
    }

    @Override
    protected Class<Node> getEntityClass() {
        return Node.class;
    }

    @Override
    public void updateStatusByIds(List<String> ids) {
        bgNodeDao.updateStatusByIds(ids);
    }

    @Override
    public boolean nodeConnection(Node node) {
        Boolean result = true;
        Session session = null;
        JSch jsch = new JSch();
        try {
            //创建会话
            session = jsch.getSession(node.getUsername(),node.getDomain(), Integer.valueOf(node.getPort()));
            //输入密码
            session.setPassword(node.getPassword());
            //配置信息
            Properties config = new Properties();
            //设置不用检查hostKey
            //如果设置成“yes”，ssh就不会自动把计算机的密匙加入“$HOME/.ssh/known_hosts”文件，
            //并且一旦计算机的密匙发生了变化，就拒绝连接。
            config.setProperty("StrictHostKeyChecking", "no");
            session.setConfig(config);
            //过期时间
            session.setTimeout(30 * 60 * 1000);
            //建立连接
            session.connect();
        }catch (Exception e){
            e.printStackTrace();
            result=false;
            log.debug("节点连接失败，节点id:"+node.getId()+",节点名称:"+node.getName());
        }finally {
            session.disconnect();
        }

        return result;
    }
}
