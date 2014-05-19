/**
 * CMISDLWStepDialog.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.ExtensionLevel;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.CmisExtensionElementImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.core.widget.ColumnInfo;
import org.pentaho.di.ui.core.widget.TableView;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;

public class CMISDLWStepDialog extends BaseStepDialog implements
    StepDialogInterface {

  protected TransMeta transMeta;

  private static Class<?> PKG = CMISDLWStepMeta.class; // for i18n purposes

  private CMISDLWStepMeta input;

  private List<ColumnInfo> tableFieldColumns = new ArrayList<ColumnInfo>();

  private Folder sites;

  // output field name
  private Label wlValServ;
  private Text wValServ;
  private FormData fdValServ, fdlValServ;

  private Label wlValUser;
  private Text wValUser;
  private FormData fdlValUser, fdValUser;

  private Label wlValPass;
  private Text wValPass;
  private FormData fdlValPass, fdValPass;

  private Button wTest;

  private FormData fdBton;
  private Listener lsTest;
  private Session session;

  private Combo wCombo;
  private FormData fdCombo;
  private String[] repoIds;

  private Label wlValSites;
  private FormData fdlValSites;
  private Folder site;

  private Combo wComboSites;
  private FormData fdComboSites;

  private Listener lsSite;
  private Label wlValTest;
  private FormData fdlValTest;

  private FormData fdlValDL;
  private Listener lsDL;
  private Combo wComboDL;
  private FormData fdComboDL;
  private Folder dl;
  private Label wlValDL;

  private String[] sitesIds, listsIds;

  private ColumnInfo[] ciReturn;
  private TableView wReturn;
  private FormData fdReturn;

  private Listener hddl;

  private String listType;

  private FormData fd;
  private Button buttonGetSites;
  private Button buttonGetLists;
  private FormData fdLists;

  private Button buttonGetFields;
  private FormData fdFields;

  public CMISDLWStepDialog(Shell parent, Object in, TransMeta transMeta,
      String sname) {
    super(parent, (BaseStepMeta) in, transMeta, sname);
    input = (CMISDLWStepMeta) in;
    this.transMeta = transMeta;
  }

  @Override
  public String open() {
    Shell parent = getParent();
    Display display = parent.getDisplay();

    shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN
        | SWT.MAX);
    props.setLook(shell);
    setShellImage(shell, input);

    ModifyListener lsMod = new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent e) {
        input.setChanged();
      }
    };
    changed = input.hasChanged();

    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = Const.FORM_MARGIN;
    formLayout.marginHeight = Const.FORM_MARGIN;

    shell.setLayout(formLayout);
    shell.setText(BaseMessages.getString(PKG, "Template.Shell.Title"));

    int middle = props.getMiddlePct();
    int margin = Const.MARGIN;

    // Stepname line
    wlStepname = new Label(shell, SWT.RIGHT);
    wlStepname
        .setText(BaseMessages.getString(PKG, "System.Label.StepName"));
    props.setLook(wlStepname);
    fdlStepname = new FormData();
    fdlStepname.left = new FormAttachment(0, 0);
    fdlStepname.right = new FormAttachment(middle, -margin);
    fdlStepname.top = new FormAttachment(0, margin);
    wlStepname.setLayoutData(fdlStepname);

    wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    wStepname.setText(stepname);
    props.setLook(wStepname);
    wStepname.addModifyListener(lsMod);
    fdStepname = new FormData();
    fdStepname.left = new FormAttachment(middle, 0);
    fdStepname.top = new FormAttachment(0, margin);
    fdStepname.right = new FormAttachment(100, 0);
    wStepname.setLayoutData(fdStepname);

    // url
    wlValServ = new Label(shell, SWT.RIGHT);
    wlValServ
        .setText(BaseMessages.getString(PKG, "Template.FieldName.URL"));
    props.setLook(wlValServ);
    fdlValServ = new FormData();
    fdlValServ.left = new FormAttachment(0, 0);
    fdlValServ.right = new FormAttachment(middle, -margin);
    fdlValServ.top = new FormAttachment(wStepname, margin);
    wlValServ.setLayoutData(fdlValServ);

    wValServ = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wValServ);
    wValServ.addModifyListener(lsMod);
    fdValServ = new FormData();
    fdValServ.left = new FormAttachment(middle, 0);
    fdValServ.right = new FormAttachment(100, 0);
    fdValServ.top = new FormAttachment(wStepname, margin);
    wValServ.setLayoutData(fdValServ);
    wValServ.setEnabled(false);
    // user
    wlValUser = new Label(shell, SWT.RIGHT);
    wlValUser.setText(BaseMessages
        .getString(PKG, "Template.FieldName.User"));
    props.setLook(wlValUser);
    fdlValUser = new FormData();
    fdlValUser.left = new FormAttachment(0, 0);
    fdlValUser.right = new FormAttachment(middle, -margin);
    fdlValUser.top = new FormAttachment(wValServ, margin);
    wlValUser.setLayoutData(fdlValUser);

    wValUser = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wValUser);

    wValUser.addModifyListener(lsMod);
    fdValUser = new FormData();
    fdValUser.left = new FormAttachment(middle, 0);
    fdValUser.right = new FormAttachment(100, 0);
    fdValUser.top = new FormAttachment(wValServ, margin);
    wValUser.setLayoutData(fdValUser);

    // Password field
    wlValPass = new Label(shell, SWT.RIGHT);

    props.setLook(wlValPass);
    fdlValPass = new FormData();
    fdlValPass.left = new FormAttachment(0, 0);
    fdlValPass.right = new FormAttachment(middle, -margin);
    fdlValPass.top = new FormAttachment(wValUser, margin);
    wlValPass.setLayoutData(fdlValPass);

    wValPass = new Text(shell, SWT.SINGLE | SWT.PASSWORD | SWT.LEFT
        | SWT.BORDER);
    props.setLook(wValPass);
    wValPass.addModifyListener(lsMod);
    fdValPass = new FormData();
    fdValPass.left = new FormAttachment(middle, 0);
    fdValPass.right = new FormAttachment(100, 0);
    fdValPass.top = new FormAttachment(wValUser, margin);
    wValPass.setLayoutData(fdValPass);

    wTest = new Button(shell, SWT.PUSH);
    wTest.setText("Connexion");
    fdBton = new FormData();
    fdBton.left = new FormAttachment(middle, 0);
    fdBton.right = new FormAttachment(100, 0);
    fdBton.top = new FormAttachment(wValPass, margin);
    wTest.setLayoutData(fdBton);

    wlValTest = new Label(shell, SWT.RIGHT);
    wlValTest.setText(BaseMessages
        .getString(PKG, "Template.FieldName.Repo"));
    props.setLook(wlValTest);
    fdlValTest = new FormData();
    fdlValTest.left = new FormAttachment(0, 0);
    fdlValTest.right = new FormAttachment(middle, -margin);
    fdlValTest.top = new FormAttachment(wTest, margin);
    wlValTest.setLayoutData(fdlValTest);

    buttonGetSites = new Button(shell, SWT.NONE);
    buttonGetSites.setText("Get Sites");

    wCombo = new Combo(shell, SWT.READ_ONLY);
    wCombo.setItems(new String[] { " " });

    fdCombo = new FormData();
    fdCombo.left = new FormAttachment(middle, 0);
    fdCombo.right = new FormAttachment(90,
        -buttonGetSites.getBounds().width - margin);
    fdCombo.top = new FormAttachment(wTest, margin);
    wCombo.setLayoutData(fdCombo);

    fd = new FormData();
    fd.left = new FormAttachment(wCombo, 0);
    fd.top = new FormAttachment(wTest, margin);
    buttonGetSites.setLayoutData(fd);

    // Sites
    wlValSites = new Label(shell, SWT.RIGHT);
    wlValSites.setText(BaseMessages.getString(PKG,
        "Template.FieldName.Sites"));
    props.setLook(wlValSites);
    fdlValSites = new FormData();
    fdlValSites.left = new FormAttachment(0, 0);
    fdlValSites.right = new FormAttachment(middle, -margin);
    fdlValSites.top = new FormAttachment(wCombo, margin);
    wlValSites.setLayoutData(fdlValSites);

    buttonGetLists = new Button(shell, SWT.NONE);
    buttonGetLists.setText("Get Lists");

    wComboSites = new Combo(shell, SWT.READ_ONLY);
    wComboSites.setItems(new String[] { " " });
    fdComboSites = new FormData();
    fdComboSites.left = new FormAttachment(middle, 0);
    fdComboSites.right = new FormAttachment(90,
        -buttonGetLists.getBounds().width - margin);
    fdComboSites.top = new FormAttachment(wCombo, margin);
    wComboSites.setLayoutData(fdComboSites);

    fdLists = new FormData();
    fdLists.left = new FormAttachment(wComboSites, 0);
    fdLists.top = new FormAttachment(wCombo, margin);
    buttonGetLists.setLayoutData(fdLists);

    // Data lists
    wlValDL = new Label(shell, SWT.RIGHT);
    wlValDL.setText(BaseMessages.getString(PKG, "Template.FieldName.DL"));
    props.setLook(wlValDL);
    fdlValDL = new FormData();
    fdlValDL.left = new FormAttachment(0, 0);
    fdlValDL.right = new FormAttachment(middle, -margin);
    fdlValDL.top = new FormAttachment(wComboSites, margin);
    wlValDL.setLayoutData(fdlValDL);

    buttonGetFields = new Button(shell, SWT.NONE);
    buttonGetFields.setText("Get Fields");

    wComboDL = new Combo(shell, SWT.READ_ONLY);
    wComboDL.setItems(new String[] { " " });
    fdComboDL = new FormData();
    fdComboDL.left = new FormAttachment(middle, 0);
    fdComboDL.right = new FormAttachment(90,
        -buttonGetFields.getBounds().width - margin);
    fdComboDL.top = new FormAttachment(wComboSites, margin);
    wComboDL.setLayoutData(fdComboDL);

    fdFields = new FormData();
    fdFields.left = new FormAttachment(wComboDL, 0);
    fdFields.top = new FormAttachment(wComboSites, margin);
    buttonGetFields.setLayoutData(fdFields);

    // View Table for matching purposes
    int UpInsCols = 2;
    int UpInsRows = 5;

    ciReturn = new ColumnInfo[UpInsCols];
    ciReturn[0] = new ColumnInfo(
        BaseMessages.getString(PKG,
            "InsertUpdateDialog.ColumnInfo.TableField"), ColumnInfo.COLUMN_TYPE_CCOMBO, new String[] { "" }, false); //$NON-NLS-1$
    ciReturn[1] = new ColumnInfo(
        BaseMessages.getString(PKG,
            "InsertUpdateDialog.ColumnInfo.StreamField"), ColumnInfo.COLUMN_TYPE_CCOMBO, new String[] { "" }, false); //$NON-NLS-1$

    wReturn = new TableView(transMeta, shell, SWT.BORDER
        | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL,
        ciReturn, UpInsRows, lsMod, props);

    fdReturn = new FormData();
    fdReturn.left = new FormAttachment(0, 0);
    fdReturn.top = new FormAttachment(wComboDL, margin);
    fdReturn.right = new FormAttachment(100, 0);
    wReturn.setLayoutData(fdReturn);
    wReturn.setSortable(false);

    tableFieldColumns.add(ciReturn[0]);

    // OK and cancel buttons
    wOK = new Button(shell, SWT.PUSH);
    wOK.setText(BaseMessages.getString(PKG, "System.Button.OK"));
    wCancel = new Button(shell, SWT.PUSH);
    wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));
    // wReturn
    BaseStepDialog.positionBottomButtons(shell,
        new Button[] { wOK, wCancel }, margin, wReturn);

    // Add listeners
    lsCancel = new Listener() {
      @Override
      public void handleEvent(Event e) {
        cancel();
      }
    };
    lsOK = new Listener() {
      @Override
      public void handleEvent(Event e) {
        ok();
      }
    };

    lsTest = new Listener() {
      @Override
      public void handleEvent(Event e) {
        setRepo();
        input.setFirstConn(false);
      }
    };

    lsSite = new Listener() {
      @Override
      public void handleEvent(Event e) {
        setSite();
      }
    };

    lsDL = new Listener() {
      @Override
      public void handleEvent(Event e) {
        getDL();
      }
    };

    hddl = new Listener() {
      @Override
      public void handleEvent(Event e) {
        // setTableFieldCombo();
        handleDL();
      }
    };

    wCancel.addListener(SWT.Selection, lsCancel);
    wOK.addListener(SWT.Selection, lsOK);
    wTest.addListener(SWT.Selection, lsTest);

    buttonGetSites.addListener(SWT.Selection, lsSite);
    buttonGetLists.addListener(SWT.Selection, lsDL);
    buttonGetFields.addListener(SWT.Selection, hddl);

    lsDef = new SelectionAdapter() {
      @Override
      public void widgetDefaultSelected(SelectionEvent e) {
        ok();
      }
    };

    wStepname.addSelectionListener(lsDef);

    // Detect X or ALT-F4 or something that kills this window...
    shell.addShellListener(new ShellAdapter() {
      @Override
      public void shellClosed(ShellEvent e) {
        cancel();
      }
    });

    // Set the shell size, based upon previous time...
    setSize();

    getData();
    input.setChanged(changed);

    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    return stepname;
  }

  // get and set all attributes from selected datalist type
  private void handleDL() {

    ColumnInfo colInfo = new ColumnInfo(BaseMessages.getString(PKG,
        "InsertUpdateDialog.ColumnInfo.TableField"),
        ColumnInfo.COLUMN_TYPE_CCOMBO, this.getDLProps());
    this.wReturn.setColumnInfo(0, colInfo);
    wReturn.setReadonly(true);
  }

  // Get entry fileds and put them in table
  private void getEntryStream() {
    try {
      wReturn.clearAll();
      RowMetaInterface r = transMeta.getPrevStepFields(stepname);
      BaseStepDialog.getFieldsFromPrevious(r, wReturn, 1,
          new int[] { 2 }, new int[] {}, -1, -1, null);

    } catch (KettleStepException e) {
      e.printStackTrace();
    }

  }

  // Read data and place it in the dialog
  public void getData() {

    getEntryStream();

    wStepname.selectAll();
    wValServ.setText(input.getUrlField());
    wValUser.setText(input.getUserField());
    wValPass.setText(input.getPasswordField());
    this.listType = input.getListType();
    session = input.getMainSession();

    wCombo.setItems(input.getRepositories());
    wCombo.select(input.getRepoIndex());
    if (!input.isFirstConn()) {
      setSites();

      String[] l = (input.getFieldOrder() == null) ? (new String[] { " " })
          : (input.getFieldOrder());
      for (int k = 0; k < l.length; k++) {
        wReturn.setText(l[k], 1, k);
      }

    }

    if (sites != null) {
      this.wComboSites.select(input.getSiteIndex());
      this.getDL();
      this.wComboDL.select(input.getListIndex());
    }
  }

  private void cancel() {
    stepname = null;
    input.setChanged(changed);
    dispose();
  }

  private void setRepo() {
    connectCMIS(wValServ.getText(), wValUser.getText(), wValPass.getText());
  }

  // let the plug-in know about the entered data
  private void ok() {

    stepname = wStepname.getText(); // return value

    input.setUrlField(wValServ.getText());
    input.setUserField(wValUser.getText());
    input.setPasswordField(wValPass.getText());

    wCombo.select(0);
    if (repoIds != null) {
      if (wCombo != null && wCombo.getItemCount() > 0
          && wCombo.getSelectionIndex() <= this.repoIds.length - 1) {
        input.setRepoIndex(wCombo.getSelectionIndex());

        input.setRepId(this.repoIds[wCombo.getSelectionIndex()]);// null
                                      // pointer
                                      // sur
                                      // open->
                                      // conf->
                                      // load

        input.setRepositories(wCombo.getItems());
      }
    }

    if (sitesIds != null) {
      if (wComboSites.getItemCount() > 0
          && wComboSites.getSelectionIndex() <= this.sitesIds.length - 1) {
        input.setSiteIndex(wComboSites.getSelectionIndex());
        input.setSiteId(this.sitesIds[wComboSites.getSelectionIndex()]);
        input.setSites(this.wComboSites.getItems());
      }
    }

    if (listsIds != null) {
      if (wComboDL.getItemCount() > 0
          && wComboDL.getSelectionIndex() <= this.listsIds.length - 1) {
        input.setListIndex(wComboDL.getSelectionIndex());
        input.setListId(listsIds[wComboDL.getSelectionIndex()]);
        input.setLists(this.wComboDL.getItems());
      } else {
        input.setListId(listsIds[0]);
      }
    }
    input.setMainSession(session);

    // save association tab
    String[] items = wReturn.getItems(0);
    logBasic(items[0]);
    input.setFieldOrder(wReturn.getItems(0));

    input.setListType(this.listType);

    dispose();
  }

  private void setSite() {
    connectCMISRepo(true);
  }

  private void getDL() {
    int ind = wComboSites.getSelectionIndex();
    String item = this.wComboSites.getItem(ind);
    listDataLists(item);
  }

  // Connect to CMIS service for getting repositories list
  private void connectCMIS(String conn_url, String user, String password) {

    // Alfresco Connexion
    SessionFactory f = SessionFactoryImpl.newInstance();

    Map<String, String> parameter = new HashMap<String, String>();

    // user credentials
    parameter.put(SessionParameter.USER, user);
    parameter.put(SessionParameter.PASSWORD, password);

    // connection settings
    parameter.put(SessionParameter.ATOMPUB_URL, conn_url);//
    parameter.put(SessionParameter.BINDING_TYPE,
        BindingType.ATOMPUB.value());
    parameter.put(SessionParameter.REPOSITORY_ID, "");

    List<Repository> rpos = f.getRepositories(parameter);

    String[] itemsList = new String[rpos.size()];
    repoIds = new String[rpos.size()];

    for (int i = 0; i < rpos.size(); i++) {
      Repository r = rpos.get(i);
      itemsList[i] = r.getName();
      repoIds[i] = r.getId();
    }

    wCombo.setItems(itemsList);
    wCombo.select(0);
    input.setRepositories(itemsList);

    // session = f.createSession(parameter);
  }

  private void connectCMISRepo(boolean maj) {

    if (input.getMainSession() == null) {
      // Connect to Alfresco
      SessionFactory f = SessionFactoryImpl.newInstance();

      Map<String, String> parameter = new HashMap<String, String>();

      // user credentials
      parameter.put(SessionParameter.USER, wValUser.getText());
      parameter.put(SessionParameter.PASSWORD, wValPass.getText());

      // connection settings
      parameter.put(SessionParameter.ATOMPUB_URL, wValServ.getText());//
      parameter.put(SessionParameter.BINDING_TYPE,
          BindingType.ATOMPUB.value());
      parameter.put(SessionParameter.REPOSITORY_ID,
          repoIds[wCombo.getSelectionIndex()]);

      logDebug("SessionParameter.REPOSITORY_ID : "
          + SessionParameter.REPOSITORY_ID + ", repoIds.length : "
          + repoIds.length + ", wCombo.getSelectionIndex() : "
          + wCombo.getSelectionIndex());

      session = f.createSession(parameter);

      input.setMainSession(f.createSession(parameter));
    } else {
      session = input.getMainSession();
    }
    setSites();
  }

  private void setSites() {

    if (session != null) {// If session has been established
      Folder root = session.getRootFolder();

      ItemIterable<CmisObject> pl = root.getChildren();

      sites = null;

      for (CmisObject o : pl) {
        if ("Sites".equals(o.getName())) {
          sites = (Folder) o;
        }
      }
      ItemIterable<CmisObject> siteList = sites.getChildren();
      String sitesShare[] = new String[(int) siteList.getTotalNumItems()];
      this.sitesIds = new String[(int) siteList.getTotalNumItems()];
      int cpt = 0;
      for (CmisObject o : siteList) {

        sitesShare[cpt] = o.getName();
        this.sitesIds[cpt] = o.getId();
        cpt++;
      }

      wComboSites.setItems(sitesShare);
      wComboSites.select(0);
      input.setSites(sitesShare);
    }
  }

  // Should get name in Alfresco aspects, in case we're in alfresco
  /*
   * This method is very lengthy because we must browse inside alfresco
   * aspects for getting real title
   */
  private void listDataLists(String siteName) {

    if (!input.isFirstConn()) {
      ItemIterable<CmisObject> pl = this.sites.getChildren();

      site = null;

      for (CmisObject o : pl) {
        if (siteName.equals(o.getName())) {
          site = (Folder) o;
        }
      }

      pl = site.getChildren();

      dl = null;

      for (CmisObject o : pl) {
        if ("dataLists".equals(o.getName())) {
          dl = (Folder) o;
        }
      }

      pl = dl.getChildren();

      dl = null;
      String[] lists = new String[(int) pl.getTotalNumItems()];
      this.listsIds = new String[(int) pl.getTotalNumItems()];
      int cpt = 0;
      for (CmisObject o : pl) {

        List list_lvl2 = o.getExtensions(ExtensionLevel.PROPERTIES);

        for (int u = 0; u < list_lvl2.size(); u++) {
          CmisExtensionElementImpl as = (CmisExtensionElementImpl) list_lvl2
              .get(u);

          List list_lvl3 = as.getChildren();
          for (int uu = 0; uu < list_lvl3.size(); uu++) {
            CmisExtensionElementImpl n = (CmisExtensionElementImpl) list_lvl3
                .get(uu);
            if ("properties".equals(n.getName())) {
              List list_lvl4 = n.getChildren();

              for (int uu3 = 0; uu3 < list_lvl4.size(); uu3++) {
                CmisExtensionElementImpl n2 = (CmisExtensionElementImpl) list_lvl4
                    .get(uu3);
                if ("propertyString".equals(n2.getName())) {
                  if (n2.getChildren() != null) {
                    List list_lvl5 = n2.getChildren();

                    for (int uu4 = 0; uu4 < list_lvl5
                        .size(); uu4++) {
                      CmisExtensionElementImpl n5 = (CmisExtensionElementImpl) list_lvl5
                          .get(uu4);
                      if (uu4 == 0) {
                        lists[cpt] = n5.getValue();
                      }
                    }
                  }
                }
              }
            }
          }
        }
        lists[cpt] = o.getName();
        listsIds[cpt] = o.getId();
        cpt++;
      }

      this.wComboDL.setItems(lists);
      this.wComboDL.select(0);
      input.setLists(lists);
    }
  }

  private String[] getDLProps() {
    // Establish a connection if session is not up
    CmisObject cmob = session.getObject(session
        .createObjectId(listsIds[wComboDL.getSelectionIndex()]));
    Folder f = (Folder) cmob;
    Property<String> pdef = f.getProperty("dl:dataListItemType");

    listType = pdef.getValueAsString();

    ObjectType typedef = session.getTypeDefinition("D:"
        + pdef.getValueAsString());

    Set<String> set = typedef.getPropertyDefinitions().keySet();

    // Inputs
    String fields = "Titre;Description";
    String[] flds = set.toArray(new String[] {});
    for (int z = 0; z < flds.length; z++) {
      if (flds[z].startsWith("dl:")) {
        fields += ";" + flds[z];
      }
    }

    // recreer tableau avec titre, desc et les dl:, delete le reste
    return fields.split(";");
  }

}
