/******************************************************************************
 * Copyright (c) 2005 BEA Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Konstantin Komissarchik - initial API and implementation
 ******************************************************************************/

package org.eclipse.jst.servlet.ui.project.facet;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.ui.FacetedProjectWizard;
import org.osgi.framework.Bundle;

/**
 * @author <a href="mailto:kosta@bea.com">Konstantin Komissarchik</a>
 */

public final class WebProjectWizardOld 

    extends FacetedProjectWizard 
    
{
    protected IFacetedProjectTemplate getTemplate()
    {
        return ProjectFacetsManager.getTemplate( "template.jst.web" ); //$NON-NLS-1$
    }

    protected String getPageDescription()
    {
        return "Create a Web project in the workspace or at an external location.";
    }
    
    protected ImageDescriptor getDefaultPageImageDescriptor()
    {
        final Bundle bundle = Platform.getBundle( "org.eclipse.jst.servlet.ui" );
        final URL url = bundle.getEntry( "icons/full/ctool16/web-wiz-banner.gif" );

        return ImageDescriptor.createFromURL( url );
    }
    
}
