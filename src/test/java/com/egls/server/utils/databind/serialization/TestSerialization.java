package com.egls.server.utils.databind.serialization;

import java.util.*;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mayer - [Created on 2018-09-03 17:24]
 */
public class TestSerialization {

    private enum TestEnum {
        test1, test2, test3, test4
    }

    @Test
    public void testPrimitive() {
        Serializer serializer = new Serializer();

        final byte b = 1;
        final boolean bool = Boolean.valueOf("false");
        final short s = 1;
        final char c = 'a';
        final int i = 1;
        final long l = 1;
        final float f = 1.0f;
        final double d = 1.0;
        final TestEnum e = TestEnum.test1;

        final Byte b1 = 2;
        final Boolean bool1 = true;
        final Short s1 = 2;
        final Character c1 = 'Z';
        final Integer i1 = 2;
        final Long l1 = 2L;
        final Float f1 = 2.0f;
        final Double d1 = 2.0d;
        final TestEnum e1 = TestEnum.test2;

        final Byte null1 = null;
        final Boolean null2 = null;
        final Short null3 = null;
        final Character null4 = null;
        final Integer null5 = null;
        final Long null6 = null;
        final Float null7 = null;
        final Double null8 = null;
        final TestEnum null9 = null;

        serializer.add(b);
        serializer.add(bool);
        serializer.add(s);
        serializer.add(c);
        serializer.add(i);
        serializer.add(l);
        serializer.add(f);
        serializer.add(d);
        serializer.add(e);

        serializer.add(b1);
        serializer.add(bool1);
        serializer.add(s1);
        serializer.add(c1);
        serializer.add(i1);
        serializer.add(l1);
        serializer.add(f1);
        serializer.add(d1);
        serializer.add(e1);

        serializer.add(null1);
        serializer.add(null2);
        serializer.add(null3);
        serializer.add(null4);
        serializer.add(null5);
        serializer.add(null6);
        serializer.add(null7);
        serializer.add(null8);
        serializer.add(null9);

        String serializedString = serializer.serialize();
        serializer = new Serializer(serializedString);

        assertEquals(b, serializer.getByte().byteValue());
        assertEquals(bool, serializer.getBoolean());
        assertEquals(s, serializer.getShort().shortValue());
        assertEquals(c, serializer.getChar().charValue());
        assertEquals(i, serializer.getInt().intValue());
        assertEquals(l, serializer.getLong().longValue());
        assertEquals(f, serializer.getFloat(), 0.0);
        assertEquals(d, serializer.getDouble(), 0.0);
        assertSame(e, serializer.getEnum(TestEnum.class));

        assertEquals(b1, serializer.getByte());
        assertEquals(bool1, serializer.getBoolean());
        assertEquals(s1, serializer.getShort());
        assertEquals(c1, serializer.getChar());
        assertEquals(i1, serializer.getInt());
        assertEquals(l1, serializer.getLong());
        assertEquals(f1, serializer.getFloat());
        assertEquals(d1, serializer.getDouble());
        assertEquals(e1, serializer.getEnum(TestEnum.class));

        assertEquals(null1, serializer.getByte());
        assertEquals(null2, serializer.getBoolean());
        assertEquals(null3, serializer.getShort());
        assertEquals(null4, serializer.getChar());
        assertEquals(null5, serializer.getInt());
        assertEquals(null6, serializer.getLong());
        assertEquals(null7, serializer.getFloat());
        assertEquals(null8, serializer.getDouble());
        assertEquals(null9, serializer.getEnum(TestEnum.class));

    }

