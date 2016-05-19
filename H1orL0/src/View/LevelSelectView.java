package View;

import Model.*;
import Controller.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Timer;

public class LevelSelectView extends JComponent implements ComponentView
{	
	//get screen dimensions
	int locX = LogicGame.locX;
	int locY = LogicGame.locY;
	int sizeX = LogicGame.width;
	int sizeY = LogicGame.height;
			
	//gate dimension arrays
	int[]dimX = new int[15];
	int[]dimY = new int[15];
	
	private Font times;
	private boolean dialogOpen=false;
	private int tutLevel;
	
	public int mode;
	private int[] pressed = new int[22];
	
	private RoundRectangle2D[] menuButton = new RoundRectangle2D[22]; 
	private boolean[] insideMenuButton = new boolean[22];
	private RoundRectangle2D.Double dialog;

//	public boolean isPlayingMode = false;
	

	
	//constructor
	public LevelSelectView()
	{
		addMouseListener(new MouseHandler());
	}
	
	//graphics
	public void paintComponent(Graphics g)
	{
		//timer.start();
		//graphics object
		Graphics2D g2 = (Graphics2D) g;
		
		//fill outside border
		Rectangle2D outsideBorder = new Rectangle2D.Double(0,0,sizeX,sizeY);
		int winX= (int) outsideBorder.getX();
		int winY = (int) outsideBorder.getY();
		g2.setColor(Color.DARK_GRAY);
		g2.fill(outsideBorder);
		
		//String image = "033-"+String.valueOf(time)+".png";
		//Image img = new ImageIcon(image).getImage();
		//g2.drawImage(img,locX-sizeX/12, locY-sizeY/3, 3504/(sizeX/400),2550/(sizeY/200),null);
		int fontSize= (int) outsideBorder.getWidth();
		times = new Font("TimesRoman", Font.BOLD, fontSize/20);
		g2.setFont(times);
		String mode="INPUT";
		if (StateController.getMode()){
			mode="GATE";
		}
		g2.setColor(Color.WHITE);
		String title = mode + " SELECT MODE";
		drawCenteredString(title, (int) outsideBorder.getX(), (int) outsideBorder.getY(), (int) outsideBorder.getWidth(),((int) outsideBorder.getHeight())/4, g2);

		
		times = new Font("Monospaced", Font.BOLD, fontSize/50);
		g2.setFont(times);
		
		for (int i=0; i<pressed.length; i++){
			if (insideMenuButton[i]){
				pressed[i]=1;
			}
			else{
				pressed[i]=0;
			}
		}
		
		//draw and fill run button
		for(int j=0;j<5;j++){
			for(int i=0;i<4;i++){
				menuButton[i+4*j] = new RoundRectangle2D.Double(((2*i + 1)*sizeX/9)-pressed[i+4*j]*(sizeX/150), ((2*j+1)*sizeY/15)+3*sizeY/16+pressed[i+4*j]*(sizeY/120), sizeX/9, sizeY/15, sizeX/60, sizeY/30);
					
			if(!StateController.notPassed(i+4*j)){
					if(StateController.getMode()){
						g2.setColor(Color.CYAN.darker());
					}
					else{
						g2.setColor(Color.RED.darker());
					}
				}
			else{
				g2.setColor(Color.gray.darker());
			}
				g2.fill(menuButton[i+4*j]);
				g2.setColor(Color.black);
				float thickness = 3;
				Stroke oldStroke = g2.getStroke();
				g2.setStroke(new BasicStroke(thickness));
				g2.draw(menuButton[i+4*j]);
				if(!StateController.notPassed(i+4*j)){g2.setColor(Color.WHITE);}
				g2.setStroke(oldStroke);
				drawCenteredString(String.valueOf((i+4*j)+1), (int) menuButton[i+4*j].getX(), (int) menuButton[i+4*j].getY(), (int) menuButton[i+4*j].getWidth(),(int) menuButton[i+4*j].getHeight(), g2);
			}
		}
		menuButton[20] = new RoundRectangle2D.Double(winX+sizeX/40-pressed[20]*(sizeX/150), winY+sizeY/40+pressed[20]*(sizeY/120), sizeX/16, sizeY/24, sizeX/60, sizeY/30);
		g2.setColor(Color.RED.darker());
		if(StateController.getMode()){g2.setColor(Color.CYAN.darker());}
		g2.fill(menuButton[20]);
		g2.setColor(Color.black);
		
		float thickness = 3;
		Stroke oldStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(thickness));
		g2.draw(menuButton[20]);
		g2.setStroke(oldStroke);
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Monospaced", Font.BOLD, fontSize/90));
		drawCenteredString("BACK", (int) menuButton[20].getX(), (int) menuButton[20].getY(), (int) menuButton[20].getWidth(),(int) menuButton[20].getHeight(), g2);
		
