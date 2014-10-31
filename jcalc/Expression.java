package jcalc;

import java.util.Hashtable;
import java.util.Stack;

public class Expression {
	protected Expression first;
	protected Expression second;
	
	Expression(){
		
	}
	
	Expression(Expression first, Expression second){
		this.first = first;
		this.second = second;
	}
	
	public boolean equals(Object object){
		Expression e = (Expression) object;
		Expression calculated = calc();
		return calculated.equals(e.calc());
	}
	
	Expression calc(){
		return new Sum(first, second);
	}
	
	Operand toOperand(){
		Expression cfirst = first.calc();
		while(!(cfirst instanceof Operand))
			cfirst = cfirst.calc();
		
		Expression csecond = second.calc();
		while(!(csecond instanceof Operand))
			csecond = csecond.calc();
		
		return (Operand)this.calc();
	}

	Expression add(Expression e){
		return new Sum(this, e);
	}
	
	Expression sub(Expression e){
		return new Sub(this, e);
	}
	
	Expression mul(Expression e){
		return new Mul(this, e);
	}
	
	Expression div(Expression e){
		return new Div(this, e);
	}
	
	static String rpn(String expression){
		Stack<String> operators = new Stack<String>();
		String[] operands = expression.split(" ");
		String output = "";
		
		Hashtable<String, Integer> priority = new Hashtable<String, Integer>();
		priority.put("*", 3);
		priority.put("/", 4);
		priority.put("-", 2);
		priority.put("+", 1);
		
		for(int i = 0; i < operands.length; ++i){
			if(isOperator(operands[i])){
				String current = operands[i];
				if(operators.empty())
					operators.push(current);
				else if(priority.get(current) < priority.get(operators.peek())){
					output += operators.pop() + " ";
					operators.push(current);
				} else {
					operators.push(current);
				}
			}
			else
				output += operands[i] + " ";	
		}
		
		while(!operators.empty()){
			output += operators.pop().toString() + " ";
		}
		
		return output.substring(0, output.length()-1);
	}
	
	static boolean isOperator(String string){
		return string.equals("+") 
				|| string.equals("-")
				|| string.equals("*")
				|| string.equals("/");
	}
	
	public static Expression eval(String expression){
		String rpn = rpn(expression);
		
		String[] operands = rpn.split(" ");
		Stack<Expression> stack = new Stack<Expression>();
		
		for(int i = 0; i < operands.length; ++i){
			if(isOperator(operands[i])){
				Expression second = stack.pop();
				Operation operation = Operation.get(stack.pop(), second, operands[i]);
				stack.push(operation.calc());
			} else {
				stack.push(new Operand(Double.parseDouble(operands[i])));
			}
		}
				
		return stack.pop();
	}
	
	public String toString(){
		return this.toOperand().toString();
	}
}

abstract class Operation extends Expression {
	Operand a;
	Operand b;
	
	Operation(){
		
	}
	
	Operation(Expression expression){
		a = expression.toOperand();
		this.first = a;
	}
	
	Operation(Expression first, Expression second){
		this(first);
		b = second.toOperand();
		this.second = b;
	}
	
	static Operation get(Expression first, Expression second, String operation){
		switch(operation) {
			case "+":
				return new Sum(first, second);
			case "-":
				return new Sub(first, second);
			case "*":
				return new Mul(first, second);
			case "/":
				return new Div(first, second);
			case "sqrt":
				return new Sqrt(first);
			default:
				return null;
		}
	}
	
	abstract Expression calc();
}

class Operand extends Expression {
	double value;
	
	Operand(double value){
		this.value = value;
	}
	
	Operand(Expression e){
		Expression calculated = e.calc();
		while(!(calculated instanceof Operand))
			calculated = calculated.calc();
		value = ((Operand)calculated).value;
	}
	
	Expression calc(){
		return this;
	}
	
	Operand toOperand(){
		return this;
	}
	
	public boolean equals(Object object){
		if(!(object instanceof Operand)){
			Expression e = (Expression) object;
			return e.calc().equals(this);
		}
		Operand o = (Operand) object;
		return Math.abs(value - o.value) < 0.000001;
	}
	
	public String toString(){
		return Double.toString(value);
	}
}

class Sum extends Operation {
	Sum(Expression first, Expression second){
		super(first, second);
	}
	
	Expression calc(){
		return new Operand(a.value + b.value);
	}
}

class Sub extends Operation {
	Sub(Expression first, Expression second){
		super(first, second);
	}
	
	Expression calc(){
		return new Operand(a.value - b.value);
	}
}

class Mul extends Operation {
	Mul(Expression first, Expression second){
		super(first, second);
	}
	
	Expression calc(){
		return new Operand(a.value * b.value);
	}
}

class Div extends Operation {
	Div(Expression first, Expression second){
		super(first, second);
	}
	
	Expression calc() throws IllegalArgumentException {
		if(b.value == 0.0) throw new IllegalArgumentException();
		return new Operand(a.value / b.value);
	}
}

class Sqrt extends Operation {
	Sqrt(Expression expression){
		super(expression);
	}
	
	Expression calc(){
		return new Operand(Math.sqrt(a.value));	
	}
}
