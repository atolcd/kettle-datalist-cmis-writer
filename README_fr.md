#Alfresco CMIS Writer & Alfresco CMIS DataList Writer Plugins for PDI-Kettle

> [Alfresco] (http://www.alfresco.com/) 4.2.1  
> [PDI-Kettle] (http://community.pentaho.com/projects/data-integration/) 5.0.1  
> [Ubuntu] (http://www.ubuntu.com/) 12.04  

##Description

Ces plugins ont été développés pour Alfresco Share et testés sur **Alfresco 3.4.x - 4.2.x**

Les plugins Kettle Alfresco CMIS Writer et Alfresco CMIS DataList Writer permettent d'extraire des données documentaires ainsi que leurs métadonnées et de les injecter, à l'aide du protocole CMIS, dans Alfresco.

L'utilisation de Kettle permet de mieux gérer les métadonnées en intégrant le traitement au sein d'une transformation.

###Alfresco CMIS Writer

Plugin Kettle pour l'écriture de documents dans Alfresco Share.
CMIS Writer fournit une étape pour PDI-Kettle qui permet d'insérer des données dans l’entrepôt Alfresco.

###Alfresco CMIS DataList Writer

Plugin Kettle pour l'écriture de datalists dans Alfresco Share.
CMIS DataLists Writer fournit une étape pour PDI-Kettle qui permet d'insérer des éléments dans une instance de liste conforme au standard des DataLists CMIS.

##Compilation

Placez vous dans le dossier où se trouve le **pom.xml** puis exécuter la commande
> **mvn clean install**

##Installation

Après avoir installé [PDI-Kettle] (http://community.pentaho.com/projects/data-integration/) et compilé les sources, la mise en place de plugins est simple et rapide.

Rendez-vous dans le répertoire où **PDI-Kettle** est installé, puis dans le répertoire :
> **{PDI-Kettle}/plugins/steps**

Déposez ici le dossiers **CMIS[DL]W**.

Pour que le plugin fonctionne, il est nécessaire de se procurer les bibliothèques suivantes :

> chemistry-opencmis-client-api-0.11.0.jar  
> chemistry-opencmis-client-bindings-0.11.0.jar  
> chemistry-opencmis-client-impl-0.11.0.jar  
> chemistry-opencmis-commons-api-0.11.0.jar  
> chemistry-opencmis-commons-impl-0.11.0.jar  
> slf4j-simple-1.7.7.jar  
> slf4j-api-2.0.99.jar  
> jdom-1.1.2.jar

Placer les dans le dossier **CMIS[DL]W/lib**

_Il est possible de les placer dans **src/main/resources/lib** avant la compilation_

###Contenu des plugins

> /lib  
> plugin.xml  
> icon.png  
> CMIS[DL]W.jar  

##Utilisation des plugins

Exécutez PDI-Kettle et créez une nouvelle transformation.
Si l'installation à été correctement effectuée un dossier **Alfresco CMIS** doit apparaitre dans la palette de création, contenant les éléments **CMIS DataList Writer Plugin** et/ou **CMIS DataList Writer Plugin**.

###Configurer Alfresco CMIS Writer
Pour configurer l'élément CMIS Writer Plugin, double-cliquez dessus.  
Avant de commencer, il est absolument nécessaire de cliquer sur le bouton **Connexion**  
Mappez ensuite les champs extraits du .csv avec les attributs du Model Type choisit. Si un ou plusieurs aspects ont été choisit, leurs attributs apparaitront lors du Mapping.

Cliquer sur le bouton Mapping.
Il existe deux Champs cibles toujours présents ici.

+    source_file : Il s'agit du chemin (path) du fichier à extraire. Le chemin peut être absolu ou relatif.
+    destination_path : Il s'agit du chemin (path) où placer le fichier extrait dans Alfresco. Ce chemin doit avoir la forme folder0/…/folder1, et ce en partant de la racine du Repository.

Les autres Champs cibles sont les attributs du modèle et des aspects choisit préalablement.
Cliquez sur le Champs source et le Champs cibles à associer puis cliquer sur le bouton Ajouter

###Configurer Alfresco CMIS DataList Writer
Pour configurer l'élément CMIS DataList Writer Plugin, double-cliquez dessus.  
Après s'être connecté, récupérez les **Sites** du Repository puis les **Lists** et enfin les **Fields**.

##

[Atol Conseils et Développements] (http://www.atolcd.com)  
Alfresco [Gold Partner] (http://www.alfresco.com/partners/atol)  
Follow us on twitter [ @atolcd] (https://twitter.com/atolcd)


