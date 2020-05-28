package com.egls.server.utils.databind.json;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author mayer - [Created on 2018-09-04 18:02]
 */
@JsonTypeName("StillAction")
public class StillAction extends BaseAction {

    @JsonProperty("time")
    private long time;

    private StillAction() {
        //反序列化使用
    }

    public StillAction(long time) {
        this.name = this.getClass().getSimpleName();
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StillAction that = (StillAction) o;
        return time == that.time;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), time);
    }
}
