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
package de.enflexit.common.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 * The Class AwbBasicTabbedPaneUI defines the appearance of tabs (e.g. the color settings).
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class AwbBasicTabbedPaneUI extends BasicTabbedPaneUI {

	private Font fontSelected = null;
	private Font fontNotSelected = null;
	 
	private boolean tabHeaderVisible = true;
	
	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicTabbedPaneUI#installDefaults()
	 */
	@Override
	protected void installDefaults() {
		super.installDefaults();
		focus = Color.WHITE;
		highlight = Color.WHITE;
		
		lightHighlight = Color.LIGHT_GRAY;
		shadow = Color.LIGHT_GRAY;
		darkShadow = Color.LIGHT_GRAY;
		
		// --- Define selected font -----------------------  
		fontSelected = tabPane.getFont().deriveFont(Font.BOLD);
		fontNotSelected = tabPane.getFont().deriveFont(Font.PLAIN);
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintText(java.awt.Graphics, int, java.awt.Font, java.awt.FontMetrics, int, java.lang.String, java.awt.Rectangle, boolean)
	 */
	@Override
	protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
		if (tabIndex==tabPane.getSelectedIndex()) {
			super.paintText(g, tabPlacement, fontSelected, metrics, tabIndex, title, textRect, isSelected);
		} else {
			super.paintText(g, tabPlacement, fontNotSelected, metrics, tabIndex, title, textRect, isSelected);
		}
	}
	
	
	
	/**
	 * Sets the tab header visible (or not).
	 * @param tabHeaderVisible the indicator to set tab header visible
	 */
	public void setTabHeaderVisible(boolean tabHeaderVisible) {
		this.tabHeaderVisible = tabHeaderVisible;
	}
	/**
	 * Checks if tab headers are visible.
	 * @return true, if tab headers are visible
	 */
	public boolean isTabHeaderVisible() {
		return tabHeaderVisible;
	}
	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateTabAreaHeight(int, int, int)
	 */
	@Override
	protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
		if (this.isTabHeaderVisible()==true) {
			return super.calculateTabAreaHeight(tabPlacement, horizRunCount, maxTabHeight);
		}
		return 0;
	}
	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateTabAreaWidth(int, int, int)
	 */
	@Override
	protected int calculateTabAreaWidth(int tabPlacement, int vertRunCount, int maxTabWidth) {
		if (this.isTabHeaderVisible()==true) {
			return super.calculateTabAreaWidth(tabPlacement, vertRunCount, maxTabWidth);
		}
		return 0;
	}
	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateTabHeight(int, int, int)
	 */
	@Override
	protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
		if (this.isTabHeaderVisible()==true) {
			return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
		}
		return 0;
	}
	@Override
	protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
		if (this.isTabHeaderVisible()==true) {
			return super.calculateTabWidth(tabPlacement, tabIndex, metrics);
		}
		return 0;
	}
}
