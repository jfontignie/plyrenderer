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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("PlyRendererService")
public interface PlyRendererService extends RemoteService {

    Point[] getPoints(int offset);

    PlyInfo getInfo();

    /**
     * Utility/Convenience class.
     * Use PlyRendererService.App.getInstance() to access static instance of PlyRendererServiceAsync
     */
    public static class App {
        private static PlyRendererServiceAsync ourInstance = GWT.create(PlyRendererService.class);

        public static synchronized PlyRendererServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
