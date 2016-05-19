
package Controller;

import java.io.FileOutputStream;

import java.io.IOException;
import java.io.ObjectOutputStream;


import Controller.SaveFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class MainMenu implements State {
	

	private String name;
	private int levelInput;
	private int levelGate;
	private List<String> saves = new ArrayList<String>();
	
	
	
	public MainMenu(){
		File dir = new File("files/saves/");
		File[] files = dir.listFiles(new FilenameFilter() {public boolean accept(File dir, String n){return n.toLowerCase().endsWith(".ser");}});
		for (int i=0; i<files.length; i++){
			String tmp=files[i].toString();
			saves.add(tmp.substring(12,tmp.length()-4));		
		}
		for (int i=saves.size(); i<3; i++){
			saves.add("  --  ");
		}
		
		
	}

	//creates a file with String n as a name
	public void createFile(String n){
			name=n; saveFile(n, 1, 1);
			}
	
	public void saveFile(String n, int input, int gate){
		SaveFile e=new SaveFile(n, input, gate);
		try{
			String filename="files/saves/"+ n.toUpperCase() + ".ser";
			File newFile= new File(filename);
			FileOutputStream saveOut = new FileOutputStream(newFile);
			ObjectOutputStream out = new ObjectOutputStream(saveOut);
			out.writeObject(e);
			out.close();
			saveOut.close();
		}
		catch(IOException x){
			x.printStackTrace();
		}
	}
	
	public void loadFile(String n){	
		SaveFile e = null;
		try{
			String filename="files/saves/"+ n.toUpperCase() + ".ser";
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			e = (SaveFile) in.readObject();
			in.close();
			fileIn.close();
		}
		catch(IOException i){
			i.printStackTrace();
			return;
		}
		catch(ClassNotFoundException c){
			System.out.println("Save File not found");
			c.printStackTrace();
			return;
		}
		name=e.getName();
		levelInput=e.getInputLevel();
		levelGate=e.getGateLevel();
	}
	
	
	public int numSaveFiles(){return saves.size();}
	public String saveName(int i){return saves.get(i);}
	public String getName(){return name;}
	public int getInput(){return levelInput;}
	public int getGate(){return levelGate;}
	
	
	public void changeState(){StateController.setState();}

	//Returns true if a save file with that name exists
	
	public boolean invalid(String n) {
		boolean check=false;
		for (int i=0; i<3; i++){
			String tmp=saves.get(i);
			if(tmp.equals(n.toUpperCase())){
				check=true;
			}
		}
		return check;
	}
	}
