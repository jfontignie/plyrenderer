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

    private final Logger logger = Logger.getLogger("PlyRenderer");

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

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
        renderer.initialiseData(new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                logger.warning("Impossible to initialise data: " + caught);
            }

            public void onSuccess(Void result) {
                renderer.initialiseScene();
                createControl();

                renderer.render();


                // Assume that the host HTML has elements defined whose
                // IDs are "slot1", "slot2".  In a real app, you probably would not want
                // to hard-code IDs.  Instead, you could, for example, search for all
                // elements with a particular CSS class and replace them with widgets.
                //
                RootPanel.get("canvas").add(canvas);
            }
        });


    }

    private void createControl() {
        Panel panel = new FlowPanel();

        RadioButton b1 = new RadioButton("controlGroup","Rotation");
        b1.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                if (booleanValueChangeEvent.getValue()) renderer.setInteraction(Renderer.Interaction.ROTATE);
            }
        });
        RadioButton b2 = new RadioButton("controlGroup","Translation");
        b2.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                if (booleanValueChangeEvent.getValue()) renderer.setInteraction(Renderer.Interaction.TRANSLATE);
            }
        });

        RadioButton b3 = new RadioButton("controlGroup","Zoom");
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

              label.setText(NumberFormat.getFormat(".00").format(renderer.getFps()) + " fps");
            }
        });

        panel.add(label);

        RootPanel.get("control").add(panel);
    }
}
