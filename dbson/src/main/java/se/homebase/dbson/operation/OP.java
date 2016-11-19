package se.homebase.dbson.operation;

import org.mapdb.HTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.homebase.dbson.util.DBSonUtil;

import java.util.UUID;

/**
 * Created by superspaceninja on 2016-11-06.
 * OP methods
 */
public final class OP {

    private static final Logger LOGGER = LoggerFactory.getLogger(OP.class);

    /**
     * get all
     *
     * @return all values from map
     */
    public static byte[] get(HTreeMap<UUID, byte[]> map) {
        LOGGER.info("get()");

        if (map.isEmpty()) {
            return DBSonUtil.makeMsg("ERROR", "No entities in db");
        }

        StringBuilder result = new StringBuilder();

        map.getValues().forEach(je -> {
            result.append(new String(je)).append(",");
            LOGGER.info("fetched: {}", new String(je));
        });

        result.insert(0, "[").replace(result.length() - 1, result.length(), "]");

        LOGGER.info("entities: {} {}", map.size(), result.toString());

        return result.toString().getBytes();
    }

    /**
     * get stored string by id
     *
     * @param id map key
     * @return map value corresponding to provided key
     */
    public static byte[] get(HTreeMap<UUID, byte[]> map, UUID id) {
        LOGGER.info("get()");

        if (id == null || !map.containsKey(id)) {
            return DBSonUtil.makeMsg("ERROR", "Invalid id");
        }

        byte[] result = map.get(id);

        if (result == null) {
            return DBSonUtil.makeMsg("ERROR", "No value mapped to provided key");
        }

        LOGGER.info("stored string: {}", new String(result));

        return result;
    }

    /**
     * put data in map
     *
     * @param id         map key
     * @param data map data
     * @return true if successfully put, else false
     */
    public static byte[] put(HTreeMap<UUID, byte[]> map, UUID id, byte[] data) {
        LOGGER.info("put()");

        if (id == null) {
            LOGGER.error("invalid id!");
            return DBSonUtil.makeMsg("ERROR", "Invalid id");
        }

        if (data == null) {
            LOGGER.error("invalid data!");
            return DBSonUtil.makeMsg("ERROR", "Invalid data");
        }

        map.put(id, data);
        LOGGER.info("stored: {} {}", id.toString(), new String(data));

        return data;
    }

    /**
     * replaces value at key position
     *
     * @param id         map key
     * @param dataUpdate updated map value
     * @return updated json element
     */
    public static byte[] update(HTreeMap<UUID, byte[]> map, UUID id, byte[] dataUpdate) {
        LOGGER.info("update()");

        if (id == null || !map.containsKey(id)) {
            return DBSonUtil.makeMsg("ERROR", "Invalid id");
        }

        if (dataUpdate == null) {
            return DBSonUtil.makeMsg("ERROR", "Invalid data");
        }

        map.replace(id, dataUpdate);
        LOGGER.info("updated: {} {}", id.toString(), new String(dataUpdate));

        return dataUpdate;
    }

    /**
     * remove provided key and corresponding value from map. add removed key and value to history map
     *
     * @param id map key
     * @return removed json element
     */
    public static byte[] remove(HTreeMap<UUID, byte[]> map, HTreeMap<UUID, byte[]> history_map, UUID id) {
        LOGGER.info("remove()");

        if (id == null || !map.containsKey(id)) {
            return DBSonUtil.makeMsg("ERROR", "Invalid id");
        }

        byte[] dataEntity = get(map, id);

        history_map.put(id, dataEntity);
        map.remove(id);

        LOGGER.info("history: {} {}", id.toString(), new String(dataEntity));

        return dataEntity;
    }
}
