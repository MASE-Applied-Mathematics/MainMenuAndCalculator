package projectCalculator;

import java.util.ArrayList;
import java.util.Scanner;

public class ScientificCalculatorApp {

	public static void start() {
		System.out.println("Enter function in () format, sin(2.2) 3^2^3 ln(2.1) exp(2+3) log(log(2)) roots(2,(3+3))");
		// the following test has been run
		showTestRecord();
				
		// user to enter expression
		String input = inputExpression();
		
		//create basicCalculator instance and run
		ScientificCalculatorJava calculator = new ScientificCalculatorJava();
		calculator.setInput(input);
		calculator.getResult();		

	}

	//start program, user inputs expression 
	public static String inputExpression(){
			
			
			Scanner sc = new Scanner(System.in);
			String s = "";
			do{		
				System.out.println("Enter expression for calculation: ");
				s = sc.nextLine();	
				//remove all spaces, and = if it ends with =
				s=s.replaceAll("\\s","");
				if(s.endsWith("=")){
					s = s.substring(0, s.length() - 1);
				}
				
			}while (s.length()<1);
			System.out.println("----------------------------------------");
			System.out.println();
			return s;
			
	}
		
	
	public static void showTestRecord(){
		ArrayList<String> successRecords = new ArrayList<String>();
		ArrayList<String> errorRecords = new ArrayList<String>();
		ArrayList<String> unexpectedErrorRecords = new ArrayList<String>();
		
		//add test expression SUCCESS
		successRecords.add(" 3*Sin(30)-roots(2,2)*exp3= -31.369 ");
		successRecords.add(" 3^2^4 = 4.30467E ");
		successRecords.add(" -(3^2)^4 =-6561.0");
		successRecords.add(" sin(ln(30)+5) = 0.85");
		successRecords.add(" log(5)+cos(3)+tan(50)+3^ln(3) = 2.78");
		successRecords.add(" sin3+log4-exp2.5 =-11.4");
		successRecords.add(" exp((3*2+3)) = 8103.08");
		
		System.out.println("------following running without error:");
		for(String s : successRecords){
			System.out.println(s);
		}		
		System.out.println("");
		
		//add test expression with handled ERROR
		errorRecords.add(" ln(-4) = NaN ");
		errorRecords.add(" 3^^4 invalid operator detected, no result ");
		errorRecords.add(" roots(-2,-2) = NaN ");
		
		System.out.println("------following running with expected error:");
		for(String s : errorRecords){
			System.out.println(s);
		}
		System.out.println();
		
		//add test expression with UNEXPECTED ERROR, need to work on these errors !
		unexpectedErrorRecords.add("extreme big numbers");	
		unexpectedErrorRecords.add("roots(-1,3) = NaN");
		
		
		System.out.println("------following expression need to work on:");
		for(String s : unexpectedErrorRecords){
			System.out.println(s);
		}
		System.out.println();
	}
	// end of java class
}