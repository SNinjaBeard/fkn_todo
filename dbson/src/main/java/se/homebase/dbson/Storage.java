package se.homebase.dbson;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.homebase.dbson.util.MSG;

import java.util.UUID;

/**
 * Created by superspaceninja on 2016-10-21.
 * String storage
 */
public final class Storage {

    private static final Logger LOGGER = LoggerFactory.getLogger(Storage.class);

    private DB dbson_memo;
    private HTreeMap<UUID, byte[]> dbson_memo_map;
    private HTreeMap<UUID, byte[]> dbson_memo_history_map;

    public Storage() {
        dbson_memo = DBMaker.memoryDB().make();
        dbson_memo_map = dbson_memo
                .hashMap("todo_memo", Serializer.UUID, Serializer.BYTE_ARRAY).createOrOpen();
        dbson_memo_history_map = dbson_memo
                .hashMap("todo_history_memo", Serializer.UUID, Serializer.BYTE_ARRAY).createOrOpen();
    }

    /**
     * get all
     *
     * @return all values from map
     */
    public byte[] get() {
        LOGGER.info("get()");

        if (isEmpty()) {
            return MSG.make("ERROR", "No entities in db");
        }

        StringBuilder result = new StringBuilder();

        dbson_memo_map.getValues().forEach(je -> {
            result.append(je).append(",");
            LOGGER.info("fetched: {}", je);
        });

        result.insert(0, "[").replace(result.length() - 1, result.length(), "]");

        LOGGER.info("entities: {} {}", mapSize(), result.toString());

        return result.toString().getBytes();
    }

    /**
     * get stored string by id
     *
     * @param id map key
     * @return map value corresponding to provided key
     */
    public byte[] get(UUID id) {
        LOGGER.info("get()");

        if (id == null || !dbson_memo_map.containsKey(id)) {
            return MSG.make("ERROR", "Invalid id");
        }

        byte[] result = dbson_memo_map.get(id);

        if (result == null) {
            return MSG.make("ERROR", "No value mapped to provided key");
        }

        LOGGER.info("stored string: {}", result);

        return result;
    }

    /**
     * put data in map
     *
     * @param id         map key
     * @param data map data
     * @return true if successfully put, else false
     */
    public byte[] put(UUID id, byte[] data) {
        LOGGER.info("put()");

        if (id == null) {
            LOGGER.error("invalid id!");
            return MSG.make("ERROR", "Invalid id");
        }

        if (data == null) {
            LOGGER.error("invalid data!");
            return MSG.make("ERROR", "Invalid data");
        }

        dbson_memo_map.put(id, data);
        LOGGER.info("stored: {} {}", id.toString(), data);

        return data;
    }

    /**
     * replaces value at key position
     *
     * @param id         map key
     * @param dataUpdate updated map value
     * @return updated json element
     */
    public byte[] update(UUID id, byte[] dataUpdate) {
        LOGGER.info("update()");

        if (id == null || !dbson_memo_map.containsKey(id)) {
            return MSG.make("ERROR", "Invalid id");
        }

        if (dataUpdate == null) {
            return MSG.make("ERROR", "Invalid data");
        }

        dbson_memo_map.replace(id, dataUpdate);
        LOGGER.info("updated: {} {}", id.toString(), dataUpdate);

        return dataUpdate;
    }

    /**
     * remove provided key and corresponding value from map. add removed key and value to history map
     *
     * @param id map key
     * @return removed json element
     */
    public byte[] remove(UUID id) {
        LOGGER.info("remove()");

        if (id == null || !dbson_memo_map.containsKey(id)) {
            return MSG.make("ERROR", "Invalid id");
        }

        byte[] dataEntity = get(id);

        dbson_memo_history_map.put(id, dataEntity);
        dbson_memo_map.remove(id);
        LOGGER.info("history: {} {}", id.toString(), dataEntity);

        return dataEntity;
    }

    //******************************************************
    // INFO
    //******************************************************

    public int mapSize() {
        return dbson_memo_map.size();
    }

    public boolean contains(UUID id) {
        return dbson_memo_map.containsKey(id);
    }

    public boolean isEmpty() {
        return dbson_memo_map.isEmpty();
    }
}