    @Test
    public void testPrimitiveArray() {
        Serializer serializer = new Serializer();
        byte[] bytes = new byte[]{1, 2};
        boolean[] booleans = new boolean[]{true, false};
        short[] shorts = new short[]{1, 2};
        char[] chars = new char[]{'a', 'b'};
        int[] ints = new int[]{1, 2};
        long[] longs = new long[]{1, 2};
        float[] floats = new float[]{1.0f, 2.0f};
        double[] doubles = new double[]{1.0, 2.0};
        TestEnum[] enums = new TestEnum[]{TestEnum.test1, TestEnum.test2};

        Byte[] bytes1 = new Byte[]{1, 2};
        Boolean[] booleans1 = new Boolean[]{true, false};
        Short[] shorts1 = new Short[]{1, 2};
        Character[] chars1 = new Character[]{'a', 'b'};
        Integer[] ints1 = new Integer[]{1, 2};
        Long[] longs1 = new Long[]{1L, 2L};
        Float[] floats1 = new Float[]{1.0f, 2.0f};
        Double[] doubles1 = new Double[]{1.0, 2.0};
        TestEnum[] enums1 = new TestEnum[]{TestEnum.test3, TestEnum.test4};

        Byte[] null1 = new Byte[]{null, null, 1, 2, null, 3, 4};
        Boolean[] null2 = new Boolean[]{null, null, true, false, null, true, false};
        Short[] null3 = new Short[]{null, null, 1, 2, null, 3, 4};
        Character[] null4 = new Character[]{null, null, 'a', 'b', null, 'c', 'd'};
        Integer[] null5 = new Integer[]{null, null, 1, 2, null, 3, 4};
        Long[] null6 = new Long[]{null, null, 1L, 2L, null, 3L, 4L};
        Float[] null7 = new Float[]{null, null, 1.0f, 2.0f, null, 3.0f, 4.0f};
        Double[] null8 = new Double[]{null, null, 1.0, 2.0, null, 3.0, 4.0};
        TestEnum[] null9 = new TestEnum[]{null, null, TestEnum.test1, TestEnum.test2, null, TestEnum.test3, TestEnum.test4};

        serializer.add(bytes);
        serializer.add(booleans);
        serializer.add(shorts);
        serializer.add(chars);
        serializer.add(ints);
        serializer.add(longs);
        serializer.add(floats);
        serializer.add(doubles);
        serializer.add(enums);

        serializer.add(bytes1);
        serializer.add(booleans1);
        serializer.add(shorts1);
        serializer.add(chars1);
        serializer.add(ints1);
        serializer.add(longs1);
        serializer.add(floats1);
        serializer.add(doubles1);
        serializer.add(enums1);

        serializer.add(null1);
        serializer.add(null2);
        serializer.add(null3);
        serializer.add(null4);
        serializer.add(null5);
        serializer.add(null6);
        serializer.add(null7);
        serializer.add(null8);
        serializer.add(null9);

        String serializedString = serializer.serialize();
        serializer = new Serializer(serializedString);

        assertArrayEquals(bytes, serializer.getBytePrimitiveArray());
        assertArrayEquals(booleans, serializer.getBooleanPrimitiveArray());
        assertArrayEquals(shorts, serializer.getShortPrimitiveArray());
        assertArrayEquals(chars, serializer.getCharPrimitiveArray());
        assertArrayEquals(ints, serializer.getIntPrimitiveArray());
        assertArrayEquals(longs, serializer.getLongPrimitiveArray());
        assertArrayEquals(floats, serializer.getFloatPrimitiveArray(), 0);
        assertArrayEquals(doubles, serializer.getDoublePrimitiveArray(), 0);
        assertArrayEquals(enums, serializer.getEnumArray(TestEnum.class));

        assertArrayEquals(bytes1, serializer.getByteObjectArray());
        assertArrayEquals(booleans1, serializer.getBooleanObjectArray());
        assertArrayEquals(shorts1, serializer.getShortObjectArray());
        assertArrayEquals(chars1, serializer.getCharObjectArray());
        assertArrayEquals(ints1, serializer.getIntObjectArray());
        assertArrayEquals(longs1, serializer.getLongObjectArray());
        assertArrayEquals(floats1, serializer.getFloatObjectArray());
        assertArrayEquals(doubles1, serializer.getDoubleObjectArray());
        assertArrayEquals(enums1, serializer.getEnumArray(TestEnum.class));

        assertArrayEquals(null1, serializer.getByteObjectArray());
        assertArrayEquals(null2, serializer.getBooleanObjectArray());
        assertArrayEquals(null3, serializer.getShortObjectArray());
        assertArrayEquals(null4, serializer.getCharObjectArray());
        assertArrayEquals(null5, serializer.getIntObjectArray());
        assertArrayEquals(null6, serializer.getLongObjectArray());
        assertArrayEquals(null7, serializer.getFloatObjectArray());
        assertArrayEquals(null8, serializer.getDoubleObjectArray());
        assertArrayEquals(null9, serializer.getEnumArray(TestEnum.class));

    }

