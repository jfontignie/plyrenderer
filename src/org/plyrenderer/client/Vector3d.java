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

/**
 * Author: Jacques Fontignie
 * Date: 5/19/12
 * Time: 9:54 PM
 */
public class Vector3d implements Serializable {

    private double x;
    private double y;
    private double z;

    public Vector3d() {

    }

    public Vector3d(double c1, double c2, double c3) {
        x = c1;
        y = c2;
        z = c3;
    }

    public Vector3d(double value) {
        x = value;
        y = value;
        z = value;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void set(Vector3d v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public void sub(Vector3d v1, Vector3d v2) {
        x = v1.x - v2.x;
        y = v1.y - v2.y;
        z = v1.z - v2.z;
    }

    public void add(Vector3d v1, Vector3d v2) {
        x = v1.x + v2.x;
        y = v1.y + v2.y;
        z = v1.z + v2.z;
    }

    public double normalize() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void div(double value) {
        double invert = 1 / value;
        mul(invert);
    }

    public void minus() {
        x = -x;
        y = -y;
        z = -z;
    }


    public void mul(double value) {
        x *= value;
        y *= value;
        z *= value;
    }

    public void add(Vector3d v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    public double normalizeSquare() {
        return x * x + y * y + z * z;
    }

    public static Vector3d minus(Vector3d v) {
        Vector3d vector = new Vector3d();
        vector.x = -v.x;
        vector.y = -v.y;
        vector.z = -v.z;
        return vector;
    }

    public static Vector3d rotate(Vector3d p0, Vector3d axis, double theta) {
        Vector3d p1 = new Vector3d(0);


        double costheta, sintheta;
        Vector3d r = normalize(axis);

        costheta = Math.cos(theta);
        sintheta = Math.sin(theta);

        p1.x += (costheta + (1 - costheta) * r.x * r.x) * p0.x;
        p1.x += ((1 - costheta) * r.x * r.y - r.z * sintheta) * p0.y;
        p1.x += ((1 - costheta) * r.x * r.z + r.y * sintheta) * p0.z;

        p1.y += ((1 - costheta) * r.x * r.y + r.z * sintheta) * p0.x;
        p1.y += (costheta + (1 - costheta) * r.y * r.y) * p0.y;
        p1.y += ((1 - costheta) * r.y * r.z - r.x * sintheta) * p0.z;

        p1.z += ((1 - costheta) * r.x * r.z - r.y * sintheta) * p0.x;
        p1.z += ((1 - costheta) * r.y * r.z + r.x * sintheta) * p0.y;
        p1.z += (costheta + (1 - costheta) * r.z * r.z) * p0.z;
        return p1;
    }

    private static Vector3d normalize(Vector3d v) {
        Vector3d r = new Vector3d();
        double normalizedValue = v.normalize();
        if (normalizedValue == 0) return new Vector3d(0);
        r.x = v.x / normalizedValue;
        r.y = v.y / normalizedValue;
        r.z = v.z / normalizedValue;
        return r;
    }

    public void crossProduct(Vector3d a, Vector3d b) {
        x = a.y * b.z - a.z * b.y;
        y = a.z * b.x - a.x * b.z;
        z = a.x * b.y - a.y * b.x;
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void sub(Vector3d v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }

    public double dotProduct(Vector3d v) {
        return x * v.x + y * v.y + z * v.z;
    }
}
