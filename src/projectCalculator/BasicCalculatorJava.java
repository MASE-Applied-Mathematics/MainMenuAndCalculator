package projectCalculator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BasicCalculatorJava {
	private String input;								// taking in expression 
	private boolean valid = true;  						// flag if expression in valid, valid = true
	private Double result = null;
	ArrayList<String> tokens = new ArrayList<String>();

	public BasicCalculatorJava() {
		super();
	}

	public void setInput(String input) {
		this.input = input;
	}

	public Double getResult() {
		this.checkInvalidChar();
		this.checkParenthese();
		this.checkStart();
		this.checkEnd();
		this.addExtraChar();
		this.checkAdjacentOperator();		
		this.convertToTokens();
		this.runShauntingYard();
		return result;
	}


	
	public String checkInvalidChar(){
		
		for(int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			if( ! "+-*/()0123456789. ".contains(""+ch)){
				System.out.println("checking - invalid chararacter detected " + ch );
				valid = false;
				return null;
			}
		}
		
		return "checked - no invalid char found";
	}
	
	public String checkParenthese(){
		
		int totalParenthese = 0;
		for ( int i=0; i < input.length(); i++){
			char ch = input.charAt(i);
			
			if( ch == '(' ){	
				totalParenthese++;
			}else if( ch == ')' ){	
				totalParenthese--;
			}	
			
			if(totalParenthese<0){
				System.out.println("checking - invalid parenthese detected " );
				valid = false;
				return null;
			}
		}
		if(totalParenthese!=0){
			System.out.println("checking - invalid parenthese detected " );
			valid = false;
			return null;
		}
			
		return "checked - no invalid parenthese found";
	}
	
	public String checkAdjacentOperator(){
		
		input = input.replaceAll("([+]|[-]|[*]|[/]){2,}", "@");
		
		CharSequence[] invalidParenthese = { "()", "(*", "(/", "*)", "/)", "+)", "-)" };
		for(CharSequence chSequence: invalidParenthese){
			input = input.replace(chSequence, "~");
		}		
		if(input.contains("@")){
			System.out.println("checking - invalid operator detected");
			valid = false;
			return null;
		}else if(input.contains("~")){
			System.out.println("checking - invalid parenthese detected");
			valid = false;
			return null;
		}
		
		return "checked - adjacent operators checked";
	}
	
	public String checkStart(){
		char ch = input.charAt(0);
		
		switch(ch){
		case '*':
		case '/':
		case ')':
			System.out.println("checking - can't start with " + ch);
			valid = false;
			return null;		
		}
		
		return "checked - start character OK";
	}
	
	public String checkEnd(){
		char ch = input.charAt(input.length()-1);
		
		switch(ch){
		case '*':
		case '/':
		case '+':
		case '-':
		case '(':
			System.out.println("checking - can't end with " + ch);
			valid = false;
			return null;		
		}
		
		return "checked - end character OK";
	}
	
	public String addExtraChar(){
		
		char ch = input.charAt(0);
		if(ch == '+' | ch == '-'){
			input = "0" + input;
		}	
	
		CharSequence negative = "(-";
		CharSequence positive = "(+";
		CharSequence negativeNew = "(0-";
		CharSequence positiveNew = "(0+";
		input = input.replace(negative, negativeNew);
		input = input.replace(positive, positiveNew);
		
		for(int i=1; i<input.length(); i++){
			ch = input.charAt(i);
			char charBefore = input.charAt(i-1);
			if(ch== '(' && !"+-*/(".contains( ""+ charBefore) ){
				input = input.substring(0, i) + "*" + input.substring(i, input.length());
			}
		}
				
		//System.out.println(input);
		return "add extra character - completed";
	}
	
	public String convertToTokens(){				
			
		if(input==null||input.length()<0|| !valid){
			System.out.println("error - expression not valid");		
			valid = false;
			return null;
		} 		
		
		String token = new String("");
		for(int i=0; i<input.length(); i++){
			Character ch = new Character(input.charAt(i));						
			switch(ch){
			case '+':
			case '-':
			case '*':
			case '/':
			case '(':
			case ')':				
				if(!token.isEmpty()){					
					tokens.add(token);
					token="";					
				}				
				tokens.add(""+ch.charValue());
				break;
			default:
				token += ch.charValue();				
			}			
		}
		if(!token.isEmpty()){
			tokens.add(token);
		}
		
		//System.out.println(tokens.toString());
		return "tokens converted";
	}
	
	public String runShauntingYard(){
		if(tokens==null || tokens.isEmpty() || !valid){
			System.out.println("error - no result");
			valid = false;
			return null;
		}
		if(tokens.size()==1){
			try {
				Double operand = Double.parseDouble(tokens.get(0));	
				result = operand.doubleValue();
				} catch (NumberFormatException e) {
				  System.out.println("error - " + e.getMessage());
				  valid = false;
				  return null;
				}			
			
			System.out.println("result - " + tokens.get(0));
			return tokens.get(0);
		}
		
		Stack<Double> operands = new Stack<Double>();
		Stack<String> operators = new Stack<String>();
		
		for(String token : tokens){
			switch( getPrecedence(token) ){
			case 0: 												// token is number				
				try {
					Double operand = Double.parseDouble(token);
					operands.add(operand);
					} catch (NumberFormatException e) {
					  System.out.println("error - " + e.getMessage());
					  valid = false;
					  return null;
					}
				break;
			case 1:													// token is (
				operators.push(token);
				break;
			case 2:													// token is +-
			case 3:													// token is */
				if(operators.isEmpty() || getPrecedence(token) > getPrecedence( operators.peek() )){
					operators.push(token);							// add token to operator stack, if token has higher precedence than the first operator in stack
				}else{												// if token P <= 1st operator in stack 's P, pop operator and calculate in operands queue
					do{
						calculateOperandsStack(operands, operators.pop());	
					}while( !operators.isEmpty() && getPrecedence( token )<= getPrecedence( operators.peek() ));
					operators.push(token);
				}
				break;
			case 10:												// token is )
				while( ! operators.peek().equals("(") ){
					calculateOperandsStack(operands, operators.pop());
				}
				operators.pop();
				break;	
			}
			
		}
		
		while( operands.size() >1){
			calculateOperandsStack( operands, operators.pop() );
		}
		
		for(String token : tokens){
			System.out.print(token + " ");
		}
		System.out.println("= "+ operands.peek());
		result = operands.peek().doubleValue();
		return operands.peek().toString();
	}
	
	//compare operator's precedence	
	public int getPrecedence(String s){
			int i=0;
			switch(s.charAt(0)){
			case '+': i=2;break;
			case '-': i=2;break;
			case '*': i=3;break;
			case '/': i=3;break;
			case '(': i=1;break;
			case ')': i=10;break;	
			default : i=0;
			}		
			return i;
		}
		
	//compare operator's precedence	
	public String calculateOperandsStack(Stack<Double> operands, String operator){
			if(operands.size()<2 || !"+-*/".contains( operator )){
				System.out.println("Error - calculating" + " operands is : " + operands.toString() + ", operator is : " + operator);
				valid = false;
				return null;
			}else{
				double temp=0;
				switch(operator.charAt(0)){
				case '+': 
					temp = operands.pop() + operands.pop();
					break;
				case '-': 
					temp = 0-operands.pop() + operands.pop();
					break;
				case '*': 
					temp = operands.pop() * operands.pop();
					break;
				case '/': 
					temp = 1/operands.pop() * operands.pop();
					break;					
				}
				operands.add(new Double(temp));

			}		
			return "operands stack calculated";
		}
		

}
