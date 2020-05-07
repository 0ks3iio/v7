package net.zdsoft.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import net.zdsoft.appstore.entity.AttendanceRecord;
import net.zdsoft.appstore.remote.SigninServerLocator;
import net.zdsoft.appstore.remote.server.AbstractServer;
import net.zdsoft.appstore.remote.server.SignInServer;
import net.zdsoft.appstore.utils.SigninRecordUpload;
import net.zdsoft.appstore.utils.Tools;
import net.zdsoft.background.common.AbstractService;
import net.zdsoft.keel.util.DateUtils;
import net.zdsoft.keel.util.concurrent.AbstractRunnableTask;
import net.zdsoft.keel.util.concurrent.ScheduledTaskExecutor;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 远距离考勤点到服务
 * 
 * @author duhc
 * @version $Revision: 1.0 $, $Date: 2018-6-5  $
 */
public class TelephoneServerTBService extends AbstractService {

    private static Logger logger = Logger.getLogger(TelephoneServerTBService.class);
    private ConcurrentLinkedQueue<AttendanceRecord> signinRecordQueue;

    private ScheduledTaskExecutor sysExecutor;
    private Calendar calStart;
    private int threadNumber = 40;
    private int recordNumber = 500;

    private String remoteIpPort;
    private String remotePath;
    
    @Override
    public void init() {
        
    	calStart = Calendar.getInstance();
        Date date = new Date();
        date = DateUtils.addDay(date, -1);
        calStart.setTime(date);
        calStart.get(Calendar.DAY_OF_YEAR);
    	
        /*
         * 获取ip和端口
         */
        String serverIp = serviceConfig.getParamValue("serverIp");
        int serverPort = Integer.parseInt(serviceConfig.getParamValue("serverPort"));
        AbstractServer server = new SignInServer(serverIp, serverPort);
        
        server.setIdleTime(Integer.parseInt(serviceConfig.getParamValue("idleTime")));
        recordNumber = Integer.parseInt(serviceConfig.getParamValue("recordNumber"));
        
        /*
         * 考勤数据队列
         */
        signinRecordQueue = new ConcurrentLinkedQueue<AttendanceRecord>();
        SigninServerLocator.getInstance().setSigninRecordQueue(signinRecordQueue);

        Tools.setZdsoftServer(true);
        System.currentTimeMillis();

        // 线程数，线程池
        try {
            threadNumber = Integer.parseInt(serviceConfig.getParamValue("threadNumber"));
        }
        catch (NumberFormatException e) {
            logger.error("threadNumber not set init 40!");
            threadNumber = 40;
        }
        sysExecutor = new ScheduledTaskExecutor(threadNumber);

        /*
         * 上传的ip，端口和路径
         */
        remoteIpPort = serviceConfig.getParamValue("remoteIpPort");
        remotePath = serviceConfig.getParamValue("remotePath");
        
        /*
         * 定时推送考勤记录
         */
        sysExecutor.scheduleWithFixedDelay(new AbstractRunnableTask("processSignRecord-task") {
            @Override
            public void processTask() throws Exception {
                processSignRecord();
            }
        }, 1000, 30000, TimeUnit.MILLISECONDS);

        /*
         * 启动远距离进出校考勤服务
         */
        try {
            server.startService();
        }
        catch (Exception e) {
            logger.error("Caught exception: " + e);
        }
    }

    private synchronized void processSignRecord() {
    	if (!signinRecordQueue.isEmpty()) {
    		JSONArray jsonArray = new JSONArray();
    		JSONObject jsonObject = null;
    		List<AttendanceRecord> attendanceRecords = new ArrayList<AttendanceRecord>();
    		AttendanceRecord attendanceRecord = null;
    		for (int i = 0; i < recordNumber; i++) {
        		if (signinRecordQueue.isEmpty()) {
        			break;
        		}
        		attendanceRecord = signinRecordQueue.poll();
        		attendanceRecords.add(attendanceRecord);
        		jsonObject = (JSONObject) JSONObject.toJSON(attendanceRecord);
        		jsonArray.add(jsonObject);
        	}
        	boolean error = false;
        	try {
        		error = SigninRecordUpload.upload(remoteIpPort,remotePath,jsonArray.toJSONString());
        		if (!error) {
        			signinRecordQueue.addAll(attendanceRecords);
        		}
			} catch (Exception e) {
				logger.error("upload Exception: 天波考勤数据上传报错!");
				signinRecordQueue.addAll(attendanceRecords);
			}
        }
    }

    @Override
    public void destroy() {
    	while (true) {
    		if (signinRecordQueue.isEmpty()) {
    			logger.info("考勤信息队列已空退出程序！");
    			break;
    		} else {
    			processSignRecord();
    		}
    	}
    }

	@Override
	public int execute() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void messageReceived(String paramString1, String paramString2) {
		// TODO Auto-generated method stub
		
	}
}
