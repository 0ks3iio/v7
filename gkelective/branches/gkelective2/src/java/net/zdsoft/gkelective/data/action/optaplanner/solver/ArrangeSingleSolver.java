package net.zdsoft.gkelective.data.action.optaplanner.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.action.optaplanner.constants.SelectionConstants;
import net.zdsoft.gkelective.data.action.optaplanner.convert.ArrangeDtoConverter;
import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeCapacityRange;
import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeClass;
import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeClassRoom;
import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeConstantInfo;
import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeStudent;
import net.zdsoft.gkelective.data.action.optaplanner.domain.ArrangeSubjectBatch;
import net.zdsoft.gkelective.data.action.optaplanner.func.ArrangeUtils;
import net.zdsoft.gkelective.data.action.optaplanner.listener.SolverListener;
import net.zdsoft.gkelective.data.dto.Room;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 7选3，6选3等问题算法类
 * @author shensiping
 *
 */
public class ArrangeSingleSolver {
    private static final Logger logger = LoggerFactory.getLogger(ArrangeSingleSolver.class);
    private static final String SOLVER_CONFIG = "businessconf/solver/optaplannerSelectionScheduleSolverConfig.xml";
    private static final String STOP_SOLVER_KEY = "gkelective.stopSolver.";
    private SolverFactory<SelectionScheduleSolution> solverFactory;
    private static Map<String,List<Solver<SelectionScheduleSolution>>> solverMap = new HashMap<String, List<Solver<SelectionScheduleSolution>>>();
    //最少批次数
    private int batchOrign;
    //最多批次数
    private int batchMax;
    private int allMinCapacity;
    private int allMaxCapacity;
    private List<ArrangeStudent> studentList;
    private Map<String, ArrangeStudent> studentMap;
    private List<ArrangeClass> arrangeClassList;
    private String currentSolverId;
    /**
     * 选2+x的学生
     * key subjectIdType
     */
    private Map<String, Set<String>> subjectIdTypeAdditionalStudentIdMap;
    /**
     * 2+x的学生在哪个组合班级
     * key studentId
     */
    private Map<String, String> studentIdAdditionalClassIdMap;
    /**
     * 2+x的班级有哪些学生
     * key classId
     */
    private Map<String, Set<String>> classIdAdditionalStudentIdSetMap;
    /**
     * 2+x的班级，2在哪个批次上课
     * key classId
     */
    private Map<String, Set<Integer>> classIdAdditionalSubjectIndexSetMap;
    /**
     * 2+x班级map
     * key arrangeClassId
     */
    private Map<String, ArrangeClass> arrangeClassMap;
    /**
     * 2+x的班级为2所预留的批次数，0<=数目<=2
     * key classId
     */
    private Map<String, Integer> classIdForFixedBatchCountMap;
    
    private List<SolverListener> listenerList = new ArrayList<SolverListener>();
    /**
     * 科目人数上下限
     */
    private Map<String, ArrangeCapacityRange> subjectIdTypeCapacityRangeMap;
    
    /**
     * 传入参数，初始化问题
     * @param batchOrign 走班课程数
     * @param batchMax 最大批次
     * @param studentList 所有学生
     * @param arrangeClassList 2+x情况
     * @param currentSolverId 轮次id
     */
    public ArrangeSingleSolver(int batchOrign, int batchMax, List<ArrangeStudent> studentList, List<ArrangeClass> arrangeClassList, String currentSolverId) {
        this.batchOrign = batchOrign;
        this.batchMax = batchMax;
        this.studentList = studentList;
        this.studentMap = new HashMap<String, ArrangeStudent>();
        for (ArrangeStudent arrangeStudent : studentList) {
            this.studentMap.put(arrangeStudent.getStudentId(), arrangeStudent);
        }
        this.arrangeClassList = arrangeClassList;
        this.currentSolverId = currentSolverId;
        init();
    }
    
