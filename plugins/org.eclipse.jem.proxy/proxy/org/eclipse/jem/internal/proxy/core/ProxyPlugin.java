package org.eclipse.jem.internal.proxy.core;
/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ProxyPlugin.java,v $
 *  $Revision: 1.2 $  $Date: 2004/01/19 22:50:35 $ 
 */


import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

import org.eclipse.core.boot.BootLoader;
import org.eclipse.core.internal.plugins.IPluginVisitor;
import org.eclipse.core.internal.plugins.PluginRegistry;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.model.*;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.*;
import org.eclipse.jem.internal.core.EclipseLogMsgLogger;
import org.eclipse.jem.internal.core.MsgLogger;

/**
 * The plugin class for the org.eclipse.jem.internal.proxy.core plugin.
 */

public class ProxyPlugin extends Plugin {
	
	/**
	 * This interface is for a listener that needs to know if this plugin (ProxyPlugin) is being shutdown. 
	 * It is needed because there are some extensions that get added dynamically that need to know when the
	 * plugin is being shutdown.
	 * 
	 * @since 1.0.0
	 */
	public interface IProxyPluginShutdownListener {
		/**
		 * ProxyPlugin is in shutdown.
		 * 
		 * @since 1.0.0
		 */
		public void shutdown();
	}
	
	private static ProxyPlugin PROXY_PLUGIN = null;

	public static final String DEBUG_DEV = "/dev"; //$NON-NLS-1$
	// If this is set to true, then in development mode and it will try for proxy jars in directories.

	public static final String IMPLEMENTATIONS_EXT_PI = "proxyimplementations"; //$NON-NLS-1$
	public static final String IMPLEMENTATIONS_VMTYPE_ID = "vmtypeid"; //$NON-NLS-1$
	public static final String IMPLEMENTATIONS_VMNAME = "vmname"; //$NON-NLS-1$
	public static final String IMPLEMENTATIONS_AWT = "awt"; //$NON-NLS-1$
	public static final String IMPLEMENTATIONS_REGISTRATION = "registration"; //$NON-NLS-1$
	public static final  String ENVIRONMENT_VARIABLE = "environment_variable";	//$NON-NLS-1$
	
	private boolean devMode;
	
	private ListenerList shutdownListeners;

	public ProxyPlugin(IPluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
		PROXY_PLUGIN = this;
		devMode = BootLoader.inDevelopmentMode();
		
	}

	/**
	 * Accessor method to get the singleton plugin.
	 */
	public static ProxyPlugin getPlugin() {
		return PROXY_PLUGIN;
	}
	
	private MsgLogger msgLogger;
	public MsgLogger getMsgLogger() {
		if (msgLogger == null)
			msgLogger = EclipseLogMsgLogger.createLogger(this);
		return msgLogger;
	}
	

