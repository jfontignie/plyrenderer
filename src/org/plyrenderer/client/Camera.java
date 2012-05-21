package org.plyrenderer.client;

/**
 * Author: Jacques Fontignie
 * Date: 5/19/12
 * Time: 9:49 PM
 */
public class Camera {


/*
 * Code translated from: http://mdkey.org/?p=406
 * Copyright: Mark Dwyer (markus.dwyer@gmail.com)
 */


    private Matrix3d viewRotationMatrix = new Matrix3d();
    Matrix3d worldTransform = new Matrix3d();

    private Vector3d position = new Vector3d();
    private Vector3d focalPoint = new Vector3d();
    private Vector3d directionOfProjection = new Vector3d();
    private double distance;

    private int viewportWidth;
    private int viewportHeight;
    double alpha;
    double beta;
    double aspect_ratio;
    double view_dist;

    private Vector3d WorldUp = new Vector3d(0.0, 1.0, 0.0);

    public void setWindow(int w, int h) {
        viewportWidth = w;
        viewportHeight = h;
        aspect_ratio = viewportWidth / viewportHeight;
        alpha = (0.5 * viewportWidth - 0.5);
        beta = (0.5 * viewportHeight - 0.5);
        view_dist = (0.5) * 2.0 * Math.tan(90.0 / 2 * Math.PI / 180.0);
    }

    public int getViewportWidth() {
        return viewportWidth;
    }

    public int getViewportHeight() {
        return viewportHeight;
    }

    /*
    * Build a transform as if you were at a point (x1,y1,z1), and
    * looking at a point (x2,y2,z2)
    */
    public int lookAt(Vector3d pos, Vector3d at) {
        // the View or "new Z" vector
        Vector3d ViewOut = new Vector3d();
// the Up or "new Y" vector
        Vector3d ViewUp = new Vector3d();
// the Right or "new X" vector
        Vector3d ViewRight = new Vector3d();

// for normalizing the View vector
        double ViewMagnitude;
// for normalizing the Up vector
        double UpMagnitude;
// magnitude of projection of View Vector3d on World UP
        double UpProjection;

        position.set(pos);

        focalPoint.set(at);

        directionOfProjection.sub(position, focalPoint);

        ViewOut.sub(at, pos);
        // first, calculate and normalize the view vector
        ViewMagnitude = ViewOut.normalize();
        distance = ViewMagnitude;

// invalid points (not far enough apart)
        if (ViewMagnitude < .000001)
            return -1;

// normalize. This is the unit vector in the "new Z" direction
        ViewOut.div(ViewMagnitude);


// Now the hard part: The ViewUp or "new Y" vector

// dot product of ViewOut vector and World Up vector gives projection of
// of ViewOut on WorldUp
        UpProjection = ViewOut.dotProduct(WorldUp);

// first try at making a View Up vector: use World Up

        //Equivalent to: WorldUp - UpProjection*ViewOut
        ViewUp.set(ViewOut);
        ViewUp.minus();
        ViewUp.mul(UpProjection);
        ViewUp.add(WorldUp);

// Check for validity:
        UpMagnitude = ViewUp.normalizeSquare();

        if (UpMagnitude < .0000001) {
            //Second try at making a View Up vector: Use Y axis default  (0,1,0)

            ViewUp.setX(-ViewOut.getY() * ViewOut.getX());
            ViewUp.setY(1 - ViewOut.getY() * ViewOut.getY());
            ViewUp.setZ(-ViewOut.getY() * ViewOut.getZ());

// Check for validity:
            UpMagnitude = ViewUp.normalizeSquare();

            if (UpMagnitude < .0000001) {
                //Final try at making a View Up vector: Use Z axis default  (0,0,1)
                ViewUp.setX(-ViewOut.getZ() * ViewOut.getX());
                ViewUp.setY(-ViewOut.getZ() * ViewOut.getY());
                ViewUp.setZ(1 - ViewOut.getZ() * ViewOut.getZ());

// Check for validity:
                UpMagnitude = ViewUp.normalizeSquare();

                if (UpMagnitude < .0000001)
                    return (-1);
            }
        }

        // normalize the Up Vector3d
        UpMagnitude = Math.sqrt(UpMagnitude);
        ViewUp.div(UpMagnitude);

        UpVector(ViewUp);

// Calculate the Right Vector3d. Use cross product of Out and Up.
        ViewRight.crossProduct(ViewUp, ViewOut);
//        ViewRight.setX( -ViewOut.getY() * ViewUp.getZ() + ViewOut.getZ() * ViewUp.getY());
//        ViewRight.setY( -ViewOut.getZ() * ViewUp.getX() + ViewOut.getX() * ViewUp.getZ());
//        ViewRight.setZ( -ViewOut.getX() * ViewUp.getY() + ViewOut.getY() * ViewUp.getX());

//        ViewRight[0] = -ViewOut[1] * ViewUp[2] + ViewOut[2] * ViewUp[1];
//        ViewRight[1] = -ViewOut[2] * ViewUp[0] + ViewOut[0] * ViewUp[2];
//        ViewRight[2] = -ViewOut[0] * ViewUp[1] + ViewOut[1] * ViewUp[0];

// Plug values into rotation matrix R
        viewRotationMatrix.set(0, 0, ViewRight.getX());
        viewRotationMatrix.set(0, 1, ViewRight.getY());
        viewRotationMatrix.set(0, 2, ViewRight.getZ());
        viewRotationMatrix.set(0, 3, 0);

        viewRotationMatrix.set(1, 0, ViewUp.getX());
        viewRotationMatrix.set(1, 1, ViewUp.getY());
        viewRotationMatrix.set(1, 2, ViewUp.getZ());
        viewRotationMatrix.set(1, 3, 0);

        viewRotationMatrix.set(2, 0, ViewOut.getX());
        viewRotationMatrix.set(2, 1, ViewOut.getY());
        viewRotationMatrix.set(2, 2, ViewOut.getZ());
        viewRotationMatrix.set(2, 3, 0);

//        viewRotationMatrix[0] = ViewRight[0];
//        viewRotationMatrix[1] = ViewRight[1];
//        viewRotationMatrix[2] = ViewRight[2];
//        viewRotationMatrix[3] = 0;
//
//        viewRotationMatrix[4] = ViewUp[0];
//        viewRotationMatrix[5] = ViewUp[1];
//        viewRotationMatrix[6] = ViewUp[2];
//        viewRotationMatrix[7] = 0;
//
//        viewRotationMatrix[8] = ViewOut[0];
//        viewRotationMatrix[9] = ViewOut[1];
//        viewRotationMatrix[10] = ViewOut[2];
//        viewRotationMatrix[11] = 0;


// Plug values into translation matrix T


        Matrix3d viewMoveMatrix = Matrix3d.MoveFill(Vector3d.minus(pos));

// build the World Transform
        worldTransform = Matrix3d.multiply(viewRotationMatrix, viewMoveMatrix);
        //worldTransform = MatrixMultiply(viewRotationMatrix, ViewMoveMatrix);

        return 0;
    }


