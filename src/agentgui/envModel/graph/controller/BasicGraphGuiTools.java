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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.components.ComponentTypeDialog;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;
import agentgui.envModel.graph.prototypes.DistributionNode;

public class BasicGraphGuiTools implements ActionListener, Observer {

    private static final long serialVersionUID = 7033208567874447367L;

    private final String pathImage = GraphGlobals.getPathImages(); // @jve:decl-index=0:
    private final Dimension jButtonSize = new Dimension(26, 26); // @jve:decl-index=0:

    private JToolBar jToolBar = null;
    private JButton jButtonComponents = null;
    private JButton jButtonZoomFit2Window = null;
    private JButton jButtonZoomOne2One = null;
    private JButton jButtonZoomIn = null;
    private JButton jButtonZoomOut = null;
    private JButton jButtonSaveImage = null;
    private JToggleButton jToggleMouseTransforming = null;
    private JToggleButton jToggleMousePicking = null;
    private JButton jButtonAddComponent = null;
    private JButton jButtonRemoveComponent = null;
    private JButton jButtonMergeNodes = null;
    private JButton jButtonSplitNode = null;
    private JButton jButtonRedo = null;
    private JButton jButtonUndo = null;
    private JButton jButtonClearGraph = null;
    private JButton jButtonImportGraph = null;

    private JPopupMenu edgePopup = null;
    private JMenuItem jMenuItemDeleteComp = null;
    private JMenuItem jMenuItemEdgeProp = null;

    private JPopupMenu vertexPopup = null;
    private JMenuItem jMenuItemNodeProp = null;
    private JMenuItem jMenuItemAddComp = null;
    private JMenuItem jMenuItemSplitNode = null;

    private GraphEnvironmentController graphController = null; // @jve:decl-index=0:
    private GraphEnvironmentControllerGUI graphControllerGUI = null;
    private BasicGraphGui basicGraphGui = null;

    /**
     * Instantiates a new graph toolbar.
     */
    public BasicGraphGuiTools(GraphEnvironmentController graphEnvironmentController) {
    	this.graphController = graphEnvironmentController;
    	this.graphController.addObserver(this);
    }

    /**
     * Gets the toolbar for the BasicGraphGui.
     * @return the toolbar
     */
    public JToolBar getJToolBar() {
		if (jToolBar == null) {
		    jToolBar = new JToolBar();
		    jToolBar.setOrientation(JToolBar.VERTICAL);
		    jToolBar.setFloatable(false);
		    jToolBar.setPreferredSize(new Dimension(30, 30));

		    jToolBar.add(getJButtonComponents());

		    // --- In case of editing the simulation setup ----------
		    if (this.graphController.getProject() != null) {
				jToolBar.addSeparator();
				jToolBar.add(getJButtonAddComponent());
				jToolBar.add(getJButtonRemoveComponent());
				jToolBar.add(getJButtonMergeNodes());
				jToolBar.add(getJButtonSplitNode());
				
				jToolBar.addSeparator();
				jToolBar.add(getJButtonUndo());
				jToolBar.add(getJButtonRedo());
				this.setUndoRedoButtonsEnabled();
				
		    }
		    
		    jToolBar.addSeparator();
		    jToolBar.add(getJToggleMousePicking());
		    jToolBar.add(getJToggleMouseTransforming());

		    ButtonGroup bg = new ButtonGroup();
		    bg.add(getJToggleMousePicking());
		    bg.add(getJToggleMouseTransforming());
		    
		    jToolBar.addSeparator();
		    jToolBar.add(getJButtonZoomFit2Window());
		    jToolBar.add(getJButtonZoomOne2One());
		    jToolBar.add(getJButtonSaveImage());
		    jToolBar.add(getJButtonZoomIn());
		    jToolBar.add(getJButtonZoomOut());
	

		    // --- In case of editing the simulation setup ----------
		    if (this.graphController.getProject() != null) {
		    	jToolBar.addSeparator();
				jToolBar.add(getJButtonImportGraph());
		    	jToolBar.addSeparator();
				jToolBar.add(getJButtonClearGraph());
		    }
		    
		}
		return jToolBar;
    }

