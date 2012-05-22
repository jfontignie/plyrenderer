package org.plyrenderer.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.logging.Logger;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class PlyRenderer implements EntryPoint {

    private static final int canvasHeight = 500;
    private static final int canvasWidth = 500;
    private Renderer renderer;
    private PointCloud cloud;

    private static final int startShowing = 25;

    private PlyRendererServiceAsync service;

    private final Logger logger = Logger.getLogger("PlyRenderer");
    private boolean added = false;
    private Label percentage;

    private static NumberFormat format = NumberFormat.getFormat(".00");

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        service = PlyRendererService.App.getInstance();

        final Canvas canvas = Canvas.createIfSupported();

        if (canvas == null) {
            RootPanel.get("canvas").add(new Label("Sorry, your browser doesn't support the HTML5 Canvas element"));
            return;
        }

        //canvas.setStyleName("mainCanvas");
        canvas.setWidth(canvasWidth + "px");
        canvas.setCoordinateSpaceWidth(canvasWidth);

        canvas.setHeight(canvasHeight + "px");
        canvas.setCoordinateSpaceHeight(canvasHeight);


        renderer = new Renderer(canvas);

        service.getInfo(new AsyncCallback<PlyInfo>() {
            public void onFailure(Throwable caught) {
                logger.warning("Impossible to get the PLY information: " + caught);

            }

            public void onSuccess(PlyInfo result) {
                renderer.setBoundingBox(result.getBoundingBox());
                renderer.initialiseScene();

                int chunksize = result.getChunkSize();
                final int numPoints = result.getNumPoints();
                cloud = new PointCloud(numPoints);

                renderer.setPointCloud(cloud);
                for (int offset = 0; offset < numPoints; offset += chunksize) {
                    service.getPoints(offset, new AsyncCallback<Point[]>() {
                        public void onFailure(Throwable caught) {
                            logger.warning("Impossible to get the points");
                        }

                        public void onSuccess(Point[] result) {
                            cloud.addPoints(result);
                            double percent = cloud.getNumberOfPoints() * 1. / numPoints * 100;

                            if (!added && percent > startShowing) {
                                createControl();
                                RootPanel.get("canvas").add(canvas);
                                added = true;
                            }

                            if (added) {
                                percentage.setText(format.format(percent) + "%");
                                renderer.render();
                            }
                            if (cloud.getNumberOfPoints() == numPoints) {
                                percentage.setVisible(false);
                                RootPanel.setVisible(RootPanel.get("loading").getElement(), false);
                                renderer.enable();
                            }

                        }
                    });
                }


            }
        });

    }

    private void createControl() {
        Panel panel = new FlowPanel();

        RadioButton b1 = new RadioButton("controlGroup", "Rotation");
        b1.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                if (booleanValueChangeEvent.getValue()) renderer.setInteraction(Renderer.Interaction.ROTATE);
            }
        });
        RadioButton b2 = new RadioButton("controlGroup", "Translation");
        b2.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                if (booleanValueChangeEvent.getValue()) renderer.setInteraction(Renderer.Interaction.TRANSLATE);
            }
        });

        RadioButton b3 = new RadioButton("controlGroup", "Zoom");
        b3.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                if (booleanValueChangeEvent.getValue()) renderer.setInteraction(Renderer.Interaction.ZOOM);
            }
        });

        b1.setValue(true);

        panel.add(b1);
        panel.add(b2);
        panel.add(b3);

        final Label label = new Label("xxx fps");
        renderer.addListener(new Renderer.RendererListener() {

            public void event() {
                label.setText(format.format(renderer.getFps()) + " fps");
            }
        });

        percentage = new Label("0 %");


        panel.add(label);
        panel.add(percentage);

        RootPanel.get("control").add(panel);
    }
}
