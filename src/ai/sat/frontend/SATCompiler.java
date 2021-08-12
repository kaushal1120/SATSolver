package ai.sat.frontend;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ai.sat.maze.Maze;

/**
 * Sat Compiler generates atoms the atoms and clauses for a given problem such that it can be
 * fed to a Sat Solver that uses the DPLL algorithm in order to generate a satisfying valuation for each atom.
 * @author kps9907
 *
 */
public class SATCompiler {
	/**
	 * Stores a mapping of 'HAS' type atoms from its string form to an integer form conducive to run Dpll on.
	 */
	private Map<String, Integer> hasAtomMap = new LinkedHashMap<String, Integer>();

	/**
	 * Stores a mapping of 'AT' type atoms from its string form to an integer form conducive to run Dpll on.
	 */
	private Map<String, Integer> atAtomMap = new LinkedHashMap<String, Integer>();

	/**
	 * Stores the list of generated clauses
	 */
	private List<String> clauses = new ArrayList<String>();

	/**
	 * Maze for which the Sat compiler is executed.
	 */
	private Maze maze;

	public SATCompiler(Maze maze) {
		this.maze = maze;
	}	

	/**
	 * Given the nodes, and treasures for a given maze problem, generates atoms corresponding to the same.
	 * @param nodes nodes in the adventure maze.
	 * @param maxSteps Maximum number of steps that the solution can take in order to collect all treasures.
	 * @param treasureList List of all the treasures that exist on the maze.
	 */
	public void generateAtoms() {
		int noOfAtoms = 1;
		for(int i = 0; i <= maze.getMaxSteps(); i++) {
			for(int j = 0; j < maze.getNodeList().size(); j++) {
				atAtomMap.put(maze.getNodeList().get(j).getName() + " " + String.valueOf(i), noOfAtoms++);
			}
		}
		for(int i = 0; i <= maze.getMaxSteps(); i++) {
			for(int j = 0; j < maze.getTreasureList().size(); j++) {
				hasAtomMap.put(maze.getTreasureList().get(j) + " " + String.valueOf(i), noOfAtoms++);
			}
		}
		/* Prints out the generated atoms.*/
		/*for(String x : atAtomMap.keySet()) {
			System.out.println(x + " : " + atAtomMap.get(x));
		}
		for(String x : hasAtomMap.keySet()) {
			System.out.println(x + " : " + hasAtomMap.get(x));
		}*/
	}

	/**
	 * Generates the set of clauses representing the adventure maze problem.
	 */
	public void generateClauses() {
		PropositionalConstraints propConstraints = new PropositionalConstraints();
		clauses.addAll(propConstraints.getAtOnePlaceAtATimeClauses(atAtomMap, maze.getMaxSteps(), maze.getNodeList()));
		clauses.addAll(propConstraints.getMoveOnEdgesClauses(atAtomMap, maze.getMaxSteps(), maze.getNodeList()));
		clauses.addAll(propConstraints.getHasTreasureAtNodeClauses(atAtomMap, hasAtomMap, maze.getMaxSteps(), maze.getNodeList()));
		clauses.addAll(propConstraints.getOnceGotTreasureHasTreasureClauses(hasAtomMap, maze.getMaxSteps(), maze.getTreasureList()));
		clauses.addAll(propConstraints.getGotTreasureClauses(maze.getTreasureMap(), hasAtomMap, atAtomMap, maze.getMaxSteps(), maze.getTreasureList()));
		clauses.addAll(propConstraints.getStartClause(atAtomMap));
		clauses.addAll(propConstraints.getNoTreasureAtStartClauses(hasAtomMap, maze.getTreasureList()));
		clauses.addAll(propConstraints.getHasAllTreasuresClauses(hasAtomMap, maze.getMaxSteps(), maze.getTreasureList()));
		writeClauses();
	}

	/**
	 * Writes the generated clauses to an output file.
	 */
	public void writeClauses() {
		File outputFile = new File(System.getProperty("user.dir") + "/src/io_files/dpll_input.txt");
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(outputFile));
			//Writes the clauses to the output file.
			for(String s : clauses) {
				out.write(s);
				out.newLine();
			}

			//Writes out a 0 to separate the clauses to be used by dpll from the key used by the backend.
			out.write("0");
			out.newLine();

			/*Writes out a key used to map the integer form of the atoms back to their respective string forms
			 * to be used by the backend for conversion back to the domain solution.
			 */
			for(String s : atAtomMap.keySet()) {
				out.write(atAtomMap.get(s) + " " + s);
				out.newLine();
			}
			/*for(String s : hasAtomMap.keySet()) {
				out.write(hasAtomMap.get(s) + " " + s);
				out.newLine();
			}*/
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
