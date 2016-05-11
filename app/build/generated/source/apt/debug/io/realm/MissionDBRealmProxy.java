package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.android.flighttime.database.FlightDB;
import com.android.flighttime.database.MissionDB;
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

public class MissionDBRealmProxy extends MissionDB
    implements RealmObjectProxy, MissionDBRealmProxyInterface {

    static final class MissionDBColumnInfo extends ColumnInfo {

        public final long cityIndex;
        public final long idIndex;
        public final long dateIndex;
        public final long durationIndex;
        public final long flightDBRealmListIndex;

        MissionDBColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(5);
            this.cityIndex = getValidColumnIndex(path, table, "MissionDB", "city");
            indicesMap.put("city", this.cityIndex);

            this.idIndex = getValidColumnIndex(path, table, "MissionDB", "id");
            indicesMap.put("id", this.idIndex);

            this.dateIndex = getValidColumnIndex(path, table, "MissionDB", "date");
            indicesMap.put("date", this.dateIndex);

            this.durationIndex = getValidColumnIndex(path, table, "MissionDB", "duration");
            indicesMap.put("duration", this.durationIndex);

            this.flightDBRealmListIndex = getValidColumnIndex(path, table, "MissionDB", "flightDBRealmList");
            indicesMap.put("flightDBRealmList", this.flightDBRealmListIndex);

            setIndicesMap(indicesMap);
        }
    }

    private final MissionDBColumnInfo columnInfo;
    private final ProxyState proxyState;
    private RealmList<FlightDB> flightDBRealmListRealmList;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("city");
        fieldNames.add("id");
        fieldNames.add("date");
        fieldNames.add("duration");
        fieldNames.add("flightDBRealmList");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    MissionDBRealmProxy(ColumnInfo columnInfo) {
        this.columnInfo = (MissionDBColumnInfo) columnInfo;
        this.proxyState = new ProxyState(MissionDB.class, this);
    }

    @SuppressWarnings("cast")
    public String realmGet$city() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.cityIndex);
    }

    public void realmSet$city(String value) {
        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            throw new IllegalArgumentException("Trying to set non-nullable field city to null.");
        }
        proxyState.getRow$realm().setString(columnInfo.cityIndex, value);
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

    public RealmList<FlightDB> realmGet$flightDBRealmList() {
        proxyState.getRealm$realm().checkIfValid();
        // use the cached value if available
        if (flightDBRealmListRealmList != null) {
            return flightDBRealmListRealmList;
        } else {
            LinkView linkView = proxyState.getRow$realm().getLinkList(columnInfo.flightDBRealmListIndex);
            flightDBRealmListRealmList = new RealmList<FlightDB>(FlightDB.class, linkView, proxyState.getRealm$realm());
            return flightDBRealmListRealmList;
        }
    }

    public void realmSet$flightDBRealmList(RealmList<FlightDB> value) {
        proxyState.getRealm$realm().checkIfValid();
        LinkView links = proxyState.getRow$realm().getLinkList(columnInfo.flightDBRealmListIndex);
        links.clear();
        if (value == null) {
            return;
        }
        for (RealmModel linkedObject : (RealmList<? extends RealmModel>) value) {
            if (!RealmObject.isValid(linkedObject)) {
                throw new IllegalArgumentException("Each element of 'value' must be a valid managed object.");
            }
            if (((RealmObjectProxy)linkedObject).realmGet$proxyState().getRealm$realm() != proxyState.getRealm$realm()) {
                throw new IllegalArgumentException("Each element of 'value' must belong to the same Realm.");
            }
            links.add(((RealmObjectProxy)linkedObject).realmGet$proxyState().getRow$realm().getIndex());
        }
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_MissionDB")) {
            Table table = transaction.getTable("class_MissionDB");
            table.addColumn(RealmFieldType.STRING, "city", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.INTEGER, "id", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.DATE, "date", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.INTEGER, "duration", Table.NOT_NULLABLE);
            if (!transaction.hasTable("class_FlightDB")) {
                FlightDBRealmProxy.initTable(transaction);
            }
            table.addColumnLink(RealmFieldType.LIST, "flightDBRealmList", transaction.getTable("class_FlightDB"));
            table.addSearchIndex(table.getColumnIndex("id"));
            table.setPrimaryKey("id");
            return table;
        }
        return transaction.getTable("class_MissionDB");
    }

    public static MissionDBColumnInfo validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_MissionDB")) {
            Table table = transaction.getTable("class_MissionDB");
            if (table.getColumnCount() != 5) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 5 but was " + table.getColumnCount());
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < 5; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final MissionDBColumnInfo columnInfo = new MissionDBColumnInfo(transaction.getPath(), table);

            if (!columnTypes.containsKey("city")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'city' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("city") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'city' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.cityIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'city' does support null values in the existing Realm file. Remove @Required or @PrimaryKey from field 'city' or migrate using RealmObjectSchema.setNullable().");
            }
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
            if (!columnTypes.containsKey("flightDBRealmList")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'flightDBRealmList'");
            }
            if (columnTypes.get("flightDBRealmList") != RealmFieldType.LIST) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'FlightDB' for field 'flightDBRealmList'");
            }
            if (!transaction.hasTable("class_FlightDB")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing class 'class_FlightDB' for field 'flightDBRealmList'");
            }
            Table table_4 = transaction.getTable("class_FlightDB");
            if (!table.getLinkTarget(columnInfo.flightDBRealmListIndex).hasSameSchema(table_4)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid RealmList type for field 'flightDBRealmList': '" + table.getLinkTarget(columnInfo.flightDBRealmListIndex).getName() + "' expected - was '" + table_4.getName() + "'");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The MissionDB class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_MissionDB";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static MissionDB createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        MissionDB obj = null;
        if (update) {
            Table table = realm.getTable(MissionDB.class);
            long pkColumnIndex = table.getPrimaryKey();
            long rowIndex = TableOrView.NO_MATCH;
            if (!json.isNull("id")) {
                rowIndex = table.findFirstLong(pkColumnIndex, json.getLong("id"));
            }
            if (rowIndex != TableOrView.NO_MATCH) {
                obj = new MissionDBRealmProxy(realm.schema.getColumnInfo(MissionDB.class));
                ((RealmObjectProxy)obj).realmGet$proxyState().setRealm$realm(realm);
                ((RealmObjectProxy)obj).realmGet$proxyState().setRow$realm(table.getUncheckedRow(rowIndex));
            }
        }
        if (obj == null) {
            if (json.has("id")) {
                if (json.isNull("id")) {
                    obj = (MissionDBRealmProxy) realm.createObject(MissionDB.class, null);
                } else {
                    obj = (MissionDBRealmProxy) realm.createObject(MissionDB.class, json.getInt("id"));
                }
            } else {
                obj = (MissionDBRealmProxy) realm.createObject(MissionDB.class);
            }
        }
        if (json.has("city")) {
            if (json.isNull("city")) {
                ((MissionDBRealmProxyInterface) obj).realmSet$city(null);
            } else {
                ((MissionDBRealmProxyInterface) obj).realmSet$city((String) json.getString("city"));
            }
        }
        if (json.has("id")) {
            if (json.isNull("id")) {
                throw new IllegalArgumentException("Trying to set non-nullable field id to null.");
            } else {
                ((MissionDBRealmProxyInterface) obj).realmSet$id((int) json.getInt("id"));
            }
        }
        if (json.has("date")) {
            if (json.isNull("date")) {
                ((MissionDBRealmProxyInterface) obj).realmSet$date(null);
            } else {
                Object timestamp = json.get("date");
                if (timestamp instanceof String) {
                    ((MissionDBRealmProxyInterface) obj).realmSet$date(JsonUtils.stringToDate((String) timestamp));
                } else {
                    ((MissionDBRealmProxyInterface) obj).realmSet$date(new Date(json.getLong("date")));
                }
            }
        }
        if (json.has("duration")) {
            if (json.isNull("duration")) {
                throw new IllegalArgumentException("Trying to set non-nullable field duration to null.");
            } else {
                ((MissionDBRealmProxyInterface) obj).realmSet$duration((long) json.getLong("duration"));
            }
        }
        if (json.has("flightDBRealmList")) {
            if (json.isNull("flightDBRealmList")) {
                ((MissionDBRealmProxyInterface) obj).realmSet$flightDBRealmList(null);
            } else {
                ((MissionDBRealmProxyInterface) obj).realmGet$flightDBRealmList().clear();
                JSONArray array = json.getJSONArray("flightDBRealmList");
                for (int i = 0; i < array.length(); i++) {
                    com.android.flighttime.database.FlightDB item = FlightDBRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    ((MissionDBRealmProxyInterface) obj).realmGet$flightDBRealmList().add(item);
                }
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    public static MissionDB createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        MissionDB obj = realm.createObject(MissionDB.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("city")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MissionDBRealmProxyInterface) obj).realmSet$city(null);
                } else {
                    ((MissionDBRealmProxyInterface) obj).realmSet$city((String) reader.nextString());
                }
            } else if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field id to null.");
                } else {
                    ((MissionDBRealmProxyInterface) obj).realmSet$id((int) reader.nextInt());
                }
            } else if (name.equals("date")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MissionDBRealmProxyInterface) obj).realmSet$date(null);
                } else if (reader.peek() == JsonToken.NUMBER) {
                    long timestamp = reader.nextLong();
                    if (timestamp > -1) {
                        ((MissionDBRealmProxyInterface) obj).realmSet$date(new Date(timestamp));
                    }
                } else {
                    ((MissionDBRealmProxyInterface) obj).realmSet$date(JsonUtils.stringToDate(reader.nextString()));
                }
            } else if (name.equals("duration")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field duration to null.");
                } else {
                    ((MissionDBRealmProxyInterface) obj).realmSet$duration((long) reader.nextLong());
                }
            } else if (name.equals("flightDBRealmList")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MissionDBRealmProxyInterface) obj).realmSet$flightDBRealmList(null);
                } else {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        com.android.flighttime.database.FlightDB item = FlightDBRealmProxy.createUsingJsonStream(realm, reader);
                        ((MissionDBRealmProxyInterface) obj).realmGet$flightDBRealmList().add(item);
                    }
                    reader.endArray();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static MissionDB copyOrUpdate(Realm realm, MissionDB object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        MissionDB realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(MissionDB.class);
            long pkColumnIndex = table.getPrimaryKey();
            long rowIndex = table.findFirstLong(pkColumnIndex, ((MissionDBRealmProxyInterface) object).realmGet$id());
            if (rowIndex != TableOrView.NO_MATCH) {
                realmObject = new MissionDBRealmProxy(realm.schema.getColumnInfo(MissionDB.class));
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

    public static MissionDB copy(Realm realm, MissionDB newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        MissionDB realmObject = realm.createObject(MissionDB.class, ((MissionDBRealmProxyInterface) newObject).realmGet$id());
        cache.put(newObject, (RealmObjectProxy) realmObject);
        ((MissionDBRealmProxyInterface) realmObject).realmSet$city(((MissionDBRealmProxyInterface) newObject).realmGet$city());
        ((MissionDBRealmProxyInterface) realmObject).realmSet$id(((MissionDBRealmProxyInterface) newObject).realmGet$id());
        ((MissionDBRealmProxyInterface) realmObject).realmSet$date(((MissionDBRealmProxyInterface) newObject).realmGet$date());
        ((MissionDBRealmProxyInterface) realmObject).realmSet$duration(((MissionDBRealmProxyInterface) newObject).realmGet$duration());

        RealmList<FlightDB> flightDBRealmListList = ((MissionDBRealmProxyInterface) newObject).realmGet$flightDBRealmList();
        if (flightDBRealmListList != null) {
            RealmList<FlightDB> flightDBRealmListRealmList = ((MissionDBRealmProxyInterface) realmObject).realmGet$flightDBRealmList();
            for (int i = 0; i < flightDBRealmListList.size(); i++) {
                FlightDB flightDBRealmListItem = flightDBRealmListList.get(i);
                FlightDB cacheflightDBRealmList = (FlightDB) cache.get(flightDBRealmListItem);
                if (cacheflightDBRealmList != null) {
                    flightDBRealmListRealmList.add(cacheflightDBRealmList);
                } else {
                    flightDBRealmListRealmList.add(FlightDBRealmProxy.copyOrUpdate(realm, flightDBRealmListList.get(i), update, cache));
                }
            }
        }

        return realmObject;
    }

    public static MissionDB createDetachedCopy(MissionDB realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        MissionDB standaloneObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (MissionDB)cachedObject.object;
            } else {
                standaloneObject = (MissionDB)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            standaloneObject = new MissionDB();
            cache.put(realmObject, new RealmObjectProxy.CacheData(currentDepth, standaloneObject));
        }
        ((MissionDBRealmProxyInterface) standaloneObject).realmSet$city(((MissionDBRealmProxyInterface) realmObject).realmGet$city());
        ((MissionDBRealmProxyInterface) standaloneObject).realmSet$id(((MissionDBRealmProxyInterface) realmObject).realmGet$id());
        ((MissionDBRealmProxyInterface) standaloneObject).realmSet$date(((MissionDBRealmProxyInterface) realmObject).realmGet$date());
        ((MissionDBRealmProxyInterface) standaloneObject).realmSet$duration(((MissionDBRealmProxyInterface) realmObject).realmGet$duration());

        // Deep copy of flightDBRealmList
        if (currentDepth == maxDepth) {
            ((MissionDBRealmProxyInterface) standaloneObject).realmSet$flightDBRealmList(null);
        } else {
            RealmList<FlightDB> managedflightDBRealmListList = ((MissionDBRealmProxyInterface) realmObject).realmGet$flightDBRealmList();
            RealmList<FlightDB> standaloneflightDBRealmListList = new RealmList<FlightDB>();
            ((MissionDBRealmProxyInterface) standaloneObject).realmSet$flightDBRealmList(standaloneflightDBRealmListList);
            int nextDepth = currentDepth + 1;
            int size = managedflightDBRealmListList.size();
            for (int i = 0; i < size; i++) {
                FlightDB item = FlightDBRealmProxy.createDetachedCopy(managedflightDBRealmListList.get(i), nextDepth, maxDepth, cache);
                standaloneflightDBRealmListList.add(item);
            }
        }
        return standaloneObject;
    }

    static MissionDB update(Realm realm, MissionDB realmObject, MissionDB newObject, Map<RealmModel, RealmObjectProxy> cache) {
        ((MissionDBRealmProxyInterface) realmObject).realmSet$city(((MissionDBRealmProxyInterface) newObject).realmGet$city());
        ((MissionDBRealmProxyInterface) realmObject).realmSet$date(((MissionDBRealmProxyInterface) newObject).realmGet$date());
        ((MissionDBRealmProxyInterface) realmObject).realmSet$duration(((MissionDBRealmProxyInterface) newObject).realmGet$duration());
        RealmList<FlightDB> flightDBRealmListList = ((MissionDBRealmProxyInterface) newObject).realmGet$flightDBRealmList();
        RealmList<FlightDB> flightDBRealmListRealmList = ((MissionDBRealmProxyInterface) realmObject).realmGet$flightDBRealmList();
        flightDBRealmListRealmList.clear();
        if (flightDBRealmListList != null) {
            for (int i = 0; i < flightDBRealmListList.size(); i++) {
                FlightDB flightDBRealmListItem = flightDBRealmListList.get(i);
                FlightDB cacheflightDBRealmList = (FlightDB) cache.get(flightDBRealmListItem);
                if (cacheflightDBRealmList != null) {
                    flightDBRealmListRealmList.add(cacheflightDBRealmList);
                } else {
                    flightDBRealmListRealmList.add(FlightDBRealmProxy.copyOrUpdate(realm, flightDBRealmListList.get(i), true, cache));
                }
            }
        }
        return realmObject;
    }

    @Override
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("MissionDB = [");
        stringBuilder.append("{city:");
        stringBuilder.append(realmGet$city());
        stringBuilder.append("}");
        stringBuilder.append(",");
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
        stringBuilder.append(",");
        stringBuilder.append("{flightDBRealmList:");
        stringBuilder.append("RealmList<FlightDB>[").append(realmGet$flightDBRealmList().size()).append("]");
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
        MissionDBRealmProxy aMissionDB = (MissionDBRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aMissionDB.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aMissionDB.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aMissionDB.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
