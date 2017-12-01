package de.enflexit.common.p2;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.operations.ProfileChangeOperation;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepository;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * This class handles p2-based update and install operations.
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 *
 */
public class P2OperationsHandler {

	private ProvisioningSession provisioningSession;
	private IProvisioningAgent provisioningAgent;

	private UpdateOperation updateOperation;

	private IMetadataRepositoryManager metadataRepositoryManager;
	private IArtifactRepositoryManager artifactRepositoryManager;

	/**
	 * Gets the provisioning session.
	 *
	 * @return the provisioning session
	 */
	private ProvisioningSession getProvisioningSession() {
		if (provisioningSession == null) {
			provisioningSession = new ProvisioningSession(getProvisioningAgent());
		}
		return provisioningSession;
	}

	/**
	 * Gets the provisioning agent.
	 *
	 * @return the provisioning agent
	 */
	private IProvisioningAgent getProvisioningAgent() {
		if (provisioningAgent == null) {
			BundleContext bundleContext = FrameworkUtil.getBundle(P2OperationsHandler.class).getBundleContext();
			ServiceReference<?> serviceReference = bundleContext.getServiceReference(IProvisioningAgent.SERVICE_NAME);

			if (serviceReference != null) {
				provisioningAgent = (IProvisioningAgent) bundleContext.getService(serviceReference);
			}
		}
		return provisioningAgent;
	}

	/**
	 * Gets a {@link IProgressMonitor} for p2 operations. Currently always returns a new {@link NullProgressMonitor}.
	 * 
	 * @return the progress monitor
	 */
	private IProgressMonitor getProgressMonitor() {
		// TODO Figure out how to handle other progress monitors for graphical visualization
		return new NullProgressMonitor();
	}

	/**
	 * Gets the update operation.
	 *
	 * @return the update operation
	 */
	private UpdateOperation getUpdateOperation() {
		if (updateOperation == null) {
			updateOperation = new UpdateOperation(this.getProvisioningSession());
		}
		return updateOperation;
	}

	/**
	 * Gets the metadata repository manager.
	 *
	 * @return the metadata repository manager
	 */
	private IMetadataRepositoryManager getMetadataRepositoryManager() {
		if (metadataRepositoryManager == null) {
			metadataRepositoryManager = (IMetadataRepositoryManager) this.getProvisioningAgent().getService(IMetadataRepositoryManager.SERVICE_NAME);
		}
		return metadataRepositoryManager;
	}

	/**
	 * Gets the artifact repository manager.
	 *
	 * @return the artifact repository manager
	 */
	private IArtifactRepositoryManager getArtifactRepositoryManager() {
		if (artifactRepositoryManager == null) {
			artifactRepositoryManager = (IArtifactRepositoryManager) this.getProvisioningAgent().getService(IArtifactRepositoryManager.SERVICE_NAME);
		}
		return artifactRepositoryManager;
	}

	/**
	 * Checks if a specific {@link IInstallableUnit} (IU) is installed
	 * 
	 * @param installableUnitID The ID of the IU of interest
	 * @return true if the IU is installed
	 */
	public boolean checkIfInstalled(String installableUnitID) {

		// --- Query the p2 profile for the InstallableUnit of interest -----------
		IProfileRegistry profileRegistry = (IProfileRegistry) this.getProvisioningAgent().getService(IProfileRegistry.SERVICE_NAME);
		IProfile profile = profileRegistry.getProfile(IProfileRegistry.SELF);
		IQuery<IInstallableUnit> query = QueryUtil.createIUQuery(installableUnitID);
		IQueryResult<IInstallableUnit> queryResult = profile.query(query, this.getProgressMonitor());

		return !(queryResult.isEmpty());
	}

	/**
	 * Adds a new p2 repository
	 * 
	 * @param repositoryURI the {@link URI} of the repository to add
	 */
	public void addRepository(URI repositoryURI) {
		this.addRepository(repositoryURI, null);
	}

	/**
	 * Adds a new p2 repository.
	 *
	 * @param repositoryURI the repository URI
	 * @param repositoryName the repository name
	 */
	public void addRepository(URI repositoryURI, String repositoryName) {
		this.getMetadataRepositoryManager().addRepository(repositoryURI);
		this.getArtifactRepositoryManager().addRepository(repositoryURI);
		if (repositoryName != null) {
			this.getMetadataRepositoryManager().setRepositoryProperty(repositoryURI, IRepository.PROP_NICKNAME, repositoryName);
		}
	}

	/**
	 * Installs an {@link IInstallableUnit} from a p2 repository.
	 * 
	 * @param installableUnitID the ID of the IU to be installed
	 * @param repositoryURI the repository ID
	 * @return true if successful
	 */
	public boolean installIU(String installableUnitID, URI repositoryURI) {

		// --- Make sure the repository is known and enabled ---------
		this.addRepository(repositoryURI);

		// --- Query the repository for the IU of interest -----------
		IQueryResult<IInstallableUnit> queryResult = this.queryRepositoryForInstallableUnit(repositoryURI, installableUnitID);

		if (queryResult.isEmpty() == false) {

			// --- If found, trigger an InstallOperation ---------------
			InstallOperation installOperation = new InstallOperation(this.getProvisioningSession(), queryResult.toSet());
			IStatus result = this.performOperation(installOperation);
			return result.isOK();

		} else {

			// --- If not, print an error message -----------------------
			String errorMessage = "Installable unit " + installableUnitID + " could not be found in the repositoty " + repositoryURI;
			System.err.println(errorMessage);
			return false;

		}

	}

