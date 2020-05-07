package net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.solver;

import java.io.IOException;

import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;

import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.dio.*;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain.*;


public class SectioningApp {
	private static final String SOLVER_CONFIG_1 = "businessconf/sectionscheduling/sectioningLayerSolverConfig1.xml";
	private static final String SOLVER_CONFIG_2="businessconf/sectionscheduling/sectioningLayerSolverConfig2.xml";


	public SectioningSolution solve (SectioningSolution sectionSolution, boolean isGuize,boolean isShowScore) throws IOException {
		SolverFactory<SectioningSolution> solverFactory ;
		if(isGuize){
			//SOLVER_CONFIG_1
			solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG_1);
		}else{
			//一般先使用这个
			solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG_2);
		}
		
		Solver<SectioningSolution> solver = solverFactory.buildSolver();
		solver.addEventListener(new SolverEventListener<SectioningSolution>() {
	        public void bestSolutionChanged(BestSolutionChangedEvent<SectioningSolution> event) {
				BendableScore score = (BendableScore) event.getNewBestScore();
				if(isShowScore){
					System.out.println("hard[0]: " + score.getHardScore(0) + 
							 "  hard[1]: " + score.getHardScore(1) + 
							 "  hard[2]: " + score.getHardScore(2) + 
							 "  hard[3]: " + score.getHardScore(3) + 
							 "  soft[0]: " + score.getSoftScore(0) + 
							 "  soft[1]: " + score.getSoftScore(1));
				}
				
	        }
	    });

		return solver.solve(sectionSolution);
	}
	
	

	public static void main(String[] args) {
		
		//除了“蜀关中学单科分层.xls”， 都可以用。第二个参数要么'A'(选考三门)，要么'B'(学考的3门或者4门)
		ExcelFileDataLoader3A dataLoader = new ExcelFileDataLoader3A("富顺二中.xls", 'B');
		
		//ExcelFileDataLoader3A4B 是用来测试6门或者7门课一起排的情况，速度确实慢很多，效果也不好
		//ExcelFileDataLoader3A4B dataLoader = new ExcelFileDataLoader3A4B("合肥一中.xls");
		
		//ExcelFileDataLoader 是 “蜀关中学单科分层.xls” 这个例子独用的，任何其它数据文件都不可以
		//ExcelFileDataLoader dataLoader = new ExcelFileDataLoader("蜀关中学单科分层.xls");
		
		SectioningSolution solution = dataLoader.buildSolution();
		
		try {
			SectioningSolution resultSolution = (new SectioningApp()).solve(solution, true,true);
			resultSolution.printResult();
			//resultSolution.validate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
