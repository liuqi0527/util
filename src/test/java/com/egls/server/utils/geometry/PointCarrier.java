package com.egls.server.utils.geometry;

import java.util.Objects;

import com.egls.server.utils.databind.json.JsonObject;
import com.egls.server.utils.databind.serialization.Serializable;
import com.egls.server.utils.databind.serialization.Serializer;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author mayer - [Created on 2018-09-04 11:35]
 */
class PointCarrier implements JsonObject, Serializable {

    @JsonProperty("point2D")
    private Point2D point2D;

    @JsonProperty("point3D")
    private Point3D point3D;

    private PointCarrier() {
    }

    public PointCarrier(Point2D point2D, Point3D point3D) {
        this.point2D = point2D;
        this.point3D = point3D;
    }

    @Override
    public String toString() {
        return "PointCarrier{" +
                "point2D=" + point2D +
                ", point3D=" + point3D +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointCarrier that = (PointCarrier) o;
        return Objects.equals(point2D, that.point2D) &&
                Objects.equals(point3D, that.point3D);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point2D, point3D);
    }

    @Override
    public String serialize() {
        Serializer serializer = new Serializer();
        serializer.add(point2D);
        serializer.add(point3D);
        return serializer.serialize();
    }

    @Override
    public void deserialize(String serializedString) {
        Serializer serializer = new Serializer(serializedString);
        point2D = serializer.getSerializableObject(Point2D.class);
        point3D = serializer.getSerializableObject(Point3D.class);
    }
}