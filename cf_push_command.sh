#!/bin/bash

app_name="gcp-pubsub-demo"
service_instance="pubsub" # Must also be set as "pubsub.instance.name" in application.properties

# Push the app (you must first authenticate to your PCF environment)
cf push $app_name -n pubsub-demo -b java_buildpack_offline -p ./target/gcp-pubsub-0.0.1-SNAPSHOT.jar --no-start

# Create an instance of the Google PubSub service (GCP service broker must be installed in your PCF environment)
cf cs google-pubsub default $service_instance

# Bind your app to this PubSub service instance, with "admin" rights
cf bs $app_name $service_instance -c '{"role": "pubsub.admin" }'

# Start the app
cf start $app_name

