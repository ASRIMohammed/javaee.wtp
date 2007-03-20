/**
 * <copyright>
 * </copyright>
 *
 * $Id: WebAppVersionType.java,v 1.1 2007/03/20 18:04:39 jsholl Exp $
 */
package org.eclipse.jst.javaee.web;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>App Version Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * 
 * 
 * 	This type contains the recognized versions of
 * 	web-application supported. It is used to designate the
 * 	version of the web application.
 * 
 *       
 * <!-- end-model-doc -->
 * @see org.eclipse.jst.javaee.web.internal.metadata.WebPackage#getWebAppVersionType()
 * @generated
 */
public final class WebAppVersionType extends AbstractEnumerator {
	/**
	 * The '<em><b>25</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>25</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #_25_LITERAL
	 * @generated
	 * @ordered
	 */
	public static final int _25 = 0;

	/**
	 * The '<em><b>25</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #_25
	 * @generated
	 * @ordered
	 */
	public static final WebAppVersionType _25_LITERAL = new WebAppVersionType(_25, "_25", "2.5"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * An array of all the '<em><b>App Version Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final WebAppVersionType[] VALUES_ARRAY =
		new WebAppVersionType[] {
			_25_LITERAL,
		};

	/**
	 * A public read-only list of all the '<em><b>App Version Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>App Version Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static WebAppVersionType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			WebAppVersionType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>App Version Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static WebAppVersionType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			WebAppVersionType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>App Version Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static WebAppVersionType get(int value) {
		switch (value) {
			case _25: return _25_LITERAL;
		}
		return null;	
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private WebAppVersionType(int value, String name, String literal) {
		super(value, name, literal);
	}

} //WebAppVersionType
