package org.plyrenderer.client;

import java.io.Serializable;

import static java.lang.System.arraycopy;

/**
 * Author: Jacques Fontignie
 * Date: 5/19/12
 * Time: 9:54 PM
 */
public class Vector3d implements Serializable {

    private final double[] array = new double[3];

    public Vector3d() {

    }

    public Vector3d(double c1, double c2, double c3) {
        array[0] = c1;
        array[1] = c2;
        array[2] = c3;
    }

    public Vector3d(double value) {
        array[0] = value;
        array[1] = value;
        array[2] = value;
    }

    public void set(int pos, double value) {
        array[pos] = value;
    }

    public double get(int pos) {
        return array[pos];
    }

    public void set(Vector3d pos) {
        arraycopy(pos.array, 0, array, 0, array.length);
    }

    public void sub(Vector3d v1, Vector3d v2) {
        array[0] = v1.array[0] - v2.array[0];
        array[1] = v1.array[1] - v2.array[1];
        array[2] = v1.array[2] - v2.array[2];
    }

    public void add(Vector3d v1, Vector3d v2) {

        for (int i = 0; i < 3; i++) {
            array[i] = v1.get(i) + v2.get(i);
        }
    }

    public double normalize() {
        return Math.sqrt(array[0] * array[0] + array[1] * array[1] + array[2] * array[2]);
    }

    public void div(double value) {
        double invert = 1 / value;
        for (int i = 0; i < 3; i++)
            array[i] *= invert;
    }

    public void minus() {
        for (int i = 0; i < 3; i++)
            array[i] = -array[i];
    }


    public void mul(double value) {
        for (int i = 0; i < 3; i++)
            array[i] *= value;
    }

    public void add(Vector3d vector) {
        for (int i = 0; i < 3; i++)
            array[i] += vector.array[i];
    }

    public double normalizeSquare() {
        return array[0] * array[0] + array[1] * array[1] + array[2] * array[2];
    }

    public static Vector3d minus(Vector3d v) {
        Vector3d vector = new Vector3d();
        for (int i = 0; i < 3; i++)
            vector.array[i] = -v.array[i];

        return vector;
    }

    public static Vector3d rotate(Vector3d v, Vector3d axis, double theta) {
        Vector3d result = new Vector3d(0);
        double[] p1 = result.array;


        double[] p0 = v.array;
        double costheta, sintheta;
        Vector3d rotation = new Vector3d(3);
        double[] r = rotation.array;
// Normalise axis
        double length = axis.normalize();
        Vector3d nAxis = Vector3d.normalize(axis);

        if (length != 0) {
            rotation.set(axis);
            rotation.div(length);
        }

        costheta = Math.cos(theta);
        sintheta = Math.sin(theta);

        p1[0] += (costheta + (1 - costheta) * r[0] * r[0]) * p0[0];
        p1[0] += ((1 - costheta) * r[0] * r[1] - r[2] * sintheta) * p0[1];
        p1[0] += ((1 - costheta) * r[0] * r[2] + r[1] * sintheta) * p0[2];

        p1[1] += ((1 - costheta) * r[0] * r[1] + r[2] * sintheta) * p0[0];
        p1[1] += (costheta + (1 - costheta) * r[1] * r[1]) * p0[1];
        p1[1] += ((1 - costheta) * r[1] * r[2] - r[0] * sintheta) * p0[2];

        p1[2] += ((1 - costheta) * r[0] * r[2] - r[1] * sintheta) * p0[0];
        p1[2] += ((1 - costheta) * r[1] * r[2] + r[0] * sintheta) * p0[1];
        p1[2] += (costheta + (1 - costheta) * r[2] * r[2]) * p0[2];
        return result;
    }

    private static Vector3d normalize(Vector3d v) {
        Vector3d r = new Vector3d();
        double normalizedValue = v.normalize();
        if (normalizedValue == 0) return new Vector3d(0);
        r.array[0] = v.array[0] / normalizedValue;
        r.array[1] = v.array[1] / normalizedValue;
        r.array[2] = v.array[2] / normalizedValue;
        return r;
    }

    public void crossProduct(Vector3d v1, Vector3d v2) {
        double [] c = array;
        double [] a = v1.array;
        double [] b = v2.array;
        c[0] = a[1]*b[2] - a[2]*b[1];
        c[1] = a[2]*b[0] - a[0]*b[2];
        c[2] = a[0]*b[1] - a[1]*b[0];
    }

    public void set(double x, double y, double z) {
        array[0] = x;
        array[1] = y;
        array[2] = z;
    }

    public void sub(Vector3d v) {
        array[0] -= v.array[0];
        array[1] -= v.array[1];
        array[2] -= v.array[2];
    }

    public double dotProduct(Vector3d v){
        return array[0]*v.array[0] + array[1]*v.array[1] + array[2]*v.array[2];
    }
}
