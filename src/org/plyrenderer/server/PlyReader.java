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

import org.plyrenderer.client.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;


/**
 * Author: Jacques Fontignie
 * Date: 5/21/12
 * Time: 2:40 PM
 */
public class PlyReader {

    private final Logger logger = Logger.getLogger(PlyReader.class.getName());
    private BufferedReader reader;

    private Point[] vertexes;

    public PlyReader(InputStreamReader streamReader) throws IOException {
        reader = new BufferedReader(streamReader);
    }


    public void parse() throws IOException {
        if (!reader.readLine().trim().equals("ply"))
            throw new IllegalStateException("PLY should start with 'ply' keyword");

        String format = reader.readLine().trim();
        if (!format.equals("format ascii 1.0"))
            throw new IllegalStateException("Format is not supported yet");

        int offset = 0;

        int x = -1, y = -1, z = -1, nx = -1, ny = -1, nz = -1, cr = -1, cg = -1, cb = -1;

        boolean done = false;
        while (true) {
            String line = reader.readLine().trim();


            if (line.startsWith("element")) {
                String array[] = line.split(" ");
                if (array[1].equals("vertex")) {
                    vertexes = new Point[Integer.valueOf(array[2])];
                    int cur = 0;
                    while (true) {
                        line = reader.readLine().trim();
                        if (line.startsWith("property")) {
                            String a[] = line.split(" ");
                            if (a[2].equals("x")) x = cur;
                            if (a[2].equals("y")) y = cur;
                            if (a[2].equals("z")) z = cur;

                            if (a[2].equals("nx")) nx = cur;
                            if (a[2].equals("ny")) ny = cur;
                            if (a[2].equals("nz")) nz = cur;

                            if (a[2].equals("diffuse_red")) cr = cur;
                            if (a[2].equals("diffuse_green")) cg = cur;
                            if (a[2].equals("diffuse_blue")) cb = cur;

                            cur++;
                        }

                        if (line.startsWith("element") || line.equals("end_header")) break;
                    }

                    done = true;
                } else {
                    if (!done) offset += Integer.valueOf(array[2]);
                }
            }

            if (line.equals("end_header")) break;
        }

        for (int i = 0; i < offset; i++) reader.readLine();

        logger.info("Number of points: " + vertexes.length);
        for (int i = 0; i < vertexes.length; i++) {

            String line = reader.readLine();
            String array[] = line.split(" ");
            Point p = new Point(Double.valueOf(array[x]),
                    Double.valueOf(array[y]),
                    Double.valueOf(array[z]),
                    0,
                    0,
                    0,
                    Integer.valueOf(array[cr]),
                    Integer.valueOf(array[cg]),
                    Integer.valueOf(array[cb])
            );
            vertexes[i] = p;
        }

    }


    public int getNumVertexes() {
        return vertexes.length;
    }

    public Point[] getVertexes() {
        return vertexes;
    }


}
