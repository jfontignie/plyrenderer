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

package org.plyrenderer.client;

import java.io.Serializable;
import java.util.logging.Logger;


/**
 * Author: Jacques Fontignie
 * Date: 5/21/12
 * Time: 9:04 AM
 */
public class Point implements Serializable {

    private Vector3d point;
    private Vector3d normal;

    private int[] color;

    public Point(double x, double y, double z, double normalX, double normalY, double normalZ, int colorRed, int colorGreen, int colorBlue) {
        point = new Vector3d(x, y, z);
        normal = new Vector3d(normalX, normalY, normalZ);
        color = new int[]{colorRed, colorGreen, colorBlue};
    }

    public Point() {
        point = new Vector3d();
        normal = new Vector3d();
        color = new int[3];
    }

    public Point(String string) {
        String array[] = string.split("\\,");

        point = new Vector3d(Double.valueOf(array[0]), Double.valueOf(array[1]), Double.valueOf(array[2]));
        normal = new Vector3d(Double.valueOf(array[3]), Double.valueOf(array[4]), Double.valueOf(array[5]));
        color[0] = Integer.valueOf(array[6]);
        color[1] = Integer.valueOf(array[7]);
        color[2] = Integer.valueOf(array[8]);
        Logger.getLogger("test").info("Point created");

    }

    public Vector3d getPoint() {
        return point;
    }

    public Vector3d getNormal() {
        return normal;
    }

    public int[] getColor() {
        return color;
    }

    public String toStorageString() {
        return point.toStorageString() + "," + normal.toStorageString() + "," +
                color[0] + "," + color[1] + "," + color[2];
    }

    public String toJson() {
        return "{" +
                JsonUtils.createField("x", point.getX()) + "," +
                JsonUtils.createField("y", point.getY()) + "," +
                JsonUtils.createField("z", point.getZ()) + "," +
                JsonUtils.createField("r", color[0]) + "," +
                JsonUtils.createField("g", color[1]) + "," +
                JsonUtils.createField("b", color[2]) +
                "}";
    }


}
