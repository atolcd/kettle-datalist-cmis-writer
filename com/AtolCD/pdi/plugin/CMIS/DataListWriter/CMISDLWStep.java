/**
* CMISDLWStep.java
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.data.Properties;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.CmisExtensionElementImpl;
import org.apache.chemistry.opencmis.commons.spi.BindingsObjectFactory;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;


public class CMISDLWStep extends BaseStep implements StepInterface {

	private CMISDLWStepData data;
	private CMISDLWStepMeta meta;

	public CMISDLWStep(StepMeta s, StepDataInterface stepDataInterface, int c,
			TransMeta t, Trans dis) {
		super(s, stepDataInterface, c, t, dis);
	}

	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi)
			throws KettleException {
		meta = (CMISDLWStepMeta) smi;
		data = (CMISDLWStepData) sdi;

		Object[] r = getRow(); // get row, blocks when needed!

		if (r == null) // no more input to be expected...
		{
			setOutputDone();
			return false;
		}

		if (first) {
			first = false;

			data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this);

			// we can connect to the repository
			connectCMIS(meta.getUrlField(), meta.getUserField(), meta
					.getPasswordField(), meta.getRepId());
		}

		logBasic("Inserting a new item");
		createItem(r);

		// end of alfresco extensions binding

		if (checkFeedback(getLinesRead())) {
			logBasic("Line " + getLinesRead()); // Some basic logging
		}

		return true;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (CMISDLWStepMeta) smi;
		data = (CMISDLWStepData) sdi;

		return super.init(smi, sdi);
	}

	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (CMISDLWStepMeta) smi;
		data = (CMISDLWStepData) sdi;

		super.dispose(smi, sdi);
	}

	private void connectCMIS(String conn_url, String user, String password,
			String repo) {
		
		// Alfresco connection
		SessionFactory f = SessionFactoryImpl.newInstance();

		Map<String, String> parameter = new HashMap<String, String>();

		// user credentials
		parameter.put(SessionParameter.USER, user);
		parameter.put(SessionParameter.PASSWORD, password);

		// connection settings
		parameter.put(SessionParameter.ATOMPUB_URL, conn_url);
		parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB
				.value());
		parameter.put(SessionParameter.REPOSITORY_ID, repo);

		data.session = f.createSession(parameter);

	}

	/*method for creating items and inserting them 
	 * in an existing alfresco share datalist instance
	**/
	private void createItem(Object[] r) {

		// Start binding alfresco aspects and object properties

		BindingsObjectFactory of = data.session.getBinding().getObjectFactory();

		String repId = data.session.getRepositoryInfo().getId();

		// set object properties: cmis:name and cmis:objectTypeId ---
		List<PropertyData<?>> propertiesList = new ArrayList<PropertyData<?>>();

		// Setting the list Type
		logBasic("Data List Type : "+meta.getListType());
		propertiesList.add(of.createPropertyIdData(PropertyIds.OBJECT_TYPE_ID,
				"D:" + meta.getListType()));
		String entryTitle = "";
		String descript = "";
		
		// First we'll need to know the Class
		String[] objectOrder = meta.getFieldOrder();
		for (int cp = 0; cp < r.length; cp++) {

			if (r[cp] == null) {
				continue;
			}

			if (objectOrder[cp].equals("") || objectOrder[cp] == null) {
				continue;
			}

			if (objectOrder[cp].equalsIgnoreCase("titre")) {
				entryTitle = (String) r[cp];
				continue;
			}

			if (objectOrder[cp].equalsIgnoreCase("Description")) {
				descript = (String) r[cp];
				continue;
			}

			// method for handling String
			if (r[cp].getClass() == String.class) {
				propertiesList.add(of.createPropertyStringData(objectOrder[cp],
						(String) r[cp]));
			}

			// method for handling GregorianCalendar
			if (r[cp].getClass() == GregorianCalendar.class) {
				propertiesList.add(of.createPropertyDateTimeData(
						objectOrder[cp], (GregorianCalendar) r[cp]));
			}

			// method for handling  integer
			if (r[cp].getClass() == Integer.class) {

				BigInteger bi = new BigInteger((String) r[cp]);
				propertiesList.add(of.createPropertyIntegerData(
						objectOrder[cp], bi));
			}
			/*
			 * method for handling Date although it should not happen 
			 * while official date format is GregorianCalendar
			 */
			if (r[cp].getClass() == Date.class) {

				GregorianCalendar gcd = new GregorianCalendar();
				gcd.setTime((Date) r[cp]);
				propertiesList.add(of.createPropertyDateTimeData(
						objectOrder[cp], gcd));
			} else {
				propertiesList.add(of.createPropertyStringData(objectOrder[cp],
						(String) r[cp]));
			}

		}

		// PropertyIds.NAME must be unique
		String t = "" + System.currentTimeMillis();
		propertiesList.add(of.createPropertyStringData(PropertyIds.NAME,
				entryTitle + t));

		// End of list-specific entries
		Properties properties = of.createPropertiesData(propertiesList);

		// --- set extensions
		String ns = "http://www.alfresco.org";
		String cmis = "http://docs.oasis-open.org/ns/cmis/core/200908/";

		List<CmisExtensionElement> aspectPropertiesElements = new ArrayList<CmisExtensionElement>();

		// title property

		Map<String, String> titlePropertyAttr = new HashMap<String, String>();
		titlePropertyAttr.put("propertyDefinitionId", "cm:title");

		List<CmisExtensionElement> titlePropertyValues = new ArrayList<CmisExtensionElement>();
		titlePropertyValues.add(new CmisExtensionElementImpl(cmis, "value",
				null, entryTitle)); // cm:title = "Title"

		aspectPropertiesElements.add(new CmisExtensionElementImpl(cmis,
				"propertyString", titlePropertyAttr, titlePropertyValues));

		// description property

		Map<String, String> descriptionPropertyAttr = new HashMap<String, String>();
		descriptionPropertyAttr.put("propertyDefinitionId", "cm:description");

		List<CmisExtensionElement> descriptionPropertyValues = new ArrayList<CmisExtensionElement>();
		descriptionPropertyValues.add(new CmisExtensionElementImpl(cmis,
				"value", null, descript)); // cm:description = "Summary"

		aspectPropertiesElements.add(new CmisExtensionElementImpl(cmis,
				"propertyString", descriptionPropertyAttr,
				descriptionPropertyValues));

		List<CmisExtensionElement> setAspectsElements = new ArrayList<CmisExtensionElement>();
		setAspectsElements.add(new CmisExtensionElementImpl(ns, "aspectsToAdd",
				null, "P:cm:titled")); // add aspect cm:titled
		setAspectsElements.add(new CmisExtensionElementImpl(ns, "properties",
				null, aspectPropertiesElements)); // add properties

		List<CmisExtensionElement> extElements = new ArrayList<CmisExtensionElement>();
		extElements.add(new CmisExtensionElementImpl(ns, "setAspects", null,
				setAspectsElements)); // add setAspects extension

		properties.setExtensions(extElements);

		// --- finally, create the document ---

		try {
			data.session.getBinding().getObjectService().createDocument(repId,
					properties, meta.getListId(), null, null, null, null, null,
					null);
		} catch (Exception e) {

			logBasic("Exception : + " + e.getMessage() + " , " + e.toString());
		}
		logBasic("Element done");
	}

	// Run is were the action happens!
	public void run() {
		logBasic("Starting to run...");
		try {

			while (processRow(meta, data) && !isStopped())
				;

		} catch (Exception e) {
			logError("Unexpected error : " + e.toString());
			logError(Const.getStackTracker(e));
			setErrors(1);
			stopAll();
		} finally {
			dispose(meta, data);
			logBasic("Finished, processing " + getLinesRead() + " rows");
			markStop();
		}
	}
}
