package pcd.part1.abstractSim;

import akka.actor.ActorSystem;

import java.util.List;

public interface SimulationListener {

	/**
	 * Called at the beginning of the simulation
	 *
	 * @param t the time of the simulation
	 */
	void notifyInit(int t);

	/**
	 * Called at each step, updater all updates
	 * @param t the time of the simulation
	 */
	void notifyStepDone(int t, ActorSystem system);
}