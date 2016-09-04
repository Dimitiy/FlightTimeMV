package com.shiz.flighttime.preference;

/**
 * Created by oldman on 31.08.16.
 */
interface SettingsPresenter {
    String getBackupInformation();

    void exportBackup();

    void importBackup();

    void importBackupFromPath(String path);

    void onPropertiesDialog();

}
