package se.homebase.to_fkn_do.model;

import se.homebase.to_fkn_do.util.Validable;

import java.util.UUID;

/**
 * Created by superspaceninja on 2016-10-22.
 * todoitem
 */
public class Todo implements Validable {

    private UUID id;
    private String name;
    private String desc;
    private boolean done;

    public Todo() {}

    public Todo(UUID id, String name, String desc, boolean done) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.done = done;
    }

    public boolean isValid() {
        return id != null && name != null && !name.equals("");
    }

    public Todo setId(UUID id) {
        this.id = id;
        return this;
    }

    public Todo setName(String name) {
        this.name = name;
        return this;
    }

    public Todo setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public Todo setDone(boolean done) {
        this.done = done;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isDone() {
        return done;
    }
}
