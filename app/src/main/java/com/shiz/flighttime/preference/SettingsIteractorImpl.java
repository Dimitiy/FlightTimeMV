package com.shiz.flighttime.preference;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.shiz.flighttime.R;
import com.shiz.flighttime.MyApplication;

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

    public SettingsIteractorImpl(Context context) {
        this.context = context;
    }


    @Override
    public void exportRealmDB() {
        Realm realm = MyApplication.getRealm();

        String realmPath = new File(context.getFilesDir(), getAppName() + ".realm").getAbsolutePath();
        try {
            // create a backup file
            File exportRealmFile = getExportFile();

            // if backup file already exists, delete it
            exportRealmFile.delete();

            // copy current realm to backup file
            try {
                if (exportRealmFile.canWrite())
                    realm.writeCopyTo(exportRealmFile);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        realm.close();

    }

    @Override
    public void importRealmDB() {
//        String restoreFilePath = EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;

//        copyBundledRealmFile(restoreFilePath);
        Log.d(TAG, "Data restore is done");
    }

    private String copyBundledRealmFile(String oldFilePath) {
        try {
            File file = getRealmPath();

            FileOutputStream outputStream = new FileOutputStream(file);

            FileInputStream inputStream = new FileInputStream(new File(oldFilePath));

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File getExportFile() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                    + getAppName(), getAppName() + ".realm");
        } else {
            /* save the folder in internal memory of phone */
            return new File(Environment.getDataDirectory().getAbsolutePath(), getAppName() + ".realm");
        }
    }

    private File getRealmPath() {
        return new File(context.getFilesDir(), getAppName() + ".realm");

    }

    private String getAppName() {
        return context.getResources().getString(R.string.app_name);
    }
}
