package com.shiz.flighttime.preference;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.shiz.flighttime.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import io.realm.Realm;
import io.realm.internal.IOException;

/**
 * Created by oldman on 31.08.16.
 */
public class SettingsIteractorImpl implements SettingsIteractor {
    private static final String TAG = SettingsIteractorImpl.class.getSimpleName();
    private Context context;
    private Realm realm = Realm.getDefaultInstance();

    public SettingsIteractorImpl(Context context) {
        this.context = context;
    }


    @Override
    public void exportRealmDB(DataBaseListener listener) {
        File exportRealmFile = getExportFile();
        if (realm.isClosed())
            this.realm = Realm.getDefaultInstance();
        try {
            // create a backup file
            Log.d(TAG, exportRealmFile.getAbsolutePath() + "hash " + +realm.getConfiguration().hashCode());
            // if backup file already exists, delete it
            exportRealmFile.delete();
            // copy current realm to backup file
            try {
                realm.writeCopyTo(exportRealmFile);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String msg = "File exported to Path: " + exportRealmFile.getAbsolutePath() + "new hash " + exportRealmFile.hashCode();
        Log.d(TAG, msg);
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        realm.setAutoRefresh(true);
        realm.close();
        listener.onExportDataBase();
    }

    @Override
    public void importRealmDB(String path, DataBaseListener listener) {
//        String restoreFilePath = EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;
        copyBundledRealmFile(path);
        Log.d(TAG, "Data restore is done" + copyBundledRealmFile(path));
//        Toast.makeText(context, "File - import: " + importPth + " " + importPth.hashCode(), Toast.LENGTH_LONG).show();
        listener.onImportDataBase();
    }

    private String copyBundledRealmFile(final String oldFilePath) {
        try {
            Log.d(TAG, "copyBundledRealmFile " + oldFilePath + " ");
            File oldFile;
            File file = new File(context.getFilesDir() + "/default.realm");

            Log.d(TAG, "file " + file.getAbsolutePath() + " " + file.canWrite());

            FileOutputStream outputStream = new FileOutputStream(file);
            if(oldFilePath == null )
                oldFile = getExportFile();
            else
                oldFile= new File(oldFilePath);
            FileInputStream inputStream = new FileInputStream(oldFile);
            Log.d(TAG, "oldFilePath " + oldFilePath + " " + inputStream.available() + " " + outputStream.toString());

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            Log.d("Settings", file.getAbsolutePath());
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Settings", "error " + e.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("Settings", "error2 " + e.toString());

        } catch (java.io.IOException e) {
            e.printStackTrace();
            Log.d("Settings", "error3 " + e.toString());

        }
        String pathToRestore = realm.getConfiguration().getPath();
        //        Realm.setDefaultConfiguration(MyApplication.mRealmConfig);
        Log.d("Settings", "pathToRestore " + pathToRestore);
        return null;
    }

    private File getExportFile() {
        File file;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            Log.d("Settings", "MEDIA_MOUNTED");
            file = new File(Environment.getExternalStorageDirectory() + File.separator
                    + getAppName() + File.separator + getAppName() + ".realm");
            try {
                file.createNewFile();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        } else {
            /* save the folder in internal memory of phone */
            Log.d("Settings", "!media_mounted");
            file = new File(Environment.getDataDirectory() + File.separator + getAppName() + File.separator + getAppName() + ".realm");
//            if (!file.exists()) {
//                file.mkdirs();
            try {
                file.createNewFile();
            } catch (java.io.IOException e) {
                e.printStackTrace();
//                }

            }
        }
        return file;
    }

    private String getRealmPath() {
        if (realm.isClosed())
            this.realm = Realm.getDefaultInstance();
        return realm.getPath();

    }

    private String getAppName() {
        return context.getResources().getString(R.string.app_name);
    }
}
