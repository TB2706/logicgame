package View;

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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.JPanel;

import Controller.LogicGame;
import Controller.StateController;
//import View.LevelSelectView.MouseHandler;

public class NewGameView extends JPanel implements ComponentView
{	
	//get screen dimensions
	int locX = LogicGame.locX;
	int locY = LogicGame.locY;
	int sizeX = LogicGame.width;
	int sizeY = LogicGame.height;
	String name=null;
	boolean dialogOpen=false;
			
	//gate dimension arrays
	int[]dimX = new int[15];
	int[]dimY = new int[15];
	
	private Font times;
	

	private int time=0;
	
	private RoundRectangle2D.Double dialog;
	private RoundRectangle2D[] menuButton = new RoundRectangle2D[5]; 
	private boolean[] insideMenuButton = new boolean[5];
	private int[] pressed = new int[5];
	private KeyboardHandler listener=new KeyboardHandler(); 
	
	public boolean isPlayingMode = false;
	
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
	
	//constructor
	public NewGameView()
	{
		addMouseListener(new MouseHandler());
		addKeyListener(listener);
		setFocusable(true);
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

	    for (int i=0; i< pressed.length; i++){
	    	if (insideMenuButton[i]){
	    		pressed[i]=1;
	    	}
	    	else{
	    		pressed[i]=0;
	    	}
	    }

		
		
		//Animated Logo
		String image = "files/033-"+String.valueOf(time)+".png";
		Image img = new ImageIcon(image).getImage();
		int imgX=img.getWidth(null);
		int imgY=img.getHeight(null);
		
		g2.drawImage(img,sizeX/2-imgX/2,sizeY/3-imgY/2, imgX, imgY,null);
		this.requestFocusInWindow();
	
		
		//draw and fill MenuButtons
		dialog=new RoundRectangle2D.Double(winX + sizeX/3, winY+sizeY/8, sizeX/3, 2*sizeY/3, sizeX/60, sizeY/30);
		if(name==null || StateController.invalidSave(name)){
			pressed[1]=0;
		}
		menuButton[0] = new RoundRectangle2D.Double(winX + 3*sizeX/8-pressed[0]*(sizeX/150), winY+5*sizeY/8+pressed[0]*(sizeY/120), sizeX/4, sizeY/15, 0, 0);
		menuButton[1] = new RoundRectangle2D.Double(winX +  7*sizeX/16-pressed[1]*(sizeX/150), winY+13*sizeY/16+pressed[1]*(sizeY/120), sizeX/8, sizeY/15, sizeX/60, sizeY/30);
		menuButton[2] = new RoundRectangle2D.Double(winX + 9*sizeX/16-pressed[2]*(sizeX/150), winY+13*sizeY/16+pressed[2]*(sizeY/120), sizeX/8, sizeY/15, sizeX/60, sizeY/30);
		int digX=(int) dialog.getX();
		int digY=(int) dialog.getY();
		int digH=(int) dialog.getHeight();
		int digW=(int) dialog.getWidth();
		menuButton[4]=new RoundRectangle2D.Double(digX+3*digW/8-pressed[4]*(sizeX/150), digY+13*digH/16+pressed[4]*(sizeY/120), digW/4, digH/10, sizeX/60, sizeY/30);
		
		g2.setColor(Color.WHITE);
		g2.fill(menuButton[0]);
		g2.setColor(Color.RED.darker());
	//	g2.fill(menuButton[2]);
		g2.setColor(Color.RED.darker());
		g2.setColor(Color.DARK_GRAY.brighter());
		if(name!=null && !StateController.invalidSave(name)){
			g2.setColor(Color.RED.darker());
		}
		g2.fill(menuButton[1]);
		g2.setColor(Color.black);
		float thickness = 4;
		Stroke oldStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(thickness));
		g2.draw(menuButton[0]);
		g2.draw(menuButton[1]);
		//g2.draw(menuButton[2]);
		g2.setStroke(oldStroke);
		int fontSize= (int) menuButton[1].getWidth();
		times = new Font("Monospaced", Font.BOLD, 3*fontSize/20);
		g2.setFont(times);
		if(name!=null){
			drawCenteredString( name.toUpperCase() , (int) menuButton[0].getX(), (int) menuButton[0].getY(), (int) menuButton[0].getWidth(),(int) menuButton[0].getHeight(), g2);
		}
	

	
		drawCenteredString("SUBMIT", (int) menuButton[1].getX(), (int) menuButton[1].getY(), (int) menuButton[1].getWidth(),(int) menuButton[1].getHeight(), g2);
	//	drawCenteredString("RESET", (int) menuButton[2].getX(), (int) menuButton[2].getY(), (int) menuButton[2].getWidth(),(int) menuButton[2].getHeight(), g2);
		
