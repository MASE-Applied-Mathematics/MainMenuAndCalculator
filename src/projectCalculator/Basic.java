package projectCalculator;

import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Basic {
	private static Scanner sc = new Scanner(System.in);
	
	public static String start(){
		System.out.println("Basic.start() ...");
		
		//--------https://stackoverflow.com/questions/11577259/can-i-convert-a-string-to-a-math-operation-in-java
		System.out.println("Enter your expression below : ");
		String input = sc.nextLine();
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine se = manager.getEngineByName("JavaScript");
		Object result=null;
		
		try {
			result = se.eval(input);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("The result is : " + result.toString());
		//........
		return null;
	}

}
