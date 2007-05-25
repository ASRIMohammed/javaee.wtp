/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.jee.archive;

public class ArchiveOpenFailureException extends ArchiveException {

	private static final long serialVersionUID = -7366116122777721148L;

	public ArchiveOpenFailureException(String message) {
		super(message);
	}

	public ArchiveOpenFailureException(Throwable cause){
		super(cause);
	}
	
	public ArchiveOpenFailureException(String message, Throwable cause){
		super(message, cause);
	}
	
}
