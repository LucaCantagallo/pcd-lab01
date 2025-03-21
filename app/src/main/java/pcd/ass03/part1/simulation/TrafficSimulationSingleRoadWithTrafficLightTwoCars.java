package pcd.ass03.part1.simulation;


import akka.actor.Props;
import akka.pattern.Patterns;
import pcd.ass03.part1.abstractSim.AbstractSimulation;
import pcd.ass03.part1.actor.CarAgentActor;
import pcd.ass03.part1.model.Message;
import pcd.ass03.part1.model.P2d;
import pcd.ass03.part1.model.Road;
import pcd.ass03.part1.model.TrafficLight;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * Traffic Simulation about 2 cars moving on a single road, with one semaphore
 * 
 */
public class TrafficSimulationSingleRoadWithTrafficLightTwoCars extends AbstractSimulation {

	public TrafficSimulationSingleRoadWithTrafficLightTwoCars() {
		super(2);
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

		TrafficLight tl;
		future = Patterns.ask(system.actorSelection("/user/roadenv"), new Message("create-traffic-light", List.of(new P2d(740,300), TrafficLight.TrafficLightState.GREEN, 75, 25, 100)), 1000);
		try {
			tl = (TrafficLight) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
		} catch (TimeoutException | InterruptedException e) {
			throw new RuntimeException(e);
		}

		r.addTrafficLight(tl, 740);

		system.actorOf(Props.create(CarAgentActor.class, "car-1", r, 0.0, 0.1, 0.3, 6.0), "car-1");
		system.actorOf(Props.create(CarAgentActor.class, "car-2", r, 100.0, 0.1, 0.3, 5.0), "car-2");

		this.syncWithTime(25);
	}	
	
}
