package org.plyrenderer.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class PlyRenderer implements EntryPoint {

    private static final int canvasHeight = 500;
    private static final int canvasWidth = 500;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        Canvas canvas = Canvas.createIfSupported();

        if (canvas == null) {
            RootPanel.get("canvas").add(new Label("Sorry, your browser doesn't support the HTML5 Canvas element"));
            return;
        }

        //canvas.setStyleName("mainCanvas");
        canvas.setWidth(canvasWidth + "px");
        canvas.setCoordinateSpaceWidth(canvasWidth);

        canvas.setHeight(canvasHeight + "px");
        canvas.setCoordinateSpaceHeight(canvasHeight);

        Renderer renderer = new Renderer(canvas);
        renderer.initialiseData();
        renderer.initialiseScene();
        renderer.render();


        // Assume that the host HTML has elements defined whose
        // IDs are "slot1", "slot2".  In a real app, you probably would not want
        // to hard-code IDs.  Instead, you could, for example, search for all
        // elements with a particular CSS class and replace them with widgets.
        //
        RootPanel.get("canvas").add(canvas);

    }
}
