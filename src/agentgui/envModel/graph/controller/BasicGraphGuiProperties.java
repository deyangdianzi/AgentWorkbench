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
package agentgui.envModel.graph.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import agentgui.core.application.Language;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;

/**
 * The Class BasicGraphGuiProperties.
 */
public class BasicGraphGuiProperties extends JInternalFrame implements Observer, ActionListener {

	private static final long serialVersionUID = -868257113588339559L;

	private final String pathImage = GraphGlobals.getPathImages();
	
	private GraphEnvironmentController graphController = null;
	private BasicGraphGuiJDesktopPane graphDesktop = null;
	
	private Object graphObject = null;
	private NetworkComponent networkComponent = null;
	private NetworkComponentAdapter networkComponentAdapter = null;
	private NetworkComponentAdapter4DataModel adapter4DataModel = null;
	
	private JPanel jContentPane = null;  
	private JToolBar jJToolBarBarNorth = null;
	private JToolBarButton jToolBarButtonSave = null;
	private JToolBarButton jToolBarButtonSaveAndExit = null;
	private JComponent jComponentContent = null;


	/**
	 * Instantiates a new basic graph gui properties.
	 * @param graphController the graph controller
	 */
	public BasicGraphGuiProperties(GraphEnvironmentController graphController, BasicGraphGuiJDesktopPane desktop, Object graphObject) {
		this.graphController = graphController;
		this.graphController.addObserver(this);
		this.graphDesktop    = desktop;
		this.graphObject     = graphObject;
		this.initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setAutoscrolls(true);
		this.setMaximizable(true);
		this.setResizable(true);
		this.setIconifiable(true);
		
		this.setClosable(true);
		this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Component");
		this.setSize(new Dimension(230, 450));
		
		BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI();
		ui.getNorthPane().remove(0);
		
		this.configureForGraphObject();
		this.setContentPane(getJContentPane());
		this.setVisible(true);
		
		this.graphDesktop.add(this, JDesktopPane.PALETTE_LAYER);
		this.graphDesktop.registerBasicGraphGuiProperties(this);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JInternalFrame#dispose()
	 */
	@Override
	public void dispose() {
		this.graphDesktop.unregisterBasicGraphGuiProperties(this);
		super.dispose();
	}

	/**
	 * Returns the graph object.
	 * @return the graphObject
	 */
	public Object getGraphObject() {
		return graphObject;
	}
	/**
	 * Sets the graph object.
	 * @param graphObject the graphObject to set
	 */
	public void setGraphObject(Object graphObject) {
		this.graphObject = graphObject;
	}
	
	/**
	 * Configure for graph object.
	 */
	private void configureForGraphObject() {

		if (graphObject instanceof GraphNode) {
			// --- Is the GraphNode a DistributionNode ? ------------
			this.networkComponent = this.graphController.getNetworkModel().isDistributionNode((GraphNode) this.graphObject);
			if (this.networkComponent==null) {
				// --- Check for central GraphNode element ----------
				// --- or single outer GraphNodes		   ----------
				HashSet<NetworkComponent> netComps = this.graphController.getNetworkModel().getNetworkComponents((GraphNode) this.graphObject);
				if (netComps.size()==1) {
					this.networkComponent = netComps.iterator().next();
				}
			}
			
		} else if (graphObject instanceof GraphEdge) {
			// --- Just get the corresponding NetworkComponent ------ 
			this.networkComponent = this.graphController.getNetworkModel().getNetworkComponent((GraphEdge) this.graphObject);
			
		} else if (graphObject instanceof NetworkComponent) {
			// --- Cast to NetworkComponent -------------------------
			this.networkComponent = this.graphController.getNetworkModel().getNetworkComponent(((NetworkComponent) this.graphObject).getId());
			
		}
		
		// --- NetworkComponent selected ? -------------------------- 
		if (this.networkComponent==null) {
			String msgHead = Language.translate("Nicht eindeutige Komponentenauswahl !");
			String msgText = Language.translate("Bitte w�hlen Sie eine einzelne Netzwerkkomponente !");			
			JOptionPane.showMessageDialog(null, msgText, msgHead, JOptionPane.WARNING_MESSAGE);
			this.dispose();
			return;
		}
		
		// --- Get the corresponding NetworkComponentAdapter -------- 
		this.networkComponentAdapter = this.graphController.getNetworkModel().getNetworkComponentAdapter(this.networkComponent);

		// --- Mark / Select NetworkComponent for user --------------
		NetworkModelNotification nmn = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Select);
		nmn.setInfoObject(this.networkComponent);
		this.graphController.notifyObservers(nmn);

		// --- Some layout stuff ----------------------------------- 
		this.setTitle("Comp.: " + this.networkComponent.getId() + " (" +  this.networkComponent.getType() + ")");

	}
	