	/**
	 * Start a Proxy implementation.
	 */
	public ProxyFactoryRegistry startImplementation(
		IProject project,
		String vmTitle,
		IConfigurationContributor[] aContribs,
		IProgressMonitor pm)
		throws CoreException {
		// Need to find the correct Registration to use.

		IJavaProject javaProject = JavaCore.create(project);
		if (javaProject == null) {
			getMsgLogger().log(
				new Status(
					IStatus.WARNING,
					getDescriptor().getUniqueIdentifier(),
					0,
					MessageFormat.format(
						ProxyMessages.getString(ProxyMessages.NOT_JAVA_PROJECT),
						new Object[] { project.getName()}),
					null));
			return null;
		}
		JavaRuntime.getVMInstallTypes(); // Needed to force initialization of VM types.
		IVMInstall vm = JavaRuntime.getVMInstall(javaProject);
		if (vm == null)
			vm = JavaRuntime.getDefaultVMInstall();
		String vmTypeID = vm.getVMInstallType().getId();
		String vmName = vm.getName();
		if (javaProject == null) {
			getMsgLogger().log(
				new Status(
					IStatus.WARNING,
					getDescriptor().getUniqueIdentifier(),
					0,
					MessageFormat.format(
						ProxyMessages.getString(ProxyMessages.NO_VM),
						new Object[] { project.getName()}),
					null));
			return null;
		}

		// Try to find the configuration element from the extension point "org.eclipse.jem.proxy.proxyImplementations"
		// that matchs the VM we will be using.
		IExtensionPoint xp = getDescriptor().getExtensionPoint(IMPLEMENTATIONS_EXT_PI);
		if (xp != null) {
			IConfigurationElement defaultImplementation = null;
			IConfigurationElement matchedImplementation = null;
			IExtension[] extensions = xp.getExtensions();
			for (int i = 0; matchedImplementation == null && i < extensions.length; i++) {
				IConfigurationElement[] ces = extensions[i].getConfigurationElements();
				for (int j = 0; j < ces.length; j++) {
					IConfigurationElement ce = ces[i];
					String evmTypeID = ce.getAttributeAsIs(IMPLEMENTATIONS_VMTYPE_ID);
					if (vmTypeID.equals(evmTypeID)) {
						String evmName = ce.getAttributeAsIs(IMPLEMENTATIONS_VMNAME);
						if (evmName == null)
							if (defaultImplementation == null) {
								defaultImplementation = ce; // Found a default
								break;
							} else
								;
						else if (evmName.equals(vmName)) {
							matchedImplementation = ce; // Found a match
							break;
						}
					}
				}
			}

			if (matchedImplementation == null)
				matchedImplementation = defaultImplementation; // Use the default.
			if (matchedImplementation != null) {
				// We have an implementation.
				String awtParm = matchedImplementation.getAttributeAsIs(IMPLEMENTATIONS_AWT);
				boolean attachAWT = awtParm == null || awtParm.equals("y"); //$NON-NLS-1$
				IRegistration reg =
					(IRegistration) matchedImplementation.createExecutableExtension(
						IMPLEMENTATIONS_REGISTRATION);
				IConfigurationContributor[] contribs =
					new IConfigurationContributor[1 + (aContribs != null ? aContribs.length : 0)];
				contribs[0] = new ProxyContributor();
				if (aContribs != null)
					System.arraycopy(aContribs, 0, contribs, 1, aContribs.length);
				return reg.startImplementation(contribs, attachAWT, project, vmTitle, pm);
			}
		}
		getMsgLogger().log(
			new Status(
				IStatus.WARNING,
				getDescriptor().getUniqueIdentifier(),
				0,
				MessageFormat.format(
					ProxyMessages.getString(ProxyMessages.NO_IMPLEMENTATION),
					new Object[] { project.getName()}),
				null));
		return null;
	}

	/**
	 * See localizeFromPluginDescriptor...
	 * This is just a helper to pass in a Plugin where the plugin is handy instead of the IPluginDescriptor.
	 */
	public String localizeFromPlugin(Plugin plugin, String fileNameWithinPlugin) {
		return localizeFromPluginDescriptor(plugin.getDescriptor(), fileNameWithinPlugin);
	}
	
	/**
	 * This will take the plugin and file name and make it local and return that
	 * fully qualified. It will not take fragments into account.
	 * 
	 * If the .options file has the org.eclipse.jem.internal.proxy.core/dev=true then we are in development and it will pick it up from the path
	 * that is listed in the proxy.jars file located in the plugin passed in. This allows development code to be
	 * used in place of the actual runtime jars. If the runtime jars are found,
	 * they will be used.
	 * 
	 * For example if looking for file runtime/xyz.jar in plugin abc, then in plugin directory for abc,
	 * there should be a file called proxy.jars. This should only be in development, this file should not
	 * be distributed for production. It would be distributed in the SDK environment when testing is desired.
	 * 
	 * The format of the file is:
	 * 	runtimefile=/projectname/builddirectory
	 *
	 * For this to work when the actual jar is not found, the Eclipse must of been started in 
	 * dev mode (i.e. the plugin location will be a project within the developer Eclipse. That way
	 * we can go up one level for the current install location and assume the above projectname
	 * will be found relative to the directory.
	 * 
	 * For the above example:
	 * 	runtime/xyz.jar=/xyzproject/bin
	 * 
	 * It will return "." if file can't be found. It means nothing but it won't cause jvm to crash.
	 */
	public String localizeFromPluginDescriptor(IPluginDescriptor pluginDescriptor, String filenameWithinPlugin) {
		URL url = urlLocalizeFromPluginDescriptor(pluginDescriptor, filenameWithinPlugin);
		return url != null ? url.getFile() : "."; //$NON-NLS-1$
	}
	
