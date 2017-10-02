package com.grument.doittestproject.dto;


import com.google.gson.annotations.SerializedName;

public class ImageDTO {

    public ImageDTO() {
    }

    @SerializedName("id")
    private int id;

    @SerializedName("parameters")
    private ImageParamsDTO imageParamsDTO;

    @SerializedName("smallImagePath")
    private String smallImageUrlPath;

    @SerializedName("bigImagePath")
    private String bigImageUrlPath;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageParamsDTO getImageParamsDTO() {
        return imageParamsDTO;
    }

    public void setImageParamsDTO(ImageParamsDTO imageParamsDTO) {
        this.imageParamsDTO = imageParamsDTO;
    }

    public String getSmallImageUrlPath() {
        return smallImageUrlPath;
    }

    public void setSmallImageUrlPath(String smallImageUrlPath) {
        this.smallImageUrlPath = smallImageUrlPath;
    }

    public String getBigImageUrlPath() {
        return bigImageUrlPath;
    }

    public void setBigImageUrlPath(String bigImageUrlPath) {
        this.bigImageUrlPath = bigImageUrlPath;
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
                "id=" + id +
                ", imageParamsDTO=" + imageParamsDTO +
                ", smallImageUrlPath='" + smallImageUrlPath + '\'' +
                ", bigImageUrlPath='" + bigImageUrlPath + '\'' +
                '}';
    }
}


