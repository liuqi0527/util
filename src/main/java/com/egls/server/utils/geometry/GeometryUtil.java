package com.egls.server.utils.geometry;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Objects;

import com.egls.server.utils.NumberUtil;
import com.egls.server.utils.math.MathUtil;
import com.egls.server.utils.math.TrigonometricMathUtil;
import com.egls.server.utils.random.RandomUtil;

import org.apache.commons.lang3.ObjectUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineSegment;

/**
 * 提供一些集合算法
 *
 * @author mayer - [Created on 2018-08-31 10:51]
 */
public final class GeometryUtil {

    /**
     * 坐标点的精度,有可能会修改,修改时不希望使用者要重新编译
     * <p>
     * This can prevent javac from inlining a constant field.
     * This way any jars that refer to this field do not have to recompile themselves if the field's value changes at some future date.
     */
    public static final double POINT_PRECISION = ObjectUtils.CONST(1000D);

    /**
     * 距离的精度,有可能会修改,修改时不希望使用者要重新编译
     * <p>
     * This can prevent javac from inlining a constant field.
     * This way any jars that refer to this field do not have to recompile themselves if the field's value changes at some future date.
     */
    public static final double DISTANCE_PRECISION = ObjectUtils.CONST(100D);

    /**
     * 多边形最少顶点数
     */
    public static final int POLYGON_VERTEX_MIN_COUNT = 3;

    /**
     * 判断点A和点B的距离是否小于等于给定的距离.
     * 判断点B是否在点A的一定范围内.
     * 判断以点A为圆心,产生圆A',点B在圆A'内.
     */
    public static boolean validateDistance2D(final double x1, final double y1,
                                             final double x2, final double y2,
                                             final double distance) {
        return calculateDistance2D(x1, y1, x2, y2) <= distance;
    }

    /**
     * 判断点A和点B的距离是否小于等于给定的距离.
     * 判断点B是否在点A的一定范围内.
     * 判断以点A为圆心,产生圆A',点B在圆A'内.
     */
    public static boolean validateDistance2D(final Point2D p1, final Point2D p2, final double distance) {
        return calculateDistance2D(p1, p2) <= distance;
    }

    public static double calculateDistance2D(final double x1, final double y1,
                                             final double x2, final double y2) {
        double tx = x1 - x2;
        double ty = y1 - y2;
        return Math.sqrt((tx * tx) + (ty * ty));
    }

    public static double calculateDistance2D(final Point2D p1, final Point2D p2) {
        if (Objects.isNull(p1) || Objects.isNull(p2)) {
            return 0;
        }
        return ((long) (calculateDistance2D(p1.x, p1.z, p2.x, p2.z) * DISTANCE_PRECISION)) / DISTANCE_PRECISION;
    }

    /**
     * 判断点A和点B的距离是否小于等于给定的距离.
     * 判断点B是否在点A的一定范围内.
     * 判断以点A为球心,产生球A',点B在球A'内.
     */
    public static boolean validateDistance3D(final double x1, final double y1, final double z1,
                                             final double x2, final double y2, final double z2,
                                             final double distance) {
        return calculateDistance3D(x1, y1, z1, x2, y2, z2) <= distance;
    }

    /**
     * 判断点A和点B的距离是否小于等于给定的距离.
     * 判断点B是否在点A的一定范围内.
     * 判断以点A为球心,产生球A',点B在球A'内.
     */
    public static boolean validateDistance3D(final Point3D p1, final Point3D p2, final double distance) {
        return calculateDistance3D(p1, p2) <= distance;
    }

    public static double calculateDistance3D(final double x1, final double y1, final double z1,
                                             final double x2, final double y2, final double z2) {
        double tx = x1 - x2;
        double ty = y1 - y2;
        double tz = z1 - z2;
        return Math.sqrt((tx * tx) + (ty * ty) + (tz * tz));
    }

