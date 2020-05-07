package net.zdsoft.newgkelective.data.optaplanner.test;

import java.io.File;

import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;

import net.zdsoft.newgkelective.data.optaplanner.domain.scheduling.CGStudentCourseSchedule;
import net.zdsoft.newgkelective.data.optaplanner.dto.CGInputData;
import net.zdsoft.newgkelective.data.optaplanner.util.DtoToData;
import net.zdsoft.newgkelective.data.optaplanner.util.XmlUtils;

public class ScheduleInputSolutionIO implements SolutionFileIO<CGStudentCourseSchedule> {

	@Override
	public String getInputFileExtension() {
		return "xml";
	}

	@Override
	public String getOutputFileExtension() {
		return "xml";
	}

	@Override
	public CGStudentCourseSchedule read(File arg0) {
		CGInputData beanFromFile = XmlUtils.toBeanFromFile(arg0.getAbsolutePath(), CGInputData.class);
		CGStudentCourseSchedule transfer = DtoToData.transfer(beanFromFile);
		transfer.calculateCourseConflictList();
		return transfer;
	}

	@Override
	public void write(CGStudentCourseSchedule arg0, File arg1) {
		
	}

}
