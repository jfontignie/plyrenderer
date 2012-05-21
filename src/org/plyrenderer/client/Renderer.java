package org.plyrenderer.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CanvasPixelArray;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.event.dom.client.*;

import java.util.logging.Logger;

/**
 * Author: Jacques Fontignie
 * Date: 5/19/12
 * Time: 9:51 PM
 */
public class Renderer {


/* Copyright: Mark Dwyer (markus.dwyer@gmail.com)
*
* Date: 21 February, 2012
*
*
* There is no warranty that it is fit for any particular purpose, or that it
* will work at all. You use this source code solely at your own risk, and no
* liability can be assumed for any damage or loss incurred
* by your use of this software.
*/

    private Logger logger = Logger.getLogger("Renderer");
    // Constants
    double DTOR = 0.0174532925;

    private Camera camera;

    // The drawing canvas
    Canvas canvas;
    Context2d ctx;

    private PointCloud pointCloud;


    // Scene interaction stuff
    enum Interaction {
        ROTATE,
        TRANSLATE,
        ZOOM
    }


    private boolean tracking = false;
    private Position oldMousePosition = new Position();
    Interaction interaction = Interaction.ROTATE;



    private void manageEvent(MouseEvent event) {
        Position newMousePosition = new Position(event.getX(), event.getY());

        double x = (newMousePosition.getX() - oldMousePosition.getX()) * 1.0 / camera.viewport_width;
        double y = (newMousePosition.getY() - oldMousePosition.getY()) * 1.0 / camera.viewport_height;

        if (x == 0 && y == 0) return;

        logger.info("Difference: (" + x + ":" + y + ") o=" + oldMousePosition + " n=" + newMousePosition);
        oldMousePosition.set(newMousePosition);

        switch (interaction) {
            case ROTATE:
                camera.RotateRight(x * 180.0 * DTOR);
                camera.RotateUp(y * 180.0 * DTOR);
                break;
            case TRANSLATE:
                camera.MoveUp(y);
                camera.MoveRight(x);
                break;
            case ZOOM:
                if (y == 0) return;
                camera.MoveForward(-y);
        }
        render();

    }


    public Renderer(Canvas canvas) {
        this.canvas = canvas;
        ctx = canvas.getContext2d();
        camera = new Camera();
        // Set the camera parameters
        camera.setWindow(canvas.getCoordinateSpaceWidth(),canvas.getCoordinateSpaceHeight());

        pointCloud = new PointCloud(2000);

        canvas.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(MouseDownEvent event) {
                logger.info("Mouse down detected");
                oldMousePosition.setX(event.getX());
                oldMousePosition.setY(event.getY());
                tracking = true;
            }
        });


        canvas.addMouseMoveHandler(new MouseMoveHandler() {
            public void onMouseMove(MouseMoveEvent event) {
                if (!tracking) return;
                logger.info("Mouse moved detected");
                manageEvent(event);
            }
        });

        canvas.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(MouseUpEvent event) {
                if (!tracking) return;
                logger.info("Mouse up detected");

                manageEvent(event);
                tracking = false;

            }
        });

    }


    public void initialiseData() {
        pointCloud.InitialiseData();
    }

    /*
    *  Essentially configures the camera to a default position depending
    *  on the input data
    */
    public void initialiseScene() {
        BoundingBox box = pointCloud.getBoundaries();

        // Camera variables
        Vector3d pos = new Vector3d();
        Vector3d dir = new Vector3d();


        // Set a default camera position
        pos.set(0, box.getMinX() + box.getRangeX() * 0.5);
        pos.set(1, box.getMinY() + box.getRangeY() * 0.5);
        pos.set(2, box.getMinZ() + box.getRangeZ());

        // Look at the centroid of the point data
        dir.set(0, box.getMinX() + box.getRangeX() * 0.5);
        dir.set(1, box.getMinY() + box.getRangeY() * 0.5);
        dir.set(2, box.getMinZ() + box.getRangeZ() * 0.5);

        camera.LookAt(pos, dir);
    }


    /*
    * Set all the pixels to black
    *
    */
    public void PaintBlack(CanvasPixelArray cpa) {

        logger.info("Painting the background screen: " + camera.viewport_width + ":" + camera.viewport_height);

//        ctx.setFillStyle(CssColor.make(0,0,0));
//
//        ctx.fillRect(0, 0, camera.viewport_width, camera.viewport_height);


        //for (int i = 0; i < camera.viewport_width * camera.viewport_height * 4; i += 4) {
//        for (int i = 0; i < 10000; i += 4) {
//            cpa.set(i,0);
//            cpa.set(i+1,0);
//            cpa.set(i+2,0);
//            cpa.set(i+3,255);
//        }

        logger.info("Paint successful");
    }


    /*
    * render the points according to the camera options
    *
    *
    */
    public void render() {
        double ca = camera.alpha;
        double cb = camera.beta;
        double vd = camera.view_dist;
        double vda = vd * camera.aspect_ratio;

        double a = camera.WorldTransform.get(0, 0);
        double b = camera.WorldTransform.get(0, 1);
        double c = camera.WorldTransform.get(0, 2);
        double d = camera.WorldTransform.get(0, 3);
        double e = camera.WorldTransform.get(1, 0);
        double f = camera.WorldTransform.get(1, 1);
        double g = camera.WorldTransform.get(1, 2);
        double h = camera.WorldTransform.get(1, 3);
        double i = camera.WorldTransform.get(2, 0);
        double j = camera.WorldTransform.get(2, 1);
        double k = camera.WorldTransform.get(2, 2);
        double l = camera.WorldTransform.get(2, 3);
        double n = pointCloud.getNumberOfPoints();


        ImageData id = ctx.createImageData(camera.viewport_width, camera.viewport_height);

        CanvasPixelArray cpa = id.getData();

//        logger.info("Painting black");
//        // clear the drawing surface
//        PaintBlack(cpa);

        double tempx, tempy, tempz;
        double px, py;
        int pxi, pyi;
        double x, y, z;
        int index;

        // Draw the points
        for (int p = 0; p < n; p++) {
            Point point = pointCloud.getPoint(p);

            x = point.getPoint().get(0);
            y = point.getPoint().get(1);
            z = point.getPoint().get(2);

            tempx = x * a + y * b + z * c + d;
            tempz = 1.0 / (x * i + y * j + z * k + l);
            px = ca + ca * tempx * vd * tempz;
            if (px < camera.viewport_width && px >= 0) {
                tempy = x * e + y * f + z * g + h;
                py = cb - cb * tempy * vda * tempz;
                if (py < camera.viewport_height && py >= 0) {
                    // Flooring is unfortunately necessary
                    pxi = (int) Math.floor(px);
                    pyi = (int) Math.floor(py);
                    index = (pyi * camera.viewport_width + pxi) * 4;

                    int[] colour = point.getColor();

                    cpa.set(index, colour[0]);
                    cpa.set(index + 1, colour[1]);
                    cpa.set(index + 2, colour[2]);
                    cpa.set(index + 3, 255);

                }
            }
        }

        logger.info("Set the new image");
        // Copy the canvas to the HTML5 context
        ctx.putImageData(id, 0, 0);
    }

}


