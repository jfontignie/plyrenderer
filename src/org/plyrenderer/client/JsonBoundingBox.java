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

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Author: Jacques Fontignie
 * Date: 5/27/12
 * Time: 3:02 PM
 */
public final class JsonBoundingBox extends JavaScriptObject implements BoundingBox {


    protected JsonBoundingBox() {
    }


    public final native double getMinX() /*-{
        return this.minX;
    }-*/;

    public final native double getMaxX() /*-{
        return this.maxX;
    }-*/;

    public final native double getMinY() /*-{
        return this.minY;
    }-*/;

    public final native double getMaxY() /*-{
        return this.maxY;
    }-*/;

    public final native double getMinZ() /*-{
        return this.minZ;
    }-*/;

    public final native double getMaxZ() /*-{
        return this.maxZ;
    }-*/;


    public double getRangeX() {
        return getMaxX() - getMinX();
    }

    public double getRangeY() {
        return getMaxY() - getMinY();
    }

    public double getRangeZ() {
        return getMaxZ() - getMinZ();
    }
}
