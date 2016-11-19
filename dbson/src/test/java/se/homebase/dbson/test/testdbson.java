package se.homebase.dbson.test;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.homebase.dbson.DBSon;
import se.homebase.dbson.util.DBSonUtil;

import java.util.UUID;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by superspaceninja on 2016-10-21.
 * json dBSon
 */
public class testdbson {

    private static final Logger LOGGER = LoggerFactory.getLogger(testdbson.class);

    private static final DBSon dBSon = new DBSon("test", "/home/superspaceninja/workspace/fkn_todo/dbson/database/dbson.db");

    UUID id_1 = UUID.randomUUID();
    UUID id_2 = UUID.randomUUID();
    UUID id_3 = UUID.randomUUID();
    byte[] data_1 = null;
    byte[] data_2 = null;
    byte[] data_3 = null;
    String name = "mock todo";
    String desc = "ipsum lorum";
    boolean done;
    boolean firstRun = false;

    private static final String INVALID_ID = new String(DBSonUtil.makeMsg("ERROR", "Invalid id"));
    private static final String INVALID_DATA = new String(DBSonUtil.makeMsg("ERROR", "Invalid data"));
    private static final String NO_VALUE_MAPPED_TO_PROVIDED_KEY = new String(DBSonUtil.makeMsg("ERROR", "No value mapped to provided key"));

    public byte[] make(UUID id) {
        JsonObject jo = new JsonObject();
        jo.addProperty("id", id.toString());
        jo.addProperty("name", name);
        jo.addProperty("desc", desc);
        jo.addProperty("done", done);
        return jo.toString().getBytes();
    }

    @Before
    public void setup() {
        if (firstRun) {
            data_1 = make(id_1);
            data_2 = make(id_2);
            data_3 = make(id_3);
            LOGGER.info("dBSon empty, saving: {}", new String(data_1));
            data_1 = dBSon.put(id_1, data_1);
            firstRun = false;
        }
    }

    @Test
    public void get() {
        byte[] data = dBSon.get(id_1);
        assertNotEquals(data, INVALID_ID);
        assertNotEquals(data, INVALID_DATA);
        assertNotEquals(data, NO_VALUE_MAPPED_TO_PROVIDED_KEY);
        LOGGER.info("TEST ::: get ::: result: {}", new String(data));
    }

    @Test
    public void put() {
        byte[] data = dBSon.put(id_2, data_2);
        assertNotEquals(data, INVALID_ID);
        assertNotEquals(data, INVALID_DATA);
        LOGGER.info("TEST ::: put ::: result: {}", new String(data));
    }

    @Test
    public void getAll() {
        byte[] data = dBSon.get();
        assertNotNull(data);
        LOGGER.info("TEST ::: getAll ::: result: {}", new String(data));
    }
}
