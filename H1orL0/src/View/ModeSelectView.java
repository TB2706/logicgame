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

public class ModeSelectView extends JPanel implements ComponentView
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
	
	public int mode;
	private int time=0;
	private int[] pressed = new int[4];
	
	private boolean openDialog=false;
	private RoundRectangle2D[] menuButton = new RoundRectangle2D[4]; 
	private boolean[] insideMenuButton = new boolean[4];
	private RoundRectangle2D.Double dialog;


	
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
	public ModeSelectView()
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
		
		String image = "files/033-"+String.valueOf(time)+".png";
		Image img = new ImageIcon(image).getImage();
		int imgX=img.getWidth(null);
		int imgY=img.getHeight(null);
		
		g2.drawImage(img,winX+sizeX/2-imgX/2, winY+sizeY/3-imgY/2, imgX, imgY,null);

		
		
		for(int i=0; i<pressed.length; i++){
			if(insideMenuButton[i]){pressed[i]=1;}
			else{pressed[i]=0;}
		}
		
		//draw and fill run button
			
		menuButton[0] = new RoundRectangle2D.Double(sizeX/4-pressed[0]*sizeX/150, winY+2*sizeY/3+pressed[0]*sizeY/120, sizeX/6, sizeY/12, sizeX/60, sizeY/30);
		menuButton[1] = new RoundRectangle2D.Double(115*sizeX/200-pressed[1]*sizeX/150, winY+2*sizeY/3+pressed[1]*sizeY/120, sizeX/6, sizeY/12, sizeX/60, sizeY/30);
		menuButton[2] = new RoundRectangle2D.Double(sizeX/40-pressed[2]*sizeX/150, winY+sizeY/40+pressed[2]*sizeY/120, sizeX/16, sizeY/24, sizeX/60, sizeY/30);
		for(int i=0;i<3;i++){			
			g2.setColor(Color.RED.darker());
			if(i==1){g2.setColor(Color.CYAN.darker());}
			g2.fill(menuButton[i]);
			g2.setColor(Color.BLACK);
			float thickness = 3;
			Stroke oldStroke = g2.getStroke();
			g2.setStroke(new BasicStroke(thickness));
			g2.draw(menuButton[i]);
			g2.setStroke(oldStroke);
		}
		
		int fontSize= (int) menuButton[1].getWidth();
		times = new Font("Monospaced", Font.BOLD, fontSize/10);	
		g2.setFont(times);
		drawCenteredString("INPUT SEL", (int) menuButton[0].getX(), (int) menuButton[0].getY(), (int) menuButton[0].getWidth(),(int) menuButton[0].getHeight(), g2);
		//g2.drawString("2", locX+sizeX/4, locY + 68*sizeY/100);
		//g2.drawString("3", locX+46*sizeX/100, locY + 68*sizeY/100);
		drawCenteredString("GATE SEL", (int) menuButton[1].getX(), (int) menuButton[1].getY(), (int) menuButton[1].getWidth(),(int) menuButton[1].getHeight(), g2);
		fontSize= (int) menuButton[2].getWidth();
		times = new Font("Monospaced", Font.BOLD, 3*fontSize/18);			
		g2.setFont(times);
		drawCenteredString("BACK", (int) menuButton[2].getX(), (int) menuButton[2].getY(), (int) menuButton[2].getWidth(),(int) menuButton[2].getHeight(), g2);
	if(openDialog){
		dialog=new RoundRectangle2D.Double(winX + sizeX/3, winY+sizeY/8, sizeX/3, 2*sizeY/3, sizeX/60, sizeY/30);
		g2.setColor(Color.DARK_GRAY.brighter());
		g2.fill(dialog);
		Stroke oldStroke= g2.getStroke();
		g2.setColor(Color.BLACK.darker());
		g2.setStroke(new BasicStroke(6));
		g2.draw(dialog);
		g2.setStroke(oldStroke);
		int digX=(int) dialog.getX();
		int digY=(int) dialog.getY();
		int digH=(int) dialog.getHeight();
		int digW=(int) dialog.getWidth();
		menuButton[3]=new RoundRectangle2D.Double(digX+digW/3-pressed[3]*sizeX/150, digY+13*digH/16+pressed[3]*sizeY/120, digW/3, digH/10, sizeX/60, sizeY/30);
		g2.setColor(Color.RED.darker());
		if(StateController.getMode()){g2.setColor(Color.CYAN.darker());}
		g2.fill(menuButton[3]);
		g2.setColor(Color.black);
		long thickness=3;
		g2.setStroke(new BasicStroke(thickness));
		g2.draw(menuButton[3]);
		g2.setStroke(oldStroke);
		fontSize= (int) menuButton[3].getWidth();
		g2.setFont(new Font("Monospaced", Font.BOLD, 3*fontSize/20));
		drawCenteredString("CONTINUE", (int) menuButton[3].getX(), (int) menuButton[3].getY(), (int) menuButton[3].getWidth(),(int) menuButton[3].getHeight(), g2);
		String pic = "files/input.png";
		if (StateController.getMode()){pic="files/gate.png"	;}
		Image id = new ImageIcon(pic).getImage();
		int idX=7*digW/8;
		int idY=digH/2;;
		g2.setColor(Color.gray);
		g2.drawImage(id,digX+digW/2-idX/2, digY+5*digH/12-idY/2, idX, idY,null);
		RoundRectangle2D border = new RoundRectangle2D.Double(digX+digW/2-idX/2-2, digY+5*digH/12-idY/2, idX+4, idY+2,sizeX/60, sizeY/30);
		g2.setStroke(new BasicStroke(8));
		g2.draw(border);
		g2.setStroke(oldStroke);
	}
}
	
	public void drawCenteredString(String s, int x, int y, int w, int h, Graphics g) {
	    FontMetrics fm = g.getFontMetrics();
	    int newX = x + ((w - fm.stringWidth(s)) / 2);
	    int newY = y + (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
	    g.drawString(s, newX, newY);
	  }
	
	//handle mouse clicks
	private class MouseHandler extends MouseAdapter{	
		//if mouse clicked (pressed and released)
		public void mouseClicked(MouseEvent event){
			int x = event.getX();
			int y = event.getY();
			Point2D point = new Point2D.Double(x, y);
								
			if(openDialog){
				if(menuButton[3]!=null){
					if(menuButton[3].contains(point)){
						openDialog=false;
						StateController.update("LevelSelect");
					}
				}
			}
				
			for(int i=0;i<2;i++){
				if(menuButton[i].contains(point) && !openDialog){
					StateController.setMode(i);
					openDialog=true;
					repaint();
				}
			}	
			if(menuButton[2].contains(point) && !openDialog){
					StateController.update("MainMenu");
			}
			
		}
		
		public void mousePressed(MouseEvent event){
			int x = event.getX();
			int y = event.getY();
			Point2D point = new Point2D.Double(x,y);
			
			for(int i=0;i<3;i++){
				if(menuButton[i].contains(point)&& !openDialog){
					insideMenuButton[i] = true;
					repaint();
				}
				else{
					insideMenuButton[i] = false;
				}
			}
			if(openDialog){
				if(menuButton[3].contains(point)){
					insideMenuButton[3] = true;
					repaint();
				}
				else{
					insideMenuButton[3]=false;
				}
			}
			else{
				insideMenuButton[3] = false;
			}
		}
		
		public void mouseReleased(MouseEvent event){
			for(int i=0;i<2;i++){
				if(insideMenuButton[i] == true){
					openDialog = true;
					StateController.setMode(i);
					insideMenuButton[i] = false;
					repaint();
				}
			}
			
			
			
			if(insideMenuButton[2]&& !openDialog){
				insideMenuButton[2]=false;
				StateController.update("MainMenu");
			}
			
			if(openDialog){
				if(insideMenuButton[3]){
					insideMenuButton[3] = false;
					openDialog=false;
					StateController.update("LevelSelect");
				}
			}
		}
		
	}
}
	