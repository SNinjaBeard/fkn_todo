package se.homebase.to_fkn_do.json.adapter;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.homebase.to_fkn_do.model.Todo;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by superspaceninja on 2016-10-22.
 * todojson converter
 */
public class TodoAdapter implements JsonSerializer<Todo>, JsonDeserializer<Todo> {

    private static final Logger logger = LoggerFactory.getLogger(TodoAdapter.class);

    @Override
    public Todo deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        logger.info("deserialize()");

        if (jsonElement == null || jsonElement.isJsonNull() || !jsonElement.isJsonObject()) {
            logger.info("Invalid json");
            throw new RuntimeException("Invalid json");
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Todo todo = new Todo();

        todo.setId(jsonObject.has("id") ? UUID.fromString(jsonObject.get("id").getAsString()) : UUID.randomUUID());
        todo.setName(jsonObject.has("name") ? jsonObject.get("name").getAsString() : "empty");
        todo.setDesc(jsonObject.has("desc") ? jsonObject.get("desc").getAsString() : "empty");
        todo.setDone(jsonObject.has("done") && jsonObject.get("done").getAsBoolean());

        return todo;
    }

    @Override
    public JsonElement serialize(Todo todo, Type type, JsonSerializationContext jsonSerializationContext) {
        logger.info("serialize()");

        JsonObject json = new JsonObject();

        json.addProperty("id", todo.getId().toString());
        json.addProperty("name", todo.getName());
        json.addProperty("desc", todo.getDesc());
        json.addProperty("done", String.valueOf(todo.isDone()));

        return json;
    }
}
