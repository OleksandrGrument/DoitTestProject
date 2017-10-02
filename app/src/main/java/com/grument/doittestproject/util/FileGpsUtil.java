package com.grument.doittestproject.util;

import android.media.ExifInterface;

import com.grument.doittestproject.model.Coordinates;

import java.io.IOException;


public class FileGpsUtil {

    private String filePath;

    public FileGpsUtil(String filePath) {
        this.filePath = filePath;
    }

    public Coordinates getCoordinates() {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filePath);

            float[] latLong = new float[2];
            boolean hasLatLong = exif.getLatLong(latLong);
            if (hasLatLong) {
                return new Coordinates(latLong[0] ,latLong[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




}
