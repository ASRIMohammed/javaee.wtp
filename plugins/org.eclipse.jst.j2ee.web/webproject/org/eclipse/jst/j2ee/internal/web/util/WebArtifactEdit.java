/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.j2ee.internal.web.util;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jst.j2ee.common.XMLResource;
import org.eclipse.jst.j2ee.internal.J2EEConstants;
import org.eclipse.jst.j2ee.internal.modulecore.util.EnterpriseArtifactEdit;
import org.eclipse.jst.j2ee.webapplication.WebApp;
import org.eclipse.jst.j2ee.webapplication.WebAppResource;
import org.eclipse.jst.j2ee.webapplication.WebapplicationFactory;
import org.eclipse.jst.j2ee.webapplication.WelcomeFile;
import org.eclipse.jst.j2ee.webapplication.WelcomeFileList;
import org.eclipse.wst.common.modulecore.ArtifactEditModel;
import org.eclipse.wst.common.modulecore.ModuleCore;
import org.eclipse.wst.common.modulecore.ModuleCoreNature;
import org.eclipse.wst.common.modulecore.ModuleType;
import org.eclipse.wst.common.modulecore.UnresolveableURIException;
import org.eclipse.wst.common.modulecore.WorkbenchModule;
import org.eclipse.wst.common.modulecore.internal.util.IModuleConstants;

/**
 * <p>
 * WebArtifactEdit utilizes the facade function of ArtifactEdit {@see ArtifactEdit}&nbsp;to obtain Web
 * specifec data from a WebAppResource (@see WebAppResource). The WebAppResource is retrieved from
 * the ArtifactEditModel {@see ArtifactEditModel}&nbsp;using a cached constant (@see
 * J2EEConstants.WEBAPP_DD_URI_OBJ). Defined methods extract data from the resource.
 * </p>
 */
public class WebArtifactEdit extends EnterpriseArtifactEdit {

	/**
	 * <p>
	 * Identifier used to link WebArtifactEdit to a WebEditAdapterFactory (@see
	 * WebEditAdapterFactory) stored in an AdapterManger (@see AdapterManager)
	 * </p>
	 */

	public static final Class ADAPTER_TYPE = WebArtifactEdit.class;

	/**
	 * <p>
	 * Identifier used to group and query common artifact edits.
	 * </p>
	 */

	public static String TYPE_ID = "jst.web"; //$NON-NLS-1$

	/**
	 * <p>
	 * Creates an instance facade for the given {@see ArtifactEditModel}.
	 * </p>
	 * 
	 * @param anArtifactEditModel
	 */

	public WebArtifactEdit(ArtifactEditModel model) {
		super(model);

	}

	/**
	 * <p>
	 * Creates an instance facade for the given {@see ArtifactEditModel}
	 * </p>
	 * 
	 * @param aNature
	 *            A non-null {@see ModuleCoreNature}for an accessible project
	 * @param aModule
	 *            A non-null {@see WorkbenchModule}pointing to a module from the given
	 *            {@see ModuleCoreNature}
	 */


	public WebArtifactEdit(ModuleCoreNature aNature, WorkbenchModule aModule, boolean toAccessAsReadOnly) {
		super(aNature, aModule, toAccessAsReadOnly);
	}

	/**
	 * <p>
	 * Retrieves J2EE version information from WebAppResource.
	 * </p>
	 * 
	 * @return an integer representation of a J2EE Spec version
	 *  
	 */
	public int getJ2EEVersion() {
		return getWebApplicationXmiResource().getJ2EEVersionID();
	}

	/**
	 * <p>
	 * Obtains the WebApp (@see WebApp) root object from the WebAppResource. If the root object does
	 * not exist, then one is created (@link addWebAppIfNecessary(getWebApplicationXmiResource())).
	 * The root object contains all other resource defined objects.
	 * </p>
	 * 
	 * @return EObject
	 *  
	 */
	public EObject getDeploymentDescriptorRoot() {
		List contents = getDeploymentDescriptorResource().getContents();
		if (contents.size() > 0)
			return (EObject) contents.get(0);
		addWebAppIfNecessary(getWebApplicationXmiResource());
		return (EObject) contents.get(0);
	}

	/**
	 * 
	 * @return WebAppResource from (@link getDeploymentDescriptorResource())
	 *  
	 */

	public WebAppResource getWebApplicationXmiResource() {
		return (WebAppResource) getDeploymentDescriptorResource();
	}

	/**
	 * <p>
	 * Retrieves the underlying resource from the ArtifactEditModel using defined URI.
	 * </p>
	 * 
	 * @return Resource
	 *  
	 */

