/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.jee.project.facet;

import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;

public interface IAppClientFacetInstallDataModelProperties extends IJ2EEModuleFacetInstallDataModelProperties {

	 /**
     * Optional, type Boolean. the default value is <code>Boolean.TRUE</code>.If this is true and CREATE_DEFAULT_FILES is true, then a default main
     * class will be generated during component creation.
     */
    public static final String CREATE_DEFAULT_MAIN_CLASS = "IAppClientComponentCreationDataModelProperties.CREATE_DEFAULT_MAIN_CLASS"; //$NON-NLS-1$
    
}
