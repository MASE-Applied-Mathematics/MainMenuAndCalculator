package projectCalculator;

import java.util.Scanner;

public class MainMenu {
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int input=0;
		do{	
			System.out.println();
			System.out.println("Main Menu, select your option by number");
			System.out.println();
			System.out.println("1 - Basic Calcualtion");
			System.out.println("2 - Scientific Calcualtion");
			System.out.println("3 - Graph");
			System.out.println("4 - Matrix and data compression");
			System.out.println("5 - Combination and probabilites");
			System.out.println("6 - Sets");
			System.out.println("7 - Exit");
			System.out.println();
			System.out.print("Enter your Choice: ");
			
			input= sc.nextInt();
			switch(input){
			case 1: BasicCalculatorApp.start(); 
					break;
			case 2: ScientificCalculatorApp.start();
					break;
			
			}
			
			
		}while(input!=7);
		

	}

}
