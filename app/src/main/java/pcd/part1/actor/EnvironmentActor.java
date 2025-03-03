package pcd.part1.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentActor extends AbstractActor {

    private Map<ActorRef, Integer> carPositions = new HashMap<>(); // Auto e loro posizioni

    public static Props props() {
        return Props.create(EnvironmentActor.class, EnvironmentActor::new);
    }

    public static class RegisterCar {
        public final ActorRef car;
        public RegisterCar(ActorRef car) { this.car = car; }
    }

    public static class UpdatePosition {
        public final ActorRef car;
        public final int position;
        public UpdatePosition(ActorRef car, int position) {
            this.car = car;
            this.position = position;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RegisterCar.class, this::onRegisterCar)
                .match(UpdatePosition.class, this::onUpdatePosition)
                .build();
    }

    private void onRegisterCar(RegisterCar msg) {
        carPositions.put(msg.car, 0); // Registra auto alla posizione 0
    }

    private void onUpdatePosition(UpdatePosition msg) {
        carPositions.put(msg.car, msg.position);
        sendPerceptions(); // Dopo aggiornamento, invia nuove percezioni alle auto
    }

    private void sendPerceptions() {
        for (Map.Entry<ActorRef, Integer> entry : carPositions.entrySet()) {
            ActorRef car = entry.getKey();
            int position = entry.getValue();
            int nextCarDistance = findNextCarDistance(position);
            boolean trafficLightRed = checkTrafficLight(position);
            car.tell(new CarActor.Perceive(nextCarDistance, trafficLightRed), getSelf());
        }
    }

    private int findNextCarDistance(int position) {
        return 5; // Da migliorare: simulare posizione auto vicine
    }

    private boolean checkTrafficLight(int position) {
        return false; // Da migliorare: simulare semafori
    }
}
