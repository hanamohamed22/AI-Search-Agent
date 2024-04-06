package code;
public class Node {
	State state; // the state associated with this node
	Node parentNode; // the parent node
	String operator; // the operator applied to achieve this node
	int depth; // the depth of the node
	int pathCost;// this is equivalent to the damage in Iron Man
	int heuristic; // This is the heuristic cost per node
	int heuristictype; // the heuristic type to specify whether this Node belongs to AS1/GS1 or GS2/AS2
	
	public Node(State state, Node parentNode, String operator, int depth, int pathCost, int heuristic, int heuristictype) {
		this.state = state;
		this.parentNode = parentNode;
		this.operator = operator;
		this.depth = depth;
		this.pathCost = pathCost;
		this.heuristic= heuristic;
		this.heuristictype=heuristictype;
	}
}
