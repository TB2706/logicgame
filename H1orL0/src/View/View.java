package View;

import Model.*;
import Controller.*;

import java.awt.*;

import javax.swing.*;

import java.awt.Color;

public class View
{   	
	
	private ViewFrame frame= new ViewFrame();
	
	
	public View() {
	//  frame.state = state;
    //    frame.PaintViews();
        frame.setTitle("H1 OR L0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true); 
       
	}
	
	public void setView(String state){
		frame.setView(state);
	}
	
	public void setVisible(boolean set){
		frame.setVisible(set);
	}
	
	
}
