package io.realm;


import android.util.JsonReader;
import io.realm.internal.ColumnInfo;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Table;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.flighttime.database.FlightDB;
import com.android.flighttime.database.MissionDB;

@io.realm.annotations.RealmModule
class DefaultRealmModuleMediator extends RealmProxyMediator {

    private static final Set<Class<? extends RealmModel>> MODEL_CLASSES;
    static {
        Set<Class<? extends RealmModel>> modelClasses = new HashSet<Class<? extends RealmModel>>();
        modelClasses.add(MissionDB.class);
        modelClasses.add(FlightDB.class);
        MODEL_CLASSES = Collections.unmodifiableSet(modelClasses);
    }

    @Override
    public Table createTable(Class<? extends RealmModel> clazz, ImplicitTransaction transaction) {
        checkClass(clazz);

        if (clazz.equals(MissionDB.class)) {
            return MissionDBRealmProxy.initTable(transaction);
        } else if (clazz.equals(FlightDB.class)) {
            return FlightDBRealmProxy.initTable(transaction);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public ColumnInfo validateTable(Class<? extends RealmModel> clazz, ImplicitTransaction transaction) {
        checkClass(clazz);

        if (clazz.equals(MissionDB.class)) {
            return MissionDBRealmProxy.validateTable(transaction);
        } else if (clazz.equals(FlightDB.class)) {
            return FlightDBRealmProxy.validateTable(transaction);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public List<String> getFieldNames(Class<? extends RealmModel> clazz) {
        checkClass(clazz);

        if (clazz.equals(MissionDB.class)) {
            return MissionDBRealmProxy.getFieldNames();
        } else if (clazz.equals(FlightDB.class)) {
            return FlightDBRealmProxy.getFieldNames();
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public String getTableName(Class<? extends RealmModel> clazz) {
        checkClass(clazz);

        if (clazz.equals(MissionDB.class)) {
            return MissionDBRealmProxy.getTableName();
        } else if (clazz.equals(FlightDB.class)) {
            return FlightDBRealmProxy.getTableName();
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmModel> E newInstance(Class<E> clazz, ColumnInfo columnInfo) {
        checkClass(clazz);

        if (clazz.equals(MissionDB.class)) {
            return clazz.cast(new MissionDBRealmProxy(columnInfo));
        } else if (clazz.equals(FlightDB.class)) {
            return clazz.cast(new FlightDBRealmProxy(columnInfo));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public Set<Class<? extends RealmModel>> getModelClasses() {
        return MODEL_CLASSES;
    }

    @Override
    public <E extends RealmModel> E copyOrUpdate(Realm realm, E obj, boolean update, Map<RealmModel, RealmObjectProxy> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(MissionDB.class)) {
            return clazz.cast(MissionDBRealmProxy.copyOrUpdate(realm, (MissionDB) obj, update, cache));
        } else if (clazz.equals(FlightDB.class)) {
            return clazz.cast(FlightDBRealmProxy.copyOrUpdate(realm, (FlightDB) obj, update, cache));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmModel> E createOrUpdateUsingJsonObject(Class<E> clazz, Realm realm, JSONObject json, boolean update)
        throws JSONException {
        checkClass(clazz);

        if (clazz.equals(MissionDB.class)) {
            return clazz.cast(MissionDBRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(FlightDB.class)) {
            return clazz.cast(FlightDBRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmModel> E createUsingJsonStream(Class<E> clazz, Realm realm, JsonReader reader)
        throws IOException {
        checkClass(clazz);

        if (clazz.equals(MissionDB.class)) {
            return clazz.cast(MissionDBRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(FlightDB.class)) {
            return clazz.cast(FlightDBRealmProxy.createUsingJsonStream(realm, reader));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmModel> E createDetachedCopy(E realmObject, int maxDepth, Map<RealmModel, RealmObjectProxy.CacheData<RealmModel>> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) realmObject.getClass().getSuperclass();

        if (clazz.equals(MissionDB.class)) {
            return clazz.cast(MissionDBRealmProxy.createDetachedCopy((MissionDB) realmObject, 0, maxDepth, cache));
        } else if (clazz.equals(FlightDB.class)) {
            return clazz.cast(FlightDBRealmProxy.createDetachedCopy((FlightDB) realmObject, 0, maxDepth, cache));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

}
