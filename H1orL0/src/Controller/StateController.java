package Controller;

import Model.*;
import View.*;



public class StateController {
	
	
	
	//Models
	private static MainMenu mainMenu=new MainMenu();
	private static ModeSelect modeSelect=new ModeSelect();
	private static LevelSelect levelSelect=new LevelSelect();
	private static PlayingMode playingMode;
	
	//State Trackers
	private static View view;
	private static State nextState=modeSelect;
	private static State currentState=mainMenu;
	
	
	//Universal parameters
	private static String name;
	private static int gate;
	private static int input;
	private static boolean mode=false;
	
	
	
	public StateController(){
			   view = new View();
    }
        
	
	
	
	
	
	/** setNextState
     * 
     * <p>Sets the next state for update
     */
	public static void setNextState(State next){nextState=next;}
	
	/**setState
     * 
     * <p>updates the state to the nextState
     */
    public static void setState(){currentState= nextState;}    

    /**createLevel
     * 
     * <p>updates the state to the nextState
     */
    public static void createLevel(int n){playingMode= new PlayingMode();
    										playingMode.createCircuit(n);}
    
    /**reload
     * 
     * <p>Reloads playing state with current level;
     */
    public static void reload(){
    	int level =StateController.getLevel();
    	createLevel(level);
    	update("PlayingMode");
    }
    
    /**update
     * 
     * <p>Updates the Model an View to "state"
     */
    public static void update(String state){
    	updateModel(state);
    	updateView(state);
    }
    
    /**updateModel
     * 
     * <p>updates the model
     */
    private static void updateModel(String name){
    	if(name.equals("MainMenu")){
    		mainMenu=new MainMenu();
    		setNextState(mainMenu);
    	}
    	else if(name.equals("ModeSelect")){
    		modeSelect=new ModeSelect();
    		setNextState(modeSelect);
    	}
    	else if(name.equals("LevelSelect")){
    		setNextState(levelSelect);
    	}
    	else if(name.equals("PlayingMode")){
    		setNextState(playingMode);
    	}
    	currentState.changeState();
    }
    	
    /**updateView
     * 
     * <p>updates the View
     */
    private static void updateView(String state){
    	view.setView(state);
    }
    
    
    
   /*
    * Main Menu Functions
    * 
    * These functions are used in conjunction with Main Menu to load, save and create files
    * 
    * Some of thes functions act as getters and setters
    */
    
    /** save()
     * 
     * <p>This function calls mainMenu.saveFiles to save the name, and current levels for the open file
     * 
     */
    public static void save(){mainMenu.saveFile(name, input, gate);}
    
    /** load
     * 
     * <p>Parameters: String s
     * <p> Loads file of name 's' and sets the variables for the files in StateController
     * 
     */
    public static void load(String s){
    	mainMenu.loadFile(s);
    	name=mainMenu.getName();
    	input=mainMenu.getInput();
    	gate=mainMenu.getGate();
    }
    
    /** createGame
     * 
     * <p>Parameters: String n
     * <p> Creates file of name 'n' and sets the variables for the files in StateController
     * 
     */
    public static void createGame(String n){
    	mainMenu.createFile(n);
    	name=mainMenu.getName();
    	input=mainMenu.getInput();
    	gate=mainMenu.getGate();
    }
    
    /** numSaves
     * 
     * 
     * <p> returns the number of saved files
     * 
     */
    public static int numSaves(){return mainMenu.numSaveFiles();}
    
    /** saveName
     * 
     * 
     * <p> Returns String of the name of save file of index n
     * 
     */    
    public static String saveName(int n){return mainMenu.saveName(n);}
    
    /** invalidSave
     * 
     * 
     * <p> Returns true if save file with name already exists
     * 
     */   
    public static boolean invalidSave(String n){return mainMenu.invalid(n);}
    	
    
    
    /*
     * Mode Select Function
     * 
     * These functions are designed to set and get the mode(Input select and Gate Select)
     * 
     */
    
    /** getMode
     * 
     * 
     * <p> Returns true if in GateSelect Mode and false if in Input Select Mode
     * 
     */   
    public static boolean getMode(){return mode;}    
   
