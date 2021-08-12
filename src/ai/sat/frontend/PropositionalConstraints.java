package ai.sat.frontend;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ai.sat.maze.Node;

/**
 * Defines and generates a set of propositional clauses for the adventure maze problem.
 * @author kps9907
 *
 */
public class PropositionalConstraints {
	/**
	 * Generates the propositional clauses for the constraint that the player can only be at one place at a time.
	 * @param atAtomMap
	 * @param maxSteps
	 * @param nodeList
	 * @return List<String> list of clauses generated.
	 */
	public List<String> getAtOnePlaceAtATimeClauses(Map<String, Integer> atAtomMap, int maxSteps, List<Node> nodeList) {
		List<String> atOnePlaceAtAtTimeClauses = new ArrayList<String>();
		for(int i = 0; i <= maxSteps; i++) {
			for(int j = 0; j < nodeList.size() - 1; j++) {
				for(int k = j + 1; k < nodeList.size(); k++) {
					atOnePlaceAtAtTimeClauses.add("-" + String.valueOf(atAtomMap.get(nodeList.get(j).getName() + " " + String.valueOf(i))) + " -" + 
							atAtomMap.get(nodeList.get(k).getName() + " " + String.valueOf(i)));
				}
			}
		}
		return atOnePlaceAtAtTimeClauses;
	}

	/**
	 * Generates the propositional clauses for the constraint that the player can only move along edges.
	 * @param atAtomMap 
	 * @param nodeList
	 * @param maxSteps
	 * @return List<String> list of clauses generated.
	 */
	public List<String> getMoveOnEdgesClauses(Map<String, Integer> atAtomMap, int maxSteps, List<Node> nodeList) {
		List<String> moveOnEdgesClauses = new ArrayList<String>();
		for(int i = 0; i < nodeList.size(); i++) {
			for(int j = 0; j < maxSteps; j++) {
				String neighbourAtoms = "";
				List<Node> neighbours = nodeList.get(i).getNeighbourNodes();
				for(int k = 0; k < neighbours.size(); k++) {
					neighbourAtoms = neighbourAtoms.concat(" ").concat(String.valueOf(atAtomMap.get(neighbours.get(k).getName() + " " + String.valueOf(j+1))));
				}
				moveOnEdgesClauses.add("-" + String.valueOf(atAtomMap.get(nodeList.get(i).getName() + " " + String.valueOf(j))) + 
						neighbourAtoms);
			}
		}
		return moveOnEdgesClauses;
	}

	/**
	 * Generates the propositional clauses for the constraint that if a player is at node N at time I and treasure T is located at N
	 * then player has treasure T at time I.
	 * @param nodeList
	 * @param atAtomMap
	 * @param hasAtomMap
	 * @param maxSteps
	 * @return List<String> list of clauses generated.
	 */
	public List<String> getHasTreasureAtNodeClauses(Map<String, Integer> atAtomMap, Map<String, Integer> hasAtomMap, int maxSteps, List<Node> nodeList){
		List<String> hasTreasureAtNodeClauses = new ArrayList<String>();
		for(int i = 0; i < nodeList.size(); i++) {
			for(int j = 0; j <= maxSteps; j++) {
				List<String> treasureList = nodeList.get(i).getTreasureList();
				for(int k = 0; k < treasureList.size(); k++) {
					hasTreasureAtNodeClauses.add("-" + String.valueOf(atAtomMap.get(nodeList.get(i).getName() + " " + String.valueOf(j))) + 
							" " + String.valueOf(hasAtomMap.get(treasureList.get(k) + " " + String.valueOf(j))));
				}
			}
		}
		return hasTreasureAtNodeClauses;
	}

