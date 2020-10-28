package ch.zhaw.gpi.externaltaskclientmocking;

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

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        // Tweet Content aus Prozessvariable auslesen
        String tweetContent = (String) externalTask.getVariable("tweetContent");

        // Tweet posten
        System.out.println("Tweeet wurde veröffentlicht: " + tweetContent);

        // Den Task erledigen
        externalTaskService.complete(externalTask);
    }

}
