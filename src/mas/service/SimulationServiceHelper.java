/**
 * 
 */
package mas.service;

import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.ServiceHelper;

import java.util.Date;
import java.util.Hashtable;

import mas.service.distribution.ontology.ClientRemoteContainerReply;
import mas.service.distribution.ontology.PlatformLoad;
import mas.service.distribution.ontology.RemoteContainerConfig;
import mas.service.load.LoadInformation.NodeDescription;
import mas.service.time.TimeModel;

/**
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public interface SimulationServiceHelper extends ServiceHelper {

	public static final String SERVICE_NAME = "mas.service.SimulationService";
	
	public static final String SERVICE_UPDATE_TIME_MODEL = "service-update-time-model";
	public static final String SERVICE_UPDATE_TIME_STEP = "service-update-time-step";
	public static final String SERVICE_UPDATE_ENVIRONMENT = "service-update-environment";
	public static final String SERVICE_UPDATE_SIMULATION = "service-update-simulation";
	
	
	// --- Methods for the load balancing ---------------------------
	public String startNewRemoteContainer() throws ServiceException;
	public String startNewRemoteContainer(RemoteContainerConfig remoteConfig) throws ServiceException;
	public RemoteContainerConfig getDefaultRemoteContainerConfig() throws ServiceException;
	
	public Hashtable<String, PlatformLoad> getContainerLoads() throws ServiceException;
	public PlatformLoad getContainerLoad(String containerName) throws ServiceException;
	
	public Hashtable<String, Location> getContainerLocations() throws ServiceException;
	public Location getContainerLocation(String containerName) throws ServiceException;
	
	public void putContainerDescription(ClientRemoteContainerReply crcReply) throws ServiceException;
	public Hashtable<String, NodeDescription> getContainerDescriptions() throws ServiceException;
	public NodeDescription getContainerDescription(String containerName) throws ServiceException;
	
	
//	public Hashtable<String, AID> getAgents() throws ServiceException;
//	public AID getAgent() throws ServiceException;
//	
//	public Hashtable<String, Integer> getNumberOfAgents() throws ServiceException;
//	public Integer getNumberOfAgentsInContainer() throws ServiceException;

	
	// --- Methods for simulations ----------------------------------
	public void notifySensors(String event) throws ServiceException;
	
	public void addSensor(Agent agentWithSensor) throws ServiceException;
	public void deleteSensor(Agent agentWithSensor) throws ServiceException;
	
	public void setManagerAgent(AID agentAddress) throws ServiceException; 
	public AID getManagerAgent() throws ServiceException;
	
	public void stepSimulation(Object envObjectInstance) throws ServiceException;
	
	public void setTimeModel(TimeModel newTimeModel) throws ServiceException;
	public TimeModel getTimeModel() throws ServiceException;		
	public void stepTimeModel() throws ServiceException;
	
	public void setEnvironmentInstance(Object envObjectInstance) throws ServiceException;
	public Object getEnvironmentInstance() throws ServiceException;
	
	
	// --- Methods for the general time synchronisation -------------
	public Date getTimeOfMainContainerAsDate();
	public Long getTimeOfMainContainerAsLong();
	
}
