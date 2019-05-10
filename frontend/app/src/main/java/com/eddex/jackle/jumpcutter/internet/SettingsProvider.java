package com.eddex.jackle.jumpcutter.internet;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsProvider {
    SharedPreferences preferences;

    public SettingsProvider(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getSoundSpeed() {
        return this.preferences.getString("list_preference_sound_speed", null);
    }

    public String getSilenceSpeed() {
        return this.preferences.getString("list_preference_silence_speed", null);
    }

    public String getSilenceThreshold() {
        return this.preferences.getString("list_preference_silence_threshold", null);
    }

    public String getFrameMargin() {
        return this.preferences.getString("list_preference_frame_margin", null);
    }

    public Boolean getAdvancedOptionsSwitchEnabled() {
        return this.preferences.getBoolean("advanced_options_switch", false);
    }

    public String getSampleRate() {
        return this.preferences.getString("list_preference_sample_rate", null);
    }

    public String getFrameQuality() {
        return this.preferences.getString("list_preference_frame_quality", null);
    }

    public String getFrameRate() {
        return this.preferences.getString("list_preference_frame_rate", null);
    }
}