	public Resource getDeploymentDescriptorResource() {
		return getArtifactEditModel().getResource(J2EEConstants.WEBAPP_DD_URI_OBJ);
	}

	/**
	 * 
	 * @return WebApp from (@link getDeploymentDescriptorRoot())
	 *  
	 */
	public WebApp getWebApplication() {
		return (WebApp) getDeploymentDescriptorRoot();
	}

	/**
	 * <p>
	 * Retrieves Servlet version information derived from the WebAppResource.
	 * </p>
	 * 
	 * @return an integer representation of a module version
	 *  
	 */


	public int getServletVersion() {
		return getWebApplicationXmiResource().getModuleVersionID();
	}


	/**
	 * <p>
	 * Creates a deployment descriptor root object (WebApp) and populates with data. Adds the root
	 * object to the deployment descriptor resource.
	 * </p>
	 * 
	 * <p>
	 * 
	 * @param aModule
	 *            A non-null pointing to a XMLResource (@see XMLResource)
	 * 
	 * Note: This method is typically used for JUNIT - move?
	 * </p>
	 */

	protected void addWebAppIfNecessary(XMLResource aResource) {

		if (aResource != null && aResource.getContents().isEmpty()) {
			WebApp webApp = WebapplicationFactory.eINSTANCE.createWebApp();
			aResource.getContents().add(webApp);
			URI moduleURI = getArtifactEditModel().getModuleURI();
			try {
				webApp.setDisplayName(ModuleCore.getDeployedName(moduleURI));
			} catch (UnresolveableURIException e) {
			}
			aResource.setID(webApp, J2EEConstants.WEBAPP_ID);

			WelcomeFileList wList = WebapplicationFactory.eINSTANCE.createWelcomeFileList();
			webApp.setFileList(wList);
			List files = wList.getFile();
			WelcomeFile file = WebapplicationFactory.eINSTANCE.createWelcomeFile();
			file.setWelcomeFile("index.html"); //$NON-NLS-1$
			files.add(file);
			file = WebapplicationFactory.eINSTANCE.createWelcomeFile();
			file.setWelcomeFile("index.htm"); //$NON-NLS-1$
			files.add(file);
			file = WebapplicationFactory.eINSTANCE.createWelcomeFile();
			file.setWelcomeFile("index.jsp"); //$NON-NLS-1$
			files.add(file);
			file = WebapplicationFactory.eINSTANCE.createWelcomeFile();
			file.setWelcomeFile("default.html"); //$NON-NLS-1$
			files.add(file);
			file = WebapplicationFactory.eINSTANCE.createWelcomeFile();
			file.setWelcomeFile("default.htm"); //$NON-NLS-1$
			files.add(file);
			file = WebapplicationFactory.eINSTANCE.createWelcomeFile();
			file.setWelcomeFile("default.jsp"); //$NON-NLS-1$
			files.add(file);
		}
	}

	/**
	 * <p>
	 * Returns an instance facade to manage the underlying edit model for the given
	 * {@see WorkbenchModule}. Instances of WebArtifactEdit that are returned through this method
	 * must be {@see #dispose()}ed of when no longer in use.
	 * </p>
	 * <p>
	 * Use to acquire an WebArtifactEdit facade for a specific {@see WorkbenchModule}that will not
	 * be used for editing. Invocations of any save*() API on an instance returned from this method
	 * will throw exceptions.
	 * </p>
	 * <p>
	 * <b>The following method may return null. </b>
	 * </p>
	 * 
	 * @param aModule
	 *            A valid {@see WorkbenchModule}with a handle that resolves to an accessible
	 *            project in the workspace
	 * @return An instance of WebArtifactEdit that may only be used to read the underlying content
	 *         model
	 * @throws UnresolveableURIException
	 *             could not resolve uri.
	 */


	public static WebArtifactEdit getWebEditForRead(WorkbenchModule aModule) {
		try {
			ModuleType moduleType = aModule.getModuleType();
			if (moduleType == null)
				return null;
			if (IModuleConstants.JST_WEB_MODULE.equals(moduleType.getModuleTypeId())) {
				IProject project = ModuleCore.getContainingProject(aModule.getHandle());
				if (project.isAccessible()) {
					ModuleCoreNature nature = ModuleCoreNature.getModuleCoreNature(project);
					return new WebArtifactEdit(nature, aModule, true);
				}
			}
		} catch (UnresolveableURIException uue) {
		}
		return null;
	}
}
