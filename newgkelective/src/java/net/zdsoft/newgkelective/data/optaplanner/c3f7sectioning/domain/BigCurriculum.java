package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.CalculateSections;

public class BigCurriculum {
	Curriculum curriculum;				//fact
	private int groupCount;				//fact, based on the above curriculum only
	Processing2X processing2X;
	
	List<SmallCurriculum> match2X_v1; 	//v1, for Processing2X::doit_v1() only
	
	//v2 only
	ArrayDeque<List<BigCurriculum>> 	combinationCandidateStack;
	
	//v5
	List<List<BigCurriculum>>	matchList;
	//v1: 分班结果, 包含待处理的小班 
	List<CourseSection>   sectionList;
	
	
	public void transferToV1() {
		match2X_v1.clear();
		if (matchList.size() > 0) {
			List<BigCurriculum> bestMatchItem = matchList.get(0);
			for (BigCurriculum bc : bestMatchItem) {
				match2X_v1.add(new SmallCurriculum(bc.getCurriculum(), processing2X));
			}
		}
	}
	
	public void setMatchList(List<List<BigCurriculum>>	mList) {
		matchList = mList;
	}

	public List<List<BigCurriculum>> getMatchList() {
		return matchList;
	}

	public int getMatchItemCount() {
		return matchList.size();
	}
	
	public int bestMatchItemScore() {
		sortByBestFit();
		return calcMatchItemScoreByBestFit(matchList.get(0));
	}

	public void addMatchItem(List<BigCurriculum> mItem) {
		matchList.add(mItem);
	}
	
	public void clearMatchList() {
		matchList.clear();
		return;
	}
	
	public void keepBestMatchItem () {
		List<BigCurriculum> bestItem = matchList.get(0).stream().collect(Collectors.toList());
		matchList.clear();
		matchList.add(bestItem);
	}

	//越好的matchItem，返回值越小
	//专门给sortByBestFit() 用的。
	public int calcMatchItemScoreByBestFit(List<BigCurriculum> matchItem) {
		int s = 0;
		int studentCount = matchItem.stream().map(e -> e.getStudentCount_v1()).reduce(0, (x, y) -> x + y);
		
		//1、刚好能凑足的，当然是最好
		if (studentCount >= getMarginCountMean() && studentCount <= getMarginCountMax()) {
			s = -200000;
		}
		else {
			s = -100000;
		}

		
		//2、组合个数比较少的，更好
		s = s - (9 - matchItem.size()) * 10000;
		
		//最后考虑捆绑的总人数，人数多的更好
		s = s - studentCount;
		
		return s;
	}
	
	public void sortByBestFit () {
		matchList.sort((x, y) -> calcMatchItemScoreByBestFit(x) - calcMatchItemScoreByBestFit(y));
	}
	
	
	public void keepFirstCombinationCandidateOnly () {
		if (combinationCandidateStack.size() > 0) {
			List<BigCurriculum> firstItem = combinationCandidateStack.peekLast();
			combinationCandidateStack.clear();
			combinationCandidateStack.add(firstItem);
		}
	}
	
	public void clearCombinationCandidate() {
		combinationCandidateStack.clear();
	}
	public void addCombinationCandidate (List<BigCurriculum> cc) {
		combinationCandidateStack.addLast(cc);
	}
	
	public List<BigCurriculum> removeCombinationCandidate () {
		if (!combinationCandidateStack.isEmpty())
			return combinationCandidateStack.removeLast();
		else
			return null;
	}

	public List<BigCurriculum> nextCombinationCandidate () {
		return removeCombinationCandidate();
	}
	
	public List<BigCurriculum> peekCombinationCandidate (){
		return combinationCandidateStack.peekLast();
	}
	
	public boolean hasNextCombinationCandidate() {
		if (combinationCandidateStack.peekLast().size() == 0) {
			return false;
		}
		else {
			return true;
		}
	}
	
	//v1 only, 基于groupCount的分组
	class CurriculumSubGroupItem {
		public int beginOnBase; //inclusive
		public int endOnBase;   //exclusive
		//public int studentCount;
		Curriculum baseCurriculum;
		List<Curriculum> curriculumList;
		List<CourseSection>    groupSectionList;
		
		public CurriculumSubGroupItem() {
			beginOnBase = endOnBase = 0;
			curriculumList = new ArrayList<>();
			sectionList = new ArrayList<>();
		}
		
		public int getStudentCount() {
			int n = endOnBase - beginOnBase;
			for (Curriculum c : curriculumList) {
				n += c.getStudentCount();
			}
			return n;
		}
		
