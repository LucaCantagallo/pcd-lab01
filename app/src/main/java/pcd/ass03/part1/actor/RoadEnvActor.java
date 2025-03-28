package pcd.ass03.part1.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import pcd.ass03.part1.abstractSim.Action;
import pcd.ass03.part1.abstractSim.Percept;
import pcd.ass03.part1.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class RoadEnvActor extends AbstractActor {

    private final String id;
    protected List<Action> submittedActions;
    private static final int MIN_DIST_ALLOWED = 5;
    private static final int CAR_DETECTION_RANGE = 30;

    private int dt;
    private final List<Road> roads;
    private int nStep;
    private int nCars;
    private int count = 0;

    private final List<TrafficLight> trafficLights;
    private int actualNumStep = 0;

    /* cars situated in the environment */
    private final HashMap<String, CarAgentInfo> registeredCars;
    private boolean isCompleted = false;

    public RoadEnvActor(String id) {
        super();
        this.id = id;
        submittedActions = new ArrayList<>();
        registeredCars = new HashMap<>();
        trafficLights = new ArrayList<>();
        roads = new ArrayList<>();
    }

    private String getId() {
        return id;
    }

    /**
     *
     * Called at the beginning of the simulation
     */
    private void init(int step, int nCars) {
        this.nStep = step;
        this.nCars = nCars;
        for (var tl: trafficLights) {
            tl.init();
        }
    }
    /**
     *
     * Called at each step of the simulation
     *
     * @param dt - time step
     */
    private void step(int dt) {
        this.dt = dt;
        // setto nella simulation actor il tempo corrente
        getContext().actorSelection("/user/sim").tell(new Message("set-current", List.of()), ActorRef.noSender());
        if (this.actualNumStep == this.nStep) {
            //informo la simulation actor che la simulazione è terminata
            getContext().actorSelection("/user/sim").tell(new Message("set-end", null), ActorRef.noSender());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.isCompleted = true;
            return;
        }
        for (var tl: trafficLights) {
            tl.step(dt);
        }
        this.actualNumStep++;

        /* clean actions */
        submittedActions.clear();

        /* ask each agent to make a step */
        getContext().actorSelection("/user/car-*").tell(new Message("step", List.of(dt)), ActorRef.noSender());
    }

    /**
     *
     * Called by an agent to get its percepts
     *
     * @param agentId - identifier of the agent
     * @return agent percepts
     */
    private Percept getCurrentPercepts(String agentId) {
        CarAgentInfo carInfo = registeredCars.get(agentId);
        double pos = carInfo.getPos();
        Road road = carInfo.getRoad();
        Optional<CarAgentInfo> nearestCar = getNearestCarInFront(road,pos);
        Optional<TrafficLightInfo> nearestSem = getNearestSemaphoreInFront(road,pos);
        return new CarPercept(pos, nearestCar, nearestSem);
    }

    /**
     *
     * Chiamato dall'agente per inviare un'azione all'ambiente
     *
     * @param act - the action
     */
    private void submitAction(Action act) {
        submittedActions.add(act);
        count++;
        if (count == nCars) {
            count = 0;
            getSelf().tell(new Message("process-actions", null), ActorRef.noSender());
        }
    }

    /**
     *
     * Chiamato a ogni fase della simulazione per elaborare le azioni
     * inviate dagli agenti.
     *
     */
    private void processActions() {
        for (var act: submittedActions) {
            if (act instanceof MoveForward mv) {
                CarAgentInfo info = registeredCars.get(mv.agentId());
                Road road = info.getRoad();
                Optional<CarAgentInfo> nearestCar = getNearestCarInFront(road, info.getPos());
                if (nearestCar.isPresent()) {
                    double dist = nearestCar.get().getPos() - info.getPos();
                    if (dist > mv.distance() + MIN_DIST_ALLOWED) {
                        info.updatePos(info.getPos() + mv.distance());
                    }
                } else {
                    info.updatePos(info.getPos() + mv.distance());
                }
                if (info.getPos() > road.getLen()) {
                    info.updatePos(0);
                }
            }
        }
        //informo la simulation actor che ho finito di processare le azioni di questo step
        getContext().actorSelection("/user/sim").tell(new Message("new-step", List.of(dt, getContext().system())), ActorRef.noSender());
    }

    private Road createRoad(P2d p0, P2d p1) {
        Road r = new Road(p0, p1);
        this.roads.add(r);
        return r;
    }

    private TrafficLight createTrafficLight(P2d pos, TrafficLight.TrafficLightState initialState, int greenDuration, int yellowDuration, int redDuration) {
        TrafficLight tl = new TrafficLight(pos, initialState, greenDuration, yellowDuration, redDuration);
        this.trafficLights.add(tl);
        return tl;
    }

    // no receive

    /**
     * Restituisce l'auto più vicina davanti all'auto indicata
     * @param road
     * @param carPos
     * @return
     */
    private Optional<CarAgentInfo> getNearestCarInFront(Road road, double carPos){
        return registeredCars.values()
                        .stream()
                        .filter((carInfo) -> carInfo.getRoad() == road)
                        .filter((carInfo) -> {
                            double dist = carInfo.getPos() - carPos;
                            return dist > 0 && dist <= (double) RoadEnvActor.CAR_DETECTION_RANGE;
                        })
                        .min((c1, c2) -> (int) Math.round(c1.getPos() - c2.getPos()));
    }

    // no receive

    /**
     * Restituisce il semaforo più vicino davanti all'auto indicata
     * @param road
     * @param carPos
     * @return
     */
    private Optional<TrafficLightInfo> getNearestSemaphoreInFront(Road road, double carPos){
        return road.getTrafficLights()
                        .stream()
                        .filter((TrafficLightInfo tl) -> tl.roadPos() > carPos)
                        .min((c1, c2) -> (int) Math.round(c1.roadPos() - c2.roadPos()));
    }

    private List<CarAgentInfo> getAgentInfo(){
        return this.registeredCars.values().stream().toList();
    }

    private void registerNewCar(String id, CarAgentActor car, Road road, double pos) {
        registeredCars.put(id, new CarAgentInfo(car, road, pos));
    }

    private List<Road> getRoads(){
        return roads;
    }

    private List<TrafficLight> getTrafficLights(){
        return trafficLights;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, message -> "get-id".equals(message.name()), message -> getSender().tell(getId(), getSelf()))
                .match(Message.class, message -> "init".equals(message.name()), message -> init((Integer) message.contents().get(0), (Integer) message.contents().get(1)))
                .match(Message.class, message -> "step".equals(message.name()), message -> step((Integer) message.contents().get(0)))
                .match(Message.class, message -> "get-current-percepts".equals(message.name()), message -> getSender().tell(getCurrentPercepts((String) message.contents().get(0)), getSelf()))
                .match(Message.class, message -> "submit-action".equals(message.name()), message -> submitAction((Action) message.contents().get(0)))
                .match(Message.class, message -> "process-actions".equals(message.name()), message -> processActions())
                .match(Message.class, message -> "create-road".equals(message.name()), message -> getSender().tell(createRoad((P2d) message.contents().get(0), (P2d) message.contents().get(1)), getSelf()))
                .match(Message.class, message -> "create-traffic-light".equals(message.name()), message -> getSender().tell(createTrafficLight((P2d) message.contents().get(0), (TrafficLight.TrafficLightState) message.contents().get(1), (Integer) message.contents().get(2), (Integer) message.contents().get(3), (Integer) message.contents().get(4)), getSelf()))
                .match(Message.class, message -> "get-agent-info".equals(message.name()), message -> getSender().tell(getAgentInfo(), getSelf()))
                .match(Message.class, message -> "get-roads".equals(message.name()), message -> getSender().tell(getRoads(), getSelf()))
                .match(Message.class, message -> "get-traffic-lights".equals(message.name()), message -> getSender().tell(getTrafficLights(), getSelf()))
                .match(Message.class, message -> "register-car".equals(message.name()), message -> registerNewCar((String) message.contents().get(0), (CarAgentActor) message.contents().get(1), (Road) message.contents().get(2), (Double) message.contents().get(3)))
                .match(Message.class, message -> "is-completed".equals(message.name()), message -> getSender().tell(isCompleted, getSelf()))
                .build();
    }
}
