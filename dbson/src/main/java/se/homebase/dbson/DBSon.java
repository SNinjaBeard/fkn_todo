package se.homebase.dbson;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.homebase.dbson.operation.OP;
import se.homebase.dbson.util.DBSonUtil;

import java.util.UUID;

/**
 * Created by superspaceninja on 2016-10-21.
 * byte storage
 */
public final class DBSon {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBSon.class);

    private static String NAME;

    private DB dbson_file;
    private HTreeMap<UUID, byte[]> dbson_map;
    private HTreeMap<UUID, byte[]> dbson_history_map;

    private DBSon() {}

    // TODO: split up methods to classes.
    // TODO: save to file on save.
    // TODO: update memory on file change.
    // TODO: get only from memory

    public DBSon(final String NAME, final String DB_DIR) {

        this.NAME = NAME;

        dbson_file = DBMaker.fileDB(DB_DIR)
                .closeOnJvmShutdown()
                .make();

        dbson_map = DBSonUtil.makeFileMap(dbson_file, NAME, false);
        dbson_history_map = DBSonUtil.makeFileMap(dbson_file, NAME, true);

        LOGGER.info("new DBson(NAME: {}, DB_DIR: {})", NAME, DB_DIR);
    }

    /**
     * get all
     *
     * @return all values from map
     */
    public byte[] get() {
        return OP.get(dbson_map);
    }

    /**
     * get stored string by id
     *
     * @param id map key
     * @return map value corresponding to provided key
     */
    public byte[] get(UUID id) {
        return OP.get(dbson_map, id);
    }

    /**
     * put data in map
     *
     * @param id   map key
     * @param data map data
     * @return true if successfully put, else false
     */
    public byte[] put(UUID id, byte[] data) {
        data = OP.put(dbson_map, id, data);
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
        return OP.update(dbson_map, id, dataUpdate);
    }

    /**
     * remove provided key and corresponding value from map. add removed key and value to history map
     *
     * @param id map key
     * @return removed json element
     */
    public byte[] remove(UUID id) {
        return OP.remove(dbson_map, dbson_history_map, id);
    }

    public int mapSize() {
        return dbson_map.size();
    }

    public boolean contains(UUID id) {
        return dbson_map.containsKey(id);
    }

    public boolean isEmpty() {
        return dbson_map.isEmpty();
    }
}
