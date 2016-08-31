package com.android.flighttime.preference;

import android.view.View;

/**
 * Created by oldman on 30.08.16.
 */
public interface SettingsActivityView {
    void onBackupButtonClick(View view);
    void onImportBackupButtonClick(View view);
    void onSelectFileButtonClick(View view);
}
