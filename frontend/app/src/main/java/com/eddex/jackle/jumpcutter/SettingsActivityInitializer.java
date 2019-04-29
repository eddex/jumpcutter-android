package com.eddex.jackle.jumpcutter;

import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;


public class SettingsActivityInitializer extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }
}