package net.zdsoft.newgkelective.data.optaplanner.shuff.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zdsoft.newgkelective.data.optaplanner.bestsectioncount.api.BestSectionCount;
import net.zdsoft.newgkelective.data.optaplanner.bestsectioncount.api.BestSectionCountInput;
import net.zdsoft.newgkelective.data.optaplanner.common.CalculateSections;
import net.zdsoft.newgkelective.data.optaplanner.shuff.domain.Group;
import net.zdsoft.newgkelective.data.optaplanner.shuff.domain.GroupClass;
import net.zdsoft.newgkelective.data.optaplanner.shuff.domain.GroupSubject;
import net.zdsoft.newgkelective.data.optaplanner.shuff.domain.ShuffleSolution;
import net.zdsoft.newgkelective.data.optaplanner.shuff.domain.Subject;
import net.zdsoft.newgkelective.data.optaplanner.shuff.domain.TimeSlot;

public class ShuffleWork {
	static final Logger LOG = LoggerFactory.getLogger(ShuffleWork.class);
	int timeSlotCount = 3;
	String configFileName = "businessconf/solveShuff/shuffConfig.xml";
	private ShuffleInput shuffleInput;
	int roomNum;
	boolean isShowScore;

	public ShuffleWork(ShuffleInput shuffleInput,int roomNum,int timeSlotCount,boolean isShowScore) {
		this.shuffleInput = shuffleInput;
		this.roomNum=roomNum;
		this.timeSlotCount=timeSlotCount;
		this.isShowScore=isShowScore;
	}

