/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.javaee.ejb;

import java.util.List;

import org.eclipse.jst.javaee.core.Description;
import org.eclipse.jst.javaee.core.JavaEEObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Method Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * <![CDATA[[
 *         The methodType is used to denote a method of an enterprise
 *         bean's business, home, component, and/or web service endpoint
 *         interface, or, in the case of a message-driven bean, the
 *         bean's message listener method, or a set of such
 *         methods. The ejb-name element must be the name of one of the
 *         enterprise beans declared in the deployment descriptor; the
 *         optional method-intf element allows to distinguish between a
 *         method with the same signature that is multiply defined
 *         across the business, home, component, and/or web service 
 *         endpoint nterfaces; the method-name element specifies the 
 *         method name; and the optional method-params elements identify 
 *         a single method among multiple methods with an overloaded
 *         method name.
 *         
 *         There are three possible styles of using methodType element
 *         within a method element:
 *         
 *         1.
 *         <method>
 *         <ejb-name>EJBNAME</ejb-name>
 *         <method-name>*</method-name>
 *         </method>
 *         
 *         This style is used to refer to all the methods of the
 *         specified enterprise bean's business, home, component, 
 *         and/or web service endpoint interfaces.
 *         
 *         2.
 *         <method>
 *         <ejb-name>EJBNAME</ejb-name>
 *         <method-name>METHOD</method-name>
 *         </method>
 *         
 *         This style is used to refer to the specified method of
 *         the specified enterprise bean. If there are multiple
 *         methods with the same overloaded name, the element of
 *         this style refers to all the methods with the overloaded
 *         name.
 *         
 *         3.
 *         <method>
 *         <ejb-name>EJBNAME</ejb-name>
 *         <method-name>METHOD</method-name>
 *         <method-params>
 *         	  <method-param>PARAM-1</method-param>
 *         	  <method-param>PARAM-2</method-param>
 *         	  ...
 *         	  <method-param>PARAM-n</method-param>
 *         </method-params>
 *         </method>
 *         
 *         This style is used to refer to a single method within a
 *         set of methods with an overloaded name. PARAM-1 through
 *         PARAM-n are the fully-qualified Java types of the
 *         method's input parameters (if the method has no input
 *         arguments, the method-params element contains no
 *         method-param elements). Arrays are specified by the
 *         array element's type, followed by one or more pair of
 *         square brackets (e.g. int[][]). If there are multiple
 *         methods with the same overloaded name, this style refers
 *         to all of the overloaded methods. 
 *         
 *         Examples:
 *         
 *         Style 1: The following method element refers to all the
 *         methods of the EmployeeService bean's business, home, 
 *         component, and/or web service endpoint interfaces:
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-name>*</method-name>
 *         </method>
 *         
 *         Style 2: The following method element refers to all the
 *         create methods of the EmployeeService bean's home
 *         interface(s).
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-name>create</method-name>
 *         </method>
 *         
 *         Style 3: The following method element refers to the
 *         create(String firstName, String LastName) method of the
 *         EmployeeService bean's home interface(s).
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-name>create</method-name>
 *         <method-params>
 *         	  <method-param>java.lang.String</method-param>
 *         	  <method-param>java.lang.String</method-param>
 *         </method-params>
 *         </method>
 *         
 *         The following example illustrates a Style 3 element with
 *         more complex parameter types. The method 
 *         foobar(char s, int i, int[] iar, mypackage.MyClass mycl, 
 *         mypackage.MyClass[][] myclaar) would be specified as:
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-name>foobar</method-name>
 *         <method-params>
 *         	  <method-param>char</method-param>
 *         	  <method-param>int</method-param>
 *         	  <method-param>int[]</method-param>
 *         	  <method-param>mypackage.MyClass</method-param>
 *         	  <method-param>mypackage.MyClass[][]</method-param>
 *         </method-params>
 *         </method>
 *         
 *         The optional method-intf element can be used when it becomes
 *         necessary to differentiate between a method that is multiply
 *         defined across the enterprise bean's business, home, component,
 *         and/or web service endpoint interfaces with the same name and
 *         signature. However, if the same method is a method of both the
 *         local business interface, and the local component interface, 
 *         the same attribute applies to the method for both interfaces.
 *         Likewise, if the same method is a method of both the remote 
 *         business interface and the remote component interface, the same
 *         attribute applies to the method for both interfaces.
 *         
 *         For example, the method element
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-intf>Remote</method-intf>
 *         <method-name>create</method-name>
 *         <method-params>
 *         	  <method-param>java.lang.String</method-param>
 *         	  <method-param>java.lang.String</method-param>
 *         </method-params>
 *         </method>
 *         
 *         can be used to differentiate the create(String, String)
 *         method defined in the remote interface from the
 *         create(String, String) method defined in the remote home
 *         interface, which would be defined as
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-intf>Home</method-intf>
 *         <method-name>create</method-name>
 *         <method-params>
 *         	  <method-param>java.lang.String</method-param>
 *         	  <method-param>java.lang.String</method-param>
 *         </method-params>
 *         </method>
 *         
 *         and the create method that is defined in the local home
 *         interface which would be defined as
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-intf>LocalHome</method-intf>
 *         <method-name>create</method-name>
 *         <method-params>
 *         	  <method-param>java.lang.String</method-param>
 *         	  <method-param>java.lang.String</method-param>
 *         </method-params>
 *         </method>
 *         
 *         The method-intf element can be used with all three Styles
 *         of the method element usage. For example, the following
 *         method element example could be used to refer to all the
 *         methods of the EmployeeService bean's remote home interface
 *         and the remote business interface.
 *         
 *         <method>
 *         <ejb-name>EmployeeService</ejb-name>
 *         <method-intf>Home</method-intf>
 *         <method-name>*</method-name>
 *         </method>
 *         
 * ]]>
 *         @since Java EE 5, EJB 3.0
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.jst.javaee.ejb.MethodType#getDescriptions <em>Descriptions</em>}</li>
 *   <li>{@link org.eclipse.jst.javaee.ejb.MethodType#getEjbName <em>Ejb Name</em>}</li>
 *   <li>{@link org.eclipse.jst.javaee.ejb.MethodType#getMethodIntf <em>Method Intf</em>}</li>
 *   <li>{@link org.eclipse.jst.javaee.ejb.MethodType#getMethodName <em>Method Name</em>}</li>
 *   <li>{@link org.eclipse.jst.javaee.ejb.MethodType#getMethodParams <em>Method Params</em>}</li>
 *   <li>{@link org.eclipse.jst.javaee.ejb.MethodType#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.jst.javaee.ejb.internal.metadata.EjbPackage#getMethodType()
 * @extends JavaEEObject
 * @generated
 */
public interface MethodType extends JavaEEObject {
	/**
	 * Returns the value of the '<em><b>Descriptions</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.jst.javaee.core.Description}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Descriptions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Descriptions</em>' containment reference list.
	 * @see org.eclipse.jst.javaee.ejb.internal.metadata.EjbPackage#getMethodType_Descriptions()
	 * @generated
	 */
	List<Description> getDescriptions();

	/**
	 * Returns the value of the '<em><b>Ejb Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ejb Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ejb Name</em>' attribute.
	 * @see #setEjbName(String)
	 * @see org.eclipse.jst.javaee.ejb.internal.metadata.EjbPackage#getMethodType_EjbName()
	 * @generated
	 */
	String getEjbName();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.javaee.ejb.MethodType#getEjbName <em>Ejb Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ejb Name</em>' attribute.
	 * @see #getEjbName()
	 * @generated
	 */
	void setEjbName(String value);

	/**
	 * Returns the value of the '<em><b>Method Intf</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.jst.javaee.ejb.MethodInterfaceType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Method Intf</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Method Intf</em>' attribute.
	 * @see org.eclipse.jst.javaee.ejb.MethodInterfaceType
	 * @see #isSetMethodIntf()
	 * @see #unsetMethodIntf()
	 * @see #setMethodIntf(MethodInterfaceType)
	 * @see org.eclipse.jst.javaee.ejb.internal.metadata.EjbPackage#getMethodType_MethodIntf()
	 * @generated
	 */
	MethodInterfaceType getMethodIntf();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.javaee.ejb.MethodType#getMethodIntf <em>Method Intf</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Method Intf</em>' attribute.
	 * @see org.eclipse.jst.javaee.ejb.MethodInterfaceType
	 * @see #isSetMethodIntf()
	 * @see #unsetMethodIntf()
	 * @see #getMethodIntf()
	 * @generated
	 */
	void setMethodIntf(MethodInterfaceType value);

	/**
	 * Unsets the value of the '{@link org.eclipse.jst.javaee.ejb.MethodType#getMethodIntf <em>Method Intf</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMethodIntf()
	 * @see #getMethodIntf()
	 * @see #setMethodIntf(MethodInterfaceType)
	 * @generated
	 */
	void unsetMethodIntf();

	/**
	 * Returns whether the value of the '{@link org.eclipse.jst.javaee.ejb.MethodType#getMethodIntf <em>Method Intf</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Method Intf</em>' attribute is set.
	 * @see #unsetMethodIntf()
	 * @see #getMethodIntf()
	 * @see #setMethodIntf(MethodInterfaceType)
	 * @generated
	 */
	boolean isSetMethodIntf();

	/**
	 * Returns the value of the '<em><b>Method Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Method Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Method Name</em>' attribute.
	 * @see #setMethodName(String)
	 * @see org.eclipse.jst.javaee.ejb.internal.metadata.EjbPackage#getMethodType_MethodName()
	 * @generated
	 */
	String getMethodName();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.javaee.ejb.MethodType#getMethodName <em>Method Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Method Name</em>' attribute.
	 * @see #getMethodName()
	 * @generated
	 */
	void setMethodName(String value);

	/**
	 * Returns the value of the '<em><b>Method Params</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Method Params</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Method Params</em>' containment reference.
	 * @see #setMethodParams(MethodParams)
	 * @see org.eclipse.jst.javaee.ejb.internal.metadata.EjbPackage#getMethodType_MethodParams()
	 * @generated
	 */
	MethodParams getMethodParams();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.javaee.ejb.MethodType#getMethodParams <em>Method Params</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Method Params</em>' containment reference.
	 * @see #getMethodParams()
	 * @generated
	 */
	void setMethodParams(MethodParams value);

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
	 * @see org.eclipse.jst.javaee.ejb.internal.metadata.EjbPackage#getMethodType_Id()
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.javaee.ejb.MethodType#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

} // MethodType