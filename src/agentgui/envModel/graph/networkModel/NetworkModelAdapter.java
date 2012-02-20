/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.envModel.graph.networkModel;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import agentgui.envModel.graph.controller.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import edu.uci.ics.jung.graph.Graph;

/**
 * The Class NetworkModelAdapter is used for the action / interaction
 * with the NetworkModel in the context of the GUI.
 */
public class NetworkModelAdapter implements NetworkModelInterface {

	/** The graph controller. */
	private GraphEnvironmentController graphController = null;

	/**
	 * Instantiates a new network model action.
	 * @param controller the controller
	 */
	public NetworkModelAdapter(GraphEnvironmentController controller) {
		this.graphController = controller;
	}

	/**
	 * Returns the network model.
	 * @return the network model
	 */
	public NetworkModel getNetworkModel() {
		return this.graphController.getNetworkModel();
	}

	/**
	 * Sets a new network model.
	 * @param networkModel the new network model
	 */
	public void setNetworkModel(NetworkModel networkModel) {
		this.graphController.setEnvironmentModel(networkModel);
	}

	/**
	 * Notifies the connected observer of the GraphEnvironmentController.
	 * @param notification the notification
	 */
	private void notifyObserver(NetworkModelNotification notification) {
		this.graphController.notifyObservers(notification);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#setGeneralGraphSettings4MAS(agentgui.envModel.graph.controller.GeneralGraphSettings4MAS)
	 */
	@Override
	public void setGeneralGraphSettings4MAS(GeneralGraphSettings4MAS generalGraphSettings4MAS) {
		this.graphController.getNetworkModel().setGeneralGraphSettings4MAS(generalGraphSettings4MAS);
		this.graphController.validateNetworkComponentAndAgents2Start();
		this.graphController.setProjectUnsaved();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getGeneralGraphSettings4MAS()
	 */
	@Override
	public GeneralGraphSettings4MAS getGeneralGraphSettings4MAS() {
		return this.graphController.getNetworkModel().getGeneralGraphSettings4MAS();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#setAlternativeNetworkModel(java.util.HashMap)
	 */
	@Override
	public void setAlternativeNetworkModel(HashMap<String, NetworkModel> alternativeNetworkModel) {
		this.graphController.getNetworkModel().setAlternativeNetworkModel(alternativeNetworkModel);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getAlternativeNetworkModel()
	 */
	@Override
	public HashMap<String, NetworkModel> getAlternativeNetworkModel() {
		return this.graphController.getNetworkModel().getAlternativeNetworkModel();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getCopy()
	 */
	@Override
	public NetworkModel getCopy() {
		return this.graphController.getNetworkModel().getCopy();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getNetworkComponentsIDs(java.util.HashSet)
	 */
	@Override
	public HashSet<String> getNetworkComponentsIDs(HashSet<NetworkComponent> networkComponents) {
		return this.graphController.getNetworkModel().getNetworkComponentsIDs(networkComponents);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getGraphElement(java.lang.String)
	 */
	@Override
	public GraphElement getGraphElement(String id) {
		return this.graphController.getNetworkModel().getGraphElement(id);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getGraphElements()
	 */
	@Override
	public HashMap<String, GraphElement> getGraphElements() {
		return this.graphController.getNetworkModel().getGraphElements();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getGraphElementsFromNetworkComponent(agentgui.envModel.graph.networkModel.NetworkComponent)
	 */
	@Override
	public Vector<GraphElement> getGraphElementsFromNetworkComponent(NetworkComponent networkComponent) {
		return this.graphController.getNetworkModel().getGraphElementsFromNetworkComponent(networkComponent);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getGraph()
	 */
	@Override
	public Graph<GraphNode, GraphEdge> getGraph() {
		return this.graphController.getNetworkModel().getGraph();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#setGraph(edu.uci.ics.jung.graph.Graph)
	 */
	@Override
	public void setGraph(Graph<GraphNode, GraphEdge> newGraph) {
		this.graphController.getNetworkModel().setGraph(newGraph);
	}

	/**
	 * Reloads the NetworModel.
	 */
	public void reLoadNetworkModel() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Reload);
		this.notifyObserver(notification);
	}

	/**
	 * Refreshes the NetworkModel visualization.
	 */
	public void refreshNetworkModel() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Repaint);
		this.notifyObserver(notification);
	}

	/**
	 * Zoom fit to window.
	 */
	public void zoomFit2Window() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Zoom_Fit2Window);
		this.notifyObserver(notification);
	}

	/**
	 * Zoom to the original size.
	 */
	public void zoomOne2One() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Zoom_One2One);
		this.notifyObserver(notification);
	}

	/**
	 * Zoom in.
	 */
	public void zoomIn() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Zoom_In);
		this.notifyObserver(notification);
	}

	/**
	 * Zoom out.
	 */
	public void zoomOut() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Zoom_Out);
		this.notifyObserver(notification);
	}

	/**
	 * Sets the graph mouse to transforming mode.
	 */
	public void setGraphMouseTransforming() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_GraphMouse_Transforming);
		this.notifyObserver(notification);
	}

	/**
	 * Sets the graph mouse to picking mode.
	 */
	public void setGraphMousePicking() {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_GraphMouse_Picking);
		this.notifyObserver(notification);
	}

	/**
	 * Can be used in order to select a NetworkComponent.
	 * @param networkComponent the network component
	 */
	public void selectNetworkComponent(NetworkComponent networkComponent) {
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Select);
		notification.setInfoObject(networkComponent);
		this.notifyObserver(notification);
	}

	/**
	 * Clears the current NetworModel.
	 */
	public void clearNetworkModel() {
		this.graphController.setEnvironmentModel(null);
		this.graphController.getAgents2Start().clear();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#addNetworkComponent(agentgui.envModel.graph.networkModel.NetworkComponent)
	 */
	@Override
	public NetworkComponent addNetworkComponent(NetworkComponent networkComponent) {

		NetworkComponent newComponent = this.graphController.getNetworkModel().addNetworkComponent(networkComponent);
		this.graphController.addAgent(networkComponent);

		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Added);
		notification.setInfoObject(newComponent);
		this.notifyObserver(notification);

		return newComponent;
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#renameComponent(java.lang.String, java.lang.String)
	 */
	@Override
	public void renameComponent(String oldCompID, String newCompID) {
		this.graphController.getNetworkModel().renameComponent(oldCompID, newCompID);
		this.graphController.renameAgent(oldCompID, newCompID);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#removeNetworkComponent(agentgui.envModel.graph.networkModel.NetworkComponent)
	 */
	@Override
	public void removeNetworkComponent(NetworkComponent networkComponent) {

		this.graphController.getNetworkModel().removeNetworkComponent(networkComponent);
		this.graphController.removeAgent(networkComponent);

		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Removed);
		notification.setInfoObject(networkComponent);
		this.notifyObserver(notification);

	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#removeNetworkComponents(java.util.HashSet)
	 */
	@Override
	public HashSet<NetworkComponent> removeNetworkComponents(HashSet<NetworkComponent> networkComponents) {

		HashSet<NetworkComponent> removedComponents = this.graphController.getNetworkModel().removeNetworkComponents(networkComponents);
		for (NetworkComponent networkComponent : removedComponents) {

			this.graphController.removeAgent(networkComponent);

			NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Removed);
			notification.setInfoObject(networkComponent);
			this.notifyObserver(notification);
		}
		return removedComponents;
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#mergeNodes(agentgui.envModel.graph.networkModel.GraphNode, agentgui.envModel.graph.networkModel.GraphNode)
	 */
	@Override
	public boolean mergeNodes(GraphNode node1, GraphNode node2) {
		boolean merged = this.graphController.getNetworkModel().mergeNodes(node1, node2);
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Nodes_Merged);
		this.notifyObserver(notification);
		return merged;
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#splitNetworkModelAtNode(agentgui.envModel.graph.networkModel.GraphNode)
	 */
	@Override
	public void splitNetworkModelAtNode(GraphNode node2SplitAt) {
		this.graphController.getNetworkModel().splitNetworkModelAtNode(node2SplitAt);
		NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Nodes_Splited);
		this.notifyObserver(notification);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getNodesFromNetworkComponent(agentgui.envModel.graph.networkModel.NetworkComponent)
	 */
	@Override
	public Vector<GraphNode> getNodesFromNetworkComponent(NetworkComponent networkComponent) {
		return this.graphController.getNetworkModel().getNodesFromNetworkComponent(networkComponent);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getNetworkComponent(java.lang.String)
	 */
	@Override
	public NetworkComponent getNetworkComponent(String id) {
		return this.graphController.getNetworkModel().getNetworkComponent(id);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getNeighbourNetworkComponents(java.util.HashSet)
	 */
	@Override
	public HashSet<NetworkComponent> getNeighbourNetworkComponents(HashSet<NetworkComponent> networkComponents) {
		return this.graphController.getNetworkModel().getNeighbourNetworkComponents(networkComponents);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getNeighbourNetworkComponents(agentgui.envModel.graph.networkModel.NetworkComponent)
	 */
	@Override
	public Vector<NetworkComponent> getNeighbourNetworkComponents(NetworkComponent networkComponent) {
		return this.graphController.getNetworkModel().getNeighbourNetworkComponents(networkComponent);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getNetworkComponent(agentgui.envModel.graph.networkModel.GraphEdge)
	 */
	@Override
	public NetworkComponent getNetworkComponent(GraphEdge graphEdge) {
		return this.graphController.getNetworkModel().getNetworkComponent(graphEdge);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getNetworkComponents(java.util.Set)
	 */
	@Override
	public HashSet<NetworkComponent> getNetworkComponents(Set<GraphNode> graphNodes) {
		return this.graphController.getNetworkModel().getNetworkComponents(graphNodes);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getNetworkComponents(agentgui.envModel.graph.networkModel.GraphNode)
	 */
	@Override
	public HashSet<NetworkComponent> getNetworkComponents(GraphNode graphNode) {
		return this.graphController.getNetworkModel().getNetworkComponents(graphNode);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getNetworkComponents()
	 */
	@Override
	public HashMap<String, NetworkComponent> getNetworkComponents() {
		return this.graphController.getNetworkModel().getNetworkComponents();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#setNetworkComponents(java.util.HashMap)
	 */
	@Override
	public void setNetworkComponents(HashMap<String, NetworkComponent> networkComponents) {
		this.graphController.getNetworkModel().setNetworkComponents(networkComponents);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#nextNetworkComponentID()
	 */
	@Override
	public String nextNetworkComponentID() {
		return this.graphController.getNetworkModel().nextNetworkComponentID();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#nextNodeID()
	 */
	@Override
	public String nextNodeID() {
		return this.graphController.getNetworkModel().nextNodeID();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getShiftedPosition(agentgui.envModel.graph.networkModel.GraphNode, agentgui.envModel.graph.networkModel.GraphNode)
	 */
	@Override
	public Point2D getShiftedPosition(GraphNode fixedNode, GraphNode shiftNode) {
		return this.graphController.getNetworkModel().getShiftedPosition(fixedNode, shiftNode);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getNetworkComponentVectorWithDistributionNodeAsLast(java.util.HashSet)
	 */
	@Override
	public Vector<NetworkComponent> getNetworkComponentVectorWithDistributionNodeAsLast(HashSet<NetworkComponent> componentVector) {
		return this.graphController.getNetworkModel().getNetworkComponentVectorWithDistributionNodeAsLast(componentVector);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#containsDistributionNode(java.util.HashSet)
	 */
	@Override
	public NetworkComponent containsDistributionNode(HashSet<NetworkComponent> components) {
		return this.graphController.getNetworkModel().containsDistributionNode(components);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getClusterComponents()
	 */
	@Override
	public ArrayList<ClusterNetworkComponent> getClusterComponents() {
		return this.graphController.getNetworkModel().getClusterComponents();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getClusterComponents(java.util.Collection)
	 */
	@Override
	public ArrayList<ClusterNetworkComponent> getClusterComponents(Collection<NetworkComponent> components) {
		return this.graphController.getNetworkModel().getClusterComponents(components);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#isStarGraphElement(agentgui.envModel.graph.networkModel.NetworkComponent)
	 */
	@Override
	public boolean isStarGraphElement(NetworkComponent comp) {
		return this.graphController.getNetworkModel().isStarGraphElement(comp);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#isCenterNodeOfStar(agentgui.envModel.graph.networkModel.GraphNode, agentgui.envModel.graph.networkModel.NetworkComponent)
	 */
	@Override
	public boolean isCenterNodeOfStar(GraphNode node, NetworkComponent comp) {
		return this.graphController.getNetworkModel().isCenterNodeOfStar(node, comp);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#isFreeGraphNode(agentgui.envModel.graph.networkModel.GraphNode)
	 */
	@Override
	public boolean isFreeGraphNode(GraphNode graphNode) {
		return this.graphController.getNetworkModel().isFreeGraphNode(graphNode);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#replaceComponentsByCluster(java.util.HashSet)
	 */
	@Override
	public ClusterNetworkComponent replaceComponentsByCluster(HashSet<NetworkComponent> networkComponents) {
		return this.graphController.getNetworkModel().replaceComponentsByCluster(networkComponents);
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkModelInterface#getOuterNetworkComponentIDs()
	 */
	@Override
	public ArrayList<String> getOuterNetworkComponentIDs() {
		return this.graphController.getNetworkModel().getOuterNetworkComponentIDs();
	}

}