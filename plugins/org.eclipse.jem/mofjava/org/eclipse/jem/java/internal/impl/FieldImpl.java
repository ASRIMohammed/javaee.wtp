/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*


 */
package org.eclipse.jem.java.internal.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.ETypedElementImpl;
import org.eclipse.emf.ecore.util.ECrossReferenceEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.java.adapters.ReadAdaptor;
import org.eclipse.jem.internal.java.adapters.ReflectionAdaptor;
import org.eclipse.jem.java.*;

/**
 * @generated
 */
public class FieldImpl extends ETypedElementImpl implements Field {

	/**
	 * The default value of the '{@link #isFinal() <em>Final</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFinal()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FINAL_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isFinal() <em>Final</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFinal()
	 * @generated
	 * @ordered
	 */
	protected static final int FINAL_EFLAG = 1 << 10;

	/**
	 * The default value of the '{@link #isStatic() <em>Static</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isStatic()
	 * @generated
	 * @ordered
	 */
	protected static final boolean STATIC_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isStatic() <em>Static</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isStatic()
	 * @generated
	 * @ordered
	 */
	protected static final int STATIC_EFLAG = 1 << 11;

	/**
	 * The default value of the '{@link #getJavaVisibility() <em>Java Visibility</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJavaVisibility()
	 * @generated
	 * @ordered
	 */
	protected static final JavaVisibilityKind JAVA_VISIBILITY_EDEFAULT = JavaVisibilityKind.PUBLIC_LITERAL;

	/**
	 * @generated This field/method will be replaced during code generation.
	 */
	protected JavaVisibilityKind javaVisibility = JAVA_VISIBILITY_EDEFAULT;

	/**
	 * The default value of the '{@link #isTransient() <em>Transient</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTransient()
	 * @generated
	 * @ordered
	 */
	protected static final boolean TRANSIENT_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isTransient() <em>Transient</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTransient()
	 * @generated
	 * @ordered
	 */
	protected static final int TRANSIENT_EFLAG = 1 << 12;

	/**
	 * The default value of the '{@link #isVolatile() <em>Volatile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVolatile()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VOLATILE_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isVolatile() <em>Volatile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVolatile()
	 * @generated
	 * @ordered
	 */
	protected static final int VOLATILE_EFLAG = 1 << 13;

	/**
	 * The cached value of the '{@link #getInitializer() <em>Initializer</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitializer()
	 * @generated
	 * @ordered
	 */
	protected Block initializer = null;

