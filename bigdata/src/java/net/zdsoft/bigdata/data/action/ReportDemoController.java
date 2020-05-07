package net.zdsoft.bigdata.data.action;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by wangdongdong on 2018/8/7 13:41.
 */
@RequestMapping("bigdata/common/")
@Controller
public class ReportDemoController {

    private static Map<String, List<Rank>> rankMap = Maps.newConcurrentMap();
    private static Map<String, List<Score>> scoreMap = Maps.newConcurrentMap();
    private static Map<String, List<SubjectRank>> subjectMap = Maps.newConcurrentMap();
    private static Map<String, List<ScoreRank>> map = Maps.newConcurrentMap();

    @ResponseBody
    @RequestMapping("/getAllYear")
    public String getAllYear() {
        JSONObject json = new JSONObject();
        json.put("infolist", Year.getAllYear());
        return json.toJSONString();
    }

    @RequestMapping("/getAllExam")
    @ResponseBody
    public String getAllExam(String yearId) {
        JSONObject json = new JSONObject();
        json.put("infolist", Exam.getAllExam(yearId));
        return json.toJSONString();
    }

    @RequestMapping("/getAllGrade")
    @ResponseBody
    public String getAllGrade(String examId) {
        JSONObject json = new JSONObject();
        json.put("infolist", Grade.getAllGrade(examId));
        return json.toJSONString();
    }

    @RequestMapping("/getAllClass")
    @ResponseBody
    public String getAllClass(String gradeId) {
        JSONObject json = new JSONObject();
        json.put("infolist", Class.getAllClass(gradeId));
        return json.toJSONString();
    }

    @RequestMapping("/getAllStudent")
    @ResponseBody
    public String getAllStudent(String classId) {
        JSONObject json = new JSONObject();
        json.put("infolist", Student.getAllStudent(classId));
        return json.toJSONString();
    }

    @RequestMapping("/getStudentRank")
    @ResponseBody
    public String getStudentRank(String studentId) {
        JSONObject json = new JSONObject();
        json.put("infolist", Rank.getAllRank(studentId));
        return json.toJSONString();
    }

    @RequestMapping("/getStudentScore")
    @ResponseBody
    public String getStudentScore(String studentId) {
        JSONObject json = new JSONObject();
        json.put("infolist", Score.getAllScore(studentId));
        return json.toJSONString();
    }

    @RequestMapping("/getSubjectRank")
    @ResponseBody
    public String getSubjectRank(String gradeId) {
        JSONObject json = new JSONObject();
        json.put("infolist", SubjectRank.getAllRank(gradeId));
        return json.toJSONString();
    }

    @RequestMapping("/getScoreRank")
    @ResponseBody
    public String getScoreRank(String classId) {
        JSONObject json = new JSONObject();
        json.put("infolist", ScoreRank.getAllRank(classId));
        return json.toJSONString();
    }

    static class Class implements Serializable {

        private String id;

        private String name;

        private String gradeId;

        public Class(String id, String name, String gradeId) {
            this.id = id;
            this.name = name;
            this.gradeId = gradeId;
        }

        public Class() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGradeId() {
            return gradeId;
        }

        public void setGradeId(String gradeId) {
            this.gradeId = gradeId;
        }

        public static List<Class> getAllClass(String gradeId) {
            List<Class> classes = Lists.newArrayList();
            if (gradeId.equals("2")) {
                classes.add(new Class("1", "小二01班", gradeId));
                classes.add(new Class("2", "小二02班", gradeId));
                classes.add(new Class("3", "小二03班", gradeId));
                classes.add(new Class("4", "小二04班", gradeId));
                classes.add(new Class("5", "小二05班", gradeId));
            }
            return classes;
        }

    }
    static class Exam implements Serializable {

        private String id;

        private String name;

        private String yearId;

        public Exam(String id, String name, String yearId) {
            this.id = id;
            this.name = name;
            this.yearId = yearId;
        }

        public Exam() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getYearId() {
            return yearId;
        }

        public void setYearId(String yearId) {
            this.yearId = yearId;
        }

        public static List<Exam> getAllExam(String yearId) {
            Exam exam1 = new Exam("1", "2017-2018第二学期其他（区县）", "1");
            Exam exam2 = new Exam("2", "2017-2018第二学期期中考试", "1");
            Exam exam3 = new Exam("3", "2017-2018第二学期期末考试", "1");
            return Lists.newArrayList(exam1, exam2, exam3);
        }
    }
    static class Grade implements Serializable {

        private String id;

        private String name;

        private String examId;

        public Grade(String id, String name, String examId) {
            this.id = id;
            this.name = name;
            this.examId = examId;
        }

        public Grade() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExamId() {
            return examId;
        }

        public void setExamId(String examId) {
            this.examId = examId;
        }

