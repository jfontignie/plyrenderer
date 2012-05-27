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

import com.google.gwt.storage.client.Storage;

import java.util.logging.Logger;

/**
 * Author: Jacques Fontignie
 * Date: 5/27/12
 * Time: 8:00 AM
 */
public class StorageSystem {

    public static final int MIN_COMPRESSED_SIZE = 255;

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Storage storage;
    private String id;

    public StorageSystem(String id) {
        this.id = id;
        storage = Storage.getLocalStorageIfSupported();
        logger.info("Storage supported: " + (storage != null));
    }

    public void save(String key, double value) {
        save(key, String.valueOf(value));
    }

    public void save(String key, int value) {
        save(key, String.valueOf(value));
    }

    public void save(String key, String value) {
        logger.info("Saving: " + key);
        if (storage == null) return;
        if (value.length() > MIN_COMPRESSED_SIZE) {
            storage.setItem(getKey(key), "gzp" + lzw_encode(value));
        } else {
            storage.setItem(getKey(key), value);
        }
    }

    private String getKey(String key) {
        return id + "_" + key;
    }


    public String load(String key) {
        logger.info("Loading: " + key);
        if (storage == null) return null;
        String result = storage.getItem(getKey(key));
        if (result == null) return result;
        if (result.startsWith("gzp"))
            return lzw_decode(result.substring(3));

        return result;
    }

    public void clear() {
        logger.info("Clearing cache");
        storage.clear();
    }


    // LZW-compress a string
    //Source code from: http://stackoverflow.com/questions/294297/javascript-implementation-of-gzip
    private native String lzw_encode(String s) /*-{
        var dict = {};
        var data = (s + "").split("");
        var out = [];
        var currChar;
        var phrase = data[0];
        var code = 256;
        for (var i = 1; i < data.length; i++) {
            currChar = data[i];
            if (dict[phrase + currChar] != null) {
                phrase += currChar;
            }
            else {
                out.push(phrase.length > 1 ? dict[phrase] : phrase.charCodeAt(0));
                dict[phrase + currChar] = code;
                code++;
                phrase = currChar;
            }
        }
        out.push(phrase.length > 1 ? dict[phrase] : phrase.charCodeAt(0));
        for (var i = 0; i < out.length; i++) {
            out[i] = String.fromCharCode(out[i]);
        }
        return out.join("");
    }-*/;

    // Decompress an LZW-encoded string
    //Source code from: http://stackoverflow.com/questions/294297/javascript-implementation-of-gzip
    private native String lzw_decode(String s) /*-{
        var dict = {};
        var data = (s + "").split("");
        var currChar = data[0];
        var oldPhrase = currChar;
        var out = [currChar];
        var code = 256;
        var phrase;
        for (var i = 1; i < data.length; i++) {
            var currCode = data[i].charCodeAt(0);
            if (currCode < 256) {
                phrase = data[i];
            }
            else {
                phrase = dict[currCode] ? dict[currCode] : (oldPhrase + currChar);
            }
            out.push(phrase);
            currChar = phrase.charAt(0);
            dict[code] = oldPhrase + currChar;
            code++;
            oldPhrase = phrase;
        }
        return out.join("");
    }-*/;
}
