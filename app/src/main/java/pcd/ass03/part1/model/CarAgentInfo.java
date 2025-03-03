package pcd.ass03.part1.model;


import pcd.ass03.part1.actor.CarAgentActor;

public  class CarAgentInfo {

	private final CarAgentActor car;
	private double pos;
	private final Road road;
	
	public CarAgentInfo(CarAgentActor car, Road road, double pos) {
		this.car = car;
		this.road = road;
		this.pos = pos;
	}
	
	public double getPos() {
		return pos;
	}
	
	public void updatePos(double pos) {
		this.pos = pos;
	}

	public CarAgentActor getCar() {
		return car;
	}
	
	public Road getRoad() {
		return road;
	}
}
