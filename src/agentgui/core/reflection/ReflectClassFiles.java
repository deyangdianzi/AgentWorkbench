package agentgui.core.reflection;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import agentgui.core.application.Application;
import agentgui.core.application.Project;

public class ReflectClassFiles extends ArrayList<String> {

	private static final long serialVersionUID = -256361698681180954L;

	private Project currProject = null;
	
	private String SearchINReference = null;
	private String[] SearchINPathParts = null;

	private Vector<String> classPathExternalJars = null;
	
	/**
	 * Constructor of this class
	 * @param SearchReference
	 */
	public ReflectClassFiles(Project project, String SearchReference) {

		this.currProject = project;
		
		// --- Die aktuellen externen Ressourcen zusammenstellen -
		this.classPathExternalJars = Application.RunInfo.getClassPathExternalJars();
		this.classPathExternalJars.addAll(currProject.projectResources);
		
		// --- Verzeichnis, in dem die Ontologie liegt auslesen ---
		SearchINReference = SearchReference;
		if ( !(SearchINReference == null) ) {
			SearchINPathParts = SearchINReference.split("\\.");
		}
		this.setClasses();
	}
	
	/**
	 * Initial detection of the available classe by using the 'SearchReference'
	 */
	private void setClasses() {
		
		String SearchPath = null;
		List<File> dirs = new ArrayList<File>();
		ArrayList<String> ClazzList = null;
		
		// --- Try to find the resource of the given Reference ------
		try {
			dirs = getClassResources(SearchINReference);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// --- Look at the Result of the Ressources-Search ----------
		if (dirs.size()==1) {
			File directory = dirs.get(0);
			String PathOfFile = directory.toString();
			String reference2JarFile = getJarReferenceFromPathOfFile(PathOfFile);
			if ( PathOfFile.startsWith("file:") && reference2JarFile!=null ) {
				// --- Path points to a jar-file --------------------
				//System.out.println("Jar-Result: " + SearchINReference + " => " + reference2JarFile);
				reference2JarFile = PathOfFile.substring(0, PathOfFile.lastIndexOf(reference2JarFile)) + reference2JarFile;
				reference2JarFile = reference2JarFile.replace("file:\\", "");
				ClazzList = getJARClasses( reference2JarFile );
			} else {
				// --- Points to the IDE-Physical2DEnvironment ----------------
				//System.out.println("IDE-Result: " + SearchINReference);
				SearchPath = Application.RunInfo.PathBaseDirIDE_BIN();
				ClazzList = getIDEClasses( SearchPath, SearchPath );
			}
		}
		if(ClazzList!=null){
			this.addAll( ClazzList );
		}
	}

	/**
	 * This Method checks if a given Path-Description  
	 * is a description to an external jar-file 
	 * @param path2Reference
	 * @return
	 */
	private String getJarReferenceFromPathOfFile( String path2Reference ) {
		
		for (String classPathSingle : classPathExternalJars) {
			if (path2Reference.contains(classPathSingle)==true) {
				return classPathSingle;
			}
		}
		return null;
	}
	
	/**
	 * This method searches for the File-Reference to a given package (e.g. jade.tools.onto)
	 * @param packageName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private List<File> getClassResources(String packageName) throws ClassNotFoundException, IOException {
		
		String path = packageName.replace('.', '/');
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			String fileName = resource.getFile();
			String fileNameDecoded = URLDecoder.decode(fileName, "UTF-8");
			dirs.add(new File(fileNameDecoded));
		}
		return dirs;
	}
	    
	/**
	 * Reading the Classes from the inside of a jar-file
	 */
	private ArrayList<String> getJARClasses(String jarName) {
		
		String CurrClass   = "";
		ArrayList<String> classes = new ArrayList<String>();
		
		try {
			
			File file = new File(jarName);
			URL jar = file.toURI().toURL();
			jar = new URL("jar:" + jar.toExternalForm() + "!/");
			
			JarURLConnection conn = (JarURLConnection) jar.openConnection();
			conn.setUseCaches(false);
			
			JarFile jarFile = conn.getJarFile();
			Enumeration<JarEntry> enu = jarFile.entries();
			while (enu.hasMoreElements()) {

				JarEntry jarEntry = enu.nextElement();

				if ((jarEntry.getName().endsWith(".class"))) {
					CurrClass = jarEntry.getName().replaceAll("/", "\\.");
					CurrClass = CurrClass.substring(0, CurrClass.length() - (".class").length());
					// --- Klasse in die Auflistung aufnehmen ? ---
					if (SearchINReference == null) {
						classes.add( CurrClass );	
					} else {
						if (CurrClass.startsWith(SearchINReference) ) {
							classes.add( CurrClass );
						}		
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}
	
	
	
	/**
	 * Reading all Classes from the IDE area by starting at 'BasePath'
	 * @param BasePath
	 * @param SearchPath
	 * @return
	 */
	private ArrayList<String> getIDEClasses(String BasePath, String SearchPath) {

		ArrayList<String> FileList = new ArrayList<String>();
		
		int CutBegin = BasePath.toString().length();
		int CutEnd   = 0;
		String CurrClass = "";
		
		File dir = new File(SearchPath);
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				// --------------------------------------------------------------------
				// --------------------------------------------------------------------
				if ( files[i].isDirectory() ) {
					// ----------------------------------------------------------------
					// --- System.out.print(" (Unterordner)\n");
					if ( SearchINReference == null ) {
						// ------------------------------------------------------------
						// --- Falls nach nichts konkretem gesucht wird, dann --------- 
						// --- alles in die Ergebnisliste aufnehmen 		  ---------
						FileList.addAll( getIDEClasses( BasePath, files[i].getAbsolutePath() ) );	
					} else {
						// ------------------------------------------------------------
						// --- Nur das durchsuchen, was auch wirklich interessiert ----
						boolean MoveDeeper = false;
						String SearchINPath = null;
						// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						for (int j=0; j<SearchINPathParts.length; j++) {
							if ( SearchINPath == null ) {
								SearchINPath = SearchINPathParts[j];	
							} else {
								SearchINPath = SearchINPath +  Application.RunInfo.AppPathSeparatorString() + SearchINPathParts[j];
							}								
							// --- Aktuellen Pfad untersuchen / vergleichen -----------
							if ( files[i].getAbsolutePath().endsWith( SearchINPath) ) {
								MoveDeeper = true;
								break;
							}							
						}
						// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						if ( MoveDeeper == true ) {
							// --- eine Verzeichnisebene tiefer suchen ----------------
							FileList.addAll( getIDEClasses( BasePath, files[i].getAbsolutePath() ) );	
						}
						// ------------------------------------------------------------
						// ------------------------------------------------------------
						}					 
				} else {
					// ----------------------------------------------------------------
					// --- System.out.println("Datei: " + CurrClass );
					CurrClass = files[i].getAbsolutePath().toString();
					if ( CurrClass.endsWith(".class") ) {
						// --- String der Klassendatei anpassen -----------------------
						CutEnd    = CurrClass.length() - (".class").length();						
						CurrClass = CurrClass.substring(CutBegin, CutEnd);
						CurrClass = CurrClass.replace('/', '.').replace('\\', '.');						
						// --- Klasse in die Auflistung aufnehmen ? -------------------
						if ( SearchINReference == null ) {
							FileList.add( CurrClass );	
						} else {
							if (CurrClass.startsWith( SearchINReference ) ) {
								FileList.add( CurrClass );
							}		
						}
						// ------------------------------------------------------------
					}
				}
				// --------------------------------------------------------------------
				// --------------------------------------------------------------------
			}		
		}
		return FileList;
	}
	
	
	
	
}