    @Test
    public void testString() {
        String string1 = "5i6op45269,038023-1-$%&^@$%^&^()&(){}{}789^&*4%&^{{}";
        String string2 = "[0,1],[true,false],[\",,,,\",\",,,,\"]";
        String string3 = "{1,false,1,a,1,1,1.0,1.0}";
        String string4 = "{[1,2],[true,false],[1,2],[a,b],[1,2],[1,2],[1.0,2.0],[1.0,2.0]}";
        String string5 = "{NULL,NULL,\"\",\"\",\"123\",\"()()()\",\"\",\"喔喔喔喔喔的你大号发到那时大家能够考虑是否能够卡拉是否\",NULL,NULL,\"animals飞机撒开绿灯飞机分手多久啦发80234&*)&%^784)*()67892345><<>?L<><,,>,.\",\"\",\"\",NULL,NULL}";
        String string6 = "animals飞机撒开绿灯飞机分手多久啦发80234&*)&%^784)*()67892345><<>?L<><,,>,.";
        String string7 = "";
        String string8 = "NULL";
        String string9 = null;
        String string10 = null;
        String string11 = "\"\"\"\"\"\"";
        String string12 = "\\\\\\\\";
        String string13 = "1\\\\\\\\2\"\\\"\"\\\\\\\"\\\\\"\\\"\"\"\"\"\"\"3";
        String string14 = "()()()}}{{}";
        String string15 = "\\\\\\\\\"\"\\\"\\\"\\\"\\\"\"\"\"\"\"\"";
        String string16 = "{null,[\"\",\"~!@#$%^&*()_+<>?:\\\"\\\\\\\\`1234567890-=;',./\"],[\"animals\",\"飞机撒开绿灯飞机分\"],[null,null],[\"~!@#$%^&*()_+<>?:\\\"`1234567890-=;',./\",\"\"],null,[\"\",\"\"],[\"null\",\"null\"],[\"null\",null],null}";
        String string17 = "animals飞机撒开绿灯飞机分手多久啦发80234&*)&%^784)*()67892345><<>?L<><,,>,.";
        String string18 = "a,abs\"\",";

        Serializer serializer = new Serializer();
        serializer.add(string1);
        serializer.add(string2);
        serializer.add(string3);
        serializer.add(string4);
        serializer.add(string5);
        serializer.add(string6);
        serializer.add(string7);
        serializer.add(string8);
        serializer.add(string9);
        serializer.add(string10);
        serializer.add(string11);
        serializer.add(string12);
        serializer.add(string13);
        serializer.add(string14);
        serializer.add(string15);
        serializer.add(string16);
        serializer.add(string17);
        serializer.add(string18);
        String serializedString = serializer.serialize();
        serializer = new Serializer(serializedString);

        assertEquals(serializer.getString(), string1);
        assertEquals(serializer.getString(), string2);
        assertEquals(serializer.getString(), string3);
        assertEquals(serializer.getString(), string4);
        assertEquals(serializer.getString(), string5);
        assertEquals(serializer.getString(), string6);
        assertEquals(serializer.getString(), string7);
        assertEquals(serializer.getString(), string8);
        assertEquals(serializer.getString(), string9);
        assertEquals(serializer.getString(), string10);
        assertEquals(serializer.getString(), string11);
        assertEquals(serializer.getString(), string12);
        assertEquals(serializer.getString(), string13);
        assertEquals(serializer.getString(), string14);
        assertEquals(serializer.getString(), string15);
        assertEquals(serializer.getString(), string16);
        assertEquals(serializer.getString(), string17);
        assertEquals(serializer.getString(), string18);
    }

