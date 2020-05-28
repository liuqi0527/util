package com.egls.server.utils.databind.json;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author mayer - [Created on 2018-09-03 18:15]
 */
class JsonObjectData implements JsonObject {

    @JsonProperty("b")
    private byte b = 1;

    @JsonProperty("bool")
    private boolean bool = Boolean.valueOf("false");

    @JsonProperty("s")
    private short s = 1;

    @JsonProperty("c")
    private char c = 'a';

    @JsonProperty("i")
    private int i = 1;

    @JsonProperty("l")
    private long l = 1;

    @JsonProperty("f")
    private float f = 1.0f;

    @JsonProperty("d")
    private double d = 1.0;

    @JsonProperty("bytes")
    private byte[] bytes = new byte[]{1, 2};

    @JsonProperty("booleans")
    private boolean[] booleans = new boolean[]{true, false};

    @JsonProperty("shorts")
    private short[] shorts = new short[]{1, 2};

    @JsonProperty("chars")
    private char[] chars = new char[]{'a', 'b'};

    @JsonProperty("ints")
    private int[] ints = new int[]{1, 2};

    @JsonProperty("longs")
    private long[] longs = new long[]{1, 2};

    @JsonProperty("floats")
    private float[] floats = new float[]{1.0f, 2.0f, Float.MIN_VALUE, Float.MAX_VALUE};

    @JsonProperty("doubles")
    private double[] doubles = new double[]{1.0, 2.0, Double.MIN_VALUE, Double.MAX_VALUE};

    @JsonProperty("str1")
    private String str1 = "1\\\\\\\\2\"\"\"\"\"\"3()()()animals飞机撒开绿}{灯飞机分手多}{久啦发80234&*)&%^784)*()67892345><<>?L<><,,>,.";

    @JsonProperty("str2")
    private String str2 = null;

    @JsonProperty("str3")
    private String str3 = "";

    @JsonProperty("strings1")
    private String[] strings1 = new String[]{"null", null, "~!@#$%^&*()_+<>?:\"`1234567890-=;',./", "飞机撒开绿灯飞机分", "~!@#$%^&*()_+<>?:\"\\\\`1234567890-=;',./", "",};

    @JsonProperty("strings2")
    private String[] strings2 = null;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonObjectData that = (JsonObjectData) o;
        return b == that.b &&
                bool == that.bool &&
                s == that.s &&
                c == that.c &&
                i == that.i &&
                l == that.l &&
                Float.compare(that.f, f) == 0 &&
                Double.compare(that.d, d) == 0 &&
                Arrays.equals(bytes, that.bytes) &&
                Arrays.equals(booleans, that.booleans) &&
                Arrays.equals(shorts, that.shorts) &&
                Arrays.equals(chars, that.chars) &&
                Arrays.equals(ints, that.ints) &&
                Arrays.equals(longs, that.longs) &&
                Arrays.equals(floats, that.floats) &&
                Arrays.equals(doubles, that.doubles) &&
                Objects.equals(str1, that.str1) &&
                Objects.equals(str2, that.str2) &&
                Objects.equals(str3, that.str3) &&
                Arrays.equals(strings1, that.strings1) &&
                Arrays.equals(strings2, that.strings2);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(b, bool, s, c, i, l, f, d, str1, str2, str3);
        result = 31 * result + Arrays.hashCode(bytes);
        result = 31 * result + Arrays.hashCode(booleans);
        result = 31 * result + Arrays.hashCode(shorts);
        result = 31 * result + Arrays.hashCode(chars);
        result = 31 * result + Arrays.hashCode(ints);
        result = 31 * result + Arrays.hashCode(longs);
        result = 31 * result + Arrays.hashCode(floats);
        result = 31 * result + Arrays.hashCode(doubles);
        result = 31 * result + Arrays.hashCode(strings1);
        result = 31 * result + Arrays.hashCode(strings2);
        return result;
    }
}
