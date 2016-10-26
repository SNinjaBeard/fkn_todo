package se.homebase.dbson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.homebase.dbson.util.MSG;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by superspaceninja on 2016-10-21.
 * json storage
 */
public final class Storage {

    private static final Logger LOGGER = LoggerFactory.getLogger(Storage.class);
    private static final JsonParser parser = new JsonParser();
    private static final ObjectMapper mapper = new ObjectMapper();

    private DB dbson_memo;
    private HTreeMap<UUID, String> dbson_memo_map;
    private HTreeMap<UUID, String> dbson_memo_history_map;

    public Storage() {
        dbson_memo = DBMaker.memoryDB().make();
        dbson_memo_map = dbson_memo
                .hashMap("todo_memo", Serializer.UUID, Serializer.STRING).createOrOpen();
        dbson_memo_history_map = dbson_memo
                .hashMap("todo_history_memo", Serializer.UUID, Serializer.STRING).createOrOpen();
    }

    /**
     * get all
     *
     * @return all values from map
     */
    public JsonElement get() {
        LOGGER.info( "get()");

        if (isEmpty()) {
            return MSG.make("ERROR", "No entities in db");
        }

        JsonArray jsonEntities = new JsonArray();

        dbson_memo_map.getValues().forEach(je -> {
            jsonEntities.add(parser.parse(je));
            LOGGER.info("fetched: {}", je);
        });

        LOGGER.info("entities: {} {}", jsonEntities.size(), jsonEntities.toString());

        return jsonEntities;
    }

    /**
     * get stored string by id
     *
     * @param id map key
     * @return map value corresponding to provided key
     */
    public JsonElement get(UUID id) {
        LOGGER.info("get()");

        if (id == null || !dbson_memo_map.containsKey(id)) {
            return MSG.make("ERROR", "Invalid id");
        }

        String storedString = dbson_memo_map.get(id);

        if (storedString == null || storedString.equals("")) {
            return MSG.make("ERROR", "No value mapped to provided key");
        }

        LOGGER.info("stored string: {}", storedString);

        return parser.parse(storedString);
    }

    /**
     * put jsonEntity in map
     *
     * @param id         map key
     * @param jsonEntity map value
     * @return true if successfully put, else false
     */
    public JsonElement put(UUID id, JsonElement jsonEntity) {
        LOGGER.info("put()");

        if (id == null) {
            LOGGER.error("invalid id!");
            return MSG.make("ERROR", "Invalid id");
        }

        if (jsonEntity == null || jsonEntity.isJsonNull()) {
            LOGGER.error("invalid json!");
            return MSG.make("ERROR", "Invalid json");
        }

        dbson_memo_map.put(id, jsonEntity.toString());
        LOGGER.info("stored: {} {}", id.toString(), jsonEntity);

        return jsonEntity;
    }

    /**
     * replaces value at key position
     *
     * @param id         map key
     * @param jsonUpdate updated map value
     * @return updated json element
     */
    public JsonElement update(UUID id, JsonElement jsonUpdate) {
        LOGGER.info("update()");

        if (id == null || !dbson_memo_map.containsKey(id)) {
            return MSG.make("ERROR", "Invalid id");
        }

        if (jsonUpdate == null || jsonUpdate.isJsonNull()) {
            return MSG.make("ERROR", "Invalid json");
        }

        dbson_memo_map.replace(id, jsonUpdate.toString());
        LOGGER.info("updated: {} {}", id.toString(), jsonUpdate);

        return jsonUpdate;
    }

    /**
     * remove provided key and corresponding value from map. add removed key and value to history map
     *
     * @param id map key
     * @return removed json element
     */
    public JsonElement remove(UUID id) {
        LOGGER.info("remove()");

        if (id == null || !dbson_memo_map.containsKey(id)) {
            return MSG.make("ERROR", "Invalid id");
        }

        JsonElement jsonEntity = get(id);

        dbson_memo_history_map.put(id, jsonEntity.toString());
        dbson_memo_map.remove(id);
        LOGGER.info("history: {} {}", id.toString(), jsonEntity);

        return jsonEntity;
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
