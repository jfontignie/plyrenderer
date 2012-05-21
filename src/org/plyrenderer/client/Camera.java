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


    private Matrix3d ViewRotationMatrix = new Matrix3d();
    Matrix3d WorldTransform = new Matrix3d();

    private Vector3d Position = new Vector3d();
    private Vector3d FocalPoint = new Vector3d();
    private Vector3d DirectionOfProjection = new Vector3d();
    private double distance;

    int viewport_width;
    int viewport_height;
    double alpha;
    double beta;
    double aspect_ratio;
    double view_dist;

    private Vector3d WorldUp = new Vector3d(0.0, 1.0, 0.0);

    public void setWindow(int w, int h) {
        viewport_width = w;
        viewport_height = h;
        aspect_ratio = viewport_width / viewport_height;
        alpha = (0.5 * viewport_width - 0.5);
        beta = (0.5 * viewport_height - 0.5);
        view_dist = (0.5) * 2.0 * Math.tan(90.0 / 2 * Math.PI / 180.0);
    }

    /*
    * Build a transform as if you were at a point (x1,y1,z1), and
    * looking at a point (x2,y2,z2)
    */
    public int LookAt(Vector3d pos, Vector3d at) {
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

        Position.set(pos);

        FocalPoint.set(at);

        DirectionOfProjection.sub(Position, FocalPoint);

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
        UpProjection =  ViewOut.dotProduct(WorldUp);

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

            ViewUp.set(0, -ViewOut.get(1) * ViewOut.get(0));
            ViewUp.set(1, 1 - ViewOut.get(1) * ViewOut.get(1));
            ViewUp.set(2, -ViewOut.get(1) * ViewOut.get(2));

// Check for validity:
            UpMagnitude = ViewUp.normalizeSquare();

            if (UpMagnitude < .0000001) {
                //Final try at making a View Up vector: Use Z axis default  (0,0,1)
                ViewUp.set(0, -ViewOut.get(2) * ViewOut.get(0));
                ViewUp.set(1, -ViewOut.get(2) * ViewOut.get(1));
                ViewUp.set(2, 1 - ViewOut.get(2) * ViewOut.get(2));

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
        ViewRight.crossProduct(ViewUp,ViewOut);
//        ViewRight.set(0, -ViewOut.get(1) * ViewUp.get(2) + ViewOut.get(2) * ViewUp.get(1));
//        ViewRight.set(1, -ViewOut.get(2) * ViewUp.get(0) + ViewOut.get(0) * ViewUp.get(2));
//        ViewRight.set(2, -ViewOut.get(0) * ViewUp.get(1) + ViewOut.get(1) * ViewUp.get(0));

//        ViewRight[0] = -ViewOut[1] * ViewUp[2] + ViewOut[2] * ViewUp[1];
//        ViewRight[1] = -ViewOut[2] * ViewUp[0] + ViewOut[0] * ViewUp[2];
//        ViewRight[2] = -ViewOut[0] * ViewUp[1] + ViewOut[1] * ViewUp[0];

// Plug values into rotation matrix R
        ViewRotationMatrix.set(0, 0, ViewRight.get(0));
        ViewRotationMatrix.set(0, 1, ViewRight.get(1));
        ViewRotationMatrix.set(0, 2, ViewRight.get(2));
        ViewRotationMatrix.set(0, 3, 0);

        ViewRotationMatrix.set(1, 0, ViewUp.get(0));
        ViewRotationMatrix.set(1, 1, ViewUp.get(1));
        ViewRotationMatrix.set(1, 2, ViewUp.get(2));
        ViewRotationMatrix.set(1, 3, 0);

        ViewRotationMatrix.set(2, 0, ViewOut.get(0));
        ViewRotationMatrix.set(2, 1, ViewOut.get(1));
        ViewRotationMatrix.set(2, 2, ViewOut.get(2));
        ViewRotationMatrix.set(2, 3, 0);

//        ViewRotationMatrix[0] = ViewRight[0];
//        ViewRotationMatrix[1] = ViewRight[1];
//        ViewRotationMatrix[2] = ViewRight[2];
//        ViewRotationMatrix[3] = 0;
//
//        ViewRotationMatrix[4] = ViewUp[0];
//        ViewRotationMatrix[5] = ViewUp[1];
//        ViewRotationMatrix[6] = ViewUp[2];
//        ViewRotationMatrix[7] = 0;
//
//        ViewRotationMatrix[8] = ViewOut[0];
//        ViewRotationMatrix[9] = ViewOut[1];
//        ViewRotationMatrix[10] = ViewOut[2];
//        ViewRotationMatrix[11] = 0;


// Plug values into translation matrix T


        Matrix3d viewMoveMatrix = Matrix3d.MoveFill(Vector3d.minus(pos));

// build the World Transform
        WorldTransform = Matrix3d.multiply(ViewRotationMatrix, viewMoveMatrix);
        //WorldTransform = MatrixMultiply(ViewRotationMatrix, ViewMoveMatrix);

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
    public void RotateUp(double theta) {
        // Axis to rotate around - cross camera up and dir
        Vector3d axis = new Vector3d();


// Cross product
        axis.crossProduct(DirectionOfProjection,WorldUp);

//        axis[0] = DirectionOfProjection[1] * WorldUp[2] - DirectionOfProjection[2] * WorldUp[1];
//        axis[1] = DirectionOfProjection[2] * WorldUp[0] - DirectionOfProjection[0] * WorldUp[2];
//        axis[2] = DirectionOfProjection[0] * WorldUp[1] - DirectionOfProjection[1] * WorldUp[0];

// Translate point relative to origin
        Position.sub(FocalPoint);

//        Position[0] -= FocalPoint[0];
//        Position[1] -= FocalPoint[1];
//        Position[2] -= FocalPoint[2];

        Vector3d newPosition = Vector3d.rotate(Position, axis, theta);

        //newPosition = RotatePoint(Position, axis, theta);
//ippsCopy_32f(Position, newPosition, 3);
// Translate point back to world space
        Position.add(newPosition, FocalPoint);
        LookAt(Position, FocalPoint);
    }


    /*
    *
    */
    public void RotateRight(double theta) {


// Axis to rotate around - up vector
        Position.sub(FocalPoint);

//        Position[0] -= FocalPoint[0];
//        Position[1] -= FocalPoint[1];
//        Position[2] -= FocalPoint[2];

        Vector3d newPosition = Vector3d.rotate(Position, WorldUp, theta);

        //newPosition = RotatePoint(Position, WorldUp, theta);
//ippsCopy_32f(Position, newPosition, 3);

        Position.add(newPosition, FocalPoint);
//        Position[0] = newPosition[0] + FocalPoint[0];
//        Position[1] = newPosition[1] + FocalPoint[1];
//        Position[2] = newPosition[2] + FocalPoint[2];
        LookAt(Position, FocalPoint);
    }


    /*
    *
    */
    public void MoveForward(double dist) {

        double diff = 1 - dist;
        Position.set(DirectionOfProjection);
        Position.mul(diff);
        Position.add(FocalPoint);

//        Position[0] = FocalPoint[0] + (1.0 - dist) * DirectionOfProjection[0];
//        Position[1] = FocalPoint[1] + (1.0 - dist) * DirectionOfProjection[1];
//        Position[2] = FocalPoint[2] + (1.0 - dist) * DirectionOfProjection[2];
        LookAt(Position, FocalPoint);
    }


    /*
    *
    */
    public void MoveRight(double dist) {
        // the Right or "new X" vector
        Vector3d ViewRight = new Vector3d();

// Calculate the Right Vector3d. Use cross product of Out and Up.
        ViewRight.crossProduct(WorldUp,DirectionOfProjection);

//        ViewRight[0] = -DirectionOfProjection[1] * WorldUp[2] + DirectionOfProjection[2] * WorldUp[1];
//        ViewRight[1] = -DirectionOfProjection[2] * WorldUp[0] + DirectionOfProjection[0] * WorldUp[2];
//        ViewRight[2] = -DirectionOfProjection[0] * WorldUp[1] + DirectionOfProjection[1] * WorldUp[0];

        Position.set(0, Position.get(0) + ViewRight.get(0) * dist);
        Position.set(1, Position.get(1) + ViewRight.get(1) * dist);
        Position.set(2, Position.get(2) + ViewRight.get(2) * dist);

//        Position[0] += ViewRight[0] * dist;
//        Position[1] += ViewRight[1] * dist;
//        Position[2] += ViewRight[2] * dist;

        FocalPoint.set(0, FocalPoint.get(0) + ViewRight.get(0) * dist);
        FocalPoint.set(1, FocalPoint.get(1) + ViewRight.get(1) * dist);
        FocalPoint.set(2, FocalPoint.get(2) + ViewRight.get(2) * dist);

//        FocalPoint[0] += ViewRight[0] * dist;
//        FocalPoint[1] += ViewRight[1] * dist;
//        FocalPoint[2] += ViewRight[2] * dist;

        LookAt(Position, FocalPoint);
    }

    /*
    *
    */
    public void MoveUp(double dist) {
        double value = dist * distance;

        Position.set(0, Position.get(0) + WorldUp.get(0) * value);
        Position.set(1, Position.get(1) + WorldUp.get(1) * value);
        Position.set(2, Position.get(2) + WorldUp.get(2) * value);

//        Position[0] += WorldUp[0] * dist * distance;
//        Position[1] += WorldUp[1] * dist * distance;
//        Position[2] += WorldUp[2] * dist * distance;

        FocalPoint.set(0, FocalPoint.get(0) + WorldUp.get(0) * value);
        FocalPoint.set(1, FocalPoint.get(1) + WorldUp.get(1) * value);
        FocalPoint.set(2, FocalPoint.get(2) + WorldUp.get(2) * value);

//        FocalPoint[0] += WorldUp[0] * dist * distance;
//        FocalPoint[1] += WorldUp[1] * dist * distance;
//        FocalPoint[2] += WorldUp[2] * dist * distance;


        LookAt(Position, FocalPoint);
    }

}