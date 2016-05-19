package View;


import Controller.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Timer;

import Controller.StateController;



public class LoadFileView extends JComponent implements ComponentView {
	
	//get screen dimensions
	int locX = LogicGame.locX;
	int locY = LogicGame.locY;
	int sizeX = LogicGame.width;
	int sizeY = LogicGame.height;
			
	//gate dimension arrays
	int[]dimX = new int[15];
	int[]dimY = new int[15];
	
	private Font times;
	
	public int mode;
	private int time=0;
	
	private RoundRectangle2D[] menuButton =new RoundRectangle2D[4];
	private boolean[] insideMenuButton = new boolean[4];
	private int[] pressed = new int[4];
	
	public boolean isPlayingMode = false;
	
	Timer timer = new Timer(100, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent ae) {
	    	if(time>0)
	    		time--;
	    	else{
	    		time = 20;
	    	}
	    	repaint();
	    }
	});	
	
	//constructor
	public LoadFileView()
	{
		addMouseListener(new MouseHandler());
	}
	
	//graphics
	public void paintComponent(Graphics g)
	{
		timer.start();
		//graphics object
		Graphics2D g2 = (Graphics2D) g;
		
		//fill outside border
		Rectangle2D outsideBorder = new Rectangle2D.Double(0,0,sizeX,sizeY);
		int winX = (int) outsideBorder.getX();
		int winY = (int) outsideBorder.getY();
		
		g2.setColor(Color.DARK_GRAY);
		g2.fill(outsideBorder);
		
		for (int i=0; i<pressed.length; i++){
			if(insideMenuButton[i]){
				pressed[i]=1;
			}
			else{
				pressed[i]=0;
			}
		}
		
		String image = "files/033-"+String.valueOf(time)+".png";
		Image img = new ImageIcon(image).getImage();
		int imgX=img.getWidth(null);
		int imgY=img.getHeight(null);
		
		g2.drawImage(img,winX+sizeX/2-imgX/2, winY+sizeY/3-imgY/2, imgX, imgY,null);

		
		//draw and fill run button
		
		menuButton[0] = new RoundRectangle2D.Double(winX+sizeX/6-pressed[0]*(sizeX/150), winY+2*sizeY/3+pressed[0]*(sizeY/120), sizeX/8, sizeY/15, sizeX/60, sizeY/30);
		menuButton[1] = new RoundRectangle2D.Double(winX+7*sizeX/16-pressed[1]*(sizeX/150), winY+2*sizeY/3+pressed[1]*(sizeY/120), sizeX/8, sizeY/15, sizeX/60, sizeY/30);
		menuButton[2] = new RoundRectangle2D.Double(winX+34*sizeX/48-pressed[2]*(sizeX/150), winY+2*sizeY/3+pressed[2]*(sizeY/120), sizeX/8, sizeY/15, sizeX/60, sizeY/30);
		menuButton[3] = new RoundRectangle2D.Double(winX+sizeX/40-pressed[3]*(sizeX/150), winY+sizeY/40+pressed[3]*(sizeY/120), sizeX/16, sizeY/24, sizeX/60, sizeY/30);
		
		for(int i=0;i<4;i++){
			g2.setColor(Color.CYAN.darker());
			g2.fill(menuButton[i]);
			g2.setColor(Color.black);
			float thickness = 3;
			Stroke oldStroke = g2.getStroke();
			g2.setStroke(new BasicStroke(thickness));
			g2.draw(menuButton[i]);
			g2.setStroke(oldStroke);
			}
		int fontSize= (int) menuButton[0].getWidth(); 
		times = new Font("Monospaced", Font.BOLD, 3*fontSize/20);			
		g2.setFont(times);
		for (int i=0; i<3; i++){
			drawCenteredString(StateController.saveName(i), (int) menuButton[i].getX(), (int) menuButton[i].getY(), (int) menuButton[i].getWidth(),(int) menuButton[i].getHeight(), g2);
		}
		fontSize= (int) menuButton[3].getWidth(); 
		times = new Font("Monospaced", Font.BOLD, 4*fontSize/20);			
		g2.setFont(times);
		drawCenteredString("BACK", (int) menuButton[3].getX(), (int) menuButton[3].getY(), (int) menuButton[3].getWidth(),(int) menuButton[3].getHeight(), g2);
	}
	
	public void drawCenteredString(String s, int x, int y, int w, int h, Graphics g) {
	    FontMetrics fm = g.getFontMetrics();
	    int newX = x + ((w - fm.stringWidth(s)) / 2);
	    int newY = y + (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
	    g.drawString(s, newX, newY);
	  }
	
	//handle mouse clicks
	private class MouseHandler extends MouseAdapter
	{
		public void mousePressed(MouseEvent event)
		{
			int x = event.getX();
			int y = event.getY();
			Point2D point = new Point2D.Double(x,y);
			
			for(int i=0;i<4;i++){
				if(menuButton[i].contains(point)){
					insideMenuButton[i] = true;
					repaint();
				}
				else{
					insideMenuButton[i] = false;
				}
			}
		}
		
		public void mouseReleased(MouseEvent event)
		{
			for (int i=0; i<3; i++){
				if(insideMenuButton[i]){
					insideMenuButton[i] = false;
					if(StateController.saveName(i)!="  --  "){
						StateController.load(StateController.saveName(i));
						StateController.update("ModeSelect");
					}
				}
			}
			if(insideMenuButton[3]){
				insideMenuButton[3]=false;
				StateController.update("MainMenu");
			}
		 
		}
		//if mouse clicked (pressed and released)
		public void mouseClicked(MouseEvent event)
		{
			int x = event.getX();
			int y = event.getY();
			Point2D point = new Point2D.Double(x, y);
			
			for(int i=0;i<3.;i++){
				if(menuButton[i].contains(point)){
					isPlayingMode = true;
					if(StateController.saveName(i)!="  --  "){
						StateController.load(StateController.saveName(i));
						StateController.update("ModeSelect");
					}
				}
			}
			if(menuButton[3].contains(point)){
				StateController.update("MainMenu");
			}
	}
	}
}

