package com.gnatiuk.searcher.utils;

/**
 * Created by sgnatiuk on 10/23/15.
 */
public enum SizeMeasure {

    KB(1), MB(2), GB(3);

    private int bytes;//count of bytes in one KB/MB/Gb

    SizeMeasure(int multiplier) {
        this.bytes = (int) Math.pow(2, 10 * multiplier);
    }

    public long convertToBytes(int size){
        return size * bytes;
    }

}
