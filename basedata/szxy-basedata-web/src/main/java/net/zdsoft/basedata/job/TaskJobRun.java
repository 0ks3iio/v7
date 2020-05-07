package net.zdsoft.basedata.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import net.zdsoft.basedata.constant.JobConstants;
import net.zdsoft.basedata.entity.TaskRecord;
import net.zdsoft.basedata.service.TaskRecordService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.TransactionException;

@DisallowConcurrentExecution
public class TaskJobRun implements Job {
	
	private static final Logger log = Logger.getLogger(TaskJobRun.class);
	
    private TaskRecordService taskRecordService;
    private TaskJobService taskJobService = null;
    
    // 是否正在运行
    private final AtomicBoolean running = new AtomicBoolean(false);
    
    // ---------------------监控使用------------------------
    private final AtomicBoolean submitTaskRunning = new AtomicBoolean(false);// 从数据库取任务是否正在运行
    private final AtomicBoolean takeTaskRunning = new AtomicBoolean(false);// 从处理任务是否正在运行
    
    // 处理任务的所在单位 key=value=unitId
//    private ConcurrentMap<String, String> unitIdMap = new ConcurrentHashMap<String, String>();
    // 处理任务的service key=value=service 防止并发造成死锁
    private ConcurrentMap<String, String> serviceMap = new ConcurrentHashMap<String, String>();
    
    private BlockingQueue<Future<TaskRecord>> finishTasks = null;// 完成任务队列
    private BlockingQueue<TaskRecord> jobs = null;// 阻塞队列
    CompletionService<TaskRecord> completionService = null;
    
    // 提交任务和处理任务线程
    private ThreadGroup threadGroup = null;
    private Thread submitThread = null;
    private Thread takeThread = null;
    
    public TaskRecordService getTaskRecordService(){
    	if(taskRecordService == null){
    		taskRecordService = Evn.getBean("taskRecordService");
    	}
    	return taskRecordService;
    }
    
	@Override
	public void execute(JobExecutionContext jobec) throws JobExecutionException {
//		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:MM:ss"); 
//		log.info("------进入任务调度"+df.format(new Date()));
//		running.compareAndSet(false, true);
//		runExec();
	}
	/**
	 * 长时间处于“预执行”和“正在执行”状态的任务（中途出现问题而中断的，比如服务器宕机等），将其改为“未处理”
	 */
	public void updateJobNoHand() {
		int resetTime = 60;//分钟
		getTaskRecordService().updateJobNoHand(resetTime);
	}
	// ---------------------------多线程方式-----------------------------------
	private void runExec() {
		int concurrentcyNum = 1;// 并发数
		//长时间处于“预执行”和“正在执行”状态的任务（中途出现问题而中断的，比如服务器宕机等），将其改为“未处理”
		updateJobNoHand();
		
		// 任务队列
        jobs = new LinkedBlockingQueue<TaskRecord>(concurrentcyNum);
        finishTasks = new LinkedBlockingQueue<Future<TaskRecord>>(concurrentcyNum);
        ExecutorService executor = Executors.newCachedThreadPool();
        completionService = new ExecutorCompletionService<TaskRecord>(executor, finishTasks);
        
        // 线程组
        threadGroup = new ThreadGroup("ThreadGroup-耗时任务");
        
        // 取导入任务并提交任务线程
        submitThread = new Thread(threadGroup, new ImportDataJobSumbit(concurrentcyNum),
                "Thread-耗时任务-submit");
        submitThread.start();
        
        // 处理导入任务
//        takeThread = new Thread(threadGroup, new ImportDataJobTake(), "Thread-耗时任务-take");
//        takeThread.start();
	}
	/**
     * 从数据库中取任务并提交任务
     * 
     */
    private class ImportDataJobSumbit implements Runnable {
        private final int concurrentcyNum;// 并发数

        public ImportDataJobSumbit(int concurrentcyNum) {
            this.concurrentcyNum = concurrentcyNum;
        }

