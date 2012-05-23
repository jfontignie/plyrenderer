/*
 * Copyright 2012 Jacques Fontignie
 *
 * This file is part of plyrenderer.
 *
 * plyrenderer is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * plyrenderer is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with plyrenderer. If not, see http://www.gnu.org/licenses/.
 */

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

    private static final int DEFAULT_LENGTH = 2500;
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