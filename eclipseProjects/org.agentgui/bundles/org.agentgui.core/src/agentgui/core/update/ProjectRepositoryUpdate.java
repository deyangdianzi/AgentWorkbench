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
package agentgui.core.update;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.project.Project;
import agentgui.core.project.transfer.DefaultProjectExportController;
import agentgui.core.project.transfer.ProjectExportSettings;
import agentgui.core.project.transfer.ProjectImportController;
import agentgui.core.project.transfer.ProjectImportSettings;
import agentgui.core.update.repositoryModel.ProjectRepository;
import agentgui.core.update.repositoryModel.RepositoryEntry;
import de.enflexit.common.transfer.Download;
import de.enflexit.common.transfer.FileCopier;

/**
 * The Class ProjectUpdater does what the name promises.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectRepositoryUpdate extends Thread {

	private Project currProject; 
	
	private boolean isRepositoryFromWeb;
	private ProjectRepository projectRepository;
	
	private boolean executedByUser;
	private boolean userRequestForDownloadAndInstallation;
	private boolean userRequestForInstallation;
	private boolean leaveUpdateProcedure;
	
	/**
	 * Instantiates a new project updater.
	 * @param projectToUpdate the project to update
	 */
	public ProjectRepositoryUpdate(Project projectToUpdate) {
		this.currProject = projectToUpdate;
		this.setName(this.getClass().getSimpleName()  + " " + this.currProject.getProjectName());
	}
	
	
	/**
	 * Checks if the ProjectRepositoryUpdate was executed by user.
	 * @return true, if is executed by user
	 */
	public boolean isExecutedByUser() {
		return executedByUser;
	}
	/**
	 * Sets the ProjectRepositoryUpdate executed by user.
	 * @param executedByUser the new executed by user
	 */
	public void setExecutedByUser(boolean executedByUser) {
		this.executedByUser = executedByUser;
	}

	/**
	 * Checks if is a user request for download and installation is required.
	 * @return true, if is user request for download
	 */
	private boolean isUserRequestForDownloadAndInstallation() {
		return userRequestForDownloadAndInstallation;
	}
	/**
	 * Sets that the user request for download and installation is required.
	 * @param userRequestForInstallation the new user request for download
	 */
	private void setUserRequestForDownloadAndInstallation(boolean userRequestForDownload) {
		this.userRequestForDownloadAndInstallation = userRequestForDownload;
	}
	/**
	 * Checks if is confirmed user request for download and installation.
	 * @param update the RepositoryEntry of the update 
	 * @return true, if is confirmed user request for download and installation
	 */
	private boolean isConfirmedUserRequestForDownloadAndInstallation(RepositoryEntry update) {
		boolean confirmed = true;
		if (this.isUserRequestForDownloadAndInstallation()==true) {
			if (Application.isOperatingHeadless()==true) {
				confirmed = false;
			} else {
				String title   = "Download and Install the Update?";
				String message = this.getUpdateAsText(update) + " ";
				message += Language.translate("is available.", Language.EN) + "\n";
				message += Language.translate("Would you like to download and install this update?", Language.EN); 
				int answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (answer==JOptionPane.NO_OPTION) {
					confirmed = false;
				}
			}
		}
		return confirmed;
	}
	
	/**
	 * Checks if is a user request for installation is required.
	 * @return true, if is user request for installation
	 */
	private boolean isUserRequestForInstallation() {
		return userRequestForInstallation;
	}
	/**
	 * Sets that the user request for installation is required.
	 * @param userRequestForInstallation the new user request for installation
	 */
	private void setUserRequestForInstallation(boolean userRequestForInstallation) {
		this.userRequestForInstallation = userRequestForInstallation;
	}
	/**
	 * Checks if is confirmed user request for the installation.
	 * @param update the RepositoryEntry of the update 
	 * @return true, if is confirmed user request for download and installation
	 */
	private boolean isConfirmedUserRequestForInstallation(RepositoryEntry update) {
		boolean confirmed = true;
		if (this.isUserRequestForInstallation()==true) {
			if (Application.isOperatingHeadless()==true) {
				confirmed = false;
			} else {
				String title   = "Install the Update?";
				String message = this.getUpdateAsText(update) + " ";
				message += Language.translate("was downloaded.", Language.EN) + "\n";
				message += Language.translate("Would you like to install this update now?", Language.EN);
				int answer = JOptionPane.showConfirmDialog(Application.getMainWindow(), message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (answer==JOptionPane.NO_OPTION) {
					confirmed = false;
				}
			}
		}
		return confirmed;
	}
	
	/**
	 * Checks if is leave update procedure.
	 * @return true, if is leave update procedure
	 */
	private boolean isLeaveUpdateProcedure() {
		return leaveUpdateProcedure;
	}
	/**
	 * Sets the leave update procedure.
	 * @param leaveUpdateProcedure the new leave update procedure
	 */
	private void setLeaveUpdateProcedure(boolean leaveUpdateProcedure) {
		this.leaveUpdateProcedure = leaveUpdateProcedure;
	}
	
	/**
	 * Configures the internal update procedure.
	 */
	private void configureInernalUpdateProcedure() {
		
		if (this.isExecutedByUser()==true) {
			// --- Do not leave the update procedure ----------------
			this.setLeaveUpdateProcedure(false);
			// --- Request for download and installation ------------
			this.setUserRequestForDownloadAndInstallation(true);
			// --- No further request for installation --------------
			this.setUserRequestForInstallation(false);
			
		} else {
			// --- Do update as configured --------------------------
			int updateConfig = this.currProject.getUpdateAutoConfiguration();
			switch (updateConfig) {
			case 0:
				// --- Auto-Update ----------------------------------
				this.setLeaveUpdateProcedure(false);
				this.setUserRequestForDownloadAndInstallation(false);
				this.setUserRequestForInstallation(false);
				break;
			case 1:
				// --- Ask for installation -------------------------
				this.setLeaveUpdateProcedure(false);
				this.setUserRequestForDownloadAndInstallation(false);
				this.setUserRequestForInstallation(true);
				break;
			case 2:
				// --- No automated update --------------------------
				this.setLeaveUpdateProcedure(true);
				this.setUserRequestForDownloadAndInstallation(true);
				this.setUserRequestForInstallation(false);
				break;
			}
			
		}
	}
	
	/**
	 * Returns the update version as text.
	 *
	 * @param update the update
	 * @return the update as text
	 */
	private String getUpdateAsText(RepositoryEntry update) {
		return "Project '" + this.currProject.getProjectName() + "', version " + update.getVersion();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		this.startInSameThreas();
	}
	/**
	 * Does the same as calling the threads {@link #start()} method, but 
	 * without starting an individual thread. Thus, it enables to wait
	 * until the project update is finalized.
	 */
	public void startInSameThreas() {
		
		// --- Configure update procedure --------------------------- 
		this.configureInernalUpdateProcedure();
		if (this.isLeaveUpdateProcedure()==true) return;
		
		// --- Check if the setting are complete --------------------
		String configError = this.getConfigurationError();
		if (configError!=null) {
			String errMsg = "[" + this.getClass().getSimpleName() + "] " + configError + " - Cancel update check.";
			throw new IllegalArgumentException(errMsg);
		}

		// -- Check for the configure update site -------------------
		if (this.currProject.getUpdateSite()==null || this.currProject.getUpdateSite().isEmpty()==true) {
			System.err.println("No update-site was specified for the project '" + this.currProject.getProjectName() + "'!");
			return;
		}
		
		// --- Check if the repository can be loaded ----------------
		if (this.getProjectRepository()==null) return;
		
		// --- Check if an update is available ----------------------
		RepositoryEntry update = this.getProjectRepository().getProjectUpdate(this.currProject);
		if (update!=null) {
			// --- An update is available ---------------------------
			if (this.isConfirmedUserRequestForDownloadAndInstallation(update)==false) return;
			
			// --- Download the update ------------------------------
			String updateFilename = this.getLinkOrPathWithDirectorySuffix(Application.getGlobalInfo().getPathProjects(true), update.getFileName());
			if (this.downloadUpdateOrCopyFromLocalRepository(update, updateFilename)==true) {
				// --- The download of update is done ---------------
				if (this.isConfirmedUserRequestForInstallation(update)==false) return;
				
				// --- Do the installation of the new update --------
				String updateTitle = null;
				String updateMessage = null;
				int updateMessageType = 0;
				if (this.updateProject(updateFilename)==true) {
					updateTitle = Language.translate("Updated successful", Language.EN);
					updateMessage = Language.translate("The project was updated successfully!", Language.EN);
					updateMessageType = JOptionPane.INFORMATION_MESSAGE;
				} else {
					updateTitle = Language.translate("Update failed", Language.EN);
					updateMessage = Language.translate("The project update failed!", Language.EN);
					updateMessageType = JOptionPane.ERROR_MESSAGE;
				}
				// --- Give some feedback to the user ---------------
				if (Application.isOperatingHeadless()==false) {
					JOptionPane.showMessageDialog(Application.getMainWindow(), updateMessage, updateTitle, updateMessageType);
				} else {
					if (updateMessageType==JOptionPane.INFORMATION_MESSAGE) {
						System.out.println(updateMessage);
					} else {
						this.printSystemError(updateMessage);
					}
				}
			}
		}
		
	}
	
	/**
	 * Updates the current project with the specified project archive file.
	 *
	 * @param updateFileName the update file name
	 * @return true, if successful
	 */
	public boolean updateProject(String updateFileName) {
		
		// --- Save and close the current project -------------------
		boolean saved = this.currProject.save();
		boolean closed = false;
		if (saved==true) {
			closed = this.currProject.close();
		}
		if (closed==false) return false;
		
		// --- Pack the current project into an project archive -----
		if (this.packCurrentProjectToArchive()==false) return false;
		
		// --- Import the new version of the project ----------------
		ProjectImportSettings pims = new ProjectImportSettings(new File(updateFileName));
		pims.setExtractInThread(false);
		ProjectImportController pic = new ProjectImportController(pims);
		return pic.doProjectImport();
	}
	
	/**
	 * Packs the current project into a project archive located 
	 * in the projects directory and removes the project.
	 * @return true, if successful
	 */
	private boolean packCurrentProjectToArchive() {
		
		boolean successful = false;
		
		String projectDir = Application.getGlobalInfo().getPathProjects();
		String fileName = ProjectRepositoryExport.getRepositoryFileName(this.currProject);
		File targetFile = new File(projectDir + fileName);
		if (targetFile.exists()==true) {
			// --- Delete the old file --------------------
			targetFile.delete();
		}

		// --- Define export settings ---------------------
		ProjectExportSettings pes = new ProjectExportSettings();
		pes.setIncludeInstallationPackage(false);
		pes.setIncludeAllSetups(true);
		pes.setTargetFile(targetFile);
		
		// --- Export the project -------------------------
		DefaultProjectExportController expController = new DefaultProjectExportController();
		expController.exportProject(this.currProject, pes, false, false);
		successful = expController.isExportSuccessful();
		
		return successful;
	}
	
	
	/**
	 * Download update or copy from repository.
	 *
	 * @param updateRepositoryEntry the update repository entry
	 * @param destinationFileName the destination file name
	 * @return true, if successful
	 */
	private boolean downloadUpdateOrCopyFromLocalRepository(RepositoryEntry updateRepositoryEntry, String destinationFileName) {
		
		boolean successful = false;
		if (this.isRepositoryFromWeb==true) {
			// -- Start the web download ----------------------------
			String sourceFileURL = this.getFileNameURLDownload(updateRepositoryEntry);
			
			Download download = new Download(sourceFileURL, destinationFileName);
			download.startDownload();
			successful = download.wasSuccessful();
			
		} else {
			// --- Copy file to destination 
			String sourceFileName = this.getFileNameDownload(updateRepositoryEntry);
			FileCopier copier = new FileCopier();
			successful = copier.copyFile(sourceFileName, destinationFileName);
		}
		return successful;
	}
	
	/**
	 * Return the download file name URL base on the specified {@link RepositoryEntry}.
	 * @param updateRepositoryEntry the update repository entry
	 * @return the download file name URL
	 */
	private String getFileNameURLDownload(RepositoryEntry updateRepositoryEntry) {
		return this.getLinkOrPathWithDirectorySuffix(this.currProject.getUpdateSite(), "/") + updateRepositoryEntry.getFileName();
	}
	/**
	 * Return the repository file name.
	 * @param updateRepositoryEntry the update repository entry
	 * @return the repository file name
	 */
	private String getFileNameDownload(RepositoryEntry updateRepositoryEntry) {
		return this.getLinkOrPathWithDirectorySuffix(this.currProject.getUpdateSite(), File.separator) + updateRepositoryEntry.getFileName();
	}
	/**
	 * Returns the link or path with the deisred directory suffix.
	 * @param linkOfPath the link of path
	 * @param desiredSuffix the desired suffix
	 * @return the link or path with directory suffix
	 */
	private String getLinkOrPathWithDirectorySuffix(String linkOfPath, String desiredSuffix) {
		String pathChecked = linkOfPath;
		if (pathChecked.endsWith(desiredSuffix)==false) {
			pathChecked += desiredSuffix;
		}
		return pathChecked;
	}
	
	/**
	 * Returns the project repository from the projects update site.
	 * @return the project repository
	 */
	public ProjectRepository getProjectRepository() {
		if (projectRepository==null && this.currProject.getUpdateSite()!=null) {

			// --- Check if the update site is a web site URL -------
			try {
				URL updateURL = new URL(this.currProject.getUpdateSite());
				projectRepository = ProjectRepository.loadProjectRepository(updateURL);
				this.isRepositoryFromWeb = true;
				
			} catch (MalformedURLException urlEx) {
				//urlEx.printStackTrace();
			}
			
			// --- Backup, if repository comes not from an URL ------
			if (projectRepository==null) {
				// --- Check if update site is a local directory ----
				File localRepo = new File(this.currProject.getUpdateSite());
				if (localRepo.exists()==true) {
					projectRepository = ProjectRepository.loadProjectRepository(localRepo);
					this.isRepositoryFromWeb = false;
				}
			}
			
			// --- If still null, write an error to console ---------
			if (projectRepository==null) {
				this.printSystemError("Could not access any projct repository!");
			}
		}
		return projectRepository;
	}
	
	/**
	 * Return the configuration error as string, if there is an error.
	 * @return the configuration error
	 */
	private String getConfigurationError() {
		if (this.currProject==null) {
			return "No project was specified for an update!";
		}
		return null;
	}
	
	/**
	 * Prints the specified system error.
	 * @param message the message
	 */
	private void printSystemError(String message) {
		System.err.println("[" + this.getClass().getSimpleName() + "] " + message);
	}
	
}
