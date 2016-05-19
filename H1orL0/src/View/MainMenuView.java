package View;


import Controller.*;

import java.awt.FontMetrics;

import javax.swing.JPanel;

import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class MainMenuView extends JPanel
{	
	//get screen dimensions

	int locX = LogicGame.locX;
	int locY = LogicGame.locY;
	int sizeX = LogicGame.width;
	int sizeY = LogicGame.height;
			

	private Font times;
	

	private int time=0;
	private int[] pressed= new int[4];
	

	Timer timer = new Timer(100, new ActionListener() {
		   
	    public void actionPerformed(ActionEvent ae) {
	    	if(time>0)
	    		time--;
	    	else{
	    		time = 20;
	    	}
	    	repaint();
	    }
	});	
	
	
	private RoundRectangle2D[] menuButton = new RoundRectangle2D[4]; 
	private boolean[] insideMenuButton = new boolean[4];

	
	
	//constructor
	public MainMenuView()
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


		g2.setColor(Color.DARK_GRAY);
		g2.fill(outsideBorder);
		
		
		//animated logo images
		String image = "files/033-"+String.valueOf(time)+".png";
		Image img = new ImageIcon(image).getImage();
		int imgX=img.getWidth(null);
		int imgY=img.getHeight(null);
		
		g2.drawImage(img,sizeX/2-imgX/2, sizeY/3-imgY/2, imgX, imgY,null);

		
		//draw and fill Menu buttons
		
		for(int i=0; i<pressed.length; i++){
			if(insideMenuButton[i]){pressed[i]=1;}
			else{pressed[i]=0;}
		}
		menuButton[0] = new RoundRectangle2D.Double((sizeX/4)-pressed[0]*(sizeX/150), 2*sizeY/3+pressed[0]*(sizeY/120), sizeX/6, sizeY/12, sizeX/60, sizeY/30);
		menuButton[1] = new RoundRectangle2D.Double(115*sizeX/200-pressed[1]*sizeX/150,2*sizeY/3+pressed[1]*sizeY/120, sizeX/6, sizeY/12, sizeX/60, sizeY/30);
		for(int i=0;i<2;i++){
			g2.setColor(Color.RED.darker());
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
		drawCenteredString("NEW", (int) menuButton[0].getX(), (int) menuButton[0].getY(), (int) menuButton[0].getWidth(),(int) menuButton[0].getHeight(), g2);
		drawCenteredString("LOAD", (int) menuButton[1].getX(), (int) menuButton[1].getY(), (int) menuButton[1].getWidth(),(int) menuButton[1].getHeight(), g2);
	}
	
	
	//Takes dimensions of buttons and draws string in center
	public void drawCenteredString(String s, int x, int y, int w, int h, Graphics g) {
	    FontMetrics fm = g.getFontMetrics();
	    int newX = x + ((w - fm.stringWidth(s)) / 2);
	    int newY = y + (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
	    g.drawString(s, newX, newY);
	  }
	
	//handle mouse clicks
	private class MouseHandler extends MouseAdapter
	{
		
		//Starts a button action to indicated button is pressed
		public void mousePressed(MouseEvent event)
		{
			int x = event.getX();
			int y = event.getY();
			Point2D point = new Point2D.Double(x,y);
			
			for(int i=0;i<2;i++){
				if(menuButton[i].contains(point)){
					insideMenuButton[i] = true;
					repaint();
				}
				else{
					insideMenuButton[i] = false;
				}
			}
		}
		
		//Releasing button after its clicked starts action
		public void mouseReleased(MouseEvent event)
		{
			
			if(insideMenuButton[0] == true){
				insideMenuButton[0] = false;
				StateController.update("NewGameView");
			}
			
			if(insideMenuButton[1] == true){
				insideMenuButton[1] = false;
				StateController.update("LoadGameView");
			}
			
		}
		//if mouse clicked (pressed and released)
		public void mouseClicked(MouseEvent event)
		{
			int x = event.getX();
			int y = event.getY();
			Point2D point = new Point2D.Double(x, y);
			
			if(menuButton[0].contains(point)){
				StateController.update("NewGameView");
			}
			if(menuButton[1].contains(point)){
				StateController.update("LoadGameView");
			}
		}
	}
}
