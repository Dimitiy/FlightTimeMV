package com.android.flighttime.database;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import io.realm.RealmResults;

/**
 * Created by OldMan on 14.02.2016.
 */
public interface DBInterface {
    public void insertMission(String address, Calendar date);

    public void insertFlightInMission(final int id, final FlightDB flight);

    public void insertFlightInMission(final int id, final Calendar date, final long duration);

    public void deleteMission(final int id);

    public void deleteFlightInMission(final int id_mission, final int id_flight);

    public void updateMission(final int id, final String city, final Date date);

    public void updateFlightInMission(final int id_mission, final int id_flight,  Date date, long duration);

    public RealmResults<MissionDB> getMissionsYear(int year);

    public Map<String, Long> getDataOfYear();
}
