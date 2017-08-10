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
package agentgui.core.config;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Image;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.apache.commons.codec.binary.Base64;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.benchmark.BenchmarkMeasurement;
import agentgui.core.charts.timeseriesChart.TimeSeriesVisualisation;
import agentgui.core.charts.xyChart.XyChartVisualisation;
import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.environment.EnvironmentTypes;
import agentgui.core.gui.MainWindow;
import agentgui.core.network.JadeUrlConfiguration;
import agentgui.core.ontologies.gui.OntologyClassVisualisation;
import agentgui.core.project.PlatformJadeConfig;
import agentgui.core.project.PlatformJadeConfig.MTP_Creation;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.visualisation.DisplayAgent;
import de.enflexit.api.LastSelectedFolderReminder;
import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;


/**
 * This class is for constant values or variables that can be accessed or used application wide.<br>
 * In the Application class the running instance can be accessed by using {@link Application#getGlobalInfo()}. 
 * 
 * @see Application#getGlobalInfo()
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GlobalInfo implements LastSelectedFolderReminder {

	// --- Constant values -------------------------------------------------- 
	private static String localAppTitle = "Agent.GUI";
	
	private final static String localFileRunnableJar = "AgentGui.jar";
	private final static String localFileRunnableUpdater = "AgentGuiUpdate.jar";
	private final static String localPathImageIntern = "/agentgui/core/gui/img/";
	
	public static final String DEFAULT_UPDATE_SITE = "http://update.agentgui.org";
	public static final String DEFAULT_OIDC_ISSUER_URI = "https://se238124.zim.uni-due.de:8443/auth/realms/EOMID/";

	private final static String eomJar = "eom4AgentGui.jar";
	
	private final static String newLineSeparator = System.getProperty("line.separator");
	private final static String newLineSeparatorReplacer = "<br>";
	
	private final static Color localColorMenuHighLight =  new Color(0, 0, 192);
	
	
	private Integer localeJadeLocalPort = 1099;
	private Integer localeJadeLocalPortMTP = 7778;
	private JadeUrlConfiguration urlConfigurationForMaster;
	
	// --- Variables --------------------------------------------------------
	private static ExecutionEnvironment localExecutionEnvironment = ExecutionEnvironment.ExecutedOverIDE;
	
	private static String localAppLnF = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
	private static Class<?> localAppLnFClass;
	
	private static String localBaseDir = "";
	private static String localPathAgentGUI	 = "bin";
	private static String localPathJade		 = "lib" + File.separator + "jade" +  File.separator + "lib";
	private static String localPathProperty  = "properties" + File.separator;
	private static String localPathProjects  = "projects" + File.separator;
	private static String localPathWebServer = "server" + File.separator;
	private static String localPathDownloads = "download" + File.separator;
	
	private static String localFileDictionary  = "dictionary";
	private static String localFileProperties  = "agentgui.ini";
	private static String localFileNameProject = "agentgui.xml";
	private static String localFileNameProjectUserObject = "agentgui.bin";
	private static String localXML_FilePostfix = ".xml";
	private static String localBIN_FilePostfix = ".bin";
	private static String localFileEndProjectZip = "agui";
	private static String localFileNameProjectOntology = "AgentGUIProjectOntology";
	
	// --- Known EnvironmentTypes of Agent.GUI ------------------------------
	private EnvironmentTypes knownEnvironmentTypes =  new EnvironmentTypes();
	
	// --- Known OntologyClassVisualisation's of Agent.GUI ------------------
	private Vector<OntologyClassVisualisation> knownOntologyClassVisualisation = null;
	
	// --- PropertyContentProvider ------------------------------------------
	private PropertyContentProvider propertyContentProvider;
	
	// --- File-Properties --------------------------------------------------
	private ExecutionMode fileExecutionMode;
	private String processID;
	
	private float filePropBenchValue = 0;
	private String filePropBenchExecOn;
	private boolean filePropBenchAllwaysSkip; 
	
	private String filePropLanguage;
	private boolean maximizeMainWindow = false;
	
	private boolean filePropLoggingEnabled;
	private String filePropLoggingBasePath;
	
	private boolean filePropServerAutoRun = true;
	private String filePropServerMasterURL;
	private Integer filePropServerMasterPort = this.localeJadeLocalPort;
	private Integer filePropServerMasterPort4MTP = 7778;
	private MtpProtocol filePropServerMasterProtocol = MtpProtocol.HTTP;
	
	private MTP_Creation filePropOwnMtpCreation = MTP_Creation.ConfiguredByJADE;
	private String filePropOwnMtpIP = PlatformJadeConfig.MTP_IP_AUTO_Config;
	private Integer filePropOwnMtpPort = 7778;
	private MtpProtocol filePropMtpProtocol = MtpProtocol.HTTP;
	
	private String filePropServerMasterDBHost;
	private String filePropServerMasterDBName;
	private String filePropServerMasterDBUser;
	private String filePropServerMasterDBPswd;
	
	private String googleKey4API;
	private String googleHTTPref;
	
	private String updateSite;
	private Integer updateAutoConfiguration = 0;
	private Integer updateKeepDictionary = 1;
	private long updateDateLastChecked = 0;
	
	private String deviceServiceProjectFolder;
	private DeviceSystemExecutionMode deviceServiceExecutionMode = DeviceSystemExecutionMode.SETUP;
	private String deviceServiceSetupSelected;  
	private String deviceServiceAgentClassName;
	private String deviceServiceAgentName;
	private EmbeddedSystemAgentVisualisation deviceServiceAgentVisualisation = EmbeddedSystemAgentVisualisation.TRAY_ICON;
	
	private String filePropKeyStoreFile;
	private String filePropKeyStorePasswordEncrypted;
	private String filePropTrustStoreFile;
	private String filePropTrustStorePasswordEncrypted;
	
	private String oidcUsername;
	private String oidcIssuerURI;

	// --- Reminder information for file dialogs ----------------------------
	private File lastSelectedFolder; 
	
	// --- FileProperties and VersionInfo -----------------------------------
	/** Holds the instance of the file properties which are defined in '/properties/agentgui.ini' */
	private FileProperties fileProperties;
	/** Can be used in order to access the version information */
	private VersionInfo versionInfo;

	
	/**
	 * The Enumeration that contains the descriptors of the ExecutionEnvironment.
	 */
	public enum ExecutionEnvironment {
		ExecutedOverIDE,
		ExecutedOverAgentGuiJar,
		ExecutedOverOSGI,
	}

	/**
	 * The Enumeration of possible ExecutionModes.
	 * In order to get the current execution mode use  
	 * the method {@link GlobalInfo#getExecutionMode()} and {@link GlobalInfo#getExecutionModeDescription()} 
	 * @see GlobalInfo#getExecutionMode()
	 */
	public enum ExecutionMode {
		APPLICATION, SERVER, SERVER_MASTER, SERVER_SLAVE, DEVICE_SYSTEM
	}

	/**
	 * The Enumeration of possible ExecutionModes. In order to get the current execution mode use  
	 * the method {@link GlobalInfo#getExecutionMode()} and {@link GlobalInfo#getExecutionModeDescription()} 
	 * @see GlobalInfo#getExecutionMode()
	 */
	public enum DeviceSystemExecutionMode {
		SETUP, AGENT
	}

	/**
	 * The Enumeration EmbeddedSystemAgentVisualisation indicates
	 * if and how visualisations are to be displayed in case that
	 * the {@link ExecutionMode} is set to {@link ExecutionMode#DEVICE_SYSTEM}
	 * or embedded system agent respectively.
	 */
	public enum EmbeddedSystemAgentVisualisation {
		NONE, TRAY_ICON
	}
	
	/**
	 * The Enumeration MtpProtocol describes the usable protocols
	 * for the message exchange between agent platforms/container
	 */
	public enum MtpProtocol {
		HTTP, HTTPS
	}
	
	/**
	 * Constructor of this class. 
	 */
	public GlobalInfo() {

		boolean debug=false;
		try {
			// ----------------------------------------------------------------			
			// --- Get initial base directory by checking this class location -
			// ----------------------------------------------------------------
			File thisFile = new File(GlobalInfo.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			if (thisFile.getAbsolutePath().contains("%20")==true) {
				try {
					thisFile = new File(GlobalInfo.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
				} catch (URISyntaxException uriEx) {
					uriEx.printStackTrace();
				}
			}
			
			// ----------------------------------------------------------------
			// --- Examine the path reference found --------------------------
			// ----------------------------------------------------------------
			String pathFound = thisFile.getAbsolutePath(); 
			if (pathFound.endsWith(".jar") && pathFound.endsWith(GlobalInfo.localFileRunnableJar)==false && pathFound.contains(File.separator + "plugins" + File.separator)) {
				// --- OSGI environment ---------------------------------------
				this.setExecutionEnvironment(ExecutionEnvironment.ExecutedOverOSGI);
				int cutAt = pathFound.indexOf("plugins" + File.separator);
				GlobalInfo.localBaseDir = pathFound.substring(0, cutAt);
				
			} else {
				// --- IDE or AgentGui.jar case -------------------------------
				GlobalInfo.localBaseDir = thisFile + File.separator;
				if (thisFile.getAbsolutePath().endsWith(GlobalInfo.localPathAgentGUI)) {
					GlobalInfo.localBaseDir = thisFile.getParent() + File.separator;
				}
			}
			
			// ----------------------------------------------------------------
			// --- Check the Class-Path settings for AgentGui.jar -------------
			// ----------------------------------------------------------------
			String[] classPathFiles = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
			for (int i=0; i<classPathFiles.length; i++) {
				// --- Correct parameter, if executed by AgentGui.jar ---------
				if (classPathFiles[i].endsWith(GlobalInfo.localFileRunnableJar)==true & classPathFiles[i].endsWith(GlobalInfo.eomJar)==false) {
					// --- Set the application executed from AgentGui.jar -----
					this.setExecutionEnvironment(ExecutionEnvironment.ExecutedOverAgentGuiJar);
					
					File agentGuiJar = new File(classPathFiles[i]);
					String agentGuiJarPath = agentGuiJar.getAbsolutePath(); 
					int cutAt = agentGuiJarPath.lastIndexOf(File.separator) + 1;
					GlobalInfo.localBaseDir = agentGuiJarPath.substring(0, cutAt);	
				}
			}
			// ----------------------------------------------------------------
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// --------------------------------------------------------------------
		if (debug==true) System.err.println(localAppTitle + " execution directory is '" + localBaseDir + "'");
		
		// --------------------------------------------------------------------
		// --- Define the known EnvironmentTypes of Agent.GUI -----------------
		// --------------------------------------------------------------------
		String envKey = null;
		String envDisplayName = null;
		String envDisplayNameLanguage = null;
		Class<? extends EnvironmentController> envControllerClass = null;
		Class<? extends Agent> displayAgentClass = null;
		EnvironmentType envType = null;
		
		// --- No environment -------------------------------------------------
		envKey = "none";
		envDisplayName = "Kein vordefiniertes Umgebungsmodell verwenden";
		envDisplayNameLanguage = Language.DE;
		envControllerClass = null;
		displayAgentClass = null;
		envType = new EnvironmentType(envKey, envDisplayName, envDisplayNameLanguage, envControllerClass, displayAgentClass);
		addEnvironmentType(envType);
		
		// --- Grid Environment -----------------------------------------------
		envKey = "gridEnvironment";
		envDisplayName = "Graph bzw. Netzwerk";
		envDisplayNameLanguage = Language.DE;
		envControllerClass = GraphEnvironmentController.class;
		displayAgentClass = DisplayAgent.class;
		envType = new EnvironmentType(envKey, envDisplayName, envDisplayNameLanguage, envControllerClass, displayAgentClass);
		addEnvironmentType(envType);
		
		
		// --------------------------------------------------------------------
		// --- Add the known OntologyClassVisualisation's of Agent.GUI --------
		this.registerOntologyClassVisualisation(TimeSeriesVisualisation.class.getName());
		this.registerOntologyClassVisualisation(XyChartVisualisation.class.getName());
		
	}
	
	/**
	 * Initialises this class by reading the file properties and the current version information.
	 */
	public void initialize() {
		try {
			this.getFileProperties();
			this.getVersionInfo();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Sets the execution mode.
	 * @param executionMode the new execution mode
	 */
	public void setExecutionMode(ExecutionMode executionMode) {
		this.fileExecutionMode = executionMode;
	}
	
	/**
	 * Gets the execution mode from configuration.
	 * @return the execution mode from configuration
	 */
	public ExecutionMode getExecutionMode() {
	
		ExecutionMode execMode = this.fileExecutionMode; 
		if (execMode==ExecutionMode.SERVER) {
			// ------------------------------------------------------
			// --- Does JADE run? -----------------------------------
			// ------------------------------------------------------			
			AgentContainer mainContainer = Application.getJadePlatform().getMainContainer();
			if (mainContainer!=null) {
				// --------------------------------------------------
				// --- JADE is running ------------------------------
				// --------------------------------------------------
				// --- Default: Running as Server [Slave] -----------
				execMode = ExecutionMode.SERVER_SLAVE;

				JadeUrlConfiguration masterConfigured = this.getJadeUrlConfigurationForMaster();
				if (masterConfigured.isLocalhost()==true) {
					// --- Compare to MainContainer address ---------
					JadeUrlConfiguration platformRunning = new JadeUrlConfiguration(mainContainer.getPlatformName());
					if (platformRunning.isEqualJadePlatform(masterConfigured)==true) {
						// --- Running as Server [Master] -----------
						execMode = ExecutionMode.SERVER_MASTER;
					}
				}
				
			}
			// ------------------------------------------------------
		}
		return execMode;
	}
	
	/**
	 * Returns the systems process ID of the JVM.
	 * @return the process ID
	 */
	public String getProcessID() {
		if (processID==null) {
			String localProcessName = ManagementFactory.getRuntimeMXBean().getName();
			processID = "PID_" + localProcessName.substring(0, localProcessName.indexOf("@"));
		}
		return processID;
	}
	
	/**
	 * Gets the execution mode description of the current {@link ExecutionMode}.
	 * @return the execution mode description
	 */
	public String getExecutionModeDescription() {
		return this.getExecutionModeDescription(this.getExecutionMode());
	}
	
	/**
	 * Returns the execution mode description.
	 * @return the execution mode description
	 */
	public String getExecutionModeDescription(ExecutionMode executionMode) {

		String executionModeDescription = null;
		switch (executionMode) {
		case APPLICATION:
			executionModeDescription = Language.translate("Anwendung");
			break;
		case SERVER:
			executionModeDescription = "Server";
			break;
		case SERVER_MASTER:
			executionModeDescription = "Server [Master]";
			break;
		case SERVER_SLAVE:
			executionModeDescription = "Server [Slave]";
			break;
		case DEVICE_SYSTEM:
			executionModeDescription = Language.translate("Dienst / Embedded System Agent");
			break;
		}
		return executionModeDescription;
	}
	
	/**
	 * Returns the title/name of the application
	 * @return the current application title
	 */
	public String getApplicationTitle() {
		return localAppTitle;
	};
	/**
	 * This method can be used in order to set a new application title.
	 * For the actual renaming of the Application, use the method in the
	 * class agentgui.core.Application
	 * 
	 * @param newApplicationTitle the Application Title to set
	 * @see Application#setApplicationTitle(String)
	 */
	public void setApplicationTitle(String newApplicationTitle) {
		GlobalInfo.localAppTitle = newApplicationTitle;
	}
	
	// -------------------------------
	// --- Look and Feel -------------
	// -------------------------------
	/**
	 * This method will return the current Look and Feel (LnF) for Java Swing.<br>
	 * Also the class reference will be validated. If the class could not be found the 
	 * method will return null.
	 * 
	 * @return the class reference for the current look and feel 
	 * or null, if the class could not be found
	 */
	public String getAppLnFClassName() {
		if (localAppLnFClass==null) {
			try {
				// --- Check current Look and Feel class --
				localAppLnFClass = ClassLoadServiceUtility.forName(localAppLnF);
			} catch (ClassNotFoundException cnfEx) {
				//cnfEx.printStackTrace();
			}
		}
		if (localAppLnFClass!=null) {
			return localAppLnFClass.getName();
		}
		return null;
	}
	/**
	 * Set the current Look and Feel (LnF) for Java Swing by using its class reference.
	 * @param newLnF the new Swing - Look and Feel 
	 */
	public void setAppLnfClassName(String newLnF) {
		localAppLnF = newLnF;
		localAppLnFClass = null;
	}
	// -------------------------------

	/**
	 * This method can be used in order to evaluate how Agent.GUI is currently executed
	 * @return - 'IDE', if Agent.GUI is executed out of the IDE, where Agent.GUI is developed OR<br>
	 * - 'Executable', if Agent.GUI is running as executable jar-File
	 */
	public ExecutionEnvironment getExecutionEnvironment() {
		return localExecutionEnvironment;
	}
	/**
	 * Sets the execution environment.
	 * @param executionEnvironment the new execution environment
	 */
	private void setExecutionEnvironment(ExecutionEnvironment executionEnvironment ) {
		localExecutionEnvironment = executionEnvironment;
	}
	
	/**
	 * This method returns the actual String for a new line string
	 * @return a String that can be used for new lines in text  
	 */
	public String getNewLineSeparator(){
		return newLineSeparator;
	}
	/**
	 * This method will return a substitute String for the new line String.
	 * It is used, for example, in the dictionary, so that it is possible
	 * to write text with several lines in a single one.<br>
	 * Basically, the HTML-tag <<i>br</i>> is used here.
	 * 
	 * @return a substitute String for a new line
	 */
	public String getNewLineSeparatorReplacer(){
		return newLineSeparatorReplacer;
	}

	// ----------------------------------------------------
	// --- General Directory information ------------------
	// ----------------------------------------------------
	/**
	 * This method returns the base path of the application (e. g. 'C:\Java_Workspace\AgentGUI\')
	 * @return the base path of the application
	 */
	public String getPathBaseDir( ) {
		return localBaseDir;
	}	
	/**
	 * This method returns the base path of the application, when Agent.GUI is running
	 * in its development environment / IDE (e. g. 'C:\Java_Workspace\AgentGUI\bin\')
	 * @return the binary- or bin- base path of the application
	 */
	public String getPathBaseDirIDE_BIN( ) {
		return localBaseDir + localPathAgentGUI + File.separator;
	}	
	/**
	 * This method can be invoked in order to get the path to the JADE libraries (e. g. 'lib\jade\lib').
	 * @param absolute set true if you want to get the full path to this 
	 * @return the path to the JADE libraries
	 */
	public String getPathJade(boolean absolute){
		if (absolute == true) { 
			return getFilePathAbsolute(localPathJade);
		} else {
			return localPathJade;	
		}		
	}
	/**
	 * Creates the directory if not already there and thus required.
	 * @param path the path
	 */
	private void createDirectoryIfRequired(String path) {
		// --- Check, if the folder exists. If not create -----------
		File testDirectory = new File(path);
		if (testDirectory.exists()==false) {
			testDirectory.mkdir();
		}
	}
	
	/**
	 * Returns the relative path to the properties. For a more sophisticated method use {@link #getPathProperty(boolean)} !
	 * @return the path property
	 */
	public static String getPathProperty(){
		return localPathProperty;
	}
	/**
	 * This method can be invoked in order to get the path to the property folder 'properties\'.
	 * @param absolute set true if you want to get the full path to this 
	 * @return the path reference to the property folder
	 */
	public String getPathProperty(boolean absolute){
		String propPath = null;
		if (absolute==true) { 
			propPath =  this.getFilePathAbsolute(localPathProperty);
		} else {
			propPath = localPathProperty;	
		}
		// --- Create directory, if not already there -----
		this.createDirectoryIfRequired(propPath);
		// --- Do check with PropertyContentProvider ------
		this.getPropertyContentProvider(propPath);

		return propPath;
	}
	/**
	 * Returns the property content provider.
	 * @param pathToProperties the path to properties. May be <code>null</code> in case that PropertyContentProvider is already initiated. 
	 * @return the property content provider
	 */
	private PropertyContentProvider getPropertyContentProvider(String pathToProperties) {
		if (propertyContentProvider==null) {
			propertyContentProvider = new PropertyContentProvider(new File(pathToProperties));
			propertyContentProvider.checkAndProvideFullPropertyContent();
		}
		return propertyContentProvider;
	}
	/**
	 * Returns the property content provider.
	 * @return the property content provider
	 */
	public PropertyContentProvider getPropertyContentProvider() {
		if (propertyContentProvider==null) {
			String pathToProperties = this.getPathProperty(true);
			propertyContentProvider = this.getPropertyContentProvider(pathToProperties);
		}
		return propertyContentProvider;
	}
	
	/**
	 * This method will return the concrete path to the property file 'agentgui.ini' relatively or absolute
	 * @param absolute set true if you want to get the full path to this
	 * @return the path reference to the property file agentgui.ini
	 */
	public String getPathConfigFile(boolean absolute) {
		return this.getPathProperty(absolute) + localFileProperties;
	}
	
	/**
	 * This method will return the path to the project folder ('project\').
	 * If the path does not exists, the path will be created.
	 * @param absolute set true if you want to get the full path to this
	 * @return the path to the project folder
	 */
	public String getPathProjects(boolean absolute){
		return this.getPathProjects(absolute, true);
	}
	
	/**
	 * This method will return the path to the project folder ('./project').
	 *
	 * @param absolute set true if you want to get the full path to this
	 * @param forcePathCreation if true, the path will be created if it not already exists
	 * @return the path to the project folder
	 */
	public String getPathProjects(boolean absolute, boolean forcePathCreation){
		String returnPath = null;
		if (absolute == true) { 
			returnPath = getFilePathAbsolute(localPathProjects);
		} else {
			returnPath = localPathProjects;	
		}
		// --- See if the folder exists. If not create it, if -------
		// --- the parameter forcePathCreation allows that ----------
		if (forcePathCreation==true) {
			this.createDirectoryIfRequired(returnPath);
		}
		return returnPath;
	}
	
	/**
	 * Returns the list of project folders, which are located in the root project folder
	 * @return Array of project folders
	 */
	public String[] getProjectSubDirectories(){		
		// --- Search for sub folders ---
		String[] localProjects = null;
		File maindir = new File(this.getPathProjects(true)) ;
		File files[] = maindir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory() && !files[i].getName().substring(0, 1).equals(".") ) {
				if (localProjects == null) {						
					String[] AddEr = { files[i].getName() };	
					localProjects = AddEr;	
				} else {
					String[] AddEr = new String[localProjects.length+1];
					System.arraycopy( localProjects, 0, AddEr, 0, localProjects.length );
					AddEr[AddEr.length-1] = files[i].getName();
					localProjects = AddEr;
				}
			} 
		}
		return localProjects;		
	}	
	
	/**
	 * Here the local root folder for the download-server can be get. In this folder 
	 * sources will be stored, which can be loaded to remote containers in order to 
	 * add them to the ClassPath.   
	 * If the folder doesn't exists, it will be created.
	 * @return Local path to the download server of Agent.GUI ('/AgentGUI/server/')
	 */
	public String getPathWebServer() {
		String returnPath = this.getFilePathAbsolute(localPathWebServer);
		this.createDirectoryIfRequired(returnPath);
		return returnPath;
	}
	
	/**
	 * This method returns the folder, where downloaded files, coming from the applications
	 * web-server (server.client) can be stored locally.
	 * If the folder doesn't exists, it will be created.
	 * @return Local path to the folder, where downloads will be saved ('/AgentGUI/download/')
	 */
	public String getPathDownloads() {
		String returnPath = this.getFilePathAbsolute(localPathDownloads);;
		this.createDirectoryIfRequired(returnPath);
		return returnPath;
	}
	/**
	 * Returns the sub folder String for downloads.
	 * @return the sub folder4 downloads
	 */
	public String getSubFolder4Downloads() {
		return localPathDownloads;
	}
	
	/**
	 * Returns the common file postfix for XML-files
	 * @return the xmlFilepostfix
	 */
	public String getXmlFilePostfix() {
		return localXML_FilePostfix;
	}
	/**
	 * Returns the common file postfix for bin-files
	 * @return the xmlFilepostfix
	 */
	public String getBinFilePostfix() {
		return localBIN_FilePostfix;
	}
	/**
	 * Returns the path or file name for a given XML-file name
	 * @param xmlFileName
	 * @return filename with the bin-suffix
	 */
	public String getBinFileNameFromXmlFileName(String xmlFileName) {
		
		String fileName = xmlFileName;
		if(fileName.endsWith(getXmlFilePostfix())) {
			fileName = fileName.substring( 0, (fileName.length()-getXmlFilePostfix().length()) );
			fileName = fileName + getBinFilePostfix();
		}
		
		if (fileName.equals(xmlFileName)==false) {
			return fileName;	
		} else {
			return null;
		}
		
	}
	/**
	 * This method can be used in order to get the applications executable 
	 * jar-file. (by default it is the file 'AgentGui.jar')
	 * @param absolute set true if you want to get the full path to this
	 * @return the path to the executable jar file
	 */
	public String getFileRunnableJar(boolean absolute){
		
		if (this.getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverAgentGuiJar) {
			// --- The current instance was executed over an AgentGui.jar -----
			if (absolute==true && localFileRunnableJar!=null) { 
				return this.getFilePathAbsolute(localFileRunnableJar);
			} else {
				return localFileRunnableJar;	
			}	
		} else if (this.getExecutionEnvironment()==ExecutionEnvironment.ExecutedOverOSGI) {
			// TODO
			System.err.println("Runnable jar for OSGI is not defined yet.");
			return null;
		} else {
			// --- The current instance was executed by using the IDE ---------
			String path2Jar = "exec" + File.separator + "AgentGUI" + File.separator + localFileRunnableJar; 
			if (absolute == true) { 
				return getFilePathAbsolute(path2Jar);
			} else {
				return path2Jar;	
			}
		}
		
	}
	/**
	 * This method can be use in order to get the path to one of the dictionary files (Base64: '*.bin' | CSV-version: '*.csv'). 
	 * 
	 * @param base64 if set true, the method will return the path to the 
	 * Base64-encoded dictionary file, otherwise the path to the csv-version of the dictionary 
	 * 
	 * @param absolute set true if you want to get the full path to this
	 * @return path to the designated dictionary file
	 */
	public String getFileDictionary(boolean base64, boolean absolute ){
		
		// --- TXT-Version or Base64-encoded dictionary ---
		String fileName = null;
		if (base64==true) {
			fileName = getPathProperty(absolute) + localFileDictionary + ".bin";
		} else {
			fileName = getPathProperty(absolute) + localFileDictionary + ".csv";
		}
		return fileName;
	}
	
	
	/**
	 * Gets the file name of the updater (AgentGuiUpdate.jar).
	 * @param absolute the absolute
	 * @return the file updater
	 */
	public String getFileNameUpdater(boolean absolute) {
		if (absolute == true) { 
			return getFilePathAbsolute(localFileRunnableUpdater);
		} else {
			return localFileRunnableUpdater;	
		}
	}
	
	/**
	 * Returns the file name of the xml-file, which contains the project configuration (file: 'agentgui.xml')
	 * @return file name of the project configuration (agentgui.xml)
	 */
	public String getFileNameProject() {
		return localFileNameProject;
	};
	/**
	 * Returns the file name of the binary file, which contains the serializable user object of the Project(file: 'agentgui_userobject.bin')
	 * @return file name of the project user object binary. (agentgui_userobject.bin)
	 */
	public String getFilenameProjectUserObject(){
		return localFileNameProjectUserObject;
	}
	/**
	 * This method returns the file suffix for Agent.GUI project files 
	 * ('agui'), which are used for the project exchange within the application
	 *  
	 * @return The file suffix for zipped Agent.GUI projects.
	 */
	public String getFileEndProjectZip(){
		return localFileEndProjectZip;
	}
	/**
	 * This method will return the name of the predefined ontology, which can be used in order 
	 * to merge different ontology's to a single one. ('AgentGUIProjectOntology')
	 * @return name of the predefined meta ontology   
	 */
	public String getFileNameProjectOntology() {
		return localFileNameProjectOntology;
	}


	// ---------------------------------------------------------
	// ---------------------------------------------------------
	/**
	 * This method will convert relative Agent.GUI paths to absolute paths. 
	 * (e. g. './AgentGUI/properties/' will be converted to 'D:/MyWorkspace/AgentGUI/properties')    
	 * @param filePathRelative The relative path to convert
	 * @return The absolute path of the given relative one 
	 */
	private String getFilePathAbsolute(String filePathRelative){
		return localBaseDir + filePathRelative;		
	}
	// ---------------------------------------------------------
	// ---------------------------------------------------------

	/**
	 * This method can be used in order to set the port on which JADE is running (by default: 1099)
	 * @param localeJadeDefaultPort the localeJadeDefaultPort to set
	 */
	public void setJadeLocalPort(int localeJadeDefaultPort) {
		localeJadeLocalPort = localeJadeDefaultPort;
	}
	/**
	 * Returns the port on which JADE will run. 
	 * @return the localeJadeDefaultPort
	 */
	public Integer getJadeLocalPort() {
		return localeJadeLocalPort;
	}
	
	/**
	 * This method can be used in order to set the MTP port on which JADE is running (by default: 1099).
	 * @param newMtpPort the new local jade MTP port 
	 */
	public void setJadeLocalPortMTP(int newMtpPort) {
		localeJadeLocalPortMTP = newMtpPort;
	}
	/**
	 * Returns the MTP port on which JADE will run. 
	 * @return the localeJadeDefaultPort
	 */
	public Integer getJadeLocalPortMTP() {
		return localeJadeLocalPortMTP;
	}
	
	/**
	 * Sets the {@link JadeUrlConfiguration} for the server.master.
	 * @param newJadeUrlConfiguration the new {@link JadeUrlConfiguration} for master
	 */
	public void setJadeUrlConfigurationForMaster(JadeUrlConfiguration newJadeUrlConfiguration) {
		this.urlConfigurationForMaster = newJadeUrlConfiguration;
	}
	/**
	 * Returns the {@link JadeUrlConfiguration} for the server.master.
	 * @return the JadeUrlConfiguration
	 */
	public JadeUrlConfiguration getJadeUrlConfigurationForMaster() {
		if (urlConfigurationForMaster==null) {
			// --- Define the address of the server.master platform -----------
			urlConfigurationForMaster = new JadeUrlConfiguration(this.getServerMasterURL());
			if(urlConfigurationForMaster.hasErrors() == false){
				urlConfigurationForMaster.setPort(this.getServerMasterPort());
				urlConfigurationForMaster.setPort4MTP(this.getServerMasterPort4MTP());
				urlConfigurationForMaster.setMtpProtocol(this.getServerMasterProtocol().toString());
			}
		}
		return urlConfigurationForMaster;
	}
	
	/**
	 * This method return the default platform configuration for JADE
	 * @return instance of class 'PlatformJadeConfig', which holds the configuration of JADE
	 * @see PlatformJadeConfig
	 */
	public PlatformJadeConfig getJadeDefaultPlatformConfig() {
		
		// --- Here the default-values can be configured ------------
		PlatformJadeConfig jadeConfig = new PlatformJadeConfig();
		jadeConfig.setLocalPort(localeJadeLocalPort);
		
		jadeConfig.addService(PlatformJadeConfig.SERVICE_AgentGUI_LoadService);
		jadeConfig.addService(PlatformJadeConfig.SERVICE_AgentGUI_SimulationService);
		
		if (Application.isRunningAsServer()==false) {
			// --- Running as application ---------------------------
			jadeConfig.addService(PlatformJadeConfig.SERVICE_AgentMobilityService);
		}
		return jadeConfig;
	}
	/**
	 * This Method returns the default JADE container-profile
	 * of the AgentGUI-Application
	 *  
	 * @return The local JADE default profile
	 * @see ProfileImpl
	 */
	public ProfileImpl getJadeDefaultProfile() {
		PlatformJadeConfig jadeConfig = this.getJadeDefaultPlatformConfig();
		return jadeConfig.getNewInstanceOfProfilImpl();
	}

	// ---------------------------------------------------------
	// --- Farbvariablen ---------------------------------------
	// ---------------------------------------------------------
	/**
	 * Due this method the colour for menu highlighting an be get
	 * @return colour for highlighted menus
	 */
	public Color ColorMenuHighLight () {
		return localColorMenuHighLight;
	}


	// ---------------------------------------------------------
	// --- File-Properties -------------------------------------
	// ---------------------------------------------------------
	/**
	 * Gets the version info.
	 * @return the version info
	 */
	public VersionInfo getVersionInfo() {
		if (this.versionInfo==null) {
			this.versionInfo = new VersionInfo();
		}
		return this.versionInfo;
	}
	/**
	 * Gets the file properties.
	 * @return the file properties
	 */
	public FileProperties getFileProperties() {
		if (this.fileProperties==null) {
			this.fileProperties = new FileProperties(this);
		}
		return this.fileProperties;
	}

	// ---- SciMark 2.0 Benchmark ----------------------------
	/**
	 * Returns the benchmark value of the current execution of Agent.GUI that 
	 * will be measured at the initial program execution (if required)
	 * @return The benchmark value, stored in the file properties
	 * @see BenchmarkMeasurement
	 * @see FileProperties
	 */
	public Float getBenchValue() {
		return filePropBenchValue;
	}
	/**
	 * Can be used in order to set the benchmark value in the file properties
	 * @param benchValue The result of the initial benchmark
	 * @see BenchmarkMeasurement
	 * @see FileProperties
	 */
	public void setBenchValue(float benchValue) {
		this.filePropBenchValue = benchValue;
	}

	/**
	 * Here the reminder value can be get, which stores the computer/host name on which Agent.GUI 
	 * was executed the last time (evaluated due: <code>InetAddress.getLocalHost().getCanonicalHostName())</code>.<br>
	 * This can prevent the execution of the benchmark measurement, if the application will be 
	 * executed on the same machine next time.  
	 *   
	 * @return the filePropBenchExecOn
	 * @see BenchmarkMeasurement
	 * @see FileProperties
	 */
	public String getBenchExecOn() {
		return filePropBenchExecOn;
	}
	/**
	 * This method can be used in order to set the reminder value for the computer/host name on which
	 * the benchmark test was executed the last time (With Agent.GUI evaluated due: <code>InetAddress.getLocalHost().getCanonicalHostName()</code>)
	 *  
	 * @param benchExecOn the filePropBenchExecOn to set
	 * @see BenchmarkMeasurement
	 * @see FileProperties
	 */
	public void setBenchExecOn(String benchExecOn) {
		this.filePropBenchExecOn = benchExecOn;
	}

	/**
	 * Returns if the file properties are configured to always skip the benchmark
	 * @return the filePropBenchAllwaySkip
	 * @see BenchmarkMeasurement
	 * @see FileProperties
	 */
	public boolean isBenchAllwaysSkip() {
		return filePropBenchAllwaysSkip;
	}
	/**
	 * Can be used in order to set the file properties to always skip the benchmark
	 * @param benchAllwaySkip the filePropBenchAllwaySkip to set
	 * @see BenchmarkMeasurement
	 * @see FileProperties
	 */
	public void setBenchAllwaysSkip(boolean benchAllwaySkip) {
		this.filePropBenchAllwaysSkip = benchAllwaySkip;
	}

	// ---- Currently used language ------------------------------------
	/**
	 * This method can be used in order to set the current application language
	 * to the property file. 
	 * @param currentLanguage the filePropLanguage to set
	 * @see BenchmarkMeasurement
	 * @see FileProperties
	 */
	public void setLanguage(String currentLanguage) {
		this.filePropLanguage = currentLanguage;
	}
	/**
	 * Returns the currently configured language of the file properties
	 * @return the filePropLanguage
	 * @see BenchmarkMeasurement
	 * @see FileProperties
	 */
	public String getLanguage() {
		return filePropLanguage;
	}

	/**
	 * Sets to initially maximize the {@link MainWindow}.
	 * @param isMaximzeMainWindow the new maximze indicator
	 */
	public void setMaximzeMainWindow(boolean isMaximzeMainWindow) {
		maximizeMainWindow = isMaximzeMainWindow;
	}
	/**
	 * Returns if the {@link MainWindow} has to be maximized at start up.
	 * @return true, if the main window is to be maximzed 
	 */
	public boolean isMaximzeMainWindow() {
		return maximizeMainWindow;
	}
	
	
	// ---- Logging configuration -----------------------------------
	/**
	 * Sets the logging enabled (or not).
	 * @param enableLogging the new logging enabled
	 */
	public void setLoggingEnabled(boolean enableLogging) {
		this.filePropLoggingEnabled = enableLogging;
	}
	/**
	 * Checks if the file logging is enabled.
	 * @return true, if file logging is enabled
	 */
	public boolean isLoggingEnabled() {
		return this.filePropLoggingEnabled;
	}
	
	/**
	 * Sets the logging base path.
	 * @param newLoggingbasePath the new logging base path
	 */
	public void setLoggingBasePath(String newLoggingbasePath) {
		if (newLoggingbasePath!=null && newLoggingbasePath.trim().equals("")==false) {
			this.filePropLoggingBasePath = newLoggingbasePath;
		}
	}
	/**
	 * Returns the relative logging base path. If not set differently, the method will return the default path.
	 * @return the logging base path
	 */
	public String getLoggingBasePath() {
		return this.getLoggingBasePath(false);
	}
	/**
	 * Returns the relative logging base path. If not set differently, the method will return the default path.
	 * @param forcePathCreation set true, if the directory should be created
	 * @return the logging base path
	 */
	public String getLoggingBasePath(boolean forcePathCreation) {
		if (filePropLoggingBasePath==null) {
			filePropLoggingBasePath = getLoggingBasePathDefault();
		}
		if (forcePathCreation==true) {
			this.createDirectoryIfRequired(filePropLoggingBasePath);
		}
		return filePropLoggingBasePath;
	}
	/**
	 * Returns the default logging base path.
	 * @return the default logging base path
	 */
	public static String getLoggingBasePathDefault() {
		String basePath = Application.getGlobalInfo().getPathBaseDir();
		String defaultLoggingBasePath = basePath + "log";
		String os = System.getProperty("os.name");
		if (os.toLowerCase().contains("windows")==true) {
			// --- nothing to do here ---
		} else if (os.toLowerCase().contains("linux")==true) {
			defaultLoggingBasePath = "/var/log/agentgui";
		}
		return defaultLoggingBasePath.replace("/", File.separator);
	}
	
	/**
	 * Returns the logging directory by month.
	 * @param timeStamp the timestamp
	 * @param forcePathCreation set true, if the directory should be created 
	 * @return the logging directory by month
	 */
	public String getLoggingPathByMonth(long timeStamp, boolean forcePathCreation){
		Date date = new Date(timeStamp);
		String monthDescriptor = new SimpleDateFormat("MM").format(date) + "_" + new SimpleDateFormat("MMM").format(date);
		String logPathByMonth = this.getLoggingBasePath(forcePathCreation) + File.separator + monthDescriptor + File.separator; 
		if (forcePathCreation==true) {
			this.createDirectoryIfRequired(logPathByMonth);
		}
		return logPathByMonth;
	}
	/**
	 * Returns the day prefix for log files using the specified timestamp (e.g. DAY_18).
	 * @param timeStamp the time stamp
	 * @return the time stamp prefix
	 */
	public String getLoggingDayPrefix(long timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return "DAY_" + sdf.format(new Date(timeStamp));
	}

	
	// ---- Connection to the Master-Server -------------------------
	/**
	 * Can be used in order to start the active server mode immediately after the program execution.
	 * This applies only if the current execution mode is set to server (server.master or server.slave)
	 * @param serverAutoRun the filePropAutoRun to set
	 * @see FileProperties
	 */
	public void setServerAutoRun(boolean serverAutoRun) {
		this.filePropServerAutoRun = serverAutoRun;
	}
	/**
	 * This method returns the current setting for the file property '10_AUTOSTART', which is 
	 * used for whether or not to start the server execution mode immediately after the program
	 * execution. This applies only if the current execution mode is set to server (server.master 
	 * or server.slave)
	 * 
	 * @return the filePropAutoRun
	 * @see FileProperties
	 * @see GlobalInfo#setServerAutoRun(boolean)
	 */
	public boolean isServerAutoRun() {
		return this.filePropServerAutoRun;
	}
	/**
	 * Here the URLS or IP of the server.master can be set 
	 * @param serverMasterURL the filePropMasterURL to set
	 * @see FileProperties
	 */
	public void setServerMasterURL(String serverMasterURL) {
		this.filePropServerMasterURL = serverMasterURL;
		this.setJadeUrlConfigurationForMaster(null);
	}
	/**
	 * Here the URL or IP of the server.master can be get
	 * @return the filePropMasterURL
	 * @see FileProperties
	 */
	public String getServerMasterURL() {
		return this.filePropServerMasterURL;
	}
	/**
	 * Here the port for the server.master can be set
	 * @param serverMasterPort the filePropMasterPort to set
	 * @see FileProperties
	 */
	public void setServerMasterPort(Integer serverMasterPort) {
		this.filePropServerMasterPort = serverMasterPort;
		this.setJadeUrlConfigurationForMaster(null);
	}
	/**
	 * This method returns the port on which the server.master can be reached 
	 * @return The port of the server.master
	 * @see FileProperties
	 */
	public Integer getServerMasterPort() {
		return this.filePropServerMasterPort;
	}
	/**
	 * This method can be used in order to set the MTP port of the server.master
	 * @param serverMasterPort4MTP the filePropMasterPort to set
	 * @see FileProperties
	 */
	public void setServerMasterPort4MTP(Integer serverMasterPort4MTP) {
		this.filePropServerMasterPort4MTP = serverMasterPort4MTP;
		this.setJadeUrlConfigurationForMaster(null);
	}
	/**
	 * Returns the MTP port of the server.master 
	 * @return the filePropMasterPort
	 * @see FileProperties
	 */
	public Integer getServerMasterPort4MTP() {
		return this.filePropServerMasterPort4MTP;
	}
	
	/**
	 * Gets the filePropServerMasterProtocol.
	 * @return the file prop server master protocol
	 * @see FileProperties
	 */
	public MtpProtocol getServerMasterProtocol() {
		return filePropServerMasterProtocol;
	}
	
	/**
	 * Sets the filePropServerMasterProtocol.
	 * @param filePropServerMasterProtocol the new filePropServerMasterProtocol
	 * @see FileProperties
	 */
	public void setServerMasterProtocol(MtpProtocol filePropServerMasterProtocol) {
		this.filePropServerMasterProtocol = filePropServerMasterProtocol;
	}
	
	/**
	 * Sets the own MTP creation settings.
	 * @param ownMtpCreation the new own MTP creation
	 */
	public void setOwnMtpCreation(MTP_Creation ownMtpCreation) {
		this.filePropOwnMtpCreation = ownMtpCreation;
	}
	/**
	 * Returns the setting for the own MTP creation.
	 * @return the own MTP creation settings
	 */
	public MTP_Creation getOwnMtpCreation() {
		return filePropOwnMtpCreation;
	}
	/**
	 * Sets the own IP-address that should be used with the MTP.
	 * @param filePropOwnMtpIP the new own MTP IP-Address
	 */
	public void setOwnMtpIP(String filePropOwnMtpIP) {
		this.filePropOwnMtpIP = filePropOwnMtpIP;
	}
	/**
	 * Returns the own MTP IP-address.
	 * @return the own MTP IP-address
	 */
	public String getOwnMtpIP() {
		return filePropOwnMtpIP;
	}
	/**
	 * Sets the own MTP port.
	 * @param newOwnMtpPort the new own MTP port
	 */
	public void setOwnMtpPort(Integer newOwnMtpPort) {
		this.filePropOwnMtpPort = newOwnMtpPort;
	}
	/**
	 * Returns the own MTP port.
	 * @return the own MTP port
	 */
	public Integer getOwnMtpPort() {
		return filePropOwnMtpPort;
	}
	
	/**
	 * Sets the MTP protocol.
	 * @param newMtpProtocol the new MTP protocol
	 */
	public void setMtpProtocol(MtpProtocol newMtpProtocol) {
		this.filePropMtpProtocol = newMtpProtocol;
	}
	
	/**
	 * Gets the MTP protocol.
	 * @return the MTP protocol
	 */
	public MtpProtocol getMtpProtocol() {
		return filePropMtpProtocol;
	}
	/**
	 * Database property for the server.nmaster
	 * @param newDBHost the filePropServerMasterDBHost to set
	 * @see FileProperties
	 */
	public void setServerMasterDBHost(String newDBHost) {
		this.filePropServerMasterDBHost = newDBHost;
	}
	/**
	 * Database property for the server.nmaster 
	 * @return the filePropServerMasterDBHost
	 * @see FileProperties
	 */
	public String getServerMasterDBHost() {
		return filePropServerMasterDBHost;
	}
	/**
	 * Database property for the server.nmaster
	 * @param newDBName the filePropServerMasterDBName to set
	 * @see FileProperties
	 */
	public void setServerMasterDBName(String newDBName) {
		this.filePropServerMasterDBName = newDBName;
	}
	/**
	 * Database property for the server.nmaster
	 * @return the filePropServerMasterDBName
	 * @see FileProperties
	 */
	public String getServerMasterDBName() {
		return filePropServerMasterDBName;
	}
	/**
	 * Database property for the server.nmaster
	 * @param newDBUser the filePropServerMasterDBUser to set
	 * @see FileProperties
	 */
	public void setServerMasterDBUser(String newDBUser) {
		this.filePropServerMasterDBUser = newDBUser;
	}
	/**
	 * Database property for the server.nmaster
	 * @return the filePropServerMasterDBUser
	 * @see FileProperties
	 */
	public String getServerMasterDBUser() {
		return filePropServerMasterDBUser;
	}
	/**
	 * Database property for the server.nmaster
	 * @param newDBPswd the filePropServerMasterDBPswd to set
	 * @see FileProperties
	 */
	public void setServerMasterDBPswd(String newDBPswd) {
		this.filePropServerMasterDBPswd = newDBPswd;
	}
	/**
	 * Database property for the server.nmaster
	 * @return the filePropServerMasterDBPswd
	 * @see FileProperties
	 */
	public String getServerMasterDBPswd() {
		return filePropServerMasterDBPswd;
	}

	// ---- Methods for the reminder of the last selected folder ----
	/**
	 * This method can be used in order to remind the last folder 
	 * in which a file was selected (e. g. while using a JFileChooser) 
	 * @param lastSelectedFolder the lastSelectedFolder to set
	 */
	@Override
	public void setLastSelectedFolder(File lastSelectedFolder) {
		this.lastSelectedFolder = lastSelectedFolder;
	}
	/**
	 * Returns the reminder value of the last selected folder as File object 
	 * @return the lastSelectedFolder
	 */
	@Override
	public File getLastSelectedFolder() {
		if (lastSelectedFolder==null) {
			lastSelectedFolder = new File(this.getPathBaseDir());
		} 
		return lastSelectedFolder;	
	}
	/**
	 * Returns the reminder value of the last selected folder as String
	 * @return the lastSelectedFolder as String
	 */
	public String getLastSelectedFolderAsString() {
		String returnFolder = this.getLastSelectedFolder().getAbsolutePath();
		if (returnFolder.endsWith(File.separator)==false) {
			returnFolder+=File.separator;
		}
		return returnFolder;
	}
	
	// -------------------------------------------------------------------------
	// ---- Methods for EnvironmentTypes ---------------------------------------
	// -------------------------------------------------------------------------
	/**
	 * This method can be used in order to define the predefined environment types of Agent.GUI
	 * @param knownEnvironmentTypes the knowEnvironmentTypes to set
	 * @see EnvironmentType
	 * @see EnvironmentTypes
	 */
	public void setKnownEnvironmentTypes(EnvironmentTypes knownEnvironmentTypes) {
		this.knownEnvironmentTypes = knownEnvironmentTypes;
	}
	/**
	 * This method returns all EnvironmentTypes known by Agent.GUI 
	 * @return the knowEnvironmentTypes
	 * @see EnvironmentType
	 * @see EnvironmentTypes
	 */
	public EnvironmentTypes getKnownEnvironmentTypes() {
		return knownEnvironmentTypes;
	}
	/**
	 * This method can be used in order to add a tailored environment type
	 * (assume for example a 3D-environment model)  
	 * @param envType
	 * @see EnvironmentType
	 */
	public void addEnvironmentType(EnvironmentType envType ) {
		this.knownEnvironmentTypes.add(envType);
	}
	/**
	 * This method can be used in order to remove a tailored environment type
	 * @param envType The EnvironmentType instance
	 * @see EnvironmentType
	 */
	public void removeEnvironmentType(EnvironmentType envType) {
		this.knownEnvironmentTypes.remove(envType);
	}
	/**
	 * This method can be used in order to remove a tailored environment type
	 * @param envTypeKey The key expression of the environment type
	 * @see EnvironmentType
	 */
	public void removeEnvironmentType(String envTypeKey) {
		EnvironmentType envType = this.knownEnvironmentTypes.getEnvironmentTypeByKey(envTypeKey);
		this.removeEnvironmentType(envType);
	}
	
	// -------------------------------------------------------------------------
	// ---- Methods for OntologyClassVisualisations ----------------------------
	// -------------------------------------------------------------------------
	/**
	 * Register an OntologyClassVisualisation.
	 * @param classNameOfOntologyClassVisualisation the class name of the OntologyClassVisualisation
	 */
	public OntologyClassVisualisation registerOntologyClassVisualisation(String classNameOfOntologyClassVisualisation) {
		
		OntologyClassVisualisation ontoClassVisualisation = null;
		try {
			if (this.isOntologyClassVisualisation(classNameOfOntologyClassVisualisation)==false) {
				ontoClassVisualisation = ClassLoadServiceUtility.getOntologyClassVisualisationInstance(classNameOfOntologyClassVisualisation);
				this.getKnownOntologyClassVisualisations().add(ontoClassVisualisation);	
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ontoClassVisualisation;
	}
	/**
	 * Unregister an OntologyClassVisualisation.
	 * @param classNameOfOntologyClassVisualisation the class name of the OntologyClassVisualisation
	 */
	public void unregisterOntologyClassVisualisation(String classNameOfOntologyClassVisualisation) {
		if (this.isOntologyClassVisualisation(classNameOfOntologyClassVisualisation)==true) {
			OntologyClassVisualisation ontoClassVisualisation=getOntologyClassVisualisation(classNameOfOntologyClassVisualisation);
			this.getKnownOntologyClassVisualisations().add(ontoClassVisualisation);	
		}
	}
	/**
	 * Unregister an OntologyClassVisualisation.
	 * @param ontologyClassVisualisation the OntologyClassVisualisation to unregister
	 */
	public void unregisterOntologyClassVisualisation(OntologyClassVisualisation ontologyClassVisualisation) {
		if (this.isOntologyClassVisualisation(ontologyClassVisualisation)==true) {
			this.getKnownOntologyClassVisualisations().add(ontologyClassVisualisation);	
		}
	}
	
	/**
	 * Returns the known ontology class visualisations.
	 * @return the Vector of known ontology class visualisations
	 */
	public Vector<OntologyClassVisualisation> getKnownOntologyClassVisualisations() {
		if (knownOntologyClassVisualisation==null) {
			knownOntologyClassVisualisation = new Vector<OntologyClassVisualisation>();
		}
		return knownOntologyClassVisualisation;
	}
	
	/**
	 * Checks if a given object can be visualized by a special OntologyClassVisualisation.
	 *
	 * @param checkObject the object to check
	 * @return true, if the given Object is ontology class visualisation
	 */
	public boolean isOntologyClassVisualisation(Object checkObject) {
		if (checkObject==null) return false;
		return this.isOntologyClassVisualisation(checkObject.getClass());
	}
	/**
	 * Checks if a given class can be visualized by a special OntologyClassVisualisation.
	 *
	 * @param checkClass the class to check
	 * @return true, if the given Object is ontology class visualisation
	 */
	public boolean isOntologyClassVisualisation(Class<?> checkClass) {
		if (checkClass==null) return false;
		return this.isOntologyClassVisualisation(checkClass.getName());
	}
	/**
	 * Checks class, given by its name, can be visualized by a special OntologyClassVisualisation.
	 *
	 * @param className the object to check
	 * @return true, if the given className is ontology class visualisation
	 */
	public boolean isOntologyClassVisualisation(String className) {
		
		boolean isVisClass = false;
		
		Vector<OntologyClassVisualisation> ontoClassViss = this.getKnownOntologyClassVisualisations();
		for (int i=0; i<ontoClassViss.size(); i++) {
			OntologyClassVisualisation ontoClassVis = ontoClassViss.get(i);
			String visClassName = ontoClassVis.getOntologyClass().getName();
			if (visClassName.equals(className)) {
				isVisClass=true;
				break;
			}
		}
		return isVisClass;
	}
	
	/**
	 * Returns the OntologyClassVisualisation for a given object.
	 *
	 * @param checkObject the check object
	 * @return the ontology class visualisation
	 */
	public OntologyClassVisualisation getOntologyClassVisualisation(Class<?> checkObject) {
		return this.getOntologyClassVisualisation(checkObject.getName());
	}
	/**
	 * Returns the OntologyClassVisualisation for a given class, specified by its name.
	 *
	 * @param className the class name
	 * @return the OntologyClassVisualisation
	 */
	public OntologyClassVisualisation getOntologyClassVisualisation(String className) {
		
		OntologyClassVisualisation ontoClassVisFound = null;
		Vector<OntologyClassVisualisation> ontoClassViss = this.getKnownOntologyClassVisualisations();
		for (int i=0; i<ontoClassViss.size(); i++) {
			OntologyClassVisualisation ontoClassVis = ontoClassViss.get(i);
			String compareWith = ontoClassVis.getOntologyClass().getName();
			if (compareWith.equals(className)==true) {
				ontoClassVisFound=ontoClassVis;
				break;
			}
		}
		return ontoClassVisFound;
	}
	

	// -------------------------------------------------------------------------
	// ---- Methods for the API Key for the Google Translation -----------------
	// -------------------------------------------------------------------------
	/**
	 * Returns the API key for Google.
	 * @return the API key for Google
	 */
	public String getGoogleKey4API() {
		return this.googleKey4API;
	}
	/**
	 * Sets the API key for Google.
	 * @param key4API the new API key for Google.
	 */
	public void setGoogleKey4API(String key4API) {
		this.googleKey4API = key4API;
	}
	/**
	 * Returns the HTTP-Reference for Google translate.
	 * @return the HTTP-Reference for Google translate
	 */
	public String getGoogleHttpRef() {
		return this.googleHTTPref;
	}
	/**
	 * Sets the HTTP-Reference for Google translate.
	 * @param httpRef the new HTTP-Reference for Google translate.
	 */
	public void setGoogleHttpRef(String httpRef) {
		this.googleHTTPref = httpRef;
	}

	/**
	 * Sets the update site.
	 * @param updateSite the new update site
	 */
	public void setUpdateSite(String updateSite) {
		this.updateSite = updateSite;
	}
	
	/**
	 * Returns the update site.
	 * @return the update site
	 */
	public String getUpdateSite() {
		return updateSite;
	}
	
	/**
	 * Sets the OIDC issuer URI.
	 * @param OIDCIssuerURI the new OIDC issuer URI
	 */
	public void setOIDCIssuerURI(String OIDCIssuerURI) {
		this.oidcIssuerURI = OIDCIssuerURI;
	}
	
	/**
	 * Returns the OIDC issuer URI.
	 * @return the OIDCIssuerURI
	 */
	public String getOIDCIssuerURI() {
		return oidcIssuerURI;
	}

	/**
	 * Sets the update auto configuration.
	 * @param updateAutoConfiguration the new update auto configuration
	 */
	public void setUpdateAutoConfiguration(Integer updateAutoConfiguration) {
		this.updateAutoConfiguration = updateAutoConfiguration;
	}
	/**
	 * Returns the update auto configuration.
	 * @return the update auto configuration
	 */
	public Integer getUpdateAutoConfiguration() {
		return updateAutoConfiguration;
	}
	
	/**
	 * Sets the update keep dictionary.
	 * @param updateKeepDictionary the new update keep dictionary
	 */
	public void setUpdateKeepDictionary(Integer updateKeepDictionary) {
		this.updateKeepDictionary = updateKeepDictionary;
	}
	/**
	 * Gets the update keep dictionary.
	 * @return the update keep dictionary
	 */
	public Integer getUpdateKeepDictionary() {
		return updateKeepDictionary;
	}
	
	/**
	 * Sets the update date last checked.
	 * @param updateDateLastChecked the new update date last checked
	 */
	public void setUpdateDateLastChecked(long updateDateLastChecked) {
		this.updateDateLastChecked = updateDateLastChecked;
	}
	/**
	 * Gets the update date last checked.
	 * @return the update date last checked
	 */
	public Long getUpdateDateLastChecked() {
		return updateDateLastChecked;
	}
	
	/**
	 * Gets the device service project folder.
	 * @return the device service project folder
	 */
	public String getDeviceServiceProjectFolder() {
		return deviceServiceProjectFolder;
	}
	/**
	 * Sets the device service project folder.
	 * @param deviceServiceProjectFolder the new device service project folder
	 */
	public void setDeviceServiceProjectFolder(String deviceServiceProjectFolder) {
		this.deviceServiceProjectFolder = deviceServiceProjectFolder;
	}
	
	/**
	 * Gets the device service execution mode.
	 * @return the device service execution mode
	 */
	public DeviceSystemExecutionMode getDeviceServiceExecutionMode() {
		return deviceServiceExecutionMode;
	}
	/**
	 * Sets the device service execution mode.
	 * @param deviceServiceExecutionMode the new device service execution mode
	 */
	public void setDeviceServiceExecutionMode(DeviceSystemExecutionMode deviceServiceExecutionMode) {
		this.deviceServiceExecutionMode = deviceServiceExecutionMode;
	}
	
	/**
	 * Gets the device service setup selected.
	 * @return the device service setup selected
	 */
	public String getDeviceServiceSetupSelected() {
		return deviceServiceSetupSelected;
	}
	/**
	 * Sets the device service setup selected.
	 * @param deviceServiceSetupSelected the new device service setup selected
	 */
	public void setDeviceServiceSetupSelected(String deviceServiceSetupSelected) {
		this.deviceServiceSetupSelected = deviceServiceSetupSelected;
	}
	
	/**
	 * Returns the device service agent class name.
	 * @return the device service agent class name
	 */
	public String getDeviceServiceAgentClassName() {
		return deviceServiceAgentClassName;
	}
	/**
	 * Sets the device service agent class name.
	 * @param deviceServiceAgentClassName the new device service agent class to use
	 */
	public void setDeviceServiceAgentClassName(String deviceServiceAgentClassName) {
		this.deviceServiceAgentClassName = deviceServiceAgentClassName;
	}
	
	/**
	 * Returns the device service agent name.
	 * @return the device service agent name
	 */
	public String getDeviceServiceAgentName() {
		return deviceServiceAgentName;
	}
	/**
	 * Sets the device service agent name.
	 * @param deviceServiceAgentName the new device service agent name 
	 */
	public void setDeviceServiceAgentName(String deviceServiceAgentName) {
		this.deviceServiceAgentName = deviceServiceAgentName;
	}
	
	/**
	 * Gets the device service agent visualisation.
	 * @return the device service agent visualisation
	 */
	public EmbeddedSystemAgentVisualisation getDeviceServiceAgentVisualisation() {
		return deviceServiceAgentVisualisation;
	}
	/**
	 * Sets the device service agent visualisation.
	 * @param deviceServiceAgentVisualisation the new device service agent visualisation
	 */
	public void setDeviceServiceAgentVisualisation(EmbeddedSystemAgentVisualisation deviceServiceAgentVisualisation) {
		this.deviceServiceAgentVisualisation = deviceServiceAgentVisualisation;
	}
	
	/**
	 * Gets the KeyStoreFile.
	 * @return KeyStoreFile
	 */
	public String getKeyStoreFile() {
		return filePropKeyStoreFile;
	}
	
	/**
	 * Sets the KeyStoreFile.
	 * @param filePropKeyStoreFile the new KeyStoreFile
	 */
	public void setKeyStoreFile(String filePropKeyStoreFile) {
		this.filePropKeyStoreFile = filePropKeyStoreFile;
	}

	/**
	 * Gets the TrustStore file.
	 * @return the TrustStore file
	 */
	public String getTrustStoreFile() {
		return filePropTrustStoreFile;
	}
	/**
	 * Sets the TrustStore file.
	 * @param filePropTrustStoreFile the new TrustStore file
	 */
	public void setTrustStoreFile(String filePropTrustStoreFile) {
		this.filePropTrustStoreFile = filePropTrustStoreFile;
	}
	
	/**
	 * Gets the KeyStorePassword
	 * @return the KeyStorePassword
	 */
	public String getKeyStorePassword() {
		return this.pwDecrypt(this.getKeyStorePasswordEncrypted());
	}
	/**
	 * Sets the KeyStore password.
	 * @param newPassword the new KeyStore password
	 */
	public void setKeyStorePassword(String newPassword) {
		this.setKeyStorePasswordEncrypted(this.pwEncrypt(newPassword));
	}
	
	/**
	 * Gets the KeyStorePasswordEncrypted.
	 * @return the filePropKeyStorePasswordEncrypted
	 */
	public String getKeyStorePasswordEncrypted() {
		return filePropKeyStorePasswordEncrypted;
	}
	
	/**
	 * Sets the KeyStorePasswordEncrypted.
	 * @param filePropKeyStorePasswordEncrypted the new KeyStorePasswordEncrypted
	 */
	public void setKeyStorePasswordEncrypted(String filePropKeyStorePasswordEncrypted) {
		this.filePropKeyStorePasswordEncrypted = filePropKeyStorePasswordEncrypted;
	}
	/**
	 * Gets the TrustStore password.
	 * @return the TrustStore password
	 */
	public String getTrustStorePassword() {
		return this.pwDecrypt(this.getTrustStorePasswordEncrypted());
	}
	/**
	 * Sets the TrustStore password.
	 * @param newPassword the new TrustStore password
	 */
	public void setTrustStorePassword(String newPassword) {
		this.setTrustStorePasswordEncrypted(this.pwEncrypt(newPassword));
	}
	
	/**
	 * Sets the trust store password encrypted.
	 * @param filePropTrustStorePasswordEncrypted the new trust store password encrypted
	 */
	public void setTrustStorePasswordEncrypted(String filePropTrustStorePasswordEncrypted) {
		this.filePropTrustStorePasswordEncrypted = filePropTrustStorePasswordEncrypted;
	}
	
	/**
	 * Gets the TrustStorePasswordEncrypted.
	 * @return the TrustStorePasswordEncrypted
	 */
	public String getTrustStorePasswordEncrypted() {
		return filePropTrustStorePasswordEncrypted;
	}
	
	/**
	 * Sets the OpenID Connect user name
	 * @param oidcUsername the user name for the OpenID Connect protocol
	 */
	public void setOIDCUsername(String oidcUsername) {
		this.oidcUsername=oidcUsername;		
	}
	
	/**
	 * Gets the OpenID Connect user name
	 * @return the user name for the OpenID Connect protocol
	 */
	public String getOIDCUsername() {
		return oidcUsername;
	}
	
	// ------------------------------------------------------------------------------------------------------
	// ---- From here some help methods for container and component handling can be found --- Start --------- 
	// ------------------------------------------------------------------------------------------------------
	/**
	 * Returns the top parent component of the specified component.
	 * @param component the component
	 * @return the top parent component
	 */
	public Component getTopParentComponent(Component component) {
		Component compFound = component;
		if (compFound!=null) {
			while (compFound.getParent()!=null) {
				compFound = compFound.getParent();
			}	
		}
		return compFound;
	}
	/**
	 * Returns the owner frame for the specified JComponent.
	 * @param component the JComponent
	 * @return the owner frame for component or null, if no component was specified
	 */
	public Frame getOwnerFrameForComponent(JComponent component) {
		if (component== null) return null;
		return this.getOwnerFrameForContainer(component.getParent());
	}
	/**
	 * Returns the owner frame for the specified Container.
	 * @param container the container
	 * @return the owner frame for component or null, if no container was specified
	 */
	public Frame getOwnerFrameForContainer(Container container) {
		if (container==null) return null;
		Frame frameFound = null;
		Container currComp = container;
		while (currComp!=null) {
			if (currComp instanceof Frame) {
				frameFound = (Frame) currComp;
				break;
			}
			currComp = currComp.getParent();
		}
		return frameFound;
	}
	/**
	 * Returns the owner dialog for a JComponent.
	 * @param component the JComponent
	 * @return the owner dialog for component
	 */
	public Dialog getOwnerDialogForComponent(JComponent component) {
		Dialog dialogFound = null;
		Container currComp = component.getParent();
		while (currComp!=null) {
			if (currComp instanceof Dialog) {
				dialogFound = (Dialog) currComp;
				break;
			}
			currComp = currComp.getParent();
		}
		return dialogFound;
	}
	
	/**
	 * Returns the path to the internal image-package of Agent.GUI (de.enflexit.common.swing.img)
	 * @return path to the images, which are located in our project
	 */
	public static String getPathImageIntern(){
		return localPathImageIntern;
	}
	/**
	 * Returns one of the internal images as ImageIcon, specified by its file name.
	 * @param imageFileName the image file name
	 * @return the internal image icon
	 */
	public static ImageIcon getInternalImageIcon(String imageFileName) {
		return new ImageIcon(GlobalInfo.class.getResource(getPathImageIntern() + imageFileName));
	}
	/**
	 * Returns one of the internal images specified by its file name.
	 * @param imageFileName the image file name
	 * @return the internal image
	 */
	public static Image getInternalImage(String imageFileName) {
		ImageIcon imageIcon = new ImageIcon(GlobalInfo.class.getResource(getPathImageIntern() + imageFileName));
		return imageIcon.getImage();
	}
	/**
	 * Returns the specified bytes in a human readable byte count.
	 *
	 * @param bytes the bytes
	 * @param si set true, if you want to use SI conversion
	 * @return the human readable byte count
	 */
	public static String getHumanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	/**
	 * Returns the next midnight time stamp for the specified time.
	 * @return the next midnight time stamp
	 */
	public static long getNextMidnightFromTimeStamp(long timeStamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeStamp);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	
	/**
	 * This method is used to decrypt password.
	 * @param encryptedPSWD the encrypted password
	 * @return the password decrypted
	 */
	public String pwDecrypt(String encryptedPSWD) {
		String pwDecrypted = null;
		if (encryptedPSWD!=null && encryptedPSWD.trim().equals("")==false) {
			try {
				pwDecrypted = new String(Base64.decodeBase64(encryptedPSWD.getBytes()), "UTF8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return pwDecrypted;
	}
	
	/**
	 * This method is used to encrypt password.
	 * @param pswd the password
	 * @return the password encrypted
	 */
	public String pwEncrypt(String pswd) {
		String encryptedPSWD = null;
		if (pswd!=null && pswd.trim().equals("")==false) {
			try {
				encryptedPSWD = new String(Base64.encodeBase64(pswd.getBytes("UTF8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return encryptedPSWD;
	}

}
