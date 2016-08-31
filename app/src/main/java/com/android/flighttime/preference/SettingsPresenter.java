package com.android.flighttime.preference;

/**
 * Created by oldman on 31.08.16.
 */
public interface SettingsPresenter {
    String getBackupInformation();
    void updateBackup();
    void importBackup();

}
