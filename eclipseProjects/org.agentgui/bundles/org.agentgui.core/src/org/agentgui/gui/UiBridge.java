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
package org.agentgui.gui;

import java.awt.Frame;

import javax.inject.Inject;

import org.agentgui.gui.ProjectNewOpenDialog.ProjectAction;
import org.agentgui.gui.swt.AppModelId;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import agentgui.core.application.Application;
import agentgui.core.gui.MainWindow;
import agentgui.core.project.Project;


/**
 * The Class UiBridge is used to provide a switch between SWT and Swing elements of the user interface.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class UiBridge {

	private static UiBridge thisInstance;
	
	/**Instantiates a new UiBridge. */
	private UiBridge() {
	}
	/**
	 * Returns the singletom instance of UiBridge.
	 * @return single instance of UiBridge
	 */
	public static UiBridge getInstance() {
		if (thisInstance==null) {
			thisInstance = new UiBridge();
		}
		return thisInstance;
	}
	
	// --------------------------------------------------------------
	// --- Helper methods for accessing the workbench ---------------
	// --------------------------------------------------------------
	/**
	 * Checks if is workbench running.
	 * @return true, if is workbench running
	 */
	public boolean isWorkbenchRunning() {
		return PlatformUI.isWorkbenchRunning();
	}
	/**
	 * Returns the current workbench, if active.
	 * @return the workbench
	 */
	public IWorkbench getWorkbench() {
		if (this.isWorkbenchRunning()==true) {
			return  PlatformUI.getWorkbench();
		}
		return null;
	}
	/**
	 * Returns the active workbench window.
	 * @return the active workbench window
	 */
	public IWorkbenchWindow getActiveWorkbenchWindow() {
		IWorkbench iwb = this.getWorkbench();
		if (iwb!=null) {
			return iwb.getActiveWorkbenchWindow();
		}
		return null;
	}
	/**
	 * Returns the active workbench window shell.
	 * @return the active workbench window shell
	 */
	public Shell getActiveWorkbenchWindowShell() {
		IWorkbenchWindow iwbWindow = this.getActiveWorkbenchWindow();
		if (iwbWindow!=null) {
			return iwbWindow.getShell();
		}
		return null;
	}

	// --------------------------------------------------------------
	// --- Helper methods for accessing the swing main window -------
	// --------------------------------------------------------------
	/**
	 * Returns the current swing main window that usually is the {@link MainWindow}.
	 * @return the swing main window
	 */
	private Frame getSwingMainWindow() {
		return Application.getMainWindow();
	}
	
	// --------------------------------------------------------------
	// --- Methods for accessing the required UI element ------------
	// --------------------------------------------------------------
	/**
	 * Returns the project new open dialog (Swing or SWT).
	 *
	 * @param title the title
	 * @param currentAction the current action
	 * @return the project new open dialog
	 */
	public ProjectNewOpenDialog getProjectNewOpenDialog(String title, ProjectAction currentAction ) {
		ProjectNewOpenDialog projectDialog = null;
		if (this.isWorkbenchRunning()==true) {
			// --- SWT dialog -------------------
			projectDialog = new org.agentgui.gui.swt.dialogs.ProjectNewOpen(this.getActiveWorkbenchWindowShell(), title, currentAction);
		} else {
			// --- Swing dialog -----------------
			projectDialog = new org.agentgui.gui.swing.dialogs.ProjectNewOpen(this.getSwingMainWindow(), title, currentAction);
		}
		return projectDialog;
	}
	
	/**
	 * Returns the project window (Swing or SWT).
	 *
	 * @param project the project
	 * @return the project window
	 */
	@Inject
	public ProjectEditorWindow getProjectEditorWindow(Project project) {
		
		ProjectEditorWindow projectEditorWindow = null;
		if (this.isWorkbenchRunning()==true) {
			// --- SWT editor -------------------
			MPartStack editorStack = (MPartStack) project.getEclipseEModelService().find(AppModelId.PARTSTACK_ORG_AGENTGUI_CORE_PARTSTACK_EDITOR, project.getEclipseMApplication());
			MPart editorPart = project.getEclipseEPartService().createPart(AppModelId.PARTDESCRIPTOR_ORG_AGENTGUI_CORE_PARTDESCRIPTOR_AGENTPROJECT);
			if (editorPart!=null) {
				editorPart.getTransientData().put(Project.class.getName(), project);
				editorPart.setVisible(true);
				editorStack.getChildren().add(editorPart);
				project.getEclipseEPartService().showPart(editorPart, PartState.VISIBLE);

				projectEditorWindow = (ProjectEditorWindow) editorPart.getObject();
			}
			
		} else {
			// --- Swing editor -----------------
			projectEditorWindow = new org.agentgui.gui.swing.project.ProjectWindow(project);
		}
		return projectEditorWindow;
	}
	
	

}