package Model;

import View.*;

public class NANDGate extends Gate {
	NANDGate(Signal in1, Signal in2, int t, int b){
		super(in1, in2, t, b);
		type=GateType.NAND;
		size=2;
	}

	NANDGate(Signal in1, Signal in2,Signal in3, int t, int m, int b){
		super(in1, in2, in3, t, m, b);
		type=GateType.NAND;
		size=3;
	}
	
	public void update(){
		if (size==2){
			if (input1.getValue()==1 && input2.getValue()==1){
				output.setValue(0);
			}
			else{
				output.setValue(1);
			}
		}
		else {
			if (input1.getValue()==1 && input2.getValue()==1 && input3.getValue()==1){
			output.setValue(0);
			}
			else{
				output.setValue(1);
			}
		}
	}
}