    /*
    *
    */
//    public Vector3d RotatePoint(p0, axis, theta) {
//        var p1 = new Array(12);
//        p1[0] = 0.0;
//        p1[1] = 0.0;
//        p1[2] = 0.0;
//        var costheta, sintheta;
//        var r = new Array(3);
//// Normalise axis
//        var length = Math.sqrt(axis[0] * axis[0] + axis[1] * axis[1] + axis[2] * axis[2]);
//        if (length != 0) {
//            r[0] = axis[0] / length;
//            r[1] = axis[1] / length;
//            r[2] = axis[2] / length;
//        }
//
//        costheta = Math.cos(theta);
//        sintheta = Math.sin(theta);
//
//        p1[0] += (costheta + (1 - costheta) * r[0] * r[0]) * p0[0];
//        p1[0] += ((1 - costheta) * r[0] * r[1] - r[2] * sintheta) * p0[1];
//        p1[0] += ((1 - costheta) * r[0] * r[2] + r[1] * sintheta) * p0[2];
//
//        p1[1] += ((1 - costheta) * r[0] * r[1] + r[2] * sintheta) * p0[0];
//        p1[1] += (costheta + (1 - costheta) * r[1] * r[1]) * p0[1];
//        p1[1] += ((1 - costheta) * r[1] * r[2] - r[0] * sintheta) * p0[2];
//
//        p1[2] += ((1 - costheta) * r[0] * r[2] - r[1] * sintheta) * p0[0];
//        p1[2] += ((1 - costheta) * r[1] * r[2] + r[0] * sintheta) * p0[1];
//        p1[2] += (costheta + (1 - costheta) * r[2] * r[2]) * p0[2];
//        return p1;
//    }


    /*
    *  Change the World Up vector (the default is (0,1,0))
    *
    */
    public void UpVector(Vector3d v) {
        WorldUp.set(v);
    }


