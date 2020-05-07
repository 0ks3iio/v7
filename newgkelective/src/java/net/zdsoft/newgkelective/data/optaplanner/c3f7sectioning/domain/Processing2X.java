package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.drools.compiler.lang.dsl.DSLMappingEntry.Section;
import org.jgrapht.alg.clique.DegeneracyBronKerboschCliqueFinder;
import org.jgrapht.alg.clique.PivotBronKerboschCliqueFinder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.CalculateSections;

public class Processing2X {
	SectionSolution solution;							//fact
	int roomCount;										//fact, parameter
//	int maxSectionSize;									//fact, parameter
//	int sectionSizeMargin = 10;							//fact, parameter
	
//	int sectionSizeMean;
//	int sectionSizeMargin;
	
	int solutionRetainCount = 100;						//fact, parameter == solutionPQ.size()
	
	List<Student> 			studentList;				//fact
	List<Course> 			courseList;					//fact
	Map<String, Curriculum> curriculumMap;				//fact
	List<Curriculum> 		allCurriculumList;			//fact

	
	List<BigCurriculum> 	bigCurriculumList;			//used by v1
	List<SmallCurriculum>	smallCurriculumList;		//used by v1
	List<SmallCurriculum>	noMatchSmallCurriculumList; //used by v1
	
	
	//for doit_v3()
	ArrayDeque<BigCurriculum>	combinationStack;
	ArrayDeque<BigCurriculum>	combinationRestStack; 
	Map<Curriculum, Boolean>  	combinedCurriculumMarkMap;
	PriorityQueue<CombinationSolutionItem> solutionPQ;
	
	//最后的结果汇总
	List<CourseSection>		allSectionList;

	
	class CombinationSolutionItem implements Comparable<CombinationSolutionItem> { 
		int score;
		List<List<BigCurriculum>> feasibleCombination;
		
		public CombinationSolutionItem(int score, List<BigCurriculum> bcList) {
			this.score = score;
			//TODO
		}
		
		public int compareTo(CombinationSolutionItem other) {
			return getScore() - other.getScore();
		}
		
		public int getScore() {
			return score;
		}
	}
	
	public Processing2X (SectionSolution s) {
		this.solution = s;
		this.roomCount = s.getRoomCount();
		//this.maxSectionSize = s.getMaxSectionSize();
		this.studentList = s.getStudentList();
		this.courseList = s.getCourseList();
		this.curriculumMap = s.getCurriculumMap();
		this.allCurriculumList = curriculumMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
		
		this.combinationStack = new ArrayDeque<BigCurriculum>();
		this.combinationRestStack = new ArrayDeque<BigCurriculum>();
		this.combinedCurriculumMarkMap = new HashMap<>();
	}
	
	public Map<String, Course> getCourseMap() {
		return solution.getCourseMap();
	}
	
	public void init_v1 () {
		bigCurriculumList = getCurriculumListWithMinStudentCount(solution.getSectionSizeMax()).stream().map(e -> new BigCurriculum(e, this)).collect(Collectors.toList());
		bigCurriculumList.sort((first, second) -> second.getMarginCountMax() - first.getMarginCountMax());
		//bigCurriculumList.stream().forEach(e -> e.setProcessing2X(this));
		smallCurriculumList = getCurriculumListWithMaxStudentCount(solution.getSectionSizeMax()).stream().map(e -> new SmallCurriculum(e, this)).collect(Collectors.toList());
		smallCurriculumList.sort((first, second) -> second.getStudentCount() - first.getStudentCount());
		//smallCurriculumList.stream().forEach(e -> e.setProcessing2X(this));
		
		noMatchSmallCurriculumList = new ArrayList<>();
	}
	
	//从小到大一个一个放：如果被放过的，就越过；如果是单个组合大于一个班的，只能被放。
	public void doit_v1_2 () {
		List<BigCurriculum> myBigCurriculumList = allCurriculumList.stream().map(e -> new BigCurriculum(e, this)).sorted((x, y) -> y.getStudentCount_v1() - x.getStudentCount_v1()).collect(Collectors.toList());
		
		for (int i = myBigCurriculumList.size() - 1; i >= 0; i --) {
			Curriculum curriculumTogo = myBigCurriculumList.get(i).getCurriculum();
			if (curriculumTogo.getStudentCount() >= solution.getSectionSizeMax()) {
				break;
			}
			if (myBigCurriculumList.get(i).getMatch2X_v1().size() > 0) {
				break;
			}
			List<BigCurriculum> matchedList = myBigCurriculumList.subList(0, i).stream().filter(e -> e.getCurriculum().isMatched2X(curriculumTogo)).sorted((x, y) -> x.getMarginCountMax() - y.getMarginCountMax()).collect(Collectors.toList());
			for (int j = 0; j < matchedList.size(); j ++) {
				if (matchedList.get(j).getMarginCountMax() >= curriculumTogo.getStudentCount()) {
					matchedList.get(j).addMatch2X_v1(new SmallCurriculum(curriculumTogo, this));
					myBigCurriculumList.remove(i);
					break;
				}
			}
		}
		
		myBigCurriculumList.stream().forEach(e -> e.do1X());
		
		//DEBUG
		//myBigCurriculumList.stream().forEach(e -> e.printSectionList());
		
		allSectionList = new ArrayList<>();
		myBigCurriculumList.stream().forEach(e -> allSectionList.addAll(e.getSectionList()));
		
		do1X_solidateSections();
		
//		int totalStudentCount = allSectionList.stream().map(e -> e.getStudentCount()).reduce(0, (x, y) -> x + y);
//		System.out.println("allSectionList.size = " + allSectionList.size() + " totalStudent# = " + totalStudentCount);
//		
//		allSectionList.stream().forEach(e -> System.out.println(e.getCourse().getCode() + ": " + e.getStudentCount()));
		
		solution.setSectionList(allSectionList);
		
	}

	//只考虑重合度，用贪心法。
	public void doit_v4 () {
		
		List<BigCurriculum> myBigCurriculumList = allCurriculumList.stream().sorted((x, y) -> x.getStudentCount() - y.getStudentCount()).map(e -> new BigCurriculum(e, this)).collect(Collectors.toList());

		combinationStack.clear();
		combinationRestStack.clear();
		List<BigCurriculum> tempList = new ArrayList<>();
		markAllFalse();
		BigCurriculum currentBC = null;
				
		while(myBigCurriculumList.size() > 0) {
			tempList.clear();
			updateMarkMapOnCurrentStack();
			myBigCurriculumList = myBigCurriculumList.stream().filter(e -> combinedCurriculumMarkMap.get(e.getCurriculum()) == Boolean.FALSE).collect(Collectors.toList());
			combinationRestStack.addAll(myBigCurriculumList);
			for (BigCurriculum bc : myBigCurriculumList) {
				generateFeasibleCombination(bc);
				sortFeasibleCombinationByMatchCourseAvgCount(bc);
				tempList.add(bc);
			}
			List<BigCurriculum> tempList2 = tempList.stream().filter(e -> e.hasNextCombinationCandidate()).collect(Collectors.toList());
			if (tempList2.size() == 0) {
				tempList2 = tempList.stream().filter(e -> !e.hasNextCombinationCandidate()).collect(Collectors.toList());
				currentBC = tempList2.get(0);
				System.out.println(currentBC.getCurriculum().getCode() + ": " + currentBC.peekCombinationCandidate().size());
				myBigCurriculumList.remove(currentBC);
				//myBigCurriculumList.removeAll(currentBC.peekCombinationCandidate());
				//currentBC.keepFirstCombinationCandidateOnly();
				System.out.println(currentBC.getCurriculum().getCode() + ": " + currentBC.peekCombinationCandidate().size());
				combinationStack.addLast(currentBC);
				combinationRestStack.clear();
			}
			else {
				tempList.sort((x, y) -> calculateCurrentMatchCourseAvgCount(y, y.peekCombinationCandidate()) - calculateCurrentMatchCourseAvgCount(x, x.peekCombinationCandidate()));
				currentBC = tempList.get(0);
				System.out.println(currentBC.getCurriculum().getCode() + ": " + currentBC.peekCombinationCandidate().size());
				myBigCurriculumList.remove(currentBC);
				myBigCurriculumList.removeAll(currentBC.peekCombinationCandidate());
				currentBC.keepFirstCombinationCandidateOnly();
				System.out.println(currentBC.getCurriculum().getCode() + ": " + currentBC.peekCombinationCandidate().size());
				combinationStack.addLast(currentBC);
				combinationRestStack.clear();
			}
			
		}
		printStack();
		
	}
	
