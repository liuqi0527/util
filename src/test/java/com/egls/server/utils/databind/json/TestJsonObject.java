package com.egls.server.utils.databind.json;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * @author mayer - [Created on 2018-09-03 18:05]
 */
public class TestJsonObject {

    @Test
    public void test() {
        JsonObject.ensureJsonObjectsValid(TestJsonObject.class.getPackage().getName());
        JsonObjectData one = new JsonObjectData();
        String jsonString = JsonObject.serialize(one);
        JsonObjectData other = JsonObject.deserialize(jsonString, JsonObjectData.class);
        //same是判断==的
        assertNotSame(one, other);
        assertEquals(one, other);
    }

    @Test
    public void test1() {
        JsonObject.ensureJsonObjectsValid(TestJsonObject.class.getPackage().getName());
        BattleAction one = new BattleAction("attack");
        String jsonString = JsonObject.serialize(one);
        BattleAction other = JsonObject.deserialize(jsonString, BattleAction.class);
        //same是判断==的
        assertNotSame(one, other);
        assertEquals(one, other);
    }

    @Test
    public void test2() {
        JsonObject.ensureJsonObjectsValid(TestJsonObject.class.getPackage().getName());
        StillAction one = new StillAction(1);
        String jsonString = JsonObject.serialize(one);
        StillAction other = JsonObject.deserialize(jsonString, StillAction.class);
        //same是判断==的
        assertNotSame(one, other);
        assertEquals(one, other);
    }

    @Test
    public void test3() {
        JsonObject.ensureJsonObjectsValid(TestJsonObject.class.getPackage().getName());
        ActionGroup one = new ActionGroup(
                "test",
                new BaseAction[]{new BattleAction("attack"), new StillAction(1)}
        );
        String jsonString = JsonObject.serialize(one);
        ActionGroup other = JsonObject.deserialize(jsonString, ActionGroup.class);

        assertNotSame(one, other);
        assertEquals(one, other);
    }

    @Test
    public void test4() {
        JsonObject.ensureJsonObjectsValid(TestJsonObject.class.getPackage().getName());
        ActionGroup one = new ActionGroup(
                "test",
                new BaseAction[]{new BaseAction("base"), new BattleAction("attack"), new StillAction(1)}
        );
        String jsonString = JsonObject.serialize(one);
        ActionGroup other = JsonObject.deserialize(jsonString, ActionGroup.class);

        assertNotSame(one, other);
        assertEquals(one, other);
    }
}
