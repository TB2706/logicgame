package View;
import Controller.*;


import java.awt.Image;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.CardLayout;

class ViewFrame extends JFrame
{ 
	private PlayingView playing;
	private LevelSelectView levelSelect=new LevelSelectView();
	private MainMenuView mainMenu=new MainMenuView();
	private ModeSelectView modeSelect= new ModeSelectView();
	private LoadFileView load= new LoadFileView();
	private NewGameView newGame= new NewGameView();
	private CardLayout viewState=new CardLayout();
	private JPanel current= new JPanel(viewState);
	private String currentState="MainMenu";
   
	public ViewFrame()
	{
		
      //dimensions of frame      
      int locX = LogicGame.locX;
      int locY = LogicGame.locY;
      int sizeX = LogicGame.width;
      int sizeY = LogicGame.height;

      // set frame width, height and let platform pick screen location
      setSize(sizeX, sizeY);
      setLocation(locX, locY);
      setResizable(false);

      // set frame icon
      Image img = new ImageIcon("icon.png").getImage();
      setIconImage(img);    
            

      // Add the various views, playing View will be constructed after circuit for the level is constructed
      current.add("MainMenu", mainMenu);
      current.add("NewGameView", newGame);
      current.add("LoadGameView", load);
      current.add("ModeSelect", modeSelect);
      current.add("LevelSelect", levelSelect);
      current.setVisible(true);
      add(current);
     
     
      }
   
   // Set the view for the state
	public void setView(String state){
	   if(state.equals("PlayingMode")){
		   playing= new PlayingView();
		   current.add("PlayingMode", playing);
	   }
	   if (state.equals("NewGameView")){
		   
	   }
	   viewState.show(current,state);
	   if (currentState.equals("PlayingMode")){
		     
   }
   }
}