        public static List<Grade> getAllGrade(String examId) {
            List<Grade> grades = Lists.newArrayList();
            if (examId.equals("1")) {
                grades.add(new Grade("1", "小一", examId));
                grades.add(new Grade("2", "小二", examId));
                grades.add(new Grade("3", "小三", examId));
            } else if (examId.equals("2")) {
                grades.add(new Grade("1", "小一", examId));
                grades.add(new Grade("2", "小二", examId));
                grades.add(new Grade("3", "小三", examId));
            } else if (examId.equals("3")) {
                grades.add(new Grade("1", "小一", examId));
                grades.add(new Grade("2", "小二", examId));
                grades.add(new Grade("3", "小三", examId));
            } else {
                grades.add(new Grade("1", "小一", "1"));
                grades.add(new Grade("2", "小二", "1"));
                grades.add(new Grade("3", "小三", "1"));
            }
            return grades;
        }
    }
    static class Rank implements Serializable {

        private String type;

        private Integer rank;

        private String exam;

        private String studentId;

        public Rank(String type, Integer rank, String exam, String studentId) {
            this.type = type;
            this.rank = rank;
            this.exam = exam;
            this.studentId = studentId;
        }

        public Rank() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }

        public String getExam() {
            return exam;
        }

        public void setExam(String exam) {
            this.exam = exam;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public static List<Rank> getAllRank(String studentId) {
            List<Rank> ranks = rankMap.get(studentId);
            if (ranks != null) {
                return ranks;
            }
            Random random = new Random();
            ranks = Lists.newArrayList();
            int a1 = random.nextInt(20) + 2;
            int a2 = random.nextInt(30) + 2;
            int a3 = random.nextInt(20) + 2;
            int a4 = random.nextInt(30) + 2;
            ranks.add(new Rank("班级排名", a1, "高一(上)期中考试联考", studentId));
            ranks.add(new Rank("班级排名", a2, "高一(上)期末考试联考", studentId));
            ranks.add(new Rank("班级排名", a3, "高一(下)期中考试联考", studentId));
            ranks.add(new Rank("班级排名", a4, "高一(下)期末考试联考", studentId));

            int b1 = random.nextInt(30) + 8;
            while (b1 < a1) {
                b1 = random.nextInt(30) + 8;
            }

            int b2 = random.nextInt(70) + 15;
            while (b2 < a2) {
                b2 = random.nextInt(70) + 15;
            }

            int b3 = random.nextInt(30) + 6;
            while (b3< a3) {
                b3 = random.nextInt(30) + 6;
            }

            int b4 = random.nextInt(60) + 12;
            while (b4 < a4) {
                b4 = random.nextInt(60) + 12;
            }
            ranks.add(new Rank("年级排名", b1, "高一(上)期中考试联考", studentId));
            ranks.add(new Rank("年级排名", b2, "高一(上)期末考试联考", studentId));
            ranks.add(new Rank("年级排名", b3, "高一(下)期中考试联考", studentId));
            ranks.add(new Rank("年级排名", b4, "高一(下)期末考试联考", studentId));
            rankMap.put(studentId, ranks);
            return ranks;
        }

    }
    static class Score implements Serializable {

        private String subjectName;

        private Integer value;

        private String studentId;

        public Score(String subjectName, Integer value, String studentId) {
            this.subjectName = subjectName;
            this.value = value;
            this.studentId = studentId;
        }

        public Score() {
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public static List<Score> getAllScore(String studentId) {
            List<Score> scores = scoreMap.get(studentId);
            if (scores != null) {
                return scores;
            }
            Random random = new Random();
            scores = new ArrayList<>();
            scores.add(new Score("小学语文", random.nextInt(30) + 70, studentId));
            scores.add(new Score("小学数学", random.nextInt(30) + 70, studentId));
            scores.add(new Score("小学英语", random.nextInt(30) + 70, studentId));
            scores.add(new Score("小学道德与法制", random.nextInt(30) + 70, studentId));
            scores.add(new Score("小学科学", random.nextInt(30) + 70, studentId));
            scoreMap.put(studentId, scores);
            return scores;
        }

    }
    static class ScoreRank implements Serializable {

        private String type;

        private Integer count;

        private String classId;

        public ScoreRank() {
        }

