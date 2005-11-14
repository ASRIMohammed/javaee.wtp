package org.eclipse.jst.j2ee.jca.ui.internal.wizard;

import org.eclipse.jst.j2ee.internal.plugin.J2EEUIMessages;
import org.eclipse.jst.j2ee.internal.wizard.J2EEModuleFacetInstallPage;
import org.eclipse.jst.j2ee.jca.project.facet.IConnectorFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.jca.ui.internal.util.JCAUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;

public class ConnectorFacetInstallPage extends J2EEModuleFacetInstallPage implements IConnectorFacetInstallDataModelProperties {

	private Label configFolderLabel;
	private Text configFolder;
	
	public ConnectorFacetInstallPage() {
		super(IModuleConstants.JST_CONNECTOR_MODULE);
		setTitle(JCAUIMessages.JCA_MODULE_MAIN_PG_TITLE);
		setDescription(JCAUIMessages.JCA_FACET_CONFIGURATION);
	}

	protected String[] getValidationPropertyNames() {
		return new String[]{EAR_PROJECT_NAME, CONFIG_FOLDER};
	}

	protected Composite createTopLevelComposite(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		setupEarControl(composite);
		
		configFolderLabel = new Label(composite, SWT.NONE);
		configFolderLabel.setText(J2EEUIMessages.getResourceString(J2EEUIMessages.SOURCEFOLDER));
		configFolderLabel.setLayoutData(gdhfill());

		configFolder = new Text(composite, SWT.BORDER);
		configFolder.setLayoutData(gdhfill());
		configFolder.setData("label", configFolderLabel); //$NON-NLS-1$
		synchHelper.synchText(configFolder, CONFIG_FOLDER, null);
		
		return composite;
	}

}