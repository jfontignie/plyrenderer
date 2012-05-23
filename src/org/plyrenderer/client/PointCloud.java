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
 * Time: 7:21 AM
 */
public class PointCloud {

    private final Logger logger = Logger.getLogger("PointCloud");
    // Dataset variables
    private int numberOfPoints;

    private Point[] points;

    public PointCloud(int size) {
        points = new Point[size];
        numberOfPoints = 0;
    }


    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public Point getPoint(int p) {
        return points[p];
    }

    public void addPoints(Point[] result) {
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < result.length; i++) {
            points[numberOfPoints++] = result[i];
        }
    }
}
