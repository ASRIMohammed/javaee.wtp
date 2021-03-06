/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jem.internal.proxy.ide.awt;
/*


 */

import java.awt.Point;

import org.eclipse.jem.internal.proxy.ide.*;

public class IDEPointBeanTypeProxy extends IDEBeanTypeProxy {

IDEPointBeanTypeProxy(IDEProxyFactoryRegistry aRegistry){
	this(aRegistry,Point.class);
}
private IDEPointBeanTypeProxy(IDEProxyFactoryRegistry aRegistry, Class type){
	super(aRegistry,type);
}
protected IIDEBeanProxy newBeanProxy(Object anObject){

	return new IDEPointBeanProxy(fProxyFactoryRegistry, anObject, this);

}
IDEPointBeanProxy createPointBeanProxy(int x, int y){
	return new IDEPointBeanProxy(fProxyFactoryRegistry,new Point(x,y),this);
}
	/*
	 * @see IDEBeanTypeProxy#newBeanTypeForClass(Class)
	 */
	public IDEBeanTypeProxy newBeanTypeForClass(Class type) {
		return new IDEPointBeanTypeProxy(fProxyFactoryRegistry, type);
	}

}
