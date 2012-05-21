package org.plyrenderer.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.plyrenderer.client.PlyRendererService;
import org.plyrenderer.client.Point;

public class PlyRendererServiceImpl extends RemoteServiceServlet implements PlyRendererService {

    public int getNumPoints() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Point[] getPoints(int offset) {
        return new Point[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}