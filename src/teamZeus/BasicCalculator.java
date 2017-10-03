package teamZeus;

import java.util.ArrayList;
import java.util.Scanner;

public class BasicCalculator {

//	private static ArrayList<String> operands = new ArrayList<String>();
//	private static ArrayList<String> operator = new ArrayList<String>();
//	private static char[] validOperator = {'+','-','*','/','(',')'};
//	private static char[] validOperands = {'0','1','2','3','4','5','6','7','8','9','.',','};
//	private static char[] validOtherChar = {};
	
	public static void main(String[] args) {
		// the following test has been run
		showTestRecord();
		
		// user to enter expression
		String input = inputExpression();
		
		// run validate(), return error for invalid input, and also, re-format input
		ArrayList<Character> validInput = validate(input);
		if(validInput==null || validInput.isEmpty()){
			System.out.println("Error with input expression");
		}else if(convertToTokenList(validInput)!=null){				
			// run convertToList(), 
			ArrayList<Object> tokens = new ArrayList<Object>(convertToTokenList(validInput));
			
			// run runShauntingYard(), return result as String. Return Null if there is runtime error
			String result = runShauntingYard(tokens);
			
			if(result==null){
				System.out.println("Error runtime");
			}else{
				for(Character ch : validInput){
					System.out.print(ch.charValue()+" ");
				}				
				System.out.print(" = " + result);
			}			
		}
		
	}

	//start program, user inputs expression 
	public static String inputExpression(){
		
		System.out.println("Enter expression for calculation: ");
		Scanner sc = new Scanner(System.in);
		String s = "";
		do{			
			s = sc.nextLine();	
			//remove all spaces; 
			s=s.replaceAll("\\s","");
			if(s!=null && s.endsWith("=")){
				s = s.substring(0, s.length() - 1);
			}
			System.out.println("Enter expression for calculation: ");
		}while (s.length()<1);
		System.out.println("----------------------------------------");
		System.out.println();
		return s;
		
	}
	
	//validate(), return error for invalid input, and also, re-format input
	public static ArrayList<Character> validate(String s){
		/* add 0 for + or - number;
		 * before and after (
		 * before and after )
		 * 1st and last char 
		 * can't have 2 consecutive +- * 
		 * . not included in this method, will be implemented in convertToList()
		 */
		//remove all space, tab, etc.
		//s=s.replaceAll("\\s","");		
		//System.out.println("validate after replace : " +s);
		
		//convert string to arrayList<>
		ArrayList<Character> chars = new ArrayList<Character>();
		for (char c : s.toCharArray()) {
		  chars.add(c);
		}


		//+ or - number changed to 0 + number, or 0 - number
		//rules for 1st and last char
		
		switch(chars.get(0)){
		case '+':
		case '-':
			chars.add(0,'0');
			break;
		case '(':			
			if(chars.size()<2 || chars.get(1)=='*' || chars.get(1)=='/' || chars.get(1)==')'){
				System.out.println("invalid character after ( ");
				return null;
			}
			break;
		case '*':
		case '/':
		case ')':
			System.out.println("invalid, can't start with " + chars.get(0));
			return null;		
		}
		
		switch(chars.get(chars.size()-1)){
		case '+':
		case '-':
		case '*':
		case '/':
		case '(':
			System.out.println("invalid, can't end with " + chars.get(chars.size()-1));
			return null;
		case ')':
			if(chars.size()<2 || chars.get(chars.size()-2)=='*' || chars.get(chars.size()-2)=='/' || chars.get(chars.size()-2)=='+'|| chars.get(chars.size()-2)=='-'|| chars.get(chars.size()-2)=='('){
				System.out.println("invalid character before ) ");
				return null;
			}
			break;
			
		}
		
		int charsSize = chars.size();
		
		if( ! "+-*/()0123456789. ".contains(chars.get(0).toString()) ){
			System.out.println("validate() - invalid chararacter detected " + chars.get(0).toString() );
			return null;
		}
		if( ! "+-*/()0123456789. ".contains(chars.get(chars.size()-1).toString()) ){
			System.out.println("validate() - invalid chararacter detected " + chars.get(chars.size()-1).toString() );
			return null;
		}
		
		for ( int i=1; i<charsSize-1; i++){		
			//check invalid character
			if( ! "+-*/()0123456789. ".contains(chars.get(i).toString()) ){
				System.out.println("validate() - invalid chararacter detected " + chars.get(i).toString() );
				return null;
			}
			
			//check  ( ) total and order			
			if( chars.get(i).charValue() == '(' ){							
				//+ or - number changed to 0 + number, or 0 - number
				if(chars.get(i+1) == '+' || chars.get(i+1) == '-'){
					chars.add(i+1, '0');
					charsSize++;
				}
				if(chars.get(i+1)=='*' || chars.get(i+1)=='/'|| chars.get(i+1)==')'){
					System.out.println("invalid character after ( ");
					return null;
				}else if( ! "+-*/(".contains(chars.get(i-1).toString())){
					System.out.println("invalid character before ( ");
					return null;
				}	
			}else if( chars.get(i).charValue() == ')' ){				
				if( "+-*/(".contains(chars.get(i-1).toString())){
					System.out.println("invalid character before ) ");
					return null;
				}else if( ! "+-*/)".contains(chars.get(i+1).toString())){
					System.out.println("invalid character after ) ");
					return null;
				}
			}else if( "+-*/".contains(chars.get(i).toString())){
				if( "+-*/".contains(chars.get(i+1).toString())){
					System.out.println("invalid character after "+ chars.get(i));
					return null;
				}
			}		
		}
		
		int totalParenthese = 0;
		for ( int i=0; i<chars.size(); i++){
			if( chars.get(i).charValue() == '(' ){	
				totalParenthese++;
			}else if( chars.get(i).charValue() == ')' ){	
				totalParenthese--;
			}	
			
			if(totalParenthese<0){
				System.out.println("validate() - invalid parenthese detected " );
				return null;
			}
		}
		if(totalParenthese!=0){
			System.out.println("validate() - invalid parenthese detected " );
			return null;
		}
			
		return chars;
	}
	
