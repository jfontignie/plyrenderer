package org.plyrenderer.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.plyrenderer.client.BoundingBox;
import org.plyrenderer.client.PlyInfo;
import org.plyrenderer.client.PlyRendererService;
import org.plyrenderer.client.Point;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class PlyRendererServiceImpl extends RemoteServiceServlet implements PlyRendererService {

    private static final int DEFAULT_LENGTH = 1024;
    private final Logger logger = Logger.getLogger(PlyRendererServiceImpl.class.getName());

    private Point[] vertexes;

    private PlyInfo info;

    public PlyRendererServiceImpl() throws IOException {
        logger.info("Current Dir is: " + new File(".").getAbsolutePath());
        PlyReader reader = new PlyReader(new FileReader("WEB-INF/examples/sample.ply"));
        reader.parse();
        vertexes = reader.getVertexes();
        BoundingBox box = new BoundingBox();
        for (Point p : vertexes)
            box.addPoint(p);

        box.flush();
        info = new PlyInfo(box, vertexes.length, DEFAULT_LENGTH);
    }

    public Point[] getPoints(int offset) {
        Point[] result;
        int size = DEFAULT_LENGTH;
        if (offset + DEFAULT_LENGTH > vertexes.length)
            size = vertexes.length - offset;
        result = new Point[size];
        System.arraycopy(vertexes, offset, result, 0, size);
        return result;
    }

    public PlyInfo getInfo() {
        return info;
    }



}