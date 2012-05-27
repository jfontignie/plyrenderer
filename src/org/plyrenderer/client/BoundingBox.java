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
 * Date: 5/27/12
 * Time: 3:04 PM
 */
public interface BoundingBox extends Serializable {
    public static final String MIN_X = "minX";
    public static final String MIN_Y = "minY";
    public static final String MIN_Z = "minZ";
    public static final String MAX_X = "maxX";
    public static final String MAX_Y = "maxY";
    public static final String MAX_Z = "maxZ";

    double getMinX();

    double getMaxX();

    double getMinY();

    double getMaxY();

    double getMinZ();

    double getMaxZ();

    double getRangeX();

    double getRangeY();

    double getRangeZ();

}
