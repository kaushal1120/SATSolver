package ai.sat.dpll;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Fetches the input to be fed to the DPLL Algorithm.
 * @author kps9907
 *
 */
public class SatSolver {
	/**
	 * Stores a list of clauses to be fed to the dpll algorithm.
	 */
	private List<List<Integer>> clauses = new ArrayList<List<Integer>>();

	/**
	 * Stored the key to map the atoms back to their string form to be relayed further to the back end.
	 */
	private List<String> remainder = new ArrayList<String>();

	/**
	 * Reads from file and stores the input clauses.
	 */
	public void getInput() {
		File inputFile = new File(System.getProperty("user.dir") + "/src/io_files/dpll_input.txt");
		Scanner scanner;
		try {
			scanner = new Scanner(inputFile);
			while(scanner.hasNextLine()) {
				String x = scanner.nextLine();
				if(x.equals("0")) {
					remainder.add(x);
					break;
				}
				List<Integer> literals = new ArrayList<Integer>();
				literals.addAll(Arrays.asList(x.split("\\s+")).stream().map(Integer::valueOf).collect(Collectors.toList()));
				clauses.add(literals);
			}
			while(scanner.hasNextLine()) {
				remainder.add(scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Runs the dpll algorithm and writes the result to a file.
	 * @param args
	 */
	public static void main(String[] args) {
		SatSolver satSolver = new SatSolver();
		satSolver.getInput();
		int noOfLiterals = 0;

		//Counts the number of atoms/literals for the dpll algorithm.
		for(int i = 0; i < satSolver.clauses.size(); i++) {
			for(int j = 0; j < satSolver.clauses.get(i).size(); j++) {
				if(Math.abs(Integer.valueOf(satSolver.clauses.get(i).get(j))) > noOfLiterals)
					noOfLiterals = Math.abs(Integer.valueOf(satSolver.clauses.get(i).get(j)));
			}
		}

		DPLL dpllSatSolver = new DPLL(noOfLiterals);
		Map<Integer, Boolean> bindings = new HashMap<Integer, Boolean>();
		try {
			File outputFile = new File(System.getProperty("user.dir") + "/src/io_files/dpll_output.txt");
			BufferedWriter out;
			out = new BufferedWriter(new FileWriter(outputFile));
			if(dpllSatSolver.solve(satSolver.clauses, bindings)) {
				//Adds a true binding to all the literals to which an assignment isn't needed.
				for(int i = 1; i <= noOfLiterals; i++) {
					if(bindings.get(i)==null)
						bindings.put(i,true);
				}
				/**
				 * Output the bindings obtained from a satisfying valuation of the dpll algorithm to file.
				 */				
				for(Integer x : bindings.keySet()) {
					out.write(String.valueOf(x) + " " + (bindings.get(x) ? "T" : "F"));
					out.newLine();
				}
			}

			//Output the key to map the valuation back to its domain solution used by the backend.
			for(String x : satSolver.remainder) {
				out.write(x);
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
