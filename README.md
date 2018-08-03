Björn Scheppler, 2.8.2018

# Camunda External Task Client Mocking Template (external-task-client-mocking-template)
Dieses Maven-Projekt kann genutzt werden als Startpunkt für Mocking-Projekte, 
bei welchen Tasks, die von einer Camunda Process Engine "veröffentlicht" werden, abzuarbeiten. 
Als Basis wird dafür der Java External Task Client von Camunda verwendet, welcher 
im Prinzip nichts anderes tut, als die REST API-Calls für External Tasks zu kapseln. 
Im konkreten Beispiel wird das Senden eines Tweets gemocked (lediglich Ausgabe in die Kommandozeile).

Entsprechend enthält das Projekt lediglich folgende Komponenten
1. Camunda External Task Client als einzige Maven-Abhängigkeit
2. Eine einzige Klasse TweetSenderMocked mit folgender Funktionalität: Sie implementiert 
den External Task Client von Camunda, welcher einen sogenannten Topic 
(SendTweet oder SendTweetTime) bei einer Process Engine abonniert und dann allenfalls vorhandene 
Aufgaben erledigt und dies der Process Engine mitteilt. Das Tweet senden wird dabei 
nur gemocked (Ausgabe in Konsole).

Siehe auch https://github.com/zhaw-gpi/external-task-client-spring-boot-template
für das Pendant zu diesem Projekt-Template, welches eine funktionsfähige
Spring Boot-Applikation enthält, die auch wirklich Tweets veröffentlicht.

## Verwendete Quellen
1. https://docs.camunda.org/manual/7.9/user-guide/ext-client/
2. https://github.com/camunda/camunda-external-task-client-java/tree/master/examples
3. https://docs.camunda.org/manual/7.9/reference/rest/external-task/
4. https://docs.camunda.org/get-started/quick-start/service-task/#implement-an-external-task-worker
5. https://docs.camunda.org/manual/7.9/user-guide/process-engine/external-tasks/

## Deployment
1. Erstmalig oder bei Problemen ein Clean & Build (Netbeans), respektive "mvn clean install" (Cmd) durchführen
2. Bei Änderungen am POM-File oder bei (Neu)kompilierungsbedarf genügt ein Build (Netbeans), respektive "mvn install"
3. Für den Start muss zunächst die Process Engine laufen, das heisst z.B. das Camunda Projekttemplate (https://github.com/zhaw-gpi/project-template)
4. Für den Start ist ein Run (Netbeans), respektive "java -jar .\target\external-task-client-mocking-1.0.1.jar" (Cmd) erforderlich. Dabei wird die Main-Methode in TweetSenderMocked.java ausgeführt.

## Nutzung
1. Damit man den Client in Aktion sieht, muss mindestens eine Aufgabe vom Topic "SendTweet" oder "SendTweetTime" zu erledigen sein.
2. Hierzu steht im Projekt Template ein Prozess für das zeitgesteuerte Senden von Tweets zur Verfügung, damit es zirka alle 10 Sekunden etwas zu tun gibt.
3. Zirka alle 10 Sekunden sollte daher in der Output-Konsole des External Task Clients der Eintrag "Tweet wird geposted: Aktuelle Uhrzeit ist ..." erscheinen...
4. ...und im Cockpit ist die zuvor laufende Prozessinstanz nicht mehr sichtbar (da beendet).