	public void doit_v5 () {
		
		//doBalance_v5();
		List<BigCurriculum> myBigCurriculumList = allCurriculumList.stream().sorted((x, y) -> y.getStudentCount() - x.getStudentCount()).map(e -> new BigCurriculum(e, this)).collect(Collectors.toList());
		List<BigCurriculum> allBigCurriculumList = myBigCurriculumList.stream().collect(Collectors.toList());

		combinationStack.clear();
		combinationRestStack.clear();
		combinationRestStack.addAll(myBigCurriculumList);
		List<BigCurriculum> tempList = new ArrayList<>();
		markAllFalse();
		BigCurriculum currentBC = null;
				
		while(myBigCurriculumList.size() > 0) {
			tempList.clear();

			for (BigCurriculum bc : myBigCurriculumList) {
				generateFeasibleCombinationV5(bc);
				bc.sortByBestFit();
				tempList.add(bc);
			}
			
			List<BigCurriculum> tempList2 = tempList.stream().filter(e -> e.getMatchItemCount() > 0).collect(Collectors.toList());
			if (tempList2.size() == 0) {
				//都不能互相捆绑了，收工了
				combinationStack.addAll(combinationRestStack);
				combinationRestStack.clear();
				break;
			}
			else {
				tempList2.sort((x, y) -> x.bestMatchItemScore() - y.bestMatchItemScore());
				currentBC = tempList2.get(0);
				myBigCurriculumList.remove(currentBC);
				myBigCurriculumList.removeAll(currentBC.getMatchList().get(0));
				combinationStack.addLast(currentBC);
				combinationRestStack.clear();
			}
			updateMarkMapOnCurrentStackV5();
			myBigCurriculumList = myBigCurriculumList.stream().filter(e -> combinedCurriculumMarkMap.get(e.getCurriculum()) == Boolean.FALSE).collect(Collectors.toList());
			myBigCurriculumList.sort((x, y) -> y.getCurriculum().getStudentCount() - x.getCurriculum().getStudentCount());
			combinationRestStack.clear();
			combinationRestStack.addAll(myBigCurriculumList);
		}
		
		List<BigCurriculum> resultList = combinationStack.stream().collect(Collectors.toList());
		resultList.stream().forEach(e -> e.transferToV1());
		
		//printStackV5();
		
		resultList.stream().forEach(e -> e.do1X());
		
		//DEBUG
		//bigCurriculumList.stream().forEach(e -> e.printSectionList());
		
		allSectionList = new ArrayList<>();
		resultList.stream().forEach(e -> allSectionList.addAll(e.getSectionList()));
		//noMatchSmallCurriculumList.stream().forEach(e -> allSectionList.addAll(e.generateSectionList()));
		
		do1X_solidateSections();
		
//		int totalStudentCount = allSectionList.stream().map(e -> e.getStudentCount()).reduce(0, (x, y) -> x + y);
//		System.out.println("allSectionList.size = " + allSectionList.size() + " totalStudent# = " + totalStudentCount);
//		
//		allSectionList.stream().forEach(e -> System.out.println(e.getCourse().getCode() + ": " + e.getStudentCount()));
		
		solution.setSectionList(allSectionList);
		
		
	}
	
	//只考虑重合度，只要不爆仓就重合度越高越好，不在乎是否可以凑足一个最大的班，通过积分方式实现。
	//全空间搜索，计算量太大，放弃
	public void doit_v3 () {
		
		//定义了对Curriculum的考察顺序，从小到大
		List<BigCurriculum> myBigCurriculumList = allCurriculumList.stream().sorted((x, y) -> x.getStudentCount() - y.getStudentCount()).map(e -> new BigCurriculum(e, this)).collect(Collectors.toList());

		//生成第一个可行的捆绑集
		combinationStack.clear();
		combinationRestStack.clear();
		for (int i = 0; i < myBigCurriculumList.size(); i ++) {
			updateMarkMapOnCurrentStack();
			BigCurriculum currentBC = myBigCurriculumList.get(i);
			generateFeasibleCombination(currentBC);
			combinationStack.addLast(currentBC);
		}
		System.out.println("INFO: in doit_v3 (), combinationStack.size() = " + combinationStack.size());
		
		int i = 0;
		while (!isMissionComplete()) {
			reapSolution();
			moveOn();
			System.out.println("i = " + (++i));
			//if (i > 30) break;
		}
		
		//结果已经在solutionPQ中了
		
	}
	
	//current solution is in combinationStack
	private void reapSolution() {
		printCurrentSolution();
	}
	
	private void printStack() {
		System.out.println("combinationStack: " + combinationStack.size());
		for (BigCurriculum bc : combinationStack) {
			System.out.println("\t" + bc.getCurriculum().getCode() + " TotalStudent# = " + bc.getCurriculum().getStudentCount() + ", MarginCount# = " + bc.getCurriculum().getMarginCountAsBigCurriculum());
			for (BigCurriculum matched : bc.peekCombinationCandidate()) {
				System.out.println("\t\t" + matched.getCurriculum().getCode() + " Student# = " + matched.getCurriculum().getSurplusCountAsSmallCurriculum());
			}
		}
		System.out.println();

		System.out.println("combinationRestStack: " + combinationRestStack.size());
		for (BigCurriculum bc : combinationRestStack) {
			System.out.println("\t" + bc.getCurriculum().getCode());
		}
		System.out.println();
		
	}

	private void printStackV5() {
		System.out.println("combinationStack: " + combinationStack.size());
		for (BigCurriculum bc : combinationStack) {
			System.out.println("\t" + bc.getCurriculum().getCode() + " TotalStudent# = " + bc.getCurriculum().getStudentCount() + ", MarginCount# = " + bc.getCurriculum().getMarginCountAsBigCurriculum());
			if (bc.getMatchList().size() == 0) continue;
			for (BigCurriculum matched : bc.getMatchList().get(0)) {
				System.out.println("\t\t" + matched.getCurriculum().getCode() + " Student# = " + matched.getCurriculum().getSurplusCountAsSmallCurriculum());
			}
		}
		System.out.println();

		System.out.println("combinationRestStack: " + combinationRestStack.size());
		for (BigCurriculum bc : combinationRestStack) {
			System.out.println("\t" + bc.getCurriculum().getCode());
		}
		System.out.println();
		
	}
	
	private void printCurrentSolution() {
		System.out.println("BEGIN: printCurrentSolution()");
		updateMarkMapOnCurrentStack();
		for (BigCurriculum bc : combinationStack) {
			if (bc.hasNextCombinationCandidate()) {
				System.out.println(bc.getCurriculum().getCode() + ": " + bc.getCurriculum().getMarginCountAsBigCurriculum());
				List<BigCurriculum> matchedList = bc.peekCombinationCandidate();
				for (BigCurriculum bcMatched : matchedList) {
					System.out.println("\t" + bcMatched.getCurriculum().getCode() + ": " + bcMatched.getCurriculum().getSurplusCountAsSmallCurriculum());
				}
			}
			else if (combinedCurriculumMarkMap.get(bc.getCurriculum()) == Boolean.FALSE) { //single, 没有被捆绑
				System.out.println(bc.getCurriculum().getCode());
			}
			else {
				//已经被捆绑
			}
		}
		System.out.println("END: printCurrentSolution()\n\n");
	}
	
