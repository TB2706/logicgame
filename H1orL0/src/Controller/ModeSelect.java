package Controller;

public class ModeSelect implements State {
	
	private boolean mode=false;
	public ModeSelect(){
	
	};
	
	public void setMode(boolean isGate){mode=isGate;}	
	public boolean getMode(){return mode;}
	
	public void changeState(){
		StateController.setState();
	}
}