		public void putCurriculum(Curriculum c) {
			curriculumList.add(c);
		}
		
		public void putBaseCurriculum(Curriculum bc, int begin, int end) {
			baseCurriculum = bc;
			beginOnBase = begin;
			endOnBase = end;
		}
		
		public void calculateSectionList() {
			Map<String, CourseSection> courseName2SectionMap = new HashMap<>();
			curriculum.getAllCourseNameSet().stream().forEach(e -> courseName2SectionMap.put(e, new CourseSection(processing2X.getCourseMap().get(e))));
			for (SmallCurriculum sc : match2X_v1) {
				sc.getCurriculum().getAllCourseNameSet().stream().forEach(e -> courseName2SectionMap.put(e, new CourseSection(processing2X.getCourseMap().get(e))));
			}
			
			//System.out.println("courseName2SectionMap.size = " + courseName2SectionMap.size());
			baseCurriculum.getAllCourseNameSet().forEach(e -> courseName2SectionMap.get(e).addStudentListAll(baseCurriculum.getStudentList().subList(beginOnBase, endOnBase)));
			for (Curriculum c : curriculumList) {
				c.getAllCourseNameSet().forEach(e -> courseName2SectionMap.get(e).addStudentListAll(c.getStudentList()));
			}
			groupSectionList = courseName2SectionMap.entrySet().stream().map(e -> e.getValue()).filter(e -> e.getStudentCount() > 0).collect(Collectors.toList());
			
			//System.out.println("calculateSectionList()::groupSectionList.size()=" + groupSectionList.size());
		}
		
		public List<CourseSection> getSectionList() {
			return groupSectionList;
		}
	}
	
	public BigCurriculum(Curriculum c, Processing2X p2X) {
		this.curriculum = c;
		this.processing2X = p2X;
		//possibleMatch2X = new HashSet<>();
		match2X_v1 = new ArrayList<>();

		combinationCandidateStack = new ArrayDeque<>();
		
		matchList = new ArrayList<>();
		
		groupCount = CalculateSections.calculateSectioning(getStudentCount_v1(), c.getSectionSizeMax(), 0).size();
	}
	
	public void do1X () {
		List<CurriculumSubGroupItem> groupList = new ArrayList<>();
		for (int i = 0; i < groupCount; i ++) {
			groupList.add(new CurriculumSubGroupItem());
		}
		
		for (int i = 0; i < match2X_v1.size(); i ++) {
			groupList.get(i % groupCount).putCurriculum(match2X_v1.get(i).getCurriculum());
		}
		
		int totalStudentCount = curriculum.getStudentCount();
		for (SmallCurriculum sc : match2X_v1) {
			totalStudentCount += sc.getCurriculum().getStudentCount();
		}
		//System.out.println("totalStudentCount = " + totalStudentCount + ", getStudentCount_v1 = " + getStudentCount_v1());
		
		//List<Integer> sectionSizeList = CalculateSections.calculateSectioning(totalStudentCount, curriculum.getSectionSizeMean(), curriculum.getSectionSizeMargin());
		List<Integer> sectionSizeList = CalculateSections.calculateSectioning(totalStudentCount, curriculum.getSectionSizeMax(), 0);
		
		int beginCount = 0;
		int endCount = 0;
		for (int i = 0; i < groupCount; i ++) {
			int currentSectionSize = sectionSizeList.get(i).intValue();
			endCount = beginCount + currentSectionSize - groupList.get(i).getStudentCount();
			groupList.get(i).putBaseCurriculum(curriculum, beginCount, endCount);
			beginCount = endCount;
		}
		
		groupList.stream().forEach(e -> e.calculateSectionList());
		sectionList = new ArrayList<>();
		groupList.stream().forEach(e -> sectionList.addAll(e.getSectionList()));
		
		//System.out.println(curriculum.getCode() + ": sectionList.size() = " + sectionList.size());
	}

