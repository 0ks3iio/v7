package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.drools.compiler.lang.dsl.DSLMappingEntry.Section;
import org.jgrapht.alg.clique.DegeneracyBronKerboschCliqueFinder;
import org.jgrapht.alg.clique.PivotBronKerboschCliqueFinder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import com.google.common.collect.Sets;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.CalculateSections;

public class Processing2XOptimized {
	SectionSolution solution;							//fact
	//int roomCount;										//fact
	//int maxSectionSize;									//fact
	List<Student> 			studentList;				//fact
	List<Course> 			courseList;					//fact
	Map<String, Curriculum> curriculumMap;				//fact
	List<Curriculum> 		allCurriculumList;			//fact

	List<GroupItemVertex>		allCurriculumGroupItemList;		
	SimpleGraph<GroupItemVertex, DefaultEdge> curriculumGroupGraph;
	PivotBronKerboschCliqueFinder<GroupItemVertex, DefaultEdge> cliqueFinder;	
	
	//最后的结果汇总
	List<CourseSection>		allSectionList;

	public Processing2XOptimized (SectionSolution s) {
		this.solution = s;
//		this.roomCount = s.getRoomCount();
//		this.maxSectionSize = s.getMaxSectionSize();
		this.studentList = s.getStudentList();
		this.courseList = s.getCourseList();
		this.curriculumMap = s.getCurriculumMap();
		this.allCurriculumList = curriculumMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
		
		buildCurriculumGroupItemList ();
		buildCurriculumGroupGraph ();
		buildCliqueFinder();
	}
	
	public void doit () {
		Iterator<Set<GroupItemVertex>> iter = cliqueFinder.iterator();
		
		int i = 0;
		while(iter.hasNext()) {
			Set<GroupItemVertex> clique = iter.next();
			System.out.println("find " + i ++);
		}
		System.out.println("Done ");
	}
	
	private void buildCurriculumGroupItemList () {
		allCurriculumGroupItemList = new ArrayList<GroupItemVertex>();
		for (Curriculum currentCurriculum : allCurriculumList) {
			List<Curriculum> matchedList = allCurriculumList.stream().filter(e -> e.isMatched2X(currentCurriculum) && e.getStudentCount() <= currentCurriculum.getStudentCount() && e.getStudentCount() < solution.getSectionSizeMax()).collect(Collectors.toList());
			generateGroupItems(currentCurriculum, matchedList);
		}
	}
	
