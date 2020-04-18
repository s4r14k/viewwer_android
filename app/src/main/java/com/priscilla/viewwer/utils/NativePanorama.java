package com.priscilla.viewwer.utils;

import android.graphics.Bitmap;

public class NativePanorama {


    public native static void processPanorama(long[] imageAddressArray, long outputAddress);

    public native static int jniStitching(String source[], String result, double scale);

    public native static void crop(long imageAddress);

    public native static int[] beginStitching(String listaddr[], int numero);

    public native static void getMat(long mat);

    private native static int getBitmap(Bitmap bitmap);
}