        public ScoreRank(String type, Integer count, String classId) {
            this.type = type;
            this.count = count;
            this.classId = classId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public static List<ScoreRank> getAllRank(String classId) {
            List<ScoreRank> ranks = map.get(classId);
            if (ranks != null) {
                return ranks;
            }
            ranks = new ArrayList<>();

            if (classId.equals("1")) {
                ranks.add(new ScoreRank("年级前10%", 5, classId));
                ranks.add(new ScoreRank("年级前10%~20%", 11, classId));
                ranks.add(new ScoreRank("年级前20%~40%", 17, classId));
                ranks.add(new ScoreRank("年级前40%~70%", 6, classId));
                ranks.add(new ScoreRank("年级前70%~90%", 5, classId));
                ranks.add(new ScoreRank("年级后10%", 3, classId));
            } else if (classId.equals("2")) {
                ranks.add(new ScoreRank("年级前10%", 2, classId));
                ranks.add(new ScoreRank("年级前10%~20%", 14, classId));
                ranks.add(new ScoreRank("年级前20%~40%", 22, classId));
                ranks.add(new ScoreRank("年级前40%~70%", 1, classId));
                ranks.add(new ScoreRank("年级前70%~90%", 3, classId));
                ranks.add(new ScoreRank("年级后10%", 5, classId));
            }else if (classId.equals("3")) {
                ranks.add(new ScoreRank("年级前10%", 4, classId));
                ranks.add(new ScoreRank("年级前10%~20%", 6, classId));
                ranks.add(new ScoreRank("年级前20%~40%", 15, classId));
                ranks.add(new ScoreRank("年级前40%~70%", 30, classId));
                ranks.add(new ScoreRank("年级前70%~90%", 6, classId));
                ranks.add(new ScoreRank("年级后10%", 1, classId));
            }else if (classId.equals("4")) {
                ranks.add(new ScoreRank("年级前10%", 1, classId));
                ranks.add(new ScoreRank("年级前10%~20%", 9, classId));
                ranks.add(new ScoreRank("年级前20%~40%", 11, classId));
                ranks.add(new ScoreRank("年级前40%~70%", 22, classId));
                ranks.add(new ScoreRank("年级前70%~90%", 10, classId));
                ranks.add(new ScoreRank("年级后10%", 1, classId));
            }else if (classId.equals("5")) {
                ranks.add(new ScoreRank("年级前10%", 3, classId));
                ranks.add(new ScoreRank("年级前10%~20%", 6, classId));
                ranks.add(new ScoreRank("年级前20%~40%", 22, classId));
                ranks.add(new ScoreRank("年级前40%~70%", 13, classId));
                ranks.add(new ScoreRank("年级前70%~90%", 7, classId));
                ranks.add(new ScoreRank("年级后10%", 2, classId));
            }
            map.put(classId, ranks);
            return ranks;
        }

    }
    static class Student implements Serializable {


        private String id;

        private String name;

        private String classId;

        public Student(String id, String name, String classId) {
            this.id = id;
            this.name = name;
            this.classId = classId;
        }

        public Student() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public static List<Student> getAllStudent(String classId) {
            List<Student> students = Lists.newArrayList();
            String stu[] = {"邵琦" ,
                    "杨慧" ,
                    "殷启英" ,
                    "张琪" ,
                    "胡雅晴" ,
                    "牛佳雯" ,
                    "蒲子琳" ,
                    "任俊柯" ,
                    "孙池" ,
                    "辛毓琛" ,
                    "周麟佳" ,
                    "刘玥" ,
                    "宋旭凯" ,
                    "陈一鸣" ,
                    "胡雅倩" ,
                    "李昀潼" ,
                    "王丹丹" ,
                    "杨子成" ,
                    "周鹏宇" ,
                    "朱茈苑 " ,
                    "马瑞泽" ,
                    "杨芯茹" ,
                    "刘禹何" ,
                    "王建策" ,
                    "姚文慧" ,
                    "赵恩至" ,
                    "朱美霖" ,
                    "孙铭辰" ,
                    "陶文轩" ,
                    "陶源" ,
                    "王志鹏 " ,
                    "亢衡" ,
                    "李佳宜" ,
                    "秦源" ,
                    "安相羽" ,
                    "穆雯瑞" ,
                    "袁子涵" ,
                    "陈佳琳" ,
                    "李昊" ,
                    "马恺润" ,
                    "周欣怡" ,
                    "李洪云" ,
                    "赵鸿翔" ,
                    "蔡文钰" ,
                    "陈宽" ,
                    "刘德华"};
            if (classId.equals("1")) {
                int i = 1;
                for (String name : stu) {
                    students.add(new Student("" + i++, name, classId));
                }
            }
            return students;
        }

    }
    static class SubjectRank implements Serializable {

        private String subjectName;

        private String className;

        private Integer rank;

        public SubjectRank(String subjectName, String className, Integer rank) {
            this.subjectName = subjectName;
            this.className = className;
            this.rank = rank;
        }

        public SubjectRank() {
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }

        public static List<SubjectRank> getAllRank(String gradeId) {
            List<SubjectRank> subjectRanks = subjectMap.get(gradeId);
            if (subjectRanks != null) {
                return subjectRanks;
            }
            Random random = new Random();
            subjectRanks = new ArrayList<>();
            if (gradeId.equals("2")) {
                subjectRanks.add(new SubjectRank("小学语文", "小二01班", 77));
                subjectRanks.add(new SubjectRank("小学数学", "小二01班", 88));
                subjectRanks.add(new SubjectRank("小学英语", "小二01班", 84));
                subjectRanks.add(new SubjectRank("小学道德与法制", "小二01班", 92));
                subjectRanks.add(new SubjectRank("小学科学", "小二01班", 76));

                subjectRanks.add(new SubjectRank("小学语文", "小二02班", 79));
                subjectRanks.add(new SubjectRank("小学数学", "小二02班", 87));
                subjectRanks.add(new SubjectRank("小学英语", "小二02班", 76));
                subjectRanks.add(new SubjectRank("小学道德与法制", "小二02班", 89));
                subjectRanks.add(new SubjectRank("小学科学", "小二02班", 83));

                subjectRanks.add(new SubjectRank("小学语文", "小二03班", 83));
                subjectRanks.add(new SubjectRank("小学数学", "小二03班", 89));
                subjectRanks.add(new SubjectRank("小学英语", "小二03班", 82));
                subjectRanks.add(new SubjectRank("小学道德与法制", "小二03班", 79));
                subjectRanks.add(new SubjectRank("小学科学", "小二03班", 82));

                subjectRanks.add(new SubjectRank("小学语文", "小二04班", 79));
                subjectRanks.add(new SubjectRank("小学数学", "小二04班", 86));
                subjectRanks.add(new SubjectRank("小学英语", "小二04班", 93));
                subjectRanks.add(new SubjectRank("小学道德与法制", "小二04班", 82));
                subjectRanks.add(new SubjectRank("小学科学", "小二04班", 71));

                subjectRanks.add(new SubjectRank("小学语文", "小二05班", 76));
                subjectRanks.add(new SubjectRank("小学数学", "小二05班", 87));
                subjectRanks.add(new SubjectRank("小学英语", "小二05班", 78));
                subjectRanks.add(new SubjectRank("小学道德与法制", "小二05班", 83));
                subjectRanks.add(new SubjectRank("小学科学", "小二05班", 93));
            } else if (gradeId.equals("3")) {

                subjectRanks.add(new SubjectRank("小学语文", "小三01班", 84));
                subjectRanks.add(new SubjectRank("小学数学", "小三01班", 82));
                subjectRanks.add(new SubjectRank("小学英语", "小三01班", 91));
                subjectRanks.add(new SubjectRank("小学道德与法制", "小三01班", 77));
                subjectRanks.add(new SubjectRank("小学科学", "小三01班", 82));

                subjectRanks.add(new SubjectRank("小学语文", "小三02班", 72));
                subjectRanks.add(new SubjectRank("小学数学", "小三02班", 88));
                subjectRanks.add(new SubjectRank("小学英语", "小三02班", 76));
                subjectRanks.add(new SubjectRank("小学道德与法制", "小三02班", 89));
                subjectRanks.add(new SubjectRank("小学科学", "小三02班", 81));

                subjectRanks.add(new SubjectRank("小学语文", "小三03班", 78));
                subjectRanks.add(new SubjectRank("小学数学", "小三03班", 85));
                subjectRanks.add(new SubjectRank("小学英语", "小三03班", 73));
                subjectRanks.add(new SubjectRank("小学道德与法制", "小三03班", 94));
                subjectRanks.add(new SubjectRank("小学科学", "小三03班", 81));

                subjectRanks.add(new SubjectRank("小学语文", "小三04班", 79));
                subjectRanks.add(new SubjectRank("小学数学", "小三04班", 87));
                subjectRanks.add(new SubjectRank("小学英语", "小三04班", 83));
                subjectRanks.add(new SubjectRank("小学道德与法制", "小三04班", 93));
                subjectRanks.add(new SubjectRank("小学科学", "小三04班", 81));

                subjectRanks.add(new SubjectRank("小学语文", "小三05班", 89));
                subjectRanks.add(new SubjectRank("小学数学", "小三05班", 82));
                subjectRanks.add(new SubjectRank("小学英语", "小三05班", 86));
                subjectRanks.add(new SubjectRank("小学道德与法制", "小三05班", 92));
                subjectRanks.add(new SubjectRank("小学科学", "小三05班", 76));

            }
            subjectMap.put(gradeId, subjectRanks);
            return subjectRanks;
        }
    }
    static class Year implements Serializable {

        private String name;

        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public static List<Year> getAllYear() {
            Year y1 = new Year();
            y1.setId("1");
            y1.setName("2017-2018学年");
            return Lists.newArrayList(y1);
        }
    }
}