	public List<ShuffleResult> shuffleSections(){
		ShuffleSolution solution = new ShuffleSolution();
		try {
			initShuffleSolution(solution);
			ShuffleSolution resultSolution = solve(solution, configFileName);
			List<ShuffleResult> resultList = resultSolution.composeResult();
			return resultList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	public void initShuffleSolution(ShuffleSolution solution) throws IOException {
		 solution.setId(1);
		 solution.setAppName("分批次");
		 List<Group> groupList=new ArrayList<>();
		 List<GroupClass> groupClassList=new ArrayList<>();
		 List<Subject> subjectList=new ArrayList<>();
		 List<GroupSubject> groupSubjectList=new ArrayList<>();
		 List<TimeSlot> timeSlotList=new ArrayList<>();
		 //1:批次
		 TimeSlot t=null;
		 Map<Integer,TimeSlot> tMap=new HashMap<>();
		 for(int i = 1;i<=timeSlotCount;i++) {
			   t=new TimeSlot();
			   t.setGroupSubject(new ArrayList<>());
			   t.setId(i);
			   t.setTimeSlotName("T"+i);
			   t.setGroupSubject(new ArrayList<>());
			   timeSlotList.add(t);
			   tMap.put(i, t);
		 }
		 List<ShuffleResult> resultList = shuffleInput.getResultList();
		 GroupSubject groupSubject;
		 Subject subject;
		 Group group;
		 GroupClass groupClass;
		 //key:subjectId
		 Map<String,Subject> subjectMap=new HashMap<>();
		 //key:subjectId 人数
		 Map<String,Integer> subjectNum=new HashMap<>();
		 
		 Map<String,GroupClass> groupClassMap=new HashMap<>();
		 int index=1;
		 for(ShuffleResult s:resultList) {
			 String[] slist = s.getSubjectIds().split(",");
			 //乱序
			 Collections.shuffle(timeSlotList);
			 int i=0;
			 
			 group = new Group();
			 groupClass=groupClassMap.get(s.getClassId());
			 if(groupClass==null) {
				 groupClass=new GroupClass();
				 groupClass.setId(index);
				 index++;
				 groupClass.setClassId(s.getClassId());
				 if(StringUtils.isNotBlank(s.getSameSubjectIds())) {
					 String[] sameList = s.getSameSubjectIds().split(",");
					 groupClass.setSameSubjectId(Arrays.asList(sameList));
				 }else {
					 groupClass.setSameSubjectId(new ArrayList<>());
				 }
				 groupClassList.add(groupClass);
				 groupClassMap.put(s.getClassId(), groupClass);
			 }
			 group.setNameSubIds(s.getNameSubjectIds());
			 group.setGroupClass(groupClass);
			 group.setId(index);
			 index++;
			 group.setSubjectIds(Arrays.asList(slist));
			 groupList.add(group);
			 for(String ss:slist) {
				 groupSubject=new GroupSubject();
				 subject=subjectMap.get(ss);
				 if(subject==null) {
					 subject=new Subject();
					 subject.setId(index);
					 index++;
					 subject.setSubjectId(ss);
					 subjectMap.put(ss, subject);
					 subjectNum.put(ss, 0);
					 subjectList.add(subject);
				 }
				 groupSubject.setId(index);
				 index++;
				 
				 groupSubject.setSubject(subject);
				 groupSubject.setStuNum(s.getStudentNum());
				 
				 subjectNum.put(ss, subjectNum.get(ss)+s.getStudentNum());
				 
				 groupSubject.setTimeSlot(timeSlotList.get(i));
				 groupSubject.setTimeSlotDomain(new ArrayList<>());
				 groupSubject.getTimeSlotDomain().addAll(timeSlotList);
				 
				 groupSubject.setGroup(group);
				 
				 groupSubjectList.add(groupSubject);
				 timeSlotList.get(i).getGroupSubject().add(groupSubject);
				 i++;
			 }
		 }
		 //subjectNum
		 List<List<String>> list = calculateSectionSize2(subjectNum, roomNum);
		 if(isShowScore) {
			System.out.println("\n最后确认的教学班大小为[课程，平均大小，误差范围]：");
			list.stream().forEach(e -> System.out.println("\t" + e));
		 }
	     for(List<String> iitem:list) {
	    	 subjectMap.get(iitem.get(0)).setSectionSizeMean(Integer.parseInt(iitem.get(1)));
	    	 subjectMap.get(iitem.get(0)).setSectionSizeMargin(Integer.parseInt(iitem.get(2)));
	    }
	     
	     solution.setGroupClassList(groupClassList);
	     solution.setGroupList(groupList);
	     solution.setGroupSubjectList(groupSubjectList);
	     solution.setSubjectList(subjectList);
	     solution.setTimeSlotList(timeSlotList);
	}
	

	private ShuffleSolution solve(ShuffleSolution sectionSolution, String configFileName) throws IOException {
      SolverFactory<ShuffleSolution> solverFactory = SolverFactory.createFromXmlResource(configFileName);
      Solver<ShuffleSolution> solver = solverFactory.buildSolver();
	  solver.addEventListener(new SolverEventListener<ShuffleSolution>() {
	        public void bestSolutionChanged(BestSolutionChangedEvent<ShuffleSolution> event) {
				BendableScore score = (BendableScore) event.getNewBestScore();
				if(isShowScore){
					System.out.println("hard[0]: " + score.getHardScore(0) + 
							 "  hard[1]: " + score.getHardScore(1)+
							 "  soft[0]: " + score.getSoftScore(0)+
							 "  soft[1]: " + score.getSoftScore(1));
				}
				
	        }
	    });
      return (ShuffleSolution)solver.solve(sectionSolution);
   }

   /**
	 * 
	 * @param 学生选课列表，studentCourseSelectionList: {<studentID><选课1><选课2><选课3>...}
	 * @return 每门课的教学班大小，sectionSizeList: {<选课><每个教学班平均人数><误差大小>}
	 * @throws IOException 
	 */
	private static List<List<String>> calculateSectionSize2(Map<String, Integer> studentCountByCourse,int roomNumber) throws IOException {
		//需要返回的结果
		List<List<String>> sectionSizeList = new ArrayList<>();
		//{<课程名><学生人数>}整理成BestSectionCount算法的输入
		List<List<String>> courseStudentCountList = new ArrayList<>();
		for (Map.Entry<String, Integer> me : studentCountByCourse.entrySet()) {
			List<String> line = new ArrayList<>();
			line.add(me.getKey());
			line.add("" + me.getValue().intValue());
			courseStudentCountList.add(line);
		}
		
		//1. Prepare the input data
		BestSectionCountInput solutionInput = new BestSectionCountInput();
		//算法参数1：总的教室数量
		solutionInput.setMaxRoomCount(roomNumber);
		solutionInput.setTimeSlotCount(3); //3个时间点是常见的，如果是7选4的B课情况，这里应该是4
		solutionInput.setCourseStudentCountList(courseStudentCountList);
		
		//2. Create the launcher
		BestSectionCount launcher = new BestSectionCount(solutionInput);
		
		//3. Go Fly!
		//resultList: <课程名><开班数><学生数> 
		List<List<String>> resultList = launcher.calculateSectionCount();

		//DEBUG 打印出来看看
		System.out.println("优化算法计算的结果：");
		//printSectionSizeList(solutionInput, resultList);
		
		//计算sectionSizeList
		//resultList: <课程名><开班数><学生数>
		for (List<String> line : resultList) {
			//提取数据
			String courseName =  line.get(0);
			int sectionCount = Integer.parseInt(line.get(1));
			int studentCount = Integer.parseInt(line.get(2));
			
			//计算教学班大小
			List<Integer> sizeList = CalculateSections.calculateSectionsByKnownCount(studentCount, sectionCount);
			int sectionSizeMean = sizeList.get(sizeList.size()/2); //去中间值
			int sectionSizeMargin = (int) (sectionSizeMean * 0.15);
			
			//输出
			List<String> outputLine = new ArrayList<>();
			outputLine.add(courseName);
			outputLine.add(""+sectionSizeMean);
			outputLine.add(""+sectionSizeMargin);
			sectionSizeList.add(outputLine);
		}
		
		return sectionSizeList;
	}
	
	
}
