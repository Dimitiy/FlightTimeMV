package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.android.flighttime.database.FlightDB;
import io.realm.RealmFieldType;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.LinkView;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.TableOrView;
import io.realm.internal.android.JsonUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FlightDBRealmProxy extends FlightDB
    implements RealmObjectProxy, FlightDBRealmProxyInterface {

    static final class FlightDBColumnInfo extends ColumnInfo {

        public final long idIndex;
        public final long dateIndex;
        public final long durationIndex;

        FlightDBColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(3);
            this.idIndex = getValidColumnIndex(path, table, "FlightDB", "id");
            indicesMap.put("id", this.idIndex);

            this.dateIndex = getValidColumnIndex(path, table, "FlightDB", "date");
            indicesMap.put("date", this.dateIndex);

            this.durationIndex = getValidColumnIndex(path, table, "FlightDB", "duration");
            indicesMap.put("duration", this.durationIndex);

            setIndicesMap(indicesMap);
        }
    }

    private final FlightDBColumnInfo columnInfo;
    private final ProxyState proxyState;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("date");
        fieldNames.add("duration");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    FlightDBRealmProxy(ColumnInfo columnInfo) {
        this.columnInfo = (FlightDBColumnInfo) columnInfo;
        this.proxyState = new ProxyState(FlightDB.class, this);
    }

    @SuppressWarnings("cast")
    public int realmGet$id() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.idIndex);
    }

    public void realmSet$id(int value) {
        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.idIndex, value);
    }

    @SuppressWarnings("cast")
    public Date realmGet$date() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.util.Date) proxyState.getRow$realm().getDate(columnInfo.dateIndex);
    }

    public void realmSet$date(Date value) {
        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            throw new IllegalArgumentException("Trying to set non-nullable field date to null.");
        }
        proxyState.getRow$realm().setDate(columnInfo.dateIndex, value);
    }

    @SuppressWarnings("cast")
    public long realmGet$duration() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.durationIndex);
    }

    public void realmSet$duration(long value) {
        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.durationIndex, value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_FlightDB")) {
            Table table = transaction.getTable("class_FlightDB");
            table.addColumn(RealmFieldType.INTEGER, "id", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.DATE, "date", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.INTEGER, "duration", Table.NOT_NULLABLE);
            table.addSearchIndex(table.getColumnIndex("id"));
            table.setPrimaryKey("id");
            return table;
        }
        return transaction.getTable("class_FlightDB");
    }

    public static FlightDBColumnInfo validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_FlightDB")) {
            Table table = transaction.getTable("class_FlightDB");
            if (table.getColumnCount() != 3) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 3 but was " + table.getColumnCount());
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < 3; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final FlightDBColumnInfo columnInfo = new FlightDBColumnInfo(transaction.getPath(), table);

            if (!columnTypes.containsKey("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'id' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("id") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'int' for field 'id' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.idIndex) && table.findFirstNull(columnInfo.idIndex) != TableOrView.NO_MATCH) {
                throw new IllegalStateException("Cannot migrate an object with null value in field 'id'. Either maintain the same type for primary key field 'id', or remove the object with null value before migration.");
            }
            if (table.getPrimaryKey() != table.getColumnIndex("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Primary key not defined for field 'id' in existing Realm file. Add @PrimaryKey.");
            }
            if (!table.hasSearchIndex(table.getColumnIndex("id"))) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Index not defined for field 'id' in existing Realm file. Either set @Index or migrate using io.realm.internal.Table.removeSearchIndex().");
            }
            if (!columnTypes.containsKey("date")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'date' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("date") != RealmFieldType.DATE) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'Date' for field 'date' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.dateIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'date' does support null values in the existing Realm file. Remove @Required or @PrimaryKey from field 'date' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("duration")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'duration' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("duration") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'long' for field 'duration' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.durationIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'duration' does support null values in the existing Realm file. Use corresponding boxed type for field 'duration' or migrate using RealmObjectSchema.setNullable().");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The FlightDB class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_FlightDB";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static FlightDB createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        FlightDB obj = null;
        if (update) {
            Table table = realm.getTable(FlightDB.class);
            long pkColumnIndex = table.getPrimaryKey();
            long rowIndex = TableOrView.NO_MATCH;
            if (!json.isNull("id")) {
                rowIndex = table.findFirstLong(pkColumnIndex, json.getLong("id"));
            }
            if (rowIndex != TableOrView.NO_MATCH) {
                obj = new FlightDBRealmProxy(realm.schema.getColumnInfo(FlightDB.class));
                ((RealmObjectProxy)obj).realmGet$proxyState().setRealm$realm(realm);
                ((RealmObjectProxy)obj).realmGet$proxyState().setRow$realm(table.getUncheckedRow(rowIndex));
            }
        }
        if (obj == null) {
            if (json.has("id")) {
                if (json.isNull("id")) {
                    obj = (FlightDBRealmProxy) realm.createObject(FlightDB.class, null);
                } else {
                    obj = (FlightDBRealmProxy) realm.createObject(FlightDB.class, json.getInt("id"));
                }
            } else {
                obj = (FlightDBRealmProxy) realm.createObject(FlightDB.class);
            }
        }
        if (json.has("id")) {
            if (json.isNull("id")) {
                throw new IllegalArgumentException("Trying to set non-nullable field id to null.");
            } else {
                ((FlightDBRealmProxyInterface) obj).realmSet$id((int) json.getInt("id"));
            }
        }
        if (json.has("date")) {
            if (json.isNull("date")) {
                ((FlightDBRealmProxyInterface) obj).realmSet$date(null);
            } else {
                Object timestamp = json.get("date");
                if (timestamp instanceof String) {
                    ((FlightDBRealmProxyInterface) obj).realmSet$date(JsonUtils.stringToDate((String) timestamp));
                } else {
                    ((FlightDBRealmProxyInterface) obj).realmSet$date(new Date(json.getLong("date")));
                }
            }
        }
        if (json.has("duration")) {
            if (json.isNull("duration")) {
                throw new IllegalArgumentException("Trying to set non-nullable field duration to null.");
            } else {
                ((FlightDBRealmProxyInterface) obj).realmSet$duration((long) json.getLong("duration"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    public static FlightDB createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        FlightDB obj = realm.createObject(FlightDB.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field id to null.");
                } else {
                    ((FlightDBRealmProxyInterface) obj).realmSet$id((int) reader.nextInt());
                }
            } else if (name.equals("date")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((FlightDBRealmProxyInterface) obj).realmSet$date(null);
                } else if (reader.peek() == JsonToken.NUMBER) {
                    long timestamp = reader.nextLong();
                    if (timestamp > -1) {
                        ((FlightDBRealmProxyInterface) obj).realmSet$date(new Date(timestamp));
                    }
                } else {
                    ((FlightDBRealmProxyInterface) obj).realmSet$date(JsonUtils.stringToDate(reader.nextString()));
                }
            } else if (name.equals("duration")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field duration to null.");
                } else {
                    ((FlightDBRealmProxyInterface) obj).realmSet$duration((long) reader.nextLong());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static FlightDB copyOrUpdate(Realm realm, FlightDB object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        FlightDB realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(FlightDB.class);
            long pkColumnIndex = table.getPrimaryKey();
            long rowIndex = table.findFirstLong(pkColumnIndex, ((FlightDBRealmProxyInterface) object).realmGet$id());
            if (rowIndex != TableOrView.NO_MATCH) {
                realmObject = new FlightDBRealmProxy(realm.schema.getColumnInfo(FlightDB.class));
                ((RealmObjectProxy)realmObject).realmGet$proxyState().setRealm$realm(realm);
                ((RealmObjectProxy)realmObject).realmGet$proxyState().setRow$realm(table.getUncheckedRow(rowIndex));
                cache.put(object, (RealmObjectProxy) realmObject);
            } else {
                canUpdate = false;
            }
        }

        if (canUpdate) {
            return update(realm, realmObject, object, cache);
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static FlightDB copy(Realm realm, FlightDB newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        FlightDB realmObject = realm.createObject(FlightDB.class, ((FlightDBRealmProxyInterface) newObject).realmGet$id());
        cache.put(newObject, (RealmObjectProxy) realmObject);
        ((FlightDBRealmProxyInterface) realmObject).realmSet$id(((FlightDBRealmProxyInterface) newObject).realmGet$id());
        ((FlightDBRealmProxyInterface) realmObject).realmSet$date(((FlightDBRealmProxyInterface) newObject).realmGet$date());
        ((FlightDBRealmProxyInterface) realmObject).realmSet$duration(((FlightDBRealmProxyInterface) newObject).realmGet$duration());
        return realmObject;
    }

    public static FlightDB createDetachedCopy(FlightDB realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        FlightDB standaloneObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (FlightDB)cachedObject.object;
            } else {
                standaloneObject = (FlightDB)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            standaloneObject = new FlightDB();
            cache.put(realmObject, new RealmObjectProxy.CacheData(currentDepth, standaloneObject));
        }
        ((FlightDBRealmProxyInterface) standaloneObject).realmSet$id(((FlightDBRealmProxyInterface) realmObject).realmGet$id());
        ((FlightDBRealmProxyInterface) standaloneObject).realmSet$date(((FlightDBRealmProxyInterface) realmObject).realmGet$date());
        ((FlightDBRealmProxyInterface) standaloneObject).realmSet$duration(((FlightDBRealmProxyInterface) realmObject).realmGet$duration());
        return standaloneObject;
    }

    static FlightDB update(Realm realm, FlightDB realmObject, FlightDB newObject, Map<RealmModel, RealmObjectProxy> cache) {
        ((FlightDBRealmProxyInterface) realmObject).realmSet$date(((FlightDBRealmProxyInterface) newObject).realmGet$date());
        ((FlightDBRealmProxyInterface) realmObject).realmSet$duration(((FlightDBRealmProxyInterface) newObject).realmGet$duration());
        return realmObject;
    }

    @Override
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("FlightDB = [");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{date:");
        stringBuilder.append(realmGet$date());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{duration:");
        stringBuilder.append(realmGet$duration());
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public ProxyState realmGet$proxyState() {
        return proxyState;
    }

    @Override
    public int hashCode() {
        String realmName = proxyState.getRealm$realm().getPath();
        String tableName = proxyState.getRow$realm().getTable().getName();
        long rowIndex = proxyState.getRow$realm().getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightDBRealmProxy aFlightDB = (FlightDBRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aFlightDB.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aFlightDB.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aFlightDB.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