    /**
     * This method initializes jButtonClearGraph
     * @return javax.swing.JButton
     */
    private JButton getJButtonClearGraph() {
		if (jButtonClearGraph == null) {
		    jButtonClearGraph = new JButton();
		    jButtonClearGraph.setPreferredSize(jButtonSize);
		    jButtonClearGraph.setIcon(new ImageIcon(getClass().getResource(pathImage + "Remove.png")));
		    jButtonClearGraph.setToolTipText(Language.translate("Clear graph", Language.EN));
		    jButtonClearGraph.addActionListener(this);
		}
		return jButtonClearGraph;
    }

    /**
     * This method initializes jButtonZoomIn
     * @return javax.swing.JButton
     */
    private JButton getJButtonComponents() {
		if (jButtonComponents == null) {
		    jButtonComponents = new JButton();
		    jButtonComponents.setPreferredSize(jButtonSize);
		    jButtonComponents.setIcon(new ImageIcon(getClass().getResource(pathImage + "components.gif")));
		    jButtonComponents.setToolTipText(Language.translate("Graph-Komponenten"));
		    jButtonComponents.addActionListener(this);
		}
		return jButtonComponents;
    }

    /**
     * This method initializes jButtonZoomReload
     * @return javax.swing.JButton
     */
    private JButton getJButtonZoomFit2Window() {
		if (jButtonZoomFit2Window == null) {
		    jButtonZoomFit2Window = new JButton();
		    jButtonZoomFit2Window.setIcon(new ImageIcon(getClass().getResource(pathImage + "FitSize.png")));
		    jButtonZoomFit2Window.setPreferredSize(jButtonSize);
		    jButtonZoomFit2Window.setToolTipText(Language.translate("An Fenster anpassen"));
		    jButtonZoomFit2Window.addActionListener(this);
		}
		return jButtonZoomFit2Window;
    }

    /**
     * This method initializes jButtonZoomReset
     * @return javax.swing.JButton
     */
    private JButton getJButtonZoomOne2One() {
		if (jButtonZoomOne2One == null) {
		    jButtonZoomOne2One = new JButton();
		    jButtonZoomOne2One.setIcon(new ImageIcon(getClass().getResource(pathImage + "One2One.png")));
		    jButtonZoomOne2One.setPreferredSize(jButtonSize);
		    jButtonZoomOne2One.setToolTipText("1:1 - Zoom 100%");
		    jButtonZoomOne2One.addActionListener(this);
		}
		return jButtonZoomOne2One;
    }

    /**
     * This method initializes jButtonZoomIn
     * @return javax.swing.JButton
     */
    private JButton getJButtonZoomIn() {
		if (jButtonZoomIn == null) {
		    jButtonZoomIn = new JButton();
		    jButtonZoomIn.setPreferredSize(jButtonSize);
		    jButtonZoomIn.setIcon(new ImageIcon(getClass().getResource(pathImage + "ZoomIn.png")));
		    jButtonZoomIn.setToolTipText(Language.translate("Vergrößern"));
		    jButtonZoomIn.addActionListener(this);
		}
		return jButtonZoomIn;
    }

    /**
     * This method initializes jButtonZoomOut
     * @return javax.swing.JButton
     */
    private JButton getJButtonZoomOut() {
		if (jButtonZoomOut == null) {
		    jButtonZoomOut = new JButton();
		    jButtonZoomOut.setIcon(new ImageIcon(getClass().getResource(pathImage + "ZoomOut.png")));
		    jButtonZoomOut.setPreferredSize(jButtonSize);
		    jButtonZoomOut.setToolTipText(Language.translate("Verkleinern"));
		    jButtonZoomOut.addActionListener(this);
		}
		return jButtonZoomOut;
    }
    
    /**
     * This method initializes jButtonSaveImage
     * @return javax.swing.JButton
     */
    private JButton getJButtonSaveImage() {
		if (jButtonSaveImage == null) {
			jButtonSaveImage = new JButton();
			jButtonSaveImage.setIcon(new ImageIcon(getClass().getResource(pathImage + "SaveAsImage.png")));
			jButtonSaveImage.setPreferredSize(jButtonSize);
			jButtonSaveImage.setToolTipText(Language.translate("Als Bild exportieren"));
			jButtonSaveImage.addActionListener(this);
		}
		return jButtonSaveImage;
    }

