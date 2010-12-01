package sma.agents;

import java.util.Vector;

import agentgui.physical2Denvironment.behaviours.MoveToPointBehaviour;
import agentgui.physical2Denvironment.behaviours.ReleasePassiveObjectBehaviour;
import agentgui.physical2Denvironment.behaviours.TakePassiveObjectBehaviour;
import agentgui.physical2Denvironment.ontology.ActiveObject;
import agentgui.physical2Denvironment.ontology.Position;
import agentgui.physical2Denvironment.provider.EnvironmentProviderHelper;
import agentgui.physical2Denvironment.provider.EnvironmentProviderService;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;

public class Carrier extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7126181085399437483L;

	private Vector<String> containerNames;
	
	private Vector<Position> storagePositions;
	
	private Position wp1;
	
	private Position wp2;
	
	public void setup(){
		String name = this.getLocalName();
		containerNames = new Vector<String>();
		if(name.equals("carrier1")){
			containerNames.add("container5");
			containerNames.add("container6");
			containerNames.add("container1");
			containerNames.add("container2");
		}else if(name.equals("carrier2")){
			containerNames.add("container8");
			containerNames.add("container7");
			containerNames.add("container4");
			containerNames.add("container3");
		}
		
		Position pos1 = new Position();
		Position pos2 = new Position();
		Position pos3 = new Position();
		Position pos4 = new Position();
		
		storagePositions = new Vector<Position>();
		if(name.equals("carrier1")){
			pos1.setXPos(49);
			pos1.setYPos(12);
			
			pos2.setXPos(46);
			pos2.setYPos(12);
			
			pos3.setXPos(43);
			pos3.setYPos(12);
			
			pos4.setXPos(40);
			pos4.setYPos(12);
		}else if(name.equals("carrier2")){
			pos1.setXPos(49);
			pos1.setYPos(22);
			
			pos2.setXPos(46);
			pos2.setYPos(22);
			
			pos3.setXPos(43);
			pos3.setYPos(22);
			
			pos4.setXPos(40);
			pos4.setYPos(22);
		}
		
		storagePositions.add(pos1);
		storagePositions.add(pos2);
		storagePositions.add(pos3);
		storagePositions.add(pos4);
		
		wp1 = new Position();
		wp1.setXPos(30);
		wp1.setYPos(17);
		
		wp2 = new Position();
		wp2.setXPos(39);
		wp2.setYPos(17);
		
		SequentialBehaviour CarrierBehaviour = new SequentialBehaviour();
		
		CarrierBehaviour.addSubBehaviour(new MoveContainerBehaviour(containerNames.get(0), storagePositions.get(0)));
		CarrierBehaviour.addSubBehaviour(new MoveContainerBehaviour(containerNames.get(1), storagePositions.get(1)));
		CarrierBehaviour.addSubBehaviour(new MoveContainerBehaviour(containerNames.get(2), storagePositions.get(2)));
		CarrierBehaviour.addSubBehaviour(new MoveContainerBehaviour(containerNames.get(3), storagePositions.get(3)));
		
		addBehaviour(CarrierBehaviour);
		
		
	}
	
	class MoveContainerBehaviour extends SequentialBehaviour{
		/**
		 * 
		 */
		private static final long serialVersionUID = -5777336143775878473L;

		public MoveContainerBehaviour(String containerID, Position storagePos){
			
			EnvironmentProviderHelper helper;
			try {
				helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
				Position contPos = helper.getObject(containerID).getPosition();
				float speed = ((ActiveObject)helper.getObject(getLocalName())).getMaxSpeed();
				
				this.addSubBehaviour(new MoveToPointBehaviour(Carrier.this, contPos, speed));
				this.addSubBehaviour(new TakePassiveObjectBehaviour(containerID));
				this.addSubBehaviour(new MoveToPointBehaviour(Carrier.this, wp1, speed));
				this.addSubBehaviour(new MoveToPointBehaviour(Carrier.this, wp2, speed));
				this.addSubBehaviour(new MoveToPointBehaviour(Carrier.this, storagePos, speed));
				this.addSubBehaviour(new ReleasePassiveObjectBehaviour(containerID));
				this.addSubBehaviour(new MoveToPointBehaviour(Carrier.this, wp2, speed));
				this.addSubBehaviour(new MoveToPointBehaviour(Carrier.this, wp1, speed));
				
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}

}