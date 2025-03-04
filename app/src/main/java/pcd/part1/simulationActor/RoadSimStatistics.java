package pcd.part1.simulationActor;

import akka.actor.ActorSystem;
import pcd.part1.abstractSim.AbstractAgent;
import pcd.part1.abstractSim.AbstractEnvironment;
import pcd.part1.abstractSim.SimulationListener;
import pcd.part1.model.CarAgent;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Simple class keeping track of some statistics about a traffic simulation
 * - average number of cars
 * - min speed
 * - max speed
 */
public class RoadSimStatistics implements SimulationListener {

	
	private double averageSpeed;
	private double minSpeed;
	private double maxSpeed;
	
	public RoadSimStatistics() {
	}
	



	
	public double getAverageSpeed() {
		return this.averageSpeed;
	}

	public double getMinSpeed() {
		return this.minSpeed;
	}
	
	public double getMaxSpeed() {
		return this.maxSpeed;
	}
	
	
	private void log(String msg) {
		System.out.println("[STAT] " + msg);
	}

	@Override
	public void notifyInit(int t) {
		averageSpeed = 0;
	}

	@Override
	public void notifyStepDone(int t, ActorSystem system) {
		double avSpeed = 0;

		maxSpeed = -1;
		minSpeed = Double.MAX_VALUE;

		for (var agent: agents) {
			CarAgent car = (CarAgent) agent;
			double currSpeed = car.getCurrentSpeed();
			avSpeed += currSpeed;
			if (currSpeed > maxSpeed) {
				maxSpeed = currSpeed;
			} else if (currSpeed < minSpeed) {
				minSpeed = currSpeed;
			}
		}

		if (agents.size() > 0) {
			avSpeed /= agents.size();
		}
		log("average speed: " + avSpeed);
	}
}
