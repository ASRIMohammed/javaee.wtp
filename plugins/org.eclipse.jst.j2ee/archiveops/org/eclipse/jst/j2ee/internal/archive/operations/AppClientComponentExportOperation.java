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
package org.eclipse.jst.j2ee.internal.archive.operations;

import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

/**
 * @deprecated use org.eclipse.jst.j2ee.internal.archive.operations.JavaEEComponentExportOperation instead
 */
public class AppClientComponentExportOperation extends J2EEArtifactExportOperation {

	public AppClientComponentExportOperation(IDataModel model) {
		super(model);
	}

}
