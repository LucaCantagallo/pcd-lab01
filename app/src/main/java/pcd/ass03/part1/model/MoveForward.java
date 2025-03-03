package pcd.ass03.part1.model;

import pcd.ass03.part1.abstractSim.Action;

/**
 * Car agent move forward action
 */
public record MoveForward(String agentId, double distance) implements Action {}