    /**
     * This method initializes jButtonAddComponent
     * @return javax.swing.JButton
     */
    private JButton getJButtonAddComponent() {
		if (jButtonAddComponent == null) {
		    jButtonAddComponent = new JButton();
		    jButtonAddComponent.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListPlus.png")));
		    jButtonAddComponent.setPreferredSize(jButtonSize);
		    jButtonAddComponent.setToolTipText(Language.translate("Add new component", Language.EN));
		    jButtonAddComponent.addActionListener(this);
		}
		return jButtonAddComponent;
    }

    /**
     * This method initializes jToggleMouseTransforming
     * @return javax.swing.JToggleButton
     */
    private JToggleButton getJToggleMouseTransforming() {
		if (jToggleMouseTransforming == null) {
		    jToggleMouseTransforming = new JToggleButton();
		    jToggleMouseTransforming.setIcon(new ImageIcon(getClass().getResource(pathImage + "move.png")));
		    jToggleMouseTransforming.setPreferredSize(jButtonSize);
		    jToggleMouseTransforming.setToolTipText(Language.translate("Switch to Transforming mode", Language.EN));
		    jToggleMouseTransforming.setSize(new Dimension(36, 36));
		    jToggleMouseTransforming.addActionListener(this);
		}
		return jToggleMouseTransforming;
    }

    /**
     * This method initializes jToggleMousePicking
     * @return javax.swing.JToggleButton
     */
    private JToggleButton getJToggleMousePicking() {
		if (jToggleMousePicking == null) {
		    jToggleMousePicking = new JToggleButton();
		    jToggleMousePicking.setIcon(new ImageIcon(getClass().getResource(pathImage + "edit.png")));
		    jToggleMousePicking.setPreferredSize(jButtonSize);
		    jToggleMousePicking.setToolTipText(Language.translate("Switch to Picking mode", Language.EN));
		    jToggleMousePicking.addActionListener(this);
		    jToggleMousePicking.setSelected(true);
		}
		return jToggleMousePicking;
    }

    /**
     * This method initializes jButtonRemoveComponent
     * @return javax.swing.JButton
     */
    private JButton getJButtonRemoveComponent() {
		if (jButtonRemoveComponent == null) {
		    jButtonRemoveComponent = new JButton();
		    jButtonRemoveComponent.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListMinus.png")));
		    jButtonRemoveComponent.setPreferredSize(jButtonSize);
		    jButtonRemoveComponent.setToolTipText(Language.translate("Remove selected component", Language.EN));
		    jButtonRemoveComponent.addActionListener(this);
		}
		return jButtonRemoveComponent;
    }

    /**
     * This method initializes jButtonMergeNodes
     * @return javax.swing.JButton
     */
    private JButton getJButtonMergeNodes() {
		if (jButtonMergeNodes == null) {
		    jButtonMergeNodes = new JButton();
		    jButtonMergeNodes.setIcon(new ImageIcon(getClass().getResource(pathImage + "Merge.png")));
		    jButtonMergeNodes.setPreferredSize(jButtonSize);
		    jButtonMergeNodes.setToolTipText(Language.translate("Merge two nodes", Language.EN));
		    jButtonMergeNodes.addActionListener(this);
		}
		return jButtonMergeNodes;
    }

    /**
     * This method initializes jButtonSplitNode
     * @return javax.swing.JButton
     */
    private JButton getJButtonSplitNode() {
		if (jButtonSplitNode == null) {
		    jButtonSplitNode = new JButton();
		    jButtonSplitNode.setIcon(new ImageIcon(getClass().getResource(pathImage + "split.png")));
		    jButtonSplitNode.setPreferredSize(jButtonSize);
		    jButtonSplitNode.setToolTipText(Language.translate("Split the node into two nodes", Language.EN));
		    jButtonSplitNode.addActionListener(this);
		}
		return jButtonSplitNode;
    }

