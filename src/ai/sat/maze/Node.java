package ai.sat.maze;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in the maze.
 * @author kps9907
 *
 */
public class Node {
	/*
	 * Name of the node
	 */
	private String name;

	/**
	 * List of treasures at the node.
	 */
	private List<String> treasureList;

	/**
	 * List of neighbour nodes at step 1 from the node.
	 */
	private List<Node> neighbourNodes;

	public Node(String name) {
		this.name = name;
		this.treasureList = new ArrayList<String>();
		this.neighbourNodes = new ArrayList<Node>();
	}

	/**
	 * Returns the name of the node.
	 * @return String name of the node.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Adds a treasure to the treasure list of the node
	 * @param String treasure that must be added to the treasure list of the node.
	 */
	public void addTreasure(String treasure) {
		treasureList.add(treasure);
	}

	/**
	 * Adds a node to the list of neighbours of the node
	 * @param Node neighbour node that must be added to the list of neighbours of the node.
	 */
	public void addNeighbour(Node neighbour) {
		neighbourNodes.add(neighbour);
	}

	/**
	 * Returns a list of treasures held by the node.
	 * @return List<String> a list of treasures held by the current node.
	 */
	public List<String> getTreasureList() {
		return treasureList;
	}

	/**
	 * Returns a list of nodes that neighbour this node.
	 * @return List<Node> a list of nodes that neighbour this node.
	 */
	public List<Node> getNeighbourNodes() {
		return neighbourNodes;
	}
}
