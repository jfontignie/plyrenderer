package org.plyrenderer.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.plyrenderer.client.PlyRendererService;
import org.plyrenderer.client.Point;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class PlyRendererServiceImpl extends RemoteServiceServlet implements PlyRendererService {

    private final Logger logger = Logger.getLogger(PlyRendererServiceImpl.class.getName());

    private PlyReader reader;

    public PlyRendererServiceImpl() throws IOException {
        logger.info("Current Dir is: " + new File(".").getAbsolutePath());
        reader = new PlyReader(new FileReader("WEB-INF/examples/sample.ply"));
        reader.parse();
    }

    public int getNumPoints() {
        return reader.getNumVertexes();
    }

    public Point[] getPoints(int offset) {
        return reader.getVertexes();
    }
}