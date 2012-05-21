package org.plyrenderer.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

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

    private BoundingBox bounds = new BoundingBox();

    public PointCloud() {
    }


    /*
    *  Get the point data statistics
    *  ie. Bounding box, etc
    */
    public void InitialiseData(final AsyncCallback<Void> callback) {
        //TODO
        PlyRendererServiceAsync service = PlyRendererService.App.getInstance();
        service.getPoints(0, new AsyncCallback<Point[]>() {
            public void onFailure(Throwable caught) {
                logger.warning("Error during vertex download: " + caught);
                callback.onFailure(caught);
            }

            public void onSuccess(Point[] result) {
                numberOfPoints = result.length;
                points = result;
                for (Point point : points)
                    bounds.addPoint(point);
                bounds.flush();
                logger.info("Point(" + numberOfPoints + ") have been loaded: " + bounds);
                callback.onSuccess(null);
            }
        });

//        Random random = new Random(0);
//        numberOfPoints = 2000;
//        points = new Point[numberOfPoints];
//
//        // Calculate the bounding box
//        for (int i = 0; i < numberOfPoints; i++) {
//            double x = random.nextDouble(), y = random.nextDouble(), z = random.nextDouble();
//            Point point = new Point(x, y, z,
//                    0, 0, 0,
//                    255, 0, 0);
//
//            points[i] = point;
//
//            bounds.addPoint(point);
//        }
//
//        bounds.flush();
//        logger.info("Points have been loaded");
    }

    public BoundingBox getBoundaries() {
        return bounds;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public Point getPoint(int p) {
        return points[p];
    }
}
