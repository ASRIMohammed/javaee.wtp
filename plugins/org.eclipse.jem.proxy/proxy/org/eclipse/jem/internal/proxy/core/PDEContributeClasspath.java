/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PDEContributeClasspath.java,v $
 *  $Revision: 1.2 $  $Date: 2004/11/01 21:52:37 $ 
 */
package org.eclipse.jem.internal.proxy.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.pde.core.plugin.*;
import org.eclipse.pde.internal.core.*;

 

/**
 * Contribute to classpath PDE entries that are needed for a launch.
 * <p>
 * This is a special class that should not be referenced directly from code. Reference it through the
 * interface that it implements. 
 * 
 * @see org.eclipse.jem.internal.proxy.core.IPDEContributeClasspath
 * @since 1.0.2
 */
class PDEContributeClasspath implements IPDEContributeClasspath {

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.remote.IPDEContributeClasspath#getPDEContributions(org.eclipse.core.resources.IProject)
	 */
	public void getPDEContributions(IConfigurationContributionController controller) throws CoreException {
		Map plugins = new HashMap();
		ProxyPlugin.getPlugin().findPlugins(plugins, true, true, controller.getJavaProject());
		if (!plugins.isEmpty()) {
			Collection pluginIds = plugins.keySet();
			PluginModelManager modelManager = PDECore.getDefault().getModelManager();
			IFragmentModel[] fragments = modelManager.getFragments();
			for (int i = 0; i < fragments.length; i++) {
				IFragment fragment = fragments[i].getFragment();
				if (pluginIds.contains(fragment.getPluginId())) {
					// We'll do a cheat for now and assume fragment is for same version of plugin. PDECore actually
					// checks the version of the fragment against the version of the plugin to see they are for each
					// other, but we'll just assume they are for now. Change this later if we actually do run into this case.
					IResource resource = fragment.getModel().getUnderlyingResource();
					if (resource != null) {
						IProject fragProject = resource.getProject();
						if (fragProject.hasNature(JavaCore.NATURE_ID)) {
							controller.contributeProject(fragProject);
						}
						continue;
					}

					IPluginLibrary[] libraries = fragment.getLibraries();
					for (int j = 0; j < libraries.length; j++) {
						IPluginLibrary library = libraries[j];
						String name = library.getName();
						String expandedName = ClasspathUtilCore.expandLibraryName(name);

						IPluginModelBase model = library.getPluginModel();
						URL url = getPath(model, expandedName);
						if (url != null)
							controller.contributeClasspath(url, IConfigurationContributionController.APPEND_USER_CLASSPATH);
					}
				}
			}
		}
		
		return;
	}
	
	private URL getPath(IPluginModelBase model, String libraryName) {
		try {
			IResource resource = model.getUnderlyingResource();
			if (resource != null) {
				IResource jarFile = resource.getProject().findMember(libraryName);
				if (jarFile != null)
					return jarFile.getFullPath().toFile().toURL();
			} else {
				File file = new File(model.getInstallLocation(), libraryName);
				if (file.exists())
					return file.toURL();
			}
		} catch (MalformedURLException e) {
			ProxyPlugin.getPlugin().getLogger().log(e, Level.WARNING);
		}
		return null;
	}
	

}