	private void generateGroupItems(Curriculum baseCurriculum, List<Curriculum> matchedList) {
		int marginCount = baseCurriculum.getMarginCountAsBigCurriculum();
		GroupItemVertex groupItemVertex = new GroupItemVertex(baseCurriculum);
		allCurriculumGroupItemList.add(groupItemVertex);
		
		System.out.println(baseCurriculum.getCode() + ": " + baseCurriculum.getStudentCount() + "， " + marginCount);
		matchedList.stream().forEach(e -> System.out.println("\t" + e.getCode() + ": " + e.getStudentCount() + ", " + e.getSurplusCountAsSmallCurriculum()));
		
		//如果baseCurriculum已经刚刚好满了，就不跟其它组合拼 2+X了。
		if (marginCount == 0) 
			return;
		
		//case 1, with 1 small curriculum
		for (Curriculum c : matchedList) {
			if (c.getSurplusCountAsSmallCurriculum() == 0) {
				continue;
			}
			
			if (c.getSurplusCountAsSmallCurriculum() <= marginCount) {
				groupItemVertex = new GroupItemVertex(baseCurriculum);
				groupItemVertex.addCurriculum(c);
				allCurriculumGroupItemList.add(groupItemVertex);
			}
		}
		
		//case 2, with 2 small curriculum
		for (int i = 0; i < matchedList.size(); i ++) {
			if (matchedList.get(i).getSurplusCountAsSmallCurriculum() == 0) {
				continue;
			}
			
			for (int j = i + 1; j < matchedList.size(); j ++) {
				if (matchedList.get(j).getSurplusCountAsSmallCurriculum() == 0) {
					continue;
				}
				
				if (matchedList.get(i).getSurplusCountAsSmallCurriculum() 
					+ matchedList.get(j).getSurplusCountAsSmallCurriculum() <= marginCount) {
					groupItemVertex = new GroupItemVertex(baseCurriculum);
					groupItemVertex.addCurriculum(matchedList.get(i));
					groupItemVertex.addCurriculum(matchedList.get(j));
					allCurriculumGroupItemList.add(groupItemVertex);
				}
			}
		}

		//case 3, with 3 small curriculum
		for (int i = 0; i < matchedList.size(); i ++) {
			if (matchedList.get(i).getSurplusCountAsSmallCurriculum() == 0) {
				continue;
			}
			
			for (int j = i + 1; j < matchedList.size(); j ++) {
				if (matchedList.get(j).getSurplusCountAsSmallCurriculum() == 0) {
					continue;
				}
				
				for (int k = j + 1; k < matchedList.size(); k ++) {
					if (matchedList.get(k).getSurplusCountAsSmallCurriculum() == 0) {
						continue;
					}
					
					if (matchedList.get(i).getSurplusCountAsSmallCurriculum() 
							+ matchedList.get(j).getSurplusCountAsSmallCurriculum() 
							+ matchedList.get(k).getSurplusCountAsSmallCurriculum() <= marginCount) {
						groupItemVertex = new GroupItemVertex(baseCurriculum);
						groupItemVertex.addCurriculum(matchedList.get(i));
						groupItemVertex.addCurriculum(matchedList.get(j));
						groupItemVertex.addCurriculum(matchedList.get(k));
						allCurriculumGroupItemList.add(groupItemVertex);
					}
				}
			}
		}

		//case 4, with 4 small curriculum
		for (int i = 0; i < matchedList.size(); i ++) {
			if (matchedList.get(i).getSurplusCountAsSmallCurriculum() == 0) {
				continue;
			}
			
			for (int j = i + 1; j < matchedList.size(); j ++) {
				if (matchedList.get(j).getSurplusCountAsSmallCurriculum() == 0) {
					continue;
				}
				
				for (int k = j + 1; k < matchedList.size(); k ++) {
					if (matchedList.get(k).getSurplusCountAsSmallCurriculum() == 0) {
						continue;
					}
					
					for (int l = k + 1; l < matchedList.size(); l ++) {
						if (matchedList.get(l).getSurplusCountAsSmallCurriculum() == 0) {
							continue;
						}
						
						if (matchedList.get(i).getSurplusCountAsSmallCurriculum() 
								+ matchedList.get(j).getSurplusCountAsSmallCurriculum() 
								+ matchedList.get(k).getSurplusCountAsSmallCurriculum()
								+ matchedList.get(l).getSurplusCountAsSmallCurriculum() <= marginCount) {
							groupItemVertex = new GroupItemVertex(baseCurriculum);
							groupItemVertex.addCurriculum(matchedList.get(i));
							groupItemVertex.addCurriculum(matchedList.get(j));
							groupItemVertex.addCurriculum(matchedList.get(k));
							groupItemVertex.addCurriculum(matchedList.get(l));
							allCurriculumGroupItemList.add(groupItemVertex);
						}	
					}
				}
			}
		}
		
		//case 5 and up: 暂时不考虑
		
		System.out.println("allCurriculumGroupItemList.size = " + allCurriculumGroupItemList.size());
	}
	
	private void buildCurriculumGroupGraph () {
		System.out.println("start buildCurriculumGroupGraph");
		curriculumGroupGraph = new SimpleGraph<GroupItemVertex, DefaultEdge>(DefaultEdge.class);
		allCurriculumGroupItemList.stream().forEach(e -> curriculumGroupGraph.addVertex(e));
		
		GroupItemVertex v1, v2;
		for(int i = 0; i < allCurriculumGroupItemList.size(); i ++) {
			v1 = allCurriculumGroupItemList.get(i);
			for(int j = i + 1; j < allCurriculumGroupItemList.size(); j ++) {
				v2 = allCurriculumGroupItemList.get(j);
				if (Sets.intersection(v1.getCurriculumGroup(), v2.getCurriculumGroup()).size() == 0) {
					curriculumGroupGraph.addEdge(v1, v2);
				}
			}
		}
		
		System.out.println("finish buildCurriculumGroupGraph");
	}
	
	private void buildCliqueFinder () {
		System.out.println("start buildCliqueFinder");
		cliqueFinder = new PivotBronKerboschCliqueFinder<GroupItemVertex, DefaultEdge>(curriculumGroupGraph);
		System.out.println("finish buildCliqueFinder");
	}
}
