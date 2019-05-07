package com.eddex.jackle.jumpcutter.internet;

import okhttp3.HttpUrl;

class ProcessUrlBuilder {

    private String host;
    private String videoId;
    private String silentThreshold;
    private String soundedSpeed;
    private String silentSpeed;
    private String frameMargin;
    private String sampleRate;
    private String frameRate;
    private String frameQuality;

    ProcessUrlBuilder withHost(String host) {

        this.host = host;
        return this;
    }

    ProcessUrlBuilder withVideoId(String videoId) {

        this.videoId = videoId;
        return this;
    }

    ProcessUrlBuilder withSilentThreshold(String silentThreshold) {

        this.silentThreshold = silentThreshold;
        return this;
    }

    ProcessUrlBuilder withSoundedSpeed(String soundedSpeed) {

        this.soundedSpeed = soundedSpeed;
        return this;
    }

    ProcessUrlBuilder withSilentSpeed(String silentSpeed) {

        this.silentSpeed = silentSpeed;
        return this;
    }

    ProcessUrlBuilder withFrameMargin(String frameMargin) {

        this.frameMargin = frameMargin;
        return this;
    }

    ProcessUrlBuilder withSampleRate(String sampleRate) {

        this.sampleRate = sampleRate;
        return this;
    }

    ProcessUrlBuilder withFrameRate(String frameRate) {

        this.frameRate = frameRate;
        return this;
    }

    ProcessUrlBuilder withFrameQuality(String frameQuality) {

        this.frameQuality = frameQuality;
        return this;
    }

    HttpUrl build() {

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
            .scheme("https")
            .host(this.host)
            .addPathSegment("process");

        if (this.videoId != null) urlBuilder.addQueryParameter("video_id", this.videoId);
        if (this.silentThreshold != null) urlBuilder.addQueryParameter("silent_threshold", this.silentThreshold);
        if (this.soundedSpeed != null) urlBuilder.addQueryParameter("sounded_speed", this.soundedSpeed);
        if (this.silentSpeed != null) urlBuilder.addQueryParameter("silent_speed", this.silentSpeed);
        if (this.frameMargin != null) urlBuilder.addQueryParameter("frame_margin", this.frameMargin);
        if (this.sampleRate != null) urlBuilder.addQueryParameter("sample_rate", this.sampleRate);
        if (this.frameRate != null) urlBuilder.addQueryParameter("frame_rate", this.frameRate);
        if (this.frameQuality != null) urlBuilder.addQueryParameter("frame_quality", this.frameQuality);

        return urlBuilder.build();
    }
}
