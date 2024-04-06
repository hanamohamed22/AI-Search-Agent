package code;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public abstract class GenericSearch {
	String[] operators; //an array of string operators which are going to be used in a certain search problem 
	State initialState; // the initial state of the GenericSearch
	Set<String> repeatedStates; //a hash set which carries the repeated states
	Comparator<Node> costComparator = new Comparator<Node>() { // a comparator which adds to the priority queue according to the cheap path cost first
		@Override
		public int compare(Node o1, Node o2) {
			// TODO Auto-generated method stub
			return o1.pathCost - o2.pathCost;
		}
	};
	Comparator<Node> GS1Comparator = new Comparator<Node>() {// a comparator which adds to the priority queue according to the cheap heuristic cost first
		@Override
		public int compare(Node o1, Node o2) {
			// TODO Auto-generated method stub
			return o1.heuristic - o2.heuristic;
		}
	};

	Comparator<Node> AstarComparator = new Comparator<Node>() {// a comparator which adds to the priority queue according to the cheap heuristic and path costs first
		@Override
		public int compare(Node o1, Node o2) {
			// TODO Auto-generated method stub
			return (o1.pathCost + o1.heuristic) - (o2.pathCost + o2.heuristic);
		}
	};

	public GenericSearch(String[] operators, State initialState) {
		this.operators = operators;
		this.initialState = initialState;
		this.repeatedStates = new HashSet<>();

	}
	
	private static void printPathFromRoot(Node goalNode) {
	    ArrayList<String> pathStrings = new ArrayList<>();
	    Node currentNode = goalNode;

	    while (currentNode != null) {
	    	NewState newnode=(NewState)currentNode.state;
	        String pathString = String.format("Depth: %d, Operator: %s, State: %s",
	                currentNode.depth, currentNode.operator, newnode.grid.toString());
	        pathStrings.add(pathString);
	        currentNode = currentNode.parentNode;
	    }

	    // Print the path in the correct order
	    for (int i = pathStrings.size() - 1; i >= 0; i--) {
	        System.out.println(pathStrings.get(i));
	    }
	}

	public String GeneralSearch(GenericSearch sp, String strategy, boolean visualize) {

		boolean isExpanded = false; // a boolean variable to check if a node is expanded or not
		Queue<Node> nodes = new LinkedList<>(); // a queue used for the BFS  
		Stack<Node> stackNodes = new Stack<Node>(); // A stack used by IDS and DFS
		PriorityQueue<Node> pq = new PriorityQueue<Node>(costComparator); // A pq used by UCS
		PriorityQueue<Node> Astarpq = new PriorityQueue<Node>(AstarComparator); //A priority queue used by A star
		PriorityQueue<Node> gpq = new PriorityQueue<Node>(GS1Comparator);//A priority queue used by Greedy
		GenericSearch eg = sp;
		Node newStn; // declare a SearchTreeNode
		if (strategy.equals("GR1") || strategy.equals("AS1")) {
			newStn = new Node(eg.initialState, null, null, 0, 0, 0, 1); //the initial state is the same the GenericSearch's, the parent null since it is the root, operator null since this is the root, depth is 0 and the path cost is 0, the heuristic cost is 0, and the type of the heuristic is 1
		} else if (strategy.equals("GR2") || strategy.equals("AS2")) {
			newStn = new Node(eg.initialState, null, null, 0, 0, 0, 2);//the initial state is the same the GenericSearch's, the parent null since it is the root, operator null since this is the root, depth is 0 and the path cost is 0, the heuristic cost is 0, and the type of the heuristic is 2
		} else {
			newStn = new Node(eg.initialState, null, null, 0, 0, 0, 0);//the initial state is the same the GenericSearch's, the parent null since it is the root, operator null since this is the root, depth is 0 and the path cost is 0, the heuristic cost is 0, and the type of the heuristic is 0
		}
//the heuristic cost is set to every node with a value in the transition, we start by( the number of stones in the transition function*2)+4.
		int nodesNumber = 0;//this will carry the number of expanded nodes
		int test =1;
		boolean flag = true; //set a flag to true
		State endgamestate; //declare endgamestate
		int count=0;
		switch (strategy) { //check the strategy

		case "BF": //if it is breadth-first

			nodes.add(newStn); //add the SearchTreeNode which we have created to the queue
			endgamestate = sp.initialState; // set to the initial state of the search problem
			
			while (flag) { //keep looping
				//System.out.println("-----------------------------------------------------------------------------");
				isExpanded = false; //
				count++;
				//System.out.println("counter: "+count);
				if (nodes.isEmpty()) { //if the queue is empty
					//System.out.println("NOSOLUTION"); //print NOSOLUTION
					return null;//and return null
				} else {

					Node node = nodes.remove(); //remove the first node off the queue
					
					//System.out.println("Depth is "+ node.depth+" Operator is "+node.operator);
					for (int i=0;i<node.depth;i++) {
						//System.out.print();
					//	System.out.println();
					}
					//System.out.println();
					if (GoalTest(node.state)) { //does it pass the goal test?
						String str = PathCost(node).substring(5); //fetch the string which carries the sequence of operators used to reach goal
						//System.out.println("yyy"+PathCost(node).substring(0));
						str = str.substring(0, str.length() - 1);
						//System.out.println("Hhhh"+str.toString());
						if (visualize) { //if the user wants to visualize the grid steps
							//PathCostVisualize(node);//call the PathCostVisualize function to display the sequence of grids from the root to the goal
							//System.out.println("hh");
							printPathFromRoot(node);
						}
						String s = str + ";" + node.pathCost + ";" + nodesNumber; //String that carries the sequence of operators; the path cost; the number of expanded nodes
						flag = false; //break from the while loop
						this.repeatedStates.clear();//clear the repeated states hash set
						return s; //return string s
					} else {
						NewState st1= ((NewState) node.state);
						//System.out.println("node"+node.operator);
						//System.out.println("details"+st1.grid.toString());
						//System.out.println("Parent");
						//if(node.parentNode!=null) {
						//System.out.println(node.parentNode.operator);
						//}
						for (int y = 0; y < sp.operators.length; y++) { //loop on all the operators of a search problem
							//System.out.println(operators[y]);
							Node searchtreenode = sp.transition(sp.operators[y], node); //call the transition function it return a SearchTreeNode.
							

							if (searchtreenode == null) { //if the node returned is null, do nothing
								//System.out.println("Null node returned");
							} else {
								/*System.out.println("Operator Child "+sp.operators[y]);
								System.out.println("Current State is "+st1.grid);
								NewState tempst=((NewState) searchtreenode.state);
								System.out.println("Updated State is "+tempst.grid);
								System.out.println();*/
								//System.out.println("My parent is "+searchtreenode.parentNode.operator);
								
								isExpanded = true; //else this means a node has been expanded so set it to true
								nodes.add(searchtreenode); //add the node created to the nodes queue
							}
							//System.out.println("--------------------------");
						}
						if (isExpanded) //if the node which the transition was applied on  has children
							nodesNumber++; //increment number of nodes
					}
				}
			}
			break;

		case "DF":

			stackNodes.push(newStn); //push the initial SearchTreeNode on top of the stack
			endgamestate = sp.initialState;// set to the initial state of the search problem
			
			while (flag) { //keep looping
				//System.out.println("------------------------------------------------------");
				count++;
				//System.out.println("counter: "+count);
				if (stackNodes.isEmpty()) { //if the stack is empty
					//System.out.println("NOSOLUTION");
					return null; //return null
				} else {
					Node node = stackNodes.pop(); //take the first SearchTreeNode off the queue
					//System.out.println("Depth is "+ node.depth+" Operator is "+node.operator);
					if (GoalTest(node.state)) { //if it passes the goal test
						String str = PathCost(node).substring(5); //fetch the string which carries the sequence of operators used to reach goal
						str = str.substring(0, str.length() - 1);
						if (visualize) {//if the user wants to visualize the grid steps
						//PathCostVisualize(node); //call the PathCostVisualize function to display the sequence of grids from the root to the goal
							//System.out.println("hh");
							printPathFromRoot(node);
						}
						String s = str + ";" + node.pathCost + ";" + nodesNumber;//String that carries the sequence of operators; the path cost; the number of expanded nodes
						flag = false; //break from loop
						this.repeatedStates.clear();//clear the repeated states hash set

						return s;//return string s
					} else {
						NewState st1= ((NewState) node.state);
						for (int y = 0; y < sp.operators.length; y++) { //loop on all the operators of a search problem
							
							Node searchtreenode = sp.transition(sp.operators[y], node); //call the transition function it return a SearchTreeNode.
							if (searchtreenode == null) { //if it is null do nothing
								//System.out.println("Null node returned");
							} else { 
								/*System.out.println("Operator Child "+sp.operators[y]);
								System.out.println("Current State is "+st1.grid);
								NewState tempst=((NewState) searchtreenode.state);
								System.out.println("Updated State is "+tempst.grid);
								System.out.println();*/
								isExpanded = true; //else this means a node has been expanded so set it to true
								stackNodes.push(searchtreenode); //push the child on top of the stack

							}
						}
						if (isExpanded) {//if the node which the transition was applied on  has children
							nodesNumber++; //increment number of expanded nodes
						}

					}
				}
			}
			break;
		case "UC":

			pq.add(newStn); //add the initial SearchTreeNode to the priority queue
			while (flag) {

				isExpanded = false; 
				if (pq.isEmpty()) { //is the priority queue is empty
					//System.out.println("NOSOLUTION");
					return null; //return null
				} else {
					Node node = pq.remove(); //remove the first element in the priority queue
					if (GoalTest(node.state)) { //if it passes the goal test
						String str = PathCost(node).substring(5);//fetch the string which carries the sequence of operators used to reach goal
						str = str.substring(0, str.length() - 1);
						if (visualize) {//if the user wants to visualize the grid steps

							//PathCostVisualize(node); //call PathCostVisualize 
							//System.out.println("hh");
							printPathFromRoot(node);
						}
						String s = str + ";" + node.pathCost + ";" + nodesNumber;//String that carries the sequence of operators; the path cost; the number of expanded nodes
						flag = false; //break from loop
						this.repeatedStates.clear();//clear the repeated states hash set

						return s; //return the string s
					} else {
						for (int y = 0; y < sp.operators.length; y++) {//loop on all the operators of a search problem

							Node searchtreenode = sp.transition(sp.operators[y], node); //call the transition function it return a SearchTreeNode.
							
							if (searchtreenode == null) {//if it is null do nothing

							} else {
								NewState tempst=((NewState) searchtreenode.state);
								//System.out.println("node"+tempst.grid);
								//System.out.println("My parent is "+searchtreenode.parentNode.operator);
								isExpanded = true;//else this means a node has been expanded so set it to true

								pq.add(searchtreenode); //add the child to the priority queue
							}
						}
						if (isExpanded == true) {//if the node which the transition was applied on  has children
							nodesNumber++; //increment number of expanded nodes
						}
					}
				}
			}

			break;
		case "ID"://push the initial SearchTreeNode to the stack
			int maxDepth = 0; //Carries the value of the maximum depth 
			for (int i = 0; i < maxDepth + 1; i++) { //loop from 0 till maxDepth
				this.repeatedStates.clear(); //make sure that nothing is in the repeated states hash set
				stackNodes.push(newStn); //push the intial SearchTreeNode to the queue
				while (flag) {//keep looping
					isExpanded = false;
					if (stackNodes.isEmpty()) {//if the stack is empty
						maxDepth++;//increment the depth, maybe you will find a goal in a deeper place
						break; //break from the while loop and go back to the for loop
					} else { // if the stack is not empty
						Node node = stackNodes.pop(); //pop the first element off the stack
						if (GoalTest(node.state)) { //if it passes the goal test
							String str = PathCost(node).substring(5);//fetch the string which carries the sequence of operators used to reach goal
							str = str.substring(0, str.length() - 1);
							if (visualize) {//if the user wants to visualize the grid steps

								printPathFromRoot(node);
							}
							String s = str + ";" + node.pathCost + ";" + nodesNumber;//String that carries the sequence of operators; the path cost; the number of expanded nodes
							flag = false;//break from loop
							this.repeatedStates.clear();//clear the repeated states hash set
							return s;//return the string s
						} else { //if the goal node is not found
							int childD = node.depth + 1; 
							if (childD <= maxDepth) {//check if the children's depth is less than or equal the maximum depth 
								for (int y = 0; y < sp.operators.length; y++) { // expand only if children at acceptable
																				// level

									Node egs = sp.transition(sp.operators[y], node); //call the transition function it return a SearchTreeNode.
									if (egs == null) { //if the node reterived from the transition is null do nothing

									} else { 
										NewState tempst=((NewState) egs.state);
										//System.out.println("node"+tempst.grid);
										//System.out.println("My parent is "+egs.parentNode.operator);//else 
										stackNodes.push(egs); //push the child node on top of the stack
										isExpanded = true; //this means a node has been expanded so set it to true

									}

								}
								if (isExpanded) {//if the node which the transition was applied on  has children
									nodesNumber++;//increment number of expanded nodes
								}
							}

						}
					}
				}
			}

			break;

		case "GR1":
			gpq.add(newStn); //add the initial SearchTreeNode to the priority queue
			while (flag) { //keep looping
				//System.out.println("-----------------------------------------------------------------------------");
				isExpanded = false;
				if (gpq.isEmpty()) { //if the pq is empty
					//System.out.println("NOSOLUTION");
					return null; //return null
				} else {
					Node node = gpq.remove(); //remove the first element off the pq
					if (GoalTest(node.state)) { //if it passes the goal test
						String str = PathCost(node).substring(5);//fetch the string which carries the sequence of operators used to reach goal
						str = str.substring(0, str.length() - 1);
						if (visualize) {//if the user wants to visualize the grid steps

							printPathFromRoot(node);
						}
						String s = str + ";" + node.pathCost + ";" + nodesNumber; //String that carries the sequence of operators; the path cost; the number of expanded nodes
						flag = false;//break from loop
						this.repeatedStates.clear(); //clear the hash set
						return s; //return the string s

					} else {
						
						for (int y = 0; y < sp.operators.length; y++) {//loop on all the operators of a search problem
							Node searchtreenode = sp.transition(sp.operators[y], node); //call the transition function it return a SearchTreeNode.
							//System.out.println("op"+sp.operators[y]);

							if (searchtreenode == null) { //if the node returned is null, do nothing
								//System.out.println("Null node returned");
							} else {
								NewState tempst=((NewState) searchtreenode.state);
								//System.out.println("new node is "+tempst.grid);
								//System.out.println("The parent is "+searchtreenode.parentNode.operator);
								isExpanded = true;//this means a node has been expanded so set it to true
								gpq.add(searchtreenode); //add the child node to the pq
							}
							//System.out.println("---------------------------");
						}
						if (isExpanded) {//if the node which the transition was applied on  has children
							nodesNumber++;//increment number of expanded nodes
						}
					}
				}
			}

			break;

		case "GR2": //IT HAS THE SAME EXPLANATION AS THE ONE ABOVE IT EXACTLY GR1
			gpq.add(newStn);
			while (flag) {
				isExpanded = false;
				if (gpq.isEmpty()) {
					//System.out.println("NOSOLUTION");
					return null;
				} else {
					Node node = gpq.remove();
					if (GoalTest(node.state)) {
						String str = PathCost(node).substring(5);
						str = str.substring(0, str.length() - 1);
						if (visualize) {
							printPathFromRoot(node);
						}
						String s = str + ";" + node.pathCost + ";" + nodesNumber;
						flag = false;
						this.repeatedStates.clear();
						return s;
					} else {
						for (int y = 0; y < sp.operators.length; y++) {

							Node searchtreenode = sp.transition(sp.operators[y], node); 
							if (searchtreenode == null) {

							} else {
								isExpanded = true; 
								gpq.add(searchtreenode);
							}
						}
						if (isExpanded) {
							nodesNumber++;
						}
					}
				}
			}

			break;

		case "AS1":
			Astarpq.add(newStn); //Add the initial SearchTreeNode to the pq 
			while (flag) {
				isExpanded = false;
				if (Astarpq.isEmpty()) { //if the pq is empty
					//System.out.println("NOSOLUTION");
					return null; //return null
				} else {
					Node node = Astarpq.remove(); //remove the first SearchTreeNode off the pq
					if (GoalTest(node.state)) { //if it passes the goal test
						String str = PathCost(node).substring(5);//fetch the string which carries the sequence of operators used to reach goal
						str = str.substring(0, str.length() - 1);
						if (visualize) {//if the user wants to visualize the grid steps
							printPathFromRoot(node);
						}
						String s = str + ";" + node.pathCost + ";" + nodesNumber;//String that carries the sequence of operators; the path cost; the number of expanded nodes
						flag = false; //break from loop
						this.repeatedStates.clear(); //clear the hashset
						return s;//return the String s
					} else {
						for (int y = 0; y < sp.operators.length; y++) {//loop on all the operators of a search problem

							Node searchtreenode = sp.transition(sp.operators[y], node); //call the transition function it return a SearchTreeNode.
							if (searchtreenode == null) { //if the SearchTreeNode is null do nothing

							} else {
								isExpanded = true;//this means a node has been expanded so set it to true
								Astarpq.add(searchtreenode); //add the child node to the pq
							}
						}
						if (isExpanded) {//if the node which the transition was applied on  has children
							nodesNumber++;//increment number of expanded nodes
						}
					}
				}
			}

			break;

		case "AS2"://IT HAS THE SAME EXPLANATION AS THE ONE ABOVE IT EXACTLY AS1
			Astarpq.add(newStn);
			while (flag) {
				isExpanded = false;
				if (Astarpq.isEmpty()) {
					//System.out.println("NOSOLUTION");
					return null;
				} else {
					Node node = Astarpq.remove();
					if (GoalTest(node.state)) {
						String str = PathCost(node).substring(5);
						str = str.substring(0, str.length() - 1);
						if (visualize) {
							printPathFromRoot(node);
						}
						String s = str + ";" + node.pathCost + ";" + nodesNumber;
						flag = false;
						this.repeatedStates.clear();
						return s;
					} else {
						for (int y = 0; y < sp.operators.length; y++) {

							Node searchtreenode = sp.transition(sp.operators[y], node); 
							if (searchtreenode == null) {

							} else {
								isExpanded = true;
								Astarpq.add(searchtreenode);
							}
						}
						if (isExpanded) {
							nodesNumber++;
						}
					}
				}
			}

			break;

		}

		return null;

	}

	public abstract String PathCost(Node node); // and abstract method to return the sequence of operators applied to reach SearchTreeNode node

	public abstract boolean GoalTest(State state); //Is used to test whether State state is the goal state or not

	public abstract Node transition(String op, Node stn); //Apply an operator op on the SearchTreeNode stn to get a new SearchTreeNode
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String grid5 = "50;22,22,22;50,60,70;30,2;19,1;15,1;300,5,7,3,20;500,8,6,3,40;";

		
//		LLAPSearch.solve(grid5,"BF",true);
//		solve(grid6,"BF",true);
//		
//		solve(grid5,"DF",false);
//		solve(grid6,"DF",true);
//		
// 		LLAPSearch.solve(grid5,"DF",false);
//		solve(grid6,"UC",false);
//		
//		solve(grid5,"ID",false);
//		solve(grid6,"ID",true);
//		
//		LLAPSearch.solve(grid5,"GR1",false);
		LLAPSearch.solve(grid5,"GR1",true);
//		
//		solve(grid5,"GR2",false);
//		solve(grid6,"GR2",true);
//		
//		solve(grid5,"AS1",false);
//		solve(grid6,"AS1",false);		
//		solve(grid5,"AS2",false);
//		solve(grid6,"AS2",false);


	}
}
