/*
 * Created on Mar 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.jst.j2ee.internal.wizard;

import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jst.j2ee.application.operations.EARComponentCreationOperation;
import org.eclipse.jst.j2ee.application.operations.J2EEModuleCreationDataModelOld;
import org.eclipse.jst.j2ee.internal.earcreation.EARComponentCreationDataModel;
import org.eclipse.jst.j2ee.internal.plugin.J2EEUIMessages;
import org.eclipse.jst.j2ee.internal.plugin.J2EEUIPlugin;
import org.eclipse.jst.j2ee.internal.plugin.J2EEUIPluginIcons;
import org.eclipse.ui.INewWizard;
import org.eclipse.wst.common.frameworks.operations.WTPOperation;
import org.eclipse.wst.common.frameworks.operations.WTPOperationDataModel;

public class EARComponentCreationWizard extends J2EEComponentCreationWizard implements IExecutableExtension, INewWizard {
	
	/**
	 * <p>
	 * The Wizard ID of the EARComponentCreationWizard. Used for internal purposes and activities management.
	 * </p>
	 */
	public static final String WIZARD_ID =  EARComponentCreationWizard.class.getName();
	protected static final String SECOND_PG = "second"; //$NON-NLS-1$
	 
	
	/**
	 * <p>
	 * The default constructor. Creates a wizard with no selection, 
	 * no model instance, and no operation instance. The model and 
	 * operation will be created as needed.
	 * </p>
	 */
	public EARComponentCreationWizard() {
		super();
	}

	/**
	 * <p>
	 * The model is used to prepopulate the wizard controls
	 * and interface with the operation.
	 * </p>
	 * @param model The model parameter is used to pre-populate wizard controls and interface with the operation
	 */
	public EARComponentCreationWizard(EARComponentCreationDataModel model) {
		super(model);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Overridden to return a {@link WebProjectCreationDataModel} and defaults
	 * the value of {@link J2EEModuleCreationDataModelOld#ADD_TO_EAR} to <b>true</b>
	 * </p>
	 * 
	 * @return Returns the specific operation data model for the creation of J2EE Web modules
	 */
	protected WTPOperationDataModel createDefaultModel() {
		EARComponentCreationDataModel aModel = new EARComponentCreationDataModel();
		return aModel;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Overridden to return an {@link WebProjectCreationOperation}. 
	 * </p>
	 * 
	 * @return Returns the specific operation for the creation of J2EE Web modules
	 */
	protected WTPOperation createBaseOperation() {
		return new EARComponentCreationOperation(getSpecificDataModel());
	}

	/** 
	 * {@inheritDoc}   
	 * 
	 * <p>
	 * Sets up the dialog window title and default page image. 
	 * </p> 
	 * 
	 * @see J2EEArtifactCreationWizard#doInit()
	 */
	protected void doInit() {
		setWindowTitle(J2EEUIMessages.getResourceString(J2EEUIMessages.EAR_COMPONENT_WIZ_TITLE));
		setDefaultPageImageDescriptor(J2EEUIPlugin.getDefault().getImageDescriptor(J2EEUIPluginIcons.EAR_WIZ_BANNER));
	} 
	 
	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Adds a {@link JCAProjectCreationPage} as the {@link J2EEComponentCreationWizard#MAIN_PG}.
	 * </p>
	 */
	protected void doAddPages() {
		EARComponentCreationWizardPage page1 = new EARComponentCreationWizardPage(getSpecificDataModel(), MAIN_PG);
//		page.setInfopopID("org.eclipse.jst.j2ee.ui.webw1000"); //$NON-NLS-1$
		addPage(page1);
		EARComponentCreationSecondPage page2 = new EARComponentCreationSecondPage(getSpecificDataModel(), SECOND_PG);
		addPage(page2);
		super.doAddPages();
	} 
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.wst.common.frameworks.internal.ui.wizard.extensions.ExtendableWizard#getWizardID()
	 */
	public String getWizardID() {
		return WIZARD_ID;
	} 
	
	private EARComponentCreationDataModel getSpecificDataModel() {
		return (EARComponentCreationDataModel) getModel();
	}

}