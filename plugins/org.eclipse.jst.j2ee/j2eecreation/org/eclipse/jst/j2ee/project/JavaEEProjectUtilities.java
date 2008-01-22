/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.j2ee.project;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jst.j2ee.internal.J2EEConstants;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.common.J2EEVersionUtil;
import org.eclipse.jst.j2ee.internal.componentcore.AppClientBinaryComponentHelper;
import org.eclipse.jst.j2ee.internal.componentcore.EJBBinaryComponentHelper;
import org.eclipse.jst.j2ee.internal.componentcore.JCABinaryComponentHelper;
import org.eclipse.jst.j2ee.internal.componentcore.WebBinaryComponentHelper;
import org.eclipse.jst.j2ee.project.facet.IJ2EEFacetConstants;
import org.eclipse.jst.jee.util.internal.JavaEEQuickPeek;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

public class JavaEEProjectUtilities extends ProjectUtilities implements IJ2EEFacetConstants {
	protected static final IVirtualReference[] NO_REFERENCES = new IVirtualReference[0];

	public JavaEEProjectUtilities() {
		// TODO Auto-generated constructor stub
	}

	public static boolean isProjectOfType(IProject project, String typeID) {
		IFacetedProject facetedProject = null;
		try {
			facetedProject = ProjectFacetsManager.create(project);
		} catch (CoreException e) {
			return false;
		}

		if (facetedProject != null && ProjectFacetsManager.isProjectFacetDefined(typeID)) {
			IProjectFacet projectFacet = ProjectFacetsManager.getProjectFacet(typeID);
			return projectFacet != null && facetedProject.hasProjectFacet(projectFacet);
		}
		return false;
	}

	private static boolean isProjectOfType(IFacetedProject facetedProject, String typeID) {

		if (facetedProject != null && ProjectFacetsManager.isProjectFacetDefined(typeID)) {
			IProjectFacet projectFacet = ProjectFacetsManager.getProjectFacet(typeID);
			return projectFacet != null && facetedProject.hasProjectFacet(projectFacet);
		}
		return false;
	}

	private static boolean isEARProject(IFacetedProject project) {
		return isProjectOfType(project, ENTERPRISE_APPLICATION);
	}

	private static boolean isDynamicWebProject(IFacetedProject project) {
		return isProjectOfType(project, DYNAMIC_WEB);
	}

	private static boolean isStaticWebProject(IFacetedProject project) {
		return isProjectOfType(project, STATIC_WEB);
	}

	private static boolean isEJBProject(IFacetedProject project) {
		return isProjectOfType(project, EJB);
	}

	private static boolean isJCAProject(IFacetedProject project) {
		return isProjectOfType(project, JCA);
	}

	private static boolean isApplicationClientProject(IFacetedProject project) {
		return isProjectOfType(project, APPLICATION_CLIENT);
	}

	private static boolean isUtilityProject(IFacetedProject project) {
		return isProjectOfType(project, UTILITY);
	}

	public static boolean isEARProject(IProject project) {
		return isProjectOfType(project, ENTERPRISE_APPLICATION);
	}

	public static boolean isDynamicWebComponent(IVirtualComponent component) {
		if (component.isBinary()) {
			return WebBinaryComponentHelper.handlesComponent(component);
		}
		return isProjectOfType(component.getProject(), DYNAMIC_WEB);
	}

	public static boolean isDynamicWebProject(IProject project) {
		return isProjectOfType(project, DYNAMIC_WEB);
	}

	public static boolean isStaticWebProject(IProject project) {
		return isProjectOfType(project, STATIC_WEB);
	}

	public static boolean isEJBComponent(IVirtualComponent component) {
		if (component.isBinary()) {
			return EJBBinaryComponentHelper.handlesComponent(component);
		}
		return isProjectOfType(component.getProject(), EJB);
	}

	public static boolean isEJBProject(IProject project) {
		return isProjectOfType(project, EJB);
	}

	public static boolean isJCAComponent(IVirtualComponent component) {
		if (component.isBinary()) {
			return JCABinaryComponentHelper.handlesComponent(component);
		}
		return isProjectOfType(component.getProject(), JCA);
	}

	public static boolean isJCAProject(IProject project) {
		return isProjectOfType(project, JCA);
	}

	public static boolean isApplicationClientComponent(IVirtualComponent component) {
		if (component.isBinary()) {
			return AppClientBinaryComponentHelper.handlesComponent(component);
		}
		return isProjectOfType(component.getProject(), APPLICATION_CLIENT);
	}

	public static boolean isApplicationClientProject(IProject project) {
		return isProjectOfType(project, APPLICATION_CLIENT);
	}

	public static boolean isUtilityProject(IProject project) {
		return isProjectOfType(project, UTILITY);
	}

