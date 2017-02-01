# GCP PubSub Spring Boot REST Service

## Minimal Spring Boot REST service which publishes messages to a Google PubSub topic and fetches them

This is meant to illustrate how to bind a Spring Boot app to an instance of the Google PubSub
service, create a subscription, publish and retrieve messages.

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

