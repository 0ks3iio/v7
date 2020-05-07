package net.zdsoft.newgkelective.data.optaplanner.solver;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;

import net.zdsoft.newgkelective.data.optaplanner.domain.batch.BatchEntity;
import net.zdsoft.newgkelective.data.optaplanner.domain.batch.BatchLecture;
import net.zdsoft.newgkelective.data.optaplanner.domain.batch.BatchPeriod;
import net.zdsoft.newgkelective.data.optaplanner.domain.batch.BatchScheduleSolution;
import net.zdsoft.newgkelective.data.optaplanner.dto.BatchInputDto;
import net.zdsoft.newgkelective.data.optaplanner.util.ScheduleUtils;
import net.zdsoft.newgkelective.data.optaplanner.util.XmlUtils;

public class BatchSolverUtils {
	private static SolverFactory<BatchScheduleSolution> solverFactory = SolverFactory
			.createFromXmlResource("businessconf/solverv3/batch/batchSolverConfig.xml");
	private static final String DELIMITER = "-";
	private static String absPath = "C:\\Users\\user\\Desktop\\xml排课结果\\";
	
	public static void main(String[] args) {
		String fileName = "2018-2019学年合肥第2学期排课特征6 课表设置 2019-5-7 17-32-00.xml";
		BatchInputDto inputDto = null;
		
		BatchInputDto solve = solve(inputDto);
		XmlUtils.toXMLFile(solve, absPath, getOutputFilename(fileName));
		System.out.println("结果导出完毕");
	}
	
	public static BatchInputDto solve(BatchInputDto inputDto) {
		BatchScheduleSolution batchSolution = transfer(inputDto);
		boolean log = false;
//		solverFactory = SolverFactory
//				.createFromXmlResource("businessconf/solverv3/batch/batchSolverConfig.xml");
		
		long st = System.currentTimeMillis();
		Solver<BatchScheduleSolution> solver = solverFactory.buildSolver();
		if(log) {
			solver.addEventListener(new SolverEventListener<BatchScheduleSolution>() {
				public void bestSolutionChanged(BestSolutionChangedEvent<BatchScheduleSolution> event) {
					System.out.println("hard[0]: " + event.getNewBestSolution().getScore().getHardScore(0) + 
							"  hard[1]: " + event.getNewBestSolution().getScore().getHardScore(1) +
							"  soft[0]: " + event.getNewBestSolution().getScore().getSoftScore(0)+
							"  soft[1]: " + event.getNewBestSolution().getScore().getSoftScore(1));
				}
			});
			System.out.println("初始化："+(System.currentTimeMillis()-st)+"ms");
		}

		st = System.currentTimeMillis();
		BatchScheduleSolution solution = solver.solve(batchSolution);
		if(log) {
			System.out.println("花费："+(System.currentTimeMillis()-st)+"ms");
			solution.printResult();
		}

		// 产生返回结果
		Map<BatchEntity, List<BatchLecture>> collect = solution.getBatchLectureList().stream().collect(Collectors.groupingBy(BatchLecture::getBatchEntity));
		Map<String,List<String>> batchResultMap = new HashMap<>();
		for (BatchEntity en : collect.keySet()) {
			List<BatchLecture> list = collect.get(en);

			List<String> times = list.stream().map(e->e.getPeriod().getPeriodCode()).collect(Collectors.toList());
			batchResultMap.put(en.getBatchStr(), times);
		}
		inputDto.setBatchDomainPeriodMap(batchResultMap);
		inputDto.setAllPeriods(null);
		inputDto.setBatchWorkTimeMap(null);
		
		int hard0 = solution.getScore().getHardScore(0);
		if(hard0 <0)
			return null;
		
		return inputDto;
	}

	private static BatchScheduleSolution transfer(BatchInputDto inputDto) {
		
		List<String> allPeriods = inputDto.getAllPeriods();
		Map<String, List<String>> batchDomainPeriodMap = inputDto.getBatchDomainPeriodMap();
		Map<String, Integer> batchWorkTimeMap = inputDto.getBatchWorkTimeMap();
		
		int id = 0;
		// period
		List<BatchPeriod> periodList = new ArrayList<>();
		Map<String,BatchPeriod> periodMap = new HashMap<>();
		BatchPeriod period = null;
		for (String p : allPeriods) {
			String[] split = p.split(DELIMITER);
			
			period = new BatchPeriod();
			period.setId(id++);
			period.setDay(Integer.parseInt(split[0]));
			period.setPeriodInterval(split[1]);
			period.setTimeslotIndex(Integer.parseInt(split[2]));
			periodList.add(period);
			periodMap.put(p, period);
		}
		
		// batchentity
		List<BatchEntity> batchEntityList = new ArrayList<>();
		List<BatchLecture> batchLectureList = new ArrayList<>();
		BatchLecture lecture = null;
		BatchEntity entity = null;
		for (String batchstr : batchDomainPeriodMap.keySet()) {
			List<String> list = batchDomainPeriodMap.get(batchstr);
			entity = new BatchEntity();
			entity.setId(id++);
			
			String[] split = batchstr.split(DELIMITER,2);
			entity.setSubjectType(split[0]);
			entity.setBatch(split[1]);
			
			List<BatchPeriod> pl = list.stream().filter(e->periodMap.containsKey(e)).map(e->periodMap.get(e)).collect(Collectors.toList());
			entity.setPeriodList(pl);
			
			Integer integer = batchWorkTimeMap.get(batchstr);
			if(integer == null) integer = 0;
			if(integer > pl.size())
				throw new RuntimeException("时间点不够，需要："+integer+"，只有："+pl.size());
			
			if(integer<1) {
				System.out.println("批次："+batchstr+" 没有课时为 0");
				continue;
			}
			
			entity.setLectureCount(integer);
			batchEntityList.add(entity);
			
			//lecture
			for (int i=0;i<integer;i++) {
				lecture = new BatchLecture();
				lecture.setId(id++);
				lecture.setBatchEntity(entity);
				lecture.setPeriod(ScheduleUtils.getRandomOne(pl));
				batchLectureList.add(lecture);
			}
		}
		
		long workdayCount = periodList.stream().map(e->e.getDay()).distinct().count();
		
		// 结果
		BatchScheduleSolution solution = new BatchScheduleSolution();
		solution.setArrayItemId(inputDto.getArrayItemId());
		solution.setBatchEntityList(batchEntityList);
		solution.setBatchLectureList(batchLectureList);
		solution.setPeriodList(periodList);
		solution.setWorkdayCount((int)workdayCount);
		
		return solution;
	}
	
	public static String getOutputFilename(String fileName) {
		fileName = fileName.replaceFirst("\\d{4}-\\d+-\\d+ \\d+-\\d+-\\d{1,2}.xml", "");
		fileName += "_result_" + new Date().toLocaleString()+".xml";
		
		fileName = fileName.replace(":", "-");
		return fileName;
	}
}
