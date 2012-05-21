package org.plyrenderer.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("plyrendererService")
public interface PlyRendererService extends RemoteService {

    int getNumPoints();

    Point[] getPoints(int offset);

    /**
     * Utility/Convenience class.
     * Use PlyRendererService.App.getInstance() to access static instance of PlyRendererServiceAsync
     */
    public static class App {
        private static PlyRendererServiceAsync ourInstance = GWT.create(PlyRendererService.class);

        public static synchronized PlyRendererServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
