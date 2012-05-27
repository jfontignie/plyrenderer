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

/**
 * Author: Jacques Fontignie
 * Date: 5/27/12
 * Time: 3:23 PM
 */
public class JsonUtils {

    public static String createField(String key, String value) {
        return "\"" + key + "\":" + value;
    }

    public static String createField(String key, double value) {
        return createField(key,String.valueOf(value));
    }

    public static String createField(String key, int value) {
        return createField(key,String.valueOf(value));
    }


}
