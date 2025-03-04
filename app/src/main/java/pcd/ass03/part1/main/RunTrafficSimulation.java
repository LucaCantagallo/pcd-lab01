package pcd.ass03.part1.main;

import pcd.ass03.part1.simulation.TrafficSimulationWithCrossRoads;
import pcd.ass03.part1.view.RoadSimView;
import pcd.ass03.part1.view.ViewController;

/**
 * 
 * Main class to create and run a simulation
 * 
 */
public class RunTrafficSimulation {

	public static void main(String[] args) {

		//var simulation = new TrafficSimulationSingleRoadTwoCars();
		var simulation = new TrafficSimulationWithCrossRoads();
		//var simulation = new TrafficSimulationSingleRoadSeveralCars();
		//var simulation = new TrafficSimulationSingleRoadWithTrafficLightTwoCars();

		simulation.setup();
		ViewController controller = new ViewController(simulation, new RoadSimView());
	}
}
