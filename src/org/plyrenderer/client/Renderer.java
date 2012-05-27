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

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CanvasPixelArray;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Author: Jacques Fontignie
 * Date: 5/19/12
 * Time: 9:51 PM
 */
public class Renderer {


/*
* Code translated from: http://mdkey.org/?p=406
* original source code: Mark Dwyer (markus.dwyer@gmail.com)
*
*/

    private Logger logger = Logger.getLogger("Renderer");
    // Constants
    double DTOR = 0.0174532925;

    private Camera camera;

    // The drawing canvas
    private Canvas canvas;
    private Context2d ctx;

    private PointCloud pointCloud;

    private double fps;

    private double[] zBuffer;

    private BoundingBox box;


    // Scene interaction stuff
    enum Interaction {
        ROTATE,
        TRANSLATE,
        ZOOM
    }

    private List<RendererListener> listeners;


    private boolean tracking = false;
    private Position oldMousePosition = new Position();
    Interaction interaction = Interaction.ROTATE;


    private void manageEvent(MouseEvent event) {
        Position newMousePosition = new Position(event.getX(), event.getY());

        managePositionChange(newMousePosition);

    }

    private void managePositionChange(Position newPosition) {
        double x = (newPosition.getX() - oldMousePosition.getX()) * 1.0 / camera.getViewportWidth();
        double y = (newPosition.getY() - oldMousePosition.getY()) * 1.0 / camera.getViewportHeight();

        if (x == 0 && y == 0) return;

        logger.finer("Difference: (" + x + ":" + y + ") o=" + oldMousePosition + " n=" + newPosition);
        oldMousePosition.set(newPosition);

        switch (interaction) {
            case ROTATE:
                camera.rotateRight(x * 180.0 * DTOR);
                camera.rotateUp(y * 180.0 * DTOR);
                break;
            case TRANSLATE:
                camera.moveUp(y);
                camera.moveRight(x);
                break;
            case ZOOM:
                if (y == 0) return;
                camera.moveForward(-y);
        }
        render();
    }


    public Renderer(Canvas canvas) {
        this.canvas = canvas;
        ctx = canvas.getContext2d();
        camera = new Camera();
        // Set the camera parameters
        camera.setWindow(canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());

        zBuffer = new double[canvas.getCoordinateSpaceHeight() * canvas.getCoordinateSpaceWidth()];

        listeners = new ArrayList<RendererListener>();

    }


    public void setBoundingBox(BoundingBox box) {
        this.box = box;
    }

    public void setPointCloud(PointCloud cloud) {
        this.pointCloud = cloud;
    }

