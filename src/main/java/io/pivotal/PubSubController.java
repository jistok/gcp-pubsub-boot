package io.pivotal;

import com.google.cloud.Page;
import com.google.cloud.pubsub.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;

@RestController
public class PubSubController {

    private final PubSub pubSub;
    private final String topicName;
    private final String subscriptionName;

    private static final Logger logger =
            LoggerFactory.getLogger(PubSubController.class);

    @Autowired
    public PubSubController(
            PubSub pubSub,
            @Value("${vcap.services.${pubsub.instance.name}.credentials.topic_name}") String topicName,
            @Value("${pubsub.subscription.name}") String subscriptionName
    ) {
        this.pubSub = pubSub;
        this.topicName = topicName;
        this.subscriptionName = subscriptionName;
        logger.info("Topic name: " + topicName + "\n" + "Subscription name: " + subscriptionName);
    }

    // Consume from the PubSub topic (fetch first message)
    // Ref. https://cloud.google.com/pubsub/docs/subscriber#pubsub-create-subscription-java
    @RequestMapping("/fetch")
    public PubSubMessage fetch() {
        Subscription subscription = pubSub.getSubscription(subscriptionName);
        if (subscription == null) {
            logger.info("Subscription is null.  Creating one (name: " + subscriptionName + ")");
            subscription = pubSub.create(SubscriptionInfo.of(topicName, subscriptionName));
            String subStatus = (subscription == null ? "FAILED" : "SUCCESS");
            logger.info("Status: " + subStatus);
        } else {
            logger.info("Got subscription \"" + subscriptionName + "\"");
        }
        // Pick up and return at most one message per REST call
        Iterator<ReceivedMessage> messageIterator = subscription.pull(1);
        PubSubMessage rv;
        if (messageIterator.hasNext()) {
            ReceivedMessage recd = messageIterator.next();
            String id = recd.getId();
            long pubTime = recd.getPublishTime();
            rv = new PubSubMessage(id, pubTime, recd.getPayloadAsString());
            recd.ack();
        } else {
            rv = new PubSubMessage("none", -1, "No Message in Queue");
        }
        return rv;
    }

    // Produce a message to the PubSub topic
    @RequestMapping("/send")
    public String send(@RequestParam(value = "msg", defaultValue = "Default Message") String msg) {
        logger.info("Sending \"" + msg + "\"");
        Message message = Message.of(msg);
        String msgId = pubSub.publish(topicName, message);
        return msgId;
    }

    // Get list of topics
    @RequestMapping("/topics")
    public String topics() {
        String rv = "";
        Page<Topic> topics = pubSub.listTopics(PubSub.ListOption.pageSize(100));
        Iterator<Topic> topicIterator = topics.iterateAll();
        while (topicIterator.hasNext()) {
            Topic topic = topicIterator.next();
            if (rv.length() > 0)
                rv += ", ";
            rv += topic.getName();
        }
        return rv;
    }

}
