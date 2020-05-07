package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.solver;

import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain.SectioningSolution;

/**
 * Hello world!
 *
 */
public class SectioningApp {
	private SolverFactory<SectioningSolution> factory;
	{
		factory = SolverFactory
				.createFromXmlResource("businessconf/divideSolver/studentCourseSectioningSolverConfig.xml");
	}

	public static void main(String[] args) {
		
	}
	
	public SectioningSolution doSolve(SectioningSolution solution) {
		Solver<SectioningSolution> solver = factory.buildSolver();
//		printTrace(solver);
		
//		solution.printCurriculumList();
		 
		SectioningSolution finalSolution = solver.solve(solution);
		
//		finalSolution.printCourseSectionList();

		return finalSolution;
	}

	private void printTrace(Solver<SectioningSolution> solver) {
		solver.addEventListener(new SolverEventListener<SectioningSolution>() {
			@Override
			public void bestSolutionChanged(BestSolutionChangedEvent<SectioningSolution> event) {
				BendableScore score = (BendableScore) event.getNewBestScore();
				System.out.println("hard[0]: " + score.getHardScore(0) + 
								 "  hard[1]: " + score.getHardScore(1) + 
								 "  hard[2]: " + score.getHardScore(2) + 
								 "  hard[3]: " + score.getHardScore(3) + 
								 "  soft[0]: " + score.getSoftScore(0) + 
								 "  soft[1]: " + score.getSoftScore(1));
			}
		});
	}
}
