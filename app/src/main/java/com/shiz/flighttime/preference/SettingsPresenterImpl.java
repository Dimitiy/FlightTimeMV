package com.shiz.flighttime.preference;

import android.content.Context;

/**
 * Created by oldman on 31.08.16.
 */
public class SettingsPresenterImpl implements SettingsPresenter {

    private Context context;
    private SettingsIteractor iteractor;

    public SettingsPresenterImpl(Context context) {
        this.context = context;
        iteractor = new SettingsIteractorImpl(context);
    }

    @Override
    public String getBackupInformation() {
        return null;
    }

    @Override
    public void updateBackup() {
        iteractor.exportRealmDB();
    }

    @Override
    public void importBackup() {

    }
}
