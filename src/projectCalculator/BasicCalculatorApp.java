package projectCalculator;
import java.util.ArrayList;
import java.util.Scanner;

public class BasicCalculatorApp {

	public static void start() {
		// the following test has been run
		showTestRecord();
				
		// user to enter expression
		String input = inputExpression();
		
		//create basicCalculator instance and run
		BasicCalculatorJava calculator = new BasicCalculatorJava();
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
		successRecords.add(" (3+5)*5+((2+3)*5 -4/(1-5)) ");
		successRecords.add(" 4+5= ");
		successRecords.add(" 200 ");
		successRecords.add(" -5+4 ");
		successRecords.add(" +5+4 ");
		successRecords.add(" 00.1+100 ");
		successRecords.add(" -(-5+2)*2 ");
		successRecords.add(" 2*2*4*4/2/(-3) ");
		successRecords.add(" (((3))) ");
		successRecords.add("(2+2)(5+5)");
		successRecords.add("3(2+5)");
		successRecords.add("+.0");
		
		System.out.println("------following running without error:");
		for(String s : successRecords){
			System.out.println(s);
		}		
		System.out.println();
		
		//add test expression with handled ERROR
		errorRecords.add(" 3 * ^2= ");
		errorRecords.add(" 5/0 ");
		errorRecords.add(" 0/0 ");
		errorRecords.add(" ((2+3)/5 ");
		errorRecords.add(" 0.1 .2 *5 ");
		errorRecords.add(" 3+)4+5(+2 ");
		errorRecords.add(" 200,000 ");
		errorRecords.add(" 5++5 ");
		errorRecords.add(" (((..))) ");
		errorRecords.add(" . ");
		
		System.out.println("------following running with expected error:");
		for(String s : errorRecords){
			System.out.println(s);
		}
		System.out.println();
		
		//add test expression with UNEXPECTED ERROR, need to work on these errors !
		unexpectedErrorRecords.add("extreme big numbers");		

		
		
		System.out.println("------following expression need to work on:");
		for(String s : unexpectedErrorRecords){
			System.out.println(s);
		}
		System.out.println();
	}
	// end of java class
	
}