        public void run() {
//            while (true && running.get()) {
//                submitTaskRunning.set(true);
//
//                boolean submit = false;// 是否提交
//                List<TaskRecord> findList = new ArrayList<TaskRecord>();
//                try {
//                	Pageable pageable = new PageRequest(0, concurrentcyNum);
//                	List<TaskRecord> trList = getTaskRecordService().findList(JobConstants.SERVER_TYPE_7,JobConstants.TYPE_1,JobConstants.TASK_STATUS_NO_HAND,pageable);
//                	if(trList.size() > 0){
//                		log.info("取得待处理任务: jobList.size=" + trList.size());
//                	}
//        			if(CollectionUtils.isNotEmpty(trList)){
//	        			for (TaskRecord job : trList) {
//	        				// 一个单位在一次并发中只执行一个任务
////	        				for (String key:unitIdMap.keySet()) {
////	        					System.out.println("有unitId:"+unitIdMap.get(key));
////							}
////	                        if (unitIdMap.containsKey(job.getUnitId())){
////	                        	System.out.println("11111111111111111");
////	                        	continue;
////	                        }
//	                        // service是否有在执行
////	                        for (String key:serviceMap.keySet()) {
////	                        	System.out.println("有service:"+serviceMap.get(key));
////	                        }
//	                        if(serviceMap.containsKey(job.getCustomParamMap().get(JobConstants.KEY_SYS_SERVICE_NAME))){
//	                        	log.info("===========service正在使用：" + job.getCustomParamMap().get(JobConstants.KEY_SYS_SERVICE_NAME));
//	                        	continue;
//	                        }
//	                        // --------判断该任务是否正在执行中---------------
//	                        if (jobs.contains(job)) {
//	                            log.info("================任务已在等待提交或执行中id="+job.getId());
//	                            continue;
//	                        }
//	                        job.setStatus(JobConstants.TASK_STATUS_PRE_HAND);
//	                        job.setModifyTime(new Date());
//	                        findList.add(job);
//	        			}
//	        			if(CollectionUtils.isEmpty(findList)){
//	        				try {
//	                            Thread.sleep(2000);// 2秒钟后轮询
//	                            continue;
//	                        } catch (InterruptedException e) {
//	                        	e.printStackTrace();
//	                             log.error(e.toString());
//	                        }
//	        			}
////	        			getTaskRecordService().saveAllEntitys(findList.toArray(new TaskRecord[0]));
//	        			
//	                    for (final TaskRecord job : findList) {
//	                       
//	                    	log.info("================任务准备加入id="+job.getId());
//	                    	boolean putJob = false;
//	                    	while(!putJob){
//	                    		 try {
//	                    			 jobs.put(job);    
//	                    			 putJob = true;
//	                    		 } catch (InterruptedException e) {
//	                    			 e.printStackTrace();
//	                                 log.error(e.toString());
//	                    			 putJob = false;
//	                              }                    		 
//	                       	}
//	                        
////	                        synchronized (unitIdMap) {
////	                            unitIdMap.putIfAbsent(job.getUnitId(), job.getUnitId());
////	                        }
//	                        synchronized (serviceMap) {
//	                        	serviceMap.putIfAbsent(job.getCustomParamMap().get(JobConstants.KEY_SYS_SERVICE_NAME), job.getCustomParamMap().get(JobConstants.KEY_SYS_SERVICE_NAME));
//	                        }
//	
//	                        // 提交任务
//	                        Callable<TaskRecord> c = new Callable<TaskRecord>() {
//	                            public TaskRecord call() throws Exception {
//	                                return runSingleJob(job);
//	                            }
//	                        };
//	                        completionService.submit(c);
//	                        submit = true;
//	                        log.info("================提交任务");
//	                       
//	                    }
//        			}
//                } catch (Exception e) {
////                	e.printStackTrace();
//                    log.error("提交任务出错！", e);
//                }
//
//                if (!submit) {
//                    try {
//                        Thread.sleep(10000);// 10秒钟后轮询
//                    } catch (InterruptedException e) {
//                    	e.printStackTrace();
//                         log.error(e.toString());
//                    }
//                }
//            }
        } 
    }
    /**
     * 处理单个任务
     * 
     * @param job
     * @return
     */
//    private TaskRecord runSingleJob(TaskRecord job) {
//    	TaskJobReply reply = new TaskJobReply();
//        String replyId = job.getId();
//        log.info("===========正在处理的任务id="+replyId);
//        try {
//            // 处理业务数据
//            TaskRecord findOne = getTaskRecordService().findOne(job.getId());
//			if(findOne.getIsDeleted() == 1){
//				job.setStatus(JobConstants.TASK_STATUS_ERROR);
//				job.setResultMsg("任务已被删除");
//				job.setModifyTime(new Date());
//				getTaskRecordService().saveAllEntitys(job);
//				return job;
//			}else if(JobConstants.TASK_STATUS_PRE_HAND != findOne.getStatus()){
////				System.out.println("============!=4+id:"+job.getId()+"-------------"+findOne.getStatus());
//				return job;
//			}else{
//				//处理
//				TaskJobDataParam dataparam = new TaskJobDataParam();
//				dataparam.setJobId(findOne.getId());
//				Map<String, String> customParamMap = job.getCustomParamMap();
//				dataparam.setCustomParamMap(customParamMap);
//				String serviceName = customParamMap.get(JobConstants.KEY_SYS_SERVICE_NAME);
//				try{
//					taskJobService = (TaskJobService)Evn.getBean(serviceName);
//				}catch (Exception e) {
//					e.printStackTrace();
//					throw new Exception("配置未找到！");
//				}
//				log.info("================任务更新状态为 正在处理 前id="+replyId);
//				// 更新任务开始时间
//				job.setStatus(JobConstants.TASK_STATUS_IN_HAND);
//				job.setJobStartTime(new Date());
//				job.setModifyTime(new Date());
//				getTaskRecordService().saveAllEntitys(job);
//				log.info("================任务更新状态为 正在处理 后id="+replyId);
//				
//				try{
//					taskJobService.jobDatas(dataparam, reply);
//				}catch (TaskErrorException e) {
//					reply.addActionError(e.getMessage());
//				}catch (Exception e) {
//					reply.addActionError("出现其他错误！");
//				}
//			}
//        } catch (TransactionException e){
//        	e.printStackTrace();
//            reply.addActionError("数据提交失败，请检查数据是否正确！");
//        } catch (Exception e) {
//        	e.printStackTrace();
//            reply.addActionError(e.getMessage());
//        } finally {
//            if (null != reply.getActionErrors() && reply.getActionErrors().size() > 0) {
//            	// 有错误时
//                Collection<String> c = reply.getActionErrors();
//                String msg = collection2HtmlStr(c);
//
//                job.setStatus(JobConstants.TASK_STATUS_ERROR);
//                job.setJobEndTime(new Date());
//                log.error("耗时任务出错"+job.getId()+":" + msg);// 方便查找问题，由于消息可能被截断
//                job.setResultMsg(msg);
//                job.setModifyTime(new Date());
//                getTaskRecordService().saveAllEntitys(job);
//            }else{
//            	//没有错误时
//            	Collection<String> c = reply.getActionMessages();
//				String msg = collection2HtmlStr(c);
//				job.setStatus(JobConstants.TASK_STATUS_SUCCESS);
//				job.setResultMsg(msg);
//				job.setJobEndTime(new Date());
//				job.setModifyTime(new Date());
//				getTaskRecordService().saveAllEntitys(job);
//            }
//            reply.setValue(JobConstants.STATUS_END);
//            RedisUtils.setObject(JobConstants.CACHE_KEY_TASK_JOB_+job.getId(), reply, JobConstants.CACHE_SECONDS);
//        }
//        return job;
//    }
    private String collection2HtmlStr(Collection<String> c) {
        String msg = "";
        if (null == c || c.size() == 0)
            return msg;

        for (String str : c) {
            msg += str + "<br>";
        }
        return msg;
    }
    /**
     * 处理任务
     * 
     * @author zhaosf
     * @version $Revision: 1.0 $, $Date: Oct 21, 2010 11:22:48 AM $
     */
    private class ImportDataJobTake implements Runnable {
        public void run() {

            // 结果反馈
            while (true && running.get()) {
                takeTaskRunning.set(true);

                TaskRecord job = null;
                String error = "";
                Exception ex = null;
                Future<TaskRecord> future = null;
                try {
                    future = completionService.take();
                    if (null != future)
                        job = future.get(1800, TimeUnit.SECONDS);// 30分钟
                } catch (InterruptedException e) {
                    error = "中断异常";
                    ex = e;
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    error = "执行异常";
                    ex = e;
                } catch (TimeoutException e) {
                    error = "超时异常";
                    ex = e;
                }

                if (null != ex) {
                    log.error(ex.toString());

                    if (null != future) {
                        future.cancel(true);
                    }

                    if (null != job) {
                        job.setStatus(JobConstants.TASK_STATUS_ERROR);
                        job.setJobEndTime(new Date());
                        job.setResultMsg(error + ": " + ex.getMessage());
                        job.setModifyTime(new Date());
                        getTaskRecordService().saveAllEntitys(job);
                    }
                }

                // 任务完成时，去掉该任务所属单位，以便下次加入该单位的其它任务
                if (null != job) {
//                    synchronized (unitIdMap) {
//                        unitIdMap.remove(job.getUnitId(), job.getUnitId());
//                    }
                    synchronized (serviceMap) {
                    	serviceMap.remove(job.getCustomParamMap().get(JobConstants.KEY_SYS_SERVICE_NAME), job.getCustomParamMap().get(JobConstants.KEY_SYS_SERVICE_NAME));
                    }
                }

                log.info("================任务完成去掉前id="+job.getId());
                // 去掉任务
                try {
                    // 注：执行完成的任务与去除的任务并非同一个
                    jobs.take();
                    log.info("================任务完成id="+job.getId());
                } catch (InterruptedException e) {
                	e.printStackTrace();
                    log.error(e.toString());
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
