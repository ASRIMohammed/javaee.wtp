/*******************************************************************************
 * Copyright (c) 2007, 2008 SAP AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Kaloyan Raev, kaloyan.raev@sap.com - initial API and implementation
 * Carl Anderson, ccc@us.ibm.com - handle null superclass (no runtime) (bug 214950)
 *******************************************************************************/
package org.eclipse.jst.j2ee.internal.web.operations;

import static org.eclipse.jst.j2ee.application.internal.operations.IAnnotationsDataModel.USE_ANNOTATIONS;
import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.DESCRIPTION;
import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.DISPLAY_NAME;

import org.eclipse.jst.j2ee.internal.common.operations.CreateJavaEEArtifactTemplateModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class CreateWebClassTemplateModel extends
		CreateJavaEEArtifactTemplateModel {
	
	public CreateWebClassTemplateModel(IDataModel dataModel) {
		super(dataModel);
	}
	
	public String getDisplayName() {
		return dataModel.getStringProperty(DISPLAY_NAME);
	}
	
	public String getDescription() {
		return dataModel.getStringProperty(DESCRIPTION);
	}

	public boolean isAnnotated() {
		return dataModel.getBooleanProperty(USE_ANNOTATIONS);
	}

}