    /*
    *
    */
    public void rotateUp(double theta) {
        // Axis to rotate around - cross camera up and dir
        Vector3d axis = new Vector3d();


// Cross product
        axis.crossProduct(directionOfProjection, WorldUp);

//        axis[0] = directionOfProjection[1] * WorldUp[2] - directionOfProjection[2] * WorldUp[1];
//        axis[1] = directionOfProjection[2] * WorldUp[0] - directionOfProjection[0] * WorldUp[2];
//        axis[2] = directionOfProjection[0] * WorldUp[1] - directionOfProjection[1] * WorldUp[0];

// Translate point relative to origin
        position.sub(focalPoint);

//        position[0] -= focalPoint[0];
//        position[1] -= focalPoint[1];
//        position[2] -= focalPoint[2];

        Vector3d newPosition = Vector3d.rotate(position, axis, theta);

        //newPosition = RotatePoint(position, axis, theta);
//ippsCopy_32f(position, newPosition, 3);
// Translate point back to world space
        position.add(newPosition, focalPoint);
        lookAt(position, focalPoint);
    }


    /*
    *
    */
    public void rotateRight(double theta) {


// Axis to rotate around - up vector
        position.sub(focalPoint);

//        position[0] -= focalPoint[0];
//        position[1] -= focalPoint[1];
//        position[2] -= focalPoint[2];

        Vector3d newPosition = Vector3d.rotate(position, WorldUp, theta);

        //newPosition = RotatePoint(position, WorldUp, theta);
//ippsCopy_32f(position, newPosition, 3);

        position.add(newPosition, focalPoint);
//        position[0] = newPosition[0] + focalPoint[0];
//        position[1] = newPosition[1] + focalPoint[1];
//        position[2] = newPosition[2] + focalPoint[2];
        lookAt(position, focalPoint);
    }


    /*
    *
    */
    public void moveForward(double dist) {

        double diff = 1 - dist;
        position.set(directionOfProjection);
        position.mul(diff);
        position.add(focalPoint);

//        position[0] = focalPoint[0] + (1.0 - dist) * directionOfProjection[0];
//        position[1] = focalPoint[1] + (1.0 - dist) * directionOfProjection[1];
//        position[2] = focalPoint[2] + (1.0 - dist) * directionOfProjection[2];
        lookAt(position, focalPoint);
    }


    /*
    *
    */
    public void moveRight(double dist) {
        // the Right or "new X" vector
        Vector3d ViewRight = new Vector3d();

// Calculate the Right Vector3d. Use cross product of Out and Up.
        ViewRight.crossProduct(WorldUp, directionOfProjection);

//        ViewRight[0] = -directionOfProjection[1] * WorldUp[2] + directionOfProjection[2] * WorldUp[1];
//        ViewRight[1] = -directionOfProjection[2] * WorldUp[0] + directionOfProjection[0] * WorldUp[2];
//        ViewRight[2] = -directionOfProjection[0] * WorldUp[1] + directionOfProjection[1] * WorldUp[0];

        position.setX(position.getX() + ViewRight.getX() * dist);
        position.setY(position.getY() + ViewRight.getY() * dist);
        position.setZ(position.getZ() + ViewRight.getZ() * dist);

//        position[0] += ViewRight[0] * dist;
//        position[1] += ViewRight[1] * dist;
//        position[2] += ViewRight[2] * dist;

        focalPoint.setX(focalPoint.getX() + ViewRight.getX() * dist);
        focalPoint.setY(focalPoint.getY() + ViewRight.getY() * dist);
        focalPoint.setZ(focalPoint.getZ() + ViewRight.getZ() * dist);

//        focalPoint[0] += ViewRight[0] * dist;
//        focalPoint[1] += ViewRight[1] * dist;
//        focalPoint[2] += ViewRight[2] * dist;

        lookAt(position, focalPoint);
    }

    /*
    *
    */
    public void moveUp(double dist) {
        double value = dist * distance;

        position.setX(position.getX() + WorldUp.getX() * value);
        position.setY(position.getY() + WorldUp.getY() * value);
        position.setZ(position.getZ() + WorldUp.getZ() * value);

//        position[0] += WorldUp[0] * dist * distance;
//        position[1] += WorldUp[1] * dist * distance;
//        position[2] += WorldUp[2] * dist * distance;

        focalPoint.setX(focalPoint.getX() + WorldUp.getX() * value);
        focalPoint.setY(focalPoint.getY() + WorldUp.getY() * value);
        focalPoint.setZ(focalPoint.getZ() + WorldUp.getZ() * value);

//        focalPoint[0] += WorldUp[0] * dist * distance;
//        focalPoint[1] += WorldUp[1] * dist * distance;
//        focalPoint[2] += WorldUp[2] * dist * distance;


        lookAt(position, focalPoint);
    }

}