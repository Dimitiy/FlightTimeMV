package com.shiz.flighttime.database;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import io.realm.RealmResults;

/**
 * Created by OldMan on 14.02.2016.
 */
public interface DBInterface {
    void insertMission(String address, Calendar date);

    void insertFlightInMission(final int id, final FlightDB flight);

    void insertFlightInMission(final int id, final Calendar date, final long duration);

    void deleteMission(final int id);

    void deleteFlightInMission(final int id_mission, final int id_flight);

    void updateMission(final int id, final String city, final Date date);

    void updateFlightInMission(final int id_mission, final int id_flight, Date date, long duration);

    RealmResults<MissionDB> getMissionsYear(int year);

    Map<String, Long> getDataOfYear();
}
