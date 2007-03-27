/**
 * <copyright>
 * </copyright>
 *
 * $Id: WelcomeFileList.java,v 1.1 2007/03/20 18:04:39 jsholl Exp $
 */
package org.eclipse.jst.javaee.web;

import java.util.List;

import org.eclipse.jst.javaee.core.JavaEEObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Welcome File List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 * 	The welcome-file-list contains an ordered list of welcome
 * 	files elements.
 * 
 * 	Used in: web-app
 * 
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.jst.javaee.web.WelcomeFileList#getWelcomeFiles <em>Welcome Files</em>}</li>
 *   <li>{@link org.eclipse.jst.javaee.web.WelcomeFileList#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.jst.javaee.web.internal.metadata.WebPackage#getWelcomeFileList()
 * @extends JavaEEObject
 * @generated
 */
public interface WelcomeFileList extends JavaEEObject {
	/**
	 * Returns the value of the '<em><b>Welcome Files</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 
	 * 	    The welcome-file element contains file name to use
	 * 	    as a default welcome file, such as index.html
	 * 
	 * 	  
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Welcome Files</em>' attribute list.
	 * @see org.eclipse.jst.javaee.web.internal.metadata.WebPackage#getWelcomeFileList_WelcomeFiles()
	 * @generated
	 */
	List getWelcomeFiles();

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.eclipse.jst.javaee.web.internal.metadata.WebPackage#getWelcomeFileList_Id()
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.javaee.web.WelcomeFileList#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

} // WelcomeFileList