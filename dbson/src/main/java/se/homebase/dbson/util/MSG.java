package se.homebase.dbson.util;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by superspaceninja on 2016-10-22.
 * generates json MSG
 */
public final class MSG {

    private static final Logger logger = LoggerFactory.getLogger(MSG.class);

    private MSG() {}

    public static String make(String type, String message) {
        logger.info("make()");
        return "{\"" + type + "\":\"" + message + "\"}";
    }
}