    public void enable() {
        canvas.addKeyDownHandler(new KeyDownHandler() {

            public void onKeyDown(KeyDownEvent event) {

                logger.info("Key pressed: " + event.getNativeKeyCode());
                double x = 0.1;
                double y = 0.1;
                switch (event.getNativeKeyCode()) {
                    case 37:
                        if (event.isShiftKeyDown())
                            camera.rotateRight(x * 0.5 * 180.0 * DTOR);
                        else
                            camera.moveRight(x);
                        break;
                    case 38:
                        if (event.isShiftKeyDown())
                            camera.rotateUp(y * 0.5 * 180.0 * DTOR);
                        else
                            camera.moveForward(y);
                        break;
                    case 39:
                        if (event.isShiftKeyDown())
                            camera.rotateRight(-x * 0.5 * 180.0 * DTOR);
                        else
                            camera.moveRight(-x);
                        break;
                    case 40:
                        if (event.isShiftKeyDown())
                            camera.rotateUp(-y * 0.5 * 180.0 * DTOR);
                        else
                            camera.moveForward(-y);
                        break;
                    default:
                        return;
                }
                render();
            }
        });


        canvas.addTouchMoveHandler(new TouchMoveHandler() {
            public void onTouchMove(TouchMoveEvent event) {
                logger.info("Touch event occured");
                event.preventDefault();
                if (event.getChangedTouches().length() == 0) return;

                int length = event.getChangedTouches().length();
                Touch touch = event.getChangedTouches().get(length - 1);
                Position newPosition = new Position(touch.getRelativeX(Renderer.this.canvas.getCanvasElement()), touch.getRelativeY(Renderer.this.canvas.getCanvasElement()));
                managePositionChange(newPosition);

            }
        });

        canvas.addTouchStartHandler(new TouchStartHandler() {
            public void onTouchStart(TouchStartEvent event) {
                logger.info("Touch start");
                event.preventDefault();
                Touch touch = event.getChangedTouches().get(0);
                oldMousePosition.set(touch.getRelativeX(Renderer.this.canvas.getCanvasElement()), touch.getRelativeY(Renderer.this.canvas.getCanvasElement()));
            }
        });

        canvas.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(MouseDownEvent event) {
                logger.fine("Mouse down detected");
                oldMousePosition.setX(event.getX());
                oldMousePosition.setY(event.getY());
                tracking = true;
            }
        });


        canvas.addMouseMoveHandler(new MouseMoveHandler() {
            public void onMouseMove(MouseMoveEvent event) {
                if (!tracking) return;
                logger.fine("Mouse moved detected");
                manageEvent(event);
            }
        });

        canvas.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(MouseUpEvent event) {
                if (!tracking) return;
                logger.fine("Mouse up detected");

                manageEvent(event);
                tracking = false;

            }
        });
    }


    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    /*
    *  Essentially configures the camera to a default position depending
    *  on the input data
    */
    public void initialiseScene() {

        // Camera variables
        Vector3d pos = new Vector3d();
        Vector3d dir = new Vector3d();


        // Set a default camera position
        pos.setX(box.getMinX() + box.getRangeX() * 0.5);
        pos.setY(box.getMinY() + box.getRangeY() * 0.5);
        pos.setZ(box.getMinZ() + Math.max(box.getRangeZ(), Math.max(box.getRangeX(), box.getRangeY())));

        // Look at the centroid of the point data
        dir.setX(box.getMinX() + box.getRangeX() * 0.5);
        dir.setY(box.getMinY() + box.getRangeY() * 0.5);
        dir.setZ(box.getMinZ() + box.getRangeZ() * 0.5);

        camera.lookAt(pos, dir);
    }

    private static native int floor(double x) /*-{
        return ~~x;
    }-*/;

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

        double a = camera.worldTransform.get(0, 0);
        double b = camera.worldTransform.get(0, 1);
        double c = camera.worldTransform.get(0, 2);
        double d = camera.worldTransform.get(0, 3);
        double e = camera.worldTransform.get(1, 0);
        double f = camera.worldTransform.get(1, 1);
        double g = camera.worldTransform.get(1, 2);
        double h = camera.worldTransform.get(1, 3);
        double i = camera.worldTransform.get(2, 0);
        double j = camera.worldTransform.get(2, 1);
        double k = camera.worldTransform.get(2, 2);
        double l = camera.worldTransform.get(2, 3);
        double n = pointCloud.getNumberOfPoints();

        long start = System.currentTimeMillis();
        ImageData id = ctx.createImageData(camera.getViewportWidth(), camera.getViewportHeight());

        CanvasPixelArray cpa = id.getData();

        double tempx, tempy, tempz;
        double invtempz;
        double px, py;
        int pxi, pyi;
        double x, y, z;
        int index;

        double cavd = ca * vd;
        double cbvda = cb * vda;

        // Draw the points
        for (int p = 0; p < n; p++) {
            Point point = pointCloud.getPoint(p);

            x = point.getPoint().getX();
            y = point.getPoint().getY();
            z = point.getPoint().getZ();

            tempx = x * a + y * b + z * c + d;
            tempz = (x * i + y * j + z * k + l);

            if (tempz <= 0) continue;
            invtempz = 1.0 / tempz;

            px = ca + cavd * tempx * invtempz;
            if (px < camera.getViewportWidth() && px >= 0) {
                tempy = x * e + y * f + z * g + h;
                py = cb - cbvda * tempy * invtempz;
                if (py < camera.getViewportHeight() && py >= 0) {
                    // Flooring is unfortunately necessary
                    pxi = floor(px);
                    pyi = floor(py);
                    index = (pyi * camera.getViewportWidth() + pxi) * 4;

                    //Check alpha to determine if it was already set.
                    //If not, it means it is the first time we add a pixel
                    //if yes, we already set if and need to check the value of zbuffer
                    if (cpa.get(index + 3) == 255) {
                        //Value already set, let's look in the z buffer
                        if (zBuffer[index] <= tempz)
                            continue;
                    }

                    zBuffer[index] = tempz;
                    int[] colour = point.getColor();

                    cpa.set(index, colour[0]);
                    cpa.set(index + 1, colour[1]);
                    cpa.set(index + 2, colour[2]);
                    cpa.set(index + 3, 255);

                }
            }
        }


        logger.fine("Set the new image");
        // Copy the canvas to the HTML5 context
        ctx.putImageData(id, 0, 0);

        long end = System.currentTimeMillis();
        fps = 1000. / (end - start);

        for (RendererListener listener : listeners)
            listener.event();
    }

    public double getFps() {
        return fps;
    }

    public void addListener(RendererListener listener) {
        listeners.add(listener);
    }

    public interface RendererListener {
        public void event();
    }

}