    public static double calculateDistance3D(final Point3D p1, final Point3D p2) {
        if (Objects.isNull(p1) || Objects.isNull(p2)) {
            return 0;
        }
        return ((long) (calculateDistance3D(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z) * DISTANCE_PRECISION)) / DISTANCE_PRECISION;
    }

    /**
     * 获取两个点的弧度表示.2D
     */
    public static double getRadian(final Point2D home, final Point2D target) {
        //由于游戏地图的坐标系左上顶点为0,0,需要旋转,所以z是x,x是y
        return TrigonometricMathUtil.getRadian(home.getZ(), home.getX(), target.getZ(), target.getX());
    }

    /**
     * 获取两个点的角度表示,2D,角度区间[0-360],角度精度为1度
     */
    public static short getAngle(final Point2D home, final Point2D target) {
        return toAngle(getRadian(home, target));
    }

    /**
     * 将弧度转换为角度,角度区间[0-360],角度精度为1度
     *
     * @param radian 弧度
     * @return 角度
     */
    public static short toAngle(final double radian) {
        final double angle = TrigonometricMathUtil.toAngle(radian);
        return (short) TrigonometricMathUtil.truncateAngle(angle);
    }

    /**
     * 将角度转为弧度,弧度区间[0-2PI],角度精度为1度
     *
     * @param angle 角度
     * @return 弧度
     */
    public static double toRadian(final short angle) {
        return TrigonometricMathUtil.toRadian(angle);
    }

    /**
     * 根据给定的点,弧度和距离，得到一个新的点
     */
    public static Point2D calculateNewPoint2D(final Point2D home, final double radian, final double distance) {
        double newX = home.getX() + (distance * TrigonometricMathUtil.getSinByRadian(radian));
        double newZ = home.getZ() + (distance * TrigonometricMathUtil.getCosByRadian(radian));
        return new Point2D(newX, newZ);
    }

    /**
     * 根据给定的点,角度和距离，得到一个新的点
     */
    public static Point2D calculateNewPoint2D(final Point2D home, final short angle, final double distance) {
        return calculateNewPoint2D(home, toRadian(angle), distance);
    }

    /**
     * 根据给定的点,弧度和距离，得到一个新的点.新坐标点的高度与home相同
     */
    public static Point3D calculateNewPoint3D(final Point3D home, final double radian, final double distance) {
        return new Point3D(GeometryUtil.calculateNewPoint2D(home, radian, distance), home.getY());
    }

    /**
     * 根据给定的点,角度和距离，得到一个新的点.新坐标点的高度与home相同
     */
    public static Point3D calculateNewPoint3D(final Point3D home, final short angle, final double distance) {
        return calculateNewPoint3D(home, toRadian(angle), distance);
    }

    public static boolean isSamePoint2D(final Point2D point1, final Point2D point2) {
        return Objects.equals(point1, point2);
    }

    public static boolean isSamePoint3D(final Point3D point1, final Point3D point2) {
        return Objects.equals(point1, point2);
    }

    public static Point2D randomNewPoint2D(final Point2D p1, final Point2D p2) {
        if (p1 == null || p2 == null || isSamePoint2D(p1, p2)) {
            throw new IllegalArgumentException("point range error");
        }

        final double minX = Math.min(p1.getX(), p2.getX());
        final double maxX = Math.max(p1.getX(), p2.getX());
        final double minZ = Math.min(p1.getZ(), p2.getZ());
        final double maxZ = Math.max(p1.getZ(), p2.getZ());

        final double nx = minX + RandomUtil.randomDouble(maxX - minX);
        final double nz = minZ + RandomUtil.randomDouble(maxZ - minZ);

        return new Point2D(nx, nz);
    }