    @Test
    public void testStringArray() {
        String[] strings1 = new String[]{"", "~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./"};
        String[] strings2 = new String[]{"animals", "飞机撒开绿灯飞机分"};
        String[] strings3 = new String[]{null, null};
        String[] strings4 = new String[]{"~!@#$%^&*()_+<>?:\"`1234567890-=;',./", ""};
        String[] strings5 = new String[]{"", ""};
        String[] strings6 = new String[]{"null", "null"};
        String[] strings7 = new String[]{"null", null};
        String[] strings8 = new String[]{"[a,b,c]", "{a,b,c}", "[1,2,3]", ",,,,,,", ",,,\",,\",", "[true,false]", ",,,,,,", ",,,\",,\",", "[0.1,0.2]", ",,,,,,", ",,,\",,\",", "[-1,-2]"};

        Serializer serializer = new Serializer();
        serializer.add(null);
        serializer.add(strings1);
        serializer.add(strings2);
        serializer.add(strings3);
        serializer.add(strings4);
        serializer.add(null);
        serializer.add(strings5);
        serializer.add(strings6);
        serializer.add(strings7);
        serializer.add(null);
        serializer.add(strings8);

        String serializedString = serializer.serialize();
        serializer = new Serializer(serializedString);
        assertArrayEquals(null, serializer.getStringArray());
        assertArrayEquals(strings1, serializer.getStringArray());
        assertArrayEquals(strings2, serializer.getStringArray());
        assertArrayEquals(strings3, serializer.getStringArray());
        assertArrayEquals(strings4, serializer.getStringArray());
        assertArrayEquals(null, serializer.getStringArray());
        assertArrayEquals(strings5, serializer.getStringArray());
        assertArrayEquals(strings6, serializer.getStringArray());
        assertArrayEquals(strings7, serializer.getStringArray());
        assertArrayEquals(null, serializer.getStringArray());
        assertArrayEquals(strings8, serializer.getStringArray());
    }

    @Test
    public void testCollection() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("animals");
        arrayList.add("飞机撒开}{绿灯飞机分");
        arrayList.add("~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./");
        arrayList.add(null);
        Serializer serializer = new Serializer();
        serializer.add(arrayList);
        String serializedString = serializer.serialize();
        serializer = new Serializer(serializedString);
        ArrayList<String> tempArrayList = serializer.getArrayList(String.class);
        if (tempArrayList == null) {
            fail();
        }
        assertEquals("animals", tempArrayList.get(0));
        assertEquals("飞机撒开}{绿灯飞机分", tempArrayList.get(1));
        assertEquals("~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./", tempArrayList.get(2));
        assertNull(tempArrayList.get(3));

        serializer.reset();
        List<String> linkedList = new LinkedList<>();
        linkedList.add("animals");
        linkedList.add("飞机撒开}{绿灯飞机分");
        linkedList.add("~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./");
        linkedList.add(null);
        serializer.add(linkedList);
        serializedString = serializer.serialize();
        serializer = new Serializer(serializedString);
        LinkedList<String> tempLinkedList = serializer.getLinkedList(String.class);
        if (tempLinkedList == null) {
            fail();
        }
        assertEquals("animals", tempLinkedList.get(0));
        assertEquals("飞机撒开}{绿灯飞机分", tempLinkedList.get(1));
        assertEquals("~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./", tempLinkedList.get(2));
        assertNull(tempLinkedList.get(3));