	/**
	 * localizeFromPluginDescriptorAndFragments.
	 * Just like localizeFromPluginDescriptor except it will return an array of Strings. It will look for the filename
	 * within the plugin and any fragments of the plugin. If none are found, an empty array will be returned.
	 * 
	 * To find the files in the fragments that are in the runtime path (i.e. libraries), it will need to use a suffix,
	 * This is because the JDT will get confused if a runtime jar in a fragment has the same name
	 * as a runtime jar in the main plugin. So we will use the following search pattern:
	 * 
	 * 1) Find in all of the fragments those that match the name exactly
	 * 2) Find in all of the fragments, in their runtime path (library stmt), those that match the name 
	 *    but have a suffix the same as the uniqueid of the fragment (preceeded by a period). This is so that it can be easily
	 *    found but yet be unique in the entire list of fragments. For example if looking for "runtime/xyz.jar"
	 *    and we have fragment "a.b.c.d.frag", then in the runtime path we will look for the file
	 *    "runtime/xyz.a.b.c.d.frag.jar".
	 * 
	 * If the files in the fragments are not in the fragments library path then it can have the same name.
	 * 
	 * This is useful for nls where the nls for the filename will be in one or more of the fragments of the plugin.
	 */
	public String[] localizeFromPluginDescriptorAndFragments(IPluginDescriptor pluginDescriptor, String filenameWithinPlugin) {
		URL[] urls = urlLocalizeFromPluginDescriptorAndFragments(pluginDescriptor, filenameWithinPlugin);
		String[] result = new String[urls.length];
		for (int i = 0; i < urls.length; i++) {
			result[i] = urls[i].getFile();
		}
		return result;
	}


	/**
	 * See localizeFromPluginDescriptorAndFragments...
	 * This is a helper to return a list of URLs instead.
	 */
	public URL[] urlLocalizeFromPluginDescriptorAndFragments(IPluginDescriptor pluginDescriptor, String filenameWithinPlugin) {

		PluginFragmentModel[] fragments = ((PluginDescriptorModel) pluginDescriptor).getFragments();	// See if there are any fragments
		if (fragments == null || fragments.length == 0) {
			URL result = urlLocalizeFromPluginDescriptor(pluginDescriptor, filenameWithinPlugin);
			return result != null ? new URL[] {result} : new URL[0];
		} else {
			ArrayList urls = new ArrayList(fragments.length+1);
			URL url = urlLocalizeFromPluginDescriptor(pluginDescriptor, filenameWithinPlugin);
			if (url != null)
				urls.add(url);
			for (int i=0; i<fragments.length; i++) {
				PluginFragmentModel fragment = fragments[i];
				url = urlLocalizeFromFragment(fragment, filenameWithinPlugin);
				if (url != null)
					urls.add(url);
				// Also, look through the libraries of the fragment to see if one matches the special path.
				LibraryModel[] libraries = fragment.getRuntime();
				if (libraries != null && libraries.length > 0) {
					int extndx = filenameWithinPlugin.lastIndexOf('.');
					String libFile = null;
					if (extndx != -1)
						libFile = filenameWithinPlugin.substring(0, extndx) + '.' + fragment.getId() + filenameWithinPlugin.substring(extndx);
					else
						libFile = filenameWithinPlugin + '.' + fragment.getId();
					for (int j = 0; j < libraries.length; j++) {
						if (libFile.equals(libraries[j].getName())) {
							url = urlLocalizeFromFragment(fragment, libFile);
							if (url != null)
								urls.add(url);
							break;
						}	
					}	
				}
			}
			return (URL[]) urls.toArray(new URL[urls.size()]);
		}
	}
	
