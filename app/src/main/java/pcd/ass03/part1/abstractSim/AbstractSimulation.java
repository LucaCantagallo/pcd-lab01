package pcd.ass03.part1.abstractSim;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import pcd.ass03.part1.actor.RoadEnvActor;
import pcd.ass03.part1.actor.SimulationActor;
import pcd.ass03.part1.model.Message;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractSimulation {
    protected ActorSystem system;

    /* logical time step (delta time) */
    private int dt;

    /* initial logical time */
    private int t0;

    protected int numCars;

    protected AbstractSimulation(int numCar){
        system = ActorSystem.create("TrafficSimulation");
        system.actorOf(Props.create(RoadEnvActor.class, "RoadEnv"), "roadenv");
        system.actorOf(Props.create(SimulationActor.class), "sim");
        this.numCars = numCar;
    }

    /**
     *
     * Method used to configure the simulation, specifying env and agents
     *
     */
    protected abstract void setup();

    /**
     * Method running the simulation for a number of steps,
     * using a sequential approach
     *
     * @param numSteps number of steps to run
     */
    public void run(int numSteps) {

        //setta il numero di step nella simulation
        system.actorSelection("/user/sim").tell(new Message("set-start", List.of(numSteps)), ActorRef.noSender()); // set the number of steps

        /* initialize the env and the agents inside */
        int t = t0;

        // inizializza l'ambiente (numero di step e numero di macchine)
        system.actorSelection("/user/roadenv").tell(new Message("init", List.of(numSteps, numCars)), ActorRef.noSender()); // initialize the env

        this.notifyReset(t); // notify the listeners

        /* make a first step */
        system.actorSelection("/user/roadenv").tell(new Message("step", List.of(dt)), ActorRef.noSender()); // make a step
    }

    /**
     * quanto tempo è durata la simulazione
     * @return
     */
    public long getSimulationDuration() {
        long dur;
        // chiedo alla simulation actor quanto tempo è durata la simulazione
        Future<Object> future = Patterns.ask(system.actorSelection("/user/sim"), new Message("get-duration", List.of()), 1000);
        try {
            dur = (long) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
        } catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return dur;
    }

    /**
     * quanto tempo in media è durato un ciclo
     * @return
     */
    public long getAverageTimePerCycle() {
        long avg;
        // chiedo alla simulation actor quanto tempo in media è durato un ciclo
        Future<Object> future = Patterns.ask(system.actorSelection("/user/sim"), new Message("get-average-time-per-cycle", List.of()), 1000);
        try {
            avg = (long) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
        } catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return avg;
    }

    /* methods for configuring the simulation */

    protected void setupTimings(int t0, int dt) {
        this.dt = dt;
        this.t0 = t0;
    }

    protected void syncWithTime(int nCyclesPerSec) {
        system.actorSelection("/user/sim").tell(new Message("sync-with-time", List.of(nCyclesPerSec)), ActorRef.noSender());
    }

    /* methods for listeners */

    public void addSimulationListener(SimulationListener l) {
        system.actorSelection("/user/sim").tell(new Message("add-listener", List.of(l)), ActorRef.noSender());
    }

    private void notifyReset(int t0) {
        // notifica la simulazione che deve resettarsi
        system.actorSelection("/user/sim").tell(new Message("reset", List.of(t0)), ActorRef.noSender());
    }

    public int getNumCars() {
        return numCars;
    }

    public boolean isCompleted(){
        // chiedo alla roadenv se la simulazione è completata
        Future<Object> future = Patterns.ask(system.actorSelection("/user/roadenv"), new Message("is-completed", List.of()), 1000);
        try {
            return (boolean) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
        } catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        system.terminate();
    }

    public void pause() {
        // chiedo alla simulation actor di mettere in pausa la simulazione
        system.actorSelection("/user/sim").tell(new Message("pause", List.of()), ActorRef.noSender());
    }

    public void resume() {
        // chiedo alla simulation actor di riprendere la simulazione
        system.actorSelection("/user/sim").tell(new Message("resume", List.of(dt, system)), ActorRef.noSender());
    }
}
