package jcalc;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExpressionTest {

	@Test
	public void testSumExpression(){
		Expression sum = new Sum(new Operand(3), new Operand(2));
		Expression result = sum.calc();
		assertEquals(new Operand(5), result);
	}
	
	@Test
	public void testOperandEquality(){
		Expression operand = new Operand(5);
		assertEquals(new Operand(5), operand);
	}
	
	@Test
	public void testExpressionAddition(){
		Expression first = new Operand(3);
		Expression second = new Operand(4);
		Expression result = new Sum(first, second);
		assertEquals(new Operand(7), result.calc());
	}
	
	@Test
	public void testOperandExpressionEquality(){
		Expression exp = new Sum(new Operand(5), new Operand(2));
		Operand seven = new Operand(7);
		assertEquals(exp, seven);
	}
	
	@Test
	public void testSubstractExpression(){
		Expression sub = new Sub(new Operand(4), new Operand(3));
		assertEquals(new Operand(1), sub);
	}
	
	@Test
	public void testTwoExpressionCalculation(){
		Expression first = new Sum(new Operand(3), new Operand(4));
		Expression result = new Sub(first, new Operand(5));
		assertEquals(new Operand(3 + 4 - 5), result);
	}
	
	@Test
	public void testExpressionToOperandConversion(){
		Expression first = new Sum(new Operand(3), new Operand(4));
		Expression result = new Sub(first, new Operand(5));
		Expression operand = result.toOperand();
		assertEquals(new Operand(3 + 4 - 5), operand);
	}
	
	@Test
	public void testSubFactoryMethod(){
		Expression sum = new Operand(3).add(new Operand(4));
		assertEquals(new Operand(3 + 4), sum);
	}
	
	@Test
	public void testTwoExpressionAddition(){
		Expression first = new Sum(new Operand(5), new Operand(7));
		Expression second = new Sum(new Operand(4), new Operand(2));
		Expression result = first.add(second);
		assertEquals(new Operand(18), result);
	}
	
	@Test
	public void testMulDivExpressions(){
		Expression mul = new Mul(new Operand(3), new Operand(4));
		Expression div = new Div(mul, new Operand(2));
		assertEquals(new Operand(12), mul);
		assertEquals(new Operand(6), div);
	}
	
	@Test
	public void testAllOperations(){
		Expression big = new Operand(5)
		  .mul(new Operand(7))
		  .add(new Operand(5))
		  .sub(new Operand(10))
		  .div(new Operand(5));
		assertEquals(new Operand((5 * 7 + 5 - 10) / 5), big);
	}
	
	@Test
	public void testOperationFactoryMethod(){
		Operation sum = Operation.get(new Operand(3), new Operand(4), "+");
		Operation sub = Operation.get(new Operand(3), new Operand(4), "-");
		Operation mul = Operation.get(new Operand(3), new Operand(4), "*");
		Operation div = Operation.get(new Operand(3), new Operand(4), "/");
		
		Operation mixed = Operation.get(div, new Operand(5), "*");
		
		assertEquals(new Operand(7), sum.calc());
		assertEquals(new Operand(-1), sub.calc());
		assertEquals(new Operand(12), mul.calc());
		assertEquals(new Operand(3.0 / 4), div.calc());
		assertEquals(new Operand(3.0 / 4 * 5), mixed.calc());
	}
	
	@Test
	public void testRPNConversion() {
		String rpn = Expression.rpn("3 + 4 - 7");
		assertEquals("3 4 7 - +", rpn);
	}
	
	@Test
	public void testEvaluateRPNExpression(){
		String rpn = Expression.rpn("3 + 4");
		Expression result = Expression.eval(rpn);
		assertEquals(new Operand(3 + 4), result);
	}
	
	@Test
	public void testConversionEvaluationTogether(){
		Expression result = Expression.eval("3 + 4 - 2");
		assertEquals(new Operand(3 + 4 - 2), result);
	}
	
	@Test
	public void testDivideByZero(){
		boolean thrown = false;
		try {
		    Expression.eval("7 / 0");
		} catch(Exception e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testOperationPrority(){
		Expression result = Expression.eval("4 * 2 + 3");
		assertEquals(new Operand(4 * 2 + 3), result);
	}
	
	@Test
	public void testDivMulOperation(){
		Expression result = Expression.eval("3 / 5 * 7");
		assertEquals(new Operand(3.0 / 5 * 7), result);
	}
	
	@Test
	public void testSqrtOperation(){
		Expression result = Operation.get(new Operand(4), null, "sqrt");
		assertEquals(new Operand(2), result);
	}
}
