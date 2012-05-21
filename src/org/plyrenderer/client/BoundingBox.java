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
        if (p.getX() < minX) minX = p.getX();
        if (p.getX() > maxX) maxX = p.getX();

        if (p.getY() < minY) minY = p.getY();
        if (p.getY() > maxY) maxY = p.getY();

        if (p.getZ() < minZ) minZ = p.getZ();
        if (p.getZ() > maxZ) maxZ = p.getZ();
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

    public String toString() {
        return "(" + minX + "," + maxX + ")x(" + minY + ","  + maxY + ")x(" + minZ + "," + maxZ + ")";
    }
}
