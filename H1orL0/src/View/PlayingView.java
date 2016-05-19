package View;

import Model.*;

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

import Controller.LogicGame;
import Controller.StateController;

import javax.swing.JPanel;

/*
 * Playing View creates interactive buttons and circuit to play Level
 */

class PlayingView extends JPanel implements ComponentView
{	
	//get screen dimensions
	private int locX = LogicGame.locX;
	private int locY = LogicGame.locY;
	private int sizeX = LogicGame.width;
	private int sizeY = LogicGame.height;
			
	//gate dimension arrays
	private int[]dimX = new int[20];
	private int[]dimY = new int[20];
	
	//gate path arrays	
	private Path2D[]gatePath = new Path2D[20];
	
	//signal path array
	private Path2D[]signalPath = new Path2D[20];
	
	//gate upper left coordinates
	private int gateX;
	private int gateY;
	
	//array of input circle dimensions
	private int[]inputCircleX = new int[8];
	private int[]inputCircleY = new int[8];
	private int[]inputCircleLen = new int[8];
	private int[]inputCircleWid = new int[8];
	
	//array of output circle dimensions
	private	int[]outputCircleX = new int[3];
	private	int[]outputCircleY = new int[3];
	private int[]outputCircleLen = new int[3];
	private int[]outputCircleWid = new int[3];
	
	//array of input rectangle dimensions
	private int[]inputRectX = new int[8];
	private	int[]inputRectY = new int[8];
	
	//array of output rectangle dimensions
	private int[]outputRectX = new int[3];
	private int[]outputRectY = new int[3];
	
	//array of signal rectangles
	private int[]signalRect1X = new int[15];
	private int[]signalRect1Y = new int[15];
	private int[]signalRect2X = new int[15];
	private int[]signalRect2Y = new int[15];
	private int[]signalRect3X = new int[15];
	private int[]signalRect3Y = new int[15];
	
	private int[]signalRect2Len = new int[15];
	
	private int playX;
	private	int playY;
	private int playW;
	private int playH;
	
	//gate scaling factor
	private int s = sizeX/500; 
	
	//circle radius
	private	int radius = (3*s);
				
	//input and output circle arrays
	private Ellipse2D[]inputCircle = new Ellipse2D[8];
	private Ellipse2D[]outputCircle = new Ellipse2D[3];
	
	//border for run button
	private	RoundRectangle2D runButtonBorder;
	
	
	private int[] pressedMenu=new int[4];
	private int pressedPause;
	private int pressedRun;
	
	//Pause menu
	private boolean isPaused;
	private boolean timeUp;
	private boolean insidePauseButton=false;
	private RoundRectangle2D.Double pauseMenu;
	private RoundRectangle2D.Double pauseButton;
	private RoundRectangle2D[] menuButton= new RoundRectangle2D.Double[4];
	private boolean[] insideMenuButton= new boolean[4];
	
	//input and output value arrays
	private int[] gateOutputVal = new int[8];
	
	//variable to hold time limit of level
	int time = StateController.getTime();
	
	//variable to hold time of simulation
	int simTime = 0;
	
	//variable to hold time to display success/unsuccessful message
	int endTime = 10;
	
	
	
	//bools representing in simulation mode or if timer runs out
	boolean isSimulation = false;
	boolean isTimeUp = false;
	
	//boolean representing if each stage is finished sim animations
	boolean[] isStageDone = new boolean[4];
	
	//simulation finished for all signals
	boolean isFinished = false;
	boolean hasSucceeded = false;
	
	//Timer to display the time left top complete the level
	
