package pcd.part1.actor;

import akka.actor.ActorSystem;
import akka.actor.ActorRef;

public class MainActor {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("TrafficSimulation");

        ActorRef environment = system.actorOf(EnvironmentActor.props(), "environment");

        ActorRef car1 = system.actorOf(CarActor.props(environment), "car1");
        ActorRef car2 = system.actorOf(CarActor.props(environment), "car2");

        environment.tell(new EnvironmentActor.RegisterCar(car1), ActorRef.noSender());
        environment.tell(new EnvironmentActor.RegisterCar(car2), ActorRef.noSender());

        environment.tell(new EnvironmentActor.UpdatePosition(car1, 0), ActorRef.noSender());
        environment.tell(new EnvironmentActor.UpdatePosition(car2, 3), ActorRef.noSender());
    }
}
