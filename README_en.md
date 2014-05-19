#Alfresco CMIS Writer & Alfresco CMIS DataList Writer Plugins for PDI-Kettle

> [Alfresco] (http://www.alfresco.com/) 4.2.1  
> [PDI-Kettle] (http://community.pentaho.com/projects/data-integration/) 5.0.1  
> [Ubuntu] (http://www.ubuntu.com/) 12.04  

##Description

These plugins had been developed for Alfresco Share and tested on **Alfresco 4.2.x**

The plugins Kettle Alfresco CMIS Writer and Alfresco CMIS DataList Writer allow to extract data and insert it into Alfresco thanks to CMIS protocol.

###Alfresco CMIS Writer

Kettle Plugin used to write documents into Alfresco Share.
CMIS Writer provides a step to PDI-Kettle allowing you to insert data into Alfresco.

###Alfresco CMIS DataList Writer

Kettle Plugin used to write datalists into Alfresco Share.
CMIS Writer provides a step to PDI-Kettle allowing you to insert data on datalists.

##Building

Execute the following command where **pom.xml** is.
> **mvn clean install**

##Installing

After installing [PDI-Kettle] (http://community.pentaho.com/projects/data-integration/) and compiling the sources.

Copy the target/**CMIS[DL]W** folder into **{PDI-Kettle}/plugins/steps**

To work, the plugin absolutly needs the following librairies :

> chemistry-opencmis-client-api-0.11.0.jar  
> chemistry-opencmis-client-bindings-0.11.0.jar  
> chemistry-opencmis-client-impl-0.11.0.jar  
> chemistry-opencmis-commons-api-0.11.0.jar  
> chemistry-opencmis-commons-impl-0.11.0.jar  
> slf4j-simple-1.7.7.jar  
> slf4j-api-2.0.99.jar  
> jdom-1.1.2.jar

Place it into the **CMIS[DL]W/lib** folder

_It is possible to place it into **src/main/resources/lib** before building_

###Plugins content

> /lib  
> plugin.xml  
> icon.png  
> Kettle-cmis-[datalist]-writer.jar  

##Using the plugins

If the plugin is correctly installed, an **Alfresco CMIS** folder should appear. Containing **CMIS DataList Writer Plugin** and/or **CMIS DataList Writer Plugin**.

###Configuring Alfresco CMIS Writer

To configure CMIS Writer Plugin step, double-click on it.  
Above all it is necessary to click on the **Connection** button.  

Then you can map the .csv fields with the chosen Model's attributs. If one or more aspects had been chosen, their attributs would appear too.

Click on the Mapping button.  
Here are two important fields  
+    source_file : The absolute or relative path to the file you want to extract.  
+    destination_path : The path where you want your file to be placed into Alfresco. The path must have the following shape : folder0/…/folder1.  

To associate two fields, click on them, then on **Add** button.  

###Configuring Alfresco CMIS DataList Writer
To configure CMIS DataList Writer Plugin step, double-click on it.  
After connecting, get the **Sites** from the Repository then the **Lists** and finally the **Fields**.

##

[Atol Conseils et Développements] (http://www.atolcd.com)  
Alfresco [Gold Partner] (http://www.alfresco.com/partners/atol)  
Follow us on twitter [ @atolcd] (https://twitter.com/atolcd)


