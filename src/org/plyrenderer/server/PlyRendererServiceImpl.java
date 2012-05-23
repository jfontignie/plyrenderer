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
import java.util.HashMap;
import java.util.logging.Logger;

public class PlyRendererServiceImpl extends RemoteServiceServlet implements PlyRendererService {

    private static final int DEFAULT_LENGTH = 2500;

    private HashMap<String, Container> map;

    private final Logger logger = Logger.getLogger(PlyRendererServiceImpl.class.getName());

    public PlyRendererServiceImpl() throws IOException {
        logger.info("Current Dir is: " + new File(".").getAbsolutePath());
        map = new HashMap<String, Container>();

        File f = new File("WEB-INF/examples");
        File[] files = f.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.isFile()) continue;
                String name = file.getName();
                if (!name.toLowerCase().endsWith(".ply")) continue;

                int index = name.indexOf(".");
                String id = name.substring(0, index);
                map.put(id, new Container(file));
            }
        }


    }

    public Point[] getPoints(String ply, int offset) {
        Point[] result;
        int size = DEFAULT_LENGTH;
        Point[] vertexes;
        try {
            vertexes = map.get(ply).getVertexes();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        if (offset + DEFAULT_LENGTH > vertexes.length)
            size = vertexes.length - offset;
        result = new Point[size];
        System.arraycopy(vertexes, offset, result, 0, size);
        return result;
    }

    public PlyInfo getInfo(String ply) {
        try {
            return map.get(ply).getInfo();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private class Container {
        private PlyInfo info;

        private Point[] vertexes;

        private File file;

        public Container(File file) {
            this.file = file;
            info = null;
        }

        private void parse() throws IOException {
            logger.info("Parsing: " + file);
            PlyReader reader = new PlyReader(new FileReader(file));
            reader.parse();
            vertexes = reader.getVertexes();
            BoundingBox box = new BoundingBox();
            for (Point p : vertexes)
                box.addPoint(p);

            box.flush();
            info = new PlyInfo(box, vertexes.length, DEFAULT_LENGTH);
        }

        public PlyInfo getInfo() throws IOException {
            if (info == null) parse();
            return info;
        }

        public Point[] getVertexes() throws IOException {
            if (info == null) parse();
            return vertexes;
        }
    }


}