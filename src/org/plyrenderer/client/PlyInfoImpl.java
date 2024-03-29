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
 * Date: 5/22/12
 * Time: 6:19 PM
 */
public class PlyInfoImpl implements Serializable, PlyInfo {


    private BoundingBoxImpl box;

    private int numPoints;
    private int chunkSize;
    public PlyInfoImpl(BoundingBoxImpl box, int numPoints, int chunkSize) {
        this.box = box;
        this.numPoints = numPoints;
        this.chunkSize = chunkSize;
    }


    //noinspection unused
    public PlyInfoImpl() {
    }

    public BoundingBox getBoundingBox() {
        return box;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public String toJson() {
        StringBuilder builder = new StringBuilder("{");

        builder.append(JsonUtils.createField(NUM_POINTS,numPoints));
        builder.append(",");
        builder.append(JsonUtils.createField(CHUNK_SIZE,chunkSize));
        builder.append(",");
        builder.append(JsonUtils.createField(BOUNDING_BOX, box.toJson()));
        builder.append("}");
        return builder.toString();
    }


}