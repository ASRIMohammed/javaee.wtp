/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.j2ee.componentcore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jst.j2ee.classpathdep.ClasspathDependencyUtil;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.ArchiveManifest;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.ArchiveManifestImpl;
import org.eclipse.jst.j2ee.commonarchivecore.internal.util.ArchiveUtil;
import org.eclipse.jst.j2ee.internal.J2EEConstants;
import org.eclipse.jst.j2ee.internal.classpathdep.ClasspathDependencyVirtualComponent;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualArchiveComponent;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualComponent;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualFolder;
import org.eclipse.wst.common.componentcore.internal.util.IComponentImplFactory;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;

public class J2EEModuleVirtualComponent extends VirtualComponent implements IComponentImplFactory {

	public J2EEModuleVirtualComponent() {
		super();
	}

	public J2EEModuleVirtualComponent(IProject aProject, IPath aRuntimePath) {
		super(aProject, aRuntimePath);
	}

	public IVirtualComponent createComponent(IProject aProject) {
		return new J2EEModuleVirtualComponent(aProject, new Path("/")); //$NON-NLS-1$
	}

	public IVirtualComponent createArchiveComponent(IProject aProject, String archiveLocation, IPath aRuntimePath) {
		return new J2EEModuleVirtualArchiveComponent(aProject, archiveLocation, aRuntimePath);
	}
	
	public IVirtualFolder createFolder(IProject aProject, IPath aRuntimePath) {
		return new VirtualFolder(aProject, aRuntimePath);
	}
	
	/**
	 * Retrieves all references except those computed dynamically from
	 * tagged Java classpath entries.
	 * @return IVirtualReferences for all non-Java classpath entry references.
	 */
	public IVirtualReference[] getNonJavaReferences() {
		return getReferences(false);
	}
	
	public IVirtualReference[] getReferences() {
		return getReferences(true);
	}
	
	private IVirtualReference[] getReferences(final boolean getJavaRefs) {
		IVirtualReference[] hardReferences = getNonManifestReferences(getJavaRefs);
		
		// retrieve the dynamic references specified via the MANIFEST.MF classpath 
		List dynamicReferences = J2EEModuleVirtualComponent.getManifestReferences(this, hardReferences);
		
		IVirtualReference[] references = null;
		if (dynamicReferences == null) {
			references = hardReferences;
		} else {
			references = new IVirtualReference[hardReferences.length + dynamicReferences.size()];
			System.arraycopy(hardReferences, 0, references, 0, hardReferences.length);
			for (int i = 0; i < dynamicReferences.size(); i++) {
				references[hardReferences.length + i] = (IVirtualReference) dynamicReferences.get(i);
			}
		}
		return references;
	}
	
	public IVirtualReference[] getNonManifestReferences() {
		return getNonManifestReferences(true);
	}
	
	public IVirtualReference[] getNonManifestReferences(final boolean getJavaRefs) {
		final List allRefs = new ArrayList();
		
		// add component file references
		IVirtualReference[] hardReferences = super.getReferences();
		for (int i = 0; i < hardReferences.length; i++) {
			allRefs.add(hardReferences[i]);
		}

		// add the dynamic references specified via specially tagged JDT classpath entries
		if (getJavaRefs) {
			IVirtualReference[] cpRefs = getJavaClasspathReferences(hardReferences); 
			for (int i = 0; i < cpRefs.length; i++) {
				allRefs.add(cpRefs[i]);
			}
		}
		
		return (IVirtualReference[]) allRefs.toArray(new IVirtualReference[allRefs.size()]);
	}

