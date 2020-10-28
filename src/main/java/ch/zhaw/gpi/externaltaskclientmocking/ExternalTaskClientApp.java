package ch.zhaw.gpi.externaltaskclientmocking;

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
 * selbst ausgelagert). Hier ist lediglich der Handler in eine eigene Klasse ausgelagert.
 *
 * @author scep
 */
public class ExternalTaskClientApp {

    public static void main(String[] args) {
        /**
         * 1. Eine neue External Task Client-Instanz erstellen und konfigurieren mit dem ExternalTaskClientBuilder
         */
        ExternalTaskClient externalTaskClient = ExternalTaskClient
                .create() // Den ExternalTaskClientBuilder initiieren
                .baseUrl("http://localhost:8080/engine-rest") // URL der REST API der Process Engine
                .workerId("ExternalTaskClientApp") // Eindeutiger Name, damit die Process Engine "weiss", wer einen bestimmten Task gelocked hat
                .maxTasks(10) // Wie viele Tasks sollen maximal auf einen "Schlag" (Batch) gefetched werden
                .lockDuration(20000) // Wie lange sollen die Tasks gelocked werden (20000 Millisekunden)
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
