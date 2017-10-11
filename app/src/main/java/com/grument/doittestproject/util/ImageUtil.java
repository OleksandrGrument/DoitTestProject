package com.grument.doittestproject.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageUtil {

    private static int DEFAULT_IMAGE_HEIGHT = 600;
    private static int DEFAULT_IMAGE_WIDTH = 600;

    private Context context;
    private File sourceFile;
    private Uri uri;

    public ImageUtil(Context context, File sourceFile, Uri uri) {
        this.context = context;
        this.sourceFile = sourceFile;
        this.uri = uri;
    }

    public File scaleImageAndReturnFile() {


        try {
            File file = File.createTempFile(sourceFile.getName(), null, context.getCacheDir());

            Bitmap selectedImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(selectedImage, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT, false);

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();
            scaledBitmap.recycle();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sourceFile;

    }


}
