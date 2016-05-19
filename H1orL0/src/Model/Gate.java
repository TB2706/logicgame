package Model;

import View.*;

import javax.swing.text.View;

public abstract class Gate {
	protected int top,bottom,middle;
	protected Signal input1, input2, input3, output;
	protected Integer size;
	protected GateType type;  //0=NOT, 1=AND,2=NAND, 3=OR, 4=NOR, 5=XOR, 6=XNOR, 7=UNIVERSAL
	
	Gate(Signal in1, int t){
		top=t;
		input1=in1;
		output=new Signal(-1);
	}
	
	Gate(Signal in1, Signal in2, int t, int b){
		top=t;
		bottom=b;
		input1=in1;
		input2=in2;
		output=new Signal(-1);
	}
	Gate(Signal in1, Signal in2, Signal in3, int t, int m, int b){
		top=t;
		middle=m;
		bottom=b;
		input1=in1;
		input2=in2;
		input3=in3;
		output=new Signal(-1);
	}
	
	public int getTop(){
		return top;
	}
	public int getMiddle(){
		return middle;
	}
	public int getBottom(){
		return bottom;
	}
	public GateType gateType(){return type;}
	
	public void toggleGate(){};
	
	public boolean isUNI(){return false;}
	
	abstract void update();
	
	/*public Integer getOutput(){
		return output;
	}*/
}
