/*
 * Created on Mar 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.jst.j2ee.application.operations;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jst.j2ee.internal.J2EEConstants;
import org.eclipse.jst.j2ee.internal.J2EEVersionUtil;
import org.eclipse.jst.j2ee.internal.earcreation.EARComponentCreationDataModel;
import org.eclipse.jst.j2ee.internal.modulecore.util.EARArtifactEdit;
import org.eclipse.wst.common.modulecore.ModuleCore;
import org.eclipse.wst.common.modulecore.WorkbenchComponent;
import org.eclipse.wst.common.modulecore.internal.operation.ComponentCreationDataModel;
import org.eclipse.wst.common.modulecore.resources.IVirtualContainer;
import org.eclipse.wst.common.modulecore.resources.IVirtualFolder;

public class EARComponentCreationOperation extends J2EEComponentCreationOperation {
	public EARComponentCreationOperation(EARComponentCreationDataModel dataModel) {
		super(dataModel);
	}

	public EARComponentCreationOperation() {
		super();
	}
    /* (non-Javadoc)
     * @see org.eclipse.jst.j2ee.application.operations.J2EEComponentCreationOperation#createAndLinkJ2EEComponents()
     */
    protected void createAndLinkJ2EEComponents() throws CoreException {
        IVirtualContainer component = ModuleCore.create(getProject(), getModuleDeployName());
        component.commit();
		//create and link META-INF folder
		IVirtualFolder metaInfFolder = component.getFolder(new Path("/" + J2EEConstants.META_INF)); //$NON-NLS-1$		
		metaInfFolder.createLink(new Path("/" + getModuleName() + "/" + J2EEConstants.META_INF), 0, null);
    }

	protected void createDeploymentDescriptor(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
		String moduleName = (String)operationDataModel.getProperty(EARComponentCreationDataModel.COMPONENT_NAME);
		IFolder moduleFolder = getProject().getFolder(moduleName);
		if (!moduleFolder.exists()) {
			moduleFolder.create(true, true, null);
		}
		
		//should cache wbmodule when created instead of  searching ?
        ModuleCore moduleCore = null;
        EARArtifactEdit edit = null;
        try {
        	EARComponentCreationDataModel dm = (EARComponentCreationDataModel)getOperationDataModel();
            moduleCore = ModuleCore.getModuleCoreForWrite(getProject());
            WorkbenchComponent earComp = moduleCore.findWorkbenchModuleByDeployName(
            		operationDataModel.getStringProperty(EARComponentCreationDataModel.COMPONENT_DEPLOY_NAME));
       		edit = EARArtifactEdit.getEARArtifactEditForWrite(earComp);
       		int versionId = ((ComponentCreationDataModel)getOperationDataModel()).getIntProperty(ComponentCreationDataModel.COMPONENT_VERSION);
       		edit.createModelRoot(versionId);
       		// set version to WorkbenchComponent
       		String versionText = J2EEVersionUtil.getJ2EETextVersion(versionId);
       		earComp.getComponentType().setVersion(versionText);
			// save
			moduleCore.saveIfNecessary(null); 
        } finally {
            if (null != moduleCore) {
                moduleCore.dispose();
            }
       		if (edit != null)
       			edit.dispose();
        }		
	}

	protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
		J2EEComponentCreationDataModel dataModel = (J2EEComponentCreationDataModel) operationDataModel;
		createAndLinkJ2EEComponents();
		if (dataModel.getBooleanProperty(J2EEComponentCreationDataModel.CREATE_DEFAULT_FILES)) {
			createDeploymentDescriptor(monitor);
		}
		addModulesToEAR(monitor);
	}
	
	private void addModulesToEAR(IProgressMonitor monitor) {
		try{
			AddComponentToEnterpriseApplicationDataModel dm = new AddComponentToEnterpriseApplicationDataModel();
			dm.setProperty(AddComponentToEnterpriseApplicationDataModel.PROJECT_NAME,
					getProject().getName());
			dm.setProperty(AddComponentToEnterpriseApplicationDataModel.EAR_MODULE_NAME, 
					getOperationDataModel().getProperty(EARComponentCreationDataModel.COMPONENT_DEPLOY_NAME));
			dm.setProperty(AddComponentToEnterpriseApplicationDataModel.MODULE_LIST,
					getOperationDataModel().getProperty(EARComponentCreationDataModel.J2EE_COMPONENT_LIST));
			AddComponentToEnterpriseApplicationOperation addModuleOp = (AddComponentToEnterpriseApplicationOperation)dm.getDefaultOperation();
			addModuleOp.execute(monitor);
		 } catch(Exception e) {
			 Logger.getLogger().log(e);
		 }
	}
    
	protected  void addResources(WorkbenchComponent component ){
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jst.j2ee.application.operations.J2EEComponentCreationOperation#getVersion()
	 */
	protected String getVersion() {
		int version = operationDataModel.getIntProperty(J2EEComponentCreationDataModel.COMPONENT_VERSION);
		return J2EEVersionUtil.getJ2EETextVersion(version);
	}
}