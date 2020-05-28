package com.egls.server.utils.geometry;

import com.egls.server.utils.databind.json.JsonObject;
import com.egls.server.utils.databind.serialization.Serializer;
import com.egls.server.utils.units.NumberUnitsConst;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-08-31 10:40]
 */
public class TestGeometry {

    @Test
    public void test0() {
        final PointCarrier pointCarrier = new PointCarrier(new Point2D(1, 1), new Point3D(2, 2, 2));
        final String string = JsonObject.serialize(pointCarrier);
        PointCarrier deserialize = JsonObject.deserialize(string, PointCarrier.class);
        Assert.assertEquals(pointCarrier, deserialize);
    }

    @Test
    public void test1() {
        final PointCarrier pointCarrier = new PointCarrier(new Point2D(1, 1), new Point3D(2, 2, 2));
        final String string = pointCarrier.serialize();
        PointCarrier deserialize = Serializer.deserialize(string, PointCarrier.class);
        Assert.assertEquals(pointCarrier, deserialize);
    }

    @Test
    public void test2() {
        Point2D p1 = new Point2D(1, 1);
        Point2D p2 = new Point2D(2, 2);
        Point2D q1 = new Point2D(2, 1);
        Point2D q2 = new Point2D(2.9, 2);

        Point2D lineIntersection = GeometryUtil.lineIntersection(p1, p2, q1, q2);
        Point2D lineSegmentIntersection = GeometryUtil.lineSegmentIntersection(p1, p2, q1, q2);
        Assert.assertNotNull(lineIntersection);
        Assert.assertNull(lineSegmentIntersection);
    }

    @Test
    public void test3() {
        Point2D p1 = new Point2D(1, 3);
        Point2D p2 = new Point2D(3, 1);
        Point2D q1 = new Point2D(1, 1);
        Point2D q2 = new Point2D(2, 4);
        Point2D lineIntersection = GeometryUtil.lineIntersection(p1, p2, q1, q2);
        Point2D lineSegmentIntersection = GeometryUtil.lineSegmentIntersection(p1, p2, q1, q2);
        Assert.assertNotNull(lineIntersection);
        Assert.assertNotNull(lineSegmentIntersection);
        Assert.assertEquals(lineIntersection.x, 1.5, NumberUnitsConst.DOUBLE_APPROXIMATE_ZERO);
        Assert.assertEquals(lineIntersection.z, 2.5, NumberUnitsConst.DOUBLE_APPROXIMATE_ZERO);
        Assert.assertEquals(lineIntersection, lineSegmentIntersection);
    }

}
