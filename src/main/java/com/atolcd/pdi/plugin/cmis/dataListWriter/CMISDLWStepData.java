/**
* CMISDLWStepData.java
*
*
*
* Copyright (C) 2010 Atol Conseils et D�veloppements,  3 bd eiffel 21600 Longvic
* See LICENSE file under this distribution for licensing information
*
* @author vpl
* @version %I%, %G%
* @since 20100322
**/

package com.atolcd.pdi.plugin.cmis.dataListWriter;

import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;



public class CMISDLWStepData extends BaseStepData implements StepDataInterface {

  public RowMetaInterface outputRowMeta;

  public Session session;
  public ObjectId list_Id;
  public String rootFolderId;

  public SessionFactory f;

  public CMISDLWStepData() {
    super();
  }
}