    /**
     * Returns the button for the undo action.
     * @return javax.swing.JButton
     */
    private JButton getJButtonUndo() {
		if (jButtonUndo == null) {
			jButtonUndo = new JButton();
		    jButtonUndo.setIcon(new ImageIcon(getClass().getResource(pathImage + "ActionUndo.png")));
		    jButtonUndo.setPreferredSize(jButtonSize);
		    jButtonUndo.setToolTipText(Language.translate("Undo Action", Language.EN));
		    jButtonUndo.addActionListener(this);
		}
		return jButtonUndo;
    }
    /**
     * Returns the button for the redo action.
     * @return javax.swing.JButton
     */
    private JButton getJButtonRedo() {
		if (jButtonRedo == null) {
			jButtonRedo = new JButton();
			jButtonRedo.setIcon(new ImageIcon(getClass().getResource(pathImage + "ActionRedo.png")));
			jButtonRedo.setPreferredSize(jButtonSize);
			jButtonRedo.setToolTipText(Language.translate("Redo Action", Language.EN));
			jButtonRedo.addActionListener(this);
		}
		return jButtonRedo;
    }
    
    /**
     * This method initializes jButtonImportGraph
     * @return javax.swing.JButton
     */
    private JButton getJButtonImportGraph() {
		if (jButtonImportGraph == null) {
		    jButtonImportGraph = new JButton();
		    jButtonImportGraph.setIcon(new ImageIcon(getClass().getResource(pathImage + "import.png")));
		    jButtonImportGraph.setPreferredSize(jButtonSize);
		    jButtonImportGraph.setToolTipText(Language.translate("Import Graph from file", Language.EN));
		    jButtonImportGraph.addActionListener(this);
		}
		return jButtonImportGraph;
    }

    /**
     * This method initializes edgePopup The menu is displayed when an edge is right clicked
     * @return javax.swing.JPopupMenu
     */
    public JPopupMenu getEdgePopup() {
		if (edgePopup == null) {
		    edgePopup = new JPopupMenu();
		    edgePopup.add(getJMenuItemDeleteComp());
		    edgePopup.addSeparator();
		    edgePopup.add(getJMenuItemEdgeProp());
		}
		return edgePopup;
    }

    /**
     * This method initializes vertexPopup The menu is displayed when a vertex is right clicked
     * @return javax.swing.JPopupMenu
     */
    public JPopupMenu getVertexPopup() {
		if (vertexPopup == null) {
		    vertexPopup = new JPopupMenu();
		    vertexPopup.add(getJMenuItemAddComp());
		    vertexPopup.add(getJMenuItemSplitNode());
		    vertexPopup.addSeparator();
		    vertexPopup.add(getJMenuItemNodeProp());
		}
		return vertexPopup;
    }

    /**
     * This method initializes jMenuItemDeleteComp
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemDeleteComp() {
		if (jMenuItemDeleteComp == null) {
		    jMenuItemDeleteComp = new JMenuItem();
		    jMenuItemDeleteComp.setText(Language.translate("Delete Component", Language.EN));
		    jMenuItemDeleteComp.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListMinus.png")));
		    jMenuItemDeleteComp.addActionListener(this);
		}
		return jMenuItemDeleteComp;
    }

    /**
     * This method initializes jMenuItemNodeProp
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemNodeProp() {
		if (jMenuItemNodeProp == null) {
		    jMenuItemNodeProp = new JMenuItem();
		    jMenuItemNodeProp.setText(Language.translate("Edit Properties", Language.EN));
		    jMenuItemNodeProp.setIcon(new ImageIcon(getClass().getResource(pathImage + "Properties.jpg")));
		    jMenuItemNodeProp.addActionListener(this);
		}
		return jMenuItemNodeProp;
    }

    /**
     * This method initializes jMenuItemEdgeProp
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemEdgeProp() {
		if (jMenuItemEdgeProp == null) {
		    jMenuItemEdgeProp = new JMenuItem();
		    jMenuItemEdgeProp.setText(Language.translate("Edit Properties", Language.EN));
		    jMenuItemEdgeProp.setIcon(new ImageIcon(getClass().getResource(pathImage + "Properties.jpg")));
		    jMenuItemEdgeProp.addActionListener(this);
		}
		return jMenuItemEdgeProp;
    }

    /**
     * This method initializes jMenuItemAddComp
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemAddComp() {
		if (jMenuItemAddComp == null) {
		    jMenuItemAddComp = new JMenuItem();
		    jMenuItemAddComp.setText(Language.translate("Add component", Language.EN));
		    jMenuItemAddComp.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListPlus.png")));
		    jMenuItemAddComp.addActionListener(this);
		}
		return jMenuItemAddComp;
    }

    /**
     * This method initializes jMenuItemSplitNode
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemSplitNode() {
		if (jMenuItemSplitNode == null) {
		    jMenuItemSplitNode = new JMenuItem();
		    jMenuItemSplitNode.setText(Language.translate("Split Node", Language.EN));
		    jMenuItemSplitNode.setIcon(new ImageIcon(getClass().getResource(pathImage + "split.png")));
		    jMenuItemSplitNode.addActionListener(this);
		}
		return jMenuItemSplitNode;
    }

    /**
     * Opens the AddComponentDialog. 
     */
    private void showAddComponentDialog() {
		AddComponentDialog addComponentDialog = new AddComponentDialog(this.graphController);
		addComponentDialog.setVisible(true);
    }

