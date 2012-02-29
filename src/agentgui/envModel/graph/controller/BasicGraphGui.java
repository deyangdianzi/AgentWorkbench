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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;

import agentgui.core.application.Application;
import agentgui.core.gui.imaging.ImageFileView;
import agentgui.core.gui.imaging.ConfigurableFileFilter;
import agentgui.core.gui.imaging.ImagePreview;
import agentgui.core.gui.imaging.ImageUtils;
import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.DomainSettings;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModelAdapter;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.FourPassImageShaper;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.LayeredIcon;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.Checkmark;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

/**
 * This class implements a GUI component for displaying visualizations for JUNG graphs. <br>
 * This class also has a toolbar component which provides various features for editing, importing and interacting with the graph.
 * 
 * @see GraphEnvironmentControllerGUI
 * @see GraphEnvironmentMousePlugin
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BasicGraphGui extends JPanel implements Observer {

	private static final long serialVersionUID = 5764679914667183305L;

	/** Environment model controller, to be passed by the parent GUI. */
	private GraphEnvironmentController controller = null; // @jve:decl-index=0:

	/** The GUI's main component, either the graph visualization, or an empty JPanel if no graph is loaded */
	private Component centerComponent = null;
	/** The ToolBar for this component */
	private BasicGraphGuiTools graphGuiTools = null;

	/** Graph visualization component */
	private VisualizationViewer<GraphNode, GraphEdge> visView = null;
	/** JUNG object handling zooming */
	private ScalingControl scalingControl = new CrossoverScalingControl(); // @jve:decl-index=0:
	/** the margin of the graph for the visualization */
	private double graphMargin = 25;
	private Point2D defaultScaleAtPoint = new Point2D.Double(graphMargin, graphMargin); // @jve:decl-index=0:
	/** Indicates that the initial scaling is allowed */
	private boolean allowInitialScaling = true;

	/** The DefaultModalGraphMouse which can be added to the visualization viewer. Used here for the transforming mode */
	private DefaultModalGraphMouse<GraphNode, GraphEdge> defaultModalGraphMouse = null; // @jve:decl-index=0:
	/** The pluggable graph mouse which can be added to the visualization viewer. Used here for customized Picking mode	 */
	private PluggableGraphMouse pluggableGraphMouse = null; // @jve:decl-index=0:

	/**
	 * This is the default constructor
	 * @param controller The Graph Environment controller
	 */
	public BasicGraphGui(GraphEnvironmentController controller) {
		super();
		this.controller = controller;
		this.controller.addObserver(this);
		this.graphGuiTools = new BasicGraphGuiTools(this.controller);
		initialize();
		
		this.reLoadGraph();
	}
	
	/**
	 * Gets the graph environment controller.
	 * @return the controller
	 */
	public GraphEnvironmentController getGraphEnvironmentController() {
		return controller;
	}
	/**
	 * Gets the VisualizationViewer
	 * @return The VisualizationViewer
	 */
	public VisualizationViewer<GraphNode, GraphEdge> getVisView() {
		return visView;
	}
	/**
	 * Gets the scaling control.
	 * @return the scalingControl
	 */
	private ScalingControl getScalingControl() {
		return scalingControl;
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {

		this.setSize(300, 300);
		this.setLayout(new BorderLayout());
		this.add(this.graphGuiTools.getJToolBar(), BorderLayout.WEST);

		// --- React on component changes ------------------
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent ce) {
				setInitialScalingAndMovement();
			}
		});
	}

	/**
	 * Gets the PluggableGraphMouse.
	 * @return the pluggableGraphMouse
	 */
	private PluggableGraphMouse getPluggableGraphMouse() {
		if (pluggableGraphMouse==null) {
			
			// Create the context menu Plugin
			GraphEnvironmentPopupPlugin<GraphNode, GraphEdge> popupPlugin = new GraphEnvironmentPopupPlugin<GraphNode, GraphEdge>(this);
			popupPlugin.setEdgePopup(this.graphGuiTools.getEdgePopup());
			popupPlugin.setVertexPopup(this.graphGuiTools.getVertexPopup());

			pluggableGraphMouse = new PluggableGraphMouse();
			pluggableGraphMouse.add(new GraphEnvironmentMousePlugin(this));
			pluggableGraphMouse.add(popupPlugin);
		}
		return pluggableGraphMouse;
	}

	/**
	 * Gets the DefaultModalGraphMouse.
	 * @return the pluggableGraphMouse
	 */
	private DefaultModalGraphMouse<GraphNode, GraphEdge> getDefaultModalGraphMouse() {
		if (defaultModalGraphMouse==null) {
			defaultModalGraphMouse = new DefaultModalGraphMouse<GraphNode, GraphEdge>();
		}
		return defaultModalGraphMouse;
	}

	/**
	 * Sets the initial scaling for the graph on the VisualizationViewer.
	 */
	private void setInitialScalingAndMovement() {

		if (this.visView == null)
			return;
		if (this.allowInitialScaling == false)
			return;

		Graph<GraphNode, GraphEdge> currGraph = this.visView.getGraphLayout().getGraph();
		Rectangle2D rectGraph = this.getGraphSpreadDimension(currGraph);
		Rectangle2D rectVis = this.visView.getVisibleRect();
		if (rectVis.isEmpty())
			return;

		Point2D scaleAt = new Point2D.Double(rectGraph.getX(), rectGraph.getY());
		scaleAt = new Point2D.Double(0, 0);
		this.setDefaultScaleAtPoint(scaleAt);

		// --- Calculate the scaling --------------------------------
		double graphWidth = rectGraph.getWidth() + 2 * this.graphMargin;
		double graphHeight = rectGraph.getHeight() + 2 * this.graphMargin;
		double visWidth = rectVis.getWidth();
		double visHeight = rectVis.getHeight();

		float scaleX = (float) (visWidth / graphWidth);
		float scaleY = (float) (visHeight / graphHeight);
		if (scaleX > 1)
			scaleX = 1;
		if (scaleY > 1)
			scaleY = 1;

		float scale = scaleX;
		if (scaleX > scaleY) {
			scale = scaleY;
		}

		// --- Calculate the movement in the view -------------------
		double moveX = 0;
		double moveY = 0;
		if (rectGraph.getX() != 0) {
			moveX = rectGraph.getX() * (-1) + this.graphMargin;
		}
		if (rectGraph.getY() != 0) {
			moveY = rectGraph.getY() * (-1) + this.graphMargin;
		}

		// --- Set movement -----------
		MutableTransformer mtView = this.visView.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
		mtView.translate(moveX, moveY);

		// --- Set scaling ------------
		if (scale != 0 && scale != 1) {
			this.scalingControl.scale(this.visView, scale, this.getDefaultScaleAtPoint());
		}
		this.allowInitialScaling = false;

	}

	/**
	 * Gets the default point to scale at for zooming.
	 * @return the default scale at point
	 */
	private Point2D getDefaultScaleAtPoint() {
		return defaultScaleAtPoint;
	}
	/**
	 * Sets the default point to scale at for zooming..
	 * @param scalePoint the new default scale at point
	 */
	private void setDefaultScaleAtPoint(Point2D scalePoint) {
		defaultScaleAtPoint = scalePoint;
	}

	/**
	 * Gets the vertices spread dimension.
	 *
	 * @param graphNodes the graph nodes
	 * @return the vertices spread dimension
	 */
	public static Rectangle2D getVerticesSpreadDimension(Collection<GraphNode> graphNodes) {

		int count = 0;
		double x_min = 0;
		double x_max = 0;
		double y_min = 0;
		double y_max = 0;

		GraphNode[] nodes = graphNodes.toArray(new GraphNode[graphNodes.size()]);
		for (GraphNode node : nodes) {
			double x = node.getPosition().getX();
			double y = node.getPosition().getY();

			if (count == 0) {
				x_min = x;
				x_max = x;
				y_min = y;
				y_max = y;
			}

			if (x < x_min)
				x_min = x;
			if (x > x_max)
				x_max = x;
			if (y < y_min)
				y_min = y;
			if (y > y_max)
				y_max = y;
			count++;
		}
		return new Rectangle2D.Double(x_min, y_min, x_max - x_min, y_max - y_min);
	}

	/**
	 * Gets the graph spread as Rectangle.
	 * 
	 * @param graph the graph
	 * @return the graph spread
	 */
	private Rectangle2D getGraphSpreadDimension(Graph<GraphNode, GraphEdge> graph) {
		if (graph == null) {
			return new Rectangle2D.Double(0, 0, 0, 0);
		}
		return getVerticesSpreadDimension(graph.getVertices());
	}

	/**
	 * Sets the graph coordinates to values bigger than 0 for the x and the y axis.
	 * 
	 * @param graph the graph
	 * @return the graph
	 */
	private Graph<GraphNode, GraphEdge> correctGraphCoordinates(Graph<GraphNode, GraphEdge> graph2Correct, double xCorrect, double yCorrect) {

		if (xCorrect == 0 && yCorrect == 0) {
			return graph2Correct; // --- Nothing to correct ---------------------
		}

		// --- Correct the positions of the graph nodes ---
		Graph<GraphNode, GraphEdge> graph = graph2Correct;
		Collection<GraphNode> nodeCollection = graph.getVertices();
		GraphNode[] nodes = nodeCollection.toArray(new GraphNode[nodeCollection.size()]);
		for (int i = 0; i < nodes.length; i++) {
			Point2D pos = nodes[i].getPosition();
			pos.setLocation(pos.getX() + xCorrect, pos.getY() + yCorrect);
			nodes[i].setPosition(pos);
		}
		return graph;
	}

	/**
	 * This method assigns a graph to a new VisualizationViewer and adds it to the GUI. This is used for creating graphs for the first time.
	 * 
	 * @param graph The graph
	 */
	private void reLoadGraph() {
		
		Graph<GraphNode, GraphEdge> graph = this.controller.getNetworkModelAdapter().getGraph();
		
		// --- Remove the old component -------------------
		if (centerComponent != null) {
			this.remove(centerComponent);
			this.centerComponent = null;
			this.visView = null;
		}

		// --- Set the display ----------------------------
		if (graph == null) {
			// --- NO graph to display ----------
			this.visView = null;
			this.centerComponent = new JPanel();
			this.add(centerComponent, BorderLayout.CENTER);

		} else {
			// --- Graph to display -------------
			this.visView = this.getNewVisualizationViewer(graph);
			this.centerComponent = new GraphZoomScrollPane(this.visView);
			this.add(this.centerComponent, BorderLayout.CENTER);

			this.allowInitialScaling = true;
			this.validate();
			this.setInitialScalingAndMovement();
		}
	}

	/**
	 * Gets the current Graph and repaints the visualisation viewer.
	 */
	private void repaintGraph() {
		visView.getGraphLayout().setGraph(this.controller.getNetworkModelAdapter().getGraph());
		visView.repaint();
	}
	
	/**
	 * Gets the new visualization viewer for a given graph.
	 *
	 * @param graph the graph
	 * @return the new VisualizationViewer
	 */
	private VisualizationViewer<GraphNode, GraphEdge> getNewVisualizationViewer(Graph<GraphNode, GraphEdge> graph) {

		// ----------------------------------------------------------------
		// --- Get the layout settings for domains ------------------------
		// ----------------------------------------------------------------
		GeneralGraphSettings4MAS graphSettings = this.controller.getNetworkModelAdapter().getGeneralGraphSettings4MAS();

		// ----------------------------------------------------------------
		// --- Get the spread of the graph and correct the positions ------
		// ----------------------------------------------------------------
		this.graphMargin = graphSettings.getSnapRaster() * 3;
		double moveX = 0;
		double moveY = 0;

		Rectangle2D rect = this.getGraphSpreadDimension(graph);
		if (rect.getX() != graphMargin) moveX = (rect.getX() * (-1)) + graphMargin;
		if (rect.getY() != graphMargin) moveY = (rect.getY() * (-1)) + graphMargin;
		graph = this.correctGraphCoordinates(graph, moveX, moveY);	

		// ----------------------------------------------------------------
		// --- Define graph layout ----------------------------------------
		// ----------------------------------------------------------------
		Layout<GraphNode, GraphEdge> layout = new StaticLayout<GraphNode, GraphEdge>(graph);
		layout.setSize(new Dimension((int) (rect.getWidth() + 2 * graphMargin), (int) (rect.getHeight() + 2 * graphMargin)));
		layout.setInitializer(new Transformer<GraphNode, Point2D>() {
			@Override
			public Point2D transform(GraphNode node) {
				return node.getPosition(); // The position is specified in the GraphNode instance
			}
		});

		// ----------------------------------------------------------------
		// --- Create a new VisualizationViewer instance ------------------
		// ----------------------------------------------------------------
		final VisualizationViewer<GraphNode, GraphEdge> vViewer = new VisualizationViewer<GraphNode, GraphEdge>(layout);
		vViewer.setBackground(Color.WHITE);

		// --- Configure mouse interaction --------------------------------
		this.defaultModalGraphMouse = null;	// Reset new VisualizationViewer
		this.pluggableGraphMouse = null;	// Reset new VisualizationViewer
		vViewer.setGraphMouse(this.getPluggableGraphMouse());

		// --- Set tool tip for nodes -------------------------------------
		vViewer.setVertexToolTipTransformer(new Transformer<GraphNode, String>() {
			@Override
			public String transform(GraphNode edge) {
				return edge.getId();
			}
		});

		// --- Configure the vertex shape and size ------------------------
		vViewer.getRenderContext().setVertexShapeTransformer(new VertexShapeSizeAspect<GraphNode, GraphEdge>());
		
		// --- Configure vertex icons, if configured ----------------------		
		vViewer.getRenderContext().setVertexIconTransformer(new Transformer<GraphNode, Icon>(){
			
			private final String pickedPostfix = "[picked]";
			private HashMap<String, LayeredIcon> iconHash = new HashMap<String, LayeredIcon>();
			
			@Override
			public Icon transform(GraphNode node) {
				
				Icon icon = null;
				boolean picked = vViewer.getPickedVertexState().isPicked(node);
				
				NetworkModelAdapter networkModel = controller.getNetworkModelAdapter();
				HashSet<NetworkComponent> componentHashSet = networkModel.getNetworkComponents(node);
				NetworkComponent distributionNode = networkModel.containsDistributionNode(componentHashSet);
				if (distributionNode!=null) {
					// --- Found a distribution node ----------------
					ComponentTypeSettings cts = controller.getComponentTypeSettings().get(distributionNode.getType());
					DomainSettings ds = controller.getDomainSettings().get(cts.getDomain());
					String nodeImagePath = cts.getEdgeImage();
					String checkColor = ds.getVertexColorPicked();
					
					if (nodeImagePath!=null) {
						if (nodeImagePath.equals("MissingIcon")==false) {
							// --- 1. Search in the local Hash ------
							LayeredIcon layeredIcon = null;
							if (picked==true) {
								layeredIcon = iconHash.get(nodeImagePath+this.pickedPostfix);	
							} else {
								layeredIcon = iconHash.get(nodeImagePath);								
							}
							// --- 2. If necessary, load the image -- 
							if (layeredIcon==null) {
								ImageIcon imageIcon = this.loadImageIcon(nodeImagePath, distributionNode.getType());
								if (imageIcon!=null){
									// --- 3. Remind this images ----
									LayeredIcon layeredIconUnPicked = new LayeredIcon(imageIcon.getImage());
									this.iconHash.put(nodeImagePath, layeredIconUnPicked);
									
									LayeredIcon layeredIconPicked = new LayeredIcon(imageIcon.getImage());
									layeredIconPicked.add(new Checkmark(new Color(Integer.parseInt(checkColor))));
									this.iconHash.put(nodeImagePath+this.pickedPostfix, layeredIconPicked);
									// --- 4. Return the right one --
									if (picked==true) {
										layeredIcon = layeredIconPicked;
									} else {
										layeredIcon = layeredIconUnPicked;
									}
								}
							}
							icon = layeredIcon;	
						}
					}
				}
				return icon;
			}
			
			/**
			 * Load image icon.
			 * @param nodeImagePath the node image path
			 * @return the image icon
			 */
			private ImageIcon loadImageIcon(String nodeImagePath, String componentType) {
				ImageIcon imageIcon = null;
				try {
					URL url = getClass().getResource(nodeImagePath);
					imageIcon = new ImageIcon(url);
					
				} catch (Exception ex) {
					System.err.println("Could not find node image for '" + componentType + "'");
					imageIcon = null;
				}
				return imageIcon;
			}
			
		});
		
		// --- Configure vertex colors ------------------------------------
		vViewer.getRenderContext().setVertexFillPaintTransformer(new Transformer<GraphNode, Paint>() {
			@Override
			public Paint transform(GraphNode node) {

				String defaultColorString = controller.getDomainSettings().get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME).getVertexColor();
				Color defaultColor = new Color(Integer.parseInt(defaultColorString));
				boolean picked = vViewer.getPickedVertexState().isPicked(node);

				// --- Get color from component type settings -----
				String colorString = null;
				NetworkModelAdapter networkModel = controller.getNetworkModelAdapter();
				HashSet<NetworkComponent> componentHashSet = networkModel.getNetworkComponents(node);
				NetworkComponent networkComponent = networkModel.containsDistributionNode(componentHashSet);
				try {
					// --- Get the vertex size from the component type settings -
					if (networkComponent != null) {
						ComponentTypeSettings cts = controller.getComponentTypeSettings().get(networkComponent.getType());
						if (cts == null) {
							return defaultColor;
						}
						colorString = cts.getColor();
						if (picked == true) {
							DomainSettings ds = controller.getDomainSettings().get(cts.getDomain());
							colorString = ds.getVertexColorPicked();
						}
						
					} else {
						if (componentHashSet.iterator().hasNext()) {
							NetworkComponent component = componentHashSet.iterator().next();
							ComponentTypeSettings cts = controller.getComponentTypeSettings().get(component.getType());
							if (cts == null) {
								return defaultColor;
							}
							DomainSettings ds = controller.getDomainSettings().get(cts.getDomain());
							if (picked == true) {
								colorString = ds.getVertexColorPicked();
							} else {
								colorString = ds.getVertexColor();
							}
						}
					}
					if (colorString != null) {
						return new Color(Integer.parseInt(colorString));
					}
					return defaultColor;

				} catch (NullPointerException ex) {
					ex.printStackTrace();
					return defaultColor;
				}
			}
		}); // end transformer
				
		// --- Configure to show vertex labels ----------------------------
		vViewer.getRenderContext().setVertexLabelTransformer(new Transformer<GraphNode, String>() {
			@Override
			public String transform(GraphNode node) {

				//GeneralGraphSettings4MAS graphSettings = controller.getNetworkModelAdapter().getGeneralGraphSettings4MAS();
				
				NetworkModelAdapter nModel = controller.getNetworkModelAdapter();
				HashSet<NetworkComponent> components = nModel.getNetworkComponents(node);
				NetworkComponent distributionNode = nModel.containsDistributionNode(components);
				if (distributionNode != null) {
					// --- This is a DistributionNode -----
					String compType = distributionNode.getType();
					ComponentTypeSettings cts = controller.getComponentTypeSettings().get(compType);
					if (cts == null) {
						return node.getId();
					}
					if (cts.isShowLabel()) {
						return node.getId();
					}
					return null;
					
				} else {
					// --- Just a normal node -------------
					if (components.iterator().hasNext()) {
						String compType = components.iterator().next().getType();
						ComponentTypeSettings cts = controller.getComponentTypeSettings().get(compType);
						if (cts == null) {
							return node.getId();
						}
						DomainSettings ds = controller.getDomainSettings().get(cts.getDomain());
						if (ds.isShowLabel()) {
							return node.getId();
						}
						return null;
					}
					
				}
				return null;
			}
		} // end transformer
				);

		// --- Configure edge colors --------------------------------------
		vViewer.getRenderContext().setEdgeDrawPaintTransformer(new Transformer<GraphEdge, Paint>() {
			@Override
			public Paint transform(GraphEdge edge) {
				if (vViewer.getPickedEdgeState().isPicked(edge)) {
					// Highlight color when picked
					return GeneralGraphSettings4MAS.DEFAULT_EDGE_PICKED_COLOR;
				}
				try {
					ComponentTypeSettings cts = controller.getComponentTypeSettings().get(edge.getComponentType());
					if (cts == null) {
						return GeneralGraphSettings4MAS.DEFAULT_EDGE_COLOR;
					}
					String colorString = cts.getColor();
					if (colorString != null) {
						Color color = new Color(Integer.parseInt(colorString));
						return color;
					}
					return GeneralGraphSettings4MAS.DEFAULT_EDGE_COLOR;
				} catch (NullPointerException ex) {
					ex.printStackTrace();
					return GeneralGraphSettings4MAS.DEFAULT_EDGE_COLOR;
				}
			}
		});
		// --- Configure Edge Image Labels --------------------------------
		vViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<GraphEdge, String>() {
			@Override
			public String transform(GraphEdge edge) {
				// Get the path of the Image from the component type settings
				String textDisplay = "";
				try {
					ComponentTypeSettings cts = controller.getComponentTypeSettings().get(edge.getComponentType());
					if (cts == null) {
						return textDisplay;
					}
					String edgeImage = cts.getEdgeImage();
					boolean showLabel = cts.isShowLabel();

					if (showLabel) {
						textDisplay = edge.getId();
					}

					if (edgeImage != null) {
						URL url = getClass().getResource(edgeImage);
						if (url != null) {
							if (showLabel) {
								textDisplay = "<html><center>" + textDisplay + "<br><img src='" + url + "'></center></html>";
							} else {
								textDisplay = "<html><center><img src='" + url + "'></center></html>";
							}
						}
					}
					return textDisplay;

				} catch (NullPointerException ex) {
					ex.printStackTrace();
					return edge.getId();
				}
			}
		});
		// --- Configure edge label position ------------------------------
		vViewer.getRenderContext().setLabelOffset(0);
		vViewer.getRenderContext().setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<GraphNode, GraphEdge>(.5, .5));

		// --- Use straight lines as edges --------------------------------
		vViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<GraphNode, GraphEdge>());

		// --- Set edge width ---------------------------------------------
		vViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<GraphEdge, Stroke>() {
			@Override
			public Stroke transform(GraphEdge edge) {
				float edgeWidth = GeneralGraphSettings4MAS.DEFAULT_EDGE_WIDTH;
				try {
					ComponentTypeSettings cts = controller.getComponentTypeSettings().get(edge.getComponentType());
					if (cts == null) {
						return new BasicStroke(edgeWidth);
					}
					edgeWidth = cts.getEdgeWidth();

					if (edgeWidth == 0) {
						edgeWidth = GeneralGraphSettings4MAS.DEFAULT_EDGE_WIDTH;
					}

				} catch (Exception e) {
					e.printStackTrace();
					edgeWidth = GeneralGraphSettings4MAS.DEFAULT_EDGE_WIDTH;
				}
				return new BasicStroke(edgeWidth);
			}
		});
		return vViewer;
	}

	/**
	 * This method notifies the observers about a graph object selection
	 * @param pickedObject The selected object
	 */
	public void handleObjectLeftClick(Object pickedObject) {
		this.clearPickedObjects();
		this.selectObject(pickedObject);
	}

	/**
	 * Notifies the observers that this object is right clicked
	 * @param pickedObject the selected object
	 */
	public void handleObjectRightClick(Object pickedObject) {
		this.clearPickedObjects();
		this.selectObject(pickedObject);
	}

	/**
	 * Invoked when a graph node or edge is double clicked (left or right)
	 * @param pickedObject
	 */
	public void handleObjectDoubleClick(Object pickedObject) {
		this.clearPickedObjects();
		selectObject(pickedObject, true);
	}

	/**
	 * Clears the picked nodes and edges
	 */
	private void clearPickedObjects() {
		visView.getPickedVertexState().clear();
		visView.getPickedEdgeState().clear();
	}

	/**
	 * Sets a node or edge as picked
	 * 
	 * @param object The GraphNode or GraphEdge to pick
	 */
	private void setPickedObject(GraphElement object) {
		if (object instanceof GraphEdge) {
			visView.getPickedEdgeState().pick((GraphEdge) object, true);
		} else if (object instanceof GraphNode) {
			visView.getPickedVertexState().pick((GraphNode) object, true);
		}
	}

	/**
	 * Marks a group of objects as picked
	 * @param objects The objects
	 */
	private void setPickedObjects(Vector<GraphElement> objects) {
		Iterator<GraphElement> objIter = objects.iterator();
		while (objIter.hasNext()) {
			setPickedObject(objIter.next());
		}
	}

	/**
	 * Returns the node which is picked. If multiple nodes are picked, returns null.
	 * @return GraphNode - the GraphNode which is picked.
	 */
	public GraphNode getPickedSingleNode() {
		Set<GraphNode> nodeSet = this.getVisView().getPickedVertexState().getPicked();
		if (nodeSet.size() == 1)
			return nodeSet.iterator().next();
		return null;
	}
	/**
	 * Gets the Set<GraphNode> of picked nodes.
	 * @return the picked nodes
	 */
	public Set<GraphNode> getPickedNodes(){
		return visView.getPickedVertexState().getPicked();	
	}
	
	/**
	 * Returns the edge which is picked. If multiple nodes are picked, returns null.
	 * @return GraphEdge - the GraphNode which is picked.
	 */
	public GraphEdge getPickedSingleEdge() {
		Set<GraphEdge> edgeSet = this.getVisView().getPickedEdgeState().getPicked();
		if (edgeSet.size() == 1)
			return edgeSet.iterator().next();
		return null;
	}
	/**
	 * Gets the Set<GraphEdge> of picked edges.
	 * @return the picked edges
	 */
	public Set<GraphEdge> getPickedEdges(){
		return this.visView.getPickedEdgeState().getPicked();	
	}
	
	/**
	 * This method handles the selection of an object, i.e. highlights the related graph elements
	 * 
	 * @param object The object to select
	 */
	private void selectObject(Object object) {
		this.selectObject(object, false);
	}

	/**
	 * Same as selectObject but optionally shows component settings dialog
	 * 
	 * @param object
	 * @param showComponentSettingsDialog - shows the dialog if true
	 */
	private void selectObject(Object object, boolean showComponentSettingsDialog) {

		if (object instanceof GraphNode) {
			this.setPickedObject((GraphElement) object);
			// --- Is that node a distribution node? ----------------
			HashSet<NetworkComponent> netComps = controller.getNetworkModelAdapter().getNetworkComponents((GraphNode) object);
			NetworkComponent disNode = controller.getNetworkModelAdapter().containsDistributionNode(netComps);
			if (disNode != null) {
				this.controller.getNetworkModelAdapter().selectNetworkComponent(disNode);
			}
			if (netComps.size()==1) {
				this.controller.getNetworkModelAdapter().selectNetworkComponent(netComps.iterator().next());
				this.clearPickedObjects();
				this.setPickedObject((GraphElement) object);
			}
			
		} else if (object instanceof GraphEdge) {
			NetworkComponent netComp = controller.getNetworkModelAdapter().getNetworkComponent((GraphEdge) object);
			this.setPickedObjects(controller.getNetworkModelAdapter().getGraphElementsFromNetworkComponent(netComp));
			this.controller.getNetworkModelAdapter().selectNetworkComponent(netComp);

		} else if (object instanceof NetworkComponent) {
			this.setPickedObjects(controller.getNetworkModelAdapter().getGraphElementsFromNetworkComponent((NetworkComponent) object));
		}

		if (showComponentSettingsDialog == true) {
			OntologySettingsDialog osd = null;
			if (object instanceof GraphNode) {
				osd = new OntologySettingsDialog(this.controller.getProject(), this.controller, object);

			} else if (object instanceof GraphEdge) {
				NetworkComponent netComp = controller.getNetworkModelAdapter().getNetworkComponent((GraphEdge) object);
				osd = new OntologySettingsDialog(this.controller.getProject(), this.controller, netComp);

			} else if (object instanceof NetworkComponent) {
				osd = new OntologySettingsDialog(this.controller.getProject(), this.controller, object);

			}
			osd.setVisible(true);
		}

	}

	
	/**
	 * Export the current graph as image by using a file selection dialog.
	 */
	private void exportAsImage() {
		
		String currentFolder = null;
		if (Application.RunInfo!=null) {
			// --- Get the last selected folder of Agent.GUI ---
			currentFolder = Application.RunInfo.getLastSelectedFolderAsString();	
		}
		
		// --- Create instance of JFileChooser ----------------- 
		JFileChooser jfc = new JFileChooser(); 
		jfc.setMultiSelectionEnabled(false);
		jfc.setAcceptAllFileFilterUsed(false);
		
		// --- Add custom icons for file types. ----------------
        jfc.setFileView(new ImageFileView());
	    // --- Add the preview pane. ---------------------------
        jfc.setAccessory(new ImagePreview(jfc));
		
        
		// --- Set the file filter -----------------------------
        String[] extensionsJPEG = {ImageUtils.jpg,ImageUtils.jpeg};
        
        ConfigurableFileFilter filterJPG = new ConfigurableFileFilter(extensionsJPEG, "JPEG - Image");
        ConfigurableFileFilter filterPNG = new ConfigurableFileFilter(ImageUtils.png, "PNG - File");
        ConfigurableFileFilter filterGIF = new ConfigurableFileFilter(ImageUtils.gif ,"GIF - Image");
		
        jfc.addChoosableFileFilter(filterGIF);
		jfc.addChoosableFileFilter(filterJPG);
		jfc.addChoosableFileFilter(filterPNG);

		jfc.setFileFilter(filterPNG);
		

		// --- Maybe set the current directory ----------------- 
		if (currentFolder!=null) {
			jfc.setCurrentDirectory(new File(currentFolder));	
		}
        
		// === Show dialog and wait on user action =============
        int state = jfc.showSaveDialog(this); 
		if (state==JFileChooser.APPROVE_OPTION) {
		
			ConfigurableFileFilter cff = (ConfigurableFileFilter) jfc.getFileFilter();
			String selectedExtension = cff.getFileExtension()[0];
			String mustExtension = "." + selectedExtension;
					
			File selectedFile = jfc.getSelectedFile();
			if (selectedFile!=null) {
				String selectedPath = selectedFile.getAbsolutePath();
				if (selectedPath.endsWith(mustExtension)==false) {
					selectedPath = selectedPath + mustExtension;
				}
				// ---------------------------------------------
				// --- Export current display to image ---------
				// ---------------------------------------------
				this.exportAsImage(this.getVisView(), selectedPath, selectedExtension);	
				// ---------------------------------------------
				
				if (Application.RunInfo!=null) {
					Application.RunInfo.setLastSelectedFolder(jfc.getCurrentDirectory());
				}	
			}
		} // end APPROVE_OPTION
		
	}
	
	/**
	 * Export the current graph as image by using specified parameters. 
	 *
	 * @param vv the VisualizationViewer
	 * @param file the current file to export to
	 */
	private void exportAsImage(VisualizationViewer<GraphNode, GraphEdge> vv, String path2File, String extension) {
		
		// --- If the VisualizationViewer is null ---------
		if (vv==null) {
			return;
		}
		// --- Overwrite existing file ? ------------------
		File writeFile = new File(path2File);
		if (writeFile.exists()) {
			String msgHead = "Overwrite?";
			String msgText = "Overwrite existing file?";
			int msgAnswer  =  JOptionPane.showInternalConfirmDialog( this, msgText, msgHead, JOptionPane.YES_NO_OPTION);
			if (msgAnswer==JOptionPane.NO_OPTION) {
				return;  
			}
		}

		// --- Lets go ! ----------------------------------
		int width = vv.getSize().width;
		int height = vv.getSize().height;

		BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
		Graphics2D graphics = bi.createGraphics();
		graphics.fillRect(0,0, width, height);
		
		//vv.setDoubleBuffered(false);
		vv.paint(graphics);
		vv.paintComponents(graphics);
		//vv.setDoubleBuffered(true);
		
		try{
			ImageIO.write(bi, extension, writeFile);
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Controls the shape, size, and aspect ratio for each vertex.
	 * 
	 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	private final class VertexShapeSizeAspect<V, E> extends AbstractVertexShapeTransformer<GraphNode> implements Transformer<GraphNode, Shape> {

		private Map<String, Shape> shapeMap = new HashMap<String, Shape>();

		public VertexShapeSizeAspect() {
			
			this.setSizeTransformer(new Transformer<GraphNode, Integer>() {

				@Override
				public Integer transform(GraphNode node) {

					Integer size = controller.getDomainSettings().get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME).getVertexSize();
					Integer sizeFromCTS = null;

					NetworkModelAdapter networkModel = controller.getNetworkModelAdapter();
					HashSet<NetworkComponent> componentHashSet = networkModel.getNetworkComponents(node);
					NetworkComponent networkComponent = networkModel.containsDistributionNode(componentHashSet);
					try {
						if (networkComponent != null) {
							// --- DistributionNode: get size from ComponentTypeSettings - Start --
							ComponentTypeSettings cts = controller.getComponentTypeSettings().get(networkComponent.getType());
							if (cts == null) {
								return size;
							}
							sizeFromCTS = (int) cts.getEdgeWidth();
							// --- DistributionNode: get size from ComponentTypeSettings - End ----
						} else {
							// --- Normal node or ClusterNode ---------------------------- Start --
							if (componentHashSet.iterator().hasNext()) {

								// --- Is this a inner node of a ClusterComponent ? ----
								ArrayList<ClusterNetworkComponent> clusterHash = networkModel.getClusterComponents(componentHashSet);
								if (componentHashSet.size()==1 && clusterHash.size()== 1) {
									// --- This is a cluster component ------------------
									ClusterNetworkComponent cnc = networkModel.getClusterComponents(componentHashSet).get(0);
									DomainSettings ds = null;
									String domain = cnc.getDomain();
									if (domain != null) {
										if (domain.equals("") == false) {
											ds = controller.getDomainSettings().get(domain);
										}
									}
									if (ds == null) {
										ds = controller.getDomainSettings().get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME);
									}
									sizeFromCTS = ds.getVertexSize();
									sizeFromCTS = sizeFromCTS * 3;

								} else {
									// --- This is a normal component -------------------
									NetworkComponent component = componentHashSet.iterator().next();
									ComponentTypeSettings cts = controller.getComponentTypeSettings().get(component.getType());
									if (cts == null) {
										return size;
									}
									DomainSettings ds = controller.getDomainSettings().get(cts.getDomain());
									sizeFromCTS = ds.getVertexSize();

								}
							}
							// --- Normal node or ClusterNode ---------------------------- End ----
						}
						if (sizeFromCTS != null) {
							size = sizeFromCTS;
						}

					} catch (NullPointerException ex) {
						System.err.println("Invalid vertex size");
						ex.printStackTrace();
					}
					return size;
				}
			});

		}// end constructor

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.apache.commons.collections15.Transformer#transform(java.lang.Object)
		 */
		@Override
		public Shape transform(GraphNode node) {

			Shape shape = factory.getEllipse(node); // DEFAULT

			NetworkModelAdapter networkModel = controller.getNetworkModelAdapter();
			HashSet<NetworkComponent> componentHashSet = networkModel.getNetworkComponents(node);
			NetworkComponent networkComponent = networkModel.containsDistributionNode(componentHashSet);
			if (networkComponent!=null) {
				// ------------------------------------------------------------
				// --- This is a DistributionNode -----------------------------
				// ------------------------------------------------------------
				// --- Have a look, if the node is an image ---- START --------
				// ------------------------------------------------------------
				ComponentTypeSettings cts = controller.getComponentTypeSettings().get(networkComponent.getType());
				String nodeImage = cts.getEdgeImage();
				if (nodeImage!=null) {
					if (nodeImage.equals("MissingIcon")==false) {
						try {
							shape = shapeMap.get(nodeImage);
							if (shape==null) {
								URL url = getClass().getResource(nodeImage);
								ImageIcon imageIcon = new ImageIcon(url);
								Image image = imageIcon.getImage();
							    shape = FourPassImageShaper.getShape(image, 30);
							    if(shape.getBounds().getWidth() > 0 &&  shape.getBounds().getHeight() > 0) {
				                    // don't cache a zero-sized shape, wait for the image to be ready
				                    int width = image.getWidth(null);
				                    int height = image.getHeight(null);
				                    AffineTransform transform = AffineTransform.getTranslateInstance(-width / 2, -height / 2);
				                    shape = transform.createTransformedShape(shape);
				                    this.shapeMap.put(nodeImage, shape);
							    }
							}
						
						} catch (Exception ex) {
							System.err.println("Could not find node image for '" + networkComponent.getType() + "'");
						}
					}
				}
				// ------------------------------------------------------------
				// --- Have a look, if the node is an image ---- END ----------
				// ------------------------------------------------------------
				
			} else if (networkComponent == null && componentHashSet.size() == 1) {
				// --- This is a ClusterNetworkComponent --
				networkComponent = componentHashSet.iterator().next();
				if (networkComponent instanceof ClusterNetworkComponent) {

					DomainSettings ds = null;
					ClusterNetworkComponent cnc = (ClusterNetworkComponent) networkComponent;
					String domain = cnc.getDomain();
					if (domain != null) {
						if (domain.equals("") == false) {
							ds = controller.getDomainSettings().get(domain);
						}
					}
					if (ds == null) {
						ds = controller.getDomainSettings().get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME);
					}
					String shapeForm = ds.getClusterShape();
					if (shapeForm.equals(GeneralGraphSettings4MAS.SHAPE_RECTANGLE)) {
						shape = factory.getRectangle(node);
					} else if (shapeForm.equals(GeneralGraphSettings4MAS.SHAPE_ROUND_RECTANGLE)) {
						shape = factory.getRoundRectangle(node);
					} else if (shapeForm.equals(GeneralGraphSettings4MAS.SHAPE_REGULAR_POLYGON)) {
						shape = factory.getRegularPolygon(node, 6);
					} else if (shapeForm.equals(GeneralGraphSettings4MAS.SHAPE_REGULAR_STAR)) {
						shape = factory.getRegularStar(node, 6);
					} else {
						shape = factory.getEllipse(node);
					}

				}
			}
			return shape;
		}
		
		 
	}

	@Override
	public void update(Observable observable, Object object) {

		if (object instanceof NetworkModelNotification) {
    		
    		NetworkModelNotification nmNotification = (NetworkModelNotification) object;
    		int reason = nmNotification.getReason();
    		Object infoObject = nmNotification.getInfoObject();
    		
    		switch (reason) {
    		case NetworkModelNotification.NETWORK_MODEL_Reload:
    			this.reLoadGraph();
    			break;
			
    		case NetworkModelNotification.NETWORK_MODEL_Repaint:
    		case NetworkModelNotification.NETWORK_MODEL_Component_Added:
    		case NetworkModelNotification.NETWORK_MODEL_Component_Removed:
    		case NetworkModelNotification.NETWORK_MODEL_Component_Renamed:
    		case NetworkModelNotification.NETWORK_MODEL_Nodes_Merged:
    		case NetworkModelNotification.NETWORK_MODEL_Nodes_Splited:
				this.clearPickedObjects();
    			this.repaintGraph();	
				break;
    			
			case NetworkModelNotification.NETWORK_MODEL_Component_Select:
				this.clearPickedObjects();
				this.selectObject(infoObject, false);
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_Zoom_Fit2Window:
				this.visView.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).setToIdentity();
				this.visView.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).setToIdentity();
				this.allowInitialScaling = true;
				this.setInitialScalingAndMovement();
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_Zoom_One2One:
				visView.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).setToIdentity();
				visView.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).setToIdentity();
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_Zoom_In:
				this.getScalingControl().scale(visView, 1.1f, this.getDefaultScaleAtPoint());
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_Zoom_Out:
				this.getScalingControl().scale(visView, 1 / 1.1f, this.getDefaultScaleAtPoint());
				break;
			
			case NetworkModelNotification.NETWORK_MODEL_ExportGraphAsImage:
				this.exportAsImage();
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_GraphMouse_Picking:
				this.visView.setGraphMouse(this.getPluggableGraphMouse());
				break;
			
			case NetworkModelNotification.NETWORK_MODEL_GraphMouse_Transforming:
				this.visView.setGraphMouse(this.getDefaultModalGraphMouse());
				break;
				
			default:
				break;
			}
    		
    		
    	}
		
	}

}