		if(dialogOpen){
			dialog=new RoundRectangle2D.Double(winX + sizeX/6, winY+sizeY/15, 2*sizeX/3, 5*sizeY/6, sizeX/60, sizeY/30);
			g2.setColor(Color.DARK_GRAY.brighter());
			g2.fill(dialog);
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(7));
			g2.draw(dialog);
			g2.setStroke(oldStroke);
			int digX=(int) dialog.getX();
			int digY=(int) dialog.getY();
			int digH=(int) dialog.getHeight();
			int digW=(int) dialog.getWidth();
			menuButton[21]=new RoundRectangle2D.Double(digX+3*digW/8-pressed[21]*(sizeX/150), digY+13*digH/16+pressed[21]*(sizeY/120), digW/4, digH/10, sizeX/60, sizeY/30);
			g2.setColor(Color.RED.darker());
			g2.fill(menuButton[21]);
			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke(thickness));
			g2.draw(menuButton[21]);
			g2.setStroke(oldStroke);
			g2.setFont(new Font("Monospaced", Font.BOLD, fontSize/60));
			drawCenteredString("CONTINUE", (int) menuButton[21].getX(), (int) menuButton[21].getY(), (int) menuButton[21].getWidth(),(int) menuButton[21].getHeight(), g2);
			int i=0;
			while(i!=tutLevel){
				i++;
			}
			String pic = "files/" + i + "Gate.png";
			Image id = new ImageIcon(pic).getImage();
			int idX=7*digW/8;
			int idY=digH/2;
			
			g2.drawImage(id,digX+digW/2-idX/2, digY+9*digH/24-idY/2, idX, idY,null);
		}
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
			
			if (dialogOpen && menuButton[21].contains(point)){
				insideMenuButton[21] = true;
				repaint();
			}
			
			for(int i=0;i<21;i++){
				if(menuButton[i].contains(point) && !dialogOpen){
					insideMenuButton[i] = true;
					repaint();
				}
				else{
					insideMenuButton[i] = false;
					repaint();
				}
			}
		}
		
		public void mouseReleased(MouseEvent event)
		{
			int tmp=6;
			if(StateController.getMode()){
				tmp=0;
			}
			
			if(dialogOpen && insideMenuButton[21]){
				dialogOpen=false;
				insideMenuButton[21]=false;
				StateController.createLevel(tutLevel);
				StateController.update("PlayingMode");
			}


			for(int i=0; i<tmp; i++){
				if(insideMenuButton[i] && !dialogOpen){
					if(!StateController.notPassed(i)){
						dialogOpen=true;
						tutLevel=i;
					}
				}
				insideMenuButton[i]=false;
				repaint();
			}
			for(int i=tmp; i<20; i++){
				if(insideMenuButton[i] && !dialogOpen){
					if(!StateController.notPassed(i)){
						StateController.createLevel(i);
						StateController.update("PlayingMode");
					}
					insideMenuButton[i]=false;				
				}
				repaint();
			}
			
			if(insideMenuButton[20] && !dialogOpen){
				insideMenuButton[20]=false;
				StateController.update("ModeSelect");
			}
			}
		}
		//if mouse clicked (pressed and released)
		public void mouseClicked(MouseEvent event)
		{
			int x = event.getX();
			int y = event.getY();
			Point2D point = new Point2D.Double(x, y);
			int tmp=6;
			if(StateController.getMode()){
				tmp=0;
			}
			if (dialogOpen){
				if (menuButton[21].contains(point)){
					dialogOpen=false;
					StateController.createLevel(tutLevel);
					StateController.update("PlayingMode");					
				}
			}
			for(int i=0; i<tmp; i++){
				if(menuButton[i].contains(point) && !dialogOpen){
					if(!StateController.notPassed(i)){
						dialogOpen=true;
						tutLevel=i;
					}
					
				}
			}
			
			for(int i=tmp;i<20;i++){
				if(menuButton[i].contains(point) && !dialogOpen){
					if(!StateController.notPassed(i)){
						StateController.createLevel(i);
						StateController.update("PlayingMode");
					}
					
				}
			}
			if(menuButton[20].contains(point) && !dialogOpen){
				StateController.update("ModeSelect");
			}
			
			
		}
}