	//如果大组合需要分解成几个教学班，就把来捆绑的小组合全都放到一个教学班里
	public void do1X_v2 () {
		List<CurriculumSubGroupItem> groupList = new ArrayList<>();
		for (int i = 0; i < groupCount; i ++) {
			groupList.add(new CurriculumSubGroupItem());
		}
		
		//所有的小组合，放到最后一个
		for (int i = 0; i < match2X_v1.size(); i ++) {
			groupList.get(groupCount - 1).putCurriculum(match2X_v1.get(i).getCurriculum());
		}
		
		//数一数
		int totalStudentCount = curriculum.getStudentCount();
		for (SmallCurriculum sc : match2X_v1) {
			totalStudentCount += sc.getCurriculum().getStudentCount();
		}
		//System.out.println("totalStudentCount = " + totalStudentCount + ", getStudentCount_v1 = " + getStudentCount_v1());
		
		//List<Integer> sectionSizeList = CalculateSections.calculateSectioning(totalStudentCount, curriculum.getSectionSizeMean(), curriculum.getSectionSizeMargin());
		List<Integer> sectionSizeList = CalculateSections.calculateSectioning(totalStudentCount, curriculum.getSectionSizeMax(), 0);
		
		int beginCount = 0;
		int endCount = 0;
		for (int i = 0; i < groupCount; i ++) {
			int currentSectionSize = sectionSizeList.get(i).intValue();
			endCount = beginCount + currentSectionSize;
			if (endCount > curriculum.getStudentCount()) { 
				groupList.get(i).putBaseCurriculum(curriculum, beginCount, curriculum.getStudentCount());
				break;
			}
			else {
				groupList.get(i).putBaseCurriculum(curriculum, beginCount, endCount);
			}
			beginCount = endCount;
		}
		
		groupList.stream().forEach(e -> e.calculateSectionList());
		sectionList = new ArrayList<>();
		groupList.stream().forEach(e -> sectionList.addAll(e.getSectionList()));
		
		//System.out.println(curriculum.getCode() + ": sectionList.size() = " + sectionList.size());
	}
	
	//for Processing2X::doit_v1() only
	public void addMatch2X_v1(SmallCurriculum sc) {
		match2X_v1.add(sc);
		//marginCount -= sc.getStudentCount();
	}
	
	public List<SmallCurriculum> getMatch2X_v1() {
		return match2X_v1;
	}
	
	
	public List<CourseSection> getSectionList() {
		return sectionList;
	}

	public void printInfo () {
		System.out.println("" + curriculum.getCode());
		System.out.println("\t studentCount = " + getStudentCount_v1());
		System.out.println("\t groupCount = " + groupCount);
		System.out.println("\t marginCountMax = " + getMarginCountMax());
		System.out.println("\t marginCountMean = " + getMarginCountMean());
		System.out.println("\t matched# = " + match2X_v1.size());
		match2X_v1.stream().forEach(e -> System.out.println("\t" + e.getCurriculum().getCode() + ": " + e.getStudentCount()));
		
	}
	
