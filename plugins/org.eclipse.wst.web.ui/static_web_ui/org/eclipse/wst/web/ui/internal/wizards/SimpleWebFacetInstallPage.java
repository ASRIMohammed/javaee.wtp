package org.eclipse.wst.web.ui.internal.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.project.facet.ISimpleWebFacetInstallDataModelProperties;
import org.eclipse.wst.web.internal.ResourceHandler;

public class SimpleWebFacetInstallPage extends DataModelFacetInstallPage implements ISimpleWebFacetInstallDataModelProperties {

	private Label configFolderLabel;
	private Text configFolder;
	
	public SimpleWebFacetInstallPage() {
		super("simpleweb.facet.install.page"); //$NON-NLS-1$
		setTitle(ResourceHandler.StaticWebProjectWizardBasePage_Page_Title);
		setDescription(ResourceHandler.ConfigureSettings);
	}

	protected String[] getValidationPropertyNames() {
		return new String[]{CONTENT_DIR};
	}

	protected Composite createTopLevelComposite(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		configFolderLabel = new Label(composite, SWT.NONE);
		configFolderLabel.setText(ResourceHandler.StaticWebSettingsPropertiesPage_Web_Content_Label);
		configFolderLabel.setLayoutData(gdhfill());

		configFolder = new Text(composite, SWT.BORDER);
		configFolder.setLayoutData(gdhfill());
		configFolder.setData("label", configFolderLabel); //$NON-NLS-1$
		synchHelper.synchText(configFolder, CONTENT_DIR, null);
		
		return composite;
	}

}