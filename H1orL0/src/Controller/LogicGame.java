package Controller;


import java.awt.Toolkit;
import java.awt.Dimension;

public class LogicGame {
	
	
	//Get Universal dimensions
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static int locX =screenSize.width/8;
	public static int locY=screenSize.height/8;
	public static int width= screenSize.width - 2*locX;
	public static int height= screenSize.height - 2*locY;
	
	private static StateController controller;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		controller = new StateController();
		

	}

}
