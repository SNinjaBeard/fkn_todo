package se.homebase.to_fkn_do;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.homebase.dbson.Storage;
import se.homebase.dbson.util.MSG;
import se.homebase.to_fkn_do.json.adapter.TodoAdapter;
import se.homebase.to_fkn_do.model.Todo;

import java.util.UUID;

import static spark.Spark.*;

/**
 * Created by superspaceninja on 2016-10-21.
 * serving todos
 */
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(Todo.class, new TodoAdapter()).create();

    private static final String APPLICATION_JSON = "application/json";
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_CREATED = 201;
    private static final int HTTP_OK = 200;

    public static void main(String[] args) {
        logger.info("main()");

        Storage storage = new Storage();

        //-- GET -- -- --
        get("/get", ((request, response) -> {
            logger.info("/get");
            response.type(APPLICATION_JSON);

            if (storage.isEmpty()) {
                response.status(HTTP_BAD_REQUEST);
                return storage.get();
            }

            response.status(HTTP_OK);
            return storage.get();
        }));

        get("/get/:id", ((request, response) -> {
            logger.info("/get/:id");
            response.type(APPLICATION_JSON);

            if (!storage.contains(UUID.fromString(request.params(":id")))) {
                response.status(HTTP_BAD_REQUEST);
                return storage.get(UUID.fromString(request.params(":id")));
            }

            response.status(HTTP_OK);
            return storage.get(UUID.fromString(request.params(":id")));
        }));

        //-- POST -- -- --
        post("/add", ((request, response) -> {
            logger.info("/add");
            response.type(APPLICATION_JSON);

            if (request.body() == null || request.body().equals("")) {
                response.status(HTTP_BAD_REQUEST);
                return MSG.make("ERROR", "Empty body");
            }

            Todo todo = gson.fromJson(new JsonParser().parse(request.body()), Todo.class);
            JsonObject responseData = storage.put(todo.getId(), gson.toJsonTree(todo, Todo.class)).getAsJsonObject();

            response.status(responseData.has("ERROR") ? HTTP_BAD_REQUEST : HTTP_CREATED);

            return responseData;
        }));

        //-- PUT -- -- --
        put("/update/:id", ((request, response) -> {
            logger.info("/update/:id");
            response.type(APPLICATION_JSON);

            if (!storage.contains(UUID.fromString(request.params(":id")))) {
                response.status(HTTP_BAD_REQUEST);
                return MSG.make("ERROR", "Invalid id");
            }

            if (request.body() == null || request.body().equals("")) {
                response.status(HTTP_BAD_REQUEST);
                return MSG.make("ERROR", "Empty body");
            }

            Todo todo = gson.fromJson(new JsonParser().parse(request.body()), Todo.class);
            JsonObject responseData = storage.update(todo.getId(), gson.toJsonTree(todo, Todo.class)).getAsJsonObject();

            response.status(responseData.has("ERROR") ? HTTP_BAD_REQUEST : HTTP_OK);

            return responseData;
        }));

        //-- DELETE -- -- --
        delete("/remove/:id", ((request, response) -> {
            logger.info("/remove/:id");
            response.type(APPLICATION_JSON);

            if (!storage.contains(UUID.fromString(request.params(":id")))) {
                response.status(HTTP_BAD_REQUEST);
                return MSG.make("ERROR", "Invalid id");
            }

            JsonObject responseData = storage.remove(UUID.fromString(request.params(":id"))).getAsJsonObject();

            response.status(responseData.has("ERROR") ? HTTP_BAD_REQUEST : HTTP_OK);

            return responseData;
        }));
    }
}
