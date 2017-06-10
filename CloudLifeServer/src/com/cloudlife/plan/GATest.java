package com.cloudlife.plan;

public class GATest {

	static private GATest m_Test;
	
	static public GATest getInstance() {
		if (m_Test == null)
			m_Test = new GATest();
		return m_Test;
	}
	
	private GATest() {
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("进入子线程执行任务==");
				DietPlanPopulation pop = new DietPlanPopulation(20);
				DietPlanGA ga = new DietPlanGA();
				ga.startEvolution(pop);
				System.out.println("执行完毕!");
			}
		}).start();
	}
	
	
}