    /** setMode
     * 
     * 
     * <p> sets to Gate SelectMode if isGate is 1, else its Input Select Mode
     * 
     */   
    public static void setMode(int isGate){if (isGate==1){mode=true;}else{mode=false;}} 
    
    
    /*
     * Level Select Functions
     * 
     * These Functions set the level selected and checks if the level is available to play
     */
    
    /** setMode
     * 
     * 
     * <p> sets to Gate SelectMode if isGate is 1, else its Input Select Mode
     * 
     */  
    public static void setLevel(int n){levelSelect.select(n);}
    
    
    /** notPassed
     * 
     * 
     * <p> returns true if the level is not available
     * 
     */
    public static boolean notPassed(int n){if (mode){return (gate<n);}else{return (input<n);}}
    
    
    
   /*
    * Playing Mode Functions
    * 
    * These functions are Playing View's connection to the circuit Model for information,
    * as well as the Circuit class's connection to playingMode to alter the inputs and outputs
    * 
    */


    /** getLevel
     * 
     * 
     * <p> Gets the level number for the current circuit
     * 
     */
    public static int getLevel(){return playingMode.getLevel();}
    


    /** addInput
     * 
     * 
     * <p> Adds input to playingMode array
     * 
     */
    public static void addInput(Signal s){playingMode.addInput(s);}
    
    
    /** getInput
     * 
     * 
     * <p> Returns signal input of index 'i'
     * 
     */
    public static Signal getInput(int i){return playingMode.getInput(i);}
    

    /** getNumInput
     * 
     * 
     * <p>Returns the number of input in the circuit
     * 
     */
    public static int getNumInputs(){return playingMode.getNumInputs();}
    

    /** getOut
     * 
     * 
     * <p>Returns the output of index i
     * 
     */
    public static int getOut(int i){return playingMode.getOutput(i);}
    

    /** addOut
     * 
     * 
     * <p>Adds the output for the Circuit
     * 
     */
    public static void addOut(int i){playingMode.addOutput(i);}
    
   
    /** toggleGate
     * 
     * 
     * <p>Toggles the gatetype of gate of index i if its a Universal gate
     * 
     */
    public static void toggleGate(int n){playingMode.togGate(n);}
    
    /**canToggle
     * 
     * <p> Checks if a get is able to toggle(is Universal)
     */
    public static boolean canToggle(int n){return playingMode.isUni(n);}
    
    /**getTime
     * 
     * <p> Returns the time allowed for the circuit
     */
    public static int getTime(){return playingMode.getTime();}
    

    /**updateSignals
     * 
     * <p> Updates the circuit gate by gate
     */
    public static void updateSignals(){playingMode.update();}
    

    /**getTotalGates
     * 
     * <p> Get the total number of gates in the circuit
     */
    public static int getTotalGates(){return playingMode.getTotalGates();}
    


    /** getGateOut
     * 
     * <p>Gets the output of gate of index 'n'
     */
    public static int getGateOut(int n){return playingMode.getGateOutput(n);}
    
    /** getNumLevels
     * 
     * <p>Gets the number of stages in the circuit
     */
    public static int getNumLevels(){return playingMode.getNumLevels();}
    
    /** getNumGates
     * 
     * <p> Gets the number of gates in stage number 'n'
     */
    public static int getNumGates(int n){return playingMode.getNumGates(n);}
    

    /** getSignal
     * 
     * <p>Gets the signal of index 'n'
     */
    public static int getSignal(int n){return playingMode.getSignal(n);}
    
    /** getGateType
     * 
     * <p>Returns the type of the gate of index n
     */
    public static GateType getGateType(int n){return playingMode.getGateType(n);}
    
    /** getTop
     * 
     * <p>Returns the index of the top input of gate of index 'n'
     */
    public static int getTop(int n){return playingMode.getTop(n);}
    
    /** getBottom
     * 
     * <p>Returns the index of the bottom input of gate of index 'n'
     */
    public static int getBottom(int n){return playingMode.getBottom(n);}
   
    
    /** levelUp
     * 
     * <p>Sets the level to the level n 
     * 
     */
    public static void levelUP(int n){
    	if(mode){
    		if (n>gate)
    			gate=n;
    		}
    	else{
   			if (n>input){  				
   				input=n;
   			}
    	}
   	}
    
    
}

