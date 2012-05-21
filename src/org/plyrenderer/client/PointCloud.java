package org.plyrenderer.client;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Author: Jacques Fontignie
 * Date: 5/21/12
 * Time: 7:21 AM
 */
public class PointCloud {

    private final Logger logger = Logger.getLogger("PointCloud");
    // Dataset variables
    int numberOfPoints;

    private Point[] points;

    private BoundingBox bounds = new BoundingBox();
    double[] range = new double[3];


    public PointCloud(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }


    /*
    *  Get the point data statistics
    *  ie. Bounding box, etc
    */
    public void InitialiseData() {
        //TODO

        Random random = new Random(0);
        numberOfPoints = 2000;
        points = new Point[numberOfPoints];

        // Calculate the bounding box
        for (int i = 0; i < numberOfPoints; i++) {
            double x = random.nextDouble(), y = random.nextDouble(), z = random.nextDouble();
            Point point = new Point(x, y, z,
                    0, 0, 0,
                    255, 0, 0);

            points[i] = point;

            bounds.addPoint(point);
        }

        bounds.flush();
        logger.info("Points have been loaded");
    }

    public BoundingBox getBoundaries() {
        return bounds;
    }

    public double[] getRanges() {
        return range;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public Point getPoint(int p) {
        return points[p];
    }
}