	private void moveOn() {
		
//		printStack();
		while (!combinationStack.peekLast().hasNextCombinationCandidate()) {
			combinationRestStack.addLast(combinationStack.removeLast());
//			printStack();
		}
		
		combinationStack.peekLast().nextCombinationCandidate();
//		printStack();
		
		int j = combinationRestStack.size();
		for (int i = 0; i < j; i ++) {
			updateMarkMapOnCurrentStack();
			BigCurriculum currentBC = combinationRestStack.removeLast();
			generateFeasibleCombination(currentBC);
			combinationStack.addLast(currentBC);
//			printStack();
		}
	}

	//俩俩捆绑时的共享课程个数的总和
	private int calculateCurrentMatchCourseCount (BigCurriculum baseBigCurriculum) {
		if (!baseBigCurriculum.hasNextCombinationCandidate()) {
			return 0;
		}
		
		int sharedCount = 0;
		List<BigCurriculum> allList = baseBigCurriculum.peekCombinationCandidate().stream().collect(Collectors.toList()); //make a copy
		allList.add(baseBigCurriculum);
		for (int i = 0; i < allList.size() - 1; i ++) {
			for (int j = i + 1; j < allList.size(); j ++) {
				sharedCount += allList.get(i).getCurriculum().sharedCourseCount(allList.get(j).getCurriculum());
			}
		}
		return sharedCount;
	}

	private int calculateCurrentMatchCourseAvgCount (BigCurriculum baseBigCurriculum, List<BigCurriculum> matchList) {
		if (!baseBigCurriculum.hasNextCombinationCandidate()) {
			return 0;
		}
		
		int sharedCount = 0;
		List<BigCurriculum> myList = matchList.stream().collect(Collectors.toList()); //make a copy
		myList.add(baseBigCurriculum);
		for (int i = 0; i < myList.size() - 1; i ++) {
			for (int j = i + 1; j < myList.size(); j ++) {
				sharedCount += myList.get(i).getCurriculum().sharedCourseCount(myList.get(j).getCurriculum());
			}
		}
		//return (int) (sharedCount * 1.0 / (matchList.size()) * 1000);
		return sharedCount;
	}
	
	private void sortFeasibleCombinationByMatchCourseAvgCount(BigCurriculum baseBigCurriculum) {
		List<List<BigCurriculum>> allMatchList = new ArrayList<>();
		while (baseBigCurriculum.hasNextCombinationCandidate()) {
			allMatchList.add(baseBigCurriculum.nextCombinationCandidate());
		}
		//allMatchList.sort((x, y) -> calculateCurrentMatchCourseAvgCount(baseBigCurriculum, x) - calculateCurrentMatchCourseAvgCount(baseBigCurriculum, y));
		allMatchList.sort((x, y) -> x.size() - y.size());
		baseBigCurriculum.clearCombinationCandidate();
		baseBigCurriculum.addCombinationCandidate(new ArrayList<BigCurriculum>());
		for (List<BigCurriculum> bcList : allMatchList) {
			baseBigCurriculum.addCombinationCandidate(bcList);
		}
	}
	
	//限制：只有小组合才会跟别的组合进行捆绑
	//另外，第一个是空的捆绑，以表示结束
	//为baseBigCurriculum 生成所有可行的捆绑：1、受baseBigCurriculum的marginCount限制；2、其它的都必须是小组合；3、其它的都还没有跟别的组合捆绑；
	private void generateFeasibleCombination(BigCurriculum baseBigCurriculum) {

		//清空一下
		baseBigCurriculum.clearCombinationCandidate();
		List<BigCurriculum> newItem = new ArrayList<BigCurriculum>();

		//case 0: 加入一个空的，表示待嫁而不是待娶
		baseBigCurriculum.addCombinationCandidate(newItem);
		
		//只考虑那些没有捆绑的小组合
		List<BigCurriculum> bigCurriculumList = combinationRestStack.stream().collect(Collectors.toList()); //bigCurriculumList应该包括了baseBigCurriculum
		List<BigCurriculum> consideredList = bigCurriculumList.stream().filter(e -> e.getCurriculum().isMatched2X(baseBigCurriculum.getCurriculum())).collect(Collectors.toList()); //consideredList 里应该没有 baseBigCurriculum
		int marginCount = baseBigCurriculum.getMarginCountMax(); 
		
		//case 1: 单个来捆绑
		for(int i = 0; i < consideredList.size(); i ++) {
			if (consideredList.get(i).getCurriculum().getStudentCount() <= marginCount) {
				newItem = new ArrayList<BigCurriculum>();
				newItem.add(consideredList.get(i));
				baseBigCurriculum.addCombinationCandidate(newItem);
			}
		}
		
		//case 2: 2个来捆绑
		for(int i = 0; i < consideredList.size() - 1; i ++) {
			for(int j = i + 1; j < consideredList.size(); j ++) {
				if (consideredList.get(i).getCurriculum().getStudentCount() + 
						consideredList.get(j).getCurriculum().getStudentCount() <= marginCount) {
					newItem = new ArrayList<BigCurriculum>();
					newItem.add(consideredList.get(i));
					newItem.add(consideredList.get(j));
					baseBigCurriculum.addCombinationCandidate(newItem);
				}
				
			}
		}

		//case 3: 3个来捆绑
		for(int i = 0; i < consideredList.size() - 2; i ++) {
			for(int j = i + 1; j < consideredList.size() - 1; j ++) {
				for(int k = j + 1; k < consideredList.size(); k ++) {
					if (consideredList.get(i).getCurriculum().getStudentCount() + 
							consideredList.get(j).getCurriculum().getStudentCount() +
							consideredList.get(k).getCurriculum().getStudentCount() <= marginCount) {
						newItem = new ArrayList<BigCurriculum>();
						newItem.add(consideredList.get(i));
						newItem.add(consideredList.get(j));
						newItem.add(consideredList.get(k));
						baseBigCurriculum.addCombinationCandidate(newItem);
					}
				}
			}
		}

		//case 4: 4个来捆绑
		for(int i = 0; i < consideredList.size() - 3; i ++) {
			for(int j = i + 1; j < consideredList.size() - 2; j ++) {
				for(int k = j + 1; k < consideredList.size() - 1; k ++) {
					for(int l = k + 1; l < consideredList.size(); l ++) {
						if (consideredList.get(i).getCurriculum().getStudentCount() + 
								consideredList.get(j).getCurriculum().getStudentCount() +
								consideredList.get(k).getCurriculum().getStudentCount() +
								consideredList.get(l).getCurriculum().getStudentCount() <= marginCount) {
							newItem = new ArrayList<BigCurriculum>();
							newItem.add(consideredList.get(i));
							newItem.add(consideredList.get(j));
							newItem.add(consideredList.get(k));
							newItem.add(consideredList.get(l));
							baseBigCurriculum.addCombinationCandidate(newItem);
						}
					}
				}
			}
		}

		//case 5: 5个来捆绑
		for(int i = 0; i < consideredList.size() - 4; i ++) {
			for(int j = i + 1; j < consideredList.size() - 3; j ++) {
				for(int k = j + 1; k < consideredList.size() - 2; k ++) {
					for(int l = k + 1; l < consideredList.size() - 1; l ++) {
						for(int m = l + 1; m < consideredList.size(); m ++) {
							if (consideredList.get(i).getCurriculum().getStudentCount() + 
									consideredList.get(j).getCurriculum().getStudentCount() +
									consideredList.get(k).getCurriculum().getStudentCount() +
									consideredList.get(l).getCurriculum().getStudentCount() +
									consideredList.get(m).getCurriculum().getStudentCount() <= marginCount) {
								newItem = new ArrayList<BigCurriculum>();
								newItem.add(consideredList.get(i));
								newItem.add(consideredList.get(j));
								newItem.add(consideredList.get(k));
								newItem.add(consideredList.get(l));
								newItem.add(consideredList.get(m));
								baseBigCurriculum.addCombinationCandidate(newItem);
							}
						}
					}
				}
			}
		}
		
		//case 6 and up: 暂时不考虑
		
	}

