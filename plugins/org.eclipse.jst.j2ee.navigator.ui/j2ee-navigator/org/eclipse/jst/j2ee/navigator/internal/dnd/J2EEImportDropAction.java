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
/*
 * Created on May 6, 2004
 * 
 */
package org.eclipse.jst.j2ee.navigator.internal.dnd;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jst.j2ee.application.operations.EnterpriseApplicationImportDataModel;
import org.eclipse.jst.j2ee.application.operations.J2EEArtifactImportDataModel;
import org.eclipse.jst.j2ee.application.operations.J2EEModuleImportDataModel;
import org.eclipse.jst.j2ee.applicationclient.creation.AppClientModuleImportDataModel;
import org.eclipse.jst.j2ee.applicationclient.creation.IApplicationClientNatureConstants;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.commonarchivecore.internal.ModuleFile;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.OpenFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.ArchiveOptions;
import org.eclipse.jst.j2ee.commonarchivecore.internal.impl.CommonarchiveFactoryImpl;
import org.eclipse.jst.j2ee.commonarchivecore.internal.util.ArchiveUtil;
import org.eclipse.jst.ejb.ui.EJBModuleImportWizard;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.earcreation.IEARNatureConstants;
import org.eclipse.jst.j2ee.internal.ejb.project.operations.EJBModuleImportDataModel;
import org.eclipse.jst.j2ee.internal.jca.operations.ConnectorModuleImportDataModel;
import org.eclipse.jst.j2ee.internal.project.IConnectorNatureConstants;
import org.eclipse.jst.j2ee.internal.project.IEJBNatureConstants;
import org.eclipse.jst.j2ee.internal.project.IWebNatureConstants;
import org.eclipse.jst.j2ee.internal.project.J2EENature;
import org.eclipse.jst.j2ee.internal.web.archive.operations.WebModuleImportDataModel;
import org.eclipse.jst.j2ee.internal.wizard.ImportUtil;
import org.eclipse.jst.j2ee.jca.ui.ConnectorModuleImportWizard;
import org.eclipse.jst.j2ee.ui.AppClientModuleImportWizard;
import org.eclipse.jst.j2ee.ui.EnterpriseApplicationImportWizard;
import org.eclipse.jst.servlet.ui.WebModuleImportWizard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.wst.common.frameworks.internal.AdaptabilityUtility;
import org.eclipse.wst.common.frameworks.internal.ui.WTPWizard;
import org.eclipse.wst.common.navigator.internal.views.dnd.IDropValidator;
import org.eclipse.wst.common.navigator.internal.views.dnd.NavigatorDropActionDelegate;
import org.eclipse.wst.common.navigator.internal.views.dnd.NavigatorDropAdapter;

/**
 * @author jsholl
 *  
 */
public class J2EEImportDropAction extends NavigatorDropActionDelegate implements IDropValidator {
	private ArchiveOptions readOnlyArchiveOptions;

	public J2EEImportDropAction() {
		readOnlyArchiveOptions = new ArchiveOptions();
		readOnlyArchiveOptions.setIsReadOnly(true);
	}

	private boolean fileExistsOnDisk(String fileName) {
		if (fileName != null && fileName.length() > 0) {
			java.io.File file = new java.io.File(fileName);
			return file.exists() && !file.isDirectory();
		}
		return false;
	}

