package Model;

public class Signal {
	private int value;
	private boolean canChange=false;
	
	Signal(int n){
		if (n==2){
			canChange=true;
		}
		value=n;
	}
	
	public boolean canChange(){return canChange;}
	
	public void setValue(int i){value=i;}

	public int getValue() {return value;}
}
