package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning3.api;

import java.io.IOException;
import java.util.List;

import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning3.domain7.SectioningSolution;

public class Sectioning2A1X {
	static final Logger LOG = LoggerFactory.getLogger(Sectioning2A1X.class);
	
	private String configFileName = "solver7/sectioning2A1XSolverConfig1.xml";
	
	private Sectioning2A1XInput s2a1xInput;

	public Sectioning2A1X(Sectioning2A1XInput s2a1xInput) {
		this.s2a1xInput = s2a1xInput;
	}
	
	
	/**
	 * 
	 * @param group3AList:			{<选课1-3A><选课2-3A><选课3-3A><人数>} 
	 * @param maxTeacherCountList:	{<课名><老师数量供1X部分使用>}
	 * @param excludedGroup2AList:	{<选课1-2A><选课2-2A>}
	 * @param pre1XList: 			{<选课1-3A><选课2-3A><选课3-3A><人数><选课1X>}
	 * @return:  					{<选课1-2A><选课2-2A> <选课1-3A><选课2-3A><选课3-3A><人数>}
	 */
	public List<List<String>> calculateSectioning2A1X() throws IOException {
		
		List<List<String>> group3AList = s2a1xInput.getGroup3AList();
		List<List<String>> maxTeacherCountList = s2a1xInput.getMaxTeacherCountList();
		List<List<String>> excludedGroup2AList = s2a1xInput.getExcludedGroup2AList();
		List<List<String>> pre1XList = s2a1xInput.getPre1XList();
		int maxRoomCount = s2a1xInput.getMaxRoomCount();
		int pure3ASectionCount = s2a1xInput.getPure3ASectionCount();
		int sectionSizeMean = s2a1xInput.getSectionSizeMean();
		int sectionSizeMargin = s2a1xInput.getSectionSizeMargin();
		List<List<String>> sectionSizeList = s2a1xInput.getSectionSizeList();
		
		SectioningSolution solution = new SectioningSolution(group3AList, 
				maxTeacherCountList, 
				excludedGroup2AList, 
				pre1XList,
				sectionSizeMean, 
				sectionSizeMargin, 
				maxRoomCount, 
				sectionSizeList,
				pure3ASectionCount);
		SectioningSolution resultSolution = solve(solution, configFileName);
		List<List<String>> result = resultSolution.composeResult();
		
//		System.out.println("BEGIN: Print Result in calculateSectioning2A1X...");
//		result.stream().forEach(e -> System.out.println(e));
//		System.out.println("END: Print Result in calculateSectioning2A1X...");


		return result; 
	}
	
	private SectioningSolution solve (SectioningSolution sectionSolution, String configFileName) throws IOException {
		SolverFactory<SectioningSolution> solverFactory = SolverFactory.createFromXmlResource(configFileName);
		
		Solver<SectioningSolution> solver = solverFactory.buildSolver();
		solver.addEventListener(new SolverEventListener<SectioningSolution>() {
	        public void bestSolutionChanged(BestSolutionChangedEvent<SectioningSolution> event) {
				BendableScore score = (BendableScore) event.getNewBestScore();
				LOG.info("hard[0]: " + score.getHardScore(0) + 
								 "  hard[1]: " + score.getHardScore(1) + 
								 "  hard[2]: " + score.getHardScore(2) + 
								 "  hard[3]: " + score.getHardScore(3) + 
								 "  soft[0]: " + score.getSoftScore(0) + 
								 "  soft[1]: " + score.getSoftScore(1));
	        }
	    });

		return solver.solve(sectionSolution);
	}		
}

