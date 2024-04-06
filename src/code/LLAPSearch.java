package code;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class LLAPSearch extends GenericSearch {
	public static int unitPriceFood;
	public static int unitPriceEnergy;
	public static int unitPriceMaterials;
	public static int amountRequestFood;
	public static int delayRequestFood;
	public static int amountRequestMaterials;
	public static int delayRequestMaterials;
	public static int amountRequestEnergy;
	public static int delayRequestEnergy;
	public static int priceBUILD1;
    public static int foodUseBUILD1;
    public static int materialsUseBUILD1;
    public static int energyUseBUILD1;
    public static int prosperityBUILD1;
    public static int priceBUILD2;
    public static int foodUseBUILD2;
    public static int materialsUseBUILD2;
    public static int energyUseBUILD2;
    public static int prosperityBUILD2;
	
	public LLAPSearch(String[] operators, State initialState) {
		super(operators, initialState);
		// TODO Auto-generated constructor stub
	}

	@Override

	public String PathCost(Node node) {
		// TODO Auto-generated method stub
		if (node == null) { // if the node is null return an empty string
			return "";
		} else {
			Node pn = node.parentNode; // get the parent node
			return PathCost(pn) + node.operator + ","; // return the parent operator + the current operator+ a comma

		}

	}

	public static String solve(String grid, String strategy, boolean visualize) {
		int budget = 100000;
		String[] getInputs = splitGrid(grid); // Take the string input and split upon semicolon.

		int Prosperity = Integer.parseInt(getInputs[0]);
		int Food = Integer.parseInt(splitComma(getInputs[1])[0]);
		int Materials = Integer.parseInt(splitComma(getInputs[1])[1]);
		int Energy = Integer.parseInt(splitComma(getInputs[1])[2]);

		unitPriceFood = Integer.parseInt(splitComma(getInputs[2])[0]);
		unitPriceMaterials = Integer.parseInt(splitComma(getInputs[2])[1]);
		unitPriceEnergy = Integer.parseInt(splitComma(getInputs[2])[2]);

		amountRequestFood=Integer.parseInt(splitComma(getInputs[3])[0]);
		delayRequestFood=Integer.parseInt(splitComma(getInputs[3])[1]);
		amountRequestMaterials= Integer.parseInt(splitComma(getInputs[4])[0]);
		delayRequestMaterials= Integer.parseInt(splitComma(getInputs[4])[1]);
		amountRequestEnergy= Integer.parseInt(splitComma(getInputs[5])[0]);
		delayRequestEnergy= Integer.parseInt(splitComma(getInputs[5])[1]);
		
		priceBUILD1=Integer.parseInt(splitComma(getInputs[6])[0]);
	    foodUseBUILD1=Integer.parseInt(splitComma(getInputs[6])[1]);
	    materialsUseBUILD1=Integer.parseInt(splitComma(getInputs[6])[2]);
	    energyUseBUILD1=Integer.parseInt(splitComma(getInputs[6])[3]);
	    prosperityBUILD1=Integer.parseInt(splitComma(getInputs[6])[4]);
		
	    priceBUILD2=Integer.parseInt(splitComma(getInputs[7])[0]);
	    foodUseBUILD2=Integer.parseInt(splitComma(getInputs[7])[1]);
	    materialsUseBUILD2=Integer.parseInt(splitComma(getInputs[7])[2]);
	    energyUseBUILD2=Integer.parseInt(splitComma(getInputs[7])[3]);
	    prosperityBUILD2=Integer.parseInt(splitComma(getInputs[7])[4]);
			
		String Requested = null;
		int currentDelay = 0;
		// snap collect lef r d u kill
		String[] operators = {"BUILD1", "BUILD2", "WAIT", "RequestFood", "RequestEnergy", "RequestMaterials"};

		String state = generateState(Prosperity, Food, Materials, Energy, Requested,
				currentDelay, budget,0);
		NewState newstate = new NewState(state); // the initial state that we are going to start with
		LLAPSearch eg = new LLAPSearch(operators, newstate); // create an instance of end game
		String x = eg.GeneralSearch(eg, strategy, visualize);
		if (x==null) {
			x="NOSOLUTION";
		}
		return x;
	}

	

	public static String generateState(int Prosperity, int Food, int Materials, int Energy, String Requested, int currentDelay, int budget, int pathcost) {
		String temp = Prosperity + ";" + Food + "," + Materials + "," + Energy + ";" +  Requested + "," + currentDelay + ";" + budget + ";" +pathcost +";";
		//System.out.println(temp.toString());
		return temp;
	}

	@Override
	public boolean GoalTest(State state) {
		// TODO Auto-generated method stub
		NewState egs = ((NewState) state);
		String[] getInputs = splitGrid(egs.grid);
		int Prosperity = Integer.parseInt(getInputs[0]);
		if (Prosperity >= 100) {
			//System.out.println("GOAL");
			return true;
		} else
			return false;
	}

	public static String[] splitGrid(String str) {
		String[] stringArray = str.split(";"); // split the string upon semicolons and returning it in an array of strings
		return stringArray;
	}

	public static String[] splitComma(String str) { // A function used to split a string upon finding a comma and returning it in an array of Strings
		String[] stringArray = str.split(",");
		return stringArray;
	}


	public int HeuristicCalculation(int Prosperity, int heuristicType, Node stn) {
		// Heuristic Functions
		//System.out.println("Prosperity "+Prosperity);
		float hcostF = 0;
		if (stn.heuristictype == 1) {
			float NoOfBuild1ToRequest =  (float)(100 - Prosperity) / prosperityBUILD1;
			//System.out.println("no1 "+NoOfBuild1ToRequest);
			float NoOfBuild2ToRequest =  (float)(100 - Prosperity) / prosperityBUILD2;
			//System.out.println("no2 "+NoOfBuild2ToRequest+" pros "+Prosperity+" "+prosperityBUILD2);
			if (NoOfBuild1ToRequest < NoOfBuild2ToRequest) {
				hcostF = foodUseBUILD1 * NoOfBuild1ToRequest * unitPriceFood; // ask f hwar eldecimals f no of requests
				//System.out.println("no2 is bigger: "+hcostF);
			} else {
				hcostF = foodUseBUILD2 * NoOfBuild2ToRequest * unitPriceFood;
				//System.out.println("no1 is bigger: "+hcostF);
			}

		} else if (stn.heuristictype == 2) {
			float NoOfBuild1ToRequest = (float)(100 - Prosperity) / prosperityBUILD1;
			//System.out.println("no1 "+NoOfBuild1ToRequest);
			float NoOfBuild2ToRequest = (float)(100 - Prosperity) / prosperityBUILD2;
			//System.out.println("no2 "+NoOfBuild2ToRequest);
			if (NoOfBuild1ToRequest < NoOfBuild2ToRequest) {
				hcostF = energyUseBUILD1 * NoOfBuild1ToRequest * unitPriceEnergy;
				//System.out.println("no2 is bigger: "+hcostF);
			} else {
				hcostF = energyUseBUILD2 * NoOfBuild2ToRequest * unitPriceEnergy;
				//System.out.println("no1 is bigger: "+hcostF);
			}
		}
		if(hcostF<0 || Prosperity>=100) {
			hcostF=0;
		}
		NewState n=(NewState)stn.state;
		/*System.out.println(n.grid.toString());
		System.out.println(stn.operator);
		System.out.println("hcost: "+hcostF);
		System.out.println("------------------------------------------------------------------");*/
		return (int)hcostF;
	}

	@Override
	public Node transition(String op, Node stn) {
		NewState st = ((NewState) stn.state); // Take the string input and split upon semicolon.
		String[] getInputs = splitGrid(st.grid); // Take the string input and split upon semicolon.
		int Prosperity = Integer.parseInt(getInputs[0]);
		//System.out.println("pp"+Prosperity);
		int Food = Integer.parseInt(splitComma(getInputs[1])[0]);
		int Materials = Integer.parseInt(splitComma(getInputs[1])[1]);
		int Energy = Integer.parseInt(splitComma(getInputs[1])[2]);
		String Requested = splitComma(getInputs[2])[0];
		int currentDelay = Integer.parseInt(splitComma(getInputs[2])[1]);
		int budget = Integer.parseInt(getInputs[3]);
		float hcostF = 0;

		// Requests
		if (Requested.equals("Food") && stn.operator.equals("RequestFood")) {
			currentDelay--;
			if (currentDelay == 0) {
				Food += amountRequestFood;
				if (Food > 50) {
					Food = 50;
				}
				Requested = null;
			}
		}
		// ask law heya 50 a3ml eh w law hya 49 a3ml eh w do it lel ba2y??
		else if (Requested.equals("Energy") && stn.operator.equals("RequestEnergy")) {
			currentDelay--;
			if (currentDelay == 0) {
				Energy += amountRequestEnergy;
				if (Energy > 50) {
					Energy = 50;
				}
				Requested = null;
			}
		} else if (Requested.equals("Materials") && stn.operator.equals("RequestMaterials")) {
			currentDelay--;
			if (currentDelay == 0) {
				Materials += amountRequestMaterials;
				if (Materials > 50) {
					Materials = 50;
				}
				Requested = null;
			}
		}
		int hcost = (int) hcostF;	
		int price=0;
		switch (op) {
		case "RequestFood":
			price = unitPriceFood + unitPriceMaterials + unitPriceEnergy;
			if (Food > 1 && Energy > 1 && Materials > 1 && budget > price  &&  currentDelay == 0 && Food < 50) {
					//System.out.println("now I am requesting food");
					budget = budget - price;
					Food--;
					Energy--;
					Materials--;
					currentDelay = delayRequestFood;
					Requested = "Food";
					int newPathcost=stn.pathCost+price;	
					String newStString = generateState(Prosperity, Food, Materials, Energy, Requested, currentDelay, budget,newPathcost);
					if (this.repeatedStates.contains(newStString)) { // if the state is repeated
						return null; // return null
					} else { // otherwise add the repeated state to the hashset
						this.repeatedStates.add(newStString);
						int depthOfTheChild = stn.depth + 1; // increment depth by 1
						NewState newSt = new NewState(newStString);
						hcost = (int) HeuristicCalculation(Prosperity, stn.heuristictype, stn);
						return new Node(newSt, stn, "RequestFood", depthOfTheChild, stn.pathCost + price, hcost,
								stn.heuristictype);
					}
				
			}
			return null;
		
		case "RequestEnergy":
			price = unitPriceFood + unitPriceMaterials + unitPriceEnergy;
			if (Food > 1 && Energy > 1 && Materials > 1 && budget > price && currentDelay == 0 && Energy < 50) {
					budget = budget - price;
					Food--;
					Energy--;
					Materials--;
					currentDelay = delayRequestEnergy;
					Requested = "Energy";
					//System.out.println("Now I am requesting Energy");
					int newPathcost=stn.pathCost+price;
					String newStString = generateState(Prosperity, Food, Materials, Energy, Requested, currentDelay, budget,newPathcost);
					if (this.repeatedStates.contains(newStString)) { // if the state is repeated
						return null; 
					} else { // otherwise add the repeated state to the hashset
						this.repeatedStates.add(newStString);
						int depthOfTheChild = stn.depth + 1; // increment depth by 1
						NewState newSt = new NewState(newStString);
						hcost = (int) HeuristicCalculation(Prosperity, stn.heuristictype, stn);
						return new Node(newSt, stn, "RequestEnergy", depthOfTheChild, stn.pathCost + price, hcost,
								stn.heuristictype);
					}
				}
			
			return null; 
		case "RequestMaterials":
			price = unitPriceFood + unitPriceMaterials + unitPriceEnergy;
			if (Food > 1 && Energy > 1 && Materials > 1 && budget > price && currentDelay == 0 && Materials < 50 ) {
					budget = budget - price;
					Food--;
					Energy--;
					Materials--;
					currentDelay = delayRequestMaterials;
					Requested = "Materials";
					//System.out.println("now I requested materials");
					int newPathcost=stn.pathCost+price;
					String newStString = generateState(Prosperity, Food, Materials, Energy, Requested, currentDelay, budget,newPathcost);
					if (this.repeatedStates.contains(newStString)) { // if the state is repeated
						//System.out.println("This state is repeated");
						return null;
					} else { // otherwise add the repeated state to the hashset
						this.repeatedStates.add(newStString);
						int depthOfTheChild = stn.depth + 1; // increment depth by 1
						NewState newSt = new NewState(newStString);
						hcost = (int) HeuristicCalculation(Prosperity, stn.heuristictype, stn);
						return new Node(newSt, stn, "RequestMaterials", depthOfTheChild, stn.pathCost + price, hcost,
								stn.heuristictype);
					}
				}
				return null;
		case "WAIT": // ask how to act if there was no delivery fixx
			 price = unitPriceFood + unitPriceMaterials + unitPriceEnergy;
			if (currentDelay>0 && Food > 1 && Energy > 1 && Materials > 1 && budget > price) {
					budget = budget - price;
					Food--;
					Energy--;
					Materials--;
					currentDelay--;
					if (Requested.equals("Food") && stn.operator.equals("RequestFood")) {
						if (currentDelay == 0) {
							Food += amountRequestFood;
							if (Food > 50) {
								Food = 50;
							}
							Requested = null;
						}
					}
					// ask law heya 50 a3ml eh w law hya 49 a3ml eh w do it lel ba2y??
					else if (Requested.equals("Energy") && stn.operator.equals("RequestEnergy")) {
						
						if (currentDelay == 0) {
							Energy += amountRequestEnergy;
							if (Energy > 50) {
								Energy = 50;
							}
							Requested = null;
						}
					} else if (Requested.equals("Materials") && stn.operator.equals("RequestMaterials")) {
						
						if (currentDelay == 0) {
							Materials += amountRequestMaterials;
							if (Materials > 50) {
								Materials = 50;
							}
							Requested = null;
						}
					}
					////System.out.println("I am waiting");
					int newPathcost=stn.pathCost+price;
					String newStString = generateState(Prosperity, Food, Materials, Energy,Requested, currentDelay, budget,newPathcost);
					if (this.repeatedStates.contains(newStString)) { // if the state is repeated
						return null;
					} else { // otherwise add the repeated state to the hashset
						this.repeatedStates.add(newStString);
						int depthOfTheChild = stn.depth + 1; // increment depth by 1
						NewState newSt = new NewState(newStString);
						hcost = (int) HeuristicCalculation(Prosperity, stn.heuristictype, stn);
						return new Node(newSt, stn, "WAIT", depthOfTheChild, stn.pathCost + price, hcost,
								stn.heuristictype);
					}
				}
			
			return null; 
		case "BUILD1": 
			 price = priceBUILD1 + (materialsUseBUILD1 * unitPriceMaterials) + (foodUseBUILD1 * unitPriceFood)
					+ (energyUseBUILD1 * unitPriceEnergy);
			if (budget > price && Food > foodUseBUILD1 && Materials > materialsUseBUILD1 && Energy > energyUseBUILD1) {
				budget -= price;
				Food -= foodUseBUILD1;
				Materials -= materialsUseBUILD1;
				Energy -= energyUseBUILD1;
				Prosperity += prosperityBUILD1;
				int newPathcost=stn.pathCost+price;
				String newStString = generateState(Prosperity, Food, Materials, Energy, Requested, currentDelay, budget,newPathcost);
				if (this.repeatedStates.contains(newStString)) { 
					//if the state is repeats
					//System.out.println("KeyCheck is "+ keycheck );
					return null;
				} else { // otherwise add the repeated state to the hashset
					this.repeatedStates.add(newStString);
					int depthOfTheChild = stn.depth + 1; // increment depth by 1
					NewState newSt = new NewState(newStString);
					hcost = (int) HeuristicCalculation(Prosperity, stn.heuristictype, stn);
					//System.out.println("NewState is " +newSt.grid);
					return new Node(newSt, stn, "BUILD1", depthOfTheChild, stn.pathCost + price, hcost,
							stn.heuristictype);
				}
			}
		
			return null;
		case "BUILD2": 
			 price = priceBUILD2 + (materialsUseBUILD2 * unitPriceMaterials) + (foodUseBUILD2 * unitPriceFood)
					+ (energyUseBUILD2 * unitPriceEnergy);
			if (budget > price && Food > foodUseBUILD2 && Materials > materialsUseBUILD2
					&& Energy > energyUseBUILD2) {
				budget -= price;
				Food -= foodUseBUILD2;
				Materials -= materialsUseBUILD2;
				Energy -= energyUseBUILD2;
				Prosperity += prosperityBUILD2;
				//System.out.println("Build 2 Activated");
				int newPathcost=stn.pathCost+price;
				String newStString = generateState(Prosperity, Food, Materials, Energy, Requested, currentDelay, budget,newPathcost);
				//System.out.println("Prosperity is " + Prosperity);
				if (this.repeatedStates.contains(newStString)) { // if the state is repeated
					return null;
				} else { // otherwise add the repeated state to the hashset
					this.repeatedStates.add(newStString);
					int depthOfTheChild = stn.depth + 1; // increment depth by 1
					NewState newSt = new NewState(newStString);
					hcost = (int) HeuristicCalculation(Prosperity, stn.heuristictype, stn);
					return new Node(newSt, stn, "BUILD2", depthOfTheChild, stn.pathCost + price, hcost,
							stn.heuristictype);
				}
			}
			return null;
		}
		return null;
	}
}
