package com.egls.server.utils.databind.json;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author mayer - [Created on 2018-09-04 18:02]
 */
@JsonTypeName("BattleAction")
public class BattleAction extends BaseAction {

    @JsonProperty("skill")
    private String skill;

    private BattleAction() {
        //反序列化使用
    }

    public BattleAction(String skill) {
        this.name = this.getClass().getSimpleName();
        this.skill = skill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BattleAction that = (BattleAction) o;
        return Objects.equals(skill, that.skill);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), skill);
    }
}
