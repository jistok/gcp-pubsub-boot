# GCP PubSub Spring Boot REST Service

## Minimal Spring Boot REST service which publishes messages to a Google PubSub topic and fetches them

This is meant to illustrate how to bind a Spring Boot app to an instance of the Google PubSub
service, create a subscription, publish and retrieve messages.

## Prerequisite
Install the GCP tile into your PCF environment, per [this procedure](https://docs.pivotal.io/pivotalcf/1-8/customizing/gcp.html)

## Steps to deploy and run this
1. Edit the [application.properties](./src/main/resources/application.properties), if you like, but any
   change to `pubsub.instance.name` will also have to be applied to the `service_instance` variable
   in [`cf_push_command.sh`](./cf_push_command.sh)
1. Build: `mvn clean package`
1. Run the script [`./cf_push_command.sh`](./cf_push_command.sh), which does the following:
   * Pushes the app
   * Creates an instance of the Google PubSub service
   * Binds your app to this PubSub service instance, with "admin" rights
   * Starts the app

## How to use the app
There are only a few REST methods provided:
* `/topics` will just return a comma separated list of topic names (TODO: JSON-ify this)
* `/send` takes a parameter, `msg`, which contains the message text to publish
* `/fetch` returns a message, and deletes it from the topic


