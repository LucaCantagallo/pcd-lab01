package pcd.ass03.part1.model;

import java.util.ArrayList;
import java.util.List;

public class Road {

	private final double len;
	private final P2d from;
	private final P2d to;
	private final List<TrafficLightInfo> trafficLights;

	public Road(P2d from, P2d to) {
		this.from = from;
		this.to = to;
		this.len = P2d.len(from, to);
		trafficLights = new ArrayList<>();
	}
	
	public double getLen() {
		return len;
	}
	
	public P2d getFrom() {
		return from;
	}
	
	public P2d getTo() {
		return to;
	}
	
	public void addTrafficLight(TrafficLight sem, double pos) {
		trafficLights.add(new TrafficLightInfo(sem, this, pos));
	}
	
	public List<TrafficLightInfo> getTrafficLights(){
		return trafficLights;
	}
}