    public static Point3D randomNewPoint3D(final Point3D p1, final Point3D p2) {
        if (p1 == null || p2 == null || isSamePoint3D(p1, p2)) {
            throw new IllegalArgumentException("point range error");
        }

        final double minX = Math.min(p1.getX(), p2.getX());
        final double maxX = Math.max(p1.getX(), p2.getX());
        final double minY = Math.min(p1.getY(), p2.getY());
        final double maxY = Math.max(p1.getY(), p2.getY());
        final double minZ = Math.min(p1.getZ(), p2.getZ());
        final double maxZ = Math.max(p1.getZ(), p2.getZ());

        final double nx = minX + RandomUtil.randomDouble(maxX - minX);
        final double ny = minY + RandomUtil.randomDouble(maxY - minY);
        final double nz = minZ + RandomUtil.randomDouble(maxZ - minZ);

        return new Point3D(nx, ny, nz);
    }

    /**
     * 判断一个点是否在矩形内.
     */
    public static boolean isPointInRectangle2D(final Point2D p, final Point2D vertex1, final Point2D vertex2) {
        final double minX = Math.min(vertex1.getX(), vertex2.getX());
        final double maxX = Math.max(vertex1.getX(), vertex2.getX());
        final double minZ = Math.min(vertex1.getZ(), vertex2.getZ());
        final double maxZ = Math.max(vertex1.getZ(), vertex2.getZ());
        return p.x >= minX && p.x <= maxX && p.z >= minZ && p.z <= maxZ;
    }

    /**
     * 判断一个点是否在立方体内
     */
    public static boolean isPointInCube3D(final Point3D p, final Point3D vertex1, final Point3D vertex2) {
        final double minX = Math.min(vertex1.getX(), vertex2.getX());
        final double maxX = Math.max(vertex1.getX(), vertex2.getX());
        final double minY = Math.min(vertex1.getY(), vertex2.getY());
        final double maxY = Math.max(vertex1.getY(), vertex2.getY());
        final double minZ = Math.min(vertex1.getZ(), vertex2.getZ());
        final double maxZ = Math.max(vertex1.getZ(), vertex2.getZ());
        return p.x >= minX && p.x <= maxX && p.y >= minY && p.y <= maxY && p.z >= minZ && p.z <= maxZ;
    }

    /**
     * 判断线段ab和线段cd是否相交,此方法是2维的,仅判断x和z维度.
     */
    public static boolean isLineIntersectsLine2D(final Point2D a, final Point2D b, final Point2D c, final Point2D d) {
        //本方法速度快
        final Line2D l1 = new Line2D.Double(a.getX(), a.getZ(), b.getX(), b.getZ());
        final Line2D l2 = new Line2D.Double(c.getX(), c.getZ(), d.getX(), d.getZ());
        return l1.intersectsLine(l2);
    }

    /**
     * 判断一个矩形和线是否相交
     *
     * @param linePoint1 线的点1
     * @param linePoint2 线的点2
     * @param vertex1    矩形对角顶点1
     * @param vertex2    矩形对角顶点2
     * @return 相交 - true, 不相交 - false
     */
    public static boolean isLineIntersectsRectangle2D(final Point2D linePoint1, final Point2D linePoint2,
                                                      final Point2D vertex1, final Point2D vertex2) {
        if (linePoint1 == null || linePoint2 == null || isSamePoint2D(linePoint1, linePoint2)) {
            throw new IllegalArgumentException("line point error");
        }
        if (vertex1 == null || vertex2 == null || isSamePoint2D(vertex1, vertex2)) {
            throw new IllegalArgumentException("vertex point error");
        }

        final double minX = Math.min(vertex1.getX(), vertex2.getX());
        final double maxX = Math.max(vertex1.getX(), vertex2.getX());
        final double minZ = Math.min(vertex1.getZ(), vertex2.getZ());
        final double maxZ = Math.max(vertex1.getZ(), vertex2.getZ());

        final Line2D line = new Line2D.Double(linePoint1.getX(), linePoint1.getZ(), linePoint2.getX(), linePoint2.getZ());
        //增加这个对角线这个条件,主要是正确判断,0宽度或者0高度的矩形
        final Line2D diagonal = new Line2D.Double(minX, minZ, maxX, maxZ);
        final Rectangle2D rectangle2D = new Rectangle2D.Double(minX, minZ, maxX - minX, maxZ - minZ);
        return line.intersectsLine(diagonal) || line.intersects(rectangle2D);
    }

