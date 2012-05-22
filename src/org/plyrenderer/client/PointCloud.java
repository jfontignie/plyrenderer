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
        for (int i = 0; i < result.length; i++) {
            points[numberOfPoints++] = result[i];
        }
    }
}
