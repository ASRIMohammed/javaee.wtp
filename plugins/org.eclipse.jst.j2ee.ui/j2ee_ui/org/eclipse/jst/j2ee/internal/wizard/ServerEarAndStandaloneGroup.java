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
package org.eclipse.jst.j2ee.internal.wizard;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jst.j2ee.application.internal.operations.J2EEComponentCreationDataModel;
import org.eclipse.jst.j2ee.internal.earcreation.EARComponentCreationDataModel;
import org.eclipse.jst.j2ee.internal.plugin.J2EEUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wst.common.componentcore.internal.operation.ComponentCreationDataModel;
import org.eclipse.wst.common.frameworks.internal.ui.WTPDataModelSynchHelper;

/**
 * 
 */
public class ServerEarAndStandaloneGroup {
	
	private Button newEAR;
	private Combo earCombo;
	private Label earLabel;
	private Button addToEAR;
	private J2EEComponentCreationDataModel model;
	private WTPDataModelSynchHelper synchHelper;
	private Composite parentComposite;

	/**
	 *  
	 */
	public ServerEarAndStandaloneGroup(Composite parent, J2EEComponentCreationDataModel model) {
		this.model = model;
		this.parentComposite = parent;
		synchHelper = new WTPDataModelSynchHelper(model);
		buildComposites(parent);
	}

	/**
	 * 
	 * @param parent
	 */
	public void buildComposites(Composite parent) {
		createEarAndStandaloneComposite(parent);
	}

	/**
	 * @param parent
	 */
	protected void createEarAndStandaloneComposite(Composite parent) {

		if (model.getBooleanProperty(J2EEComponentCreationDataModel.UI_SHOW_EAR_SECTION)) {

			Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 3;
			separator.setLayoutData(gd);

			new Label(parent, SWT.NONE); //pad

			// Create Add to EAR checkbox
			addToEAR = new Button(parent, SWT.CHECK);
			addToEAR.setText(J2EEUIMessages.getResourceString(J2EEUIMessages.LINK_MODULETO_EAR_PROJECT));
			addToEAR.setSelection(true);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			addToEAR.setLayoutData(gd);
			synchHelper.synchCheckbox(addToEAR, J2EEComponentCreationDataModel.ADD_TO_EAR, null);
			addToEAR.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					handleAddToEarSelection();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					//do nothing
				}
			});
			// Create EAR Group
			earLabel = new Label(parent, SWT.NONE);
			earLabel.setText(J2EEUIMessages.getResourceString(J2EEUIMessages.EAR_PROJECT_FOR_MODULE_CREATION));

			earCombo = new Combo(parent, SWT.NONE);
			earCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			newEAR = new Button(parent, SWT.NONE);
			newEAR.setText(J2EEUIMessages.getResourceString(J2EEUIMessages.NEW_THREE_DOTS_W));
			newEAR.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			newEAR.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					handleNewEarSelected();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					//do nothing
				}

			});

			Control[] deps = new Control[]{earLabel, newEAR};
			synchHelper.synchCombo(earCombo, J2EEComponentCreationDataModel.EAR_MODULE_NAME, deps);
		}
	}

	/**
	 *  
	 */
	protected void handleAddToEarSelection() {
		boolean selection = addToEAR.getSelection();
		earLabel.setEnabled(selection);
		earCombo.setEnabled(selection);
		newEAR.setEnabled(selection);
	}

	/**
	 *  
	 */
	protected void handleNewEarSelected() {
		J2EEComponentCreationDataModel moduleModel = model;
		EARComponentCreationDataModel earModel = new EARComponentCreationDataModel();
		earModel.setIntProperty(J2EEComponentCreationDataModel.J2EE_VERSION, moduleModel.getJ2EEVersion());
		earModel.setProperty(ComponentCreationDataModel.COMPONENT_NAME, moduleModel.getProperty(J2EEComponentCreationDataModel.EAR_MODULE_NAME));
		EARComponentCreationWizard earWizard = new EARComponentCreationWizard(earModel);
		WizardDialog dialog = new WizardDialog(parentComposite.getShell(), earWizard);
		if (Window.OK == dialog.open()) {
			moduleModel.setProperty(J2EEComponentCreationDataModel.EAR_MODULE_NAME, earModel.getProperty(ComponentCreationDataModel.COMPONENT_NAME));
		}
	}

	public void dispose() {
		model.removeListener(synchHelper);
		model.dispose();
		synchHelper = null;
		model = null;
	}
}