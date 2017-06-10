package com.cloudlife.nsga2;

public class Nsga2Test {

	static private Nsga2Test nsga2;
	static public Nsga2Test getInstance() {
		if (nsga2 == null)
			nsga2 = new Nsga2Test();
		return nsga2;
	}
	
	private Nsga2Test() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				UserModel model = new UserModel();
				TargetFunImp fun = new TargetFunImp(model);
				Population pop = new Population();
				pop.produceIndividual(20, model);
				Nsga2 nsga2 = new Nsga2();
				nsga2.startEvolution(pop, fun.getTargetFun(), model);	
			}
		}).start();
	}
}