	/**
	 * Generates the propositional clauses for the constraint that if a player has treasure T at time I-1, then he has treasure T at time I...K.
	 * @param hasAtomMap
	 * @param maxSteps
	 * @param treasureList
	 * @return List<String> list of clauses generated.
	 */
	public List<String> getOnceGotTreasureHasTreasureClauses(Map<String, Integer> hasAtomMap, int maxSteps, List<String> treasureList){
		List<String> onceGotTreasureHasTreasureClauses = new ArrayList<String>();
		for(int i = 0; i < maxSteps; i++) {
			for(int j = 0; j < treasureList.size(); j++) {
				onceGotTreasureHasTreasureClauses.add("-" + hasAtomMap.get(treasureList.get(j) + " " + String.valueOf(i)) + " " + 
						hasAtomMap.get(treasureList.get(j) + " " + String.valueOf(i + 1)));
			}
		}
		return onceGotTreasureHasTreasureClauses;
	}

	/**
	 * Generates the propositional clauses for the constraint that if a player does not have the treasure T at time I-1 and has it at time I,
	 * then he is at one of the nodes that has treasure T at time I.
	 * @param treasureMap
	 * @param hasAtomMap
	 * @param atAtomMap
	 * @param maxSteps
	 * @param treasureList
	 * @return List<String> list of clauses generated.
	 */
	public List<String> getGotTreasureClauses(Map<String, List<Node>> treasureMap, Map<String, Integer> hasAtomMap, Map<String, Integer> atAtomMap, int maxSteps, List<String> treasureList){
		List<String> gotTreasureClauses = new ArrayList<String>();
		
		for(int i = 0; i < maxSteps; i++) {
			for(int j =0; j < treasureList.size(); j++) {
				String treasureNodeAtoms = "";
				for(int k = 0; k < treasureMap.get(treasureList.get(j)).size(); k++) {
					treasureNodeAtoms = treasureNodeAtoms.concat(" ")
							.concat(String.valueOf(atAtomMap.get(treasureMap.get(treasureList.get(j)).get(k).getName() + " " + String.valueOf(i+1))));
				}
				gotTreasureClauses.add(hasAtomMap.get(treasureList.get(j) + " " + String.valueOf(i)) + 
						" -" + hasAtomMap.get(treasureList.get(j) + " " + String.valueOf(i+1)) + treasureNodeAtoms);
			}
		}
		return gotTreasureClauses;
	}

	/**
	 * Generates the propositional clauses for the constraint that a player is at START node at time 0.
	 * @param atAtomMap
	 * @return List<String> list of clauses generated.
	 */
	public List<String> getStartClause(Map<String, Integer> atAtomMap){
		List<String> startClause = new ArrayList<String>();
		startClause.add(String.valueOf(atAtomMap.get("START 0")));
		return startClause;
	}

	/**
	 * Generates the propositional clauses for the constraint that a player has no treasures at time 0.
	 * @param atAtomMap
	 * @param treasureList
	 * @return List<String> list of clauses generated.
	 */
	public List<String> getNoTreasureAtStartClauses(Map<String, Integer> hasAtomMap, List<String> treasureList){
		List<String> noTreasureAtStartClauses = new ArrayList<String>();
		for(int i = 0; i < treasureList.size(); i++) {
			noTreasureAtStartClauses.add("-" + String.valueOf(hasAtomMap.get(treasureList.get(i) + " 0")));
		}		
		return noTreasureAtStartClauses;
	}

	/**
	 * Generates the propositional clauses for the constraint that a player has all the treasures at time K.
	 * @param hasAtomMap
	 * @param treasureList
	 * @param maxSteps
	 * @return List<String> list of clauses generated.
	 */
	public List<String> getHasAllTreasuresClauses(Map<String, Integer> hasAtomMap, int maxSteps, List<String> treasureList){
		List<String> hasAllTreasureClauses = new ArrayList<String>();
		for(int i = 0; i < treasureList.size(); i++) {
			hasAllTreasureClauses.add(String.valueOf(hasAtomMap.get(treasureList.get(i) + " " + String.valueOf(maxSteps))));
		}		
		return hasAllTreasureClauses;
	}
}
