package Controller;


import java.io.Serializable;

public class SaveFile extends Object implements Serializable {

	private String name;
	private int iLevel;
	private int gLevel;
	
	
	SaveFile(String name, int iLevel, int gLevel){
		this.name=name;
		this.iLevel=iLevel;
		this.gLevel=gLevel;
	
	}
	
	public String getName(){return name;}
	public int getInputLevel(){return iLevel;}
	public int getGateLevel(){return gLevel;}
}

