package com.egls.server.utils.geometry;

import com.egls.server.utils.databind.serialization.Serializer;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * 三维坐标点
 * 注意使用序列化时,必须使用具体类型.不能使用泛型反序列化.
 *
 * @author mayer - [Created on 2018-08-09 17:09]
 */
@JsonTypeName("Point3D")
public class Point3D extends Point2D {

    @JsonProperty("y")
    protected double y;

    protected Point3D() {
    }

    public Point3D(final Point2D point, final double y) {
        this(point.x, y, point.z);
    }

    public Point3D(final Point3D point) {
        this(point.x, point.y, point.z);
    }

    public Point3D(final String pointStr) {
        this(StringUtils.split(pointStr, ','));
    }

    public Point3D(final String[] array) {
        this(Double.valueOf(array[0]), Double.valueOf(array[1]), Double.valueOf(array[2]));
    }

    public Point3D(final double x, final double y, final double z) {
        this.setXYZ(x, y, z);
    }

    void setXYZ(final double x, final double y, final double z) {
        this.x = ((long) (x * POINT_PRECISION)) / POINT_PRECISION;
        this.y = ((long) (y * POINT_PRECISION)) / POINT_PRECISION;
        this.z = ((long) (z * POINT_PRECISION)) / POINT_PRECISION;
    }

    public double getY() {
        return y;
    }

    public boolean equalsY(final Point3D point) {
        return ((long) (y * POINT_PRECISION)) == ((long) (point.y * POINT_PRECISION));
    }

    public double getDistance3D(final Point3D point) {
        return GeometryUtil.calculateDistance3D(this, point);
    }

    @Override
    public boolean isAvailable() {
        return super.isAvailable() && y >= 0;
    }

    @Override
    public String toString() {
        return String.format("%.3f,%.3f,%.3f", x, y, z);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Point3D point = (Point3D) o;
        if (((long) (x * POINT_PRECISION)) != ((long) (point.x * POINT_PRECISION))) {
            return false;
        }
        if (((long) (y * POINT_PRECISION)) != ((long) (point.y * POINT_PRECISION))) {
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
        temp = (long) (y * POINT_PRECISION);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = (long) (z * POINT_PRECISION);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String serialize() {
        final Serializer serializer = new Serializer();
        serializer.add(this.x);
        serializer.add(this.y);
        serializer.add(this.z);
        return serializer.serialize();
    }

    @Override
    public void deserialize(String serializedString) {
        final Serializer serializer = new Serializer(serializedString);
        this.setXYZ(serializer.getDouble(), serializer.getDouble(), serializer.getDouble());
    }

}
