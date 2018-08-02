package ch.zhaw.gpi.externaltaskclientmocking.handlers;

import ch.zhaw.gpi.externaltaskclientmocking.services.TwitterService;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

/**
 * Enthält die Business Logik, welche für vom External Task Client gefetchte
 * Tasks abarbeitet und der Process Engine als erledigt mitteilt. In diesem
 * Beispiel umfasst dies das (gemockte) Senden eines Tweets.
 *
 * @author scep
 */
public class SendTweetHandler implements ExternalTaskHandler {
    // Das eigentliche Posten des Tweets ist ausgelagert an eine Service-Klasse,
    // die hier instanziert wird.
    TwitterService twitterService = new TwitterService();

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        // Tweet Content aus Prozessvariable auslesen
        String tweetContent = (String) externalTask.getVariable("tweetContent");

        // Den gemockten Twitter-Service aufrufen, um einen Tweet zu posten (Status updaten)
        twitterService.updateStatus(tweetContent);

        // Den Task erledigen
        externalTaskService.complete(externalTask);
    }

}
