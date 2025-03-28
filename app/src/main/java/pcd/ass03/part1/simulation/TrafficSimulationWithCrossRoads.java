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

public class TrafficSimulationWithCrossRoads extends AbstractSimulation {

	public TrafficSimulationWithCrossRoads() {
		super(5);
	}
	
	public void setup() {

		this.setupTimings(0, 1);

				
		TrafficLight tl1;
		Future<Object> future = Patterns.ask(system.actorSelection("/user/roadenv"), new Message("create-traffic-light", List.of(new P2d(740,300), TrafficLight.TrafficLightState.GREEN, 75, 25, 100)), 1000);
		try {
			tl1 = (TrafficLight) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
		} catch (TimeoutException | InterruptedException e) {
			throw new RuntimeException(e);
		}

		Road r1;
		future = Patterns.ask(system.actorSelection("/user/roadenv"), new Message("create-road", List.of(new P2d(0, 300), new P2d(1500, 300))), 1000);
		try {
			r1 = (Road) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
		} catch (TimeoutException | InterruptedException e) {
			throw new RuntimeException(e);
		}
		r1.addTrafficLight(tl1, 740);

		system.actorOf(Props.create(CarAgentActor.class, "car-1", r1, 0.0, 0.1, 0.3, 6.0), "car-1");
		System.out.println("creato car-1");

		system.actorOf(Props.create(CarAgentActor.class, "car-2", r1, 100.0, 0.1, 0.3, 5.0), "car-2");
		System.out.println("creato car-2");

		TrafficLight tl2;
		future = Patterns.ask(system.actorSelection("/user/roadenv"), new Message("create-traffic-light", List.of(new P2d(750,290), TrafficLight.TrafficLightState.RED, 75, 25, 100)), 1000);
		try {
			tl2 = (TrafficLight) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
		} catch (TimeoutException | InterruptedException e) {
			throw new RuntimeException(e);
		}

		Road r2;
		future = Patterns.ask(system.actorSelection("/user/roadenv"), new Message("create-road", List.of(new P2d(750,0), new P2d(750,600))), 1000);
		try {
			r2 = (Road) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
		} catch (TimeoutException | InterruptedException e) {
			throw new RuntimeException(e);
		}
		r2.addTrafficLight(tl2, 290);

		system.actorOf(Props.create(CarAgentActor.class, "car-3", r2, 0.0, 0.1, 0.2, 5.0), "car-3");
		System.out.println("creato car-3");

		system.actorOf(Props.create(CarAgentActor.class, "car-4", r2, 100.0, 0.1, 0.1, 4.0), "car-4");
		System.out.println("creato car-4");

		system.actorOf(Props.create(CarAgentActor.class, "car-5", r2, 200.0, 0.1, 0.1, 4.0), "car-5");
		System.out.println("creato car-5");
		
		this.syncWithTime(25);
	}	
	
}
