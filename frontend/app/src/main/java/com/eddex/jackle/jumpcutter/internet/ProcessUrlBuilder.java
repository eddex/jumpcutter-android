package com.eddex.jackle.jumpcutter.internet;

import okhttp3.HttpUrl;

public class ProcessUrlBuilder {

    private String host;
    private String videoId;
    private String silentThreshold;
    private String soundedSpeed;
    private String silentSpeed;
    private String frameMargin;
    private String sampleRate;
    private String frameRate;
    private String frameQuality;

    public ProcessUrlBuilder withHost(String host) {

        this.host = host;
        return this;
    }

    public ProcessUrlBuilder withVideoId(String videoId) {

        this.videoId = videoId;
        return this;
    }

    public ProcessUrlBuilder withSilentThreshold(String silentThreshold) {

        this.silentThreshold = silentThreshold;
        return this;
    }

    public ProcessUrlBuilder withSoundedSpeed(String soundedSpeed) {

        this.soundedSpeed = soundedSpeed;
        return this;
    }

    public ProcessUrlBuilder withSilentSpeed(String silentSpeed) {

        this.silentSpeed = silentSpeed;
        return this;
    }

    public ProcessUrlBuilder withFrameMargin(String frameMargin) {

        this.frameMargin = frameMargin;
        return this;
    }

    public ProcessUrlBuilder withSampleRate(String sampleRate) {

        this.sampleRate = sampleRate;
        return this;
    }

    public ProcessUrlBuilder withFrameRate(String frameRate) {

        this.frameRate = frameRate;
        return this;
    }

    public ProcessUrlBuilder withFrameQuality(String frameQuality) {

        this.frameQuality = frameQuality;
        return this;
    }

    public HttpUrl Build() {

        HttpUrl url = new HttpUrl.Builder()
            .scheme("http")
            .host(this.host)
            .addPathSegment("process")
            .addQueryParameter("video_id", this.videoId)
            .addQueryParameter("silent_threshold", this.silentThreshold)
            .addQueryParameter("sounded_speed", this.soundedSpeed)
            .addQueryParameter("silent_speed", this.silentSpeed)
            .addQueryParameter("frame_margin", this.frameMargin)
            .addQueryParameter("sample_rate", this.sampleRate)
            .addQueryParameter("frame_rate", this.frameRate)
            .addQueryParameter("frame_quality", this.frameQuality)
            .build();

        return url;
    }
}