	protected FieldImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JavaRefPackage.Literals.FIELD;
	}

	/**
	 * createFieldRef - return a JavaURL reference to the named field in the named Java class in the form "package.class_field"
	 * @deprecated no replacement at this time.
	 */
	public static Field createFieldRef(String className, String fieldName) {
		Field ref = JavaRefFactory.eINSTANCE.createField();
		URI classURI = JavaRefFactory.eINSTANCE.createTypeURI(className);
		((InternalEObject) ref).eSetProxyURI(URI.createURI(classURI.toString()+ReflectionAdaptor.C_CLASS_MEMBER_DELIMITER+fieldName));
		return ref;
	}

	/**
	 * Get the class that this field is within.
	 */
	public JavaClass getContainingJavaClass() {
		return this.getJavaClass();
	}

	/**
	 * Overrides to perform lazy initializations/reflection.
	 */
	public EClassifier getEType() {
		reflectValues();
		return super.getEType();
	}

	public Block getInitializer() {
		reflectValues();
		return getInitializerGen();
	}

	public boolean isFinal() {
		reflectValues();
		return isFinalGen();
	}

	public boolean isStatic() {
		reflectValues();
		return isStaticGen();
	}

	public boolean isTransient() {
		reflectValues();
		return isTransientGen();
	}
	
	public boolean isVolatile() {
		reflectValues();
		return isVolatileGen();
	}	

	public JavaHelpers getJavaType() {
		return (JavaHelpers) getEType();
	}

	public JavaVisibilityKind getJavaVisibility() {
		reflectValues();
		return getJavaVisibilityGen();
	}

	protected synchronized ReadAdaptor getReadAdapter() {
		return (ReadAdaptor) EcoreUtil.getRegisteredAdapter(this, ReadAdaptor.TYPE_KEY);
	}

	protected boolean hasReflected = false;

	protected void reflectValues() {
		// We only want the testing of the hasReflected and get readadapter to be sync(this) so that
		// it is short and no deadlock possibility (this is because the the method reflection adapter may go
		// back to the containing java class to get its reflection adapter, which would lock on itself. So
		// we need to keep the sections that are sync(this) to not be deadlockable by not doing significant work
		// during the sync.
		ReadAdaptor readAdaptor = null;
		synchronized (this) {
			if (!hasReflected) {
				readAdaptor = getReadAdapter();
			}
		}
		if (readAdaptor != null) {
			boolean setReflected = readAdaptor.reflectValuesIfNecessary();
			synchronized (this) {
				// Don't want to set it false. That is job of reflection adapter. Otherwise we could have a race.
				if (setReflected)
					hasReflected = setReflected;
			}
		}
	}

	/*
	 * Used by reflection adapter to clear the reflection. This not intended to be used by others.
	 */
	public synchronized void setReflected(boolean reflected) {
		hasReflected = reflected;
	}

	/**
	 * Is this field an array type.
	 */
	public boolean isArray() {
		return getJavaType().isArray();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case JavaRefPackage.FIELD__JAVA_CLASS:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetJavaClass((JavaClass)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case JavaRefPackage.FIELD__JAVA_CLASS:
				return basicSetJavaClass(null, msgs);
			case JavaRefPackage.FIELD__INITIALIZER:
				return basicSetInitializer(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case JavaRefPackage.FIELD__JAVA_CLASS:
				return eInternalContainer().eInverseRemove(this, JavaRefPackage.JAVA_CLASS__FIELDS, JavaClass.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case JavaRefPackage.FIELD__FINAL:
				return isFinal() ? Boolean.TRUE : Boolean.FALSE;
			case JavaRefPackage.FIELD__STATIC:
				return isStatic() ? Boolean.TRUE : Boolean.FALSE;
			case JavaRefPackage.FIELD__JAVA_VISIBILITY:
				return getJavaVisibility();
			case JavaRefPackage.FIELD__TRANSIENT:
				return isTransient() ? Boolean.TRUE : Boolean.FALSE;
			case JavaRefPackage.FIELD__VOLATILE:
				return isVolatile() ? Boolean.TRUE : Boolean.FALSE;
			case JavaRefPackage.FIELD__JAVA_CLASS:
				return getJavaClass();
			case JavaRefPackage.FIELD__INITIALIZER:
				return getInitializer();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case JavaRefPackage.FIELD__FINAL:
				setFinal(((Boolean)newValue).booleanValue());
				return;
			case JavaRefPackage.FIELD__STATIC:
				setStatic(((Boolean)newValue).booleanValue());
				return;
			case JavaRefPackage.FIELD__JAVA_VISIBILITY:
				setJavaVisibility((JavaVisibilityKind)newValue);
				return;
			case JavaRefPackage.FIELD__TRANSIENT:
				setTransient(((Boolean)newValue).booleanValue());
				return;
			case JavaRefPackage.FIELD__VOLATILE:
				setVolatile(((Boolean)newValue).booleanValue());
				return;
			case JavaRefPackage.FIELD__JAVA_CLASS:
				setJavaClass((JavaClass)newValue);
				return;
			case JavaRefPackage.FIELD__INITIALIZER:
				setInitializer((Block)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case JavaRefPackage.FIELD__FINAL:
				setFinal(FINAL_EDEFAULT);
				return;
			case JavaRefPackage.FIELD__STATIC:
				setStatic(STATIC_EDEFAULT);
				return;
			case JavaRefPackage.FIELD__JAVA_VISIBILITY:
				setJavaVisibility(JAVA_VISIBILITY_EDEFAULT);
				return;
			case JavaRefPackage.FIELD__TRANSIENT:
				setTransient(TRANSIENT_EDEFAULT);
				return;
			case JavaRefPackage.FIELD__VOLATILE:
				setVolatile(VOLATILE_EDEFAULT);
				return;
			case JavaRefPackage.FIELD__JAVA_CLASS:
				setJavaClass((JavaClass)null);
				return;
			case JavaRefPackage.FIELD__INITIALIZER:
				setInitializer((Block)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case JavaRefPackage.FIELD__FINAL:
				return ((eFlags & FINAL_EFLAG) != 0) != FINAL_EDEFAULT;
			case JavaRefPackage.FIELD__STATIC:
				return ((eFlags & STATIC_EFLAG) != 0) != STATIC_EDEFAULT;
			case JavaRefPackage.FIELD__JAVA_VISIBILITY:
				return javaVisibility != JAVA_VISIBILITY_EDEFAULT;
			case JavaRefPackage.FIELD__TRANSIENT:
				return ((eFlags & TRANSIENT_EFLAG) != 0) != TRANSIENT_EDEFAULT;
			case JavaRefPackage.FIELD__VOLATILE:
				return ((eFlags & VOLATILE_EFLAG) != 0) != VOLATILE_EDEFAULT;
			case JavaRefPackage.FIELD__JAVA_CLASS:
				return getJavaClass() != null;
			case JavaRefPackage.FIELD__INITIALIZER:
				return initializer != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * Overridden to prevent the reflection of the class.
	 */
	public EList eContents() {
		EList results = new BasicEList();
		//FB
		//FB EList containments = eClass().getEAllContainments();
		//FB if (containments != null) {
		//FB Iterator i = containments.iterator();
		//FB while (i.hasNext()) {
		//FB EStructuralFeature sf = (EStructuralFeature) i.next();
		//FB //Change from super to primRefValue
		//FB Object value = primRefValue(sf);
		//FB //EndChange
		//FB if (value != null)
		//FB if (sf.isMany())
		//FB results.addAll((Collection) value);
		//FB else
		//FB results.add(value);
		//FB }
		//FB }
		if (getInitializerGen() != null)
			results.add(getInitializerGen()); //FB
		return results;
	}

	public String toString() {
		return getClass().getName() + " " + "(" + getName() + ")";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JavaVisibilityKind getJavaVisibilityGen() {
		return javaVisibility;
	}

	/**
	 * @generated This field/method will be replaced during code generation 
	 */
	public boolean isFinalGen() {
		return (eFlags & FINAL_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFinal(boolean newFinal) {
		boolean oldFinal = (eFlags & FINAL_EFLAG) != 0;
		if (newFinal) eFlags |= FINAL_EFLAG; else eFlags &= ~FINAL_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JavaRefPackage.FIELD__FINAL, oldFinal, newFinal));
	}

	/**
	 * @generated This field/method will be replaced during code generation 
	 */
	public boolean isStaticGen() {
		return (eFlags & STATIC_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStatic(boolean newStatic) {
		boolean oldStatic = (eFlags & STATIC_EFLAG) != 0;
		if (newStatic) eFlags |= STATIC_EFLAG; else eFlags &= ~STATIC_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JavaRefPackage.FIELD__STATIC, oldStatic, newStatic));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setJavaVisibility(JavaVisibilityKind newJavaVisibility) {
		JavaVisibilityKind oldJavaVisibility = javaVisibility;
		javaVisibility = newJavaVisibility == null ? JAVA_VISIBILITY_EDEFAULT : newJavaVisibility;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JavaRefPackage.FIELD__JAVA_VISIBILITY, oldJavaVisibility, javaVisibility));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isTransientGen() {
		return (eFlags & TRANSIENT_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTransient(boolean newTransient) {
		boolean oldTransient = (eFlags & TRANSIENT_EFLAG) != 0;
		if (newTransient) eFlags |= TRANSIENT_EFLAG; else eFlags &= ~TRANSIENT_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JavaRefPackage.FIELD__TRANSIENT, oldTransient, newTransient));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isVolatileGen() {
		return (eFlags & VOLATILE_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolatile(boolean newVolatile) {
		boolean oldVolatile = (eFlags & VOLATILE_EFLAG) != 0;
		if (newVolatile) eFlags |= VOLATILE_EFLAG; else eFlags &= ~VOLATILE_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JavaRefPackage.FIELD__VOLATILE, oldVolatile, newVolatile));
	}

	/**
	 * @generated This field/method will be replaced during code generation 
	 */
	public JavaClass getJavaClass() {
		if (eContainerFeatureID() != JavaRefPackage.FIELD__JAVA_CLASS) return null;
		return (JavaClass)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetJavaClass(JavaClass newJavaClass, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newJavaClass, JavaRefPackage.FIELD__JAVA_CLASS, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setJavaClass(JavaClass newJavaClass) {
		if (newJavaClass != eInternalContainer() || (eContainerFeatureID() != JavaRefPackage.FIELD__JAVA_CLASS && newJavaClass != null)) {
			if (EcoreUtil.isAncestor(this, newJavaClass))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newJavaClass != null)
				msgs = ((InternalEObject)newJavaClass).eInverseAdd(this, JavaRefPackage.JAVA_CLASS__FIELDS, JavaClass.class, msgs);
			msgs = basicSetJavaClass(newJavaClass, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JavaRefPackage.FIELD__JAVA_CLASS, newJavaClass, newJavaClass));
	}

	/**
	 * @generated This field/method will be replaced during code generation 
	 */
	public Block getInitializerGen() {
		return initializer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInitializer(Block newInitializer, NotificationChain msgs) {
		Block oldInitializer = initializer;
		initializer = newInitializer;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JavaRefPackage.FIELD__INITIALIZER, oldInitializer, newInitializer);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInitializer(Block newInitializer) {
		if (newInitializer != initializer) {
			NotificationChain msgs = null;
			if (initializer != null)
				msgs = ((InternalEObject)initializer).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JavaRefPackage.FIELD__INITIALIZER, null, msgs);
			if (newInitializer != null)
				msgs = ((InternalEObject)newInitializer).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JavaRefPackage.FIELD__INITIALIZER, null, msgs);
			msgs = basicSetInitializer(newInitializer, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JavaRefPackage.FIELD__INITIALIZER, newInitializer, newInitializer));
	}

	/**
	 * @generated This field/method will be replaced during code generation.
	 */
	public String toStringGen() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (final: ");
		result.append((eFlags & FINAL_EFLAG) != 0);
		result.append(", static: ");
		result.append((eFlags & STATIC_EFLAG) != 0);
		result.append(", javaVisibility: ");
		result.append(javaVisibility);
		result.append(", transient: ");
		result.append((eFlags & TRANSIENT_EFLAG) != 0);
		result.append(", volatile: ");
		result.append((eFlags & VOLATILE_EFLAG) != 0);
		result.append(')');
		return result.toString();
	}

	  @Override
	  public EList<EObject> eCrossReferences()
	  {
	    return ECrossReferenceEList.createECrossReferenceEList(this);
	  }

}

