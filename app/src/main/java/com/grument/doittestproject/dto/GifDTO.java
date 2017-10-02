package com.grument.doittestproject.dto;

import com.google.gson.annotations.SerializedName;



public class GifDTO {

    public GifDTO(){}

    @SerializedName("gif")
    private String gifUrlPath;

    public String getGifUrlPath() {
        return gifUrlPath;
    }

    public void setGifUrlPath(String gifUrlPath) {
        this.gifUrlPath = gifUrlPath;
    }

    @Override
    public String toString() {
        return "GifDTO{" +
                "gifUrlPath='" + gifUrlPath + '\'' +
                '}';
    }
}
