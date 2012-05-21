package org.plyrenderer.client;

import java.io.Serializable;

/**
 * Author: Jacques Fontignie
 * Date: 5/21/12
 * Time: 9:04 AM
 */
public class Point implements Serializable {

    private final Vector3d point;
    private final Vector3d normal;

    private final int[] color;

    public Point(double x, double y, double z, double normalX, double normalY, double normalZ, int colorRed, int colorGreen, int colorBlue) {
        point = new Vector3d(x, y, z);
        normal = new Vector3d(normalX, normalY, normalZ);
        color = new int[]{colorRed, colorGreen, colorBlue};
    }

    protected Point(){
        point = new Vector3d();
        normal = new Vector3d();
        color = new int[3];
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
}
