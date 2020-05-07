package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.dio;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common.AbstractPersistable;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common.CalculateSections;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common.ExcelUtil;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common.StudentVO;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common.StudentVO2;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain.V4Course;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain.CourseSection;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain.Curriculum;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain.CurriculumCourse;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain.SectioningSolution;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain.V4Student;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain.StudentCurriculum;

public class ToData {
	private static final Double sizeMore = 1.5;
	
	public static void main(String[] args) {
		Integer meanCount = 55;
		Integer margin = 5;
//		SectioningSolution solution = getSolutionFromFile("学生基础信息、成绩2.xls",1,maxCount, margin, 55);
		
		SectioningSolution solution = getSolutionFromV3File("学生信息_22.xls", 55, 5, 60);
		solution.printCourseSectionList();
		solution.printCurriculumList();
	}
	/**
	 * 导入7选3的数据 文件
	 * @param fileName
	 * @param target
	 * @param sectionSizeMean 班级平均人数
	 * @param sectionSizeMargin
	 * @param divideCurriculumNum
	 * @return 
	 */
	public static SectioningSolution getSolutionFromV3File(String fileName, Integer sectionSizeMean, Integer sectionSizeMargin, int divideCurriculumNum) {
		InputStream input = ToData.class.getClassLoader().getResourceAsStream("data/"+fileName);
		List<StudentVO2> stuVO2s = new ExcelUtil<StudentVO2>(StudentVO2.class).importExcel("学生信息", input);
		
		List<String> allSubject = Arrays.asList(new String[]{"政治","历史","地理","化学","生物","物理","技术"});
		List<String> types = Arrays.asList(new String[] {"A","B"});
		Map<String, V4Course> courseMap = new HashMap<>();
		makeCourses(types, allSubject, courseMap );
		List<String> courseCodeList = courseMap.keySet().stream().collect(Collectors.toList());
		
		int mode = 1;
		return getSolutionFromV3(stuVO2s, courseCodeList, sectionSizeMean, sectionSizeMargin, divideCurriculumNum, mode );
	}
	
	/**
	 * 使用  7选三 情况下的数据
	 * @param stuVO2s
	 * @param courseCodeList
	 * @param sectionSizeMean
	 * @param sectionSizeMargin
	 * @param divideCurriculumNum
	 * @param mode 
	 * @return
	 */
	public static SectioningSolution getSolutionFromV3(List<StudentVO2> stuVO2s,List<String> courseCodeList, 
			Integer sectionSizeMean, Integer sectionSizeMargin, int divideCurriculumNum, int mode) {
		V4Course course;
		Map<String, V4Course> courseMap = new HashMap<>();
		for(String code:courseCodeList) {
			course = new V4Course(code);
			courseMap.put(code, course);
		}
		List<V4Course> courseList = new ArrayList<>(courseMap.values());
		
		Set<String> allSubject = courseCodeList.stream().map(e->e.substring(0, e.length()-1)).collect(Collectors.toSet());
		
		//TODO 先统计 已经手动 分班的科目 级学生
		Map<String, List<StudentVO2>> zhbMap = stuVO2s.stream().filter(e->StringUtils.isNotBlank(e.getSectionID()))
				.collect(Collectors.groupingBy(StudentVO2::getSectionID));

		List<V4Student> students = new ArrayList<>();
		V4Student student;
		String code = null;
		double score = 0.0;
		Map<V4Course,Integer>  studentCourseScoreMap;
		for (StudentVO2 vo : stuVO2s) {
			student = new V4Student(vo.getName(),vo.getStuId());
			String chooseStr = vo.getChooseSubject1() + "-" + vo.getChooseSubject2() + "-" + vo.getChooseSubject3();
			String readyStr = vo.getSectionSubject1() + "-" + vo.getSectionSubject2();

			studentCourseScoreMap = new HashMap<>();
			for(String subj:allSubject) {
				code = subj + "B";
				if(chooseStr.contains(subj)) {
					// 检验是否 已经有了手动分班，
					if(readyStr.contains(subj)) {
						// TODO 二 加 X 已经安排过的科目，不用安排
						//continue;
					}
					code = subj + "A";
				}

				course = courseMap.get(code);
				studentCourseScoreMap.put(course, (int)score);
			}
			student.setStudentCourseScoreMap(studentCourseScoreMap);
			students.add(student);
		}
		
		Map<String, List<StudentVO2>> sectionIDToStuMap = stuVO2s.stream().filter(e->e.getSectionID()!=null).collect(Collectors.groupingBy(StudentVO2::getSectionID));
		
		

		return makeSolution2(sectionIDToStuMap,sectionSizeMean, sectionSizeMargin, divideCurriculumNum, courseMap, courseList, students, mode);
		
	}
	private static void batchId(SectioningSolution solution) {
		List<V4Course> courseList = solution.getCourseList();
		List<V4Student> studentList = solution.getStudentList();
		List<Curriculum> curriculumList = solution.getCurriculumList();
		List<StudentCurriculum> studentCurriculumList = solution.getStudentCurriculumList();
		List<CurriculumCourse> curriculumCourseList = solution.getCurriculumCourseList();
		List<CourseSection> courseSectionList = solution.getCourseSectionList();
		
		int id = 1;
		List<List<? extends AbstractPersistable>> batchList = Arrays.asList(courseList,studentList,curriculumList,studentCurriculumList,curriculumCourseList,courseSectionList);
		for (List<? extends AbstractPersistable> list : batchList) {
			id = 1;
			for (AbstractPersistable entity : list) {
				entity.setId(id++);
			}
		}
		
	}

