package com.g.lazyloadlistenersample.data;

import android.text.TextUtils;

import java.util.List;

public class VolumeInfo {

    private String title;

    private String subtitle;

    private List<String> authors;

    private String publishedDate;

    private int pageCount;

    private String printType;

    private ImageLinks imageLinks;

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getTitleStrings() {
        return subtitle != null ?
                String.format("%s (%s)", title, subtitle) : title;
    }

    public String getPublishDateString() {
        return String.format("Published: %s", publishedDate);
    }

    public String getAuthorsString() {
        return authors == null ? "Unknown author" : TextUtils.join(",", authors);
    }

    public String getThumbnail() {
        return imageLinks != null ? imageLinks.getThumbnail() : null;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getPrintType() {
        return printType;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Authors: " + authors + ", " +
                "PDate: " + publishedDate + ", Type: " + printType + ", Pages: " + pageCount;
    }
}
