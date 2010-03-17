/***********************************************************************
 * Copyright (c) 2008, 2010 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 ***********************************************************************/
package org.eclipse.jst.jee.ui.internal.navigator.web;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.javaee.core.JavaEEObject;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.jee.ui.internal.Messages;
import org.eclipse.jst.jee.ui.internal.navigator.AbstractGroupProvider;
import org.eclipse.jst.jee.ui.plugin.JEEUIPlugin;
import org.eclipse.jst.jee.ui.plugin.JEEUIPluginIcons;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;

/**
 * Content and Label Provider helper class for WebApp element.
 * 
 * @author Dimitar Giormov
 * @author Kaloyan Raev
 */
public class WebAppProvider extends AbstractWebGroupProvider implements IAdaptable {
  
	private static final String PROJECT_RELATIVE_PATH = "WEB-INF/web.xml"; //$NON-NLS-1$
	
	private GroupErrorPagesItemProvider errors;
	private GroupServletItemProvider servlets;
	private GroupFiltersItemProvider filters;
	private GroupListenerItemProvider listeners;
	private GroupServletMappingItemProvider servletMapping;
	private GroupFilterMappingItemProvider filterMapping;
	private GroupReferenceItemProvider references;
	private GroupWelcomePagesItemProvider welcome;
	private GroupContextParamsItemProvider contextParams;
	
	private List<Object> children = new ArrayList<Object>();
	
	private String text;
	
	private Image web25Image;
	private Image web30Image;
	
	private IProject prjct = null;
	private IFile ddFile = null;

	public WebAppProvider(WebApp webApp, IProject project) {
		super(webApp);
		text = NLS.bind(Messages.DEPLOYMENT_DESCRIPTOR, project.getName());
		contextParams = new GroupContextParamsItemProvider(webApp);
		errors = new GroupErrorPagesItemProvider(webApp);
		servlets = new GroupServletItemProvider(webApp);
		servletMapping = new GroupServletMappingItemProvider(webApp);
		filters = new GroupFiltersItemProvider(webApp);
		filterMapping = new GroupFilterMappingItemProvider(webApp);
		listeners = new GroupListenerItemProvider(webApp);
		references = new GroupReferenceItemProvider(webApp);
		welcome = new GroupWelcomePagesItemProvider(webApp);
		children.add(contextParams);
		children.add(errors);
		children.add(servlets);
		children.add(filters);
		children.add(listeners);
		children.add(servletMapping);
		children.add(filterMapping);
		children.add(references);
		children.add(welcome);
		prjct = project;
	}
	
	@Override
	public List getChildren(){
		return children;
	}

	@Override
	public String getText(){
		return text;
	}
	
	@Override
	public Image getImage() {
		String version = ((WebApp) javaee).getVersion().getLiteral();
		if (J2EEVersionConstants.VERSION_2_5_TEXT.equals(version)) {
			return getWeb25Image();
		} else if (J2EEVersionConstants.VERSION_3_0_TEXT.equals(version)) {
			return getWeb30Image();
		}
		return getWeb25Image();
	}

	public IProject getProject(){
		return prjct;
	}
	
	public IFile getDDFile() {
		if (ddFile != null){
			return ddFile;
		}

		IVirtualFolder virtualFolder = ComponentCore.createComponent(getProject()).getRootFolder();
		return virtualFolder.getFile(PROJECT_RELATIVE_PATH).getUnderlyingFile();
	}
	
	@Override
	public void reinit(JavaEEObject modelObject) {
		super.reinit(modelObject);
		for (Object child : children) {
			((AbstractGroupProvider)child).reinit(modelObject);
		}
	}

	public Object getAdapter(Class adapter) {
		if (IProject.class == adapter){
			return getProject();
		}
		return null;
	}
	
	private Image getWeb25Image() {
		if (web25Image == null) {
			web25Image = JEEUIPlugin.getDefault().getImageDescriptor(JEEUIPluginIcons.IMG_WEBEEMODEL).createImage();
		}
		return web25Image;
	}
	
	private Image getWeb30Image() {
		if (web30Image == null) {
			web30Image = JEEUIPlugin.getDefault().getImageDescriptor(JEEUIPluginIcons.IMG_WEBEE6MODEL).createImage();
		}
		return web30Image;
	}
}