        serializer.reset();
        Set<String> hashSet = new HashSet<>();
        hashSet.add("animals");
        hashSet.add("飞机撒开}{绿灯飞机分");
        hashSet.add("~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./");
        hashSet.add(null);
        serializer.add(hashSet);
        serializedString = serializer.serialize();
        serializer = new Serializer(serializedString);
        HashSet<String> tempHashSet = serializer.getHashSet(String.class);
        if (tempHashSet == null) {
            fail();
        }
        assertTrue(tempHashSet.contains("animals"));
        assertTrue(tempHashSet.contains("飞机撒开}{绿灯飞机分"));
        assertTrue(tempHashSet.contains("~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./"));
        assertTrue(tempHashSet.contains(null));
    }

    @Test
    public void testMap() {
        Serializer serializer = new Serializer();
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("1", 1);
        hashMap.put("2", 2);
        hashMap.put("3}", 3);
        hashMap.put("4}", 4);
        serializer.add(hashMap);
        String serializedString = serializer.serialize();
        serializer = new Serializer(serializedString);
        HashMap<String, Integer> tempHashMap = serializer.getHashMap(String.class, Integer.class);
        if (tempHashMap == null) {
            fail();
        }
        assertEquals(tempHashMap.get("1"), hashMap.get("1"));
        assertEquals(tempHashMap.get("2"), hashMap.get("2"));
        assertEquals(tempHashMap.get("3}"), hashMap.get("3}"));
        assertEquals(tempHashMap.get("4}"), hashMap.get("4}"));

        serializer.reset();
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("1", 1);
        linkedHashMap.put("2", 2);
        linkedHashMap.put("3}", 3);
        linkedHashMap.put("4}", 4);
        serializer.add(linkedHashMap);
        serializedString = serializer.serialize();
        serializer = new Serializer(serializedString);
        LinkedHashMap<String, Integer> tempLinkedHashMap = serializer.getLinkedHashMap(String.class, Integer.class);
        if (tempLinkedHashMap == null) {
            fail();
        }
        assertEquals(tempLinkedHashMap.get("1"), linkedHashMap.get("1"));
        assertEquals(tempLinkedHashMap.get("2"), linkedHashMap.get("2"));
        assertEquals(tempLinkedHashMap.get("3}"), linkedHashMap.get("3}"));
        assertEquals(tempLinkedHashMap.get("4}"), linkedHashMap.get("4}"));
    }

    @Test
    public void testNested() {
        String[] strings = new String[]{"[a,b,c]", "{a,b,c}", "[1,2,3]", ",,,,,,", ",,,\",,\",", "[true,false]", ",,,,,,", ",,,\",,\",", "[0.1,0.2]", ",,,,,,", ",,,\",,\",", "[-1,-2]"};
        Serializer serializeCache1 = new Serializer();
        serializeCache1.add(strings);
        Serializer serializeCache2 = new Serializer();
        serializeCache2.add(serializeCache1);

        String serializedString = serializeCache2.serialize();
        serializeCache2 = new Serializer(serializedString);
        serializeCache1 = serializeCache2.getSerializableObject(Serializer.class);
        Assert.assertNotNull(serializeCache1);
        Assert.assertArrayEquals(strings, serializeCache1.getStringArray());
    }

    @Test
    public void testClone() {
        SerializableData one = new SerializableData();
        SerializableData other = Serializer.clone(one);
        //same是判断==的
        assertNotSame(one, other);
        assertEquals(one, other);
    }

    @Test
    public void testSerializable() {
        Serializer serializer = new Serializer();
        SerializableData serializableData = new SerializableData();
        serializer.add(serializableData);

        String serializedString = serializer.serialize();
        serializer = new Serializer(serializedString);
        Assert.assertEquals(serializableData, serializer.getSerializableObject(SerializableData.class));
    }

    @Test
    public void testSerializableArray() {
        SerializableData[] serializableDataArray = new SerializableData[3];
        for (int i = 1; i < serializableDataArray.length; i++) {
            serializableDataArray[i] = new SerializableData();
        }

        Serializer serializer = new Serializer();
        serializer.add(serializableDataArray);
        String serializedString = serializer.serialize();
        serializer = new Serializer(serializedString);

        Assert.assertArrayEquals(serializableDataArray, serializer.getSerializeObjectArray(SerializableData.class));
    }


}
