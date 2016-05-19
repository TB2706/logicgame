package Model;


public class UniversalGate extends Gate {
	
	UniversalGate(Signal in1, Signal in2, int t, int b){
		super(in1, in2, t, b);
		size=2;
		type = GateType.UNI;
		
	}
	UniversalGate(Signal in1, Signal in2, Signal in3, int t, int m, int b){
		super(in1, in2, in3, t, m, b);
		size=3;
		type = GateType.UNI;
	}
	
	@Override
	public void toggleGate(){
		if(type==GateType.XNOR || type==GateType.UNI){
			type=GateType.AND;
		}
		else if(type==GateType.AND) {
			type=GateType.NOR;
		}
		else if(type==GateType.NOR) {
			type=GateType.XOR;
		}
		else{
			type=GateType.XNOR;
		}
	}
	
	@Override
	public boolean isUNI(){return true;}

	@Override
	void update(){
		if (type == GateType.AND){
			if (size==2){
				if (input1.getValue()==1 && input2.getValue()==1){
					output.setValue(1);
				}
				else{
					output.setValue(0);
				}
			}
			else{
				if (input1.getValue()==1 && input2.getValue()==1 && input3.getValue()==1){
					output.setValue(1);
				}
				else{
					output.setValue(0);
				}
			}
		}
		if (type == GateType.NAND){
			if (size==2){
				if (input1.getValue()==1 && input2.getValue()==1){
					output.setValue(0);
				}
				else{
					output.setValue(1);
				}
			}
			if (input1.getValue()==1 && input2.getValue()==1 && input3.getValue()==1){
				output.setValue(0);
			}
			else{
				output.setValue(1);
			}
		}
		if (type ==GateType.OR){
			if (size==2){
				if (input1.getValue()==1 || input2.getValue()==1){
					output.setValue(1);
				}
				else{
					output.setValue(0);
				}
			}
			if (size==3){
				if (input1.getValue()==0 || input2.getValue()==1 || input3.getValue()==1){
					output.setValue(1);
				}
				else{
					output.setValue(0);
				}
			}
		}
		if (type == GateType.NOR){
			if (size==2){
				if (input1.getValue()==1 || input2.getValue()==1){
					output.setValue(0);
				}
				else{
					output.setValue(1);
				}
			}
			if (size==3){
				if (input1.getValue()==0 || input2.getValue()==1 || input3.getValue()==1){
					output.setValue(0);
				}
				else{
					output.setValue(1);
				}
			}
		}
		if ( type == GateType.XOR){
			if ((input1.getValue()==1 && input2.getValue()==0) || ((input1.getValue()==0 && input2.getValue()==1))){
				output.setValue(1);
			}
			else{
				output.setValue(0);
			}
		}
		if (type == GateType.XNOR){
			if ((input1.getValue()==1 && input2.getValue()==0) || ((input1.getValue()==0 && input2.getValue()==1))){
				output.setValue(0);
			}
			else{
				output.setValue(1);
			}
			
		}
		
	}

}
