package Controller;

import java.util.ArrayList;
import java.util.List;

import Model.Circuit;
import Model.GateType;
import Model.Signal;

public class PlayingMode implements State {
	
	private List<Signal> inputs = new ArrayList<Signal>();
	private static List<Integer> outputs = new ArrayList<Integer>();
	private Circuit circuit;

	
	public PlayingMode(){}
	
	public void createCircuit(int n){
		outputs.clear();		
		circuit=new Circuit(n);
		}
	
	
	void changeInput(int n){
	}

	@Override
	public void changeState() {
		StateController.setState();
	}
	
	/*
	 * These functions are getters and setters for the inputs and outputs of the circuit 
	 * as well as the getters and setters of the circuit
	 * 
	 * All of these functions are accessible through the StateController
	 */
	protected void addInput(Signal s){inputs.add(s);}
	protected void setInput(int i, Signal s){inputs.set(i,s);}
	protected Signal getInput(int i){return inputs.get(i);}
	protected void addOutput(int i){outputs.add(i);}
	protected int getOutput(int i){return outputs.get(i);}
	protected int getNumInputs(){return inputs.size();}
	protected void togGate(int n){circuit.getGate(n).toggleGate();}
	protected void update(){circuit.updateSignals();}
	protected boolean isUni(int n){return circuit.getGate(n).isUNI();}
	protected int getTime(){return circuit.getTime();}
	protected int getTotalGates(){return circuit.getTotalGates();}
	protected int getLevel(){return circuit.getLevel();}
	protected int getGateOutput(int i){return circuit.getGateOutput(i).getValue();}
	protected int getNumLevels(){return circuit.getNumLevels();}
	protected int getNumGates(int n){return circuit.numGates(n);}
	protected int getSignal(int n){ return circuit.getSignal(n).getValue();}
	protected GateType getGateType(int n) {return circuit.getGate(n).gateType();}
	protected int getTop(int n){return circuit.getGate(n).getTop();}
	protected int getBottom(int n){return circuit.getGate(n).getBottom();}
}