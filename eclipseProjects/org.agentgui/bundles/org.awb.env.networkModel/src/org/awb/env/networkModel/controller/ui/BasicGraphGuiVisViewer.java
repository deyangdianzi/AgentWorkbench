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
package org.awb.env.networkModel.controller.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.awb.env.networkModel.maps.MapPreRenderer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * The Class BasicGraphGuiVisualizationViewer was basically created to
 * apply and test {@link RenderingHints} for the {@link VisualizationViewer}.
 *
 * @param <V> the value type
 * @param <E> the element type
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BasicGraphGuiVisViewer<V,E> extends VisualizationViewer<V,E> {

	private static final long serialVersionUID = 8187230173732284770L;
	
	private boolean isDoStatsForPaintComp = false;
	private long statsPaintCompObservationInterval = 1000; // one second
	private long statsPaintCompIntervalStart;
	private long statsLastPaintCompIntervalCounter;
	
	
	private boolean actionOnTop;
	private Timer resetTimerForActionOnTop;
	private boolean resetIsActionOnTop;
	
	private MapPreRenderer<V, E> mapPreRenderer;
	private boolean doMapPreRendering;

	
	/**
	 * Instantiates a new VisualizationViewer for the BasicGraphGui.
	 * @param layout the layout
	 */
	public BasicGraphGuiVisViewer(Layout<V,E> layout) {
		super(layout);
		this.initialize();
	}
	/**
	 * Instantiates a new VisualizationViewer for the BasicGraphGui.
	 * @param layout the layout
	 * @param preferredSize the preferred size
	 */
	public BasicGraphGuiVisViewer(Layout<V,E>layout, Dimension preferredSize) {
		super(layout, preferredSize);
		this.initialize();
	}
	/**
	 * Instantiates a new VisualizationViewer for the BasicGraphGui.
	 * @param model the model
	 */
	public BasicGraphGuiVisViewer(VisualizationModel<V,E>model) {
		super(model);
		this.initialize();
	}
	/**
	 * Instantiates a new VisualizationViewer for the BasicGraphGui.
	 * @param model the model
	 * @param preferredSize the preferred size
	 */
	public BasicGraphGuiVisViewer(VisualizationModel<V,E> model, Dimension preferredSize) {
		super(model, preferredSize);
		this.initialize();
	}

	/**
	 * This Initializes the VisualizationViewer.
	 */
	private void initialize() {
		
		this.setBackground(Color.GRAY);
		this.setDoubleBuffered(true);
		
		// --- Configure some rendering hints in order to accelerate the visualization --
		this.renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		this.renderingHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		this.renderingHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		this.renderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		this.renderingHints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DEFAULT);
		this.renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		
		this.renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		
		// --- useful and faster, but it makes the image quite unclear --------!!
		this.renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		
		// ------------------------------------------------------------------------------
		// --- Test area ----------------------------------------------------------------
		// ------------------------------------------------------------------------------
		//this.setDoMapPreRendering(true);
		
	}
	
	private void resetObservationVariables(long currentTimeMillis) {
		this.statsPaintCompIntervalStart = currentTimeMillis;
		this.statsLastPaintCompIntervalCounter = 1;
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.BasicVisualizationServer#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics graphics) {
		
		boolean successfulPainted = false;
		int paintTrials = 0;
		int paintTrialsMax = 3;
		Exception lastException = null;

		// ----------------------------------------------------------
		// --- Debug, diagnosis and optimization area ---------------
		// ----------------------------------------------------------
		if (this.isDoStatsForPaintComp==true) {
			// --- Statistics for graph paint activated -------------
			long currentTimeMillis = System.currentTimeMillis();
			if (this.statsPaintCompIntervalStart==0) {
				// --- Define the interval for the observation ------
				this.resetObservationVariables(currentTimeMillis);
			
			} else if (this.statsPaintCompIntervalStart + this.statsPaintCompObservationInterval <= currentTimeMillis) {
				// --- Print statistics -----------------------------
				long observationDuration = currentTimeMillis - this.statsPaintCompIntervalStart;
				double avgCals = (double) observationDuration / (double) this.statsLastPaintCompIntervalCounter; 
				System.out.println("[" + this.getClass().getSimpleName() + "] PaintStats: " + this.statsLastPaintCompIntervalCounter + " calls in " + observationDuration + " ms (interval length: " + ((int)avgCals) + " ms)");
				
				// --- Reset interval for the observation -----------
				this.resetObservationVariables(currentTimeMillis);
			}
			// --- Increase counter and so on -----------------------
			this.statsLastPaintCompIntervalCounter++;
		
		}
		// ----------------------------------------------------------

		// ----------------------------------------------------------
		// --- Start to do the actual painting ----------------------
		// ----------------------------------------------------------
		while (successfulPainted==false) {
			
			try {

				paintTrials++;
				if (this.isActionOnTop()==true || paintTrials>paintTrialsMax) {
					Graphics2D g2d = (Graphics2D)graphics;
					g2d.drawImage(offscreen, null, 0, 0);
					if (paintTrials>paintTrialsMax) break;
					
				} else {
					super.paintComponent(graphics);
					
				}
				successfulPainted = true;
				break;
				
			} catch (Exception ex) {
				String errMessage = "[" + this.getClass().getSimpleName() + "] Error while painting components - Retry ...";
				if (paintTrials>1) {
					errMessage = "\n" + errMessage;
				}
				System.err.print(errMessage);
				successfulPainted = false;
				lastException = ex;
				//ex.printStackTrace();
			}	
		}
		
		
		if (lastException!=null) {
			if (successfulPainted==false) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error while painting components:");
				lastException.printStackTrace();
			} else {
				System.err.println(" Done!");
			}
		}
		
	}
	
	
	/**
	 * Resets the setting for 'action on top' with a time delay (of 100 ms).
	 * @param isActionOnTop the is action on top
	 */
	public void resetActionOnTopWithTimeDelay(boolean isActionOnTop) {
		this.resetActionOnTopWithTimeDelay(isActionOnTop, 100);
	}
	/**
	 * Resets the setting for 'action on top' with a time delay (of 'delayMillis').
	 *
	 * @param isActionOnTop the is action on top
	 * @param delayMillis the delay in milliseconds
	 */
	public void resetActionOnTopWithTimeDelay(final boolean isActionOnTop, int delayMillis) {
		
		this.resetIsActionOnTop = isActionOnTop;
		this.getResetTimerForActionOnTop().setDelay(delayMillis);
		if (this.getResetTimerForActionOnTop().isRunning()==false) {
			this.getResetTimerForActionOnTop().start();
		} else {
			this.getResetTimerForActionOnTop().restart();
		}
	}
	
	/**
	 * Returns the reset timer to set the value action on top.
	 * @return the reset timer for action on
	 */
	public Timer getResetTimerForActionOnTop() {
		if (resetTimerForActionOnTop==null) {
			resetTimerForActionOnTop = new Timer(100, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					if (BasicGraphGuiVisViewer.this.isActionOnTop()!=BasicGraphGuiVisViewer.this.resetIsActionOnTop) {
						BasicGraphGuiVisViewer.this.setActionOnTop(BasicGraphGuiVisViewer.this.resetIsActionOnTop);
					}
				}
			});
			resetTimerForActionOnTop.setRepeats(false);
		}
		return resetTimerForActionOnTop;
	}
	
	/**
	 * Setter to indicate, if there is an action on the top of the graph visualization.
	 * @param actionOnTop the new indicator for an action on top
	 */
	public void setActionOnTop(boolean actionOnTop) {
		this.actionOnTop = actionOnTop;
	}
	/**
	 * Checks if there is an action on top of this graph visualization.
	 * @return true, if there is currently an action on top 
	 */
	public boolean isActionOnTop() {
		return actionOnTop;
	}

	
	/**
	 * Returns the Map pre-renderer.
	 * @return the map pre-renderer
	 */
	private MapPreRenderer<V, E> getMapPreRenderer() {
		if (mapPreRenderer==null) {
			mapPreRenderer = new MapPreRenderer<V, E>(this);
		}
		return mapPreRenderer;
	}
	/**
	 * Sets to do a map pre-rendering or not.
	 * @param doMapPreRendering the new do map pre-rendering
	 */
	public void setDoMapPreRendering(boolean doMapPreRendering) {
		if (doMapPreRendering==true) {
			if (this.preRenderers!=null) {
				this.preRenderers.clear();
			}
			this.addPreRenderPaintable(this.getMapPreRenderer());
		} else {
			this.removePreRenderPaintable(this.getMapPreRenderer());
			this.mapPreRenderer=null;
		}
		this.doMapPreRendering = doMapPreRendering;
		this.repaint();
	}
	/**
	 * Returns if there is currently a map pre-rendering to do.
	 * @return true, if is do map pre rendering
	 */
	public boolean isDoMapPreRendering() {
		return doMapPreRendering;
	}
	
}