    /**
     * 实例化问题
     */
    public void init() {
        solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);
        TerminationConfig terminationConfig = new TerminationConfig();
//        terminationConfig.setHoursSpentLimit(SelectionConstants.SPENT_LIMIT_HOUR);
        //TODO
        terminationConfig.setMinutesSpentLimit(SelectionConstants.SPENT_LIMIT_MIN);
//        terminationConfig.setMinutesSpentLimit(60L);
//        terminationConfig.setSecondsSpentLimit(20L);
        terminationConfig.setBestScoreLimit("-0hard/-0medium/-3soft");
        solverFactory.getSolverConfig().setTerminationConfig(terminationConfig);
//        solver = solverFactory.buildSolver();
    }
    
    /**
     * 添加观察者
     * @param listener
     */
    public void addListener(SolverListener listener) {
        this.listenerList.add(listener);
    }
    
    public void removeListener(SolverListener listener) {
        this.listenerList.remove(listener);
    }
    public void removeAllListener() {
        this.listenerList.clear();
    }
    
    /**
     * check是否在排
     * @param currentSolverId
     * @return true 正在排
     */
    public static boolean isSolverIdRunning(String currentSolverId) {
        return ArrangeReentrantLock.getLock().isKeyLocked(currentSolverId);
    }
    
    /**
     * 取消(停止)排课
     * @param currentSolverId
     */
    public static void stopSolver(String currentSolverId) {
        synchronized (ArrangeSingleSolver.class) {
        	RedisUtils.set(STOP_SOLVER_KEY+currentSolverId, currentSolverId);
            ArrangeReentrantLock.getLock().removeKey(currentSolverId);
        }
    }
    
    /**
     * action开始排
     * @param subjectIdTypeCapacityRangeMap
     */
    public void solve(final Map<String, ArrangeCapacityRange> subjectIdTypeCapacityRangeMap) {
        this.subjectIdTypeCapacityRangeMap = subjectIdTypeCapacityRangeMap;
        if (subjectIdTypeCapacityRangeMap == null) {
            throw new RuntimeException("班级人数范围集合没有传入！");
        }
        if (StringUtils.isBlank(currentSolverId)) {
            throw new RuntimeException("currentSolverId没有传入！");
        }
        int minCapacity = 0;
        int maxCapacity = 0;
        allMinCapacity = Integer.MAX_VALUE;
        allMaxCapacity = 1;
        ArrangeCapacityRange capacityRange = null;
        for (Map.Entry<String, ArrangeCapacityRange> capacityRangeEntry: subjectIdTypeCapacityRangeMap.entrySet()) {
            capacityRange = capacityRangeEntry.getValue();
            minCapacity = capacityRange.getMinCapacity();
            maxCapacity = capacityRange.getMaxCapacity();
            if (minCapacity < 1 || maxCapacity < 1) {
                throw new RuntimeException("人数范围不能小于1!");
            }
            if (maxCapacity < minCapacity) {
                throw new RuntimeException("人数范围设置不正确!");
            }
            if (allMinCapacity > minCapacity) {
                allMinCapacity = minCapacity;
            }
            if (allMaxCapacity < maxCapacity) {
                allMaxCapacity = maxCapacity;
            }
        }
        if (batchMax < batchOrign) {
            throw new RuntimeException("设定"+BaseConstants.PC_KC+"小于走班"+BaseConstants.PC_KC+"!");
        }
        
        String subjectIdType = null;
        Set<String> classFixedSubjectIds = null;
        // 2+x的信息获取
        subjectIdTypeAdditionalStudentIdMap = new HashMap<String, Set<String>>();
        studentIdAdditionalClassIdMap = new HashMap<String, String>();
        classIdAdditionalStudentIdSetMap = new HashMap<String, Set<String>>();
        classIdAdditionalSubjectIndexSetMap = new HashMap<String, Set<Integer>>();
        arrangeClassMap = new HashMap<String, ArrangeClass>();
        classIdForFixedBatchCountMap = new HashMap<String, Integer>();
        int fixedBatchCount = 0;
        Set<String> additionalStudentIdSet = null;
        Set<String> choosedSubjects = null;
        List<String> allSubjectList = null;
        for (ArrangeClass arrangeClass : arrangeClassList) {
            classFixedSubjectIds = arrangeClass.getChoosedSubjects();
            for (String fixedSubjectId : classFixedSubjectIds) {
                subjectIdType = fixedSubjectId+SelectionConstants.TYPE_A;
                if (subjectIdTypeAdditionalStudentIdMap.containsKey(subjectIdType)) {
                    additionalStudentIdSet = subjectIdTypeAdditionalStudentIdMap.get(subjectIdType);
                } else {
                    additionalStudentIdSet = new HashSet<String>();
                    subjectIdTypeAdditionalStudentIdMap.put(subjectIdType, additionalStudentIdSet);
                }
                additionalStudentIdSet.addAll(arrangeClass.getStudentIds());
            }
            if (StringUtils.isBlank(arrangeClass.getClassId())) {
                arrangeClass.setClassId(UuidUtils.generateUuid());
            }
            for (String studentId : arrangeClass.getStudentIds()) {
                studentIdAdditionalClassIdMap.put(studentId, arrangeClass.getClassId());
            }
            classIdAdditionalStudentIdSetMap.put(arrangeClass.getClassId(), arrangeClass.getStudentIds());
            arrangeClassMap.put(arrangeClass.getClassId(), arrangeClass);
            
            fixedBatchCount = 0;
            choosedSubjects = arrangeClass.getChoosedSubjects();
            allSubjectList = studentMap.get(arrangeClass.getStudentIds().iterator().next()).getAllSubjectList();
            for (String choosedSubjectId : choosedSubjects) {
                if (allSubjectList.contains(choosedSubjectId)) {
                    fixedBatchCount++;
                }
            }
            classIdForFixedBatchCountMap.put(arrangeClass.getClassId(), fixedBatchCount);
        }

        Integer[] batchIndexs = new Integer[batchMax];
        for (int i = 0; i < batchIndexs.length; i++) {
            batchIndexs[i] = i;
        }
        // 排列种类list获取
        List<Integer[]> subjectBatchIndexList = ArrangeUtils.arrangementSelectInteger(batchIndexs, batchOrign);
        List<Integer[]> subjectBatchIndexListForFixedOne = ArrangeUtils.arrangementSelectInteger(batchIndexs, batchOrign-1);
        List<Integer[]> subjectBatchIndexListForFixedTwo = ArrangeUtils.arrangementSelectInteger(batchIndexs, batchOrign-2);
        
        final List<ArrangeSubjectBatch> subjectBatchList = new ArrayList<ArrangeSubjectBatch>(subjectBatchIndexList.size());
        final List<ArrangeSubjectBatch> subjectBatchListForFixedOne = new ArrayList<ArrangeSubjectBatch>(subjectBatchIndexListForFixedOne.size());
        final List<ArrangeSubjectBatch> subjectBatchListForFixedTwo = new ArrayList<ArrangeSubjectBatch>(subjectBatchIndexListForFixedTwo.size());
        
        ArrangeSubjectBatch subjectBatch = null;
        int id = 0;
        for (Integer[] subjectIdIndexs : subjectBatchIndexList) {
            subjectBatch = new ArrangeSubjectBatch();
            subjectBatch.setId(id);
            subjectBatch.setSubjectIdIndexs(subjectIdIndexs);
            subjectBatchList.add(subjectBatch);
            id++;
        }
        for (Integer[] subjectIdIndexs : subjectBatchIndexListForFixedOne) {
            subjectBatch = new ArrangeSubjectBatch();
            subjectBatch.setId(id);
            subjectBatch.setSubjectIdIndexs(subjectIdIndexs);
            subjectBatchListForFixedOne.add(subjectBatch);
            id++;
        }
        for (Integer[] subjectIdIndexs : subjectBatchIndexListForFixedTwo) {
            subjectBatch = new ArrangeSubjectBatch();
            subjectBatch.setId(id);
            subjectBatch.setSubjectIdIndexs(subjectIdIndexs);
            subjectBatchListForFixedTwo.add(subjectBatch);
            id++;
        }
        
        if (ArrangeReentrantLock.getLock().isKeyLocked(currentSolverId)) {
        	throw new RuntimeException("当前年级正在排班！");
        }
        Thread thread = new Thread(new Runnable() {
        	@Override
        	public void run() {
        		try {
        			if (ArrangeReentrantLock.getLock().tryLock(currentSolverId)) {
        				long stTime = System.currentTimeMillis();
        				try{
    						Thread stopThread = new Thread(new Runnable() {
    							@Override
    							public void run() {
    								try {
    									while (true) {
    										String currSolverId = RedisUtils.get(STOP_SOLVER_KEY+currentSolverId);
    										if(StringUtils.isNotBlank(currSolverId)){
    											List<Solver<SelectionScheduleSolution>> list = solverMap.get(currSolverId);
    											if(CollectionUtils.isNotEmpty(list)){
    												RedisUtils.del(STOP_SOLVER_KEY+currentSolverId);
    												for (Solver<SelectionScheduleSolution> item : list) {
    													if (!item.isTerminateEarly()) {
    														item.terminateEarly();
    													}
    												}
    												solverMap.remove(currSolverId);
    											}
    											return;
    										}else if(!ArrangeReentrantLock.getLock().isKeyLocked(currentSolverId)){
    											return;
    										}
    										Thread.sleep(500);
    									}
    								} catch (Exception e) {
    									e.printStackTrace();
    								}
    							}
    						});
    						stopThread.start();
        					//获得开班结果和相应的学生批次的可能性种类集合
					        List<Map<Integer, Map<String, Integer>>> setClassRoomList = setClassRoomList(subjectBatchList, subjectBatchListForFixedOne, subjectBatchListForFixedTwo);
					        if (!ArrangeReentrantLock.getLock().isKeyLocked(currentSolverId)) {
					        	return;
					        }
					        
				        	ArrangeCapacityRange arrangeCapacityRange = null;
					        //TODO 创建线程池，设置最大并发数
					        int runCom = 1;
					        logger.info("--- solve "+currentSolverId+"--- 创建线程池，设置最大并发数为"+runCom+"，准备开始并发计算 " + new Date() + "---");
					        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(runCom);
					        final List<Solver<SelectionScheduleSolution>> solverList = new ArrayList<Solver<SelectionScheduleSolution>>();
					        final List<Map<String, List<ArrangeClassRoom>>[]> roomMapList = new ArrayList<Map<String, List<ArrangeClassRoom>>[]>();
					        solverMap.put(currentSolverId, solverList);
					        List<Map<Integer, Map<String, Integer>>> newClassRoomList = new ArrayList<Map<Integer,Map<String,Integer>>>();
					        Set<Integer> linSet = new HashSet<Integer>();
					        int size = setClassRoomList.size();
					        while(newClassRoomList.size() < runCom && newClassRoomList.size() < size){
					        	int num = (int) (Math.random() * size);
					        	if(!linSet.contains(num)){
					        		logger.info("--- solve "+currentSolverId+"获取随机值"+num+",hashcode:"+setClassRoomList.get(num).hashCode());
					        		linSet.add(num);
					        		newClassRoomList.add(setClassRoomList.get(num));
					        	}
					        }
					        int runCount = newClassRoomList.size();
					        final String ssId = UuidUtils.generateUuid();
					        ArrangeReentrantLock.getLock().tryLockOneDay(ssId);
					        for (final Map<Integer, Map<String, Integer>> item : newClassRoomList) {
					        	fixedThreadPool.execute(new Runnable() {
									@Override
									public void run() {
										if(ArrangeReentrantLock.getLock().isKeyLocked(ssId)){//这个判断用于中途取消，不执行线程池中剩余任务
											try{
												SelectionScheduleSolution solution = new SelectionScheduleSolution();
												solution.setId(0);
												solution.setName(new Date() + "- 走班安排");
												solution.setStudentList(studentList);
												// 分配班级的信息放入类中，供分数计算那边调用
												ArrangeConstantInfo constantInfo = new ArrangeConstantInfo();
												constantInfo.setBatchSubjectIdTypeRoomCountMap(item);
												constantInfo.setSubjectIdTypeCapacityRangeMap(subjectIdTypeCapacityRangeMap);
												solution.setConstantInfo(constantInfo);
												
												logger.info("（hashcode："+item.hashCode()+"）--- solve "+currentSolverId+" start at " + new Date() + " ---");
//												for (SolverListener listener : listenerList) {
//													listener.solveStarted();
//												}
												long solveStart = System.currentTimeMillis();
												Solver<SelectionScheduleSolution> solverCopy = solverFactory.buildSolver();
												synchronized(solverList){
													solverList.add(solverCopy);
												}
												SelectionScheduleSolution bestSolution = solverCopy.solve(solution);
												long solveEnd = System.currentTimeMillis();
												
												if (!ArrangeReentrantLock.getLock().isKeyLocked(currentSolverId)) {
													// 中途取消
													logger.info("（hashcode："+item.hashCode()+"）--- solve "+currentSolverId+" cancelled ");
//													for (SolverListener listener : listenerList) {
//														listener.solveCancelled();
//													}
													if(ArrangeReentrantLock.getLock().isKeyLocked(ssId)){
														ArrangeReentrantLock.getLock().unLock(ssId);
													}
												} else {
													logger.info("（hashcode："+item.hashCode()+"）--- solve "+currentSolverId+" end HardScore:" + bestSolution.getScore().getHardScore() + " MediumScore:"
															+ bestSolution.getScore().getMediumScore() + " SoftScore:"
															+ bestSolution.getScore().getSoftScore() + " seconds---");
													
													if (bestSolution.getScore().getHardScore() < 0) {
//													if (false) {
														logger.info("（hashcode："+item.hashCode()+"）--- 有学生未排入班级---");
													} else {
//														synchronized(roomMapList){
//															roomMapList.add(ArrangeDtoConverter.convertToRoomResultMapArray(getResult(bestSolution.getStudentList())));
//														}
														synchronized(roomMapList){
															roomMapList.add(getResult(bestSolution.getStudentList()));
														}
													}
												}
												logger.info("（hashcode："+item.hashCode()+"）--- solve "+currentSolverId+" costs " + (solveEnd - solveStart) / 1000 + " seconds---");
											}catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
					        	 });
							}
					        //启动一次顺序关闭，执行以前提交的任务，但不接受新任务
					        fixedThreadPool.shutdown();
					        while (true) {
					        	long enTime = System.currentTimeMillis();
					        	logger.info("--- solve "+currentSolverId+" 线程池已运行"+((int)((enTime-stTime)/1000/60))+"分钟 "+"，线程总数"+runCount+"，正在执行线程数"+((ThreadPoolExecutor)fixedThreadPool).getActiveCount()+"，已执行线程数"+((ThreadPoolExecutor)fixedThreadPool).getCompletedTaskCount()+"，可用资源数"+roomMapList.size());
					            if (fixedThreadPool.isTerminated()) {
					                logger.info("--- solve "+currentSolverId+" 线程池执行完成！");
					                break;  
					            }
					            Thread.sleep(10000);
					        }
					        
					        if (!ArrangeReentrantLock.getLock().isKeyLocked(currentSolverId)) {
								// 中途取消
								logger.info("--- solve "+currentSolverId+" cancelled All!");
								for (SolverListener listener : listenerList) {
									listener.solveCancelled();
								}
							}else if(CollectionUtils.isNotEmpty(roomMapList)){
					        	Map<Integer,Map<String, List<ArrangeClassRoom>>[]> intMap = new HashMap<Integer, Map<String, List<ArrangeClassRoom>>[]>();
					        	Map<Integer,Integer> intFMap = new TreeMap<Integer, Integer>();
					        	//key subjectId+type
					        	Map<String,List<ArrangeClassRoom>> roomNumMap = new HashMap<String, List<ArrangeClassRoom>>();
					        	List<ArrangeClassRoom> linRoomList = null;
					        	for(Map<String, List<ArrangeClassRoom>>[] item : roomMapList){
					        		for(Map<String, List<ArrangeClassRoom>> itemMap : item){
					        			for(Map.Entry<String, List<ArrangeClassRoom>> entry : itemMap.entrySet()){
					        				for(ArrangeClassRoom roomItem : entry.getValue()){
					        					if("A".equals(roomItem.getRoomType())){
					        						linRoomList = roomNumMap.get(entry.getKey()+"A");
					        						if(linRoomList == null){
					        							linRoomList = new ArrayList<ArrangeClassRoom>();
					        							roomNumMap.put(entry.getKey()+"A", linRoomList);
					        						}
					        						linRoomList.add(roomItem);
					        					}else if("B".equals(roomItem.getRoomType())){
					        						linRoomList = roomNumMap.get(entry.getKey()+"B");
					        						if(linRoomList == null){
					        							linRoomList = new ArrayList<ArrangeClassRoom>();
					        							roomNumMap.put(entry.getKey()+"B", linRoomList);
					        						}
					        						linRoomList.add(roomItem);
					        					}
					        				}
					        			}
					        		}
					        		int roomScore = 0;
					        		intMap.put(item.hashCode(), item);
					        		//计算权衡分数
				        			for(Map.Entry<String, List<ArrangeClassRoom>> entry : roomNumMap.entrySet()){
				        				arrangeCapacityRange = subjectIdTypeCapacityRangeMap.get(entry.getKey());
				        				if(arrangeCapacityRange!=null){
				        					linRoomList = entry.getValue();
				        					for(ArrangeClassRoom roomItem : linRoomList){
				        						if(roomItem.getStudentList().size() < arrangeCapacityRange.getMinCapacity()){
				        							roomScore = roomScore - (arrangeCapacityRange.getMinCapacity() - roomItem.getStudentList().size());
				        						}else if(roomItem.getStudentList().size() > arrangeCapacityRange.getMaxCapacity()){
				        							roomScore = roomScore - (roomItem.getStudentList().size() - arrangeCapacityRange.getMaxCapacity());
				        						}
				        					}
				        					logger.info(entry.getKey()+":"+linRoomList.size()+":"+arrangeCapacityRange.getClassNum());
				        					roomScore = roomScore - 1000 * Math.abs(linRoomList.size() - arrangeCapacityRange.getClassNum());
				        				}else{
				        					throw new RuntimeException(entry.getKey()+"未找到分班设置！");
				        				}
				        			}
					        		logger.info("--- solve " + currentSolverId + " hashcode:"+item.hashCode()+",score:"+roomScore);
					        		intFMap.put(item.hashCode(), roomScore);
					        	}
					        	//排序按分数降序
					        	//这里将map.entrySet()转换成list
					            List<Map.Entry<Integer,Integer>> list = new ArrayList<Map.Entry<Integer,Integer>>(intFMap.entrySet());
					            //然后通过比较器来实现排序
					            Collections.sort(list,new Comparator<Map.Entry<Integer,Integer>>() {
					                //降序排序
					                public int compare(Entry<Integer, Integer> o1,
					                        Entry<Integer, Integer> o2) {
					                    return o2.getValue().compareTo(o1.getValue());
					                }
					            });
					        	//取分数最高的结果返回
					        	for (SolverListener listener : listenerList) {
					        		logger.info("--- solve " + currentSolverId + "最终分数："+list.get(0).getValue());
					        		Map<String, List<Room>>[] convertToRoomResultMapArray = ArrangeDtoConverter.convertToRoomResultMapArray(intMap.get(list.get(0).getKey()));
					        		//TODO
					        		RedisUtils.setObject("arrange.solver."+currentSolverId+"."+UuidUtils.generateUuid(), convertToRoomResultMapArray, RedisUtils.TIME_ONE_WEEK);
									listener.solveFinished(convertToRoomResultMapArray,classIdAdditionalSubjectIndexSetMap);
								}
					        }else{
								for (SolverListener listener : listenerList) {
									listener.onError(new RuntimeException("未找到合理的分班结果，请修改班级人数范围！"));
								}
					        }
        				}catch (Exception e) {
        					e.printStackTrace();
        					throw new RuntimeException(e.getMessage());
        				}finally {
                            ArrangeReentrantLock.getLock().unLock(currentSolverId);
                        }
                    } else {
                        throw new RuntimeException("当前年级已经在排班中！");
                    }
                }catch (Exception e) {
                    for (SolverListener listener : listenerList) {
                        listener.onError(e);
                    }
                }
            }
        });
		thread.start();
    }

    /**
     * 获取各个批次的班级列表Map
     * 
     * @param subjectIdByTypeStudentCountMap 
     * @param division
     * @return key：batchIndex value：subjectIdTypeList
     */
    private Map<Integer, List<String>> getClassRoomDistribution2(Map<String, Map<String, Integer>> subjectIdByTypeStudentCountMap, int division) {
        // key：批次 value:map（key:类型 value：班级数）
        Map<Integer, Map<String, Integer>> batchSubjectIdTypeRoomCountMap = new HashMap<Integer, Map<String, Integer>>();
        Map<String, Integer> roomTypeStudentCountMap = null;
        String subjectId = null;
        String subjectIdType = null;
        String roomType = null;
        Integer batchIndex = 0;
        int dived = 0;
        int minRemainder = 0;
        int minQuotient = 0;
        int maxRemainder = 0;
        int maxQuotient = 0;
        int studentCount = 0;
        Map<String, Integer> subjectIdTypeRoomCountMap = null;
        int roomCount = 0;
        ArrangeCapacityRange capacityRange = null;
        int minCapacity = 0;
        int maxCapacity = 0;
        int myDivision = 0;
        int startIndex = 0;
        
        for (Map.Entry<String, Map<String, Integer>> subjectIdByTypeCountEntry : subjectIdByTypeStudentCountMap.entrySet()) {
            subjectId = subjectIdByTypeCountEntry.getKey();
            roomTypeStudentCountMap = subjectIdByTypeCountEntry.getValue();
            for (Map.Entry<String, Integer> typeCountEntry : roomTypeStudentCountMap.entrySet()) {
                
                if (subjectId == null) {
                    continue;
                }
                roomType = typeCountEntry.getKey();
                subjectIdType = subjectId + roomType;
                studentCount = typeCountEntry.getValue();
                if (studentCount < 1) {
                    continue;
                }
                
                capacityRange = subjectIdTypeCapacityRangeMap.get(subjectIdType);
                if (capacityRange == null) {
                    logger.error("subjectIdType:" + subjectIdType + "科目范围没有传递");
                    throw new RuntimeException("科目范围没有传递");
                }
                minCapacity = capacityRange.getMinCapacity();
                maxCapacity = capacityRange.getMaxCapacity();
                minRemainder = studentCount % minCapacity;//学生数除以设置的人数下限 取余学生数
                minQuotient = studentCount / minCapacity;//学生数除以设置的人数下限 得到班级数
                maxRemainder = studentCount % maxCapacity;//学生数除以设置的人数上限 取余学生数
                maxQuotient = studentCount / maxCapacity;//学生数除以设置的人数上限 得到班级数
                if (minRemainder == 0) {
                    // 此项完美分班
                    dived = minQuotient;
                } else if (maxRemainder == 0) {
                    // 此项完美分班
                    dived = minQuotient;
                } else if ((maxCapacity / minCapacity == 1) &&  minQuotient > maxQuotient) {
                	// 人数上限比人数下限>=1倍 且 下限所得班级大于上限所得班级数时 例如 学生数为100人，上下限设置为45~60
                    // 此项完美分班
                    dived = maxQuotient + 1;
                } else {
                	// 例如 学生数为100人，上下限设置为60~65
                    // myDivision 和参数 division 相差1
                    myDivision = maxCapacity / minCapacity;
                    dived = maxQuotient + 1;
                    if (myDivision > division) {
                        dived += division;
                    }
                }
                if (dived == 0) {
                    dived = 1;
                }
                for (int i = 0; i < dived; i++) {
                    batchIndex = startIndex % batchMax;
                    startIndex++;
                    if (batchSubjectIdTypeRoomCountMap.containsKey(batchIndex)) {
                        subjectIdTypeRoomCountMap = batchSubjectIdTypeRoomCountMap.get(batchIndex);
                    } else {
                        subjectIdTypeRoomCountMap = new HashMap<String, Integer>();
                        batchSubjectIdTypeRoomCountMap.put(batchIndex, subjectIdTypeRoomCountMap);
                    }
                    if (subjectIdTypeRoomCountMap.containsKey(subjectIdType)) {
                        roomCount = subjectIdTypeRoomCountMap.get(subjectIdType);
                    } else {
                        roomCount = 0;
                    }
                    subjectIdTypeRoomCountMap.put(subjectIdType, roomCount + 1);
                }
            }
        }
        
        // key：batchIndex value：subjectIdTypeList
        Map<Integer, List<String>> batchSubjectIdTypeMap = new HashMap<Integer, List<String>>();
        for (Map.Entry<Integer, Map<String, Integer>> batchSubjectIdTypeRoomCountEntry : batchSubjectIdTypeRoomCountMap.entrySet()) {
            batchIndex = batchSubjectIdTypeRoomCountEntry.getKey();//取得批次
            List<String> subjectIdTypeList = new ArrayList<String>();
            subjectIdTypeRoomCountMap = batchSubjectIdTypeRoomCountEntry.getValue();
            for (Map.Entry<String, Integer> subjectIdTypeRoomCountEntry: subjectIdTypeRoomCountMap.entrySet()) {
                subjectIdType = subjectIdTypeRoomCountEntry.getKey();
                roomCount = subjectIdTypeRoomCountEntry.getValue();
                for (int i = 0; i < roomCount; i++) {
                    subjectIdTypeList.add(subjectIdType);
                }
            }
            batchSubjectIdTypeMap.put(batchIndex, subjectIdTypeList);
        }
        return batchSubjectIdTypeMap;
    }
   
    /**
     * 获取各个批次下的各科目班级数Map
     * @param subjectIdByTypeStudentCountMap
     * @param startIndex
     * @param step
     * @param division
     * @return key：批次 value:map（key:类型 value：班级数）
     */
    private Map<Integer, Map<String, Integer>> getClassRoomDistribution(Map<String, Map<String, Integer>> subjectIdByTypeStudentCountMap,
            int startIndex, int step, int division) {
        // key：批次 value:map（key:类型 value：班级数）
        Map<Integer, Map<String, Integer>> batchSubjectIdTypeRoomCountMap = new HashMap<Integer, Map<String, Integer>>();
        Map<String, Integer> roomTypeStudentCountMap = null;
        String subjectId = null;
        String subjectIdType = null;
        String roomType = null;
        Integer batchIndex = 0;
        int dived = 0;
        int minRemainder = 0;
        int minQuotient = 0;
        int maxRemainder = 0;
        int maxQuotient = 0;
        int studentCount = 0;
        if (step < 1) {
            step = 1;
        }
        Map<String, Integer> subjectIdTypeRoomCountMap = null;
        int roomCount = 0;
        ArrangeCapacityRange capacityRange = null;
        int minCapacity = 0;
        int maxCapacity = 0;
        int myDivision = 0;
        for (Map.Entry<String, Map<String, Integer>> subjectIdByTypeCountEntry : subjectIdByTypeStudentCountMap.entrySet()) {
            subjectId = subjectIdByTypeCountEntry.getKey();
            if (subjectId == null) {
                continue;
            }
            roomTypeStudentCountMap = subjectIdByTypeCountEntry.getValue();
            for (Map.Entry<String, Integer> typeCountEntry : roomTypeStudentCountMap.entrySet()) {
                roomType = typeCountEntry.getKey();
                subjectIdType = subjectId + roomType;
                studentCount = typeCountEntry.getValue();
                if (studentCount < 1) {
                    continue;
                }
                
                capacityRange = subjectIdTypeCapacityRangeMap.get(subjectIdType);
                if (capacityRange == null) {
                    logger.error("subjectIdType:" + subjectIdType + "科目范围没有传递");
                    throw new RuntimeException("科目范围没有传递");
                }
                minCapacity = capacityRange.getMinCapacity();
                maxCapacity = capacityRange.getMaxCapacity();
                minRemainder = studentCount % minCapacity;//学生数除以设置的人数下限 取余学生数
                minQuotient = studentCount / minCapacity;//学生数除以设置的人数下限 得到班级数
                maxRemainder = studentCount % maxCapacity;//学生数除以设置的人数上限 取余学生数
                maxQuotient = studentCount / maxCapacity;//学生数除以设置的人数上限 得到班级数
                if (minRemainder == 0) {
                    // 此项完美分班
                    dived = minQuotient;
                } else if (maxRemainder == 0) {
                    // 此项完美分班
                    dived = minQuotient;
                } else if ((maxCapacity / minCapacity == 1) &&  minQuotient > maxQuotient) {
                	// 人数上限比人数下限>=1倍 且 下限所得班级大于上限所得班级数时 例如 学生数为100人，上下限设置为45~60
                    // 此项完美分班
                    dived = maxQuotient + 1;
                } else {
                	// 例如 学生数为100人，上下限设置为60~65
                    // myDivision 和参数 division 相差1
                    myDivision = maxCapacity / minCapacity;
                    dived = maxQuotient + 1;
                    if (myDivision > division) {
                        dived += division;
                    }
                }
                if (dived == 0) {
                    dived = 1;
                }
                for (int i = 0; i < dived; i++) {
                    batchIndex = startIndex % batchMax;//批次
                    startIndex+=step;//加间隔数
                    if (batchSubjectIdTypeRoomCountMap.containsKey(batchIndex)) {
                        subjectIdTypeRoomCountMap = batchSubjectIdTypeRoomCountMap.get(batchIndex);
                    } else {
                        subjectIdTypeRoomCountMap = new HashMap<String, Integer>();
                        batchSubjectIdTypeRoomCountMap.put(batchIndex, subjectIdTypeRoomCountMap);
                    }
                    if (subjectIdTypeRoomCountMap.containsKey(subjectIdType)) {
                        roomCount = subjectIdTypeRoomCountMap.get(subjectIdType);
                    } else {
                        roomCount = 0;
                    }
                    subjectIdTypeRoomCountMap.put(subjectIdType, roomCount + 1);
                }
            }
        }
        return batchSubjectIdTypeRoomCountMap;
    }
    
    /**
     * 判断分班是否合理，使所有学生能够有班级上课
     * @param batchSubjectIdTypeRoomCountMap 批次科目对应的班级数
     * @param subjectBatchList 
     * @param subjectBatchListForFixedOne 
     * @param subjectBatchListForFixedTwo 
     * @return true有班级，false 没班级
     */
    @SuppressWarnings("unchecked")
	private boolean setStudentAvailableBatchIndexs(Map<Integer, Map<String, Integer>> batchSubjectIdTypeRoomCountMap,
            List<ArrangeSubjectBatch> subjectBatchList, List<ArrangeSubjectBatch> subjectBatchListForFixedOne, List<ArrangeSubjectBatch> subjectBatchListForFixedTwo) {
        Map<String, Integer> subjectIdTypeRoomCountMap = null;
        boolean batchIndexOk = false;
        Integer[] subjectIdIndexs = null;
        Integer subjectIdIndex = null;
        List<String> allSubjectIdTypeList = null;
        String subjectIdType = null;
        List<ArrangeSubjectBatch> availableBatchIndexsList = null;
        
        String studentId = null;
        //组合班id
        String newClassId = null;
        //key 组合班id 学生id value 学生2+x的2的时段
        Map<String, Map<String, Set<Set<Integer>>>> classStudentForFixedSubjectBatchMap = new HashMap<String, Map<String, Set<Set<Integer>>>>();
        Map<String, Set<Set<Integer>>> studentForFixedSubjectBatchMap = null;
        Set<Set<Integer>> additionalClassStudentBatchSet = null;
        
        Integer[] allBatchIndexs = new Integer[batchMax];
        for (int i = 0; i < allBatchIndexs.length; i++) {
            allBatchIndexs[i] = i;
        }
        List<Integer> allBatchIndexList = Arrays.asList(allBatchIndexs);
        boolean isForFixed = false;
        int fixedBatchCount = 0;
        List<ArrangeSubjectBatch> subjectBatchListForFixed = null;
        
        classIdAdditionalSubjectIndexSetMap.clear();
        for (ArrangeStudent arrangeStudent : studentList) {
            allSubjectIdTypeList = arrangeStudent.getAllSubjectIdTypeList();//学生需要上课的subjectId + type
            availableBatchIndexsList = new ArrayList<ArrangeSubjectBatch>();
            arrangeStudent.setAvailableBatchIndexsList(availableBatchIndexsList);//解的范围
            // 有定二的任何一门是开课的，true为有 开课，false都不开课
            isForFixed = false;
            if (studentIdAdditionalClassIdMap.containsKey(arrangeStudent.getStudentId())) {
                // 2+x班级
                newClassId = studentIdAdditionalClassIdMap.get(arrangeStudent.getStudentId());
                fixedBatchCount = classIdForFixedBatchCountMap.get(newClassId);
                if (fixedBatchCount > 0) {
                    isForFixed = true;
                } else {
                    // 2+x的2都不走班
                    classIdAdditionalSubjectIndexSetMap.put(newClassId, new HashSet<Integer>());
                }
            }
            // 有定二的任何一门是开课的，true为有 开课，false都不开课
            if (isForFixed) {
                //有定二的任何一门是开课的，true为有 开课
                if (fixedBatchCount == 1) {
                    // n-1的组合集合
                    subjectBatchListForFixed = subjectBatchListForFixedOne;
                } else {
                    // n-2的组合集合
                    subjectBatchListForFixed = subjectBatchListForFixedTwo;
                }
                
                for (ArrangeSubjectBatch subjectBatch : subjectBatchListForFixed) {
                    subjectIdIndexs = subjectBatch.getSubjectIdIndexs();//取出某个组合批次如n-2的组合，[1,3,4,5,7]
                    // 存在该科目的班级
                    batchIndexOk = false;
                    for (int i = 0; i < subjectIdIndexs.length; i++) {
                        subjectIdIndex = subjectIdIndexs[i];//取得批次
                        subjectIdType = allSubjectIdTypeList.get(i);//学生需要上课的subjectId + type---------TODO
                        if (subjectIdType == null) {
                            continue;
                        }
                        //批次科目对应的班级数Map
                        subjectIdTypeRoomCountMap = batchSubjectIdTypeRoomCountMap.get(subjectIdIndex);
                        if (subjectIdTypeRoomCountMap != null) {//判断该批次下是否存在该科目的班级
                            if (subjectIdTypeRoomCountMap.containsKey(subjectIdType)) {
                                batchIndexOk = true;
                            } else {
                                batchIndexOk = false;
                                break;
                            }
                        } else {
                            batchIndexOk = false;
                            break;
                        }
                    }
                    if (batchIndexOk) {
                    	// 所有批次满足该学生的选课情况
                        availableBatchIndexsList.add(subjectBatch);
                        // 班级
                        if (classStudentForFixedSubjectBatchMap.containsKey(newClassId)) {
                            studentForFixedSubjectBatchMap = classStudentForFixedSubjectBatchMap.get(newClassId);
                        } else {
                            studentForFixedSubjectBatchMap = new HashMap<String, Set<Set<Integer>>>();
                            classStudentForFixedSubjectBatchMap.put(newClassId, studentForFixedSubjectBatchMap);
                        }
                        //学生
                        studentId = arrangeStudent.getStudentId();
                        if (studentForFixedSubjectBatchMap.containsKey(studentId)) {
                            additionalClassStudentBatchSet = studentForFixedSubjectBatchMap.get(studentId);
                        } else {
                            additionalClassStudentBatchSet = new HashSet<Set<Integer>>();
                            studentForFixedSubjectBatchMap.put(studentId, additionalClassStudentBatchSet);
                        }
                        // 该学生上2+x的2的时段（全时段与上课时段的补集）
                        additionalClassStudentBatchSet.add(new HashSet<Integer>(CollectionUtils.disjunction(allBatchIndexList, Arrays.asList(subjectIdIndexs))));
                    }
                }
            } else {
                // 非2+x班级或2+x班级2都不开课
                for (ArrangeSubjectBatch subjectBatch : subjectBatchList) {
                    subjectIdIndexs = subjectBatch.getSubjectIdIndexs();
                    batchIndexOk = false;
                    for (int i = 0; i < subjectIdIndexs.length; i++) {
                        subjectIdIndex = subjectIdIndexs[i];//取得批次
                        subjectIdType = allSubjectIdTypeList.get(i);//学生需要上课的subjectId + type---------TODO
                        if (subjectIdType == null) {
                            continue;
                        }
                        //批次科目对应的班级数Map
                        subjectIdTypeRoomCountMap = batchSubjectIdTypeRoomCountMap.get(subjectIdIndex);
                        if (subjectIdTypeRoomCountMap != null) {
                            if (subjectIdTypeRoomCountMap.containsKey(subjectIdType)) {
                                batchIndexOk = true;
                            } else {
                                batchIndexOk = false;
                                break;
                            }
                        } else {
                            batchIndexOk = false;
                            break;
                        }
                    }
                    if (batchIndexOk) {
                        // 所有批次满足该学生的选课情况
                        availableBatchIndexsList.add(subjectBatch);
                    }
                }
            }
            if (availableBatchIndexsList.size() < 1) {
                // 班级分配不成功
                return false;
            }
        }
        
        // 存在2+x的情况
        if (classStudentForFixedSubjectBatchMap.size() > 0) {
            Set<Set<Integer>> studentBatchIndexForFixedSet = null;
            Set<String> studentIds = null;
            Set<Integer> choosenCommonFixedBatches = null;
            ArrangeStudent arrangeStudent = null;
            List<ArrangeSubjectBatch> studentSubjectBatchList = null;
            //循环组合班，需要所有组合班都满足条件
            for (Map.Entry<String, Map<String, Set<Set<Integer>>>> classStudentForFixedSubjectBatchEntry : classStudentForFixedSubjectBatchMap.entrySet()) {
            	// 组合班id
            	newClassId = classStudentForFixedSubjectBatchEntry.getKey();
                // 最终可用集合
                studentBatchIndexForFixedSet = null;
                // 班级内各学生对应的可用集合
                studentForFixedSubjectBatchMap = classStudentForFixedSubjectBatchEntry.getValue();
                //循环班内学生取得可用集合的交集，如果最终交集为空说明2+x分配不成功
                for (Map.Entry<String, Set<Set<Integer>>> additionalClassStudentBatchListEntry : studentForFixedSubjectBatchMap.entrySet()) {
                    // 学生
                    studentId = additionalClassStudentBatchListEntry.getKey();
                    additionalClassStudentBatchSet = additionalClassStudentBatchListEntry.getValue();
                    if (studentBatchIndexForFixedSet == null) {
                        studentBatchIndexForFixedSet = additionalClassStudentBatchSet;
                    } else {
                        // 这个班学生都可以在2的时间点上2或1（1是另外一个1不开课，剩下来的）的课的集合，取交集
                        studentBatchIndexForFixedSet = new HashSet<Set<Integer>>(CollectionUtils.intersection(studentBatchIndexForFixedSet, additionalClassStudentBatchSet));
                        if (CollectionUtils.isEmpty(studentBatchIndexForFixedSet)) {
                            // 2+x分配不成功
                            return false;
                        }
                    }
                }
                studentIds = classIdAdditionalStudentIdSetMap.get(newClassId);
                Object[] arrayObjects = studentBatchIndexForFixedSet.toArray();
                // 学生批次可能性数量
                int setSize = studentBatchIndexForFixedSet.size();
                List<Integer> studentBatchIndexList = new ArrayList<Integer>();
                for (int i = 0; i < setSize; i++) {
                    studentBatchIndexList.add(i);
                }
                /**
                 * TODO
                 * 打乱集合顺序
                 * 打乱前:
                 * [0, 1, 2, 3]
                 * 打乱后：
                 * [2, 1, 3, 0]
                 */
                Collections.shuffle(studentBatchIndexList);
                boolean isAllStudentIn = false;
                //循环所有可能结果
                for (Integer index : studentBatchIndexList) {
                	//不在该批次集合下上课
                    choosenCommonFixedBatches = (Set<Integer>) arrayObjects[index];
                    //学生是否全部符合
                    isAllStudentIn = true;
                    for (String studentIdInClass : studentIds) {
                        arrangeStudent = studentMap.get(studentIdInClass);
                        List<ArrangeSubjectBatch> newStudentSubjectBatchList = new ArrayList<ArrangeSubjectBatch>();
                        //循环学生可用批次集合
                        studentSubjectBatchList = arrangeStudent.getAvailableBatchIndexsList();
                        for (ArrangeSubjectBatch arrangeSubjectBatch : studentSubjectBatchList) {
                            // 2+x的学生上除了2的批次和随机取的2的批次的交集，为空，则该批次是符合要求的
                            if (CollectionUtils.isEmpty(CollectionUtils.intersection(Arrays.asList(arrangeSubjectBatch.getSubjectIdIndexs()), choosenCommonFixedBatches))) {
                                newStudentSubjectBatchList.add(arrangeSubjectBatch);
                            }
                        }
                        if (CollectionUtils.isEmpty(newStudentSubjectBatchList)) {
                        	//没有符合的结果
                            isAllStudentIn = false;
                            break;
                        }
                        //TODO 2+x最终解的范围
                        arrangeStudent.setAvailableBatchIndexsList(newStudentSubjectBatchList);
                    }
                    if (isAllStudentIn) {
                        classIdAdditionalSubjectIndexSetMap.put(newClassId, choosenCommonFixedBatches);
                        break;
                    }
                }
                if (!isAllStudentIn) {
                    return false;
                }
            }
        }
        return true;
    }
    

    /**
     * 获得开班结果和相应的学生批次的可能性种类集合
     * @param solution
     * @param subjectBatchList
     * @param subjectBatchListForFixedOne
     * @param subjectBatchListForFixedTwo
     * @return 
     */
    private List<Map<Integer, Map<String, Integer>>> setClassRoomList(List<ArrangeSubjectBatch> subjectBatchList,
            List<ArrangeSubjectBatch> subjectBatchListForFixedOne, List<ArrangeSubjectBatch> subjectBatchListForFixedTwo) {
        String subjectIdType = null;
        String roomType = null;
        List<String> studentAllSubjectList = null;
        Set<String> chooseSubjectIds = null;
        int studentCount = 0;
        // 科目id 班级类型A或B
        Map<String, Map<String, Integer>> subjectIdByTypeStudentCountMap = new HashMap<String, Map<String, Integer>>();
        Map<String, Integer> roomTypeStudentCountMap = null;
        
        Map<String, Integer> subjectIdRoomTypeStudentCountMap = new HashMap<String, Integer>();
        List<String> allSubjectIdTypeList = null;
        Set<String> allSubjectIdTypeSet = null;
        Set<String> additionalStudentIdSet = null;
        // 获得科目对应的人数
        for (ArrangeStudent arrangeStudent : studentList) {
            studentAllSubjectList = arrangeStudent.getAllSubjectList();
            chooseSubjectIds = arrangeStudent.getChooseSubjectIds();
            allSubjectIdTypeList = new ArrayList<String>();
            allSubjectIdTypeSet = new HashSet<String>();
            //循环每个学生
            for (String subjectId : studentAllSubjectList) {
                if (chooseSubjectIds.contains(subjectId)) {
                    roomType = SelectionConstants.TYPE_A;
                } else {
                    roomType = SelectionConstants.TYPE_B;
                }
                if (subjectId == null) {
                    subjectIdType = null;
                } else {
                    subjectIdType = subjectId + roomType;
                }
                //判断该科目中是否有2+x的学生
                if (subjectIdTypeAdditionalStudentIdMap.containsKey(subjectIdType)) {
                    additionalStudentIdSet = subjectIdTypeAdditionalStudentIdMap.get(subjectIdType);
                    //有这个学生的话跳过
                    if (additionalStudentIdSet.contains(arrangeStudent.getStudentId())) {
                        // 2+x中的2不计入新开教学班
                        continue;
                    }
                }
                
                allSubjectIdTypeList.add(subjectIdType);
                allSubjectIdTypeSet.add(subjectIdType);
                if (subjectIdByTypeStudentCountMap.containsKey(subjectId)) {
                    roomTypeStudentCountMap = subjectIdByTypeStudentCountMap.get(subjectId);
                } else {
                    roomTypeStudentCountMap = new LinkedHashMap<String, Integer>();
                    subjectIdByTypeStudentCountMap.put(subjectId, roomTypeStudentCountMap);
                }
                if (roomTypeStudentCountMap.containsKey(roomType)) {
                    studentCount = roomTypeStudentCountMap.get(roomType);
                } else {
                    studentCount = 0;
                }
                roomTypeStudentCountMap.put(roomType, studentCount + 1);

                if (subjectIdRoomTypeStudentCountMap.containsKey(subjectIdType)) {
                    studentCount = subjectIdRoomTypeStudentCountMap.get(subjectIdType);
                } else {
                    studentCount = 0;
                }
                // key：科目+类型，value：学生数量
                subjectIdRoomTypeStudentCountMap.put(subjectIdType, studentCount + 1);
            }
            arrangeStudent.setAllSubjectIdTypeList(allSubjectIdTypeList);
            arrangeStudent.setAllSubjectIdTypeSet(allSubjectIdTypeSet);
        }
        
        boolean studentAllFitIn = false;
        //key 批次   subjectId+type value数量
        Map<Integer, Map<String, Integer>> batchSubjectIdTypeRoomCountMap = null;
        logger.info("--- init student start --- ");
        long assignStudentClassRoomStart = System.currentTimeMillis();
        //所有科目中 最大的上限人数 除以 最小的上限人数
        int maxDivedToMin = allMaxCapacity / allMinCapacity;
        
        // division：人数范围差别大，导致班级范围差别大，循环班级数
        // startIndex：起始位置点
        // step：步进间隔，从1开始
        List<Map<Integer, Map<String, Integer>>> classGroupList = new ArrayList<Map<Integer,Map<String,Integer>>>();
        logger.info("准备计算完美分班："+maxDivedToMin+":"+batchMax);
        Set<Integer> intSet = new HashSet<Integer>();
//        GetPerfectSwitchNone:
        for (int division = 0; division < maxDivedToMin; division++) {
            for (int startIndex = 0; startIndex < batchMax; startIndex++) {
                for (int step = 1; step < batchMax; step++) {
                	if (!ArrangeReentrantLock.getLock().isKeyLocked(currentSolverId)) {
			        	return null;
			        }
                	logger.info("正在计算完美分班:" + division + ":"+ startIndex + ":" + step);
                	//获取各个批次下的各科目班级数Map
                    batchSubjectIdTypeRoomCountMap = getClassRoomDistribution(subjectIdByTypeStudentCountMap, startIndex, step, division);
                    //判断分班是否合理，使所有学生能够有班级上课
                    studentAllFitIn = setStudentAvailableBatchIndexs(batchSubjectIdTypeRoomCountMap, subjectBatchList, subjectBatchListForFixedOne, subjectBatchListForFixedTwo);
                    if (studentAllFitIn && !intSet.contains(batchSubjectIdTypeRoomCountMap.hashCode())) {
                    	intSet.add(batchSubjectIdTypeRoomCountMap.hashCode());
                    	logger.info("hashcode:"+batchSubjectIdTypeRoomCountMap.hashCode());
                    	classGroupList.add(batchSubjectIdTypeRoomCountMap);
                    	logger.info("分班合理数量:"+classGroupList.size());
                    }
                }
            }
        }
        if(CollectionUtils.isNotEmpty(classGroupList)){
        	studentAllFitIn = true;
        }else{
        	studentAllFitIn = false;
        }
        if (!studentAllFitIn) {
            //---不完美的话采用交换班级的形式计算
              Integer batchIndex = 0;
              List<String> subjectIdTypeList = null;
              Map<Integer, List<String>> batchSubjectIdTypeMap = null;
              Map<String, Integer> subjectIdTypeRoomCountMap = null;
              int roomCount = 0;
              
              // 不同科目班级交换一次批次，这样最多两门科目不完美分班
              GetUnperfectSwitchOne:
              for (int division = 0; division < maxDivedToMin; division++) {
            	  if (!ArrangeReentrantLock.getLock().isKeyLocked(currentSolverId)) {
			        	return null;
			        }
            	  //获取各个批次的班级列表Map
                  batchSubjectIdTypeMap = getClassRoomDistribution2(subjectIdByTypeStudentCountMap, division);
                  String[][] matrix = new String[batchMax][];//批次下对应哪些科目班级的二维数组
                  for (Map.Entry<Integer, List<String>> batchSubjectIdTypeEntry : batchSubjectIdTypeMap.entrySet()) {
                      batchIndex = batchSubjectIdTypeEntry.getKey();
                      subjectIdTypeList = batchSubjectIdTypeEntry.getValue();
                      matrix[batchIndex] = subjectIdTypeList.toArray(new String[0]);
                  }
                  
                  batchSubjectIdTypeRoomCountMap = new HashMap<Integer, Map<String, Integer>>();
                  //按批次个数循环
                  for (int n = 0; n < matrix.length; n++) {
                      String[] subjectIdTypeArray = matrix[n];//获取班级subjectId+type
                      if (batchSubjectIdTypeRoomCountMap.containsKey(n)) {
                          subjectIdTypeRoomCountMap = batchSubjectIdTypeRoomCountMap.get(n);
                      } else {
                          subjectIdTypeRoomCountMap = new HashMap<String, Integer>();
                          batchSubjectIdTypeRoomCountMap.put(n, subjectIdTypeRoomCountMap);
                      }
                      for (String subjectType : subjectIdTypeArray) {
                          if (subjectIdTypeRoomCountMap.containsKey(subjectType)) {
                              roomCount = subjectIdTypeRoomCountMap.get(subjectType);
                          } else {
                              roomCount = 0;
                          }
                          subjectIdTypeRoomCountMap.put(subjectType, roomCount + 1);
                      }
                  }
                  logger.info("准备计算不完美分班："+matrix.length);
                  //subjectIdTypeRoomCountMap key subjectId+type value 对应的班级数
                  //按批次个数循环
                  for (int i = 0; i < matrix.length; i++) {
                      String[] iSubjectType = matrix[i];
                      //循环该i批次后的几个批次
                      for (int j = i+1; j < matrix.length; j++) {
                          String[] jSubjectType = matrix[j];
                          //循环i这个批次里的班级
                          for (int k = 0; k < iSubjectType.length; k++) {
                        	  //循环j这个批次里的班级
                              for (int m = 0; m < jSubjectType.length; m++) {
                            	  if (!ArrangeReentrantLock.getLock().isKeyLocked(currentSolverId)) {
              			        	return null;
              			        }
                            	  logger.info("正在计算不完美分班:" + i + ":"+ j + ":" + k + ":" + m);
                            	  //一旦i中的班级和j中的班级subjectId+type不一样则进行交换
                                  if (!StringUtils.equals(iSubjectType[k], jSubjectType[m])) {
                                      // 不相等就交换
                                      String subjectIdTypeK = iSubjectType[k];
                                      String subjectIdTypeM = jSubjectType[m];

                                      Map<String, Integer> subjectIdTypeRoomCountMapI = batchSubjectIdTypeRoomCountMap.get(i);
                                      Map<String, Integer> subjectIdTypeRoomCountMapJ = batchSubjectIdTypeRoomCountMap.get(j);
                                      
                                      // start change 计算变动后的i和j批次的班级数
                                      // for k
                                      roomCount = subjectIdTypeRoomCountMapI.get(subjectIdTypeK);
                                      if (roomCount > 1) {
                                          subjectIdTypeRoomCountMapI.put(subjectIdTypeK, roomCount-1);
                                      } else {
                                          subjectIdTypeRoomCountMapI.remove(subjectIdTypeK);
                                      }
                                      if (subjectIdTypeRoomCountMapI.containsKey(subjectIdTypeM)) {
                                          roomCount = subjectIdTypeRoomCountMapI.get(subjectIdTypeM);
                                      } else {
                                          roomCount = 0;
                                      }
                                      subjectIdTypeRoomCountMapI.put(subjectIdTypeM, roomCount + 1);
                                      
                                      // for m
                                      roomCount = subjectIdTypeRoomCountMapJ.get(subjectIdTypeM);
                                      if (roomCount > 1) {
                                          subjectIdTypeRoomCountMapJ.put(subjectIdTypeM, roomCount-1);
                                      } else {
                                          subjectIdTypeRoomCountMapJ.remove(subjectIdTypeM);
                                      }
                                      if (subjectIdTypeRoomCountMapJ.containsKey(subjectIdTypeK)) {
                                          roomCount = subjectIdTypeRoomCountMapJ.get(subjectIdTypeK);
                                      } else {
                                          roomCount = 0;
                                      }
                                      subjectIdTypeRoomCountMapJ.put(subjectIdTypeK, roomCount + 1);
                                      // change finished
                                    //交换完后，判断这个分班是否对所有学生有效
                                      studentAllFitIn = setStudentAvailableBatchIndexs(batchSubjectIdTypeRoomCountMap, subjectBatchList, subjectBatchListForFixedOne, subjectBatchListForFixedTwo);
                                      if (studentAllFitIn) {
                                    	  	intSet.add(batchSubjectIdTypeRoomCountMap.hashCode());
	                                      	logger.info("hashcode:"+batchSubjectIdTypeRoomCountMap.hashCode());
	                                      	classGroupList.add(batchSubjectIdTypeRoomCountMap);
	                                      	logger.info("分班合理数量:"+classGroupList.size());
                                          break GetUnperfectSwitchOne;
                                      } else {
                                          // 交换后，还原回去
                                          // start restore
                                          // for k
                                          roomCount = subjectIdTypeRoomCountMapI.get(subjectIdTypeM);
                                          if (roomCount > 1) {
                                              subjectIdTypeRoomCountMapI.put(subjectIdTypeM, roomCount-1);
                                          } else {
                                              subjectIdTypeRoomCountMapI.remove(subjectIdTypeM);
                                          }
                                          if (subjectIdTypeRoomCountMapI.containsKey(subjectIdTypeK)) {
                                              roomCount = subjectIdTypeRoomCountMapI.get(subjectIdTypeK);
                                          } else {
                                              roomCount = 0;
                                          }
                                          subjectIdTypeRoomCountMapI.put(subjectIdTypeK, roomCount + 1);
                                          
                                          // for m
                                          roomCount = subjectIdTypeRoomCountMapJ.get(subjectIdTypeK);
                                          if (roomCount > 1) {
                                              subjectIdTypeRoomCountMapJ.put(subjectIdTypeK, roomCount-1);
                                          } else {
                                              subjectIdTypeRoomCountMapJ.remove(subjectIdTypeK);
                                          }
                                          if (subjectIdTypeRoomCountMapJ.containsKey(subjectIdTypeM)) {
                                              roomCount = subjectIdTypeRoomCountMapJ.get(subjectIdTypeM);
                                          } else {
                                              roomCount = 0;
                                          }
                                          subjectIdTypeRoomCountMapJ.put(subjectIdTypeM, roomCount + 1);
                                          // restore finished
                                      }
                                  }
                              }
                          }
                      }
                  }
              }
              if(CollectionUtils.isNotEmpty(classGroupList)){
              	studentAllFitIn = true;
              }else{
              	studentAllFitIn = false;
              }
              if (!studentAllFitIn) {
                  // 交换下去,不还原
                  GetUnperfectSwitchAll:
                  for (int division = 0; division < maxDivedToMin; division++) {
                	  if (!ArrangeReentrantLock.getLock().isKeyLocked(currentSolverId)) {
  			        	return null;
  			        }
                      batchSubjectIdTypeMap = getClassRoomDistribution2(subjectIdByTypeStudentCountMap, division);
                      String[][] matrix = new String[batchMax][];
                      for (Map.Entry<Integer, List<String>> batchSubjectIdTypeEntry : batchSubjectIdTypeMap.entrySet()) {
                          batchIndex = batchSubjectIdTypeEntry.getKey();
                          subjectIdTypeList = batchSubjectIdTypeEntry.getValue();
                          matrix[batchIndex] = subjectIdTypeList.toArray(new String[0]);
                      }
                      // key：批次 value:map（key:类型 value：班级数）
                      batchSubjectIdTypeRoomCountMap = new HashMap<Integer, Map<String, Integer>>();
                      for (int n = 0; n < matrix.length; n++) {
                          String[] subjectIdTypeArray = matrix[n];
                          if (batchSubjectIdTypeRoomCountMap.containsKey(n)) {
                              subjectIdTypeRoomCountMap = batchSubjectIdTypeRoomCountMap.get(n);
                          } else {
                              subjectIdTypeRoomCountMap = new HashMap<String, Integer>();
                              batchSubjectIdTypeRoomCountMap.put(n, subjectIdTypeRoomCountMap);
                          }
                          for (String subjectType : subjectIdTypeArray) {
                              if (subjectIdTypeRoomCountMap.containsKey(subjectType)) {
                                  roomCount = subjectIdTypeRoomCountMap.get(subjectType);
                              } else {
                                  roomCount = 0;
                              }
                              // key:类型 value：班级数
                              subjectIdTypeRoomCountMap.put(subjectType, roomCount + 1);
                          }
                      }
                      logger.info("(交换下去,不还原)准备计算不完美分班："+matrix.length);
                      for (int i = 0; i < matrix.length; i++) {
                          String[] iSubjectType = matrix[i];
                          for (int j = i+1; j < matrix.length; j++) {
                              String[] jSubjectType = matrix[j];
                              for (int k = 0; k < iSubjectType.length; k++) {
                                  for (int m = 0; m < jSubjectType.length; m++) {
                                	  if (!ArrangeReentrantLock.getLock().isKeyLocked(currentSolverId)) {
                  			        	return null;
                  			        }
                                	  logger.info("(交换下去,不还原)正在计算不完美分班:" + i + ":"+ j + ":" + k + ":" + m);
                                      if (!StringUtils.equals(iSubjectType[k], jSubjectType[m])) {
                                          // 不相等就交换
                                          String subjectIdTypeK = iSubjectType[k];
                                          String subjectIdTypeM = jSubjectType[m];
                                          // 交换
                                          iSubjectType[k] = subjectIdTypeM;
                                          jSubjectType[m] = subjectIdTypeK;
                                          Map<String, Integer> subjectIdTypeRoomCountMapI = batchSubjectIdTypeRoomCountMap.get(i);
                                          Map<String, Integer> subjectIdTypeRoomCountMapJ = batchSubjectIdTypeRoomCountMap.get(j);
                                          
                                          // 开始交换
                                          // for k
                                          roomCount = subjectIdTypeRoomCountMapI.get(subjectIdTypeK);
                                          if (roomCount > 1) {
                                              subjectIdTypeRoomCountMapI.put(subjectIdTypeK, roomCount-1);
                                          } else {
                                              subjectIdTypeRoomCountMapI.remove(subjectIdTypeK);
                                          }
                                          if (subjectIdTypeRoomCountMapI.containsKey(subjectIdTypeM)) {
                                              roomCount = subjectIdTypeRoomCountMapI.get(subjectIdTypeM);
                                          } else {
                                              roomCount = 0;
                                          }
                                          subjectIdTypeRoomCountMapI.put(subjectIdTypeM, roomCount + 1);
                                          
                                          // for m
                                          roomCount = subjectIdTypeRoomCountMapJ.get(subjectIdTypeM);
                                          if (roomCount > 1) {
                                              subjectIdTypeRoomCountMapJ.put(subjectIdTypeM, roomCount-1);
                                          } else {
                                              subjectIdTypeRoomCountMapJ.remove(subjectIdTypeM);
                                          }
                                          if (subjectIdTypeRoomCountMapJ.containsKey(subjectIdTypeK)) {
                                              roomCount = subjectIdTypeRoomCountMapJ.get(subjectIdTypeK);
                                          } else {
                                              roomCount = 0;
                                          }
                                          subjectIdTypeRoomCountMapJ.put(subjectIdTypeK, roomCount + 1);
                                          // change finished
                                          //交换完后，判断这个分班是否对所有学生有效
                                          studentAllFitIn = setStudentAvailableBatchIndexs(batchSubjectIdTypeRoomCountMap, subjectBatchList, subjectBatchListForFixedOne, subjectBatchListForFixedTwo);
                                          if (studentAllFitIn) {
                                        	  	intSet.add(batchSubjectIdTypeRoomCountMap.hashCode());
	                                          	logger.info("hashcode:"+batchSubjectIdTypeRoomCountMap.hashCode());
	                                          	classGroupList.add(batchSubjectIdTypeRoomCountMap);
	                                          	logger.info("分班合理数量:"+classGroupList.size());
                                              break GetUnperfectSwitchAll;
                                          }
                                      }
                                  }
                              }
                          }
                      }
                  }
              }
          }
        if(CollectionUtils.isNotEmpty(classGroupList)){
        	studentAllFitIn = true;
//        	batchSubjectIdTypeRoomCountMap = classGroupList.get((int)(Math.random()*(classGroupList.size()))); 
        }else{
        	studentAllFitIn = false;
        }
        long assignStudentClassRoomEnd = System.currentTimeMillis();
        logger.info("--- init student costs " + (assignStudentClassRoomEnd - assignStudentClassRoomStart) / 1000 + " seconds ---");
        if (!studentAllFitIn) {
            throw new RuntimeException("班级分配不成功，适当调整班级人数值");
        }
        return classGroupList;
//        // 分配班级的信息放入类中，供分数计算那边调用
//        ArrangeConstantInfo constantInfo = new ArrangeConstantInfo();
//        constantInfo.setBatchSubjectIdTypeRoomCountMap(batchSubjectIdTypeRoomCountMap);
//        constantInfo.setSubjectIdTypeCapacityRangeMap(subjectIdTypeCapacityRangeMap);
//        solution.setConstantInfo(constantInfo);
    }

    /**
     * 组装返回的解，生成教学班
     * @param students
     * @return
     */
    @SuppressWarnings("unchecked")
	public Map<String, List<ArrangeClassRoom>>[] getResult(List<ArrangeStudent> students) {
    	//获得每个批次，科目对于的学生人数Map
        Map<String, Map<String, List<ArrangeStudent>>>[] subjectIdByTypeStudentListMapArray = getArrangedStudentMapArray(students, batchMax);
        Map<String, Map<String, List<ArrangeStudent>>> subjectIdByTypeStudentListMap = null;
        Map<String, List<ArrangeStudent>> typeStudentListMap = null;
        List<ArrangeStudent> sameTypeStudentList = null;
        List<ArrangeStudent> sameTypeStudentListWithOutFixed = null;
        String subjectId = null;
        String roomType = null;
        String subjectIdType = null;
        int dived = 0;
        int count = 0;
        int minRemainder = 0;
        int minQuotient = 0;
        int maxRemainder = 0;
        int maxQuotient = 0;
        int[] divideArray = null;

        // key：subjectId value：roomList 科目对应的教学班
        Map<String, List<ArrangeClassRoom>>[] subjectIdRoomMapArray = new HashMap[batchMax];
        for (int i = 0; i < batchMax; i++) {
            subjectIdRoomMapArray[i] = new HashMap<String, List<ArrangeClassRoom>>();
        }
        Map<String, List<ArrangeClassRoom>> subjectIdRoomMap = null;
        List<ArrangeClassRoom> rooms = null;
        List<ArrangeStudent> roomStudentList = null;
        int startIndex = 0;
        int endIndex = 0;

        int roomNumIndex = 1;
        boolean perfect = false;
        int minCapacity = 0;
        int maxCapacity = 0;
        Set<String> additionalStudentIdSet = null;
        ArrangeCapacityRange capacityRange = null;
        ArrangeStudent linArrStu = null;
        //循环批次
        for (int i = 0; i < subjectIdByTypeStudentListMapArray.length; i++) {
            subjectIdByTypeStudentListMap = subjectIdByTypeStudentListMapArray[i];
            if (subjectIdByTypeStudentListMap == null) {
                continue;
            }
            subjectIdRoomMap = subjectIdRoomMapArray[i];
            roomNumIndex = 1;
            //循环科目
            for (Map.Entry<String, Map<String, List<ArrangeStudent>>> subjectIdByTypeStudentListEntry : subjectIdByTypeStudentListMap.entrySet()) {
                subjectId = subjectIdByTypeStudentListEntry.getKey();
                if (subjectId == null) {
                    continue;
                }
                typeStudentListMap = subjectIdByTypeStudentListEntry.getValue();
                //循环类型A或B
                for (Map.Entry<String, List<ArrangeStudent>> typeStudentListEntry : typeStudentListMap.entrySet()) {
                    roomType = typeStudentListEntry.getKey();//取得类型
                    sameTypeStudentList = typeStudentListEntry.getValue();//取得学生
                    count = sameTypeStudentList.size();
                    if (count < 1) {
                        continue;
                    }
                    subjectIdType = subjectId + roomType;
                    sameTypeStudentListWithOutFixed = sameTypeStudentList;
                    if (subjectIdTypeAdditionalStudentIdMap.containsKey(subjectIdType)) {
                        sameTypeStudentListWithOutFixed = new ArrayList<ArrangeStudent>();
                        //选了2+x的学生
                        additionalStudentIdSet = subjectIdTypeAdditionalStudentIdMap.get(subjectIdType);
                        //循环取得的学生
                        for (ArrangeStudent arrangeStudent : sameTypeStudentList) {
                            if (!additionalStudentIdSet.contains(arrangeStudent.getStudentId())) {
                                // 2+x中除了2的外，计入新开教学班
                                sameTypeStudentListWithOutFixed.add(arrangeStudent);
                            }
                        }
                        sameTypeStudentList = sameTypeStudentListWithOutFixed;
                        count = sameTypeStudentList.size();
                        if (count < 1) {
                            continue;
                        }
                    }
                    //取得班级人数上下限
                    capacityRange = subjectIdTypeCapacityRangeMap.get(subjectIdType);
                    if (capacityRange == null) {
                        logger.error("subjectIdType:" + subjectIdType + "科目范围没有传递");
                        throw new RuntimeException("科目范围没有传递");
                    }
                    minCapacity = capacityRange.getMinCapacity();
                    maxCapacity = capacityRange.getMaxCapacity();
                    
                    minRemainder = count % minCapacity;
                    minQuotient = count / minCapacity;
                    maxRemainder = count % maxCapacity;
                    maxQuotient = count / maxCapacity;
                    perfect = false;
                    if (minRemainder == 0) {
                        // 此项完美
                        dived = minQuotient;
                        perfect = true;
                    }
                    if (maxRemainder == 0) {
                        // 此项完美
                        dived = minQuotient;
                        perfect = true;
                    } else if (minQuotient > maxQuotient) {
                        // 此项完美
                        dived = maxQuotient + 1;
                        perfect = true;
                    } else {
                        dived = maxQuotient + 1;
                        perfect = false;
                    }
                    if (dived == 0) {
                        dived = 1;
                    }
                    //count为学生数
                    if (perfect) {
                        // 完美分班的话，平均分，例子：8人分3个班人数分别为 = 3，3，2
                        divideArray = ArrangeUtils.getBatchArray(dived, count);
                    } else {
                        // 优先前分，例子：10份，按4为一份 = 4，4，2
                        divideArray = ArrangeUtils.getFulledArray((minCapacity + maxCapacity) / 2, count);
                    }

                    if (subjectIdRoomMap.containsKey(subjectId)) {
                        rooms = subjectIdRoomMap.get(subjectId);
                    } else {
                        rooms = new ArrayList<ArrangeClassRoom>();
                        subjectIdRoomMap.put(subjectId, rooms);
                    }
//                    final String subjectIdCopy = subjectId;
//                    Collections.sort(sameTypeStudentList,new Comparator<ArrangeStudent>() {
//                    	//分数高低降序排序
//                    	public int compare(ArrangeStudent o1,
//                    			ArrangeStudent o2) {
////                    		Double double1 = o1.getStudentSubjectDto().getScoreMap().get(subjectIdCopy);
//                    		Double double1 = o1.getStudentSubjectDto().getScoreMap().get(Constant.GUID_ONE);
//                    		if(double1 == null){
//                    			double1 = 0.0;
//                    		}
////                    		Double double2 = o2.getStudentSubjectDto().getScoreMap().get(subjectIdCopy);
//                    		Double double2 = o2.getStudentSubjectDto().getScoreMap().get(Constant.GUID_ONE);
//                    		if(double2 == null){
//                    			double2 = 0.0;
//                    		}
//                    		return double2.compareTo(double1);
//                    	}
//                    });
                    startIndex = 0;
                    int roomNum = divideArray.length;//班级数量
                    int curIndex = 1;
                    // TODO 生成教学班级
                    for (int roomSize : divideArray) {
//                    	logger.info("班级数量：" + roomNum);
//                    	logger.info("教室人数：" + roomSize);
//                    	if(roomNum == 1){
//                    		roomStudentList = sameTypeStudentList;
//                    	}else{
//                    		roomStudentList = new ArrayList<ArrangeStudent>();
//                    		int sameTypeStuSize = sameTypeStudentList.size();
//                    		logger.info("现有人数："+sameTypeStuSize);
//                    		startIndex = -1;
//                    		//加入分数排序逻辑，如果开三个班级那么按123321这样取人
//                    		for(int stuInd = 0 ; stuInd < roomSize ; stuInd++){
//                    			boolean ifEven = (stuInd%2 == 0 ? true : false);//是否偶数
//                    			if(ifEven){
//                    				if(stuInd < sameTypeStuSize){
//                    					startIndex = startIndex+1;
//                    					if(startIndex < sameTypeStuSize){
//	                    					linArrStu = sameTypeStudentList.get(startIndex);
////	                    					Double double1 = linArrStu.getStudentSubjectDto().getScoreMap().get(subjectIdCopy);
////	                    					logger.info("index：" + startIndex+":"+(double1!=null?double1.intValue():0));
//                    					}
//                    				}
//                    			}else{
//                    				startIndex = startIndex+(roomNum*2-1);
//                    				if(startIndex < sameTypeStuSize){
//                    					linArrStu = sameTypeStudentList.get(startIndex);
////                    					Double double1 = linArrStu.getStudentSubjectDto().getScoreMap().get(subjectIdCopy);
////                    					logger.info("index：" + startIndex+":"+(double1!=null?double1.intValue():0));
//                    				}
//                    			}
//                    			roomStudentList.add(linArrStu);
//                    		}
//                    		logger.info("安排人数：" + roomStudentList.size());
//                    		sameTypeStudentList.removeAll(roomStudentList);
//                    		logger.info("剩余人数：" + sameTypeStudentList.size());
//                    		roomNum-=1;
//                    	}
                    	
                    	endIndex = startIndex + roomSize;
                        //截取学生个数放入班级
                        roomStudentList = ArrangeUtils.subList(sameTypeStudentList, startIndex, endIndex);
                        startIndex = endIndex;
                    	
                        //生成教学班放入之前的学生
                        ArrangeClassRoom room = new ArrangeClassRoom();
                        room.setBatch(i);
                        room.setSubjectId(subjectId);
                        room.setId(roomNumIndex);
                        room.setRoomType(roomType);
                        room.setStudentList(roomStudentList);
                        roomNumIndex++;
//                        if(roomNum!=1){
//                        	if(curIndex==1){
//                        		room.setLevel("A");
//                        	}else{
//                        		room.setLevel("B");
//                        	}
//                        }else{
//                        	//room.setLevel("B");
//                        }
//                        curIndex++;
                        rooms.add(room);
                    }
                }
            }
        }
        return subjectIdRoomMapArray;
    }
    

    /**
     * 获得每个批次，科目对于的学生人数Map
     * key ：subjectid value：map（key：type value studentlist）
     * 
     * @param students
     * @param batchAllSize
     * @return
     */
    @SuppressWarnings("unchecked")
	private Map<String, Map<String, List<ArrangeStudent>>>[] getArrangedStudentMapArray(List<ArrangeStudent> students, int batchAllSize) {
        Map<String, Map<String, List<ArrangeStudent>>>[] subjectIdByTypeStudentListMapArray = new HashMap[batchAllSize];
        Map<String, Map<String, List<ArrangeStudent>>> subjectIdByTypeStudentListMap = null;
        Map<String, List<ArrangeStudent>> typeStudentListMap = null;
        List<ArrangeStudent> sameTypeStudentList = null;
        List<String> allSubjectIds = null;
        String subjectId = null;
        String roomType = null;
        ArrangeSubjectBatch subjectBatch = null;
        Integer[] subjectBatchIndexs = null;
        Integer batchIndex = null;
        ArrangeClass arrangeClass = null;
        for (ArrangeStudent student : students) {
            allSubjectIds = student.getAllSubjectList();
            // 除去2+x的2的课程
            if (studentIdAdditionalClassIdMap.containsKey(student.getStudentId())) {
                arrangeClass = arrangeClassMap.get(studentIdAdditionalClassIdMap.get(student.getStudentId()));
                for (String choosedSubjectId : arrangeClass.getChoosedSubjects()) {
                    allSubjectIds.remove(choosedSubjectId);
                }
            }
            if (CollectionUtils.isEmpty(allSubjectIds)) {
                continue;
            }
            subjectBatch = student.getSubjectBatch();
            if (subjectBatch == null) {
                continue;
            }
            subjectBatchIndexs = subjectBatch.getSubjectIdIndexs();
            for (int i = 0; i < subjectBatchIndexs.length; i++) {
            	subjectId = allSubjectIds.get(i);//需要走班的科目
                batchIndex = subjectBatchIndexs[i];//需要走班的科目对应的批次
                subjectIdByTypeStudentListMap = subjectIdByTypeStudentListMapArray[batchIndex];//批次对应的科目+type对应的学生

                if (subjectId != null) {
                    if (subjectIdByTypeStudentListMap == null) {
                        subjectIdByTypeStudentListMap = new HashMap<String, Map<String, List<ArrangeStudent>>>();
                        subjectIdByTypeStudentListMapArray[batchIndex] = subjectIdByTypeStudentListMap;
                    }
                    if (student.getChooseSubjectIds().contains(subjectId)) {
                        roomType = SelectionConstants.TYPE_A;
                    } else {
                        roomType = SelectionConstants.TYPE_B;
                    }

                    if (subjectIdByTypeStudentListMap.containsKey(subjectId)) {
                        typeStudentListMap = subjectIdByTypeStudentListMap.get(subjectId);
                    } else {
                        typeStudentListMap = new HashMap<String, List<ArrangeStudent>>();
                        subjectIdByTypeStudentListMap.put(subjectId, typeStudentListMap);
                    }
                    if (typeStudentListMap.containsKey(roomType)) {
                        sameTypeStudentList = typeStudentListMap.get(roomType);
                    } else {
                        sameTypeStudentList = new ArrayList<ArrangeStudent>();
                        typeStudentListMap.put(roomType, sameTypeStudentList);
                    }
                    sameTypeStudentList.add(student);
                }
            }
        }
        return subjectIdByTypeStudentListMapArray;
    }
    
    /**
     * 获取currentSolverId
     * @return currentSolverId
     */
    public String getCurrentSolverId() {
        return currentSolverId;
    }

    /**
     * 设置currentSolverId
     * @param currentSolverId currentSolverId
     */
    public void setCurrentSolverId(String currentSolverId) {
        this.currentSolverId = currentSolverId;
    }

}
