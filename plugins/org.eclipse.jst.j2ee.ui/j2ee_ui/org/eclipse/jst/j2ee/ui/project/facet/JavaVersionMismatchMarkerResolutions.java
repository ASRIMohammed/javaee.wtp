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

package org.eclipse.jst.j2ee.ui.project.facet;

import java.util.Collections;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jst.common.project.facet.JavaFacetUtils;
import org.eclipse.jst.common.project.facet.JavaFacetValidator;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.IFacetedProject.Action;

/**
 * @author <a href="mailto:kosta@bea.com">Konstantin Komissarchik</a>
 */

public final class JavaVersionMismatchMarkerResolutions

    implements IMarkerResolutionGenerator
    
{
    public IMarkerResolution[] getResolutions( final IMarker marker )
    {
        return new IMarkerResolution[] 
        { 
            new Resolution1( marker ), 
            new Resolution2( marker ) 
        };
    }
    
    private static final class Resolution1
    
        implements IMarkerResolution
        
    {
        private final IMarker marker;
        
        public Resolution1( final IMarker marker )
        {
            this.marker = marker;
        }
        
        public String getLabel()
        {
            final IProjectFacetVersion fv = getProjectFacetVersion();
            return NLS.bind( Resources.changeFacetVersion, fv );
        }

        public void run( final IMarker marker )
        {
            final Action action
                = new Action( Action.Type.VERSION_CHANGE, 
                              getProjectFacetVersion(), null );

            final IProject proj = this.marker.getResource().getProject();
            
            try
            {
                final IFacetedProject fproj 
                    = ProjectFacetsManager.create( proj );
                
                fproj.modify( Collections.singleton( action ), null );
            }
            catch( CoreException e )
            {
                ErrorDialog.openError( null, Resources.errorDialogTitle,
                                       Resources.errorDialogMessage,
                                       e.getStatus() );
            }
        }
        
        private IProjectFacetVersion getProjectFacetVersion()
        {
            final String level 
                = this.marker.getAttribute( JavaFacetValidator.ATTR_COMPILER_LEVEL, null );
            
            return JavaFacetUtils.compilerLevelToFacet( level );
        }
    }

    private static final class Resolution2
    
        implements IMarkerResolution
        
    {
        private final IMarker marker;
        
        public Resolution2( final IMarker marker )
        {
            this.marker = marker;
        }
        
        public String getLabel()
        {
            final String level = getCompilerLevel();
            return NLS.bind( Resources.changeCompilerLevel, level );
        }
    
        public void run( final IMarker marker )
        {
            final IProject project = this.marker.getResource().getProject();
            final String level = getCompilerLevel();
            
            try
            {
                JavaFacetUtils.setCompilerLevel( project, level );
                JavaFacetUtils.scheduleFullBuild( project );
            }
            catch( CoreException e )
            {
                ErrorDialog.openError( null, Resources.errorDialogTitle,
                                       Resources.errorDialogMessage,
                                       e.getStatus() );
            }
        }
        
        private String getCompilerLevel()
        {
            final String fvstr
                = this.marker.getAttribute( JavaFacetValidator.ATTR_FACET_VERSION, null);
            
            final IProjectFacetVersion fv
                = JavaFacetUtils.JAVA_FACET.getVersion( fvstr );
            
            return JavaFacetUtils.facetToCompilerLevel( fv );
        }
    }
    
    private static final class Resources
    
        extends NLS
        
    {
        public static String changeFacetVersion;
        public static String changeCompilerLevel;
        public static String errorDialogTitle;
        public static String errorDialogMessage;
        
        static
        {
            initializeMessages( JavaVersionMismatchMarkerResolutions.class.getName(), 
                                Resources.class );
        }
    }
    
    
}