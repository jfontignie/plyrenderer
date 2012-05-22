package org.plyrenderer.client;

import java.io.Serializable;

/**
 * Author: Jacques Fontignie
 * Date: 5/22/12
 * Time: 6:19 PM
 */
public class PlyInfo implements Serializable {
    private BoundingBox box;

    private int numPoints;
    private int chunkSize;

    public PlyInfo(BoundingBox box, int numPoints, int chunkSize) {
        this.box = box;
        this.numPoints = numPoints;
        this.chunkSize = chunkSize;
    }


    public PlyInfo() {}

    public BoundingBox getBoundingBox() {
        return box;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public int getChunkSize() {
        return chunkSize;
    }
}