    public static boolean isRectangleIntersectsRectangle2D(final Point2D vertex1, final Point2D vertex2,
                                                           final Point2D vertex3, final Point2D vertex4) {
        final double minX1 = Math.min(vertex1.getX(), vertex2.getX());
        final double maxX1 = Math.max(vertex1.getX(), vertex2.getX());
        final double minZ1 = Math.min(vertex1.getZ(), vertex2.getZ());
        final double maxZ1 = Math.max(vertex1.getZ(), vertex2.getZ());
        final Rectangle2D rectangle1 = new Rectangle2D.Double(minX1, minZ1, maxX1 - minX1, maxZ1 - minZ1);

        final double minX2 = Math.min(vertex3.getX(), vertex4.getX());
        final double maxX2 = Math.max(vertex3.getX(), vertex4.getX());
        final double minZ2 = Math.min(vertex3.getZ(), vertex4.getZ());
        final double maxZ2 = Math.max(vertex3.getZ(), vertex4.getZ());
        final Rectangle2D rectangle2 = new Rectangle2D.Double(minX2, minZ2, maxX2 - minX2, maxZ2 - minZ2);

        return rectangle1.intersects(rectangle2);
    }

    public static boolean isCircleIntersectsCircle2D(final Point2D center1, final double radius1,
                                                     final Point2D center2, final double radius2) {
        if (radius1 < 0 || radius2 < 0) {
            throw new IllegalArgumentException("radius must be positive");
        }
        if (NumberUtil.isApproximateZero(radius1) && NumberUtil.isApproximateZero(radius2)) {
            throw new IllegalArgumentException("radius must be positive");
        }
        final Rectangle2D rectangle2 = new Rectangle2D.Double(center2.getX() - radius2, center2.getZ() - radius2, 2 * radius2, 2 * radius2);
        if (NumberUtil.isApproximateZero(radius1)) {
            return rectangle2.contains(center1.getX(), center1.getZ());
        }
        final Rectangle2D rectangle1 = new Rectangle2D.Double(center1.getX() - radius1, center1.getZ() - radius1, 2 * radius1, 2 * radius1);
        if (NumberUtil.isApproximateZero(radius2)) {
            return rectangle1.contains(center2.getX(), center2.getZ());
        }
        return rectangle1.intersects(rectangle2);
    }

    public static boolean isCircleIntersectsRectangle2D(final Point2D center, final double radius,
                                                        final Point2D vertex1, final Point2D vertex2) {
        if (radius < 0) {
            throw new IllegalArgumentException("radius must be positive");
        }
        if (NumberUtil.isApproximateZero(radius)) {
            return isPointInRectangle2D(center, vertex1, vertex2);
        } else {
            final double minX = Math.min(vertex1.getX(), vertex2.getX());
            final double maxX = Math.max(vertex1.getX(), vertex2.getX());
            final double minZ = Math.min(vertex1.getZ(), vertex2.getZ());
            final double maxZ = Math.max(vertex1.getZ(), vertex2.getZ());
            final Rectangle2D rectangle1 = new Rectangle2D.Double(minX, minZ, maxX - minX, maxZ - minZ);
            final Rectangle2D rectangle2 = new Rectangle2D.Double(center.getX() - radius, center.getZ() - radius, 2 * radius, 2 * radius);
            return rectangle1.intersects(rectangle2);
        }
    }

