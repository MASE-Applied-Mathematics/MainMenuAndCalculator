package projectCalculator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ScientificCalculatorJava {
	private String input;								// taking in expression 
	private boolean valid = true;  						// flag if expression in valid, valid = true
	private Double result = null;
	ArrayList<String> tokens = new ArrayList<String>();

	public ScientificCalculatorJava() {
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
		this.addParentheseForPowerSet();
		this.convertToTokens();
		this.runShauntingYard();
		return result;
	}

	
	public String checkInvalidChar(){
		
		input = input.toLowerCase();
		input = input.replace("sin", "1S");		//change sin-> S, simpler to program, add place holder 1, sin(5)->1S5, change to infix notation for shauntingYard 
		input = input.replace("cos", "1C");
		input = input.replace("tan", "1T");
		input = input.replace("roots", "O"); 	//roots -> O -> R
		input = input.replace("exp", "1P");
		input = input.replace("log", "1G");
		input = input.replace("ln", "1N");
		
		// check if roots(a,b) is valid
		int totalR = 0;
		for(int i=0; i<input.length(); i++){
			char ch = input.charAt(i);
			if(ch == 'O'){
				totalR++;
			}else if(ch == ','){
				totalR--;
			}
			if(totalR<0){
				System.out.println("error - with roots ,");
				valid = false;
				return null;
			}
		}
		if(totalR != 0){
			System.out.println("error - with roots ,");
			valid = false;
			return null;
		}
		// convert O(a,b) -> (a)R(b)
		int in = input.length()-1;		
		for(int i=in; i>=0; i--){				
			char ch = input.charAt(i);
			if(ch == 'O'){
				input = input.replaceFirst("[,]", ")R(");
			}			
		}		
		input = input.replaceAll("O", "");
		
		for(int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			if( ! "+-*/()0123456789.SCT^RNPG ".contains(""+ch)){
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
		
		input = input.replaceAll("(['^']|[+]|[-]|[*]|[/]){2,}", "@");
		
		//input = input.replaceAll("( [(] & [*]|[/]|[S]|[C]|[T]|[^]|[R]|[N]|[P]|[G]){2,}", "!");
		if(input.contains("!")){
			System.out.println("line 123");
		}
		
		CharSequence[] invalidParenthese = { "()", "(*", "(/","(^", "*)", "/)", "+)", "-)","^)" };
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
		case '^':
		case 'R':
			System.out.println("checking - can't start with " + ch);
			valid = false;
			return null;		
		}
		
		return "checked - start character OK";
	}
	
	public String checkEnd(){
		char ch = input.charAt(input.length()-1);
		
		if("*/+-(SCT^RNPG".contains(""+ch)){		
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
		
		//add * when ()() or 3() -> ()*(), 3*()
		for(int i=1; i<input.length(); i++){
			ch = input.charAt(i);
			char charBefore = input.charAt(i-1);
			if(ch== '(' && !"+-*/SCT^RNPG(".contains( ""+ charBefore) ){
				input = input.substring(0, i) + "*" + input.substring(i, input.length());
			}
		}
		return "add extra character - completed";
	}
	
	public String addParentheseForPowerSet(){
		//System.out.println(input + " -addPFP");
		// 3^4^5 right/left precedence changed to -> 3^(4^5)
		int last = input.length()-2;			// ^ not be 1st or last char, as checked already
		for(int i=last; i>0; i-- ){
			char ch = input.charAt(i);
			if(ch == '^'){
					int totalParentheseAfter = 0;	
					int length = input.length();
					labelAfter:
					for(int j=1; i+j<length; j++){
							char afterCh = input.charAt(i+j);							
							switch(afterCh){
								case '(': totalParentheseAfter++; break;
								case ')': totalParentheseAfter--; break;
								case '+':
								case '-':
								case '*':
								case '/':
									if( (input.charAt(i+1)=='(' && totalParentheseAfter ==0) || input.charAt(i+1) != '('){
										input = input.substring(0, i+j) + ")" + input.substring(i+j, input.length());
										break labelAfter;
									}
									break;
							}							
							if(i+j==input.length()-1){
								input = input+")";
							}	 // if reach the end and no ) added, will add ) in the end
					}
						
					
		  				int totalParentheseBefore = 0;
		  				labelBefore:
		  				for(int j=1; i-j>=0; j++){
		  					char beforeCh = input.charAt(i-j);
		  					switch(beforeCh){
		  					case '(': totalParentheseBefore++; break;
		  					case ')': totalParentheseBefore--; break;
		  					case '+':
		  					case '-':
		  					case '*':
		  					case '/':
		  					case '^':
		  						if((input.charAt(i-1)==')' && totalParentheseBefore ==0) || input.charAt(i-1)!=')'){
		  							input = input.substring(0, i-j+1) + "(" + input.substring(i-j+1, input.length());
		  							break labelBefore;
		  						}
		  						break;
		  					}						
		  					if(i-j == 0){
		  						input = "("+input;
		  					}					
		   				}
					}
  			}
			
		return "add parenthese for power set - completed";
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
				
			case 'S':
			case 'C':
			case 'T':
			case 'R':
			case 'N':
			case 'P':	
			case 'G':
			case '^':				
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
		
		//System.out.println(tokens);
		
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
			case 4:													// token is ^
			case 5:													// token is sin cos tan roots ln exp log
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
		
		//System.out.println("line 377 ---" + operands + " --- " + operators);
		while( operands.size() >1){
			//System.out.println(operands);
			//System.out.println(operators);
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
			
			case 'S': i=5;break;
			case 'C': i=5;break;
			case 'T': i=5;break;
			case 'N': i=5;break;
			case 'P': i=5;break;
			case 'G': i=5;break;			
			case 'R': i=5;break;
			case '^': i=4;break;
			
			case ')': i=10;break;	
			default : i=0;
			}		
			return i;
		}
		
	//compare operator's precedence	
	public String calculateOperandsStack(Stack<Double> operands, String operator){
			if(operands.size()<2 || !"+-*/SCTRNPG^".contains( operator )){
				System.out.println("Error - calculating" + " operands is : " + operands.toString() + ", operator is : " + operator);
				valid = false;
				return null;
			}else{
				double temp=0;
				double a,b;
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
					
				case 'S': 
					temp = Math.sin(operands.pop()) * operands.pop();
					break;
				case 'C': 
					temp = Math.cos(operands.pop()) * operands.pop();
					break;
				case 'T': 
					temp = Math.tan(operands.pop()) * operands.pop();
					break;
				case 'N': 
					temp = Math.log(operands.pop()) * operands.pop();
					break;		
				case 'P': 
					temp = Math.exp(operands.pop()) * operands.pop();
					break;
				case 'G': 
					temp = Math.log10(operands.pop()) * operands.pop();
					break;
				case 'R': 
					a = operands.pop();
					b = operands.pop();	
					
					temp = Math.pow(b, 1.0 /a);
										
					break;
				case '^': 
					a = operands.pop();
					b = operands.pop();
					temp = Math.pow(b,a);
					break;
									
					
					
				}
				operands.add(new Double(temp));

			}		
			return "operands stack calculated";
		}
		

}
