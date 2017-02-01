package io.pivotal;

public class PubSubMessage {

    private final String id;
    private final long pubTime;
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
