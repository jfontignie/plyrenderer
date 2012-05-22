package org.plyrenderer.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("PlyRendererService")
public interface PlyRendererService extends RemoteService {

    Point[] getPoints(int offset);

    PlyInfo getInfo();

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
