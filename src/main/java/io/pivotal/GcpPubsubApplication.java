package io.pivotal;

import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.pubsub.PubSub;
import com.google.cloud.pubsub.PubSubOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

@SpringBootApplication
public class GcpPubsubApplication {

    private static final Logger logger =
            LoggerFactory.getLogger(GcpPubsubApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GcpPubsubApplication.class, args);
    }

	/*
     * References:
	 *
	 * https://github.com/GoogleCloudPlatform/google-cloud-java/tree/master/google-cloud-pubsub
	 * https://github.com/GoogleCloudPlatform/google-cloud-java/issues/1430
	 *
	 * Caveats:
	 * (1) If running via IntelliJ, you will get a "DEADLINE_EXCEEDED" error (just run on command line)
	 * (2) If you use the PubSubOptions.newBuilder()... approach (below), you may just hit a
	 *     "PERMISSION_DENIED" error.  For now, the only way I could get past that was to use the
	 *     alternative, PubSubOptions.getDefaultInstance().getService()
	 *
	 */
    @Bean
    public PubSub pubSubCloud(
            @Value("${vcap.services.${pubsub.instance.name}.credentials.PrivateKeyData}") String privateKeyData,
            @Value("${vcap.services.${pubsub.instance.name}.credentials.ProjectId}") String projectId)
            throws Exception {
        PubSub rv;
        String json = new String(Base64.getDecoder().decode(privateKeyData), "UTF-8");
        //logger.info("JSON: " + json);
        InputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(privateKeyData));
        Credentials cred = ServiceAccountCredentials.fromStream(in);
        // The approach below, the newBuilder()... way, fails with "PERMISSION_DENIED" errors
        rv = PubSubOptions.newBuilder().setProjectId(projectId).setCredentials(cred).build().getService();
        logger.info("Just got a new PubSub instance");
        // Ref. for below: https://github.com/GoogleCloudPlatform/python-docs-samples/issues/697
        /*
         * For the approach below to work, you must dump that JSON (see above) into a file, then
         * set the environment variable GOOGLE_APPLICATION_CREDENTIALS to the location of the file.
         */
        //rv = PubSubOptions.getDefaultInstance().getService();
        return rv;
    }

}
