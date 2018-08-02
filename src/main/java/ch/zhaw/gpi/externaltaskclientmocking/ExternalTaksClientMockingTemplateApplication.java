package ch.zhaw.gpi.externaltaskclientmocking;

import ch.zhaw.gpi.externaltaskclientmocking.handlers.SendTweetHandler;
import org.camunda.bpm.client.ExternalTaskClient;

/**
 * Diese Klasse implementiert den External Task Client von Camunda, welcher
 * einen sogenannten Topic (SendTweet) bei einer Process Engine abonniert und
 * dann allenfalls vorhandene Aufgaben erledigt und dies der Process Engine
 * mitteilt.
 *
 * Das Tweet senden wird dabei nur gemocked (Ausgabe in Konsole).
 *
 * Wer es bezüglich Abhängigkeiten noch puristischer will, kann statt den
 * External Task Client zu nutzen, auch direkt mit der Camunda REST API
 * kommunizieren.
 * 
 * In einer produktiven Applikation gäbe es sicher mehr Schichten (z.B. der Client
 * selbst ausgelagert). Hier ist lediglich das (gemockte) Senden der Tweets in
 * einen eigenen Service ausgelagert, um den Vergleich mit dem funktionsfähigen
 * Projekt (https://github.com/zhaw-gpi/external-task-client-spring-boot-template), 
 * welches tatsächlich Tweets sendet, zu vereinfachen. Und auch der
 * Handler (SendTweetHandler) ist in einer eigenen Klasse ausgelagert.
 *
 * @author scep
 */
public class ExternalTaksClientMockingTemplateApplication {

    public static void main(String[] args) {
        /**
         * 1. Eine neue External Task Client-Instanz erstellen und konfigurieren mit dem ExternalTaskClientBuilder
         *    https://github.com/camunda/camunda-external-task-client-java/blob/1.0.0/client/src/main/java/org/camunda/bpm/client/ExternalTaskClientBuilder.java
         */
        ExternalTaskClient externalTaskClient = ExternalTaskClient
                .create() // Den ExternalTaskClientBuilder initiieren
                .baseUrl("http://localhost:8080/rest") // URL der REST API der Process Engine
                .workerId("ExternalTaksClientMockingTemplateApplication") // Eindeutiger Name, damit die Process Engine "weiss", wer einen bestimmten Task gelocked hat
                .maxTasks(10) // Wie viele Tasks sollen maximal auf einen "Schlag" (Batch) gefetched werden
                .lockDuration(2000) // Long Polling für 2 Sekunden (2000 Millisekunden) -> siehe https://docs.camunda.org/manual/latest/user-guide/process-engine/external-tasks/#long-polling-to-fetch-and-lock-external-tasks
                .build(); // Die External Task Client-Instanz mit den vorhergehenden Angaben erstellen
        
        /**
         * 2. Der External Task Client kann sich für mehrere Topics registrieren,
         *    in diesem Beispiel nur für das "SendTweet"-Topic. Registrieren
         *    bedeutet hierbei, dass der Client in regelmässigen Abständen (siehe
         *    lockDuration oben) bei der Process Engine nach neuen Tasks für den
         *    Topic anfrägt. Falls welche vorhanden sind, werden diese bezogen
         *    (Fetch) und blockiert (lock), so dass kein anderer Client die Aufgaben
         *    auch bearbeiten könnte (=> Konflikte). Nun werden sie von einem
         *    External Task Handler (die eigentliche Business Logik) abgearbeitet
         *    und der Process Engine als erledigt (complete) gemeldet. Die
         *    Registration umfasst die folgenden Schritte:
         */
        
        /**
         *    a) Für jedes Topic ist eine External Task Handler-Implementation
         *    anzugeben, welche wie hier gezeigt als eigene Klasse SendTweetHandler implementiert
         *    sein kann und eine Instanz davon hier erstellt wird.
         *    Oder wer sich mit Lambda-Expressions auskennt, kann
         *    dies auch kürzer haben wie z.B. gezeigt in 
         *    https://docs.camunda.org/get-started/quick-start/service-task/#implement-an-external-task-worker
         *    https://github.com/camunda/camunda-external-task-client-java/blob/1.0.0/client/src/main/java/org/camunda/bpm/client/topic/TopicSubscriptionBuilder.java
         */
        SendTweetHandler tweetSenderHandler = new SendTweetHandler();

        /**
         *    b) Das Registrieren geschieht über einen Fluent Builder wie schon
         *       in Schritt 1. Es ist im Folgenden zweimal aufgeführt für zwei
         *       Topics. Er umfasst:
         *       - Festlegen des Topics (subscribe)
         *       - Die Handler-Klasse (handler), welche gefetchte Tasks abarbeitet
         *       - Das eigentliche Registrieren (open)
         */
        externalTaskClient
                .subscribe("SendTweet")
                .handler(tweetSenderHandler)
                .open();
        
        externalTaskClient
                .subscribe("SendTweetTime")
                .handler(tweetSenderHandler)
                .open();
    }
}
