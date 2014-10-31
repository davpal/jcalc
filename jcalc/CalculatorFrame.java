package jcalc;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class CalculatorFrame extends JFrame {
	JPanel resultPane, buttonPane, mainPane;
	JButton[] buttons;
	JTextField resultField;

	CalculatorFrame(){
		setTitle("jCalc alpha");
		setSize(350,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initComponents();
		setVisible(true);
	}
	
	private void initComponents(){
		buttons = new JButton[]{
			new JButton("Sqrt"),
			new JButton("MOD"),
			new JButton("+/-"),
			new JButton("C"),
			new JButton("7"),
			new JButton("8"),
			new JButton("9"),
			new JButton("/"),
			new JButton("4"),
			new JButton("5"),
			new JButton("6"),
			new JButton("*"),
			new JButton("1"),
			new JButton("2"),
			new JButton("3"),
			new JButton("-"),
			new JButton("0"),
			new JButton("."),
			new JButton("="),
			new JButton("+")
		};
		
		resultField = new JTextField(25);
		resultField.setHorizontalAlignment(JTextField.RIGHT);
		resultField.setText("0");
		resultField.setEditable(false);
		
		for(int i=0; i < 3; i++){
			buttons[i].setEnabled(false);
		}
		
		resultPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPane = new JPanel(new GridLayout(5,4,5,5));
		mainPane = new JPanel(new BorderLayout());
		
		resultPane.add(resultField);
		
		ActionListener al = new ActionControler();
		
		for(int i = 0; i < buttons.length; i++){
			buttons[i].addActionListener(al);
			buttonPane.add(buttons[i]);
		}
		
		mainPane.add(resultPane, BorderLayout.NORTH);
		mainPane.add(buttonPane, BorderLayout.SOUTH);
		
		add(mainPane);
		pack();
	}
	
	public static void main(String[] args){
		new CalculatorFrame();
	}
	
	private class ActionControler implements ActionListener{
		
		private boolean clear;
	
		public void actionPerformed(ActionEvent e){
			String sl = ((JButton)e.getSource()).getText();
			if(sl == "C"){
				resultField.setText("0");
			}
			else if(sl.equals("*") 
					|| sl.equals("/") 
					|| sl.equals("-") 
					|| sl.equals("+"))
					resultField.setText(resultField.getText() + " " + sl + " ");
			else if(sl.equals("=")){	
				resultField.setText(Expression.eval(resultField.getText()).toString());
				clear = true;
			
			} else {
				if(clear){
					resultField.setText("0");
					clear = false;
				}
				if(resultField.getText().equals("0")) resultField.setText("");
				resultField.setText(resultField.getText() + sl);
			}
		}
	}
}