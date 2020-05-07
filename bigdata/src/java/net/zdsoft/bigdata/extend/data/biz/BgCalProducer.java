package net.zdsoft.bigdata.extend.data.biz;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Lists;

import net.zdsoft.bigdata.extend.data.entity.UserProfile;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.bigdata.frame.data.phoenix.PhoenixClientService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.StringUtils;

public class BgCalProducer implements Runnable {
	private UserProfile up;
	private CountDownLatch cdl;
	private BgCalStorage bcs;
	
	public BgCalProducer(UserProfile up,CountDownLatch cdl,BgCalStorage bcs) {
		super();
		this.up = up;
		this.cdl = cdl;
		this.bcs = bcs;
	}

	@Override
	public void run() {
		MysqlClientService mysqlClientService = (MysqlClientService) Evn
				.getBean("mysqlClientService");
		PhoenixClientService phoenixClientService = (PhoenixClientService) Evn
				.getBean("phoenixClientService");
		
		for(int i=0;i<Integer.MAX_VALUE;i++){
			List<Json> infoResult = Lists.newArrayList();
			try {
				StringBuilder infoSql = new StringBuilder();
				infoSql.append("SELECT ");
				infoSql.append(up.getPrimaryKey());
				infoSql.append(",");
				infoSql.append(up.getBasicColumns());
				infoSql.append(" FROM ");
				infoSql.append(up.getMainTableName());
				if ("hbase".equals(up.getMainDbType())) {
					infoSql.append(" LIMIT 1000 OFFSET");
					infoSql.append(bcs.count.getAndAdd(1000));
					infoResult = phoenixClientService.getDataListFromPhoenix(null, infoSql.toString(), null);
					
				}else if ("mysql".equals(up.getMainDbType())) {
					infoSql.append(" LIMIT ");
					infoSql.append(bcs.count.getAndAdd(1000));
					infoSql.append(",1000");
					infoResult = mysqlClientService.getDataListFromMysql(null, null, infoSql.toString(), null, null);
				}
				if(CollectionUtils.isEmpty(infoResult)){
					break;
				}
				for(Json j:infoResult){
					if(StringUtils.isNotBlank(j.getString(up.getPrimaryKey()))){
						try {
							bcs.push(j);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		cdl.countDown();
	}

}
