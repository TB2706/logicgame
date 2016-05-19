package Model;


public class XNORGate extends Gate {
	XNORGate(Signal in1, Signal in2, int t, int b){
		super(in1, in2,t,b);
		size=2;
		type = GateType.XNOR;
		}
	
	
	public void update(){
		if ((input1.getValue()==1 && input2.getValue()==0) || ((input1.getValue()==0 && input2.getValue()==1))){
			output.setValue(0);
		}
		else{
			output.setValue(1);
		}
	}
}
