package com.egls.server.utils.databind.serialization;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import static org.junit.Assert.*;

/**
 * @author mayer - [Created on 2018-09-03 17:55]
 */
class SerializableData implements Serializable {

    private byte b = 1;
    private boolean bool = Boolean.valueOf("false");
    private short s = 1;
    private char c = 'a';
    private int i = 1;
    private long l = 1;
    private float f = 1.0f;
    private double d = 1.0;

    private byte[] bytes = new byte[]{1, 2};
    private boolean[] booleans = new boolean[]{true, false};
    private short[] shorts = new short[]{1, 2};
    private char[] chars = new char[]{'a', 'b'};
    private int[] ints = new int[]{1, 2};
    private long[] longs = new long[]{1, 2};
    private float[] floats = new float[]{1.0f, 2.0f};
    private double[] doubles = new double[]{1.0, 2.0};

    private String str1 = "\"\"\"\"\"\"";
    private String str2 = "\\\\\\\\";
    private String str3 = "1\\\\\\\\2\"\"\"\"\"\"3";
    private String str4 = "()()()";
    private String str5 = "\\\\\\\\\"\"\\\"\\\"\\\"\\\"\"\"\"\"\"\"";
    private String str6 = "喔喔喔喔喔的你大号发到那{{{时大家能够考虑是否能够卡拉是否";
    private String str7 = "animals飞机撒开绿}{灯飞机分手多}{久啦发80234&*)&%^784)*()67892345><<>?L<><,,>,.";
    private String str8 = null;
    private String str9 = "";

    private String[] strings1 = new String[]{"", "~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./"};
    private String[] strings2 = new String[]{"animals", "飞机撒开绿灯飞机分"};
    private String[] strings3 = new String[]{null, null};
    private String[] strings4 = new String[]{"~!@#$%^&*()_+<>?:\"`1234567890-=;',./", ""};
    private String[] strings5 = new String[]{"", ""};
    private String[] strings6 = new String[]{"null", "null"};
    private String[] strings7 = new String[]{"null", null};
    private String[] strings8 = null;

    private List<String> arrayList = new ArrayList<>();
    private List<String> linkedList = new LinkedList<>();
    private Set<String> hashSet = new HashSet<>();

    private HashMap<String, Integer> hashMap = new HashMap<>();
    private LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();

    SerializableData() {
        arrayList.add("animals");
        arrayList.add("飞机撒}{绿灯飞机分");
        arrayList.add("~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./");
        arrayList.add(null);

        linkedList.add("animals");
        linkedList.add("飞机撒}{绿灯飞机分");
        linkedList.add("~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./");
        linkedList.add(null);

        hashSet.add("animals");
        hashSet.add("飞机撒}{绿灯飞机分");
        hashSet.add("~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./");
        hashSet.add(null);

        hashMap.put("1}", 1);
        hashMap.put("2}", 2);
        hashMap.put("3{}", 3);
        hashMap.put("4}{", 4);

        linkedHashMap.put("1}", 1);
        linkedHashMap.put("2}", 2);
        linkedHashMap.put("3{}", 3);
        linkedHashMap.put("4}{", 4);
    }

    @Override
    public String serialize() {
        Serializer serializer = new Serializer();

        serializer.add(b);
        serializer.add(bool);
        serializer.add(s);
        serializer.add(c);
        serializer.add(i);
        serializer.add(l);
        serializer.add(f);
        serializer.add(d);

        serializer.add(bytes);
        serializer.add(booleans);
        serializer.add(shorts);
        serializer.add(chars);
        serializer.add(ints);
        serializer.add(longs);
        serializer.add(floats);
        serializer.add(doubles);

        serializer.add(str1);
        serializer.add(str2);
        serializer.add(str3);
        serializer.add(str4);
        serializer.add(str5);
        serializer.add(str6);
        serializer.add(str7);
        serializer.add(str8);
        serializer.add(str9);

        serializer.add(strings1);
        serializer.add(strings2);
        serializer.add(strings3);
        serializer.add(strings4);
        serializer.add(strings5);
        serializer.add(strings6);
        serializer.add(strings7);
        serializer.add(strings8);

        serializer.add(arrayList);
        serializer.add(linkedList);
        serializer.add(hashSet);

        serializer.add(hashMap);
        serializer.add(linkedHashMap);

        return serializer.serialize();
    }

    private String toString(String string) {
        return StringUtils.deleteWhitespace(string);
    }

