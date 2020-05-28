package com.egls.server.utils.databind.json;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author mayer - [Created on 2018-10-17 11:18]
 */
public class ActionGroup implements JsonObject {

    @JsonProperty("id")
    private String id;

    @JsonProperty("baseActions")
    private BaseAction[] baseActions;

    public ActionGroup() {
    }

    public ActionGroup(String id, BaseAction[] baseActions) {
        this.id = id;
        this.baseActions = baseActions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionGroup that = (ActionGroup) o;
        return Objects.equals(id, that.id) &&
                Arrays.equals(baseActions, that.baseActions);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(baseActions);
        return result;
    }
}
