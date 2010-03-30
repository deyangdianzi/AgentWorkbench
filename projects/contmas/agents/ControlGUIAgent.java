/**
 * @author Hanno - Felix Wagner Copyright 2010 Hanno - Felix Wagner This file is
 *         part of ContMAS. ContMAS is free software: you can redistribute it
 *         and/or modify it under the terms of the GNU Lesser General Public
 *         License as published by the Free Software Foundation, either version
 *         3 of the License, or (at your option) any later version. ContMAS is
 *         distributed in the hope that it will be useful, but WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *         License for more details. You should have received a copy of the GNU
 *         Lesser General Public License along with ContMAS. If not, see
 *         <http://www.gnu.org/licenses/>.
 */

package contmas.agents;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.SniffOn;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Iterator;
import jade.util.leap.List;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import javax.swing.DefaultListModel;
import javax.swing.JDesktopPane;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import contmas.behaviours.getHarbourSetup;
import contmas.behaviours.getOntologyRepresentation;
import contmas.behaviours.requestStartAgent;
import contmas.behaviours.subscribeToDF;
import contmas.main.AgentDesktop;
import contmas.main.ControlGUI;
import contmas.main.DomainOntologyElement;
import contmas.main.OntologyElement;
import contmas.ontology.*;

public class ControlGUIAgent extends GuiAgent implements OntRepRequester{

	class listenForLogMessage extends CyclicBehaviour{
		/**
		 * 
		 */
		private static final long serialVersionUID=3580181910527477281L;
		MessageTemplate loggingTemplate=MessageTemplate.MatchTopic(((ControlGUIAgent) this.myAgent).loggingTopic);

		/**
		 * @param controlGUIAgent
		 */
		public listenForLogMessage(ControlGUIAgent myCAgent){
			super(myCAgent);
		}

		/**
		 * 
		 */

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action(){
			ACLMessage logMsg=ControlGUIAgent.this.receive(this.loggingTemplate);
			if(logMsg != null){
				String content=logMsg.getContent();
				((ControlGUIAgent) this.myAgent).writeLogMsg(content);
			}else{
				this.block();
			}

		}
	}

