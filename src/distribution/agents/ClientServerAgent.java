package distribution.agents;

import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.introspection.AMSSubscriber;
import jade.domain.introspection.AddedContainer;
import jade.domain.introspection.Event;
import jade.domain.introspection.IntrospectionVocabulary;
import jade.domain.introspection.Occurred;
import jade.domain.introspection.RemovedContainer;
import jade.lang.acl.ACLMessage;

import java.util.Map;

import network.JadeUrlChecker;
import application.Application;
import distribution.ontology.AgentGUI_DistributionOntology;
import distribution.ontology.ClientRegister;
import distribution.ontology.ClientUnregister;
import distribution.ontology.PlatformAddress;
import distribution.ontology.PlatformTime;
import distribution.ontology.SlaveRegister;

public class ClientServerAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3947798460986588734L;
	
	private Ontology ontology = AgentGUI_DistributionOntology.getInstance();
	private Ontology ontologyJadeMgmt = JADEManagementOntology.getInstance();
	private Codec codec = new SLCodec();
	
	private PlatformTime myPlatformTime = new PlatformTime();
	private PlatformAddress myPlatform = new PlatformAddress();
	private PlatformAddress mainPlatform = new PlatformAddress();
	private AID mainPlatformAgent = null; 
	
	private ParallelBehaviour parBehaiv = null;

	
	@Override
	protected void setup() {
		super.setup();
		
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		getContentManager().registerOntology(ontologyJadeMgmt);
		
		// --- Define Platfornm-Info ----------------------
		JadeUrlChecker myURL = new JadeUrlChecker( this.getContainerController().getPlatformName() );
		myPlatform.setIp(myURL.getHostIP());
		myPlatform.setUrl(myURL.getHostName());
		myPlatform.setPort(myURL.getPort());
		myPlatform.setHttp4mtp( getAMS().getAddressesArray()[0] );
		
		// --- Define Main-Platform-Info ------------------
		myURL = Application.JadePlatform.MASmasterAddress;
		mainPlatform.setIp(myURL.getHostIP());
		mainPlatform.setUrl(myURL.getHostName());
		mainPlatform.setPort(myURL.getPort());
		mainPlatform.setHttp4mtp(myURL.getJADEurl4MTP());
		
		// --- Define Receiver of local Status-Info -------
		mainPlatformAgent = new AID("server.master" + "@" + myURL.getJADEurl(), AID.ISGUID );
		mainPlatformAgent.addAddresses(mainPlatform.getHttp4mtp());
		
		// --- Send 'Register'-Information ----------------
		ClientRegister reg = new ClientRegister();
		reg.setClientAddress( myPlatform );
		myPlatformTime.setTimeStampAsString( Long.toString(System.currentTimeMillis()) ) ;
		reg.setClientTime( myPlatformTime );
		this.sendMessage2MainServer(reg);
		
		// --- Add Main-Behaiviours -----------------------
		parBehaiv = new ParallelBehaviour(this,ParallelBehaviour.WHEN_ALL);
		parBehaiv.addSubBehaviour( new amsSubscriber() );
		parBehaiv.addSubBehaviour( new ReceiveBehaviour() );
		// --- Add Parallel Behaiviour --------------------
		this.addBehaviour(parBehaiv);
		
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
		
		// --- Stop Parallel-Behaiviour -------------------
		this.removeBehaviour(parBehaiv);
		
		// --- Send 'Unregister'-Information --------------
		ClientUnregister unReg = new ClientUnregister();
		this.sendMessage2MainServer(unReg);
		
	}

	
	private boolean sendMessage2MainServer( Concept agentAction ) {
		
		try {
			// --- Definition einer neuen 'Action' --------
			Action act = new Action();
			act.setActor(getAID());
			act.setAction(agentAction);

			// --- Nachricht zusammenbauen und ... --------
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setSender(getAID());
			msg.addReceiver(mainPlatformAgent);
			msg.setLanguage(codec.getName());
			msg.setOntology(ontology.getName());
			// msg.setContent(trig);

			// --- ... versenden --------------------------
			getContentManager().fillContent(msg, act);
			send(msg);			
			return true;

		} catch (CodecException e) {
			e.printStackTrace();
			return false;
		} catch (OntologyException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	// -----------------------------------------------------
	// --- Message-Receive-Behaiviour --- S T A R T --------
	// -----------------------------------------------------
	private class ReceiveBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -1701739199514787426L;

		@Override
		public void action() {
			
			Action act = null;
			Occurred occ = null;
			Concept agentAction = null; 
			AID senderAID = null;

			ACLMessage msg = myAgent.receive();			
			if (msg!=null) {
				
				if (msg.getPerformative()==ACLMessage.FAILURE) {
					// --- No Ontology-specific Message -------------
					act = null;
					System.out.println( "ACLMessage.FAILURE from " + msg.getSender().getName() + ": " + msg.getContent() );
				} else {
					// --- Ontology-specific Message ----------------
					try {
						ContentElement con = getContentManager().extractContent(msg);	
						if (con instanceof Action) {
							act = (Action) con;	
						} else if (con instanceof Occurred) {
							occ = (Occurred) con;
							// --- Messages in the context of Introspection ---
							// --- Not of any further interest (yet)-- --------
							// System.out.println( "++++++ Introspection: " + occ.toString() + "++++++" );
						} else {
							System.out.println( "=> " + myAgent.getName() + " - Unknown MessageType: " + con.toString() );
						}						
					} catch (UngroundedException e) {
						e.printStackTrace();
					} catch (CodecException e) {
						e.printStackTrace();
					} catch (OntologyException e) {
						e.printStackTrace();
					};
				}
				
				// --- Work on the Content-Msg ----------------------
				if (act!=null) {
					
					agentAction = act.getAction();
					senderAID = act.getActor();
					
					// ------------------------------------------------------------------
					// --- Fallunterscheidung AgentAction --- S T A R T -----------------
					// ------------------------------------------------------------------
					if (agentAction instanceof SlaveRegister) {
						
						
					} else {
						// --- Unknown AgentAction ------------
						System.out.println( "----------------------------------------------------" );
						System.out.println( myAgent.getLocalName() + ": Unknown Message-Type!" );
						System.out.println( agentAction.toString() );
					}
					// ------------------------------------------------------------------
					// --- Fallunterscheidung AgentAction --- E N D E -------------------
					// ------------------------------------------------------------------
				}
			}
			else {
				block();
			}			
		}
		
	}
	// -----------------------------------------------------
	// --- Message-Receive-Behaiviour --- E N D ------------
	// -----------------------------------------------------
	

	// -----------------------------------------------------
	// --- amsSubscriber-SubClass/Behaiviour --- S T A R T -
	// -----------------------------------------------------
	private class amsSubscriber extends AMSSubscriber {
		
		private static final long serialVersionUID = -4346695401399663561L;

		@SuppressWarnings("unchecked")
		@Override
		protected void installHandlers(Map handlers) {
			// ----------------------------------------------------------------
			EventHandler containerAddedHandler = new EventHandler() {
				private static final long serialVersionUID = -7426704911904579411L;
				@Override
				public void handle(Event event) {
					AddedContainer aCon = (AddedContainer) event;
					if (aCon.getContainer().getName().equalsIgnoreCase("Main-Container")==false) {
						Application.JadePlatform.MASremoteContainer.add(aCon.getContainer());
						//System.out.println( "Container hinzugefügt: " + aCon.getName() + " " + aCon.getContainer() + aCon );
					}
				}
			};
			handlers.put(IntrospectionVocabulary.ADDEDCONTAINER, containerAddedHandler);

			// ----------------------------------------------------------------
			EventHandler containerRemovedHandler = new EventHandler() {
				private static final long serialVersionUID = 8614456287558634409L;
				@Override
				public void handle(Event event) {
					RemovedContainer rCon = (RemovedContainer) event;
					Application.JadePlatform.MASremoteContainer.remove(rCon.getContainer());
					//System.out.println( "Container gelöscht: " + rCon.getName() + " " + rCon.getContainer()  );
				}
				
			};
			handlers.put(IntrospectionVocabulary.REMOVEDCONTAINER, containerRemovedHandler);
			
			// ----------------------------------------------------------------
		}
		
	}
	// -----------------------------------------------------
	// --- amsSubscriber-SubClass/Behaiviour --- E N D -----
	// -----------------------------------------------------

	
}
