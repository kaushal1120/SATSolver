package ai.sat.frontend;
import ai.sat.maze.Maze;

/**
 * Front End to convert an adventure maze problem into a satisfiability problem.
 * @author kps9907
 *
 */
public class FrontEnd {
	/**
	 * Creates a new adventure maze object and feeds it into a SAT Compiler to generate clauses.
	 * @param args
	 */
	public static void main(String[] args) {
		Maze maze = new Maze();
		maze.getInput();
		//maze.print();
		SATCompiler satCompiler = new SATCompiler(maze);
		satCompiler.generateAtoms();
		satCompiler.generateClauses();
	}
}
