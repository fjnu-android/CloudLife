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
				System.out.println("�������߳�ִ������==");
				DietPlanPopulation pop = new DietPlanPopulation(20);
				DietPlanGA ga = new DietPlanGA();
				ga.startEvolution(pop);
				System.out.println("ִ�����!");
			}
		}).start();
	}
	
	
}
