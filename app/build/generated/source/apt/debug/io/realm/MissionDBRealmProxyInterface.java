package io.realm;


public interface MissionDBRealmProxyInterface {
    public String realmGet$city();
    public void realmSet$city(String value);
    public int realmGet$id();
    public void realmSet$id(int value);
    public java.util.Date realmGet$date();
    public void realmSet$date(java.util.Date value);
    public long realmGet$duration();
    public void realmSet$duration(long value);
    public RealmList<com.android.flighttime.database.FlightDB> realmGet$flightDBRealmList();
    public void realmSet$flightDBRealmList(RealmList<com.android.flighttime.database.FlightDB> value);
}
