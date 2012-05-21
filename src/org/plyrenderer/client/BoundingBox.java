package org.plyrenderer.client;

/**
 * Author: Jacques Fontignie
 * Date: 5/21/12
 * Time: 9:13 AM
 */
public class BoundingBox {

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double minZ;
    private double maxZ;

    private double rangeX;
    private double rangeY;
    private double rangeZ;


    public BoundingBox() {
        minX = Double.MAX_VALUE;
        minY = Double.MAX_VALUE;
        minZ = Double.MAX_VALUE;
        maxX = -Double.MAX_VALUE;
        maxY = -Double.MAX_VALUE;
        maxZ = -Double.MAX_VALUE;
    }

    public void addPoint(Point point) {
        Vector3d p = point.getPoint();
        if (p.get(0) < minX) minX = p.get(0);
        if (p.get(0) > maxX) maxX = p.get(0);

        if (p.get(1) < minY) minY = p.get(1);
        if (p.get(1) > maxY) maxY = p.get(1);

        if (p.get(2) < minZ) minZ = p.get(2);
        if (p.get(2) > maxZ) maxZ = p.get(2);
    }

    public void flush() {
        rangeX = maxX-minX;
        rangeY = maxY-minY;
        rangeZ = maxZ-minZ;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public double getRangeX() {
        return rangeX;
    }

    public double getRangeY() {
        return rangeY;
    }

    public double getRangeZ() {
        return rangeZ;
    }
}
