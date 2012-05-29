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

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.logging.Logger;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class PlyRenderer implements EntryPoint {

    static final String VERSION = "1.1";

    private static final String DEFAULT_PLY = "sample";

    private static final int canvasHeight = 500;
    private static final int canvasWidth = 500;
    public static final String POINTS = "points";
    public static final String PLY_INFO = "PlyInfo";
    public static final int MAX_SIMULTANEOUS_QUERIES = 10;
    public static final int REFRESH_MILLISECONDS = 500;
    private Renderer renderer;
    private PointCloud cloud;

    private PlyRendererServiceAsync service;

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Label percentage;

    private RadioButton b1, b2, b3;
    private Label fps;

    private Canvas canvas;

    private static NumberFormat format = NumberFormat.getFormat(".00");

    private String ply;

    private StorageSystem storage;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        service = PlyRendererService.App.getInstance();

        canvas = Canvas.createIfSupported();

        if (canvas == null) {
            RootPanel.get("canvas").add(new Label("Sorry, your browser doesn't support the HTML5 Canvas element"));
            return;
        }


        //canvas.setStyleName("mainCanvas");
        canvas.setWidth(canvasWidth + "px");
        canvas.setCoordinateSpaceWidth(canvasWidth);

        canvas.setHeight(canvasHeight + "px");
        canvas.setCoordinateSpaceHeight(canvasHeight);

        canvas.setVisible(false);
        RootPanel.get("canvas").add(canvas);
        renderer = new Renderer(canvas);

        createControl();

        ply = Window.Location.getParameter("ply");
        if (ply == null || ply.equals(""))
            ply = DEFAULT_PLY;

        logger.info("--- READING PARAMETERS ---");
        String useNormal = Window.Location.getParameter("normal");
        if (useNormal != null) {
            renderer.setUseNormal(useNormal.equalsIgnoreCase("true"));
        }

        String useLight = Window.Location.getParameter("light");
        if (useLight != null) {
            logger.info("Use Light: " + useLight);
            renderer.setUseLight(useLight.toLowerCase().equals("true"));
        }


        storage = new StorageSystem(ply);

        String value = storage.load(ply);

        boolean uptodate = false;
        if (value != null) {
            uptodate = VERSION.equals(value);
            if (!uptodate) storage.clear();
        }


        if (uptodate) {
            logger.info(ply + " found, will load from storage device");
            //Data is present, let's load it
            String plyContent = storage.load(PLY_INFO);
            logger.info("PlyContent is: " + plyContent);

            if (plyContent != null) {
                PlyInfo plyInfo = asPlyInfo(plyContent);
                setPlyInfo(plyInfo);
                return;

            }
        }


        service.getInfo(ply, new AsyncCallback<PlyInfoImpl>() {
            public void onFailure(Throwable caught) {
                logger.warning("Impossible to get the PLY information: " + caught);
            }

            public void onSuccess(PlyInfoImpl result) {
                logger.info("Successfully received the PLY information");
                logger.info("Saving: " + ply);
                storage.save(ply, VERSION);
                storage.save(PLY_INFO, result.toJson());
                setPlyInfo(result);
            }
        });


    }


    private void setPlyInfo(PlyInfo result) {

        renderer.setBoundingBox(result.getBoundingBox());

        renderer.initialiseScene();
        canvas.setVisible(true);

        int chunkSize = result.getChunkSize();
        final int numPoints = result.getNumPoints();
        cloud = new PointCloud(numPoints);

        renderer.setPointCloud(cloud);

        PointLoader loader = new PointLoader(numPoints, chunkSize);
        loader.run();
    }

    private class PointLoader {
        private int numPoints;
        private int chunkSize;
        private int offset;

        public PointLoader(int numPoints, int chunkSize) {
            this.numPoints = numPoints;
            this.chunkSize = chunkSize;
            offset = 0;
        }

        public void run() {
            AnimationScheduler.get().requestAnimationFrame(new MyCallback(), canvas.getCanvasElement());
        }

        private class MyCallback implements AnimationScheduler.AnimationCallback {

            public void execute(double timestamp) {
                int simultaneousQueries = 0;
                long start = System.currentTimeMillis();
                for (; offset < numPoints; offset += chunkSize) {
                    //Check if offset is in storage
                    String pointsString = storage.load(POINTS + offset);
                    if (pointsString != null) {
                        JsArray<JsonPoint> points = asJSPointArray(pointsString);
                        for (int i = 0; i < points.length(); i++) {
                            JsonPoint p = points.get(i);
                            cloud.addPoint(new Point(p.getX(), p.getY(), p.getZ(), p.getNX(), p.getNY(), p.getNZ(), p.getRed(), p.getGreen(), p.getBlue()));
                        }
                        update(numPoints);
                    } else {
                        simultaneousQueries++;
                        downloadPoints(numPoints, offset);
                    }


                    //If the time is more than 1 second, let's stop and ask for an animation frame
                    if (System.currentTimeMillis() - start > REFRESH_MILLISECONDS || simultaneousQueries > MAX_SIMULTANEOUS_QUERIES) {
                        offset += chunkSize;
                        if (offset < numPoints) {
                            AnimationScheduler.get().requestAnimationFrame(this);
                            return;
                        }
                    }
                }
            }
        }


    }

    private void update(int numPoints) {
        double percent = cloud.getNumberOfPoints() * 1. / numPoints * 100;

        percentage.setText(format.format(percent) + "%");

        renderer.render();

        if (cloud.getNumberOfPoints() == numPoints) {
            percentage.setText(cloud.getNumberOfPoints() + " p.");
            enable();
        }


    }


    private void downloadPoints(final int numPoints, final int offset) {
        service.getPoints(ply, offset, new AsyncCallback<Point[]>() {
            public void onFailure(Throwable caught) {
                percentage.setText("Error!");
                logger.warning("Impossible to get the points");
            }

            public void onSuccess(Point[] result) {
                //Let's save the points in the storage...

                StringBuilder builder = new StringBuilder("[");
                for (Point p : result) {
                    builder.append(p.toJson());
                    builder.append(",");
                }
                builder.append("]");

                try {
                    storage.save(POINTS + offset, builder.toString());
                } catch (Exception e) {
                    logger.warning("Impossible to save: probably out of memory");
                }

                cloud.addPoints(result);
                update(numPoints);
            }
        });
    }

    private void enable() {
        b1.setVisible(true);
        b2.setVisible(true);
        b3.setVisible(true);
        fps.setVisible(true);
        RootPanel.setVisible(RootPanel.get("loading").getElement(), false);
        renderer.enable();
    }

    private void createControl() {
        Panel panel = new FlowPanel();


        b1 = new RadioButton("controlGroup", "Rotate");
        b1.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                if (booleanValueChangeEvent.getValue()) renderer.setInteraction(Renderer.Interaction.ROTATE);
            }
        });
        b1.setVisible(false);

        b2 = new RadioButton("controlGroup", "Translate");
        b2.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                if (booleanValueChangeEvent.getValue()) renderer.setInteraction(Renderer.Interaction.TRANSLATE);
            }
        });
        b2.setVisible(false);

        b3 = new RadioButton("controlGroup", "Zoom");
        b3.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                if (booleanValueChangeEvent.getValue()) renderer.setInteraction(Renderer.Interaction.ZOOM);
            }
        });
        b3.setVisible(false);
        b1.setValue(true);

        panel.add(b1);
        panel.add(b2);
        panel.add(b3);

        RootPanel.get("control").add(panel);

        fps = new Label("---");
        renderer.addListener(new Renderer.RendererListener() {

            public void event() {
                fps.setVisible(true);
                fps.setText(format.format(renderer.getFps()) + " fps");
            }
        });
        RootPanel.get("fps").add(fps);

        percentage = new Label("---");
        RootPanel.get("percent").add(percentage);


    }


    private static native JsonPlyInfo asPlyInfo(String json) /*-{
        eval('var res = ' + json);
        return res;
    }-*/;

    private static native JsArray<JsonPoint> asJSPointArray(String json) /*-{
        eval('var res = ' + json);
        return res;
    }-*/;

    private static class JsonPoint extends JavaScriptObject {
        protected JsonPoint() {
        }

        public native final double getX() /*-{
            return this.x;
        }-*/;

        public native final double getY() /*-{
            return this.y;
        }-*/;

        public native final double getZ() /*-{
            return this.z;
        }-*/;

        public native final int getRed() /*-{
            return this.r;
        }-*/;


        public native final int getGreen() /*-{
            return this.g;
        }-*/;


        public native final int getBlue() /*-{
            return this.b;
        }-*/;

        public native final double getNX() /*-{
            return this.nx;
        }-*/;

        public native final double getNY() /*-{
            return this.ny;
        }-*/;

        public native final double getNZ() /*-{
            return this.nz;
        }-*/;


    }

}