	public static String [] getManifestClasspath(IVirtualComponent moduleComponent) {
		String[] manifestClasspath = null;
		if(!moduleComponent.isBinary()){
			IVirtualFile vManifest = moduleComponent.getRootFolder().getFile(J2EEConstants.MANIFEST_URI);
			if (vManifest.exists()) {
				IFile manifestFile = vManifest.getUnderlyingFile();
				InputStream in = null;
				try {
					in = manifestFile.getContents();
					ArchiveManifest manifest = new ArchiveManifestImpl(in);
					manifestClasspath = manifest.getClassPathTokenized();
				} catch (IOException e) {
					Logger.getLogger().logError(e);
				} catch (CoreException e) {
					Logger.getLogger().logError(e);
				} finally {
					if (in != null) {
						try {
							in.close();
							in = null;
						} catch (IOException e) {
							Logger.getLogger().logError(e);
						}
					}
				}
			}
		} else {
			manifestClasspath = ((J2EEModuleVirtualArchiveComponent)moduleComponent).getManifestClasspath();
		}
		
		return manifestClasspath;
			
	}
	
	public IVirtualReference[] getJavaClasspathReferences() {
		return getJavaClasspathReferences(null);
	}
	
	private IVirtualReference[] getJavaClasspathReferences(IVirtualReference[] hardReferences) {
		final IProject project = getProject();
		final List cpRefs = new ArrayList();
		final boolean isWebApp = J2EEProjectUtilities.isDynamicWebComponent(this);
		
		try {
			if (project == null || !project.isAccessible() || !project.hasNature(JavaCore.NATURE_ID)) { 
				return new IVirtualReference[0];
			}

			final IJavaProject javaProject = JavaCore.create(project);
			if (javaProject == null) {
				return new IVirtualReference[0];
			}

			// retrieve all referenced classpath entries
			final Map referencedEntries = ClasspathDependencyUtil.getComponentClasspathDependencies(javaProject, isWebApp);

			if (referencedEntries.isEmpty()) {
				return new IVirtualReference[0];
			}

			if (hardReferences == null) {
				// only compute this not set and if we have some cp dependencies 
				hardReferences = super.getReferences();
			}
			final IPath[] hardRefPaths = new IPath[hardReferences.length];
			for (int j = 0; j < hardReferences.length; j++) {
				final IVirtualComponent comp = hardReferences[j].getReferencedComponent();
				if (comp.isBinary()) {
					final VirtualArchiveComponent archiveComp = (VirtualArchiveComponent) comp;
					final File diskFile = archiveComp.getUnderlyingDiskFile();
					IPath diskPath = null;
					if (diskFile.exists()) {
						diskPath =new Path(diskFile.getAbsolutePath());
	                } else {
	                    final IFile iFile = archiveComp.getUnderlyingWorkbenchFile();
	                    diskPath = iFile.getFullPath();
	                }
					hardRefPaths[j] = diskPath;
				}
			}
			
			IContainer[] mappedClassFolders = null;
			final Iterator i = referencedEntries.keySet().iterator();
			while (i.hasNext()) {
				final IClasspathEntry entry = (IClasspathEntry) i.next();
				final IClasspathAttribute attrib = (IClasspathAttribute) referencedEntries.get(entry);
				final boolean isClassFolder = ClasspathDependencyUtil.isClassFolderEntry(entry);
				final IPath runtimePath = ClasspathDependencyUtil.getRuntimePath(attrib, isWebApp, isClassFolder);				
				boolean add = true;
				final IPath entryLocation = ClasspathDependencyUtil.getEntryLocation(entry);
				if (entryLocation == null) {
					// unable to retrieve location for cp entry, do not contribute as a virtual ref
					add = false;
				} else if (!isClassFolder) { // check hard archive refs
					for (int j = 0; j < hardRefPaths.length; j++) {
						if (entryLocation.equals(hardRefPaths[j])) {
							// entry resolves to same file as existing hard reference, can skip
							add = false;
							break;
						}
					}
				} else { // check class folders mapped in component file as class folders associated with mapped src folders
					if (mappedClassFolders == null) {
						mappedClassFolders = J2EEProjectUtilities.getAllOutputContainers(getProject());
					}
					for (int j = 0; j < mappedClassFolders.length; j++) {
						if (entryLocation.equals(mappedClassFolders[j].getFullPath())) {
							// entry resolves to same file as existing class folder mapping, skip
							add = false;
							break;
						}
					} 
				}

				if (add) {
					String componentPath = null;
					ClasspathDependencyVirtualComponent entryComponent = null;
					/*
					if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
						componentPath = VirtualArchiveComponent.CLASSPATHARCHIVETYPE;
						final IProject cpEntryProject = ResourcesPlugin.getWorkspace().getRoot().getProject(entry.getPath().lastSegment());
						entryComponent = (VirtualArchiveComponent) ComponentCore.createArchiveComponent(cpEntryProject, componentPath);
					} else {
					*/
					componentPath = VirtualArchiveComponent.CLASSPATHARCHIVETYPE + IPath.SEPARATOR + entryLocation.toPortableString();
					entryComponent = new ClasspathDependencyVirtualComponent(project, componentPath, isClassFolder);
					//}
					final IVirtualReference entryReference = ComponentCore.createReference(this, entryComponent, runtimePath);
					entryReference.setArchiveName(ClasspathDependencyUtil.getArchiveName(entry));
					cpRefs.add(entryReference);
				}
			}

		} catch (CoreException jme) {
			Logger.getLogger().logError(jme);
		} 
		
		return (IVirtualReference[]) cpRefs.toArray(new IVirtualReference[cpRefs.size()]);
	}

