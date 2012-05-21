package org.plyrenderer.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PlyRendererServiceAsync {
    void getNumPoints(AsyncCallback<Integer> async);

    void getPoints(int offset, AsyncCallback<Point[]> async);
}
