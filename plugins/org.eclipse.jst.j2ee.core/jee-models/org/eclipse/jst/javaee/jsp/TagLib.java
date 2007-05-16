/**
 * <copyright>
 * </copyright>
 *
 * $Id: TagLib.java,v 1.1 2007/05/16 06:42:41 cbridgha Exp $
 */
package org.eclipse.jst.javaee.jsp;

import org.eclipse.jst.javaee.core.JavaEEObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tag Lib</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 * 	The taglibType defines the syntax for declaring in
 * 	the deployment descriptor that a tag library is
 * 	available to the application.  This can be done
 * 	to override implicit map entries from TLD files and
 * 	from the container.
 * 
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.jst.javaee.jsp.TagLib#getTaglibUri <em>Taglib Uri</em>}</li>
 *   <li>{@link org.eclipse.jst.javaee.jsp.TagLib#getTaglibLocation <em>Taglib Location</em>}</li>
 *   <li>{@link org.eclipse.jst.javaee.jsp.TagLib#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.jst.javaee.jsp.internal.metadata.JspPackage#getTagLib()
 * @extends JavaEEObject
 * @generated
 */
public interface TagLib extends JavaEEObject {
	/**
	 * Returns the value of the '<em><b>Taglib Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 
	 * 	    A taglib-uri element describes a URI identifying a
	 * 	    tag library used in the web application.  The body
	 * 	    of the taglib-uri element may be either an
	 * 	    absolute URI specification, or a relative URI.
	 * 	    There should be no entries in web.xml with the
	 * 	    same taglib-uri value.
	 * 
	 * 	  
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Taglib Uri</em>' attribute.
	 * @see #setTaglibUri(String)
	 * @see org.eclipse.jst.javaee.jsp.internal.metadata.JspPackage#getTagLib_TaglibUri()
	 * @generated
	 */
	String getTaglibUri();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.javaee.jsp.TagLib#getTaglibUri <em>Taglib Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Taglib Uri</em>' attribute.
	 * @see #getTaglibUri()
	 * @generated
	 */
	void setTaglibUri(String value);

	/**
	 * Returns the value of the '<em><b>Taglib Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 
	 * 	    the taglib-location element contains the location
	 * 	    (as a resource relative to the root of the web
	 * 	    application) where to find the Tag Library
	 * 	    Description file for the tag library.
	 * 
	 * 	  
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Taglib Location</em>' attribute.
	 * @see #setTaglibLocation(String)
	 * @see org.eclipse.jst.javaee.jsp.internal.metadata.JspPackage#getTagLib_TaglibLocation()
	 * @generated
	 */
	String getTaglibLocation();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.javaee.jsp.TagLib#getTaglibLocation <em>Taglib Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Taglib Location</em>' attribute.
	 * @see #getTaglibLocation()
	 * @generated
	 */
	void setTaglibLocation(String value);

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
	 * @see org.eclipse.jst.javaee.jsp.internal.metadata.JspPackage#getTagLib_Id()
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.javaee.jsp.TagLib#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

} // TagLib