    @Override
    public void deserialize(String serializedString) {
        Serializer serializer = new Serializer(serializedString);
        assertEquals(b, serializer.getByte().byteValue());
        assertEquals(bool, serializer.getBoolean());
        assertEquals(s, serializer.getShort().shortValue());
        assertEquals(c, serializer.getChar().charValue());
        assertEquals(i, serializer.getInt().intValue());
        assertEquals(l, serializer.getLong().longValue());
        assertEquals(f, serializer.getFloat(), 0.0);
        assertEquals(d, serializer.getDouble(), 0.0);

        assertEquals("[1,2]", toString(Arrays.toString(serializer.getByteObjectArray())));
        assertEquals("[true,false]", toString(Arrays.toString(serializer.getBooleanPrimitiveArray())));
        assertEquals("[1,2]", toString(Arrays.toString(serializer.getShortPrimitiveArray())));
        assertEquals("[a,b]", toString(Arrays.toString(serializer.getCharPrimitiveArray())));
        assertEquals("[1,2]", toString(Arrays.toString(serializer.getIntPrimitiveArray())));
        assertEquals("[1,2]", toString(Arrays.toString(serializer.getLongPrimitiveArray())));
        assertEquals("[1.0,2.0]", toString(Arrays.toString(serializer.getFloatPrimitiveArray())));
        assertEquals("[1.0,2.0]", toString(Arrays.toString(serializer.getDoublePrimitiveArray())));

        assertEquals(str1, serializer.getString());
        assertEquals(str2, serializer.getString());
        assertEquals(str3, serializer.getString());
        assertEquals(str4, serializer.getString());
        assertEquals(str5, serializer.getString());
        assertEquals(str6, serializer.getString());
        assertEquals(str7, serializer.getString());
        assertEquals(str8, serializer.getString());
        assertEquals(str9, serializer.getString());

        assertEquals(toString(Arrays.toString(strings1)), toString(Arrays.toString(serializer.getStringArray())));
        assertEquals(toString(Arrays.toString(strings2)), toString(Arrays.toString(serializer.getStringArray())));
        assertEquals(toString(Arrays.toString(strings3)), toString(Arrays.toString(serializer.getStringArray())));
        assertEquals(toString(Arrays.toString(strings4)), toString(Arrays.toString(serializer.getStringArray())));
        assertEquals(toString(Arrays.toString(strings5)), toString(Arrays.toString(serializer.getStringArray())));
        assertEquals(toString(Arrays.toString(strings6)), toString(Arrays.toString(serializer.getStringArray())));
        assertEquals(toString(Arrays.toString(strings7)), toString(Arrays.toString(serializer.getStringArray())));
        assertArrayEquals(strings8, serializer.getStringArray());

        ArrayList<String> tempArrayList = serializer.getArrayList(String.class);
        if (tempArrayList == null) {
            fail();
        }
        assertEquals("animals", tempArrayList.get(0));
        assertEquals("飞机撒}{绿灯飞机分", tempArrayList.get(1));
        assertEquals("~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./", tempArrayList.get(2));
        assertNull(tempArrayList.get(3));

        LinkedList<String> tempLinkedList = serializer.getLinkedList(String.class);
        if (tempLinkedList == null) {
            fail();
        }
        assertEquals("animals", tempLinkedList.get(0));
        assertEquals("飞机撒}{绿灯飞机分", tempLinkedList.get(1));
        assertEquals("~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./", tempLinkedList.get(2));
        assertNull(tempLinkedList.get(3));

        HashSet<String> tempHashSet = serializer.getHashSet(String.class);
        if (tempHashSet == null) {
            fail();
        }
        assertTrue(tempHashSet.contains("animals"));
        assertTrue(tempHashSet.contains("飞机撒}{绿灯飞机分"));
        assertTrue(tempHashSet.contains("~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./"));
        assertTrue(tempHashSet.contains(null));

        HashMap<String, Integer> tempHashMap = serializer.getHashMap(String.class, Integer.class);
        if (tempHashMap == null) {
            fail();
        }
        assertEquals(tempHashMap.get("1}"), hashMap.get("1}"));
        assertEquals(tempHashMap.get("2}"), hashMap.get("2}"));
        assertEquals(tempHashMap.get("3{}"), hashMap.get("3{}"));
        assertEquals(tempHashMap.get("4}{"), hashMap.get("4}{"));

        LinkedHashMap<String, Integer> tempLinkedHashMap = serializer.getLinkedHashMap(String.class, Integer.class);
        if (tempLinkedHashMap == null) {
            fail();
        }
        assertEquals(tempLinkedHashMap.get("1}"), linkedHashMap.get("1}"));
        assertEquals(tempLinkedHashMap.get("2}"), linkedHashMap.get("2}"));
        assertEquals(tempLinkedHashMap.get("3{}"), linkedHashMap.get("3{}"));
        assertEquals(tempLinkedHashMap.get("4}{"), linkedHashMap.get("4}{"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerializableData that = (SerializableData) o;

        if (b != that.b) return false;
        if (bool != that.bool) return false;
        if (s != that.s) return false;
        if (c != that.c) return false;
        if (i != that.i) return false;
        if (l != that.l) return false;
        if (Float.compare(that.f, f) != 0) return false;
        if (Double.compare(that.d, d) != 0) return false;
        if (!Arrays.equals(bytes, that.bytes)) return false;
        if (!Arrays.equals(booleans, that.booleans)) return false;
        if (!Arrays.equals(shorts, that.shorts)) return false;
        if (!Arrays.equals(chars, that.chars)) return false;
        if (!Arrays.equals(ints, that.ints)) return false;
        if (!Arrays.equals(longs, that.longs)) return false;
        if (!Arrays.equals(floats, that.floats)) return false;
        if (!Arrays.equals(doubles, that.doubles)) return false;
        if (str1 != null ? !str1.equals(that.str1) : that.str1 != null) return false;
        if (str2 != null ? !str2.equals(that.str2) : that.str2 != null) return false;
        if (str3 != null ? !str3.equals(that.str3) : that.str3 != null) return false;
        if (str4 != null ? !str4.equals(that.str4) : that.str4 != null) return false;
        if (str5 != null ? !str5.equals(that.str5) : that.str5 != null) return false;
        if (str6 != null ? !str6.equals(that.str6) : that.str6 != null) return false;
        if (str7 != null ? !str7.equals(that.str7) : that.str7 != null) return false;
        if (str8 != null ? !str8.equals(that.str8) : that.str8 != null) return false;
        if (str9 != null ? !str9.equals(that.str9) : that.str9 != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(strings1, that.strings1)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(strings2, that.strings2)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(strings3, that.strings3)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(strings4, that.strings4)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(strings5, that.strings5)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(strings6, that.strings6)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(strings7, that.strings7)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(strings8, that.strings8)) return false;
        if (arrayList != null ? !arrayList.equals(that.arrayList) : that.arrayList != null) return false;
        if (linkedList != null ? !linkedList.equals(that.linkedList) : that.linkedList != null) return false;
        if (hashSet != null ? !hashSet.equals(that.hashSet) : that.hashSet != null) return false;
        if (hashMap != null ? !hashMap.equals(that.hashMap) : that.hashMap != null) return false;
        return linkedHashMap != null ? linkedHashMap.equals(that.linkedHashMap) : that.linkedHashMap == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) b;
        result = 31 * result + (bool ? 1 : 0);
        result = 31 * result + (int) s;
        result = 31 * result + (int) c;
        result = 31 * result + i;
        result = 31 * result + (int) (l ^ (l >>> 32));
        result = 31 * result + (f != +0.0f ? Float.floatToIntBits(f) : 0);
        temp = Double.doubleToLongBits(d);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + Arrays.hashCode(bytes);
        result = 31 * result + Arrays.hashCode(booleans);
        result = 31 * result + Arrays.hashCode(shorts);
        result = 31 * result + Arrays.hashCode(chars);
        result = 31 * result + Arrays.hashCode(ints);
        result = 31 * result + Arrays.hashCode(longs);
        result = 31 * result + Arrays.hashCode(floats);
        result = 31 * result + Arrays.hashCode(doubles);
        result = 31 * result + (str1 != null ? str1.hashCode() : 0);
        result = 31 * result + (str2 != null ? str2.hashCode() : 0);
        result = 31 * result + (str3 != null ? str3.hashCode() : 0);
        result = 31 * result + (str4 != null ? str4.hashCode() : 0);
        result = 31 * result + (str5 != null ? str5.hashCode() : 0);
        result = 31 * result + (str6 != null ? str6.hashCode() : 0);
        result = 31 * result + (str7 != null ? str7.hashCode() : 0);
        result = 31 * result + (str8 != null ? str8.hashCode() : 0);
        result = 31 * result + (str9 != null ? str9.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(strings1);
        result = 31 * result + Arrays.hashCode(strings2);
        result = 31 * result + Arrays.hashCode(strings3);
        result = 31 * result + Arrays.hashCode(strings4);
        result = 31 * result + Arrays.hashCode(strings5);
        result = 31 * result + Arrays.hashCode(strings6);
        result = 31 * result + Arrays.hashCode(strings7);
        result = 31 * result + Arrays.hashCode(strings8);
        result = 31 * result + (arrayList != null ? arrayList.hashCode() : 0);
        result = 31 * result + (linkedList != null ? linkedList.hashCode() : 0);
        result = 31 * result + (hashSet != null ? hashSet.hashCode() : 0);
        result = 31 * result + (hashMap != null ? hashMap.hashCode() : 0);
        result = 31 * result + (linkedHashMap != null ? linkedHashMap.hashCode() : 0);
        return result;
    }

}