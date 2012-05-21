package org.plyrenderer.client;

/**
 * Author: Jacques Fontignie
 * Date: 5/19/12
 * Time: 9:51 PM
 */
public class Matrix3d {

    private final double[] array;
    private final int row;
    private final int col;

    public Matrix3d(){
        array = new double[12];
        this.row = 3;
        this.col = 4;
    }

    public double get(int row, int col){
        return array[row*this.col + col];
    }

    public void set(int row, int col, double value){
        array[row*this.col+col] = value;
    }


    public static Matrix3d multiply(Matrix3d mA, Matrix3d mB) {

        Matrix3d mC = new Matrix3d();

        double[] A = mA.array;
        double[] B = mB.array;
        double [] C = mC.array;

        C[0] = A[0] * B[0] + A[1] * B[4] + A[2] * B[8];
        C[1] = A[0] * B[1] + A[1] * B[5] + A[2] * B[9];
        C[2] = A[0] * B[2] + A[1] * B[6] + A[2] * B[10];
        C[3] = A[0] * B[3] + A[1] * B[7] + A[2] * B[11] + A[3];

        C[4] = A[4] * B[0] + A[5] * B[4] + A[6] * B[8];
        C[5] = A[4] * B[1] + A[5] * B[5] + A[6] * B[9];
        C[6] = A[4] * B[2] + A[5] * B[6] + A[6] * B[10];
        C[7] = A[4] * B[3] + A[5] * B[7] + A[6] * B[11] + A[7];

        C[8] = A[8] * B[0] + A[9] * B[4] + A[10] * B[8];
        C[9] = A[8] * B[1] + A[9] * B[5] + A[10] * B[9];
        C[10] = A[8] * B[2] + A[9] * B[6] + A[10] * B[10];
        C[11] = A[8] * B[3] + A[9] * B[7] + A[10] * B[11] + A[11];
        return mC;

    }

    public static Matrix3d MoveFill(Vector3d v) {
        Matrix3d mA = new Matrix3d();
        double [] A = mA.array;

        A[0] = 1;
        A[1] = 0;
        A[2] = 0;
        A[3] = v.get(0);
        A[4] = 0;
        A[5] = 1;
        A[6] = 0;
        A[7] = v.get(1);
        A[8] = 0;
        A[9] = 0;
        A[10] = 1;
        A[11] = v.get(2);
        return mA;
    }



}