	private static boolean validate(SectioningSolution solution) {
		List<V4Course> courseList = solution.getCourseList();
		List<V4Student> studentList = solution.getStudentList();
		List<Curriculum> curriculumList = solution.getCurriculumList();
		List<StudentCurriculum> studentCurriculumList = solution.getStudentCurriculumList();
		List<CurriculumCourse> curriculumCourseList = solution.getCurriculumCourseList();
		List<CourseSection> courseSectionList = solution.getCourseSectionList();
		
		System.out.println("---------开始验证------------");
		boolean success = true;
		// 本次分班科目/学生数量
		Set<String> subjset = courseList.stream().map(e->e.getCourseCode().substring(0, e.getCourseCode().length()-1)).collect(Collectors.toSet());
		System.out.println("本次分班科目："+subjset);
		System.out.println("学生数量："+studentList.size());
		System.out.println("学生ID数量："+studentList.stream().map(e->e.getStudentId()).collect(Collectors.toSet()).size());
		
		// 组合Curriculum
		Optional<Integer> stuSizeT = curriculumList.stream().map(e->e.getStudentInCurriculumList().size()).reduce((e1,e2)->e1+e2);
		if(!stuSizeT.orElse(0).equals(new Integer(studentList.size()))) {
			System.out.println("所有组合里的学生相加："+stuSizeT.orElse(0)+" 不等于总的学生:"+studentList.size());
			success = false;
		}
		
		boolean allMatch = curriculumList.stream().allMatch(e->e.getCurriculumSectionList().stream().allMatch(e2->e2.getCurriculum().equals(e)));
		printMsg(allMatch," Curriculum 包含 StudentCurriculum错误");
		success = allMatch==false?allMatch:success;
		
		// StudentCurriculum
		allMatch = studentCurriculumList.stream().allMatch(e->e.getCurriculum().getCurriculumSectionList().contains(e));
		printMsg(allMatch,"StudentCurriculum 包含 Curriculum错误");
		success = allMatch==false?allMatch:success;
		
		// curriculumCourseList
		allMatch = curriculumCourseList.stream().allMatch(e->{
			boolean a = e.getCourseSectionList().stream().allMatch(e2->e2.getCourse().equals(e.getCourse()));
			boolean b = e.getStudentCurriculum().getCourseList().contains(e.getCourse());
			return a && b;
		});
		printMsg(allMatch,"curriculumCourseList 包含 StudentCurriculum 或者section 错误");
		success = allMatch==false?allMatch:success;
		
		System.out.println("\n#----初始化 solution验证完毕：\t"+(success==true?"通过":"未通过"));
		return success;
	}

