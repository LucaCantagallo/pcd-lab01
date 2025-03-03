package pcd.part1.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;

public class CarActor extends AbstractActor {

    private int position;
    private final ActorRef environment;

    // Costruttore
    public CarActor(ActorRef environment) {
        this.position = 0;
        this.environment = environment;
    }

    public static class Perceive {
        public final int distanceToNextCar;
        public final boolean trafficLightRed;

        public Perceive(int distanceToNextCar, boolean trafficLightRed) {
            this.distanceToNextCar = distanceToNextCar;
            this.trafficLightRed = trafficLightRed;
        }
    }

    public static class MoveForward {}

    public static class Stop {}

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Perceive.class, this::onPerceive) // Gestisce le percezioni
                .match(MoveForward.class, this::onMoveForward) // Muove l’auto
                .build();
    }

    // Quando l'auto riceve informazioni sull'ambiente
    private void onPerceive(Perceive perception) {
        if (perception.trafficLightRed || perception.distanceToNextCar < 2) {
            getSender().tell(new Stop(), getSelf()); // Ferma l'auto
        } else {
            getSelf().tell(new MoveForward(), getSelf()); // Decide di avanzare
        }
    }

    // Quando l'auto decide di avanzare
    private void onMoveForward(MoveForward msg) {
        position += 1; // Simula il movimento
        environment.tell(new EnvironmentActor.UpdatePosition(getSelf(), position), getSelf()); // Notifica l’ambiente
    }

    // Metodo per creare l’attore
    public static Props props(ActorRef environment) {
        return Props.create(CarActor.class, () -> new CarActor(environment));
    }
}