	/**
	 * This method initializes jPanelContent	
	 * @return javax.swing.JPanel	
	 */
	private JComponent getJPanelContent() {
		if (this.jComponentContent==null) {
			
			if (this.networkComponentAdapter== null) {
				this.getJToolBarButtonSave().setEnabled(false);
				this.getJToolBarButtonSaveAndExit().setEnabled(false);
				
			} else {
				this.adapter4DataModel = this.networkComponentAdapter.invokeGetDataModelAdapter();
				if (this.adapter4DataModel == null) {
					this.getJToolBarButtonSave().setEnabled(false);
					this.getJToolBarButtonSaveAndExit().setEnabled(false);
					
				} else {
					Object dataModel = null;
					// --- Get the Base64 encoded Vector<String> ---- 
					Vector<String> dataModelBase64 = this.networkComponent.getDataModelBase64();
					if (dataModelBase64!=null) {
    					// --- Get Base64 decoded Object ------------
    					dataModel = this.adapter4DataModel.getDataModelBase64Decoded(dataModelBase64);
    					this.networkComponent.setDataModel(dataModel);
	    			}
					
					this.adapter4DataModel.setDataModel(this.networkComponent.getDataModel());
					JComponent visualisation = this.adapter4DataModel.getVisualisationComponent();
					visualisation.validate();
					this.jComponentContent = visualisation;
				}
			}
		}
		return this.jComponentContent;
	}
	
	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(this.getJJToolBarBarNorth(), BorderLayout.NORTH);
			JComponent dataContent = this.getJPanelContent();
			if (dataContent!=null) {
				jContentPane.add(dataContent, BorderLayout.CENTER);	
			}
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes jJToolBarBarNorth	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJJToolBarBarNorth() {
		if (jJToolBarBarNorth == null) {
			jJToolBarBarNorth = new JToolBar("Properties Bar");
			jJToolBarBarNorth.setFloatable(false);
			jJToolBarBarNorth.setRollover(true);
			jJToolBarBarNorth.add(this.getJToolBarButtonSave());
			jJToolBarBarNorth.add(this.getJToolBarButtonSaveAndExit());
		}
		return jJToolBarBarNorth;
	}
	
	/**
	 * Returns the JToolBarButton for the save action.
	 * @return the JToolBarButton for the save action
	 */
	private JToolBarButton getJToolBarButtonSave() {
		if (this.jToolBarButtonSave==null) {
			this.jToolBarButtonSave=new JToolBarButton("Save", Language.translate("Save", Language.EN), null, "MBsave.png", this);
		}
		return this.jToolBarButtonSave;
	}
	
	/**
	 * Returns the JToolBarButton for the save and exit action.
	 * @return the JToolBarButton for the save and exit action
	 */
	private JToolBarButton getJToolBarButtonSaveAndExit() {
		if (this.jToolBarButtonSaveAndExit==null) {
			this.jToolBarButtonSaveAndExit=new JToolBarButton("SaveAndExit", Language.translate("Save and Exit", Language.EN), null, "MBsaveAndExit.png", this);
		}
		return this.jToolBarButtonSaveAndExit;
	}
	
	/**
	 * The Class JToolBarButton.
	 */
	public class JToolBarButton extends JButton {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;
 
		/**
		 * Instantiates a new j tool bar button.
		 *
		 * @param actionCommand the action command
		 * @param toolTipText the tool tip text
		 * @param altText the alt text
		 * @param imgName the image name
		 * @param actionListener the ActionListener
		 */
		private JToolBarButton(String actionCommand, String toolTipText, String altText, String imgName, ActionListener actionListener) {
				
			this.setText(altText);
			this.setToolTipText(toolTipText);
			this.setSize(36, 36);
			
			if (imgName!=null) {
				this.setPreferredSize(new Dimension(26,26));
			} else {
				this.setPreferredSize(null);	
			}

			if (imgName!=null) {
				try {
					ImageIcon imageIcon = new ImageIcon( this.getClass().getResource(pathImage + imgName), altText);
					this.setIcon(imageIcon);
					
				} catch (Exception err) {
					System.err.println(Language.translate("Fehler beim Laden des Bildes: ") + err.getMessage());
				}				
			}
			this.addActionListener(actionListener);	
			this.setActionCommand(actionCommand);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object object) {
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String actionCommand = ae.getActionCommand();
		
		if (actionCommand.equals("Save") || actionCommand.equals("SaveAndExit")) {
			// --- Confirmed, apply changes -----
			this.adapter4DataModel.save();
			
			Object dataModel = this.adapter4DataModel.getDataModel();
			Vector<String> dataModelBase64 = this.adapter4DataModel.getDataModelBase64Encoded(dataModel);

			this.networkComponent.setDataModel(dataModel);
			this.networkComponent.setDataModelBase64(dataModelBase64);
			
			this.graphController.getNetworkModel().getNetworkComponent(this.networkComponent.getId()).setDataModel(dataModel);
			this.graphController.getNetworkModel().getNetworkComponent(this.networkComponent.getId()).setDataModelBase64(dataModelBase64);
			
		}
		
		if (actionCommand.equals("SaveAndExit")) {
			this.setVisible(false);
			this.dispose();
		}
		
	}
	
}