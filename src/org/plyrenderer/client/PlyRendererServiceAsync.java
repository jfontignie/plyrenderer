package org.plyrenderer.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PlyRendererServiceAsync {
    void getPoints(int offset, AsyncCallback<Point[]> async);

    void getInfo(AsyncCallback<PlyInfo> async);
}
