/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Jun 1, 2004
 *
 */
package org.eclipse.jst.j2ee.internal.web.operations;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.j2ee.internal.common.operations.J2EEModelModifierOperationDataModel;
import org.eclipse.wst.common.frameworks.operations.WTPOperation;
import org.eclispe.wst.common.frameworks.internal.plugin.WTPCommonPlugin;

/**
 * @author jialin
 *  
 */
public class RemoveServletDataModel extends J2EEModelModifierOperationDataModel {
	public static final String REMOVE_JAVA_CLASS = "RemoveServletDataModel.REMOVE_JAVA_CLASS"; //$NON-NLS-1$
	public static final String SERVLET_LIST = "RemoveServletDataModel.SERVLET_LIST"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.frameworks.internal.operation.WTPOperationDataModel#getDefaultOperation()
	 */
	public WTPOperation getDefaultOperation() {
		return new RemoveServletOperation(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.internal.emfworkbench.operation.ModelModifierOperationDataModel#initValidBaseProperties()
	 */
	protected void initValidBaseProperties() {
		super.initValidBaseProperties();
		addValidBaseProperty(REMOVE_JAVA_CLASS);
		addValidBaseProperty(SERVLET_LIST);
	}

	protected Object getDefaultProperty(String propertyName) {
		if (propertyName.equals(REMOVE_JAVA_CLASS)) {
			return Boolean.FALSE;
		}
		return super.getDefaultProperty(propertyName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.frameworks.internal.operation.WTPOperationDataModel#doValidateProperty(java.lang.String)
	 */
	protected IStatus doValidateProperty(String propertyName) {
		if (propertyName.equals(SERVLET_LIST))
			return validateServletList((List) getProperty(propertyName));
		return super.doValidateProperty(propertyName);
	}

	private IStatus validateServletList(List prop) {
		if (prop == null || prop.isEmpty()) {
			String msg = WebMessages.getResourceString(WebMessages.ERR_SERVLET_LIST_EMPTY);
			return WTPCommonPlugin.createErrorStatus(msg);
		}
		return WTPCommonPlugin.OK_STATUS;
	}
}