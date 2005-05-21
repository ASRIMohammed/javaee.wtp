/***************************************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.jst.j2ee.internal.project;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaRefFactory;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jem.util.emf.workbench.WorkbenchByteArrayOutputStream;
import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jem.util.plugin.JEMUtilPlugin;
import org.eclipse.jem.workbench.utility.JemProjectUtilities;
import org.eclipse.jst.j2ee.applicationclient.internal.creation.IApplicationClientNatureConstants;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EARFile;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EJBJarFile;
import org.eclipse.jst.j2ee.commonarchivecore.internal.File;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.OpenFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.ArchiveConstants;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.ArchiveManifest;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.ArchiveManifestImpl;
import org.eclipse.jst.j2ee.commonarchivecore.internal.impl.CommonarchiveFactoryImpl;
import org.eclipse.jst.j2ee.commonarchivecore.internal.util.ArchiveUtil;
import org.eclipse.jst.j2ee.ejb.EJBJar;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.jst.j2ee.internal.J2EEConstants;
import org.eclipse.jst.j2ee.internal.archive.operations.JavaProjectLoadStrategyImpl;
import org.eclipse.jst.j2ee.internal.earcreation.EAREditModel;
import org.eclipse.jst.j2ee.internal.earcreation.EARNatureRuntime;
import org.eclipse.jst.j2ee.internal.earcreation.modulemap.ModuleMapping;
import org.eclipse.jst.j2ee.internal.earcreation.modulemap.UtilityJARMapping;
import org.eclipse.jst.j2ee.internal.moduleextension.EarModuleManager;

public class J2EEProjectUtilities extends ProjectUtilities {

	/**
	 * @deprecated use {@link ProjectUtilities#isBinaryProject(IProject)}
	 */
	public static boolean isBinaryProject(String jarUri, IProject aProject) {
		return JemProjectUtilities.isBinaryProject(aProject);
	}

	/**
	 * Return the absolute path of a loose archive in a J2EE application or WAR file
	 */
	public static IPath getRuntimeLocation(IProject aProject) {
		if (JemProjectUtilities.isBinaryProject(aProject))
			return getBinaryProjectJARLocation(aProject);
		return JemProjectUtilities.getJavaProjectOutputAbsoluteLocation(aProject);
	}

	public static IPath getBinaryProjectJARLocation(IProject aProject) {
		List sources = JemProjectUtilities.getLocalJARPathsFromClasspath(aProject);
		if (!sources.isEmpty()) {
			IPath path = (IPath) sources.get(0);
			return aProject.getFile(path).getLocation();
		}
		return null;
	}

	public static Archive getClientJAR(EJBJarFile file, EARFile earFile) {
		EJBJar jar = file.getDeploymentDescriptor();
		if (jar == null)
			return null;
		String clientJAR = jar.getEjbClientJar();
		if (clientJAR == null || clientJAR.length() == 0)
			return null;
		String normalized = ArchiveUtil.deriveEARRelativeURI(clientJAR, file.getURI());
		if (normalized != null) {
			try {
				File aFile = earFile.getFile(normalized);
				if (aFile.isArchive() && !aFile.isModuleFile())
					return (Archive) aFile;
			} catch (FileNotFoundException nothingThere) {
			}
		}
		return null;
		//TODO - release the DD here to free up space
	}

	/**
	 * @param uri
	 * @param projectName
	 * @param exportSource
	 * @param includeProjectMetaFiles
	 * @return
	 */
	public static Archive asArchive(String jarUri, IProject aProject, boolean exportSource, boolean includeProjectMetaFiles) throws OpenFailureException {
		if (JemProjectUtilities.isBinaryProject(aProject))
			return asArchiveFromBinary(jarUri, aProject);
		return asArchiveFromSource(jarUri, aProject, exportSource, includeProjectMetaFiles);
	}

	public static Archive asArchive(String jarUri, IProject aProject, boolean exportSource) throws OpenFailureException {
		return asArchive(jarUri, aProject, exportSource, false);
	}

	public static Archive asArchiveFromSource(String jarUri, IProject aProject, boolean exportSource, boolean includeProjectMetaFiles) throws OpenFailureException {
		JavaProjectLoadStrategyImpl strat = new JavaProjectLoadStrategyImpl(aProject);
		strat.setExportSource(exportSource);
		strat.setIncludeProjectMetaFiles(includeProjectMetaFiles);
		return CommonarchiveFactoryImpl.getActiveFactory().primOpenArchive(strat, jarUri);
	}

	/**
	 * Append one IClasspathEntry to the build path of the passed project. If a classpath entry
	 * having the same path as the parameter already exists, then does nothing.
	 */
	public static void appendJavaClassPath(IProject p, IClasspathEntry newEntry) throws JavaModelException {
		IJavaProject javaProject = JemProjectUtilities.getJavaProject(p);
		if (javaProject == null)
			return;
		IClasspathEntry[] classpath = javaProject.getRawClasspath();
		List newPathList = new ArrayList(classpath.length);
		for (int i = 0; i < classpath.length; i++) {
			IClasspathEntry entry = classpath[i];
			// fix dup class path entry for .JETEmitter project
			// Skip the entry to be added if it already exists
			if (Platform.getOS().equals(Platform.OS_WIN32)) {
				if (!entry.getPath().toString().equalsIgnoreCase(newEntry.getPath().toString()))
					newPathList.add(entry);
				else
					return;
			} else {
				if (!entry.getPath().equals(newEntry.getPath()))
					newPathList.add(entry);
				else
					return;
			}
		}
		newPathList.add(newEntry);
		IClasspathEntry[] newClasspath = (IClasspathEntry[]) newPathList.toArray(new IClasspathEntry[newPathList.size()]);
		javaProject.setRawClasspath(newClasspath, new NullProgressMonitor());
	}


	public static Archive asArchiveFromSource(String jarUri, IProject aProject, boolean exportSource) throws OpenFailureException {
		return asArchiveFromSource(jarUri, aProject, exportSource, false);
	}

	public static Archive asArchiveFromBinary(String jarUri, IProject aProject) throws OpenFailureException {
		IPath path = getBinaryProjectJARLocation(aProject);
		if (path != null) {
			String location = path.toOSString();
			Archive anArchive = CommonarchiveFactoryImpl.getActiveFactory().primOpenArchive(location);
			anArchive.setURI(jarUri);
			return anArchive;
		}
		return null;
	}

	public static Archive asArchive(String jarUri, String projectName, boolean exportSource, boolean includeProjectMetaFiles) throws OpenFailureException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (!project.exists())
			return null;

		return asArchive(jarUri, project, exportSource, includeProjectMetaFiles);
	}

	public static Archive asArchive(String jarUri, String projectName, boolean exportSource) throws OpenFailureException {
		return asArchive(jarUri, projectName, exportSource, false);
	}

	public static EARNatureRuntime[] getReferencingEARProjects(IProject aProject) {

		List earProjects = EARNatureRuntime.getAllEARProjectsInWorkbench();
		List result = new ArrayList();
		EAREditModel editModel = null;
		for (int i = 0; i < earProjects.size(); i++) {
			IProject earProject = (IProject) earProjects.get(i);
			EARNatureRuntime earNature = EARNatureRuntime.getRuntime(earProject);
			Object accessorKey = new Object();
			try {

				editModel = earNature.getEarEditModelForRead(accessorKey);
				UtilityJARMapping map = editModel.getUtilityJARMapping(aProject);
				if (map != null)
					result.add(earNature);
				else {
					ModuleMapping modMap = editModel.getModuleMapping(aProject);
					if (modMap != null)
						result.add(earNature);
				}
			} finally {
				if (editModel != null)
					editModel.releaseAccess(accessorKey);
			}
		}
		return (EARNatureRuntime[]) result.toArray(new EARNatureRuntime[result.size()]);
	}

	public static EARNatureRuntime getFirstReferencingEARProject(IProject aProject) {
		EARNatureRuntime[] natures = getReferencingEARProjects(aProject);
		if (natures.length != 0)
			return natures[0];
		return null;
	}

	/**
	 * If the project is referenced by the EAR, return the URI of the JAR or module
	 * 
	 * @deprecated use {@link EARNatureRuntime#getJARUri(IProject)}
	 */
	public static String getJARUri(EARNatureRuntime runtime, IProject project) {
		return runtime.getJARUri(project);
	}

	public static ArchiveManifest readManifest(IFile aFile) {

		InputStream in = null;
		try {

			if (aFile == null || !aFile.exists())
				return null;
			in = aFile.getContents();
			return new ArchiveManifestImpl(in);
		} catch (Exception ex) {
			org.eclipse.jem.util.logger.proxy.Logger.getLogger().logError(ex);
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException weTried) {
				}
			}
		}
	}

	
	public static ArchiveManifest readManifest(IProject p) {

		InputStream in = null;
		try {
			IFile aFile = getManifestFile(p);
			if (aFile == null || !aFile.exists())
				return null;
			in = aFile.getContents();
			return new ArchiveManifestImpl(in);
		} catch (Exception ex) {
			org.eclipse.jem.util.logger.proxy.Logger.getLogger().logError(ex);
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException weTried) {
				}
			}
		}
	}

	public static IFile getManifestFile(IProject p) {
		IContainer source = null;
		J2EENature nature = J2EENature.getRegisteredRuntime(p);
		if (nature != null)
			source = nature.getEMFRoot();
		else
			source = getSourceFolderOrFirst(p, null);
		if (source != null)
			return source.getFile(new Path(ArchiveConstants.MANIFEST_URI));
		return null;
	}

	public static void writeManifest(IProject p, ArchiveManifest manifest) throws java.io.IOException {

		IFile aFile = getManifestFile(p);
		if (aFile != null) {
			OutputStream out = new WorkbenchByteArrayOutputStream(aFile);
			manifest.writeSplittingClasspath(out);
			out.close();
		}
	}
	
	public static void writeManifest(IFile aFile, ArchiveManifest manifest) throws java.io.IOException {
		if (aFile != null) {
			OutputStream out = new WorkbenchByteArrayOutputStream(aFile);
			manifest.writeSplittingClasspath(out);
			out.close();
		}
	}
	

	public static String getUtilityJARUriInFirstEAR(IProject project) {
		EARNatureRuntime[] earNatures = getReferencingEARProjects(project);
		String uri;
		for (int i = 0; i < earNatures.length; i++) {
			uri = getJARUri(earNatures[i], project);
			if (uri != null && uri.length() > 0)
				return uri;
		}
		return null;
	}

	/**
	 * Keys are the EJB JAR files and the values are the respective client JARs; includes only key
	 * value pairs for which EJB Client JARs are defined and exist.
	 * 
	 * @author schacher
	 */
	public static Map collectEJBClientJARs(EARFile earFile) {
		if (earFile == null)
			return Collections.EMPTY_MAP;
		Map ejbClientJARs = null;
		List ejbJARFiles = earFile.getEJBJarFiles();
		Archive clientJAR = null;
		for (int i = 0; i < ejbJARFiles.size(); i++) {
			EJBJarFile ejbJarFile = (EJBJarFile) ejbJARFiles.get(i);
			clientJAR = getClientJAR(ejbJarFile, earFile);
			if (clientJAR != null) {
				if (ejbClientJARs == null)
					ejbClientJARs = new HashMap();
				ejbClientJARs.put(ejbJarFile, clientJAR);
			}
		}
		return ejbClientJARs == null ? Collections.EMPTY_MAP : ejbClientJARs;
	}

	public static void makeJ2EENatureFirst(IProject proj) {
		String natureID = J2EENature.getRegisteredRuntimeID(proj);
		if (natureID != null) {
			try {
				IProjectDescription description = proj.getDescription();
				String[] prevNatures = description.getNatureIds();
				String[] newNatures = new String[prevNatures.length];
				newNatures[0] = natureID;
				int next = 1;
				for (int i = 0; i < prevNatures.length; i++) {
					if (!prevNatures[i].equals(natureID))
						newNatures[next++] = prevNatures[i];
				}
				description.setNatureIds(newNatures);
				proj.setDescription(description, null);
			} catch (CoreException e) {
				Logger.getLogger().logError(e);
			}
		}
	}

	/**
	 * If the project is referenced by the EAR, return the URI of the JAR or module
	 */
	public static boolean hasProjectMapping(EARNatureRuntime runtime, IProject project) {
		if (runtime != null) {
			EAREditModel model = null;
			Object key = new Object();
			try {
				model = runtime.getEarEditModelForRead(key);
				ModuleMapping moduleMap = model.getModuleMapping(project);
				if (moduleMap != null) {
					return true;
				}
				UtilityJARMapping jarMap = model.getUtilityJARMapping(project);
				if (jarMap != null)
					return true;

			} finally {
				if (model != null)
					model.releaseAccess(key);
			}
		}
		return false;
	}

	public static String computeRelativeText(String referencingURI, String referencedURI, EnterpriseBean bean) {
		if (bean == null)
			return null;

		String beanName = bean.getName();
		if (beanName == null)
			return null;

		String relativeUri = computeRelativeText(referencingURI, referencedURI);
		if (relativeUri == null)
			return beanName;
		return relativeUri + "#" + beanName; //$NON-NLS-1$
	}

	public static String computeRelativeText(String referencingURI, String referencedURI) {
		if (referencingURI == null || referencedURI == null)
			return null;
		IPath pPre = new Path(referencingURI);
		IPath pDep = new Path(referencedURI);
		if (pPre.getDevice() != null || pDep.getDevice() != null)
			return null;
		pPre = pPre.makeRelative();
		pDep = pDep.makeRelative(); //referenced Archive path URI

		while (pPre.segmentCount() > 1 && pDep.segmentCount() > 1 && pPre.segment(0).equals(pDep.segment(0))) {
			pPre = pPre.removeFirstSegments(1);
			pDep = pDep.removeFirstSegments(1);
		}

		IPath result = null; //$NON-NLS-1$
		StringBuffer buf = new StringBuffer();
		String segment = null;
		do {
			segment = pDep.lastSegment();
			pPre = pPre.removeLastSegments(1);
			pDep = pDep.removeLastSegments(1);
			if (segment != null) {
				if (result == null)
					result = new Path(segment);
				else
					result = new Path(segment).append(result);
			}
			if (!pPre.equals(pDep) && !pPre.isEmpty())
				buf.append("../"); //$NON-NLS-1$
		} while (!pPre.equals(pDep));

		if (result != null)
			buf.append(result.makeRelative().toString());

		return buf.toString();
	}

	public static IProject getEJBProjectFromEJBClientProject(IProject ejbClientProject) {
		try {
			if (null != ejbClientProject && ejbClientProject.hasNature(JavaCore.NATURE_ID)) {
				IProject[] allProjects = getAllProjects();
				for (int i = 0; i < allProjects.length; i++) {
					if (null != EarModuleManager.getEJBModuleExtension().getEJBJar(allProjects[i])) {
						if (ejbClientProject == EarModuleManager.getEJBModuleExtension().getDefinedEJBClientJARProject(allProjects[i])) {
							return allProjects[i];
						}
					}
				}
			}
		} catch (CoreException e) {
		}
		return null;
	}


	public static EnterpriseBean getEnterpriseBean(ICompilationUnit cu) {
		IProject proj = cu.getJavaProject().getProject();
		EJBJar jar = EarModuleManager.getEJBModuleExtension().getEJBJar(proj);
		if (null == jar) {
			jar = EarModuleManager.getEJBModuleExtension().getEJBJar(getEJBProjectFromEJBClientProject(proj));
		}
		if (jar != null) {
			int index = cu.getElementName().indexOf('.');
			String className = cu.getElementName();
			if (index > 0)
				className = className.substring(0, index);
			JavaClass javaClass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType(cu.getParent().getElementName(), className, jar.eResource().getResourceSet());
			return jar.getEnterpriseBeanWithReference(javaClass);
		}
		return null;
	}

	public static IContainer getSourceFolderOrFirst(IProject p, String defaultSourceName) {
		try {
			IPath sourcePath = getSourcePathOrFirst(p, defaultSourceName);
			if (sourcePath == null)
				return null;
			else if (sourcePath.isEmpty())
				return p;
			else
				return p.getFolder(sourcePath);
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	public static IPath getSourcePathOrFirst(IProject p, String defaultSourceName) {
		IJavaProject javaProj = JemProjectUtilities.getJavaProject(p);
		if (javaProj == null)
			return null;
		IClasspathEntry[] cp = null;
		try {
			cp = javaProj.getRawClasspath();
		} catch (JavaModelException ex) {
			JEMUtilPlugin.getLogger().logError(ex);
			return null;
		}
		IClasspathEntry firstSource = null;
		IPath defaultSourcePath = null;
		if (defaultSourceName != null)
			defaultSourcePath = createPath(p, defaultSourceName);
		boolean found = false;
		for (int i = 0; i < cp.length; i++) {
			if (cp[i].getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				// check if it contains /META-INF/MANIFEST.MF
				IPath sourceFolderPath = cp[i].getPath().removeFirstSegments(1);
				IFolder sourceFolder = p.getFolder(sourceFolderPath);
				if (isSourceFolderAnInputContainer(sourceFolder)) {
					found = true;
					if (firstSource == null) {
						firstSource = cp[i];
						if (defaultSourcePath == null)
							break;
					}
					if (cp[i].getPath().equals(defaultSourcePath))
						return defaultSourcePath.removeFirstSegments(1);
				}
			}
		}
		if (!found) {
			for (int i = 0; i < cp.length; i++) {
				if (cp[i].getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					if (firstSource == null) {
						firstSource = cp[i];
						if (defaultSourcePath == null)
							break;
					}
					if (cp[i].getPath().equals(defaultSourcePath))
						return defaultSourcePath.removeFirstSegments(1);
				}
			}
		}
		if (firstSource == null)
			return null;
		if (firstSource.getPath().segment(0).equals(p.getName()))
			return firstSource.getPath().removeFirstSegments(1);
		return null;
	}

	public static boolean isSourceFolderAnInputContainer(IFolder sourceFolder) {
		IContainer parent = sourceFolder;
		while (true) {
			parent = parent.getParent();
			if (parent == null)
				return false;
			if (parent instanceof IProject)
				break;
		}
		IProject project = (IProject) parent;
		try {
			if(!project.isAccessible())
				return false;
			
			IProjectDescription desc = project.getDescription();
			if (desc.hasNature(IEJBNatureConstants.NATURE_ID)) {
				return sourceFolder.findMember(J2EEConstants.EJBJAR_DD_URI) != null;
			} else if (desc.hasNature(IApplicationClientNatureConstants.NATURE_ID)) {
				return sourceFolder.findMember(J2EEConstants.APP_CLIENT_DD_URI) != null;
			} else if (desc.hasNature(IWebNatureConstants.J2EE_NATURE_ID)) {
				return sourceFolder.findMember(J2EEConstants.WEBAPP_DD_URI) != null;
			} else if (desc.hasNature(IConnectorNatureConstants.NATURE_ID)) {
				return sourceFolder.findMember(J2EEConstants.RAR_DD_URI) != null;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return false;
	}

}