	//限制：只有小组合才会跟别的组合进行捆绑
	//另外，第一个是空的捆绑，以表示结束
	//为baseBigCurriculum 生成所有可行的捆绑：1、受baseBigCurriculum的marginCount限制；2、其它的都必须是小组合；3、其它的都还没有跟别的组合捆绑；
	private void generateFeasibleCombinationV5(BigCurriculum baseBigCurriculum) {

		//清空一下
		List<BigCurriculum> newItem = new ArrayList<>();
		List<List<BigCurriculum>> myMatchList = new ArrayList<>();
		
		baseBigCurriculum.setMatchList(myMatchList);

		//只考虑那些没有捆绑的小组合
		List<BigCurriculum> bigCurriculumList = combinationRestStack.stream().collect(Collectors.toList()); //bigCurriculumList应该包括了baseBigCurriculum
		List<BigCurriculum> consideredList = bigCurriculumList.stream().filter(e -> e.getCurriculum().isMatched2X(baseBigCurriculum.getCurriculum())).collect(Collectors.toList()); //consideredList 里应该没有 baseBigCurriculum
		int marginCount = baseBigCurriculum.getMarginCountMax(); 
		
		//case 1: 单个来捆绑
		for(int i = 0; i < consideredList.size(); i ++) {
			if (consideredList.get(i).getCurriculum().getStudentCount() <= marginCount) {
				newItem = new ArrayList<BigCurriculum>();
				newItem.add(consideredList.get(i));
				myMatchList.add(newItem);
			}
		}
		
		//case 2: 2个来捆绑
		for(int i = 0; i < consideredList.size() - 1; i ++) {
			for(int j = i + 1; j < consideredList.size(); j ++) {
				if (consideredList.get(i).getCurriculum().getStudentCount() + 
						consideredList.get(j).getCurriculum().getStudentCount() <= marginCount) {
					newItem = new ArrayList<BigCurriculum>();
					newItem.add(consideredList.get(i));
					newItem.add(consideredList.get(j));
					myMatchList.add(newItem);
				}
				
			}
		}

		//case 3: 3个来捆绑
		for(int i = 0; i < consideredList.size() - 2; i ++) {
			for(int j = i + 1; j < consideredList.size() - 1; j ++) {
				for(int k = j + 1; k < consideredList.size(); k ++) {
					if (consideredList.get(i).getCurriculum().getStudentCount() + 
							consideredList.get(j).getCurriculum().getStudentCount() +
							consideredList.get(k).getCurriculum().getStudentCount() <= marginCount) {
						newItem = new ArrayList<BigCurriculum>();
						newItem.add(consideredList.get(i));
						newItem.add(consideredList.get(j));
						newItem.add(consideredList.get(k));
						myMatchList.add(newItem);
					}
				}
			}
		}

		//case 4: 4个来捆绑
		for(int i = 0; i < consideredList.size() - 3; i ++) {
			for(int j = i + 1; j < consideredList.size() - 2; j ++) {
				for(int k = j + 1; k < consideredList.size() - 1; k ++) {
					for(int l = k + 1; l < consideredList.size(); l ++) {
						if (consideredList.get(i).getCurriculum().getStudentCount() + 
								consideredList.get(j).getCurriculum().getStudentCount() +
								consideredList.get(k).getCurriculum().getStudentCount() +
								consideredList.get(l).getCurriculum().getStudentCount() <= marginCount) {
							newItem = new ArrayList<BigCurriculum>();
							newItem.add(consideredList.get(i));
							newItem.add(consideredList.get(j));
							newItem.add(consideredList.get(k));
							newItem.add(consideredList.get(l));
							myMatchList.add(newItem);
						}
					}
				}
			}
		}

		//case 5: 5个来捆绑
		for(int i = 0; i < consideredList.size() - 4; i ++) {
			for(int j = i + 1; j < consideredList.size() - 3; j ++) {
				for(int k = j + 1; k < consideredList.size() - 2; k ++) {
					for(int l = k + 1; l < consideredList.size() - 1; l ++) {
						for(int m = l + 1; m < consideredList.size(); m ++) {
							if (consideredList.get(i).getCurriculum().getStudentCount() + 
									consideredList.get(j).getCurriculum().getStudentCount() +
									consideredList.get(k).getCurriculum().getStudentCount() +
									consideredList.get(l).getCurriculum().getStudentCount() +
									consideredList.get(m).getCurriculum().getStudentCount() <= marginCount) {
								newItem = new ArrayList<BigCurriculum>();
								newItem.add(consideredList.get(i));
								newItem.add(consideredList.get(j));
								newItem.add(consideredList.get(k));
								newItem.add(consideredList.get(l));
								newItem.add(consideredList.get(m));
								myMatchList.add(newItem);
							}
						}
					}
				}
			}
		}

		//case 6: 6个来捆绑
		for(int i = 0; i < consideredList.size() - 5; i ++) {
			for(int j = i + 1; j < consideredList.size() - 4; j ++) {
				for(int k = j + 1; k < consideredList.size() - 3; k ++) {
					for(int l = k + 1; l < consideredList.size() - 2; l ++) {
						for(int m = l + 1; m < consideredList.size() - 1; m ++) {
							for(int n = m + 1; n < consideredList.size(); n ++) {
								if (consideredList.get(i).getCurriculum().getStudentCount() + 
										consideredList.get(j).getCurriculum().getStudentCount() +
										consideredList.get(k).getCurriculum().getStudentCount() +
										consideredList.get(l).getCurriculum().getStudentCount() +
										consideredList.get(m).getCurriculum().getStudentCount() +
										consideredList.get(n).getCurriculum().getStudentCount() <= marginCount) {
									newItem = new ArrayList<BigCurriculum>();
									newItem.add(consideredList.get(i));
									newItem.add(consideredList.get(j));
									newItem.add(consideredList.get(k));
									newItem.add(consideredList.get(l));
									newItem.add(consideredList.get(m));
									newItem.add(consideredList.get(n));
									myMatchList.add(newItem);
								}
							}
						}
					}
				}
			}
		}

		//case 7: 7个来捆绑
		for(int i = 0; i < consideredList.size() - 6; i ++) {
			for(int j = i + 1; j < consideredList.size() - 5; j ++) {
				for(int k = j + 1; k < consideredList.size() - 4; k ++) {
					for(int l = k + 1; l < consideredList.size() - 3; l ++) {
						for(int m = l + 1; m < consideredList.size() - 2; m ++) {
							for(int n = m + 1; n < consideredList.size() - 1; n ++) {
								for(int o = n + 1; o < consideredList.size(); o ++) {
									if (consideredList.get(i).getCurriculum().getStudentCount() + 
											consideredList.get(j).getCurriculum().getStudentCount() +
											consideredList.get(k).getCurriculum().getStudentCount() +
											consideredList.get(l).getCurriculum().getStudentCount() +
											consideredList.get(m).getCurriculum().getStudentCount() +
											consideredList.get(n).getCurriculum().getStudentCount() +
											consideredList.get(o).getCurriculum().getStudentCount() <= marginCount) {
										newItem = new ArrayList<BigCurriculum>();
										newItem.add(consideredList.get(i));
										newItem.add(consideredList.get(j));
										newItem.add(consideredList.get(k));
										newItem.add(consideredList.get(l));
										newItem.add(consideredList.get(m));
										newItem.add(consideredList.get(n));
										newItem.add(consideredList.get(o));
										myMatchList.add(newItem);
									}
								}
							}
						}
					}
				}
			}
		}
		
		//case 8: 8个来捆绑
		for(int i = 0; i < consideredList.size() - 7; i ++) {
			for(int j = i + 1; j < consideredList.size() - 6; j ++) {
				for(int k = j + 1; k < consideredList.size() - 5; k ++) {
					for(int l = k + 1; l < consideredList.size() - 4; l ++) {
						for(int m = l + 1; m < consideredList.size() - 3; m ++) {
							for(int n = m + 1; n < consideredList.size() - 2; n ++) {
								for(int o = n + 1; o < consideredList.size() - 1; o ++) {
									for(int p = o + 1; p < consideredList.size(); p ++) {
										if (consideredList.get(i).getCurriculum().getStudentCount() + 
												consideredList.get(j).getCurriculum().getStudentCount() +
												consideredList.get(k).getCurriculum().getStudentCount() +
												consideredList.get(l).getCurriculum().getStudentCount() +
												consideredList.get(m).getCurriculum().getStudentCount() +
												consideredList.get(n).getCurriculum().getStudentCount() +
												consideredList.get(o).getCurriculum().getStudentCount() + 
												consideredList.get(p).getCurriculum().getStudentCount() <= marginCount) {
											newItem = new ArrayList<BigCurriculum>();
											newItem.add(consideredList.get(i));
											newItem.add(consideredList.get(j));
											newItem.add(consideredList.get(k));
											newItem.add(consideredList.get(l));
											newItem.add(consideredList.get(m));
											newItem.add(consideredList.get(n));
											newItem.add(consideredList.get(o));
											newItem.add(consideredList.get(p));
											myMatchList.add(newItem);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		//case 9~12: 暂时不考虑。理论上最多可以有12个可以捆绑的组合。
		
	}
	
	
	//重大假设：所有的组合都在combinationStack里了
	//所有的组合都是待嫁状态时，就表示完成了
	private boolean isMissionComplete () {
		
		if(allCurriculumList.size() != combinationStack.size()) {
			System.out.println("ERROR: in isMissionComplete (), allCurriculumList.size() != combinationStack.size()");
			System.out.println("\t allCurriculumList.size()=" + allCurriculumList.size() + ", combinationStack.size() = " + combinationStack.size());
		}
		
		boolean isComplete = true;
		
		List<BigCurriculum> myList = combinationStack.stream().collect(Collectors.toList());
		for (BigCurriculum bc : myList) {
			if (bc.hasNextCombinationCandidate()) {
				isComplete = false;
				break;
			}
		}
		return isComplete;
	}
	
	//标记所有的组合都是待嫁的
	private void markAllFalse() {
		combinedCurriculumMarkMap.clear();
		allCurriculumList.stream().forEach(e -> combinedCurriculumMarkMap.put(e, Boolean.FALSE));
	}
	
	//只有那些捆绑和被捆绑的，标记为TRUE；那些待嫁的保持FALSE
	private void updateMarkMapOnCurrentStack () {
		markAllFalse();
		List<BigCurriculum> bcList = combinationStack.stream().collect(Collectors.toList());
		for (BigCurriculum bc : bcList) {
			if (bc.hasNextCombinationCandidate()) {
				combinedCurriculumMarkMap.put(bc.getCurriculum(), Boolean.TRUE);
			}
			List<BigCurriculum> combinationCandidate = bc.peekCombinationCandidate();
			for (BigCurriculum cc : combinationCandidate) {
				combinedCurriculumMarkMap.put(cc.getCurriculum(), Boolean.TRUE);
			}
		}
	}

	//只有那些捆绑和被捆绑的，标记为TRUE；那些待嫁的保持FALSE
	private void updateMarkMapOnCurrentStackV5 () {
		markAllFalse();
		List<BigCurriculum> bcList = combinationStack.stream().collect(Collectors.toList());
		for (BigCurriculum bc : bcList) {
			combinedCurriculumMarkMap.put(bc.getCurriculum(), Boolean.TRUE);
			if (bc.getMatchList().size() == 0) {
				continue;
			}

			List<BigCurriculum> bestMatchItem = bc.getMatchList().get(0);
			for (BigCurriculum cc : bestMatchItem) {
				combinedCurriculumMarkMap.put(cc.getCurriculum(), Boolean.TRUE);
			}
		}
	}
	
	public void doit_v1 () {
		
		doBalance_v1();
//		//DEBUG
//		System.out.println("after doBalance: ");
//		printAll();
		
//		{
//			//int numStudentBC = bigCurriculumList.stream().map(e -> e.getCurriculum().getStudentList().size()).reduce(0, (x, y) -> x + y);
//			int numStudentBC = bigCurriculumList.stream().map(e -> e.getStudentCount_v1()).reduce(0, (x, y) -> x + y);
//			int numStudentSC = smallCurriculumList.stream().map(e -> e.getCurriculum().getStudentList().size()).reduce(0, (x, y) -> x + y);
//			System.out.println("numStudentBC = " + numStudentBC + ", numStudentSC = " + numStudentSC);
//		}
		
		bigCurriculumList.sort((x, y) -> y.getMarginCountMax() - x.getMarginCountMax());
		//bigCurriculumList.stream().forEach(e -> e.clearPossibleMatch2X());
		smallCurriculumList.sort((x, y) -> y.getStudentCount() - x.getStudentCount());
		
		List<BigCurriculum> tempMatched = new ArrayList<>();
		for (SmallCurriculum sc : smallCurriculumList) {
			tempMatched.clear();
			for (BigCurriculum bc : bigCurriculumList) {
				if ((bc.getCurriculum().isMatched2X(sc.getCurriculum())) && (bc.getMarginCountMax() >= sc.getStudentCount()) ) {
					tempMatched.add(bc);
				}
			}
			
			if (tempMatched.size() == 0) {
				System.out.println("INFO: can NOT find a match for SmallCurriculum " + sc.getCurriculum().getCode() + " studentCount = " + sc.getStudentCount() );
				noMatchSmallCurriculumList.add(sc);
//				for (BigCurriculum bc : bigCurriculumList) {
//					if (bc.getMatch2X_v1().size() == 0) {
//						continue;
//					}
//					else {
//						for (SmallCurriculum sc2 : bc.getMatch2X_v1()) {
//							if (sc2.getCurriculum().isMatched2X(sc.getCurriculum()) && bc.getMarginCountMax() >= sc.getStudentCount()) {
//								bc.getMatch2X_v1().add(sc2);
//							}
//						}
//					}
//				}
			}
			else if (tempMatched.size() == 1){
				tempMatched.get(0).addMatch2X_v1(sc);
			}
			else {
				tempMatched.sort((x, y) -> (y.getGroupCount() - y.getMatch2X_v1().size()) - (x.getGroupCount() - x.getMatch2X_v1().size()));
				tempMatched.get(0).addMatch2X_v1(sc);
			}
		}
		
//		System.out.println("after match: ");
//		bigCurriculumList.stream().forEach(e -> e.printInfo());
//		
		{
			int numStudentBC = bigCurriculumList.stream().map(e -> e.getStudentCount_v1()).reduce(0, (x, y) -> x + y);
			int numStudentSC = smallCurriculumList.stream().map(e -> e.getCurriculum().getStudentList().size()).reduce(0, (x, y) -> x + y);
			int numStudentNoMatch = noMatchSmallCurriculumList.stream().map(e -> e.getCurriculum().getStudentList().size()).reduce(0, (x, y) -> x + y);
			
			System.out.println("numStudentBC = " + numStudentBC + ", numStudentSC = " + numStudentSC + ", numStudentNoMatch = " + numStudentNoMatch);
		}

		
		bigCurriculumList.sort((x, y) -> y.getMarginCountMax() - x.getMarginCountMax());
		
		bigCurriculumList.stream().forEach(e -> e.do1X());
		
		//DEBUG
		//bigCurriculumList.stream().forEach(e -> e.printSectionList());
		
		allSectionList = new ArrayList<>();
		bigCurriculumList.stream().forEach(e -> allSectionList.addAll(e.getSectionList()));
		noMatchSmallCurriculumList.stream().forEach(e -> allSectionList.addAll(e.generateSectionList()));
		
//		int i = bigCurriculumList.stream().map(e -> e.getStudentCount_v1()).reduce(0, (x, y) -> x +y);
//		i += noMatchSmallCurriculumList.stream().map(e -> e.getStudentCount()).reduce(0, (x, y) -> x +y);
//		System.out.println("i = " + i);
		
//		int studentCnt = allSectionList.stream().map(e -> e.getStudentCount()).reduce(0, (x, y) -> x +y);
//		System.out.println("studentCnt = " + studentCnt);
		
		do1X_solidateSections();
		
//		int totalStudentCount = allSectionList.stream().map(e -> e.getStudentCount()).reduce(0, (x, y) -> x + y);
//		System.out.println("allSectionList.size = " + allSectionList.size() + " totalStudent# = " + totalStudentCount);
//		
//		allSectionList.stream().forEach(e -> System.out.println(e.getCourse().getCode() + ": " + e.getStudentCount()));
		
		solution.setSectionList(allSectionList);
		
	}

	//在v1的基础上，1、所有的小组合放到最后一个；2、最后组合小班的时候，用clique
	public void doit_v2 () {
		
		doBalance_v1();
//		//DEBUG
//		System.out.println("after doBalance: ");
//		printAll();
		
//		{
//			//int numStudentBC = bigCurriculumList.stream().map(e -> e.getCurriculum().getStudentList().size()).reduce(0, (x, y) -> x + y);
//			int numStudentBC = bigCurriculumList.stream().map(e -> e.getStudentCount_v1()).reduce(0, (x, y) -> x + y);
//			int numStudentSC = smallCurriculumList.stream().map(e -> e.getCurriculum().getStudentList().size()).reduce(0, (x, y) -> x + y);
//			System.out.println("numStudentBC = " + numStudentBC + ", numStudentSC = " + numStudentSC);
//		}
		
		bigCurriculumList.sort((x, y) -> y.getMarginCountMax() - x.getMarginCountMax());
		//bigCurriculumList.stream().forEach(e -> e.clearPossibleMatch2X());
		smallCurriculumList.sort((x, y) -> y.getStudentCount() - x.getStudentCount());
		
		List<BigCurriculum> tempMatched = new ArrayList<>();
		for (SmallCurriculum sc : smallCurriculumList) {
			tempMatched.clear();
			for (BigCurriculum bc : bigCurriculumList) {
				if ((bc.getCurriculum().isMatched2X(sc.getCurriculum())) && (bc.getMarginCountMax() >= sc.getStudentCount()) ) {
					tempMatched.add(bc);
				}
			}
			
			if (tempMatched.size() == 0) {
				//System.out.println("INFO: can NOT find a match for SmallCurriculum " + sc.getCurriculum().getCode() + " studentCount = " + sc.getStudentCount() );
				noMatchSmallCurriculumList.add(sc);
//				for (BigCurriculum bc : bigCurriculumList) {
//					if (bc.getMatch2X_v1().size() == 0) {
//						continue;
//					}
//					else {
//						for (SmallCurriculum sc2 : bc.getMatch2X_v1()) {
//							if (sc2.getCurriculum().isMatched2X(sc.getCurriculum()) && bc.getMarginCountMax() >= sc.getStudentCount()) {
//								bc.getMatch2X_v1().add(sc2);
//							}
//						}
//					}
//				}
			}
			else if (tempMatched.size() == 1){
				tempMatched.get(0).addMatch2X_v1(sc);
			}
			else {
				tempMatched.sort((x, y) -> (y.getGroupCount() - y.getMatch2X_v1().size()) - (x.getGroupCount() - x.getMatch2X_v1().size()));
				tempMatched.get(0).addMatch2X_v1(sc);
			}
		}
		
//		System.out.println("after match: ");
//		bigCurriculumList.stream().forEach(e -> e.printInfo());
//		
//		{
//			int numStudentBC = bigCurriculumList.stream().map(e -> e.getStudentCount_v1()).reduce(0, (x, y) -> x + y);
//			int numStudentSC = smallCurriculumList.stream().map(e -> e.getCurriculum().getStudentList().size()).reduce(0, (x, y) -> x + y);
//			int numStudentNoMatch = noMatchSmallCurriculumList.stream().map(e -> e.getCurriculum().getStudentList().size()).reduce(0, (x, y) -> x + y);
//			
//			System.out.println("Processing2X::doit_v2(): numStudentBC = " + numStudentBC + ", numStudentSC = " + numStudentSC + ", numStudentNoMatch = " + numStudentNoMatch);
//		}

		
		bigCurriculumList.sort((x, y) -> y.getMarginCountMax() - x.getMarginCountMax());
		
		bigCurriculumList.stream().forEach(e -> e.do1X_v2());
		
		//DEBUG
		//bigCurriculumList.stream().forEach(e -> e.printSectionList());
		
		allSectionList = new ArrayList<>();
		bigCurriculumList.stream().forEach(e -> allSectionList.addAll(e.getSectionList()));
		noMatchSmallCurriculumList.stream().forEach(e -> allSectionList.addAll(e.generateSectionList()));
		
//		int i = bigCurriculumList.stream().map(e -> e.getStudentCount_v1()).reduce(0, (x, y) -> x +y);
//		i += noMatchSmallCurriculumList.stream().map(e -> e.getStudentCount()).reduce(0, (x, y) -> x +y);
//		System.out.println("i = " + i);
		
//		int studentCnt = allSectionList.stream().map(e -> e.getStudentCount()).reduce(0, (x, y) -> x +y);
//		System.out.println("studentCnt = " + studentCnt);
		
		do1X_solidateSections_v2();
		
//		int totalStudentCount = allSectionList.stream().map(e -> e.getStudentCount()).reduce(0, (x, y) -> x + y);
//		System.out.println("allSectionList.size = " + allSectionList.size() + " totalStudent# = " + totalStudentCount);
//		
//		allSectionList.stream().forEach(e -> System.out.println(e.getCourse().getCode() + ": " + e.getStudentCount()));
		
		solution.setSectionList(allSectionList);
		
	}
	
	
	private void do1X_solidateSections() {
		
		//把手动分班剩下的小班拿过来
		allSectionList.addAll(solution.getPartialSectionList());
		
		//按课程
		Map<Course, List<CourseSection>> sectionGroupbyCourse = allSectionList.stream().collect(Collectors.groupingBy(CourseSection::getCourse));
		
		allSectionList.clear();
		sectionGroupbyCourse.entrySet().stream().forEach(e -> e.setValue(do1X_combineSections_v1(e.getValue())));
		//sectionGroupbyCourse.entrySet().stream().forEach(e -> {System.out.println(e.getKey()); e.getValue().stream().forEach(x -> System.out.println(x.getStudentCount()));});
		
		//把手动分班的班级拿过来
		for(CourseSection cs : solution.getFullSectionList()) {
			sectionGroupbyCourse.get(cs.getCourse()).add(cs);
		}
		
		//按课程，给教学班编号
		sectionGroupbyCourse.entrySet().stream().forEach(e -> setCourseSectionID(e.getValue()));
		
		//放回去
		sectionGroupbyCourse.entrySet().stream().forEach(e -> allSectionList.addAll(e.getValue()));
	}

	private void do1X_solidateSections_v2() {
		//把手动分班剩下的小班拿过来
		allSectionList.addAll(solution.getPartialSectionList());
		
		Map<Course, List<CourseSection>> sectionGroupbyCourse = allSectionList.stream().collect(Collectors.groupingBy(CourseSection::getCourse));
		allSectionList.clear();
		sectionGroupbyCourse.entrySet().stream().forEach(e -> e.setValue(do1X_combineSections_v3(e.getValue())));
		//sectionGroupbyCourse.entrySet().stream().forEach(e -> {System.out.println(e.getKey()); e.getValue().stream().forEach(x -> System.out.println(x.getStudentCount()));});

		//把手动分班的班级拿过来
		for(CourseSection cs : solution.getFullSectionList()) {
			sectionGroupbyCourse.get(cs.getCourse()).add(cs);
		}
		
		//按课程，给教学班编号
		sectionGroupbyCourse.entrySet().stream().forEach(e -> setCourseSectionID(e.getValue()));
		sectionGroupbyCourse.entrySet().stream().forEach(e -> allSectionList.addAll(e.getValue()));
	}
	
	private void setCourseSectionID (List<CourseSection> sections) {
		for (int i = 0; i < sections.size(); i ++) {
			sections.get(i).setSectionID(i + 1);
		}
	}
	
	//V1: 幼稚版
	private List<CourseSection> do1X_combineSections_v1(List<CourseSection> sections) {
		sections.sort((x, y) -> y.getStudentCount() - x.getStudentCount()); //从大到小
		for (int i = sections.size() - 1; i >= 0; i --) {
			for (int j = 0; j < i; j ++) {
				if (sections.get(i).getStudentCount() + sections.get(j).getStudentCount() <= solution.getSectionSizeMax() ) {
					sections.get(j).addStudentListAll(sections.get(i).getStudentList());
					sections.get(i).clearStudentAll();
					break;
				}
			}
		}
		
		List<CourseSection> r = sections.stream().filter(e -> e.getStudentCount() > 0).collect(Collectors.toList());
		return r;
	}

	//V2: 贪心版，非优化版
	private List<CourseSection> do1X_combineSections_v2(List<CourseSection> sections) {
		List<Integer> sectionSizeList = sections.stream().map(e -> new Integer(e.getStudentCount())).collect(Collectors.toList());
		sectionSizeList.sort((x, y) -> x.intValue() - y.intValue()); //从小到大
		int i = findSplitPosition(sectionSizeList);
		
		sections.sort((x, y) -> x.getStudentCount() - y.getStudentCount());
		List<CourseSection> smallSectionList = new ArrayList<>();
		smallSectionList.addAll(sections.subList(0, i));
		List<CourseSection> bigSectionList = new ArrayList<>();
		bigSectionList.addAll(sections.subList(i, sections.size()));
		
		smallSectionList.sort((x, y) -> y.getStudentCount() - x.getStudentCount()); //从大到小
		bigSectionList.sort((x, y) -> x.getStudentCount() - y.getStudentCount());   //从小到大

		for (int j = 0; j < smallSectionList.size(); j ++) {
			for (int k = 0; k < bigSectionList.size(); k ++) {
				if (smallSectionList.get(j).getStudentCount() + bigSectionList.get(k).getStudentCount() <= solution.getSectionSizeMax()) {
					bigSectionList.get(k).addStudentListAll(smallSectionList.get(j).getStudentList());
					smallSectionList.get(j).clearStudentAll();
					break;
				}
			}
			bigSectionList.sort((x, y) -> x.getStudentCount() - y.getStudentCount());
		}
		
		return sections.stream().filter(e -> e.getStudentCount() > 0).collect(Collectors.toList());
	}
	
	//v3: 优化版, 穷举法
	private List<CourseSection> do1X_combineSections_v3(List<CourseSection> sections) {
		List<SectionItemVertex>		allSectionGroupItemList = generateVertexList(sections);	
		//System.out.println("in Processing2X::do1X_combineSections_v3(), allSectionGroupItemList.size = " + allSectionGroupItemList.size());
		if (allSectionGroupItemList.size() > 150) {
			//System.out.println("\t abort using full optimization");
			return do1X_combineSections_v1(sections);
		}
		
		SimpleGraph<SectionItemVertex, DefaultEdge> sectionGroupGraph = buildSectionGroupGraph(allSectionGroupItemList);
		PivotBronKerboschCliqueFinder<SectionItemVertex, DefaultEdge> cliqueFinder = new PivotBronKerboschCliqueFinder<>(sectionGroupGraph);
		
		Iterator<Set<SectionItemVertex>> iter = cliqueFinder.iterator();
		
		List<CourseSection> resultSectionList;
		int bestScore = solution.getSectionSizeMax() * solution.getSectionSizeMax() * sections.size();
		int currentScore;
		Set<SectionItemVertex> bestSolution = null, currentSolution = null;
		while(iter.hasNext()) {
			currentSolution = iter.next();
			currentScore = computeScore(currentSolution);
			if (currentScore < bestScore) {
				bestScore = currentScore;
				bestSolution = currentSolution;
			}
		}
		resultSectionList = generateOptimizedSectionList(bestSolution);	
		
		return resultSectionList;
	}
	
	private List<CourseSection> generateOptimizedSectionList (Set<SectionItemVertex> bestSolution) {
		return bestSolution.stream().map(e -> e.generateCombinedSection()).collect(Collectors.toList());
	}
	
	private int computeScore(Set<SectionItemVertex> theSolution) {
		int resultScore = 0;
		for (SectionItemVertex v : theSolution) {
			resultScore += v.getSquredScore(solution.getSectionSizeMax());
		}
		
		return resultScore;
	}
	
	private List<SectionItemVertex> generateVertexList(List<CourseSection> sections) {
		//case 0: singles
		List<SectionItemVertex> resultList = sections.stream().map(e -> new SectionItemVertex(e)).collect(Collectors.toList());
		
		sections.sort((x, y) -> y.getStudentCount() - x.getStudentCount());
		
		//case 1: combine with another 
		for (int i = 0; i < sections.size() - 1; i ++) {
			for (int j = i + 1; j < sections.size(); j ++) {
				if (sections.get(i).getStudentCount() + sections.get(j).getStudentCount() <= solution.getSectionSizeMax()) {
					SectionItemVertex newVertex = new SectionItemVertex(sections.get(i));
					newVertex.addSectionItem(sections.get(j));
					resultList.add(newVertex);
				}
			}
		}
		
		//case 2: combine with other two 
		for (int i = 0; i < sections.size() - 2; i ++) {
			for (int j = i + 1; j < sections.size() - 1; j ++) {
				for (int k = j + 1; k < sections.size(); k ++) {
					if (sections.get(i).getStudentCount() + sections.get(j).getStudentCount() 
							+ sections.get(k).getStudentCount() <= solution.getSectionSizeMax()) {
						SectionItemVertex newVertex = new SectionItemVertex(sections.get(i));
						newVertex.addSectionItem(sections.get(j));
						newVertex.addSectionItem(sections.get(k));
						resultList.add(newVertex);
					}
				}
			}
		}

		//case 3: combine with other three 
//		for (int i = 0; i < sections.size() - 3; i ++) {
//			for (int j = i + 1; j < sections.size() - 2; j ++) {
//				for (int k = j + 1; k < sections.size() - 1; k ++) {
//					for (int l = k + 1; l < sections.size(); l ++) {
//						if (sections.get(i).getStudentCount() + sections.get(j).getStudentCount() 
//								+ sections.get(k).getStudentCount() 
//								+ sections.get(l).getStudentCount() <= solution.getSectionSizeMax()) {
//							SectionItemVertex newVertex = new SectionItemVertex(sections.get(i));
//							newVertex.addSectionItem(sections.get(j));
//							newVertex.addSectionItem(sections.get(k));
//							newVertex.addSectionItem(sections.get(l));
//							resultList.add(newVertex);
//						}
//					}
//				}
//			}
//		}

//		//case 4: combine with other four 
//		for (int i = 0; i < sections.size() - 4; i ++) {
//			for (int j = i + 1; j < sections.size() - 3; j ++) {
//				for (int k = j + 1; k < sections.size() - 2; k ++) {
//					for (int l = k + 1; l < sections.size() - 1; l ++) {
//						for (int m = l + 1; m < sections.size(); m ++) {
//							if (sections.get(i).getStudentCount() + sections.get(j).getStudentCount() 
//									+ sections.get(k).getStudentCount() 
//									+ sections.get(l).getStudentCount() 
//									+ sections.get(m).getStudentCount() <= solution.getSectionSizeMax()) {
//								SectionItemVertex newVertex = new SectionItemVertex(sections.get(i));
//								newVertex.addSectionItem(sections.get(j));
//								newVertex.addSectionItem(sections.get(k));
//								newVertex.addSectionItem(sections.get(l));
//								newVertex.addSectionItem(sections.get(m));
//								resultList.add(newVertex);
//							}
//						}
//					}
//				}
//			}
//		}
//
//		//case 5: combine with other five 
//		for (int i = 0; i < sections.size() - 5; i ++) {
//			for (int j = i + 1; j < sections.size() - 4; j ++) {
//				for (int k = j + 1; k < sections.size() - 3; k ++) {
//					for (int l = k + 1; l < sections.size() - 2; l ++) {
//						for (int m = l + 1; m < sections.size() - 1; m ++) {
//							for (int n = m + 1; n < sections.size(); n ++) {
//								if (sections.get(i).getStudentCount() + sections.get(j).getStudentCount() 
//										+ sections.get(k).getStudentCount() 
//										+ sections.get(l).getStudentCount() 
//										+ sections.get(m).getStudentCount()
//										+ sections.get(n).getStudentCount() <= solution.getSectionSizeMax()) {
//									SectionItemVertex newVertex = new SectionItemVertex(sections.get(i));
//									newVertex.addSectionItem(sections.get(j));
//									newVertex.addSectionItem(sections.get(k));
//									newVertex.addSectionItem(sections.get(l));
//									newVertex.addSectionItem(sections.get(m));
//									newVertex.addSectionItem(sections.get(n));
//									resultList.add(newVertex);
//								}
//							}
//						}
//					}
//				}
//			}
//		}

		//case 6 and up: not supported for now
		
		return resultList;
	}
	
	private SimpleGraph<SectionItemVertex, DefaultEdge> buildSectionGroupGraph(List<SectionItemVertex> vertexList) {
		SimpleGraph<SectionItemVertex, DefaultEdge> sectionItemGraph = new SimpleGraph<SectionItemVertex, DefaultEdge>(DefaultEdge.class);
		vertexList.stream().forEach(e -> sectionItemGraph.addVertex(e));
		
		for (int i = 0; i < vertexList.size(); i ++) {
			for (int j = i + 1; j < vertexList.size(); j ++) {
				if (!vertexList.get(i).isIntersected(vertexList.get(j))) {
					sectionItemGraph.addEdge(vertexList.get(i), vertexList.get(j));
				}
			}
		}
		
		return sectionItemGraph;
	}
	
	
	private int findSplitPosition (List<Integer> sortedSectionSizeList) {
		//sortedSectionSizeList 是从小到大排序的
		int i = 0; //分界点，i 是第一个无法通过贪心法分摊到其它的CourseSection中的
		boolean findMatched = false;
		for (i = 0; i < sortedSectionSizeList.size() - 1; i ++){
			findMatched = false;
			for (int j = i + 1; j < sortedSectionSizeList.size(); j ++) {
				if (sortedSectionSizeList.get(i).intValue() + sortedSectionSizeList.get(j).intValue() <= solution.getSectionSizeMax() ) {
					findMatched = true;
					break;
				}
			}
			if (!findMatched) {
				break;
			}
		}

		List<Integer> smallSections;
		List<Integer> bigSections;
		
		//演绎一下先，保证所有的smallSections可以通过贪心法，放到bigSections中
		boolean isFit = true;
		while (i > 0) {
			smallSections = new ArrayList<>();
			smallSections.addAll(sortedSectionSizeList.subList(0, i));
			bigSections = new ArrayList<>();
			bigSections.addAll(sortedSectionSizeList.subList(i, sortedSectionSizeList.size()));
			smallSections.sort((x, y) -> y.intValue() - x.intValue()); //从大到小
			bigSections.sort((x, y) -> x.intValue() - y.intValue());   //从小到大
			for (int j = 0; j < smallSections.size(); j ++) {
				findMatched = false;
				for (int k = 0; k < bigSections.size(); k ++) {
					int smallSize = smallSections.get(j).intValue();
					int bigSize = bigSections.get(k).intValue();
					if (smallSize + bigSize <= solution.getSectionSizeMax()) {
						bigSections.set(k, new Integer(smallSize + bigSize));
						findMatched = true;
						break;
					}
				}
				if (!findMatched) {
					isFit = false;
					break;
				}
				else {
					bigSections.sort((x, y) -> x.intValue() - y.intValue());
				}
			}
			if (!isFit) {
				i --;
			}
			else {
				break;
			}
		}
		
		return i;
	}

//	private boolean do2X_isFinished () {
//		int i = 0;
//		for (BigCurriculum bc : bigCurriculumList) {
//			if (bc.do2X_current().isEmpty() || bc.getCandidateGroups().isEmpty()) {
//				//bc.do2X_current().isEmpty(): 当前外来组合是最后一个
//				//bc.getCandidateGroups().isEmpty(): 可能的外来组合是空的
//				i ++;
//			}
//		}
//		
//		if (i == bigCurriculumList.size()) //所有的情况都试过了
//			return true;
//		else
//			return false;
//		
//	}
	
//	//把每个小组合，放到每个可以2+X的大组合那里
//	private void do2X_registration () {
//		bigCurriculumList.stream().forEach(e -> e.do2X_init(smallCurriculumList));
//	}
	
//	//找出跟最少个数的SmallCurriculum匹配的BigCurriculum
//	private BigCurriculum do2X_2findNext () {
//		List<BigCurriculum> sortedBC = bigCurriculumList.stream()
//														.filter(e -> e.isProcessed == false)
//														.sorted((first, second) -> first.getAvgMatchCount() - second.getAvgMatchCount())
//														.collect(Collectors.toList());
//		if (sortedBC.size() > 0) 
//			return sortedBC.get(0);
//		else
//			return null;
//	}
	
//	private void do2X_3updateRest (List<SmallCurriculum> scList) {
//		
//	}
	
	private void printAll() {
		System.out.println("====bigCurriculumList=====");
		bigCurriculumList.stream().forEach(e -> System.out.println(e.getStudentCount_v1()));
		System.out.println("====smallCurriculumList=====");
		smallCurriculumList.stream().forEach(e -> System.out.println(e.getStudentCount()));
	}
	
	//找到大小分组的分界线
	private void doBalance_v1 () {
		while (getTotalMarginOfBigCurriculum() < getTotalStudentCountOfSmallCurriculum () ||
			   getTotalGroupCount() < roomCount) {
			if (smallCurriculumList.size() == 0)
				break;
			
			SmallCurriculum sc = smallCurriculumList.get(0);
			smallCurriculumList.remove(sc);
			bigCurriculumList.add(new BigCurriculum(sc.getCurriculum(), this));
		}
	}

	//找到大小分组的分界线
	private void doBalance_v5 () {
		while (getTotalMarginOfBigCurriculum() < getTotalStudentCountOfSmallCurriculum () ||
			   getTotalGroupCount() < roomCount) {
			if (smallCurriculumList.size() == 0)
				break;
			
			SmallCurriculum sc = smallCurriculumList.get(0);
			smallCurriculumList.remove(sc);
			bigCurriculumList.add(new BigCurriculum(sc.getCurriculum(), this));
		}
	}
	
//	//找到大小分组的分界线
//	private void doBalance_v2 () {
//		while (getTotalMarginOfBigCurriculum() < getTotalStudentCountOfSmallCurriculum () ||
//			   getTotalGroupCount() < roomCount) {
//			SmallCurriculum sc = smallCurriculumList.get(0);
//			smallCurriculumList.remove(sc);
//			bigCurriculumList.add(new BigCurriculum(sc.getCurriculum()));
//		}
//	}
	
	private int getTotalGroupCount () {
		return bigCurriculumList.stream().map(e -> e.getGroupCount()).reduce(0, (x, y) -> x +y);
	}
	
	private int getTotalMarginOfBigCurriculum () {
		int r = bigCurriculumList.stream().map(e -> e.getMarginCountMax()).reduce(0, (x, y) -> x + y);
		//System.out.println("getTotalMarginOfBigCurriculum = " + r);
		return r;
	}
	
	private int getTotalStudentCountOfSmallCurriculum () {
		int r = smallCurriculumList.stream().map(e -> e.getStudentCount()).reduce(0, (x, y) -> x + y);
		//System.out.println("getTotalStudentCountOfSmallCurriculum = " + r);
		return r; 
	}
	

	
	private List<Curriculum> getCurriculumListWithMinStudentCount(int minStudentCount) {
		return allCurriculumList.stream().filter(e -> e.getStudentCount() > minStudentCount).collect(Collectors.toList());
	}

	private List<Curriculum> getCurriculumListWithMaxStudentCount(int maxStudentCount) {
		return allCurriculumList.stream().filter(e -> e.getStudentCount() <= maxStudentCount).collect(Collectors.toList());
	}
}