	@Override
	public void processOntologyRepresentation(ContainerHolder ontRep,AID agent){
		if(ontRep == null){
			return;
		}
		XMLCodec xmlCodec=new XMLCodec();
		String s;
		try{
			s=xmlCodec.encodeObject(ContainerTerminalOntology.getInstance(),ontRep,true);
			this.myGui.printOntRep(s);
		}catch(CodecException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(OntologyException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.myGui.setOntRepAgent(agent.getLocalName());
		DefaultListModel containerList=new DefaultListModel();
		Iterator allContStates=ontRep.getAllContainer_states();
		List allContsList=ontRep.getContains().getIs_filled_with();
		while(allContStates.hasNext()){
			TOCHasState contState=(TOCHasState) allContStates.next();
			String administeredContBic=contState.getSubjected_toc().getTransports().getBic_code();
			String dispString=administeredContBic;
			dispString+=" - " + contState.getState().getClass().getSimpleName();
			Iterator allConts=allContsList.iterator();
			Boolean found=false;
			while(allConts.hasNext()){
				found=false;
				BlockAddress curCont=(BlockAddress) allConts.next();
				if(curCont.getLocates().getBic_code().equals(administeredContBic)){
					dispString+=" (x=" + curCont.getX_dimension() + ", y=" + curCont.getY_dimension() + ", z=" + curCont.getZ_dimension() + ")";
					found=true;
				}
			}
			if(found == false){
				dispString+=" [ERROR]";
			}
			containerList.addElement(dispString);
		}
		this.myGui.setContainerList(containerList);
	}

	private static final long serialVersionUID= -7176620366025244274L;
	private JDesktopPane canvas=null;
	public ControlGUI myGui=null;
	private AID harbourMaster=null;
	private TopicManagementHelper tmh;
	private AID loggingTopic=null;
	private AID sniffer=null;

	@Override
	protected void onGuiEvent(GuiEvent ev){
		int command=ev.getType();
		if(command == 1){ //create new ship
			AgentContainer c=this.getContainerController();
			try{
				String name=ev.getParameter(0).toString();
				//	            AgentController a = c.createNewAgent(name , "contmas.agents.ShipAgent", null );
				Class selectedContainerHolderType=(Class) ev.getParameter(7);
				ContainerHolder ontologyRepresentation=(ContainerHolder) selectedContainerHolderType.newInstance();

				if((ev.getParameter(1).toString() != "") && (ev.getParameter(1).toString() != "") && (ev.getParameter(1).toString() != "")){
					BayMap loadBay=new BayMap();
					loadBay.setX_dimension(Integer.parseInt(ev.getParameter(1).toString()));
					loadBay.setY_dimension(Integer.parseInt(ev.getParameter(2).toString()));
					loadBay.setZ_dimension(Integer.parseInt(ev.getParameter(3).toString()));
					ontologyRepresentation.setContains(loadBay);
				}
				if(ontologyRepresentation instanceof Ship){
					((Ship) ontologyRepresentation).setLength(Float.parseFloat(ev.getParameter(6).toString()));
				}
				DomainOntologyElement habitat=((DomainOntologyElement) ev.getParameter(8));
				ontologyRepresentation.setLives_in(habitat.getDomain());
				if(ontologyRepresentation instanceof ActiveContainerHolder){
					Object[] capabilities=((Object[]) ev.getParameter(9));
					for(int i=0;i < capabilities.length;i++){
						DomainOntologyElement domainOntologyElement=(DomainOntologyElement) capabilities[i];
						((ActiveContainerHolder) ontologyRepresentation).addCapable_of(domainOntologyElement.getDomain());
					}
				}
				/*
				//				habitat.setLies_in(terminalArea);
				AgentController a=c.acceptNewAgent(name,new ShipAgent(ontologyRepresentation));
				a.start();
				*/
				StartNewContainerHolder act=new StartNewContainerHolder();
				act.setName(name);
				act.setTo_be_added(ontologyRepresentation);
				act.setRandomize(Boolean.parseBoolean(ev.getParameter(4).toString()));
				act.setPopulate(Boolean.parseBoolean(ev.getParameter(5).toString()));
				this.addBehaviour(new requestStartAgent(this,harbourMaster,act));

				/*
				AID address=new AID();
				address.setName(a.getName());
				SniffOn agact=new SniffOn();
				agact.addSniffedAgents(address);
				agact.setSniffer(sniffer);
				ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
				msg.setLanguage(new SLCodec().getName());
				msg.setOntology(JADEManagementOntology.getInstance().getName());
				getContentManager().registerLanguage(new SLCodec());
				getContentManager().registerOntology(JADEManagementOntology.getInstance());
				Action act=new Action(sniffer,agact);
				getContentManager().fillContent(msg,act);
				msg.addReceiver(sniffer);
				send(msg);
				*/
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(command == 2){ // get ontologyRepresentation
			AID inQuestion=new AID();
			inQuestion.setLocalName(ev.getParameter(0).toString());

			this.addBehaviour(new getOntologyRepresentation(this,inQuestion,harbourMaster));
		}else if(command == -1){ // GUI closed
			this.doDelete();
		}

	}

	@Override
	protected void setup(){
		// Instanciate the gui

		this.myGui=new ControlGUI(this);

		AgentDesktop ad=new AgentDesktop("ContMAS");
		this.canvas=ad.getDesktopPane();
		this.myGui.displayOn(this.canvas);
		this.myGui.setVisible(true);
		if(ad.getStandaloneMode() == AgentDesktop.AGENTDESKTOP_STANDALONE){
			this.myGui.getCanvas().getParent().getParent().getParent().getParent().setSize(this.myGui.getWidth() + 10,this.myGui.getHeight() + 30);
		}

		try{
			this.tmh=(TopicManagementHelper) this.getHelper(TopicManagementHelper.SERVICE_NAME);
			this.loggingTopic=this.tmh.createTopic("container-harbour-logging");
			this.tmh.register(this.loggingTopic);
		}catch(ServiceException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		this.addBehaviour(new listenForLogMessage(this));

		try{
			this.addBehaviour(new subscribeToDF(this,this.getClass().getMethod("updateAgentTree",List.class),"container-handling"));
		}catch(SecurityException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch(NoSuchMethodException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		AgentContainer c=this.getContainerController();
		try{
			AgentController a=c.createNewAgent("RandomGenerator","contmas.agents.RandomGeneratorAgent",null);
			a.start();
			a=c.createNewAgent("HarborMaster","contmas.agents.HarborMasterAgent",null);
			a.start();
			this.harbourMaster=new AID();
			this.harbourMaster.setName(a.getName());
			a=c.createNewAgent("Sniffer","jade.tools.sniffer.Sniffer",new Object[] {"Yard;StraddleCarrier;Apron;Crane-#2;Crane-#1"});
			a.start();
			this.sniffer=new AID();
			this.sniffer.setName(a.getName());
		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.addBehaviour(new getHarbourSetup(this,harbourMaster));

	}

	@Override
	protected void takeDown(){
		this.myGui.dispose();
		try{
			this.tmh.deregister(this.loggingTopic); //does it work?
		}catch(ServiceException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateAgentTree(List newAgents){
		this.myGui.updateAgentTree(newAgents);
	}

	/**
	 * @param content
	 */
	public void writeLogMsg(String content){
		this.myGui.writeLogMsg(content);
	}

	/**
	 * @param current_harbour_layout
	 */
	public void processHarbourLayout(Domain current_harbour_layout){
		XMLCodec xmlCodec=new XMLCodec();
		String s;
		try{
			s=xmlCodec.encodeObject(ContainerTerminalOntology.getInstance(),current_harbour_layout,true);
			this.myGui.printOntRep(s);
		}catch(CodecException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(OntologyException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DomainOntologyElement root=renderDomain(current_harbour_layout);

		this.myGui.displayHarbourLayout(root);

//		this.myGui.displayHarbourLayout(current_harbour_layout);
	}

	private DomainOntologyElement renderDomain(Domain dohmein){
		Iterator agentIter=dohmein.getAllHas_subdomains();
		DomainOntologyElement root=new DomainOntologyElement(dohmein);//dohmein.getClass().getSimpleName()+" - "+dohmein.getId());
		DomainOntologyElement domNode=null;

		while(agentIter.hasNext()){
			Domain curDom=(Domain) agentIter.next();
			domNode=renderDomain(curDom);
			root.add(domNode);
		}
		return root;
	}
}