	Timer timer = new Timer(1000, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent ae) {
	    	if(time>0 && !isSimulation){
	    		if(!isPaused){
	    			time--;	        
	    			repaint();
	    		}
	        }
	    	else{
	    		isTimeUp = true;
	    	}
	    }
	});	
	
	int y;
	
	//simulation timer object
	Timer simTimer = new Timer(100, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent ae) {
	    	if(isStageDone[0] == false || isStageDone[StateController.getNumLevels()-1] == true){
	    		y = 11;
	    	}
	    	else{
	    		y = 31;
	    	}
	    	if(simTime<y&& isSimulation == true){
	    		simTime++;	        
	        	repaint();
	        }
	    	else if(isStageDone[0] == false){
	    		isStageDone[0] = true;
	    		simTime = 0;
	    		repaint();
	    	}
	    	else if(StateController.getNumLevels() > 1 && isStageDone[1] == false){
	    		isStageDone[1] = true;
	    		simTime = 0;
	    		repaint();
	    	}
	    	else if(StateController.getNumLevels() > 2 && isStageDone[2] == false){
	    		isStageDone[2] = true;
	    		simTime = 0;
	    		repaint();
	    	}
	    	else{
	    		isFinished = true;
	    		repaint();
	    	}
	    }
	});	
	
	//timer used to count time to display successful message at the end of simulation
	Timer endTimer = new Timer(100, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent ae) {
	    	if(endTime>0){
	    		endTime--;
	    		repaint();
	        }
	    	else{
	    		isPaused=true;
	    		timeUp=true;
	    		endTimer.stop();
	    		repaint();
	    	}
	    }
	    
	});	
	
	//Run button and timer font
	Font times;
	
	//boolean for mouse click inside run button
	boolean insideRun = false;
	
	//constructor
	public PlayingView()
	{
		addMouseListener(new MouseHandler());
	}
	
	//graphics
	public void paintComponent(Graphics g)
	{
		//graphics object
		Graphics2D g2 = (Graphics2D) g;
		
		//If low resolution, increase scale
	
		//fill outside border
		Rectangle2D outsideBorder = new Rectangle2D.Double(0,0,sizeX,sizeY);
		g2.setColor(Color.DARK_GRAY);
		g2.fill(outsideBorder);
		int fontSize= (int) outsideBorder.getWidth();

		
		//Button action 
		for (int i=0; i<4; i++){
			if(insideMenuButton[i] && isPaused){
				pressedMenu[i]=1;
			}
			else{
				pressedMenu[i]=0;
			}
		}
		
		if(insidePauseButton && !isPaused){
			pressedPause=1;
			}
		else{
			pressedPause=0;
		}
		
		if (insideRun && !isPaused){
			pressedRun=1;
		}
		else{
			pressedRun=0;
		}
		
		//draw and fill insider game border
		RoundRectangle2D gameBorder = new RoundRectangle2D.Double(sizeX/20, sizeY/8, 9*sizeX/10, 3*sizeY/4, sizeX/30, sizeY/15);
		g2.setColor(Color.GRAY);
		g2.fill(gameBorder);
		playX=(int) gameBorder.getX();
		playY=(int) gameBorder.getY();
		playW=(int) gameBorder.getWidth();
		playH=(int) gameBorder.getHeight();
		
		
		//draw and fill run button
		runButtonBorder = new RoundRectangle2D.Double((playX+3*playW/4)-sizeX/16-pressedRun*(sizeX/150), ((playY+playH)+(sizeY-3*sizeY/30))/2+pressedRun*(sizeY/120), sizeX/8, sizeY/15, sizeX/60, sizeY/30);
		g2.setColor(Color.RED.darker());
		if(StateController.getMode()){g2.setColor(Color.CYAN.darker());}
		g2.fill(runButtonBorder);
		g2.setColor(Color.black);
		float thickness = 3;
		Stroke oldStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(thickness));
		g2.draw(runButtonBorder);
		g2.setStroke(oldStroke);
		
		
		
		//draw and fill timer border
		RoundRectangle2D timerBorder = new RoundRectangle2D.Double(playX+playW/4-sizeX/16,((playY+playH)+(sizeY-3*sizeY/30))/2, sizeX/8, sizeY/15, sizeX/60, sizeY/30);
		g2.setColor(Color.LIGHT_GRAY);
		g2.fill(timerBorder);
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(thickness));
		g2.draw(timerBorder);
		g2.setStroke(oldStroke);;
		
		
		//display title string
		g2.setColor(Color.black);
		times = new Font("Monospaced", Font.BOLD, fontSize/20);
		g2.setFont(times);
		String title = "LEVEL "+ String.valueOf(StateController.getLevel()+1);
		drawCenteredString(title, (int) outsideBorder.getX(), (int) outsideBorder.getY(), (int) outsideBorder.getWidth(),playY, g2);
		times = new Font("Monospaced", Font.BOLD, fontSize/40);
		g2.setFont(times);
		
		//start playing timer
		timer.start();
		
		//determine and display string showing current time left
		String displayTime = getTime();
		drawCenteredString(displayTime, (int) timerBorder.getX(), (int) timerBorder.getY(), (int) timerBorder.getWidth(),(int) timerBorder.getHeight(), g2);
				
		//print RUN on run button
		drawCenteredString("RUN", (int) runButtonBorder.getX(), (int) runButtonBorder.getY(), (int) runButtonBorder.getWidth(),(int) runButtonBorder.getHeight(), g2);
			
				
		//variable to count number of gates currently built
		int gateNum=0;
		
		//set dimensions for each gate
      	setGateDimensions();
      	
      	//set dimensions for each signal
       	setSignalDimensions();
       	
       	//variables to index signals in each stage
       	int signalNum1 = StateController.getNumInputs();
       	int signalNum2 = StateController.getNumInputs()+StateController.getNumGates(0);
       	int rect1Len;
       	
       	//draw and fill signal paths
       	if(StateController.getNumLevels() == 2){
       		for(int i=0;i<2*(StateController.getNumGates(1));i++,signalNum1++){
       			rect1Len = signalRect2X[signalNum1]-signalRect1X[signalNum1];
       			if(i%2 == 1)
       				rect1Len = rect1Len + 3*s;
       			g2.setColor(Color.black);
       			g2.draw(signalPath[signalNum1]);
       			if(StateController.getSignal(signalNum1) == 0){
       				g2.setColor(Color.cyan);
       			}
       			else{
       				g2.setColor(Color.red);
       			}
       			if(isSimulation == true){
       			 	if(isStageDone[0] == true && isStageDone[StateController.getNumLevels()-1] == false){
       					if(simTime < 11){
       						g2.fillRect(signalRect1X[signalNum1], signalRect1Y[signalNum1], (simTime*rect1Len)/11,3*s);
       					}
       					else if(simTime < 21){
       						g2.fillRect(signalRect1X[signalNum1], signalRect1Y[signalNum1], rect1Len,3*s);
       						if(i%2 == 0)
       							g2.fillRect(signalRect2X[signalNum1], signalRect2Y[signalNum1], 3*s, ((simTime-10)*signalRect2Len[signalNum1])/11);
       						else
       							g2.fillRect(signalRect2X[signalNum1], signalRect2Y[signalNum1], 3*s, ((simTime-10)*signalRect2Len[signalNum1])/11);
       					}
       					else{
       						g2.fillRect(signalRect1X[signalNum1], signalRect1Y[signalNum1], rect1Len,3*s);
       						if(i%2 == 0)
       							g2.fillRect(signalRect2X[signalNum1], signalRect2Y[signalNum1], 3*s, signalRect2Len[signalNum1]);
       						else
       							g2.fillRect(signalRect2X[signalNum1], signalRect2Y[signalNum1], 3*s, signalRect2Len[signalNum1]);
       						g2.fillRect(signalRect3X[signalNum1], signalRect3Y[signalNum1], playW/7*(simTime-20)/10, 3*s);
       					}
       				}
       				else if(isStageDone[1] == true){
       					g2.fillRect(signalRect1X[signalNum1], signalRect1Y[signalNum1], rect1Len,3*s);
       					if(i%2 == 0)
       						g2.fillRect(signalRect2X[signalNum1], signalRect2Y[signalNum1], 3*s, signalRect2Len[signalNum1]);
       					else
       						g2.fillRect(signalRect2X[signalNum1], signalRect2Y[signalNum1], 3*s, signalRect2Len[signalNum1]);
       					g2.fillRect(signalRect3X[signalNum1], signalRect3Y[signalNum1], playW/7, 3*s);
       				}
       			}
       		}
       	}
       	if(StateController.getNumLevels() == 3){
       		for(int i=0;i<2*(StateController.getNumGates(1));i++,signalNum1++){
       			rect1Len = signalRect2X[signalNum1]-signalRect1X[signalNum1];
       			if(i%2 == 1)
       				rect1Len = rect1Len + 3*s;
       			g2.setPaint(Color.black);
       			g2.draw(signalPath[signalNum1]);
       			if(StateController.getSignal(signalNum1) == 0){
       				g2.setPaint(Color.cyan);
       			}
       			else{
       				g2.setPaint(Color.red);
       			}
       			
       			if(isSimulation == true){
       				if(isStageDone[0] == true && isStageDone[1] == false){
       					if(simTime < 11){
       						g2.fillRect(signalRect1X[signalNum1], signalRect1Y[signalNum1], (simTime*rect1Len)/11,3*s);
       					}
       					else if(simTime < 21){
       						g2.fillRect(signalRect1X[signalNum1], signalRect1Y[signalNum1], rect1Len,3*s);
       						if(i%2 == 0)
       							g2.fillRect(signalRect2X[signalNum1], signalRect2Y[signalNum1], 3*s, ((simTime-10)*signalRect2Len[signalNum1])/11);
       						else
       							g2.fillRect(signalRect2X[signalNum1], signalRect2Y[signalNum1], 3*s, ((simTime-10)*signalRect2Len[signalNum1])/11);
       					}
       					else{
       						g2.fillRect(signalRect1X[signalNum1], signalRect1Y[signalNum1], rect1Len,3*s);
       						if(i%2 == 0)
       							g2.fillRect(signalRect2X[signalNum1], signalRect2Y[signalNum1], 3*s, signalRect2Len[signalNum1]);
       						else
       							g2.fillRect(signalRect2X[signalNum1], signalRect2Y[signalNum1], 3*s, signalRect2Len[signalNum1]);
       						g2.fillRect(signalRect3X[signalNum1], signalRect3Y[signalNum1], playW/9*(simTime-20)/10, 3*s);
       					}
       				}
       				else if(isStageDone[1] == true && isStageDone[2] == false){
       					g2.fillRect(signalRect1X[signalNum1], signalRect1Y[signalNum1], rect1Len,3*s);
       					if(i%2 == 0)
       						g2.fillRect(signalRect2X[signalNum1], signalRect2Y[signalNum1], 3*s, signalRect2Len[signalNum1]);
       					else
       						g2.fillRect(signalRect2X[signalNum1], signalRect2Y[signalNum1], 3*s, signalRect2Len[signalNum1]);
       					g2.fillRect(signalRect3X[signalNum1], signalRect3Y[signalNum1], playW/9, 3*s);
       					
       				}
       				else if(isStageDone[2] == true){
       					g2.fillRect(signalRect1X[signalNum1], signalRect1Y[signalNum1], rect1Len,3*s);
       					if(i%2 == 0)
       						g2.fillRect(signalRect2X[signalNum1], signalRect2Y[signalNum1], 3*s, signalRect2Len[signalNum1]);
       					else
       						g2.fillRect(signalRect2X[signalNum1], signalRect2Y[signalNum1], 3*s, signalRect2Len[signalNum1]);
       					g2.fillRect(signalRect3X[signalNum1], signalRect3Y[signalNum1], playW/9, 3*s);
       					
       				}
       			}
       		}
       		for(int i=0;i<2*(StateController.getNumGates(2));i++,signalNum2++){
       			rect1Len = signalRect2X[signalNum2]-signalRect1X[signalNum2];
       			if(i%2 == 1)
       				rect1Len = rect1Len + 3*s;
       			g2.setColor(Color.black);
       			g2.draw(signalPath[signalNum2]);
       			if(StateController.getSignal(signalNum2) == 0){
       				g2.setPaint(Color.cyan);
       			}
       			else{
       				g2.setPaint(Color.red);
       			}
       			if(isStageDone[1] == true && isStageDone[2] == false){
       				if(simTime < 11){
   						g2.fillRect(signalRect1X[signalNum2], signalRect1Y[signalNum2], (simTime*rect1Len)/11,3*s);
   					}
   					else if(simTime < 21){
   						g2.fillRect(signalRect1X[signalNum2], signalRect1Y[signalNum2], rect1Len,3*s);
   						if(i%2 == 0)
   							g2.fillRect(signalRect2X[signalNum2], signalRect2Y[signalNum2], 3*s, ((simTime-10)*signalRect2Len[signalNum2])/11);
   						else
   							g2.fillRect(signalRect2X[signalNum2], signalRect2Y[signalNum2], 3*s, ((simTime-10)*signalRect2Len[signalNum2])/11);
   					}
   					else{
   						g2.fillRect(signalRect1X[signalNum2], signalRect1Y[signalNum2], rect1Len,3*s);
   						if(i%2 == 0)
   							g2.fillRect(signalRect2X[signalNum2], signalRect2Y[signalNum2], 3*s, signalRect2Len[signalNum2]);
   						else
   							g2.fillRect(signalRect2X[signalNum2], signalRect2Y[signalNum2], 3*s, signalRect2Len[signalNum2]);
   						g2.fillRect(signalRect3X[signalNum2], signalRect3Y[signalNum2], playW/9*(simTime-20)/10, 3*s);
   					}
       			}
       			else if(isStageDone[2] == true){
   					g2.fillRect(signalRect1X[signalNum2], signalRect1Y[signalNum2], rect1Len,3*s);
   					if(i%2 == 0)
   						g2.fillRect(signalRect2X[signalNum2], signalRect2Y[signalNum2], 3*s, signalRect2Len[signalNum2]);
   					else
   						g2.fillRect(signalRect2X[signalNum2], signalRect2Y[signalNum2], 3*s, signalRect2Len[signalNum2]);
   					g2.fillRect(signalRect3X[signalNum2], signalRect3Y[signalNum2], playW/9, 3*s);
   					
   				}
       		}
       	}
       		
       	//get dimensions for input and output circles
    	getCircleDimensions();
		
		//draw input rectangles
		for(int i=0;i<StateController.getNumInputs();i++){
			Rectangle2D inRect = new Rectangle2D.Double(inputRectX[i], inputRectY[i], 52*s, 3*s);
			if(isSimulation == true && isStageDone[0] == false){
				g2.setPaint(getRectPaint(StateController.getInput(i).getValue(),gateNum));
				if(StateController.getInput(i).canChange() == false){
					g2.fillRect(inputRectX[i], inputRectY[i], 52*s, 3*s);
				}
				else{
					g2.fillRect(inputRectX[i], inputRectY[i], playW/9*simTime/10, 3*s);
				}						
			}
			else if(isSimulation == false){
				if(StateController.getInput(i).canChange() == false){
					g2.setPaint(getRectPaint(StateController.getInput(i).getValue(),gateNum));
					g2.fillRect(inputRectX[i],inputRectY[i], 52*s, 3*s);
				}
			}
			else if(isSimulation == true && isStageDone[0] == true){
					g2.setPaint(getRectPaint(StateController.getInput(i).getValue(),gateNum));
					g2.fillRect(inputRectX[i],inputRectY[i], 52*s, 3*s);
			}
			g2.setColor(Color.black);					
			g2.draw(inRect);	
			
			inputCircle[i] = new Ellipse2D.Double();
			inputCircle[i].setFrameFromCenter(inputCircleX[i], inputCircleY[i], inputCircleLen[i], inputCircleWid[i]);
	      	
						
			if(inputCircle[i] != null){
				g2.setPaint(getCirclePaint(StateController.getInput(i).getValue(),inputCircleLen[i],inputCircleWid[i]));
				g2.fillOval(inputCircleLen[i], inputCircleWid[i], radius*5, radius*5);
			}
			
	
			g2.setStroke(new BasicStroke(7));
			g2.draw(inputCircle[i]);
			g2.setStroke(oldStroke);
			
			if(i%2 == 1)
				gateNum ++;
			
		}

	   	gateNum=0;
	   	if(StateController.getNumInputs()>8){
			s=s/2;
		}
	   	int lastLevel = StateController.getNumLevels() -1;
	   	int lastLevelGates = StateController.getNumGates(lastLevel);
	   		
	   	//draw output rectangles and circles
	   	for(int i=0;i<lastLevelGates;i++){	
	   		//Rectangle2D outRect = new Rectangle2D.Double(dimX[lastGate-j]+(37*s)+s/2, dimY[lastGate-j]+((40*s)/2 - ((3*s)/2)), 53*s, 3*s);
	   		
	   		outputCircle[i] = new Ellipse2D.Double();
	      	outputCircle[i].setFrameFromCenter(outputCircleX[i], outputCircleY[i], outputCircleLen[i]-radius*5 , outputCircleWid[i]-radius*5);
	      
	      	int width= ((int) outputCircle[i].getX())-(outputRectX[i]);
	      	
	   		
	   		Rectangle2D outRect = new Rectangle2D.Double(outputRectX[i], outputRectY[i], 53*s, 3*s);
	   		if(isSimulation == true && isStageDone[StateController.getNumLevels()-1] == true){
	      		g2.setPaint(getRectPaint(gateOutputVal[StateController.getTotalGates()-1],0));
	      		//g2.fillRect(dimX[0]+(37*s)+s/2, dimY[0]+((40*s)/2 - ((3*s)/2)), 10*simTime, 3*s);	     
	      		g2.fillRect(outputRectX[0], outputRectY[0], width*simTime/11, 3*s);	 
	      	}
	      	g2.setColor(Color.black);
			g2.draw(outRect);
			g2.setPaint(getCirclePaint(StateController.getOut(i),outputCircleLen[i],outputCircleWid[i]));
	      	g2.fill(outputCircle[i]);
	 
			
			
	   	}
	   	
	  	//variable to track active gate
		int total = 0;
      
		for(int i=0;i<StateController.getNumLevels();i++){
			for(int j=0;j<StateController.getNumGates(i);j++){
				//assign gate dimensions
				gateX = dimX[j+total];
				gateY = dimY[j+total];
				
				//AND gate graphics
				Path2D.Double ANDpath = new Path2D.Double();
				ANDpath.moveTo(gateX, gateY);
				ANDpath.curveTo(gateX+50*s, gateY-5*s, gateX+50*s, (gateY+40*s)+5*s, gateX, gateY+40*s);
				ANDpath.lineTo(gateX,gateY); 
				//OR gate graphics
				Path2D.Double ORpath = new Path2D.Double();
				ORpath.moveTo(gateX, gateY);
				ORpath.curveTo(gateX, gateY, gateX+25*s, gateY,gateX+39*s , gateY+(40*s)/2);
				ORpath.curveTo(gateX+39*s , gateY+(40*s)/2, gateX+25*s, gateY+40*s,gateX, gateY+40*s);
				ORpath.curveTo(gateX, gateY+(40*s), gateX+5*s, gateY+20*s,gateX,gateY);
    	      
				//NAND gate graphics
				Path2D.Double NANDpath = new Path2D.Double();
				NANDpath.moveTo(gateX, gateY);
				NANDpath.curveTo(gateX+50*s, gateY-5*s, gateX+50*s, (gateY+40*s)+5*s, gateX, gateY+40*s);
				NANDpath.lineTo(gateX,gateY);   	            
				Ellipse2D NANDcircle = new Ellipse2D.Double();
				NANDcircle.setFrameFromCenter((gateX+(37*s)+s/2)+radius , gateY+((40*s)/2), (gateX+(37*s)+s/2)+radius + radius , gateY+((40*s)/2) + radius);
    	  
				//NOR gate graphics
				Path2D.Double NORpath = new Path2D.Double();
				NORpath.moveTo(gateX, gateY);
				NORpath.curveTo(gateX, gateY, gateX+25*s, gateY,gateX+39*s , gateY+(40*s)/2);
				NORpath.curveTo(gateX+39*s , gateY+(40*s)/2, gateX+25*s, gateY+40*s,gateX, gateY+40*s);
				NORpath.curveTo(gateX, gateY+(40*s), gateX+5*s, gateY+20*s,gateX,gateY);
    	      	Ellipse2D NORcircle = new Ellipse2D.Double();
    	      	NORcircle.setFrameFromCenter((gateX+39*s)+radius , gateY+(40*s)/2, (gateX+39*s)+radius + radius , (gateY+(40*s)/2) + radius);
    	      	
    	      	//XOR gate graphics
    	      	Path2D.Double XORcurvePath = new Path2D.Double();
    	      	XORcurvePath.moveTo(gateX-5, gateY);
    	      	XORcurvePath.curveTo(gateX-5, gateY, gateX-5+5*s, gateY+20*s,gateX-5,gateY+(40*s));
    	      	XORcurvePath.lineTo(gateX-10, gateY+(40*s));
    	      	XORcurvePath.curveTo(gateX-10,gateY+(40*s), gateX-10+5*s, gateY+20*s, gateX-10,gateY);
    	      	XORcurvePath.lineTo(gateX-5,gateY);
				Path2D.Double XORpath = new Path2D.Double();
				XORpath.moveTo(gateX, gateY);
				XORpath.curveTo(gateX, gateY, gateX+25*s, gateY,gateX+39*s , gateY+(40*s)/2);
				XORpath.curveTo(gateX+39*s , gateY+(40*s)/2, gateX+25*s, gateY+40*s,gateX, gateY+40*s);
				XORpath.curveTo(gateX, gateY+(40*s), gateX+5*s, gateY+20*s,gateX,gateY);
				
				//XNOR gate graphics
				Path2D.Double XNORcurvePath = new Path2D.Double();
    	      	XNORcurvePath.moveTo(gateX-5, gateY);
    	      	XNORcurvePath.curveTo(gateX-5, gateY, gateX-5+5*s, gateY+20*s,gateX-5,gateY+(40*s));
    	      	XNORcurvePath.lineTo(gateX-10, gateY+(40*s));
    	      	XNORcurvePath.curveTo(gateX-10,gateY+(40*s), gateX-10+5*s, gateY+20*s, gateX-10,gateY);
    	      	XNORcurvePath.lineTo(gateX-5,gateY);
				Path2D.Double XNORpath = new Path2D.Double();
				XNORpath.moveTo(gateX, gateY);
				XNORpath.curveTo(gateX, gateY, gateX+25*s, gateY,gateX+39*s , gateY+(40*s)/2);
				XNORpath.curveTo(gateX+39*s , gateY+(40*s)/2, gateX+25*s, gateY+40*s,gateX, gateY+40*s);
				XNORpath.curveTo(gateX, gateY+(40*s), gateX+5*s, gateY+20*s,gateX,gateY);
				Ellipse2D XNORcircle = new Ellipse2D.Double();
    	      	XNORcircle.setFrameFromCenter((gateX+39*s)+radius , gateY+(40*s)/2, (gateX+39*s)+radius + radius , (gateY+(40*s)/2) + radius);
    	      	
    	      	//UNI gate graphics
    	      	Path2D.Double UNIpath = new Path2D.Double();
    	      	UNIpath.moveTo(gateX, gateY);
    	      	UNIpath.lineTo(gateX+39*s, gateY);
    	      	UNIpath.lineTo(gateX+39*s, gateY+40*s);
    	      	UNIpath.lineTo(gateX, gateY+40*s);
    	      	UNIpath.lineTo(gateX, gateY);
    	      	//Rectangle2D.Double UniRect = new Rectangle2D.Double();
				//UniRect.setFrame(gateX, gateY, 39*s, 40*s);
    	      	
    	      	GradientPaint gp1;
    	      	switch(StateController.getGateType(j+total))
    	      	{
    	      	case AND:;
    	      		if(StateController.canToggle(j+total) == true)
    	      			gp1 = new GradientPaint(0, 0, Color.DARK_GRAY, 200, 0, Color.DARK_GRAY, true);
    	      		else
    	      		  gp1 = new GradientPaint(0, 0, Color.yellow, 200, 0, Color.orange, true);
   	      	        g2.setPaint(gp1);
    	      		g2.fill(ANDpath);
    	      		g2.setColor(Color.black);
    	      		g2.draw(ANDpath);
    	      		gatePath[j+total] = ANDpath;
    	      		break;
    	      	case OR:
    	      		if(StateController.canToggle(j+total) == true)
    	      			gp1 = new GradientPaint(0, 0, Color.DARK_GRAY, 200, 0, Color.DARK_GRAY, true);
    	      		else
    	      		  gp1 = new GradientPaint(0, 0, Color.yellow, 200, 0, Color.orange, true);
   	      	        g2.setPaint(gp1);
    	      		g2.fill(ORpath);
    	      		g2.setColor(Color.black);
    	      		g2.draw(ORpath);
    	      		gatePath[j+total] = ORpath;
      	      		break;
    	      	case NAND:
    	      		if(StateController.canToggle(j+total) == true)
    	      			gp1 = new GradientPaint(0, 0, Color.DARK_GRAY, 200, 0, Color.DARK_GRAY, true);
    	      		else
    	      		  gp1 = new GradientPaint(0, 0, Color.yellow, 200, 0, Color.orange, true);
   	      	        g2.setPaint(gp1);
    	      		g2.fill(NANDpath);
    	      		g2.setColor(Color.black);
    	      		g2.draw(NANDpath);
    	      		g2.setPaint(gp1);
    	      		g2.fill(NANDcircle);
    	      		g2.setColor(Color.black);
    	      		g2.draw(NANDcircle);
    	      		gatePath[j+total] = NANDpath;
    	      		break;
    	      	case NOR:
    	      		if(StateController.canToggle(j+total) == true)
    	      			gp1 = new GradientPaint(0, 0, Color.DARK_GRAY, 200, 0, Color.DARK_GRAY, true);
    	      		else
    	      		  gp1 = new GradientPaint(0, 0, Color.yellow, 200, 0, Color.orange, true);
   	      	        g2.setPaint(gp1);
    	      		g2.fill(NORpath);    	      		
    	      		g2.setColor(Color.black);
    	      		g2.draw(NORpath);
    	      		g2.setPaint(gp1);
    	      		g2.fill(NORcircle);
    	      		g2.setColor(Color.black);
    	      		g2.draw(NORcircle);
    	      		gatePath[j+total] = NORpath;
    	      		break;
    	      	case XOR:
    	      		if(StateController.canToggle(j+total) == true)
    	      			gp1 = new GradientPaint(0, 0, Color.DARK_GRAY, 200, 0, Color.DARK_GRAY, true);
    	      		else
    	      		  gp1 = new GradientPaint(0, 0, Color.yellow, 200, 0, Color.orange, true);
   	      	        g2.setPaint(gp1);
    	      		g2.fill(XORpath);
    	      		g2.fill(XORcurvePath);
    	      		g2.setColor(Color.black);
    	      		g2.draw(XORcurvePath);
    	      		g2.draw(XORpath);
    	      		gatePath[j+total] = XORpath;
    	      		break;
    	      	case XNOR:
    	      		if(StateController.canToggle(j+total) == true)
    	      			gp1 = new GradientPaint(0, 0, Color.DARK_GRAY, 200, 0, Color.DARK_GRAY, true);
    	      		else
    	      		  gp1 = new GradientPaint(0, 0, Color.yellow, 200, 0, Color.orange, true);
   	      	        g2.setPaint(gp1);
   	      	        g2.fill(XNORcurvePath);
    	      		g2.fill(XNORpath);    	      		
    	      		g2.setColor(Color.black);
    	      		g2.draw(XNORcurvePath);
    	      		g2.draw(XNORpath);
    	      		g2.setPaint(gp1);
    	      		g2.fill(XNORcircle);
    	      		g2.setColor(Color.black);
    	      		g2.draw(XNORcircle);
    	      		gatePath[j+total] = XNORpath;
    	      		break;
    	     	case UNI:
    	      		g2.setColor(Color.DARK_GRAY);
    	      		g2.fill(UNIpath);
    	      		g2.setColor(Color.black);
    	      		g2.draw(UNIpath);
    	      		drawCenteredString("?",gateX, gateY, 39*s, 40*s,g2);
    	      		gatePath[j+total] = UNIpath;
    	      		break;
    	      	}    		  
			}
    	  
			total = total + StateController.getNumGates(i);	  
		}
		
		//generate success message and start end timer
		if(isFinished == true || time == 0){
			endTimer.start();
			String success;
			if(time != 0 && gateOutputVal[StateController.getTotalGates() -1] == StateController.getOut(0)){
					StateController.levelUP(StateController.getLevel()+1);
					StateController.save();
					success = "Success!";
					hasSucceeded=true;
			}
			else{
				success = "Unsuccessful!";
			}
			
			g2.drawString(success, playX+3*playW/4, playY + 3*playH/4);
		}
		
		//draw and fill pauseButton
		pauseButton = new RoundRectangle2D.Double(sizeX/40, sizeY/40, sizeX/16, sizeY/24, sizeX/60, sizeY/30);
		g2.setColor(Color.RED.darker());
		if(StateController.getMode()){g2.setColor(Color.cyan.darker());}
		g2.fill(pauseButton);
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(thickness));
		g2.draw(pauseButton);
		g2.setStroke(oldStroke);
		g2.setFont(new Font("Monospaced", Font.BOLD, fontSize/90));
		drawCenteredString("PAUSE", (int) pauseButton.getX(), (int) pauseButton.getY(), (int) pauseButton.getWidth(),(int) pauseButton.getHeight(), g2);
				
		//draw and fill pause menu and pause menu buttons
		if(isPaused){
			pauseMenu=new RoundRectangle2D.Double(sizeX/12-pressedPause*(sizeX/150), sizeY/15+pressedPause*(sizeY/120), 5*sizeX/6, 5*sizeY/6, sizeX/60, sizeY/30);
			g2.setColor(Color.DARK_GRAY);
			g2.fill(pauseMenu);
			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke(6));
			g2.draw(pauseMenu);
			int digX=(int) pauseMenu.getX();
			int digY=(int) pauseMenu.getY();
			int digH=(int) pauseMenu.getHeight();
			int digW=(int) pauseMenu.getWidth();
			menuButton[0]=new RoundRectangle2D.Double(digX+ digW/9-pressedMenu[0]*(sizeX/150), digY+13*digH/16+pressedMenu[0]*(sizeY/120), digW/6, digH/10, sizeX/60, sizeY/30);
			menuButton[1]=new RoundRectangle2D.Double(digX+ 3*digW/9-pressedMenu[1]*(sizeX/150), digY+13*digH/16+pressedMenu[1]*(sizeY/120), digW/6, digH/10, sizeX/60, sizeY/30);
			menuButton[2]=new RoundRectangle2D.Double(digX+ 5*digW/9-pressedMenu[2]*(sizeX/150), digY+13*digH/16+pressedMenu[2]*(sizeY/120), digW/6, digH/10, sizeX/60, sizeY/30);
			menuButton[3]=new RoundRectangle2D.Double(digX+ 7*digW/9-pressedMenu[3]*(sizeX/150), digY+13*digH/16+pressedMenu[3]*(sizeY/120), digW/6, digH/10, sizeX/60, sizeY/30);			
			for (int i=0; i<4; i++){
				g2.setColor(Color.RED.darker());
				if(i>1){g2.setColor(Color.CYAN.darker());}
				g2.fill(menuButton[i]);
				g2.setColor(Color.black);
				g2.setStroke(new BasicStroke(thickness));
				g2.draw(menuButton[i]);
				g2.setStroke(oldStroke);
			}
				
			g2.setFont(new Font("Monospaced", Font.BOLD, fontSize/40));
			if(timeUp){drawCenteredString("HOME", (int) menuButton[0].getX(), (int) menuButton[0].getY(), (int) menuButton[0].getWidth(),(int) menuButton[0].getHeight(), g2);}
			else{drawCenteredString("CONTINUE", (int) menuButton[0].getX(), (int) menuButton[0].getY(), (int) menuButton[0].getWidth(),(int) menuButton[0].getHeight(), g2);}
			drawCenteredString("LEVELS", (int) menuButton[1].getX(), (int) menuButton[1].getY(), (int) menuButton[1].getWidth(),(int) menuButton[1].getHeight(), g2);
			drawCenteredString("MODE", (int) menuButton[2].getX(), (int) menuButton[2].getY(), (int) menuButton[2].getWidth(),(int) menuButton[2].getHeight(), g2);
			drawCenteredString("RESET", (int) menuButton[3].getX(), (int) menuButton[3].getY(), (int) menuButton[3].getWidth(),(int) menuButton[3].getHeight(), g2);
					
			String pic = "files/033-0.png";
			Image id = new ImageIcon(pic).getImage();
			int idX=3*digW/4;
			int idY=5*digH/6;
			g2.drawImage(id,digX+digW/2-idX/2, digY+5*digH/12-idY/2, idX, idY,null);
			
			}
		

}	//handle mouse clicks
	private class MouseHandler extends MouseAdapter
	{
		public void mousePressed(MouseEvent event)
		{
			int x = event.getX();
			int y = event.getY();
			Point2D point = new Point2D.Double(x,y);
			
			if (isPaused==true){
				for (int i=0; i<4; i++){
					if(menuButton[i].contains(point)){
						insideMenuButton[i]=true;
					}
					else{
						insideMenuButton[i]=false;
					}
				}
			}
			else{
				for (int i=0; i<4; i++){
					insideMenuButton[i]=false;
					}
			}
			if(pauseButton.contains(point)&& !isPaused){
				insidePauseButton = true;
			}
			else{
				insidePauseButton = false;
			}
			if(runButtonBorder.contains(point) && !isPaused && !isSimulation){
				insideRun = true;
			}
			else{
				insideRun = false;
			}
			
			if(pauseButton.contains(point) && !isPaused && !isSimulation){
				insidePauseButton=true;
			}
			else{
				insidePauseButton=false;
			}
			
			
			repaint();
		}
		
		public void mouseReleased(MouseEvent event)
		{
			
			if(insideRun== true){
				insideRun = false;
				boolean gatesUnset = false;
				for(int i=0; i<StateController.getNumInputs();i++){
					if(StateController.getInput(i).getValue() == 2){
						gatesUnset = true;
					}
				}
				if(StateController.getMode()){
					for(int i=0; i<StateController.getTotalGates(); i++){
						if(StateController.getGateType(i)==GateType.UNI){
							gatesUnset = true;
						}
					}
				}
				if(gatesUnset == false){
					isSimulation = true;
					simTimer.start();
					StateController.updateSignals();
					for(int i=0;i<StateController.getTotalGates();i++){
						//	circuit.updateSignals();
						gateOutputVal[i] = StateController.getGateOut(i);
					}
				}
				repaint();
			}
			
			if (insideMenuButton[0]){
				insideMenuButton[0]=false;
				isPaused=false;
				if(timeUp){
					StateController.update("MainMenu");
				}
				repaint();
			}
			
			if (insideMenuButton[1]){
				insideMenuButton[1]=false;
				isPaused=false;
				StateController.update("LevelSelect");
			}
			
			if (insideMenuButton[2]){
				insideMenuButton[2]=false;
				isPaused=false;
				StateController.update("ModeSelect");
			}
			
			if (insideMenuButton[3]){
				insideMenuButton[3]=false;
				isPaused=false;
				StateController.reload();
			}
			
			
			if( insidePauseButton == true){
				insidePauseButton = false;
				isPaused=true;
				repaint();
			}
		}
		//if mouse clicked (pressed and released)
		public void mouseClicked(MouseEvent event)
		{
			int x = event.getX();
			int y = event.getY();
			Point2D point = new Point2D.Double(x, y);
			
			//check if clicked within run button
			
			if(runButtonBorder.contains(point) == true && !isPaused){
				boolean gatesUnset = false;
				for(int i=0; i<StateController.getNumInputs();i++){
					if(StateController.getInput(i).getValue() == 2){
						gatesUnset = true;
					}
				}
				if(StateController.getMode()){
					for(int i=0; i<StateController.getTotalGates(); i++){
						if(StateController.getGateType(i)==GateType.UNI){
							gatesUnset = true;
						}
					}
				}
				if(gatesUnset == false){
					isSimulation = true;
					simTimer.start();
					StateController.updateSignals();
					for(int i=0;i<StateController.getTotalGates();i++){
						//	circuit.updateSignals();
						gateOutputVal[i] = StateController.getGateOut(i);
					}
				}
				repaint();
			}
			
			
			//check if clicked within input circles
			for(int i=0;i<StateController.getNumInputs();i++){
				if(inputCircle[i] != null && isTimeUp == false && inputCircle[i].contains(point) == true && !isPaused){
					if(StateController.getInput(i).canChange() == true){
						if(StateController.getInput(i).getValue() == 0){
							StateController.getInput(i).setValue(1);
							repaint();
						}
						else{
							StateController.getInput(i).setValue(0);
							repaint();
						}
					}	
				}
			}
			
			//check if clicked within universal gates
			for(int i=0;i<StateController.getTotalGates();i++){
				if(StateController.canToggle(i) && gatePath[i].contains(point) && !isSimulation && !isPaused){
					StateController.toggleGate(i);
					repaint();
				}
			}
			
			if (isPaused){
				if(menuButton[0]!=null && menuButton[0].contains(point)){
					isPaused=false;
					if(timeUp){
					StateController.update("MainMenu");
					}
				}
				if(menuButton[1]!=null && menuButton[1].contains(point)){StateController.update("LevelSelect");}
				if(menuButton[2]!=null && menuButton[2].contains(point)){StateController.update("ModeSelect");}
				if(menuButton[3]!=null && menuButton[3].contains(point)){StateController.reload();}
				
			}
			
			if(pauseButton.contains(point)==true){
				isPaused=true;
			}
		}
	}
	

		
	
	
	private String getTime(){
		String displayTime;
		
		if(time>59){
			displayTime = "0"+String.valueOf(time/60)+":";
		}
		else{
			displayTime = "00:";	
		}
		if(time%60 < 10){
				displayTime += "0"+String.valueOf(time%60);
		}
		else{
				displayTime +=String.valueOf(time%60);
		}
		
		return displayTime;
	}
	
	private void getCircleDimensions(){
		int gate = 0;
		for(int i=0;i<2*StateController.getNumGates(0);i++){
			inputCircleX[i] = (dimX[gate]-50*s) - radius;
			inputCircleY[i] = (dimY[gate]+(6+(i%2)*23)*s) + (radius/2);
			inputCircleLen[i] = (dimX[gate]-50*s)-(7*radius/2);
			inputCircleWid[i] = (dimY[gate]+(6+(i%2)*23)*s)-2*radius; 
			
			inputRectX[i] = (dimX[gate]-50*s);
			inputRectY[i] = (dimY[gate]+(6+(i%2)*23)*s);
			
			if(i%2 == 1)
				gate++;
		}
		int lastLevel = StateController.getNumLevels()-1;
		int lastGate = StateController.getTotalGates();
		int lastLevelGates = StateController.getNumGates(lastLevel);
		
		for(int i=0,j=lastLevelGates;i<lastLevelGates;i++,j--){		
			outputCircleX[i] = (dimX[lastGate-j]+(37*s)+s/2)+2*radius+53*s; 
			outputCircleY[i] = dimY[lastGate-j]+((40*s)/2);
			outputCircleLen[i] = (dimX[lastGate-j]+(37*s)+s/2)+(9*radius/2)+53*s;
			outputCircleWid[i] = dimY[lastGate-j]+((40*s)/2)+5*radius/2;
			
			outputRectX[i] = (dimX[lastGate-j]+(37*s)+s/2); 
			outputRectY[i] = dimY[lastGate-j]+((40*s)/2 - ((3*s)/2));
		}
	}
	
	private GradientPaint getRectPaint(int in, int gate){
		if(in == 0){
			return new GradientPaint(dimX[gate]-50*s, dimY[gate]+6*s, Color.cyan, dimX[gate]-50*s, dimY[gate]+6*s+(3*s/2), Color.cyan, true);
		}
		else if(in == 1){
			return new GradientPaint(dimX[gate]-50*s, dimY[gate]+6*s, Color.red, dimX[gate]-50*s, dimY[gate]+6*s+(3*s/2), Color.red, true);
		}
		else{
			return new GradientPaint(dimX[gate]-50*s, dimY[gate]+6*s, Color.LIGHT_GRAY, dimX[gate]-50*s, dimY[gate]+6*s+(3*s/2), Color.white, true);
		}
		
	}
	
	private RadialGradientPaint getCirclePaint(int val, int len, int wid){
		Point2D center = new Point2D.Float(len, wid);
		float rad = radius*5;
		float[] dist = {0.0f, 0.2f, 1.0f};
		RadialGradientPaint p;
		
		if(val == 2){
			Color[] colors0 = {Color.WHITE, Color.white, Color.gray};
			p = new RadialGradientPaint(center, rad, dist, colors0);
		}
		else if(val == 0){
			Color[] colors1 = {Color.white, Color.white, Color.cyan};
			p = new RadialGradientPaint(center, rad, dist, colors1);
		}
		else{
			Color[] colors2 = {Color.WHITE, Color.white, Color.RED};
			p = new RadialGradientPaint(center, rad, dist, colors2);
		}
		return p;
	}
	
	private void setGateDimensions(){
		int levels = StateController.getNumLevels();
		int gatesDoneY = 0;
		
		for(int i=0;i<levels;i++){
			switch(levels)
			{
				case 1:
					for(int j=0;j<StateController.getNumGates(0);j++)
						dimX[j] = playX+playW/2-20*s;
				break;
				case 2:
					for(int j=0;j<StateController.getNumGates(0);j++)
						dimX[j] = playX+playW/3-20*s;
					for(int k=0;k<StateController.getNumGates(1);k++)
						dimX[k+StateController.getNumGates(0)] = playX+2*playW/3-20*s;
				break;
				case 3:
					for(int j=0;j<StateController.getNumGates(0);j++)
						dimX[j] = playX+playW/4-20*s;
					for(int k=0;k<StateController.getNumGates(1);k++)
						dimX[k+StateController.getNumGates(0)] = playX+playW/2-20*s;
					for(int m=0;m<StateController.getNumGates(2);m++)
						dimX[m+StateController.getNumGates(0)+StateController.getNumGates(1)] = playX+3*playW/4-20*s;
				break;	
			}
			switch(StateController.getNumGates(i))
			{
				case 1:
					dimY[gatesDoneY] = playY+playH/2-20*s;
					gatesDoneY++;			
				break;
				case 2:
					dimY[gatesDoneY] = playY+playH/4-20*s;
					gatesDoneY++;
					dimY[gatesDoneY] =  playY+3*playH/4-20*s;
					gatesDoneY++;
				break;
				case 4: 
					dimY[gatesDoneY] = playY+playH/8-20*s;
					gatesDoneY++;
					dimY[gatesDoneY] = playY+3*playH/8-20*s;
					gatesDoneY++;
					dimY[gatesDoneY] = playY+5*playH/8 - 20*s;
					gatesDoneY++;
					dimY[gatesDoneY] = playY+7*playH/8-20*s;
					gatesDoneY++;
			}
		}
	}
	
	//set dimensions for each signal
	private void setSignalDimensions(){
		//if non 2-input gates, will need an accessor to see number of signals in each level
		int startingGate = StateController.getNumGates(0);
		int activeGate = startingGate;
		int sourceGate;
		int sourceSignal;
		
		startingGate = 0; 
		sourceGate = 0; //first gate in circuit
		sourceSignal = StateController.getNumInputs(); //first signal in circuit
		
		if(StateController.getNumLevels()>1){ //if number of levels is greater than 1
			for(int i=1;i<StateController.getNumLevels();i++){ //iterate through each level
				startingGate = startingGate + StateController.getNumGates(i-1); //first gate in level 
				activeGate = startingGate; 
				for(int j=0;j<2*StateController.getNumGates(i);j++){ //iterate through each input in level
					//if gate in level has an input that is the source signal
					if(StateController.getTop(activeGate) == sourceSignal ||StateController.getBottom(activeGate) == sourceSignal){
						
						int len1 = 0;
	    	      		int len2 = 0;
	    	      		if(dimY[sourceGate]>dimY[activeGate]){
	    	      			len1 = 3*s;
	    	      		}
	    	      		else{
	    	      			len2 = 3*s;
	    	      		}
						
						int k=0;
						if(StateController.getTop(activeGate) == sourceSignal){
							k=6;
						}
						else{
							k=29;
						}
						Path2D outputRect = new Path2D.Double();
						
						signalRect1X[sourceSignal] = dimX[sourceGate]+(37*s)+s/2;
						signalRect1Y[sourceSignal] = dimY[sourceGate]+((40*s)/2 - ((3*s)/2));
						signalRect2X[sourceSignal] = (dimX[activeGate]+dimX[sourceGate]+39*s)/2;
						signalRect2Y[sourceSignal] = dimY[sourceGate]+((40*s)/2 - ((3*s)/2));
						signalRect3X[sourceSignal] = (dimX[activeGate]+dimX[sourceGate]+39*s)/2;
						signalRect3Y[sourceSignal] = (dimY[activeGate]+(k*s));
						
						signalRect2Len[sourceSignal] = -(dimY[sourceGate]+((40*s)/2 - ((3*s)/2))) + (dimY[activeGate]+(k*s)) + 3*s;
						
	    	      		outputRect.moveTo(dimX[sourceGate]+(37*s)+s/2, dimY[sourceGate]+((40*s)/2 - ((3*s)/2))); //starting
						outputRect.lineTo((dimX[activeGate]+dimX[sourceGate]+39*s)/2+len2, dimY[sourceGate]+((40*s)/2 - ((3*s)/2))); //left top horizontal
						outputRect.lineTo((dimX[activeGate]+dimX[sourceGate]+39*s)/2+len2,(dimY[activeGate]+(k*s))); //middle vertical right
						outputRect.lineTo((dimX[activeGate]+s/2),(dimY[activeGate]+(k*s))); //bottom top horizontal
						outputRect.lineTo((dimX[activeGate]+s/2),(dimY[activeGate]+(k*s))+3*s); //right vertical right
						outputRect.lineTo((dimX[activeGate]+dimX[sourceGate]+39*s)/2 - 3*s + len1 + 3*s, (dimY[activeGate]+(k*s))+3*s); //bottom bottom horizontal
						outputRect.lineTo((dimX[activeGate]+dimX[sourceGate]+39*s)/2 - 3*s + len1 + 3*s, dimY[sourceGate] + ((40*s)/2 -((3*s)/2))+3*s); //middle vertical left
						outputRect.lineTo(dimX[sourceGate] + 37*s + s/2, dimY[sourceGate] + ((40*s)/2 -((3*s)/2))+3*s);
						outputRect.lineTo(dimX[sourceGate]+(37*s)+s/2, dimY[sourceGate]+((40*s)/2 - ((3*s)/2)));
						signalPath[sourceSignal] = outputRect;
						sourceSignal++;
						sourceGate++;
					}
					if(j%2 == 1){
						activeGate++;
					}
				}
			}
		}
	}
	
	
	//method to draw a string centered within a rectangle 
	public void drawCenteredString(String s, int x, int y, int w, int h, Graphics g) {
	    FontMetrics fm = g.getFontMetrics();
	    int newX = x + ((w - fm.stringWidth(s)) / 2);
	    int newY = y + (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
	    g.drawString(s, newX, newY);
	  }
}