/*
 * Copyright 2012 Jacques Fontignie
 *
 * This file is part of plyrenderer.
 *
 * plyrenderer is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * plyrenderer is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with plyrenderer. If not, see http://www.gnu.org/licenses/.
 */

package org.plyrenderer.client;

import java.util.logging.Logger;

/**
 * Author: Jacques Fontignie
 * Date: 5/21/12
 * Time: 9:13 AM
 */
public class BoundingBoxImpl implements BoundingBox {

    private transient final Logger logger = Logger.getLogger(BoundingBoxImpl.class.getName());


    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double minZ;
    private double maxZ;

    private double rangeX;
    private double rangeY;
    private double rangeZ;

    public BoundingBoxImpl() {
        minX = Double.MAX_VALUE;
        minY = Double.MAX_VALUE;
        minZ = Double.MAX_VALUE;
        maxX = -Double.MAX_VALUE;
        maxY = -Double.MAX_VALUE;
        maxZ = -Double.MAX_VALUE;
    }

    public BoundingBoxImpl(StorageSystem storage) {
        logger.info("Loading bounding box from storage");

        minX = Double.valueOf(storage.load(MIN_X));
        minY = Double.valueOf(storage.load(MIN_Y));
        minZ = Double.valueOf(storage.load(MIN_Z));

        maxX = Double.valueOf(storage.load(MAX_X));
        maxY = Double.valueOf(storage.load(MAX_Y));
        maxZ = Double.valueOf(storage.load(MAX_Z));

        flush();

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
        logger.info("Generating the range arguments");
        rangeX = maxX - minX;
        rangeY = maxY - minY;
        rangeZ = maxZ - minZ;
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
        return "(" + minX + "," + maxX + ")x(" + minY + "," + maxY + ")x(" + minZ + "," + maxZ + ")";
    }

    public String toJson() {
        StringBuilder builder = new StringBuilder("{");
        builder.append(JsonUtils.createField(MIN_X, minX));
        builder.append(",");
        builder.append(JsonUtils.createField(MIN_Y, minY));
        builder.append(",");
        builder.append(JsonUtils.createField(MIN_Z, minZ));
        builder.append(",");

        builder.append(JsonUtils.createField(MAX_X, maxX));
        builder.append(",");
        builder.append(JsonUtils.createField(MAX_Y, maxY));
        builder.append(",");
        builder.append(JsonUtils.createField(MAX_Z, maxZ));

        builder.append("}");
        return builder.toString();
    }



}
