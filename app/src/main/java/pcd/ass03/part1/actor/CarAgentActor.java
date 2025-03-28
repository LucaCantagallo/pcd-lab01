package pcd.ass03.part1.actor;


import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.pattern.Patterns;
import pcd.ass03.part1.abstractSim.Action;
import pcd.ass03.part1.model.*;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CarAgentActor extends AbstractActor {

    private final String myId;

    protected double maxSpeed;
    protected double currentSpeed;
    protected double acceleration;
    protected double deceleration;
    protected CarPercept currentPercepts;
    private Action selectedAction;

    private static final int CAR_NEAR_DIST = 15;
    private static final int CAR_FAR_ENOUGH_DIST = 20;
    private static final int MAX_WAITING_TIME = 2;
    private static final int SEM_NEAR_DIST = 100;

    private enum CarAgentState { STOPPED, ACCELERATING,
        DECELERATING_BECAUSE_OF_A_CAR,
        DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM,
        WAITING_FOR_GREEN_SEM,
        WAIT_A_BIT, MOVING_CONSTANT_SPEED}

    private CarAgentState state;

    private int waitingTime;

    public CarAgentActor(String id, Road road, double initialPos, double acc, double dec, double vMax) {
        super();
        this.myId = id;
        this.acceleration = acc;
        this.deceleration = dec;
        this.maxSpeed = vMax;
        getContext().actorSelection("/user/roadenv").tell(new Message("register-car", List.of(id, this, road, initialPos)), ActorRef.noSender()); //registriamo la macchina alla road env
        state = CarAgentState.STOPPED;
    }

    private String getId() {
        return myId;
    }

    private void step(int dt) {

        Future<Object> future = Patterns.ask(getContext().actorSelection("/user/roadenv"), new Message("get-current-percepts", List.of(getId())), 1000); // chiedo informazioni alla road env su cosa mi circonda
        try {
            currentPercepts = (CarPercept) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
        } catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        /* decide */
        //selectedAction = Optional.empty();

        decide(dt);

        /* act */
        getContext().actorSelection("/user/roadenv").tell(new Message("submit-action", List.of(selectedAction)), ActorRef.noSender());
    }

    private double getCurrentSpeed() {
        return currentSpeed;
    }

    public void decide(int dt) {
        switch (state) {
            case STOPPED -> {
                if (!detectedNearCar()) {
                    state = CarAgentState.ACCELERATING;
                }
            }
            case ACCELERATING -> {
                if (detectedNearCar()) {
                    state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR;
                } else if (detectedRedOrOrangeSemNear()) {
                    state = CarAgentState.DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM;
                } else {
                    this.currentSpeed += acceleration * dt;
                    if (currentSpeed >= maxSpeed) {
                        state = CarAgentState.MOVING_CONSTANT_SPEED;
                    }
                }
            }
            case MOVING_CONSTANT_SPEED -> {
                if (detectedNearCar()) {
                    state = CarAgentState.DECELERATING_BECAUSE_OF_A_CAR;
                } else if (detectedRedOrOrangeSemNear()) {
                    state = CarAgentState.DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM;
                }
            }
            case DECELERATING_BECAUSE_OF_A_CAR -> {
                this.currentSpeed -= deceleration * dt;
                if (this.currentSpeed <= 0) {
                    state = CarAgentState.STOPPED;
                } else if (this.carFarEnough()) {
                    state = CarAgentState.WAIT_A_BIT;
                    waitingTime = 0;
                }
            }
            case DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM -> {
                this.currentSpeed -= deceleration * dt;
                if (this.currentSpeed <= 0) {
                    state = CarAgentState.WAITING_FOR_GREEN_SEM;
                } else if (!detectedRedOrOrangeSemNear()) {
                    state = CarAgentState.ACCELERATING;
                }
            }
            case WAIT_A_BIT -> {
                waitingTime += dt;
                if (waitingTime > MAX_WAITING_TIME) {
                    state = CarAgentState.ACCELERATING;
                }
            }
            case WAITING_FOR_GREEN_SEM -> {
                if (detectedGreenSem()) {
                    state = CarAgentState.ACCELERATING;
                }
            }
        }

        if (currentSpeed > 0) {
            selectedAction = new MoveForward(getId(), currentSpeed * dt);
        } else {
            selectedAction = new MoveForward(getId(), 0);
        }
    }

    private boolean detectedNearCar() {
        Optional<CarAgentInfo> car = currentPercepts.nearestCarInFront();
        if (car.isEmpty()) {
            return false;
        } else {
            double dist = car.get().getPos() - currentPercepts.roadPos();
            return dist < CAR_NEAR_DIST;
        }
    }

    private boolean detectedRedOrOrangeSemNear() {
        Optional<TrafficLightInfo> sem = currentPercepts.nearestSem();
        if (sem.isEmpty() || sem.get().sem().isGreen()) {
            return false;
        } else {
            double dist = sem.get().roadPos() - currentPercepts.roadPos();
            return dist > 0 && dist < SEM_NEAR_DIST;
        }
    }

    private boolean detectedGreenSem() {
        Optional<TrafficLightInfo> sem = currentPercepts.nearestSem();
        return (sem.isPresent() && sem.get().sem().isGreen());
    }

    private boolean carFarEnough() {
        Optional<CarAgentInfo> car = currentPercepts.nearestCarInFront();
        if (car.isEmpty()) {
            return true;
        } else {
            double dist = car.get().getPos() - currentPercepts.roadPos();
            return dist > CAR_FAR_ENOUGH_DIST;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, message -> "step".equals(message.name()), message -> step((Integer) message.contents().get(0)))
                .match(Message.class, message -> "get-current-speed".equals(message.name()), message -> getSender().tell(getCurrentSpeed(), getSelf()))
                .build();
    }
}
