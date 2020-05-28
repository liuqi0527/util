package com.egls.server.utils.geometry;

import com.egls.server.utils.databind.json.JsonObject;
import com.egls.server.utils.databind.serialization.Serializable;
import com.egls.server.utils.databind.serialization.Serializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import org.apache.commons.lang3.StringUtils;

import static com.egls.server.utils.geometry.GeometryUtil.POINT_PRECISION;

/**
 * <pre>
 *         ^
 *       y |
 *         |
 *         |
 *         |    /
 *         |   / z
 *         |  /
 *         | /
 *         |/
 *         -------------------->
 *                            x
 * </pre>
 * 2维坐标点
 * 注意使用序列化时,必须使用具体类型.不能使用泛型反序列化.
 *
 * @author mayer - [Created on 2018-08-09 17:09]
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonTypeName("Point2D")
public class Point2D implements Serializable, JsonObject {

    @JsonProperty("x")
    protected double x;

    @JsonProperty("z")
    protected double z;

    /**
     * 反序列化使用
     */
    protected Point2D() {
    }

    public Point2D(final Point2D point) {
        this(point.x, point.z);
    }

    public Point2D(final String pointStr) {
        this(StringUtils.split(pointStr, ','));
    }

    public Point2D(final String[] array) {
        this(Double.valueOf(array[0]), Double.valueOf(array[1]));
    }

    public Point2D(final double x, final double z) {
        this.setXZ(x, z);
    }

    void setXZ(final double x, final double z) {
        this.x = ((long) (x * POINT_PRECISION)) / POINT_PRECISION;
        this.z = ((long) (z * POINT_PRECISION)) / POINT_PRECISION;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    public boolean equalsX(final Point2D point) {
        return ((long) (x * POINT_PRECISION)) == ((long) (point.x * POINT_PRECISION));
    }

    public boolean equalsZ(final Point2D point) {
        return ((long) (z * POINT_PRECISION)) == ((long) (point.z * POINT_PRECISION));
    }

    public double getDistance2D(final Point2D point) {
        return GeometryUtil.calculateDistance2D(this, point);
    }

    public boolean isAvailable() {
        return x >= 0 && z >= 0;
    }

    @Override
    public String toString() {
        return String.format("%.3f,%.3f", x, z);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point2D point = (Point2D) o;
        if (((long) (x * POINT_PRECISION)) != ((long) (point.x * POINT_PRECISION))) {
            return false;
        }
        if (((long) (z * POINT_PRECISION)) != ((long) (point.z * POINT_PRECISION))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = (long) (x * POINT_PRECISION);
        result = (int) (temp ^ (temp >>> 32));
        temp = (long) (z * POINT_PRECISION);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String serialize() {
        final Serializer serializer = new Serializer();
        serializer.add(this.x);
        serializer.add(this.z);
        return serializer.serialize();
    }

    @Override
    public void deserialize(String serializedString) {
        final Serializer serializer = new Serializer(serializedString);
        this.setXZ(serializer.getDouble(), serializer.getDouble());
    }

}
