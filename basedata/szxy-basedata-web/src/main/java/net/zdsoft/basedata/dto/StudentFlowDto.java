package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StudentFlow;

public class StudentFlowDto extends BaseDto {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Student student;
	private StudentFlow studentFlow;
    private String schoolName;
    private String className;
    private String studentName;
    private String identityCard;
    
    
    public StudentFlowDto() {
		super();
		this.studentFlow = new StudentFlow();
	}

	public StudentFlow getStudentFlow() {
		return studentFlow;
	}

	public void setStudentFlow(StudentFlow studentFlow) {
		this.studentFlow = studentFlow;
	}

	public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

}
