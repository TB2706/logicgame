package Model;


public class NOTGate extends Gate {
	NOTGate(Signal in1, int t){
		super(in1, t);
		type=GateType.NOT;
		size=1;
	}
	
	void update(){
		if (input1.getValue()==1){
			output.setValue(0);
		}
		else{
			output.setValue(1);
		}
	}
}
