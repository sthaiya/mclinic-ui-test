package com.mclinic.view.sample.activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.mclinic.view.sample.R;
import com.mclinic.view.sample.utilities.Constants;
import com.mclinic.view.sample.utilities.FileUtils;

public class PreferencesActivity extends PreferenceActivity
        implements
        OnSharedPreferenceChangeListener {

    public static String KEY_SERVER = "server";
    public static String KEY_USERNAME = "username";
    public static String KEY_PASSWORD = "password";
    public static String KEY_SAVED_SEARCH = "saved_search";

    public static String KEY_PROVIDER = "provider";
    public static String KEY_COHORT = "cohort";
    public static String KEY_FIRST_RUN = "firstRun";
    public static String KEY_INFO = "info";
    public static String KEY_USE_SAVED_SEARCHES = "use_saved_searches";
    public static String KEY_CODE = "code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.server_preferences);

        // Display app version number for user to know
        PreferenceScreen ps = (PreferenceScreen) findPreference(KEY_INFO);
        ps.setTitle(getString(R.string.app_name) + " v"
                + getString(R.string.app_version));

        setTitle(getString(R.string.app_name) + " > "
                + getString(R.string.server_preferences));

        if (!FileUtils.storageReady()) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_storage), Toast.LENGTH_LONG).show();
            finish();
        }

        updateServer();
        updateUsername();
        updatePassword();
        updateSavedSearch();
        updateProvider();
        updateCode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(KEY_SERVER)) {
            updateServer();
        } else if (key.equals(KEY_USERNAME)) {
            updateUsername();
        } else if (key.equals(KEY_PASSWORD)) {
            updatePassword();
        } else if (key.equals(KEY_SAVED_SEARCH)) {
            updateSavedSearch();
        } else if (key.equals(KEY_PROVIDER)) {
            updateProvider();
        } else if (key.equals(KEY_CODE)) {
            updateCode();
        }
    }

    private void updateServer() {
        EditTextPreference etp = (EditTextPreference) this
                .getPreferenceScreen().findPreference(KEY_SERVER);
        String s = etp.getText();
        if (s.endsWith("/")) {
            s = s.substring(0, s.lastIndexOf("/"));
        }
        etp.setSummary(s);

    }

    private void updateUsername() {
        EditTextPreference etp = (EditTextPreference) this
                .getPreferenceScreen().findPreference(KEY_USERNAME);
        etp.setSummary(etp.getText());
    }

    private void updatePassword() {
        EditTextPreference etp = (EditTextPreference) this
                .getPreferenceScreen().findPreference(KEY_PASSWORD);
        etp.setSummary(etp.getText().replaceAll(".", "*"));
    }

    private void updateSavedSearch() {
        EditTextPreference etp = (EditTextPreference) this
                .getPreferenceScreen().findPreference(KEY_SAVED_SEARCH);
        etp.setSummary(etp.getText());
    }

    private void updateProvider() {
        EditTextPreference etp = (EditTextPreference) this
                .getPreferenceScreen().findPreference(KEY_PROVIDER);
        etp.setSummary(etp.getText());
    }

    private void updateCode() {
        EditTextPreference etp = (EditTextPreference) this.getPreferenceScreen()
                .findPreference(KEY_CODE);
        if (etp.getText() != null) {
            // if preference isn't all numbers, don't allow it.
            if (!etp.getText().toString().matches(Constants.CODE_REGEX)) {
                etp.setText("");
                etp.setSummary(getString(R.string.invalid_code));
            } else {
                etp.setSummary(etp.getText().replaceAll(".", "*"));
            }
        }
    }
}