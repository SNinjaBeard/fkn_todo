package se.homebase.dbson.util;

import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created by superspaceninja on 2016-11-06.
 * dbsonutils
 */
public final class DBSonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBSonUtil.class);

    private static final String MEMORY = "_memo";
    private static final String FILE = "_file";
    private static final String HISTORY = "_history";

    private DBSonUtil() {}

    public static HTreeMap<UUID, byte[]> makeMemoryMap(DB DB, final String NAME, boolean history) {
        return history
                ? DB.hashMap(NAME + HISTORY + MEMORY, Serializer.UUID, Serializer.BYTE_ARRAY).createOrOpen()
                : DB.hashMap(NAME + MEMORY, Serializer.UUID, Serializer.BYTE_ARRAY).createOrOpen();
    }

    public static HTreeMap<UUID, byte[]> makeFileMap(DB DB, final String NAME, boolean history) {
        return history
                ? DB.hashMap(NAME + HISTORY + FILE, Serializer.UUID, Serializer.BYTE_ARRAY).createOrOpen()
                : DB.hashMap(NAME + FILE, Serializer.UUID, Serializer.BYTE_ARRAY).createOrOpen();
    }

    public static byte[] makeMsg(String type, String message) {
        String msg = "{\"" + type + "\":\"" + message + "\"}";
        LOGGER.info("makeMsg() {}", msg);
        return msg.getBytes();
    }
}
