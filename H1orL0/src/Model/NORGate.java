package Model;


public class NORGate extends Gate {
		NORGate(Signal in1, Signal in2, int t, int b){
			super(in1, in2, t, b);
			size=2;
			type = GateType.NOR;
			
		}
		NORGate(Signal in1, Signal in2, Signal in3, int t, int m, int b){
			super(in1, in2, in3, t, m, b);
			size=3;
			type = GateType.NOR;
		}
		
		
		public void update(){
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
}