	public boolean validateDrop(NavigatorDropAdapter dropAdapter, Object target, int operation, TransferData transferType) {
		if (FileTransfer.getInstance().isSupportedType(transferType)) {
			String[] sourceNames = (String[]) FileTransfer.getInstance().nativeToJava(transferType);
			if (sourceNames == null || sourceNames.length != 1) { //only handle one file for now
				return false;
			}
			String fileName = sourceNames[0];
			if (!(fileName.endsWith(".ear") || fileName.endsWith(".jar") || fileName.endsWith(".war") || fileName.endsWith(".rar"))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				return false;
			}
			if (!fileExistsOnDisk(fileName)) {
				return false;
			}
			int archiveType = ImportUtil.UNKNOWN;
			Archive archive = null;
			try {
				archive = CommonarchiveFactoryImpl.getActiveFactory().openArchive(fileName);
				archiveType = ImportUtil.getArchiveType(archive);
				String[] possibleNatures = null;
				switch (archiveType) {
					case ImportUtil.EARFILE :
						possibleNatures = new String[]{IEARNatureConstants.NATURE_ID};
						break;
					case ImportUtil.EJBJARFILE :
						possibleNatures = new String[]{IEARNatureConstants.NATURE_ID, IEJBNatureConstants.NATURE_ID};
						break;
					case ImportUtil.CLIENTJARFILE :
						possibleNatures = new String[]{IEARNatureConstants.NATURE_ID, IApplicationClientNatureConstants.NATURE_ID};
						break;
					case ImportUtil.WARFILE :
						possibleNatures = new String[]{IEARNatureConstants.NATURE_ID, IWebNatureConstants.J2EE_NATURE_ID};
						break;
					case ImportUtil.RARFILE :
						possibleNatures = new String[]{IEARNatureConstants.NATURE_ID, IConnectorNatureConstants.NATURE_ID};
						break;
					default :
						return false;
				}

				IProject project = (IProject) AdaptabilityUtility.getAdapter(target, IProject.class);
				if (null != project) {
					try {
						boolean foundNature = false;
						for (int i = 0; !foundNature && i < possibleNatures.length; i++) {
							if (project.hasNature(possibleNatures[i])) {
								foundNature = true;
								J2EENature nature = (J2EENature) project.getNature(possibleNatures[i]);
								int projectJ2EEVersion = nature.getJ2EEVersion();
								int archiveModuleVersion = ArchiveUtil.getFastSpecVersion((ModuleFile) archive);
								boolean fail = false;
								switch (archiveType) {
									case ImportUtil.EARFILE :
									case ImportUtil.CLIENTJARFILE :
										if (projectJ2EEVersion < archiveModuleVersion) {
											fail = true;
										}
										break;
									case ImportUtil.EJBJARFILE :
										if (projectJ2EEVersion == J2EEVersionConstants.J2EE_1_2_ID && archiveModuleVersion > J2EEVersionConstants.EJB_1_1_ID) {
											fail = true;
										} else if (projectJ2EEVersion == J2EEVersionConstants.J2EE_1_3_ID && archiveModuleVersion > J2EEVersionConstants.EJB_2_0_ID) {
											fail = true;
										}
										break;
									case ImportUtil.WARFILE :
										if (projectJ2EEVersion == J2EEVersionConstants.J2EE_1_2_ID && archiveModuleVersion > J2EEVersionConstants.WEB_2_2_ID) {
											fail = true;
										} else if (projectJ2EEVersion == J2EEVersionConstants.J2EE_1_3_ID && archiveModuleVersion > J2EEVersionConstants.WEB_2_3_ID) {
											fail = true;
										}
										break;
									case ImportUtil.RARFILE :
										if (projectJ2EEVersion == J2EEVersionConstants.J2EE_1_2_ID) {
											fail = true;
										} else if (projectJ2EEVersion == J2EEVersionConstants.J2EE_1_3_ID && archiveModuleVersion > J2EEVersionConstants.JCA_1_0_ID) {
											fail = true;
										}
										break;
								}
								if (fail) {
									return false;
								}
								return true;
							}
						}
						if (!foundNature) {
							return false;
						}
					} catch (CoreException e) {
						return false;
					}
				}

			} catch (OpenFailureException e) {
				return false;
			} finally {
				if (null != archive && archive.isOpen()) {
					archive.close();
				}
			}
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.navigator.internal.views.navigator.dnd.NavigatorDropActionDelegate#run(org.eclipse.wst.common.navigator.internal.views.navigator.dnd.NavigatorDropAdapter,
	 *      java.lang.Object, java.lang.Object)
	 */
	public boolean run(NavigatorDropAdapter dropAdapter, Object source, Object target) {
		TransferData currentTransfer = dropAdapter.getCurrentTransfer();
		if (FileTransfer.getInstance().isSupportedType(currentTransfer)) {
			final String[] fileNames = (String[]) source;
			final String fileName = fileNames[0];
			WTPWizard wizard = null;
			J2EEArtifactImportDataModel dataModel = null;

			int archiveType = ImportUtil.UNKNOWN;
			Archive archive = null;
			try {
				archive = CommonarchiveFactoryImpl.getActiveFactory().openArchive(fileName);
				archiveType = ImportUtil.getArchiveType(archive);
				switch (archiveType) {
					case ImportUtil.EARFILE :
						dataModel = new EnterpriseApplicationImportDataModel();
						break;
					case ImportUtil.EJBJARFILE :
						dataModel = new EJBModuleImportDataModel();
						break;
					case ImportUtil.CLIENTJARFILE :
						dataModel = new AppClientModuleImportDataModel();
						break;
					case ImportUtil.WARFILE :
						dataModel = new WebModuleImportDataModel();
						break;
					case ImportUtil.RARFILE :
						dataModel = new ConnectorModuleImportDataModel();
						break;
					default :
						return false;
				}
			} catch (OpenFailureException e) {
				return false;
			} finally {
				if (null != archive && archive.isOpen()) {
					archive.close();
				}
			}

			dataModel.setProperty(J2EEArtifactImportDataModel.FILE_NAME, fileName);

			IProject project = (IProject) AdaptabilityUtility.getAdapter(target, IProject.class);
			if (null != project) {
				try {
					if (archiveType == ImportUtil.EARFILE || !project.hasNature(IEARNatureConstants.NATURE_ID)) {
						dataModel.setProperty(J2EEArtifactImportDataModel.PROJECT_NAME, project.getName());
					} else {
						dataModel.setProperty(J2EEModuleImportDataModel.EAR_PROJECT, project.getName());
						dataModel.setBooleanProperty(J2EEModuleImportDataModel.ADD_TO_EAR, true);
					}
				} catch (CoreException e) {
					return false;
				}
			}
			switch (archiveType) {
				case ImportUtil.EARFILE :
					wizard = new EnterpriseApplicationImportWizard((EnterpriseApplicationImportDataModel) dataModel);
					break;
				case ImportUtil.EJBJARFILE :
					wizard = new EJBModuleImportWizard((EJBModuleImportDataModel) dataModel);
					break;
				case ImportUtil.CLIENTJARFILE :
					wizard = new AppClientModuleImportWizard((AppClientModuleImportDataModel) dataModel);
					break;
				case ImportUtil.WARFILE :
					wizard = new WebModuleImportWizard((WebModuleImportDataModel) dataModel);
					break;
				case ImportUtil.RARFILE :
					wizard = new ConnectorModuleImportWizard((ConnectorModuleImportDataModel) dataModel);
					break;
			}

			if (null != wizard) {
				WizardDialog dialog = new WizardDialog(getShell(), wizard);
				dialog.open();
				return true;
			}
		}
		return false;
	}
}