	//convertToList(), convert string to operator<>, and operands<>
	public static ArrayList<Object> convertToTokenList(ArrayList<Character> chs){
		ArrayList<Object> tokens = new ArrayList<Object>();
		String tokenString="";
		for(Character ch : chs){
			switch(ch){
			case '+':
			case '-':
			case '*':
			case '/':
			case '(':
			case ')':				
				if(!tokenString.isEmpty()){					
					tokens.add(tokenString);
					tokenString="";					
				}				
				tokens.add(ch);
				break;
			default:
				tokenString += ch.charValue();				
			}			
		}
		if(!tokenString.isEmpty()){
			tokens.add(tokenString);	
			tokenString="";
		}	
		
		for(Object ob : tokens){
			if(ob.getClass().equals(String.class)){
				try{
					double d = Double.parseDouble((String) ob);
					ob = new Double(d);
				}catch(NumberFormatException e){
					System.out.println("Error with input number : " + e.toString());	
					return null;
				}				
				
			}
			//System.out.println("tokens are : " + ob.toString());
		}
		return tokens;
	}
	
	//runShauntingYard(), calculate operator<>, operands<>, result converted to String. Return null if there is runtime error
	public static String runShauntingYard(ArrayList<Object> tokens){
		if(tokens.size()==1){
			return tokens.get(0).toString();
		}
		
		ArrayList<Double> operands = new ArrayList<Double>();
		ArrayList<Character> operators = new ArrayList<Character>();
		
		for(Object ob : tokens){
			
			if(ob.getClass().equals(String.class)){
				//operands.add((Double) ob);
				operands.add(new Double(ob.toString()));
			}else if(getPrecedence(ob) == 1){
				// the token is (
				operators.add((Character) ob);
			}else if(getPrecedence(ob) == 10){
				// the token is )
				while( operators.get(operators.size()-1).charValue() != '('){
					calculateNumberQueue(operands, operators.get(operators.size()-1));
					operators.remove(operators.size()-1);
				};
				operators.remove(operators.size()-1);				
			}else if(operators.size()==0 || getPrecedence(ob)>getPrecedence(operators.get(operators.size()-1)) ){
				// the next token has higher precedence, except )
				operators.add((Character) ob);				
			}else{
				// the next token <= current top operator's precedence in operators stack
				do{
					calculateNumberQueue(operands, operators.get(operators.size()-1));
					operators.remove(operators.size()-1);
				}while( operators.size()>0 && getPrecedence(ob)<=getPrecedence(operators.get(operators.size()-1)));			
				operators.add((Character) ob);				
			}
						
		}
		
		while(operands.size()>1){
			calculateNumberQueue(operands, operators.get(operators.size()-1));
			operators.remove(operators.size()-1);			
		}
		
				
		return operands.get(0).toString();
	}
	
	//compare operator's precedence	
	public static int getPrecedence(Object c){
		int i=0;
		switch(((Character)c).charValue()){
		case '+': i=2;break;
		case '-': i=2;break;
		case '*': i=3;break;
		case '/': i=3;break;
		case '(': i=1;break;
		case ')': i=10;break;			
		}		
		return i;
	}
	
	//compare operator's precedence	
	public static String calculateNumberQueue(ArrayList<Double> operands, Object c){
		if(operands.size()<2 || !"+-*/".contains( ""+ (Character) c)){
			System.out.println("Error with calculate in operands queue" + ". operands is : " + operands.toString() + " character c is " + c.toString());
			return null;
		}else{
			double temp=0;
			switch(((Character) c).charValue()){
			case '+': temp=operands.get(operands.size()-2)+operands.get(operands.size()-1); break;
			case '-': temp=operands.get(operands.size()-2)-operands.get(operands.size()-1); break;
			case '*': temp=operands.get(operands.size()-2)*operands.get(operands.size()-1); break;
			case '/': temp=operands.get(operands.size()-2)/operands.get(operands.size()-1); break;
			}
			operands.remove(operands.size()-1);
			operands.remove(operands.size()-1);
			operands.add(new Double(temp));

		}		
		return "";
	}
	
	//show testing record
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
		
		System.out.println("------following running with expected error:");
		for(String s : errorRecords){
			System.out.println(s);
		}
		System.out.println();
		
		//add test expression with UNEXPECTED ERROR, need to work on these errors !
		unexpectedErrorRecords.add("extreme big numbers");		
		unexpectedErrorRecords.add("(2+2)(5+5)");
		unexpectedErrorRecords.add("3(2+5)");
		
		System.out.println("------following expression need to work on:");
		for(String s : unexpectedErrorRecords){
			System.out.println(s);
		}
		System.out.println();
	}
	// end of java class
}
