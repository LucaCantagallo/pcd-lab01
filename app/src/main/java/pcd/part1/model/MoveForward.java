package pcd.part1.model;

import pcd.part1.abstractSim.Action;

/**
 * Car agent move forward action
 */
public record MoveForward(String agentId, double distance) implements Action {}
