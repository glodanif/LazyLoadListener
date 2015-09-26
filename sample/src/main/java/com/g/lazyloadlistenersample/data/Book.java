package com.g.lazyloadlistenersample.data;

public class Book {

    private String id;

    private String etag;

    private VolumeInfo volumeInfo;

    public String getId() {
        return id;
    }

    public String getEtag() {
        return etag;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    @Override
    public String toString() {
        return "Id: " + id + ", " + volumeInfo.toString();
    }
}
