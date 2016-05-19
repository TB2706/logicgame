package Controller;
import Controller.*;

import java.util.ArrayList;
import java.util.List;

public class LevelSelect implements State {
	
	
	
	public LevelSelect(){}
	
	
	public void select(int n){StateController.createLevel(n);}
	
	public void changeState(){StateController.setState();}
}