    /**
     * 2D判断一个点是否在多边形内.
     */
    public static boolean isPointInPolygon2D(final Point2D p, final Point2D[] polygonVertices) {
        //多边形的点少于3,则不能成为多边形.
        if (polygonVertices.length < POLYGON_VERTEX_MIN_COUNT) {
            return false;
        }
        //如果给定点是某个顶点,顶点算在多边形内
        if (Arrays.asList(polygonVertices).contains(p)) {
            return true;
        }

        final int[] xs = new int[polygonVertices.length];
        final int[] zs = new int[polygonVertices.length];
        for (int i = 0; i < polygonVertices.length; i++) {
            xs[i] = MathUtil.getIntValue(polygonVertices[i].getX() * POINT_PRECISION);
            zs[i] = MathUtil.getIntValue(polygonVertices[i].getZ() * POINT_PRECISION);
        }
        final Polygon polygon = new Polygon(xs, zs, polygonVertices.length);
        final int x = MathUtil.getIntValue(p.getX() * POINT_PRECISION);
        final int z = MathUtil.getIntValue(p.getZ() * POINT_PRECISION);
        return polygon.contains(x, z);
    }

    public static void adjustRectangleDiagonalVertex2D(Point2D vertex1, Point2D vertex2) {
        double minX = Math.min(vertex1.getX(), vertex2.getX());
        double maxX = Math.max(vertex1.getX(), vertex2.getX());
        double minZ = Math.min(vertex1.getZ(), vertex2.getZ());
        double maxZ = Math.max(vertex1.getZ(), vertex2.getZ());
        vertex1.setXZ(minX, minZ);
        vertex2.setXZ(maxX, maxZ);
    }

    public static void adjustCubeDiagonalVertex3D(Point3D vertex1, Point3D vertex2) {
        double minX = Math.min(vertex1.getX(), vertex2.getX());
        double maxX = Math.max(vertex1.getX(), vertex2.getX());
        double minY = Math.min(vertex1.getY(), vertex2.getY());
        double maxY = Math.max(vertex1.getY(), vertex2.getY());
        double minZ = Math.min(vertex1.getZ(), vertex2.getZ());
        double maxZ = Math.max(vertex1.getZ(), vertex2.getZ());
        vertex1.setXYZ(minX, minY, minZ);
        vertex2.setXYZ(maxX, maxY, maxZ);
    }

    /**
     * 计算由两条线段(如果有一条线段)定义的无限延伸线的交点。两条直线之间可能有0、1或无数个交点(重合)。
     *
     * @param p1 线段p1点
     * @param p2 线段p2点
     * @param q1 线段q1点
     * @param q2 线段q2点
     * @return 如果有一个唯一的交点，就会返回。否则，返回null
     */
    public static Point2D lineIntersection(Point2D p1, Point2D p2, Point2D q1, Point2D q2) {
        LineSegment lineSegment1 = new LineSegment(p1.getX(), p1.getZ(), p2.getX(), p2.getZ());
        LineSegment lineSegment2 = new LineSegment(q1.getX(), q1.getZ(), q2.getX(), q2.getZ());
        Coordinate coordinate = lineSegment1.lineIntersection(lineSegment2);
        if (coordinate != null) {
            return new Point2D(coordinate.x, coordinate.y);
        }
        return null;
    }

    /**
     * 计算两个线段之间的交点，如果有一个线段的话。两个部分之间可能有0,1或多个交点。
     *
     * @param p1 线段p1点
     * @param p2 线段p2点
     * @param q1 线段q1点
     * @param q2 线段q2点
     * @return 如果有0，则返回null。如果有1个或更多，则会返回其中的一个(根据算法选择)
     */
    public static Point2D lineSegmentIntersection(Point2D p1, Point2D p2, Point2D q1, Point2D q2) {
        LineSegment lineSegment1 = new LineSegment(p1.getX(), p1.getZ(), p2.getX(), p2.getZ());
        LineSegment lineSegment2 = new LineSegment(q1.getX(), q1.getZ(), q2.getX(), q2.getZ());
        Coordinate coordinate = lineSegment1.intersection(lineSegment2);
        if (coordinate != null) {
            return new Point2D(coordinate.x, coordinate.y);
        }
        return null;
    }

}