	/**
	 * Return all projects in workspace of the specified type
	 * 
	 * @param type -
	 *            use one of the static strings on this class as a type
	 * @return IProject[]
	 */
	public static IProject[] getAllProjectsInWorkspaceOfType(String type) {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List result = new ArrayList();
		for (int i = 0; i < projects.length; i++) {
			if (isProjectOfType(projects[i], type))
				result.add(projects[i]);
		}
		return (IProject[]) result.toArray(new IProject[result.size()]);
	}

	public static String getJ2EEComponentType(IVirtualComponent component) {
		if (null != component) {
			if (component.isBinary()) {
				if (isApplicationClientComponent(component))
					return APPLICATION_CLIENT;
				else if (isDynamicWebComponent(component))
					return DYNAMIC_WEB;
				else if (isEJBComponent(component))
					return EJB;
				else if (isJCAComponent(component))
					return JCA;
				else
					return UTILITY;
			}
			return getJ2EEProjectType(component.getProject());
		}
		return ""; //$NON-NLS-1$
	}

	public static String getJ2EEProjectType(IProject project) {
		if (null != project && project.isAccessible()) {
			IFacetedProject facetedProject = null;
			try {
				facetedProject = ProjectFacetsManager.create(project);
			} catch (CoreException e) {
				return ""; //$NON-NLS-1$
			}
			if (isApplicationClientProject(facetedProject))
				return APPLICATION_CLIENT;
			else if (isDynamicWebProject(facetedProject))
				return DYNAMIC_WEB;
			else if (isEJBProject(facetedProject))
				return EJB;
			else if (isEARProject(facetedProject))
				return ENTERPRISE_APPLICATION;
			else if (isJCAProject(facetedProject))
				return JCA;
			else if (isStaticWebProject(facetedProject))
				return STATIC_WEB;
			else if (isUtilityProject(facetedProject))
				return UTILITY;
		}
		return ""; //$NON-NLS-1$
	}
	/**
	 * Returns the J2EE Module version based on the DD XML file
	 * @param project
	 * @return version String
	 */
	public static String getJ2EEDDProjectVersion(IProject project) {
		int type = J2EEVersionConstants.UNKNOWN;
		String ddURI = null;
		if (isEARProject(project)) {
			type = J2EEVersionConstants.APPLICATION_TYPE;
			ddURI = J2EEConstants.APPLICATION_DD_URI;
		} else if (isEJBProject(project)) {
			type = J2EEVersionConstants.EJB_TYPE;
			ddURI = J2EEConstants.EJBJAR_DD_URI;
		} else if (isApplicationClientProject(project)) {
			type = J2EEVersionConstants.APPLICATION_CLIENT_TYPE;
			ddURI = J2EEConstants.APP_CLIENT_DD_URI;
		} else if (isJCAProject(project)) {
			type = J2EEVersionConstants.CONNECTOR_TYPE;
			ddURI = J2EEConstants.RAR_DD_URI;
		} else if (isDynamicWebProject(project)) {
			type = J2EEVersionConstants.WEB_TYPE;
			ddURI = J2EEConstants.WEBAPP_DD_URI;
		} 

		if(type != J2EEVersionConstants.UNKNOWN){
			IVirtualComponent comp = ComponentCore.createComponent(project);
			if (comp != null) {
				IVirtualFile vFile = comp.getRootFolder().getFile(new Path(ddURI));
				if(vFile.exists()){
					InputStream in= null;
					try{
						in = vFile.getUnderlyingFile().getContents();
						JavaEEQuickPeek quickPeek = new JavaEEQuickPeek(in);
						int vers = (quickPeek.getVersion() == J2EEVersionConstants.UNKNOWN) ? getJEEVersion(project) : quickPeek.getVersion();
						return J2EEVersionUtil.convertVersionIntToString(vers);
					} catch (CoreException e) {
						Logger.getLogger().logError(e);
					} finally {
						if(in != null){
							try {
								in.close();
							} catch (IOException e) {
								Logger.getLogger().logError(e);
							}
						}
					}
					
				}
				else
					return J2EEVersionUtil.convertVersionIntToString(getJEEVersion(project));
			}
		}
		
		return null;
	}


	private static int getJEEVersion(IProject project) {
		if (isEARProject(project) || isApplicationClientProject(project))
			return J2EEVersionConstants.VERSION_5_0;
		if (isEJBProject(project))
			return J2EEVersionConstants.VERSION_3_0;
		if (isDynamicWebProject(project))
			return J2EEVersionConstants.VERSION_2_5;
		return J2EEVersionConstants.UNKNOWN;
			
	}

	public static int getJ2EEVersion(IProject javaEEProject)
	{
		int retVal = 0;
		return retVal;
	}
}