	private URL urlLocalizeFromFragment(PluginFragmentModel fragmentDescriptor, String filenameWithinPlugin) {
		try {
			URL installURL = new URL("platform:/fragment/"+ fragmentDescriptor.toString() + "/");	// Could change in future, but they hid getInstallURL from fragment model. //$NON-NLS-1$ //$NON-NLS-2$
			return urlLocalize(installURL, filenameWithinPlugin, true);	// Fragments need to be tested because they may not exist.
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	/**
	 * See localizeFromPluginDescriptor...
	 * This is just a helper to return a url instead.
	 */		
	public URL urlLocalizeFromPluginDescriptor(IPluginDescriptor pluginDescriptor, String filenameWithinPlugin) {			
		return urlLocalize(pluginDescriptor.getInstallURL(), filenameWithinPlugin, false);
	}
	
	private URL urlLocalize(URL installURL, String filenameWithinPlugin, boolean mustExist) {			
		
		try {
			URL pvm =
				Platform.asLocalURL(
					new URL(installURL, filenameWithinPlugin));
			if (devMode || mustExist) {
				// Need to test if found in devmode. Otherwise we will just assume it is found, or test if mustTest is true. If not found on remote to cache, an IOException would be thrown.
				InputStream ios = null;
				try {
					ios = pvm.openStream();
					if (ios != null)
						return pvm;	// Found it, so return it.
				} finally {
					if (ios != null)
						ios.close();
				}
			} else
				return pvm;
		} catch (IOException e) {
		}

		if (devMode) {
			// Got this far and in dev mode means it wasn't found, so we'll try for development style
			try {
				URL pvm = new URL(installURL, "proxy.jars"); //$NON-NLS-1$
				InputStream ios = null;
				try {
					ios = pvm.openStream();
					Properties props = new Properties();
					props.load(ios);
					String pathString = props.getProperty(filenameWithinPlugin);
					if (pathString != null) {
						IPath path = new Path(Platform.resolve(installURL).getFile());
						path = path.removeLastSegments(1);	// Move up one level to workspace root of development workspace.
						path = path.append(pathString);
						return new URL("file", null, path.toString()); //$NON-NLS-1$
					}
				} finally {
					if (ios != null)
						ios.close();
				}
			} catch (IOException e) {
			}
		}

		return null; // Nothing found
	}

	class ProxyContributor implements IConfigurationContributor {
		public void contributeClasspaths(List classPaths, IClasspathContributionController controller) {
			// Add the required jars to the end of the classpath.
			controller.contributeClasspath(
				localizeFromPlugin(ProxyPlugin.this, "proxycommon.jar"), //$NON-NLS-1$
				classPaths,
				-1);
			controller.contributeClasspath(
				urlLocalizeFromPluginDescriptorAndFragments(ProxyPlugin.this.getDescriptor(), "initparser.jar"), //$NON-NLS-1$
				classPaths,
				-1);
		}
		public void contributeToConfiguration(VMRunnerConfiguration config) {
		}
		public void contributeToRegistry(ProxyFactoryRegistry registry) {
		}
	}
	
	/**
	 * A helper to order the plugin descriptors into pre-req order. 
	 * If A eventually depends on B, then B will be ahead of A in the
	 * list of plugins. (I.e. B is a pre-req somewhere of A).
	 */
	public static IPluginDescriptor[] orderPlugins(final Set pluginsToOrder) {
		// Use the internal method in PluginRegistry to do this. If we ever loose this, made to come up with a different way.
		PluginRegistry registry = (PluginRegistry) Platform.getPluginRegistry();
		final int[] ndx = new int[] {pluginsToOrder.size()};
		final IPluginDescriptor[] result = new IPluginDescriptor[ndx[0]];
		IPluginVisitor visitor = new IPluginVisitor() {
			/**
			 * @see org.eclipse.core.internal.plugins.IPluginVisitor#visit(IPluginDescriptor)
			 */
			public void visit(IPluginDescriptor descriptor) {
				if (pluginsToOrder.contains(descriptor))
					result[--ndx[0]] = descriptor;
			}
		};
		registry.accept(visitor, false);
		return result;
	}
	
	public static Process exec(String[] cmdLine, File workingDirectory, String[] environmentProperties) throws CoreException {
		// This was copied from DebugPlugin so we need to check back with this for correct exception handling
		// For the JVE we need control over the environment properties, hence the extra argument 
		Process p= null;
		try {

			if (workingDirectory == null) {
				p= Runtime.getRuntime().exec(cmdLine, environmentProperties);
			} else {
				p= Runtime.getRuntime().exec(cmdLine, environmentProperties, workingDirectory);
			}
		} catch (IOException e) {
				if (p != null) {
					p.destroy();
				}
				throw new CoreException(null);	//TODO exception handling
		} catch (NoSuchMethodError e) {
			//TODO exception handling			
		}
		return p;
	}	
	
	/**
	 * Add a shutdown listener
	 * @param listener
	 * 
	 * @since 1.0.0
	 */
	public void addProxyShutdownListener(IProxyPluginShutdownListener listener) {
		if (shutdownListeners == null)
			shutdownListeners = new ListenerList();
		shutdownListeners.add(listener);
	}

	/**
	 * Remove a shutdown listener
	 * @param listener
	 * 
	 * @since 1.0.0
	 */
	public void removeProxyShutdownListener(IProxyPluginShutdownListener listener) {
		if (shutdownListeners != null)
			shutdownListeners.remove(listener);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#shutdown()
	 */
	public void shutdown() throws CoreException {
		if (shutdownListeners != null) {
			Object[] listeners = shutdownListeners.getListeners();
			for (int i = 0; i < listeners.length; i++) {
				((IProxyPluginShutdownListener) listeners[i]).shutdown();
			}
		}
		super.shutdown();
	}

}