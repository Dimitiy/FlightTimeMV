package com.android.flighttime.database;

import android.content.Context;
import android.util.Log;

import com.android.flighttime.utils.DateFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static io.realm.Realm.Transaction;
import static io.realm.Realm.getDefaultInstance;

/**
 * Created by OldMan on 14.02.2016.
 */
public class DBHelper implements DBInterface {
    private Context mContext;
    private Realm realm;
    private String TAG = "DBHelper";
    private RealmAsyncTask transaction;

    public DBHelper(Context ctx) {
        this.mContext = ctx;
        realm = getDefaultInstance();

    }
    public void onStop() {
        if (transaction != null && !transaction.isCancelled()) {
            transaction.cancel();
        }
    }


    public void addListener(RealmChangeListener listener) {
        realm.addChangeListener(listener);
    }

    @Override
    public void insertMission(final String city, final Calendar date, final Calendar time) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d(DBHelper.class.getCanonicalName().toString(), "start insertFlightInMission, city: " + city + " " + date.toString() + " " + time);

                MissionDB mission = realm.createObject(MissionDB.class);
                mission.setId(getPrimaryKey(mission));
                mission.setCity(city);
                mission.setDate(date.getTime());
                mission.setDuration(DateFormatter.getCountMinute(time));
                Log.d(TAG, "insertMission");


            }
        });
    }
    public void insertFlightInMission(final int id, final Calendar date, final Calendar time) {
        realm.executeTransaction(new Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d(DBHelper.class.getCanonicalName().toString(), "start insertFlightInMission" + time + " " + date.toString() + " " + id);
                long duration = DateFormatter.getCountMinute(time);
                FlightDB flight = realm.createObject(FlightDB.class);
                flight.setId(getPrimaryKey(flight));
                flight.setDate(date.getTime());
                flight.setDuration(duration);
                MissionDB mission = realm.where(MissionDB.class)
                        .equalTo("id", id)
                        .findFirst();
                mission.getFlightDBRealmList().add(flight);
                mission.setDuration(mission.getDuration() + duration);
                Log.d(DBHelper.class.getCanonicalName().toString(), "insertFlightInMission" + mission.getDuration());

            }
        });
    }

    public void insertFlightInMission(final int id, final FlightDB flight) {
        realm.executeTransactionAsync(new Transaction() {
            @Override
            public void execute(Realm realm) {
                MissionDB mission = realm.where(MissionDB.class)
                        .equalTo("id", id)
                        .findFirst();
                mission.getFlightDBRealmList().add(flight);
                mission.setDuration(mission.getDuration() + flight.getDuration());
                Log.d(DBHelper.class.getCanonicalName().toString(), "insertFlightInMission" + mission.getId());

            }
        });
    }

    public void deleteMission(final int id) {
        realm.executeTransactionAsync(new Transaction() {
            @Override
            public void execute(Realm realm) {
                MissionDB results = realm.where(MissionDB.class).equalTo("id", id).findFirst();
                results.deleteFromRealm();
                Log.d(DBHelper.class.getCanonicalName().toString(), "deleteMission " + id);

            }
        });
    }

    public void deleteFlightInMission(final int id_mission, final int id_flight) {
        realm.executeTransactionAsync(new Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d("DBHelper", "mission" + id_mission + "flight" + id_flight);
                MissionDB mission = realm.where(MissionDB.class).equalTo("id", id_mission).findFirst();
                FlightDB db = mission.getFlightDBRealmList().where().equalTo("id", id_flight).findFirst();
                db.deleteFromRealm();
            }
        });
    }

    public void updateFlightInMission(final int id_mission, final int id_flight, final long duration, final Date date) {
        realm.executeTransaction(new Transaction() {
            @Override
            public void execute(Realm realm) {
                MissionDB mission = realm.where(MissionDB.class).equalTo("id_mission", true).findFirst();
                Log.d("DBHelper", "updateFlightInMission" + mission.getFlightDBRealmList().get(id_flight) + "");
                mission.getFlightDBRealmList().get(id_flight).setDate(date);
                mission.getFlightDBRealmList().get(id_flight).setDuration(duration);
            }
        });
    }

    public void updateMission(final int id, final String city, final Date date, final long duration_mission) {
        realm.executeTransaction(new Transaction() {
            @Override
            public void execute(Realm realm) {
                // begin & end transcation calls are done for you
                MissionDB mission = realm.where(MissionDB.class)
                        .equalTo("id", id)
                        .findFirst();
                mission.setDate(date);
                mission.setCity(city);
                mission.setDuration(duration_mission);
                Log.d("DBHelper", "updateMission");

            }
        });
    }

    public RealmResults<MissionDB> getMissionsYear(int year) {
        Log.d("DBHelper", "getMissionsPerYear" + String.valueOf(year));
        GregorianCalendar calendarFrom = new GregorianCalendar(year,
                Calendar.JANUARY, 1);
        GregorianCalendar calendarTo = new GregorianCalendar(year,
                Calendar.DECEMBER, 31);
        RealmResults<MissionDB> result = realm.where(MissionDB.class)
                .between("date", calendarFrom.getTime(), calendarTo.getTime())
                .findAll();
        return result;
    }

    public Map<String, Long> getDataOfYear() {
        RealmResults<MissionDB> result = realm.where(MissionDB.class).findAll();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        long duration = 0;
        Map<String, Long> data = new HashMap<String, Long>();
        if (!result.isEmpty())
            for (final MissionDB c : result) {

                String year = df.format(c.getDate());
                duration = c.getDuration();
                if (data.containsKey(year))
                    data.put(year, data.get(year) + duration);
                else
                    data.put(year, duration);
            }
        return data;
    }

    public int getPrimaryKey(RealmObject object) {
        return realm.where(object.getClass()).max("id").intValue() + 1;
    }

    public void closeRealm(RealmChangeListener listener) {
        realm.removeChangeListener(listener);
        realm.close();
    }
}
