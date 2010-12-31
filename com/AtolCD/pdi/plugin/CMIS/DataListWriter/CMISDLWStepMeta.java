/**
* CMISDLWStepMeta.java
*
* 
*
* Copyright (C) 2010 Atol Conseils et Développements,  3 bd eiffel 21600 Longvic
* See LICENSE file under this distribution for licensing information
* 
* @author vpl
* @version %I%, %G%
* @since 20100322 
**/

package com.AtolCD.pdi.plugin.CMIS.DataListWriter;

import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Session;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.*;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.*;
import org.pentaho.di.core.row.*;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.*;
import org.pentaho.di.trans.*;
import org.pentaho.di.trans.step.*;
import org.w3c.dom.Node;


public class CMISDLWStepMeta extends BaseStepMeta implements StepMetaInterface {

	private static Class<?> PKG = CMISDLWStepMeta.class; // for i18n purposes
	private String outputField;

	private String urlField;
	private String userField;
	private String passwordField;
	private String repoField;
	private String siteField;
	private String listField;

	private String listType;
	private String[] repositories;
	private int repoIndex;
	private String[] sites;
	private int siteIndex;
	private String[] lists;
	private int listIndex;
	private String repId;
	private String siteId;
	private String listId;

	private boolean firstConn;

	private String[] fieldOrder;

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public String[] getFieldOrder() {
		return fieldOrder;
	}

	public void setFieldOrder(String[] fieldOrder) {
		this.fieldOrder = fieldOrder;
	}

	public boolean isFirstConn() {
		return firstConn;
	}

	public void setFirstConn(boolean firstConn) {
		this.firstConn = firstConn;
	}

	private Session mainSession;

	public Session getMainSession() {
		return mainSession;
	}

	public void setMainSession(Session mainSession) {
		this.mainSession = mainSession;
	}

	public String[] getRepositories() {
		return repositories;
	}

	public void setRepositories(String[] repositories) {
		this.repositories = repositories;
	}

	public int getRepoIndex() {
		return repoIndex;
	}

	public void setRepoIndex(int repoIndex) {
		this.repoIndex = repoIndex;
	}

	public String[] getSites() {
		return sites;
	}

	public void setSites(String[] sites) {
		this.sites = sites;
	}

	public int getSiteIndex() {
		return siteIndex;
	}

	public void setSiteIndex(int siteIndex) {
		this.siteIndex = siteIndex;
	}

	public String[] getLists() {
		return lists;
	}

	public void setLists(String[] lists) {
		this.lists = lists;
	}

	public int getListIndex() {
		return listIndex;
	}

	public void setListIndex(int listIndex) {
		this.listIndex = listIndex;
	}

	public String getRepId() {
		return repId;
	}

