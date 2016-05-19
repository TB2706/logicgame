package Model;

import Controller.*;
import View.*;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.*;



public class Circuit {
	
	//
	private List <Gate> gates=new ArrayList<Gate>();
	private List<Signal> signals=new ArrayList<Signal>();
	private List<Signal> outputs=new ArrayList<Signal>();
	private List<Integer> lvls = new ArrayList<Integer>();
	private int numLevels;
	private int numInputs;
	private int level;
	private int timer;
	
	
	
	/*
	 * Circuit
	 * 
	 * Constructor Takes a Level from the JSON file to build the circuit
	 */
	public Circuit (int lvl){
		level=lvl;
		JSONParser parser = new JSONParser();
		try{
			JSONObject obj = (JSONObject) parser.parse( new FileReader("files/levels.json"));
			JSONArray levels;
			if (StateController.getMode()){
				levels = (JSONArray) obj.get("GateLevels");
				}
			else {
				levels = (JSONArray) obj.get("InputLevels");
			}
			JSONObject circuit =  (JSONObject) levels.get(lvl);
			toInputs(circuit);
			JSONArray stages = (JSONArray) circuit.get("Stages");
			numLevels = stages.size();
			JSONObject curlevel;
			for (int i=0; i<numLevels; i++){                         //Builds the circuit one stage at a time
				curlevel = (JSONObject) stages.get(i);
				buildLevel(curlevel);	
			}
			JSONArray outputSignals = (JSONArray) circuit.get("Output");
			signalOutputs(outputSignals);
			JSONArray desoutputs = (JSONArray) circuit.get("Goal");
			desiredOutputs(desoutputs);
			String id = (String) circuit.get("Timer");
			timer=Integer.parseInt(id);
		}
		catch (FileNotFoundException e) {
           e.printStackTrace();
		}catch (IOException e) {
            e.printStackTrace();
		}catch (ParseException e) {
            e.printStackTrace();
    }
}
	
	
	/*
	 * Takes in the inputs, places them in inputs array and in signals array
	 */
	private void toInputs(JSONObject circuit){
		JSONArray inputs = (JSONArray) circuit.get("Inputs");
		numInputs=inputs.size();
		String id;
		Signal tmp;
		for (int i=0; i<numInputs; i++){
			id = (String) inputs.get(i);
			tmp=new Signal(Integer.parseInt(id));
			StateController.addInput(tmp);
			signals.add(StateController.getInput(i));
		}
	}
	
	/*
	 * update the signals based on current value of gates and inputs
	 * 
	 */
	public void updateSignals(){
		int i=0;
		while(i < numInputs){
			signals.set(i,StateController.getInput(i));
			i++;
		}
		for (int j=0; j<gates.size(); j++){
			gates.get(j).update();
		}
	}
	
	/*
	 * Sets which signals are the ouputs of the circuit
	 */
	private void signalOutputs(JSONArray outs){
		String id;
		for (int i=0; i<outputs.size(); i++){
			id=(String) outs.get(i);
			outputs.add(signals.get(Integer.parseInt(id)));
		}
	}
	
	
	/*
	 * Sets the desired outputs for the level
	 */
		private void desiredOutputs(JSONArray goal){
			for (int i=0; i< goal.size(); i++){
				String tmp = (String) goal.get(i);
				int n= Integer.parseInt(tmp);
				StateController.addOut(n);
			}
		
		}
		
		
	/*
	 * Builds the current stage of the level a gate at a time
	 */
	private void buildLevel(JSONObject level){
		JSONArray gate = (JSONArray) level.get("Gates");
		lvls.add(gate.size());
		JSONObject curGate;
		String type;
		JSONArray inputs;
		int size;
		for (int i=0; i<gate.size(); i++){
			curGate = (JSONObject) gate.get(i);
			type = (String) curGate.get("Type");
			inputs = (JSONArray) curGate.get("Inputs");
			size = inputs.size();
			buildGate(type, inputs, size);
		}
	}
	
