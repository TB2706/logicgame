package Model;


public class XORGate extends Gate {
	XORGate(Signal in1, Signal in2, int t, int b){
		super(in1, in2,t,b);
		size=2;
		type = GateType.XOR;
		}
	
	
	public void update(){
		if ((input1.getValue()==1 && input2.getValue()==0) || ((input1.getValue()==0 && input2.getValue()==1))){
			output.setValue(1);
		}
		else{
			output.setValue(0);
		}
	}
}
