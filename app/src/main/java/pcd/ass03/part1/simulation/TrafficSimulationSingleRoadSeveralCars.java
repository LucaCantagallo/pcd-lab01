package pcd.ass03.part1.simulation;


import akka.actor.Props;
import akka.pattern.Patterns;
import pcd.ass03.part1.abstractSim.AbstractSimulation;
import pcd.ass03.part1.actor.CarAgentActor;
import pcd.ass03.part1.model.Message;
import pcd.ass03.part1.model.P2d;
import pcd.ass03.part1.model.Road;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * Traffic Simulation about a number of cars 
 * moving on a single road, no traffic lights
 * 
 */
public class TrafficSimulationSingleRoadSeveralCars extends AbstractSimulation {

	public TrafficSimulationSingleRoadSeveralCars() {
		super(30);
	}
	
	public void setup() {

		this.setupTimings(0, 1);

		Road r;
		Future<Object> future = Patterns.ask(system.actorSelection("/user/roadenv"), new Message("create-road", List.of(new P2d(0, 300), new P2d(1500, 300))), 1000);
		try {
			r = (Road) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
		} catch (TimeoutException | InterruptedException e) {
			throw new RuntimeException(e);
		}

		for (int i = 0; i < this.numCars; i++) {
			
			String carId = "car-" + i;
			// double initialPos = i*30;
			double initialPos = i*10;
			
			double carAcceleration = 1; //  + gen.nextDouble()/2;
			double carDeceleration = 0.3; //  + gen.nextDouble()/2;
			double carMaxSpeed = 7; // 4 + gen.nextDouble();
			system.actorOf(Props.create(CarAgentActor.class, carId, r, initialPos, carAcceleration, carDeceleration, carMaxSpeed), carId);

		}
		
		this.syncWithTime(25);
	}	
}
	