	/*
	 * Builds the gate using created signals as inputs and creating new signals for the output
	 */
	private void buildGate(String gate,JSONArray inputs, int size){
		
		Gate next;
		String value ;
		List<Integer> sigs=new ArrayList<Integer>();
		for (int i=0; i<size; i++){
			value = (String) inputs.get(i);
			sigs.add(Integer.parseInt(value));
		}
		if (size==1){
			next=new NOTGate(signals.get(sigs.get(0)), sigs.get(0));
			gates.add(next);
			signals.add(next.output);
		}
		if (size==2){
				if (gate.equals("AND")){
					
					next=new ANDGate(signals.get(sigs.get(0)),signals.get(sigs.get(1)),sigs.get(0), sigs.get(1));
					gates.add(next);
					signals.add(next.output);
				}
				else if(gate.equals("NAND")){
					next=new NANDGate(signals.get(sigs.get(0)),signals.get(sigs.get(1)),sigs.get(0), sigs.get(1));
					gates.add(next);
					signals.add(next.output);
				}
				else if (gate.equals("OR")){
					next=new ORGate(signals.get(sigs.get(0)),signals.get(sigs.get(1)),sigs.get(0), sigs.get(1));
					gates.add(next);
					signals.add(next.output);
				}
				else if (gate.equals("NOR")){
					next=new NORGate(signals.get(sigs.get(0)),signals.get(sigs.get(1)),sigs.get(0), sigs.get(1));
					gates.add(next);
					signals.add(next.output);
				}
				else if (gate.equals("XOR")){
					next=new XORGate(signals.get(sigs.get(0)),signals.get(sigs.get(1)),sigs.get(0), sigs.get(1));
					gates.add(next);
					signals.add(next.output);
				}
				else if (gate.equals("XNOR")){
					next=new XNORGate(signals.get(sigs.get(0)),signals.get(sigs.get(1)),sigs.get(0), sigs.get(1));
					gates.add(next);
					signals.add(next.output);
				}else if (gate.equals("UNI")){
					next=new UniversalGate(signals.get(sigs.get(0)),signals.get(sigs.get(1)),sigs.get(0), sigs.get(1));
					gates.add(next);
					signals.add(next.output);
				}
			}
		else if (size==3){
				if (gate.equals("AND")){
					next=new ANDGate(signals.get(sigs.get(0)),signals.get(sigs.get(1)), signals.get(sigs.get(2)), sigs.get(0), sigs.get(1), sigs.get(2));
					gates.add(next);
					signals.add(next.output);
				}
				else if(gate.equals("NAND")){
					next=new NANDGate(signals.get(sigs.get(0)),signals.get(sigs.get(1)),signals.get(sigs.get(2)),sigs.get(0), sigs.get(1), sigs.get(2));
					gates.add(next);
					signals.add(next.output);

				}
				else if (gate.equals("OR")){
					next=new ORGate(signals.get(sigs.get(0)),signals.get(sigs.get(1)),signals.get(sigs.get(2)),sigs.get(0), sigs.get(1), sigs.get(2));
					gates.add(next);
					signals.add(next.output);

				}
				else if (gate.equals("NOR")){
					next=new NORGate(signals.get(sigs.get(0)),signals.get(sigs.get(1)), signals.get(sigs.get(2)), sigs.get(0), sigs.get(1), sigs.get(2));
					gates.add(next);
					signals.add(next.output);

				}
				else if (gate.equals("UNI")){
					next=new UniversalGate(signals.get(sigs.get(0)),signals.get(sigs.get(1)), signals.get(sigs.get(2)), sigs.get(0), sigs.get(1), sigs.get(2));
					gates.add(next);
					signals.add(next.output);
				}
		}
	}
	
	
	//Returns gate type for gate index n
	public GateType gateType(int n){return gates.get(n).type ;}
	
	//Returns gate of of index n
	public Gate getGate(int n){return gates.get(n);}
	
	//Returns signal of Gate n
	public Signal getGateOutput(int n) {return gates.get(n).output;}
	
	//Returns number of gates in the circuit stage
	public int numGates(int n){return lvls.get(n);}
	
	//Returns number of stages in circuit
	public int getNumLevels(){return numLevels;}
	
	//return the desired outputs of the level
	public Signal getOutput(int n){return outputs.get(n);}
	
	//Return the total number of gates
	public int getTotalGates(){return gates.size();}
	
	//Return the Level number
	public int getLevel(){return level;}
	
	//Return the total number of inputs
	public int getNumInputs(){return numInputs;}
	
	//Returns the signal of index n
	public Signal getSignal(int n){return signals.get(n);}
	
	//Returns the timer for this level
	public int getTime(){return timer;}
}