	public static List getManifestReferences(IVirtualComponent moduleComponent, IVirtualReference[] hardReferences) {
		List dynamicReferences = null;
		String [] manifestClasspath = getManifestClasspath(moduleComponent); 

		IVirtualReference foundRef = null;
		String earArchiveURI = null; //The URI for this archive in the EAR
		boolean simplePath = false;
		
		if (manifestClasspath != null && manifestClasspath.length > 0) {
			IProject[] earProjects = J2EEProjectUtilities.getAllProjectsInWorkspaceOfType(J2EEProjectUtilities.ENTERPRISE_APPLICATION);
			IVirtualReference[] earRefs = null;
			for (int i = 0; i < earProjects.length && null == earRefs; i++) {
				IVirtualComponent tempEARComponent = ComponentCore.createComponent(earProjects[i]);
				IVirtualReference[] tempEarRefs = tempEARComponent.getReferences();
				for (int j = 0; j < tempEarRefs.length && earRefs == null; j++) {
					if (tempEarRefs[j].getReferencedComponent().equals(moduleComponent)) {
						earRefs = tempEarRefs;
						foundRef = tempEarRefs[j];
						earArchiveURI = foundRef.getArchiveName(); 
						simplePath = earArchiveURI != null ? earArchiveURI.lastIndexOf("/") == -1 : true; //$NON-NLS-1$
					}
				}
			}

			if (null != earRefs) {
				for (int i = 0; i < manifestClasspath.length; i++) {
					boolean found = false;
					for (int j = 0; j < earRefs.length && !found; j++) {
						if(foundRef != earRefs[j]){
							String archiveName = earRefs[j].getArchiveName();
							if (null != archiveName){
								boolean shouldAdd = false;
								if(simplePath && manifestClasspath[i].lastIndexOf("/") == -1){ //$NON-NLS-1$
									shouldAdd = archiveName.equals(manifestClasspath[i]);	
								} else {
									String earRelativeURI = ArchiveUtil.deriveEARRelativeURI(manifestClasspath[i], earArchiveURI);
									if(null != earRelativeURI){
										shouldAdd = earRelativeURI.equals(archiveName);	
									}
								}
								
								if(shouldAdd){
									found = true;
									boolean shouldInclude = true;
									IVirtualComponent dynamicComponent = earRefs[j].getReferencedComponent();
									if(null != hardReferences){
										for (int k = 0; k < hardReferences.length && shouldInclude; k++) {
											if (hardReferences[k].getReferencedComponent().equals(dynamicComponent)) {
												shouldInclude = false;
											}
										}
									}
									if (shouldInclude) {
										IVirtualReference dynamicReference = ComponentCore.createReference(moduleComponent, dynamicComponent);
										if (null == dynamicReferences) {
											dynamicReferences = new ArrayList();
										}
										dynamicReferences.add(dynamicReference);
									}
								}
							}
						}
					}
				}
			}
		}
		return dynamicReferences;
	}

}
