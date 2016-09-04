package com.shiz.flighttime.preference;

import android.view.View;

import com.github.angads25.filepicker.model.DialogProperties;

/**
 * Created by oldman on 30.08.16.
 */
interface SettingsActivityView {
    void onBackupButtonClick(View view);

    void onImportBackupButtonClick(View view);

    void onSelectFileButtonClick(View view);

    void onCreateDialogPicker(DialogProperties properties);

    void onRestartApplication();


}