	private static void printMsg(boolean allMatch, String msg) {
		if(!allMatch) {
			System.out.println(msg);
		}
	}
	/**
	 * 文理 分科  A/B/C分层的情况
	 * @param fileName
	 * @param target 分班的对象。0.文科语数外  1.理科语数外   2.物化生   3.政史地
	 * @param sectionSizeMean
	 * @param sectionSizeMargin
	 * @param divideCurriculumNum
	 * @return
	 */
	public static SectioningSolution getSolutionFromFile(String fileName, int target, Integer sectionSizeMean, Integer sectionSizeMargin, int divideCurriculumNum) {
		List<StudentVO> stuVOs = importData(fileName);
		
		System.out.println("共有学生 ："+stuVOs.size());
		// 初始化 course
		V4Course course;
		List<String> typeList = Arrays.asList("A","B","C");
		List<String> subjectList = Arrays.asList("语文","数学","英语");
		switch(target) {
			case 0:
				stuVOs = stuVOs.stream().filter(e->{
					double classNum = Double.parseDouble(e.getClassNum());
					if(classNum >= 10 && classNum <= 15) {
						return true;
					}
					return false;
				}).collect(Collectors.toList());
				break;
			case 1:
				stuVOs = stuVOs.stream().filter(e->{
					double classNum = Double.parseDouble(e.getClassNum());
					if(classNum < 10 || classNum > 15) {
						return true;
					}
					return false;
				}).collect(Collectors.toList());
				break;
			case 2:
				subjectList = Arrays.asList("化学","生物","物理");
				stuVOs = stuVOs.stream().filter(e->{
					double classNum = Double.parseDouble(e.getClassNum());
					if(classNum < 10 || classNum > 15) {
						return true;
					}
					return false;
				}).collect(Collectors.toList());
				break;
			case 3:
				subjectList = Arrays.asList("政治","历史","地理");
				stuVOs = stuVOs.stream().filter(e->{
					double classNum = Double.parseDouble(e.getClassNum());
					if(classNum >= 10 && classNum <= 15) {
						return true;
					}
					return false;
				}).collect(Collectors.toList());
				break;
			default:
				System.out.println("target 输入有误！");
		}
		
		
		Map<String, V4Course> courseMap = new HashMap<>();
		makeCourses(typeList, subjectList, courseMap);
		List<V4Course> courseList = new ArrayList<>(courseMap.values());
		
		// 构建student
		List<V4Student> students = new ArrayList<>();
		V4Student student;
		String code = null;
		double score = 0.0;
		Map<V4Course,Integer>  studentCourseScoreMap;
		for (StudentVO vo : stuVOs) {
			student = new V4Student(vo.getName(),vo.getStuId());
			
			studentCourseScoreMap = new HashMap<>();
			for(String subj:subjectList) {
				switch(subj) {
					case "语文":
						code = subj + vo.getYuwenLevel();
						score = vo.getYuwen(); break;
					case "数学":
						code = subj + vo.getMathLevel();
						score = vo.getMath(); break;
					case "英语":
						code = subj + vo.getEngLevel();
						score = vo.getEnglish(); break;
					case "政治":
						code = subj + vo.getPoliticLevel();
						score = vo.getPolitic(); break;
					case "历史":
						code = subj + vo.getHistoryLevel();
						score = vo.getHistory(); break;
					case "地理":
						code = subj + vo.getGeoLevel();
						score = vo.getGeo(); break;
					case "化学":
						code = subj + vo.getChemistryLevel();
						score = vo.getChemistry(); break;
					case "生物":
						code = subj + vo.getBiologyLevel();
						score = vo.getBiology(); break;
					case "物理":
						code = subj + vo.getPhysicalLevel();
						score = vo.getPhysics(); break;
					default:
						System.out.println("未知的科目");
				}
				course = courseMap.get(code);
				studentCourseScoreMap.put(course, (int)score);
			}
			student.setStudentCourseScoreMap(studentCourseScoreMap);
			students.add(student);
		}
		
		SectioningSolution solution = makeSolution(sectionSizeMean, sectionSizeMargin, divideCurriculumNum, courseMap, courseList,
				students);
		
		return solution;
	}
	public static SectioningSolution makeSolution2(Map<String, List<StudentVO2>> sectionIDToStuMap, Integer sectionSizeMean, Integer sectionSizeMargin, int divideCurriculumNum,
			Map<String, V4Course> courseMap, List<V4Course> courseList, List<V4Student> students, int mode) {
		List<StudentVO2> zhbStudentVO = sectionIDToStuMap.values().stream().reduce(new ArrayList<StudentVO2>(), (e1,e2)->e1.addAll(e2)?e1:e2);
		Map<String, List<StudentVO2>> zhbStuMap = zhbStudentVO.stream().collect(Collectors.groupingBy(e->e.getStuId()));
		// key:curriculumCode 
		List<Curriculum> curriList = new ArrayList<>();
		Map<String, List<V4Student>> collect = makeCurriculums(courseMap, students, curriList);
		
//		collect.keySet().stream().forEach(e->System.out.println(e+" "+collect.get(e).size()));
		Optional<Integer> reduce = collect.values().stream().map(e->e.size()).reduce((e,e2)->e+e2);
		System.out.println("数量："+reduce.orElse(0));
		//  studentCurriculum 
		Map<String, List<V4Student>> v4StuMap = students.stream().collect(Collectors.groupingBy(V4Student::getStudentId));
		List<StudentCurriculum> stuCurriuList = new ArrayList<>();
		for (Curriculum curriculum : curriList) {
			List<V4Student> studentsT = new ArrayList<>(curriculum.getStudentInCurriculumList());
			int size = studentsT.size();
			List<StudentCurriculum> stuCurrisT = new ArrayList<>();
			
			//TODO 在分之前先把 组合班的数据拿出来
			Map<String, List<StudentVO2>> zhbStuMapT = studentsT.stream().filter(e->zhbStuMap.containsKey(e.getStudentId()))
					.map(e->zhbStuMap.get(e.getStudentId()).get(0))
					.collect(Collectors.groupingBy(StudentVO2::getSectionID));
			
			int j = 1;
			for (String zhbId : zhbStuMapT.keySet()) {
				List<StudentVO2> list = zhbStuMapT.get(zhbId);
				List<V4Student> zhbStusT = list.stream().map(e->v4StuMap.get(e.getStuId()).get(0)).collect(Collectors.toList());
				String[] sectionCodeArr  = new String[] {list.get(0).getSectionSubject1()+"A",
						list.get(0).getSectionSubject2()+"A"};
				if(2 == mode) {
					// 纯2+X的情况，需要把A课程都去掉
					sectionCodeArr  = new String[] {list.get(0).getChooseSubject1()+"A",
							list.get(0).getChooseSubject2()+"A", list.get(0).getChooseSubject3()+"A"};
				}
				Set<String> sectionCodes = Arrays.stream(sectionCodeArr).collect(Collectors.toSet());
				
				List<V4Course> courseList2 = curriculum.getCourseInCurriculumList().stream()
						.filter(e->!sectionCodes.contains(e.getCourseCode()))
						.collect(Collectors.toList());
				
				
				StudentCurriculum studentCurriculum = new StudentCurriculum(curriculum,j++);
				studentCurriculum.setCourseInStucurriculumList(courseList2);
				studentCurriculum.addStudents(zhbStusT);
				zhbStusT.stream().forEach(e->e.setCurriculumSection(studentCurriculum));
				zhbStusT.forEach(e->{
					List<StudentCurriculum> stuCurriculumsT = new ArrayList<>();
					stuCurriculumsT.add(studentCurriculum);
					e.setCurriculumSectionList(stuCurriculumsT);
				});
				
				stuCurriuList.add(studentCurriculum);
				stuCurrisT.add(studentCurriculum);
				
				studentsT.removeAll(zhbStusT);
			}
			
			int start = 0;
			int end = 0;
			int remainCount = studentsT.size();
			List<StudentCurriculum> stuCurrisT2 = new ArrayList<>();
			while(remainCount > 0) {
				end += divideCurriculumNum;
				if(end >= studentsT.size()) {
					end = studentsT.size();
				}
				StudentCurriculum studentCurriculum = new StudentCurriculum(curriculum,j++);
				List<V4Student> subList = studentsT.subList(start, end);
				studentCurriculum.addStudents(subList);
				subList.stream().forEach(e->e.setCurriculumSection(studentCurriculum));
				
				stuCurriuList.add(studentCurriculum);
				stuCurrisT.add(studentCurriculum);
				stuCurrisT2.add(studentCurriculum);
				
				start = end;
				remainCount -= divideCurriculumNum;
			}
			
			curriculum.setCurriculumSectionList(stuCurrisT);
			studentsT.stream().forEach(e->e.setCurriculumSectionList(stuCurrisT2));
		}
		
		SectioningSolution solution = makeFinalSolution(sectionSizeMean, sectionSizeMargin, courseList, students,
				curriList, stuCurriuList);
		
		batchId(solution);
		if(!validate(solution)) {
			return null;
		}
		
		return solution;
	}
	private static Map<String, List<V4Student>> makeCurriculums(Map<String, V4Course> courseMap,
			List<V4Student> students, List<Curriculum> curriList) {
		Map<String, List<V4Student>> collect = students.stream().collect(Collectors.groupingBy(e->e.getCurriculumCode()));
		for (Entry<String, List<V4Student>> entry : collect.entrySet()) {
			String curriculumCode = entry.getKey();
			List<V4Student> stuList = entry.getValue();
			String[] split = curriculumCode.split("-");
			
			List<V4Course> courses = new ArrayList<>();
			for (String code2 : split) {
				courses.add(courseMap.get(code2));
			}
			
			Curriculum curriculum = new Curriculum(courses, stuList);
			curriList.add(curriculum);
		}
		return collect;
	}
	private static SectioningSolution makeFinalSolution(Integer sectionSizeMean, Integer sectionSizeMargin,
			List<V4Course> courseList, List<V4Student> students, List<Curriculum> curriList,
			List<StudentCurriculum> stuCurriuList) {
		// CurriculumCourse
		List<CurriculumCourse> curriculumCourses = new ArrayList<>();
		for(StudentCurriculum studentCurriculum:stuCurriuList) {
			for(V4Course courseT:studentCurriculum.getCourseList()) {
				CurriculumCourse curriculumCourse = new CurriculumCourse(courseT, studentCurriculum);
				curriculumCourses.add(curriculumCourse);
			}
		}
		
		// section
		Map<V4Course,List<V4Student>> courseStusMap = new HashMap<>();
		for (V4Student stu : students) {
			for (V4Course course2:stu.getStudentCurriculum().getCourseList()) {
				List<V4Student> list = courseStusMap.get(course2);
				if(list == null) {
					list = new ArrayList<>();
					courseStusMap.put(course2, list);
				}
				list.add(stu);
			}
		}
		List<CourseSection> sectionList = new ArrayList<>();
		for (V4Course course2 : courseStusMap.keySet()) {
			List<V4Student> list = courseStusMap.get(course2);
			Double avgScore = list.stream().map(e->e.getStudentCourseScoreMap().get(course2))
				.collect(Collectors.averagingDouble(e->e));
			int sectionCount = CalculateSections.calculateSectioning(list.size(), sectionSizeMean, sectionSizeMargin).size();
			
			if (sectionCount * (sizeMore - 1) < 3) {
				sectionCount += 3;
			}
			else {
				sectionCount = (int)(sectionCount*sizeMore);
			}
			
			int j = 1;
			List<CourseSection> sectionsT = new ArrayList<>();
			for(int i=0;i<sectionCount;i++) {
				CourseSection courseSection = new CourseSection(course2, j++, (int)(avgScore*100), true, sectionSizeMean, sectionSizeMean+sectionSizeMargin);
				sectionList.add(courseSection);
				sectionsT.add(courseSection);
			}
			course2.setCourseSectionList(sectionsT);
		}
		
		// for CurriculumCourse->courseSection /List
		Map<V4Course, List<CourseSection>> courseSectionMap = sectionList.stream().collect(Collectors.groupingBy(e->e.getCourse()));
		for (CurriculumCourse curriCourse : curriculumCourses) {
			List<CourseSection> sectionsT = courseSectionMap.get(curriCourse.getCourse());
			curriCourse.setCourseSectionList(sectionsT);
			if(CollectionUtils.isNotEmpty(sectionsT)) {
				int randomIndex = (int)(Math.random()*sectionsT.size());
				CourseSection courseSection = sectionsT.get(randomIndex);
				curriCourse.setCourseSection(courseSection);
				
				courseSection.addCurriculumCourse(curriCourse);
			}else {
				System.out.println(curriCourse.getCourse().getCourseCode()+"没有开班级");
			}
		}
		
		// 构建 最终结果
		SectioningSolution solution = new SectioningSolution();
		solution.setAppName("dev");
		solution.setCourseList(courseList);
		solution.setStudentList(students);
		solution.setCurriculumList(curriList);
		solution.setStudentCurriculumList(stuCurriuList);
		solution.setCurriculumCourseList(curriculumCourses);
		solution.setCourseSectionList(sectionList);
		return solution;
	}
	public static SectioningSolution makeSolution(Integer sectionSizeMean, Integer sectionSizeMargin, int divideCurriculumNum,
			Map<String, V4Course> courseMap, List<V4Course> courseList, List<V4Student> students) {
		// key:curriculumCode 
		List<Curriculum> curriList = new ArrayList<>();
		Map<String, List<V4Student>> collect = makeCurriculums(courseMap, students, curriList);
		
//		collect.keySet().stream().forEach(e->System.out.println(e+" "+collect.get(e).size()));
		Optional<Integer> reduce = collect.values().stream().map(e->e.size()).reduce((e,e2)->e+e2);
		System.out.println("数量："+reduce.orElse(0));
		//  studentCurriculum 
		List<StudentCurriculum> stuCurriuList = new ArrayList<>();
		for (Curriculum curriculum : curriList) {
			List<V4Student> studentsT = curriculum.getStudentInCurriculumList();
			int size = curriculum.getStudentInCurriculumList().size();
			List<StudentCurriculum> stuCurrisT = new ArrayList<>();
			int j = 1;
			int start = 0;
			int end = 0;
			int remainCount = size;
			while(remainCount >= 0) {
				end += divideCurriculumNum;
				if(end >= size) {
					end = size;
				}
				StudentCurriculum studentCurriculum = new StudentCurriculum(curriculum,j++);
				List<V4Student> subList = studentsT.subList(start, end);
				studentCurriculum.addStudents(subList);
				subList.stream().forEach(e->e.setCurriculumSection(studentCurriculum));
				
				stuCurriuList.add(studentCurriculum);
				stuCurrisT.add(studentCurriculum);
				
				start = end;
				remainCount -= divideCurriculumNum;
			}
			
			curriculum.setCurriculumSectionList(stuCurrisT);
			curriculum.getStudentInCurriculumList().stream().forEach(e->e.setCurriculumSectionList(stuCurrisT));
		}
		
		SectioningSolution solution = makeFinalSolution(sectionSizeMean, sectionSizeMargin, courseList, students, curriList, stuCurriuList);
		
		/*// CurriculumCourse
		List<CurriculumCourse> curriculumCourses = new ArrayList<>();
		for(StudentCurriculum studentCurriculum:stuCurriuList) {
			for(V4Course courseT:studentCurriculum.getCourseList()) {
				CurriculumCourse curriculumCourse = new CurriculumCourse(courseT, studentCurriculum);
				curriculumCourses.add(curriculumCourse);
			}
		}
		
		// section
		Map<V4Course,List<V4Student>> courseStusMap = new HashMap<>();
		for (V4Student stu : students) {
			for (V4Course course2:stu.getStudentCourseScoreMap().keySet() ) {
				List<V4Student> list = courseStusMap.get(course2);
				if(list == null) {
					list = new ArrayList<>();
					courseStusMap.put(course2, list);
				}
				list.add(stu);
			}
		}
		List<CourseSection> sectionList = new ArrayList<>();
		for (V4Course course2 : courseStusMap.keySet()) {
			List<V4Student> list = courseStusMap.get(course2);
			Double avgScore = list.stream().map(e->e.getStudentCourseScoreMap().get(course2))
					.collect(Collectors.averagingDouble(e->e));
			int sectionCount = CalculateSections.calculateSectioning(list.size(), sectionSizeMean, sectionSizeMargin).size();
			
			if (sectionCount * (sizeMore - 1) < 3) {
				sectionCount += 3;
			}
			else {
				sectionCount = (int)(sectionCount*sizeMore);
			}
			
			int j = 1;
			List<CourseSection> sectionsT = new ArrayList<>();
			for(int i=0;i<sectionCount;i++) {
				CourseSection courseSection = new CourseSection(course2, j++, (int)(avgScore*100), true, sectionSizeMean, sectionSizeMean+sectionSizeMargin);
				sectionList.add(courseSection);
				sectionsT.add(courseSection);
			}
			course2.setCourseSectionList(sectionsT);
		}
		
		// for CurriculumCourse->courseSection /List
		Map<V4Course, List<CourseSection>> courseSectionMap = sectionList.stream().collect(Collectors.groupingBy(e->e.getCourse()));
		for (CurriculumCourse curriCourse : curriculumCourses) {
			List<CourseSection> sectionsT = courseSectionMap.get(curriCourse.getCourse());
			curriCourse.setCourseSectionList(sectionsT);
			if(CollectionUtils.isNotEmpty(sectionsT)) {
				int randomIndex = (int)(Math.random()*sectionsT.size());
				CourseSection courseSection = sectionsT.get(randomIndex);
				curriCourse.setCourseSection(courseSection);
				
				courseSection.addCurriculumCourse(curriCourse);
			}else {
				System.out.println(curriCourse.getCourse().getCourseCode()+"没有开班级");
			}
		}
		
		// 构建 最终结果
		SectioningSolution solution = new SectioningSolution();
		solution.setAppName("dev");
		solution.setCourseList(courseList);
		solution.setStudentList(students);
		solution.setCurriculumList(curriList);
		solution.setStudentCurriculumList(stuCurriuList);
		solution.setCurriculumCourseList(curriculumCourses);
		solution.setCourseSectionList(sectionList);*/
		
		batchId(solution);
		if(!validate(solution)) {
			return null;
		}
		
		return solution;
	}
	private static void makeCourses(List<String> typeList, List<String> subjectList,
			Map<String, V4Course> courseMap) {
		V4Course course;
		for (String subj : subjectList) {
			for (String type : typeList) {
				String courseCode  =subj+type;
				course = new V4Course(courseCode);
				courseMap.put(courseCode, course);
			}
		}
	}

	private static List<StudentVO> importData(String fileName) {
		InputStream input = ToData.class.getClassLoader().getResourceAsStream("data/"+fileName);
		List<StudentVO> stuVOs = new ExcelUtil<StudentVO>(StudentVO.class).importExcel("Sheet2", input);
		
		stuVOs.stream().forEach(e->{
			double d = Double.parseDouble(e.getClassNum());
			e.setClassNum(((int)d)+"");
		});
		
		Set<String> collect = stuVOs.stream().map(e->e.getClassNum()).collect(Collectors.toSet());
		System.out.println(collect);
		System.out.println(collect.size());
		return stuVOs;
	}
	
	
}