		//"BACK" button
		menuButton[3] = new RoundRectangle2D.Double(winX+sizeX/40-pressed[3]*(sizeX/150), winY+sizeY/40+pressed[3]*(sizeY/120), sizeX/16, sizeY/24, sizeX/60, sizeY/30);
		g2.setColor(Color.RED.darker());
		g2.fill(menuButton[3]);
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(thickness));
		g2.draw(menuButton[3]);
		g2.setStroke(oldStroke);
		fontSize= (int) menuButton[3].getWidth();
		g2.setFont(new Font("Monospaced", Font.BOLD, 4*fontSize/20));
		drawCenteredString("BACK", (int) menuButton[3].getX(), (int) menuButton[3].getY(), (int) menuButton[3].getWidth(),(int) menuButton[3].getHeight(), g2);
		
		//Draw and fill dialog Box if applicable
		if(dialogOpen){
			g2.setColor(Color.DARK_GRAY);
			g2.fill(dialog);
			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke(5));
			g2.draw(dialog);
			g2.setColor(Color.RED.darker());
			g2.fill(menuButton[4]);
			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke(thickness));
			g2.draw(menuButton[4]);
			g2.setStroke(oldStroke);
			fontSize= (int) menuButton[4].getWidth();
			g2.setFont(new Font("Monospaced", Font.BOLD,3*fontSize/20));
			drawCenteredString("CLOSE", (int) menuButton[4].getX(), (int) menuButton[4].getY(), (int) menuButton[4].getWidth(),(int) menuButton[4].getHeight(), g2);
			String pic = "files/newgame.png";
			Image id = new ImageIcon(pic).getImage();
			int idX=3*digW/4;
			int idY=digH/2;
			g2.drawImage(id,digX+digW/2-idX/2, digY+5*digH/12-idY/2, idX, idY,null);
			RoundRectangle2D.Double border=new RoundRectangle2D.Double(digX+digW/2-idX/2-1, digY+5*digH/12-idY/2,idX+1, idY, sizeX/60, sizeY/30);
			g2.setColor(Color.gray);
			g2.setStroke(new BasicStroke(8));
			g2.draw(border);

		}
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
		//Starts button action if pressed
		public void mousePressed(MouseEvent event)
		{
			int x = event.getX();
			int y = event.getY();
			Point2D point = new Point2D.Double(x,y);
			
			for(int i=0;i<5;i++){
				if(menuButton[i].contains(point) && !dialogOpen ){
					insideMenuButton[i] = true;
					repaint();
				}
				else if (menuButton[4].contains(point)){
					insideMenuButton[4] = true;
				}
				else{
					insideMenuButton[i] =false;
				}
			}
		}
		
		//Starts action if mouse is released after the button is pressed
		public void mouseReleased(MouseEvent event)
		{
			for(int i=0;i<5;i++){
				if(insideMenuButton[i] == true){
					insideMenuButton[i] = false;
					repaint();
				}
			}
			if (insideMenuButton[3]){
				StateController.update("MainMenu");
			}
			if(insideMenuButton[1]){
				if(name!=null&& name!=""){
					if(!StateController.invalidSave(name.toUpperCase())){
						StateController.createGame(name.toUpperCase());
						StateController.update("ModeSelect");
						}
					else{
						dialogOpen=true;
					}
			 }
			}
			
			if(insideMenuButton[4]){
				StateController.update("ModeSelect");
			}
		}
		//if mouse clicked (pressed and released)
		public void mouseClicked(MouseEvent event)
		{
			int x = event.getX();
			int y = event.getY();
			Point2D point = new Point2D.Double(x, y);
			
			if (dialogOpen==true){
				if (menuButton[4].contains(point)){
					dialogOpen=false;
					repaint();
				}
			}
			
			if (menuButton[1].contains(point) && !dialogOpen){
				if(name!=null&&name!=""){
					if(!StateController.invalidSave(name.toUpperCase())){
						 StateController.createGame(name.toUpperCase());
						 StateController.update("ModeSelect");
					}
					else{
						dialogOpen=true;
						repaint();
					}
				}
			}
			/*if (menuButton[2].contains(point) && !dialogOpen){
				StateController.update(6);
			}*/
			if (menuButton[3].contains(point) && !dialogOpen){
				StateController.update("MainMenu");
			}
		}
	}
	
	//Listens to keyboard for valid input
	private class KeyboardHandler implements KeyListener{
		public void keyPressed(KeyEvent e) { }
		public void keyReleased(KeyEvent e) { }
		public void keyTyped(KeyEvent e) {
			 char a= e.getKeyChar();
			 if(((64<a)&&(a<91)) || ((96<a)&&(a<123))){
				 if (name==null){name=""+ a;}
				 else{name += a;}				 
				 repaint();
			 }
			 else if (a==8 && !name.equals("")){
				 name=name.substring(0,name.length()-1);
				 repaint();
			 }
		}
	}
}

