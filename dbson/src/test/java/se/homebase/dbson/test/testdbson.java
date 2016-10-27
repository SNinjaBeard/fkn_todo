package se.homebase.dbson.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.homebase.dbson.Storage;
import se.homebase.dbson.util.MSG;

import java.util.UUID;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by superspaceninja on 2016-10-21.
 * json storage
 */
public class testdbson {

    private static final Logger logger = LoggerFactory.getLogger(testdbson.class);

    Storage storage;

    JsonObject jsonObject = null;
    JsonObject _jsonObject = null;
    JsonObject d_jsonObject = null;
    UUID id = null;
    UUID _id = null;
    UUID d_id = null;
    String name = null;
    String desc = null;
    boolean done;

    @Before
    public void setup() {
        logger.debug("setup()");

        storage = new Storage();

        id = UUID.randomUUID();
        _id = UUID.randomUUID();
        d_id = UUID.randomUUID();
        name = "todo";
        desc = "stuff to do goes here.";
        done = false;

        jsonObject = new JsonObject();
        jsonObject.addProperty("id", id.toString());
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("desc", desc);
        jsonObject.addProperty("done", done);

        d_jsonObject = new JsonObject();
        jsonObject.addProperty("id", d_id.toString());
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("desc", desc);
        jsonObject.addProperty("done", done);

        _jsonObject = new JsonObject();
        _jsonObject.addProperty("id", id.toString());
        _jsonObject.addProperty("name", "name mod");
        _jsonObject.addProperty("desc", "desc mod");
        _jsonObject.addProperty("done", done);

        storage.put(id, jsonObject.toString());
        storage.put(d_id, d_jsonObject.toString());
    }

    @Test
    public void test_db_put() {
        String json = storage.put(_id, jsonObject.toString());
        assertNotEquals(json, MSG.make("ERROR", "Invalid id"));
        assertNotEquals(json, MSG.make("ERROR", "Invalid json"));
    }

    @Test
    public void test_db_update() {
        String json = storage.update(id, _jsonObject.toString());
        assertNotEquals(json, MSG.make("ERROR", "Invalid id"));
        assertNotEquals(json, MSG.make("ERROR", "Invalid json"));
    }

    @Test
    public void test_db_get() {
        String json = storage.get(id);
        assertNotEquals(json, MSG.make("ERROR", "Invalid id"));
        assertNotEquals(json, MSG.make("ERROR", "Invalid json"));
        assertNotEquals(json, MSG.make("ERROR", "No value mapped to provided key"));
    }

    @Test
    public void test_db_delete() {
        String json = storage.remove(d_id);
        assertNotEquals(json, MSG.make("ERROR", "Invalid id"));
    }

    @Test
    public void test_db_get_all() {
        String json = storage.get();
        assertNotNull(json);
    }
}
