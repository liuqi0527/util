package com.egls.server.utils.databind.json;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author mayer - [Created on 2018-09-04 18:01]
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonTypeName("BaseAction")
public class BaseAction implements JsonObject {

    @JsonProperty("name")
    protected String name;

    protected BaseAction() {
    }

    public BaseAction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseAction that = (BaseAction) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
