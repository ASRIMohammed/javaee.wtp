/*
 * Created on Feb 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.jst.j2ee.internal.webservice;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.internal.actions.OpenJ2EEResourceAction;
import org.eclipse.jst.j2ee.internal.webservice.helper.WebServicesManager;
import org.eclipse.jst.j2ee.internal.webservices.WSDLServiceExtManager;
import org.eclipse.jst.j2ee.internal.webservices.WSDLServiceHelper;
import org.eclipse.jst.j2ee.webservice.wsdd.WsddResource;
import org.eclipse.wst.common.internal.emfworkbench.WorkbenchResourceHelper;
import org.eclipse.wst.wsdl.internal.impl.ServiceImpl;

/**
 * @author jlanuti
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WebServicesNavigatorGroupOpenListener implements IOpenListener {

	private OpenExternalWSDLAction openExternalWSDLAction = new OpenExternalWSDLAction(WebServiceUIResourceHandler.getString("WebServiceGroupContentExtension_UI_1")); //$NON-NLS-1$
	private OpenJ2EEResourceAction openAction = new OpenJ2EEResourceAction();
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IOpenListener#open(org.eclipse.jface.viewers.OpenEvent)
	 */
	public void open(OpenEvent event) {
		if (event == null)
			return;
		StructuredSelection selection = (StructuredSelection)event.getSelection();
		WSDLServiceHelper serviceHelper = WSDLServiceExtManager.getServiceHelper();
		if (selection == null || selection.getFirstElement()==null)
			return;
		Object selectedObject = selection.getFirstElement();
		if (serviceHelper==null)
			return;
		else if (serviceHelper.isWSDLResource(selectedObject)) {
			Resource wsdl = (Resource) selectedObject;
			IFile wsdlFile = WorkbenchResourceHelper.getFile(wsdl);
			if (wsdlFile == null || !wsdlFile.exists()) {
				openExternalWSDLAction.selectionChanged(selection);
				openExternalWSDLAction.run();
				return;
			}
			openAction.selectionChanged(selection);
			openAction.run();
		}
		else if (selectedObject instanceof ServiceImpl) {
			WsddResource resource = WebServicesManager.getInstance().getWsddResource((ServiceImpl)selectedObject);
			List wsddSelection = new ArrayList();
			wsddSelection.add(resource);
			
			openAction.selectionChanged(new StructuredSelection(wsddSelection));
			openAction.run();
		}
		else {
			openAction.selectionChanged(selection);
			openAction.run();
		}	
	}
	//TODO fill open with menu for web services group
	
//	return new CommonEditActionGroup(getExtensionSite()) {
//		public void fillOpenWithMenu(IMenuManager menu) {
//			if ((getExtensionSite().getSelection().getFirstElement() instanceof BeanLink)) {
//				openAction.selectionChanged(getExtensionSite().getSelection());
//				menu.insertAfter(ICommonMenuConstants.COMMON_MENU_TOP, openAction);
//			} else if ((getExtensionSite().getSelection().getFirstElement() instanceof WSDLResourceImpl)) {
//				WSDLResourceImpl wsdl = (WSDLResourceImpl) getExtensionSite().getSelection().getFirstElement();
//				IFile wsdlFile = WorkbenchResourceHelper.getFile(wsdl);
//				if (wsdlFile == null || !wsdlFile.exists()) {
//					openExternalWSDLAction.selectionChanged(getExtensionSite().getSelection());
//					menu.insertAfter(ICommonMenuConstants.COMMON_MENU_TOP, openExternalWSDLAction);
//				}
//			}
//		}

}
