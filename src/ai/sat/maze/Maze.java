package ai.sat.maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Representing an adventure maze problem
 * @author kps9907
 *
 */
public class Maze {
	/**
	 * List of nodes in the maze.
	 */
	private List<Node> nodeList = new ArrayList<Node>();

	/**
	 * Mapping on the name of a node to its node. Required to add neighbours to the node when read from input.
	 */
	private Map<String, Node> nodeMap = new LinkedHashMap<String, Node>();

	/**
	 * Exhaustive list of treasures in the maze.
	 */
	private List<String> treasureList;

	/**
	 * Mapping on a treasure to the nodes at which its present.
	 */
	private Map<String, List<Node>> treasureMap = new LinkedHashMap<String, List<Node>>();

	/**
	 * Maximum number of steps within which the maze problem must be solved.
	 */
	private int maxSteps;

	/**
	 * Reads input for the maze from the input file.
	 */
	public void getInput() {
		File inputFile = new File(System.getProperty("user.dir") + "/src/io_files/maze_input.txt");
		Scanner scanner;
		try {
			scanner = new Scanner(inputFile);
			//Reads all the nodes.
			List<String> nodes = Arrays.asList(scanner.nextLine().split("\\s+"));
			nodes.stream().forEach(n -> {
				Node node = new Node(n);
				nodeMap.put(n, node);
				nodeList.add(node);
			});

			//Reads all the treasures.
			treasureList = Arrays.asList(scanner.nextLine().split("\\s+"));
			for(int i = 0; i < treasureList.size();i++) {
				treasureMap.put(treasureList.get(i), new ArrayList<Node>());
			}

			maxSteps = Integer.parseInt(scanner.nextLine());

			List<String> nodeInfo;
			while(scanner.hasNextLine()) {
				String x = scanner.nextLine();
				nodeInfo = Arrays.asList(x.split("\\s+"));
				Node node = nodeMap.get(nodeInfo.get(0));
				int i;
				//Adds current node to the list of nodes for each treasure where they may be found.
				for(i = 2; !nodeInfo.get(i).equals("NEXT"); i++) {
					treasureMap.get(nodeInfo.get(i)).add(node);
					node.addTreasure(nodeInfo.get(i));
				}
				//Adds a nodes neighbours to its list of neighbours
				for(i = i + 1; i < nodeInfo.size(); i++)
					node.addNeighbour(nodeMap.get(nodeInfo.get(i)));
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * Prints the input read for debugging purposes.
	 */
	public void print() {
		for(String x : nodeMap.keySet())
			System.out.print(x);
		System.out.println();
		for(String x : treasureList)
			System.out.print(x);
		System.out.println();
		System.out.println(maxSteps);
		for(String x : nodeMap.keySet()) {
			System.out.print(x);
			for(String y : nodeMap.get(x).getTreasureList())
				System.out.print(y);
			for(Node z : nodeMap.get(x).getNeighbourNodes())
				System.out.print(z.getName());
			System.out.print("|");
		}
	}

	/**
	 * Returns the max number of steps the the solution can use.
	 * @return int The max number of steps than can be used by the solution
	 */
	public int getMaxSteps() {
		return maxSteps;
	}

	/**
	 * Returns the list of all treasures in the maze.
	 * @return List<String> The list of possible treasures.
	 */
	public List<String> getTreasureList(){
		return treasureList;
	}

	/**
	 * Returns the list of nodes in the maze.
	 * @return List<Node> the list of nodes in the maze.
	 */
	public List<Node> getNodeList(){
		return nodeList;
	}

	/**
	 * Returns a mapping of treasures to the nodes that they may be found at.
	 * @return Map<String, List<Node>> treasureMap a mapping of treasures to the nodes that they may be found at.
	 */
	public Map<String, List<Node>> getTreasureMap(){
		return treasureMap;
	}
}