	public void printSectionList() {
		sectionList.stream().forEach(e -> System.out.println("\t" + e.getCourse().getCode() + "\n\t\t" + e.getStudentCount()));
	}
	
//	public int getAvgMatchCount () {
//		return (int) (possibleMatch2X.size() * 100.0 / groupCount); 
//	}
	
//	public void do2X_init (List<SmallCurriculum> scList) {
//		clearPossibleMatch2X();
//		for (SmallCurriculum sc : scList) {
//			if (getCurriculum().isMatched2X(sc.getCurriculum())) {
//				addPossibleMatch2X(sc);
//			}
//		}
//		do2X_populateCandidateGroups();
//	}
	
//	private void do2X_populateCandidateGroups () {
//		candidateGroups = new ArrayList<>();
//		
//		//marginCount = SectionSolution.maxSectionSize * roomCount - getStudentCount_v1();
//		int marginCount = getMarginCount();
//		List<SmallCurriculum> sortedList = possibleMatch2X.stream()
//				                                          .sorted((first, second) -> first.getStudentCount() - second.getStudentCount())
//				                                          .collect(Collectors.toList());
//		List<SmallCurriculum> newGroup;
//		//case1: 单个
//		for (SmallCurriculum sc : sortedList) {
//			if (sc.getStudentCount() <= marginCount) {
//				newGroup = new ArrayList<SmallCurriculum>();
//				newGroup.add(sc);
//				candidateGroups.add(new CandidateItem(newGroup)); 
//			}
//		}
//		
//		//case2: 2个组合同时加入
//		if (groupCount >= 2) {
//			for (SmallCurriculum sc1 : sortedList) {
//				for (SmallCurriculum sc2 : sortedList) {
//					if (sc1.getStudentCount() + sc2.getStudentCount() <= marginCount) {
//						newGroup = new ArrayList<SmallCurriculum>();
//						newGroup.add(sc1);
//						newGroup.add(sc2);
//						candidateGroups.add(new CandidateItem(newGroup)); 
//					}
//				}
//			}
//		}
//		
//		//case3: 3个组合同时加入
//		if (groupCount >= 3) {
//			for (SmallCurriculum sc1 : sortedList) {
//				for (SmallCurriculum sc2 : sortedList) {
//					for (SmallCurriculum sc3 : sortedList) {
//						if (sc1.getStudentCount() + sc2.getStudentCount() + sc3.getStudentCount() <= marginCount) {
//							newGroup = new ArrayList<SmallCurriculum>();
//							newGroup.add(sc1);
//							newGroup.add(sc2);
//							newGroup.add(sc3);
//							candidateGroups.add(new CandidateItem(newGroup));
//						}
//					}
//				}
//			}
//		}
//
//		//case4: 4个组合同时加入
//		if (groupCount >= 4) {
//			for (SmallCurriculum sc1 : sortedList) {
//				for (SmallCurriculum sc2 : sortedList) {
//					for (SmallCurriculum sc3 : sortedList) {
//						for (SmallCurriculum sc4 : sortedList) {
//							if (sc1.getStudentCount() + sc2.getStudentCount() + sc3.getStudentCount() + sc4.getStudentCount() <= marginCount) {
//								newGroup = new ArrayList<SmallCurriculum>();
//								newGroup.add(sc1);
//								newGroup.add(sc2);
//								newGroup.add(sc3);
//								newGroup.add(sc4);
//								candidateGroups.add(new CandidateItem(newGroup));
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		//case5 and up: 暂时不考虑
//		
//		//case0: 表示一个SmallCurriculum都不要
//		newGroup = new ArrayList<SmallCurriculum>();
//		candidateGroups.add(new CandidateItem(newGroup));
//		
//		//按人数倒序排序
//		candidateGroups.sort((first, second) -> second.getStudentCount() - first.getStudentCount());
//		candidatePointer = 0;
//		//candidateCount = candidateGroups.size();
//	}
//	

//	public List<SmallCurriculum> do2X_current () {
//		if (candidatePointer >= candidateGroups.size())
//			return null;
//		else
//			return candidateGroups.get(candidatePointer).candidateGroup;
//	}
	
//	public List<SmallCurriculum> do2X_next () {
//		if (candidatePointer >= candidateGroups.size())
//			return null;
//		else
//			return candidateGroups.get(candidatePointer++).candidateGroup;
//	}
	
//	public int getMarginCount() {
//		return (curriculum.getSectionSizeMean() + curriculum.getSectionSizeMargin()) * groupCount - getStudentCount_v1();
//	}

	public int getMarginCountMean() {
		return curriculum.getSectionSizeMean() * groupCount - getStudentCount_v1();
	}

	public int getMarginCountMax() {
		return (curriculum.getSectionSizeMean() + curriculum.getSectionSizeMargin()) * groupCount - getStudentCount_v1();
	}
	
	public void setCurriculum (Curriculum c) {
		this.curriculum = c;
	}	

	public Curriculum getCurriculum () {
		return this.curriculum;
	}

	public int getStudentCount_v1() {
		int n = curriculum.getStudentCount();
		for (SmallCurriculum c : match2X_v1) {
			n += c.getStudentCount();
		}
		return n;
	}

	public int getGroupCount() {
		return groupCount;
	}

	public Processing2X getProcessing2X() {
		return processing2X;
	}

	public void setProcessing2X(Processing2X processing2x) {
		processing2X = processing2x;
	}
	
	

//	public void addPossibleMatch2X(SmallCurriculum sc) {
//		possibleMatch2X.add(sc);
//	}
//	
//	public void removePossibleMatch2X(SmallCurriculum sc)  {
//		possibleMatch2X.remove(sc);
//	}
//
//	public void removePossibleMatch2XBatch(List<SmallCurriculum> scList)  {
//		possibleMatch2X.removeAll(scList);
//	}
//	
//	public void clearPossibleMatch2X () {
//		possibleMatch2X.clear();
//	}
//
//	public Set<SmallCurriculum> getPossibleMatch2X() {
//		return possibleMatch2X;
//	}
//
//	public void setPossibleMatch2X(Set<SmallCurriculum> possibleMatch2X) {
//		this.possibleMatch2X = possibleMatch2X;
//	}
//
//	public List<CandidateItem> getCandidateGroups() {
//		return candidateGroups;
//	}
//
//	public void setCandidateGroups(List<CandidateItem> candidateGroups) {
//		this.candidateGroups = candidateGroups;
//	}
//
//	public int getCandidatePointer() {
//		return candidatePointer;
//	}
//
//	public void setCandidatePointer(int candidatePointer) {
//		this.candidatePointer = candidatePointer;
//	}
//
//	public int getCandidateCount() {
//		return candidateGroups.size();
//	}

}
