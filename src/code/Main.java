/*package code;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Main {
	public static String [] splitGrid(String str){
		String[] stringArray = str.split(";"); 
		return stringArray;
	}
	public static String [] splitComma(String str){ //A function used to split a string upon finding a comma
		String[] stringArray = str.split(","); 
		return stringArray;
	}
	public static String generateState(int Prosperity,int Food,int Materials, int Energy,ArrayList<Integer> unitPrice, ArrayList<Integer> Request, ArrayList<Integer> Build1,ArrayList<Integer> Build2,String Requested, int currentDelay,int budget) {
		String temp=Prosperity+";"+Food+","+Materials+","+Energy+";";
		
		StringBuilder temp1=new StringBuilder();
		for (int i=0;i<unitPrice.size();i++) {
		    temp1.append(unitPrice.get(i));
		    if (i < unitPrice.size() - 1) {
		        temp1.append(",");
		    }
		}
		temp1.append(";");
		for (int i=0;i<Request.size();i=i+2) {
		    temp1.append(Request.get(i));
		    temp1.append(",");
		    temp1.append(Request.get(i+1));
		    temp1.append(";");  
		}
		for (int i=0;i<Build1.size();i++) {
		    temp1.append(Build1.get(i));
		    if (i < Build1.size() - 1) {
		        temp1.append(",");
		    }
		}
		temp1.append(";");
		for (int i=0;i<Build2.size();i++) {
		    temp1.append(Build2.get(i));
		    if (i < Build2.size() - 1) {
		        temp1.append(",");
		    }
		}
		temp1.append(";");
		temp=temp+temp1+Requested+","+currentDelay+";"+budget+";";
		//System.out.println(temp.toString());
		return temp;
	}
	public static void visualize(String grid) {
		String [] visualize= splitGrid(grid); //Take the string input and split upon semicolon.
		int Prosperity=Integer.parseInt(visualize[0]);
		int Food=Integer.parseInt(splitComma(visualize[1])[0]);
		int Materials=Integer.parseInt(splitComma(visualize[1])[1]);
		int Energy=Integer.parseInt(splitComma(visualize[1])[2]);
		System.out.println("Prosperity:"+Prosperity+"\n"+"Food:"+Food+"\n"+"Materials:"+Materials+"\n"+"Energy:"+Energy);
	}

	public static String solve(String grid, String strategy, boolean visualize) {
		//H: Defind elworld
		int budget=100000;
		String [] getInputs= splitGrid(grid); //Take the string input and split upon semicolon.
		
		int Prosperity=Integer.parseInt(getInputs[0]);
		int Food=Integer.parseInt(splitComma(getInputs[1])[0]);
		int Materials=Integer.parseInt(splitComma(getInputs[1])[1]);
		int Energy=Integer.parseInt(splitComma(getInputs[1])[2]);
		
		ArrayList<Integer> unitPrice = new ArrayList<Integer>();
		int unitPriceFood=Integer.parseInt(splitComma(getInputs[2])[0]);
		int unitPriceMaterial=Integer.parseInt(splitComma(getInputs[2])[1]);
		int unitPriceEnergy=Integer.parseInt(splitComma(getInputs[2])[2]);
		unitPrice.add(unitPriceFood);
		unitPrice.add(unitPriceMaterial);
		unitPrice.add(unitPriceEnergy);
		
		ArrayList<Integer> Request = new ArrayList<Integer>();
		Request.add(Integer.parseInt(splitComma(getInputs[3])[0])); // Add amountRequestFood
		Request.add(Integer.parseInt(splitComma(getInputs[3])[1])); // Add delayRequestFood
		Request.add(Integer.parseInt(splitComma(getInputs[4])[0])); // Add amountRequestMaterials
		Request.add(Integer.parseInt(splitComma(getInputs[4])[1])); // Add delayRequestMaterials
		Request.add(Integer.parseInt(splitComma(getInputs[5])[0])); // Add amountRequestEnergy
		Request.add(Integer.parseInt(splitComma(getInputs[5])[1])); // Add delayRequestEnergy
		
		ArrayList<Integer> Build1 = new ArrayList<Integer>();
		Build1.add(Integer.parseInt(splitComma(getInputs[6])[0])); // Add priceBUILD1
		Build1.add(Integer.parseInt(splitComma(getInputs[6])[1])); // Add foodUseBUILD1
		Build1.add(Integer.parseInt(splitComma(getInputs[6])[2])); // Add materialsUseBUILD1
		Build1.add(Integer.parseInt(splitComma(getInputs[6])[3])); // Add energyUseBUILD1
		Build1.add(Integer.parseInt(splitComma(getInputs[6])[4])); // Add prosperityBUILD1

		ArrayList<Integer> Build2 = new ArrayList<Integer>();
		Build2.add(Integer.parseInt(splitComma(getInputs[7])[0])); // Add priceBUILD1
		Build2.add(Integer.parseInt(splitComma(getInputs[7])[1])); // Add foodUseBUILD1
		Build2.add(Integer.parseInt(splitComma(getInputs[7])[2])); // Add materialsUseBUILD1
		Build2.add(Integer.parseInt(splitComma(getInputs[7])[3])); // Add energyUseBUILD1
		Build2.add(Integer.parseInt(splitComma(getInputs[7])[4])); // Add prosperityBUILD1

		String Requested=null;
		int currentDelay=0;
		//snap collect lef r d u kill
		String [] operators = {"RequestFood","RequestMaterials","RequestEnergy","BUILD1","BUILD2","WAIT"};// The array of operators used in this SearchProblem	//ask how can a state be repeated
		String state= generateState(Prosperity,Food,Materials,Energy,unitPrice,Request,Build1,Build2,Requested,currentDelay,budget);
		NewState newstate= new NewState(state); // the initial state that we are going to start with
		LLAPSearch eg = new LLAPSearch(operators,newstate); //create an instance of end game	
    	String x = eg.GeneralSearch(eg,strategy,visualize);
    	System.out.println(x);
		return x;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String grid5 = "50;22,22,22;50,60,70;30,2;19,1;15,1;300,5,7,3,20;500,8,6,3,40;";

		
//		solve(grid5,"BF",false);
//		solve(grid6,"BF",true);
//		
//		solve(grid5,"DF",false);
//		solve(grid6,"DF",true);
//		
		//solve(grid5,"BF",true);
		//solve(grid6,"UC",false);
//		
//		solve(grid5,"ID",false);
//		solve(grid6,"ID",true);
//		
		solve(grid5,"GR1",false);
//		solve(grid6,"GR1",true);
//		
//		solve(grid5,"GR2",false);
//		solve(grid6,"GR2",true);
//		
//	solve(grid5,"AS1",false);
//		solve(grid6,"AS1",false);
		
//		solve(grid5,"AS2",false);
//		solve(grid6,"AS2",false);


	}

}*/