	public void setRepId(String repId) {
		this.repId = repId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getUrlField() {
		return urlField;
	}

	public void setUrlField(String urlField) {
		this.urlField = urlField;
	}

	public String getUserField() {
		return userField;
	}

	public void setUserField(String userField) {
		this.userField = userField;
	}

	public String getPasswordField() {
		return passwordField;
	}

	public void setPasswordField(String passwordField) {
		this.passwordField = passwordField;
	}

	public String getRepoField() {
		return repoField;
	}

	public void setRepoField(String repoField) {
		this.repoField = repoField;
	}

	public String getSiteField() {
		return siteField;
	}

	public void setSiteField(String siteField) {
		this.siteField = siteField;
	}

	// private list type

	public String getListField() {
		return listField;
	}

	public void setListField(String listField) {
		this.listField = listField;
	}

	private boolean create;

	public boolean isCreate() {
		return create;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}

	public CMISDLWStepMeta() {
		super();
	}

	public String getOutputField() {
		return outputField;
	}

	// unused
	public void setOutputField(String outputField) {
		this.outputField = outputField;
	}

	// Serialisation method
	public String getXML() throws KettleValueException {

		StringBuffer retval = new StringBuffer(800);

		retval.append("    ").append(
				XMLHandler.addTagValue("outputfield", getOutputField()));
		retval.append("    ").append(
				XMLHandler.addTagValue("repId", getRepId()));
		retval.append("    ").append(
				XMLHandler.addTagValue("listId", getListId()));
		retval.append("    ").append(
				XMLHandler.addTagValue("urlField", getUrlField()));
		retval.append("    ").append(
				XMLHandler.addTagValue("userField", getUserField()));
		retval.append("    ").append(
				XMLHandler.addTagValue("passwordField", getPasswordField()));
		retval.append("    ").append(
				XMLHandler.addTagValue("listType", getListType()));
		// retval.append("    ").append(XMLHandler.addTagValue());
		if (fieldOrder == null) {
			fieldOrder = new String[] { " " };
		}
		for (int i = 0; i < fieldOrder.length; i++) {
			retval.append("      <field>").append(Const.CR); //$NON-NLS-1$
			retval
					.append("        ").append(XMLHandler.addTagValue("order", i)); //$NON-NLS-1$ //$NON-NLS-2$
			retval
					.append("        ").append(XMLHandler.addTagValue("name", fieldOrder[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval.append("      </field>").append(Const.CR); //$NON-NLS-1$
		}

		//ajouter les 3 tableaux
		
		 for (int i = 0; i < repositories.length; i++) {
			retval.append("      <repos>").append(Const.CR);
			retval
					.append("        ").append(XMLHandler.addTagValue("order", i));
			retval
					.append("        ").append(XMLHandler.addTagValue("name", repositories[i]));
			retval.append("      </repos>").append(Const.CR);
		}

		 for (int i = 0; i < sites.length; i++) {
			retval.append("      <sites>").append(Const.CR);
			retval
					.append("        ").append(XMLHandler.addTagValue("order", i)); 
			retval
					.append("        ").append(XMLHandler.addTagValue("name", sites[i])); 
			retval.append("      </sites>").append(Const.CR);
		}

		 for (int i = 0; i < lists.length; i++) {
			retval.append("      <lists>").append(Const.CR); 
			retval
					.append("        ").append(XMLHandler.addTagValue("order", i)); 
			retval
					.append("        ").append(XMLHandler.addTagValue("name", lists[i])); 
			retval.append("      </lists>").append(Const.CR); 
		}
		 
		retval.append(Const.CR);
		return retval.toString();
	}

	public void getFields(RowMetaInterface r, String origin,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) {

		// append the outputField to the output
		ValueMetaInterface v = new ValueMeta();
		v.setName(outputField);
		v.setType(ValueMeta.TYPE_STRING);
		v.setTrimType(ValueMeta.TRIM_TYPE_BOTH);
		v.setOrigin(origin);

		r.addValueMeta(v);
	}

	public Object clone() {
		Object retval = super.clone();
		return retval;
	}

	// unserialisation
	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {

		try {
			setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(
					stepnode, "outputfield")));
			setRepId(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode,
					"repId")));
			setListId(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode,
					"listId")));
			setUrlField(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode,
					"urlField")));
			setUserField(XMLHandler.getNodeValue(XMLHandler.getSubNode(
					stepnode, "userField")));
			setPasswordField(XMLHandler.getNodeValue(XMLHandler.getSubNode(
					stepnode, "passwordField")));
			setListType(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode,
					"listType")));
			int k = XMLHandler.countNodes(stepnode, "field");

			String fields[] = new String[k];

			for (int i = 0; i < k; i++) {
				fields[i] = XMLHandler.getNodeValue(XMLHandler
						.getSubNode(XMLHandler.getSubNodeByNr(stepnode,
								"field", i), "name"));
			}


			k = XMLHandler.countNodes(stepnode, "repo");

			String repositories[] = new String[k]; 
			 
			 for (int i = 0; i < k; i++) {
				repositories[i] = XMLHandler.getNodeValue(XMLHandler
						.getSubNode(XMLHandler.getSubNodeByNr(stepnode,
								"repo", i), "name"));
			}
			
			k = XMLHandler.countNodes(stepnode, "sites");

			String sites[] = new String[k];
			
			for (int i = 0; i < k; i++) {
				sites[i] = XMLHandler.getNodeValue(XMLHandler
						.getSubNode(XMLHandler.getSubNodeByNr(stepnode,
								"sites", i), "name"));
			}
			
			k = XMLHandler.countNodes(stepnode, "lists");

			String lists[] = new String[k];
			
			for (int i = 0; i < k; i++) {
				lists[i] = XMLHandler.getNodeValue(XMLHandler
						.getSubNode(XMLHandler.getSubNodeByNr(stepnode,
								"lists", i), "name"));
			}
			
			 
			
			setFieldOrder(fields);
			setRepositories(repositories);
			setSites(sites);
			setLists(lists);
			
		} catch (Exception e) {
			throw new KettleXMLException(
					"Template Plugin Unable to read step info from XML node", e);
		}

	}

	public void setDefault() {
		outputField = "template_outfield";
		urlField = BaseMessages.getString(PKG, "Template.FieldName.URLBase");
		userField = "user";
		passwordField = "password";

		repositories = new String[] { " " };
		sites = new String[] { " " };
		lists = new String[] { " " };

		repoIndex = 0;
		siteIndex = 0;
		listIndex = 0;

		repId = "idnull1";
		siteId = "idnull2";
		listId = "idnull3";

		firstConn = true;
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transmeta,
			StepMeta stepMeta, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {

		CheckResult cr;

		// See if we have input streams leading to this step!
		if (input.length > 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK,
					"Step is receiving info from other steps.", stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR,
					"No input received from other steps!", stepMeta);
			remarks.add(cr);
		}

	}

	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta,
			TransMeta transMeta, String name) {
		return new CMISDLWStepDialog(shell, meta, transMeta, name);
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans disp) {
		return new CMISDLWStep(stepMeta, stepDataInterface, cnr, transMeta,
				disp);
	}

	public StepDataInterface getStepData() {
		return new CMISDLWStepData();
	}

	public void readRep(Repository rep, ObjectId id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			outputField = rep.getStepAttributeString(id_step, "outputfield"); //$NON-NLS-1$
			repId = rep.getStepAttributeString(id_step, "repId"); //$NON-NLS-1$
			repId = rep.getStepAttributeString(id_step, "listId");
			repId = rep.getStepAttributeString(id_step, "urlField");
			repId = rep.getStepAttributeString(id_step, "userField");
			repId = rep.getStepAttributeString(id_step, "passwordField");
			repId = rep.getStepAttributeString(id_step, "listType");
			repId = rep.getStepAttributeString(id_step, "urlField");
			int nrFields = rep.countNrStepAttributes(id_step, "order");

			String fields[] = new String[nrFields];

			for (int i = 0; i < nrFields; i++) {
				fields[i] = rep.getStepAttributeString(id_step, i, "name");
			}

			setFieldOrder(fields);

		} catch (Exception e) {
			throw new KettleException(BaseMessages.getString(PKG,
					"TemplateStep.Exception.UnexpectedErrorInReadingStepInfo"),
					e);
		}
	}

	public void saveRep(Repository rep, ObjectId id_transformation,
			ObjectId id_step) throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step,
					"outputfield", outputField); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "repId", repId); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "listId", listId);
			rep.saveStepAttribute(id_transformation, id_step, "urlField",
					urlField);
			rep.saveStepAttribute(id_transformation, id_step, "userField",
					userField);
			rep.saveStepAttribute(id_transformation, id_step, "passwordField",
					passwordField);
			rep.saveStepAttribute(id_transformation, id_step, "listType",
					listType);
			rep.saveStepAttribute(id_transformation, id_step, "urlField",
					urlField);
			if (fieldOrder == null) {
				fieldOrder = new String[] { " " };
			}
			for (int i = 0; i < fieldOrder.length; i++) {
				rep
						.saveStepAttribute(id_transformation, id_step, i,
								"order", i);
				rep.saveStepAttribute(id_transformation, id_step, i, "name",
						fieldOrder[i]);
			}

		} catch (Exception e) {
			throw new KettleException(BaseMessages.getString(PKG,
					"TemplateStep.Exception.UnableToSaveStepInfoToRepository")
					+ id_step, e);
		}
	}
}
