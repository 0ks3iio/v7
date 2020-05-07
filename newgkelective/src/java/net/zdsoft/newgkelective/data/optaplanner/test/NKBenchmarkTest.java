package net.zdsoft.newgkelective.data.optaplanner.test;

import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;

public class NKBenchmarkTest {

	public static void main(String[] args) {
		PlannerBenchmarkFactory plannerBenchmarkFactory = PlannerBenchmarkFactory.createFromXmlResource("test/benchmark/curriculumCourseBenchmarkConfig.xml");
		PlannerBenchmark plannerBenchmark = plannerBenchmarkFactory.buildPlannerBenchmark();
		plannerBenchmark.benchmark();
	}

}
