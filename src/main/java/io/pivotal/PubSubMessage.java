package io.pivotal;

public class PubSubMessage {

    private final String id;
    private final long pubTime; // Units: milliseconds since 00:00:00 UTC, January 1, 1970
    private final String content;

    public PubSubMessage(String id, long pubTime, String content) {
        this.id = id;
        this.pubTime = pubTime;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public long getPubTime() {
        return pubTime;
    }

    public String getContent() {
        return content;
    }
}