    /**
     * Sets locally the GUI elements for the graph.
     */
    private void setGraphGuiElements() {
    	if (this.graphControllerGUI==null) {
    		this.graphControllerGUI = (GraphEnvironmentControllerGUI) this.graphController.getEnvironmentPanel();
    		this.basicGraphGui = graphControllerGUI.getGraphGUI();	
    	}
    }
    
    /**
     * Sets the undo and redo buttons enabled or not. 
     * Additionally the ToolTipText will be set.
     */
    private void setUndoRedoButtonsEnabled() {
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				UndoManager undoManager = graphController.getNetworkModelAdapter().getUndoManager();
				
				getJButtonUndo().setEnabled(undoManager.canUndo());
				getJButtonUndo().setToolTipText(undoManager.getUndoPresentationName());
				
				getJButtonRedo().setEnabled(undoManager.canRedo());
				getJButtonRedo().setToolTipText(undoManager.getRedoPresentationName());
				
			}
		});
    }
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object object) {
		if (object instanceof NetworkModelNotification) {
			this.setUndoRedoButtonsEnabled();
		}
	}
    
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
		
    	this.setGraphGuiElements();
    	
		if (ae.getSource() == getJButtonComponents()) {
			// ------------------------------------------------------
			// --- Edit the ComponentType settings ------------------
			ComponentTypeDialog ctsDialog = new ComponentTypeDialog(this.graphController.getNetworkModelAdapter().getGeneralGraphSettings4MAS(), this.graphController.getProject());
			ctsDialog.setVisible(true);
			// - - - Waiting here - - -
			if (ctsDialog.isCanceled()==false) {
				this.graphController.getNetworkModelAdapter().setGeneralGraphSettings4MAS(ctsDialog.getGeneralGraphSettings4MAS());
			}
			ctsDialog.dispose();
			ctsDialog = null;

		} else if (ae.getSource() == getJButtonZoomFit2Window()) {
			// ------------------------------------------------------
			// --- Button Reset zoom --------------------------------
			this.graphController.getNetworkModelAdapter().zoomFit2Window();
			
		} else if (ae.getSource() == getJButtonZoomOne2One()) {
			// ------------------------------------------------------
			// --- Button Reset zoom --------------------------------
			this.graphController.getNetworkModelAdapter().zoomOne2One();
			
		} else if (ae.getSource() == getJButtonZoomIn()) {
			// ------------------------------------------------------
			// --- Button Zoom in -----------------------------------
			this.graphController.getNetworkModelAdapter().zoomIn();
			
		} else if (ae.getSource() == getJButtonZoomOut()) {
			// ------------------------------------------------------
			// --- Button Zoom out ----------------------------------
			this.graphController.getNetworkModelAdapter().zoomOut();

		} else if (ae.getSource() == getJButtonSaveImage()) {
			// ------------------------------------------------------
			// --- Save graph as Image ------------------------------
			this.graphController.getNetworkModelAdapter().saveAsImage();
			
		} else if (ae.getSource() == getJToggleMouseTransforming()) {
			// ------------------------------------------------------
			// --- Button Transforming Mouse mode -------------------
			this.graphController.getNetworkModelAdapter().setGraphMouseTransforming();
			
		} else if (ae.getSource() == getJToggleMousePicking()) {
			// ------------------------------------------------------
			// --- Button Picking Mouse mode ------------------------
			this.graphController.getNetworkModelAdapter().setGraphMousePicking();
			
		} else if (ae.getSource() == getJButtonAddComponent() || ae.getSource() == getJMenuItemAddComp()) {
			// ------------------------------------------------------
			// --- Add Component Button Clicked ---------------------
			if(this.graphController.getNetworkModelAdapter().getGraph().getVertexCount()==0){
				// --- If the graph is empty - starting from scratch -
				this.showAddComponentDialog();	
				
			} else if(basicGraphGui.getPickedSingleNode()!=null) {
				// --- Picked a vertex ------------------------------
					if(this.graphController.getNetworkModelAdapter().isFreeGraphNode(basicGraphGui.getPickedSingleNode())) {
						this.showAddComponentDialog();
					} else {
						JOptionPane.showMessageDialog(this.graphControllerGUI, Language.translate("Select a valid vertex", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
					};
			} else {
				// --- No vertex is picked ---------------------------
				JOptionPane.showMessageDialog(this.graphControllerGUI, Language.translate("Select a valid vertex first", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
			}
			
		} else if (ae.getSource() == getJButtonRemoveComponent() || ae.getSource() == getJMenuItemDeleteComp()) {
			// ------------------------------------------------------
			// --- Remove Component Button clicked ------------------
			NetworkComponent selectedComponent = null;
			Set<GraphEdge> edgeSet = this.basicGraphGui.getPickedEdges();
			if(edgeSet.size()>0){ 
				// --- At least one edge is picked ------------------
				// --- Get the Network component from picked edge ---
				selectedComponent = this.graphController.getNetworkModelAdapter().getNetworkComponent(edgeSet.iterator().next());
				// --- Remove component and update graph ------------ 
				this.graphController.getNetworkModelAdapter().removeNetworkComponent(selectedComponent);
				
			} else {
				// --- No edge is picked ----------------------------
				Set<GraphNode> nodeSet = this.basicGraphGui.getPickedNodes();
				if (nodeSet.size()>0) {
					// --- At least one node is picked --------------
					HashSet<NetworkComponent> components = this.graphController.getNetworkModelAdapter().getNetworkComponents(nodeSet.iterator().next());
					Iterator<NetworkComponent> it = components.iterator();
					while(it.hasNext()) {
						selectedComponent = it.next();
						if (selectedComponent.getPrototypeClassName().equals(DistributionNode.class.getName())) {
							// --- Remove component, update graph --- 
							this.graphController.getNetworkModelAdapter().removeNetworkComponent(selectedComponent);
							return;
						}	
					}
					
				} 
				// --- Nothing valid picked -------------------------
				JOptionPane.showMessageDialog(graphControllerGUI, Language.translate("Select a valid element first!", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);	
				
				
			}
			
		} else if (ae.getSource() == getJButtonMergeNodes()) {
			// ------------------------------------------------------
			// --- Merge Nodes Button clicked -----------------------
			Set<GraphNode> nodeSet = this.basicGraphGui.getPickedNodes();
			if(nodeSet.size()==2){
				// --- Two nodes are picked
				Iterator<GraphNode> nodeIter = nodeSet.iterator();
				GraphNode node1 = nodeIter.next();
				GraphNode node2 = nodeIter.next();
				if (this.graphController.getNetworkModelAdapter().isFreeGraphNode(node1) && this.graphController.getNetworkModelAdapter().isFreeGraphNode(node2)) {
					// --- Valid nodes are picked
					this.graphController.getNetworkModelAdapter().mergeNodes(node1, node2);
				} else {
					// --- Invalid nodes are picked
					JOptionPane.showMessageDialog(graphControllerGUI, Language.translate("Select two valid vertices", Language.EN),
							Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
				}
			} else {
				// --- Two nodes are not picked
				JOptionPane.showMessageDialog(graphControllerGUI, Language.translate("Use Shift and click two vertices", Language.EN),
						Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
			}	
			
			
		} else if (ae.getSource() == getJButtonSplitNode() || ae.getSource() == getJMenuItemSplitNode()) {
			// ------------------------------------------------------
			// --- Button Split node --------------------------------
			GraphNode pickedNode = basicGraphGui.getPickedSingleNode();
			if(pickedNode!=null){
				// --- One vertex is picked -----
				HashSet<NetworkComponent> components = this.graphController.getNetworkModelAdapter().getNetworkComponents(pickedNode);
				NetworkComponent containsDistributionNode = this.graphController.getNetworkModelAdapter().containsDistributionNode(components);
				if (containsDistributionNode!=null) {
					if(components.size()>=2){
						this.graphController.getNetworkModelAdapter().splitNetworkModelAtNode(pickedNode);
					} else {
						JOptionPane.showMessageDialog(graphControllerGUI, Language.translate("The select Vertex should be at least between two components !", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
					}

				} else {
					if(components.size()==2){
						this.graphController.getNetworkModelAdapter().splitNetworkModelAtNode(pickedNode);
					} else {
						JOptionPane.showMessageDialog(graphControllerGUI, Language.translate("The select Vertex should be between two components !", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
					}
				}
					
			} else {
				//Multiple vertices are picked
				JOptionPane.showMessageDialog(graphControllerGUI, Language.translate("Select one vertex !", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
			}
		
		} else if (ae.getSource() == getJButtonUndo()) {
			this.graphController.getNetworkModelAdapter().undo();
			this.setUndoRedoButtonsEnabled();
			
		} else if (ae.getSource() == getJButtonRedo()) {
			this.graphController.getNetworkModelAdapter().redo();
			this.setUndoRedoButtonsEnabled();
			
		} else if (ae.getSource() == getJButtonClearGraph()) {
			// ------------------------------------------------------
			// --- Button Clear graph -------------------------------
			int answer = JOptionPane.showConfirmDialog(Application.MainWindow,
						 Language.translate("Are you sure that you want to clear the graph?", Language.EN), 
						 Language.translate("Confirmation", Language.EN), 
						 JOptionPane.YES_NO_OPTION);

			if (answer == JOptionPane.YES_OPTION) {
				// --- Clearing the actual Network and Graph model ------------
				this.graphController.getNetworkModelAdapter().clearNetworkModel();
			}
		
		} else if (ae.getSource() == getJButtonImportGraph()) {
			// ------------------------------------------------------
			// --- Button Import graph from file --------------------
			JFileChooser graphFC = new JFileChooser();
			graphFC.setFileFilter(new FileNameExtensionFilter(Language.translate(this.graphController.getGraphFileImporter().getTypeString()), this.graphController.getGraphFileImporter().getGraphFileExtension()));
			graphFC.setCurrentDirectory(Application.RunInfo.getLastSelectedFolder());
			if(graphFC.showOpenDialog(this.getJToolBar()) == JFileChooser.APPROVE_OPTION){
				Application.RunInfo.setLastSelectedFolder(graphFC.getCurrentDirectory());
				File graphMLFile = graphFC.getSelectedFile();
				this.graphController.importNetworkModel(graphMLFile);
			}
			
		} else if(ae.getSource() == getJMenuItemNodeProp()) {
			// ------------------------------------------------------
			// --- Popup Menu Item Node properties clicked ----------
			GraphNode pickedVertex = basicGraphGui.getPickedSingleNode();
			if(pickedVertex!=null){
				new OntologySettingsDialog(this.graphController.getProject(), this.graphController, pickedVertex).setVisible(true);
			}
		
		} else if(ae.getSource() == getJMenuItemEdgeProp()){
			// ------------------------------------------------------
			// --- Popup Menu Item Edge properties clicked ----------
			GraphEdge pickedEdge = basicGraphGui.getPickedSingleEdge();
			if(pickedEdge!=null){
				NetworkComponent netComp = graphController.getNetworkModelAdapter().getNetworkComponent(pickedEdge);					
				new OntologySettingsDialog(this.graphController.getProject(), this.graphController, netComp).setVisible(true);
			}
		}		
		
	} // end actionPerformed()

}
