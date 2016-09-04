package com.shiz.flighttime.preference;

/**
 * Created by oldman on 31.08.16.
 */
interface SettingsIteractor {
    void exportRealmDB(DataBaseListener listener);

    void importRealmDB(String path, DataBaseListener listener);

    interface DataBaseListener {
        void onExportDataBase();
        void onImportDataBase();
    }
}