	/**
	 * Performs a {@link ProfileChangeOperation}
	 * 
	 * @param operation the operation
	 * @return the result status
	 */
	private IStatus performOperation(ProfileChangeOperation operation) {
		IStatus status = operation.resolveModal(this.getProgressMonitor());
		if (status.isOK()) {
			ProvisioningJob provisioningJob = operation.getProvisioningJob(this.getProgressMonitor());

			if (provisioningJob == null) {
				System.err.println("Trying to install from the Eclipse IDE? This won't work!");
				return Status.CANCEL_STATUS;
			}

			status = provisioningJob.runModal(this.getProgressMonitor());
		}
		return status;
	}

	/**
	 * Checks for updates.
	 * 
	 * @return the result status
	 */
	public IStatus checkForUpdates() {
		return this.getUpdateOperation().resolveModal(this.getProgressMonitor());
	}

	/**
	 * Installs available updates from all repositories.
	 *
	 * @return the result status
	 */
	public IStatus installAvailableUpdates() {
		ProvisioningJob provisioningJob = this.getUpdateOperation().getProvisioningJob(this.getProgressMonitor());
		if (provisioningJob == null) {
			System.err.println("Trying to update from the Eclipse IDE? This won't work!");
			return Status.CANCEL_STATUS;
		}

		return provisioningJob.runModal(this.getProgressMonitor());
	}

	/**
	 * Gets the repository for the {@link IInstallableUnit} (IU) with the given ID.
	 *
	 * @param installableUnitID the ID of the IU of interest
	 * @return the repository, null if no known repository contains the IU
	 */
	public URI getRepositoryForInstallableUnit(String installableUnitID) {

		// --- Get a list of all known repositories ---------------
		IMetadataRepositoryManager metadataRepositoryManager = (IMetadataRepositoryManager) this.getProvisioningAgent().getService(IMetadataRepositoryManager.SERVICE_NAME);
		URI[] knownRepositories = metadataRepositoryManager.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_ALL);

		// --- Check if the repository contains an IU with the requested ID ------
		for (URI repository : knownRepositories) {
			IQueryResult<IInstallableUnit> queryResult = this.queryRepositoryForInstallableUnit(repository, installableUnitID);
			if (queryResult != null && queryResult.isEmpty() == false) {
				return repository;
			}
		}

		// --- No repository containing the IU could be found ------------
		return null;
	}

	/**
	 * Queries a repository for a specific {@link IInstallableUnit} (IU).
	 *
	 * @param repositoryURI the repository URI
	 * @param installableUnitID the ID of the IU
	 * @return the {@link IQueryResult}
	 */
	private IQueryResult<IInstallableUnit> queryRepositoryForInstallableUnit(URI repositoryURI, String installableUnitID) {

		// --- Load the repository ------------
		IQueryResult<IInstallableUnit> queryResult = null;
		try {
			IMetadataRepository metadataRepository = this.getMetadataRepositoryManager().loadRepository(repositoryURI, this.getProgressMonitor());
			// --- Query for the IU of interest -----
			if (metadataRepository != null) {
				queryResult = metadataRepository.query(QueryUtil.createIUQuery(installableUnitID), this.getProgressMonitor());
			}
		} catch (ProvisionException | OperationCanceledException e) {
			System.err.println("Error loading the repository at " + repositoryURI);
			e.printStackTrace();
		}

		return queryResult;
	}

	/**
	 * Gets the repository name.
	 *
	 * @param repositoryURI the repository URI
	 * @return the repository name
	 */
	public String getRepositoryName(URI repositoryURI) {
		return this.getMetadataRepositoryManager().getRepositoryProperty(repositoryURI, IRepository.PROP_NICKNAME);
	}

	public List<IInstallableUnit> getInstalledFeatures() throws Exception {

		List<IInstallableUnit> featuresList = new ArrayList<IInstallableUnit>();

		IProvisioningAgent provisioningAgent = this.getProvisioningAgent();

		String profileId = IProfileRegistry.SELF; // the profile id for the currently running system

		IProfileRegistry profileRegistry = (IProfileRegistry) provisioningAgent.getService(IProfileRegistry.SERVICE_NAME);
		IProfile profile = profileRegistry.getProfile(profileId);
		if (profile == null) {
			throw new Exception("Unable to access p2 profile - This is not possible when starting the application from the IDE!");
		}
		IQuery<IInstallableUnit> query = QueryUtil.createIUGroupQuery();
		IQueryResult<IInstallableUnit> queryResult = profile.query(query, null);

		for (IInstallableUnit feature : queryResult) {
			if (QueryUtil.isProduct(feature) == false) {
				featuresList.add(feature);
			}
		}
		return featuresList;
	}

	// Requires the (deprecated) bundle org.eclipse.update.configurator to work properly
//	public void getInstalledFeaturesOldStyle() {
//		IBundleGroupProvider[] providers = Platform.getBundleGroupProviders();
//		if (providers.length == 0) {
//			System.out.println("No BundleGroupProviders found!");
//		} else {
//
//			for (IBundleGroupProvider provider : providers) {
//
//				System.out.println("BGP: " + provider.getName());
//				IBundleGroup[] groups = provider.getBundleGroups();
//
//				if (groups.length == 0) {
//					System.out.println("- No BundleGroups found!");
//				} else {
//
//					for (IBundleGroup group : groups) {
//
//						System.out.println("-BG: " + group.getName());
//						Bundle[] bundles = group.getBundles();
//
//						if (bundles.length == 0) {
//							System.out.println("  - No Bundles found");
//						} else {
//							for (Bundle bundle : bundles) {
//								System.out.println("--B: " + bundle.getSymbolicName());
//							}
//						}
//
//					}
//				}
//			}
//		}
//	}

}